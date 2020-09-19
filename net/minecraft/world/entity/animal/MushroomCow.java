/*     */ package net.minecraft.world.entity.animal;
/*     */ import java.util.Optional;
/*     */ import java.util.Random;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.entity.AgableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LightningBolt;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.Shearable;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.BlockItem;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ItemUtils;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.SuspiciousStewItem;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.FlowerBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import org.apache.commons.lang3.tuple.Pair;
/*     */ 
/*     */ public class MushroomCow extends Cow implements Shearable {
/*  45 */   private static final EntityDataAccessor<String> DATA_TYPE = SynchedEntityData.defineId(MushroomCow.class, EntityDataSerializers.STRING);
/*     */   
/*     */   private MobEffect effect;
/*     */   
/*     */   private int effectDuration;
/*     */   private UUID lastLightningBoltUUID;
/*     */   
/*     */   public MushroomCow(EntityType<? extends MushroomCow> debug1, Level debug2) {
/*  53 */     super((EntityType)debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getWalkTargetValue(BlockPos debug1, LevelReader debug2) {
/*  58 */     if (debug2.getBlockState(debug1.below()).is(Blocks.MYCELIUM)) {
/*  59 */       return 10.0F;
/*     */     }
/*  61 */     return debug2.getBrightness(debug1) - 0.5F;
/*     */   }
/*     */   
/*     */   public static boolean checkMushroomSpawnRules(EntityType<MushroomCow> debug0, LevelAccessor debug1, MobSpawnType debug2, BlockPos debug3, Random debug4) {
/*  65 */     return (debug1.getBlockState(debug3.below()).is(Blocks.MYCELIUM) && debug1
/*  66 */       .getRawBrightness(debug3, 0) > 8);
/*     */   }
/*     */ 
/*     */   
/*     */   public void thunderHit(ServerLevel debug1, LightningBolt debug2) {
/*  71 */     UUID debug3 = debug2.getUUID();
/*  72 */     if (!debug3.equals(this.lastLightningBoltUUID)) {
/*  73 */       setMushroomType((getMushroomType() == MushroomType.RED) ? MushroomType.BROWN : MushroomType.RED);
/*  74 */       this.lastLightningBoltUUID = debug3;
/*  75 */       playSound(SoundEvents.MOOSHROOM_CONVERT, 2.0F, 1.0F);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/*  81 */     super.defineSynchedData();
/*     */     
/*  83 */     this.entityData.define(DATA_TYPE, MushroomType.RED.type);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player debug1, InteractionHand debug2) {
/*  88 */     ItemStack debug3 = debug1.getItemInHand(debug2);
/*  89 */     if (debug3.getItem() == Items.BOWL && !isBaby()) {
/*     */       ItemStack debug4; SoundEvent debug7;
/*  91 */       boolean debug5 = false;
/*     */       
/*  93 */       if (this.effect != null) {
/*  94 */         debug5 = true;
/*  95 */         debug4 = new ItemStack((ItemLike)Items.SUSPICIOUS_STEW);
/*  96 */         SuspiciousStewItem.saveMobEffect(debug4, this.effect, this.effectDuration);
/*  97 */         this.effect = null;
/*  98 */         this.effectDuration = 0;
/*     */       } else {
/* 100 */         debug4 = new ItemStack((ItemLike)Items.MUSHROOM_STEW);
/*     */       } 
/*     */       
/* 103 */       ItemStack debug6 = ItemUtils.createFilledResult(debug3, debug1, debug4, false);
/* 104 */       debug1.setItemInHand(debug2, debug6);
/*     */ 
/*     */       
/* 107 */       if (debug5) {
/* 108 */         debug7 = SoundEvents.MOOSHROOM_MILK_SUSPICIOUSLY;
/*     */       } else {
/* 110 */         debug7 = SoundEvents.MOOSHROOM_MILK;
/*     */       } 
/*     */       
/* 113 */       playSound(debug7, 1.0F, 1.0F);
/*     */       
/* 115 */       return InteractionResult.sidedSuccess(this.level.isClientSide);
/*     */     } 
/*     */     
/* 118 */     if (debug3.getItem() == Items.SHEARS && readyForShearing()) {
/* 119 */       shear(SoundSource.PLAYERS);
/* 120 */       if (!this.level.isClientSide) {
/* 121 */         debug3.hurtAndBreak(1, (LivingEntity)debug1, debug1 -> debug1.broadcastBreakEvent(debug0));
/*     */       }
/* 123 */       return InteractionResult.sidedSuccess(this.level.isClientSide);
/*     */     } 
/*     */ 
/*     */     
/* 127 */     if (getMushroomType() == MushroomType.BROWN && debug3.getItem().is((Tag)ItemTags.SMALL_FLOWERS)) {
/* 128 */       if (this.effect != null) {
/* 129 */         for (int debug4 = 0; debug4 < 2; debug4++) {
/* 130 */           this.level.addParticle((ParticleOptions)ParticleTypes.SMOKE, getX() + this.random.nextDouble() / 2.0D, getY(0.5D), getZ() + this.random.nextDouble() / 2.0D, 0.0D, this.random.nextDouble() / 5.0D, 0.0D);
/*     */         }
/*     */       } else {
/* 133 */         Optional<Pair<MobEffect, Integer>> debug4 = getEffectFromItemStack(debug3);
/* 134 */         if (!debug4.isPresent()) {
/* 135 */           return InteractionResult.PASS;
/*     */         }
/*     */         
/* 138 */         Pair<MobEffect, Integer> debug5 = debug4.get();
/* 139 */         if (!debug1.abilities.instabuild) {
/* 140 */           debug3.shrink(1);
/*     */         }
/* 142 */         for (int debug6 = 0; debug6 < 4; debug6++) {
/* 143 */           this.level.addParticle((ParticleOptions)ParticleTypes.EFFECT, getX() + this.random.nextDouble() / 2.0D, getY(0.5D), getZ() + this.random.nextDouble() / 2.0D, 0.0D, this.random.nextDouble() / 5.0D, 0.0D);
/*     */         }
/* 145 */         this.effect = (MobEffect)debug5.getLeft();
/* 146 */         this.effectDuration = ((Integer)debug5.getRight()).intValue();
/* 147 */         playSound(SoundEvents.MOOSHROOM_EAT, 2.0F, 1.0F);
/*     */       } 
/* 149 */       return InteractionResult.sidedSuccess(this.level.isClientSide);
/*     */     } 
/*     */     
/* 152 */     return super.mobInteract(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void shear(SoundSource debug1) {
/* 157 */     this.level.playSound(null, (Entity)this, SoundEvents.MOOSHROOM_SHEAR, debug1, 1.0F, 1.0F);
/*     */     
/* 159 */     if (!this.level.isClientSide()) {
/* 160 */       ((ServerLevel)this.level).sendParticles((ParticleOptions)ParticleTypes.EXPLOSION, getX(), getY(0.5D), getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
/*     */       
/* 162 */       remove();
/* 163 */       Cow debug2 = (Cow)EntityType.COW.create(this.level);
/* 164 */       debug2.moveTo(getX(), getY(), getZ(), this.yRot, this.xRot);
/* 165 */       debug2.setHealth(getHealth());
/* 166 */       debug2.yBodyRot = this.yBodyRot;
/* 167 */       if (hasCustomName()) {
/* 168 */         debug2.setCustomName(getCustomName());
/* 169 */         debug2.setCustomNameVisible(isCustomNameVisible());
/*     */       } 
/* 171 */       if (isPersistenceRequired()) {
/* 172 */         debug2.setPersistenceRequired();
/*     */       }
/* 174 */       debug2.setInvulnerable(isInvulnerable());
/*     */       
/* 176 */       this.level.addFreshEntity((Entity)debug2);
/* 177 */       for (int debug3 = 0; debug3 < 5; debug3++) {
/* 178 */         this.level.addFreshEntity((Entity)new ItemEntity(this.level, getX(), getY(1.0D), getZ(), new ItemStack((ItemLike)(getMushroomType()).blockState.getBlock())));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean readyForShearing() {
/* 185 */     return (isAlive() && !isBaby());
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 190 */     super.addAdditionalSaveData(debug1);
/* 191 */     debug1.putString("Type", (getMushroomType()).type);
/*     */     
/* 193 */     if (this.effect != null) {
/* 194 */       debug1.putByte("EffectId", (byte)MobEffect.getId(this.effect));
/* 195 */       debug1.putInt("EffectDuration", this.effectDuration);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 201 */     super.readAdditionalSaveData(debug1);
/* 202 */     setMushroomType(MushroomType.byType(debug1.getString("Type")));
/*     */     
/* 204 */     if (debug1.contains("EffectId", 1)) {
/* 205 */       this.effect = MobEffect.byId(debug1.getByte("EffectId"));
/*     */     }
/*     */     
/* 208 */     if (debug1.contains("EffectDuration", 3)) {
/* 209 */       this.effectDuration = debug1.getInt("EffectDuration");
/*     */     }
/*     */   }
/*     */   
/*     */   private Optional<Pair<MobEffect, Integer>> getEffectFromItemStack(ItemStack debug1) {
/* 214 */     Item debug2 = debug1.getItem();
/* 215 */     if (debug2 instanceof BlockItem) {
/* 216 */       Block debug3 = ((BlockItem)debug2).getBlock();
/* 217 */       if (debug3 instanceof FlowerBlock) {
/* 218 */         FlowerBlock debug4 = (FlowerBlock)debug3;
/* 219 */         return Optional.of(Pair.of(debug4.getSuspiciousStewEffect(), Integer.valueOf(debug4.getEffectDuration())));
/*     */       } 
/*     */     } 
/* 222 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   private void setMushroomType(MushroomType debug1) {
/* 226 */     this.entityData.set(DATA_TYPE, debug1.type);
/*     */   }
/*     */   
/*     */   public MushroomType getMushroomType() {
/* 230 */     return MushroomType.byType((String)this.entityData.get(DATA_TYPE));
/*     */   }
/*     */ 
/*     */   
/*     */   public MushroomCow getBreedOffspring(ServerLevel debug1, AgableMob debug2) {
/* 235 */     MushroomCow debug3 = (MushroomCow)EntityType.MOOSHROOM.create((Level)debug1);
/* 236 */     debug3.setMushroomType(getOffspringType((MushroomCow)debug2));
/* 237 */     return debug3;
/*     */   }
/*     */   
/*     */   private MushroomType getOffspringType(MushroomCow debug1) {
/* 241 */     MushroomType debug4, debug2 = getMushroomType();
/* 242 */     MushroomType debug3 = debug1.getMushroomType();
/*     */ 
/*     */     
/* 245 */     if (debug2 == debug3 && this.random.nextInt(1024) == 0) {
/* 246 */       debug4 = (debug2 == MushroomType.BROWN) ? MushroomType.RED : MushroomType.BROWN;
/*     */     } else {
/* 248 */       debug4 = this.random.nextBoolean() ? debug2 : debug3;
/*     */     } 
/* 250 */     return debug4;
/*     */   }
/*     */   
/*     */   public enum MushroomType {
/* 254 */     RED("red", Blocks.RED_MUSHROOM.defaultBlockState()),
/* 255 */     BROWN("brown", Blocks.BROWN_MUSHROOM.defaultBlockState());
/*     */     
/*     */     private final String type;
/*     */     private final BlockState blockState;
/*     */     
/*     */     MushroomType(String debug3, BlockState debug4) {
/* 261 */       this.type = debug3;
/* 262 */       this.blockState = debug4;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static MushroomType byType(String debug0) {
/* 270 */       for (MushroomType debug4 : values()) {
/* 271 */         if (debug4.type.equals(debug0)) {
/* 272 */           return debug4;
/*     */         }
/*     */       } 
/*     */       
/* 276 */       return RED;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\animal\MushroomCow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */