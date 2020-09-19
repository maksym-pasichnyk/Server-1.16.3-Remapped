/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.Hook;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class V102
/*    */   extends Schema
/*    */ {
/*    */   public V102(int debug1, Schema debug2) {
/* 21 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public void registerTypes(Schema debug1, Map<String, Supplier<TypeTemplate>> debug2, Map<String, Supplier<TypeTemplate>> debug3) {
/* 26 */     super.registerTypes(debug1, debug2, debug3);
/*    */     
/* 28 */     debug1.registerType(true, References.ITEM_STACK, () -> DSL.hook(DSL.optionalFields("id", References.ITEM_NAME.in(debug0), "tag", DSL.optionalFields("EntityTag", References.ENTITY_TREE.in(debug0), "BlockEntityTag", References.BLOCK_ENTITY.in(debug0), "CanDestroy", DSL.list(References.BLOCK_NAME.in(debug0)), "CanPlaceOn", DSL.list(References.BLOCK_NAME.in(debug0)))), V99.ADD_NAMES, Hook.HookFunction.IDENTITY));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\schemas\V102.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */