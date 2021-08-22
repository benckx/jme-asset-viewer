package be.encelade.viewer.frames

import be.encelade.viewer.managers.SceneManager
import java.awt.BorderLayout
import java.awt.Font
import java.awt.GridLayout
import javax.swing.*
import javax.swing.JFileChooser.APPROVE_OPTION

class AssetMenu(private val sceneManager: SceneManager) : JFrame() {

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
        val xPosField = JTextField("0")
        val yPosLabel = JLabel("y:")
        val yPosField = JTextField("0")
        val zPosLabel = JLabel("z:")
        val zPosField = JTextField("0")

        southPanel.add(xPosLabel)
        southPanel.add(xPosField)
        southPanel.add(yPosLabel)
        southPanel.add(yPosField)
        southPanel.add(zPosLabel)
        southPanel.add(zPosField)
        southPanel.components.forEach { it.font = font }

        isVisible = true

        importButton.addActionListener { e ->
            val fileChooser = JFileChooser()
            fileChooser.showOpenDialog(this)
            val returnValue = fileChooser.showOpenDialog(importButton)
            if (returnValue == APPROVE_OPTION) {
                println(fileChooser.selectedFile)
            }
        }
    }

}
