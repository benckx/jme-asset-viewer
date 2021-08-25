package be.encelade.viewer.menus

import be.encelade.viewer.commands.CloneCommand
import be.encelade.viewer.commands.CommandQueue
import be.encelade.viewer.commands.DeleteAssetNodeCommand
import be.encelade.viewer.commands.ImportAssetCommand
import be.encelade.viewer.utils.PropertiesFile
import java.awt.Font
import java.awt.GridLayout
import java.io.File
import javax.swing.JButton
import javax.swing.JFileChooser
import javax.swing.JPanel

internal class AssetButtonPanel(font: Font,
                                commandQueue: CommandQueue,
                                propertiesFile: PropertiesFile,
                                parent: AssetMenu) : JPanel() {

    private var lastFolder: String? = null

    private val importButton = JButton("Import")
    private val cloneButton = JButton("Clone")
    private val deleteButton = JButton("Delete")

    init {
        layout = GridLayout(0, 1)

        add(importButton)
        add(deleteButton)
        add(cloneButton)
        components.forEach { component -> component.font = font }

        disableFocus()

        importButton.addActionListener {
            val fileChooser = JFileChooser()
            lastFolder?.let { folder -> fileChooser.currentDirectory = File(folder) }
            val returnValue = fileChooser.showOpenDialog(importButton)
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                val file = fileChooser.selectedFile
                val containingFolder = file.path.split(File.separator).dropLast(1).joinToString(File.separator)
                lastFolder = containingFolder
                propertiesFile.persistProperty(PropertiesFile.DEFAULT_FOLDER_KEY, containingFolder)
                commandQueue.push(ImportAssetCommand(file) { sceneNode -> parent.showInForm(sceneNode) })
            }
        }

        cloneButton.addActionListener {
            parent.selectedAssetNode()?.let { assetNode ->
                commandQueue.push(CloneCommand(assetNode.id) { sceneNode -> parent.showInForm(sceneNode) })
            }
        }

        deleteButton.addActionListener {
            parent.selectedAssetNode()?.let { assetNode ->
                commandQueue.push(DeleteAssetNodeCommand(assetNode.id) { parent.disableFocus() })
            }
        }

        propertiesFile.getProperty(PropertiesFile.DEFAULT_FOLDER_KEY)?.let { containingFolder ->
            lastFolder = containingFolder
        }
    }

    internal fun enableFocus() {
        deleteButton.isEnabled = true
        cloneButton.isEnabled = true
    }

    internal fun disableFocus() {
        deleteButton.isEnabled = false
        cloneButton.isEnabled = false
    }

}
