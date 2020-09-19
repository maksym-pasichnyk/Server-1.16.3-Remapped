/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.world.ContainerHelper;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.WorldlyContainer;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.BrewingStandMenu;
/*     */ import net.minecraft.world.inventory.ContainerData;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.alchemy.PotionBrewing;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.block.BrewingStandBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class BrewingStandBlockEntity
/*     */   extends BaseContainerBlockEntity
/*     */   implements WorldlyContainer, TickableBlockEntity
/*     */ {
/*  33 */   private static final int[] SLOTS_FOR_UP = new int[] { 3 };
/*     */ 
/*     */   
/*  36 */   private static final int[] SLOTS_FOR_DOWN = new int[] { 0, 1, 2, 3 };
/*     */ 
/*     */   
/*  39 */   private static final int[] SLOTS_FOR_SIDES = new int[] { 0, 1, 2, 4 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  49 */   private NonNullList<ItemStack> items = NonNullList.withSize(5, ItemStack.EMPTY);
/*     */   
/*     */   private int brewTime;
/*     */   private boolean[] lastPotionCount;
/*     */   private Item ingredient;
/*     */   private int fuel;
/*     */   
/*  56 */   protected final ContainerData dataAccess = new ContainerData()
/*     */     {
/*     */       public int get(int debug1) {
/*  59 */         switch (debug1) {
/*     */           case 0:
/*  61 */             return BrewingStandBlockEntity.this.brewTime;
/*     */           case 1:
/*  63 */             return BrewingStandBlockEntity.this.fuel;
/*     */         } 
/*  65 */         return 0;
/*     */       }
/*     */ 
/*     */       
/*     */       public void set(int debug1, int debug2) {
/*  70 */         switch (debug1) {
/*     */           case 0:
/*  72 */             BrewingStandBlockEntity.this.brewTime = debug2;
/*     */             break;
/*     */           case 1:
/*  75 */             BrewingStandBlockEntity.this.fuel = debug2;
/*     */             break;
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/*     */       public int getCount() {
/*  82 */         return 2;
/*     */       }
/*     */     };
/*     */   
/*     */   public BrewingStandBlockEntity() {
/*  87 */     super(BlockEntityType.BREWING_STAND);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Component getDefaultName() {
/*  92 */     return (Component)new TranslatableComponent("container.brewing");
/*     */   }
/*     */ 
/*     */   
/*     */   public int getContainerSize() {
/*  97 */     return this.items.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 102 */     for (ItemStack debug2 : this.items) {
/* 103 */       if (!debug2.isEmpty()) {
/* 104 */         return false;
/*     */       }
/*     */     } 
/* 107 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 112 */     ItemStack debug1 = (ItemStack)this.items.get(4);
/* 113 */     if (this.fuel <= 0 && debug1.getItem() == Items.BLAZE_POWDER) {
/* 114 */       this.fuel = 20;
/* 115 */       debug1.shrink(1);
/* 116 */       setChanged();
/*     */     } 
/*     */     
/* 119 */     boolean debug2 = isBrewable();
/* 120 */     boolean debug3 = (this.brewTime > 0);
/* 121 */     ItemStack debug4 = (ItemStack)this.items.get(3);
/* 122 */     if (debug3) {
/* 123 */       this.brewTime--;
/*     */       
/* 125 */       boolean debug5 = (this.brewTime == 0);
/* 126 */       if (debug5 && debug2) {
/*     */         
/* 128 */         doBrew();
/* 129 */         setChanged();
/* 130 */       } else if (!debug2) {
/* 131 */         this.brewTime = 0;
/* 132 */         setChanged();
/* 133 */       } else if (this.ingredient != debug4.getItem()) {
/* 134 */         this.brewTime = 0;
/* 135 */         setChanged();
/*     */       } 
/* 137 */     } else if (debug2 && this.fuel > 0) {
/* 138 */       this.fuel--;
/* 139 */       this.brewTime = 400;
/* 140 */       this.ingredient = debug4.getItem();
/* 141 */       setChanged();
/*     */     } 
/*     */     
/* 144 */     if (!this.level.isClientSide) {
/* 145 */       boolean[] debug5 = getPotionBits();
/* 146 */       if (!Arrays.equals(debug5, this.lastPotionCount)) {
/* 147 */         this.lastPotionCount = debug5;
/* 148 */         BlockState debug6 = this.level.getBlockState(getBlockPos());
/* 149 */         if (!(debug6.getBlock() instanceof BrewingStandBlock)) {
/*     */           return;
/*     */         }
/* 152 */         for (int debug7 = 0; debug7 < BrewingStandBlock.HAS_BOTTLE.length; debug7++) {
/* 153 */           debug6 = (BlockState)debug6.setValue((Property)BrewingStandBlock.HAS_BOTTLE[debug7], Boolean.valueOf(debug5[debug7]));
/*     */         }
/* 155 */         this.level.setBlock(this.worldPosition, debug6, 2);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean[] getPotionBits() {
/* 162 */     boolean[] debug1 = new boolean[3];
/* 163 */     for (int debug2 = 0; debug2 < 3; debug2++) {
/* 164 */       if (!((ItemStack)this.items.get(debug2)).isEmpty()) {
/* 165 */         debug1[debug2] = true;
/*     */       }
/*     */     } 
/* 168 */     return debug1;
/*     */   }
/*     */   
/*     */   private boolean isBrewable() {
/* 172 */     ItemStack debug1 = (ItemStack)this.items.get(3);
/* 173 */     if (debug1.isEmpty()) {
/* 174 */       return false;
/*     */     }
/*     */     
/* 177 */     if (!PotionBrewing.isIngredient(debug1)) {
/* 178 */       return false;
/*     */     }
/*     */     
/* 181 */     for (int debug2 = 0; debug2 < 3; debug2++) {
/* 182 */       ItemStack debug3 = (ItemStack)this.items.get(debug2);
/* 183 */       if (!debug3.isEmpty())
/*     */       {
/*     */ 
/*     */         
/* 187 */         if (PotionBrewing.hasMix(debug3, debug1))
/* 188 */           return true; 
/*     */       }
/*     */     } 
/* 191 */     return false;
/*     */   }
/*     */   
/*     */   private void doBrew() {
/* 195 */     ItemStack debug1 = (ItemStack)this.items.get(3);
/*     */     
/* 197 */     for (int i = 0; i < 3; i++) {
/* 198 */       this.items.set(i, PotionBrewing.mix(debug1, (ItemStack)this.items.get(i)));
/*     */     }
/*     */     
/* 201 */     debug1.shrink(1);
/* 202 */     BlockPos debug2 = getBlockPos();
/* 203 */     if (debug1.getItem().hasCraftingRemainingItem()) {
/* 204 */       ItemStack debug3 = new ItemStack((ItemLike)debug1.getItem().getCraftingRemainingItem());
/* 205 */       if (debug1.isEmpty()) {
/* 206 */         debug1 = debug3;
/*     */       }
/* 208 */       else if (!this.level.isClientSide) {
/* 209 */         Containers.dropItemStack(this.level, debug2.getX(), debug2.getY(), debug2.getZ(), debug3);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 214 */     this.items.set(3, debug1);
/*     */     
/* 216 */     this.level.levelEvent(1035, debug2, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void load(BlockState debug1, CompoundTag debug2) {
/* 221 */     super.load(debug1, debug2);
/*     */     
/* 223 */     this.items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
/* 224 */     ContainerHelper.loadAllItems(debug2, this.items);
/*     */     
/* 226 */     this.brewTime = debug2.getShort("BrewTime");
/* 227 */     this.fuel = debug2.getByte("Fuel");
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag save(CompoundTag debug1) {
/* 232 */     super.save(debug1);
/*     */     
/* 234 */     debug1.putShort("BrewTime", (short)this.brewTime);
/* 235 */     ContainerHelper.saveAllItems(debug1, this.items);
/*     */     
/* 237 */     debug1.putByte("Fuel", (byte)this.fuel);
/*     */     
/* 239 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItem(int debug1) {
/* 244 */     if (debug1 >= 0 && debug1 < this.items.size()) {
/* 245 */       return (ItemStack)this.items.get(debug1);
/*     */     }
/* 247 */     return ItemStack.EMPTY;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItem(int debug1, int debug2) {
/* 252 */     return ContainerHelper.removeItem((List)this.items, debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItemNoUpdate(int debug1) {
/* 257 */     return ContainerHelper.takeItem((List)this.items, debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItem(int debug1, ItemStack debug2) {
/* 262 */     if (debug1 >= 0 && debug1 < this.items.size()) {
/* 263 */       this.items.set(debug1, debug2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player debug1) {
/* 269 */     if (this.level.getBlockEntity(this.worldPosition) != this) {
/* 270 */       return false;
/*     */     }
/* 272 */     if (debug1.distanceToSqr(this.worldPosition.getX() + 0.5D, this.worldPosition.getY() + 0.5D, this.worldPosition.getZ() + 0.5D) > 64.0D) {
/* 273 */       return false;
/*     */     }
/* 275 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canPlaceItem(int debug1, ItemStack debug2) {
/* 280 */     if (debug1 == 3) {
/* 281 */       return PotionBrewing.isIngredient(debug2);
/*     */     }
/*     */     
/* 284 */     Item debug3 = debug2.getItem();
/* 285 */     if (debug1 == 4) {
/* 286 */       return (debug3 == Items.BLAZE_POWDER);
/*     */     }
/*     */     
/* 289 */     return ((debug3 == Items.POTION || debug3 == Items.SPLASH_POTION || debug3 == Items.LINGERING_POTION || debug3 == Items.GLASS_BOTTLE) && getItem(debug1).isEmpty());
/*     */   }
/*     */ 
/*     */   
/*     */   public int[] getSlotsForFace(Direction debug1) {
/* 294 */     if (debug1 == Direction.UP) {
/* 295 */       return SLOTS_FOR_UP;
/*     */     }
/* 297 */     if (debug1 == Direction.DOWN) {
/* 298 */       return SLOTS_FOR_DOWN;
/*     */     }
/* 300 */     return SLOTS_FOR_SIDES;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canPlaceItemThroughFace(int debug1, ItemStack debug2, @Nullable Direction debug3) {
/* 305 */     return canPlaceItem(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canTakeItemThroughFace(int debug1, ItemStack debug2, Direction debug3) {
/* 310 */     if (debug1 == 3) {
/* 311 */       return (debug2.getItem() == Items.GLASS_BOTTLE);
/*     */     }
/* 313 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearContent() {
/* 318 */     this.items.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   protected AbstractContainerMenu createMenu(int debug1, Inventory debug2) {
/* 323 */     return (AbstractContainerMenu)new BrewingStandMenu(debug1, debug2, this, this.dataAccess);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\entity\BrewingStandBlockEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */