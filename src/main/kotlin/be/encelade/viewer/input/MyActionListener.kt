package be.encelade.viewer.input

import be.encelade.viewer.menus.AssetMenu
import be.encelade.viewer.scene.AssetNodeManager
import be.encelade.viewer.scene.SceneNode
import be.encelade.viewer.utils.LazyLogging
import com.jme3.app.SimpleApplication
import com.jme3.input.controls.ActionListener
import com.jme3.scene.Node

class MyActionListener(app: SimpleApplication,
                       private val mouseInputManager: MouseInputManager,
                       private val assetNodeManager: AssetNodeManager,
                       private val assetMenu: AssetMenu) : ActionListener, LazyLogging {

    private val rootNode by lazy { app.rootNode }

    override fun onAction(name: String?, isPressed: Boolean, tpf: Float) {
        if (!isPressed) {
            when (name) {
                MOUSE_CLICK -> {
                    val sceneNode = findSceneNode()
                    if (sceneNode != null) {
                        assetMenu.loadInGui(sceneNode)
                    } else {
                        assetMenu.unFocusAll()
                    }
                }
            }
        }
    }

    private fun findSceneNode(): SceneNode? {
        val collisionIds = mouseInputManager.collisionIds()
        if (collisionIds.isNotEmpty()) {
            val id = collisionIds.first()
            val assetNode = assetNodeManager.findById(id)
            val spatial = rootNode.getChild(id)

            if (assetNode != null && spatial != null) {
                return SceneNode(assetNode, spatial as Node)
            }
        }

        return null
    }

    companion object {

        const val MOUSE_CLICK = "MOUSE_CLICK"

    }

}
