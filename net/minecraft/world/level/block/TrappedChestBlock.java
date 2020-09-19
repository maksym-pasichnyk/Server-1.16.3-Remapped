/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.stats.Stat;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.entity.ChestBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.TrappedChestBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class TrappedChestBlock extends ChestBlock {
/*    */   public TrappedChestBlock(BlockBehaviour.Properties debug1) {
/* 19 */     super(debug1, () -> BlockEntityType.TRAPPED_CHEST);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockGetter debug1) {
/* 24 */     return (BlockEntity)new TrappedChestBlockEntity();
/*    */   }
/*    */ 
/*    */   
/*    */   protected Stat<ResourceLocation> getOpenChestStat() {
/* 29 */     return Stats.CUSTOM.get(Stats.TRIGGER_TRAPPED_CHEST);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSignalSource(BlockState debug1) {
/* 34 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getSignal(BlockState debug1, BlockGetter debug2, BlockPos debug3, Direction debug4) {
/* 39 */     return Mth.clamp(ChestBlockEntity.getOpenCount(debug2, debug3), 0, 15);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getDirectSignal(BlockState debug1, BlockGetter debug2, BlockPos debug3, Direction debug4) {
/* 44 */     if (debug4 == Direction.UP) {
/* 45 */       return debug1.getSignal(debug2, debug3, debug4);
/*    */     }
/*    */     
/* 48 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\TrappedChestBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */