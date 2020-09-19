/*    */ package net.minecraft.advancements.critereon;
/*    */ 
/*    */ import com.google.gson.JsonArray;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonNull;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonSyntaxException;
/*    */ import java.util.Map;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.world.item.enchantment.Enchantment;
/*    */ 
/*    */ public class EnchantmentPredicate
/*    */ {
/* 17 */   public static final EnchantmentPredicate ANY = new EnchantmentPredicate();
/* 18 */   public static final EnchantmentPredicate[] NONE = new EnchantmentPredicate[0];
/*    */   
/*    */   private final Enchantment enchantment;
/*    */   private final MinMaxBounds.Ints level;
/*    */   
/*    */   public EnchantmentPredicate() {
/* 24 */     this.enchantment = null;
/* 25 */     this.level = MinMaxBounds.Ints.ANY;
/*    */   }
/*    */   
/*    */   public EnchantmentPredicate(@Nullable Enchantment debug1, MinMaxBounds.Ints debug2) {
/* 29 */     this.enchantment = debug1;
/* 30 */     this.level = debug2;
/*    */   }
/*    */   
/*    */   public boolean containedIn(Map<Enchantment, Integer> debug1) {
/* 34 */     if (this.enchantment != null) {
/*    */       
/* 36 */       if (!debug1.containsKey(this.enchantment)) {
/* 37 */         return false;
/*    */       }
/* 39 */       int debug2 = ((Integer)debug1.get(this.enchantment)).intValue();
/* 40 */       if (this.level != null && !this.level.matches(debug2)) {
/* 41 */         return false;
/*    */       }
/* 43 */     } else if (this.level != null) {
/*    */       
/* 45 */       for (Integer debug3 : debug1.values()) {
/* 46 */         if (this.level.matches(debug3.intValue())) {
/* 47 */           return true;
/*    */         }
/*    */       } 
/* 50 */       return false;
/*    */     } 
/*    */     
/* 53 */     return true;
/*    */   }
/*    */   
/*    */   public JsonElement serializeToJson() {
/* 57 */     if (this == ANY) {
/* 58 */       return (JsonElement)JsonNull.INSTANCE;
/*    */     }
/*    */     
/* 61 */     JsonObject debug1 = new JsonObject();
/*    */     
/* 63 */     if (this.enchantment != null) {
/* 64 */       debug1.addProperty("enchantment", Registry.ENCHANTMENT.getKey(this.enchantment).toString());
/*    */     }
/* 66 */     debug1.add("levels", this.level.serializeToJson());
/*    */     
/* 68 */     return (JsonElement)debug1;
/*    */   }
/*    */   
/*    */   public static EnchantmentPredicate fromJson(@Nullable JsonElement debug0) {
/* 72 */     if (debug0 == null || debug0.isJsonNull()) {
/* 73 */       return ANY;
/*    */     }
/* 75 */     JsonObject debug1 = GsonHelper.convertToJsonObject(debug0, "enchantment");
/*    */     
/* 77 */     Enchantment debug2 = null;
/* 78 */     if (debug1.has("enchantment")) {
/* 79 */       ResourceLocation resourceLocation = new ResourceLocation(GsonHelper.getAsString(debug1, "enchantment"));
/* 80 */       debug2 = (Enchantment)Registry.ENCHANTMENT.getOptional(resourceLocation).orElseThrow(() -> new JsonSyntaxException("Unknown enchantment '" + debug0 + "'"));
/*    */     } 
/* 82 */     MinMaxBounds.Ints debug3 = MinMaxBounds.Ints.fromJson(debug1.get("levels"));
/*    */     
/* 84 */     return new EnchantmentPredicate(debug2, debug3);
/*    */   }
/*    */   
/*    */   public static EnchantmentPredicate[] fromJsonArray(@Nullable JsonElement debug0) {
/* 88 */     if (debug0 == null || debug0.isJsonNull()) {
/* 89 */       return NONE;
/*    */     }
/*    */     
/* 92 */     JsonArray debug1 = GsonHelper.convertToJsonArray(debug0, "enchantments");
/* 93 */     EnchantmentPredicate[] debug2 = new EnchantmentPredicate[debug1.size()];
/* 94 */     for (int debug3 = 0; debug3 < debug2.length; debug3++) {
/* 95 */       debug2[debug3] = fromJson(debug1.get(debug3));
/*    */     }
/*    */     
/* 98 */     return debug2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\EnchantmentPredicate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */