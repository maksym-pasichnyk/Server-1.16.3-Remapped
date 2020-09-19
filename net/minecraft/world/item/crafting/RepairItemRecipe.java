/*     */ package net.minecraft.world.item.crafting;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.inventory.CraftingContainer;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class RepairItemRecipe extends CustomRecipe {
/*     */   public RepairItemRecipe(ResourceLocation debug1) {
/*  19 */     super(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean matches(CraftingContainer debug1, Level debug2) {
/*  24 */     List<ItemStack> debug3 = Lists.newArrayList();
/*     */     
/*  26 */     for (int debug4 = 0; debug4 < debug1.getContainerSize(); debug4++) {
/*  27 */       ItemStack debug5 = debug1.getItem(debug4);
/*     */       
/*  29 */       if (!debug5.isEmpty()) {
/*  30 */         debug3.add(debug5);
/*     */         
/*  32 */         if (debug3.size() > 1) {
/*  33 */           ItemStack debug6 = debug3.get(0);
/*  34 */           if (debug5.getItem() != debug6.getItem() || debug6.getCount() != 1 || debug5.getCount() != 1 || !debug6.getItem().canBeDepleted()) {
/*  35 */             return false;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  41 */     return (debug3.size() == 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack assemble(CraftingContainer debug1) {
/*  46 */     List<ItemStack> debug2 = Lists.newArrayList();
/*     */     
/*  48 */     for (int debug3 = 0; debug3 < debug1.getContainerSize(); debug3++) {
/*  49 */       ItemStack debug4 = debug1.getItem(debug3);
/*     */       
/*  51 */       if (!debug4.isEmpty()) {
/*  52 */         debug2.add(debug4);
/*     */         
/*  54 */         if (debug2.size() > 1) {
/*  55 */           ItemStack debug5 = debug2.get(0);
/*  56 */           if (debug4.getItem() != debug5.getItem() || debug5.getCount() != 1 || debug4.getCount() != 1 || !debug5.getItem().canBeDepleted()) {
/*  57 */             return ItemStack.EMPTY;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  63 */     if (debug2.size() == 2) {
/*  64 */       ItemStack itemStack1 = debug2.get(0);
/*  65 */       ItemStack debug4 = debug2.get(1);
/*     */       
/*  67 */       if (itemStack1.getItem() == debug4.getItem() && itemStack1.getCount() == 1 && debug4.getCount() == 1 && itemStack1.getItem().canBeDepleted()) {
/*  68 */         Item debug5 = itemStack1.getItem();
/*  69 */         int debug6 = debug5.getMaxDamage() - itemStack1.getDamageValue();
/*  70 */         int debug7 = debug5.getMaxDamage() - debug4.getDamageValue();
/*  71 */         int debug8 = debug6 + debug7 + debug5.getMaxDamage() * 5 / 100;
/*  72 */         int debug9 = debug5.getMaxDamage() - debug8;
/*  73 */         if (debug9 < 0) {
/*  74 */           debug9 = 0;
/*     */         }
/*     */         
/*  77 */         ItemStack debug10 = new ItemStack((ItemLike)itemStack1.getItem());
/*  78 */         debug10.setDamageValue(debug9);
/*     */         
/*  80 */         Map<Enchantment, Integer> debug11 = Maps.newHashMap();
/*  81 */         Map<Enchantment, Integer> debug12 = EnchantmentHelper.getEnchantments(itemStack1);
/*  82 */         Map<Enchantment, Integer> debug13 = EnchantmentHelper.getEnchantments(debug4);
/*  83 */         Registry.ENCHANTMENT.stream().filter(Enchantment::isCurse).forEach(debug3 -> {
/*     */               int debug4 = Math.max(((Integer)debug0.getOrDefault(debug3, Integer.valueOf(0))).intValue(), ((Integer)debug1.getOrDefault(debug3, Integer.valueOf(0))).intValue());
/*     */               
/*     */               if (debug4 > 0) {
/*     */                 debug2.put(debug3, Integer.valueOf(debug4));
/*     */               }
/*     */             });
/*  90 */         if (!debug11.isEmpty()) {
/*  91 */           EnchantmentHelper.setEnchantments(debug11, debug10);
/*     */         }
/*     */         
/*  94 */         return debug10;
/*     */       } 
/*     */     } 
/*     */     
/*  98 */     return ItemStack.EMPTY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RecipeSerializer<?> getSerializer() {
/* 108 */     return RecipeSerializer.REPAIR_ITEM;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\crafting\RepairItemRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */