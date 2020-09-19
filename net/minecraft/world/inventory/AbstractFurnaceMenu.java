/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import net.minecraft.recipebook.ServerPlaceSmeltingRecipe;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.player.StackedContents;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.crafting.AbstractCookingRecipe;
/*     */ import net.minecraft.world.item.crafting.Recipe;
/*     */ import net.minecraft.world.item.crafting.RecipeType;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
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
/*     */ public abstract class AbstractFurnaceMenu
/*     */   extends RecipeBookMenu<Container>
/*     */ {
/*     */   private final Container container;
/*     */   private final ContainerData data;
/*     */   protected final Level level;
/*     */   private final RecipeType<? extends AbstractCookingRecipe> recipeType;
/*     */   private final RecipeBookType recipeBookType;
/*     */   
/*     */   protected AbstractFurnaceMenu(MenuType<?> debug1, RecipeType<? extends AbstractCookingRecipe> debug2, RecipeBookType debug3, int debug4, Inventory debug5) {
/*  41 */     this(debug1, debug2, debug3, debug4, debug5, (Container)new SimpleContainer(3), new SimpleContainerData(4));
/*     */   }
/*     */   
/*     */   protected AbstractFurnaceMenu(MenuType<?> debug1, RecipeType<? extends AbstractCookingRecipe> debug2, RecipeBookType debug3, int debug4, Inventory debug5, Container debug6, ContainerData debug7) {
/*  45 */     super(debug1, debug4);
/*  46 */     this.recipeType = debug2;
/*  47 */     this.recipeBookType = debug3;
/*  48 */     checkContainerSize(debug6, 3);
/*  49 */     checkContainerDataCount(debug7, 4);
/*  50 */     this.container = debug6;
/*  51 */     this.data = debug7;
/*  52 */     this.level = debug5.player.level;
/*     */     
/*  54 */     addSlot(new Slot(debug6, 0, 56, 17));
/*  55 */     addSlot(new FurnaceFuelSlot(this, debug6, 1, 56, 53));
/*  56 */     addSlot(new FurnaceResultSlot(debug5.player, debug6, 2, 116, 35));
/*     */     int debug8;
/*  58 */     for (debug8 = 0; debug8 < 3; debug8++) {
/*  59 */       for (int debug9 = 0; debug9 < 9; debug9++) {
/*  60 */         addSlot(new Slot((Container)debug5, debug9 + debug8 * 9 + 9, 8 + debug9 * 18, 84 + debug8 * 18));
/*     */       }
/*     */     } 
/*  63 */     for (debug8 = 0; debug8 < 9; debug8++) {
/*  64 */       addSlot(new Slot((Container)debug5, debug8, 8 + debug8 * 18, 142));
/*     */     }
/*     */     
/*  67 */     addDataSlots(debug7);
/*     */   }
/*     */ 
/*     */   
/*     */   public void fillCraftSlotsStackedContents(StackedContents debug1) {
/*  72 */     if (this.container instanceof StackedContentsCompatible) {
/*  73 */       ((StackedContentsCompatible)this.container).fillStackedContents(debug1);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearCraftingContent() {
/*  79 */     this.container.clearContent();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void handlePlacement(boolean debug1, Recipe<?> debug2, ServerPlayer debug3) {
/*  85 */     (new ServerPlaceSmeltingRecipe(this)).recipeClicked(debug3, debug2, debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean recipeMatches(Recipe<? super Container> debug1) {
/*  90 */     return debug1.matches(this.container, this.level);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getResultSlotIndex() {
/*  95 */     return 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getGridWidth() {
/* 100 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getGridHeight() {
/* 105 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player debug1) {
/* 115 */     return this.container.stillValid(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack quickMoveStack(Player debug1, int debug2) {
/* 120 */     ItemStack debug3 = ItemStack.EMPTY;
/* 121 */     Slot debug4 = this.slots.get(debug2);
/* 122 */     if (debug4 != null && debug4.hasItem()) {
/* 123 */       ItemStack debug5 = debug4.getItem();
/* 124 */       debug3 = debug5.copy();
/*     */       
/* 126 */       if (debug2 == 2) {
/* 127 */         if (!moveItemStackTo(debug5, 3, 39, true)) {
/* 128 */           return ItemStack.EMPTY;
/*     */         }
/* 130 */         debug4.onQuickCraft(debug5, debug3);
/* 131 */       } else if (debug2 == 1 || debug2 == 0) {
/* 132 */         if (!moveItemStackTo(debug5, 3, 39, false)) {
/* 133 */           return ItemStack.EMPTY;
/*     */         }
/* 135 */       } else if (canSmelt(debug5)) {
/* 136 */         if (!moveItemStackTo(debug5, 0, 1, false)) {
/* 137 */           return ItemStack.EMPTY;
/*     */         }
/* 139 */       } else if (isFuel(debug5)) {
/* 140 */         if (!moveItemStackTo(debug5, 1, 2, false)) {
/* 141 */           return ItemStack.EMPTY;
/*     */         }
/* 143 */       } else if (debug2 >= 3 && debug2 < 30) {
/* 144 */         if (!moveItemStackTo(debug5, 30, 39, false)) {
/* 145 */           return ItemStack.EMPTY;
/*     */         }
/* 147 */       } else if (debug2 >= 30 && debug2 < 39 && 
/* 148 */         !moveItemStackTo(debug5, 3, 30, false)) {
/* 149 */         return ItemStack.EMPTY;
/*     */       } 
/*     */       
/* 152 */       if (debug5.isEmpty()) {
/* 153 */         debug4.set(ItemStack.EMPTY);
/*     */       } else {
/* 155 */         debug4.setChanged();
/*     */       } 
/* 157 */       if (debug5.getCount() == debug3.getCount()) {
/* 158 */         return ItemStack.EMPTY;
/*     */       }
/* 160 */       debug4.onTake(debug1, debug5);
/*     */     } 
/*     */     
/* 163 */     return debug3;
/*     */   }
/*     */   
/*     */   protected boolean canSmelt(ItemStack debug1) {
/* 167 */     return this.level.getRecipeManager().getRecipeFor(this.recipeType, (Container)new SimpleContainer(new ItemStack[] { debug1 }, ), this.level).isPresent();
/*     */   }
/*     */   
/*     */   protected boolean isFuel(ItemStack debug1) {
/* 171 */     return AbstractFurnaceBlockEntity.isFuel(debug1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\inventory\AbstractFurnaceMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */