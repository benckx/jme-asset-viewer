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
import javax.swing.event.ListSelectionListener

internal class AssetListPanel(guiFont: Font, commandQueue: CommandQueue, parent: AssetMenu) : JPanel(), LazyLogging {

    private val listModel = DefaultListModel<SceneNode>()
    private val list = JList(listModel)
    private val scrollPane = JScrollPane(list)

    private val selectionListener = ListSelectionListener { e ->
        logger.info("received event [firstIndex=${e.firstIndex}, lastIndex=${e.lastIndex}, isAdjusting=${e.valueIsAdjusting}]")
        val sceneNode = listModel.get(e.firstIndex)
        commandQueue.queue(SelectAssetCommand(sceneNode) {
            parent.show(sceneNode, showInList = false)
        })
    }

    init {
        layout = BorderLayout()
        list.cellRenderer = AssetNodeRenderer()

        val titleLabel = JLabel("Scene Assets:")
        titleLabel.font = guiFont

        add(titleLabel, BorderLayout.NORTH)
        add(scrollPane, BorderLayout.CENTER)

        list.addListSelectionListener(selectionListener)
    }

    internal fun add(sceneNode: SceneNode) {
        listModel.addElement(sceneNode)
        list.selectedIndex = list.lastVisibleIndex
    }

    internal fun remove(id: String) {
        list.remove(indexOf(id))
        disableFocus()
    }

    internal fun show(sceneNode: SceneNode) {
        list.removeListSelectionListener(selectionListener)
        list.selectedIndex = indexOf(sceneNode)
        list.addListSelectionListener(selectionListener)
    }

    internal fun disableFocus() {
        list.removeListSelectionListener(selectionListener)
        list.clearSelection()
        list.addListSelectionListener(selectionListener)
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
            if (isSelected || cellHasFocus) {
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
