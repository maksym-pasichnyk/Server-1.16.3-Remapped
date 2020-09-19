/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.alchemy.Potion;
/*     */ import net.minecraft.world.item.alchemy.PotionBrewing;
/*     */ import net.minecraft.world.item.alchemy.PotionUtils;
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
/*     */ public class BrewingStandMenu
/*     */   extends AbstractContainerMenu
/*     */ {
/*     */   private final Container brewingStand;
/*     */   private final ContainerData brewingStandData;
/*     */   private final Slot ingredientSlot;
/*     */   
/*     */   public BrewingStandMenu(int debug1, Inventory debug2) {
/*  36 */     this(debug1, debug2, (Container)new SimpleContainer(5), new SimpleContainerData(2));
/*     */   }
/*     */   
/*     */   public BrewingStandMenu(int debug1, Inventory debug2, Container debug3, ContainerData debug4) {
/*  40 */     super(MenuType.BREWING_STAND, debug1);
/*  41 */     checkContainerSize(debug3, 5);
/*  42 */     checkContainerDataCount(debug4, 2);
/*  43 */     this.brewingStand = debug3;
/*  44 */     this.brewingStandData = debug4;
/*     */     
/*  46 */     addSlot(new PotionSlot(debug3, 0, 56, 51));
/*  47 */     addSlot(new PotionSlot(debug3, 1, 79, 58));
/*  48 */     addSlot(new PotionSlot(debug3, 2, 102, 51));
/*  49 */     this.ingredientSlot = addSlot(new IngredientsSlot(debug3, 3, 79, 17));
/*  50 */     addSlot(new FuelSlot(debug3, 4, 17, 17));
/*     */     
/*  52 */     addDataSlots(debug4);
/*     */     int debug5;
/*  54 */     for (debug5 = 0; debug5 < 3; debug5++) {
/*  55 */       for (int debug6 = 0; debug6 < 9; debug6++) {
/*  56 */         addSlot(new Slot((Container)debug2, debug6 + debug5 * 9 + 9, 8 + debug6 * 18, 84 + debug5 * 18));
/*     */       }
/*     */     } 
/*  59 */     for (debug5 = 0; debug5 < 9; debug5++) {
/*  60 */       addSlot(new Slot((Container)debug2, debug5, 8 + debug5 * 18, 142));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player debug1) {
/*  66 */     return this.brewingStand.stillValid(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack quickMoveStack(Player debug1, int debug2) {
/*  71 */     ItemStack debug3 = ItemStack.EMPTY;
/*  72 */     Slot debug4 = this.slots.get(debug2);
/*  73 */     if (debug4 != null && debug4.hasItem()) {
/*  74 */       ItemStack debug5 = debug4.getItem();
/*  75 */       debug3 = debug5.copy();
/*     */       
/*  77 */       if ((debug2 >= 0 && debug2 <= 2) || debug2 == 3 || debug2 == 4) {
/*  78 */         if (!moveItemStackTo(debug5, 5, 41, true)) {
/*  79 */           return ItemStack.EMPTY;
/*     */         }
/*  81 */         debug4.onQuickCraft(debug5, debug3);
/*  82 */       } else if (FuelSlot.mayPlaceItem(debug3)) {
/*  83 */         if (moveItemStackTo(debug5, 4, 5, false) || (this.ingredientSlot.mayPlace(debug5) && !moveItemStackTo(debug5, 3, 4, false))) {
/*  84 */           return ItemStack.EMPTY;
/*     */         }
/*  86 */       } else if (this.ingredientSlot.mayPlace(debug5)) {
/*  87 */         if (!moveItemStackTo(debug5, 3, 4, false)) {
/*  88 */           return ItemStack.EMPTY;
/*     */         }
/*  90 */       } else if (PotionSlot.mayPlaceItem(debug3) && debug3.getCount() == 1) {
/*  91 */         if (!moveItemStackTo(debug5, 0, 3, false)) {
/*  92 */           return ItemStack.EMPTY;
/*     */         }
/*  94 */       } else if (debug2 >= 5 && debug2 < 32) {
/*  95 */         if (!moveItemStackTo(debug5, 32, 41, false)) {
/*  96 */           return ItemStack.EMPTY;
/*     */         }
/*  98 */       } else if (debug2 >= 32 && debug2 < 41) {
/*  99 */         if (!moveItemStackTo(debug5, 5, 32, false)) {
/* 100 */           return ItemStack.EMPTY;
/*     */         }
/*     */       }
/* 103 */       else if (!moveItemStackTo(debug5, 5, 41, false)) {
/* 104 */         return ItemStack.EMPTY;
/*     */       } 
/*     */       
/* 107 */       if (debug5.isEmpty()) {
/* 108 */         debug4.set(ItemStack.EMPTY);
/*     */       } else {
/* 110 */         debug4.setChanged();
/*     */       } 
/* 112 */       if (debug5.getCount() == debug3.getCount()) {
/* 113 */         return ItemStack.EMPTY;
/*     */       }
/* 115 */       debug4.onTake(debug1, debug5);
/*     */     } 
/*     */     
/* 118 */     return debug3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class PotionSlot
/*     */     extends Slot
/*     */   {
/*     */     public PotionSlot(Container debug1, int debug2, int debug3, int debug4) {
/* 131 */       super(debug1, debug2, debug3, debug4);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean mayPlace(ItemStack debug1) {
/* 136 */       return mayPlaceItem(debug1);
/*     */     }
/*     */ 
/*     */     
/*     */     public int getMaxStackSize() {
/* 141 */       return 1;
/*     */     }
/*     */ 
/*     */     
/*     */     public ItemStack onTake(Player debug1, ItemStack debug2) {
/* 146 */       Potion debug3 = PotionUtils.getPotion(debug2);
/* 147 */       if (debug1 instanceof ServerPlayer) {
/* 148 */         CriteriaTriggers.BREWED_POTION.trigger((ServerPlayer)debug1, debug3);
/*     */       }
/* 150 */       super.onTake(debug1, debug2);
/* 151 */       return debug2;
/*     */     }
/*     */     
/*     */     public static boolean mayPlaceItem(ItemStack debug0) {
/* 155 */       Item debug1 = debug0.getItem();
/* 156 */       return (debug1 == Items.POTION || debug1 == Items.SPLASH_POTION || debug1 == Items.LINGERING_POTION || debug1 == Items.GLASS_BOTTLE);
/*     */     }
/*     */   }
/*     */   
/*     */   static class IngredientsSlot extends Slot {
/*     */     public IngredientsSlot(Container debug1, int debug2, int debug3, int debug4) {
/* 162 */       super(debug1, debug2, debug3, debug4);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean mayPlace(ItemStack debug1) {
/* 167 */       return PotionBrewing.isIngredient(debug1);
/*     */     }
/*     */ 
/*     */     
/*     */     public int getMaxStackSize() {
/* 172 */       return 64;
/*     */     }
/*     */   }
/*     */   
/*     */   static class FuelSlot extends Slot {
/*     */     public FuelSlot(Container debug1, int debug2, int debug3, int debug4) {
/* 178 */       super(debug1, debug2, debug3, debug4);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean mayPlace(ItemStack debug1) {
/* 183 */       return mayPlaceItem(debug1);
/*     */     }
/*     */     
/*     */     public static boolean mayPlaceItem(ItemStack debug0) {
/* 187 */       return (debug0.getItem() == Items.BLAZE_POWDER);
/*     */     }
/*     */ 
/*     */     
/*     */     public int getMaxStackSize() {
/* 192 */       return 64;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\inventory\BrewingStandMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */