package be.encelade.viewer.scene

import be.encelade.chimp.material.UnshadedMaterial
import be.encelade.chimp.utils.ColorHelperUtils.ColorRGBA
import be.encelade.chimp.utils.NodeHelperUtils.attachChildren
import com.jme3.math.ColorRGBA.Blue
import com.jme3.math.FastMath.HALF_PI
import com.jme3.renderer.queue.RenderQueue
import com.jme3.scene.Geometry
import com.jme3.scene.Node
import com.jme3.scene.debug.Grid
import com.jme3.scene.shape.Box

class DecorNode : Node("DECOR") {

    init {
        attachChildren(makeFloor(), makeGrid())
    }

    private companion object {

        const val sizeX = 24
        const val sizeY = 18

        fun makeFloor(): Geometry {
            val floorMat = UnshadedMaterial()
            floorMat.setColor(ColorRGBA(155, 164, 193))

            val floor = Geometry("FLOOR", Box(sizeX / 2f, sizeY / 2f, 0f))
            floor.material = floorMat
            floor.shadowMode = RenderQueue.ShadowMode.Receive

            return floor
        }

        fun makeGrid(): Geometry {
            val gridMat = UnshadedMaterial()
            gridMat.setColor(Blue)

            val grid = Geometry("GRID", Grid(sizeX + 1, sizeY + 1, 1f))
            grid.material = gridMat
            grid.rotate(HALF_PI, 0f, HALF_PI)
            grid.move(-(sizeX / 2f), -(sizeY / 2f), 0.01f)

            return grid
        }

    }

}
