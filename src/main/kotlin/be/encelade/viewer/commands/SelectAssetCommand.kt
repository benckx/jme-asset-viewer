package be.encelade.viewer.commands

import be.encelade.viewer.scene.SceneNode

data class SelectAssetCommand(val sceneNode: SceneNode, val callback: () -> Unit)
