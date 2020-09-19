/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*    */ import net.minecraft.world.item.enchantment.Enchantments;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class OreBlock extends Block {
/*    */   public OreBlock(BlockBehaviour.Properties debug1) {
/* 15 */     super(debug1);
/*    */   }
/*    */   
/*    */   protected int xpOnDrop(Random debug1) {
/* 19 */     if (this == Blocks.COAL_ORE)
/* 20 */       return Mth.nextInt(debug1, 0, 2); 
/* 21 */     if (this == Blocks.DIAMOND_ORE)
/* 22 */       return Mth.nextInt(debug1, 3, 7); 
/* 23 */     if (this == Blocks.EMERALD_ORE)
/* 24 */       return Mth.nextInt(debug1, 3, 7); 
/* 25 */     if (this == Blocks.LAPIS_ORE)
/* 26 */       return Mth.nextInt(debug1, 2, 5); 
/* 27 */     if (this == Blocks.NETHER_QUARTZ_ORE)
/* 28 */       return Mth.nextInt(debug1, 2, 5); 
/* 29 */     if (this == Blocks.NETHER_GOLD_ORE) {
/* 30 */       return Mth.nextInt(debug1, 0, 1);
/*    */     }
/* 32 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public void spawnAfterBreak(BlockState debug1, ServerLevel debug2, BlockPos debug3, ItemStack debug4) {
/* 37 */     super.spawnAfterBreak(debug1, debug2, debug3, debug4);
/*    */     
/* 39 */     if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, debug4) == 0) {
/* 40 */       int debug5 = xpOnDrop(debug2.random);
/* 41 */       if (debug5 > 0)
/* 42 */         popExperience(debug2, debug3, debug5); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\OreBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */