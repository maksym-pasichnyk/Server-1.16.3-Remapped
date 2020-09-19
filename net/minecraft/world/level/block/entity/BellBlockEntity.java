/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.FastColor;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import org.apache.commons.lang3.mutable.MutableInt;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BellBlockEntity
/*     */   extends BlockEntity
/*     */   implements TickableBlockEntity
/*     */ {
/*     */   private long lastRingTimestamp;
/*     */   public int ticks;
/*     */   public boolean shaking;
/*     */   public Direction clickDirection;
/*     */   private List<LivingEntity> nearbyEntities;
/*     */   private boolean resonating;
/*     */   private int resonationTicks;
/*     */   
/*     */   public BellBlockEntity() {
/*  44 */     super(BlockEntityType.BELL);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean triggerEvent(int debug1, int debug2) {
/*  49 */     if (debug1 == 1) {
/*  50 */       updateEntities();
/*  51 */       this.resonationTicks = 0;
/*  52 */       this.clickDirection = Direction.from3DDataValue(debug2);
/*  53 */       this.ticks = 0;
/*  54 */       this.shaking = true;
/*  55 */       return true;
/*     */     } 
/*  57 */     return super.triggerEvent(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  62 */     if (this.shaking) {
/*  63 */       this.ticks++;
/*     */     }
/*     */     
/*  66 */     if (this.ticks >= 50) {
/*  67 */       this.shaking = false;
/*  68 */       this.ticks = 0;
/*     */     } 
/*     */     
/*  71 */     if (this.ticks >= 5 && this.resonationTicks == 0 && areRaidersNearby()) {
/*  72 */       this.resonating = true;
/*  73 */       playResonateSound();
/*     */     } 
/*     */     
/*  76 */     if (this.resonating) {
/*  77 */       if (this.resonationTicks < 40) {
/*  78 */         this.resonationTicks++;
/*     */       } else {
/*  80 */         makeRaidersGlow(this.level);
/*  81 */         showBellParticles(this.level);
/*  82 */         this.resonating = false;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private void playResonateSound() {
/*  88 */     this.level.playSound(null, getBlockPos(), SoundEvents.BELL_RESONATE, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public void onHit(Direction debug1) {
/*  92 */     BlockPos debug2 = getBlockPos();
/*     */     
/*  94 */     this.clickDirection = debug1;
/*  95 */     if (this.shaking) {
/*  96 */       this.ticks = 0;
/*     */     } else {
/*  98 */       this.shaking = true;
/*     */     } 
/*     */     
/* 101 */     this.level.blockEvent(debug2, getBlockState().getBlock(), 1, debug1.get3DDataValue());
/*     */   }
/*     */   
/*     */   private void updateEntities() {
/* 105 */     BlockPos debug1 = getBlockPos();
/*     */     
/* 107 */     if (this.level.getGameTime() > this.lastRingTimestamp + 60L || this.nearbyEntities == null) {
/* 108 */       this.lastRingTimestamp = this.level.getGameTime();
/* 109 */       AABB debug2 = (new AABB(debug1)).inflate(48.0D);
/* 110 */       this.nearbyEntities = this.level.getEntitiesOfClass(LivingEntity.class, debug2);
/*     */     } 
/*     */     
/* 113 */     if (!this.level.isClientSide) {
/* 114 */       for (LivingEntity debug3 : this.nearbyEntities) {
/* 115 */         if (!debug3.isAlive() || debug3.removed) {
/*     */           continue;
/*     */         }
/* 118 */         if (debug1.closerThan((Position)debug3.position(), 32.0D)) {
/* 119 */           debug3.getBrain().setMemory(MemoryModuleType.HEARD_BELL_TIME, Long.valueOf(this.level.getGameTime()));
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean areRaidersNearby() {
/* 126 */     BlockPos debug1 = getBlockPos();
/* 127 */     for (LivingEntity debug3 : this.nearbyEntities) {
/* 128 */       if (!debug3.isAlive() || debug3.removed) {
/*     */         continue;
/*     */       }
/* 131 */       if (debug1.closerThan((Position)debug3.position(), 32.0D) && 
/* 132 */         debug3.getType().is((Tag)EntityTypeTags.RAIDERS)) {
/* 133 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 137 */     return false;
/*     */   }
/*     */   
/*     */   private void makeRaidersGlow(Level debug1) {
/* 141 */     if (debug1.isClientSide) {
/*     */       return;
/*     */     }
/*     */     
/* 145 */     this.nearbyEntities.stream()
/* 146 */       .filter(this::isRaiderWithinRange)
/* 147 */       .forEach(this::glow);
/*     */   }
/*     */   
/*     */   private void showBellParticles(Level debug1) {
/* 151 */     if (!debug1.isClientSide) {
/*     */       return;
/*     */     }
/*     */     
/* 155 */     BlockPos debug2 = getBlockPos();
/* 156 */     MutableInt debug3 = new MutableInt(16700985);
/*     */     
/* 158 */     int debug4 = (int)this.nearbyEntities.stream().filter(debug1 -> debug0.closerThan((Position)debug1.position(), 48.0D)).count();
/*     */     
/* 160 */     this.nearbyEntities.stream()
/* 161 */       .filter(this::isRaiderWithinRange)
/* 162 */       .forEach(debug4 -> {
/*     */           float debug5 = 1.0F;
/*     */           float debug6 = Mth.sqrt((debug4.getX() - debug0.getX()) * (debug4.getX() - debug0.getX()) + (debug4.getZ() - debug0.getZ()) * (debug4.getZ() - debug0.getZ()));
/*     */           double debug7 = (debug0.getX() + 0.5F) + (1.0F / debug6) * (debug4.getX() - debug0.getX());
/*     */           double debug9 = (debug0.getZ() + 0.5F) + (1.0F / debug6) * (debug4.getZ() - debug0.getZ());
/*     */           int debug11 = Mth.clamp((debug1 - 21) / -2, 3, 15);
/*     */           for (int debug12 = 0; debug12 < debug11; debug12++) {
/*     */             int debug13 = debug2.addAndGet(5);
/*     */             double debug14 = FastColor.ARGB32.red(debug13) / 255.0D;
/*     */             double debug16 = FastColor.ARGB32.green(debug13) / 255.0D;
/*     */             double debug18 = FastColor.ARGB32.blue(debug13) / 255.0D;
/*     */             debug3.addParticle((ParticleOptions)ParticleTypes.ENTITY_EFFECT, debug7, (debug0.getY() + 0.5F), debug9, debug14, debug16, debug18);
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isRaiderWithinRange(LivingEntity debug1) {
/* 180 */     return (debug1.isAlive() && !debug1.removed && 
/*     */       
/* 182 */       getBlockPos().closerThan((Position)debug1.position(), 48.0D) && debug1
/* 183 */       .getType().is((Tag)EntityTypeTags.RAIDERS));
/*     */   }
/*     */   
/*     */   private void glow(LivingEntity debug1) {
/* 187 */     debug1.addEffect(new MobEffectInstance(MobEffects.GLOWING, 60));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\entity\BellBlockEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */