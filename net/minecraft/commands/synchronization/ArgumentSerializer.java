package net.minecraft.commands.synchronization;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;

public interface ArgumentSerializer<T extends com.mojang.brigadier.arguments.ArgumentType<?>> {
  void serializeToNetwork(T paramT, FriendlyByteBuf paramFriendlyByteBuf);
  
  T deserializeFromNetwork(FriendlyByteBuf paramFriendlyByteBuf);
  
  void serializeToJson(T paramT, JsonObject paramJsonObject);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\synchronization\ArgumentSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */