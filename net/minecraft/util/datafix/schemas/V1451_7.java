/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class V1451_7
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V1451_7(int debug1, Schema debug2) {
/* 16 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public void registerTypes(Schema debug1, Map<String, Supplier<TypeTemplate>> debug2, Map<String, Supplier<TypeTemplate>> debug3) {
/* 21 */     super.registerTypes(debug1, debug2, debug3);
/*    */     
/* 23 */     debug1.registerType(false, References.STRUCTURE_FEATURE, () -> DSL.optionalFields("Children", DSL.list(DSL.optionalFields("CA", References.BLOCK_STATE.in(debug0), "CB", References.BLOCK_STATE.in(debug0), "CC", References.BLOCK_STATE.in(debug0), "CD", References.BLOCK_STATE.in(debug0)))));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\schemas\V1451_7.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */