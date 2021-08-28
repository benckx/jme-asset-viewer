package be.encelade.viewer.gui

import be.encelade.viewer.commands.CommandQueue
import be.encelade.viewer.scene.SceneNode
import be.encelade.viewer.utils.LazyLogging
import be.encelade.viewer.utils.PropertiesFile
import java.awt.BorderLayout
import java.awt.Point
import javax.swing.JFrame

class AssetMenu(properties: PropertiesFile, commandQueue: CommandQueue, jmeLocation: Point) : JFrame(), LazyLogging {

    private val context = GuiContext(properties)
    private val buttonPanel = AssetButtonPanel(commandQueue, context, this)
    private val assetListPanel = AssetListPanel(commandQueue, this)
    private val coordinatesPanel = AssetCoordinatesPanel(commandQueue, context)

    init {
        title = defaultTitle
        isResizable = true

        val height = 800
        val width = 290
        val margin = 15
        val x = jmeLocation.x - width - margin
        val y = jmeLocation.y - 60
        setBounds(x, y, width, height)

        layout = BorderLayout()
        add(buttonPanel, BorderLayout.NORTH)
        add(assetListPanel, BorderLayout.CENTER)
        add(coordinatesPanel, BorderLayout.SOUTH)

        defaultCloseOperation = EXIT_ON_CLOSE

        logger.debug("actual width: ${1280 + (width + margin) * 2}")
    }

    fun addToAssetList(sceneNode: SceneNode) {
        assetListPanel.add(sceneNode)
    }

    fun removeFromAssetList(id: String) {
        assetListPanel.remove(id)
    }

    fun show(sceneNode: SceneNode, showInList: Boolean = true) {
        context.assetUpdateEnabled = false
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
