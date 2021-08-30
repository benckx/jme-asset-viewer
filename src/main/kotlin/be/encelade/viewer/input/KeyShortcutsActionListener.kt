package be.encelade.viewer.input

import be.encelade.viewer.commands.CommandQueue
import be.encelade.viewer.commands.DeleteAssetNodeCommand
import be.encelade.viewer.gui.AssetMenu
import be.encelade.viewer.scene.BoundingBoxManager
import com.jme3.input.controls.ActionListener

class KeyShortcutsActionListener(private val boundingBoxManager: BoundingBoxManager,
                                 private val commandQueue: CommandQueue,
                                 private val assetMenu: AssetMenu) :
        ActionListener {

    override fun onAction(name: String?, isPressed: Boolean, tpf: Float) {
        if (isPressed) {
            when (name) {
                DELETE_ASSET -> {
                    boundingBoxManager.currentSceneNode()?.let { sceneNode ->
                        commandQueue.queue(DeleteAssetNodeCommand(sceneNode.assetNode, assetMenu))
                    }
                }
            }
        }
    }

    companion object {

        const val DELETE_ASSET = "DELETE_ASSET"

    }

}
