/*    */ package net.minecraft.advancements.critereon;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonNull;
/*    */ import com.google.gson.JsonObject;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class DistancePredicate
/*    */ {
/* 12 */   public static final DistancePredicate ANY = new DistancePredicate(MinMaxBounds.Floats.ANY, MinMaxBounds.Floats.ANY, MinMaxBounds.Floats.ANY, MinMaxBounds.Floats.ANY, MinMaxBounds.Floats.ANY);
/*    */   
/*    */   private final MinMaxBounds.Floats x;
/*    */   private final MinMaxBounds.Floats y;
/*    */   private final MinMaxBounds.Floats z;
/*    */   private final MinMaxBounds.Floats horizontal;
/*    */   private final MinMaxBounds.Floats absolute;
/*    */   
/*    */   public DistancePredicate(MinMaxBounds.Floats debug1, MinMaxBounds.Floats debug2, MinMaxBounds.Floats debug3, MinMaxBounds.Floats debug4, MinMaxBounds.Floats debug5) {
/* 21 */     this.x = debug1;
/* 22 */     this.y = debug2;
/* 23 */     this.z = debug3;
/* 24 */     this.horizontal = debug4;
/* 25 */     this.absolute = debug5;
/*    */   }
/*    */   
/*    */   public static DistancePredicate horizontal(MinMaxBounds.Floats debug0) {
/* 29 */     return new DistancePredicate(MinMaxBounds.Floats.ANY, MinMaxBounds.Floats.ANY, MinMaxBounds.Floats.ANY, debug0, MinMaxBounds.Floats.ANY);
/*    */   }
/*    */   
/*    */   public static DistancePredicate vertical(MinMaxBounds.Floats debug0) {
/* 33 */     return new DistancePredicate(MinMaxBounds.Floats.ANY, debug0, MinMaxBounds.Floats.ANY, MinMaxBounds.Floats.ANY, MinMaxBounds.Floats.ANY);
/*    */   }
/*    */   
/*    */   public boolean matches(double debug1, double debug3, double debug5, double debug7, double debug9, double debug11) {
/* 37 */     float debug13 = (float)(debug1 - debug7);
/* 38 */     float debug14 = (float)(debug3 - debug9);
/* 39 */     float debug15 = (float)(debug5 - debug11);
/* 40 */     if (!this.x.matches(Mth.abs(debug13)) || !this.y.matches(Mth.abs(debug14)) || !this.z.matches(Mth.abs(debug15))) {
/* 41 */       return false;
/*    */     }
/* 43 */     if (!this.horizontal.matchesSqr((debug13 * debug13 + debug15 * debug15))) {
/* 44 */       return false;
/*    */     }
/* 46 */     if (!this.absolute.matchesSqr((debug13 * debug13 + debug14 * debug14 + debug15 * debug15))) {
/* 47 */       return false;
/*    */     }
/* 49 */     return true;
/*    */   }
/*    */   
/*    */   public static DistancePredicate fromJson(@Nullable JsonElement debug0) {
/* 53 */     if (debug0 == null || debug0.isJsonNull()) {
/* 54 */       return ANY;
/*    */     }
/* 56 */     JsonObject debug1 = GsonHelper.convertToJsonObject(debug0, "distance");
/* 57 */     MinMaxBounds.Floats debug2 = MinMaxBounds.Floats.fromJson(debug1.get("x"));
/* 58 */     MinMaxBounds.Floats debug3 = MinMaxBounds.Floats.fromJson(debug1.get("y"));
/* 59 */     MinMaxBounds.Floats debug4 = MinMaxBounds.Floats.fromJson(debug1.get("z"));
/* 60 */     MinMaxBounds.Floats debug5 = MinMaxBounds.Floats.fromJson(debug1.get("horizontal"));
/* 61 */     MinMaxBounds.Floats debug6 = MinMaxBounds.Floats.fromJson(debug1.get("absolute"));
/* 62 */     return new DistancePredicate(debug2, debug3, debug4, debug5, debug6);
/*    */   }
/*    */   
/*    */   public JsonElement serializeToJson() {
/* 66 */     if (this == ANY) {
/* 67 */       return (JsonElement)JsonNull.INSTANCE;
/*    */     }
/*    */     
/* 70 */     JsonObject debug1 = new JsonObject();
/*    */     
/* 72 */     debug1.add("x", this.x.serializeToJson());
/* 73 */     debug1.add("y", this.y.serializeToJson());
/* 74 */     debug1.add("z", this.z.serializeToJson());
/* 75 */     debug1.add("horizontal", this.horizontal.serializeToJson());
/* 76 */     debug1.add("absolute", this.absolute.serializeToJson());
/*    */     
/* 78 */     return (JsonElement)debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\DistancePredicate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */