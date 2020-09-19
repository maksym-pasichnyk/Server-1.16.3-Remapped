/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.MenuProvider;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlastFurnaceBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BlastFurnaceBlock
/*    */   extends AbstractFurnaceBlock
/*    */ {
/*    */   protected BlastFurnaceBlock(BlockBehaviour.Properties debug1) {
/* 21 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockGetter debug1) {
/* 26 */     return (BlockEntity)new BlastFurnaceBlockEntity();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void openContainer(Level debug1, BlockPos debug2, Player debug3) {
/* 31 */     BlockEntity debug4 = debug1.getBlockEntity(debug2);
/* 32 */     if (debug4 instanceof BlastFurnaceBlockEntity) {
/* 33 */       debug3.openMenu((MenuProvider)debug4);
/* 34 */       debug3.awardStat(Stats.INTERACT_WITH_BLAST_FURNACE);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\BlastFurnaceBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */