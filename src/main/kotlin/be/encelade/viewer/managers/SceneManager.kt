package be.encelade.viewer.managers

import be.encelade.viewer.scene.AssetNode
import be.encelade.viewer.utils.LazyLogging
import com.github.guepardoapps.kulid.ULID
import com.jme3.app.SimpleApplication
import com.jme3.asset.plugins.FileLocator
import com.jme3.renderer.queue.RenderQueue
import com.jme3.scene.Geometry
import com.jme3.scene.Node
import com.jme3.scene.Spatial
import java.io.File

class SceneManager(private val app: SimpleApplication) : LazyLogging {

    private val assetManager by lazy { app.assetManager }
    private val rootNode by lazy { app.rootNode }

    private val assetNodes = mutableListOf<AssetNode>()

    fun importAsset(file: File) {
        val splitPath = file.path.split(File.separator)
        val containingFolder = splitPath.dropLast(1).joinToString(separator = File.separator)
        assetManager.registerLocator(containingFolder, FileLocator::class.java)
        val asset = assetManager.loadModel(splitPath.last())

        when (asset) {
            is Node ->
                asset
                        .children
                        .filterIsInstance<Geometry>()
                        .forEach { geometry ->
                            geometry.shadowMode = RenderQueue.ShadowMode.CastAndReceive
                        }
            is Geometry ->
                asset.shadowMode = RenderQueue.ShadowMode.CastAndReceive
        }

        addToScene(asset)
    }

    private fun addToScene(spatial: Spatial) {
        val ulid = ULID.random()
        assetNodes += AssetNode(ulid, spatial)

        val node = Node(ulid)
        node.attachChild(spatial)
        rootNode.attachChild(node)
    }

}
