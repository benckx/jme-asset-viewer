package be.encelade.viewer.input

import be.encelade.viewer.managers.MouseInputManager
import be.encelade.viewer.managers.SceneManager
import be.encelade.viewer.menus.AssetMenu
import be.encelade.viewer.utils.LazyLogging
import com.jme3.input.controls.ActionListener

class MyActionListener(private val mouseInputManager: MouseInputManager,
                       private val sceneManager: SceneManager,
                       private val assetMenu: AssetMenu) : ActionListener, LazyLogging {

    override fun onAction(name: String?, isPressed: Boolean, tpf: Float) {
        when (name) {
            MOUSE_CLICK -> {
                val collisionIds = mouseInputManager.collisionIds()
                if (collisionIds.isNotEmpty()) {
                    logger.info("to show: ${collisionIds.first()}")
                    sceneManager.findById(collisionIds.first())?.let { assetNode ->
                        logger.info("node: $assetNode")
                    }
                }
            }
        }
    }

    companion object {

        const val MOUSE_CLICK = "MOUSE_CLICK"

    }

}
