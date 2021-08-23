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

    private val assetNodes = mutableListOf<AssetNode>()

    private val assetManager by lazy { app.assetManager }
    private val rootNode by lazy { app.rootNode }

    fun importAsset(file: File): SceneNode {
        val spatial = importSpatial(file)
        val node = Node(spatial.name)
        node.attachChild(spatial)
        node.move(0f, 0f, 1f)

        val assetNode = AssetNode(spatial.name, file)
        assetNodes += assetNode
        rootNode.attachChild(node)

        return SceneNode(assetNode, node)
    }

    private fun importSpatial(file: File): Spatial {
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

    fun deleteById(id: String) {
        assetNodes
                .find { it.id == id }
                ?.let { assetNode ->
                    rootNode.detachChildNamed(assetNode.id)
                    assetNodes.remove(assetNode)
                }
    }

    fun cloneById(id: String): SceneNode? {
        val sourceAssetNode = assetNodes.find { it.id == id }
        val sourceNode = rootNode.getChild(id)

        if (sourceAssetNode != null && sourceNode != null) {
            val file = File(sourceAssetNode.file.name)
            val spatial = importSpatial(file)

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

}
