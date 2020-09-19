/*     */ package net.minecraft.world.entity.animal.horse;
/*     */ 
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgableMob;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.MobType;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class ZombieHorse extends AbstractHorse {
/*     */   public ZombieHorse(EntityType<? extends ZombieHorse> debug1, Level debug2) {
/*  23 */     super((EntityType)debug1, debug2);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  27 */     return createBaseHorseAttributes()
/*  28 */       .add(Attributes.MAX_HEALTH, 15.0D)
/*  29 */       .add(Attributes.MOVEMENT_SPEED, 0.20000000298023224D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomizeAttributes() {
/*  34 */     getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(generateRandomJumpStrength());
/*     */   }
/*     */ 
/*     */   
/*     */   public MobType getMobType() {
/*  39 */     return MobType.UNDEAD;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/*  44 */     super.getAmbientSound();
/*  45 */     return SoundEvents.ZOMBIE_HORSE_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/*  50 */     super.getDeathSound();
/*  51 */     return SoundEvents.ZOMBIE_HORSE_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/*  56 */     super.getHurtSound(debug1);
/*  57 */     return SoundEvents.ZOMBIE_HORSE_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public AgableMob getBreedOffspring(ServerLevel debug1, AgableMob debug2) {
/*  63 */     return (AgableMob)EntityType.ZOMBIE_HORSE.create((Level)debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player debug1, InteractionHand debug2) {
/*  69 */     ItemStack debug3 = debug1.getItemInHand(debug2);
/*     */     
/*  71 */     if (!isTamed()) {
/*  72 */       return InteractionResult.PASS;
/*     */     }
/*     */     
/*  75 */     if (isBaby()) {
/*  76 */       return super.mobInteract(debug1, debug2);
/*     */     }
/*     */     
/*  79 */     if (debug1.isSecondaryUseActive()) {
/*  80 */       openInventory(debug1);
/*  81 */       return InteractionResult.sidedSuccess(this.level.isClientSide);
/*     */     } 
/*     */     
/*  84 */     if (isVehicle()) {
/*  85 */       return super.mobInteract(debug1, debug2);
/*     */     }
/*     */     
/*  88 */     if (!debug3.isEmpty()) {
/*  89 */       if (debug3.getItem() == Items.SADDLE && !isSaddled()) {
/*  90 */         openInventory(debug1);
/*  91 */         return InteractionResult.sidedSuccess(this.level.isClientSide);
/*     */       } 
/*     */ 
/*     */       
/*  95 */       InteractionResult debug4 = debug3.interactLivingEntity(debug1, (LivingEntity)this, debug2);
/*  96 */       if (debug4.consumesAction()) {
/*  97 */         return debug4;
/*     */       }
/*     */     } 
/*     */     
/* 101 */     doPlayerRide(debug1);
/* 102 */     return InteractionResult.sidedSuccess(this.level.isClientSide);
/*     */   }
/*     */   
/*     */   protected void addBehaviourGoals() {}
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\animal\horse\ZombieHorse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */