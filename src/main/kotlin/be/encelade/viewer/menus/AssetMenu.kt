package be.encelade.viewer.menus

import be.encelade.viewer.managers.SceneManager
import be.encelade.viewer.scene.AssetNode
import java.awt.BorderLayout
import java.awt.Font
import java.awt.GridLayout
import java.io.File
import javax.swing.*
import javax.swing.JFileChooser.APPROVE_OPTION

class AssetMenu(private val sceneManager: SceneManager) : JFrame() {

    private var lastFolder: String? = null

    private val xPosField = JTextField("0")
    private val yPosField = JTextField("0")
    private val zPosField = JTextField("0")

    init {
        title = "Asset"
        setBounds(300, 90, 200, 150)
        defaultCloseOperation = EXIT_ON_CLOSE
        isResizable = false

        val font = Font("Arial", Font.PLAIN, 20)

        val northPanel = JPanel()
        northPanel.layout = GridLayout(0, 1)

        val southPanel = JPanel()
        southPanel.layout = GridLayout(0, 2)

        val panel = JPanel()
        panel.layout = BorderLayout()
        panel.add(northPanel, BorderLayout.NORTH)
        panel.add(southPanel, BorderLayout.SOUTH)
        add(panel)

        val importButton = JButton("Import")
        northPanel.add(importButton)
        northPanel.components.forEach { it.font = font }

        val xPosLabel = JLabel("x:")
        val yPosLabel = JLabel("y:")
        val zPosLabel = JLabel("z:")

        southPanel.add(xPosLabel)
        southPanel.add(xPosField)
        southPanel.add(yPosLabel)
        southPanel.add(yPosField)
        southPanel.add(zPosLabel)
        southPanel.add(zPosField)
        southPanel.components.forEach { it.font = font }

        isVisible = true

        importButton.addActionListener {
            val fileChooser = JFileChooser()
            lastFolder?.let { folder -> fileChooser.currentDirectory = File(folder) }
            val returnValue = fileChooser.showOpenDialog(importButton)
            if (returnValue == APPROVE_OPTION) {
                val file = fileChooser.selectedFile
                lastFolder = file.path.split(File.separator).dropLast(1).joinToString(separator = File.separator)
                sceneManager.importAsset(file)
            }
        }
    }

    fun loadInGui(assetNode: AssetNode) {
        title = assetNode.fileName

        val localTranslation = assetNode.worldTranslation()
        xPosField.text = localTranslation.x.toString()
        yPosField.text = localTranslation.y.toString()
        zPosField.text = localTranslation.z.toString()
    }

}
