/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public class V2519
/*    */   extends NamespacedSchema {
/*    */   public V2519(int debug1, Schema debug2) {
/* 11 */     super(debug1, debug2);
/*    */   }
/*    */   
/*    */   protected static void registerMob(Schema debug0, Map<String, Supplier<TypeTemplate>> debug1, String debug2) {
/* 15 */     debug0.register(debug1, debug2, () -> V100.equipment(debug0));
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema debug1) {
/* 20 */     Map<String, Supplier<TypeTemplate>> debug2 = super.registerEntities(debug1);
/* 21 */     registerMob(debug1, debug2, "minecraft:strider");
/* 22 */     return debug2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\schemas\V2519.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */