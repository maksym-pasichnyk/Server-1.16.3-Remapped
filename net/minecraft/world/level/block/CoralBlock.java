/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import java.util.Random;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ 
/*    */ public class CoralBlock
/*    */   extends Block {
/*    */   public CoralBlock(Block debug1, BlockBehaviour.Properties debug2) {
/* 20 */     super(debug2);
/* 21 */     this.deadBlock = debug1;
/*    */   }
/*    */   private final Block deadBlock;
/*    */   
/*    */   public void tick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/* 26 */     if (!scanForWater((BlockGetter)debug2, debug3)) {
/* 27 */       debug2.setBlock(debug3, this.deadBlock.defaultBlockState(), 2);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 33 */     if (!scanForWater((BlockGetter)debug4, debug5)) {
/* 34 */       debug4.getBlockTicks().scheduleTick(debug5, this, 60 + debug4.getRandom().nextInt(40));
/*    */     }
/* 36 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */   }
/*    */   
/*    */   protected boolean scanForWater(BlockGetter debug1, BlockPos debug2) {
/* 40 */     for (Direction debug6 : Direction.values()) {
/* 41 */       FluidState debug7 = debug1.getFluidState(debug2.relative(debug6));
/* 42 */       if (debug7.is((Tag)FluidTags.WATER)) {
/* 43 */         return true;
/*    */       }
/*    */     } 
/* 46 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 52 */     if (!scanForWater((BlockGetter)debug1.getLevel(), debug1.getClickedPos())) {
/* 53 */       debug1.getLevel().getBlockTicks().scheduleTick(debug1.getClickedPos(), this, 60 + debug1.getLevel().getRandom().nextInt(40));
/*    */     }
/* 55 */     return defaultBlockState();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\CoralBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */