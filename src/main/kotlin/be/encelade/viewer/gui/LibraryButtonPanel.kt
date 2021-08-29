package be.encelade.viewer.gui

import be.encelade.viewer.gui.GuiUtils.buildFileChooser
import be.encelade.viewer.gui.GuiUtils.copy
import be.encelade.viewer.gui.GuiUtils.createDefaultPanelBorder
import be.encelade.viewer.gui.GuiUtils.guiFont
import be.encelade.viewer.persistence.SavedSceneWriter
import be.encelade.viewer.utils.LazyLogging
import java.awt.GridLayout
import java.nio.file.Files
import java.nio.file.Paths
import javax.swing.JButton
import javax.swing.JFileChooser
import javax.swing.JPanel

internal class LibraryButtonPanel(context: GuiContext,
                                  libraryListPanel: LibraryListPanel,
                                  savedSceneWriter: SavedSceneWriter) : JPanel(), LazyLogging {

    private val scanFolderButton = JButton("Scan Folder")
    private val deleteAllButton = JButton("Delete All")

    init {
        layout = GridLayout(0, 1)
        border = createDefaultPanelBorder().copy(top = 3, bottom = 3)

        add(scanFolderButton)
        add(deleteAllButton)
        components.forEach { component -> component.font = guiFont }

        scanFolderButton.addActionListener {
            val fileChooser = buildFileChooser(context.lastFolder)
            fileChooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
            val returnValue = fileChooser.showOpenDialog(scanFolderButton)
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                val folder = fileChooser.selectedFile
                context.lastFolder = folder.absolutePath

                Files.walk(Paths.get(folder.path))
                        .map { path -> path.toFile() }
                        .filter { file -> extensions.contains(file.extension) }
                        .forEach { file -> libraryListPanel.add(file) }

                savedSceneWriter.requestWriteToFile()
                libraryListPanel.disableFocus()
            }
        }

        deleteAllButton.addActionListener {
            libraryListPanel.clear()
            savedSceneWriter.requestWriteToFile()
        }
    }

    private companion object {

        val extensions = listOf("obj", "glb", "j3o", "xml", "blend", "fbx")

    }

}
