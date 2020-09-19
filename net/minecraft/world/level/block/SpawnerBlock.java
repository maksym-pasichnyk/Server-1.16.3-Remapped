/*    */ package net.minecraft.world.level.block;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class SpawnerBlock extends BaseEntityBlock {
/*    */   protected SpawnerBlock(BlockBehaviour.Properties debug1) {
/* 13 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockGetter debug1) {
/* 18 */     return (BlockEntity)new SpawnerBlockEntity();
/*    */   }
/*    */ 
/*    */   
/*    */   public void spawnAfterBreak(BlockState debug1, ServerLevel debug2, BlockPos debug3, ItemStack debug4) {
/* 23 */     super.spawnAfterBreak(debug1, debug2, debug3, debug4);
/*    */     
/* 25 */     int debug5 = 15 + debug2.random.nextInt(15) + debug2.random.nextInt(15);
/* 26 */     popExperience(debug2, debug3, debug5);
/*    */   }
/*    */ 
/*    */   
/*    */   public RenderShape getRenderShape(BlockState debug1) {
/* 31 */     return RenderShape.MODEL;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\SpawnerBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */