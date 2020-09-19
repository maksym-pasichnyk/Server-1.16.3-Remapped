/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class BeetrootBlock
/*    */   extends CropBlock
/*    */ {
/* 20 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
/*    */   
/* 22 */   private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[] {
/* 23 */       Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), 
/* 24 */       Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D), 
/* 25 */       Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D), 
/* 26 */       Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D)
/*    */     };
/*    */   
/*    */   public BeetrootBlock(BlockBehaviour.Properties debug1) {
/* 30 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public IntegerProperty getAgeProperty() {
/* 35 */     return AGE;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxAge() {
/* 40 */     return 3;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void randomTick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/* 51 */     if (debug4.nextInt(3) != 0) {
/* 52 */       super.randomTick(debug1, debug2, debug3, debug4);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getBonemealAgeIncrease(Level debug1) {
/* 58 */     return super.getBonemealAgeIncrease(debug1) / 3;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 63 */     debug1.add(new Property[] { (Property)AGE });
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 68 */     return SHAPE_BY_AGE[((Integer)debug1.getValue((Property)getAgeProperty())).intValue()];
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\BeetrootBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */