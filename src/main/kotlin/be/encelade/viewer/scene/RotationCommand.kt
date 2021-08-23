package be.encelade.viewer.scene

import com.jme3.math.Quaternion

data class RotationCommand(val id: String, val rotation: Quaternion)
