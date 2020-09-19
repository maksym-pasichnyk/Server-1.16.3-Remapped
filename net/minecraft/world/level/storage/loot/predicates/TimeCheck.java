/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.RandomValueBounds;
/*    */ 
/*    */ 
/*    */ public class TimeCheck
/*    */   implements LootItemCondition
/*    */ {
/*    */   @Nullable
/*    */   private final Long period;
/*    */   private final RandomValueBounds value;
/*    */   
/*    */   private TimeCheck(@Nullable Long debug1, RandomValueBounds debug2) {
/* 21 */     this.period = debug1;
/* 22 */     this.value = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemConditionType getType() {
/* 27 */     return LootItemConditions.TIME_CHECK;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(LootContext debug1) {
/* 32 */     ServerLevel debug2 = debug1.getLevel();
/*    */     
/* 34 */     long debug3 = debug2.getDayTime();
/*    */     
/* 36 */     if (this.period != null) {
/* 37 */       debug3 %= this.period.longValue();
/*    */     }
/*    */     
/* 40 */     return this.value.matchesValue((int)debug3);
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
/*    */   public static class Serializer
/*    */     implements net.minecraft.world.level.storage.loot.Serializer<TimeCheck>
/*    */   {
/*    */     public void serialize(JsonObject debug1, TimeCheck debug2, JsonSerializationContext debug3) {
/* 71 */       debug1.addProperty("period", debug2.period);
/* 72 */       debug1.add("value", debug3.serialize(debug2.value));
/*    */     }
/*    */ 
/*    */     
/*    */     public TimeCheck deserialize(JsonObject debug1, JsonDeserializationContext debug2) {
/* 77 */       Long debug3 = debug1.has("period") ? Long.valueOf(GsonHelper.getAsLong(debug1, "period")) : null;
/* 78 */       RandomValueBounds debug4 = (RandomValueBounds)GsonHelper.getAsObject(debug1, "value", debug2, RandomValueBounds.class);
/* 79 */       return new TimeCheck(debug3, debug4);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\TimeCheck.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */