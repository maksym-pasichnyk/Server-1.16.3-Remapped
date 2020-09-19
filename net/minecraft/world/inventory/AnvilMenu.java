/*     */ package net.minecraft.world.inventory;
/*     */ import java.util.Map;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TextComponent;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.EnchantedBookItem;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.AnvilBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class AnvilMenu extends ItemCombinerMenu {
/*  23 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */ 
/*     */   
/*     */   private int repairItemCountCost;
/*     */ 
/*     */   
/*     */   private String itemName;
/*     */   
/*  31 */   private final DataSlot cost = DataSlot.standalone();
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
/*     */ 
/*     */ 
/*     */   
/*     */   public AnvilMenu(int debug1, Inventory debug2) {
/*  54 */     this(debug1, debug2, ContainerLevelAccess.NULL);
/*     */   }
/*     */   
/*     */   public AnvilMenu(int debug1, Inventory debug2, ContainerLevelAccess debug3) {
/*  58 */     super(MenuType.ANVIL, debug1, debug2, debug3);
/*     */     
/*  60 */     addDataSlot(this.cost);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isValidBlock(BlockState debug1) {
/*  65 */     return debug1.is((Tag)BlockTags.ANVIL);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean mayPickup(Player debug1, boolean debug2) {
/*  70 */     return ((debug1.abilities.instabuild || debug1.experienceLevel >= this.cost.get()) && this.cost.get() > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack onTake(Player debug1, ItemStack debug2) {
/*  75 */     if (!debug1.abilities.instabuild) {
/*  76 */       debug1.giveExperienceLevels(-this.cost.get());
/*     */     }
/*  78 */     this.inputSlots.setItem(0, ItemStack.EMPTY);
/*     */     
/*  80 */     if (this.repairItemCountCost > 0) {
/*  81 */       ItemStack debug3 = this.inputSlots.getItem(1);
/*  82 */       if (!debug3.isEmpty() && debug3.getCount() > this.repairItemCountCost) {
/*  83 */         debug3.shrink(this.repairItemCountCost);
/*  84 */         this.inputSlots.setItem(1, debug3);
/*     */       } else {
/*  86 */         this.inputSlots.setItem(1, ItemStack.EMPTY);
/*     */       } 
/*     */     } else {
/*  89 */       this.inputSlots.setItem(1, ItemStack.EMPTY);
/*     */     } 
/*  91 */     this.cost.set(0);
/*     */     
/*  93 */     this.access.execute((debug1, debug2) -> {
/*     */           BlockState debug3 = debug1.getBlockState(debug2);
/*     */           if (!debug0.abilities.instabuild && debug3.is((Tag)BlockTags.ANVIL) && debug0.getRandom().nextFloat() < 0.12F) {
/*     */             BlockState debug4 = AnvilBlock.damage(debug3);
/*     */             if (debug4 == null) {
/*     */               debug1.removeBlock(debug2, false);
/*     */               debug1.levelEvent(1029, debug2, 0);
/*     */             } else {
/*     */               debug1.setBlock(debug2, debug4, 2);
/*     */               debug1.levelEvent(1030, debug2, 0);
/*     */             } 
/*     */           } else {
/*     */             debug1.levelEvent(1030, debug2, 0);
/*     */           } 
/*     */         });
/* 108 */     return debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public void createResult() {
/* 113 */     ItemStack debug1 = this.inputSlots.getItem(0);
/* 114 */     this.cost.set(1);
/* 115 */     int debug2 = 0;
/* 116 */     int debug3 = 0;
/* 117 */     int debug4 = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 123 */     if (debug1.isEmpty()) {
/* 124 */       this.resultSlots.setItem(0, ItemStack.EMPTY);
/* 125 */       this.cost.set(0);
/*     */       
/*     */       return;
/*     */     } 
/* 129 */     ItemStack debug5 = debug1.copy();
/* 130 */     ItemStack debug6 = this.inputSlots.getItem(1);
/* 131 */     Map<Enchantment, Integer> debug7 = EnchantmentHelper.getEnchantments(debug5);
/*     */     
/* 133 */     debug3 += debug1.getBaseRepairCost() + (debug6.isEmpty() ? 0 : debug6.getBaseRepairCost());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 138 */     this.repairItemCountCost = 0;
/*     */     
/* 140 */     if (!debug6.isEmpty()) {
/* 141 */       boolean debug8 = (debug6.getItem() == Items.ENCHANTED_BOOK && !EnchantedBookItem.getEnchantments(debug6).isEmpty());
/*     */       
/* 143 */       if (debug5.isDamageableItem() && debug5.getItem().isValidRepairItem(debug1, debug6)) {
/* 144 */         int debug9 = Math.min(debug5.getDamageValue(), debug5.getMaxDamage() / 4);
/* 145 */         if (debug9 <= 0) {
/* 146 */           this.resultSlots.setItem(0, ItemStack.EMPTY);
/* 147 */           this.cost.set(0);
/*     */           return;
/*     */         } 
/* 150 */         int debug10 = 0;
/* 151 */         while (debug9 > 0 && debug10 < debug6.getCount()) {
/* 152 */           int debug11 = debug5.getDamageValue() - debug9;
/* 153 */           debug5.setDamageValue(debug11);
/* 154 */           debug2++;
/*     */           
/* 156 */           debug9 = Math.min(debug5.getDamageValue(), debug5.getMaxDamage() / 4);
/* 157 */           debug10++;
/*     */         } 
/* 159 */         this.repairItemCountCost = debug10;
/*     */       } else {
/* 161 */         if (!debug8 && (debug5.getItem() != debug6.getItem() || !debug5.isDamageableItem())) {
/* 162 */           this.resultSlots.setItem(0, ItemStack.EMPTY);
/* 163 */           this.cost.set(0);
/*     */           return;
/*     */         } 
/* 166 */         if (debug5.isDamageableItem() && !debug8) {
/* 167 */           int i = debug1.getMaxDamage() - debug1.getDamageValue();
/* 168 */           int j = debug6.getMaxDamage() - debug6.getDamageValue();
/* 169 */           int k = j + debug5.getMaxDamage() * 12 / 100;
/* 170 */           int debug12 = i + k;
/* 171 */           int debug13 = debug5.getMaxDamage() - debug12;
/* 172 */           if (debug13 < 0) {
/* 173 */             debug13 = 0;
/*     */           }
/*     */           
/* 176 */           if (debug13 < debug5.getDamageValue()) {
/* 177 */             debug5.setDamageValue(debug13);
/* 178 */             debug2 += 2;
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 185 */         Map<Enchantment, Integer> debug9 = EnchantmentHelper.getEnchantments(debug6);
/* 186 */         boolean debug10 = false;
/* 187 */         boolean debug11 = false;
/*     */         
/* 189 */         for (Enchantment debug13 : debug9.keySet()) {
/* 190 */           if (debug13 == null) {
/*     */             continue;
/*     */           }
/* 193 */           int debug14 = ((Integer)debug7.getOrDefault(debug13, Integer.valueOf(0))).intValue();
/* 194 */           int debug15 = ((Integer)debug9.get(debug13)).intValue();
/* 195 */           debug15 = (debug14 == debug15) ? (debug15 + 1) : Math.max(debug15, debug14);
/*     */           
/* 197 */           boolean debug16 = debug13.canEnchant(debug1);
/* 198 */           if (this.player.abilities.instabuild || debug1.getItem() == Items.ENCHANTED_BOOK) {
/* 199 */             debug16 = true;
/*     */           }
/*     */           
/* 202 */           for (Enchantment debug18 : debug7.keySet()) {
/* 203 */             if (debug18 != debug13 && !debug13.isCompatibleWith(debug18)) {
/* 204 */               debug16 = false;
/* 205 */               debug2++;
/*     */             } 
/*     */           } 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 212 */           if (!debug16) {
/* 213 */             debug11 = true;
/*     */             continue;
/*     */           } 
/* 216 */           debug10 = true;
/* 217 */           if (debug15 > debug13.getMaxLevel()) {
/* 218 */             debug15 = debug13.getMaxLevel();
/*     */           }
/* 220 */           debug7.put(debug13, Integer.valueOf(debug15));
/* 221 */           int debug17 = 0;
/*     */           
/* 223 */           switch (debug13.getRarity()) {
/*     */             case COMMON:
/* 225 */               debug17 = 1;
/*     */               break;
/*     */             case UNCOMMON:
/* 228 */               debug17 = 2;
/*     */               break;
/*     */             case RARE:
/* 231 */               debug17 = 4;
/*     */               break;
/*     */             case VERY_RARE:
/* 234 */               debug17 = 8;
/*     */               break;
/*     */           } 
/*     */           
/* 238 */           if (debug8) {
/* 239 */             debug17 = Math.max(1, debug17 / 2);
/*     */           }
/*     */           
/* 242 */           debug2 += debug17 * debug15;
/*     */           
/* 244 */           if (debug1.getCount() > 1) {
/* 245 */             debug2 = 40;
/*     */           }
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 252 */         if (debug11 && !debug10) {
/*     */           
/* 254 */           this.resultSlots.setItem(0, ItemStack.EMPTY);
/* 255 */           this.cost.set(0);
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/* 261 */     if (StringUtils.isBlank(this.itemName)) {
/* 262 */       if (debug1.hasCustomHoverName()) {
/* 263 */         debug4 = 1;
/*     */         
/* 265 */         debug2 += debug4;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 270 */         debug5.resetHoverName();
/*     */       } 
/* 272 */     } else if (!this.itemName.equals(debug1.getHoverName().getString())) {
/* 273 */       debug4 = 1;
/*     */       
/* 275 */       debug2 += debug4;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 280 */       debug5.setHoverName((Component)new TextComponent(this.itemName));
/*     */     } 
/*     */     
/* 283 */     this.cost.set(debug3 + debug2);
/* 284 */     if (debug2 <= 0)
/*     */     {
/*     */ 
/*     */       
/* 288 */       debug5 = ItemStack.EMPTY;
/*     */     }
/* 290 */     if (debug4 == debug2 && debug4 > 0 && this.cost.get() >= 40)
/*     */     {
/*     */ 
/*     */       
/* 294 */       this.cost.set(39);
/*     */     }
/* 296 */     if (this.cost.get() >= 40 && !this.player.abilities.instabuild)
/*     */     {
/*     */ 
/*     */       
/* 300 */       debug5 = ItemStack.EMPTY;
/*     */     }
/*     */     
/* 303 */     if (!debug5.isEmpty()) {
/* 304 */       int debug8 = debug5.getBaseRepairCost();
/* 305 */       if (!debug6.isEmpty() && debug8 < debug6.getBaseRepairCost()) {
/* 306 */         debug8 = debug6.getBaseRepairCost();
/*     */       }
/*     */       
/* 309 */       if (debug4 != debug2 || debug4 == 0) {
/* 310 */         debug8 = calculateIncreasedRepairCost(debug8);
/*     */       }
/*     */       
/* 313 */       debug5.setRepairCost(debug8);
/* 314 */       EnchantmentHelper.setEnchantments(debug7, debug5);
/*     */     } 
/*     */     
/* 317 */     this.resultSlots.setItem(0, debug5);
/*     */     
/* 319 */     broadcastChanges();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int calculateIncreasedRepairCost(int debug0) {
/* 327 */     return debug0 * 2 + 1;
/*     */   }
/*     */   
/*     */   public void setItemName(String debug1) {
/* 331 */     this.itemName = debug1;
/*     */     
/* 333 */     if (getSlot(2).hasItem()) {
/* 334 */       ItemStack debug2 = getSlot(2).getItem();
/*     */       
/* 336 */       if (StringUtils.isBlank(debug1)) {
/* 337 */         debug2.resetHoverName();
/*     */       } else {
/* 339 */         debug2.setHoverName((Component)new TextComponent(this.itemName));
/*     */       } 
/*     */     } 
/*     */     
/* 343 */     createResult();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\inventory\AnvilMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */