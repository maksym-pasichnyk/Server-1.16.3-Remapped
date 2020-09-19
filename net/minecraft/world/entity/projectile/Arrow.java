/*     */ package net.minecraft.world.entity.projectile;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Collection;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.alchemy.Potion;
/*     */ import net.minecraft.world.item.alchemy.PotionUtils;
/*     */ import net.minecraft.world.item.alchemy.Potions;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ 
/*     */ public class Arrow
/*     */   extends AbstractArrow
/*     */ {
/*  30 */   private static final EntityDataAccessor<Integer> ID_EFFECT_COLOR = SynchedEntityData.defineId(Arrow.class, EntityDataSerializers.INT);
/*     */ 
/*     */ 
/*     */   
/*  34 */   private Potion potion = Potions.EMPTY;
/*  35 */   private final Set<MobEffectInstance> effects = Sets.newHashSet();
/*     */   private boolean fixedColor;
/*     */   
/*     */   public Arrow(EntityType<? extends Arrow> debug1, Level debug2) {
/*  39 */     super((EntityType)debug1, debug2);
/*     */   }
/*     */   
/*     */   public Arrow(Level debug1, double debug2, double debug4, double debug6) {
/*  43 */     super(EntityType.ARROW, debug2, debug4, debug6, debug1);
/*     */   }
/*     */   
/*     */   public Arrow(Level debug1, LivingEntity debug2) {
/*  47 */     super(EntityType.ARROW, debug2, debug1);
/*     */   }
/*     */   
/*     */   public void setEffectsFromItem(ItemStack debug1) {
/*  51 */     if (debug1.getItem() == Items.TIPPED_ARROW) {
/*  52 */       this.potion = PotionUtils.getPotion(debug1);
/*  53 */       Collection<MobEffectInstance> debug2 = PotionUtils.getCustomEffects(debug1);
/*  54 */       if (!debug2.isEmpty()) {
/*  55 */         for (MobEffectInstance debug4 : debug2) {
/*  56 */           this.effects.add(new MobEffectInstance(debug4));
/*     */         }
/*     */       }
/*     */       
/*  60 */       int debug3 = getCustomColor(debug1);
/*  61 */       if (debug3 == -1) {
/*  62 */         updateColor();
/*     */       } else {
/*  64 */         setFixedColor(debug3);
/*     */       } 
/*  66 */     } else if (debug1.getItem() == Items.ARROW) {
/*  67 */       this.potion = Potions.EMPTY;
/*  68 */       this.effects.clear();
/*  69 */       this.entityData.set(ID_EFFECT_COLOR, Integer.valueOf(-1));
/*     */     } 
/*     */   }
/*     */   
/*     */   public static int getCustomColor(ItemStack debug0) {
/*  74 */     CompoundTag debug1 = debug0.getTag();
/*  75 */     if (debug1 != null && debug1.contains("CustomPotionColor", 99)) {
/*  76 */       return debug1.getInt("CustomPotionColor");
/*     */     }
/*  78 */     return -1;
/*     */   }
/*     */   
/*     */   private void updateColor() {
/*  82 */     this.fixedColor = false;
/*  83 */     if (this.potion == Potions.EMPTY && this.effects.isEmpty()) {
/*  84 */       this.entityData.set(ID_EFFECT_COLOR, Integer.valueOf(-1));
/*     */     } else {
/*  86 */       this.entityData.set(ID_EFFECT_COLOR, Integer.valueOf(PotionUtils.getColor(PotionUtils.getAllEffects(this.potion, this.effects))));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addEffect(MobEffectInstance debug1) {
/*  91 */     this.effects.add(debug1);
/*  92 */     getEntityData().set(ID_EFFECT_COLOR, Integer.valueOf(PotionUtils.getColor(PotionUtils.getAllEffects(this.potion, this.effects))));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/*  97 */     super.defineSynchedData();
/*  98 */     this.entityData.define(ID_EFFECT_COLOR, Integer.valueOf(-1));
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 103 */     super.tick();
/*     */     
/* 105 */     if (this.level.isClientSide) {
/* 106 */       if (this.inGround) {
/* 107 */         if (this.inGroundTime % 5 == 0) {
/* 108 */           makeParticle(1);
/*     */         }
/*     */       } else {
/* 111 */         makeParticle(2);
/*     */       }
/*     */     
/* 114 */     } else if (this.inGround && this.inGroundTime != 0 && 
/* 115 */       !this.effects.isEmpty() && this.inGroundTime >= 600) {
/* 116 */       this.level.broadcastEntityEvent(this, (byte)0);
/* 117 */       this.potion = Potions.EMPTY;
/* 118 */       this.effects.clear();
/* 119 */       this.entityData.set(ID_EFFECT_COLOR, Integer.valueOf(-1));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void makeParticle(int debug1) {
/* 126 */     int debug2 = getColor();
/* 127 */     if (debug2 == -1 || debug1 <= 0) {
/*     */       return;
/*     */     }
/*     */     
/* 131 */     double debug3 = (debug2 >> 16 & 0xFF) / 255.0D;
/* 132 */     double debug5 = (debug2 >> 8 & 0xFF) / 255.0D;
/* 133 */     double debug7 = (debug2 >> 0 & 0xFF) / 255.0D;
/*     */     
/* 135 */     for (int debug9 = 0; debug9 < debug1; debug9++) {
/* 136 */       this.level.addParticle((ParticleOptions)ParticleTypes.ENTITY_EFFECT, getRandomX(0.5D), getRandomY(), getRandomZ(0.5D), debug3, debug5, debug7);
/*     */     }
/*     */   }
/*     */   
/*     */   public int getColor() {
/* 141 */     return ((Integer)this.entityData.get(ID_EFFECT_COLOR)).intValue();
/*     */   }
/*     */   
/*     */   private void setFixedColor(int debug1) {
/* 145 */     this.fixedColor = true;
/* 146 */     this.entityData.set(ID_EFFECT_COLOR, Integer.valueOf(debug1));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 151 */     super.addAdditionalSaveData(debug1);
/*     */     
/* 153 */     if (this.potion != Potions.EMPTY && this.potion != null) {
/* 154 */       debug1.putString("Potion", Registry.POTION.getKey(this.potion).toString());
/*     */     }
/* 156 */     if (this.fixedColor) {
/* 157 */       debug1.putInt("Color", getColor());
/*     */     }
/* 159 */     if (!this.effects.isEmpty()) {
/* 160 */       ListTag debug2 = new ListTag();
/* 161 */       for (MobEffectInstance debug4 : this.effects) {
/* 162 */         debug2.add(debug4.save(new CompoundTag()));
/*     */       }
/* 164 */       debug1.put("CustomPotionEffects", (Tag)debug2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 170 */     super.readAdditionalSaveData(debug1);
/*     */     
/* 172 */     if (debug1.contains("Potion", 8)) {
/* 173 */       this.potion = PotionUtils.getPotion(debug1);
/*     */     }
/* 175 */     for (MobEffectInstance debug3 : PotionUtils.getCustomEffects(debug1)) {
/* 176 */       addEffect(debug3);
/*     */     }
/*     */     
/* 179 */     if (debug1.contains("Color", 99)) {
/* 180 */       setFixedColor(debug1.getInt("Color"));
/*     */     } else {
/* 182 */       updateColor();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doPostHurtEffects(LivingEntity debug1) {
/* 188 */     super.doPostHurtEffects(debug1);
/*     */     
/* 190 */     for (MobEffectInstance debug3 : this.potion.getEffects()) {
/* 191 */       debug1.addEffect(new MobEffectInstance(debug3.getEffect(), Math.max(debug3.getDuration() / 8, 1), debug3.getAmplifier(), debug3.isAmbient(), debug3.isVisible()));
/*     */     }
/* 193 */     if (!this.effects.isEmpty()) {
/* 194 */       for (MobEffectInstance debug3 : this.effects) {
/* 195 */         debug1.addEffect(debug3);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack getPickupItem() {
/* 202 */     if (this.effects.isEmpty() && this.potion == Potions.EMPTY) {
/* 203 */       return new ItemStack((ItemLike)Items.ARROW);
/*     */     }
/* 205 */     ItemStack debug1 = new ItemStack((ItemLike)Items.TIPPED_ARROW);
/* 206 */     PotionUtils.setPotion(debug1, this.potion);
/* 207 */     PotionUtils.setCustomEffects(debug1, this.effects);
/* 208 */     if (this.fixedColor) {
/* 209 */       debug1.getOrCreateTag().putInt("CustomPotionColor", getColor());
/*     */     }
/* 211 */     return debug1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\projectile\Arrow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */