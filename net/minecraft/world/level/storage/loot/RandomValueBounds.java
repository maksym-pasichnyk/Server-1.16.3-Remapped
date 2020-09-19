/*    */ package net.minecraft.world.level.storage.loot;
/*    */ 
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonDeserializer;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonParseException;
/*    */ import com.google.gson.JsonPrimitive;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import com.google.gson.JsonSerializer;
/*    */ import java.lang.reflect.Type;
/*    */ import java.util.Random;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class RandomValueBounds
/*    */   implements RandomIntGenerator {
/*    */   private final float min;
/*    */   private final float max;
/*    */   
/*    */   public RandomValueBounds(float debug1, float debug2) {
/* 23 */     this.min = debug1;
/* 24 */     this.max = debug2;
/*    */   }
/*    */   
/*    */   public RandomValueBounds(float debug1) {
/* 28 */     this.min = debug1;
/* 29 */     this.max = debug1;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static RandomValueBounds between(float debug0, float debug1) {
/* 37 */     return new RandomValueBounds(debug0, debug1);
/*    */   }
/*    */   
/*    */   public float getMin() {
/* 41 */     return this.min;
/*    */   }
/*    */   
/*    */   public float getMax() {
/* 45 */     return this.max;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getInt(Random debug1) {
/* 50 */     return Mth.nextInt(debug1, Mth.floor(this.min), Mth.floor(this.max));
/*    */   }
/*    */   
/*    */   public float getFloat(Random debug1) {
/* 54 */     return Mth.nextFloat(debug1, this.min, this.max);
/*    */   }
/*    */   
/*    */   public boolean matchesValue(int debug1) {
/* 58 */     return (debug1 <= this.max && debug1 >= this.min);
/*    */   }
/*    */ 
/*    */   
/*    */   public ResourceLocation getType() {
/* 63 */     return UNIFORM;
/*    */   }
/*    */   
/*    */   public static class Serializer
/*    */     implements JsonDeserializer<RandomValueBounds>, JsonSerializer<RandomValueBounds> {
/*    */     public RandomValueBounds deserialize(JsonElement debug1, Type debug2, JsonDeserializationContext debug3) throws JsonParseException {
/* 69 */       if (GsonHelper.isNumberValue(debug1)) {
/* 70 */         return new RandomValueBounds(GsonHelper.convertToFloat(debug1, "value"));
/*    */       }
/* 72 */       JsonObject debug4 = GsonHelper.convertToJsonObject(debug1, "value");
/* 73 */       float debug5 = GsonHelper.getAsFloat(debug4, "min");
/* 74 */       float debug6 = GsonHelper.getAsFloat(debug4, "max");
/* 75 */       return new RandomValueBounds(debug5, debug6);
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public JsonElement serialize(RandomValueBounds debug1, Type debug2, JsonSerializationContext debug3) {
/* 81 */       if (debug1.min == debug1.max) {
/* 82 */         return (JsonElement)new JsonPrimitive(Float.valueOf(debug1.min));
/*    */       }
/* 84 */       JsonObject debug4 = new JsonObject();
/* 85 */       debug4.addProperty("min", Float.valueOf(debug1.min));
/* 86 */       debug4.addProperty("max", Float.valueOf(debug1.max));
/* 87 */       return (JsonElement)debug4;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\RandomValueBounds.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */