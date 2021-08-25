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

    private var selectedAssetNode: AssetNode? = null
    private var assetUpdateEnabled = false

    // TODO: replace parent by "context"?
    private val buttonPanel = AssetButtonPanel(guiFont, commandQueue, propertiesFile, this)
    private val coordinatesPanel = AssetCoordinatesPanel(guiFont, commandQueue, this)

    init {
        title = defaultTitle
        setBounds(300, 90, 250, 500)
        defaultCloseOperation = EXIT_ON_CLOSE
        isResizable = true

        layout = BorderLayout()
        add(buttonPanel, BorderLayout.NORTH)
        add(coordinatesPanel, BorderLayout.SOUTH)

        isVisible = true
    }

    fun selectedAssetNode(): AssetNode? {
        return if (assetUpdateEnabled) {
            selectedAssetNode
        } else {
            null
        }
    }

    fun showInForm(sceneNode: SceneNode) {
        showInForm(sceneNode.assetNode, sceneNode.node)
    }

    private fun showInForm(assetNode: AssetNode, node: Node) {
        this.selectedAssetNode = assetNode
        this.title = assetNode.fileName
        buttonPanel.enableFocus()
        coordinatesPanel.show(node)
        assetUpdateEnabled = true
    }

    fun disableFocus() {
        this.selectedAssetNode = null
        this.assetUpdateEnabled = false
        buttonPanel.disableFocus()
        coordinatesPanel.disableFocus()
        title = defaultTitle
    }

    private companion object {

        const val defaultTitle = "Asset"

    }

}
