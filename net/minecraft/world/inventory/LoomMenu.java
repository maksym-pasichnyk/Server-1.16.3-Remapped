/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.BannerPatternItem;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.DyeItem;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.BannerPattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LoomMenu
/*     */   extends AbstractContainerMenu
/*     */ {
/*     */   private final ContainerLevelAccess access;
/*  29 */   private final DataSlot selectedBannerPatternIndex = DataSlot.standalone();
/*     */   private Runnable slotUpdateListener = () -> {
/*     */     
/*     */     };
/*     */   private final Slot bannerSlot;
/*     */   private final Slot dyeSlot;
/*     */   private final Slot patternSlot;
/*     */   private final Slot resultSlot;
/*     */   private long lastSoundTime;
/*     */   
/*  39 */   private final Container inputContainer = (Container)new SimpleContainer(3)
/*     */     {
/*     */       public void setChanged() {
/*  42 */         super.setChanged();
/*  43 */         LoomMenu.this.slotsChanged((Container)this);
/*  44 */         LoomMenu.this.slotUpdateListener.run();
/*     */       }
/*     */     };
/*     */   
/*  48 */   private final Container outputContainer = (Container)new SimpleContainer(1)
/*     */     {
/*     */       public void setChanged() {
/*  51 */         super.setChanged();
/*  52 */         LoomMenu.this.slotUpdateListener.run();
/*     */       }
/*     */     };
/*     */   
/*     */   public LoomMenu(int debug1, Inventory debug2) {
/*  57 */     this(debug1, debug2, ContainerLevelAccess.NULL);
/*     */   }
/*     */   
/*     */   public LoomMenu(int debug1, Inventory debug2, final ContainerLevelAccess access) {
/*  61 */     super(MenuType.LOOM, debug1);
/*  62 */     this.access = access;
/*     */     
/*  64 */     this.bannerSlot = addSlot(new Slot(this.inputContainer, 0, 13, 26)
/*     */         {
/*     */           public boolean mayPlace(ItemStack debug1) {
/*  67 */             return debug1.getItem() instanceof net.minecraft.world.item.BannerItem;
/*     */           }
/*     */         });
/*     */     
/*  71 */     this.dyeSlot = addSlot(new Slot(this.inputContainer, 1, 33, 26)
/*     */         {
/*     */           public boolean mayPlace(ItemStack debug1) {
/*  74 */             return debug1.getItem() instanceof DyeItem;
/*     */           }
/*     */         });
/*     */     
/*  78 */     this.patternSlot = addSlot(new Slot(this.inputContainer, 2, 23, 45)
/*     */         {
/*     */           public boolean mayPlace(ItemStack debug1) {
/*  81 */             return debug1.getItem() instanceof BannerPatternItem;
/*     */           }
/*     */         });
/*     */     
/*  85 */     this.resultSlot = addSlot(new Slot(this.outputContainer, 0, 143, 58)
/*     */         {
/*     */           public boolean mayPlace(ItemStack debug1) {
/*  88 */             return false;
/*     */           }
/*     */ 
/*     */           
/*     */           public ItemStack onTake(Player debug1, ItemStack debug2) {
/*  93 */             LoomMenu.this.bannerSlot.remove(1);
/*  94 */             LoomMenu.this.dyeSlot.remove(1);
/*  95 */             if (!LoomMenu.this.bannerSlot.hasItem() || !LoomMenu.this.dyeSlot.hasItem()) {
/*  96 */               LoomMenu.this.selectedBannerPatternIndex.set(0);
/*     */             }
/*  98 */             access.execute((debug1, debug2) -> {
/*     */                   long debug3 = debug1.getGameTime();
/*     */                   
/*     */                   if (LoomMenu.this.lastSoundTime != debug3) {
/*     */                     debug1.playSound(null, debug2, SoundEvents.UI_LOOM_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */                     
/*     */                     LoomMenu.this.lastSoundTime = debug3;
/*     */                   } 
/*     */                 });
/*     */             
/* 108 */             return super.onTake(debug1, debug2);
/*     */           }
/*     */         });
/*     */     int debug4;
/* 112 */     for (debug4 = 0; debug4 < 3; debug4++) {
/* 113 */       for (int debug5 = 0; debug5 < 9; debug5++) {
/* 114 */         addSlot(new Slot((Container)debug2, debug5 + debug4 * 9 + 9, 8 + debug5 * 18, 84 + debug4 * 18));
/*     */       }
/*     */     } 
/* 117 */     for (debug4 = 0; debug4 < 9; debug4++) {
/* 118 */       addSlot(new Slot((Container)debug2, debug4, 8 + debug4 * 18, 142));
/*     */     }
/*     */     
/* 121 */     addDataSlot(this.selectedBannerPatternIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player debug1) {
/* 130 */     return stillValid(this.access, debug1, Blocks.LOOM);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean clickMenuButton(Player debug1, int debug2) {
/* 135 */     if (debug2 > 0 && debug2 <= BannerPattern.AVAILABLE_PATTERNS) {
/* 136 */       this.selectedBannerPatternIndex.set(debug2);
/* 137 */       setupResultSlot();
/* 138 */       return true;
/*     */     } 
/*     */     
/* 141 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void slotsChanged(Container debug1) {
/* 146 */     ItemStack debug2 = this.bannerSlot.getItem();
/* 147 */     ItemStack debug3 = this.dyeSlot.getItem();
/* 148 */     ItemStack debug4 = this.patternSlot.getItem();
/* 149 */     ItemStack debug5 = this.resultSlot.getItem();
/*     */     
/* 151 */     if (!debug5.isEmpty() && (debug2.isEmpty() || debug3.isEmpty() || this.selectedBannerPatternIndex.get() <= 0 || (this.selectedBannerPatternIndex.get() >= BannerPattern.COUNT - BannerPattern.PATTERN_ITEM_COUNT && debug4.isEmpty()))) {
/* 152 */       this.resultSlot.set(ItemStack.EMPTY);
/* 153 */       this.selectedBannerPatternIndex.set(0);
/* 154 */     } else if (!debug4.isEmpty() && debug4.getItem() instanceof BannerPatternItem) {
/* 155 */       CompoundTag debug6 = debug2.getOrCreateTagElement("BlockEntityTag");
/* 156 */       boolean debug7 = (debug6.contains("Patterns", 9) && !debug2.isEmpty() && debug6.getList("Patterns", 10).size() >= 6);
/* 157 */       if (debug7) {
/* 158 */         this.selectedBannerPatternIndex.set(0);
/*     */       } else {
/* 160 */         this.selectedBannerPatternIndex.set(((BannerPatternItem)debug4.getItem()).getBannerPattern().ordinal());
/*     */       } 
/*     */     } 
/*     */     
/* 164 */     setupResultSlot();
/* 165 */     broadcastChanges();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack quickMoveStack(Player debug1, int debug2) {
/* 174 */     ItemStack debug3 = ItemStack.EMPTY;
/* 175 */     Slot debug4 = this.slots.get(debug2);
/* 176 */     if (debug4 != null && debug4.hasItem()) {
/* 177 */       ItemStack debug5 = debug4.getItem();
/* 178 */       debug3 = debug5.copy();
/*     */       
/* 180 */       if (debug2 == this.resultSlot.index) {
/* 181 */         if (!moveItemStackTo(debug5, 4, 40, true)) {
/* 182 */           return ItemStack.EMPTY;
/*     */         }
/* 184 */         debug4.onQuickCraft(debug5, debug3);
/* 185 */       } else if (debug2 == this.dyeSlot.index || debug2 == this.bannerSlot.index || debug2 == this.patternSlot.index) {
/* 186 */         if (!moveItemStackTo(debug5, 4, 40, false)) {
/* 187 */           return ItemStack.EMPTY;
/*     */         }
/* 189 */       } else if (debug5.getItem() instanceof net.minecraft.world.item.BannerItem) {
/* 190 */         if (!moveItemStackTo(debug5, this.bannerSlot.index, this.bannerSlot.index + 1, false)) {
/* 191 */           return ItemStack.EMPTY;
/*     */         }
/* 193 */       } else if (debug5.getItem() instanceof DyeItem) {
/* 194 */         if (!moveItemStackTo(debug5, this.dyeSlot.index, this.dyeSlot.index + 1, false)) {
/* 195 */           return ItemStack.EMPTY;
/*     */         }
/* 197 */       } else if (debug5.getItem() instanceof BannerPatternItem) {
/* 198 */         if (!moveItemStackTo(debug5, this.patternSlot.index, this.patternSlot.index + 1, false)) {
/* 199 */           return ItemStack.EMPTY;
/*     */         }
/* 201 */       } else if (debug2 >= 4 && debug2 < 31) {
/* 202 */         if (!moveItemStackTo(debug5, 31, 40, false)) {
/* 203 */           return ItemStack.EMPTY;
/*     */         }
/* 205 */       } else if (debug2 >= 31 && debug2 < 40 && 
/* 206 */         !moveItemStackTo(debug5, 4, 31, false)) {
/* 207 */         return ItemStack.EMPTY;
/*     */       } 
/*     */ 
/*     */       
/* 211 */       if (debug5.isEmpty()) {
/* 212 */         debug4.set(ItemStack.EMPTY);
/*     */       } else {
/* 214 */         debug4.setChanged();
/*     */       } 
/* 216 */       if (debug5.getCount() == debug3.getCount()) {
/* 217 */         return ItemStack.EMPTY;
/*     */       }
/* 219 */       debug4.onTake(debug1, debug5);
/*     */     } 
/*     */     
/* 222 */     return debug3;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removed(Player debug1) {
/* 227 */     super.removed(debug1);
/* 228 */     this.access.execute((debug2, debug3) -> clearContainer(debug1, debug1.level, this.inputContainer));
/*     */   }
/*     */   
/*     */   private void setupResultSlot() {
/* 232 */     if (this.selectedBannerPatternIndex.get() > 0) {
/* 233 */       ItemStack debug1 = this.bannerSlot.getItem();
/* 234 */       ItemStack debug2 = this.dyeSlot.getItem();
/* 235 */       ItemStack debug3 = ItemStack.EMPTY;
/*     */       
/* 237 */       if (!debug1.isEmpty() && !debug2.isEmpty()) {
/* 238 */         ListTag debug7; debug3 = debug1.copy();
/* 239 */         debug3.setCount(1);
/*     */         
/* 241 */         BannerPattern debug4 = BannerPattern.values()[this.selectedBannerPatternIndex.get()];
/* 242 */         DyeColor debug5 = ((DyeItem)debug2.getItem()).getDyeColor();
/*     */         
/* 244 */         CompoundTag debug6 = debug3.getOrCreateTagElement("BlockEntityTag");
/*     */         
/* 246 */         if (debug6.contains("Patterns", 9)) {
/* 247 */           debug7 = debug6.getList("Patterns", 10);
/*     */         } else {
/* 249 */           debug7 = new ListTag();
/* 250 */           debug6.put("Patterns", (Tag)debug7);
/*     */         } 
/* 252 */         CompoundTag debug8 = new CompoundTag();
/* 253 */         debug8.putString("Pattern", debug4.getHashname());
/* 254 */         debug8.putInt("Color", debug5.getId());
/* 255 */         debug7.add(debug8);
/*     */       } 
/* 257 */       if (!ItemStack.matches(debug3, this.resultSlot.getItem()))
/* 258 */         this.resultSlot.set(debug3); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\inventory\LoomMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */