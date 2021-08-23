package be.encelade.viewer.scene

import be.encelade.viewer.utils.LazyLogging
import com.github.guepardoapps.kulid.ULID
import com.jme3.app.SimpleApplication
import com.jme3.asset.plugins.FileLocator
import com.jme3.renderer.queue.RenderQueue
import com.jme3.scene.Geometry
import com.jme3.scene.Node
import com.jme3.scene.Spatial
import java.io.File

class AssetNodeManager(private val app: SimpleApplication) : LazyLogging {

    private val assetManager by lazy { app.assetManager }
    private val rootNode by lazy { app.rootNode }

    private val assetNodes = mutableListOf<AssetNode>()

    fun importAsset(file: File): SceneNode {
        val spatial = importSpatial(file)

        when (spatial) {
            is Node ->
                spatial
                        .children
                        .filterIsInstance<Geometry>()
                        .forEach { geometry ->
                            geometry.shadowMode = RenderQueue.ShadowMode.CastAndReceive
                            geometry.name = spatial.name
                        }
            is Geometry ->
                spatial.shadowMode = RenderQueue.ShadowMode.CastAndReceive
        }

        val node = Node(spatial.name)
        node.attachChild(spatial)
        node.move(0f, 0f, 1f)

        val assetNode = AssetNode(spatial.name, file)
        val sceneNode = SceneNode(assetNode, node)
        assetNodes += assetNode
        rootNode.attachChild(node)

        return sceneNode
    }

    private fun importSpatial(file: File): Spatial {
        val id = ULID.random()
        val splitPath = file.path.split(File.separator)
        val containingFolder = splitPath.dropLast(1).joinToString(File.separator)
        assetManager.registerLocator(containingFolder, FileLocator::class.java)
        val spatial = assetManager.loadModel(splitPath.last())

        when (spatial) {
            is Node ->
                spatial
                        .children
                        .filterIsInstance<Geometry>()
                        .forEach { geometry ->
                            geometry.shadowMode = RenderQueue.ShadowMode.CastAndReceive
                            geometry.name = id
                        }
            is Geometry ->
                spatial.shadowMode = RenderQueue.ShadowMode.CastAndReceive
        }

        return spatial
    }

    fun findById(id: String): AssetNode? {
        return assetNodes.find { it.id == id }
    }

    fun deleteById(id: String) {
        assetNodes
                .find { it.id == id }
                ?.let { assetNode ->
                    rootNode.detachChildNamed(assetNode.id)
                    assetNodes.remove(assetNode)
                }
    }

    fun clone(id: String): SceneNode? {
        assetNodes
                .find { it.id == id }
                ?.let { sourceAssetNode ->
                    val spatial = importSpatial(sourceAssetNode.file)
                    val sourceNode = rootNode.getChild(sourceAssetNode.id)

                    if (sourceNode != null) {
                        val assetNode = AssetNode(spatial.name, sourceAssetNode.file)
                        val node = Node(spatial.name)
                        node.attachChild(spatial)
                        node.localTranslation = sourceNode.localTranslation
                        node.localRotation = sourceNode.localRotation
                        node.localScale = sourceNode.localScale

                        assetNodes += assetNode
                        rootNode.attachChild(node)

                        return SceneNode(assetNode, node)
                    }
                }

        return null
    }

}
