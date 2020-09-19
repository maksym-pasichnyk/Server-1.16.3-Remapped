/*    */ package net.minecraft.world.level.block;
/*    */ import java.util.Random;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class RedstoneLampBlock extends Block {
/* 15 */   public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;
/*    */   
/*    */   public RedstoneLampBlock(BlockBehaviour.Properties debug1) {
/* 18 */     super(debug1);
/* 19 */     registerDefaultState((BlockState)defaultBlockState().setValue((Property)LIT, Boolean.valueOf(false)));
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 25 */     return (BlockState)defaultBlockState().setValue((Property)LIT, Boolean.valueOf(debug1.getLevel().hasNeighborSignal(debug1.getClickedPos())));
/*    */   }
/*    */ 
/*    */   
/*    */   public void neighborChanged(BlockState debug1, Level debug2, BlockPos debug3, Block debug4, BlockPos debug5, boolean debug6) {
/* 30 */     if (debug2.isClientSide) {
/*    */       return;
/*    */     }
/*    */     
/* 34 */     boolean debug7 = ((Boolean)debug1.getValue((Property)LIT)).booleanValue();
/* 35 */     if (debug7 != debug2.hasNeighborSignal(debug3)) {
/* 36 */       if (debug7) {
/* 37 */         debug2.getBlockTicks().scheduleTick(debug3, this, 4);
/*    */       } else {
/* 39 */         debug2.setBlock(debug3, (BlockState)debug1.cycle((Property)LIT), 2);
/*    */       } 
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/* 46 */     if (((Boolean)debug1.getValue((Property)LIT)).booleanValue() && !debug2.hasNeighborSignal(debug3)) {
/* 47 */       debug2.setBlock(debug3, (BlockState)debug1.cycle((Property)LIT), 2);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 53 */     debug1.add(new Property[] { (Property)LIT });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\RedstoneLampBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */