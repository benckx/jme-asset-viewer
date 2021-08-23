package be.encelade.viewer.menus

import be.encelade.viewer.managers.SceneManager
import be.encelade.viewer.scene.AssetNode
import be.encelade.viewer.utils.LazyLogging
import be.encelade.viewer.utils.PropertiesFile
import be.encelade.viewer.utils.PropertiesFile.Companion.DEFAULT_FOLDER_KEY
import com.jme3.math.FastMath.DEG_TO_RAD
import com.jme3.math.FastMath.RAD_TO_DEG
import com.jme3.math.Quaternion
import com.jme3.math.Vector3f
import java.awt.BorderLayout
import java.awt.Font
import java.awt.GridLayout
import java.io.File
import javax.swing.*
import javax.swing.JFileChooser.APPROVE_OPTION
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class AssetMenu(private val propertiesFile: PropertiesFile,
                private val sceneManager: SceneManager) : JFrame(), LazyLogging {

    private var lastFolder: String? = null
    private var selectedAssetNode: AssetNode? = null
    private var assetUpdateEnabled = false

    private val importButton = JButton("Import")
    private val cloneButton = JButton("Clone")
    private val deleteButton = JButton("Delete")

    private val xPosField = JTextField("0.0")
    private val yPosField = JTextField("0.0")
    private val zPosField = JTextField("0.0")
    private val positionFields = listOf(xPosField, yPosField, zPosField)

    private val xRotationField = JTextField("0.0")
    private val yRotationField = JTextField("0.0")
    private val zRotationField = JTextField("0.0")
    private val wRotationField = JTextField("0.0")
    private val rotationFields = listOf(xRotationField, yRotationField, zRotationField, wRotationField)

    init {
        title = defaultTitle
        setBounds(300, 90, 200, 400)
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

        northPanel.add(importButton)
        northPanel.add(deleteButton)
        northPanel.add(cloneButton)
        northPanel.components.forEach { it.font = font }

        deleteButton.isEnabled = false
        cloneButton.isEnabled = false

        val xPosLabel = JLabel("position x:")
        val yPosLabel = JLabel("position y:")
        val zPosLabel = JLabel("position z:")

        val xRotationLabel = JLabel("rotation x:")
        val yRotationLabel = JLabel("rotation y:")
        val zRotationLabel = JLabel("rotation z:")
        val wRotationLabel = JLabel("rotation w:")

        positionFields.forEach { it.isEnabled = false }
        rotationFields.forEach { it.isEnabled = false }

        southPanel.add(xPosLabel)
        southPanel.add(xPosField)
        southPanel.add(yPosLabel)
        southPanel.add(yPosField)
        southPanel.add(zPosLabel)
        southPanel.add(zPosField)

        southPanel.add(xRotationLabel)
        southPanel.add(xRotationField)
        southPanel.add(yRotationLabel)
        southPanel.add(yRotationField)
        southPanel.add(zRotationLabel)
        southPanel.add(zRotationField)
        southPanel.add(wRotationLabel)
        southPanel.add(wRotationField)

        southPanel.components.forEach { it.font = font }

        isVisible = true

        positionFields.forEach { field ->
            field.document.addDocumentListener(object : DocumentListener {
                override fun insertUpdate(e: DocumentEvent?) = updatePosition()
                override fun removeUpdate(e: DocumentEvent?) = updatePosition()
                override fun changedUpdate(e: DocumentEvent?) = updatePosition()
            })

            updateOnMouseWheel(field, .1f)
        }

        rotationFields.forEach { field ->
            field.document.addDocumentListener(object : DocumentListener {
                override fun insertUpdate(e: DocumentEvent?) = updateRotation()
                override fun removeUpdate(e: DocumentEvent?) = updateRotation()
                override fun changedUpdate(e: DocumentEvent?) = updateRotation()
            })

            updateOnMouseWheel(field, 1f)
        }

        importButton.addActionListener {
            val fileChooser = JFileChooser()
            lastFolder?.let { folder -> fileChooser.currentDirectory = File(folder) }
            val returnValue = fileChooser.showOpenDialog(importButton)
            if (returnValue == APPROVE_OPTION) {
                val file = fileChooser.selectedFile
                val containingFolder = file.path.split(File.separator).dropLast(1).joinToString(File.separator)
                lastFolder = containingFolder
                propertiesFile.persistProperty(DEFAULT_FOLDER_KEY, containingFolder)
                val assetNode = sceneManager.importAsset(file)
                loadInGui(assetNode)
            }
        }

        deleteButton.addActionListener {
            selectedAssetNode?.let { assetNode ->
                sceneManager.delete(assetNode)
                unFocusAll()
            }
        }

        propertiesFile.getProperty(DEFAULT_FOLDER_KEY)?.let { containingFolder ->
            lastFolder = containingFolder
        }
    }

    private fun updatePosition() {
        if (assetUpdateEnabled) {
            selectedAssetNode?.let { assetNode ->
                if (allFloats(positionFields)) {
                    assetNode.node.localTranslation = toVector3f(positionFields)
                }
            }
        }
    }

    private fun updateRotation() {
        if (assetUpdateEnabled) {
            selectedAssetNode?.let { assetNode ->
                if (allFloats(rotationFields)) {
                    assetNode.node.localRotation = toQuaternion(rotationFields)
                }
            }
        }
    }

    private fun updateOnMouseWheel(field: JTextField, amount: Float) {
        field.addMouseWheelListener { e ->
            if (isFloat(field)) {
                val value = field.text.toFloat()
                val delta = e.wheelRotation * amount * -1f
                val newValue = value + delta
                field.text = newValue.toString()
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

        val rotation = assetNode.localRotation()
        xRotationField.text = (RAD_TO_DEG * rotation.x).toString()
        yRotationField.text = (RAD_TO_DEG * rotation.y).toString()
        zRotationField.text = (RAD_TO_DEG * rotation.z).toString()
        wRotationField.text = (RAD_TO_DEG * rotation.w).toString()

        positionFields.forEach { field -> field.isEnabled = true }
        rotationFields.forEach { field -> field.isEnabled = true }
        deleteButton.isEnabled = true
        cloneButton.isEnabled = true
        assetUpdateEnabled = true
    }

    fun unFocusAll() {
        this.selectedAssetNode = null
        assetUpdateEnabled = false
        deleteButton.isEnabled = false
        cloneButton.isEnabled = false
        positionFields.forEach { it.isEnabled = false }
        rotationFields.forEach { it.isEnabled = false }
        positionFields.forEach { it.text = "0.0" }
        rotationFields.forEach { it.text = "0.0" }
        title = defaultTitle
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

        fun allFloats(fields: List<JTextField>): Boolean {
            return fields.all { field -> isFloat(field) }
        }

        fun toVector3f(fields: List<JTextField>): Vector3f {
            if (fields.size == 3) {
                return Vector3f(fields[0].text.toFloat(), fields[1].text.toFloat(), fields[2].text.toFloat())
            } else {
                throw IllegalArgumentException("Vector3f needs 3 fields, found ${fields.size}")
            }
        }

        fun toQuaternion(fields: List<JTextField>): Quaternion {
            if (fields.size == 4) {
                return Quaternion(
                        DEG_TO_RAD * fields[0].text.toFloat(),
                        DEG_TO_RAD * fields[1].text.toFloat(),
                        DEG_TO_RAD * fields[2].text.toFloat(),
                        DEG_TO_RAD * fields[3].text.toFloat()
                )
            } else {
                throw IllegalArgumentException("Quaternion needs 4 fields, found ${fields.size}")
            }
        }

    }

}
