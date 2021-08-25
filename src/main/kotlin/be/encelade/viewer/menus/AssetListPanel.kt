package be.encelade.viewer.menus

import be.encelade.viewer.scene.SceneNode
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Component
import java.awt.Font
import javax.swing.*

internal class AssetListPanel(guiFont: Font) : JPanel() {

    private val listModel = DefaultListModel<SceneNode>()
    private val list = JList(listModel)
    private val scrollPane = JScrollPane(list)

    init {
        layout = BorderLayout()
        list.cellRenderer = AssetNodeRenderer()

        val titleLabel = JLabel("Scene Assets:")
        titleLabel.font = guiFont

        add(titleLabel, BorderLayout.NORTH)
        add(scrollPane, BorderLayout.CENTER)
    }

    internal fun add(sceneNode: SceneNode) {
        listModel.addElement(sceneNode)
        list.selectedIndex = list.lastVisibleIndex
    }

    internal fun show(sceneNode: SceneNode) {
        val i = listModel.elements()!!.toList().indexOf(sceneNode)
        list.selectedIndex = i
    }

    internal fun disableFocus() {
        list.clearSelection()
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
