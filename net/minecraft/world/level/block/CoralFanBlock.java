/*    */ package net.minecraft.world.level.block;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ 
/*    */ public class CoralFanBlock extends BaseCoralFanBlock {
/*    */   protected CoralFanBlock(Block debug1, BlockBehaviour.Properties debug2) {
/* 17 */     super(debug2);
/* 18 */     this.deadBlock = debug1;
/*    */   }
/*    */   private final Block deadBlock;
/*    */   
/*    */   public void onPlace(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/* 23 */     tryScheduleDieTick(debug1, (LevelAccessor)debug2, debug3);
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/* 28 */     if (!scanForWater(debug1, (BlockGetter)debug2, debug3)) {
/* 29 */       debug2.setBlock(debug3, (BlockState)this.deadBlock.defaultBlockState().setValue((Property)WATERLOGGED, Boolean.valueOf(false)), 2);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 35 */     if (debug2 == Direction.DOWN && !debug1.canSurvive((LevelReader)debug4, debug5)) {
/* 36 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/*    */     
/* 39 */     tryScheduleDieTick(debug1, debug4, debug5);
/*    */     
/* 41 */     if (((Boolean)debug1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 42 */       debug4.getLiquidTicks().scheduleTick(debug5, Fluids.WATER, Fluids.WATER.getTickDelay((LevelReader)debug4));
/*    */     }
/*    */     
/* 45 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\CoralFanBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */