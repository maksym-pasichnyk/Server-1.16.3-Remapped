/*    */ package net.minecraft.world.item.trading;
/*    */ 
/*    */ import java.util.OptionalInt;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.world.MenuProvider;
/*    */ import net.minecraft.world.SimpleMenuProvider;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*    */ import net.minecraft.world.inventory.MerchantMenu;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public interface Merchant {
/*    */   void setTradingPlayer(@Nullable Player paramPlayer);
/*    */   
/*    */   @Nullable
/*    */   Player getTradingPlayer();
/*    */   
/*    */   MerchantOffers getOffers();
/*    */   
/*    */   void notifyTrade(MerchantOffer paramMerchantOffer);
/*    */   
/*    */   void notifyTradeUpdated(ItemStack paramItemStack);
/*    */   
/*    */   Level getLevel();
/*    */   
/*    */   int getVillagerXp();
/*    */   
/*    */   void overrideXp(int paramInt);
/*    */   
/*    */   boolean showProgressBar();
/*    */   
/*    */   SoundEvent getNotifyTradeSound();
/*    */   
/*    */   default boolean canRestock() {
/* 39 */     return false;
/*    */   }
/*    */   
/*    */   default void openTradingScreen(Player debug1, Component debug2, int debug3) {
/* 43 */     OptionalInt debug4 = debug1.openMenu((MenuProvider)new SimpleMenuProvider((debug1, debug2, debug3) -> new MerchantMenu(debug1, debug2, this), debug2));
/*    */     
/* 45 */     if (debug4.isPresent()) {
/* 46 */       MerchantOffers debug5 = getOffers();
/* 47 */       if (!debug5.isEmpty())
/* 48 */         debug1.sendMerchantOffers(debug4.getAsInt(), debug5, debug3, getVillagerXp(), showProgressBar(), canRestock()); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\trading\Merchant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */