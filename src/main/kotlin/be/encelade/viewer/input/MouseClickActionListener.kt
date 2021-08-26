package be.encelade.viewer.input

import be.encelade.viewer.gui.AssetMenu
import be.encelade.viewer.scene.AssetNodeManager
import be.encelade.viewer.scene.BoundingBoxManager
import be.encelade.viewer.scene.SceneNode
import be.encelade.viewer.utils.LazyLogging
import com.jme3.input.controls.ActionListener
import com.jme3.scene.Node

class MouseClickActionListener(private val rootNode: Node,
                               private val mouseInputManager: MouseInputManager,
                               private val assetNodeManager: AssetNodeManager,
                               private val boundingBoxManager: BoundingBoxManager,
                               private val assetMenu: AssetMenu) : ActionListener, LazyLogging {

    override fun onAction(name: String?, isPressed: Boolean, tpf: Float) {
        if (!isPressed) {
            when (name) {
                MOUSE_CLICK -> {
                    val sceneNode = findSceneNode()
                    if (sceneNode != null) {
                        assetMenu.show(sceneNode)
                        boundingBoxManager.drawBoundingBox(sceneNode)
                    } else {
                        assetMenu.disableFocus()
                        boundingBoxManager.deleteBoundingBox()
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
