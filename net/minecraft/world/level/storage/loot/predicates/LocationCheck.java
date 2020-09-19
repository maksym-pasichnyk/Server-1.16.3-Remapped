/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import net.minecraft.advancements.critereon.LocationPredicate;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class LocationCheck
/*    */   implements LootItemCondition {
/*    */   private final LocationPredicate predicate;
/*    */   private final BlockPos offset;
/*    */   
/*    */   private LocationCheck(LocationPredicate debug1, BlockPos debug2) {
/* 19 */     this.predicate = debug1;
/* 20 */     this.offset = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemConditionType getType() {
/* 25 */     return LootItemConditions.LOCATION_CHECK;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(LootContext debug1) {
/* 30 */     Vec3 debug2 = (Vec3)debug1.getParamOrNull(LootContextParams.ORIGIN);
/* 31 */     return (debug2 != null && this.predicate.matches(debug1.getLevel(), debug2.x() + this.offset.getX(), debug2.y() + this.offset.getY(), debug2.z() + this.offset.getZ()));
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder checkLocation(LocationPredicate.Builder debug0) {
/* 35 */     return () -> new LocationCheck(debug0.build(), BlockPos.ZERO);
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder checkLocation(LocationPredicate.Builder debug0, BlockPos debug1) {
/* 39 */     return () -> new LocationCheck(debug0.build(), debug1);
/*    */   }
/*    */   
/*    */   public static class Serializer
/*    */     implements net.minecraft.world.level.storage.loot.Serializer<LocationCheck> {
/*    */     public void serialize(JsonObject debug1, LocationCheck debug2, JsonSerializationContext debug3) {
/* 45 */       debug1.add("predicate", debug2.predicate.serializeToJson());
/* 46 */       if (debug2.offset.getX() != 0) {
/* 47 */         debug1.addProperty("offsetX", Integer.valueOf(debug2.offset.getX()));
/*    */       }
/* 49 */       if (debug2.offset.getY() != 0) {
/* 50 */         debug1.addProperty("offsetY", Integer.valueOf(debug2.offset.getY()));
/*    */       }
/* 52 */       if (debug2.offset.getZ() != 0) {
/* 53 */         debug1.addProperty("offsetZ", Integer.valueOf(debug2.offset.getZ()));
/*    */       }
/*    */     }
/*    */ 
/*    */     
/*    */     public LocationCheck deserialize(JsonObject debug1, JsonDeserializationContext debug2) {
/* 59 */       LocationPredicate debug3 = LocationPredicate.fromJson(debug1.get("predicate"));
/* 60 */       int debug4 = GsonHelper.getAsInt(debug1, "offsetX", 0);
/* 61 */       int debug5 = GsonHelper.getAsInt(debug1, "offsetY", 0);
/* 62 */       int debug6 = GsonHelper.getAsInt(debug1, "offsetZ", 0);
/* 63 */       return new LocationCheck(debug3, new BlockPos(debug4, debug5, debug6));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\LocationCheck.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */