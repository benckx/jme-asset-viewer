package be.encelade.viewer.gui

import be.encelade.viewer.commands.CommandQueue
import be.encelade.viewer.scene.SceneNode
import be.encelade.viewer.utils.LazyLogging
import be.encelade.viewer.utils.PropertiesFile
import java.awt.BorderLayout
import java.awt.Font
import javax.swing.JFrame

class AssetMenu(propertiesFile: PropertiesFile, commandQueue: CommandQueue) : JFrame(), LazyLogging {

    private val guiFont = Font("Arial", Font.PLAIN, 17)

    private val context = GuiContext(propertiesFile)
    private val buttonPanel = AssetButtonPanel(guiFont, commandQueue, context, this)
    private val assetListPanel = AssetListPanel(guiFont, commandQueue, this)
    private val coordinatesPanel = AssetCoordinatesPanel(guiFont, commandQueue, context)

    init {
        title = defaultTitle
        setBounds(300, 90, 350, 600)
        defaultCloseOperation = EXIT_ON_CLOSE
        isResizable = true

        layout = BorderLayout()
        add(buttonPanel, BorderLayout.NORTH)
        add(assetListPanel, BorderLayout.CENTER)
        add(coordinatesPanel, BorderLayout.SOUTH)

        isVisible = true
    }

    fun addToAssetList(sceneNode: SceneNode) {
        assetListPanel.add(sceneNode)
    }

    fun removeFromAssetList(id: String) {
        assetListPanel.remove(id)
    }

    fun show(sceneNode: SceneNode, showInList: Boolean = true) {
        context.selectedAssetNode = sceneNode.assetNode
        title = sceneNode.assetNode.fileName
        buttonPanel.enableFocus()
        if (showInList) {
            assetListPanel.show(sceneNode)
        }
        coordinatesPanel.show(sceneNode.node)
        context.assetUpdateEnabled = true
    }

    fun disableFocus() {
        context.selectedAssetNode = null
        context.assetUpdateEnabled = false
        buttonPanel.disableFocus()
        assetListPanel.disableFocus()
        coordinatesPanel.disableFocus()
        title = defaultTitle
    }

    private companion object {

        const val defaultTitle = "Asset"

    }

}
