package be.encelade.viewer.utils

import java.io.File

object FileUtils {

    fun containingFolder(file : File) : String {
        val splitPath = file.path.split(File.separator)
        return splitPath.dropLast(1).joinToString(separator = File.separator)
    }

}