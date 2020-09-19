/*     */ package net.minecraft.world.effect;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeInstance;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeMap;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ 
/*     */ 
/*     */ public class MobEffect
/*     */ {
/*     */   @Nullable
/*     */   public static MobEffect byId(int debug0) {
/*  25 */     return (MobEffect)Registry.MOB_EFFECT.byId(debug0);
/*     */   }
/*     */   
/*     */   public static int getId(MobEffect debug0) {
/*  29 */     return Registry.MOB_EFFECT.getId(debug0);
/*     */   }
/*     */   
/*  32 */   private final Map<Attribute, AttributeModifier> attributeModifiers = Maps.newHashMap();
/*     */   private final MobEffectCategory category;
/*     */   private final int color;
/*     */   @Nullable
/*     */   private String descriptionId;
/*     */   
/*     */   protected MobEffect(MobEffectCategory debug1, int debug2) {
/*  39 */     this.category = debug1;
/*  40 */     this.color = debug2;
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
/*     */   public void applyEffectTick(LivingEntity debug1, int debug2) {
/*  56 */     if (this == MobEffects.REGENERATION) {
/*  57 */       if (debug1.getHealth() < debug1.getMaxHealth()) {
/*  58 */         debug1.heal(1.0F);
/*     */       }
/*  60 */     } else if (this == MobEffects.POISON) {
/*  61 */       if (debug1.getHealth() > 1.0F) {
/*  62 */         debug1.hurt(DamageSource.MAGIC, 1.0F);
/*     */       }
/*  64 */     } else if (this == MobEffects.WITHER) {
/*  65 */       debug1.hurt(DamageSource.WITHER, 1.0F);
/*  66 */     } else if (this == MobEffects.HUNGER && debug1 instanceof Player) {
/*     */ 
/*     */       
/*  69 */       ((Player)debug1).causeFoodExhaustion(0.005F * (debug2 + 1));
/*  70 */     } else if (this == MobEffects.SATURATION && debug1 instanceof Player) {
/*  71 */       if (!debug1.level.isClientSide) {
/*  72 */         ((Player)debug1).getFoodData().eat(debug2 + 1, 1.0F);
/*     */       }
/*  74 */     } else if ((this == MobEffects.HEAL && !debug1.isInvertedHealAndHarm()) || (this == MobEffects.HARM && debug1.isInvertedHealAndHarm())) {
/*  75 */       debug1.heal(Math.max(4 << debug2, 0));
/*  76 */     } else if ((this == MobEffects.HARM && !debug1.isInvertedHealAndHarm()) || (this == MobEffects.HEAL && debug1.isInvertedHealAndHarm())) {
/*  77 */       debug1.hurt(DamageSource.MAGIC, (6 << debug2));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void applyInstantenousEffect(@Nullable Entity debug1, @Nullable Entity debug2, LivingEntity debug3, int debug4, double debug5) {
/*  82 */     if ((this == MobEffects.HEAL && !debug3.isInvertedHealAndHarm()) || (this == MobEffects.HARM && debug3.isInvertedHealAndHarm())) {
/*  83 */       int debug7 = (int)(debug5 * (4 << debug4) + 0.5D);
/*  84 */       debug3.heal(debug7);
/*  85 */     } else if ((this == MobEffects.HARM && !debug3.isInvertedHealAndHarm()) || (this == MobEffects.HEAL && debug3.isInvertedHealAndHarm())) {
/*  86 */       int debug7 = (int)(debug5 * (6 << debug4) + 0.5D);
/*  87 */       if (debug1 == null) {
/*  88 */         debug3.hurt(DamageSource.MAGIC, debug7);
/*     */       } else {
/*  90 */         debug3.hurt(DamageSource.indirectMagic(debug1, debug2), debug7);
/*     */       } 
/*     */     } else {
/*  93 */       applyEffectTick(debug3, debug4);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDurationEffectTick(int debug1, int debug2) {
/* 108 */     if (this == MobEffects.REGENERATION) {
/* 109 */       int debug3 = 50 >> debug2;
/* 110 */       if (debug3 > 0) {
/* 111 */         return (debug1 % debug3 == 0);
/*     */       }
/* 113 */       return true;
/* 114 */     }  if (this == MobEffects.POISON) {
/* 115 */       int debug3 = 25 >> debug2;
/* 116 */       if (debug3 > 0) {
/* 117 */         return (debug1 % debug3 == 0);
/*     */       }
/* 119 */       return true;
/* 120 */     }  if (this == MobEffects.WITHER) {
/* 121 */       int debug3 = 40 >> debug2;
/* 122 */       if (debug3 > 0) {
/* 123 */         return (debug1 % debug3 == 0);
/*     */       }
/* 125 */       return true;
/* 126 */     }  if (this == MobEffects.HUNGER) {
/* 127 */       return true;
/*     */     }
/*     */     
/* 130 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInstantenous() {
/* 135 */     return false;
/*     */   }
/*     */   
/*     */   protected String getOrCreateDescriptionId() {
/* 139 */     if (this.descriptionId == null) {
/* 140 */       this.descriptionId = Util.makeDescriptionId("effect", Registry.MOB_EFFECT.getKey(this));
/*     */     }
/* 142 */     return this.descriptionId;
/*     */   }
/*     */   
/*     */   public String getDescriptionId() {
/* 146 */     return getOrCreateDescriptionId();
/*     */   }
/*     */   
/*     */   public Component getDisplayName() {
/* 150 */     return (Component)new TranslatableComponent(getDescriptionId());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getColor() {
/* 158 */     return this.color;
/*     */   }
/*     */   
/*     */   public MobEffect addAttributeModifier(Attribute debug1, String debug2, double debug3, AttributeModifier.Operation debug5) {
/* 162 */     AttributeModifier debug6 = new AttributeModifier(UUID.fromString(debug2), this::getDescriptionId, debug3, debug5);
/* 163 */     this.attributeModifiers.put(debug1, debug6);
/* 164 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeAttributeModifiers(LivingEntity debug1, AttributeMap debug2, int debug3) {
/* 172 */     for (Map.Entry<Attribute, AttributeModifier> debug5 : this.attributeModifiers.entrySet()) {
/* 173 */       AttributeInstance debug6 = debug2.getInstance(debug5.getKey());
/*     */       
/* 175 */       if (debug6 != null) {
/* 176 */         debug6.removeModifier(debug5.getValue());
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addAttributeModifiers(LivingEntity debug1, AttributeMap debug2, int debug3) {
/* 182 */     for (Map.Entry<Attribute, AttributeModifier> debug5 : this.attributeModifiers.entrySet()) {
/* 183 */       AttributeInstance debug6 = debug2.getInstance(debug5.getKey());
/*     */       
/* 185 */       if (debug6 != null) {
/* 186 */         AttributeModifier debug7 = debug5.getValue();
/* 187 */         debug6.removeModifier(debug7);
/* 188 */         debug6.addPermanentModifier(new AttributeModifier(debug7.getId(), getDescriptionId() + " " + debug3, getAttributeModifierValue(debug3, debug7), debug7.getOperation()));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public double getAttributeModifierValue(int debug1, AttributeModifier debug2) {
/* 194 */     return debug2.getAmount() * (debug1 + 1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\effect\MobEffect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */