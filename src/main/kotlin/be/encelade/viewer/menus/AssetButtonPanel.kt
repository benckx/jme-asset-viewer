package be.encelade.viewer.menus

import be.encelade.viewer.commands.CloneCommand
import be.encelade.viewer.commands.CommandQueue
import be.encelade.viewer.commands.DeleteAssetNodeCommand
import be.encelade.viewer.commands.ImportAssetCommand
import java.awt.Font
import java.awt.GridLayout
import java.io.File
import javax.swing.JButton
import javax.swing.JFileChooser
import javax.swing.JPanel

internal class AssetButtonPanel(guiFont: Font,
                                commandQueue: CommandQueue,
                                context: GuiContext,
                                parent: AssetMenu) : JPanel() {

    private val importButton = JButton("Import")
    private val cloneButton = JButton("Clone")
    private val deleteButton = JButton("Delete")

    init {
        layout = GridLayout(0, 1)

        add(importButton)
        add(deleteButton)
        add(cloneButton)
        components.forEach { component -> component.font = guiFont }

        disableFocus()

        importButton.addActionListener {
            val fileChooser = JFileChooser()
            context.lastFolder?.let { folder -> fileChooser.currentDirectory = File(folder) }
            val returnValue = fileChooser.showOpenDialog(importButton)
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                val file = fileChooser.selectedFile
                val containingFolder = file.path.split(File.separator).dropLast(1).joinToString(File.separator)
                context.lastFolder = containingFolder
                commandQueue.queue(ImportAssetCommand(file) { sceneNode ->
                    parent.addToAssetList(sceneNode)
                    parent.show(sceneNode)
                })
            }
        }

        cloneButton.addActionListener {
            context.selectedAssetNode?.let { assetNode ->
                commandQueue.queue(CloneCommand(assetNode.id) { sceneNode ->
                    parent.addToAssetList(sceneNode)
                    parent.show(sceneNode)
                })
            }
        }

        deleteButton.addActionListener {
            context.selectedAssetNode?.let { assetNode ->
                commandQueue.queue(DeleteAssetNodeCommand(assetNode.id) {
                    parent.removeFromAssetList(assetNode.id)
                    parent.disableFocus()
                })
            }
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
