/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public class V1483
/*    */   extends NamespacedSchema {
/*    */   public V1483(int debug1, Schema debug2) {
/* 11 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema debug1) {
/* 16 */     Map<String, Supplier<TypeTemplate>> debug2 = super.registerEntities(debug1);
/*    */     
/* 18 */     debug2.put("minecraft:pufferfish", debug2.remove("minecraft:puffer_fish"));
/*    */     
/* 20 */     return debug2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\schemas\V1483.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */