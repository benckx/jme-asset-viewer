package be.encelade.viewer.persistence

import com.fasterxml.jackson.databind.module.SimpleModule

/**
 * Serialize JME objects to JSON:
 *
 * - [import com.jme3.math.Vector3f]
 * - [import com.jme3.math.Quaternion]
 */
class JmeModule : SimpleModule() {

    init {
        addSerializer(Vector3Serializer())
        addSerializer(QuaternionSerializer())
    }

}
