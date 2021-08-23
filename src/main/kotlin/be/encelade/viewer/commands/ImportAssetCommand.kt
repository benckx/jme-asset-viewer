package be.encelade.viewer.commands

import be.encelade.viewer.scene.SceneNode
import java.io.File

data class ImportAssetCommand(val file: File, val callback: (SceneNode) -> Unit)
