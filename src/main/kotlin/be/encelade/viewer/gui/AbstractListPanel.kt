package be.encelade.viewer.gui

import be.encelade.viewer.commands.CommandQueue
import be.encelade.viewer.gui.GuiUtils.copy
import be.encelade.viewer.gui.GuiUtils.createDefaultPanelBorder
import be.encelade.viewer.gui.GuiUtils.createEmptyBorder
import be.encelade.viewer.gui.GuiUtils.guiFont
import be.encelade.viewer.utils.LazyLogging
import java.awt.BorderLayout
import java.awt.Font
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*

internal abstract class AbstractListPanel<T>(title: String, protected val commandQueue: CommandQueue) :
        JPanel(), LazyLogging {

    private val listModel = DefaultListModel<T>()
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

        list.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                if (isListenerEnabled && e.clickCount == 2) {
                    onDoubleClick(list.selectedValue)
                }
            }
        })

        list.addKeyListener(object : KeyListener {
            override fun keyTyped(e: KeyEvent?) {}

            override fun keyPressed(e: KeyEvent?) {
                if (isListenerEnabled) {
                    onKeyPressed(list.selectedValue, e!!)
                }
            }

            override fun keyReleased(e: KeyEvent?) {}

        })
    }

    open fun onSelect(value: T) {
        logger.debug("$value selected")
    }

    open fun onDoubleClick(value: T) {
        logger.debug("$value double-clicked")
    }

    open fun onKeyPressed(value: T, e: KeyEvent) {
        logger.debug("key ${e.keyCode} on $value")
    }

    protected fun listItems(): List<T> {
        return getListModel().elements()!!.toList()
    }

    internal fun getListModel(): DefaultListModel<T> {
        return listModel
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

    internal fun clear() {
        executeWithoutListener {
            listModel.clear()
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
