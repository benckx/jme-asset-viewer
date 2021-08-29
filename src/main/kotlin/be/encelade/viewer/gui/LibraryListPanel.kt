package be.encelade.viewer.gui

import be.encelade.viewer.commands.CommandQueue
import java.io.File

internal class LibraryListPanel(commandQueue: CommandQueue) :
        AbstractListPanel<File>("Asset Library", commandQueue) {

    override fun renderItemName(value: File): String {
        return value.name
    }

    override fun indexOf(value: File): Int {
        return listModel.elements()!!.toList().indexOf(value)
    }

    override fun indexOf(id: String): Int {
        return listModel.elements()!!.toList().indexOfFirst { file -> file.absolutePath == id }
    }

    fun alreadyInList(file: File): Boolean {
        return indexOf(file) > -1
    }

}
