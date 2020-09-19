/*     */ package net.minecraft.world.inventory;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.EnchantedBookItem;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentInstance;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ 
/*     */ public class EnchantmentMenu extends AbstractContainerMenu {
/*  25 */   private final Container enchantSlots = (Container)new SimpleContainer(2)
/*     */     {
/*     */       public void setChanged() {
/*  28 */         super.setChanged();
/*  29 */         EnchantmentMenu.this.slotsChanged((Container)this);
/*     */       }
/*     */     };
/*     */   
/*     */   private final ContainerLevelAccess access;
/*  34 */   private final Random random = new Random();
/*  35 */   private final DataSlot enchantmentSeed = DataSlot.standalone();
/*     */   
/*  37 */   public final int[] costs = new int[3];
/*  38 */   public final int[] enchantClue = new int[] { -1, -1, -1 };
/*  39 */   public final int[] levelClue = new int[] { -1, -1, -1 };
/*     */   
/*     */   public EnchantmentMenu(int debug1, Inventory debug2) {
/*  42 */     this(debug1, debug2, ContainerLevelAccess.NULL);
/*     */   }
/*     */   
/*     */   public EnchantmentMenu(int debug1, Inventory debug2, ContainerLevelAccess debug3) {
/*  46 */     super(MenuType.ENCHANTMENT, debug1);
/*  47 */     this.access = debug3;
/*  48 */     addSlot(new Slot(this.enchantSlots, 0, 15, 47)
/*     */         {
/*     */           public boolean mayPlace(ItemStack debug1) {
/*  51 */             return true;
/*     */           }
/*     */ 
/*     */           
/*     */           public int getMaxStackSize() {
/*  56 */             return 1;
/*     */           }
/*     */         });
/*     */     
/*  60 */     addSlot(new Slot(this.enchantSlots, 1, 35, 47)
/*     */         {
/*     */           public boolean mayPlace(ItemStack debug1) {
/*  63 */             return (debug1.getItem() == Items.LAPIS_LAZULI);
/*     */           }
/*     */         });
/*     */     int debug4;
/*  67 */     for (debug4 = 0; debug4 < 3; debug4++) {
/*  68 */       for (int debug5 = 0; debug5 < 9; debug5++) {
/*  69 */         addSlot(new Slot((Container)debug2, debug5 + debug4 * 9 + 9, 8 + debug5 * 18, 84 + debug4 * 18));
/*     */       }
/*     */     } 
/*  72 */     for (debug4 = 0; debug4 < 9; debug4++) {
/*  73 */       addSlot(new Slot((Container)debug2, debug4, 8 + debug4 * 18, 142));
/*     */     }
/*     */     
/*  76 */     addDataSlot(DataSlot.shared(this.costs, 0));
/*  77 */     addDataSlot(DataSlot.shared(this.costs, 1));
/*  78 */     addDataSlot(DataSlot.shared(this.costs, 2));
/*     */     
/*  80 */     addDataSlot(this.enchantmentSeed).set(debug2.player.getEnchantmentSeed());
/*     */     
/*  82 */     addDataSlot(DataSlot.shared(this.enchantClue, 0));
/*  83 */     addDataSlot(DataSlot.shared(this.enchantClue, 1));
/*  84 */     addDataSlot(DataSlot.shared(this.enchantClue, 2));
/*     */     
/*  86 */     addDataSlot(DataSlot.shared(this.levelClue, 0));
/*  87 */     addDataSlot(DataSlot.shared(this.levelClue, 1));
/*  88 */     addDataSlot(DataSlot.shared(this.levelClue, 2));
/*     */   }
/*     */ 
/*     */   
/*     */   public void slotsChanged(Container debug1) {
/*  93 */     if (debug1 == this.enchantSlots) {
/*  94 */       ItemStack debug2 = debug1.getItem(0);
/*     */       
/*  96 */       if (debug2.isEmpty() || !debug2.isEnchantable()) {
/*  97 */         for (int debug3 = 0; debug3 < 3; debug3++) {
/*  98 */           this.costs[debug3] = 0;
/*  99 */           this.enchantClue[debug3] = -1;
/* 100 */           this.levelClue[debug3] = -1;
/*     */         } 
/*     */       } else {
/* 103 */         this.access.execute((debug2, debug3) -> {
/*     */               int debug4 = 0;
/*     */               int debug5;
/*     */               for (debug5 = -1; debug5 <= 1; debug5++) {
/*     */                 for (int debug6 = -1; debug6 <= 1; debug6++) {
/*     */                   if (debug5 != 0 || debug6 != 0) {
/*     */                     if (debug2.isEmptyBlock(debug3.offset(debug6, 0, debug5)) && debug2.isEmptyBlock(debug3.offset(debug6, 1, debug5))) {
/*     */                       if (debug2.getBlockState(debug3.offset(debug6 * 2, 0, debug5 * 2)).is(Blocks.BOOKSHELF)) {
/*     */                         debug4++;
/*     */                       }
/*     */                       if (debug2.getBlockState(debug3.offset(debug6 * 2, 1, debug5 * 2)).is(Blocks.BOOKSHELF)) {
/*     */                         debug4++;
/*     */                       }
/*     */                       if (debug6 != 0 && debug5 != 0) {
/*     */                         if (debug2.getBlockState(debug3.offset(debug6 * 2, 0, debug5)).is(Blocks.BOOKSHELF)) {
/*     */                           debug4++;
/*     */                         }
/*     */                         if (debug2.getBlockState(debug3.offset(debug6 * 2, 1, debug5)).is(Blocks.BOOKSHELF)) {
/*     */                           debug4++;
/*     */                         }
/*     */                         if (debug2.getBlockState(debug3.offset(debug6, 0, debug5 * 2)).is(Blocks.BOOKSHELF)) {
/*     */                           debug4++;
/*     */                         }
/*     */                         if (debug2.getBlockState(debug3.offset(debug6, 1, debug5 * 2)).is(Blocks.BOOKSHELF)) {
/*     */                           debug4++;
/*     */                         }
/*     */                       } 
/*     */                     } 
/*     */                   }
/*     */                 } 
/*     */               } 
/*     */               this.random.setSeed(this.enchantmentSeed.get());
/*     */               for (debug5 = 0; debug5 < 3; debug5++) {
/*     */                 this.costs[debug5] = EnchantmentHelper.getEnchantmentCost(this.random, debug5, debug4, debug1);
/*     */                 this.enchantClue[debug5] = -1;
/*     */                 this.levelClue[debug5] = -1;
/*     */                 if (this.costs[debug5] < debug5 + 1) {
/*     */                   this.costs[debug5] = 0;
/*     */                 }
/*     */               } 
/*     */               for (debug5 = 0; debug5 < 3; debug5++) {
/*     */                 if (this.costs[debug5] > 0) {
/*     */                   List<EnchantmentInstance> debug6 = getEnchantmentList(debug1, debug5, this.costs[debug5]);
/*     */                   if (debug6 != null && !debug6.isEmpty()) {
/*     */                     EnchantmentInstance debug7 = debug6.get(this.random.nextInt(debug6.size()));
/*     */                     this.enchantClue[debug5] = Registry.ENCHANTMENT.getId(debug7.enchantment);
/*     */                     this.levelClue[debug5] = debug7.level;
/*     */                   } 
/*     */                 } 
/*     */               } 
/*     */               broadcastChanges();
/*     */             });
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean clickMenuButton(Player debug1, int debug2) {
/* 167 */     ItemStack debug3 = this.enchantSlots.getItem(0);
/* 168 */     ItemStack debug4 = this.enchantSlots.getItem(1);
/*     */     
/* 170 */     int debug5 = debug2 + 1;
/*     */     
/* 172 */     if ((debug4.isEmpty() || debug4.getCount() < debug5) && !debug1.abilities.instabuild) {
/* 173 */       return false;
/*     */     }
/* 175 */     if (this.costs[debug2] > 0 && !debug3.isEmpty() && ((debug1.experienceLevel >= debug5 && debug1.experienceLevel >= this.costs[debug2]) || debug1.abilities.instabuild)) {
/* 176 */       this.access.execute((debug6, debug7) -> {
/*     */             ItemStack debug8 = debug1;
/*     */             
/*     */             List<EnchantmentInstance> debug9 = getEnchantmentList(debug8, debug2, this.costs[debug2]);
/*     */             
/*     */             if (!debug9.isEmpty()) {
/*     */               debug3.onEnchantmentPerformed(debug8, debug4);
/*     */               
/*     */               boolean debug10 = (debug8.getItem() == Items.BOOK);
/*     */               
/*     */               if (debug10) {
/*     */                 debug8 = new ItemStack((ItemLike)Items.ENCHANTED_BOOK);
/*     */                 
/*     */                 CompoundTag compoundTag = debug1.getTag();
/*     */                 
/*     */                 if (compoundTag != null) {
/*     */                   debug8.setTag(compoundTag.copy());
/*     */                 }
/*     */                 
/*     */                 this.enchantSlots.setItem(0, debug8);
/*     */               } 
/*     */               for (int debug11 = 0; debug11 < debug9.size(); debug11++) {
/*     */                 EnchantmentInstance debug12 = debug9.get(debug11);
/*     */                 if (debug10) {
/*     */                   EnchantedBookItem.addEnchantment(debug8, debug12);
/*     */                 } else {
/*     */                   debug8.enchant(debug12.enchantment, debug12.level);
/*     */                 } 
/*     */               } 
/*     */               if (!debug3.abilities.instabuild) {
/*     */                 debug5.shrink(debug4);
/*     */                 if (debug5.isEmpty()) {
/*     */                   this.enchantSlots.setItem(1, ItemStack.EMPTY);
/*     */                 }
/*     */               } 
/*     */               debug3.awardStat(Stats.ENCHANT_ITEM);
/*     */               if (debug3 instanceof ServerPlayer) {
/*     */                 CriteriaTriggers.ENCHANTED_ITEM.trigger((ServerPlayer)debug3, debug8, debug4);
/*     */               }
/*     */               this.enchantSlots.setChanged();
/*     */               this.enchantmentSeed.set(debug3.getEnchantmentSeed());
/*     */               slotsChanged(this.enchantSlots);
/*     */               debug6.playSound(null, debug7, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, debug6.random.nextFloat() * 0.1F + 0.9F);
/*     */             } 
/*     */           });
/* 221 */       return true;
/*     */     } 
/* 223 */     return false;
/*     */   }
/*     */   
/*     */   private List<EnchantmentInstance> getEnchantmentList(ItemStack debug1, int debug2, int debug3) {
/* 227 */     this.random.setSeed((this.enchantmentSeed.get() + debug2));
/*     */     
/* 229 */     List<EnchantmentInstance> debug4 = EnchantmentHelper.selectEnchantment(this.random, debug1, debug3, false);
/*     */     
/* 231 */     if (debug1.getItem() == Items.BOOK && debug4.size() > 1)
/*     */     {
/* 233 */       debug4.remove(this.random.nextInt(debug4.size()));
/*     */     }
/* 235 */     return debug4;
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
/*     */   public void removed(Player debug1) {
/* 252 */     super.removed(debug1);
/* 253 */     this.access.execute((debug2, debug3) -> clearContainer(debug1, debug1.level, this.enchantSlots));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player debug1) {
/* 258 */     return stillValid(this.access, debug1, Blocks.ENCHANTING_TABLE);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack quickMoveStack(Player debug1, int debug2) {
/* 263 */     ItemStack debug3 = ItemStack.EMPTY;
/* 264 */     Slot debug4 = this.slots.get(debug2);
/* 265 */     if (debug4 != null && debug4.hasItem()) {
/* 266 */       ItemStack debug5 = debug4.getItem();
/* 267 */       debug3 = debug5.copy();
/*     */       
/* 269 */       if (debug2 == 0) {
/* 270 */         if (!moveItemStackTo(debug5, 2, 38, true)) {
/* 271 */           return ItemStack.EMPTY;
/*     */         }
/* 273 */       } else if (debug2 == 1) {
/* 274 */         if (!moveItemStackTo(debug5, 2, 38, true)) {
/* 275 */           return ItemStack.EMPTY;
/*     */         }
/* 277 */       } else if (debug5.getItem() == Items.LAPIS_LAZULI) {
/* 278 */         if (!moveItemStackTo(debug5, 1, 2, true)) {
/* 279 */           return ItemStack.EMPTY;
/*     */         }
/* 281 */       } else if (!((Slot)this.slots.get(0)).hasItem() && ((Slot)this.slots.get(0)).mayPlace(debug5)) {
/* 282 */         ItemStack debug6 = debug5.copy();
/* 283 */         debug6.setCount(1);
/* 284 */         debug5.shrink(1);
/* 285 */         ((Slot)this.slots.get(0)).set(debug6);
/*     */       } else {
/* 287 */         return ItemStack.EMPTY;
/*     */       } 
/* 289 */       if (debug5.isEmpty()) {
/* 290 */         debug4.set(ItemStack.EMPTY);
/*     */       } else {
/* 292 */         debug4.setChanged();
/*     */       } 
/* 294 */       if (debug5.getCount() == debug3.getCount()) {
/* 295 */         return ItemStack.EMPTY;
/*     */       }
/* 297 */       debug4.onTake(debug1, debug5);
/*     */     } 
/*     */     
/* 300 */     return debug3;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\inventory\EnchantmentMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */