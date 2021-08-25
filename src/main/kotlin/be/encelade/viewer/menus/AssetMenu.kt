package be.encelade.viewer.menus

import be.encelade.viewer.commands.CommandQueue
import be.encelade.viewer.scene.AssetNode
import be.encelade.viewer.scene.SceneNode
import be.encelade.viewer.utils.LazyLogging
import be.encelade.viewer.utils.PropertiesFile
import com.jme3.scene.Node
import java.awt.BorderLayout
import java.awt.Font
import javax.swing.JFrame

class AssetMenu(propertiesFile: PropertiesFile, commandQueue: CommandQueue) : JFrame(), LazyLogging {

    private val guiFont = Font("Arial", Font.PLAIN, 17)

    private val context = GuiContext(propertiesFile)
    private val buttonPanel = AssetButtonPanel(guiFont, commandQueue, context, this)
    private val coordinatesPanel = AssetCoordinatesPanel(guiFont, commandQueue, context)

    init {
        title = defaultTitle
        setBounds(300, 90, 340, 500)
        defaultCloseOperation = EXIT_ON_CLOSE
        isResizable = true

        layout = BorderLayout()
        add(buttonPanel, BorderLayout.NORTH)
        add(coordinatesPanel, BorderLayout.SOUTH)

        isVisible = true
    }

    fun show(sceneNode: SceneNode) {
        show(sceneNode.assetNode, sceneNode.node)
    }

    private fun show(assetNode: AssetNode, node: Node) {
        context.selectedAssetNode = assetNode
        this.title = assetNode.fileName
        buttonPanel.enableFocus()
        coordinatesPanel.show(node)
        context.assetUpdateEnabled = true
    }

    fun disableFocus() {
        context.selectedAssetNode = null
        context.assetUpdateEnabled = false
        buttonPanel.disableFocus()
        coordinatesPanel.disableFocus()
        title = defaultTitle
    }

    private companion object {

        const val defaultTitle = "Asset"

    }

}
