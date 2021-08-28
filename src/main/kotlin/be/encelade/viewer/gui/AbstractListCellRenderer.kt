package be.encelade.viewer.gui

import java.awt.Color
import java.awt.Component
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.ListCellRenderer

abstract class AbstractListCellRenderer<T> : ListCellRenderer<T> {

    abstract fun renderItemToString(value: T): String

    override fun getListCellRendererComponent(list: JList<out T>?, value: T?, index: Int, isSelected: Boolean, cellHasFocus: Boolean): Component {
        val label = JLabel(renderItemToString(value!!))
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
