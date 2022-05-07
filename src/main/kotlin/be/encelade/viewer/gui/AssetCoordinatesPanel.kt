package be.encelade.viewer.gui

import be.encelade.viewer.commands.CommandQueue
import be.encelade.viewer.commands.RotationCommand
import be.encelade.viewer.commands.ScaleCommand
import be.encelade.viewer.commands.TranslationCommand
import be.encelade.viewer.gui.EventListenerUtils.addDocumentListener
import be.encelade.viewer.gui.GuiUtils.allFloats
import be.encelade.viewer.gui.GuiUtils.copy
import be.encelade.viewer.gui.GuiUtils.createDefaultPanelBorder
import be.encelade.viewer.gui.GuiUtils.guiFont
import be.encelade.viewer.gui.GuiUtils.toQuaternion
import be.encelade.viewer.gui.GuiUtils.toVector3f
import com.jme3.math.FastMath
import com.jme3.scene.Node
import java.awt.GridLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

internal class AssetCoordinatesPanel(private val commandQueue: CommandQueue,
                                     private val context: GuiContext) : JPanel() {

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

    init {
        layout = GridLayout(0, 2)
        border = createDefaultPanelBorder().copy(bottom = 4)

        val xPosLabel = JLabel("position x:")
        val yPosLabel = JLabel("position y:")
        val zPosLabel = JLabel("position z:")

        val xRotationLabel = JLabel("rotation x:")
        val yRotationLabel = JLabel("rotation y:")
        val zRotationLabel = JLabel("rotation z:")
        val wRotationLabel = JLabel("rotation w:")

        val scaleLabel = JLabel("scale:")

        add(xPosLabel)
        add(xPosField)
        add(yPosLabel)
        add(yPosField)
        add(zPosLabel)
        add(zPosField)

        add(xRotationLabel)
        add(xRotationField)
        add(yRotationLabel)
        add(yRotationField)
        add(zRotationLabel)
        add(zRotationField)
        add(wRotationLabel)
        add(wRotationField)

        add(scaleLabel)
        add(scaleField)

        components.forEach { component -> component.font = guiFont }

        disableFocus()

        positionFields.forEach { field ->
            field.addDocumentListener { updatePosition() }
            field.addMouseWheelListener(IncrementValueWheelListener(.1f))
        }

        rotationFields.forEach { field ->
            field.addDocumentListener { updateRotation() }
            field.addMouseWheelListener(IncrementValueWheelListener(1f))
        }

        scaleFields.forEach { field ->
            field.addDocumentListener { updateScale() }
            field.addMouseWheelListener(IncrementValueWheelListener(.05f))
        }
    }

    private fun updatePosition() {
        context.selectedAssetNode?.let { assetNode ->
            if (allFloats(positionFields)) {
                commandQueue.queue(TranslationCommand(assetNode.id, toVector3f(positionFields)))
            }
        }
    }

    private fun updateRotation() {
        context.selectedAssetNode?.let { assetNode ->
            if (allFloats(rotationFields)) {
                commandQueue.queue(RotationCommand(assetNode.id, toQuaternion(rotationFields)))
            }

        }
    }

    private fun updateScale() {
        context.selectedAssetNode?.let { assetNode ->
            if (allFloats(scaleFields)) {
                commandQueue.queue(ScaleCommand(assetNode.id, scaleField.text.toFloat()))
            }
        }
    }

    internal fun show(node: Node) {
        val translation = node.localTranslation
        xPosField.text = translation.x.toString()
        yPosField.text = translation.y.toString()
        zPosField.text = translation.z.toString()

        val rotation = node.localRotation
        xRotationField.text = (FastMath.RAD_TO_DEG * rotation.x).toString()
        yRotationField.text = (FastMath.RAD_TO_DEG * rotation.y).toString()
        zRotationField.text = (FastMath.RAD_TO_DEG * rotation.z).toString()
        wRotationField.text = (FastMath.RAD_TO_DEG * rotation.w).toString()

        scaleField.text = node.localScale.x.toString()

        allFields.forEach { field -> field.isEnabled = true }
    }

    internal fun disableFocus() {
        allFields.forEach { field -> field.isEnabled = false }
        positionFields.forEach { field -> field.text = "0.0" }
        rotationFields.forEach { field -> field.text = "0.0" }
        scaleFields.forEach { field -> field.text = "1.0" }
    }

}
