/*     */ package net.minecraft.world.entity.animal;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgableMob;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FollowParentGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ItemUtils;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.crafting.Ingredient;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class Cow extends Animal {
/*     */   public Cow(EntityType<? extends Cow> debug1, Level debug2) {
/*  35 */     super((EntityType)debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  40 */     this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
/*  41 */     this.goalSelector.addGoal(1, (Goal)new PanicGoal((PathfinderMob)this, 2.0D));
/*  42 */     this.goalSelector.addGoal(2, (Goal)new BreedGoal(this, 1.0D));
/*  43 */     this.goalSelector.addGoal(3, (Goal)new TemptGoal((PathfinderMob)this, 1.25D, Ingredient.of(new ItemLike[] { (ItemLike)Items.WHEAT }, ), false));
/*  44 */     this.goalSelector.addGoal(4, (Goal)new FollowParentGoal(this, 1.25D));
/*  45 */     this.goalSelector.addGoal(5, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 1.0D));
/*  46 */     this.goalSelector.addGoal(6, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 6.0F));
/*  47 */     this.goalSelector.addGoal(7, (Goal)new RandomLookAroundGoal((Mob)this));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  51 */     return Mob.createMobAttributes()
/*  52 */       .add(Attributes.MAX_HEALTH, 10.0D)
/*  53 */       .add(Attributes.MOVEMENT_SPEED, 0.20000000298023224D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/*  58 */     return SoundEvents.COW_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/*  63 */     return SoundEvents.COW_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/*  68 */     return SoundEvents.COW_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos debug1, BlockState debug2) {
/*  73 */     playSound(SoundEvents.COW_STEP, 0.15F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getSoundVolume() {
/*  78 */     return 0.4F;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player debug1, InteractionHand debug2) {
/*  83 */     ItemStack debug3 = debug1.getItemInHand(debug2);
/*  84 */     if (debug3.getItem() == Items.BUCKET && !isBaby()) {
/*  85 */       debug1.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
/*  86 */       ItemStack debug4 = ItemUtils.createFilledResult(debug3, debug1, Items.MILK_BUCKET.getDefaultInstance());
/*  87 */       debug1.setItemInHand(debug2, debug4);
/*  88 */       return InteractionResult.sidedSuccess(this.level.isClientSide);
/*     */     } 
/*  90 */     return super.mobInteract(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public Cow getBreedOffspring(ServerLevel debug1, AgableMob debug2) {
/*  95 */     return (Cow)EntityType.COW.create((Level)debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getStandingEyeHeight(Pose debug1, EntityDimensions debug2) {
/* 100 */     if (isBaby()) {
/* 101 */       return debug2.height * 0.95F;
/*     */     }
/* 103 */     return 1.3F;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\animal\Cow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */