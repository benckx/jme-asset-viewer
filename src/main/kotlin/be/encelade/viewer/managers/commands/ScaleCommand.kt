package be.encelade.viewer.managers.commands

import com.jme3.math.Vector3f

data class ScaleCommand(val id: String, val scale: Float) {

    fun toVector3f(): Vector3f = Vector3f(scale, scale, scale)

}