/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.ContainerHelper;
/*     */ import net.minecraft.world.WorldlyContainer;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.ExperienceOrb;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.player.StackedContents;
/*     */ import net.minecraft.world.inventory.ContainerData;
/*     */ import net.minecraft.world.inventory.RecipeHolder;
/*     */ import net.minecraft.world.inventory.StackedContentsCompatible;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.crafting.AbstractCookingRecipe;
/*     */ import net.minecraft.world.item.crafting.Recipe;
/*     */ import net.minecraft.world.item.crafting.RecipeType;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.AbstractFurnaceBlock;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public abstract class AbstractFurnaceBlockEntity
/*     */   extends BaseContainerBlockEntity
/*     */   implements WorldlyContainer, RecipeHolder, StackedContentsCompatible, TickableBlockEntity
/*     */ {
/*  48 */   private static final int[] SLOTS_FOR_UP = new int[] { 0 };
/*     */ 
/*     */   
/*  51 */   private static final int[] SLOTS_FOR_DOWN = new int[] { 2, 1 };
/*     */ 
/*     */   
/*  54 */   private static final int[] SLOTS_FOR_SIDES = new int[] { 1 };
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
/*  66 */   protected NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
/*     */   private int litTime;
/*     */   private int litDuration;
/*     */   private int cookingProgress;
/*     */   private int cookingTotalTime;
/*     */   
/*  72 */   protected final ContainerData dataAccess = new ContainerData()
/*     */     {
/*     */       public int get(int debug1) {
/*  75 */         switch (debug1) {
/*     */           case 0:
/*  77 */             return AbstractFurnaceBlockEntity.this.litTime;
/*     */           case 1:
/*  79 */             return AbstractFurnaceBlockEntity.this.litDuration;
/*     */           case 2:
/*  81 */             return AbstractFurnaceBlockEntity.this.cookingProgress;
/*     */           case 3:
/*  83 */             return AbstractFurnaceBlockEntity.this.cookingTotalTime;
/*     */         } 
/*     */ 
/*     */         
/*  87 */         return 0;
/*     */       }
/*     */ 
/*     */       
/*     */       public void set(int debug1, int debug2) {
/*  92 */         switch (debug1) {
/*     */           case 0:
/*  94 */             AbstractFurnaceBlockEntity.this.litTime = debug2;
/*     */             break;
/*     */           case 1:
/*  97 */             AbstractFurnaceBlockEntity.this.litDuration = debug2;
/*     */             break;
/*     */           case 2:
/* 100 */             AbstractFurnaceBlockEntity.this.cookingProgress = debug2;
/*     */             break;
/*     */           case 3:
/* 103 */             AbstractFurnaceBlockEntity.this.cookingTotalTime = debug2;
/*     */             break;
/*     */         } 
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public int getCount() {
/* 112 */         return 4;
/*     */       }
/*     */     };
/*     */   
/* 116 */   private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap();
/*     */   protected final RecipeType<? extends AbstractCookingRecipe> recipeType;
/*     */   
/*     */   protected AbstractFurnaceBlockEntity(BlockEntityType<?> debug1, RecipeType<? extends AbstractCookingRecipe> debug2) {
/* 120 */     super(debug1);
/* 121 */     this.recipeType = debug2;
/*     */   }
/*     */   
/*     */   public static Map<Item, Integer> getFuel() {
/* 125 */     Map<Item, Integer> debug0 = Maps.newLinkedHashMap();
/*     */     
/* 127 */     add(debug0, (ItemLike)Items.LAVA_BUCKET, 20000);
/* 128 */     add(debug0, (ItemLike)Blocks.COAL_BLOCK, 16000);
/* 129 */     add(debug0, (ItemLike)Items.BLAZE_ROD, 2400);
/* 130 */     add(debug0, (ItemLike)Items.COAL, 1600);
/* 131 */     add(debug0, (ItemLike)Items.CHARCOAL, 1600);
/* 132 */     add(debug0, (Tag<Item>)ItemTags.LOGS, 300);
/* 133 */     add(debug0, (Tag<Item>)ItemTags.PLANKS, 300);
/* 134 */     add(debug0, (Tag<Item>)ItemTags.WOODEN_STAIRS, 300);
/* 135 */     add(debug0, (Tag<Item>)ItemTags.WOODEN_SLABS, 150);
/* 136 */     add(debug0, (Tag<Item>)ItemTags.WOODEN_TRAPDOORS, 300);
/* 137 */     add(debug0, (Tag<Item>)ItemTags.WOODEN_PRESSURE_PLATES, 300);
/* 138 */     add(debug0, (ItemLike)Blocks.OAK_FENCE, 300);
/* 139 */     add(debug0, (ItemLike)Blocks.BIRCH_FENCE, 300);
/* 140 */     add(debug0, (ItemLike)Blocks.SPRUCE_FENCE, 300);
/* 141 */     add(debug0, (ItemLike)Blocks.JUNGLE_FENCE, 300);
/* 142 */     add(debug0, (ItemLike)Blocks.DARK_OAK_FENCE, 300);
/* 143 */     add(debug0, (ItemLike)Blocks.ACACIA_FENCE, 300);
/* 144 */     add(debug0, (ItemLike)Blocks.OAK_FENCE_GATE, 300);
/* 145 */     add(debug0, (ItemLike)Blocks.BIRCH_FENCE_GATE, 300);
/* 146 */     add(debug0, (ItemLike)Blocks.SPRUCE_FENCE_GATE, 300);
/* 147 */     add(debug0, (ItemLike)Blocks.JUNGLE_FENCE_GATE, 300);
/* 148 */     add(debug0, (ItemLike)Blocks.DARK_OAK_FENCE_GATE, 300);
/* 149 */     add(debug0, (ItemLike)Blocks.ACACIA_FENCE_GATE, 300);
/* 150 */     add(debug0, (ItemLike)Blocks.NOTE_BLOCK, 300);
/* 151 */     add(debug0, (ItemLike)Blocks.BOOKSHELF, 300);
/* 152 */     add(debug0, (ItemLike)Blocks.LECTERN, 300);
/* 153 */     add(debug0, (ItemLike)Blocks.JUKEBOX, 300);
/* 154 */     add(debug0, (ItemLike)Blocks.CHEST, 300);
/* 155 */     add(debug0, (ItemLike)Blocks.TRAPPED_CHEST, 300);
/* 156 */     add(debug0, (ItemLike)Blocks.CRAFTING_TABLE, 300);
/* 157 */     add(debug0, (ItemLike)Blocks.DAYLIGHT_DETECTOR, 300);
/* 158 */     add(debug0, (Tag<Item>)ItemTags.BANNERS, 300);
/* 159 */     add(debug0, (ItemLike)Items.BOW, 300);
/* 160 */     add(debug0, (ItemLike)Items.FISHING_ROD, 300);
/* 161 */     add(debug0, (ItemLike)Blocks.LADDER, 300);
/* 162 */     add(debug0, (Tag<Item>)ItemTags.SIGNS, 200);
/* 163 */     add(debug0, (ItemLike)Items.WOODEN_SHOVEL, 200);
/* 164 */     add(debug0, (ItemLike)Items.WOODEN_SWORD, 200);
/* 165 */     add(debug0, (ItemLike)Items.WOODEN_HOE, 200);
/* 166 */     add(debug0, (ItemLike)Items.WOODEN_AXE, 200);
/* 167 */     add(debug0, (ItemLike)Items.WOODEN_PICKAXE, 200);
/* 168 */     add(debug0, (Tag<Item>)ItemTags.WOODEN_DOORS, 200);
/* 169 */     add(debug0, (Tag<Item>)ItemTags.BOATS, 1200);
/* 170 */     add(debug0, (Tag<Item>)ItemTags.WOOL, 100);
/* 171 */     add(debug0, (Tag<Item>)ItemTags.WOODEN_BUTTONS, 100);
/* 172 */     add(debug0, (ItemLike)Items.STICK, 100);
/* 173 */     add(debug0, (Tag<Item>)ItemTags.SAPLINGS, 100);
/* 174 */     add(debug0, (ItemLike)Items.BOWL, 100);
/* 175 */     add(debug0, (Tag<Item>)ItemTags.CARPETS, 67);
/* 176 */     add(debug0, (ItemLike)Blocks.DRIED_KELP_BLOCK, 4001);
/* 177 */     add(debug0, (ItemLike)Items.CROSSBOW, 300);
/* 178 */     add(debug0, (ItemLike)Blocks.BAMBOO, 50);
/* 179 */     add(debug0, (ItemLike)Blocks.DEAD_BUSH, 100);
/* 180 */     add(debug0, (ItemLike)Blocks.SCAFFOLDING, 400);
/* 181 */     add(debug0, (ItemLike)Blocks.LOOM, 300);
/* 182 */     add(debug0, (ItemLike)Blocks.BARREL, 300);
/* 183 */     add(debug0, (ItemLike)Blocks.CARTOGRAPHY_TABLE, 300);
/* 184 */     add(debug0, (ItemLike)Blocks.FLETCHING_TABLE, 300);
/* 185 */     add(debug0, (ItemLike)Blocks.SMITHING_TABLE, 300);
/* 186 */     add(debug0, (ItemLike)Blocks.COMPOSTER, 300);
/*     */     
/* 188 */     return debug0;
/*     */   }
/*     */   
/*     */   private static boolean isNeverAFurnaceFuel(Item debug0) {
/* 192 */     return ItemTags.NON_FLAMMABLE_WOOD.contains(debug0);
/*     */   }
/*     */   
/*     */   private static void add(Map<Item, Integer> debug0, Tag<Item> debug1, int debug2) {
/* 196 */     for (Item debug4 : debug1.getValues()) {
/* 197 */       if (!isNeverAFurnaceFuel(debug4)) {
/* 198 */         debug0.put(debug4, Integer.valueOf(debug2));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void add(Map<Item, Integer> debug0, ItemLike debug1, int debug2) {
/* 204 */     Item debug3 = debug1.asItem();
/* 205 */     if (isNeverAFurnaceFuel(debug3)) {
/* 206 */       if (SharedConstants.IS_RUNNING_IN_IDE) {
/* 207 */         throw (IllegalStateException)Util.pauseInIde(new IllegalStateException("A developer tried to explicitly make fire resistant item " + debug3.getName(null).getString() + " a furnace fuel. That will not work!"));
/*     */       }
/*     */       return;
/*     */     } 
/* 211 */     debug0.put(debug3, Integer.valueOf(debug2));
/*     */   }
/*     */   
/*     */   private boolean isLit() {
/* 215 */     return (this.litTime > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void load(BlockState debug1, CompoundTag debug2) {
/* 220 */     super.load(debug1, debug2);
/*     */     
/* 222 */     this.items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
/* 223 */     ContainerHelper.loadAllItems(debug2, this.items);
/*     */     
/* 225 */     this.litTime = debug2.getShort("BurnTime");
/* 226 */     this.cookingProgress = debug2.getShort("CookTime");
/* 227 */     this.cookingTotalTime = debug2.getShort("CookTimeTotal");
/* 228 */     this.litDuration = getBurnDuration((ItemStack)this.items.get(1));
/*     */     
/* 230 */     CompoundTag debug3 = debug2.getCompound("RecipesUsed");
/* 231 */     for (String debug5 : debug3.getAllKeys()) {
/* 232 */       this.recipesUsed.put(new ResourceLocation(debug5), debug3.getInt(debug5));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag save(CompoundTag debug1) {
/* 238 */     super.save(debug1);
/* 239 */     debug1.putShort("BurnTime", (short)this.litTime);
/* 240 */     debug1.putShort("CookTime", (short)this.cookingProgress);
/* 241 */     debug1.putShort("CookTimeTotal", (short)this.cookingTotalTime);
/*     */     
/* 243 */     ContainerHelper.saveAllItems(debug1, this.items);
/*     */     
/* 245 */     CompoundTag debug2 = new CompoundTag();
/* 246 */     this.recipesUsed.forEach((debug1, debug2) -> debug0.putInt(debug1.toString(), debug2.intValue()));
/* 247 */     debug1.put("RecipesUsed", (Tag)debug2);
/*     */     
/* 249 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 254 */     boolean debug1 = isLit();
/* 255 */     boolean debug2 = false;
/*     */     
/* 257 */     if (isLit())
/*     */     {
/* 259 */       this.litTime--;
/*     */     }
/*     */     
/* 262 */     if (!this.level.isClientSide) {
/* 263 */       ItemStack debug3 = (ItemStack)this.items.get(1);
/* 264 */       if (isLit() || (!debug3.isEmpty() && !((ItemStack)this.items.get(0)).isEmpty())) {
/*     */ 
/*     */         
/* 267 */         Recipe<?> debug4 = this.level.getRecipeManager().getRecipeFor(this.recipeType, this, this.level).orElse(null);
/* 268 */         if (!isLit() && canBurn(debug4)) {
/*     */           
/* 270 */           this.litTime = getBurnDuration(debug3);
/* 271 */           this.litDuration = this.litTime;
/*     */           
/* 273 */           if (isLit()) {
/* 274 */             debug2 = true;
/*     */             
/* 276 */             if (!debug3.isEmpty()) {
/* 277 */               Item debug5 = debug3.getItem();
/* 278 */               debug3.shrink(1);
/* 279 */               if (debug3.isEmpty()) {
/* 280 */                 Item debug6 = debug5.getCraftingRemainingItem();
/* 281 */                 this.items.set(1, (debug6 == null) ? ItemStack.EMPTY : new ItemStack((ItemLike)debug6));
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 287 */         if (isLit() && canBurn(debug4)) {
/* 288 */           this.cookingProgress++;
/*     */           
/* 290 */           if (this.cookingProgress == this.cookingTotalTime) {
/* 291 */             this.cookingProgress = 0;
/* 292 */             this.cookingTotalTime = getTotalCookTime();
/* 293 */             burn(debug4);
/* 294 */             debug2 = true;
/*     */           } 
/*     */         } else {
/* 297 */           this.cookingProgress = 0;
/*     */         } 
/* 299 */       } else if (!isLit() && this.cookingProgress > 0) {
/* 300 */         this.cookingProgress = Mth.clamp(this.cookingProgress - 2, 0, this.cookingTotalTime);
/*     */       } 
/*     */       
/* 303 */       if (debug1 != isLit()) {
/* 304 */         debug2 = true;
/* 305 */         this.level.setBlock(this.worldPosition, (BlockState)this.level.getBlockState(this.worldPosition).setValue((Property)AbstractFurnaceBlock.LIT, Boolean.valueOf(isLit())), 3);
/*     */       } 
/*     */     } 
/*     */     
/* 309 */     if (debug2) {
/* 310 */       setChanged();
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean canBurn(@Nullable Recipe<?> debug1) {
/* 315 */     if (((ItemStack)this.items.get(0)).isEmpty() || debug1 == null) {
/* 316 */       return false;
/*     */     }
/* 318 */     ItemStack debug2 = debug1.getResultItem();
/* 319 */     if (debug2.isEmpty()) {
/* 320 */       return false;
/*     */     }
/*     */     
/* 323 */     ItemStack debug3 = (ItemStack)this.items.get(2);
/* 324 */     if (debug3.isEmpty()) {
/* 325 */       return true;
/*     */     }
/* 327 */     if (!debug3.sameItem(debug2)) {
/* 328 */       return false;
/*     */     }
/* 330 */     if (debug3.getCount() < getMaxStackSize() && debug3.getCount() < debug3.getMaxStackSize()) {
/* 331 */       return true;
/*     */     }
/* 333 */     return (debug3.getCount() < debug2.getMaxStackSize());
/*     */   }
/*     */   
/*     */   private void burn(@Nullable Recipe<?> debug1) {
/* 337 */     if (debug1 == null || !canBurn(debug1)) {
/*     */       return;
/*     */     }
/*     */     
/* 341 */     ItemStack debug2 = (ItemStack)this.items.get(0);
/* 342 */     ItemStack debug3 = debug1.getResultItem();
/* 343 */     ItemStack debug4 = (ItemStack)this.items.get(2);
/* 344 */     if (debug4.isEmpty()) {
/* 345 */       this.items.set(2, debug3.copy());
/* 346 */     } else if (debug4.getItem() == debug3.getItem()) {
/* 347 */       debug4.grow(1);
/*     */     } 
/*     */     
/* 350 */     if (!this.level.isClientSide) {
/* 351 */       setRecipeUsed(debug1);
/*     */     }
/*     */     
/* 354 */     if (debug2.getItem() == Blocks.WET_SPONGE.asItem() && !((ItemStack)this.items.get(1)).isEmpty() && ((ItemStack)this.items.get(1)).getItem() == Items.BUCKET) {
/* 355 */       this.items.set(1, new ItemStack((ItemLike)Items.WATER_BUCKET));
/*     */     }
/*     */     
/* 358 */     debug2.shrink(1);
/*     */   }
/*     */   
/*     */   protected int getBurnDuration(ItemStack debug1) {
/* 362 */     if (debug1.isEmpty()) {
/* 363 */       return 0;
/*     */     }
/*     */     
/* 366 */     Item debug2 = debug1.getItem();
/* 367 */     return ((Integer)getFuel().getOrDefault(debug2, Integer.valueOf(0))).intValue();
/*     */   }
/*     */   
/*     */   protected int getTotalCookTime() {
/* 371 */     return ((Integer)this.level.getRecipeManager().getRecipeFor(this.recipeType, this, this.level).map(AbstractCookingRecipe::getCookingTime).orElse(Integer.valueOf(200))).intValue();
/*     */   }
/*     */   
/*     */   public static boolean isFuel(ItemStack debug0) {
/* 375 */     return getFuel().containsKey(debug0.getItem());
/*     */   }
/*     */ 
/*     */   
/*     */   public int[] getSlotsForFace(Direction debug1) {
/* 380 */     if (debug1 == Direction.DOWN)
/* 381 */       return SLOTS_FOR_DOWN; 
/* 382 */     if (debug1 == Direction.UP) {
/* 383 */       return SLOTS_FOR_UP;
/*     */     }
/* 385 */     return SLOTS_FOR_SIDES;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canPlaceItemThroughFace(int debug1, ItemStack debug2, @Nullable Direction debug3) {
/* 391 */     return canPlaceItem(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canTakeItemThroughFace(int debug1, ItemStack debug2, Direction debug3) {
/* 396 */     if (debug3 == Direction.DOWN && debug1 == 1) {
/* 397 */       Item debug4 = debug2.getItem();
/* 398 */       if (debug4 != Items.WATER_BUCKET && debug4 != Items.BUCKET) {
/* 399 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 403 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getContainerSize() {
/* 408 */     return this.items.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 413 */     for (ItemStack debug2 : this.items) {
/* 414 */       if (!debug2.isEmpty()) {
/* 415 */         return false;
/*     */       }
/*     */     } 
/* 418 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItem(int debug1) {
/* 423 */     return (ItemStack)this.items.get(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItem(int debug1, int debug2) {
/* 428 */     return ContainerHelper.removeItem((List)this.items, debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeItemNoUpdate(int debug1) {
/* 433 */     return ContainerHelper.takeItem((List)this.items, debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItem(int debug1, ItemStack debug2) {
/* 438 */     ItemStack debug3 = (ItemStack)this.items.get(debug1);
/* 439 */     boolean debug4 = (!debug2.isEmpty() && debug2.sameItem(debug3) && ItemStack.tagMatches(debug2, debug3));
/* 440 */     this.items.set(debug1, debug2);
/* 441 */     if (debug2.getCount() > getMaxStackSize()) {
/* 442 */       debug2.setCount(getMaxStackSize());
/*     */     }
/*     */     
/* 445 */     if (debug1 == 0 && !debug4) {
/* 446 */       this.cookingTotalTime = getTotalCookTime();
/* 447 */       this.cookingProgress = 0;
/* 448 */       setChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player debug1) {
/* 454 */     if (this.level.getBlockEntity(this.worldPosition) != this) {
/* 455 */       return false;
/*     */     }
/* 457 */     return (debug1.distanceToSqr(this.worldPosition.getX() + 0.5D, this.worldPosition.getY() + 0.5D, this.worldPosition.getZ() + 0.5D) <= 64.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canPlaceItem(int debug1, ItemStack debug2) {
/* 462 */     if (debug1 == 2) {
/* 463 */       return false;
/*     */     }
/* 465 */     if (debug1 == 1) {
/* 466 */       ItemStack debug3 = (ItemStack)this.items.get(1);
/* 467 */       return (isFuel(debug2) || (debug2.getItem() == Items.BUCKET && debug3.getItem() != Items.BUCKET));
/*     */     } 
/* 469 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearContent() {
/* 474 */     this.items.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRecipeUsed(@Nullable Recipe<?> debug1) {
/* 479 */     if (debug1 != null) {
/* 480 */       ResourceLocation debug2 = debug1.getId();
/* 481 */       this.recipesUsed.addTo(debug2, 1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Recipe<?> getRecipeUsed() {
/* 488 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void awardUsedRecipes(Player debug1) {}
/*     */ 
/*     */   
/*     */   public void awardUsedRecipesAndPopExperience(Player debug1) {
/* 497 */     List<Recipe<?>> debug2 = getRecipesToAwardAndPopExperience(debug1.level, debug1.position());
/* 498 */     debug1.awardRecipes(debug2);
/* 499 */     this.recipesUsed.clear();
/*     */   }
/*     */   
/*     */   public List<Recipe<?>> getRecipesToAwardAndPopExperience(Level debug1, Vec3 debug2) {
/* 503 */     List<Recipe<?>> debug3 = Lists.newArrayList();
/* 504 */     for (ObjectIterator<Object2IntMap.Entry<ResourceLocation>> objectIterator = this.recipesUsed.object2IntEntrySet().iterator(); objectIterator.hasNext(); ) { Object2IntMap.Entry<ResourceLocation> debug5 = objectIterator.next();
/* 505 */       debug1.getRecipeManager().byKey((ResourceLocation)debug5.getKey()).ifPresent(debug4 -> {
/*     */             debug0.add(debug4);
/*     */             createExperience(debug1, debug2, debug3.getIntValue(), ((AbstractCookingRecipe)debug4).getExperience());
/*     */           }); }
/*     */     
/* 510 */     return debug3;
/*     */   }
/*     */   
/*     */   private static void createExperience(Level debug0, Vec3 debug1, int debug2, float debug3) {
/* 514 */     int debug4 = Mth.floor(debug2 * debug3);
/* 515 */     float debug5 = Mth.frac(debug2 * debug3);
/* 516 */     if (debug5 != 0.0F && Math.random() < debug5) {
/* 517 */       debug4++;
/*     */     }
/*     */     
/* 520 */     while (debug4 > 0) {
/* 521 */       int debug6 = ExperienceOrb.getExperienceValue(debug4);
/* 522 */       debug4 -= debug6;
/* 523 */       debug0.addFreshEntity((Entity)new ExperienceOrb(debug0, debug1.x, debug1.y, debug1.z, debug6));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void fillStackedContents(StackedContents debug1) {
/* 529 */     for (ItemStack debug3 : this.items)
/* 530 */       debug1.accountStack(debug3); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\entity\AbstractFurnaceBlockEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */