package be.encelade.viewer.gui

import java.io.File
import java.util.function.Supplier
import javax.swing.DefaultListModel

/**
 * Communication channel between [LibraryListPanel] and [be.encelade.viewer.persistence.SavedSceneWriter],
 * with no direct reference to AWT objects.
 */
class LibraryFileSupplier : Supplier<List<File>> {

    var listModel: DefaultListModel<File> = DefaultListModel<File>()

    override fun get(): List<File> {
        return listModel.elements().toList()
    }

}
