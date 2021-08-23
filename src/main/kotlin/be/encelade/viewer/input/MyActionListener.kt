package be.encelade.viewer.input

import be.encelade.viewer.managers.MouseInputManager
import be.encelade.viewer.managers.SceneManager
import be.encelade.viewer.menus.AssetMenu
import be.encelade.viewer.scene.AssetNode
import be.encelade.viewer.utils.LazyLogging
import com.jme3.input.controls.ActionListener

class MyActionListener(private val mouseInputManager: MouseInputManager,
                       private val sceneManager: SceneManager,
                       private val assetMenu: AssetMenu) : ActionListener, LazyLogging {

    override fun onAction(name: String?, isPressed: Boolean, tpf: Float) {
        if (!isPressed) {
            when (name) {
                MOUSE_CLICK -> {
                    val assetNode = findClickedAssetNode()
                    if (assetNode != null) {
                        assetMenu.loadInGui(assetNode)
                    } else {
                        assetMenu.unFocusAll()
                    }
                }
            }
        }
    }

    private fun findClickedAssetNode(): AssetNode? {
        val collisionIds = mouseInputManager.collisionIds()
        return if (collisionIds.isNotEmpty()) {
            sceneManager.findById(collisionIds.first())
        } else {
            null
        }
    }

    companion object {

        const val MOUSE_CLICK = "MOUSE_CLICK"

    }

}
