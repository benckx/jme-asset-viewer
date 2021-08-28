package be.encelade.viewer.gui

import be.encelade.viewer.commands.CommandQueue
import be.encelade.viewer.gui.GuiUtils.copy
import be.encelade.viewer.gui.GuiUtils.createDefaultPanelBorder
import be.encelade.viewer.gui.GuiUtils.createEmptyBorder
import be.encelade.viewer.gui.GuiUtils.guiFont
import be.encelade.viewer.utils.LazyLogging
import java.awt.BorderLayout
import java.awt.Font
import javax.swing.*

internal abstract class AbstractListPanel<T>(title: String, protected val commandQueue: CommandQueue) :
        JPanel(), LazyLogging {

    protected val listModel = DefaultListModel<T>()
    private val list = JList(listModel)
    private val scrollPane = JScrollPane(list)

    private var isListenerEnabled = true

    abstract fun renderItemName(value: T): String
    abstract fun indexOf(value: T): Int
    abstract fun indexOf(id: String): Int

    init {
        list.cellRenderer = object : AbstractListCellRenderer<T>() {
            override fun renderItemToString(value: T) = renderItemName(value)
        }

        layout = BorderLayout()
        border = createDefaultPanelBorder().copy(top = 7, bottom = 7)

        val titleLabel = JLabel(title)
        titleLabel.border = createEmptyBorder(bottom = 3)
        titleLabel.font = guiFont.deriveFont(Font.BOLD)

        super.add(titleLabel, BorderLayout.NORTH)
        super.add(scrollPane, BorderLayout.CENTER)

        list.addListSelectionListener { e ->
            if (isListenerEnabled && !e.valueIsAdjusting) {
                list.selectedValue?.let { value -> onSelect(value) }
            }
        }
    }

    open fun onSelect(value: T) {
        logger.debug("$value selected")
    }

    internal fun add(value: T) {
        executeWithoutListener {
            listModel.addElement(value)
            list.selectedIndex = list.lastVisibleIndex
        }
    }

    internal fun remove(id: String) {
        executeWithoutListener {
            listModel.remove(indexOf(id))
            list.clearSelection()
        }
    }

    internal fun show(value: T) {
        executeWithoutListener {
            list.selectedIndex = indexOf(value)
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

}
