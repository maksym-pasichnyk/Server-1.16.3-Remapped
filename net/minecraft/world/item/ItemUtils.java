/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResultHolder;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ItemUtils
/*    */ {
/*    */   public static InteractionResultHolder<ItemStack> useDrink(Level debug0, Player debug1, InteractionHand debug2) {
/* 16 */     debug1.startUsingItem(debug2);
/* 17 */     return InteractionResultHolder.consume(debug1.getItemInHand(debug2));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static ItemStack createFilledResult(ItemStack debug0, Player debug1, ItemStack debug2, boolean debug3) {
/* 26 */     boolean debug4 = debug1.abilities.instabuild;
/* 27 */     if (debug3 && debug4) {
/* 28 */       if (!debug1.inventory.contains(debug2)) {
/* 29 */         debug1.inventory.add(debug2);
/*    */       }
/* 31 */       return debug0;
/*    */     } 
/*    */     
/* 34 */     if (!debug4) {
/* 35 */       debug0.shrink(1);
/*    */     }
/* 37 */     if (debug0.isEmpty()) {
/* 38 */       return debug2;
/*    */     }
/* 40 */     if (!debug1.inventory.add(debug2)) {
/* 41 */       debug1.drop(debug2, false);
/*    */     }
/* 43 */     return debug0;
/*    */   }
/*    */   
/*    */   public static ItemStack createFilledResult(ItemStack debug0, Player debug1, ItemStack debug2) {
/* 47 */     return createFilledResult(debug0, debug1, debug2, true);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\ItemUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */