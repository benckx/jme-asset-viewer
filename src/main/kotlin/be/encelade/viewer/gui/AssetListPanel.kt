package be.encelade.viewer.gui

import be.encelade.viewer.commands.CommandQueue
import be.encelade.viewer.commands.SelectAssetCommand
import be.encelade.viewer.scene.SceneNode

internal class AssetListPanel(commandQueue: CommandQueue, private val parent: AssetMenu) :
        AbstractListPanel<SceneNode>("Scene Asset", commandQueue) {

    override fun renderItemName(value: SceneNode): String {
        return value.assetNode.fileName
    }

    override fun onSelect(value: SceneNode) {
        commandQueue.queue(SelectAssetCommand(value) {
            parent.show(value, showInList = false)
        })
    }

    override fun indexOf(value: SceneNode): Int {
        return listModel.elements()!!.toList().indexOf(value)
    }

    override fun indexOf(id: String): Int {
        return listModel.elements()!!.toList().indexOfFirst { sceneNode -> sceneNode.id() == id }
    }

}
