package be.encelade.viewer.scene

import com.jme3.math.Vector3f
import com.jme3.scene.Node
import com.jme3.scene.Spatial

data class AssetNode(val id: String,
                     val fileName: String,
                     val node: Node) {

    fun spatial(): Spatial {
        return node.children.first()
    }

    fun worldTranslation(): Vector3f {
        return spatial().worldTranslation
    }

}
