package be.encelade.viewer.gui

import be.encelade.viewer.scene.AssetNode
import be.encelade.viewer.utils.PropertiesFile
import be.encelade.viewer.utils.PropertiesKey.DEFAULT_FOLDER

internal class GuiContext(private val propertiesFile: PropertiesFile) {

    var selectedAssetNode: AssetNode? = null
        get() {
            return if (assetUpdateEnabled) field else null
        }

    var assetUpdateEnabled = false

    var lastFolder: String? = null
        get() {
            return if (field == null) {
                propertiesFile.getProperty(DEFAULT_FOLDER)
            } else {
                field
            }
        }
        set(value) {
            if (value != null) {
                field = value
                propertiesFile.persistProperty(DEFAULT_FOLDER, value)
            }
        }

}
