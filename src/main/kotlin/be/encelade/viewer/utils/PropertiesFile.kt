package be.encelade.viewer.utils

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

class PropertiesFile(fileName: String) : LazyLogging {

    private val props = Properties()
    private val file = File(fileName)

    init {
        logger.debug("file: $fileName")
        logger.debug("path: ${file.absolutePath}")

        if (!file.exists()) {
            file.createNewFile()
        } else {
            props.load(FileInputStream(file))
            props.forEach { (key, value) -> logger.debug("$key = $value") }
        }
    }

    fun getProperty(key: String): String? {
        return props.getProperty(key)
    }

    fun persistProperty(key: String, value: String) {
        props.setProperty(key, value)
        props.store(FileOutputStream(file), null)
    }

    companion object {

        const val DEFAULT_FOLDER_KEY = "DEFAULT_FOLDER"

    }

}
