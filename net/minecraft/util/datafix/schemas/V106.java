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
/*    */ 
/*    */ public class V106
/*    */   extends Schema
/*    */ {
/*    */   public V106(int debug1, Schema debug2) {
/* 17 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public void registerTypes(Schema debug1, Map<String, Supplier<TypeTemplate>> debug2, Map<String, Supplier<TypeTemplate>> debug3) {
/* 22 */     super.registerTypes(debug1, debug2, debug3);
/*    */     
/* 24 */     debug1.registerType(true, References.UNTAGGED_SPAWNER, () -> DSL.optionalFields("SpawnPotentials", DSL.list(DSL.fields("Entity", References.ENTITY_TREE.in(debug0))), "SpawnData", References.ENTITY_TREE.in(debug0)));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\schemas\V106.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */