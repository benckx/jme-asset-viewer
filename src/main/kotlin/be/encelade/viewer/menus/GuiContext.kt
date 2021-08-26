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
        get() {
            return if (field == null) {
                propertiesFile.getProperty(DEFAULT_FOLDER_KEY)
            } else {
                field
            }
        }
        set(value) {
            if (value != null) {
                field = value
                propertiesFile.persistProperty(DEFAULT_FOLDER_KEY, value)
            }
        }

}
