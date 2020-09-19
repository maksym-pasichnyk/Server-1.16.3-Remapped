/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ItemCombinerMenu
/*     */   extends AbstractContainerMenu
/*     */ {
/*  21 */   protected final ResultContainer resultSlots = new ResultContainer();
/*  22 */   protected final Container inputSlots = (Container)new SimpleContainer(2)
/*     */     {
/*     */       public void setChanged() {
/*  25 */         super.setChanged();
/*  26 */         ItemCombinerMenu.this.slotsChanged((Container)this);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ContainerLevelAccess access;
/*     */ 
/*     */   
/*     */   protected final Player player;
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemCombinerMenu(@Nullable MenuType<?> debug1, int debug2, Inventory debug3, ContainerLevelAccess debug4) {
/*  40 */     super(debug1, debug2);
/*  41 */     this.access = debug4;
/*  42 */     this.player = debug3.player;
/*     */     
/*  44 */     addSlot(new Slot(this.inputSlots, 0, 27, 47));
/*  45 */     addSlot(new Slot(this.inputSlots, 1, 76, 47));
/*  46 */     addSlot(new Slot(this.resultSlots, 2, 134, 47)
/*     */         {
/*     */           public boolean mayPlace(ItemStack debug1) {
/*  49 */             return false;
/*     */           }
/*     */ 
/*     */           
/*     */           public boolean mayPickup(Player debug1) {
/*  54 */             return ItemCombinerMenu.this.mayPickup(debug1, hasItem());
/*     */           }
/*     */ 
/*     */           
/*     */           public ItemStack onTake(Player debug1, ItemStack debug2) {
/*  59 */             return ItemCombinerMenu.this.onTake(debug1, debug2);
/*     */           }
/*     */         });
/*     */     int debug5;
/*  63 */     for (debug5 = 0; debug5 < 3; debug5++) {
/*  64 */       for (int debug6 = 0; debug6 < 9; debug6++) {
/*  65 */         addSlot(new Slot((Container)debug3, debug6 + debug5 * 9 + 9, 8 + debug6 * 18, 84 + debug5 * 18));
/*     */       }
/*     */     } 
/*  68 */     for (debug5 = 0; debug5 < 9; debug5++) {
/*  69 */       addSlot(new Slot((Container)debug3, debug5, 8 + debug5 * 18, 142));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void slotsChanged(Container debug1) {
/*  77 */     super.slotsChanged(debug1);
/*     */     
/*  79 */     if (debug1 == this.inputSlots) {
/*  80 */       createResult();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void removed(Player debug1) {
/*  86 */     super.removed(debug1);
/*  87 */     this.access.execute((debug2, debug3) -> clearContainer(debug1, debug2, this.inputSlots));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player debug1) {
/*  92 */     return ((Boolean)this.access.<Boolean>evaluate((debug2, debug3) -> !isValidBlock(debug2.getBlockState(debug3)) ? Boolean.valueOf(false) : Boolean.valueOf((debug1.distanceToSqr(debug3.getX() + 0.5D, debug3.getY() + 0.5D, debug3.getZ() + 0.5D) <= 64.0D)), 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  97 */         Boolean.valueOf(true))).booleanValue();
/*     */   }
/*     */   
/*     */   protected boolean shouldQuickMoveToAdditionalSlot(ItemStack debug1) {
/* 101 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack quickMoveStack(Player debug1, int debug2) {
/* 106 */     ItemStack debug3 = ItemStack.EMPTY;
/* 107 */     Slot debug4 = this.slots.get(debug2);
/* 108 */     if (debug4 != null && debug4.hasItem()) {
/* 109 */       ItemStack debug5 = debug4.getItem();
/* 110 */       debug3 = debug5.copy();
/*     */       
/* 112 */       if (debug2 == 2) {
/* 113 */         if (!moveItemStackTo(debug5, 3, 39, true)) {
/* 114 */           return ItemStack.EMPTY;
/*     */         }
/* 116 */         debug4.onQuickCraft(debug5, debug3);
/* 117 */       } else if (debug2 == 0 || debug2 == 1) {
/* 118 */         if (!moveItemStackTo(debug5, 3, 39, false)) {
/* 119 */           return ItemStack.EMPTY;
/*     */         }
/* 121 */       } else if (debug2 >= 3 && debug2 < 39) {
/* 122 */         int debug6 = shouldQuickMoveToAdditionalSlot(debug3) ? 1 : 0;
/* 123 */         if (!moveItemStackTo(debug5, debug6, 2, false)) {
/* 124 */           return ItemStack.EMPTY;
/*     */         }
/*     */       } 
/*     */       
/* 128 */       if (debug5.isEmpty()) {
/* 129 */         debug4.set(ItemStack.EMPTY);
/*     */       } else {
/* 131 */         debug4.setChanged();
/*     */       } 
/* 133 */       if (debug5.getCount() == debug3.getCount()) {
/* 134 */         return ItemStack.EMPTY;
/*     */       }
/* 136 */       debug4.onTake(debug1, debug5);
/*     */     } 
/*     */     
/* 139 */     return debug3;
/*     */   }
/*     */   
/*     */   protected abstract boolean mayPickup(Player paramPlayer, boolean paramBoolean);
/*     */   
/*     */   protected abstract ItemStack onTake(Player paramPlayer, ItemStack paramItemStack);
/*     */   
/*     */   protected abstract boolean isValidBlock(BlockState paramBlockState);
/*     */   
/*     */   public abstract void createResult();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\inventory\ItemCombinerMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */