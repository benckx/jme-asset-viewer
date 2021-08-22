package be.encelade.viewer

import java.awt.Font
import java.awt.GridLayout
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JTextField

class AssetFrame : JFrame() {

    init {
        title = "Asset"
        setBounds(300, 90, 200, 120)
        defaultCloseOperation = EXIT_ON_CLOSE
        isResizable = false

        contentPane.layout = GridLayout(0, 2)
        val font = Font("Arial", Font.PLAIN, 20)

        val xPosLabel = JLabel("x:")
        val xPosField = JTextField("0")
        val yPosLabel = JLabel("y:")
        val yPosField = JTextField("0")
        val zPosLabel = JLabel("z:")
        val zPosField = JTextField("0")

        contentPane.add(xPosLabel)
        contentPane.add(xPosField)
        contentPane.add(yPosLabel)
        contentPane.add(yPosField)
        contentPane.add(zPosLabel)
        contentPane.add(zPosField)

        contentPane.components.forEach { it.font = font }
    }

}
