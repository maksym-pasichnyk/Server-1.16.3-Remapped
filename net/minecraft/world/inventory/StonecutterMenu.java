/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.crafting.Recipe;
/*     */ import net.minecraft.world.item.crafting.RecipeType;
/*     */ import net.minecraft.world.item.crafting.StonecutterRecipe;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StonecutterMenu
/*     */   extends AbstractContainerMenu
/*     */ {
/*     */   private final ContainerLevelAccess access;
/*  28 */   private final DataSlot selectedRecipeIndex = DataSlot.standalone();
/*     */   
/*     */   private final Level level;
/*  31 */   private List<StonecutterRecipe> recipes = Lists.newArrayList();
/*  32 */   private ItemStack input = ItemStack.EMPTY;
/*     */   private long lastSoundTime;
/*     */   final Slot inputSlot;
/*     */   final Slot resultSlot;
/*     */   private Runnable slotUpdateListener = () -> {
/*     */     
/*     */     };
/*     */   
/*  40 */   public final Container container = (Container)new SimpleContainer(1)
/*     */     {
/*     */       public void setChanged() {
/*  43 */         super.setChanged();
/*  44 */         StonecutterMenu.this.slotsChanged((Container)this);
/*  45 */         StonecutterMenu.this.slotUpdateListener.run();
/*     */       }
/*     */     };
/*  48 */   private final ResultContainer resultContainer = new ResultContainer();
/*     */   
/*     */   public StonecutterMenu(int debug1, Inventory debug2) {
/*  51 */     this(debug1, debug2, ContainerLevelAccess.NULL);
/*     */   }
/*     */   
/*     */   public StonecutterMenu(int debug1, Inventory debug2, final ContainerLevelAccess access) {
/*  55 */     super(MenuType.STONECUTTER, debug1);
/*     */     
/*  57 */     this.access = access;
/*  58 */     this.level = debug2.player.level;
/*     */     
/*  60 */     this.inputSlot = addSlot(new Slot(this.container, 0, 20, 33));
/*     */     
/*  62 */     this.resultSlot = addSlot(new Slot(this.resultContainer, 1, 143, 33)
/*     */         {
/*     */           public boolean mayPlace(ItemStack debug1) {
/*  65 */             return false;
/*     */           }
/*     */ 
/*     */           
/*     */           public ItemStack onTake(Player debug1, ItemStack debug2) {
/*  70 */             debug2.onCraftedBy(debug1.level, debug1, debug2.getCount());
/*  71 */             StonecutterMenu.this.resultContainer.awardUsedRecipes(debug1);
/*     */ 
/*     */             
/*  74 */             ItemStack debug3 = StonecutterMenu.this.inputSlot.remove(1);
/*  75 */             if (!debug3.isEmpty()) {
/*  76 */               StonecutterMenu.this.setupResultSlot();
/*     */             }
/*     */             
/*  79 */             access.execute((debug1, debug2) -> {
/*     */                   long debug3 = debug1.getGameTime();
/*     */                   
/*     */                   if (StonecutterMenu.this.lastSoundTime != debug3) {
/*     */                     debug1.playSound(null, debug2, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */                     
/*     */                     StonecutterMenu.this.lastSoundTime = debug3;
/*     */                   } 
/*     */                 });
/*  88 */             return super.onTake(debug1, debug2);
/*     */           }
/*     */         });
/*     */     int debug4;
/*  92 */     for (debug4 = 0; debug4 < 3; debug4++) {
/*  93 */       for (int debug5 = 0; debug5 < 9; debug5++) {
/*  94 */         addSlot(new Slot((Container)debug2, debug5 + debug4 * 9 + 9, 8 + debug5 * 18, 84 + debug4 * 18));
/*     */       }
/*     */     } 
/*  97 */     for (debug4 = 0; debug4 < 9; debug4++) {
/*  98 */       addSlot(new Slot((Container)debug2, debug4, 8 + debug4 * 18, 142));
/*     */     }
/*     */     
/* 101 */     addDataSlot(this.selectedRecipeIndex);
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
/*     */   public boolean stillValid(Player debug1) {
/* 122 */     return stillValid(this.access, debug1, Blocks.STONECUTTER);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean clickMenuButton(Player debug1, int debug2) {
/* 127 */     if (isValidRecipeIndex(debug2)) {
/* 128 */       this.selectedRecipeIndex.set(debug2);
/* 129 */       setupResultSlot();
/*     */     } 
/*     */     
/* 132 */     return true;
/*     */   }
/*     */   
/*     */   private boolean isValidRecipeIndex(int debug1) {
/* 136 */     return (debug1 >= 0 && debug1 < this.recipes.size());
/*     */   }
/*     */ 
/*     */   
/*     */   public void slotsChanged(Container debug1) {
/* 141 */     ItemStack debug2 = this.inputSlot.getItem();
/* 142 */     if (debug2.getItem() != this.input.getItem()) {
/* 143 */       this.input = debug2.copy();
/* 144 */       setupRecipeList(debug1, debug2);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setupRecipeList(Container debug1, ItemStack debug2) {
/* 149 */     this.recipes.clear();
/* 150 */     this.selectedRecipeIndex.set(-1);
/* 151 */     this.resultSlot.set(ItemStack.EMPTY);
/*     */     
/* 153 */     if (!debug2.isEmpty()) {
/* 154 */       this.recipes = this.level.getRecipeManager().getRecipesFor(RecipeType.STONECUTTING, debug1, this.level);
/*     */     }
/*     */   }
/*     */   
/*     */   private void setupResultSlot() {
/* 159 */     if (!this.recipes.isEmpty() && isValidRecipeIndex(this.selectedRecipeIndex.get())) {
/* 160 */       StonecutterRecipe debug1 = this.recipes.get(this.selectedRecipeIndex.get());
/* 161 */       this.resultContainer.setRecipeUsed((Recipe<?>)debug1);
/* 162 */       this.resultSlot.set(debug1.assemble(this.container));
/*     */     } else {
/* 164 */       this.resultSlot.set(ItemStack.EMPTY);
/*     */     } 
/*     */     
/* 167 */     broadcastChanges();
/*     */   }
/*     */ 
/*     */   
/*     */   public MenuType<?> getType() {
/* 172 */     return MenuType.STONECUTTER;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canTakeItemForPickAll(ItemStack debug1, Slot debug2) {
/* 181 */     return (debug2.container != this.resultContainer && super.canTakeItemForPickAll(debug1, debug2));
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack quickMoveStack(Player debug1, int debug2) {
/* 186 */     ItemStack debug3 = ItemStack.EMPTY;
/* 187 */     Slot debug4 = this.slots.get(debug2);
/* 188 */     if (debug4 != null && debug4.hasItem()) {
/* 189 */       ItemStack debug5 = debug4.getItem();
/* 190 */       Item debug6 = debug5.getItem();
/* 191 */       debug3 = debug5.copy();
/*     */       
/* 193 */       if (debug2 == 1) {
/* 194 */         debug6.onCraftedBy(debug5, debug1.level, debug1);
/* 195 */         if (!moveItemStackTo(debug5, 2, 38, true)) {
/* 196 */           return ItemStack.EMPTY;
/*     */         }
/* 198 */         debug4.onQuickCraft(debug5, debug3);
/* 199 */       } else if (debug2 == 0) {
/* 200 */         if (!moveItemStackTo(debug5, 2, 38, false)) {
/* 201 */           return ItemStack.EMPTY;
/*     */         }
/* 203 */       } else if (this.level.getRecipeManager().getRecipeFor(RecipeType.STONECUTTING, (Container)new SimpleContainer(new ItemStack[] { debug5 }, ), this.level).isPresent()) {
/* 204 */         if (!moveItemStackTo(debug5, 0, 1, false)) {
/* 205 */           return ItemStack.EMPTY;
/*     */         }
/* 207 */       } else if (debug2 >= 2 && debug2 < 29) {
/* 208 */         if (!moveItemStackTo(debug5, 29, 38, false)) {
/* 209 */           return ItemStack.EMPTY;
/*     */         }
/* 211 */       } else if (debug2 >= 29 && debug2 < 38 && 
/* 212 */         !moveItemStackTo(debug5, 2, 29, false)) {
/* 213 */         return ItemStack.EMPTY;
/*     */       } 
/*     */ 
/*     */       
/* 217 */       if (debug5.isEmpty()) {
/* 218 */         debug4.set(ItemStack.EMPTY);
/*     */       }
/*     */       
/* 221 */       debug4.setChanged();
/*     */       
/* 223 */       if (debug5.getCount() == debug3.getCount()) {
/* 224 */         return ItemStack.EMPTY;
/*     */       }
/* 226 */       debug4.onTake(debug1, debug5);
/* 227 */       broadcastChanges();
/*     */     } 
/*     */     
/* 230 */     return debug3;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removed(Player debug1) {
/* 235 */     super.removed(debug1);
/*     */     
/* 237 */     this.resultContainer.removeItemNoUpdate(1);
/* 238 */     this.access.execute((debug2, debug3) -> clearContainer(debug1, debug1.level, this.container));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\inventory\StonecutterMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */