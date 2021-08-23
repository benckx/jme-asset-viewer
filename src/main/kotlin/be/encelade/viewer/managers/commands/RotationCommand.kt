package be.encelade.viewer.managers.commands

import com.jme3.math.Quaternion

data class RotationCommand(val id: String, val rotation: Quaternion)
