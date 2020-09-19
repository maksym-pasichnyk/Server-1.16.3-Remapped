/*      */ package net.minecraft.world.entity.animal;
/*      */ import java.util.Arrays;
/*      */ import java.util.Comparator;
/*      */ import java.util.EnumSet;
/*      */ import java.util.List;
/*      */ import java.util.Random;
/*      */ import java.util.function.Predicate;
/*      */ import javax.annotation.Nullable;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Vec3i;
/*      */ import net.minecraft.core.particles.ItemParticleOption;
/*      */ import net.minecraft.core.particles.ParticleOptions;
/*      */ import net.minecraft.core.particles.ParticleTypes;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.network.syncher.EntityDataAccessor;
/*      */ import net.minecraft.network.syncher.EntityDataSerializers;
/*      */ import net.minecraft.network.syncher.SynchedEntityData;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.sounds.SoundEvent;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.world.DifficultyInstance;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.InteractionResult;
/*      */ import net.minecraft.world.damagesource.DamageSource;
/*      */ import net.minecraft.world.entity.AgableMob;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntitySelector;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.EquipmentSlot;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.Mob;
/*      */ import net.minecraft.world.entity.MobSpawnType;
/*      */ import net.minecraft.world.entity.PathfinderMob;
/*      */ import net.minecraft.world.entity.SpawnGroupData;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*      */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*      */ import net.minecraft.world.entity.ai.control.MoveControl;
/*      */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*      */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*      */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*      */ import net.minecraft.world.entity.ai.goal.FollowParentGoal;
/*      */ import net.minecraft.world.entity.ai.goal.Goal;
/*      */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*      */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*      */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*      */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*      */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*      */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*      */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*      */ import net.minecraft.world.entity.item.ItemEntity;
/*      */ import net.minecraft.world.entity.monster.Monster;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.item.crafting.Ingredient;
/*      */ import net.minecraft.world.level.BlockGetter;
/*      */ import net.minecraft.world.level.GameRules;
/*      */ import net.minecraft.world.level.ItemLike;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.ServerLevelAccessor;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ 
/*      */ public class Panda extends Animal {
/*   67 */   private static final EntityDataAccessor<Integer> UNHAPPY_COUNTER = SynchedEntityData.defineId(Panda.class, EntityDataSerializers.INT);
/*   68 */   private static final EntityDataAccessor<Integer> SNEEZE_COUNTER = SynchedEntityData.defineId(Panda.class, EntityDataSerializers.INT);
/*   69 */   private static final EntityDataAccessor<Integer> EAT_COUNTER = SynchedEntityData.defineId(Panda.class, EntityDataSerializers.INT);
/*   70 */   private static final EntityDataAccessor<Byte> MAIN_GENE_ID = SynchedEntityData.defineId(Panda.class, EntityDataSerializers.BYTE);
/*   71 */   private static final EntityDataAccessor<Byte> HIDDEN_GENE_ID = SynchedEntityData.defineId(Panda.class, EntityDataSerializers.BYTE);
/*      */   
/*   73 */   private static final EntityDataAccessor<Byte> DATA_ID_FLAGS = SynchedEntityData.defineId(Panda.class, EntityDataSerializers.BYTE);
/*      */   
/*   75 */   private static final TargetingConditions BREED_TARGETING = (new TargetingConditions()).range(8.0D).allowSameTeam().allowInvulnerable();
/*      */   
/*      */   private boolean gotBamboo;
/*      */   
/*      */   private boolean didBite;
/*      */   
/*      */   public int rollCounter;
/*      */   
/*      */   private Vec3 rollDelta;
/*      */   
/*      */   private float sitAmount;
/*      */   
/*      */   private float sitAmountO;
/*      */   
/*      */   private float onBackAmount;
/*      */   
/*      */   private float onBackAmountO;
/*      */   
/*      */   private float rollAmount;
/*      */   
/*      */   private float rollAmountO;
/*      */   
/*      */   private PandaLookAtPlayerGoal lookAtPlayerGoal;
/*      */   private static final Predicate<ItemEntity> PANDA_ITEMS;
/*      */   
/*      */   public Panda(EntityType<? extends Panda> debug1, Level debug2) {
/*  101 */     super((EntityType)debug1, debug2);
/*      */     
/*  103 */     this.moveControl = new PandaMoveControl(this);
/*      */     
/*  105 */     if (!isBaby()) {
/*  106 */       setCanPickUpLoot(true);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canTakeItem(ItemStack debug1) {
/*  112 */     EquipmentSlot debug2 = Mob.getEquipmentSlotForItem(debug1);
/*  113 */     if (!getItemBySlot(debug2).isEmpty()) {
/*  114 */       return false;
/*      */     }
/*  116 */     return (debug2 == EquipmentSlot.MAINHAND && super.canTakeItem(debug1));
/*      */   }
/*      */   
/*      */   public int getUnhappyCounter() {
/*  120 */     return ((Integer)this.entityData.get(UNHAPPY_COUNTER)).intValue();
/*      */   }
/*      */   
/*      */   public void setUnhappyCounter(int debug1) {
/*  124 */     this.entityData.set(UNHAPPY_COUNTER, Integer.valueOf(debug1));
/*      */   }
/*      */   
/*      */   public boolean isSneezing() {
/*  128 */     return getFlag(2);
/*      */   }
/*      */   
/*      */   public boolean isSitting() {
/*  132 */     return getFlag(8);
/*      */   }
/*      */   
/*      */   public void sit(boolean debug1) {
/*  136 */     setFlag(8, debug1);
/*      */   }
/*      */   
/*      */   public boolean isOnBack() {
/*  140 */     return getFlag(16);
/*      */   }
/*      */   
/*      */   public void setOnBack(boolean debug1) {
/*  144 */     setFlag(16, debug1);
/*      */   }
/*      */   
/*      */   public boolean isEating() {
/*  148 */     return (((Integer)this.entityData.get(EAT_COUNTER)).intValue() > 0);
/*      */   }
/*      */   
/*      */   public void eat(boolean debug1) {
/*  152 */     this.entityData.set(EAT_COUNTER, Integer.valueOf(debug1 ? 1 : 0));
/*      */   }
/*      */   
/*      */   private int getEatCounter() {
/*  156 */     return ((Integer)this.entityData.get(EAT_COUNTER)).intValue();
/*      */   }
/*      */   
/*      */   private void setEatCounter(int debug1) {
/*  160 */     this.entityData.set(EAT_COUNTER, Integer.valueOf(debug1));
/*      */   }
/*      */   
/*      */   public void sneeze(boolean debug1) {
/*  164 */     setFlag(2, debug1);
/*      */     
/*  166 */     if (!debug1) {
/*  167 */       setSneezeCounter(0);
/*      */     }
/*      */   }
/*      */   
/*      */   public int getSneezeCounter() {
/*  172 */     return ((Integer)this.entityData.get(SNEEZE_COUNTER)).intValue();
/*      */   }
/*      */   
/*      */   public void setSneezeCounter(int debug1) {
/*  176 */     this.entityData.set(SNEEZE_COUNTER, Integer.valueOf(debug1));
/*      */   }
/*      */   
/*      */   public Gene getMainGene() {
/*  180 */     return Gene.byId(((Byte)this.entityData.get(MAIN_GENE_ID)).byteValue());
/*      */   }
/*      */   
/*      */   public void setMainGene(Gene debug1) {
/*  184 */     if (debug1.getId() > 6) {
/*  185 */       debug1 = Gene.getRandom(this.random);
/*      */     }
/*      */     
/*  188 */     this.entityData.set(MAIN_GENE_ID, Byte.valueOf((byte)debug1.getId()));
/*      */   }
/*      */   
/*      */   public Gene getHiddenGene() {
/*  192 */     return Gene.byId(((Byte)this.entityData.get(HIDDEN_GENE_ID)).byteValue());
/*      */   }
/*      */   
/*      */   public void setHiddenGene(Gene debug1) {
/*  196 */     if (debug1.getId() > 6) {
/*  197 */       debug1 = Gene.getRandom(this.random);
/*      */     }
/*      */     
/*  200 */     this.entityData.set(HIDDEN_GENE_ID, Byte.valueOf((byte)debug1.getId()));
/*      */   }
/*      */   
/*      */   public boolean isRolling() {
/*  204 */     return getFlag(4);
/*      */   }
/*      */   
/*      */   public void roll(boolean debug1) {
/*  208 */     setFlag(4, debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void defineSynchedData() {
/*  213 */     super.defineSynchedData();
/*  214 */     this.entityData.define(UNHAPPY_COUNTER, Integer.valueOf(0));
/*  215 */     this.entityData.define(SNEEZE_COUNTER, Integer.valueOf(0));
/*  216 */     this.entityData.define(MAIN_GENE_ID, Byte.valueOf((byte)0));
/*  217 */     this.entityData.define(HIDDEN_GENE_ID, Byte.valueOf((byte)0));
/*  218 */     this.entityData.define(DATA_ID_FLAGS, Byte.valueOf((byte)0));
/*  219 */     this.entityData.define(EAT_COUNTER, Integer.valueOf(0));
/*      */   }
/*      */   
/*      */   private boolean getFlag(int debug1) {
/*  223 */     return ((((Byte)this.entityData.get(DATA_ID_FLAGS)).byteValue() & debug1) != 0);
/*      */   }
/*      */   
/*      */   private void setFlag(int debug1, boolean debug2) {
/*  227 */     byte debug3 = ((Byte)this.entityData.get(DATA_ID_FLAGS)).byteValue();
/*  228 */     if (debug2) {
/*  229 */       this.entityData.set(DATA_ID_FLAGS, Byte.valueOf((byte)(debug3 | debug1)));
/*      */     } else {
/*  231 */       this.entityData.set(DATA_ID_FLAGS, Byte.valueOf((byte)(debug3 & (debug1 ^ 0xFFFFFFFF))));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void addAdditionalSaveData(CompoundTag debug1) {
/*  237 */     super.addAdditionalSaveData(debug1);
/*      */     
/*  239 */     debug1.putString("MainGene", getMainGene().getName());
/*  240 */     debug1.putString("HiddenGene", getHiddenGene().getName());
/*      */   }
/*      */ 
/*      */   
/*      */   public void readAdditionalSaveData(CompoundTag debug1) {
/*  245 */     super.readAdditionalSaveData(debug1);
/*      */     
/*  247 */     setMainGene(Gene.byName(debug1.getString("MainGene")));
/*  248 */     setHiddenGene(Gene.byName(debug1.getString("HiddenGene")));
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public AgableMob getBreedOffspring(ServerLevel debug1, AgableMob debug2) {
/*  254 */     Panda debug3 = (Panda)EntityType.PANDA.create((Level)debug1);
/*  255 */     if (debug2 instanceof Panda) {
/*  256 */       debug3.setGeneFromParents(this, (Panda)debug2);
/*      */     }
/*      */     
/*  259 */     debug3.setAttributes();
/*      */     
/*  261 */     return debug3;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void registerGoals() {
/*  266 */     this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
/*  267 */     this.goalSelector.addGoal(2, (Goal)new PandaPanicGoal(this, 2.0D));
/*  268 */     this.goalSelector.addGoal(2, (Goal)new PandaBreedGoal(this, 1.0D));
/*  269 */     this.goalSelector.addGoal(3, (Goal)new PandaAttackGoal(this, 1.2000000476837158D, true));
/*  270 */     this.goalSelector.addGoal(4, (Goal)new TemptGoal((PathfinderMob)this, 1.0D, Ingredient.of(new ItemLike[] { (ItemLike)Blocks.BAMBOO.asItem() }, ), false));
/*  271 */     this.goalSelector.addGoal(6, (Goal)new PandaAvoidGoal<>(this, Player.class, 8.0F, 2.0D, 2.0D));
/*  272 */     this.goalSelector.addGoal(6, (Goal)new PandaAvoidGoal<>(this, Monster.class, 4.0F, 2.0D, 2.0D));
/*  273 */     this.goalSelector.addGoal(7, new PandaSitGoal());
/*  274 */     this.goalSelector.addGoal(8, new PandaLieOnBackGoal(this));
/*  275 */     this.goalSelector.addGoal(8, new PandaSneezeGoal(this));
/*  276 */     this.lookAtPlayerGoal = new PandaLookAtPlayerGoal(this, (Class)Player.class, 6.0F);
/*  277 */     this.goalSelector.addGoal(9, (Goal)this.lookAtPlayerGoal);
/*  278 */     this.goalSelector.addGoal(10, (Goal)new RandomLookAroundGoal((Mob)this));
/*  279 */     this.goalSelector.addGoal(12, new PandaRollGoal(this));
/*  280 */     this.goalSelector.addGoal(13, (Goal)new FollowParentGoal(this, 1.25D));
/*  281 */     this.goalSelector.addGoal(14, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 1.0D));
/*      */     
/*  283 */     this.targetSelector.addGoal(1, (Goal)(new PandaHurtByTargetGoal(this, new Class[0])).setAlertOthers(new Class[0]));
/*      */   }
/*      */   
/*      */   public static AttributeSupplier.Builder createAttributes() {
/*  287 */     return Mob.createMobAttributes()
/*  288 */       .add(Attributes.MOVEMENT_SPEED, 0.15000000596046448D)
/*  289 */       .add(Attributes.ATTACK_DAMAGE, 6.0D);
/*      */   }
/*      */ 
/*      */   
/*      */   public enum Gene
/*      */   {
/*  295 */     NORMAL(0, "normal", false),
/*  296 */     LAZY(1, "lazy", false),
/*  297 */     WORRIED(2, "worried", false),
/*  298 */     PLAYFUL(3, "playful", false),
/*  299 */     BROWN(4, "brown", true),
/*  300 */     WEAK(5, "weak", true),
/*  301 */     AGGRESSIVE(6, "aggressive", false); private static final Gene[] BY_ID; private final int id;
/*      */     static {
/*  303 */       BY_ID = (Gene[])Arrays.<Gene>stream(values()).sorted(Comparator.comparingInt(Gene::getId)).toArray(debug0 -> new Gene[debug0]);
/*      */     }
/*      */ 
/*      */     
/*      */     private final String name;
/*      */     private final boolean isRecessive;
/*      */     
/*      */     Gene(int debug3, String debug4, boolean debug5) {
/*  311 */       this.id = debug3;
/*  312 */       this.name = debug4;
/*  313 */       this.isRecessive = debug5;
/*      */     }
/*      */     
/*      */     public int getId() {
/*  317 */       return this.id;
/*      */     }
/*      */     
/*      */     public String getName() {
/*  321 */       return this.name;
/*      */     }
/*      */     
/*      */     public boolean isRecessive() {
/*  325 */       return this.isRecessive;
/*      */     }
/*      */     
/*      */     private static Gene getVariantFromGenes(Gene debug0, Gene debug1) {
/*  329 */       if (debug0.isRecessive()) {
/*  330 */         if (debug0 == debug1) {
/*  331 */           return debug0;
/*      */         }
/*  333 */         return NORMAL;
/*      */       } 
/*      */ 
/*      */       
/*  337 */       return debug0;
/*      */     }
/*      */     
/*      */     public static Gene byId(int debug0) {
/*  341 */       if (debug0 < 0 || debug0 >= BY_ID.length) {
/*  342 */         debug0 = 0;
/*      */       }
/*  344 */       return BY_ID[debug0];
/*      */     }
/*      */     
/*      */     public static Gene byName(String debug0) {
/*  348 */       for (Gene debug4 : values()) {
/*  349 */         if (debug4.name.equals(debug0)) {
/*  350 */           return debug4;
/*      */         }
/*      */       } 
/*      */       
/*  354 */       return NORMAL;
/*      */     }
/*      */     
/*      */     public static Gene getRandom(Random debug0) {
/*  358 */       int debug1 = debug0.nextInt(16);
/*  359 */       if (debug1 == 0) {
/*  360 */         return LAZY;
/*      */       }
/*  362 */       if (debug1 == 1) {
/*  363 */         return WORRIED;
/*      */       }
/*  365 */       if (debug1 == 2) {
/*  366 */         return PLAYFUL;
/*      */       }
/*  368 */       if (debug1 == 4) {
/*  369 */         return AGGRESSIVE;
/*      */       }
/*  371 */       if (debug1 < 9) {
/*  372 */         return WEAK;
/*      */       }
/*  374 */       if (debug1 < 11) {
/*  375 */         return BROWN;
/*      */       }
/*      */       
/*  378 */       return NORMAL;
/*      */     }
/*      */   }
/*      */   
/*      */   public Gene getVariant() {
/*  383 */     return Gene.getVariantFromGenes(getMainGene(), getHiddenGene());
/*      */   }
/*      */   
/*      */   public boolean isLazy() {
/*  387 */     return (getVariant() == Gene.LAZY);
/*      */   }
/*      */   
/*      */   public boolean isWorried() {
/*  391 */     return (getVariant() == Gene.WORRIED);
/*      */   }
/*      */   
/*      */   public boolean isPlayful() {
/*  395 */     return (getVariant() == Gene.PLAYFUL);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isWeak() {
/*  403 */     return (getVariant() == Gene.WEAK);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isAggressive() {
/*  408 */     return (getVariant() == Gene.AGGRESSIVE);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canBeLeashed(Player debug1) {
/*  413 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean doHurtTarget(Entity debug1) {
/*  418 */     playSound(SoundEvents.PANDA_BITE, 1.0F, 1.0F);
/*  419 */     if (!isAggressive()) {
/*  420 */       this.didBite = true;
/*      */     }
/*  422 */     return super.doHurtTarget(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void tick() {
/*  427 */     super.tick();
/*      */     
/*  429 */     if (isWorried()) {
/*  430 */       if (this.level.isThundering() && !isInWater()) {
/*  431 */         sit(true);
/*  432 */         eat(false);
/*  433 */       } else if (!isEating()) {
/*  434 */         sit(false);
/*      */       } 
/*      */     }
/*      */     
/*  438 */     if (getTarget() == null) {
/*  439 */       this.gotBamboo = false;
/*  440 */       this.didBite = false;
/*      */     } 
/*      */     
/*  443 */     if (getUnhappyCounter() > 0) {
/*  444 */       if (getTarget() != null) {
/*  445 */         lookAt((Entity)getTarget(), 90.0F, 90.0F);
/*      */       }
/*      */       
/*  448 */       if (getUnhappyCounter() == 29 || getUnhappyCounter() == 14) {
/*  449 */         playSound(SoundEvents.PANDA_CANT_BREED, 1.0F, 1.0F);
/*      */       }
/*      */       
/*  452 */       setUnhappyCounter(getUnhappyCounter() - 1);
/*      */     } 
/*      */     
/*  455 */     if (isSneezing()) {
/*  456 */       setSneezeCounter(getSneezeCounter() + 1);
/*  457 */       if (getSneezeCounter() > 20) {
/*  458 */         sneeze(false);
/*  459 */         afterSneeze();
/*  460 */       } else if (getSneezeCounter() == 1) {
/*  461 */         playSound(SoundEvents.PANDA_PRE_SNEEZE, 1.0F, 1.0F);
/*      */       } 
/*      */     } 
/*      */     
/*  465 */     if (isRolling()) {
/*  466 */       handleRoll();
/*      */     } else {
/*  468 */       this.rollCounter = 0;
/*      */     } 
/*      */     
/*  471 */     if (isSitting()) {
/*  472 */       this.xRot = 0.0F;
/*      */     }
/*      */     
/*  475 */     updateSitAmount();
/*  476 */     handleEating();
/*  477 */     updateOnBackAnimation();
/*  478 */     updateRollAmount();
/*      */   }
/*      */   
/*      */   public boolean isScared() {
/*  482 */     return (isWorried() && this.level.isThundering());
/*      */   }
/*      */   
/*      */   private void handleEating() {
/*  486 */     if (!isEating() && isSitting() && !isScared() && !getItemBySlot(EquipmentSlot.MAINHAND).isEmpty() && this.random.nextInt(80) == 1) {
/*  487 */       eat(true);
/*  488 */     } else if (getItemBySlot(EquipmentSlot.MAINHAND).isEmpty() || !isSitting()) {
/*  489 */       eat(false);
/*      */     } 
/*      */     
/*  492 */     if (isEating()) {
/*  493 */       addEatingParticles();
/*      */       
/*  495 */       if (!this.level.isClientSide && getEatCounter() > 80 && this.random.nextInt(20) == 1) {
/*  496 */         if (getEatCounter() > 100 && isFoodOrCake(getItemBySlot(EquipmentSlot.MAINHAND))) {
/*      */           
/*  498 */           if (!this.level.isClientSide) {
/*  499 */             setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
/*      */           }
/*      */           
/*  502 */           sit(false);
/*      */         } 
/*  504 */         eat(false);
/*      */         
/*      */         return;
/*      */       } 
/*  508 */       setEatCounter(getEatCounter() + 1);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void addEatingParticles() {
/*  513 */     if (getEatCounter() % 5 == 0) {
/*  514 */       playSound(SoundEvents.PANDA_EAT, 0.5F + 0.5F * this.random.nextInt(2), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
/*      */       
/*  516 */       for (int debug1 = 0; debug1 < 6; debug1++) {
/*  517 */         Vec3 debug2 = new Vec3((this.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, (this.random.nextFloat() - 0.5D) * 0.1D);
/*  518 */         debug2 = debug2.xRot(-this.xRot * 0.017453292F);
/*  519 */         debug2 = debug2.yRot(-this.yRot * 0.017453292F);
/*      */         
/*  521 */         double debug3 = -this.random.nextFloat() * 0.6D - 0.3D;
/*  522 */         Vec3 debug5 = new Vec3((this.random.nextFloat() - 0.5D) * 0.8D, debug3, 1.0D + (this.random.nextFloat() - 0.5D) * 0.4D);
/*  523 */         debug5 = debug5.yRot(-this.yBodyRot * 0.017453292F);
/*      */         
/*  525 */         debug5 = debug5.add(getX(), getEyeY() + 1.0D, getZ());
/*  526 */         this.level.addParticle((ParticleOptions)new ItemParticleOption(ParticleTypes.ITEM, getItemBySlot(EquipmentSlot.MAINHAND)), debug5.x, debug5.y, debug5.z, debug2.x, debug2.y + 0.05D, debug2.z);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void updateSitAmount() {
/*  532 */     this.sitAmountO = this.sitAmount;
/*  533 */     if (isSitting()) {
/*  534 */       this.sitAmount = Math.min(1.0F, this.sitAmount + 0.15F);
/*      */     } else {
/*  536 */       this.sitAmount = Math.max(0.0F, this.sitAmount - 0.19F);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void updateOnBackAnimation() {
/*  541 */     this.onBackAmountO = this.onBackAmount;
/*  542 */     if (isOnBack()) {
/*  543 */       this.onBackAmount = Math.min(1.0F, this.onBackAmount + 0.15F);
/*      */     } else {
/*  545 */       this.onBackAmount = Math.max(0.0F, this.onBackAmount - 0.19F);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void updateRollAmount() {
/*  550 */     this.rollAmountO = this.rollAmount;
/*  551 */     if (isRolling()) {
/*  552 */       this.rollAmount = Math.min(1.0F, this.rollAmount + 0.15F);
/*      */     } else {
/*  554 */       this.rollAmount = Math.max(0.0F, this.rollAmount - 0.19F);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void handleRoll() {
/*  571 */     this.rollCounter++;
/*  572 */     if (this.rollCounter > 32) {
/*  573 */       roll(false);
/*      */       
/*      */       return;
/*      */     } 
/*  577 */     if (!this.level.isClientSide) {
/*  578 */       Vec3 debug1 = getDeltaMovement();
/*  579 */       if (this.rollCounter == 1) {
/*  580 */         float debug2 = this.yRot * 0.017453292F;
/*  581 */         float debug3 = isBaby() ? 0.1F : 0.2F;
/*  582 */         this
/*      */ 
/*      */           
/*  585 */           .rollDelta = new Vec3(debug1.x + (-Mth.sin(debug2) * debug3), 0.0D, debug1.z + (Mth.cos(debug2) * debug3));
/*      */         
/*  587 */         setDeltaMovement(this.rollDelta.add(0.0D, 0.27D, 0.0D));
/*  588 */       } else if (this.rollCounter == 7.0F || this.rollCounter == 15.0F || this.rollCounter == 23.0F) {
/*  589 */         setDeltaMovement(0.0D, this.onGround ? 0.27D : debug1.y, 0.0D);
/*      */       } else {
/*  591 */         setDeltaMovement(this.rollDelta.x, debug1.y, this.rollDelta.z);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void afterSneeze() {
/*  597 */     Vec3 debug1 = getDeltaMovement();
/*  598 */     this.level.addParticle((ParticleOptions)ParticleTypes.SNEEZE, getX() - (getBbWidth() + 1.0F) * 0.5D * Mth.sin(this.yBodyRot * 0.017453292F), getEyeY() - 0.10000000149011612D, getZ() + (getBbWidth() + 1.0F) * 0.5D * Mth.cos(this.yBodyRot * 0.017453292F), debug1.x, 0.0D, debug1.z);
/*  599 */     playSound(SoundEvents.PANDA_SNEEZE, 1.0F, 1.0F);
/*      */ 
/*      */     
/*  602 */     List<Panda> debug2 = this.level.getEntitiesOfClass(Panda.class, getBoundingBox().inflate(10.0D));
/*  603 */     for (Panda debug4 : debug2) {
/*  604 */       if (!debug4.isBaby() && debug4.onGround && !debug4.isInWater() && debug4.canPerformAction()) {
/*  605 */         debug4.jumpFromGround();
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  610 */     if (!this.level.isClientSide() && this.random.nextInt(700) == 0 && this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
/*  611 */       spawnAtLocation((ItemLike)Items.SLIME_BALL);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected void pickUpItem(ItemEntity debug1) {
/*  617 */     if (getItemBySlot(EquipmentSlot.MAINHAND).isEmpty() && PANDA_ITEMS.test(debug1)) {
/*  618 */       onItemPickup(debug1);
/*  619 */       ItemStack debug2 = debug1.getItem();
/*  620 */       setItemSlot(EquipmentSlot.MAINHAND, debug2);
/*  621 */       this.handDropChances[EquipmentSlot.MAINHAND.getIndex()] = 2.0F;
/*  622 */       take((Entity)debug1, debug2.getCount());
/*  623 */       debug1.remove();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hurt(DamageSource debug1, float debug2) {
/*  629 */     sit(false);
/*  630 */     return super.hurt(debug1, debug2);
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor debug1, DifficultyInstance debug2, MobSpawnType debug3, @Nullable SpawnGroupData debug4, @Nullable CompoundTag debug5) {
/*      */     AgableMob.AgableMobGroupData agableMobGroupData;
/*  636 */     setMainGene(Gene.getRandom(this.random));
/*  637 */     setHiddenGene(Gene.getRandom(this.random));
/*      */     
/*  639 */     setAttributes();
/*      */     
/*  641 */     if (debug4 == null) {
/*  642 */       agableMobGroupData = new AgableMob.AgableMobGroupData(0.2F);
/*      */     }
/*      */     
/*  645 */     return super.finalizeSpawn(debug1, debug2, debug3, (SpawnGroupData)agableMobGroupData, debug5);
/*      */   }
/*      */   
/*      */   public void setGeneFromParents(Panda debug1, @Nullable Panda debug2) {
/*  649 */     if (debug2 == null) {
/*  650 */       if (this.random.nextBoolean()) {
/*  651 */         setMainGene(debug1.getOneOfGenesRandomly());
/*  652 */         setHiddenGene(Gene.getRandom(this.random));
/*      */       } else {
/*  654 */         setMainGene(Gene.getRandom(this.random));
/*  655 */         setHiddenGene(debug1.getOneOfGenesRandomly());
/*      */       }
/*      */     
/*  658 */     } else if (this.random.nextBoolean()) {
/*  659 */       setMainGene(debug1.getOneOfGenesRandomly());
/*  660 */       setHiddenGene(debug2.getOneOfGenesRandomly());
/*      */     } else {
/*  662 */       setMainGene(debug2.getOneOfGenesRandomly());
/*  663 */       setHiddenGene(debug1.getOneOfGenesRandomly());
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  668 */     if (this.random.nextInt(32) == 0) {
/*  669 */       setMainGene(Gene.getRandom(this.random));
/*      */     }
/*      */     
/*  672 */     if (this.random.nextInt(32) == 0) {
/*  673 */       setHiddenGene(Gene.getRandom(this.random));
/*      */     }
/*      */   }
/*      */   
/*      */   private Gene getOneOfGenesRandomly() {
/*  678 */     if (this.random.nextBoolean()) {
/*  679 */       return getMainGene();
/*      */     }
/*      */     
/*  682 */     return getHiddenGene();
/*      */   }
/*      */   
/*      */   public void setAttributes() {
/*  686 */     if (isWeak()) {
/*  687 */       getAttribute(Attributes.MAX_HEALTH).setBaseValue(10.0D);
/*      */     }
/*      */     
/*  690 */     if (isLazy()) {
/*  691 */       getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.07000000029802322D);
/*      */     }
/*      */   }
/*      */   
/*      */   private void tryToSit() {
/*  696 */     if (!isInWater()) {
/*  697 */       setZza(0.0F);
/*  698 */       getNavigation().stop();
/*  699 */       sit(true);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public InteractionResult mobInteract(Player debug1, InteractionHand debug2) {
/*  705 */     ItemStack debug3 = debug1.getItemInHand(debug2);
/*      */     
/*  707 */     if (isScared()) {
/*  708 */       return InteractionResult.PASS;
/*      */     }
/*      */     
/*  711 */     if (isOnBack()) {
/*  712 */       setOnBack(false);
/*  713 */       return InteractionResult.sidedSuccess(this.level.isClientSide);
/*      */     } 
/*      */     
/*  716 */     if (isFood(debug3)) {
/*  717 */       if (getTarget() != null) {
/*  718 */         this.gotBamboo = true;
/*      */       }
/*      */       
/*  721 */       if (isBaby()) {
/*  722 */         usePlayerItem(debug1, debug3);
/*  723 */         ageUp((int)((-getAge() / 20) * 0.1F), true);
/*  724 */       } else if (!this.level.isClientSide && getAge() == 0 && canFallInLove()) {
/*  725 */         usePlayerItem(debug1, debug3);
/*  726 */         setInLove(debug1);
/*  727 */       } else if (!this.level.isClientSide && !isSitting() && !isInWater()) {
/*  728 */         tryToSit();
/*  729 */         eat(true);
/*      */         
/*  731 */         ItemStack debug4 = getItemBySlot(EquipmentSlot.MAINHAND);
/*  732 */         if (!debug4.isEmpty() && !debug1.abilities.instabuild) {
/*  733 */           spawnAtLocation(debug4);
/*      */         }
/*  735 */         setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)debug3.getItem(), 1));
/*      */         
/*  737 */         usePlayerItem(debug1, debug3);
/*      */       } else {
/*  739 */         return InteractionResult.PASS;
/*      */       } 
/*      */       
/*  742 */       return InteractionResult.SUCCESS;
/*      */     } 
/*      */     
/*  745 */     return InteractionResult.PASS;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   protected SoundEvent getAmbientSound() {
/*  751 */     if (isAggressive())
/*  752 */       return SoundEvents.PANDA_AGGRESSIVE_AMBIENT; 
/*  753 */     if (isWorried()) {
/*  754 */       return SoundEvents.PANDA_WORRIED_AMBIENT;
/*      */     }
/*  756 */     return SoundEvents.PANDA_AMBIENT;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void playStepSound(BlockPos debug1, BlockState debug2) {
/*  762 */     playSound(SoundEvents.PANDA_STEP, 0.15F, 1.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isFood(ItemStack debug1) {
/*  767 */     return (debug1.getItem() == Blocks.BAMBOO.asItem());
/*      */   }
/*      */   
/*      */   private boolean isFoodOrCake(ItemStack debug1) {
/*  771 */     return (isFood(debug1) || debug1.getItem() == Blocks.CAKE.asItem());
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   protected SoundEvent getDeathSound() {
/*  777 */     return SoundEvents.PANDA_DEATH;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   protected SoundEvent getHurtSound(DamageSource debug1) {
/*  783 */     return SoundEvents.PANDA_HURT;
/*      */   }
/*      */   
/*      */   public boolean canPerformAction() {
/*  787 */     return (!isOnBack() && !isScared() && !isEating() && !isRolling() && !isSitting());
/*      */   }
/*      */   
/*      */   static class PandaMoveControl extends MoveControl {
/*      */     private final Panda panda;
/*      */     
/*      */     public PandaMoveControl(Panda debug1) {
/*  794 */       super((Mob)debug1);
/*  795 */       this.panda = debug1;
/*      */     }
/*      */ 
/*      */     
/*      */     public void tick() {
/*  800 */       if (!this.panda.canPerformAction()) {
/*      */         return;
/*      */       }
/*      */       
/*  804 */       super.tick();
/*      */     }
/*      */   }
/*      */   
/*      */   static class PandaAttackGoal extends MeleeAttackGoal {
/*      */     private final Panda panda;
/*      */     
/*      */     public PandaAttackGoal(Panda debug1, double debug2, boolean debug4) {
/*  812 */       super((PathfinderMob)debug1, debug2, debug4);
/*  813 */       this.panda = debug1;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canUse() {
/*  818 */       return (this.panda.canPerformAction() && super.canUse());
/*      */     }
/*      */   }
/*      */   
/*      */   static class PandaLookAtPlayerGoal extends LookAtPlayerGoal {
/*      */     private final Panda panda;
/*      */     
/*      */     public PandaLookAtPlayerGoal(Panda debug1, Class<? extends LivingEntity> debug2, float debug3) {
/*  826 */       super((Mob)debug1, debug2, debug3);
/*  827 */       this.panda = debug1;
/*      */     }
/*      */     
/*      */     public void setTarget(LivingEntity debug1) {
/*  831 */       this.lookAt = (Entity)debug1;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canContinueToUse() {
/*  836 */       return (this.lookAt != null && super.canContinueToUse());
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canUse() {
/*  841 */       if (this.mob.getRandom().nextFloat() >= this.probability) {
/*  842 */         return false;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  847 */       if (this.lookAt == null) {
/*  848 */         if (this.lookAtType == Player.class) {
/*  849 */           this.lookAt = (Entity)this.mob.level.getNearestPlayer(this.lookAtContext, (LivingEntity)this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
/*      */         } else {
/*  851 */           this.lookAt = (Entity)this.mob.level.getNearestLoadedEntity(this.lookAtType, this.lookAtContext, (LivingEntity)this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ(), this.mob.getBoundingBox().inflate(this.lookDistance, 3.0D, this.lookDistance));
/*      */         } 
/*      */       }
/*      */       
/*  855 */       return (this.panda.canPerformAction() && this.lookAt != null);
/*      */     }
/*      */ 
/*      */     
/*      */     public void tick() {
/*  860 */       if (this.lookAt != null)
/*  861 */         super.tick(); 
/*      */     }
/*      */   }
/*      */   
/*      */   static class PandaRollGoal
/*      */     extends Goal {
/*      */     private final Panda panda;
/*      */     
/*      */     public PandaRollGoal(Panda debug1) {
/*  870 */       this.panda = debug1;
/*  871 */       setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canUse() {
/*  876 */       if ((!this.panda.isBaby() && !this.panda.isPlayful()) || !this.panda.onGround) {
/*  877 */         return false;
/*      */       }
/*      */       
/*  880 */       if (!this.panda.canPerformAction()) {
/*  881 */         return false;
/*      */       }
/*      */       
/*  884 */       float debug1 = this.panda.yRot * 0.017453292F;
/*  885 */       int debug2 = 0;
/*  886 */       int debug3 = 0;
/*  887 */       float debug4 = -Mth.sin(debug1);
/*  888 */       float debug5 = Mth.cos(debug1);
/*  889 */       if (Math.abs(debug4) > 0.5D) {
/*  890 */         debug2 = (int)(debug2 + debug4 / Math.abs(debug4));
/*      */       }
/*      */       
/*  893 */       if (Math.abs(debug5) > 0.5D) {
/*  894 */         debug3 = (int)(debug3 + debug5 / Math.abs(debug5));
/*      */       }
/*      */       
/*  897 */       if (this.panda.level.getBlockState(this.panda.blockPosition().offset(debug2, -1, debug3)).isAir()) {
/*  898 */         return true;
/*      */       }
/*      */       
/*  901 */       if (this.panda.isPlayful() && this.panda.random.nextInt(60) == 1) {
/*  902 */         return true;
/*      */       }
/*      */       
/*  905 */       return (this.panda.random.nextInt(500) == 1);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canContinueToUse() {
/*  910 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/*  915 */       this.panda.roll(true);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isInterruptable() {
/*  920 */       return false;
/*      */     }
/*      */   }
/*      */   
/*      */   static class PandaSneezeGoal
/*      */     extends Goal {
/*      */     private final Panda panda;
/*      */     
/*      */     public PandaSneezeGoal(Panda debug1) {
/*  929 */       this.panda = debug1;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canUse() {
/*  934 */       if (!this.panda.isBaby() || !this.panda.canPerformAction()) {
/*  935 */         return false;
/*      */       }
/*      */       
/*  938 */       if (this.panda.isWeak() && this.panda.random.nextInt(500) == 1) {
/*  939 */         return true;
/*      */       }
/*      */       
/*  942 */       return (this.panda.random.nextInt(6000) == 1);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canContinueToUse() {
/*  947 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/*  952 */       this.panda.sneeze(true);
/*      */     }
/*      */   }
/*      */   
/*      */   class PandaBreedGoal extends BreedGoal {
/*      */     private final Panda panda;
/*      */     private int unhappyCooldown;
/*      */     
/*      */     public PandaBreedGoal(Panda debug2, double debug3) {
/*  961 */       super(debug2, debug3);
/*  962 */       this.panda = debug2;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canUse() {
/*  967 */       if (super.canUse() && this.panda.getUnhappyCounter() == 0) {
/*  968 */         if (!canFindBamboo()) {
/*  969 */           if (this.unhappyCooldown <= this.panda.tickCount) {
/*  970 */             this.panda.setUnhappyCounter(32);
/*  971 */             this.unhappyCooldown = this.panda.tickCount + 600;
/*  972 */             if (this.panda.isEffectiveAi()) {
/*  973 */               Player debug1 = this.level.getNearestPlayer(Panda.BREED_TARGETING, (LivingEntity)this.panda);
/*  974 */               this.panda.lookAtPlayerGoal.setTarget((LivingEntity)debug1);
/*      */             } 
/*      */           } 
/*      */           
/*  978 */           return false;
/*      */         } 
/*      */         
/*  981 */         return true;
/*      */       } 
/*      */       
/*  984 */       return false;
/*      */     }
/*      */     
/*      */     private boolean canFindBamboo() {
/*  988 */       BlockPos debug1 = this.panda.blockPosition();
/*  989 */       BlockPos.MutableBlockPos debug2 = new BlockPos.MutableBlockPos();
/*  990 */       for (int debug3 = 0; debug3 < 3; debug3++) {
/*  991 */         for (int debug4 = 0; debug4 < 8; debug4++) {
/*  992 */           int debug5; for (debug5 = 0; debug5 <= debug4; debug5 = (debug5 > 0) ? -debug5 : (1 - debug5)) {
/*  993 */             int debug6 = (debug5 < debug4 && debug5 > -debug4) ? debug4 : 0;
/*  994 */             for (; debug6 <= debug4; debug6 = (debug6 > 0) ? -debug6 : (1 - debug6)) {
/*  995 */               debug2.setWithOffset((Vec3i)debug1, debug5, debug3, debug6);
/*  996 */               if (this.level.getBlockState((BlockPos)debug2).is(Blocks.BAMBOO)) {
/*  997 */                 return true;
/*      */               }
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/* 1003 */       return false;
/*      */     }
/*      */   }
/*      */   
/*      */   static class PandaAvoidGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
/*      */     private final Panda panda;
/*      */     
/*      */     public PandaAvoidGoal(Panda debug1, Class<T> debug2, float debug3, double debug4, double debug6) {
/* 1011 */       super((PathfinderMob)debug1, debug2, debug3, debug4, debug6, EntitySelector.NO_SPECTATORS::test);
/*      */       
/* 1013 */       this.panda = debug1;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canUse() {
/* 1018 */       return (this.panda.isWorried() && this.panda.canPerformAction() && super.canUse());
/*      */     } }
/*      */   
/*      */   static {
/* 1022 */     PANDA_ITEMS = (debug0 -> {
/*      */         Item debug1 = debug0.getItem().getItem();
/* 1024 */         return ((debug1 == Blocks.BAMBOO.asItem() || debug1 == Blocks.CAKE.asItem()) && debug0.isAlive() && !debug0.hasPickUpDelay());
/*      */       });
/*      */   }
/*      */   
/*      */   class PandaSitGoal extends Goal { private int cooldown;
/*      */     
/*      */     public PandaSitGoal() {
/* 1031 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canUse() {
/* 1036 */       if (this.cooldown > Panda.this.tickCount || Panda.this.isBaby() || Panda.this.isInWater() || !Panda.this.canPerformAction() || Panda.this.getUnhappyCounter() > 0) {
/* 1037 */         return false;
/*      */       }
/*      */       
/* 1040 */       List<ItemEntity> debug1 = Panda.this.level.getEntitiesOfClass(ItemEntity.class, Panda.this.getBoundingBox().inflate(6.0D, 6.0D, 6.0D), Panda.PANDA_ITEMS);
/* 1041 */       return (!debug1.isEmpty() || !Panda.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty());
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canContinueToUse() {
/* 1046 */       if (Panda.this.isInWater() || (!Panda.this.isLazy() && Panda.this.random.nextInt(600) == 1)) {
/* 1047 */         return false;
/*      */       }
/*      */       
/* 1050 */       if (Panda.this.random.nextInt(2000) == 1) {
/* 1051 */         return false;
/*      */       }
/*      */       
/* 1054 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public void tick() {
/* 1059 */       if (!Panda.this.isSitting() && !Panda.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
/* 1060 */         Panda.this.tryToSit();
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/* 1066 */       List<ItemEntity> debug1 = Panda.this.level.getEntitiesOfClass(ItemEntity.class, Panda.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Panda.PANDA_ITEMS);
/* 1067 */       if (!debug1.isEmpty() && Panda.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
/* 1068 */         Panda.this.getNavigation().moveTo((Entity)debug1.get(0), 1.2000000476837158D);
/* 1069 */       } else if (!Panda.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
/* 1070 */         Panda.this.tryToSit();
/*      */       } 
/*      */       
/* 1073 */       this.cooldown = 0;
/*      */     }
/*      */ 
/*      */     
/*      */     public void stop() {
/* 1078 */       ItemStack debug1 = Panda.this.getItemBySlot(EquipmentSlot.MAINHAND);
/* 1079 */       if (!debug1.isEmpty()) {
/* 1080 */         Panda.this.spawnAtLocation(debug1);
/* 1081 */         Panda.this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
/* 1082 */         int debug2 = Panda.this.isLazy() ? (Panda.this.random.nextInt(50) + 10) : (Panda.this.random.nextInt(150) + 10);
/* 1083 */         this.cooldown = Panda.this.tickCount + debug2 * 20;
/*      */       } 
/*      */       
/* 1086 */       Panda.this.sit(false);
/*      */     } }
/*      */ 
/*      */   
/*      */   static class PandaLieOnBackGoal extends Goal {
/*      */     private final Panda panda;
/*      */     private int cooldown;
/*      */     
/*      */     public PandaLieOnBackGoal(Panda debug1) {
/* 1095 */       this.panda = debug1;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canUse() {
/* 1100 */       return (this.cooldown < this.panda.tickCount && this.panda.isLazy() && this.panda.canPerformAction() && this.panda.random.nextInt(400) == 1);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canContinueToUse() {
/* 1105 */       if (this.panda.isInWater() || (!this.panda.isLazy() && this.panda.random.nextInt(600) == 1)) {
/* 1106 */         return false;
/*      */       }
/*      */       
/* 1109 */       if (this.panda.random.nextInt(2000) == 1) {
/* 1110 */         return false;
/*      */       }
/*      */       
/* 1113 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/* 1118 */       this.panda.setOnBack(true);
/* 1119 */       this.cooldown = 0;
/*      */     }
/*      */ 
/*      */     
/*      */     public void stop() {
/* 1124 */       this.panda.setOnBack(false);
/* 1125 */       this.cooldown = this.panda.tickCount + 200;
/*      */     }
/*      */   }
/*      */   
/*      */   static class PandaHurtByTargetGoal extends HurtByTargetGoal {
/*      */     private final Panda panda;
/*      */     
/*      */     public PandaHurtByTargetGoal(Panda debug1, Class<?>... debug2) {
/* 1133 */       super((PathfinderMob)debug1, debug2);
/* 1134 */       this.panda = debug1;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canContinueToUse() {
/* 1139 */       if (this.panda.gotBamboo || this.panda.didBite) {
/* 1140 */         this.panda.setTarget(null);
/* 1141 */         return false;
/*      */       } 
/* 1143 */       return super.canContinueToUse();
/*      */     }
/*      */ 
/*      */     
/*      */     protected void alertOther(Mob debug1, LivingEntity debug2) {
/* 1148 */       if (debug1 instanceof Panda && ((Panda)debug1).isAggressive())
/* 1149 */         debug1.setTarget(debug2); 
/*      */     }
/*      */   }
/*      */   
/*      */   static class PandaPanicGoal
/*      */     extends PanicGoal {
/*      */     private final Panda panda;
/*      */     
/*      */     public PandaPanicGoal(Panda debug1, double debug2) {
/* 1158 */       super((PathfinderMob)debug1, debug2);
/* 1159 */       this.panda = debug1;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean canUse() {
/* 1165 */       if (!this.panda.isOnFire()) {
/* 1166 */         return false;
/*      */       }
/*      */       
/* 1169 */       BlockPos debug1 = lookForWater((BlockGetter)this.mob.level, (Entity)this.mob, 5, 4);
/* 1170 */       if (debug1 != null) {
/* 1171 */         this.posX = debug1.getX();
/* 1172 */         this.posY = debug1.getY();
/* 1173 */         this.posZ = debug1.getZ();
/*      */         
/* 1175 */         return true;
/*      */       } 
/*      */       
/* 1178 */       return findRandomPosition();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canContinueToUse() {
/* 1183 */       if (this.panda.isSitting()) {
/* 1184 */         this.panda.getNavigation().stop();
/* 1185 */         return false;
/*      */       } 
/* 1187 */       return super.canContinueToUse();
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\animal\Panda.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */