/*    */ package net.minecraft.world.level.block;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.world.entity.item.FallingBlockEntity;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class ConcretePowderBlock extends FallingBlock {
/*    */   public ConcretePowderBlock(Block debug1, BlockBehaviour.Properties debug2) {
/* 17 */     super(debug2);
/* 18 */     this.concrete = debug1.defaultBlockState();
/*    */   }
/*    */   private final BlockState concrete;
/*    */   
/*    */   public void onLand(Level debug1, BlockPos debug2, BlockState debug3, BlockState debug4, FallingBlockEntity debug5) {
/* 23 */     if (shouldSolidify((BlockGetter)debug1, debug2, debug4)) {
/* 24 */       debug1.setBlock(debug2, this.concrete, 3);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 30 */     Level level = debug1.getLevel();
/* 31 */     BlockPos debug3 = debug1.getClickedPos();
/* 32 */     BlockState debug4 = level.getBlockState(debug3);
/*    */     
/* 34 */     if (shouldSolidify((BlockGetter)level, debug3, debug4)) {
/* 35 */       return this.concrete;
/*    */     }
/* 37 */     return super.getStateForPlacement(debug1);
/*    */   }
/*    */   
/*    */   private static boolean shouldSolidify(BlockGetter debug0, BlockPos debug1, BlockState debug2) {
/* 41 */     return (canSolidify(debug2) || touchesLiquid(debug0, debug1));
/*    */   }
/*    */   
/*    */   private static boolean touchesLiquid(BlockGetter debug0, BlockPos debug1) {
/* 45 */     boolean debug2 = false;
/* 46 */     BlockPos.MutableBlockPos debug3 = debug1.mutable();
/* 47 */     for (Direction debug7 : Direction.values()) {
/* 48 */       BlockState debug8 = debug0.getBlockState((BlockPos)debug3);
/* 49 */       if (debug7 != Direction.DOWN || canSolidify(debug8)) {
/*    */ 
/*    */         
/* 52 */         debug3.setWithOffset((Vec3i)debug1, debug7);
/* 53 */         debug8 = debug0.getBlockState((BlockPos)debug3);
/* 54 */         if (canSolidify(debug8) && !debug8.isFaceSturdy(debug0, debug1, debug7.getOpposite())) {
/* 55 */           debug2 = true; break;
/*    */         } 
/*    */       } 
/*    */     } 
/* 59 */     return debug2;
/*    */   }
/*    */   
/*    */   private static boolean canSolidify(BlockState debug0) {
/* 63 */     return debug0.getFluidState().is((Tag)FluidTags.WATER);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 68 */     if (touchesLiquid((BlockGetter)debug4, debug5)) {
/* 69 */       return this.concrete;
/*    */     }
/*    */     
/* 72 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\ConcretePowderBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */