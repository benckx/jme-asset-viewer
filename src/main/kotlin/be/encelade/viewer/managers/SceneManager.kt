package be.encelade.viewer.managers

import be.encelade.viewer.scene.AssetNode
import com.github.guepardoapps.kulid.ULID
import com.jme3.app.SimpleApplication
import com.jme3.asset.plugins.FileLocator
import com.jme3.renderer.queue.RenderQueue
import com.jme3.scene.Geometry
import com.jme3.scene.Node
import com.jme3.scene.Spatial
import java.io.File

class SceneManager(private val app: SimpleApplication) {

    private val assetManager by lazy { app.assetManager }
    private val rootNode by lazy { app.rootNode }

    private val assetNodes = mutableListOf<AssetNode>()

    fun importAsset(file: File) {
        assetManager.registerLocator(file.path, FileLocator::class.java)
        val asset = assetManager.loadAsset(file.path)
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

        val spatial = asset as Spatial
        val ulid = ULID.random()
        assetNodes += AssetNode(ulid, spatial)

        val node = Node(ulid)
        node.attachChild(spatial)
        rootNode.attachChild(node)
    }

}
