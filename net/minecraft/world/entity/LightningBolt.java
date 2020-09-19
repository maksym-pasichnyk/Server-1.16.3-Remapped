/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.BaseFireBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ public class LightningBolt
/*     */   extends Entity {
/*     */   private int life;
/*     */   public long seed;
/*     */   private int flashes;
/*     */   private boolean visualOnly;
/*     */   @Nullable
/*     */   private ServerPlayer cause;
/*     */   
/*     */   public LightningBolt(EntityType<? extends LightningBolt> debug1, Level debug2) {
/*  33 */     super(debug1, debug2);
/*     */     
/*  35 */     this.noCulling = true;
/*  36 */     this.life = 2;
/*  37 */     this.seed = this.random.nextLong();
/*  38 */     this.flashes = this.random.nextInt(3) + 1;
/*     */   }
/*     */   
/*     */   public void setVisualOnly(boolean debug1) {
/*  42 */     this.visualOnly = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundSource getSoundSource() {
/*  47 */     return SoundSource.WEATHER;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCause(@Nullable ServerPlayer debug1) {
/*  56 */     this.cause = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  61 */     super.tick();
/*     */     
/*  63 */     if (this.life == 2) {
/*  64 */       Difficulty debug1 = this.level.getDifficulty();
/*  65 */       if (debug1 == Difficulty.NORMAL || debug1 == Difficulty.HARD) {
/*  66 */         spawnFire(4);
/*     */       }
/*     */       
/*  69 */       this.level.playSound(null, getX(), getY(), getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.WEATHER, 10000.0F, 0.8F + this.random.nextFloat() * 0.2F);
/*  70 */       this.level.playSound(null, getX(), getY(), getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.WEATHER, 2.0F, 0.5F + this.random.nextFloat() * 0.2F);
/*     */     } 
/*     */     
/*  73 */     this.life--;
/*  74 */     if (this.life < 0) {
/*  75 */       if (this.flashes == 0) {
/*  76 */         remove();
/*  77 */       } else if (this.life < -this.random.nextInt(10)) {
/*  78 */         this.flashes--;
/*  79 */         this.life = 1;
/*  80 */         this.seed = this.random.nextLong();
/*  81 */         spawnFire(0);
/*     */       } 
/*     */     }
/*     */     
/*  85 */     if (this.life >= 0) {
/*  86 */       if (!(this.level instanceof ServerLevel)) {
/*  87 */         this.level.setSkyFlashTime(2);
/*  88 */       } else if (!this.visualOnly) {
/*  89 */         double debug1 = 3.0D;
/*  90 */         List<Entity> debug3 = this.level.getEntities(this, new AABB(getX() - 3.0D, getY() - 3.0D, getZ() - 3.0D, getX() + 3.0D, getY() + 6.0D + 3.0D, getZ() + 3.0D), Entity::isAlive);
/*  91 */         for (Entity debug5 : debug3) {
/*  92 */           debug5.thunderHit((ServerLevel)this.level, this);
/*     */         }
/*  94 */         if (this.cause != null) {
/*  95 */           CriteriaTriggers.CHANNELED_LIGHTNING.trigger(this.cause, debug3);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private void spawnFire(int debug1) {
/* 102 */     if (this.visualOnly || this.level.isClientSide || !this.level.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {
/*     */       return;
/*     */     }
/*     */     
/* 106 */     BlockPos debug2 = blockPosition();
/* 107 */     BlockState debug3 = BaseFireBlock.getState((BlockGetter)this.level, debug2);
/*     */     
/* 109 */     if (this.level.getBlockState(debug2).isAir() && debug3.canSurvive((LevelReader)this.level, debug2)) {
/* 110 */       this.level.setBlockAndUpdate(debug2, debug3);
/*     */     }
/*     */     
/* 113 */     for (int debug4 = 0; debug4 < debug1; debug4++) {
/* 114 */       BlockPos debug5 = debug2.offset(this.random.nextInt(3) - 1, this.random.nextInt(3) - 1, this.random.nextInt(3) - 1);
/* 115 */       debug3 = BaseFireBlock.getState((BlockGetter)this.level, debug5);
/* 116 */       if (this.level.getBlockState(debug5).isAir() && debug3.canSurvive((LevelReader)this.level, debug5)) {
/* 117 */         this.level.setBlockAndUpdate(debug5, debug3);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(CompoundTag debug1) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(CompoundTag debug1) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Packet<?> getAddEntityPacket() {
/* 143 */     return (Packet<?>)new ClientboundAddEntityPacket(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\LightningBolt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */