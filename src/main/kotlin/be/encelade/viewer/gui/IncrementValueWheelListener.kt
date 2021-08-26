package be.encelade.viewer.gui

import be.encelade.viewer.gui.GuiUtils.isFloat
import java.awt.event.MouseWheelEvent
import java.awt.event.MouseWheelListener
import javax.swing.JTextField

class IncrementValueWheelListener(private val amount: Float) : MouseWheelListener {

    override fun mouseWheelMoved(e: MouseWheelEvent?) {
        e?.let { _ ->
            if (e.source is JTextField) {
                val field = e.source as JTextField
                if (field.isEnabled && isFloat(field)) {
                    val value = field.text.toFloat()
                    val delta = e.wheelRotation * amount * -1f
                    val newValue = value + delta
                    field.text = newValue.toString()
                }
            }
        }
    }

}
