/*    */ package net.minecraft.world.level.storage.loot;
/*    */ 
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonDeserializer;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonParseException;
/*    */ import com.google.gson.JsonPrimitive;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import com.google.gson.JsonSerializer;
/*    */ import java.lang.reflect.Type;
/*    */ import java.util.Random;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ 
/*    */ public final class ConstantIntValue
/*    */   implements RandomIntGenerator {
/*    */   private final int value;
/*    */   
/*    */   public ConstantIntValue(int debug1) {
/* 20 */     this.value = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getInt(Random debug1) {
/* 25 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public ResourceLocation getType() {
/* 30 */     return CONSTANT;
/*    */   }
/*    */   
/*    */   public static ConstantIntValue exactly(int debug0) {
/* 34 */     return new ConstantIntValue(debug0);
/*    */   }
/*    */   
/*    */   public static class Serializer
/*    */     implements JsonDeserializer<ConstantIntValue>, JsonSerializer<ConstantIntValue> {
/*    */     public ConstantIntValue deserialize(JsonElement debug1, Type debug2, JsonDeserializationContext debug3) throws JsonParseException {
/* 40 */       return new ConstantIntValue(GsonHelper.convertToInt(debug1, "value"));
/*    */     }
/*    */ 
/*    */     
/*    */     public JsonElement serialize(ConstantIntValue debug1, Type debug2, JsonSerializationContext debug3) {
/* 45 */       return (JsonElement)new JsonPrimitive(Integer.valueOf(debug1.value));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\ConstantIntValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */