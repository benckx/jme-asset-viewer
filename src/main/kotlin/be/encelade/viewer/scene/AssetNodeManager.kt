package be.encelade.viewer.scene

import be.encelade.chimp.material.LightingMaterial
import be.encelade.viewer.ViewerJmeApp.Companion.SELECTED_ASSET
import be.encelade.viewer.utils.GeometryUtils.extractBoundingVolume
import be.encelade.viewer.utils.LazyLogging
import com.github.guepardoapps.kulid.ULID
import com.jme3.app.SimpleApplication
import com.jme3.asset.plugins.FileLocator
import com.jme3.bounding.BoundingBox
import com.jme3.math.Vector3f
import com.jme3.renderer.queue.RenderQueue
import com.jme3.scene.Geometry
import com.jme3.scene.Node
import com.jme3.scene.Spatial
import com.jme3.scene.shape.Box
import java.awt.Color.GREEN
import java.io.File

/**
 * Manage the list of imported assets in the scene (store as [AssetNode])
 */
class AssetNodeManager(private val app: SimpleApplication) : LazyLogging {

    private val assetNodes = mutableListOf<AssetNode>()

    private val assetManager by lazy { app.assetManager }
    private val rootNode by lazy { app.rootNode }

    fun importAsset(file: File): SceneNode {
        val offset = Vector3f(0f, 0f, 1f)

        val spatial = loadAssetSpatial(file)
        val node = Node(spatial.name)
        node.attachChild(spatial)
        node.move(offset)

        val assetNode = AssetNode(spatial.name, file)
        assetNodes += assetNode
        rootNode.attachChild(node)
        drawBoundingBox(node)

        return SceneNode(assetNode, node)
    }

    /**
     * Add shadows and id
     */
    private fun loadAssetSpatial(file: File): Spatial {
        val name = ULID.random()
        val splitPath = file.path.split(File.separator)
        val containingFolder = splitPath.dropLast(1).joinToString(File.separator)
        assetManager.registerLocator(containingFolder, FileLocator::class.java)
        val spatial = assetManager.loadModel(splitPath.last())
        spatial.name = name

        when (spatial) {
            is Node ->
                spatial
                        .children
                        .filterIsInstance<Geometry>()
                        .forEach { geometry ->
                            geometry.shadowMode = RenderQueue.ShadowMode.CastAndReceive
                            geometry.name = name
                        }
            is Geometry ->
                spatial.shadowMode = RenderQueue.ShadowMode.CastAndReceive
        }

        return spatial
    }

    fun findById(id: String): AssetNode? {
        return assetNodes.find { it.id == id }
    }

    fun delete(id: String) {
        findById(id)?.let { assetNode ->
            rootNode.detachChildNamed(assetNode.id)
            assetNodes.remove(assetNode)
        }
    }

    fun clone(id: String): SceneNode? {
        val sourceAssetNode = findById(id)
        val sourceNode = rootNode.getChild(id)

        if (sourceAssetNode != null && sourceNode != null) {
            val file = File(sourceAssetNode.file.name)
            val spatial = loadAssetSpatial(file)

            val assetNode = AssetNode(spatial.name, file)
            val node = Node(spatial.name)
            node.attachChild(spatial)
            node.localTranslation = sourceNode.localTranslation
            node.localRotation = sourceNode.localRotation
            node.localScale = sourceNode.localScale

            assetNodes += assetNode
            rootNode.attachChild(node)

            return SceneNode(assetNode, node)
        } else {
            return null
        }
    }

    fun reDrawBoundingBox(node: Node) {
        deleteBoundingBox()
        drawBoundingBox(node)
    }

    fun deleteBoundingBox() {
        rootNode.detachChildNamed(SELECTED_ASSET)
    }

    fun drawBoundingBox(sceneNode: SceneNode): Geometry? {
        return drawBoundingBox(sceneNode.node)
    }

    private fun drawBoundingBox(node: Node): Geometry? {
        deleteBoundingBox()

        val volume = extractBoundingVolume(node)
        if (volume is BoundingBox) {
            val material = LightingMaterial()
            material.setColor(GREEN)
            material.additionalRenderState.isWireframe = true

            val box = Geometry(SELECTED_ASSET, Box(volume.xExtent, volume.yExtent, volume.zExtent))
            box.material = material

            box.localTranslation = node.localTranslation
            box.localRotation = node.localRotation
            box.localScale = node.localScale
            box.move(volume.center)
            rootNode.attachChild(box)

            return box
        }

        return null
    }

}
