package be.encelade.viewer.menus

import be.encelade.viewer.scene.SceneNode
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Component
import java.awt.Font
import javax.swing.*

internal class AssetListPanel(private val guiFont: Font, private val context: GuiContext) : JPanel() {

    private val listModel = DefaultListModel<SceneNode>()
    private val list = JList(listModel)

    init {
        layout = BorderLayout()

        list.cellRenderer = AssetNodeRenderer()

        add(JLabel("Scene Assets:"), BorderLayout.NORTH)
        add(list, BorderLayout.CENTER)
    }

    fun add(sceneNode: SceneNode) {
        listModel.addElement(sceneNode)
        list.selectedIndex = list.lastVisibleIndex
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
