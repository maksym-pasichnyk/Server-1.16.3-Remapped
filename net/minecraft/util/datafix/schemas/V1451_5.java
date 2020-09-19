/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public class V1451_5
/*    */   extends NamespacedSchema {
/*    */   public V1451_5(int debug1, Schema debug2) {
/* 11 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema debug1) {
/* 16 */     Map<String, Supplier<TypeTemplate>> debug2 = super.registerBlockEntities(debug1);
/*    */     
/* 18 */     debug2.remove("minecraft:flower_pot");
/* 19 */     debug2.remove("minecraft:noteblock");
/*    */     
/* 21 */     return debug2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\schemas\V1451_5.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */