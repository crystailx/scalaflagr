package crystailx.scalaflagr.json.jackson

import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.{ JsonDeserializer, JsonSerializer }
import com.fasterxml.jackson.databind.module.SimpleModule
import crystailx.scalaflagr.data.RawValue

import scala.collection.JavaConverters.{ mapAsJavaMapConverter, seqAsJavaListConverter }

object ByteArrayModule
    extends SimpleModule(
      "ByteArrayModule",
      Version.unknownVersion(),
      Map[Class[_], JsonDeserializer[_]](
        classOf[RawValue] -> new ByteArrayDeserializer
      ).asJava,
      List[JsonSerializer[_]](new ByteArraySerializer).asJava
    )
