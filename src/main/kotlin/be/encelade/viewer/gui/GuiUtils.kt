package be.encelade.viewer.gui

import com.jme3.math.FastMath
import com.jme3.math.Quaternion
import com.jme3.math.Vector3f
import javax.swing.JTextField

internal object GuiUtils {

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
