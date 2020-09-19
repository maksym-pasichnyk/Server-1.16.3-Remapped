/*    */ package net.minecraft.world.level.block;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class HayBlock extends RotatedPillarBlock {
/*    */   public HayBlock(BlockBehaviour.Properties debug1) {
/* 10 */     super(debug1);
/* 11 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)AXIS, (Comparable)Direction.Axis.Y));
/*    */   }
/*    */ 
/*    */   
/*    */   public void fallOn(Level debug1, BlockPos debug2, Entity debug3, float debug4) {
/* 16 */     debug3.causeFallDamage(debug4, 0.2F);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\HayBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */