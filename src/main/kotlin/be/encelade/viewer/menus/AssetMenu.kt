package be.encelade.viewer.menus

import be.encelade.viewer.managers.SceneManager
import be.encelade.viewer.scene.AssetNode
import be.encelade.viewer.utils.LazyLogging
import com.jme3.math.Vector3f
import java.awt.BorderLayout
import java.awt.Font
import java.awt.GridLayout
import java.io.File
import javax.swing.*
import javax.swing.JFileChooser.APPROVE_OPTION
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class AssetMenu(private val sceneManager: SceneManager) : JFrame(), LazyLogging {

    private var lastFolder: String? = null
    private var selectedAssetNode: AssetNode? = null
    private var assetUpdateEnabled = false

    private val xPosField = JTextField("0.0")
    private val yPosField = JTextField("0.0")
    private val zPosField = JTextField("0.0")

    private val positionFields = listOf(xPosField, yPosField, zPosField)

    init {
        title = defaultTitle
        setBounds(300, 90, 200, 150)
        defaultCloseOperation = EXIT_ON_CLOSE
        isResizable = true

        val font = Font("Arial", Font.PLAIN, 19)

        val northPanel = JPanel()
        northPanel.layout = GridLayout(0, 1)

        val southPanel = JPanel()
        southPanel.layout = GridLayout(0, 2)

        val panel = JPanel()
        panel.layout = BorderLayout()
        panel.add(northPanel, BorderLayout.NORTH)
        panel.add(southPanel, BorderLayout.SOUTH)
        add(panel)

        val importButton = JButton("Import")
        northPanel.add(importButton)
        northPanel.components.forEach { it.font = font }

        val xPosLabel = JLabel("x:")
        val yPosLabel = JLabel("y:")
        val zPosLabel = JLabel("z:")

        positionFields.forEach { it.isEnabled = false }

        southPanel.add(xPosLabel)
        southPanel.add(xPosField)
        southPanel.add(yPosLabel)
        southPanel.add(yPosField)
        southPanel.add(zPosLabel)
        southPanel.add(zPosField)
        southPanel.components.forEach { it.font = font }

        isVisible = true

        importButton.addActionListener {
            val fileChooser = JFileChooser()
            lastFolder?.let { folder -> fileChooser.currentDirectory = File(folder) }
            val returnValue = fileChooser.showOpenDialog(importButton)
            if (returnValue == APPROVE_OPTION) {
                val file = fileChooser.selectedFile
                lastFolder = file.path.split(File.separator).dropLast(1).joinToString(separator = File.separator)
                sceneManager.importAsset(file)
            }
        }

        positionFields.forEach { field ->
            field.document.addDocumentListener(object : DocumentListener {
                override fun insertUpdate(e: DocumentEvent?) = updatePosition()
                override fun removeUpdate(e: DocumentEvent?) = updatePosition()
                override fun changedUpdate(e: DocumentEvent?) = updatePosition()
            })
        }
    }

    private fun updatePosition() {
        if (assetUpdateEnabled) {
            selectedAssetNode?.let { assetNode ->
                if (positionFields.all { isFloat(it) }) {
                    val position = Vector3f(xPosField.text.toFloat(), yPosField.text.toFloat(), zPosField.text.toFloat())
                    logger.debug("new position: $position")
                    assetNode.node.localTranslation = position
                }
            }
        }
    }

    fun loadInGui(assetNode: AssetNode) {
        this.selectedAssetNode = assetNode

        title = assetNode.fileName

        val translation = assetNode.localTranslation()
        xPosField.text = translation.x.toString()
        yPosField.text = translation.y.toString()
        zPosField.text = translation.z.toString()

        positionFields.forEach { field -> field.isEnabled = true }
        assetUpdateEnabled = true
    }

    fun unloadAll() {
        this.selectedAssetNode = null
        positionFields.forEach { it.isEnabled = false }
        positionFields.forEach { it.text = "0.0" }
        title = defaultTitle
        assetUpdateEnabled = false
    }

    private companion object {

        const val defaultTitle = "Asset"

        fun isFloat(value: String): Boolean {
            return try {
                value.toFloat()
                true
            } catch (e: NumberFormatException) {
                false
            }
        }

        fun isFloat(field: JTextField): Boolean {
            return isFloat(field.text)
        }

    }

}
