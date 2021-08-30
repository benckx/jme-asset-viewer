package be.encelade.viewer.scene

import be.encelade.chimp.material.UnshadedMaterial
import be.encelade.viewer.utils.GeometryUtils.extractBoundingVolume
import be.encelade.viewer.utils.LazyLogging
import com.jme3.app.SimpleApplication
import com.jme3.bounding.BoundingBox
import com.jme3.scene.Geometry
import com.jme3.scene.Node
import com.jme3.scene.shape.Box
import java.awt.Color.GREEN

/**
 * Draw the wireframe [BoundingBox] around select assets, as to show which asset is selected.
 */
class BoundingBoxManager(app: SimpleApplication) : LazyLogging {

    private val rootNode by lazy { app.rootNode }
    private var currentSceneNode: SceneNode? = null

    fun currentSceneNode(): SceneNode? {
        return currentSceneNode
    }

    fun reDrawBoundingBox(node: Node) {
        deleteBoundingBox(clearState = false)
        drawBoundingBox(node)
    }

    fun deleteBoundingBox(clearState: Boolean = true) {
        rootNode.detachChildNamed(SELECTED_ASSET)
        if (clearState) {
            currentSceneNode = null
        }
    }

    fun drawBoundingBox(sceneNode: SceneNode): Geometry? {
        this.currentSceneNode = sceneNode
        return drawBoundingBox(sceneNode.node)
    }

    private fun drawBoundingBox(node: Node): Geometry? {
        deleteBoundingBox(clearState = false)

        val volume = extractBoundingVolume(node)
        if (volume is BoundingBox) {
            val material = UnshadedMaterial()
            material.setColor(GREEN)
            material.additionalRenderState.isWireframe = true

            val box = Geometry(SELECTED_ASSET, Box(volume.xExtent, volume.yExtent, volume.zExtent))
            box.material = material

            box.localTranslation = node.localTranslation
            box.localRotation = node.localRotation
            box.localScale = node.localScale
            box.move(volume.center)
            rootNode.attachChild(box)

            return box
        }

        return null
    }

    private companion object {

        const val SELECTED_ASSET = "SELECTED_ASSET"

    }

}
