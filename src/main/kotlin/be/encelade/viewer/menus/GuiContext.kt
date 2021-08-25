package be.encelade.viewer.menus

import be.encelade.viewer.scene.AssetNode
import be.encelade.viewer.utils.PropertiesFile
import be.encelade.viewer.utils.PropertiesFile.Companion.DEFAULT_FOLDER_KEY

internal class GuiContext(private val propertiesFile: PropertiesFile) {

    var selectedAssetNode: AssetNode? = null
        get() {
            return if (assetUpdateEnabled) field else null
        }

    var assetUpdateEnabled = false

    var lastFolder: String? = null
        set(value) {
            if (value != null) {
                field = value
                propertiesFile.persistProperty(DEFAULT_FOLDER_KEY, value)
            }
        }

    init {
        this.lastFolder = propertiesFile.getProperty(DEFAULT_FOLDER_KEY)
    }

}
