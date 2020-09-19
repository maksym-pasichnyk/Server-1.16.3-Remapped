/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.MapItem;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CartographyTableMenu
/*     */   extends AbstractContainerMenu
/*     */ {
/*     */   private final ContainerLevelAccess access;
/*     */   private long lastSoundTime;
/*     */   
/*  28 */   public final Container container = (Container)new SimpleContainer(2)
/*     */     {
/*     */       public void setChanged() {
/*  31 */         CartographyTableMenu.this.slotsChanged((Container)this);
/*  32 */         super.setChanged();
/*     */       }
/*     */     };
/*  35 */   private final ResultContainer resultContainer = new ResultContainer()
/*     */     {
/*     */       public void setChanged()
/*     */       {
/*  39 */         CartographyTableMenu.this.slotsChanged(this);
/*  40 */         super.setChanged();
/*     */       }
/*     */     };
/*     */   
/*     */   public CartographyTableMenu(int debug1, Inventory debug2) {
/*  45 */     this(debug1, debug2, ContainerLevelAccess.NULL);
/*     */   }
/*     */   
/*     */   public CartographyTableMenu(int debug1, Inventory debug2, final ContainerLevelAccess access) {
/*  49 */     super(MenuType.CARTOGRAPHY_TABLE, debug1);
/*     */     
/*  51 */     this.access = access;
/*     */     
/*  53 */     addSlot(new Slot(this.container, 0, 15, 15)
/*     */         {
/*     */           public boolean mayPlace(ItemStack debug1) {
/*  56 */             return (debug1.getItem() == Items.FILLED_MAP);
/*     */           }
/*     */         });
/*     */     
/*  60 */     addSlot(new Slot(this.container, 1, 15, 52)
/*     */         {
/*     */           public boolean mayPlace(ItemStack debug1) {
/*  63 */             Item debug2 = debug1.getItem();
/*  64 */             return (debug2 == Items.PAPER || debug2 == Items.MAP || debug2 == Items.GLASS_PANE);
/*     */           }
/*     */         });
/*     */     
/*  68 */     addSlot(new Slot(this.resultContainer, 2, 145, 39)
/*     */         {
/*     */           public boolean mayPlace(ItemStack debug1) {
/*  71 */             return false;
/*     */           }
/*     */ 
/*     */           
/*     */           public ItemStack onTake(Player debug1, ItemStack debug2) {
/*  76 */             ((Slot)CartographyTableMenu.this.slots.get(0)).remove(1);
/*  77 */             ((Slot)CartographyTableMenu.this.slots.get(1)).remove(1);
/*     */             
/*  79 */             debug2.getItem().onCraftedBy(debug2, debug1.level, debug1);
/*     */             
/*  81 */             access.execute((debug1, debug2) -> {
/*     */                   long debug3 = debug1.getGameTime();
/*     */                   
/*     */                   if (CartographyTableMenu.this.lastSoundTime != debug3) {
/*     */                     debug1.playSound(null, debug2, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */                     
/*     */                     CartographyTableMenu.this.lastSoundTime = debug3;
/*     */                   } 
/*     */                 });
/*  90 */             return super.onTake(debug1, debug2);
/*     */           }
/*     */         });
/*     */     int debug4;
/*  94 */     for (debug4 = 0; debug4 < 3; debug4++) {
/*  95 */       for (int debug5 = 0; debug5 < 9; debug5++) {
/*  96 */         addSlot(new Slot((Container)debug2, debug5 + debug4 * 9 + 9, 8 + debug5 * 18, 84 + debug4 * 18));
/*     */       }
/*     */     } 
/*  99 */     for (debug4 = 0; debug4 < 9; debug4++) {
/* 100 */       addSlot(new Slot((Container)debug2, debug4, 8 + debug4 * 18, 142));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player debug1) {
/* 106 */     return stillValid(this.access, debug1, Blocks.CARTOGRAPHY_TABLE);
/*     */   }
/*     */ 
/*     */   
/*     */   public void slotsChanged(Container debug1) {
/* 111 */     ItemStack debug2 = this.container.getItem(0);
/* 112 */     ItemStack debug3 = this.container.getItem(1);
/* 113 */     ItemStack debug4 = this.resultContainer.getItem(2);
/*     */     
/* 115 */     if (!debug4.isEmpty() && (debug2.isEmpty() || debug3.isEmpty())) {
/* 116 */       this.resultContainer.removeItemNoUpdate(2);
/* 117 */     } else if (!debug2.isEmpty() && !debug3.isEmpty()) {
/* 118 */       setupResultSlot(debug2, debug3, debug4);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setupResultSlot(ItemStack debug1, ItemStack debug2, ItemStack debug3) {
/* 123 */     this.access.execute((debug4, debug5) -> {
/*     */           ItemStack debug8;
/*     */           Item debug6 = debug1.getItem();
/*     */           MapItemSavedData debug7 = MapItem.getSavedData(debug2, debug4);
/*     */           if (debug7 == null) {
/*     */             return;
/*     */           }
/*     */           if (debug6 == Items.PAPER && !debug7.locked && debug7.scale < 4) {
/*     */             debug8 = debug2.copy();
/*     */             debug8.setCount(1);
/*     */             debug8.getOrCreateTag().putInt("map_scale_direction", 1);
/*     */             broadcastChanges();
/*     */           } else if (debug6 == Items.GLASS_PANE && !debug7.locked) {
/*     */             debug8 = debug2.copy();
/*     */             debug8.setCount(1);
/*     */             debug8.getOrCreateTag().putBoolean("map_to_lock", true);
/*     */             broadcastChanges();
/*     */           } else if (debug6 == Items.MAP) {
/*     */             debug8 = debug2.copy();
/*     */             debug8.setCount(2);
/*     */             broadcastChanges();
/*     */           } else {
/*     */             this.resultContainer.removeItemNoUpdate(2);
/*     */             broadcastChanges();
/*     */             return;
/*     */           } 
/*     */           if (!ItemStack.matches(debug8, debug3)) {
/*     */             this.resultContainer.setItem(2, debug8);
/*     */             broadcastChanges();
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canTakeItemForPickAll(ItemStack debug1, Slot debug2) {
/* 162 */     return (debug2.container != this.resultContainer && super.canTakeItemForPickAll(debug1, debug2));
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack quickMoveStack(Player debug1, int debug2) {
/* 167 */     ItemStack debug3 = ItemStack.EMPTY;
/* 168 */     Slot debug4 = this.slots.get(debug2);
/* 169 */     if (debug4 != null && debug4.hasItem()) {
/* 170 */       ItemStack debug5 = debug4.getItem();
/* 171 */       ItemStack debug6 = debug5;
/* 172 */       Item debug7 = debug6.getItem();
/* 173 */       debug3 = debug6.copy();
/*     */       
/* 175 */       if (debug2 == 2) {
/* 176 */         debug7.onCraftedBy(debug6, debug1.level, debug1);
/* 177 */         if (!moveItemStackTo(debug6, 3, 39, true)) {
/* 178 */           return ItemStack.EMPTY;
/*     */         }
/* 180 */         debug4.onQuickCraft(debug6, debug3);
/* 181 */       } else if (debug2 == 1 || debug2 == 0) {
/* 182 */         if (!moveItemStackTo(debug6, 3, 39, false)) {
/* 183 */           return ItemStack.EMPTY;
/*     */         }
/* 185 */       } else if (debug7 == Items.FILLED_MAP) {
/* 186 */         if (!moveItemStackTo(debug6, 0, 1, false)) {
/* 187 */           return ItemStack.EMPTY;
/*     */         }
/* 189 */       } else if (debug7 == Items.PAPER || debug7 == Items.MAP || debug7 == Items.GLASS_PANE) {
/* 190 */         if (!moveItemStackTo(debug6, 1, 2, false)) {
/* 191 */           return ItemStack.EMPTY;
/*     */         }
/* 193 */       } else if (debug2 >= 3 && debug2 < 30) {
/* 194 */         if (!moveItemStackTo(debug6, 30, 39, false)) {
/* 195 */           return ItemStack.EMPTY;
/*     */         }
/* 197 */       } else if (debug2 >= 30 && debug2 < 39 && 
/* 198 */         !moveItemStackTo(debug6, 3, 30, false)) {
/* 199 */         return ItemStack.EMPTY;
/*     */       } 
/*     */ 
/*     */       
/* 203 */       if (debug6.isEmpty()) {
/* 204 */         debug4.set(ItemStack.EMPTY);
/*     */       }
/*     */       
/* 207 */       debug4.setChanged();
/*     */       
/* 209 */       if (debug6.getCount() == debug3.getCount()) {
/* 210 */         return ItemStack.EMPTY;
/*     */       }
/* 212 */       debug4.onTake(debug1, debug6);
/* 213 */       broadcastChanges();
/*     */     } 
/*     */     
/* 216 */     return debug3;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removed(Player debug1) {
/* 221 */     super.removed(debug1);
/*     */     
/* 223 */     this.resultContainer.removeItemNoUpdate(2);
/* 224 */     this.access.execute((debug2, debug3) -> clearContainer(debug1, debug1.level, this.container));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\inventory\CartographyTableMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */