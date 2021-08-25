package be.encelade.viewer.menus

import be.encelade.viewer.commands.CommandQueue
import be.encelade.viewer.commands.RotationCommand
import be.encelade.viewer.commands.ScaleCommand
import be.encelade.viewer.commands.TranslationCommand
import be.encelade.viewer.scene.AssetNode
import be.encelade.viewer.scene.SceneNode
import be.encelade.viewer.utils.LazyLogging
import be.encelade.viewer.utils.PropertiesFile
import com.jme3.math.FastMath.DEG_TO_RAD
import com.jme3.math.FastMath.RAD_TO_DEG
import com.jme3.math.Quaternion
import com.jme3.math.Vector3f
import com.jme3.scene.Node
import java.awt.BorderLayout
import java.awt.Font
import java.awt.GridLayout
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class AssetMenu(propertiesFile: PropertiesFile, private val commandQueue: CommandQueue) : JFrame(), LazyLogging {

    private val guiFont = Font("Arial", Font.PLAIN, 16)

    private var selectedAssetNode: AssetNode? = null
    private var assetUpdateEnabled = false

    private val xPosField = JTextField("0.0")
    private val yPosField = JTextField("0.0")
    private val zPosField = JTextField("0.0")
    private val positionFields = listOf(xPosField, yPosField, zPosField)

    private val xRotationField = JTextField("0.0")
    private val yRotationField = JTextField("0.0")
    private val zRotationField = JTextField("0.0")
    private val wRotationField = JTextField("0.0")
    private val rotationFields = listOf(xRotationField, yRotationField, zRotationField, wRotationField)

    private val scaleField = JTextField("1.0")
    private val scaleFields = listOf(scaleField)

    private val allFields = positionFields + rotationFields + scaleFields

    private val buttonPanel = AssetButtonPanel(guiFont, commandQueue, propertiesFile, this)

    init {
        title = defaultTitle
        setBounds(300, 90, 250, 500)
        defaultCloseOperation = EXIT_ON_CLOSE
        isResizable = true

        val southPanel = JPanel()
        southPanel.layout = GridLayout(0, 2)

        layout = BorderLayout()
        add(buttonPanel, BorderLayout.NORTH)
        add(southPanel, BorderLayout.SOUTH)

        val xPosLabel = JLabel("position x:")
        val yPosLabel = JLabel("position y:")
        val zPosLabel = JLabel("position z:")

        val xRotationLabel = JLabel("rotation x:")
        val yRotationLabel = JLabel("rotation y:")
        val zRotationLabel = JLabel("rotation z:")
        val wRotationLabel = JLabel("rotation w:")

        val scaleLabel = JLabel("scale:")

        allFields.forEach { it.isEnabled = false }

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

        southPanel.add(scaleLabel)
        southPanel.add(scaleField)

        southPanel.components.forEach { it.font = guiFont }

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

        scaleFields.forEach { field ->
            field.document.addDocumentListener(object : DocumentListener {
                override fun insertUpdate(e: DocumentEvent?) = updateScale()
                override fun removeUpdate(e: DocumentEvent?) = updateScale()
                override fun changedUpdate(e: DocumentEvent?) = updateScale()
            })

            updateOnMouseWheel(field, .05f)
        }
    }

    private fun updatePosition() {
        if (assetUpdateEnabled) {
            selectedAssetNode?.let { assetNode ->
                if (allFloats(positionFields)) {
                    commandQueue.push(TranslationCommand(assetNode.id, toVector3f(positionFields)))
                }
            }
        }
    }

    private fun updateRotation() {
        if (assetUpdateEnabled) {
            selectedAssetNode?.let { assetNode ->
                if (allFloats(rotationFields)) {
                    commandQueue.push(RotationCommand(assetNode.id, toQuaternion(rotationFields)))
                }
            }
        }
    }

    private fun updateScale() {
        if (assetUpdateEnabled) {
            selectedAssetNode?.let { assetNode ->
                if (allFloats(scaleFields)) {
                    commandQueue.push(ScaleCommand(assetNode.id, scaleField.text.toFloat()))
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

    fun selectedAssetNode(): AssetNode? {
        return selectedAssetNode
    }

    fun showInForm(sceneNode: SceneNode) {
        showInForm(sceneNode.assetNode, sceneNode.node)
    }

    private fun showInForm(assetNode: AssetNode, node: Node) {
        this.selectedAssetNode = assetNode

        title = assetNode.fileName

        val translation = node.localTranslation
        xPosField.text = translation.x.toString()
        yPosField.text = translation.y.toString()
        zPosField.text = translation.z.toString()

        val rotation = node.localRotation
        xRotationField.text = (RAD_TO_DEG * rotation.x).toString()
        yRotationField.text = (RAD_TO_DEG * rotation.y).toString()
        zRotationField.text = (RAD_TO_DEG * rotation.z).toString()
        wRotationField.text = (RAD_TO_DEG * rotation.w).toString()

        scaleField.text = node.localScale.x.toString()

        allFields.forEach { field -> field.isEnabled = true }
        buttonPanel.enableFocus()
        assetUpdateEnabled = true
    }

    fun unFocusAll() {
        this.selectedAssetNode = null
        assetUpdateEnabled = false
        buttonPanel.disableFocus()
        allFields.forEach { it.isEnabled = false }
        positionFields.forEach { it.text = "0.0" }
        rotationFields.forEach { it.text = "0.0" }
        scaleFields.forEach { it.text = "1.0" }
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
