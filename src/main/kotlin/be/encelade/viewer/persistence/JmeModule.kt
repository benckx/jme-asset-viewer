package be.encelade.viewer.persistence

import com.fasterxml.jackson.databind.module.SimpleModule

class JmeModule : SimpleModule() {

    init {
        addSerializer(Vector3Serializer())
        addSerializer(QuaternionSerializer())
    }

}
