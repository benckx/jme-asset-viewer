package be.encelade.viewer

import be.encelade.viewer.commands.CommandQueue
import be.encelade.viewer.gui.AssetMenu
import be.encelade.viewer.utils.PropertiesFile
import java.awt.Point
import javax.swing.UIManager

/**
 * Run the GUI only.
 * For test and development purpose.
 */
fun main() {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())

    val propertiesFile = PropertiesFile(PROPERTIES_FILE)
    val commandQueue = CommandQueue()
    AssetMenu(propertiesFile, commandQueue, Point(500, 500))
}
