package net.minecraft.advancements;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.resources.ResourceLocation;

public interface CriterionTriggerInstance {
  ResourceLocation getCriterion();
  
  JsonObject serializeToJson(SerializationContext paramSerializationContext);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\CriterionTriggerInstance.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */