/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ public class V1451_2
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V1451_2(int debug1, Schema debug2) {
/* 14 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema debug1) {
/* 19 */     Map<String, Supplier<TypeTemplate>> debug2 = super.registerBlockEntities(debug1);
/*    */     
/* 21 */     debug1.register(debug2, "minecraft:piston", debug1 -> DSL.optionalFields("blockState", References.BLOCK_STATE.in(debug0)));
/*    */ 
/*    */ 
/*    */     
/* 25 */     return debug2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\schemas\V1451_2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */