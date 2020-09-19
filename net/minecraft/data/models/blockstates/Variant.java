/*    */ package net.minecraft.data.models.blockstates;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import com.google.gson.JsonArray;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public class Variant
/*    */   implements Supplier<JsonElement>
/*    */ {
/* 14 */   private final Map<VariantProperty<?>, VariantProperty<?>.Value> values = Maps.newLinkedHashMap();
/*    */   
/*    */   public <T> Variant with(VariantProperty<T> debug1, T debug2) {
/* 17 */     VariantProperty<?>.Value debug3 = this.values.put(debug1, debug1.withValue(debug2));
/* 18 */     if (debug3 != null) {
/* 19 */       throw new IllegalStateException("Replacing value of " + debug3 + " with " + debug2);
/*    */     }
/* 21 */     return this;
/*    */   }
/*    */   
/*    */   public static Variant variant() {
/* 25 */     return new Variant();
/*    */   }
/*    */   
/*    */   public static Variant merge(Variant debug0, Variant debug1) {
/* 29 */     Variant debug2 = new Variant();
/* 30 */     debug2.values.putAll(debug0.values);
/* 31 */     debug2.values.putAll(debug1.values);
/* 32 */     return debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public JsonElement get() {
/* 37 */     JsonObject debug1 = new JsonObject();
/* 38 */     this.values.values().forEach(debug1 -> debug1.addToVariant(debug0));
/* 39 */     return (JsonElement)debug1;
/*    */   }
/*    */   
/*    */   public static JsonElement convertList(List<Variant> debug0) {
/* 43 */     if (debug0.size() == 1) {
/* 44 */       return ((Variant)debug0.get(0)).get();
/*    */     }
/*    */     
/* 47 */     JsonArray debug1 = new JsonArray();
/* 48 */     debug0.forEach(debug1 -> debug0.add(debug1.get()));
/* 49 */     return (JsonElement)debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\models\blockstates\Variant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */