/*    */ package net.minecraft.advancements.critereon;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonNull;
/*    */ import com.google.gson.JsonObject;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ 
/*    */ public class LightPredicate
/*    */ {
/* 13 */   public static final LightPredicate ANY = new LightPredicate(MinMaxBounds.Ints.ANY);
/*    */   
/*    */   private final MinMaxBounds.Ints composite;
/*    */   
/*    */   private LightPredicate(MinMaxBounds.Ints debug1) {
/* 18 */     this.composite = debug1;
/*    */   }
/*    */   
/*    */   public boolean matches(ServerLevel debug1, BlockPos debug2) {
/* 22 */     if (this == ANY) {
/* 23 */       return true;
/*    */     }
/* 25 */     if (!debug1.isLoaded(debug2)) {
/* 26 */       return false;
/*    */     }
/* 28 */     if (!this.composite.matches(debug1.getMaxLocalRawBrightness(debug2))) {
/* 29 */       return false;
/*    */     }
/* 31 */     return true;
/*    */   }
/*    */   
/*    */   public JsonElement serializeToJson() {
/* 35 */     if (this == ANY) {
/* 36 */       return (JsonElement)JsonNull.INSTANCE;
/*    */     }
/*    */     
/* 39 */     JsonObject debug1 = new JsonObject();
/* 40 */     debug1.add("light", this.composite.serializeToJson());
/* 41 */     return (JsonElement)debug1;
/*    */   }
/*    */   
/*    */   public static LightPredicate fromJson(@Nullable JsonElement debug0) {
/* 45 */     if (debug0 == null || debug0.isJsonNull()) {
/* 46 */       return ANY;
/*    */     }
/* 48 */     JsonObject debug1 = GsonHelper.convertToJsonObject(debug0, "light");
/* 49 */     MinMaxBounds.Ints debug2 = MinMaxBounds.Ints.fromJson(debug1.get("light"));
/* 50 */     return new LightPredicate(debug2);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\LightPredicate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */