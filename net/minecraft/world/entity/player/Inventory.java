/*     */ package net.minecraft.world.entity.player;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.ContainerHelper;
/*     */ import net.minecraft.world.Nameable;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Inventory
/*     */   implements Container, Nameable
/*     */ {
/*  37 */   public final NonNullList<ItemStack> items = NonNullList.withSize(36, ItemStack.EMPTY);
/*  38 */   public final NonNullList<ItemStack> armor = NonNullList.withSize(4, ItemStack.EMPTY);
/*  39 */   public final NonNullList<ItemStack> offhand = NonNullList.withSize(1, ItemStack.EMPTY);
/*  40 */   private final List<NonNullList<ItemStack>> compartments = (List<NonNullList<ItemStack>>)ImmutableList.of(this.items, this.armor, this.offhand);
/*     */   
/*     */   public int selected;
/*     */   public final Player player;
/*  44 */   private ItemStack carried = ItemStack.EMPTY;
/*     */   
/*     */   private int timesChanged;
/*     */   
/*     */   public Inventory(Player debug1) {
/*  49 */     this.player = debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack getSelected() {
/*  55 */     if (isHotbarSlot(this.selected)) {
/*  56 */       return (ItemStack)this.items.get(this.selected);
/*     */     }
/*  58 */     return ItemStack.EMPTY;
/*     */   }
/*     */   
/*     */   public static int getSelectionSize() {
/*  62 */     return 9;
/*     */   }
/*     */   
/*     */   private boolean hasRemainingSpaceForItem(ItemStack debug1, ItemStack debug2) {
/*  66 */     return (!debug1.isEmpty() && 
/*  67 */       isSameItem(debug1, debug2) && debug1
/*  68 */       .isStackable() && debug1
/*  69 */       .getCount() < debug1.getMaxStackSize() && debug1
/*  70 */       .getCount() < getMaxStackSize());
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isSameItem(ItemStack debug1, ItemStack debug2) {
/*  75 */     return (debug1.getItem() == debug2.getItem() && ItemStack.tagMatches(debug1, debug2));
/*     */   }
/*     */   
/*     */   public int getFreeSlot() {
/*  79 */     for (int debug1 = 0; debug1 < this.items.size(); debug1++) {
/*  80 */       if (((ItemStack)this.items.get(debug1)).isEmpty()) {
/*  81 */         return debug1;
/*     */       }
/*     */     } 
/*  84 */     return -1;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void pickSlot(int debug1) {
/* 112 */     this.selected = getSuitableHotbarSlot();
/*     */ 
/*     */     
/* 115 */     ItemStack debug2 = (ItemStack)this.items.get(this.selected);
/* 116 */     this.items.set(this.selected, this.items.get(debug1));
/* 117 */     this.items.set(debug1, debug2);
/*     */   }
/*     */   
/*     */   public static boolean isHotbarSlot(int debug0) {
/* 121 */     return (debug0 >= 0 && debug0 < 9);
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
/*     */   public int findSlotMatchingUnusedItem(ItemStack debug1) {
/* 134 */     for (int debug2 = 0; debug2 < this.items.size(); debug2++) {
/* 135 */       ItemStack debug3 = (ItemStack)this.items.get(debug2);
/* 136 */       if (!((ItemStack)this.items.get(debug2)).isEmpty() && 
/* 137 */         isSameItem(debug1, (ItemStack)this.items.get(debug2)) && 
/* 138 */         !((ItemStack)this.items.get(debug2)).isDamaged() && 
/* 139 */         !debug3.isEnchanted() && 
/* 140 */         !debug3.hasCustomHoverName())
/*     */       {
/* 142 */         return debug2;
/*     */       }
/*     */     } 
/* 145 */     return -1;
/*     */   }
/*     */   
/*     */   public int getSuitableHotbarSlot() {
/*     */     int debug1;
/* 150 */     for (debug1 = 0; debug1 < 9; debug1++) {
/* 151 */       int debug2 = (this.selected + debug1) % 9;
/*     */       
/* 153 */       if (((ItemStack)this.items.get(debug2)).isEmpty()) {
/* 154 */         return debug2;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 159 */     for (debug1 = 0; debug1 < 9; debug1++) {
/* 160 */       int debug2 = (this.selected + debug1) % 9;
/*     */       
/* 162 */       if (!((ItemStack)this.items.get(debug2)).isEnchanted()) {
/* 163 */         return debug2;
/*     */       }
/*     */     } 
/*     */     
/* 167 */     return this.selected;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public int clearOrCountMatchingItems(Predicate<ItemStack> debug1, int debug2, Container debug3) {
/* 189 */     int debug4 = 0;
/* 190 */     boolean debug5 = (debug2 == 0);
/*     */     
/* 192 */     debug4 += ContainerHelper.clearOrCountMatchingItems(this, debug1, debug2 - debug4, debug5);
/* 193 */     debug4 += ContainerHelper.clearOrCountMatchingItems(debug3, debug1, debug2 - debug4, debug5);
/* 194 */     debug4 += ContainerHelper.clearOrCountMatchingItems(this.carried, debug1, debug2 - debug4, debug5);
/* 195 */     if (this.carried.isEmpty()) {
/* 196 */       this.carried = ItemStack.EMPTY;
/*     */     }
/* 198 */     return debug4;
/*     */   }
/*     */   
/*     */   private int addResource(ItemStack debug1) {
/* 202 */     int debug2 = getSlotWithRemainingSpace(debug1);
/* 203 */     if (debug2 == -1) {
/* 204 */       debug2 = getFreeSlot();
/*     */     }
/* 206 */     if (debug2 == -1) {
/* 207 */       return debug1.getCount();
/*     */     }
/* 209 */     return addResource(debug2, debug1);
/*     */   }
/*     */   
/*     */   private int addResource(int debug1, ItemStack debug2) {
/* 213 */     Item debug3 = debug2.getItem();
/* 214 */     int debug4 = debug2.getCount();
/*     */     
/* 216 */     ItemStack debug5 = getItem(debug1);
/* 217 */     if (debug5.isEmpty()) {
/* 218 */       debug5 = new ItemStack((ItemLike)debug3, 0);
/* 219 */       if (debug2.hasTag()) {
/* 220 */         debug5.setTag(debug2.getTag().copy());
/*     */       }
/* 222 */       setItem(debug1, debug5);
/*     */     } 
/*     */     
/* 225 */     int debug6 = debug4;
/* 226 */     if (debug6 > debug5.getMaxStackSize() - debug5.getCount()) {
/* 227 */       debug6 = debug5.getMaxStackSize() - debug5.getCount();
/*     */     }
/* 229 */     if (debug6 > getMaxStackSize() - debug5.getCount()) {
/* 230 */       debug6 = getMaxStackSize() - debug5.getCount();
/*     */     }
/*     */     
/* 233 */     if (debug6 == 0) {
/* 234 */       return debug4;
/*     */     }
/*     */     
/* 237 */     debug4 -= debug6;
/* 238 */     debug5.grow(debug6);
/* 239 */     debug5.setPopTime(5);
/*     */     
/* 241 */     return debug4;
/*     */   }
/*     */   
/*     */   public int getSlotWithRemainingSpace(ItemStack debug1) {
/* 245 */     if (hasRemainingSpaceForItem(getItem(this.selected), debug1)) {
/* 246 */       return this.selected;
/*     */     }
/* 248 */     if (hasRemainingSpaceForItem(getItem(40), debug1)) {
/* 249 */       return 40;
/*     */     }
/* 251 */     for (int debug2 = 0; debug2 < this.items.size(); debug2++) {
/* 252 */       if (hasRemainingSpaceForItem((ItemStack)this.items.get(debug2), debug1)) {
/* 253 */         return debug2;
/*     */       }
/*     */     } 
/* 256 */     return -1;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 260 */     for (NonNullList<ItemStack> debug2 : this.compartments) {
/* 261 */       for (int debug3 = 0; debug3 < debug2.size(); debug3++) {
/* 262 */         if (!((ItemStack)debug2.get(debug3)).isEmpty()) {
/* 263 */           ((ItemStack)debug2.get(debug3)).inventoryTick(this.player.level, (Entity)this.player, debug3, (this.selected == debug3));
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean add(ItemStack debug1) {
/* 270 */     return add(-1, debug1);
/*     */   }
/*     */   
/*     */   public boolean add(int debug1, ItemStack debug2) {
/* 274 */     if (debug2.isEmpty()) {
/* 275 */       return false;
/*     */     }
/*     */     
/*     */     try {
/* 279 */       if (!debug2.isDamaged()) {
/*     */         int debug3;
/*     */         do {
/* 282 */           debug3 = debug2.getCount();
/* 283 */           if (debug1 == -1) {
/* 284 */             debug2.setCount(addResource(debug2));
/*     */           } else {
/* 286 */             debug2.setCount(addResource(debug1, debug2));
/*     */           } 
/* 288 */         } while (!debug2.isEmpty() && debug2.getCount() < debug3);
/* 289 */         if (debug2.getCount() == debug3 && this.player.abilities.instabuild) {
/*     */           
/* 291 */           debug2.setCount(0);
/* 292 */           return true;
/*     */         } 
/* 294 */         return (debug2.getCount() < debug3);
/*     */       } 
/*     */       
/* 297 */       if (debug1 == -1) {
/* 298 */         debug1 = getFreeSlot();
/*     */       }
/* 300 */       if (debug1 >= 0) {
/* 301 */         this.items.set(debug1, debug2.copy());
/* 302 */         ((ItemStack)this.items.get(debug1)).setPopTime(5);
/* 303 */         debug2.setCount(0);
/* 304 */         return true;
/* 305 */       }  if (this.player.abilities.instabuild) {
/*     */         
/* 307 */         debug2.setCount(0);
/* 308 */         return true;
/*     */       } 
/* 310 */       return false;
/* 311 */     } catch (Throwable debug3) {
/* 312 */       CrashReport debug4 = CrashReport.forThrowable(debug3, "Adding item to inventory");
/* 313 */       CrashReportCategory debug5 = debug4.addCategory("Item being added");
/*     */       
/* 315 */       debug5.setDetail("Item ID", Integer.valueOf(Item.getId(debug2.getItem())));
/* 316 */       debug5.setDetail("Item data", Integer.valueOf(debug2.getDamageValue()));
/* 317 */       debug5.setDetail("Item name", () -> debug0.getHoverName().getString());
/*     */       
/* 319 */       throw new ReportedException(debug4);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void placeItemBackInInventory(Level debug1, ItemStack debug2) {
/* 324 */     if (debug1.isClientSide) {
/*     */       return;
/*     */     }
/*     */     
/* 328 */     while (!debug2.isEmpty()) {
/* 329 */       int debug3 = getSlotWithRemainingSpace(debug2);
/* 330 */       if (debug3 == -1) {
/* 331 */         debug3 = getFreeSlot();
/*     */       }
/*     */       
/* 334 */       if (debug3 == -1) {
/* 335 */         this.player.drop(debug2, false);
/*     */         
/*     */         break;
/*     */       } 
/* 339 */       int debug4 = debug2.getMaxStackSize() - getItem(debug3).getCount();
/*     */       
/* 341 */       if (add(debug3, debug2.split(debug4))) {
/* 342 */         ((ServerPlayer)this.player).connection.send((Packet)new ClientboundContainerSetSlotPacket(-2, debug3, getItem(debug3)));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public ItemStack removeItem(int debug1, int debug2) {
/*     */     NonNullList<ItemStack> nonNullList;
/* 349 */     List<ItemStack> debug3 = null;
/*     */     
/* 351 */     for (NonNullList<ItemStack> debug5 : this.compartments) {
/* 352 */       if (debug1 < debug5.size()) {
/* 353 */         nonNullList = debug5;
/*     */         break;
/*     */       } 
/* 356 */       debug1 -= debug5.size();
/*     */     } 
/*     */ 
/*     */     
/* 360 */     if (nonNullList != null && !((ItemStack)nonNullList.get(debug1)).isEmpty()) {
/* 361 */       return ContainerHelper.removeItem((List)nonNullList, debug1, debug2);
/*     */     }
/* 363 */     return ItemStack.EMPTY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeItem(ItemStack debug1) {
/* 372 */     for (NonNullList<ItemStack> debug3 : this.compartments) {
/* 373 */       for (int debug4 = 0; debug4 < debug3.size(); debug4++) {
/* 374 */         if (debug3.get(debug4) == debug1) {
/* 375 */           debug3.set(debug4, ItemStack.EMPTY);
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItemNoUpdate(int debug1) {
/* 384 */     NonNullList<ItemStack> debug2 = null;
/*     */     
/* 386 */     for (NonNullList<ItemStack> debug4 : this.compartments) {
/* 387 */       if (debug1 < debug4.size()) {
/* 388 */         debug2 = debug4;
/*     */         break;
/*     */       } 
/* 391 */       debug1 -= debug4.size();
/*     */     } 
/*     */     
/* 394 */     if (debug2 != null && !((ItemStack)debug2.get(debug1)).isEmpty()) {
/* 395 */       ItemStack debug3 = (ItemStack)debug2.get(debug1);
/* 396 */       debug2.set(debug1, ItemStack.EMPTY);
/* 397 */       return debug3;
/*     */     } 
/* 399 */     return ItemStack.EMPTY;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItem(int debug1, ItemStack debug2) {
/* 404 */     NonNullList<ItemStack> debug3 = null;
/*     */     
/* 406 */     for (NonNullList<ItemStack> debug5 : this.compartments) {
/* 407 */       if (debug1 < debug5.size()) {
/* 408 */         debug3 = debug5;
/*     */         break;
/*     */       } 
/* 411 */       debug1 -= debug5.size();
/*     */     } 
/*     */ 
/*     */     
/* 415 */     if (debug3 != null) {
/* 416 */       debug3.set(debug1, debug2);
/*     */     }
/*     */   }
/*     */   
/*     */   public float getDestroySpeed(BlockState debug1) {
/* 421 */     return ((ItemStack)this.items.get(this.selected)).getDestroySpeed(debug1);
/*     */   }
/*     */   
/*     */   public ListTag save(ListTag debug1) {
/*     */     int debug2;
/* 426 */     for (debug2 = 0; debug2 < this.items.size(); debug2++) {
/* 427 */       if (!((ItemStack)this.items.get(debug2)).isEmpty()) {
/* 428 */         CompoundTag debug3 = new CompoundTag();
/* 429 */         debug3.putByte("Slot", (byte)debug2);
/* 430 */         ((ItemStack)this.items.get(debug2)).save(debug3);
/* 431 */         debug1.add(debug3);
/*     */       } 
/*     */     } 
/* 434 */     for (debug2 = 0; debug2 < this.armor.size(); debug2++) {
/* 435 */       if (!((ItemStack)this.armor.get(debug2)).isEmpty()) {
/* 436 */         CompoundTag debug3 = new CompoundTag();
/* 437 */         debug3.putByte("Slot", (byte)(debug2 + 100));
/* 438 */         ((ItemStack)this.armor.get(debug2)).save(debug3);
/* 439 */         debug1.add(debug3);
/*     */       } 
/*     */     } 
/* 442 */     for (debug2 = 0; debug2 < this.offhand.size(); debug2++) {
/* 443 */       if (!((ItemStack)this.offhand.get(debug2)).isEmpty()) {
/* 444 */         CompoundTag debug3 = new CompoundTag();
/* 445 */         debug3.putByte("Slot", (byte)(debug2 + 150));
/* 446 */         ((ItemStack)this.offhand.get(debug2)).save(debug3);
/* 447 */         debug1.add(debug3);
/*     */       } 
/*     */     } 
/* 450 */     return debug1;
/*     */   }
/*     */   
/*     */   public void load(ListTag debug1) {
/* 454 */     this.items.clear();
/* 455 */     this.armor.clear();
/* 456 */     this.offhand.clear();
/* 457 */     for (int debug2 = 0; debug2 < debug1.size(); debug2++) {
/* 458 */       CompoundTag debug3 = debug1.getCompound(debug2);
/* 459 */       int debug4 = debug3.getByte("Slot") & 0xFF;
/* 460 */       ItemStack debug5 = ItemStack.of(debug3);
/* 461 */       if (!debug5.isEmpty()) {
/* 462 */         if (debug4 >= 0 && debug4 < this.items.size()) {
/* 463 */           this.items.set(debug4, debug5);
/* 464 */         } else if (debug4 >= 100 && debug4 < this.armor.size() + 100) {
/* 465 */           this.armor.set(debug4 - 100, debug5);
/* 466 */         } else if (debug4 >= 150 && debug4 < this.offhand.size() + 150) {
/* 467 */           this.offhand.set(debug4 - 150, debug5);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getContainerSize() {
/* 475 */     return this.items.size() + this.armor.size() + this.offhand.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 480 */     for (ItemStack debug2 : this.items) {
/* 481 */       if (!debug2.isEmpty()) {
/* 482 */         return false;
/*     */       }
/*     */     } 
/* 485 */     for (ItemStack debug2 : this.armor) {
/* 486 */       if (!debug2.isEmpty()) {
/* 487 */         return false;
/*     */       }
/*     */     } 
/* 490 */     for (ItemStack debug2 : this.offhand) {
/* 491 */       if (!debug2.isEmpty()) {
/* 492 */         return false;
/*     */       }
/*     */     } 
/* 495 */     return true;
/*     */   }
/*     */   
/*     */   public ItemStack getItem(int debug1) {
/*     */     NonNullList<ItemStack> nonNullList;
/* 500 */     List<ItemStack> debug2 = null;
/*     */     
/* 502 */     for (NonNullList<ItemStack> debug4 : this.compartments) {
/* 503 */       if (debug1 < debug4.size()) {
/* 504 */         nonNullList = debug4;
/*     */         break;
/*     */       } 
/* 507 */       debug1 -= debug4.size();
/*     */     } 
/*     */ 
/*     */     
/* 511 */     return (nonNullList == null) ? ItemStack.EMPTY : nonNullList.get(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getName() {
/* 516 */     return (Component)new TranslatableComponent("container.inventory");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void hurtArmor(DamageSource debug1, float debug2) {
/* 524 */     if (debug2 <= 0.0F) {
/*     */       return;
/*     */     }
/*     */     
/* 528 */     debug2 /= 4.0F;
/* 529 */     if (debug2 < 1.0F) {
/* 530 */       debug2 = 1.0F;
/*     */     }
/* 532 */     for (int debug3 = 0; debug3 < this.armor.size(); debug3++) {
/* 533 */       ItemStack debug4 = (ItemStack)this.armor.get(debug3);
/* 534 */       if (!debug1.isFire() || !debug4.getItem().isFireResistant())
/*     */       {
/*     */         
/* 537 */         if (debug4.getItem() instanceof net.minecraft.world.item.ArmorItem) {
/* 538 */           int debug5 = debug3;
/* 539 */           debug4.hurtAndBreak((int)debug2, this.player, debug1 -> debug1.broadcastBreakEvent(EquipmentSlot.byTypeAndIndex(EquipmentSlot.Type.ARMOR, debug0)));
/*     */         }  } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void dropAll() {
/* 545 */     for (List<ItemStack> debug2 : this.compartments) {
/* 546 */       for (int debug3 = 0; debug3 < debug2.size(); debug3++) {
/* 547 */         ItemStack debug4 = debug2.get(debug3);
/* 548 */         if (!debug4.isEmpty()) {
/* 549 */           this.player.drop(debug4, true, false);
/* 550 */           debug2.set(debug3, ItemStack.EMPTY);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setChanged() {
/* 558 */     this.timesChanged++;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCarried(ItemStack debug1) {
/* 566 */     this.carried = debug1;
/*     */   }
/*     */   
/*     */   public ItemStack getCarried() {
/* 570 */     return this.carried;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player debug1) {
/* 575 */     if (this.player.removed) {
/* 576 */       return false;
/*     */     }
/* 578 */     if (debug1.distanceToSqr((Entity)this.player) > 64.0D) {
/* 579 */       return false;
/*     */     }
/* 581 */     return true;
/*     */   }
/*     */   
/*     */   public boolean contains(ItemStack debug1) {
/* 585 */     for (List<ItemStack> debug3 : this.compartments) {
/* 586 */       for (ItemStack debug5 : debug3) {
/* 587 */         if (!debug5.isEmpty() && debug5.sameItem(debug1)) {
/* 588 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 592 */     return false;
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
/*     */   public void replaceWith(Inventory debug1) {
/* 607 */     for (int debug2 = 0; debug2 < getContainerSize(); debug2++) {
/* 608 */       setItem(debug2, debug1.getItem(debug2));
/*     */     }
/* 610 */     this.selected = debug1.selected;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearContent() {
/* 615 */     for (List<ItemStack> debug2 : this.compartments) {
/* 616 */       debug2.clear();
/*     */     }
/*     */   }
/*     */   
/*     */   public void fillStackedContents(StackedContents debug1) {
/* 621 */     for (ItemStack debug3 : this.items)
/* 622 */       debug1.accountSimpleStack(debug3); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\player\Inventory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */