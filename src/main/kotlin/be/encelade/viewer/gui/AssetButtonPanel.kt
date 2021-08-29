package be.encelade.viewer.gui

import be.encelade.viewer.commands.*
import be.encelade.viewer.gui.GuiUtils.buildFileChooser
import be.encelade.viewer.gui.GuiUtils.copy
import be.encelade.viewer.gui.GuiUtils.createDefaultPanelBorder
import be.encelade.viewer.gui.GuiUtils.guiFont
import be.encelade.viewer.utils.LazyLogging
import java.awt.GridLayout
import java.io.File
import javax.swing.JButton
import javax.swing.JFileChooser
import javax.swing.JPanel

internal class AssetButtonPanel(commandQueue: CommandQueue,
                                private val context: GuiContext,
                                parent: AssetMenu) : JPanel(), LazyLogging {

    private val importButton = JButton("Import")
    private val cloneButton = JButton("Clone")
    private val deleteButton = JButton("Delete")
    private val deleteAllButton = JButton("Delete All")

    init {
        layout = GridLayout(0, 1)
        border = createDefaultPanelBorder().copy(top = 3)

        add(importButton)
        add(deleteButton)
        add(cloneButton)
        add(deleteAllButton)
        components.forEach { component -> component.font = guiFont }

        disableFocus()

        importButton.addActionListener {
            val fileChooser = buildFileChooser(context.lastFolder)
            val returnValue = fileChooser.showOpenDialog(importButton)
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                val file = fileChooser.selectedFile
                val containingFolder = file.path.split(File.separator).dropLast(1).joinToString(File.separator)
                context.lastFolder = containingFolder
                commandQueue.queue(ImportAssetCommand(file) { sceneNode ->
                    parent.addToAssetList(sceneNode)
                    parent.show(sceneNode, showInList = false)
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

        deleteAllButton.addActionListener {
            commandQueue.queue(DeleteAllCommand {
                parent.clear()
                parent.disableFocus()
            })
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
