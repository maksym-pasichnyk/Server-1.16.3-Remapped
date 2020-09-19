/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public class V1486
/*    */   extends NamespacedSchema {
/*    */   public V1486(int debug1, Schema debug2) {
/* 11 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema debug1) {
/* 16 */     Map<String, Supplier<TypeTemplate>> debug2 = super.registerEntities(debug1);
/*    */     
/* 18 */     debug2.put("minecraft:cod", debug2.remove("minecraft:cod_mob"));
/* 19 */     debug2.put("minecraft:salmon", debug2.remove("minecraft:salmon_mob"));
/*    */     
/* 21 */     return debug2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\schemas\V1486.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */