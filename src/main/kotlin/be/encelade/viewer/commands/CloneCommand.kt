package be.encelade.viewer.commands

import be.encelade.viewer.scene.SceneNode

data class CloneCommand(val id: String, val callback: (SceneNode) -> Unit)
