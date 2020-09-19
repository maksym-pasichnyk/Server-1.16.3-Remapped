/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.ListTag;
/*    */ import net.minecraft.world.SimpleContainer;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
/*    */ 
/*    */ public class PlayerEnderChestContainer extends SimpleContainer {
/*    */   private EnderChestBlockEntity activeChest;
/*    */   
/*    */   public PlayerEnderChestContainer() {
/* 14 */     super(27);
/*    */   }
/*    */   
/*    */   public void setActiveChest(EnderChestBlockEntity debug1) {
/* 18 */     this.activeChest = debug1;
/*    */   }
/*    */   
/*    */   public void fromTag(ListTag debug1) {
/*    */     int debug2;
/* 23 */     for (debug2 = 0; debug2 < getContainerSize(); debug2++) {
/* 24 */       setItem(debug2, ItemStack.EMPTY);
/*    */     }
/* 26 */     for (debug2 = 0; debug2 < debug1.size(); debug2++) {
/* 27 */       CompoundTag debug3 = debug1.getCompound(debug2);
/* 28 */       int debug4 = debug3.getByte("Slot") & 0xFF;
/* 29 */       if (debug4 >= 0 && debug4 < getContainerSize()) {
/* 30 */         setItem(debug4, ItemStack.of(debug3));
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public ListTag createTag() {
/* 37 */     ListTag debug1 = new ListTag();
/* 38 */     for (int debug2 = 0; debug2 < getContainerSize(); debug2++) {
/* 39 */       ItemStack debug3 = getItem(debug2);
/* 40 */       if (!debug3.isEmpty()) {
/* 41 */         CompoundTag debug4 = new CompoundTag();
/* 42 */         debug4.putByte("Slot", (byte)debug2);
/* 43 */         debug3.save(debug4);
/* 44 */         debug1.add(debug4);
/*    */       } 
/*    */     } 
/* 47 */     return debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean stillValid(Player debug1) {
/* 52 */     if (this.activeChest != null && !this.activeChest.stillValid(debug1)) {
/* 53 */       return false;
/*    */     }
/* 55 */     return super.stillValid(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void startOpen(Player debug1) {
/* 60 */     if (this.activeChest != null) {
/* 61 */       this.activeChest.startOpen();
/*    */     }
/* 63 */     super.startOpen(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void stopOpen(Player debug1) {
/* 68 */     if (this.activeChest != null) {
/* 69 */       this.activeChest.stopOpen();
/*    */     }
/* 71 */     super.stopOpen(debug1);
/* 72 */     this.activeChest = null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\inventory\PlayerEnderChestContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */