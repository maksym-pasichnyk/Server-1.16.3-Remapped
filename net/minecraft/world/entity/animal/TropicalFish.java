/*     */ package net.minecraft.world.entity.animal;
/*     */ 
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ 
/*     */ 
/*     */ public class TropicalFish
/*     */   extends AbstractSchoolingFish
/*     */ {
/*  28 */   private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT = SynchedEntityData.defineId(TropicalFish.class, EntityDataSerializers.INT);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  34 */   private static final ResourceLocation[] BASE_TEXTURE_LOCATIONS = new ResourceLocation[] { new ResourceLocation("textures/entity/fish/tropical_a.png"), new ResourceLocation("textures/entity/fish/tropical_b.png") };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  39 */   private static final ResourceLocation[] PATTERN_A_TEXTURE_LOCATIONS = new ResourceLocation[] { new ResourceLocation("textures/entity/fish/tropical_a_pattern_1.png"), new ResourceLocation("textures/entity/fish/tropical_a_pattern_2.png"), new ResourceLocation("textures/entity/fish/tropical_a_pattern_3.png"), new ResourceLocation("textures/entity/fish/tropical_a_pattern_4.png"), new ResourceLocation("textures/entity/fish/tropical_a_pattern_5.png"), new ResourceLocation("textures/entity/fish/tropical_a_pattern_6.png") };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  48 */   private static final ResourceLocation[] PATTERN_B_TEXTURE_LOCATIONS = new ResourceLocation[] { new ResourceLocation("textures/entity/fish/tropical_b_pattern_1.png"), new ResourceLocation("textures/entity/fish/tropical_b_pattern_2.png"), new ResourceLocation("textures/entity/fish/tropical_b_pattern_3.png"), new ResourceLocation("textures/entity/fish/tropical_b_pattern_4.png"), new ResourceLocation("textures/entity/fish/tropical_b_pattern_5.png"), new ResourceLocation("textures/entity/fish/tropical_b_pattern_6.png") };
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
/*  60 */   public static final int[] COMMON_VARIANTS = new int[] { 
/*  61 */       calculateVariant(Pattern.STRIPEY, DyeColor.ORANGE, DyeColor.GRAY), 
/*  62 */       calculateVariant(Pattern.FLOPPER, DyeColor.GRAY, DyeColor.GRAY), 
/*  63 */       calculateVariant(Pattern.FLOPPER, DyeColor.GRAY, DyeColor.BLUE), 
/*  64 */       calculateVariant(Pattern.CLAYFISH, DyeColor.WHITE, DyeColor.GRAY), 
/*  65 */       calculateVariant(Pattern.SUNSTREAK, DyeColor.BLUE, DyeColor.GRAY), 
/*  66 */       calculateVariant(Pattern.KOB, DyeColor.ORANGE, DyeColor.WHITE), 
/*  67 */       calculateVariant(Pattern.SPOTTY, DyeColor.PINK, DyeColor.LIGHT_BLUE), 
/*  68 */       calculateVariant(Pattern.BLOCKFISH, DyeColor.PURPLE, DyeColor.YELLOW), 
/*  69 */       calculateVariant(Pattern.CLAYFISH, DyeColor.WHITE, DyeColor.RED), 
/*  70 */       calculateVariant(Pattern.SPOTTY, DyeColor.WHITE, DyeColor.YELLOW), 
/*  71 */       calculateVariant(Pattern.GLITTER, DyeColor.WHITE, DyeColor.GRAY), 
/*  72 */       calculateVariant(Pattern.CLAYFISH, DyeColor.WHITE, DyeColor.ORANGE), 
/*  73 */       calculateVariant(Pattern.DASHER, DyeColor.CYAN, DyeColor.PINK), 
/*  74 */       calculateVariant(Pattern.BRINELY, DyeColor.LIME, DyeColor.LIGHT_BLUE), 
/*  75 */       calculateVariant(Pattern.BETTY, DyeColor.RED, DyeColor.WHITE), 
/*  76 */       calculateVariant(Pattern.SNOOPER, DyeColor.GRAY, DyeColor.RED), 
/*  77 */       calculateVariant(Pattern.BLOCKFISH, DyeColor.RED, DyeColor.WHITE), 
/*  78 */       calculateVariant(Pattern.FLOPPER, DyeColor.WHITE, DyeColor.YELLOW), 
/*  79 */       calculateVariant(Pattern.KOB, DyeColor.RED, DyeColor.WHITE), 
/*  80 */       calculateVariant(Pattern.SUNSTREAK, DyeColor.GRAY, DyeColor.WHITE), 
/*  81 */       calculateVariant(Pattern.DASHER, DyeColor.CYAN, DyeColor.YELLOW), 
/*  82 */       calculateVariant(Pattern.FLOPPER, DyeColor.YELLOW, DyeColor.YELLOW) };
/*     */   
/*     */   enum Pattern
/*     */   {
/*  86 */     KOB(0, 0),
/*  87 */     SUNSTREAK(0, 1),
/*  88 */     SNOOPER(0, 2),
/*  89 */     DASHER(0, 3),
/*  90 */     BRINELY(0, 4),
/*  91 */     SPOTTY(0, 5),
/*  92 */     FLOPPER(1, 0),
/*  93 */     STRIPEY(1, 1),
/*  94 */     GLITTER(1, 2),
/*  95 */     BLOCKFISH(1, 3),
/*  96 */     BETTY(1, 4),
/*  97 */     CLAYFISH(1, 5);
/*     */     
/*     */     private final int base;
/*     */     private final int index;
/* 101 */     private static final Pattern[] VALUES = values();
/*     */     
/*     */     Pattern(int debug3, int debug4) {
/* 104 */       this.base = debug3;
/* 105 */       this.index = debug4;
/*     */     } static {
/*     */     
/*     */     } public int getBase() {
/* 109 */       return this.base;
/*     */     }
/*     */     
/*     */     public int getIndex() {
/* 113 */       return this.index;
/*     */     }
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
/*     */   private static int calculateVariant(Pattern debug0, DyeColor debug1, DyeColor debug2) {
/* 126 */     return debug0.getBase() & 0xFF | (debug0.getIndex() & 0xFF) << 8 | (debug1.getId() & 0xFF) << 16 | (debug2.getId() & 0xFF) << 24;
/*     */   }
/*     */   
/*     */   private boolean isSchool = true;
/*     */   
/*     */   public TropicalFish(EntityType<? extends TropicalFish> debug1, Level debug2) {
/* 132 */     super((EntityType)debug1, debug2);
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
/*     */   protected void defineSynchedData() {
/* 155 */     super.defineSynchedData();
/*     */     
/* 157 */     this.entityData.define(DATA_ID_TYPE_VARIANT, Integer.valueOf(0));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 162 */     super.addAdditionalSaveData(debug1);
/*     */     
/* 164 */     debug1.putInt("Variant", getVariant());
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 169 */     super.readAdditionalSaveData(debug1);
/*     */     
/* 171 */     setVariant(debug1.getInt("Variant"));
/*     */   }
/*     */   
/*     */   public void setVariant(int debug1) {
/* 175 */     this.entityData.set(DATA_ID_TYPE_VARIANT, Integer.valueOf(debug1));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isMaxGroupSizeReached(int debug1) {
/* 180 */     return !this.isSchool;
/*     */   }
/*     */   
/*     */   public int getVariant() {
/* 184 */     return ((Integer)this.entityData.get(DATA_ID_TYPE_VARIANT)).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveToBucketTag(ItemStack debug1) {
/* 189 */     super.saveToBucketTag(debug1);
/*     */     
/* 191 */     CompoundTag debug2 = debug1.getOrCreateTag();
/* 192 */     debug2.putInt("BucketVariantTag", getVariant());
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack getBucketItemStack() {
/* 197 */     return new ItemStack((ItemLike)Items.TROPICAL_FISH_BUCKET);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 202 */     return SoundEvents.TROPICAL_FISH_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 207 */     return SoundEvents.TROPICAL_FISH_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 212 */     return SoundEvents.TROPICAL_FISH_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getFlopSound() {
/* 217 */     return SoundEvents.TROPICAL_FISH_FLOP;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor debug1, DifficultyInstance debug2, MobSpawnType debug3, @Nullable SpawnGroupData debug4, @Nullable CompoundTag debug5) {
/*     */     int debug6, debug7, debug8, debug9;
/* 263 */     debug4 = super.finalizeSpawn(debug1, debug2, debug3, debug4, debug5);
/*     */     
/* 265 */     if (debug5 != null && debug5.contains("BucketVariantTag", 3)) {
/* 266 */       setVariant(debug5.getInt("BucketVariantTag"));
/* 267 */       return debug4;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 276 */     if (debug4 instanceof TropicalFishGroupData) {
/* 277 */       TropicalFishGroupData debug10 = (TropicalFishGroupData)debug4;
/* 278 */       debug6 = debug10.base;
/* 279 */       debug7 = debug10.pattern;
/* 280 */       debug8 = debug10.baseColor;
/* 281 */       debug9 = debug10.patternColor;
/* 282 */     } else if (this.random.nextFloat() < 0.9D) {
/*     */       
/* 284 */       int debug10 = Util.getRandom(COMMON_VARIANTS, this.random);
/* 285 */       debug6 = debug10 & 0xFF;
/* 286 */       debug7 = (debug10 & 0xFF00) >> 8;
/* 287 */       debug8 = (debug10 & 0xFF0000) >> 16;
/* 288 */       debug9 = (debug10 & 0xFF000000) >> 24;
/* 289 */       debug4 = new TropicalFishGroupData(this, debug6, debug7, debug8, debug9);
/*     */     } else {
/* 291 */       this.isSchool = false;
/* 292 */       debug6 = this.random.nextInt(2);
/* 293 */       debug7 = this.random.nextInt(6);
/* 294 */       debug8 = this.random.nextInt(15);
/* 295 */       debug9 = this.random.nextInt(15);
/*     */     } 
/*     */     
/* 298 */     setVariant(debug6 | debug7 << 8 | debug8 << 16 | debug9 << 24);
/*     */     
/* 300 */     return debug4;
/*     */   }
/*     */   
/*     */   static class TropicalFishGroupData extends AbstractSchoolingFish.SchoolSpawnGroupData {
/*     */     private final int base;
/*     */     private final int pattern;
/*     */     private final int baseColor;
/*     */     private final int patternColor;
/*     */     
/*     */     private TropicalFishGroupData(TropicalFish debug1, int debug2, int debug3, int debug4, int debug5) {
/* 310 */       super(debug1);
/* 311 */       this.base = debug2;
/* 312 */       this.pattern = debug3;
/* 313 */       this.baseColor = debug4;
/* 314 */       this.patternColor = debug5;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\animal\TropicalFish.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */