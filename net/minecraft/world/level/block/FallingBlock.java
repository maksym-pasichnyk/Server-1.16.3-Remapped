/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.item.FallingBlockEntity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.Material;
/*    */ 
/*    */ public class FallingBlock
/*    */   extends Block
/*    */ {
/*    */   public FallingBlock(BlockBehaviour.Properties debug1) {
/* 21 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onPlace(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/* 26 */     debug2.getBlockTicks().scheduleTick(debug3, this, getDelayAfterPlace());
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 31 */     debug4.getBlockTicks().scheduleTick(debug5, this, getDelayAfterPlace());
/*    */     
/* 33 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/* 38 */     if (!isFree(debug2.getBlockState(debug3.below())) || debug3.getY() < 0) {
/*    */       return;
/*    */     }
/*    */ 
/*    */     
/* 43 */     FallingBlockEntity debug5 = new FallingBlockEntity((Level)debug2, debug3.getX() + 0.5D, debug3.getY(), debug3.getZ() + 0.5D, debug2.getBlockState(debug3));
/* 44 */     falling(debug5);
/* 45 */     debug2.addFreshEntity((Entity)debug5);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void falling(FallingBlockEntity debug1) {}
/*    */   
/*    */   protected int getDelayAfterPlace() {
/* 52 */     return 2;
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean isFree(BlockState debug0) {
/* 57 */     Material debug1 = debug0.getMaterial();
/* 58 */     return (debug0.isAir() || debug0.is((Tag)BlockTags.FIRE) || debug1.isLiquid() || debug1.isReplaceable());
/*    */   }
/*    */   
/*    */   public void onLand(Level debug1, BlockPos debug2, BlockState debug3, BlockState debug4, FallingBlockEntity debug5) {}
/*    */   
/*    */   public void onBroken(Level debug1, BlockPos debug2, FallingBlockEntity debug3) {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\FallingBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */