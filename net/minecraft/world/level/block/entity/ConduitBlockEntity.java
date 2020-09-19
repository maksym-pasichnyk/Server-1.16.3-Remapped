/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.UUID;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConduitBlockEntity
/*     */   extends BlockEntity
/*     */   implements TickableBlockEntity
/*     */ {
/*  39 */   private static final Block[] VALID_BLOCKS = new Block[] { Blocks.PRISMARINE, Blocks.PRISMARINE_BRICKS, Blocks.SEA_LANTERN, Blocks.DARK_PRISMARINE };
/*     */   
/*     */   public int tickCount;
/*     */   
/*     */   private float activeRotation;
/*     */   private boolean isActive;
/*     */   private boolean isHunting;
/*  46 */   private final List<BlockPos> effectBlocks = Lists.newArrayList();
/*     */   
/*     */   @Nullable
/*     */   private LivingEntity destroyTarget;
/*     */   @Nullable
/*     */   private UUID destroyTargetUUID;
/*     */   private long nextAmbientSoundActivation;
/*     */   
/*     */   public ConduitBlockEntity() {
/*  55 */     this(BlockEntityType.CONDUIT);
/*     */   }
/*     */   
/*     */   public ConduitBlockEntity(BlockEntityType<?> debug1) {
/*  59 */     super(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void load(BlockState debug1, CompoundTag debug2) {
/*  64 */     super.load(debug1, debug2);
/*     */     
/*  66 */     if (debug2.hasUUID("Target")) {
/*  67 */       this.destroyTargetUUID = debug2.getUUID("Target");
/*     */     } else {
/*  69 */       this.destroyTargetUUID = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag save(CompoundTag debug1) {
/*  75 */     super.save(debug1);
/*     */     
/*  77 */     if (this.destroyTarget != null) {
/*  78 */       debug1.putUUID("Target", this.destroyTarget.getUUID());
/*     */     }
/*     */     
/*  81 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ClientboundBlockEntityDataPacket getUpdatePacket() {
/*  87 */     return new ClientboundBlockEntityDataPacket(this.worldPosition, 5, getUpdateTag());
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag getUpdateTag() {
/*  92 */     return save(new CompoundTag());
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  97 */     this.tickCount++;
/*     */     
/*  99 */     long debug1 = this.level.getGameTime();
/* 100 */     if (debug1 % 40L == 0L) {
/* 101 */       setActive(updateShape());
/* 102 */       if (!this.level.isClientSide && isActive()) {
/* 103 */         applyEffects();
/* 104 */         updateDestroyTarget();
/*     */       } 
/*     */     } 
/*     */     
/* 108 */     if (debug1 % 80L == 0L && isActive()) {
/* 109 */       playSound(SoundEvents.CONDUIT_AMBIENT);
/*     */     }
/*     */     
/* 112 */     if (debug1 > this.nextAmbientSoundActivation && isActive()) {
/* 113 */       this.nextAmbientSoundActivation = debug1 + 60L + this.level.getRandom().nextInt(40);
/* 114 */       playSound(SoundEvents.CONDUIT_AMBIENT_SHORT);
/*     */     } 
/*     */     
/* 117 */     if (this.level.isClientSide) {
/* 118 */       updateClientTarget();
/* 119 */       animationTick();
/* 120 */       if (isActive()) {
/* 121 */         this.activeRotation++;
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean updateShape() {
/* 127 */     this.effectBlocks.clear();
/*     */     
/*     */     int debug1;
/* 130 */     for (debug1 = -1; debug1 <= 1; debug1++) {
/* 131 */       for (int debug2 = -1; debug2 <= 1; debug2++) {
/* 132 */         for (int debug3 = -1; debug3 <= 1; debug3++) {
/* 133 */           BlockPos debug4 = this.worldPosition.offset(debug1, debug2, debug3);
/* 134 */           if (!this.level.isWaterAt(debug4)) {
/* 135 */             return false;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 142 */     for (debug1 = -2; debug1 <= 2; debug1++) {
/* 143 */       for (int debug2 = -2; debug2 <= 2; debug2++) {
/* 144 */         for (int debug3 = -2; debug3 <= 2; debug3++) {
/* 145 */           int debug4 = Math.abs(debug1);
/* 146 */           int debug5 = Math.abs(debug2);
/* 147 */           int debug6 = Math.abs(debug3);
/* 148 */           if (debug4 > 1 || debug5 > 1 || debug6 > 1)
/*     */           {
/*     */             
/* 151 */             if ((debug1 == 0 && (debug5 == 2 || debug6 == 2)) || (debug2 == 0 && (debug4 == 2 || debug6 == 2)) || (debug3 == 0 && (debug4 == 2 || debug5 == 2))) {
/* 152 */               BlockPos debug7 = this.worldPosition.offset(debug1, debug2, debug3);
/* 153 */               BlockState debug8 = this.level.getBlockState(debug7);
/* 154 */               for (Block debug12 : VALID_BLOCKS) {
/* 155 */                 if (debug8.is(debug12)) {
/* 156 */                   this.effectBlocks.add(debug7);
/*     */                 }
/*     */               } 
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 164 */     setHunting((this.effectBlocks.size() >= 42));
/*     */     
/* 166 */     return (this.effectBlocks.size() >= 16);
/*     */   }
/*     */   
/*     */   private void applyEffects() {
/* 170 */     int debug1 = this.effectBlocks.size();
/* 171 */     int debug2 = debug1 / 7 * 16;
/*     */ 
/*     */     
/* 174 */     int debug3 = this.worldPosition.getX();
/* 175 */     int debug4 = this.worldPosition.getY();
/* 176 */     int debug5 = this.worldPosition.getZ();
/* 177 */     AABB debug6 = (new AABB(debug3, debug4, debug5, (debug3 + 1), (debug4 + 1), (debug5 + 1))).inflate(debug2).expandTowards(0.0D, this.level.getMaxBuildHeight(), 0.0D);
/* 178 */     List<Player> debug7 = this.level.getEntitiesOfClass(Player.class, debug6);
/*     */     
/* 180 */     if (debug7.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 184 */     for (Player debug9 : debug7) {
/* 185 */       if (this.worldPosition.closerThan((Vec3i)debug9.blockPosition(), debug2) && debug9.isInWaterOrRain()) {
/* 186 */         debug9.addEffect(new MobEffectInstance(MobEffects.CONDUIT_POWER, 260, 0, true, true));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateDestroyTarget() {
/* 192 */     LivingEntity debug1 = this.destroyTarget;
/* 193 */     int debug2 = this.effectBlocks.size();
/* 194 */     if (debug2 < 42) {
/* 195 */       this.destroyTarget = null;
/* 196 */     } else if (this.destroyTarget == null && this.destroyTargetUUID != null) {
/*     */       
/* 198 */       this.destroyTarget = findDestroyTarget();
/* 199 */       this.destroyTargetUUID = null;
/* 200 */     } else if (this.destroyTarget == null) {
/* 201 */       List<LivingEntity> debug3 = this.level.getEntitiesOfClass(LivingEntity.class, getDestroyRangeAABB(), debug0 -> (debug0 instanceof net.minecraft.world.entity.monster.Enemy && debug0.isInWaterOrRain()));
/* 202 */       if (!debug3.isEmpty()) {
/* 203 */         this.destroyTarget = debug3.get(this.level.random.nextInt(debug3.size()));
/*     */       }
/* 205 */     } else if (!this.destroyTarget.isAlive() || !this.worldPosition.closerThan((Vec3i)this.destroyTarget.blockPosition(), 8.0D)) {
/* 206 */       this.destroyTarget = null;
/*     */     } 
/*     */     
/* 209 */     if (this.destroyTarget != null) {
/* 210 */       this.level.playSound(null, this.destroyTarget.getX(), this.destroyTarget.getY(), this.destroyTarget.getZ(), SoundEvents.CONDUIT_ATTACK_TARGET, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 211 */       this.destroyTarget.hurt(DamageSource.MAGIC, 4.0F);
/*     */     } 
/*     */     
/* 214 */     if (debug1 != this.destroyTarget) {
/* 215 */       BlockState debug3 = getBlockState();
/* 216 */       this.level.sendBlockUpdated(this.worldPosition, debug3, debug3, 2);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateClientTarget() {
/* 221 */     if (this.destroyTargetUUID == null) {
/* 222 */       this.destroyTarget = null;
/* 223 */     } else if (this.destroyTarget == null || !this.destroyTarget.getUUID().equals(this.destroyTargetUUID)) {
/* 224 */       this.destroyTarget = findDestroyTarget();
/* 225 */       if (this.destroyTarget == null) {
/* 226 */         this.destroyTargetUUID = null;
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private AABB getDestroyRangeAABB() {
/* 232 */     int debug1 = this.worldPosition.getX();
/* 233 */     int debug2 = this.worldPosition.getY();
/* 234 */     int debug3 = this.worldPosition.getZ();
/* 235 */     return (new AABB(debug1, debug2, debug3, (debug1 + 1), (debug2 + 1), (debug3 + 1))).inflate(8.0D);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private LivingEntity findDestroyTarget() {
/* 240 */     List<LivingEntity> debug1 = this.level.getEntitiesOfClass(LivingEntity.class, getDestroyRangeAABB(), debug1 -> debug1.getUUID().equals(this.destroyTargetUUID));
/* 241 */     if (debug1.size() == 1) {
/* 242 */       return debug1.get(0);
/*     */     }
/* 244 */     return null;
/*     */   }
/*     */   
/*     */   private void animationTick() {
/* 248 */     Random debug1 = this.level.random;
/*     */     
/* 250 */     double debug2 = (Mth.sin((this.tickCount + 35) * 0.1F) / 2.0F + 0.5F);
/* 251 */     debug2 = (debug2 * debug2 + debug2) * 0.30000001192092896D;
/*     */     
/* 253 */     Vec3 debug4 = new Vec3(this.worldPosition.getX() + 0.5D, this.worldPosition.getY() + 1.5D + debug2, this.worldPosition.getZ() + 0.5D);
/* 254 */     for (BlockPos debug6 : this.effectBlocks) {
/* 255 */       if (debug1.nextInt(50) != 0) {
/*     */         continue;
/*     */       }
/*     */       
/* 259 */       float debug7 = -0.5F + debug1.nextFloat();
/* 260 */       float debug8 = -2.0F + debug1.nextFloat();
/* 261 */       float debug9 = -0.5F + debug1.nextFloat();
/* 262 */       BlockPos debug10 = debug6.subtract((Vec3i)this.worldPosition);
/* 263 */       Vec3 debug11 = (new Vec3(debug7, debug8, debug9)).add(debug10.getX(), debug10.getY(), debug10.getZ());
/* 264 */       this.level.addParticle((ParticleOptions)ParticleTypes.NAUTILUS, debug4.x, debug4.y, debug4.z, debug11.x, debug11.y, debug11.z);
/*     */     } 
/*     */     
/* 267 */     if (this.destroyTarget != null) {
/* 268 */       Vec3 debug5 = new Vec3(this.destroyTarget.getX(), this.destroyTarget.getEyeY(), this.destroyTarget.getZ());
/* 269 */       float debug6 = (-0.5F + debug1.nextFloat()) * (3.0F + this.destroyTarget.getBbWidth());
/* 270 */       float debug7 = -1.0F + debug1.nextFloat() * this.destroyTarget.getBbHeight();
/* 271 */       float debug8 = (-0.5F + debug1.nextFloat()) * (3.0F + this.destroyTarget.getBbWidth());
/* 272 */       Vec3 debug9 = new Vec3(debug6, debug7, debug8);
/* 273 */       this.level.addParticle((ParticleOptions)ParticleTypes.NAUTILUS, debug5.x, debug5.y, debug5.z, debug9.x, debug9.y, debug9.z);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isActive() {
/* 278 */     return this.isActive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setActive(boolean debug1) {
/* 286 */     if (debug1 != this.isActive) {
/* 287 */       playSound(debug1 ? SoundEvents.CONDUIT_ACTIVATE : SoundEvents.CONDUIT_DEACTIVATE);
/*     */     }
/*     */     
/* 290 */     this.isActive = debug1;
/*     */   }
/*     */   
/*     */   private void setHunting(boolean debug1) {
/* 294 */     this.isHunting = debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void playSound(SoundEvent debug1) {
/* 302 */     this.level.playSound(null, this.worldPosition, debug1, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\entity\ConduitBlockEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */