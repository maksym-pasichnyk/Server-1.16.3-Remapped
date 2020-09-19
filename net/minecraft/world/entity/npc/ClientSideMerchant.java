/*    */ package net.minecraft.world.entity.npc;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.inventory.MerchantContainer;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.trading.Merchant;
/*    */ import net.minecraft.world.item.trading.MerchantOffer;
/*    */ import net.minecraft.world.item.trading.MerchantOffers;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class ClientSideMerchant
/*    */   implements Merchant
/*    */ {
/*    */   private final MerchantContainer container;
/*    */   private final Player source;
/* 19 */   private MerchantOffers offers = new MerchantOffers();
/*    */   private int xp;
/*    */   
/*    */   public ClientSideMerchant(Player debug1) {
/* 23 */     this.source = debug1;
/* 24 */     this.container = new MerchantContainer(this);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Player getTradingPlayer() {
/* 34 */     return this.source;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setTradingPlayer(@Nullable Player debug1) {}
/*    */ 
/*    */   
/*    */   public MerchantOffers getOffers() {
/* 43 */     return this.offers;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void notifyTrade(MerchantOffer debug1) {
/* 53 */     debug1.increaseUses();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void notifyTradeUpdated(ItemStack debug1) {}
/*    */ 
/*    */   
/*    */   public Level getLevel() {
/* 62 */     return this.source.level;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getVillagerXp() {
/* 67 */     return this.xp;
/*    */   }
/*    */ 
/*    */   
/*    */   public void overrideXp(int debug1) {
/* 72 */     this.xp = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean showProgressBar() {
/* 77 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public SoundEvent getNotifyTradeSound() {
/* 82 */     return SoundEvents.VILLAGER_YES;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\npc\ClientSideMerchant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */