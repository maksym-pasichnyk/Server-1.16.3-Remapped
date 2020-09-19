/*    */ package net.minecraft.world.entity.monster;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.effect.MobEffects;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*    */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class ElderGuardian extends Guardian {
/* 21 */   public static final float ELDER_SIZE_SCALE = EntityType.ELDER_GUARDIAN.getWidth() / EntityType.GUARDIAN.getWidth();
/*    */   
/*    */   public ElderGuardian(EntityType<? extends ElderGuardian> debug1, Level debug2) {
/* 24 */     super((EntityType)debug1, debug2);
/*    */     
/* 26 */     setPersistenceRequired();
/*    */ 
/*    */     
/* 29 */     if (this.randomStrollGoal != null) {
/* 30 */       this.randomStrollGoal.setInterval(400);
/*    */     }
/*    */   }
/*    */   
/*    */   public static AttributeSupplier.Builder createAttributes() {
/* 35 */     return Guardian.createAttributes()
/* 36 */       .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D)
/* 37 */       .add(Attributes.ATTACK_DAMAGE, 8.0D)
/* 38 */       .add(Attributes.MAX_HEALTH, 80.0D);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getAttackDuration() {
/* 43 */     return 60;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getAmbientSound() {
/* 48 */     return isInWaterOrBubble() ? SoundEvents.ELDER_GUARDIAN_AMBIENT : SoundEvents.ELDER_GUARDIAN_AMBIENT_LAND;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 53 */     return isInWaterOrBubble() ? SoundEvents.ELDER_GUARDIAN_HURT : SoundEvents.ELDER_GUARDIAN_HURT_LAND;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getDeathSound() {
/* 58 */     return isInWaterOrBubble() ? SoundEvents.ELDER_GUARDIAN_DEATH : SoundEvents.ELDER_GUARDIAN_DEATH_LAND;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getFlopSound() {
/* 63 */     return SoundEvents.ELDER_GUARDIAN_FLOP;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void customServerAiStep() {
/* 68 */     super.customServerAiStep();
/*    */ 
/*    */     
/* 71 */     int debug1 = 1200;
/* 72 */     if ((this.tickCount + getId()) % 1200 == 0) {
/* 73 */       MobEffect debug2 = MobEffects.DIG_SLOWDOWN;
/*    */       
/* 75 */       List<ServerPlayer> debug3 = ((ServerLevel)this.level).getPlayers(debug1 -> (distanceToSqr((Entity)debug1) < 2500.0D && debug1.gameMode.isSurvival()));
/*    */       
/* 77 */       int debug4 = 2;
/* 78 */       int debug5 = 6000;
/* 79 */       int debug6 = 1200;
/*    */       
/* 81 */       for (ServerPlayer debug8 : debug3) {
/* 82 */         if (!debug8.hasEffect(debug2) || debug8.getEffect(debug2).getAmplifier() < 2 || debug8.getEffect(debug2).getDuration() < 1200) {
/* 83 */           debug8.connection.send((Packet)new ClientboundGameEventPacket(ClientboundGameEventPacket.GUARDIAN_ELDER_EFFECT, isSilent() ? 0.0F : 1.0F));
/* 84 */           debug8.addEffect(new MobEffectInstance(debug2, 6000, 2));
/*    */         } 
/*    */       } 
/*    */     } 
/*    */ 
/*    */     
/* 90 */     if (!hasRestriction())
/* 91 */       restrictTo(blockPosition(), 16); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\ElderGuardian.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */