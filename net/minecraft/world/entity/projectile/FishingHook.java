/*     */ package net.minecraft.world.entity.projectile;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.ExperienceOrb;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class FishingHook extends Projectile {
/*     */   private boolean biting;
/*  49 */   private final Random syncronizedRandom = new Random();
/*     */   private int outOfWaterTime;
/*     */   
/*     */   enum FishHookState {
/*  53 */     FLYING, HOOKED_IN_ENTITY, BOBBING;
/*     */   }
/*     */ 
/*     */   
/*  57 */   private static final EntityDataAccessor<Integer> DATA_HOOKED_ENTITY = SynchedEntityData.defineId(FishingHook.class, EntityDataSerializers.INT);
/*  58 */   private static final EntityDataAccessor<Boolean> DATA_BITING = SynchedEntityData.defineId(FishingHook.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private int life;
/*     */   
/*     */   private int nibble;
/*     */   private int timeUntilLured;
/*     */   private int timeUntilHooked;
/*     */   private float fishAngle;
/*     */   private boolean openWater = true;
/*     */   private Entity hookedIn;
/*  68 */   private FishHookState currentState = FishHookState.FLYING;
/*     */   
/*     */   private final int luck;
/*     */   private final int lureSpeed;
/*     */   
/*     */   private FishingHook(Level debug1, Player debug2, int debug3, int debug4) {
/*  74 */     super(EntityType.FISHING_BOBBER, debug1);
/*  75 */     this.noCulling = true;
/*  76 */     setOwner((Entity)debug2);
/*  77 */     debug2.fishing = this;
/*  78 */     this.luck = Math.max(0, debug3);
/*  79 */     this.lureSpeed = Math.max(0, debug4);
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
/*     */   public FishingHook(Player debug1, Level debug2, int debug3, int debug4) {
/*  91 */     this(debug2, debug1, debug3, debug4);
/*  92 */     float debug5 = debug1.xRot;
/*  93 */     float debug6 = debug1.yRot;
/*     */     
/*  95 */     float debug7 = Mth.cos(-debug6 * 0.017453292F - 3.1415927F);
/*  96 */     float debug8 = Mth.sin(-debug6 * 0.017453292F - 3.1415927F);
/*  97 */     float debug9 = -Mth.cos(-debug5 * 0.017453292F);
/*  98 */     float debug10 = Mth.sin(-debug5 * 0.017453292F);
/*     */     
/* 100 */     double debug11 = debug1.getX() - debug8 * 0.3D;
/* 101 */     double debug13 = debug1.getEyeY();
/* 102 */     double debug15 = debug1.getZ() - debug7 * 0.3D;
/*     */     
/* 104 */     moveTo(debug11, debug13, debug15, debug6, debug5);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 109 */     Vec3 debug17 = new Vec3(-debug8, Mth.clamp(-(debug10 / debug9), -5.0F, 5.0F), -debug7);
/*     */ 
/*     */ 
/*     */     
/* 113 */     double debug18 = debug17.length();
/* 114 */     debug17 = debug17.multiply(0.6D / debug18 + 0.5D + this.random
/* 115 */         .nextGaussian() * 0.0045D, 0.6D / debug18 + 0.5D + this.random
/* 116 */         .nextGaussian() * 0.0045D, 0.6D / debug18 + 0.5D + this.random
/* 117 */         .nextGaussian() * 0.0045D);
/*     */     
/* 119 */     setDeltaMovement(debug17);
/*     */     
/* 121 */     this.yRot = (float)(Mth.atan2(debug17.x, debug17.z) * 57.2957763671875D);
/* 122 */     this.xRot = (float)(Mth.atan2(debug17.y, Mth.sqrt(getHorizontalDistanceSqr(debug17))) * 57.2957763671875D);
/* 123 */     this.yRotO = this.yRot;
/* 124 */     this.xRotO = this.xRot;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/* 129 */     getEntityData().define(DATA_HOOKED_ENTITY, Integer.valueOf(0));
/* 130 */     getEntityData().define(DATA_BITING, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> debug1) {
/* 135 */     if (DATA_HOOKED_ENTITY.equals(debug1)) {
/* 136 */       int debug2 = ((Integer)getEntityData().get(DATA_HOOKED_ENTITY)).intValue();
/* 137 */       this.hookedIn = (debug2 > 0) ? this.level.getEntity(debug2 - 1) : null;
/*     */     } 
/*     */     
/* 140 */     if (DATA_BITING.equals(debug1)) {
/* 141 */       this.biting = ((Boolean)getEntityData().get(DATA_BITING)).booleanValue();
/* 142 */       if (this.biting) {
/* 143 */         setDeltaMovement((getDeltaMovement()).x, (-0.4F * Mth.nextFloat(this.syncronizedRandom, 0.6F, 1.0F)), (getDeltaMovement()).z);
/*     */       }
/*     */     } 
/* 146 */     super.onSyncedDataUpdated(debug1);
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
/*     */   public void tick() {
/* 163 */     this.syncronizedRandom.setSeed(getUUID().getLeastSignificantBits() ^ this.level.getGameTime());
/*     */     
/* 165 */     super.tick();
/*     */     
/* 167 */     Player debug1 = getPlayerOwner();
/* 168 */     if (debug1 == null) {
/* 169 */       remove();
/*     */       return;
/*     */     } 
/* 172 */     if (!this.level.isClientSide && 
/* 173 */       shouldStopFishing(debug1)) {
/*     */       return;
/*     */     }
/*     */     
/* 177 */     if (this.onGround) {
/* 178 */       this.life++;
/* 179 */       if (this.life >= 1200) {
/* 180 */         remove();
/*     */         return;
/*     */       } 
/*     */     } else {
/* 184 */       this.life = 0;
/*     */     } 
/*     */     
/* 187 */     float debug2 = 0.0F;
/* 188 */     BlockPos debug3 = blockPosition();
/*     */     
/* 190 */     FluidState debug4 = this.level.getFluidState(debug3);
/* 191 */     if (debug4.is((Tag)FluidTags.WATER)) {
/* 192 */       debug2 = debug4.getHeight((BlockGetter)this.level, debug3);
/*     */     }
/*     */     
/* 195 */     boolean debug5 = (debug2 > 0.0F);
/* 196 */     if (this.currentState == FishHookState.FLYING)
/* 197 */     { if (this.hookedIn != null) {
/* 198 */         setDeltaMovement(Vec3.ZERO);
/*     */         
/* 200 */         this.currentState = FishHookState.HOOKED_IN_ENTITY;
/*     */         
/*     */         return;
/*     */       } 
/* 204 */       if (debug5) {
/* 205 */         setDeltaMovement(getDeltaMovement().multiply(0.3D, 0.2D, 0.3D));
/*     */         
/* 207 */         this.currentState = FishHookState.BOBBING;
/*     */         
/*     */         return;
/*     */       } 
/* 211 */       checkCollision(); }
/* 212 */     else { if (this.currentState == FishHookState.HOOKED_IN_ENTITY) {
/* 213 */         if (this.hookedIn != null)
/* 214 */           if (this.hookedIn.removed) {
/* 215 */             this.hookedIn = null;
/* 216 */             this.currentState = FishHookState.FLYING;
/*     */           } else {
/* 218 */             setPos(this.hookedIn.getX(), this.hookedIn.getY(0.8D), this.hookedIn.getZ());
/*     */           }  
/*     */         return;
/*     */       } 
/* 222 */       if (this.currentState == FishHookState.BOBBING) {
/* 223 */         Vec3 vec3 = getDeltaMovement();
/* 224 */         double debug7 = getY() + vec3.y - debug3.getY() - debug2;
/* 225 */         if (Math.abs(debug7) < 0.01D) {
/* 226 */           debug7 += Math.signum(debug7) * 0.1D;
/*     */         }
/*     */         
/* 229 */         setDeltaMovement(vec3.x * 0.9D, vec3.y - debug7 * this.random
/*     */ 
/*     */             
/* 232 */             .nextFloat() * 0.2D, vec3.z * 0.9D);
/*     */ 
/*     */         
/* 235 */         if (this.nibble > 0 || this.timeUntilHooked > 0) {
/* 236 */           this.openWater = (this.openWater && this.outOfWaterTime < 10 && calculateOpenWater(debug3));
/*     */         } else {
/* 238 */           this.openWater = true;
/*     */         } 
/*     */         
/* 241 */         if (debug5) {
/* 242 */           this.outOfWaterTime = Math.max(0, this.outOfWaterTime - 1);
/* 243 */           if (this.biting) {
/* 244 */             setDeltaMovement(getDeltaMovement().add(0.0D, -0.1D * this.syncronizedRandom.nextFloat() * this.syncronizedRandom.nextFloat(), 0.0D));
/*     */           }
/* 246 */           if (!this.level.isClientSide) {
/* 247 */             catchingFish(debug3);
/*     */           }
/*     */         } else {
/* 250 */           this.outOfWaterTime = Math.min(10, this.outOfWaterTime + 1);
/*     */         } 
/*     */       }  }
/*     */     
/* 254 */     if (!debug4.is((Tag)FluidTags.WATER)) {
/* 255 */       setDeltaMovement(getDeltaMovement().add(0.0D, -0.03D, 0.0D));
/*     */     }
/*     */     
/* 258 */     move(MoverType.SELF, getDeltaMovement());
/* 259 */     updateRotation();
/*     */     
/* 261 */     if (this.currentState == FishHookState.FLYING && (
/* 262 */       this.onGround || this.horizontalCollision)) {
/* 263 */       setDeltaMovement(Vec3.ZERO);
/*     */     }
/*     */ 
/*     */     
/* 267 */     double debug6 = 0.92D;
/* 268 */     setDeltaMovement(getDeltaMovement().scale(0.92D));
/*     */     
/* 270 */     reapplyPosition();
/*     */   }
/*     */   
/*     */   private boolean shouldStopFishing(Player debug1) {
/* 274 */     ItemStack debug2 = debug1.getMainHandItem();
/* 275 */     ItemStack debug3 = debug1.getOffhandItem();
/* 276 */     boolean debug4 = (debug2.getItem() == Items.FISHING_ROD);
/* 277 */     boolean debug5 = (debug3.getItem() == Items.FISHING_ROD);
/* 278 */     if (debug1.removed || !debug1.isAlive() || (!debug4 && !debug5) || distanceToSqr((Entity)debug1) > 1024.0D) {
/* 279 */       remove();
/* 280 */       return true;
/*     */     } 
/* 282 */     return false;
/*     */   }
/*     */   
/*     */   private void checkCollision() {
/* 286 */     HitResult debug1 = ProjectileUtil.getHitResult(this, this::canHitEntity);
/* 287 */     onHit(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canHitEntity(Entity debug1) {
/* 292 */     return (super.canHitEntity(debug1) || (debug1.isAlive() && debug1 instanceof ItemEntity));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHitEntity(EntityHitResult debug1) {
/* 297 */     super.onHitEntity(debug1);
/* 298 */     if (!this.level.isClientSide) {
/* 299 */       this.hookedIn = debug1.getEntity();
/* 300 */       setHookedEntity();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHitBlock(BlockHitResult debug1) {
/* 306 */     super.onHitBlock(debug1);
/* 307 */     setDeltaMovement(getDeltaMovement().normalize().scale(debug1.distanceTo(this)));
/*     */   }
/*     */   
/*     */   private void setHookedEntity() {
/* 311 */     getEntityData().set(DATA_HOOKED_ENTITY, Integer.valueOf(this.hookedIn.getId() + 1));
/*     */   }
/*     */   
/*     */   private void catchingFish(BlockPos debug1) {
/* 315 */     ServerLevel debug2 = (ServerLevel)this.level;
/*     */     
/* 317 */     int debug3 = 1;
/* 318 */     BlockPos debug4 = debug1.above();
/* 319 */     if (this.random.nextFloat() < 0.25F && this.level.isRainingAt(debug4)) {
/* 320 */       debug3++;
/*     */     }
/* 322 */     if (this.random.nextFloat() < 0.5F && !this.level.canSeeSky(debug4)) {
/* 323 */       debug3--;
/*     */     }
/*     */     
/* 326 */     if (this.nibble > 0) {
/* 327 */       this.nibble--;
/*     */       
/* 329 */       if (this.nibble <= 0) {
/* 330 */         this.timeUntilLured = 0;
/* 331 */         this.timeUntilHooked = 0;
/* 332 */         getEntityData().set(DATA_BITING, Boolean.valueOf(false));
/*     */       } 
/* 334 */     } else if (this.timeUntilHooked > 0) {
/* 335 */       this.timeUntilHooked -= debug3;
/*     */       
/* 337 */       if (this.timeUntilHooked > 0) {
/* 338 */         this.fishAngle = (float)(this.fishAngle + this.random.nextGaussian() * 4.0D);
/*     */         
/* 340 */         float debug5 = this.fishAngle * 0.017453292F;
/* 341 */         float debug6 = Mth.sin(debug5);
/* 342 */         float debug7 = Mth.cos(debug5);
/* 343 */         double debug8 = getX() + (debug6 * this.timeUntilHooked * 0.1F);
/* 344 */         double debug10 = (Mth.floor(getY()) + 1.0F);
/* 345 */         double debug12 = getZ() + (debug7 * this.timeUntilHooked * 0.1F);
/*     */         
/* 347 */         BlockState debug14 = debug2.getBlockState(new BlockPos(debug8, debug10 - 1.0D, debug12));
/* 348 */         if (debug14.is(Blocks.WATER)) {
/* 349 */           if (this.random.nextFloat() < 0.15F) {
/* 350 */             debug2.sendParticles((ParticleOptions)ParticleTypes.BUBBLE, debug8, debug10 - 0.10000000149011612D, debug12, 1, debug6, 0.1D, debug7, 0.0D);
/*     */           }
/*     */           
/* 353 */           float debug15 = debug6 * 0.04F;
/* 354 */           float debug16 = debug7 * 0.04F;
/*     */           
/* 356 */           debug2.sendParticles((ParticleOptions)ParticleTypes.FISHING, debug8, debug10, debug12, 0, debug16, 0.01D, -debug15, 1.0D);
/* 357 */           debug2.sendParticles((ParticleOptions)ParticleTypes.FISHING, debug8, debug10, debug12, 0, -debug16, 0.01D, debug15, 1.0D);
/*     */         } 
/*     */       } else {
/* 360 */         playSound(SoundEvents.FISHING_BOBBER_SPLASH, 0.25F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
/* 361 */         double debug5 = getY() + 0.5D;
/* 362 */         debug2.sendParticles((ParticleOptions)ParticleTypes.BUBBLE, getX(), debug5, getZ(), (int)(1.0F + getBbWidth() * 20.0F), getBbWidth(), 0.0D, getBbWidth(), 0.20000000298023224D);
/* 363 */         debug2.sendParticles((ParticleOptions)ParticleTypes.FISHING, getX(), debug5, getZ(), (int)(1.0F + getBbWidth() * 20.0F), getBbWidth(), 0.0D, getBbWidth(), 0.20000000298023224D);
/*     */         
/* 365 */         this.nibble = Mth.nextInt(this.random, 20, 40);
/* 366 */         getEntityData().set(DATA_BITING, Boolean.valueOf(true));
/*     */       } 
/* 368 */     } else if (this.timeUntilLured > 0) {
/* 369 */       this.timeUntilLured -= debug3;
/*     */       
/* 371 */       float debug5 = 0.15F;
/* 372 */       if (this.timeUntilLured < 20) {
/* 373 */         debug5 = (float)(debug5 + (20 - this.timeUntilLured) * 0.05D);
/* 374 */       } else if (this.timeUntilLured < 40) {
/* 375 */         debug5 = (float)(debug5 + (40 - this.timeUntilLured) * 0.02D);
/* 376 */       } else if (this.timeUntilLured < 60) {
/* 377 */         debug5 = (float)(debug5 + (60 - this.timeUntilLured) * 0.01D);
/*     */       } 
/*     */       
/* 380 */       if (this.random.nextFloat() < debug5) {
/* 381 */         float debug6 = Mth.nextFloat(this.random, 0.0F, 360.0F) * 0.017453292F;
/* 382 */         float debug7 = Mth.nextFloat(this.random, 25.0F, 60.0F);
/* 383 */         double debug8 = getX() + (Mth.sin(debug6) * debug7 * 0.1F);
/* 384 */         double debug10 = (Mth.floor(getY()) + 1.0F);
/* 385 */         double debug12 = getZ() + (Mth.cos(debug6) * debug7 * 0.1F);
/* 386 */         BlockState debug14 = debug2.getBlockState(new BlockPos(debug8, debug10 - 1.0D, debug12));
/* 387 */         if (debug14.is(Blocks.WATER)) {
/* 388 */           debug2.sendParticles((ParticleOptions)ParticleTypes.SPLASH, debug8, debug10, debug12, 2 + this.random.nextInt(2), 0.10000000149011612D, 0.0D, 0.10000000149011612D, 0.0D);
/*     */         }
/*     */       } 
/*     */       
/* 392 */       if (this.timeUntilLured <= 0) {
/* 393 */         this.fishAngle = Mth.nextFloat(this.random, 0.0F, 360.0F);
/* 394 */         this.timeUntilHooked = Mth.nextInt(this.random, 20, 80);
/*     */       } 
/*     */     } else {
/* 397 */       this.timeUntilLured = Mth.nextInt(this.random, 100, 600);
/* 398 */       this.timeUntilLured -= this.lureSpeed * 20 * 5;
/*     */     } 
/*     */   }
/*     */   
/*     */   enum OpenWaterType {
/* 403 */     ABOVE_WATER, INSIDE_WATER, INVALID;
/*     */   }
/*     */   
/*     */   private boolean calculateOpenWater(BlockPos debug1) {
/* 407 */     OpenWaterType debug2 = OpenWaterType.INVALID;
/* 408 */     for (int debug3 = -1; debug3 <= 2; debug3++) {
/* 409 */       OpenWaterType debug4 = getOpenWaterTypeForArea(debug1.offset(-2, debug3, -2), debug1.offset(2, debug3, 2));
/* 410 */       switch (debug4) {
/*     */         case INVALID:
/* 412 */           return false;
/*     */         case ABOVE_WATER:
/* 414 */           if (debug2 == OpenWaterType.INVALID) {
/* 415 */             return false;
/*     */           }
/*     */           break;
/*     */         case INSIDE_WATER:
/* 419 */           if (debug2 == OpenWaterType.ABOVE_WATER)
/* 420 */             return false; 
/*     */           break;
/*     */       } 
/* 423 */       debug2 = debug4;
/*     */     } 
/* 425 */     return true;
/*     */   }
/*     */   
/*     */   private OpenWaterType getOpenWaterTypeForArea(BlockPos debug1, BlockPos debug2) {
/* 429 */     return BlockPos.betweenClosedStream(debug1, debug2).map(this::getOpenWaterTypeForBlock).reduce((debug0, debug1) -> (debug0 == debug1) ? debug0 : OpenWaterType.INVALID).orElse(OpenWaterType.INVALID);
/*     */   }
/*     */   
/*     */   private OpenWaterType getOpenWaterTypeForBlock(BlockPos debug1) {
/* 433 */     BlockState debug2 = this.level.getBlockState(debug1);
/* 434 */     if (debug2.isAir() || debug2.is(Blocks.LILY_PAD)) {
/* 435 */       return OpenWaterType.ABOVE_WATER;
/*     */     }
/* 437 */     FluidState debug3 = debug2.getFluidState();
/* 438 */     if (debug3.is((Tag)FluidTags.WATER) && debug3.isSource() && debug2.getCollisionShape((BlockGetter)this.level, debug1).isEmpty()) {
/* 439 */       return OpenWaterType.INSIDE_WATER;
/*     */     }
/* 441 */     return OpenWaterType.INVALID;
/*     */   }
/*     */   
/*     */   public boolean isOpenWaterFishing() {
/* 445 */     return this.openWater;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {}
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {}
/*     */ 
/*     */   
/*     */   public int retrieve(ItemStack debug1) {
/* 457 */     Player debug2 = getPlayerOwner();
/* 458 */     if (this.level.isClientSide || debug2 == null) {
/* 459 */       return 0;
/*     */     }
/*     */     
/* 462 */     int debug3 = 0;
/* 463 */     if (this.hookedIn != null) {
/* 464 */       bringInHookedEntity();
/* 465 */       CriteriaTriggers.FISHING_ROD_HOOKED.trigger((ServerPlayer)debug2, debug1, this, Collections.emptyList());
/* 466 */       this.level.broadcastEntityEvent(this, (byte)31);
/* 467 */       debug3 = (this.hookedIn instanceof ItemEntity) ? 3 : 5;
/* 468 */     } else if (this.nibble > 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 474 */       LootContext.Builder debug4 = (new LootContext.Builder((ServerLevel)this.level)).withParameter(LootContextParams.ORIGIN, position()).withParameter(LootContextParams.TOOL, debug1).withParameter(LootContextParams.THIS_ENTITY, this).withRandom(this.random).withLuck(this.luck + debug2.getLuck());
/* 475 */       LootTable debug5 = this.level.getServer().getLootTables().get(BuiltInLootTables.FISHING);
/* 476 */       List<ItemStack> debug6 = debug5.getRandomItems(debug4.create(LootContextParamSets.FISHING));
/* 477 */       CriteriaTriggers.FISHING_ROD_HOOKED.trigger((ServerPlayer)debug2, debug1, this, debug6);
/* 478 */       for (ItemStack debug8 : debug6) {
/* 479 */         ItemEntity debug9 = new ItemEntity(this.level, getX(), getY(), getZ(), debug8);
/* 480 */         double debug10 = debug2.getX() - getX();
/* 481 */         double debug12 = debug2.getY() - getY();
/* 482 */         double debug14 = debug2.getZ() - getZ();
/*     */         
/* 484 */         double debug16 = 0.1D;
/* 485 */         debug9.setDeltaMovement(debug10 * 0.1D, debug12 * 0.1D + 
/*     */             
/* 487 */             Math.sqrt(Math.sqrt(debug10 * debug10 + debug12 * debug12 + debug14 * debug14)) * 0.08D, debug14 * 0.1D);
/*     */ 
/*     */         
/* 490 */         this.level.addFreshEntity((Entity)debug9);
/* 491 */         debug2.level.addFreshEntity((Entity)new ExperienceOrb(debug2.level, debug2.getX(), debug2.getY() + 0.5D, debug2.getZ() + 0.5D, this.random.nextInt(6) + 1));
/*     */         
/* 493 */         if (debug8.getItem().is((Tag)ItemTags.FISHES)) {
/* 494 */           debug2.awardStat(Stats.FISH_CAUGHT, 1);
/*     */         }
/*     */       } 
/* 497 */       debug3 = 1;
/*     */     } 
/* 499 */     if (this.onGround) {
/* 500 */       debug3 = 2;
/*     */     }
/*     */     
/* 503 */     remove();
/* 504 */     return debug3;
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
/*     */   protected void bringInHookedEntity() {
/* 519 */     Entity debug1 = getOwner();
/* 520 */     if (debug1 == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 528 */     Vec3 debug2 = (new Vec3(debug1.getX() - getX(), debug1.getY() - getY(), debug1.getZ() - getZ())).scale(0.1D);
/*     */     
/* 530 */     this.hookedIn.setDeltaMovement(this.hookedIn.getDeltaMovement().add(debug2));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isMovementNoisy() {
/* 535 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove() {
/* 540 */     super.remove();
/* 541 */     Player debug1 = getPlayerOwner();
/* 542 */     if (debug1 != null) {
/* 543 */       debug1.fishing = null;
/*     */     }
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Player getPlayerOwner() {
/* 549 */     Entity debug1 = getOwner();
/* 550 */     return (debug1 instanceof Player) ? (Player)debug1 : null;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Entity getHookedIn() {
/* 555 */     return this.hookedIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canChangeDimensions() {
/* 560 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Packet<?> getAddEntityPacket() {
/* 565 */     Entity debug1 = getOwner();
/* 566 */     return (Packet<?>)new ClientboundAddEntityPacket(this, (debug1 == null) ? getId() : debug1.getId());
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\projectile\FishingHook.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */