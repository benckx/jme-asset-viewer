package be.encelade.viewer.gui

import java.io.File
import java.util.function.Supplier
import javax.swing.DefaultListModel

class LibraryFileSupplier : Supplier<List<File>> {

    var listModel: DefaultListModel<File> = DefaultListModel<File>()

    override fun get(): List<File> {
        return listModel.elements().toList()
    }

}
