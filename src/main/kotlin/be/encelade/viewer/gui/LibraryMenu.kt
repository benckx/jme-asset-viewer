package be.encelade.viewer.gui

import be.encelade.viewer.commands.CommandQueue
import be.encelade.viewer.persistence.SavedSceneWriter
import be.encelade.viewer.utils.LazyLogging
import be.encelade.viewer.utils.PropertiesFile
import be.encelade.viewer.utils.PropertiesKey
import java.awt.BorderLayout
import java.awt.Point
import java.io.File
import javax.swing.DefaultListModel
import javax.swing.JFrame

class LibraryMenu(properties: PropertiesFile,
                  commandQueue: CommandQueue,
                  savedSceneWriter: SavedSceneWriter,
                  assetMenu: AssetMenu,
                  jmeLocation: Point) : JFrame(), LazyLogging {

    private val context = GuiContext(properties)
    private val libraryListPanel = LibraryListPanel(commandQueue, assetMenu)
    private val libraryButtonPanel = LibraryButtonPanel(context, libraryListPanel, savedSceneWriter)

    init {
        val jmeWidth = properties.getProperty(PropertiesKey.WIDTH)!!.toInt()

        val height = 800
        val width = 290
        val margin = 15
        val x = jmeLocation.x + jmeWidth + margin
        val y = jmeLocation.y - 60
        setBounds(x, y, width, height)

        layout = BorderLayout()
        add(libraryButtonPanel, BorderLayout.NORTH)
        add(libraryListPanel, BorderLayout.CENTER)

        defaultCloseOperation = EXIT_ON_CLOSE
    }

    fun addFileToLibrary(file: File) {
        if (!libraryListPanel.alreadyInList(file)) {
            libraryListPanel.add(file)
        } else {
            logger.warn("$file already in library")
        }
    }

    fun getListModel(): DefaultListModel<File> {
        return libraryListPanel.getListModel()
    }

    fun disableFocus() {
        libraryListPanel.disableFocus()
    }

}
