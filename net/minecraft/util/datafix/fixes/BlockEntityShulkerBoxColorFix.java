/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class BlockEntityShulkerBoxColorFix extends NamedEntityFix {
/*    */   public BlockEntityShulkerBoxColorFix(Schema debug1, boolean debug2) {
/*  9 */     super(debug1, debug2, "BlockEntityShulkerBoxColorFix", References.BLOCK_ENTITY, "minecraft:shulker_box");
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> debug1) {
/* 14 */     return debug1.update(DSL.remainderFinder(), debug0 -> debug0.remove("Color"));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\BlockEntityShulkerBoxColorFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */