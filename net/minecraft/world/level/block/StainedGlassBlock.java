/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ 
/*    */ public class StainedGlassBlock
/*    */   extends AbstractGlassBlock implements BeaconBeamBlock {
/*    */   public StainedGlassBlock(DyeColor debug1, BlockBehaviour.Properties debug2) {
/*  9 */     super(debug2);
/* 10 */     this.color = debug1;
/*    */   }
/*    */   private final DyeColor color;
/*    */   
/*    */   public DyeColor getColor() {
/* 15 */     return this.color;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\StainedGlassBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */