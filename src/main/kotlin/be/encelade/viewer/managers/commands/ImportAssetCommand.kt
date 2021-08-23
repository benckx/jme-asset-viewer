package be.encelade.viewer.managers.commands

import be.encelade.viewer.scene.SceneNode
import java.io.File

data class ImportAssetCommand(val file: File, val callback: (SceneNode) -> Unit)
