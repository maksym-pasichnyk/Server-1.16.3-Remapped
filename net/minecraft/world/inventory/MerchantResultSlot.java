/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.trading.Merchant;
/*    */ import net.minecraft.world.item.trading.MerchantOffer;
/*    */ 
/*    */ public class MerchantResultSlot extends Slot {
/*    */   private final MerchantContainer slots;
/*    */   private final Player player;
/*    */   private int removeCount;
/*    */   private final Merchant merchant;
/*    */   
/*    */   public MerchantResultSlot(Player debug1, Merchant debug2, MerchantContainer debug3, int debug4, int debug5, int debug6) {
/* 16 */     super(debug3, debug4, debug5, debug6);
/* 17 */     this.player = debug1;
/* 18 */     this.merchant = debug2;
/* 19 */     this.slots = debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean mayPlace(ItemStack debug1) {
/* 24 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack remove(int debug1) {
/* 29 */     if (hasItem()) {
/* 30 */       this.removeCount += Math.min(debug1, getItem().getCount());
/*    */     }
/* 32 */     return super.remove(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onQuickCraft(ItemStack debug1, int debug2) {
/* 37 */     this.removeCount += debug2;
/* 38 */     checkTakeAchievements(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void checkTakeAchievements(ItemStack debug1) {
/* 43 */     debug1.onCraftedBy(this.player.level, this.player, this.removeCount);
/* 44 */     this.removeCount = 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack onTake(Player debug1, ItemStack debug2) {
/* 49 */     checkTakeAchievements(debug2);
/*    */     
/* 51 */     MerchantOffer debug3 = this.slots.getActiveOffer();
/*    */     
/* 53 */     if (debug3 != null) {
/* 54 */       ItemStack debug4 = this.slots.getItem(0);
/* 55 */       ItemStack debug5 = this.slots.getItem(1);
/*    */ 
/*    */       
/* 58 */       if (debug3.take(debug4, debug5) || debug3.take(debug5, debug4)) {
/* 59 */         this.merchant.notifyTrade(debug3);
/* 60 */         debug1.awardStat(Stats.TRADED_WITH_VILLAGER);
/*    */         
/* 62 */         this.slots.setItem(0, debug4);
/* 63 */         this.slots.setItem(1, debug5);
/*    */       } 
/* 65 */       this.merchant.overrideXp(this.merchant.getVillagerXp() + debug3.getXp());
/*    */     } 
/* 67 */     return debug2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\inventory\MerchantResultSlot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */