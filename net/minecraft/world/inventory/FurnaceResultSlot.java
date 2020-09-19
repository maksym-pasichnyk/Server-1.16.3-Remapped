/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
/*    */ 
/*    */ public class FurnaceResultSlot extends Slot {
/*    */   private final Player player;
/*    */   private int removeCount;
/*    */   
/*    */   public FurnaceResultSlot(Player debug1, Container debug2, int debug3, int debug4, int debug5) {
/* 13 */     super(debug2, debug3, debug4, debug5);
/* 14 */     this.player = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean mayPlace(ItemStack debug1) {
/* 19 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack remove(int debug1) {
/* 24 */     if (hasItem()) {
/* 25 */       this.removeCount += Math.min(debug1, getItem().getCount());
/*    */     }
/* 27 */     return super.remove(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack onTake(Player debug1, ItemStack debug2) {
/* 32 */     checkTakeAchievements(debug2);
/* 33 */     super.onTake(debug1, debug2);
/* 34 */     return debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onQuickCraft(ItemStack debug1, int debug2) {
/* 39 */     this.removeCount += debug2;
/* 40 */     checkTakeAchievements(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void checkTakeAchievements(ItemStack debug1) {
/* 45 */     debug1.onCraftedBy(this.player.level, this.player, this.removeCount);
/* 46 */     if (!this.player.level.isClientSide && this.container instanceof AbstractFurnaceBlockEntity) {
/* 47 */       ((AbstractFurnaceBlockEntity)this.container).awardUsedRecipesAndPopExperience(this.player);
/*    */     }
/*    */     
/* 50 */     this.removeCount = 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\inventory\FurnaceResultSlot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */