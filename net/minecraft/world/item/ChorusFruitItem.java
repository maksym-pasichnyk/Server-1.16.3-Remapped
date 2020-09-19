/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class ChorusFruitItem
/*    */   extends Item
/*    */ {
/*    */   public ChorusFruitItem(Item.Properties debug1) {
/* 15 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack finishUsingItem(ItemStack debug1, Level debug2, LivingEntity debug3) {
/* 20 */     ItemStack debug4 = super.finishUsingItem(debug1, debug2, debug3);
/*    */     
/* 22 */     if (!debug2.isClientSide) {
/* 23 */       double debug5 = debug3.getX();
/* 24 */       double debug7 = debug3.getY();
/* 25 */       double debug9 = debug3.getZ();
/*    */       
/* 27 */       for (int debug11 = 0; debug11 < 16; debug11++) {
/* 28 */         double debug12 = debug3.getX() + (debug3.getRandom().nextDouble() - 0.5D) * 16.0D;
/* 29 */         double debug14 = Mth.clamp(debug3.getY() + (debug3.getRandom().nextInt(16) - 8), 0.0D, (debug2.getHeight() - 1));
/* 30 */         double debug16 = debug3.getZ() + (debug3.getRandom().nextDouble() - 0.5D) * 16.0D;
/* 31 */         if (debug3.isPassenger()) {
/* 32 */           debug3.stopRiding();
/*    */         }
/* 34 */         if (debug3.randomTeleport(debug12, debug14, debug16, true)) {
/*    */           
/* 36 */           SoundEvent debug18 = (debug3 instanceof net.minecraft.world.entity.animal.Fox) ? SoundEvents.FOX_TELEPORT : SoundEvents.CHORUS_FRUIT_TELEPORT;
/* 37 */           debug2.playSound(null, debug5, debug7, debug9, debug18, SoundSource.PLAYERS, 1.0F, 1.0F);
/* 38 */           debug3.playSound(debug18, 1.0F, 1.0F);
/*    */           
/*    */           break;
/*    */         } 
/*    */       } 
/*    */       
/* 44 */       if (debug3 instanceof Player) {
/* 45 */         ((Player)debug3).getCooldowns().addCooldown(this, 20);
/*    */       }
/*    */     } 
/*    */     
/* 49 */     return debug4;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\ChorusFruitItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */