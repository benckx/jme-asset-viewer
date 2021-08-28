package be.encelade.viewer.gui

import be.encelade.viewer.commands.CommandQueue
import java.awt.Font
import java.io.File

internal class LibraryListPanel(guiFont: Font, commandQueue: CommandQueue) :
        AbstractListPanel<File>(guiFont, "Library", commandQueue) {

    override fun renderItemName(value: File): String {
        return value.name
    }

    override fun onSelect(value: File) {
        println("NOTHING: selected $value")
    }

    override fun indexOf(value: File): Int {
        return listModel.elements()!!.toList().indexOf(value)
    }

    override fun indexOf(id: String): Int {
        return listModel.elements()!!.toList().indexOfFirst { file -> file.absolutePath == id }
    }

}
