package net.minecraft.world.level.storage.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public interface Serializer<T> {
  void serialize(JsonObject paramJsonObject, T paramT, JsonSerializationContext paramJsonSerializationContext);
  
  T deserialize(JsonObject paramJsonObject, JsonDeserializationContext paramJsonDeserializationContext);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\Serializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */