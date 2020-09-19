/*     */ package net.minecraft.world.entity.animal;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Arrays;
/*     */ import java.util.EnumMap;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.Shearable;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*     */ import net.minecraft.world.entity.ai.goal.EatBlockGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FollowParentGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.CraftingContainer;
/*     */ import net.minecraft.world.inventory.MenuType;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.DyeItem;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.crafting.CraftingRecipe;
/*     */ import net.minecraft.world.item.crafting.Ingredient;
/*     */ import net.minecraft.world.item.crafting.RecipeType;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ 
/*     */ public class Sheep extends Animal implements Shearable {
/*  67 */   private static final EntityDataAccessor<Byte> DATA_WOOL_ID = SynchedEntityData.defineId(Sheep.class, EntityDataSerializers.BYTE); private static final Map<DyeColor, ItemLike> ITEM_BY_DYE; private static final Map<DyeColor, float[]> COLORARRAY_BY_COLOR;
/*     */   static {
/*  69 */     ITEM_BY_DYE = (Map<DyeColor, ItemLike>)Util.make(Maps.newEnumMap(DyeColor.class), debug0 -> {
/*     */           debug0.put(DyeColor.WHITE, Blocks.WHITE_WOOL);
/*     */           
/*     */           debug0.put(DyeColor.ORANGE, Blocks.ORANGE_WOOL);
/*     */           debug0.put(DyeColor.MAGENTA, Blocks.MAGENTA_WOOL);
/*     */           debug0.put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_WOOL);
/*     */           debug0.put(DyeColor.YELLOW, Blocks.YELLOW_WOOL);
/*     */           debug0.put(DyeColor.LIME, Blocks.LIME_WOOL);
/*     */           debug0.put(DyeColor.PINK, Blocks.PINK_WOOL);
/*     */           debug0.put(DyeColor.GRAY, Blocks.GRAY_WOOL);
/*     */           debug0.put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_WOOL);
/*     */           debug0.put(DyeColor.CYAN, Blocks.CYAN_WOOL);
/*     */           debug0.put(DyeColor.PURPLE, Blocks.PURPLE_WOOL);
/*     */           debug0.put(DyeColor.BLUE, Blocks.BLUE_WOOL);
/*     */           debug0.put(DyeColor.BROWN, Blocks.BROWN_WOOL);
/*     */           debug0.put(DyeColor.GREEN, Blocks.GREEN_WOOL);
/*     */           debug0.put(DyeColor.RED, Blocks.RED_WOOL);
/*     */           debug0.put(DyeColor.BLACK, Blocks.BLACK_WOOL);
/*     */         });
/*  88 */     COLORARRAY_BY_COLOR = Maps.newEnumMap((Map)Arrays.<DyeColor>stream(DyeColor.values()).collect(Collectors.toMap(debug0 -> debug0, Sheep::createSheepColor)));
/*     */   } private int eatAnimationTick; private EatBlockGoal eatBlockGoal;
/*     */   private static float[] createSheepColor(DyeColor debug0) {
/*  91 */     if (debug0 == DyeColor.WHITE) {
/*  92 */       return new float[] { 0.9019608F, 0.9019608F, 0.9019608F };
/*     */     }
/*  94 */     float[] debug1 = debug0.getTextureDiffuseColors();
/*     */ 
/*     */     
/*  97 */     float debug2 = 0.75F;
/*     */     
/*  99 */     return new float[] { debug1[0] * 0.75F, debug1[1] * 0.75F, debug1[2] * 0.75F };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Sheep(EntityType<? extends Sheep> debug1, Level debug2) {
/* 110 */     super((EntityType)debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/* 115 */     this.eatBlockGoal = new EatBlockGoal((Mob)this);
/* 116 */     this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
/* 117 */     this.goalSelector.addGoal(1, (Goal)new PanicGoal((PathfinderMob)this, 1.25D));
/* 118 */     this.goalSelector.addGoal(2, (Goal)new BreedGoal(this, 1.0D));
/* 119 */     this.goalSelector.addGoal(3, (Goal)new TemptGoal((PathfinderMob)this, 1.1D, Ingredient.of(new ItemLike[] { (ItemLike)Items.WHEAT }, ), false));
/* 120 */     this.goalSelector.addGoal(4, (Goal)new FollowParentGoal(this, 1.1D));
/* 121 */     this.goalSelector.addGoal(5, (Goal)this.eatBlockGoal);
/* 122 */     this.goalSelector.addGoal(6, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 1.0D));
/* 123 */     this.goalSelector.addGoal(7, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 6.0F));
/* 124 */     this.goalSelector.addGoal(8, (Goal)new RandomLookAroundGoal((Mob)this));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep() {
/* 129 */     this.eatAnimationTick = this.eatBlockGoal.getEatAnimationTick();
/* 130 */     super.customServerAiStep();
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 135 */     if (this.level.isClientSide) {
/* 136 */       this.eatAnimationTick = Math.max(0, this.eatAnimationTick - 1);
/*     */     }
/* 138 */     super.aiStep();
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 142 */     return Mob.createMobAttributes()
/* 143 */       .add(Attributes.MAX_HEALTH, 8.0D)
/* 144 */       .add(Attributes.MOVEMENT_SPEED, 0.23000000417232513D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/* 149 */     super.defineSynchedData();
/*     */ 
/*     */     
/* 152 */     this.entityData.define(DATA_WOOL_ID, Byte.valueOf((byte)0));
/*     */   }
/*     */ 
/*     */   
/*     */   public ResourceLocation getDefaultLootTable() {
/* 157 */     if (isSheared()) {
/* 158 */       return getType().getDefaultLootTable();
/*     */     }
/* 160 */     switch (getColor())
/*     */     
/*     */     { default:
/* 163 */         return BuiltInLootTables.SHEEP_WHITE;
/*     */       case ORANGE:
/* 165 */         return BuiltInLootTables.SHEEP_ORANGE;
/*     */       case MAGENTA:
/* 167 */         return BuiltInLootTables.SHEEP_MAGENTA;
/*     */       case LIGHT_BLUE:
/* 169 */         return BuiltInLootTables.SHEEP_LIGHT_BLUE;
/*     */       case YELLOW:
/* 171 */         return BuiltInLootTables.SHEEP_YELLOW;
/*     */       case LIME:
/* 173 */         return BuiltInLootTables.SHEEP_LIME;
/*     */       case PINK:
/* 175 */         return BuiltInLootTables.SHEEP_PINK;
/*     */       case GRAY:
/* 177 */         return BuiltInLootTables.SHEEP_GRAY;
/*     */       case LIGHT_GRAY:
/* 179 */         return BuiltInLootTables.SHEEP_LIGHT_GRAY;
/*     */       case CYAN:
/* 181 */         return BuiltInLootTables.SHEEP_CYAN;
/*     */       case PURPLE:
/* 183 */         return BuiltInLootTables.SHEEP_PURPLE;
/*     */       case BLUE:
/* 185 */         return BuiltInLootTables.SHEEP_BLUE;
/*     */       case BROWN:
/* 187 */         return BuiltInLootTables.SHEEP_BROWN;
/*     */       case GREEN:
/* 189 */         return BuiltInLootTables.SHEEP_GREEN;
/*     */       case RED:
/* 191 */         return BuiltInLootTables.SHEEP_RED;
/*     */       case BLACK:
/* 193 */         break; }  return BuiltInLootTables.SHEEP_BLACK;
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
/*     */   public InteractionResult mobInteract(Player debug1, InteractionHand debug2) {
/* 232 */     ItemStack debug3 = debug1.getItemInHand(debug2);
/* 233 */     if (debug3.getItem() == Items.SHEARS) {
/* 234 */       if (!this.level.isClientSide && readyForShearing()) {
/* 235 */         shear(SoundSource.PLAYERS);
/* 236 */         debug3.hurtAndBreak(1, (LivingEntity)debug1, debug1 -> debug1.broadcastBreakEvent(debug0));
/* 237 */         return InteractionResult.SUCCESS;
/*     */       } 
/* 239 */       return InteractionResult.CONSUME;
/*     */     } 
/*     */     
/* 242 */     return super.mobInteract(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void shear(SoundSource debug1) {
/* 247 */     this.level.playSound(null, (Entity)this, SoundEvents.SHEEP_SHEAR, debug1, 1.0F, 1.0F);
/*     */     
/* 249 */     setSheared(true);
/* 250 */     int debug2 = 1 + this.random.nextInt(3);
/* 251 */     for (int debug3 = 0; debug3 < debug2; debug3++) {
/* 252 */       ItemEntity debug4 = spawnAtLocation(ITEM_BY_DYE.get(getColor()), 1);
/* 253 */       if (debug4 != null) {
/* 254 */         debug4.setDeltaMovement(debug4.getDeltaMovement().add(((this.random
/* 255 */               .nextFloat() - this.random.nextFloat()) * 0.1F), (this.random
/* 256 */               .nextFloat() * 0.05F), ((this.random
/* 257 */               .nextFloat() - this.random.nextFloat()) * 0.1F)));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean readyForShearing() {
/* 265 */     return (isAlive() && !isSheared() && !isBaby());
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 270 */     super.addAdditionalSaveData(debug1);
/* 271 */     debug1.putBoolean("Sheared", isSheared());
/* 272 */     debug1.putByte("Color", (byte)getColor().getId());
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 277 */     super.readAdditionalSaveData(debug1);
/* 278 */     setSheared(debug1.getBoolean("Sheared"));
/* 279 */     setColor(DyeColor.byId(debug1.getByte("Color")));
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 284 */     return SoundEvents.SHEEP_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 289 */     return SoundEvents.SHEEP_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 294 */     return SoundEvents.SHEEP_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos debug1, BlockState debug2) {
/* 299 */     playSound(SoundEvents.SHEEP_STEP, 0.15F, 1.0F);
/*     */   }
/*     */   
/*     */   public DyeColor getColor() {
/* 303 */     return DyeColor.byId(((Byte)this.entityData.get(DATA_WOOL_ID)).byteValue() & 0xF);
/*     */   }
/*     */   
/*     */   public void setColor(DyeColor debug1) {
/* 307 */     byte debug2 = ((Byte)this.entityData.get(DATA_WOOL_ID)).byteValue();
/* 308 */     this.entityData.set(DATA_WOOL_ID, Byte.valueOf((byte)(debug2 & 0xF0 | debug1.getId() & 0xF)));
/*     */   }
/*     */   
/*     */   public boolean isSheared() {
/* 312 */     return ((((Byte)this.entityData.get(DATA_WOOL_ID)).byteValue() & 0x10) != 0);
/*     */   }
/*     */   
/*     */   public void setSheared(boolean debug1) {
/* 316 */     byte debug2 = ((Byte)this.entityData.get(DATA_WOOL_ID)).byteValue();
/* 317 */     if (debug1) {
/* 318 */       this.entityData.set(DATA_WOOL_ID, Byte.valueOf((byte)(debug2 | 0x10)));
/*     */     } else {
/* 320 */       this.entityData.set(DATA_WOOL_ID, Byte.valueOf((byte)(debug2 & 0xFFFFFFEF)));
/*     */     } 
/*     */   }
/*     */   
/*     */   public static DyeColor getRandomSheepColor(Random debug0) {
/* 325 */     int debug1 = debug0.nextInt(100);
/* 326 */     if (debug1 < 5) {
/* 327 */       return DyeColor.BLACK;
/*     */     }
/* 329 */     if (debug1 < 10) {
/* 330 */       return DyeColor.GRAY;
/*     */     }
/* 332 */     if (debug1 < 15) {
/* 333 */       return DyeColor.LIGHT_GRAY;
/*     */     }
/* 335 */     if (debug1 < 18) {
/* 336 */       return DyeColor.BROWN;
/*     */     }
/* 338 */     if (debug0.nextInt(500) == 0) {
/* 339 */       return DyeColor.PINK;
/*     */     }
/* 341 */     return DyeColor.WHITE;
/*     */   }
/*     */ 
/*     */   
/*     */   public Sheep getBreedOffspring(ServerLevel debug1, AgableMob debug2) {
/* 346 */     Sheep debug3 = (Sheep)debug2;
/* 347 */     Sheep debug4 = (Sheep)EntityType.SHEEP.create((Level)debug1);
/*     */     
/* 349 */     debug4.setColor(getOffspringColor(this, debug3));
/*     */     
/* 351 */     return debug4;
/*     */   }
/*     */ 
/*     */   
/*     */   public void ate() {
/* 356 */     setSheared(false);
/* 357 */     if (isBaby())
/*     */     {
/* 359 */       ageUp(60);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor debug1, DifficultyInstance debug2, MobSpawnType debug3, @Nullable SpawnGroupData debug4, @Nullable CompoundTag debug5) {
/* 366 */     setColor(getRandomSheepColor(debug1.getRandom()));
/* 367 */     return super.finalizeSpawn(debug1, debug2, debug3, debug4, debug5);
/*     */   }
/*     */   
/*     */   private DyeColor getOffspringColor(Animal debug1, Animal debug2) {
/* 371 */     DyeColor debug3 = ((Sheep)debug1).getColor();
/* 372 */     DyeColor debug4 = ((Sheep)debug2).getColor();
/*     */     
/* 374 */     CraftingContainer debug5 = makeContainer(debug3, debug4);
/*     */     
/* 376 */     return this.level.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, (Container)debug5, this.level)
/* 377 */       .map(debug1 -> debug1.assemble((Container)debug0))
/* 378 */       .map(ItemStack::getItem)
/* 379 */       .filter(DyeItem.class::isInstance)
/* 380 */       .map(DyeItem.class::cast)
/* 381 */       .map(DyeItem::getDyeColor)
/* 382 */       .orElseGet(() -> this.level.random.nextBoolean() ? debug1 : debug2);
/*     */   }
/*     */   
/*     */   private static CraftingContainer makeContainer(DyeColor debug0, DyeColor debug1) {
/* 386 */     CraftingContainer debug2 = new CraftingContainer(new AbstractContainerMenu(null, -1)
/*     */         {
/*     */           public boolean stillValid(Player debug1) {
/* 389 */             return false;
/*     */           }
/*     */         }2, 1);
/* 392 */     debug2.setItem(0, new ItemStack((ItemLike)DyeItem.byColor(debug0)));
/* 393 */     debug2.setItem(1, new ItemStack((ItemLike)DyeItem.byColor(debug1)));
/* 394 */     return debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getStandingEyeHeight(Pose debug1, EntityDimensions debug2) {
/* 399 */     return 0.95F * debug2.height;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\animal\Sheep.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */