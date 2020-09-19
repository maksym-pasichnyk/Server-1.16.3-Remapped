/*    */ package net.minecraft.util.datafix.schemas;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.Const;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.codecs.PrimitiveCodec;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ 
/*    */ public class NamespacedSchema extends Schema {
/*    */   public NamespacedSchema(int debug1, Schema debug2) {
/* 14 */     super(debug1, debug2);
/*    */   }
/*    */   
/*    */   public static String ensureNamespaced(String debug0) {
/* 18 */     ResourceLocation debug1 = ResourceLocation.tryParse(debug0);
/* 19 */     if (debug1 != null) {
/* 20 */       return debug1.toString();
/*    */     }
/* 22 */     return debug0;
/*    */   }
/*    */   
/* 25 */   public static final PrimitiveCodec<String> NAMESPACED_STRING_CODEC = new PrimitiveCodec<String>()
/*    */     {
/*    */       public <T> DataResult<String> read(DynamicOps<T> debug1, T debug2) {
/* 28 */         return debug1
/* 29 */           .getStringValue(debug2)
/* 30 */           .map(NamespacedSchema::ensureNamespaced);
/*    */       }
/*    */ 
/*    */       
/*    */       public <T> T write(DynamicOps<T> debug1, String debug2) {
/* 35 */         return (T)debug1.createString(debug2);
/*    */       }
/*    */ 
/*    */       
/*    */       public String toString() {
/* 40 */         return "NamespacedString";
/*    */       }
/*    */     };
/*    */   
/* 44 */   private static final Type<String> NAMESPACED_STRING = (Type<String>)new Const.PrimitiveType((Codec)NAMESPACED_STRING_CODEC);
/*    */   
/*    */   public static Type<String> namespacedString() {
/* 47 */     return NAMESPACED_STRING;
/*    */   }
/*    */ 
/*    */   
/*    */   public Type<?> getChoiceType(DSL.TypeReference debug1, String debug2) {
/* 52 */     return super.getChoiceType(debug1, ensureNamespaced(debug2));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\schemas\NamespacedSchema.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */