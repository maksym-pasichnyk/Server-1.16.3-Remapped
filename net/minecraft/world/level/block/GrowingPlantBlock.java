/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import java.util.Random;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public abstract class GrowingPlantBlock extends Block {
/*    */   protected final Direction growthDirection;
/*    */   protected final boolean scheduleFluidTicks;
/*    */   protected final VoxelShape shape;
/*    */   
/*    */   protected GrowingPlantBlock(BlockBehaviour.Properties debug1, Direction debug2, VoxelShape debug3, boolean debug4) {
/* 23 */     super(debug1);
/* 24 */     this.growthDirection = debug2;
/* 25 */     this.shape = debug3;
/* 26 */     this.scheduleFluidTicks = debug4;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 32 */     BlockState debug2 = debug1.getLevel().getBlockState(debug1.getClickedPos().relative(this.growthDirection));
/* 33 */     if (debug2.is(getHeadBlock()) || debug2.is(getBodyBlock())) {
/* 34 */       return getBodyBlock().defaultBlockState();
/*    */     }
/* 36 */     return getStateForPlacement((LevelAccessor)debug1.getLevel());
/*    */   }
/*    */   
/*    */   public BlockState getStateForPlacement(LevelAccessor debug1) {
/* 40 */     return defaultBlockState();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/* 45 */     BlockPos debug4 = debug3.relative(this.growthDirection.getOpposite());
/* 46 */     BlockState debug5 = debug2.getBlockState(debug4);
/* 47 */     Block debug6 = debug5.getBlock();
/* 48 */     if (!canAttachToBlock(debug6)) {
/* 49 */       return false;
/*    */     }
/*    */     
/* 52 */     return (debug6 == getHeadBlock() || debug6 == getBodyBlock() || debug5.isFaceSturdy((BlockGetter)debug2, debug4, this.growthDirection));
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/* 57 */     if (!debug1.canSurvive((LevelReader)debug2, debug3)) {
/* 58 */       debug2.destroyBlock(debug3, true);
/*    */     }
/*    */   }
/*    */   
/*    */   protected boolean canAttachToBlock(Block debug1) {
/* 63 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 68 */     return this.shape;
/*    */   }
/*    */   
/*    */   protected abstract GrowingPlantHeadBlock getHeadBlock();
/*    */   
/*    */   protected abstract Block getBodyBlock();
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\GrowingPlantBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */