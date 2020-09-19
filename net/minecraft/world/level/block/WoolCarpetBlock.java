/*    */ package net.minecraft.world.level.block;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class WoolCarpetBlock extends Block {
/* 14 */   protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
/*    */   
/*    */   private final DyeColor color;
/*    */   
/*    */   protected WoolCarpetBlock(DyeColor debug1, BlockBehaviour.Properties debug2) {
/* 19 */     super(debug2);
/* 20 */     this.color = debug1;
/*    */   }
/*    */   
/*    */   public DyeColor getColor() {
/* 24 */     return this.color;
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 29 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 34 */     if (!debug1.canSurvive((LevelReader)debug4, debug5)) {
/* 35 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/*    */     
/* 38 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/* 43 */     return !debug2.isEmptyBlock(debug3.below());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\WoolCarpetBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */