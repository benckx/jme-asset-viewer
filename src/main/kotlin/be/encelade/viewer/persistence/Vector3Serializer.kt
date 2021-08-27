package be.encelade.viewer.persistence

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.jme3.math.Vector3f

class Vector3Serializer : StdSerializer<Vector3f>(Vector3f::class.java) {

    override fun serialize(value: Vector3f?, gen: JsonGenerator?, provider: SerializerProvider?) {
        if (value != null && gen != null) {
            gen.writeStartObject()
            gen.writeNumberField("x", value.x)
            gen.writeNumberField("y", value.y)
            gen.writeNumberField("z", value.z)
            gen.writeEndObject()
        }
    }

}
