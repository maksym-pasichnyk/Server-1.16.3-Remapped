/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class FrostedIceBlock extends IceBlock {
/* 19 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public FrostedIceBlock(BlockBehaviour.Properties debug1) {
/* 25 */     super(debug1);
/* 26 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)AGE, Integer.valueOf(0)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void randomTick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/* 32 */     tick(debug1, debug2, debug3, debug4);
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/* 37 */     if ((debug4.nextInt(3) == 0 || fewerNeigboursThan((BlockGetter)debug2, debug3, 4)) && debug2.getMaxLocalRawBrightness(debug3) > 11 - ((Integer)debug1.getValue((Property)AGE)).intValue() - debug1.getLightBlock((BlockGetter)debug2, debug3) && 
/* 38 */       slightlyMelt(debug1, (Level)debug2, debug3)) {
/* 39 */       BlockPos.MutableBlockPos debug5 = new BlockPos.MutableBlockPos();
/* 40 */       for (Direction debug9 : Direction.values()) {
/* 41 */         debug5.setWithOffset((Vec3i)debug3, debug9);
/* 42 */         BlockState debug10 = debug2.getBlockState((BlockPos)debug5);
/* 43 */         if (debug10.is(this) && !slightlyMelt(debug10, (Level)debug2, (BlockPos)debug5)) {
/* 44 */           debug2.getBlockTicks().scheduleTick((BlockPos)debug5, this, Mth.nextInt(debug4, 20, 40));
/*    */         }
/*    */       } 
/*    */       
/*    */       return;
/*    */     } 
/* 50 */     debug2.getBlockTicks().scheduleTick(debug3, this, Mth.nextInt(debug4, 20, 40));
/*    */   }
/*    */   
/*    */   private boolean slightlyMelt(BlockState debug1, Level debug2, BlockPos debug3) {
/* 54 */     int debug4 = ((Integer)debug1.getValue((Property)AGE)).intValue();
/* 55 */     if (debug4 < 3) {
/* 56 */       debug2.setBlock(debug3, (BlockState)debug1.setValue((Property)AGE, Integer.valueOf(debug4 + 1)), 2);
/* 57 */       return false;
/*    */     } 
/* 59 */     melt(debug1, debug2, debug3);
/* 60 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void neighborChanged(BlockState debug1, Level debug2, BlockPos debug3, Block debug4, BlockPos debug5, boolean debug6) {
/* 66 */     if (debug4 == this && 
/* 67 */       fewerNeigboursThan((BlockGetter)debug2, debug3, 2)) {
/* 68 */       melt(debug1, debug2, debug3);
/*    */     }
/*    */ 
/*    */     
/* 72 */     super.neighborChanged(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */   }
/*    */   
/*    */   private boolean fewerNeigboursThan(BlockGetter debug1, BlockPos debug2, int debug3) {
/* 76 */     int debug4 = 0;
/* 77 */     BlockPos.MutableBlockPos debug5 = new BlockPos.MutableBlockPos();
/* 78 */     for (Direction debug9 : Direction.values()) {
/* 79 */       debug5.setWithOffset((Vec3i)debug2, debug9);
/*    */       
/* 81 */       debug4++;
/* 82 */       if (debug1.getBlockState((BlockPos)debug5).is(this) && debug4 >= debug3) {
/* 83 */         return false;
/*    */       }
/*    */     } 
/*    */     
/* 87 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 92 */     debug1.add(new Property[] { (Property)AGE });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\FrostedIceBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */