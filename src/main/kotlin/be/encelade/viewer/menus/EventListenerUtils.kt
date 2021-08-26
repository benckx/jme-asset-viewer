package be.encelade.viewer.menus

import javax.swing.JTextField
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.text.Document

object EventListenerUtils {

    /**
     * Syntactic sugar for [DocumentListener] implementation, where callback is identical for all types of updates
     */
    fun Document.addAnyUpdateDocumentListener(callback: (DocumentEvent) -> Unit) {
        this.addDocumentListener(object : DocumentListener {
            override fun insertUpdate(e: DocumentEvent?) = callback(e!!)
            override fun removeUpdate(e: DocumentEvent?) = callback(e!!)
            override fun changedUpdate(e: DocumentEvent?) = callback(e!!)
        })
    }

    fun JTextField.addAnyUpdateDocumentListener(callback: (DocumentEvent) -> Unit) {
        this.document.addAnyUpdateDocumentListener(callback)
    }

}
