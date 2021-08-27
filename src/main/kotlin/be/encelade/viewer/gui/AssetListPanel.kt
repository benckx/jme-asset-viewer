package be.encelade.viewer.gui

import be.encelade.viewer.commands.CommandQueue
import be.encelade.viewer.commands.SelectAssetCommand
import be.encelade.viewer.scene.SceneNode
import be.encelade.viewer.utils.LazyLogging
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Component
import java.awt.Font
import javax.swing.*

internal class AssetListPanel(guiFont: Font, commandQueue: CommandQueue, parent: AssetMenu) : JPanel(), LazyLogging {

    private val listModel = DefaultListModel<SceneNode>()
    private val list = JList(listModel)
    private val scrollPane = JScrollPane(list)

    private var isListenerEnabled = true

    init {
        list.cellRenderer = AssetNodeRenderer()

        layout = BorderLayout()
        border = BorderFactory.createEmptyBorder(7, 5, 7, 5)

        val titleLabel = JLabel("Scene Assets")
        titleLabel.border = BorderFactory.createEmptyBorder(0, 0, 3, 0)
        titleLabel.font = guiFont.deriveFont(Font.BOLD)

        add(titleLabel, BorderLayout.NORTH)
        add(scrollPane, BorderLayout.CENTER)

        list.addListSelectionListener { e ->
            if (isListenerEnabled && !e.valueIsAdjusting) {
                list.selectedValue?.let { sceneNode ->
                    commandQueue.queue(SelectAssetCommand(sceneNode) {
                        parent.show(sceneNode, showInList = false)
                    })
                }
            }
        }
    }

    internal fun add(sceneNode: SceneNode) {
        executeWithoutListener {
            listModel.addElement(sceneNode)
            list.selectedIndex = list.lastVisibleIndex
        }
    }

    internal fun remove(id: String) {
        executeWithoutListener {
            listModel.remove(indexOf(id))
            list.clearSelection()
        }
    }

    internal fun show(sceneNode: SceneNode) {
        executeWithoutListener {
            list.selectedIndex = indexOf(sceneNode)
        }
    }

    internal fun disableFocus() {
        executeWithoutListener {
            list.clearSelection()
        }
    }

    private fun executeWithoutListener(callback: () -> Unit) {
        isListenerEnabled = false
        callback()
        isListenerEnabled = true
    }

    private fun indexOf(sceneNode: SceneNode): Int {
        return listModel.elements()!!.toList().indexOf(sceneNode)
    }

    private fun indexOf(id: String): Int {
        return listModel.elements()!!.toList().indexOfFirst { sceneNode -> sceneNode.id() == id }
    }

    private class AssetNodeRenderer : ListCellRenderer<SceneNode> {

        override fun getListCellRendererComponent(list: JList<out SceneNode>?, value: SceneNode?, index: Int, isSelected: Boolean, cellHasFocus: Boolean): Component {
            val label = JLabel(value!!.assetNode.fileName)
            if (isSelected) {
                label.background = Color.BLUE
                label.foreground = Color.WHITE
                label.isOpaque = true
            } else {
                label.background = Color.WHITE
                label.foreground = Color.BLACK
            }
            return label
        }

    }

}
