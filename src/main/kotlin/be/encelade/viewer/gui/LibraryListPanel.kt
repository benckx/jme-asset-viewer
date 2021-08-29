package be.encelade.viewer.gui

import be.encelade.viewer.commands.CommandQueue
import be.encelade.viewer.commands.ImportAssetCommand
import java.io.File

internal class LibraryListPanel(commandQueue: CommandQueue, private val assetMenu: AssetMenu) :
        AbstractListPanel<File>("Asset Library", commandQueue) {

    override fun renderItemName(value: File): String {
        return value.name
    }

    override fun indexOf(value: File): Int {
        return listItems().indexOf(value)
    }

    override fun indexOf(id: String): Int {
        return listItems().indexOfFirst { file -> file.absolutePath == id }
    }

    fun alreadyInList(file: File): Boolean {
        return indexOf(file) > -1
    }

    override fun onDoubleClick(value: File) {
        commandQueue.queue(ImportAssetCommand(value) { sceneNode ->
            assetMenu.addToAssetList(sceneNode)
            assetMenu.show(sceneNode, showInList = false)
            disableFocus()
        })
    }

}
