/*    */ package net.minecraft.world.item.enchantment;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.LiquidBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.material.Material;
/*    */ 
/*    */ public class FrostWalkerEnchantment extends Enchantment {
/*    */   public FrostWalkerEnchantment(Enchantment.Rarity debug1, EquipmentSlot... debug2) {
/* 17 */     super(debug1, EnchantmentCategory.ARMOR_FEET, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMinCost(int debug1) {
/* 22 */     return debug1 * 10;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxCost(int debug1) {
/* 27 */     return getMinCost(debug1) + 15;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isTreasureOnly() {
/* 32 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxLevel() {
/* 37 */     return 2;
/*    */   }
/*    */   
/*    */   public static void onEntityMoved(LivingEntity debug0, Level debug1, BlockPos debug2, int debug3) {
/* 41 */     if (!debug0.isOnGround()) {
/*    */       return;
/*    */     }
/*    */     
/* 45 */     BlockState debug4 = Blocks.FROSTED_ICE.defaultBlockState();
/*    */     
/* 47 */     float debug5 = Math.min(16, 2 + debug3);
/* 48 */     BlockPos.MutableBlockPos debug6 = new BlockPos.MutableBlockPos();
/* 49 */     for (BlockPos debug8 : BlockPos.betweenClosed(debug2.offset(-debug5, -1.0D, -debug5), debug2.offset(debug5, -1.0D, debug5))) {
/* 50 */       if (debug8.closerThan((Position)debug0.position(), debug5)) {
/* 51 */         debug6.set(debug8.getX(), debug8.getY() + 1, debug8.getZ());
/* 52 */         BlockState debug9 = debug1.getBlockState((BlockPos)debug6);
/* 53 */         if (!debug9.isAir()) {
/*    */           continue;
/*    */         }
/* 56 */         BlockState debug10 = debug1.getBlockState(debug8);
/* 57 */         if (debug10.getMaterial() == Material.WATER && ((Integer)debug10.getValue((Property)LiquidBlock.LEVEL)).intValue() == 0 && 
/* 58 */           debug4.canSurvive((LevelReader)debug1, debug8) && debug1.isUnobstructed(debug4, debug8, CollisionContext.empty())) {
/* 59 */           debug1.setBlockAndUpdate(debug8, debug4);
/* 60 */           debug1.getBlockTicks().scheduleTick(debug8, Blocks.FROSTED_ICE, Mth.nextInt(debug0.getRandom(), 60, 120));
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean checkCompatibility(Enchantment debug1) {
/* 69 */     return (super.checkCompatibility(debug1) && debug1 != Enchantments.DEPTH_STRIDER);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\enchantment\FrostWalkerEnchantment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */