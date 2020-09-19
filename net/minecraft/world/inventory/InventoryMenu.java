/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.player.StackedContents;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.crafting.Recipe;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
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
/*     */ public class InventoryMenu
/*     */   extends RecipeBookMenu<CraftingContainer>
/*     */ {
/*  29 */   public static final ResourceLocation BLOCK_ATLAS = new ResourceLocation("textures/atlas/blocks.png");
/*     */   
/*  31 */   public static final ResourceLocation EMPTY_ARMOR_SLOT_HELMET = new ResourceLocation("item/empty_armor_slot_helmet");
/*  32 */   public static final ResourceLocation EMPTY_ARMOR_SLOT_CHESTPLATE = new ResourceLocation("item/empty_armor_slot_chestplate");
/*  33 */   public static final ResourceLocation EMPTY_ARMOR_SLOT_LEGGINGS = new ResourceLocation("item/empty_armor_slot_leggings");
/*  34 */   public static final ResourceLocation EMPTY_ARMOR_SLOT_BOOTS = new ResourceLocation("item/empty_armor_slot_boots");
/*  35 */   public static final ResourceLocation EMPTY_ARMOR_SLOT_SHIELD = new ResourceLocation("item/empty_armor_slot_shield");
/*     */ 
/*     */   
/*  38 */   private static final ResourceLocation[] TEXTURE_EMPTY_SLOTS = new ResourceLocation[] { EMPTY_ARMOR_SLOT_BOOTS, EMPTY_ARMOR_SLOT_LEGGINGS, EMPTY_ARMOR_SLOT_CHESTPLATE, EMPTY_ARMOR_SLOT_HELMET };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  45 */   private static final EquipmentSlot[] SLOT_IDS = new EquipmentSlot[] { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  52 */   private final CraftingContainer craftSlots = new CraftingContainer(this, 2, 2);
/*  53 */   private final ResultContainer resultSlots = new ResultContainer();
/*     */   public final boolean active;
/*     */   private final Player owner;
/*     */   
/*     */   public InventoryMenu(Inventory debug1, boolean debug2, Player debug3) {
/*  58 */     super((MenuType<?>)null, 0);
/*  59 */     this.active = debug2;
/*  60 */     this.owner = debug3;
/*  61 */     addSlot(new ResultSlot(debug1.player, this.craftSlots, this.resultSlots, 0, 154, 28));
/*     */     int debug4;
/*  63 */     for (debug4 = 0; debug4 < 2; debug4++) {
/*  64 */       for (final int slot = 0; debug5 < 2; debug5++) {
/*  65 */         addSlot(new Slot(this.craftSlots, debug5 + debug4 * 2, 98 + debug5 * 18, 18 + debug4 * 18));
/*     */       }
/*     */     } 
/*     */     
/*  69 */     for (debug4 = 0; debug4 < 4; debug4++) {
/*  70 */       final EquipmentSlot slot = SLOT_IDS[debug4];
/*  71 */       addSlot(new Slot((Container)debug1, 39 - debug4, 8, 8 + debug4 * 18)
/*     */           {
/*     */             public int getMaxStackSize() {
/*  74 */               return 1;
/*     */             }
/*     */ 
/*     */             
/*     */             public boolean mayPlace(ItemStack debug1) {
/*  79 */               return (slot == Mob.getEquipmentSlotForItem(debug1));
/*     */             }
/*     */ 
/*     */             
/*     */             public boolean mayPickup(Player debug1) {
/*  84 */               ItemStack debug2 = getItem();
/*  85 */               if (!debug2.isEmpty() && !debug1.isCreative() && EnchantmentHelper.hasBindingCurse(debug2)) {
/*  86 */                 return false;
/*     */               }
/*  88 */               return super.mayPickup(debug1);
/*     */             }
/*     */           });
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  97 */     for (debug4 = 0; debug4 < 3; debug4++) {
/*  98 */       for (final int slot = 0; debug5 < 9; debug5++) {
/*  99 */         addSlot(new Slot((Container)debug1, debug5 + (debug4 + 1) * 9, 8 + debug5 * 18, 84 + debug4 * 18));
/*     */       }
/*     */     } 
/* 102 */     for (debug4 = 0; debug4 < 9; debug4++) {
/* 103 */       addSlot(new Slot((Container)debug1, debug4, 8 + debug4 * 18, 142));
/*     */     }
/*     */     
/* 106 */     addSlot(new Slot((Container)debug1, 40, 77, 62)
/*     */         {
/*     */         
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fillCraftSlotsStackedContents(StackedContents debug1) {
/* 116 */     this.craftSlots.fillStackedContents(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearCraftingContent() {
/* 121 */     this.resultSlots.clearContent();
/* 122 */     this.craftSlots.clearContent();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean recipeMatches(Recipe<? super CraftingContainer> debug1) {
/* 127 */     return debug1.matches(this.craftSlots, this.owner.level);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void slotsChanged(Container debug1) {
/* 134 */     CraftingMenu.slotChangedCraftingGrid(this.containerId, this.owner.level, this.owner, this.craftSlots, this.resultSlots);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removed(Player debug1) {
/* 139 */     super.removed(debug1);
/*     */     
/* 141 */     this.resultSlots.clearContent();
/*     */     
/* 143 */     if (debug1.level.isClientSide) {
/*     */       return;
/*     */     }
/*     */     
/* 147 */     clearContainer(debug1, debug1.level, this.craftSlots);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player debug1) {
/* 152 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack quickMoveStack(Player debug1, int debug2) {
/* 157 */     ItemStack debug3 = ItemStack.EMPTY;
/* 158 */     Slot debug4 = this.slots.get(debug2);
/* 159 */     if (debug4 != null && debug4.hasItem()) {
/* 160 */       ItemStack debug5 = debug4.getItem();
/* 161 */       debug3 = debug5.copy();
/*     */       
/* 163 */       EquipmentSlot debug6 = Mob.getEquipmentSlotForItem(debug3);
/*     */       
/* 165 */       if (debug2 == 0) {
/* 166 */         if (!moveItemStackTo(debug5, 9, 45, true)) {
/* 167 */           return ItemStack.EMPTY;
/*     */         }
/* 169 */         debug4.onQuickCraft(debug5, debug3);
/* 170 */       } else if (debug2 >= 1 && debug2 < 5) {
/* 171 */         if (!moveItemStackTo(debug5, 9, 45, false)) {
/* 172 */           return ItemStack.EMPTY;
/*     */         }
/* 174 */       } else if (debug2 >= 5 && debug2 < 9) {
/* 175 */         if (!moveItemStackTo(debug5, 9, 45, false)) {
/* 176 */           return ItemStack.EMPTY;
/*     */         }
/* 178 */       } else if (debug6.getType() == EquipmentSlot.Type.ARMOR && !((Slot)this.slots.get(8 - debug6.getIndex())).hasItem()) {
/* 179 */         int i = 8 - debug6.getIndex();
/* 180 */         if (!moveItemStackTo(debug5, i, i + 1, false)) {
/* 181 */           return ItemStack.EMPTY;
/*     */         }
/* 183 */       } else if (debug6 == EquipmentSlot.OFFHAND && !((Slot)this.slots.get(45)).hasItem()) {
/* 184 */         if (!moveItemStackTo(debug5, 45, 46, false)) {
/* 185 */           return ItemStack.EMPTY;
/*     */         }
/* 187 */       } else if (debug2 >= 9 && debug2 < 36) {
/* 188 */         if (!moveItemStackTo(debug5, 36, 45, false)) {
/* 189 */           return ItemStack.EMPTY;
/*     */         }
/* 191 */       } else if (debug2 >= 36 && debug2 < 45) {
/* 192 */         if (!moveItemStackTo(debug5, 9, 36, false)) {
/* 193 */           return ItemStack.EMPTY;
/*     */         }
/*     */       }
/* 196 */       else if (!moveItemStackTo(debug5, 9, 45, false)) {
/* 197 */         return ItemStack.EMPTY;
/*     */       } 
/*     */       
/* 200 */       if (debug5.isEmpty()) {
/* 201 */         debug4.set(ItemStack.EMPTY);
/*     */       } else {
/* 203 */         debug4.setChanged();
/*     */       } 
/* 205 */       if (debug5.getCount() == debug3.getCount())
/*     */       {
/* 207 */         return ItemStack.EMPTY;
/*     */       }
/* 209 */       ItemStack debug7 = debug4.onTake(debug1, debug5);
/* 210 */       if (debug2 == 0) {
/* 211 */         debug1.drop(debug7, false);
/*     */       }
/*     */     } 
/*     */     
/* 215 */     return debug3;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canTakeItemForPickAll(ItemStack debug1, Slot debug2) {
/* 220 */     return (debug2.container != this.resultSlots && super.canTakeItemForPickAll(debug1, debug2));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getResultSlotIndex() {
/* 225 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getGridWidth() {
/* 230 */     return this.craftSlots.getWidth();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getGridHeight() {
/* 235 */     return this.craftSlots.getHeight();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CraftingContainer getCraftSlots() {
/* 244 */     return this.craftSlots;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\inventory\InventoryMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */