package be.encelade.viewer.gui

import be.encelade.viewer.commands.CommandQueue
import be.encelade.viewer.commands.DeleteAssetNodeCommand
import be.encelade.viewer.commands.SelectAssetCommand
import be.encelade.viewer.scene.SceneNode
import java.awt.event.KeyEvent
import java.awt.event.KeyEvent.VK_DELETE

internal class AssetListPanel(commandQueue: CommandQueue, private val parent: AssetMenu) :
        AbstractListPanel<SceneNode>("Scene Assets", commandQueue) {

    override fun renderItemName(value: SceneNode): String {
        return value.assetNode.fileName
    }

    override fun onSelect(value: SceneNode) {
        commandQueue.queue(SelectAssetCommand(value) {
            parent.show(value, showInList = false)
        })
    }

    override fun onKeyPressed(value: SceneNode, e: KeyEvent) {
        if (e.keyCode == VK_DELETE) {
            commandQueue.queue(DeleteAssetNodeCommand(value.assetNode, parent))
        }
    }

    override fun indexOf(value: SceneNode): Int {
        return listItems().indexOf(value)
    }

    override fun indexOf(id: String): Int {
        return listItems().indexOfFirst { sceneNode -> sceneNode.id() == id }
    }

}
