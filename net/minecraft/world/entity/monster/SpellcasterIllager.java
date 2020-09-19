/*     */ package net.minecraft.world.entity.monster;
/*     */ import java.util.EnumSet;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public abstract class SpellcasterIllager extends AbstractIllager {
/*  20 */   private static final EntityDataAccessor<Byte> DATA_SPELL_CASTING_ID = SynchedEntityData.defineId(SpellcasterIllager.class, EntityDataSerializers.BYTE);
/*     */   
/*     */   protected int spellCastingTickCount;
/*  23 */   private IllagerSpell currentSpell = IllagerSpell.NONE;
/*     */   
/*     */   protected SpellcasterIllager(EntityType<? extends SpellcasterIllager> debug1, Level debug2) {
/*  26 */     super((EntityType)debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/*  31 */     super.defineSynchedData();
/*     */     
/*  33 */     this.entityData.define(DATA_SPELL_CASTING_ID, Byte.valueOf((byte)0));
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/*  38 */     super.readAdditionalSaveData(debug1);
/*     */     
/*  40 */     this.spellCastingTickCount = debug1.getInt("SpellTicks");
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/*  45 */     super.addAdditionalSaveData(debug1);
/*     */     
/*  47 */     debug1.putInt("SpellTicks", this.spellCastingTickCount);
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
/*     */   public boolean isCastingSpell() {
/*  61 */     if (this.level.isClientSide) {
/*  62 */       return (((Byte)this.entityData.get(DATA_SPELL_CASTING_ID)).byteValue() > 0);
/*     */     }
/*  64 */     return (this.spellCastingTickCount > 0);
/*     */   }
/*     */   
/*     */   public void setIsCastingSpell(IllagerSpell debug1) {
/*  68 */     this.currentSpell = debug1;
/*  69 */     this.entityData.set(DATA_SPELL_CASTING_ID, Byte.valueOf((byte)debug1.id));
/*     */   }
/*     */   
/*     */   protected IllagerSpell getCurrentSpell() {
/*  73 */     if (!this.level.isClientSide) {
/*  74 */       return this.currentSpell;
/*     */     }
/*  76 */     return IllagerSpell.byId(((Byte)this.entityData.get(DATA_SPELL_CASTING_ID)).byteValue());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep() {
/*  81 */     super.customServerAiStep();
/*     */     
/*  83 */     if (this.spellCastingTickCount > 0) {
/*  84 */       this.spellCastingTickCount--;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  90 */     super.tick();
/*     */     
/*  92 */     if (this.level.isClientSide && isCastingSpell()) {
/*  93 */       IllagerSpell debug1 = getCurrentSpell();
/*  94 */       double debug2 = debug1.spellColor[0];
/*  95 */       double debug4 = debug1.spellColor[1];
/*  96 */       double debug6 = debug1.spellColor[2];
/*     */ 
/*     */       
/*  99 */       float debug8 = this.yBodyRot * 0.017453292F + Mth.cos(this.tickCount * 0.6662F) * 0.25F;
/* 100 */       float debug9 = Mth.cos(debug8);
/* 101 */       float debug10 = Mth.sin(debug8);
/*     */       
/* 103 */       this.level.addParticle((ParticleOptions)ParticleTypes.ENTITY_EFFECT, getX() + debug9 * 0.6D, getY() + 1.8D, getZ() + debug10 * 0.6D, debug2, debug4, debug6);
/* 104 */       this.level.addParticle((ParticleOptions)ParticleTypes.ENTITY_EFFECT, getX() - debug9 * 0.6D, getY() + 1.8D, getZ() - debug10 * 0.6D, debug2, debug4, debug6);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected int getSpellCastingTime() {
/* 109 */     return this.spellCastingTickCount;
/*     */   }
/*     */   
/*     */   protected abstract SoundEvent getCastingSoundEvent();
/*     */   
/*     */   public class SpellcasterCastingSpellGoal extends Goal {
/*     */     public SpellcasterCastingSpellGoal() {
/* 116 */       setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 121 */       return (SpellcasterIllager.this.getSpellCastingTime() > 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 126 */       super.start();
/* 127 */       SpellcasterIllager.this.navigation.stop();
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 132 */       super.stop();
/* 133 */       SpellcasterIllager.this.setIsCastingSpell(SpellcasterIllager.IllagerSpell.NONE);
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 138 */       if (SpellcasterIllager.this.getTarget() != null)
/* 139 */         SpellcasterIllager.this.getLookControl().setLookAt((Entity)SpellcasterIllager.this.getTarget(), SpellcasterIllager.this.getMaxHeadYRot(), SpellcasterIllager.this.getMaxHeadXRot()); 
/*     */     }
/*     */   }
/*     */   
/*     */   public abstract class SpellcasterUseSpellGoal
/*     */     extends Goal
/*     */   {
/*     */     protected int attackWarmupDelay;
/*     */     protected int nextAttackTickCount;
/*     */     
/*     */     public boolean canUse() {
/* 150 */       LivingEntity debug1 = SpellcasterIllager.this.getTarget();
/* 151 */       if (debug1 == null || !debug1.isAlive()) {
/* 152 */         return false;
/*     */       }
/* 154 */       if (SpellcasterIllager.this.isCastingSpell())
/*     */       {
/* 156 */         return false;
/*     */       }
/* 158 */       if (SpellcasterIllager.this.tickCount < this.nextAttackTickCount) {
/* 159 */         return false;
/*     */       }
/* 161 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 166 */       LivingEntity debug1 = SpellcasterIllager.this.getTarget();
/* 167 */       return (debug1 != null && debug1.isAlive() && this.attackWarmupDelay > 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 172 */       this.attackWarmupDelay = getCastWarmupTime();
/* 173 */       SpellcasterIllager.this.spellCastingTickCount = getCastingTime();
/* 174 */       this.nextAttackTickCount = SpellcasterIllager.this.tickCount + getCastingInterval();
/* 175 */       SoundEvent debug1 = getSpellPrepareSound();
/* 176 */       if (debug1 != null) {
/* 177 */         SpellcasterIllager.this.playSound(debug1, 1.0F, 1.0F);
/*     */       }
/* 179 */       SpellcasterIllager.this.setIsCastingSpell(getSpell());
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 184 */       this.attackWarmupDelay--;
/* 185 */       if (this.attackWarmupDelay == 0) {
/* 186 */         performSpellCasting();
/* 187 */         SpellcasterIllager.this.playSound(SpellcasterIllager.this.getCastingSoundEvent(), 1.0F, 1.0F);
/*     */       } 
/*     */     }
/*     */     
/*     */     protected abstract void performSpellCasting();
/*     */     
/*     */     protected int getCastWarmupTime() {
/* 194 */       return 20;
/*     */     }
/*     */     
/*     */     protected abstract int getCastingTime();
/*     */     
/*     */     protected abstract int getCastingInterval();
/*     */     
/*     */     @Nullable
/*     */     protected abstract SoundEvent getSpellPrepareSound();
/*     */     
/*     */     protected abstract SpellcasterIllager.IllagerSpell getSpell();
/*     */   }
/*     */   
/*     */   public enum IllagerSpell {
/* 208 */     NONE(0, 0.0D, 0.0D, 0.0D),
/* 209 */     SUMMON_VEX(1, 0.7D, 0.7D, 0.8D),
/* 210 */     FANGS(2, 0.4D, 0.3D, 0.35D),
/* 211 */     WOLOLO(3, 0.7D, 0.5D, 0.2D),
/* 212 */     DISAPPEAR(4, 0.3D, 0.3D, 0.8D),
/* 213 */     BLINDNESS(5, 0.1D, 0.1D, 0.2D);
/*     */     
/*     */     private final int id;
/*     */     
/*     */     private final double[] spellColor;
/*     */     
/*     */     IllagerSpell(int debug3, double debug4, double debug6, double debug8) {
/* 220 */       this.id = debug3;
/* 221 */       this.spellColor = new double[] { debug4, debug6, debug8 };
/*     */     }
/*     */     
/*     */     public static IllagerSpell byId(int debug0) {
/* 225 */       for (IllagerSpell debug4 : values()) {
/* 226 */         if (debug0 == debug4.id) {
/* 227 */           return debug4;
/*     */         }
/*     */       } 
/* 230 */       return NONE;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\SpellcasterIllager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */