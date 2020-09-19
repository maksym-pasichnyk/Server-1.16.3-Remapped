/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.player.StackedContents;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.crafting.CraftingRecipe;
/*     */ import net.minecraft.world.item.crafting.Recipe;
/*     */ import net.minecraft.world.item.crafting.RecipeType;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CraftingMenu
/*     */   extends RecipeBookMenu<CraftingContainer>
/*     */ {
/*  27 */   private final CraftingContainer craftSlots = new CraftingContainer(this, 3, 3);
/*  28 */   private final ResultContainer resultSlots = new ResultContainer();
/*     */   
/*     */   private final ContainerLevelAccess access;
/*     */   private final Player player;
/*     */   
/*     */   public CraftingMenu(int debug1, Inventory debug2) {
/*  34 */     this(debug1, debug2, ContainerLevelAccess.NULL);
/*     */   }
/*     */   
/*     */   public CraftingMenu(int debug1, Inventory debug2, ContainerLevelAccess debug3) {
/*  38 */     super(MenuType.CRAFTING, debug1);
/*  39 */     this.access = debug3;
/*  40 */     this.player = debug2.player;
/*  41 */     addSlot(new ResultSlot(debug2.player, this.craftSlots, this.resultSlots, 0, 124, 35));
/*     */     int debug4;
/*  43 */     for (debug4 = 0; debug4 < 3; debug4++) {
/*  44 */       for (int debug5 = 0; debug5 < 3; debug5++) {
/*  45 */         addSlot(new Slot(this.craftSlots, debug5 + debug4 * 3, 30 + debug5 * 18, 17 + debug4 * 18));
/*     */       }
/*     */     } 
/*     */     
/*  49 */     for (debug4 = 0; debug4 < 3; debug4++) {
/*  50 */       for (int debug5 = 0; debug5 < 9; debug5++) {
/*  51 */         addSlot(new Slot((Container)debug2, debug5 + debug4 * 9 + 9, 8 + debug5 * 18, 84 + debug4 * 18));
/*     */       }
/*     */     } 
/*  54 */     for (debug4 = 0; debug4 < 9; debug4++) {
/*  55 */       addSlot(new Slot((Container)debug2, debug4, 8 + debug4 * 18, 142));
/*     */     }
/*     */   }
/*     */   
/*     */   protected static void slotChangedCraftingGrid(int debug0, Level debug1, Player debug2, CraftingContainer debug3, ResultContainer debug4) {
/*  60 */     if (debug1.isClientSide) {
/*     */       return;
/*     */     }
/*     */     
/*  64 */     ServerPlayer debug5 = (ServerPlayer)debug2;
/*  65 */     ItemStack debug6 = ItemStack.EMPTY;
/*  66 */     Optional<CraftingRecipe> debug7 = debug1.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, debug3, debug1);
/*  67 */     if (debug7.isPresent()) {
/*  68 */       CraftingRecipe debug8 = debug7.get();
/*  69 */       if (debug4.setRecipeUsed(debug1, debug5, (Recipe<?>)debug8)) {
/*  70 */         debug6 = debug8.assemble(debug3);
/*     */       }
/*     */     } 
/*     */     
/*  74 */     debug4.setItem(0, debug6);
/*  75 */     debug5.connection.send((Packet)new ClientboundContainerSetSlotPacket(debug0, 0, debug6));
/*     */   }
/*     */ 
/*     */   
/*     */   public void slotsChanged(Container debug1) {
/*  80 */     this.access.execute((debug1, debug2) -> slotChangedCraftingGrid(this.containerId, debug1, this.player, this.craftSlots, this.resultSlots));
/*     */   }
/*     */ 
/*     */   
/*     */   public void fillCraftSlotsStackedContents(StackedContents debug1) {
/*  85 */     this.craftSlots.fillStackedContents(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearCraftingContent() {
/*  90 */     this.craftSlots.clearContent();
/*  91 */     this.resultSlots.clearContent();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean recipeMatches(Recipe<? super CraftingContainer> debug1) {
/*  96 */     return debug1.matches(this.craftSlots, this.player.level);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removed(Player debug1) {
/* 101 */     super.removed(debug1);
/* 102 */     this.access.execute((debug2, debug3) -> clearContainer(debug1, debug2, this.craftSlots));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player debug1) {
/* 107 */     return stillValid(this.access, debug1, Blocks.CRAFTING_TABLE);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack quickMoveStack(Player debug1, int debug2) {
/* 112 */     ItemStack debug3 = ItemStack.EMPTY;
/* 113 */     Slot debug4 = this.slots.get(debug2);
/* 114 */     if (debug4 != null && debug4.hasItem()) {
/* 115 */       ItemStack debug5 = debug4.getItem();
/* 116 */       debug3 = debug5.copy();
/*     */       
/* 118 */       if (debug2 == 0) {
/* 119 */         this.access.execute((debug2, debug3) -> debug0.getItem().onCraftedBy(debug0, debug2, debug1));
/* 120 */         if (!moveItemStackTo(debug5, 10, 46, true)) {
/* 121 */           return ItemStack.EMPTY;
/*     */         }
/* 123 */         debug4.onQuickCraft(debug5, debug3);
/* 124 */       } else if (debug2 >= 10 && debug2 < 46) {
/* 125 */         if (!moveItemStackTo(debug5, 1, 10, false)) {
/* 126 */           if (debug2 < 37) {
/* 127 */             if (!moveItemStackTo(debug5, 37, 46, false)) {
/* 128 */               return ItemStack.EMPTY;
/*     */             }
/*     */           }
/* 131 */           else if (!moveItemStackTo(debug5, 10, 37, false)) {
/* 132 */             return ItemStack.EMPTY;
/*     */           }
/*     */         
/*     */         }
/*     */       }
/* 137 */       else if (!moveItemStackTo(debug5, 10, 46, false)) {
/* 138 */         return ItemStack.EMPTY;
/*     */       } 
/*     */       
/* 141 */       if (debug5.isEmpty()) {
/* 142 */         debug4.set(ItemStack.EMPTY);
/*     */       } else {
/* 144 */         debug4.setChanged();
/*     */       } 
/* 146 */       if (debug5.getCount() == debug3.getCount())
/*     */       {
/* 148 */         return ItemStack.EMPTY;
/*     */       }
/* 150 */       ItemStack debug6 = debug4.onTake(debug1, debug5);
/* 151 */       if (debug2 == 0) {
/* 152 */         debug1.drop(debug6, false);
/*     */       }
/*     */     } 
/*     */     
/* 156 */     return debug3;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canTakeItemForPickAll(ItemStack debug1, Slot debug2) {
/* 161 */     return (debug2.container != this.resultSlots && super.canTakeItemForPickAll(debug1, debug2));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getResultSlotIndex() {
/* 166 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getGridWidth() {
/* 171 */     return this.craftSlots.getWidth();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getGridHeight() {
/* 176 */     return this.craftSlots.getHeight();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\inventory\CraftingMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */