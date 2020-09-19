/*    */ package net.minecraft.advancements.critereon;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonNull;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonPrimitive;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.projectile.FishingHook;
/*    */ 
/*    */ public class FishingHookPredicate
/*    */ {
/* 14 */   public static final FishingHookPredicate ANY = new FishingHookPredicate(false);
/*    */   
/*    */   private boolean inOpenWater;
/*    */ 
/*    */   
/*    */   private FishingHookPredicate(boolean debug1) {
/* 20 */     this.inOpenWater = debug1;
/*    */   }
/*    */   
/*    */   public static FishingHookPredicate inOpenWater(boolean debug0) {
/* 24 */     return new FishingHookPredicate(debug0);
/*    */   }
/*    */   
/*    */   public static FishingHookPredicate fromJson(@Nullable JsonElement debug0) {
/* 28 */     if (debug0 == null || debug0.isJsonNull()) {
/* 29 */       return ANY;
/*    */     }
/*    */     
/* 32 */     JsonObject debug1 = GsonHelper.convertToJsonObject(debug0, "fishing_hook");
/* 33 */     JsonElement debug2 = debug1.get("in_open_water");
/* 34 */     if (debug2 != null) {
/* 35 */       return new FishingHookPredicate(GsonHelper.convertToBoolean(debug2, "in_open_water"));
/*    */     }
/* 37 */     return ANY;
/*    */   }
/*    */   
/*    */   public JsonElement serializeToJson() {
/* 41 */     if (this == ANY) {
/* 42 */       return (JsonElement)JsonNull.INSTANCE;
/*    */     }
/*    */     
/* 45 */     JsonObject debug1 = new JsonObject();
/* 46 */     debug1.add("in_open_water", (JsonElement)new JsonPrimitive(Boolean.valueOf(this.inOpenWater)));
/* 47 */     return (JsonElement)debug1;
/*    */   }
/*    */   
/*    */   public boolean matches(Entity debug1) {
/* 51 */     if (this == ANY) {
/* 52 */       return true;
/*    */     }
/*    */     
/* 55 */     if (!(debug1 instanceof FishingHook)) {
/* 56 */       return false;
/*    */     }
/*    */     
/* 59 */     FishingHook debug2 = (FishingHook)debug1;
/* 60 */     return (this.inOpenWater == debug2.isOpenWaterFishing());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\FishingHookPredicate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */