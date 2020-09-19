/*     */ package net.minecraft.world.item.enchantment;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.MobType;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ 
/*     */ public abstract class Enchantment
/*     */ {
/*     */   private final EquipmentSlot[] slots;
/*     */   private final Rarity rarity;
/*     */   public final EnchantmentCategory category;
/*     */   @Nullable
/*     */   protected String descriptionId;
/*     */   
/*     */   public enum Rarity
/*     */   {
/*  29 */     COMMON(10),
/*  30 */     UNCOMMON(5),
/*  31 */     RARE(2),
/*  32 */     VERY_RARE(1);
/*     */     
/*     */     private final int weight;
/*     */     
/*     */     Rarity(int debug3) {
/*  37 */       this.weight = debug3;
/*     */     }
/*     */     
/*     */     public int getWeight() {
/*  41 */       return this.weight;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Enchantment(Rarity debug1, EnchantmentCategory debug2, EquipmentSlot[] debug3) {
/*  52 */     this.rarity = debug1;
/*  53 */     this.category = debug2;
/*  54 */     this.slots = debug3;
/*     */   }
/*     */   
/*     */   public Map<EquipmentSlot, ItemStack> getSlotItems(LivingEntity debug1) {
/*  58 */     Map<EquipmentSlot, ItemStack> debug2 = Maps.newEnumMap(EquipmentSlot.class);
/*  59 */     for (EquipmentSlot debug6 : this.slots) {
/*  60 */       ItemStack debug7 = debug1.getItemBySlot(debug6);
/*  61 */       if (!debug7.isEmpty()) {
/*  62 */         debug2.put(debug6, debug7);
/*     */       }
/*     */     } 
/*  65 */     return debug2;
/*     */   }
/*     */   
/*     */   public Rarity getRarity() {
/*  69 */     return this.rarity;
/*     */   }
/*     */   
/*     */   public int getMinLevel() {
/*  73 */     return 1;
/*     */   }
/*     */   
/*     */   public int getMaxLevel() {
/*  77 */     return 1;
/*     */   }
/*     */   
/*     */   public int getMinCost(int debug1) {
/*  81 */     return 1 + debug1 * 10;
/*     */   }
/*     */   
/*     */   public int getMaxCost(int debug1) {
/*  85 */     return getMinCost(debug1) + 5;
/*     */   }
/*     */   
/*     */   public int getDamageProtection(int debug1, DamageSource debug2) {
/*  89 */     return 0;
/*     */   }
/*     */   
/*     */   public float getDamageBonus(int debug1, MobType debug2) {
/*  93 */     return 0.0F;
/*     */   }
/*     */   
/*     */   public final boolean isCompatibleWith(Enchantment debug1) {
/*  97 */     return (checkCompatibility(debug1) && debug1.checkCompatibility(this));
/*     */   }
/*     */   
/*     */   protected boolean checkCompatibility(Enchantment debug1) {
/* 101 */     return (this != debug1);
/*     */   }
/*     */   
/*     */   protected String getOrCreateDescriptionId() {
/* 105 */     if (this.descriptionId == null) {
/* 106 */       this.descriptionId = Util.makeDescriptionId("enchantment", Registry.ENCHANTMENT.getKey(this));
/*     */     }
/* 108 */     return this.descriptionId;
/*     */   }
/*     */   
/*     */   public String getDescriptionId() {
/* 112 */     return getOrCreateDescriptionId();
/*     */   }
/*     */   
/*     */   public Component getFullname(int debug1) {
/* 116 */     TranslatableComponent translatableComponent = new TranslatableComponent(getDescriptionId());
/* 117 */     if (isCurse()) {
/* 118 */       translatableComponent.withStyle(ChatFormatting.RED);
/*     */     } else {
/* 120 */       translatableComponent.withStyle(ChatFormatting.GRAY);
/*     */     } 
/* 122 */     if (debug1 != 1 || getMaxLevel() != 1) {
/* 123 */       translatableComponent.append(" ").append((Component)new TranslatableComponent("enchantment.level." + debug1));
/*     */     }
/* 125 */     return (Component)translatableComponent;
/*     */   }
/*     */   
/*     */   public boolean canEnchant(ItemStack debug1) {
/* 129 */     return this.category.canEnchant(debug1.getItem());
/*     */   }
/*     */ 
/*     */   
/*     */   public void doPostAttack(LivingEntity debug1, Entity debug2, int debug3) {}
/*     */ 
/*     */   
/*     */   public void doPostHurt(LivingEntity debug1, Entity debug2, int debug3) {}
/*     */   
/*     */   public boolean isTreasureOnly() {
/* 139 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isCurse() {
/* 143 */     return false;
/*     */   }
/*     */   public boolean isTradeable() {
/* 146 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDiscoverable() {
/* 154 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\enchantment\Enchantment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */