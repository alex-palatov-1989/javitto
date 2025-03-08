package com.solar.academy.handlers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.json.JSONObject;

import java.io.IOException;

public class CommentSerializer {
    static public class Serializer extends JsonSerializer<Comment> {
        @Override
        public void serialize(Comment value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeRawValue(value.toJSON().toString()); // Use the custom toJson method
        }
    }

    static public class Deserializer extends JsonDeserializer<Comment> {
        @Override
        public Comment deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String json = p.readValueAs(String.class);
            var val = new Comment();
            val.fromJSON( new JSONObject(json), Comment.class ); // Use the custom fromJson method
            return val;
        }
    }
}
