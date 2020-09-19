/*     */ package net.minecraft.core.dispenser;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.BlockSource;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.Saddleable;
/*     */ import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
/*     */ import net.minecraft.world.entity.animal.horse.AbstractHorse;
/*     */ import net.minecraft.world.entity.decoration.ArmorStand;
/*     */ import net.minecraft.world.entity.item.PrimedTnt;
/*     */ import net.minecraft.world.entity.projectile.AbstractArrow;
/*     */ import net.minecraft.world.entity.projectile.Arrow;
/*     */ import net.minecraft.world.entity.projectile.FireworkRocketEntity;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.entity.projectile.SmallFireball;
/*     */ import net.minecraft.world.entity.projectile.Snowball;
/*     */ import net.minecraft.world.entity.projectile.SpectralArrow;
/*     */ import net.minecraft.world.entity.projectile.ThrownEgg;
/*     */ import net.minecraft.world.entity.projectile.ThrownExperienceBottle;
/*     */ import net.minecraft.world.entity.projectile.ThrownPotion;
/*     */ import net.minecraft.world.entity.vehicle.Boat;
/*     */ import net.minecraft.world.item.ArmorItem;
/*     */ import net.minecraft.world.item.BoneMealItem;
/*     */ import net.minecraft.world.item.BucketItem;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.SpawnEggItem;
/*     */ import net.minecraft.world.item.alchemy.PotionUtils;
/*     */ import net.minecraft.world.item.alchemy.Potions;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.BaseFireBlock;
/*     */ import net.minecraft.world.level.block.BeehiveBlock;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.BucketPickup;
/*     */ import net.minecraft.world.level.block.CampfireBlock;
/*     */ import net.minecraft.world.level.block.CarvedPumpkinBlock;
/*     */ import net.minecraft.world.level.block.DispenserBlock;
/*     */ import net.minecraft.world.level.block.RespawnAnchorBlock;
/*     */ import net.minecraft.world.level.block.ShulkerBoxBlock;
/*     */ import net.minecraft.world.level.block.SkullBlock;
/*     */ import net.minecraft.world.level.block.TntBlock;
/*     */ import net.minecraft.world.level.block.WitherSkullBlock;
/*     */ import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.DispenserBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.SkullBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ public interface DispenseItemBehavior
/*     */ {
/*     */   static {
/*  78 */     NOOP = ((debug0, debug1) -> debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final DispenseItemBehavior NOOP;
/*     */ 
/*     */ 
/*     */   
/*     */   static void bootStrap() {
/*  88 */     DispenserBlock.registerBehavior((ItemLike)Items.ARROW, new AbstractProjectileDispenseBehavior()
/*     */         {
/*     */           protected Projectile getProjectile(Level debug1, Position debug2, ItemStack debug3) {
/*  91 */             Arrow debug4 = new Arrow(debug1, debug2.x(), debug2.y(), debug2.z());
/*  92 */             debug4.pickup = AbstractArrow.Pickup.ALLOWED;
/*     */             
/*  94 */             return (Projectile)debug4;
/*     */           }
/*     */         });
/*  97 */     DispenserBlock.registerBehavior((ItemLike)Items.TIPPED_ARROW, new AbstractProjectileDispenseBehavior()
/*     */         {
/*     */           protected Projectile getProjectile(Level debug1, Position debug2, ItemStack debug3) {
/* 100 */             Arrow debug4 = new Arrow(debug1, debug2.x(), debug2.y(), debug2.z());
/* 101 */             debug4.setEffectsFromItem(debug3);
/* 102 */             debug4.pickup = AbstractArrow.Pickup.ALLOWED;
/*     */             
/* 104 */             return (Projectile)debug4;
/*     */           }
/*     */         });
/* 107 */     DispenserBlock.registerBehavior((ItemLike)Items.SPECTRAL_ARROW, new AbstractProjectileDispenseBehavior()
/*     */         {
/*     */           protected Projectile getProjectile(Level debug1, Position debug2, ItemStack debug3) {
/* 110 */             SpectralArrow spectralArrow = new SpectralArrow(debug1, debug2.x(), debug2.y(), debug2.z());
/* 111 */             ((AbstractArrow)spectralArrow).pickup = AbstractArrow.Pickup.ALLOWED;
/*     */             
/* 113 */             return (Projectile)spectralArrow;
/*     */           }
/*     */         });
/* 116 */     DispenserBlock.registerBehavior((ItemLike)Items.EGG, new AbstractProjectileDispenseBehavior()
/*     */         {
/*     */           protected Projectile getProjectile(Level debug1, Position debug2, ItemStack debug3) {
/* 119 */             return (Projectile)Util.make(new ThrownEgg(debug1, debug2.x(), debug2.y(), debug2.z()), debug1 -> debug1.setItem(debug0));
/*     */           }
/*     */         });
/* 122 */     DispenserBlock.registerBehavior((ItemLike)Items.SNOWBALL, new AbstractProjectileDispenseBehavior()
/*     */         {
/*     */           protected Projectile getProjectile(Level debug1, Position debug2, ItemStack debug3) {
/* 125 */             return (Projectile)Util.make(new Snowball(debug1, debug2.x(), debug2.y(), debug2.z()), debug1 -> debug1.setItem(debug0));
/*     */           }
/*     */         });
/* 128 */     DispenserBlock.registerBehavior((ItemLike)Items.EXPERIENCE_BOTTLE, new AbstractProjectileDispenseBehavior()
/*     */         {
/*     */           protected Projectile getProjectile(Level debug1, Position debug2, ItemStack debug3) {
/* 131 */             return (Projectile)Util.make(new ThrownExperienceBottle(debug1, debug2.x(), debug2.y(), debug2.z()), debug1 -> debug1.setItem(debug0));
/*     */           }
/*     */ 
/*     */           
/*     */           protected float getUncertainty() {
/* 136 */             return super.getUncertainty() * 0.5F;
/*     */           }
/*     */ 
/*     */           
/*     */           protected float getPower() {
/* 141 */             return super.getPower() * 1.25F;
/*     */           }
/*     */         });
/*     */     
/* 145 */     DispenserBlock.registerBehavior((ItemLike)Items.SPLASH_POTION, new DispenseItemBehavior()
/*     */         {
/*     */           public ItemStack dispense(BlockSource debug1, ItemStack debug2) {
/* 148 */             return (new AbstractProjectileDispenseBehavior()
/*     */               {
/*     */                 protected Projectile getProjectile(Level debug1, Position debug2, ItemStack debug3) {
/* 151 */                   return (Projectile)Util.make(new ThrownPotion(debug1, debug2.x(), debug2.y(), debug2.z()), debug1 -> debug1.setItem(debug0));
/*     */                 }
/*     */ 
/*     */                 
/*     */                 protected float getUncertainty() {
/* 156 */                   return super.getUncertainty() * 0.5F;
/*     */                 }
/*     */ 
/*     */                 
/*     */                 protected float getPower() {
/* 161 */                   return super.getPower() * 1.25F;
/*     */                 }
/* 163 */               }).dispense(debug1, debug2);
/*     */           }
/*     */         });
/*     */     
/* 167 */     DispenserBlock.registerBehavior((ItemLike)Items.LINGERING_POTION, new DispenseItemBehavior()
/*     */         {
/*     */           public ItemStack dispense(BlockSource debug1, ItemStack debug2) {
/* 170 */             return (new AbstractProjectileDispenseBehavior()
/*     */               {
/*     */                 protected Projectile getProjectile(Level debug1, Position debug2, ItemStack debug3) {
/* 173 */                   return (Projectile)Util.make(new ThrownPotion(debug1, debug2.x(), debug2.y(), debug2.z()), debug1 -> debug1.setItem(debug0));
/*     */                 }
/*     */ 
/*     */                 
/*     */                 protected float getUncertainty() {
/* 178 */                   return super.getUncertainty() * 0.5F;
/*     */                 }
/*     */ 
/*     */                 
/*     */                 protected float getPower() {
/* 183 */                   return super.getPower() * 1.25F;
/*     */                 }
/* 185 */               }).dispense(debug1, debug2);
/*     */           }
/*     */         });
/*     */     
/* 189 */     DefaultDispenseItemBehavior debug0 = new DefaultDispenseItemBehavior()
/*     */       {
/*     */         public ItemStack execute(BlockSource debug1, ItemStack debug2) {
/* 192 */           Direction debug3 = (Direction)debug1.getBlockState().getValue((Property)DispenserBlock.FACING);
/*     */           
/* 194 */           EntityType<?> debug4 = ((SpawnEggItem)debug2.getItem()).getType(debug2.getTag());
/* 195 */           debug4.spawn(debug1.getLevel(), debug2, null, debug1.getPos().relative(debug3), MobSpawnType.DISPENSER, (debug3 != Direction.UP), false);
/* 196 */           debug2.shrink(1);
/* 197 */           return debug2;
/*     */         }
/*     */       };
/*     */     
/* 201 */     for (SpawnEggItem spawnEggItem : SpawnEggItem.eggs()) {
/* 202 */       DispenserBlock.registerBehavior((ItemLike)spawnEggItem, debug0);
/*     */     }
/*     */     
/* 205 */     DispenserBlock.registerBehavior((ItemLike)Items.ARMOR_STAND, new DefaultDispenseItemBehavior()
/*     */         {
/*     */           public ItemStack execute(BlockSource debug1, ItemStack debug2) {
/* 208 */             Direction debug3 = (Direction)debug1.getBlockState().getValue((Property)DispenserBlock.FACING);
/* 209 */             BlockPos debug4 = debug1.getPos().relative(debug3);
/* 210 */             ServerLevel serverLevel = debug1.getLevel();
/* 211 */             ArmorStand debug6 = new ArmorStand((Level)serverLevel, debug4.getX() + 0.5D, debug4.getY(), debug4.getZ() + 0.5D);
/* 212 */             EntityType.updateCustomEntityTag((Level)serverLevel, null, (Entity)debug6, debug2.getTag());
/* 213 */             debug6.yRot = debug3.toYRot();
/* 214 */             serverLevel.addFreshEntity((Entity)debug6);
/* 215 */             debug2.shrink(1);
/* 216 */             return debug2;
/*     */           }
/*     */         });
/*     */     
/* 220 */     DispenserBlock.registerBehavior((ItemLike)Items.SADDLE, new OptionalDispenseItemBehavior()
/*     */         {
/*     */           public ItemStack execute(BlockSource debug1, ItemStack debug2) {
/* 223 */             BlockPos debug3 = debug1.getPos().relative((Direction)debug1.getBlockState().getValue((Property)DispenserBlock.FACING));
/* 224 */             List<LivingEntity> debug4 = debug1.getLevel().getEntitiesOfClass(LivingEntity.class, new AABB(debug3), debug0 -> {
/*     */                   if (debug0 instanceof Saddleable) {
/*     */                     Saddleable debug1 = (Saddleable)debug0;
/* 227 */                     return (!debug1.isSaddled() && debug1.isSaddleable());
/*     */                   } 
/*     */                   
/*     */                   return false;
/*     */                 });
/* 232 */             if (!debug4.isEmpty()) {
/* 233 */               ((Saddleable)debug4.get(0)).equipSaddle(SoundSource.BLOCKS);
/* 234 */               debug2.shrink(1);
/* 235 */               setSuccess(true);
/* 236 */               return debug2;
/*     */             } 
/*     */             
/* 239 */             return super.execute(debug1, debug2);
/*     */           }
/*     */         });
/*     */     
/* 243 */     DefaultDispenseItemBehavior debug1 = new OptionalDispenseItemBehavior()
/*     */       {
/*     */         protected ItemStack execute(BlockSource debug1, ItemStack debug2) {
/* 246 */           BlockPos debug3 = debug1.getPos().relative((Direction)debug1.getBlockState().getValue((Property)DispenserBlock.FACING));
/* 247 */           List<AbstractHorse> debug4 = debug1.getLevel().getEntitiesOfClass(AbstractHorse.class, new AABB(debug3), debug0 -> (debug0.isAlive() && debug0.canWearArmor()));
/*     */           
/* 249 */           for (AbstractHorse debug6 : debug4) {
/* 250 */             if (debug6.isArmor(debug2) && !debug6.isWearingArmor() && debug6.isTamed()) {
/* 251 */               debug6.setSlot(401, debug2.split(1));
/* 252 */               setSuccess(true);
/* 253 */               return debug2;
/*     */             } 
/*     */           } 
/*     */           
/* 257 */           return super.execute(debug1, debug2);
/*     */         }
/*     */       };
/*     */     
/* 261 */     DispenserBlock.registerBehavior((ItemLike)Items.LEATHER_HORSE_ARMOR, debug1);
/* 262 */     DispenserBlock.registerBehavior((ItemLike)Items.IRON_HORSE_ARMOR, debug1);
/* 263 */     DispenserBlock.registerBehavior((ItemLike)Items.GOLDEN_HORSE_ARMOR, debug1);
/* 264 */     DispenserBlock.registerBehavior((ItemLike)Items.DIAMOND_HORSE_ARMOR, debug1);
/*     */     
/* 266 */     DispenserBlock.registerBehavior((ItemLike)Items.WHITE_CARPET, debug1);
/* 267 */     DispenserBlock.registerBehavior((ItemLike)Items.ORANGE_CARPET, debug1);
/* 268 */     DispenserBlock.registerBehavior((ItemLike)Items.CYAN_CARPET, debug1);
/* 269 */     DispenserBlock.registerBehavior((ItemLike)Items.BLUE_CARPET, debug1);
/* 270 */     DispenserBlock.registerBehavior((ItemLike)Items.BROWN_CARPET, debug1);
/* 271 */     DispenserBlock.registerBehavior((ItemLike)Items.BLACK_CARPET, debug1);
/* 272 */     DispenserBlock.registerBehavior((ItemLike)Items.GRAY_CARPET, debug1);
/* 273 */     DispenserBlock.registerBehavior((ItemLike)Items.GREEN_CARPET, debug1);
/* 274 */     DispenserBlock.registerBehavior((ItemLike)Items.LIGHT_BLUE_CARPET, debug1);
/* 275 */     DispenserBlock.registerBehavior((ItemLike)Items.LIGHT_GRAY_CARPET, debug1);
/* 276 */     DispenserBlock.registerBehavior((ItemLike)Items.LIME_CARPET, debug1);
/* 277 */     DispenserBlock.registerBehavior((ItemLike)Items.MAGENTA_CARPET, debug1);
/* 278 */     DispenserBlock.registerBehavior((ItemLike)Items.PINK_CARPET, debug1);
/* 279 */     DispenserBlock.registerBehavior((ItemLike)Items.PURPLE_CARPET, debug1);
/* 280 */     DispenserBlock.registerBehavior((ItemLike)Items.RED_CARPET, debug1);
/* 281 */     DispenserBlock.registerBehavior((ItemLike)Items.YELLOW_CARPET, debug1);
/*     */     
/* 283 */     DispenserBlock.registerBehavior((ItemLike)Items.CHEST, new OptionalDispenseItemBehavior()
/*     */         {
/*     */           public ItemStack execute(BlockSource debug1, ItemStack debug2) {
/* 286 */             BlockPos debug3 = debug1.getPos().relative((Direction)debug1.getBlockState().getValue((Property)DispenserBlock.FACING));
/* 287 */             List<AbstractChestedHorse> debug4 = debug1.getLevel().getEntitiesOfClass(AbstractChestedHorse.class, new AABB(debug3), debug0 -> (debug0.isAlive() && !debug0.hasChest()));
/*     */             
/* 289 */             for (AbstractChestedHorse debug6 : debug4) {
/* 290 */               if (debug6.isTamed() && debug6.setSlot(499, debug2)) {
/* 291 */                 debug2.shrink(1);
/* 292 */                 setSuccess(true);
/* 293 */                 return debug2;
/*     */               } 
/*     */             } 
/*     */             
/* 297 */             return super.execute(debug1, debug2);
/*     */           }
/*     */         });
/*     */     
/* 301 */     DispenserBlock.registerBehavior((ItemLike)Items.FIREWORK_ROCKET, new DefaultDispenseItemBehavior()
/*     */         {
/*     */           public ItemStack execute(BlockSource debug1, ItemStack debug2) {
/* 304 */             Direction debug3 = (Direction)debug1.getBlockState().getValue((Property)DispenserBlock.FACING);
/*     */             
/* 306 */             FireworkRocketEntity debug4 = new FireworkRocketEntity((Level)debug1.getLevel(), debug2, debug1.x(), debug1.y(), debug1.x(), true);
/* 307 */             DispenseItemBehavior.setEntityPokingOutOfBlock(debug1, (Entity)debug4, debug3);
/* 308 */             debug4.shoot(debug3.getStepX(), debug3.getStepY(), debug3.getStepZ(), 0.5F, 1.0F);
/* 309 */             debug1.getLevel().addFreshEntity((Entity)debug4);
/*     */             
/* 311 */             debug2.shrink(1);
/* 312 */             return debug2;
/*     */           }
/*     */ 
/*     */           
/*     */           protected void playSound(BlockSource debug1) {
/* 317 */             debug1.getLevel().levelEvent(1004, debug1.getPos(), 0);
/*     */           }
/*     */         });
/*     */     
/* 321 */     DispenserBlock.registerBehavior((ItemLike)Items.FIRE_CHARGE, new DefaultDispenseItemBehavior()
/*     */         {
/*     */           public ItemStack execute(BlockSource debug1, ItemStack debug2) {
/* 324 */             Direction debug3 = (Direction)debug1.getBlockState().getValue((Property)DispenserBlock.FACING);
/*     */             
/* 326 */             Position debug4 = DispenserBlock.getDispensePosition(debug1);
/* 327 */             double debug5 = debug4.x() + (debug3.getStepX() * 0.3F);
/* 328 */             double debug7 = debug4.y() + (debug3.getStepY() * 0.3F);
/* 329 */             double debug9 = debug4.z() + (debug3.getStepZ() * 0.3F);
/*     */             
/* 331 */             ServerLevel serverLevel = debug1.getLevel();
/* 332 */             Random debug12 = ((Level)serverLevel).random;
/*     */             
/* 334 */             double debug13 = debug12.nextGaussian() * 0.05D + debug3.getStepX();
/* 335 */             double debug15 = debug12.nextGaussian() * 0.05D + debug3.getStepY();
/* 336 */             double debug17 = debug12.nextGaussian() * 0.05D + debug3.getStepZ();
/*     */             
/* 338 */             serverLevel.addFreshEntity((Entity)Util.make(new SmallFireball((Level)serverLevel, debug5, debug7, debug9, debug13, debug15, debug17), debug1 -> debug1.setItem(debug0)));
/*     */             
/* 340 */             debug2.shrink(1);
/* 341 */             return debug2;
/*     */           }
/*     */ 
/*     */           
/*     */           protected void playSound(BlockSource debug1) {
/* 346 */             debug1.getLevel().levelEvent(1018, debug1.getPos(), 0);
/*     */           }
/*     */         });
/*     */     
/* 350 */     DispenserBlock.registerBehavior((ItemLike)Items.OAK_BOAT, new BoatDispenseItemBehavior(Boat.Type.OAK));
/* 351 */     DispenserBlock.registerBehavior((ItemLike)Items.SPRUCE_BOAT, new BoatDispenseItemBehavior(Boat.Type.SPRUCE));
/* 352 */     DispenserBlock.registerBehavior((ItemLike)Items.BIRCH_BOAT, new BoatDispenseItemBehavior(Boat.Type.BIRCH));
/* 353 */     DispenserBlock.registerBehavior((ItemLike)Items.JUNGLE_BOAT, new BoatDispenseItemBehavior(Boat.Type.JUNGLE));
/* 354 */     DispenserBlock.registerBehavior((ItemLike)Items.DARK_OAK_BOAT, new BoatDispenseItemBehavior(Boat.Type.DARK_OAK));
/* 355 */     DispenserBlock.registerBehavior((ItemLike)Items.ACACIA_BOAT, new BoatDispenseItemBehavior(Boat.Type.ACACIA));
/*     */     
/* 357 */     DispenseItemBehavior debug2 = new DefaultDispenseItemBehavior() {
/* 358 */         private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();
/*     */ 
/*     */         
/*     */         public ItemStack execute(BlockSource debug1, ItemStack debug2) {
/* 362 */           BucketItem debug3 = (BucketItem)debug2.getItem();
/* 363 */           BlockPos debug4 = debug1.getPos().relative((Direction)debug1.getBlockState().getValue((Property)DispenserBlock.FACING));
/*     */           
/* 365 */           ServerLevel serverLevel = debug1.getLevel();
/* 366 */           if (debug3.emptyBucket(null, (Level)serverLevel, debug4, null)) {
/* 367 */             debug3.checkExtraContent((Level)serverLevel, debug2, debug4);
/* 368 */             return new ItemStack((ItemLike)Items.BUCKET);
/*     */           } 
/*     */           
/* 371 */           return this.defaultDispenseItemBehavior.dispense(debug1, debug2);
/*     */         }
/*     */       };
/* 374 */     DispenserBlock.registerBehavior((ItemLike)Items.LAVA_BUCKET, debug2);
/* 375 */     DispenserBlock.registerBehavior((ItemLike)Items.WATER_BUCKET, debug2);
/* 376 */     DispenserBlock.registerBehavior((ItemLike)Items.SALMON_BUCKET, debug2);
/* 377 */     DispenserBlock.registerBehavior((ItemLike)Items.COD_BUCKET, debug2);
/* 378 */     DispenserBlock.registerBehavior((ItemLike)Items.PUFFERFISH_BUCKET, debug2);
/* 379 */     DispenserBlock.registerBehavior((ItemLike)Items.TROPICAL_FISH_BUCKET, debug2);
/*     */     
/* 381 */     DispenserBlock.registerBehavior((ItemLike)Items.BUCKET, new DefaultDispenseItemBehavior() {
/* 382 */           private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();
/*     */           
/*     */           public ItemStack execute(BlockSource debug1, ItemStack debug2) {
/*     */             Item debug7;
/* 386 */             ServerLevel serverLevel = debug1.getLevel();
/*     */             
/* 388 */             BlockPos debug4 = debug1.getPos().relative((Direction)debug1.getBlockState().getValue((Property)DispenserBlock.FACING));
/*     */             
/* 390 */             BlockState debug5 = serverLevel.getBlockState(debug4);
/* 391 */             Block debug6 = debug5.getBlock();
/*     */ 
/*     */ 
/*     */             
/* 395 */             if (debug6 instanceof BucketPickup) {
/* 396 */               Fluid debug8 = ((BucketPickup)debug6).takeLiquid((LevelAccessor)serverLevel, debug4, debug5);
/* 397 */               if (!(debug8 instanceof net.minecraft.world.level.material.FlowingFluid)) {
/* 398 */                 return super.execute(debug1, debug2);
/*     */               }
/* 400 */               debug7 = debug8.getBucket();
/*     */             } else {
/* 402 */               return super.execute(debug1, debug2);
/*     */             } 
/*     */             
/* 405 */             debug2.shrink(1);
/* 406 */             if (debug2.isEmpty())
/* 407 */               return new ItemStack((ItemLike)debug7); 
/* 408 */             if (((DispenserBlockEntity)debug1.getEntity()).addItem(new ItemStack((ItemLike)debug7)) < 0) {
/* 409 */               this.defaultDispenseItemBehavior.dispense(debug1, new ItemStack((ItemLike)debug7));
/*     */             }
/* 411 */             return debug2;
/*     */           }
/*     */         });
/*     */     
/* 415 */     DispenserBlock.registerBehavior((ItemLike)Items.FLINT_AND_STEEL, new OptionalDispenseItemBehavior()
/*     */         {
/*     */           protected ItemStack execute(BlockSource debug1, ItemStack debug2) {
/* 418 */             ServerLevel serverLevel = debug1.getLevel();
/*     */             
/* 420 */             setSuccess(true);
/*     */             
/* 422 */             Direction debug4 = (Direction)debug1.getBlockState().getValue((Property)DispenserBlock.FACING);
/* 423 */             BlockPos debug5 = debug1.getPos().relative(debug4);
/* 424 */             BlockState debug6 = serverLevel.getBlockState(debug5);
/* 425 */             if (BaseFireBlock.canBePlacedAt((Level)serverLevel, debug5, debug4)) {
/* 426 */               serverLevel.setBlockAndUpdate(debug5, BaseFireBlock.getState((BlockGetter)serverLevel, debug5));
/* 427 */             } else if (CampfireBlock.canLight(debug6)) {
/* 428 */               serverLevel.setBlockAndUpdate(debug5, (BlockState)debug6.setValue((Property)BlockStateProperties.LIT, Boolean.valueOf(true)));
/* 429 */             } else if (debug6.getBlock() instanceof TntBlock) {
/* 430 */               TntBlock.explode((Level)serverLevel, debug5);
/* 431 */               serverLevel.removeBlock(debug5, false);
/*     */             } else {
/* 433 */               setSuccess(false);
/*     */             } 
/*     */             
/* 436 */             if (isSuccess() && debug2.hurt(1, ((Level)serverLevel).random, null)) {
/* 437 */               debug2.setCount(0);
/*     */             }
/*     */             
/* 440 */             return debug2;
/*     */           }
/*     */         });
/*     */     
/* 444 */     DispenserBlock.registerBehavior((ItemLike)Items.BONE_MEAL, new OptionalDispenseItemBehavior()
/*     */         {
/*     */           protected ItemStack execute(BlockSource debug1, ItemStack debug2) {
/* 447 */             setSuccess(true);
/* 448 */             ServerLevel serverLevel = debug1.getLevel();
/*     */             
/* 450 */             BlockPos debug4 = debug1.getPos().relative((Direction)debug1.getBlockState().getValue((Property)DispenserBlock.FACING));
/* 451 */             if (BoneMealItem.growCrop(debug2, (Level)serverLevel, debug4) || BoneMealItem.growWaterPlant(debug2, (Level)serverLevel, debug4, null)) {
/* 452 */               if (!((Level)serverLevel).isClientSide) {
/* 453 */                 serverLevel.levelEvent(2005, debug4, 0);
/*     */               }
/*     */             } else {
/* 456 */               setSuccess(false);
/*     */             } 
/*     */             
/* 459 */             return debug2;
/*     */           }
/*     */         });
/*     */     
/* 463 */     DispenserBlock.registerBehavior((ItemLike)Blocks.TNT, new DefaultDispenseItemBehavior()
/*     */         {
/*     */           protected ItemStack execute(BlockSource debug1, ItemStack debug2) {
/* 466 */             ServerLevel serverLevel = debug1.getLevel();
/* 467 */             BlockPos debug4 = debug1.getPos().relative((Direction)debug1.getBlockState().getValue((Property)DispenserBlock.FACING));
/*     */             
/* 469 */             PrimedTnt debug5 = new PrimedTnt((Level)serverLevel, debug4.getX() + 0.5D, debug4.getY(), debug4.getZ() + 0.5D, null);
/* 470 */             serverLevel.addFreshEntity((Entity)debug5);
/* 471 */             serverLevel.playSound(null, debug5.getX(), debug5.getY(), debug5.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */             
/* 473 */             debug2.shrink(1);
/* 474 */             return debug2;
/*     */           }
/*     */         });
/*     */     
/* 478 */     DispenseItemBehavior debug3 = new OptionalDispenseItemBehavior()
/*     */       {
/*     */         protected ItemStack execute(BlockSource debug1, ItemStack debug2) {
/* 481 */           setSuccess(ArmorItem.dispenseArmor(debug1, debug2));
/* 482 */           return debug2;
/*     */         }
/*     */       };
/* 485 */     DispenserBlock.registerBehavior((ItemLike)Items.CREEPER_HEAD, debug3);
/* 486 */     DispenserBlock.registerBehavior((ItemLike)Items.ZOMBIE_HEAD, debug3);
/* 487 */     DispenserBlock.registerBehavior((ItemLike)Items.DRAGON_HEAD, debug3);
/* 488 */     DispenserBlock.registerBehavior((ItemLike)Items.SKELETON_SKULL, debug3);
/* 489 */     DispenserBlock.registerBehavior((ItemLike)Items.PLAYER_HEAD, debug3);
/* 490 */     DispenserBlock.registerBehavior((ItemLike)Items.WITHER_SKELETON_SKULL, new OptionalDispenseItemBehavior()
/*     */         {
/*     */           protected ItemStack execute(BlockSource debug1, ItemStack debug2) {
/* 493 */             ServerLevel serverLevel = debug1.getLevel();
/* 494 */             Direction debug4 = (Direction)debug1.getBlockState().getValue((Property)DispenserBlock.FACING);
/* 495 */             BlockPos debug5 = debug1.getPos().relative(debug4);
/*     */             
/* 497 */             if (serverLevel.isEmptyBlock(debug5) && WitherSkullBlock.canSpawnMob((Level)serverLevel, debug5, debug2)) {
/* 498 */               serverLevel.setBlock(debug5, (BlockState)Blocks.WITHER_SKELETON_SKULL.defaultBlockState().setValue((Property)SkullBlock.ROTATION, Integer.valueOf((debug4.getAxis() == Direction.Axis.Y) ? 0 : (debug4.getOpposite().get2DDataValue() * 4))), 3);
/* 499 */               BlockEntity debug6 = serverLevel.getBlockEntity(debug5);
/* 500 */               if (debug6 instanceof SkullBlockEntity) {
/* 501 */                 WitherSkullBlock.checkSpawn((Level)serverLevel, debug5, (SkullBlockEntity)debug6);
/*     */               }
/* 503 */               debug2.shrink(1);
/* 504 */               setSuccess(true);
/*     */             } else {
/* 506 */               setSuccess(ArmorItem.dispenseArmor(debug1, debug2));
/*     */             } 
/* 508 */             return debug2;
/*     */           }
/*     */         });
/*     */     
/* 512 */     DispenserBlock.registerBehavior((ItemLike)Blocks.CARVED_PUMPKIN, new OptionalDispenseItemBehavior()
/*     */         {
/*     */           protected ItemStack execute(BlockSource debug1, ItemStack debug2) {
/* 515 */             ServerLevel serverLevel = debug1.getLevel();
/* 516 */             BlockPos debug4 = debug1.getPos().relative((Direction)debug1.getBlockState().getValue((Property)DispenserBlock.FACING));
/* 517 */             CarvedPumpkinBlock debug5 = (CarvedPumpkinBlock)Blocks.CARVED_PUMPKIN;
/*     */             
/* 519 */             if (serverLevel.isEmptyBlock(debug4) && debug5.canSpawnGolem((LevelReader)serverLevel, debug4)) {
/* 520 */               if (!((Level)serverLevel).isClientSide) {
/* 521 */                 serverLevel.setBlock(debug4, debug5.defaultBlockState(), 3);
/*     */               }
/* 523 */               debug2.shrink(1);
/* 524 */               setSuccess(true);
/*     */             } else {
/* 526 */               setSuccess(ArmorItem.dispenseArmor(debug1, debug2));
/*     */             } 
/* 528 */             return debug2;
/*     */           }
/*     */         });
/*     */     
/* 532 */     DispenserBlock.registerBehavior((ItemLike)Blocks.SHULKER_BOX.asItem(), new ShulkerBoxDispenseBehavior());
/* 533 */     for (DyeColor debug7 : DyeColor.values()) {
/* 534 */       DispenserBlock.registerBehavior((ItemLike)ShulkerBoxBlock.getBlockByColor(debug7).asItem(), new ShulkerBoxDispenseBehavior());
/*     */     }
/*     */     
/* 537 */     DispenserBlock.registerBehavior((ItemLike)Items.GLASS_BOTTLE.asItem(), new OptionalDispenseItemBehavior() {
/* 538 */           private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();
/*     */           
/*     */           private ItemStack takeLiquid(BlockSource debug1, ItemStack debug2, ItemStack debug3) {
/* 541 */             debug2.shrink(1);
/* 542 */             if (debug2.isEmpty())
/* 543 */               return debug3.copy(); 
/* 544 */             if (((DispenserBlockEntity)debug1.getEntity()).addItem(debug3.copy()) < 0) {
/* 545 */               this.defaultDispenseItemBehavior.dispense(debug1, debug3.copy());
/*     */             }
/* 547 */             return debug2;
/*     */           }
/*     */ 
/*     */           
/*     */           public ItemStack execute(BlockSource debug1, ItemStack debug2) {
/* 552 */             setSuccess(false);
/* 553 */             ServerLevel debug3 = debug1.getLevel();
/*     */             
/* 555 */             BlockPos debug4 = debug1.getPos().relative((Direction)debug1.getBlockState().getValue((Property)DispenserBlock.FACING));
/*     */             
/* 557 */             BlockState debug5 = debug3.getBlockState(debug4);
/*     */             
/* 559 */             if (debug5.is((Tag)BlockTags.BEEHIVES, debug0 -> debug0.hasProperty((Property)BeehiveBlock.HONEY_LEVEL)) && ((Integer)debug5.getValue((Property)BeehiveBlock.HONEY_LEVEL)).intValue() >= 5) {
/* 560 */               ((BeehiveBlock)debug5.getBlock()).releaseBeesAndResetHoneyLevel((Level)debug3, debug5, debug4, null, BeehiveBlockEntity.BeeReleaseStatus.BEE_RELEASED);
/* 561 */               setSuccess(true);
/* 562 */               return takeLiquid(debug1, debug2, new ItemStack((ItemLike)Items.HONEY_BOTTLE));
/* 563 */             }  if (debug3.getFluidState(debug4).is((Tag)FluidTags.WATER)) {
/* 564 */               setSuccess(true);
/* 565 */               return takeLiquid(debug1, debug2, PotionUtils.setPotion(new ItemStack((ItemLike)Items.POTION), Potions.WATER));
/*     */             } 
/* 567 */             return super.execute(debug1, debug2);
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 572 */     DispenserBlock.registerBehavior((ItemLike)Items.GLOWSTONE, new OptionalDispenseItemBehavior()
/*     */         {
/*     */           public ItemStack execute(BlockSource debug1, ItemStack debug2) {
/* 575 */             Direction debug3 = (Direction)debug1.getBlockState().getValue((Property)DispenserBlock.FACING);
/* 576 */             BlockPos debug4 = debug1.getPos().relative(debug3);
/* 577 */             ServerLevel serverLevel = debug1.getLevel();
/* 578 */             BlockState debug6 = serverLevel.getBlockState(debug4);
/* 579 */             setSuccess(true);
/* 580 */             if (debug6.is(Blocks.RESPAWN_ANCHOR)) {
/* 581 */               if (((Integer)debug6.getValue((Property)RespawnAnchorBlock.CHARGE)).intValue() != 4) {
/* 582 */                 RespawnAnchorBlock.charge((Level)serverLevel, debug4, debug6);
/* 583 */                 debug2.shrink(1);
/*     */               } else {
/* 585 */                 setSuccess(false);
/*     */               } 
/*     */               
/* 588 */               return debug2;
/*     */             } 
/* 590 */             return super.execute(debug1, debug2);
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 595 */     DispenserBlock.registerBehavior((ItemLike)Items.SHEARS.asItem(), new ShearsDispenseItemBehavior());
/*     */   }
/*     */   
/*     */   static void setEntityPokingOutOfBlock(BlockSource debug0, Entity debug1, Direction debug2) {
/* 599 */     debug1.setPos(debug0
/* 600 */         .x() + debug2.getStepX() * (0.5000099999997474D - debug1.getBbWidth() / 2.0D), debug0
/* 601 */         .y() + debug2.getStepY() * (0.5000099999997474D - debug1.getBbHeight() / 2.0D) - debug1.getBbHeight() / 2.0D, debug0
/* 602 */         .z() + debug2.getStepZ() * (0.5000099999997474D - debug1.getBbWidth() / 2.0D));
/*     */   }
/*     */   
/*     */   ItemStack dispense(BlockSource paramBlockSource, ItemStack paramItemStack);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\core\dispenser\DispenseItemBehavior.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */