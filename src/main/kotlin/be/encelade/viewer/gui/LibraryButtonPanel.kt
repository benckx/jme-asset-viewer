package be.encelade.viewer.gui

import be.encelade.viewer.gui.GuiUtils.buildFileChooser
import be.encelade.viewer.gui.GuiUtils.copy
import be.encelade.viewer.gui.GuiUtils.createDefaultPanelBorder
import be.encelade.viewer.gui.GuiUtils.guiFont
import be.encelade.viewer.persistence.SavedSceneWriter
import be.encelade.viewer.utils.LazyLogging
import java.awt.GridLayout
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors
import javax.swing.JButton
import javax.swing.JFileChooser
import javax.swing.JPanel

internal class LibraryButtonPanel(context: GuiContext,
                                  savedSceneWriter: SavedSceneWriter,
                                  addFileToList: (File) -> Unit) : JPanel(), LazyLogging {

    private val scanFolder = JButton("Scan Folder")

    init {
        layout = GridLayout(0, 1)
        border = createDefaultPanelBorder().copy(top = 3, bottom = 3)

        add(scanFolder)
        components.forEach { component -> component.font = guiFont }

        scanFolder.addActionListener {
            val fileChooser = buildFileChooser(context.lastFolder)
            fileChooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
            val returnValue = fileChooser.showOpenDialog(scanFolder)
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                val folder = fileChooser.selectedFile
                context.lastFolder = folder.absolutePath

                val files: List<File> = Files.walk(Paths.get(folder.path))
                        .map { path -> path.toFile() }
                        .filter { extensions.contains(it.extension) }
                        .collect(Collectors.toList())
                        .toList()
                        .filterNotNull()

                files.forEach { file -> addFileToList(file) }

                savedSceneWriter.requestWriteToFile()
            }
        }
    }

    private companion object {

        val extensions = listOf("obj", "glb", "j3o", "xml", "blend", "fbx")

    }

}
