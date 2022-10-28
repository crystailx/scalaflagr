package io.github.crystailx.scalaflagr.json

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.{ JsonGenerator, JsonParser, Version }
import com.fasterxml.jackson.databind._
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.scala.{ ClassTagExtensions, DefaultScalaModule }
import io.github.crystailx.scalaflagr.data.RawValue

import scala.jdk.CollectionConverters.{ MapHasAsJava, SeqHasAsJava }
package object jackson extends EncoderAdapter with DecoderAdapter {

  private[jackson] class ByteArrayDeserializer
      extends StdDeserializer[RawValue](classOf[RawValue]) {

    override def deserialize(p: JsonParser, ctxt: DeserializationContext): RawValue =
      p.getCodec.readTree[JsonNode](p).toString.getBytes
  }

  private[jackson] class ByteArraySerializer extends StdSerializer[RawValue](classOf[RawValue]) {

    override def serialize(
      value: RawValue,
      gen: JsonGenerator,
      provider: SerializerProvider
    ): Unit = gen.writeRaw(new String(value))
  }

  object ByteArrayModule
      extends SimpleModule(
        "ByteArrayModule",
        Version.unknownVersion(),
        Map[Class[_], JsonDeserializer[_]](
          classOf[RawValue] -> new ByteArrayDeserializer
        ).asJava,
        List[JsonSerializer[_]](new ByteArraySerializer).asJava
      )

  lazy val mapper = new ObjectMapper() with ClassTagExtensions
  mapper.registerModules(DefaultScalaModule, new JavaTimeModule(), ByteArrayModule)
  mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
  mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
  mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
  mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
  mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)

}
