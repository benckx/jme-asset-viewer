package be.encelade.viewer.persistence

import com.jme3.math.Quaternion
import com.jme3.math.Vector3f

data class SceneNodeDto(val id: String,
                        val fileName: String,
                        val translation: Vector3f,
                        val rotation: Quaternion,
                        val scale: Vector3f)
