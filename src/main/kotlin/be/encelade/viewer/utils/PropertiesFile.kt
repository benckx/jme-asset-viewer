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

    fun getProperty(key: PropertiesKey): String? {
        return props.getProperty(key.name)
    }

    fun persistProperty(key: PropertiesKey, value: String) {
        props.setProperty(key.name, value)
        props.store(FileOutputStream(file), null)
        logger.debug("persisted $key -> $value")
    }

}
