/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class WitherWallSkullBlock extends WallSkullBlock {
/*    */   protected WitherWallSkullBlock(BlockBehaviour.Properties debug1) {
/* 13 */     super(SkullBlock.Types.WITHER_SKELETON, debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setPlacedBy(Level debug1, BlockPos debug2, BlockState debug3, @Nullable LivingEntity debug4, ItemStack debug5) {
/* 18 */     Blocks.WITHER_SKELETON_SKULL.setPlacedBy(debug1, debug2, debug3, debug4, debug5);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\WitherWallSkullBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */