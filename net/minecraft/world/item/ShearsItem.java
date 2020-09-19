/*    */ package net.minecraft.world.item;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class ShearsItem extends Item {
/*    */   public ShearsItem(Item.Properties debug1) {
/* 13 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean mineBlock(ItemStack debug1, Level debug2, BlockState debug3, BlockPos debug4, LivingEntity debug5) {
/* 18 */     if (!debug2.isClientSide && !debug3.getBlock().is((Tag)BlockTags.FIRE)) {
/* 19 */       debug1.hurtAndBreak(1, debug5, debug0 -> debug0.broadcastBreakEvent(EquipmentSlot.MAINHAND));
/*    */     }
/*    */     
/* 22 */     if (debug3.is((Tag)BlockTags.LEAVES) || debug3
/* 23 */       .is(Blocks.COBWEB) || debug3
/* 24 */       .is(Blocks.GRASS) || debug3
/* 25 */       .is(Blocks.FERN) || debug3
/* 26 */       .is(Blocks.DEAD_BUSH) || debug3
/* 27 */       .is(Blocks.VINE) || debug3
/* 28 */       .is(Blocks.TRIPWIRE) || debug3
/* 29 */       .is((Tag)BlockTags.WOOL))
/*    */     {
/* 31 */       return true;
/*    */     }
/* 33 */     return super.mineBlock(debug1, debug2, debug3, debug4, debug5);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCorrectToolForDrops(BlockState debug1) {
/* 38 */     return (debug1.is(Blocks.COBWEB) || debug1.is(Blocks.REDSTONE_WIRE) || debug1.is(Blocks.TRIPWIRE));
/*    */   }
/*    */ 
/*    */   
/*    */   public float getDestroySpeed(ItemStack debug1, BlockState debug2) {
/* 43 */     if (debug2.is(Blocks.COBWEB) || debug2.is((Tag)BlockTags.LEAVES)) {
/* 44 */       return 15.0F;
/*    */     }
/* 46 */     if (debug2.is((Tag)BlockTags.WOOL)) {
/* 47 */       return 5.0F;
/*    */     }
/* 49 */     return super.getDestroySpeed(debug1, debug2);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\ShearsItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */