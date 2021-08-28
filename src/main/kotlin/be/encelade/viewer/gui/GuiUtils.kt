package be.encelade.viewer.gui

import com.jme3.math.FastMath
import com.jme3.math.Quaternion
import com.jme3.math.Vector3f
import java.io.File
import javax.swing.BorderFactory
import javax.swing.JFileChooser
import javax.swing.JTextField
import javax.swing.border.EmptyBorder

object GuiUtils {

    fun createDefaultPanelBorder(): EmptyBorder {
        return createEmptyBorder(left = 5, right = 5)
    }

    /**
     * Convenience method with Kotlin named parameters
     */
    fun EmptyBorder.copy(top: Int? = null, left: Int? = null, bottom: Int? = null, right: Int? = null): EmptyBorder {
        val t = top ?: borderInsets.top
        val l = left ?: borderInsets.left
        val b = bottom ?: borderInsets.bottom
        val r = right ?: borderInsets.right
        return EmptyBorder(t, l, b, r)
    }

    /**
     * Convenience method with Kotlin named parameters
     */
    fun createEmptyBorder(top: Int = 0, left: Int = 0, bottom: Int = 0, right: Int = 0): EmptyBorder {
        return BorderFactory.createEmptyBorder(top, left, bottom, right) as EmptyBorder
    }

    fun buildFileChooser(defaultFolder: String?): JFileChooser {
        val fileChooser = JFileChooser()
        defaultFolder?.let { folder -> fileChooser.currentDirectory = File(folder) }
        return fileChooser
    }

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
                    FastMath.DEG_TO_RAD * fields[0].text.toFloat(),
                    FastMath.DEG_TO_RAD * fields[1].text.toFloat(),
                    FastMath.DEG_TO_RAD * fields[2].text.toFloat(),
                    FastMath.DEG_TO_RAD * fields[3].text.toFloat()
            )
        } else {
            throw IllegalArgumentException("Quaternion needs 4 fields, found ${fields.size}")
        }
    }

}
