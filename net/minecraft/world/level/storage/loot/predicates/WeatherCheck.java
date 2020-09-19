/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public class WeatherCheck
/*    */   implements LootItemCondition
/*    */ {
/*    */   @Nullable
/*    */   private final Boolean isRaining;
/*    */   @Nullable
/*    */   private final Boolean isThundering;
/*    */   
/*    */   private WeatherCheck(@Nullable Boolean debug1, @Nullable Boolean debug2) {
/* 20 */     this.isRaining = debug1;
/* 21 */     this.isThundering = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemConditionType getType() {
/* 26 */     return LootItemConditions.WEATHER_CHECK;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(LootContext debug1) {
/* 31 */     ServerLevel debug2 = debug1.getLevel();
/*    */     
/* 33 */     if (this.isRaining != null && this.isRaining.booleanValue() != debug2.isRaining()) {
/* 34 */       return false;
/*    */     }
/*    */     
/* 37 */     if (this.isThundering != null && this.isThundering.booleanValue() != debug2.isThundering()) {
/* 38 */       return false;
/*    */     }
/*    */     
/* 41 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static class Serializer
/*    */     implements net.minecraft.world.level.storage.loot.Serializer<WeatherCheck>
/*    */   {
/*    */     public void serialize(JsonObject debug1, WeatherCheck debug2, JsonSerializationContext debug3) {
/* 74 */       debug1.addProperty("raining", debug2.isRaining);
/* 75 */       debug1.addProperty("thundering", debug2.isThundering);
/*    */     }
/*    */ 
/*    */     
/*    */     public WeatherCheck deserialize(JsonObject debug1, JsonDeserializationContext debug2) {
/* 80 */       Boolean debug3 = debug1.has("raining") ? Boolean.valueOf(GsonHelper.getAsBoolean(debug1, "raining")) : null;
/* 81 */       Boolean debug4 = debug1.has("thundering") ? Boolean.valueOf(GsonHelper.getAsBoolean(debug1, "thundering")) : null;
/* 82 */       return new WeatherCheck(debug3, debug4);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\WeatherCheck.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */