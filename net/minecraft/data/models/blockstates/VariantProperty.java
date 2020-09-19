/*    */ package net.minecraft.data.models.blockstates;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public class VariantProperty<T>
/*    */ {
/*    */   private final String key;
/*    */   private final Function<T, JsonElement> serializer;
/*    */   
/*    */   public VariantProperty(String debug1, Function<T, JsonElement> debug2) {
/* 13 */     this.key = debug1;
/* 14 */     this.serializer = debug2;
/*    */   }
/*    */   
/*    */   public Value withValue(T debug1) {
/* 18 */     return new Value(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 23 */     return this.key;
/*    */   }
/*    */   
/*    */   public class Value {
/*    */     private final T value;
/*    */     
/*    */     public Value(T debug2) {
/* 30 */       this.value = debug2;
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public void addToVariant(JsonObject debug1) {
/* 38 */       debug1.add(VariantProperty.this.key, VariantProperty.this.serializer.apply(this.value));
/*    */     }
/*    */ 
/*    */     
/*    */     public String toString() {
/* 43 */       return VariantProperty.this.key + "=" + this.value;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\models\blockstates\VariantProperty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */