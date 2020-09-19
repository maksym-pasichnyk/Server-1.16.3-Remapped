/*     */ package net.minecraft.world.item.alchemy;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.item.ItemStack;
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
/*     */ public class PotionUtils
/*     */ {
/*  33 */   private static final MutableComponent NO_EFFECT = (new TranslatableComponent("effect.none")).withStyle(ChatFormatting.GRAY);
/*     */   
/*     */   public static List<MobEffectInstance> getMobEffects(ItemStack debug0) {
/*  36 */     return getAllEffects(debug0.getTag());
/*     */   }
/*     */   
/*     */   public static List<MobEffectInstance> getAllEffects(Potion debug0, Collection<MobEffectInstance> debug1) {
/*  40 */     List<MobEffectInstance> debug2 = Lists.newArrayList();
/*     */     
/*  42 */     debug2.addAll(debug0.getEffects());
/*  43 */     debug2.addAll(debug1);
/*     */     
/*  45 */     return debug2;
/*     */   }
/*     */   
/*     */   public static List<MobEffectInstance> getAllEffects(@Nullable CompoundTag debug0) {
/*  49 */     List<MobEffectInstance> debug1 = Lists.newArrayList();
/*     */     
/*  51 */     debug1.addAll(getPotion(debug0).getEffects());
/*  52 */     getCustomEffects(debug0, debug1);
/*     */     
/*  54 */     return debug1;
/*     */   }
/*     */   
/*     */   public static List<MobEffectInstance> getCustomEffects(ItemStack debug0) {
/*  58 */     return getCustomEffects(debug0.getTag());
/*     */   }
/*     */   
/*     */   public static List<MobEffectInstance> getCustomEffects(@Nullable CompoundTag debug0) {
/*  62 */     List<MobEffectInstance> debug1 = Lists.newArrayList();
/*  63 */     getCustomEffects(debug0, debug1);
/*  64 */     return debug1;
/*     */   }
/*     */   
/*     */   public static void getCustomEffects(@Nullable CompoundTag debug0, List<MobEffectInstance> debug1) {
/*  68 */     if (debug0 != null && debug0.contains("CustomPotionEffects", 9)) {
/*  69 */       ListTag debug2 = debug0.getList("CustomPotionEffects", 10);
/*     */       
/*  71 */       for (int debug3 = 0; debug3 < debug2.size(); debug3++) {
/*  72 */         CompoundTag debug4 = debug2.getCompound(debug3);
/*  73 */         MobEffectInstance debug5 = MobEffectInstance.load(debug4);
/*  74 */         if (debug5 != null) {
/*  75 */           debug1.add(debug5);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static int getColor(ItemStack debug0) {
/*  82 */     CompoundTag debug1 = debug0.getTag();
/*  83 */     if (debug1 != null && 
/*  84 */       debug1.contains("CustomPotionColor", 99)) {
/*  85 */       return debug1.getInt("CustomPotionColor");
/*     */     }
/*     */     
/*  88 */     return (getPotion(debug0) == Potions.EMPTY) ? 16253176 : getColor(getMobEffects(debug0));
/*     */   }
/*     */   
/*     */   public static int getColor(Potion debug0) {
/*  92 */     return (debug0 == Potions.EMPTY) ? 16253176 : getColor(debug0.getEffects());
/*     */   }
/*     */   
/*     */   public static int getColor(Collection<MobEffectInstance> debug0) {
/*  96 */     int debug1 = 3694022;
/*  97 */     if (debug0.isEmpty()) {
/*  98 */       return 3694022;
/*     */     }
/*     */     
/* 101 */     float debug2 = 0.0F;
/* 102 */     float debug3 = 0.0F;
/* 103 */     float debug4 = 0.0F;
/* 104 */     int debug5 = 0;
/*     */     
/* 106 */     for (MobEffectInstance debug7 : debug0) {
/* 107 */       if (!debug7.isVisible()) {
/*     */         continue;
/*     */       }
/*     */       
/* 111 */       int debug8 = debug7.getEffect().getColor();
/* 112 */       int debug9 = debug7.getAmplifier() + 1;
/* 113 */       debug2 += (debug9 * (debug8 >> 16 & 0xFF)) / 255.0F;
/* 114 */       debug3 += (debug9 * (debug8 >> 8 & 0xFF)) / 255.0F;
/* 115 */       debug4 += (debug9 * (debug8 >> 0 & 0xFF)) / 255.0F;
/* 116 */       debug5 += debug9;
/*     */     } 
/*     */     
/* 119 */     if (debug5 == 0) {
/* 120 */       return 0;
/*     */     }
/*     */     
/* 123 */     debug2 = debug2 / debug5 * 255.0F;
/* 124 */     debug3 = debug3 / debug5 * 255.0F;
/* 125 */     debug4 = debug4 / debug5 * 255.0F;
/*     */     
/* 127 */     return (int)debug2 << 16 | (int)debug3 << 8 | (int)debug4;
/*     */   }
/*     */   
/*     */   public static Potion getPotion(ItemStack debug0) {
/* 131 */     return getPotion(debug0.getTag());
/*     */   }
/*     */   
/*     */   public static Potion getPotion(@Nullable CompoundTag debug0) {
/* 135 */     if (debug0 == null) {
/* 136 */       return Potions.EMPTY;
/*     */     }
/*     */     
/* 139 */     return Potion.byName(debug0.getString("Potion"));
/*     */   }
/*     */   
/*     */   public static ItemStack setPotion(ItemStack debug0, Potion debug1) {
/* 143 */     ResourceLocation debug2 = Registry.POTION.getKey(debug1);
/*     */     
/* 145 */     if (debug1 == Potions.EMPTY) {
/* 146 */       debug0.removeTagKey("Potion");
/*     */     } else {
/* 148 */       debug0.getOrCreateTag().putString("Potion", debug2.toString());
/*     */     } 
/*     */     
/* 151 */     return debug0;
/*     */   }
/*     */   
/*     */   public static ItemStack setCustomEffects(ItemStack debug0, Collection<MobEffectInstance> debug1) {
/* 155 */     if (debug1.isEmpty()) {
/* 156 */       return debug0;
/*     */     }
/*     */     
/* 159 */     CompoundTag debug2 = debug0.getOrCreateTag();
/* 160 */     ListTag debug3 = debug2.getList("CustomPotionEffects", 9);
/*     */     
/* 162 */     for (MobEffectInstance debug5 : debug1) {
/* 163 */       debug3.add(debug5.save(new CompoundTag()));
/*     */     }
/* 165 */     debug2.put("CustomPotionEffects", (Tag)debug3);
/*     */     
/* 167 */     return debug0;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\alchemy\PotionUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */