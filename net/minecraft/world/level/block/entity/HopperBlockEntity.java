/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.ContainerHelper;
/*     */ import net.minecraft.world.WorldlyContainer;
/*     */ import net.minecraft.world.WorldlyContainerHolder;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.HopperMenu;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.ChestBlock;
/*     */ import net.minecraft.world.level.block.HopperBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ 
/*     */ public class HopperBlockEntity extends RandomizableContainerBlockEntity implements Hopper, TickableBlockEntity {
/*  39 */   private NonNullList<ItemStack> items = NonNullList.withSize(5, ItemStack.EMPTY);
/*  40 */   private int cooldownTime = -1;
/*     */   private long tickedGameTime;
/*     */   
/*     */   public HopperBlockEntity() {
/*  44 */     super(BlockEntityType.HOPPER);
/*     */   }
/*     */ 
/*     */   
/*     */   public void load(BlockState debug1, CompoundTag debug2) {
/*  49 */     super.load(debug1, debug2);
/*     */     
/*  51 */     this.items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
/*  52 */     if (!tryLoadLootTable(debug2)) {
/*  53 */       ContainerHelper.loadAllItems(debug2, this.items);
/*     */     }
/*  55 */     this.cooldownTime = debug2.getInt("TransferCooldown");
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag save(CompoundTag debug1) {
/*  60 */     super.save(debug1);
/*     */     
/*  62 */     if (!trySaveLootTable(debug1)) {
/*  63 */       ContainerHelper.saveAllItems(debug1, this.items);
/*     */     }
/*     */     
/*  66 */     debug1.putInt("TransferCooldown", this.cooldownTime);
/*  67 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getContainerSize() {
/*  72 */     return this.items.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItem(int debug1, int debug2) {
/*  77 */     unpackLootTable((Player)null);
/*     */ 
/*     */     
/*  80 */     return ContainerHelper.removeItem((List)getItems(), debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItem(int debug1, ItemStack debug2) {
/*  85 */     unpackLootTable((Player)null);
/*  86 */     getItems().set(debug1, debug2);
/*  87 */     if (debug2.getCount() > getMaxStackSize()) {
/*  88 */       debug2.setCount(getMaxStackSize());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Component getDefaultName() {
/*  95 */     return (Component)new TranslatableComponent("container.hopper");
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 100 */     if (this.level == null || this.level.isClientSide) {
/*     */       return;
/*     */     }
/*     */     
/* 104 */     this.cooldownTime--;
/* 105 */     this.tickedGameTime = this.level.getGameTime();
/*     */     
/* 107 */     if (!isOnCooldown()) {
/* 108 */       setCooldown(0);
/* 109 */       tryMoveItems(() -> Boolean.valueOf(suckInItems(this)));
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean tryMoveItems(Supplier<Boolean> debug1) {
/* 114 */     if (this.level == null || this.level.isClientSide) {
/* 115 */       return false;
/*     */     }
/*     */     
/* 118 */     if (!isOnCooldown() && ((Boolean)getBlockState().getValue((Property)HopperBlock.ENABLED)).booleanValue()) {
/* 119 */       boolean debug2 = false;
/*     */       
/* 121 */       if (!isEmpty()) {
/* 122 */         debug2 = ejectItems();
/*     */       }
/* 124 */       if (!inventoryFull()) {
/* 125 */         debug2 |= ((Boolean)debug1.get()).booleanValue();
/*     */       }
/*     */       
/* 128 */       if (debug2) {
/* 129 */         setCooldown(8);
/* 130 */         setChanged();
/* 131 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 135 */     return false;
/*     */   }
/*     */   
/*     */   private boolean inventoryFull() {
/* 139 */     for (ItemStack debug2 : this.items) {
/* 140 */       if (debug2.isEmpty() || debug2.getCount() != debug2.getMaxStackSize()) {
/* 141 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 145 */     return true;
/*     */   }
/*     */   
/*     */   private boolean ejectItems() {
/* 149 */     Container debug1 = getAttachedContainer();
/* 150 */     if (debug1 == null) {
/* 151 */       return false;
/*     */     }
/*     */     
/* 154 */     Direction debug2 = ((Direction)getBlockState().getValue((Property)HopperBlock.FACING)).getOpposite();
/* 155 */     if (isFullContainer(debug1, debug2)) {
/* 156 */       return false;
/*     */     }
/*     */     
/* 159 */     for (int debug3 = 0; debug3 < getContainerSize(); debug3++) {
/* 160 */       if (!getItem(debug3).isEmpty()) {
/*     */ 
/*     */ 
/*     */         
/* 164 */         ItemStack debug4 = getItem(debug3).copy();
/* 165 */         ItemStack debug5 = addItem(this, debug1, removeItem(debug3, 1), debug2);
/*     */         
/* 167 */         if (debug5.isEmpty()) {
/* 168 */           debug1.setChanged();
/* 169 */           return true;
/*     */         } 
/* 171 */         setItem(debug3, debug4);
/*     */       } 
/*     */     } 
/*     */     
/* 175 */     return false;
/*     */   }
/*     */   
/*     */   private static IntStream getSlots(Container debug0, Direction debug1) {
/* 179 */     if (debug0 instanceof WorldlyContainer) {
/* 180 */       return IntStream.of(((WorldlyContainer)debug0).getSlotsForFace(debug1));
/*     */     }
/*     */     
/* 183 */     return IntStream.range(0, debug0.getContainerSize());
/*     */   }
/*     */   
/*     */   private boolean isFullContainer(Container debug1, Direction debug2) {
/* 187 */     return getSlots(debug1, debug2).allMatch(debug1 -> {
/*     */           ItemStack debug2 = debug0.getItem(debug1);
/*     */           return (debug2.getCount() >= debug2.getMaxStackSize());
/*     */         });
/*     */   }
/*     */   
/*     */   private static boolean isEmptyContainer(Container debug0, Direction debug1) {
/* 194 */     return getSlots(debug0, debug1).allMatch(debug1 -> debug0.getItem(debug1).isEmpty());
/*     */   }
/*     */   
/*     */   public static boolean suckInItems(Hopper debug0) {
/* 198 */     Container debug1 = getSourceContainer(debug0);
/*     */     
/* 200 */     if (debug1 != null) {
/* 201 */       Direction debug2 = Direction.DOWN;
/* 202 */       if (isEmptyContainer(debug1, debug2)) {
/* 203 */         return false;
/*     */       }
/*     */       
/* 206 */       return getSlots(debug1, debug2).anyMatch(debug3 -> tryTakeInItemFromSlot(debug0, debug1, debug3, debug2));
/*     */     } 
/* 208 */     for (ItemEntity debug3 : getItemsAtAndAbove(debug0)) {
/* 209 */       if (addItem(debug0, debug3)) {
/* 210 */         return true;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 215 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean tryTakeInItemFromSlot(Hopper debug0, Container debug1, int debug2, Direction debug3) {
/* 219 */     ItemStack debug4 = debug1.getItem(debug2);
/*     */     
/* 221 */     if (!debug4.isEmpty() && canTakeItemFromContainer(debug1, debug4, debug2, debug3)) {
/* 222 */       ItemStack debug5 = debug4.copy();
/* 223 */       ItemStack debug6 = addItem(debug1, debug0, debug1.removeItem(debug2, 1), (Direction)null);
/*     */       
/* 225 */       if (debug6.isEmpty()) {
/* 226 */         debug1.setChanged();
/* 227 */         return true;
/*     */       } 
/* 229 */       debug1.setItem(debug2, debug5);
/*     */     } 
/*     */ 
/*     */     
/* 233 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean addItem(Container debug0, ItemEntity debug1) {
/* 237 */     boolean debug2 = false;
/*     */     
/* 239 */     ItemStack debug3 = debug1.getItem().copy();
/* 240 */     ItemStack debug4 = addItem((Container)null, debug0, debug3, (Direction)null);
/*     */     
/* 242 */     if (debug4.isEmpty()) {
/* 243 */       debug2 = true;
/*     */       
/* 245 */       debug1.remove();
/*     */     } else {
/* 247 */       debug1.setItem(debug4);
/*     */     } 
/*     */     
/* 250 */     return debug2;
/*     */   }
/*     */   
/*     */   public static ItemStack addItem(@Nullable Container debug0, Container debug1, ItemStack debug2, @Nullable Direction debug3) {
/* 254 */     if (debug1 instanceof WorldlyContainer && debug3 != null) {
/* 255 */       WorldlyContainer debug4 = (WorldlyContainer)debug1;
/* 256 */       int[] debug5 = debug4.getSlotsForFace(debug3);
/*     */       
/* 258 */       for (int debug6 = 0; debug6 < debug5.length && !debug2.isEmpty(); debug6++) {
/* 259 */         debug2 = tryMoveInItem(debug0, debug1, debug2, debug5[debug6], debug3);
/*     */       }
/*     */     } else {
/* 262 */       int debug4 = debug1.getContainerSize();
/* 263 */       for (int debug5 = 0; debug5 < debug4 && !debug2.isEmpty(); debug5++) {
/* 264 */         debug2 = tryMoveInItem(debug0, debug1, debug2, debug5, debug3);
/*     */       }
/*     */     } 
/*     */     
/* 268 */     return debug2;
/*     */   }
/*     */   
/*     */   private static boolean canPlaceItemInContainer(Container debug0, ItemStack debug1, int debug2, @Nullable Direction debug3) {
/* 272 */     if (!debug0.canPlaceItem(debug2, debug1)) {
/* 273 */       return false;
/*     */     }
/* 275 */     if (debug0 instanceof WorldlyContainer && !((WorldlyContainer)debug0).canPlaceItemThroughFace(debug2, debug1, debug3)) {
/* 276 */       return false;
/*     */     }
/* 278 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean canTakeItemFromContainer(Container debug0, ItemStack debug1, int debug2, Direction debug3) {
/* 282 */     if (debug0 instanceof WorldlyContainer && !((WorldlyContainer)debug0).canTakeItemThroughFace(debug2, debug1, debug3)) {
/* 283 */       return false;
/*     */     }
/* 285 */     return true;
/*     */   }
/*     */   
/*     */   private static ItemStack tryMoveInItem(@Nullable Container debug0, Container debug1, ItemStack debug2, int debug3, @Nullable Direction debug4) {
/* 289 */     ItemStack debug5 = debug1.getItem(debug3);
/*     */     
/* 291 */     if (canPlaceItemInContainer(debug1, debug2, debug3, debug4)) {
/* 292 */       boolean debug6 = false;
/* 293 */       boolean debug7 = debug1.isEmpty();
/* 294 */       if (debug5.isEmpty()) {
/* 295 */         debug1.setItem(debug3, debug2);
/* 296 */         debug2 = ItemStack.EMPTY;
/* 297 */         debug6 = true;
/* 298 */       } else if (canMergeItems(debug5, debug2)) {
/* 299 */         int debug8 = debug2.getMaxStackSize() - debug5.getCount();
/* 300 */         int debug9 = Math.min(debug2.getCount(), debug8);
/*     */         
/* 302 */         debug2.shrink(debug9);
/* 303 */         debug5.grow(debug9);
/* 304 */         debug6 = (debug9 > 0);
/*     */       } 
/* 306 */       if (debug6) {
/* 307 */         if (debug7 && debug1 instanceof HopperBlockEntity) {
/* 308 */           HopperBlockEntity debug8 = (HopperBlockEntity)debug1;
/* 309 */           if (!debug8.isOnCustomCooldown()) {
/* 310 */             int debug9 = 0;
/* 311 */             if (debug0 instanceof HopperBlockEntity) {
/* 312 */               HopperBlockEntity debug10 = (HopperBlockEntity)debug0;
/* 313 */               if (debug8.tickedGameTime >= debug10.tickedGameTime)
/*     */               {
/* 315 */                 debug9 = 1;
/*     */               }
/*     */             } 
/* 318 */             debug8.setCooldown(8 - debug9);
/*     */           } 
/*     */         } 
/* 321 */         debug1.setChanged();
/*     */       } 
/*     */     } 
/* 324 */     return debug2;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Container getAttachedContainer() {
/* 329 */     Direction debug1 = (Direction)getBlockState().getValue((Property)HopperBlock.FACING);
/* 330 */     return getContainerAt(getLevel(), this.worldPosition.relative(debug1));
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static Container getSourceContainer(Hopper debug0) {
/* 335 */     return getContainerAt(debug0.getLevel(), debug0.getLevelX(), debug0.getLevelY() + 1.0D, debug0.getLevelZ());
/*     */   }
/*     */   
/*     */   public static List<ItemEntity> getItemsAtAndAbove(Hopper debug0) {
/* 339 */     return (List<ItemEntity>)debug0.getSuckShape().toAabbs().stream().flatMap(debug1 -> debug0.getLevel().getEntitiesOfClass(ItemEntity.class, debug1.move(debug0.getLevelX() - 0.5D, debug0.getLevelY() - 0.5D, debug0.getLevelZ() - 0.5D), EntitySelector.ENTITY_STILL_ALIVE).stream()).collect(Collectors.toList());
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static Container getContainerAt(Level debug0, BlockPos debug1) {
/* 344 */     return getContainerAt(debug0, debug1.getX() + 0.5D, debug1.getY() + 0.5D, debug1.getZ() + 0.5D);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static Container getContainerAt(Level debug0, double debug1, double debug3, double debug5) {
/* 349 */     Container debug7 = null;
/* 350 */     BlockPos debug8 = new BlockPos(debug1, debug3, debug5);
/*     */     
/* 352 */     BlockState debug9 = debug0.getBlockState(debug8);
/* 353 */     Block debug10 = debug9.getBlock();
/* 354 */     if (debug10 instanceof WorldlyContainerHolder) {
/* 355 */       WorldlyContainer worldlyContainer = ((WorldlyContainerHolder)debug10).getContainer(debug9, (LevelAccessor)debug0, debug8);
/* 356 */     } else if (debug10.isEntityBlock()) {
/* 357 */       BlockEntity debug11 = debug0.getBlockEntity(debug8);
/*     */       
/* 359 */       if (debug11 instanceof Container) {
/* 360 */         debug7 = (Container)debug11;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 365 */         if (debug7 instanceof ChestBlockEntity && 
/* 366 */           debug10 instanceof ChestBlock) {
/* 367 */           debug7 = ChestBlock.getContainer((ChestBlock)debug10, debug9, debug0, debug8, true);
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 373 */     if (debug7 == null) {
/* 374 */       List<Entity> debug11 = debug0.getEntities((Entity)null, new AABB(debug1 - 0.5D, debug3 - 0.5D, debug5 - 0.5D, debug1 + 0.5D, debug3 + 0.5D, debug5 + 0.5D), EntitySelector.CONTAINER_ENTITY_SELECTOR);
/*     */       
/* 376 */       if (!debug11.isEmpty()) {
/* 377 */         debug7 = (Container)debug11.get(debug0.random.nextInt(debug11.size()));
/*     */       }
/*     */     } 
/*     */     
/* 381 */     return debug7;
/*     */   }
/*     */   
/*     */   private static boolean canMergeItems(ItemStack debug0, ItemStack debug1) {
/* 385 */     if (debug0.getItem() != debug1.getItem()) {
/* 386 */       return false;
/*     */     }
/* 388 */     if (debug0.getDamageValue() != debug1.getDamageValue()) {
/* 389 */       return false;
/*     */     }
/* 391 */     if (debug0.getCount() > debug0.getMaxStackSize()) {
/* 392 */       return false;
/*     */     }
/* 394 */     if (!ItemStack.tagMatches(debug0, debug1)) {
/* 395 */       return false;
/*     */     }
/* 397 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getLevelX() {
/* 402 */     return this.worldPosition.getX() + 0.5D;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getLevelY() {
/* 407 */     return this.worldPosition.getY() + 0.5D;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getLevelZ() {
/* 412 */     return this.worldPosition.getZ() + 0.5D;
/*     */   }
/*     */   
/*     */   private void setCooldown(int debug1) {
/* 416 */     this.cooldownTime = debug1;
/*     */   }
/*     */   
/*     */   private boolean isOnCooldown() {
/* 420 */     return (this.cooldownTime > 0);
/*     */   }
/*     */   
/*     */   private boolean isOnCustomCooldown() {
/* 424 */     return (this.cooldownTime > 8);
/*     */   }
/*     */ 
/*     */   
/*     */   protected NonNullList<ItemStack> getItems() {
/* 429 */     return this.items;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setItems(NonNullList<ItemStack> debug1) {
/* 434 */     this.items = debug1;
/*     */   }
/*     */   
/*     */   public void entityInside(Entity debug1) {
/* 438 */     if (debug1 instanceof ItemEntity) {
/* 439 */       BlockPos debug2 = getBlockPos();
/* 440 */       if (Shapes.joinIsNotEmpty(Shapes.create(debug1.getBoundingBox().move(-debug2.getX(), -debug2.getY(), -debug2.getZ())), getSuckShape(), BooleanOp.AND)) {
/* 441 */         tryMoveItems(() -> Boolean.valueOf(addItem(this, (ItemEntity)debug1)));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected AbstractContainerMenu createMenu(int debug1, Inventory debug2) {
/* 448 */     return (AbstractContainerMenu)new HopperMenu(debug1, debug2, this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\entity\HopperBlockEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */