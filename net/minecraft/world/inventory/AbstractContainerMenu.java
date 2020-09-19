/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractContainerMenu
/*     */ {
/*  35 */   private final NonNullList<ItemStack> lastSlots = NonNullList.create();
/*  36 */   public final List<Slot> slots = Lists.newArrayList();
/*  37 */   private final List<DataSlot> dataSlots = Lists.newArrayList();
/*     */   
/*     */   @Nullable
/*     */   private final MenuType<?> menuType;
/*     */   
/*     */   public final int containerId;
/*     */   
/*  44 */   private int quickcraftType = -1;
/*     */   private int quickcraftStatus;
/*  46 */   private final Set<Slot> quickcraftSlots = Sets.newHashSet();
/*     */   
/*  48 */   private final List<ContainerListener> containerListeners = Lists.newArrayList();
/*     */ 
/*     */   
/*     */   private final Set<Player> unSynchedPlayers;
/*     */ 
/*     */ 
/*     */   
/*     */   protected static boolean stillValid(ContainerLevelAccess debug0, Player debug1, Block debug2) {
/*  56 */     return ((Boolean)debug0.<Boolean>evaluate((debug2, debug3) -> !debug2.getBlockState(debug3).is(debug0) ? Boolean.valueOf(false) : Boolean.valueOf((debug1.distanceToSqr(debug3.getX() + 0.5D, debug3.getY() + 0.5D, debug3.getZ() + 0.5D) <= 64.0D)), 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  61 */         Boolean.valueOf(true))).booleanValue();
/*     */   }
/*     */   
/*     */   public MenuType<?> getType() {
/*  65 */     if (this.menuType == null) {
/*  66 */       throw new UnsupportedOperationException("Unable to construct this menu by type");
/*     */     }
/*  68 */     return this.menuType;
/*     */   }
/*     */   
/*     */   protected static void checkContainerSize(Container debug0, int debug1) {
/*  72 */     int debug2 = debug0.getContainerSize();
/*  73 */     if (debug2 < debug1) {
/*  74 */       throw new IllegalArgumentException("Container size " + debug2 + " is smaller than expected " + debug1);
/*     */     }
/*     */   }
/*     */   
/*     */   protected static void checkContainerDataCount(ContainerData debug0, int debug1) {
/*  79 */     int debug2 = debug0.getCount();
/*  80 */     if (debug2 < debug1) {
/*  81 */       throw new IllegalArgumentException("Container data count " + debug2 + " is smaller than expected " + debug1);
/*     */     }
/*     */   }
/*     */   
/*     */   protected Slot addSlot(Slot debug1) {
/*  86 */     debug1.index = this.slots.size();
/*  87 */     this.slots.add(debug1);
/*  88 */     this.lastSlots.add(ItemStack.EMPTY);
/*  89 */     return debug1;
/*     */   }
/*     */   
/*     */   protected DataSlot addDataSlot(DataSlot debug1) {
/*  93 */     this.dataSlots.add(debug1);
/*  94 */     return debug1;
/*     */   }
/*     */   
/*     */   protected void addDataSlots(ContainerData debug1) {
/*  98 */     for (int debug2 = 0; debug2 < debug1.getCount(); debug2++) {
/*  99 */       addDataSlot(DataSlot.forContainer(debug1, debug2));
/*     */     }
/*     */   }
/*     */   
/*     */   public void addSlotListener(ContainerListener debug1) {
/* 104 */     if (this.containerListeners.contains(debug1)) {
/*     */       return;
/*     */     }
/* 107 */     this.containerListeners.add(debug1);
/*     */     
/* 109 */     debug1.refreshContainer(this, getItems());
/* 110 */     broadcastChanges();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NonNullList<ItemStack> getItems() {
/* 118 */     NonNullList<ItemStack> debug1 = NonNullList.create();
/* 119 */     for (int debug2 = 0; debug2 < this.slots.size(); debug2++) {
/* 120 */       debug1.add(((Slot)this.slots.get(debug2)).getItem());
/*     */     }
/* 122 */     return debug1;
/*     */   }
/*     */   public void broadcastChanges() {
/*     */     int debug1;
/* 126 */     for (debug1 = 0; debug1 < this.slots.size(); debug1++) {
/* 127 */       ItemStack debug2 = ((Slot)this.slots.get(debug1)).getItem();
/* 128 */       ItemStack debug3 = (ItemStack)this.lastSlots.get(debug1);
/* 129 */       if (!ItemStack.matches(debug3, debug2)) {
/* 130 */         ItemStack debug4 = debug2.copy();
/* 131 */         this.lastSlots.set(debug1, debug4);
/* 132 */         for (ContainerListener debug6 : this.containerListeners) {
/* 133 */           debug6.slotChanged(this, debug1, debug4);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 138 */     for (debug1 = 0; debug1 < this.dataSlots.size(); debug1++) {
/* 139 */       DataSlot debug2 = this.dataSlots.get(debug1);
/* 140 */       if (debug2.checkAndClearUpdateFlag()) {
/* 141 */         for (ContainerListener debug4 : this.containerListeners) {
/* 142 */           debug4.setContainerData(this, debug1, debug2.get());
/*     */         }
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean clickMenuButton(Player debug1, int debug2) {
/* 149 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Slot getSlot(int debug1) {
/* 164 */     return this.slots.get(debug1);
/*     */   }
/*     */   
/*     */   public ItemStack quickMoveStack(Player debug1, int debug2) {
/* 168 */     Slot debug3 = this.slots.get(debug2);
/* 169 */     if (debug3 != null) {
/* 170 */       return debug3.getItem();
/*     */     }
/* 172 */     return ItemStack.EMPTY;
/*     */   }
/*     */   
/*     */   public ItemStack clicked(int debug1, int debug2, ClickType debug3, Player debug4) {
/*     */     try {
/* 177 */       return doClick(debug1, debug2, debug3, debug4);
/* 178 */     } catch (Exception debug5) {
/* 179 */       CrashReport debug6 = CrashReport.forThrowable(debug5, "Container click");
/* 180 */       CrashReportCategory debug7 = debug6.addCategory("Click info");
/* 181 */       debug7.setDetail("Menu Type", () -> (this.menuType != null) ? Registry.MENU.getKey(this.menuType).toString() : "<no type>");
/* 182 */       debug7.setDetail("Menu Class", () -> getClass().getCanonicalName());
/* 183 */       debug7.setDetail("Slot Count", Integer.valueOf(this.slots.size()));
/* 184 */       debug7.setDetail("Slot", Integer.valueOf(debug1));
/* 185 */       debug7.setDetail("Button", Integer.valueOf(debug2));
/* 186 */       debug7.setDetail("Type", debug3);
/* 187 */       throw new ReportedException(debug6);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private ItemStack doClick(int debug1, int debug2, ClickType debug3, Player debug4) {
/* 193 */     ItemStack debug5 = ItemStack.EMPTY;
/* 194 */     Inventory debug6 = debug4.inventory;
/*     */     
/* 196 */     if (debug3 == ClickType.QUICK_CRAFT) {
/* 197 */       int debug7 = this.quickcraftStatus;
/* 198 */       this.quickcraftStatus = getQuickcraftHeader(debug2);
/*     */       
/* 200 */       if ((debug7 != 1 || this.quickcraftStatus != 2) && debug7 != this.quickcraftStatus) {
/* 201 */         resetQuickCraft();
/* 202 */       } else if (debug6.getCarried().isEmpty()) {
/* 203 */         resetQuickCraft();
/* 204 */       } else if (this.quickcraftStatus == 0) {
/* 205 */         this.quickcraftType = getQuickcraftType(debug2);
/*     */         
/* 207 */         if (isValidQuickcraftType(this.quickcraftType, debug4)) {
/* 208 */           this.quickcraftStatus = 1;
/* 209 */           this.quickcraftSlots.clear();
/*     */         } else {
/* 211 */           resetQuickCraft();
/*     */         } 
/* 213 */       } else if (this.quickcraftStatus == 1) {
/* 214 */         Slot debug8 = this.slots.get(debug1);
/*     */         
/* 216 */         ItemStack debug9 = debug6.getCarried();
/* 217 */         if (debug8 != null && canItemQuickReplace(debug8, debug9, true) && debug8.mayPlace(debug9) && (this.quickcraftType == 2 || debug9.getCount() > this.quickcraftSlots.size()) && canDragTo(debug8)) {
/* 218 */           this.quickcraftSlots.add(debug8);
/*     */         }
/* 220 */       } else if (this.quickcraftStatus == 2) {
/* 221 */         if (!this.quickcraftSlots.isEmpty()) {
/* 222 */           ItemStack debug8 = debug6.getCarried().copy();
/* 223 */           int debug9 = debug6.getCarried().getCount();
/*     */           
/* 225 */           for (Slot debug11 : this.quickcraftSlots) {
/* 226 */             ItemStack debug12 = debug6.getCarried();
/* 227 */             if (debug11 != null && canItemQuickReplace(debug11, debug12, true) && debug11.mayPlace(debug12) && (this.quickcraftType == 2 || debug12.getCount() >= this.quickcraftSlots.size()) && canDragTo(debug11)) {
/* 228 */               ItemStack debug13 = debug8.copy();
/* 229 */               int debug14 = debug11.hasItem() ? debug11.getItem().getCount() : 0;
/* 230 */               getQuickCraftSlotCount(this.quickcraftSlots, this.quickcraftType, debug13, debug14);
/*     */               
/* 232 */               int debug15 = Math.min(debug13.getMaxStackSize(), debug11.getMaxStackSize(debug13));
/* 233 */               if (debug13.getCount() > debug15) {
/* 234 */                 debug13.setCount(debug15);
/*     */               }
/*     */               
/* 237 */               debug9 -= debug13.getCount() - debug14;
/* 238 */               debug11.set(debug13);
/*     */             } 
/*     */           } 
/*     */           
/* 242 */           debug8.setCount(debug9);
/* 243 */           debug6.setCarried(debug8);
/*     */         } 
/*     */         
/* 246 */         resetQuickCraft();
/*     */       } else {
/* 248 */         resetQuickCraft();
/*     */       } 
/* 250 */     } else if (this.quickcraftStatus != 0) {
/* 251 */       resetQuickCraft();
/* 252 */     } else if ((debug3 == ClickType.PICKUP || debug3 == ClickType.QUICK_MOVE) && (debug2 == 0 || debug2 == 1)) {
/* 253 */       if (debug1 == -999) {
/* 254 */         if (!debug6.getCarried().isEmpty()) {
/* 255 */           if (debug2 == 0) {
/* 256 */             debug4.drop(debug6.getCarried(), true);
/* 257 */             debug6.setCarried(ItemStack.EMPTY);
/*     */           } 
/* 259 */           if (debug2 == 1) {
/* 260 */             debug4.drop(debug6.getCarried().split(1), true);
/*     */           }
/*     */         } 
/* 263 */       } else if (debug3 == ClickType.QUICK_MOVE) {
/* 264 */         if (debug1 < 0) {
/* 265 */           return ItemStack.EMPTY;
/*     */         }
/* 267 */         Slot debug7 = this.slots.get(debug1);
/* 268 */         if (debug7 == null || !debug7.mayPickup(debug4)) {
/* 269 */           return ItemStack.EMPTY;
/*     */         }
/*     */         
/* 272 */         ItemStack debug8 = quickMoveStack(debug4, debug1);
/* 273 */         while (!debug8.isEmpty() && ItemStack.isSame(debug7.getItem(), debug8)) {
/* 274 */           debug5 = debug8.copy();
/* 275 */           debug8 = quickMoveStack(debug4, debug1);
/*     */         } 
/*     */       } else {
/* 278 */         if (debug1 < 0) {
/* 279 */           return ItemStack.EMPTY;
/*     */         }
/*     */         
/* 282 */         Slot debug7 = this.slots.get(debug1);
/* 283 */         if (debug7 != null) {
/* 284 */           ItemStack debug8 = debug7.getItem();
/* 285 */           ItemStack debug9 = debug6.getCarried();
/*     */           
/* 287 */           if (!debug8.isEmpty()) {
/* 288 */             debug5 = debug8.copy();
/*     */           }
/*     */           
/* 291 */           if (debug8.isEmpty()) {
/* 292 */             if (!debug9.isEmpty() && debug7.mayPlace(debug9)) {
/* 293 */               int debug10 = (debug2 == 0) ? debug9.getCount() : 1;
/* 294 */               if (debug10 > debug7.getMaxStackSize(debug9)) {
/* 295 */                 debug10 = debug7.getMaxStackSize(debug9);
/*     */               }
/*     */               
/* 298 */               debug7.set(debug9.split(debug10));
/*     */             } 
/* 300 */           } else if (debug7.mayPickup(debug4)) {
/* 301 */             if (debug9.isEmpty()) {
/* 302 */               if (debug8.isEmpty()) {
/* 303 */                 debug7.set(ItemStack.EMPTY);
/* 304 */                 debug6.setCarried(ItemStack.EMPTY);
/*     */               } else {
/* 306 */                 int debug10 = (debug2 == 0) ? debug8.getCount() : ((debug8.getCount() + 1) / 2);
/*     */                 
/* 308 */                 debug6.setCarried(debug7.remove(debug10));
/* 309 */                 if (debug8.isEmpty()) {
/* 310 */                   debug7.set(ItemStack.EMPTY);
/*     */                 }
/* 312 */                 debug7.onTake(debug4, debug6.getCarried());
/*     */               } 
/* 314 */             } else if (debug7.mayPlace(debug9)) {
/*     */               
/* 316 */               if (consideredTheSameItem(debug8, debug9)) {
/*     */                 
/* 318 */                 int debug10 = (debug2 == 0) ? debug9.getCount() : 1;
/* 319 */                 if (debug10 > debug7.getMaxStackSize(debug9) - debug8.getCount()) {
/* 320 */                   debug10 = debug7.getMaxStackSize(debug9) - debug8.getCount();
/*     */                 }
/* 322 */                 if (debug10 > debug9.getMaxStackSize() - debug8.getCount()) {
/* 323 */                   debug10 = debug9.getMaxStackSize() - debug8.getCount();
/*     */                 }
/* 325 */                 debug9.shrink(debug10);
/* 326 */                 debug8.grow(debug10);
/*     */               
/*     */               }
/* 329 */               else if (debug9.getCount() <= debug7.getMaxStackSize(debug9)) {
/* 330 */                 debug7.set(debug9);
/* 331 */                 debug6.setCarried(debug8);
/*     */               }
/*     */             
/* 334 */             } else if (debug9.getMaxStackSize() > 1 && consideredTheSameItem(debug8, debug9)) {
/*     */               
/* 336 */               if (!debug8.isEmpty()) {
/* 337 */                 int debug10 = debug8.getCount();
/* 338 */                 if (debug10 + debug9.getCount() <= debug9.getMaxStackSize()) {
/* 339 */                   debug9.grow(debug10);
/* 340 */                   debug8 = debug7.remove(debug10);
/* 341 */                   if (debug8.isEmpty()) {
/* 342 */                     debug7.set(ItemStack.EMPTY);
/*     */                   }
/* 344 */                   debug7.onTake(debug4, debug6.getCarried());
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           } 
/* 349 */           debug7.setChanged();
/*     */         } 
/*     */       } 
/* 352 */     } else if (debug3 == ClickType.SWAP) {
/* 353 */       Slot debug7 = this.slots.get(debug1);
/* 354 */       ItemStack debug8 = debug6.getItem(debug2);
/*     */       
/* 356 */       ItemStack debug9 = debug7.getItem();
/* 357 */       if (!debug8.isEmpty() || !debug9.isEmpty())
/*     */       {
/* 359 */         if (debug8.isEmpty()) {
/*     */           
/* 361 */           if (debug7.mayPickup(debug4)) {
/* 362 */             debug6.setItem(debug2, debug9);
/* 363 */             debug7.onSwapCraft(debug9.getCount());
/* 364 */             debug7.set(ItemStack.EMPTY);
/* 365 */             debug7.onTake(debug4, debug9);
/*     */           } 
/* 367 */         } else if (debug9.isEmpty()) {
/* 368 */           if (debug7.mayPlace(debug8)) {
/* 369 */             int debug10 = debug7.getMaxStackSize(debug8);
/* 370 */             if (debug8.getCount() > debug10) {
/*     */               
/* 372 */               debug7.set(debug8.split(debug10));
/*     */             } else {
/*     */               
/* 375 */               debug7.set(debug8);
/* 376 */               debug6.setItem(debug2, ItemStack.EMPTY);
/*     */             } 
/*     */           } 
/* 379 */         } else if (debug7.mayPickup(debug4) && debug7.mayPlace(debug8)) {
/*     */ 
/*     */           
/* 382 */           int debug10 = debug7.getMaxStackSize(debug8);
/* 383 */           if (debug8.getCount() > debug10) {
/* 384 */             debug7.set(debug8.split(debug10));
/* 385 */             debug7.onTake(debug4, debug9);
/* 386 */             if (!debug6.add(debug9)) {
/* 387 */               debug4.drop(debug9, true);
/*     */             }
/*     */           } else {
/* 390 */             debug7.set(debug8);
/* 391 */             debug6.setItem(debug2, debug9);
/* 392 */             debug7.onTake(debug4, debug9);
/*     */           } 
/*     */         }  } 
/* 395 */     } else if (debug3 == ClickType.CLONE && debug4.abilities.instabuild && debug6.getCarried().isEmpty() && debug1 >= 0) {
/* 396 */       Slot debug7 = this.slots.get(debug1);
/* 397 */       if (debug7 != null && debug7.hasItem()) {
/* 398 */         ItemStack debug8 = debug7.getItem().copy();
/* 399 */         debug8.setCount(debug8.getMaxStackSize());
/* 400 */         debug6.setCarried(debug8);
/*     */       } 
/* 402 */     } else if (debug3 == ClickType.THROW && debug6.getCarried().isEmpty() && debug1 >= 0) {
/* 403 */       Slot debug7 = this.slots.get(debug1);
/* 404 */       if (debug7 != null && debug7.hasItem() && debug7.mayPickup(debug4)) {
/* 405 */         ItemStack debug8 = debug7.remove((debug2 == 0) ? 1 : debug7.getItem().getCount());
/* 406 */         debug7.onTake(debug4, debug8);
/* 407 */         debug4.drop(debug8, true);
/*     */       } 
/* 409 */     } else if (debug3 == ClickType.PICKUP_ALL && debug1 >= 0) {
/* 410 */       Slot debug7 = this.slots.get(debug1);
/* 411 */       ItemStack debug8 = debug6.getCarried();
/*     */       
/* 413 */       if (!debug8.isEmpty() && (debug7 == null || !debug7.hasItem() || !debug7.mayPickup(debug4))) {
/* 414 */         int debug9 = (debug2 == 0) ? 0 : (this.slots.size() - 1);
/* 415 */         int debug10 = (debug2 == 0) ? 1 : -1;
/*     */         
/* 417 */         for (int debug11 = 0; debug11 < 2; debug11++) {
/*     */           int debug12;
/* 419 */           for (debug12 = debug9; debug12 >= 0 && debug12 < this.slots.size() && debug8.getCount() < debug8.getMaxStackSize(); debug12 += debug10) {
/* 420 */             Slot debug13 = this.slots.get(debug12);
/*     */             
/* 422 */             if (debug13.hasItem() && canItemQuickReplace(debug13, debug8, true) && debug13.mayPickup(debug4) && canTakeItemForPickAll(debug8, debug13)) {
/* 423 */               ItemStack debug14 = debug13.getItem();
/* 424 */               if (debug11 != 0 || debug14.getCount() != debug14.getMaxStackSize()) {
/*     */ 
/*     */                 
/* 427 */                 int debug15 = Math.min(debug8.getMaxStackSize() - debug8.getCount(), debug14.getCount());
/* 428 */                 ItemStack debug16 = debug13.remove(debug15);
/* 429 */                 debug8.grow(debug15);
/*     */                 
/* 431 */                 if (debug16.isEmpty()) {
/* 432 */                   debug13.set(ItemStack.EMPTY);
/*     */                 }
/* 434 */                 debug13.onTake(debug4, debug16);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 440 */       broadcastChanges();
/*     */     } 
/*     */     
/* 443 */     return debug5;
/*     */   }
/*     */   
/*     */   public static boolean consideredTheSameItem(ItemStack debug0, ItemStack debug1) {
/* 447 */     return (debug0.getItem() == debug1.getItem() && ItemStack.tagMatches(debug0, debug1));
/*     */   }
/*     */   
/*     */   public boolean canTakeItemForPickAll(ItemStack debug1, Slot debug2) {
/* 451 */     return true;
/*     */   }
/*     */   
/*     */   public void removed(Player debug1) {
/* 455 */     Inventory debug2 = debug1.inventory;
/* 456 */     if (!debug2.getCarried().isEmpty()) {
/* 457 */       debug1.drop(debug2.getCarried(), false);
/* 458 */       debug2.setCarried(ItemStack.EMPTY);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void clearContainer(Player debug1, Level debug2, Container debug3) {
/* 463 */     if (!debug1.isAlive() || (debug1 instanceof ServerPlayer && ((ServerPlayer)debug1).hasDisconnected())) {
/* 464 */       for (int i = 0; i < debug3.getContainerSize(); i++) {
/* 465 */         debug1.drop(debug3.removeItemNoUpdate(i), false);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 470 */     for (int debug4 = 0; debug4 < debug3.getContainerSize(); debug4++) {
/* 471 */       debug1.inventory.placeItemBackInInventory(debug2, debug3.removeItemNoUpdate(debug4));
/*     */     }
/*     */   }
/*     */   
/*     */   public void slotsChanged(Container debug1) {
/* 476 */     broadcastChanges();
/*     */   }
/*     */   
/*     */   public void setItem(int debug1, ItemStack debug2) {
/* 480 */     getSlot(debug1).set(debug2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setData(int debug1, int debug2) {
/* 499 */     ((DataSlot)this.dataSlots.get(debug1)).set(debug2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractContainerMenu(@Nullable MenuType<?> debug1, int debug2) {
/* 507 */     this.unSynchedPlayers = Sets.newHashSet();
/*     */     this.menuType = debug1;
/*     */     this.containerId = debug2; } public boolean isSynched(Player debug1) {
/* 510 */     return !this.unSynchedPlayers.contains(debug1);
/*     */   }
/*     */   
/*     */   public void setSynched(Player debug1, boolean debug2) {
/* 514 */     if (debug2) {
/* 515 */       this.unSynchedPlayers.remove(debug1);
/*     */     } else {
/* 517 */       this.unSynchedPlayers.add(debug1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean moveItemStackTo(ItemStack debug1, int debug2, int debug3, boolean debug4) {
/* 524 */     boolean debug5 = false;
/*     */     
/* 526 */     int debug6 = debug2;
/* 527 */     if (debug4) {
/* 528 */       debug6 = debug3 - 1;
/*     */     }
/*     */ 
/*     */     
/* 532 */     if (debug1.isStackable()) {
/* 533 */       while (!debug1.isEmpty() && (debug4 ? (debug6 >= debug2) : (debug6 < debug3))) {
/* 534 */         Slot debug7 = this.slots.get(debug6);
/* 535 */         ItemStack debug8 = debug7.getItem();
/* 536 */         if (!debug8.isEmpty() && consideredTheSameItem(debug1, debug8)) {
/* 537 */           int debug9 = debug8.getCount() + debug1.getCount();
/* 538 */           if (debug9 <= debug1.getMaxStackSize()) {
/* 539 */             debug1.setCount(0);
/* 540 */             debug8.setCount(debug9);
/* 541 */             debug7.setChanged();
/* 542 */             debug5 = true;
/* 543 */           } else if (debug8.getCount() < debug1.getMaxStackSize()) {
/* 544 */             debug1.shrink(debug1.getMaxStackSize() - debug8.getCount());
/* 545 */             debug8.setCount(debug1.getMaxStackSize());
/* 546 */             debug7.setChanged();
/* 547 */             debug5 = true;
/*     */           } 
/*     */         } 
/*     */         
/* 551 */         if (debug4) {
/* 552 */           debug6--; continue;
/*     */         } 
/* 554 */         debug6++;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 560 */     if (!debug1.isEmpty()) {
/* 561 */       if (debug4) {
/* 562 */         debug6 = debug3 - 1;
/*     */       } else {
/* 564 */         debug6 = debug2;
/*     */       } 
/* 566 */       while (debug4 ? (debug6 >= debug2) : (debug6 < debug3)) {
/* 567 */         Slot debug7 = this.slots.get(debug6);
/* 568 */         ItemStack debug8 = debug7.getItem();
/*     */         
/* 570 */         if (debug8.isEmpty() && debug7.mayPlace(debug1)) {
/* 571 */           if (debug1.getCount() > debug7.getMaxStackSize()) {
/* 572 */             debug7.set(debug1.split(debug7.getMaxStackSize()));
/*     */           } else {
/* 574 */             debug7.set(debug1.split(debug1.getCount()));
/*     */           } 
/* 576 */           debug7.setChanged();
/* 577 */           debug5 = true;
/*     */           
/*     */           break;
/*     */         } 
/* 581 */         if (debug4) {
/* 582 */           debug6--; continue;
/*     */         } 
/* 584 */         debug6++;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 589 */     return debug5;
/*     */   }
/*     */   
/*     */   public static int getQuickcraftType(int debug0) {
/* 593 */     return debug0 >> 2 & 0x3;
/*     */   }
/*     */   
/*     */   public static int getQuickcraftHeader(int debug0) {
/* 597 */     return debug0 & 0x3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isValidQuickcraftType(int debug0, Player debug1) {
/* 605 */     if (debug0 == 0) {
/* 606 */       return true;
/*     */     }
/* 608 */     if (debug0 == 1) {
/* 609 */       return true;
/*     */     }
/* 611 */     if (debug0 == 2 && debug1.abilities.instabuild) {
/* 612 */       return true;
/*     */     }
/* 614 */     return false;
/*     */   }
/*     */   
/*     */   protected void resetQuickCraft() {
/* 618 */     this.quickcraftStatus = 0;
/* 619 */     this.quickcraftSlots.clear();
/*     */   }
/*     */   
/*     */   public static boolean canItemQuickReplace(@Nullable Slot debug0, ItemStack debug1, boolean debug2) {
/* 623 */     boolean debug3 = (debug0 == null || !debug0.hasItem());
/*     */     
/* 625 */     if (!debug3 && debug1.sameItem(debug0.getItem()) && ItemStack.tagMatches(debug0.getItem(), debug1)) {
/* 626 */       return (debug0.getItem().getCount() + (debug2 ? 0 : debug1.getCount()) <= debug1.getMaxStackSize());
/*     */     }
/*     */     
/* 629 */     return debug3;
/*     */   }
/*     */   
/*     */   public static void getQuickCraftSlotCount(Set<Slot> debug0, int debug1, ItemStack debug2, int debug3) {
/* 633 */     switch (debug1) {
/*     */       case 0:
/* 635 */         debug2.setCount(Mth.floor(debug2.getCount() / debug0.size()));
/*     */         break;
/*     */       case 1:
/* 638 */         debug2.setCount(1);
/*     */         break;
/*     */       case 2:
/* 641 */         debug2.setCount(debug2.getItem().getMaxStackSize());
/*     */         break;
/*     */     } 
/*     */     
/* 645 */     debug2.grow(debug3);
/*     */   }
/*     */   
/*     */   public boolean canDragTo(Slot debug1) {
/* 649 */     return true;
/*     */   }
/*     */   
/*     */   public static int getRedstoneSignalFromBlockEntity(@Nullable BlockEntity debug0) {
/* 653 */     if (debug0 instanceof Container) {
/* 654 */       return getRedstoneSignalFromContainer((Container)debug0);
/*     */     }
/*     */     
/* 657 */     return 0;
/*     */   }
/*     */   
/*     */   public static int getRedstoneSignalFromContainer(@Nullable Container debug0) {
/* 661 */     if (debug0 == null) {
/* 662 */       return 0;
/*     */     }
/* 664 */     int debug1 = 0;
/* 665 */     float debug2 = 0.0F;
/*     */     
/* 667 */     for (int debug3 = 0; debug3 < debug0.getContainerSize(); debug3++) {
/* 668 */       ItemStack debug4 = debug0.getItem(debug3);
/*     */       
/* 670 */       if (!debug4.isEmpty()) {
/* 671 */         debug2 += debug4.getCount() / Math.min(debug0.getMaxStackSize(), debug4.getMaxStackSize());
/* 672 */         debug1++;
/*     */       } 
/*     */     } 
/*     */     
/* 676 */     debug2 /= debug0.getContainerSize();
/* 677 */     return Mth.floor(debug2 * 14.0F) + ((debug1 > 0) ? 1 : 0);
/*     */   }
/*     */   
/*     */   public abstract boolean stillValid(Player paramPlayer);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\inventory\AbstractContainerMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */