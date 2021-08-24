package be.encelade.viewer.scene

import java.io.File

/**
 * Reference to the imported asset in the scene.
 *
 * @param id is used to identified which asset has been selected by clicking in the scene,
 *        it will be used as the name of all [com.jme3.scene.Spatial] of the import asset.
 */
data class AssetNode(val id: String, val file: File) {

    val fileName = file.path.split(File.separator).last()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AssetNode

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

}
