/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.MenuProvider;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.SmokerBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SmokerBlock
/*    */   extends AbstractFurnaceBlock
/*    */ {
/*    */   protected SmokerBlock(BlockBehaviour.Properties debug1) {
/* 20 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockGetter debug1) {
/* 25 */     return (BlockEntity)new SmokerBlockEntity();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void openContainer(Level debug1, BlockPos debug2, Player debug3) {
/* 30 */     BlockEntity debug4 = debug1.getBlockEntity(debug2);
/* 31 */     if (debug4 instanceof SmokerBlockEntity) {
/* 32 */       debug3.openMenu((MenuProvider)debug4);
/* 33 */       debug3.awardStat(Stats.INTERACT_WITH_SMOKER);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\SmokerBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */