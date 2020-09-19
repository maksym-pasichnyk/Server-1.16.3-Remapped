/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.commands.arguments.ParticleArgument;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.item.alchemy.Potion;
/*     */ import net.minecraft.world.item.alchemy.PotionUtils;
/*     */ import net.minecraft.world.item.alchemy.Potions;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.material.PushReaction;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class AreaEffectCloud
/*     */   extends Entity
/*     */ {
/*  38 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */ 
/*     */ 
/*     */   
/*  42 */   private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(AreaEffectCloud.class, EntityDataSerializers.FLOAT);
/*  43 */   private static final EntityDataAccessor<Integer> DATA_COLOR = SynchedEntityData.defineId(AreaEffectCloud.class, EntityDataSerializers.INT);
/*  44 */   private static final EntityDataAccessor<Boolean> DATA_WAITING = SynchedEntityData.defineId(AreaEffectCloud.class, EntityDataSerializers.BOOLEAN);
/*  45 */   private static final EntityDataAccessor<ParticleOptions> DATA_PARTICLE = SynchedEntityData.defineId(AreaEffectCloud.class, EntityDataSerializers.PARTICLE);
/*     */   
/*  47 */   private Potion potion = Potions.EMPTY;
/*  48 */   private final List<MobEffectInstance> effects = Lists.newArrayList();
/*  49 */   private final Map<Entity, Integer> victims = Maps.newHashMap();
/*  50 */   private int duration = 600;
/*  51 */   private int waitTime = 20;
/*  52 */   private int reapplicationDelay = 20;
/*     */   private boolean fixedColor;
/*     */   private int durationOnUse;
/*     */   private float radiusOnUse;
/*     */   private float radiusPerTick;
/*     */   private LivingEntity owner;
/*     */   private UUID ownerUUID;
/*     */   
/*     */   public AreaEffectCloud(EntityType<? extends AreaEffectCloud> debug1, Level debug2) {
/*  61 */     super(debug1, debug2);
/*  62 */     this.noPhysics = true;
/*  63 */     setRadius(3.0F);
/*     */   }
/*     */   
/*     */   public AreaEffectCloud(Level debug1, double debug2, double debug4, double debug6) {
/*  67 */     this(EntityType.AREA_EFFECT_CLOUD, debug1);
/*  68 */     setPos(debug2, debug4, debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/*  73 */     getEntityData().define(DATA_COLOR, Integer.valueOf(0));
/*  74 */     getEntityData().define(DATA_RADIUS, Float.valueOf(0.5F));
/*  75 */     getEntityData().define(DATA_WAITING, Boolean.valueOf(false));
/*  76 */     getEntityData().define(DATA_PARTICLE, ParticleTypes.ENTITY_EFFECT);
/*     */   }
/*     */   
/*     */   public void setRadius(float debug1) {
/*  80 */     if (!this.level.isClientSide) {
/*  81 */       getEntityData().set(DATA_RADIUS, Float.valueOf(debug1));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void refreshDimensions() {
/*  87 */     double debug1 = getX();
/*  88 */     double debug3 = getY();
/*  89 */     double debug5 = getZ();
/*  90 */     super.refreshDimensions();
/*  91 */     setPos(debug1, debug3, debug5);
/*     */   }
/*     */   
/*     */   public float getRadius() {
/*  95 */     return ((Float)getEntityData().get(DATA_RADIUS)).floatValue();
/*     */   }
/*     */   
/*     */   public void setPotion(Potion debug1) {
/*  99 */     this.potion = debug1;
/* 100 */     if (!this.fixedColor) {
/* 101 */       updateColor();
/*     */     }
/*     */   }
/*     */   
/*     */   private void updateColor() {
/* 106 */     if (this.potion == Potions.EMPTY && this.effects.isEmpty()) {
/* 107 */       getEntityData().set(DATA_COLOR, Integer.valueOf(0));
/*     */     } else {
/* 109 */       getEntityData().set(DATA_COLOR, Integer.valueOf(PotionUtils.getColor(PotionUtils.getAllEffects(this.potion, this.effects))));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addEffect(MobEffectInstance debug1) {
/* 114 */     this.effects.add(debug1);
/* 115 */     if (!this.fixedColor) {
/* 116 */       updateColor();
/*     */     }
/*     */   }
/*     */   
/*     */   public int getColor() {
/* 121 */     return ((Integer)getEntityData().get(DATA_COLOR)).intValue();
/*     */   }
/*     */   
/*     */   public void setFixedColor(int debug1) {
/* 125 */     this.fixedColor = true;
/* 126 */     getEntityData().set(DATA_COLOR, Integer.valueOf(debug1));
/*     */   }
/*     */   
/*     */   public ParticleOptions getParticle() {
/* 130 */     return (ParticleOptions)getEntityData().get(DATA_PARTICLE);
/*     */   }
/*     */   
/*     */   public void setParticle(ParticleOptions debug1) {
/* 134 */     getEntityData().set(DATA_PARTICLE, debug1);
/*     */   }
/*     */   
/*     */   protected void setWaiting(boolean debug1) {
/* 138 */     getEntityData().set(DATA_WAITING, Boolean.valueOf(debug1));
/*     */   }
/*     */   
/*     */   public boolean isWaiting() {
/* 142 */     return ((Boolean)getEntityData().get(DATA_WAITING)).booleanValue();
/*     */   }
/*     */   
/*     */   public int getDuration() {
/* 146 */     return this.duration;
/*     */   }
/*     */   
/*     */   public void setDuration(int debug1) {
/* 150 */     this.duration = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 155 */     super.tick();
/* 156 */     boolean debug1 = isWaiting();
/*     */     
/* 158 */     float debug2 = getRadius();
/* 159 */     if (this.level.isClientSide) {
/* 160 */       ParticleOptions debug3 = getParticle();
/*     */       
/* 162 */       if (debug1) {
/* 163 */         if (this.random.nextBoolean()) {
/* 164 */           for (int debug4 = 0; debug4 < 2; debug4++) {
/* 165 */             float debug5 = this.random.nextFloat() * 6.2831855F;
/* 166 */             float debug6 = Mth.sqrt(this.random.nextFloat()) * 0.2F;
/* 167 */             float debug7 = Mth.cos(debug5) * debug6;
/* 168 */             float debug8 = Mth.sin(debug5) * debug6;
/*     */             
/* 170 */             if (debug3.getType() == ParticleTypes.ENTITY_EFFECT) {
/* 171 */               int debug9 = this.random.nextBoolean() ? 16777215 : getColor();
/* 172 */               int debug10 = debug9 >> 16 & 0xFF;
/* 173 */               int debug11 = debug9 >> 8 & 0xFF;
/* 174 */               int debug12 = debug9 & 0xFF;
/* 175 */               this.level.addAlwaysVisibleParticle(debug3, getX() + debug7, getY(), getZ() + debug8, (debug10 / 255.0F), (debug11 / 255.0F), (debug12 / 255.0F));
/*     */             } else {
/* 177 */               this.level.addAlwaysVisibleParticle(debug3, getX() + debug7, getY(), getZ() + debug8, 0.0D, 0.0D, 0.0D);
/*     */             } 
/*     */           } 
/*     */         }
/*     */       } else {
/* 182 */         float debug4 = 3.1415927F * debug2 * debug2;
/*     */         
/* 184 */         for (int debug5 = 0; debug5 < debug4; debug5++) {
/* 185 */           float debug6 = this.random.nextFloat() * 6.2831855F;
/* 186 */           float debug7 = Mth.sqrt(this.random.nextFloat()) * debug2;
/* 187 */           float debug8 = Mth.cos(debug6) * debug7;
/* 188 */           float debug9 = Mth.sin(debug6) * debug7;
/*     */           
/* 190 */           if (debug3.getType() == ParticleTypes.ENTITY_EFFECT) {
/* 191 */             int debug10 = getColor();
/* 192 */             int debug11 = debug10 >> 16 & 0xFF;
/* 193 */             int debug12 = debug10 >> 8 & 0xFF;
/* 194 */             int debug13 = debug10 & 0xFF;
/* 195 */             this.level.addAlwaysVisibleParticle(debug3, getX() + debug8, getY(), getZ() + debug9, (debug11 / 255.0F), (debug12 / 255.0F), (debug13 / 255.0F));
/*     */           } else {
/* 197 */             this.level.addAlwaysVisibleParticle(debug3, getX() + debug8, getY(), getZ() + debug9, (0.5D - this.random.nextDouble()) * 0.15D, 0.009999999776482582D, (0.5D - this.random.nextDouble()) * 0.15D);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } else {
/* 202 */       if (this.tickCount >= this.waitTime + this.duration) {
/* 203 */         remove();
/*     */         
/*     */         return;
/*     */       } 
/* 207 */       boolean debug3 = (this.tickCount < this.waitTime);
/* 208 */       if (debug1 != debug3) {
/* 209 */         setWaiting(debug3);
/*     */       }
/* 211 */       if (debug3) {
/*     */         return;
/*     */       }
/*     */       
/* 215 */       if (this.radiusPerTick != 0.0F) {
/* 216 */         debug2 += this.radiusPerTick;
/* 217 */         if (debug2 < 0.5F) {
/* 218 */           remove();
/*     */           return;
/*     */         } 
/* 221 */         setRadius(debug2);
/*     */       } 
/*     */       
/* 224 */       if (this.tickCount % 5 == 0) {
/* 225 */         for (Iterator<Map.Entry<Entity, Integer>> iterator = this.victims.entrySet().iterator(); iterator.hasNext(); ) {
/* 226 */           Map.Entry<Entity, Integer> debug5 = iterator.next();
/* 227 */           if (this.tickCount >= ((Integer)debug5.getValue()).intValue()) {
/* 228 */             iterator.remove();
/*     */           }
/*     */         } 
/* 231 */         List<MobEffectInstance> debug4 = Lists.newArrayList();
/* 232 */         for (MobEffectInstance debug6 : this.potion.getEffects()) {
/* 233 */           debug4.add(new MobEffectInstance(debug6.getEffect(), debug6.getDuration() / 4, debug6.getAmplifier(), debug6.isAmbient(), debug6.isVisible()));
/*     */         }
/* 235 */         debug4.addAll(this.effects);
/*     */         
/* 237 */         if (debug4.isEmpty()) {
/* 238 */           this.victims.clear();
/*     */         } else {
/* 240 */           List<LivingEntity> debug5 = this.level.getEntitiesOfClass(LivingEntity.class, getBoundingBox());
/* 241 */           if (!debug5.isEmpty()) {
/* 242 */             for (LivingEntity debug7 : debug5) {
/* 243 */               if (this.victims.containsKey(debug7) || !debug7.isAffectedByPotions()) {
/*     */                 continue;
/*     */               }
/* 246 */               double debug8 = debug7.getX() - getX();
/* 247 */               double debug10 = debug7.getZ() - getZ();
/* 248 */               double debug12 = debug8 * debug8 + debug10 * debug10;
/* 249 */               if (debug12 <= (debug2 * debug2)) {
/* 250 */                 this.victims.put(debug7, Integer.valueOf(this.tickCount + this.reapplicationDelay));
/* 251 */                 for (MobEffectInstance debug15 : debug4) {
/* 252 */                   if (debug15.getEffect().isInstantenous()) {
/* 253 */                     debug15.getEffect().applyInstantenousEffect(this, getOwner(), debug7, debug15.getAmplifier(), 0.5D); continue;
/*     */                   } 
/* 255 */                   debug7.addEffect(new MobEffectInstance(debug15));
/*     */                 } 
/*     */                 
/* 258 */                 if (this.radiusOnUse != 0.0F) {
/* 259 */                   debug2 += this.radiusOnUse;
/* 260 */                   if (debug2 < 0.5F) {
/* 261 */                     remove();
/*     */                     return;
/*     */                   } 
/* 264 */                   setRadius(debug2);
/*     */                 } 
/* 266 */                 if (this.durationOnUse != 0) {
/* 267 */                   this.duration += this.durationOnUse;
/* 268 */                   if (this.duration <= 0) {
/* 269 */                     remove();
/*     */                     return;
/*     */                   } 
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRadiusOnUse(float debug1) {
/* 286 */     this.radiusOnUse = debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRadiusPerTick(float debug1) {
/* 294 */     this.radiusPerTick = debug1;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWaitTime(int debug1) {
/* 319 */     this.waitTime = debug1;
/*     */   }
/*     */   
/*     */   public void setOwner(@Nullable LivingEntity debug1) {
/* 323 */     this.owner = debug1;
/* 324 */     this.ownerUUID = (debug1 == null) ? null : debug1.getUUID();
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public LivingEntity getOwner() {
/* 329 */     if (this.owner == null && this.ownerUUID != null && this.level instanceof ServerLevel) {
/* 330 */       Entity debug1 = ((ServerLevel)this.level).getEntity(this.ownerUUID);
/* 331 */       if (debug1 instanceof LivingEntity) {
/* 332 */         this.owner = (LivingEntity)debug1;
/*     */       }
/*     */     } 
/*     */     
/* 336 */     return this.owner;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(CompoundTag debug1) {
/* 341 */     this.tickCount = debug1.getInt("Age");
/* 342 */     this.duration = debug1.getInt("Duration");
/* 343 */     this.waitTime = debug1.getInt("WaitTime");
/* 344 */     this.reapplicationDelay = debug1.getInt("ReapplicationDelay");
/* 345 */     this.durationOnUse = debug1.getInt("DurationOnUse");
/* 346 */     this.radiusOnUse = debug1.getFloat("RadiusOnUse");
/* 347 */     this.radiusPerTick = debug1.getFloat("RadiusPerTick");
/* 348 */     setRadius(debug1.getFloat("Radius"));
/* 349 */     if (debug1.hasUUID("Owner")) {
/* 350 */       this.ownerUUID = debug1.getUUID("Owner");
/*     */     }
/*     */     
/* 353 */     if (debug1.contains("Particle", 8)) {
/*     */       try {
/* 355 */         setParticle(ParticleArgument.readParticle(new StringReader(debug1.getString("Particle"))));
/* 356 */       } catch (CommandSyntaxException debug2) {
/* 357 */         LOGGER.warn("Couldn't load custom particle {}", debug1.getString("Particle"), debug2);
/*     */       } 
/*     */     }
/*     */     
/* 361 */     if (debug1.contains("Color", 99)) {
/* 362 */       setFixedColor(debug1.getInt("Color"));
/*     */     }
/*     */     
/* 365 */     if (debug1.contains("Potion", 8)) {
/* 366 */       setPotion(PotionUtils.getPotion(debug1));
/*     */     }
/* 368 */     if (debug1.contains("Effects", 9)) {
/* 369 */       ListTag debug2 = debug1.getList("Effects", 10);
/* 370 */       this.effects.clear();
/* 371 */       for (int debug3 = 0; debug3 < debug2.size(); debug3++) {
/* 372 */         MobEffectInstance debug4 = MobEffectInstance.load(debug2.getCompound(debug3));
/* 373 */         if (debug4 != null) {
/* 374 */           addEffect(debug4);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(CompoundTag debug1) {
/* 382 */     debug1.putInt("Age", this.tickCount);
/* 383 */     debug1.putInt("Duration", this.duration);
/* 384 */     debug1.putInt("WaitTime", this.waitTime);
/* 385 */     debug1.putInt("ReapplicationDelay", this.reapplicationDelay);
/* 386 */     debug1.putInt("DurationOnUse", this.durationOnUse);
/* 387 */     debug1.putFloat("RadiusOnUse", this.radiusOnUse);
/* 388 */     debug1.putFloat("RadiusPerTick", this.radiusPerTick);
/* 389 */     debug1.putFloat("Radius", getRadius());
/*     */     
/* 391 */     debug1.putString("Particle", getParticle().writeToString());
/*     */     
/* 393 */     if (this.ownerUUID != null) {
/* 394 */       debug1.putUUID("Owner", this.ownerUUID);
/*     */     }
/*     */     
/* 397 */     if (this.fixedColor) {
/* 398 */       debug1.putInt("Color", getColor());
/*     */     }
/*     */     
/* 401 */     if (this.potion != Potions.EMPTY && this.potion != null) {
/* 402 */       debug1.putString("Potion", Registry.POTION.getKey(this.potion).toString());
/*     */     }
/* 404 */     if (!this.effects.isEmpty()) {
/* 405 */       ListTag debug2 = new ListTag();
/* 406 */       for (MobEffectInstance debug4 : this.effects) {
/* 407 */         debug2.add(debug4.save(new CompoundTag()));
/*     */       }
/* 409 */       debug1.put("Effects", (Tag)debug2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> debug1) {
/* 415 */     if (DATA_RADIUS.equals(debug1)) {
/* 416 */       refreshDimensions();
/*     */     }
/* 418 */     super.onSyncedDataUpdated(debug1);
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
/*     */   public PushReaction getPistonPushReaction() {
/* 431 */     return PushReaction.IGNORE;
/*     */   }
/*     */ 
/*     */   
/*     */   public Packet<?> getAddEntityPacket() {
/* 436 */     return (Packet<?>)new ClientboundAddEntityPacket(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityDimensions getDimensions(Pose debug1) {
/* 441 */     return EntityDimensions.scalable(getRadius() * 2.0F, 0.5F);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\AreaEffectCloud.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */