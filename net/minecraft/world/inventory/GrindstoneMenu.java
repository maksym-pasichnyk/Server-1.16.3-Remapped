/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.ExperienceOrb;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GrindstoneMenu
/*     */   extends AbstractContainerMenu
/*     */ {
/*  31 */   private final Container resultSlots = new ResultContainer();
/*  32 */   private final Container repairSlots = (Container)new SimpleContainer(2)
/*     */     {
/*     */       public void setChanged() {
/*  35 */         super.setChanged();
/*  36 */         GrindstoneMenu.this.slotsChanged((Container)this);
/*     */       }
/*     */     };
/*     */   
/*     */   private final ContainerLevelAccess access;
/*     */   
/*     */   public GrindstoneMenu(int debug1, Inventory debug2) {
/*  43 */     this(debug1, debug2, ContainerLevelAccess.NULL);
/*     */   }
/*     */   
/*     */   public GrindstoneMenu(int debug1, Inventory debug2, final ContainerLevelAccess access) {
/*  47 */     super(MenuType.GRINDSTONE, debug1);
/*  48 */     this.access = access;
/*     */     
/*  50 */     addSlot(new Slot(this.repairSlots, 0, 49, 19)
/*     */         {
/*     */           public boolean mayPlace(ItemStack debug1) {
/*  53 */             return (debug1.isDamageableItem() || debug1.getItem() == Items.ENCHANTED_BOOK || debug1.isEnchanted());
/*     */           }
/*     */         });
/*  56 */     addSlot(new Slot(this.repairSlots, 1, 49, 40)
/*     */         {
/*     */           public boolean mayPlace(ItemStack debug1) {
/*  59 */             return (debug1.isDamageableItem() || debug1.getItem() == Items.ENCHANTED_BOOK || debug1.isEnchanted());
/*     */           }
/*     */         });
/*  62 */     addSlot(new Slot(this.resultSlots, 2, 129, 34)
/*     */         {
/*     */           public boolean mayPlace(ItemStack debug1) {
/*  65 */             return false;
/*     */           }
/*     */ 
/*     */           
/*     */           public ItemStack onTake(Player debug1, ItemStack debug2) {
/*  70 */             access.execute((debug1, debug2) -> {
/*     */                   int debug3 = getExperienceAmount(debug1);
/*     */                   
/*     */                   while (debug3 > 0) {
/*     */                     int debug4 = ExperienceOrb.getExperienceValue(debug3);
/*     */                     debug3 -= debug4;
/*     */                     debug1.addFreshEntity((Entity)new ExperienceOrb(debug1, debug2.getX(), debug2.getY() + 0.5D, debug2.getZ() + 0.5D, debug4));
/*     */                   } 
/*     */                   debug1.levelEvent(1042, debug2, 0);
/*     */                 });
/*  80 */             GrindstoneMenu.this.repairSlots.setItem(0, ItemStack.EMPTY);
/*  81 */             GrindstoneMenu.this.repairSlots.setItem(1, ItemStack.EMPTY);
/*     */             
/*  83 */             return debug2;
/*     */           }
/*     */           
/*     */           private int getExperienceAmount(Level debug1) {
/*  87 */             int debug2 = 0;
/*  88 */             debug2 += getExperienceFromItem(GrindstoneMenu.this.repairSlots.getItem(0));
/*  89 */             debug2 += getExperienceFromItem(GrindstoneMenu.this.repairSlots.getItem(1));
/*     */             
/*  91 */             if (debug2 > 0) {
/*  92 */               int debug3 = (int)Math.ceil(debug2 / 2.0D);
/*  93 */               return debug3 + debug1.random.nextInt(debug3);
/*     */             } 
/*     */             
/*  96 */             return 0;
/*     */           }
/*     */           
/*     */           private int getExperienceFromItem(ItemStack debug1) {
/* 100 */             int debug2 = 0;
/* 101 */             Map<Enchantment, Integer> debug3 = EnchantmentHelper.getEnchantments(debug1);
/* 102 */             for (Map.Entry<Enchantment, Integer> debug5 : debug3.entrySet()) {
/* 103 */               Enchantment debug6 = debug5.getKey();
/* 104 */               Integer debug7 = debug5.getValue();
/*     */               
/* 106 */               if (!debug6.isCurse()) {
/* 107 */                 debug2 += debug6.getMinCost(debug7.intValue());
/*     */               }
/*     */             } 
/*     */             
/* 111 */             return debug2;
/*     */           }
/*     */         });
/*     */     int debug4;
/* 115 */     for (debug4 = 0; debug4 < 3; debug4++) {
/* 116 */       for (int debug5 = 0; debug5 < 9; debug5++) {
/* 117 */         addSlot(new Slot((Container)debug2, debug5 + debug4 * 9 + 9, 8 + debug5 * 18, 84 + debug4 * 18));
/*     */       }
/*     */     } 
/* 120 */     for (debug4 = 0; debug4 < 9; debug4++) {
/* 121 */       addSlot(new Slot((Container)debug2, debug4, 8 + debug4 * 18, 142));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void slotsChanged(Container debug1) {
/* 127 */     super.slotsChanged(debug1);
/*     */     
/* 129 */     if (debug1 == this.repairSlots) {
/* 130 */       createResult();
/*     */     }
/*     */   }
/*     */   
/*     */   private void createResult() {
/* 135 */     ItemStack debug1 = this.repairSlots.getItem(0);
/* 136 */     ItemStack debug2 = this.repairSlots.getItem(1);
/*     */     
/* 138 */     boolean debug3 = (!debug1.isEmpty() || !debug2.isEmpty());
/* 139 */     boolean debug4 = (!debug1.isEmpty() && !debug2.isEmpty());
/*     */     
/* 141 */     if (debug3) {
/* 142 */       int debug6; ItemStack debug8; boolean debug5 = ((!debug1.isEmpty() && debug1.getItem() != Items.ENCHANTED_BOOK && !debug1.isEnchanted()) || (!debug2.isEmpty() && debug2.getItem() != Items.ENCHANTED_BOOK && !debug2.isEnchanted()));
/* 143 */       if (debug1.getCount() > 1 || debug2.getCount() > 1 || (!debug4 && debug5)) {
/* 144 */         this.resultSlots.setItem(0, ItemStack.EMPTY);
/* 145 */         broadcastChanges();
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 150 */       int debug7 = 1;
/*     */       
/* 152 */       if (debug4) {
/* 153 */         if (debug1.getItem() == debug2.getItem()) {
/* 154 */           Item debug9 = debug1.getItem();
/* 155 */           int debug10 = debug9.getMaxDamage() - debug1.getDamageValue();
/* 156 */           int debug11 = debug9.getMaxDamage() - debug2.getDamageValue();
/* 157 */           int debug12 = debug10 + debug11 + debug9.getMaxDamage() * 5 / 100;
/* 158 */           debug6 = Math.max(debug9.getMaxDamage() - debug12, 0);
/*     */           
/* 160 */           debug8 = mergeEnchants(debug1, debug2);
/*     */           
/* 162 */           if (!debug8.isDamageableItem()) {
/* 163 */             if (!ItemStack.matches(debug1, debug2)) {
/* 164 */               this.resultSlots.setItem(0, ItemStack.EMPTY);
/* 165 */               broadcastChanges();
/*     */               
/*     */               return;
/*     */             } 
/* 169 */             debug7 = 2;
/*     */           } 
/*     */         } else {
/* 172 */           this.resultSlots.setItem(0, ItemStack.EMPTY);
/* 173 */           broadcastChanges();
/*     */           return;
/*     */         } 
/*     */       } else {
/* 177 */         boolean debug9 = !debug1.isEmpty();
/* 178 */         debug6 = debug9 ? debug1.getDamageValue() : debug2.getDamageValue();
/* 179 */         debug8 = debug9 ? debug1 : debug2;
/*     */       } 
/*     */       
/* 182 */       this.resultSlots.setItem(0, removeNonCurses(debug8, debug6, debug7));
/*     */     } else {
/* 184 */       this.resultSlots.setItem(0, ItemStack.EMPTY);
/*     */     } 
/*     */     
/* 187 */     broadcastChanges();
/*     */   }
/*     */   
/*     */   private ItemStack mergeEnchants(ItemStack debug1, ItemStack debug2) {
/* 191 */     ItemStack debug3 = debug1.copy();
/*     */     
/* 193 */     Map<Enchantment, Integer> debug4 = EnchantmentHelper.getEnchantments(debug2);
/* 194 */     for (Map.Entry<Enchantment, Integer> debug6 : debug4.entrySet()) {
/* 195 */       Enchantment debug7 = debug6.getKey();
/* 196 */       if (!debug7.isCurse() || EnchantmentHelper.getItemEnchantmentLevel(debug7, debug3) == 0) {
/* 197 */         debug3.enchant(debug7, ((Integer)debug6.getValue()).intValue());
/*     */       }
/*     */     } 
/*     */     
/* 201 */     return debug3;
/*     */   }
/*     */   
/*     */   private ItemStack removeNonCurses(ItemStack debug1, int debug2, int debug3) {
/* 205 */     ItemStack debug4 = debug1.copy();
/* 206 */     debug4.removeTagKey("Enchantments");
/* 207 */     debug4.removeTagKey("StoredEnchantments");
/*     */     
/* 209 */     if (debug2 > 0) {
/* 210 */       debug4.setDamageValue(debug2);
/*     */     } else {
/* 212 */       debug4.removeTagKey("Damage");
/*     */     } 
/*     */     
/* 215 */     debug4.setCount(debug3);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 222 */     Map<Enchantment, Integer> debug5 = (Map<Enchantment, Integer>)EnchantmentHelper.getEnchantments(debug1).entrySet().stream().filter(debug0 -> ((Enchantment)debug0.getKey()).isCurse()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
/*     */     
/* 224 */     EnchantmentHelper.setEnchantments(debug5, debug4);
/* 225 */     debug4.setRepairCost(0);
/*     */     
/* 227 */     if (debug4.getItem() == Items.ENCHANTED_BOOK && debug5.size() == 0) {
/* 228 */       debug4 = new ItemStack((ItemLike)Items.BOOK);
/* 229 */       if (debug1.hasCustomHoverName()) {
/* 230 */         debug4.setHoverName(debug1.getHoverName());
/*     */       }
/*     */     } 
/*     */     
/* 234 */     for (int debug6 = 0; debug6 < debug5.size(); debug6++) {
/* 235 */       debug4.setRepairCost(AnvilMenu.calculateIncreasedRepairCost(debug4.getBaseRepairCost()));
/*     */     }
/*     */     
/* 238 */     return debug4;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removed(Player debug1) {
/* 243 */     super.removed(debug1);
/* 244 */     this.access.execute((debug2, debug3) -> clearContainer(debug1, debug2, this.repairSlots));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stillValid(Player debug1) {
/* 249 */     return stillValid(this.access, debug1, Blocks.GRINDSTONE);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack quickMoveStack(Player debug1, int debug2) {
/* 254 */     ItemStack debug3 = ItemStack.EMPTY;
/* 255 */     Slot debug4 = this.slots.get(debug2);
/* 256 */     if (debug4 != null && debug4.hasItem()) {
/* 257 */       ItemStack debug5 = debug4.getItem();
/* 258 */       debug3 = debug5.copy();
/*     */       
/* 260 */       ItemStack debug6 = this.repairSlots.getItem(0);
/* 261 */       ItemStack debug7 = this.repairSlots.getItem(1);
/*     */       
/* 263 */       if (debug2 == 2) {
/* 264 */         if (!moveItemStackTo(debug5, 3, 39, true)) {
/* 265 */           return ItemStack.EMPTY;
/*     */         }
/* 267 */         debug4.onQuickCraft(debug5, debug3);
/* 268 */       } else if (debug2 == 0 || debug2 == 1) {
/* 269 */         if (!moveItemStackTo(debug5, 3, 39, false)) {
/* 270 */           return ItemStack.EMPTY;
/*     */         }
/* 272 */       } else if (debug6.isEmpty() || debug7.isEmpty()) {
/* 273 */         if (!moveItemStackTo(debug5, 0, 2, false)) {
/* 274 */           return ItemStack.EMPTY;
/*     */         }
/* 276 */       } else if (debug2 >= 3 && debug2 < 30) {
/* 277 */         if (!moveItemStackTo(debug5, 30, 39, false)) {
/* 278 */           return ItemStack.EMPTY;
/*     */         }
/* 280 */       } else if (debug2 >= 30 && debug2 < 39 && 
/* 281 */         !moveItemStackTo(debug5, 3, 30, false)) {
/* 282 */         return ItemStack.EMPTY;
/*     */       } 
/*     */ 
/*     */       
/* 286 */       if (debug5.isEmpty()) {
/* 287 */         debug4.set(ItemStack.EMPTY);
/*     */       } else {
/* 289 */         debug4.setChanged();
/*     */       } 
/*     */       
/* 292 */       if (debug5.getCount() == debug3.getCount()) {
/* 293 */         return ItemStack.EMPTY;
/*     */       }
/* 295 */       debug4.onTake(debug1, debug5);
/*     */     } 
/*     */ 
/*     */     
/* 299 */     return debug3;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\inventory\GrindstoneMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */