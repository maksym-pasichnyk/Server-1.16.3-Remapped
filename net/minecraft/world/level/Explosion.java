/*     */ package net.minecraft.world.level;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectListIterator;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.item.PrimedTnt;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.enchantment.ProtectionEnchantment;
/*     */ import net.minecraft.world.level.block.BaseFireBlock;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Explosion {
/*  44 */   private static final ExplosionDamageCalculator EXPLOSION_DAMAGE_CALCULATOR = new ExplosionDamageCalculator(); private final boolean fire;
/*     */   private final BlockInteraction blockInteraction;
/*     */   
/*  47 */   public enum BlockInteraction { NONE,
/*  48 */     BREAK,
/*  49 */     DESTROY; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  56 */   private final Random random = new Random();
/*     */   
/*     */   private final Level level;
/*     */   private final double x;
/*     */   private final double y;
/*     */   private final double z;
/*     */   @Nullable
/*     */   private final Entity source;
/*     */   private final float radius;
/*     */   private final DamageSource damageSource;
/*     */   private final ExplosionDamageCalculator damageCalculator;
/*  67 */   private final List<BlockPos> toBlow = Lists.newArrayList();
/*  68 */   private final Map<Player, Vec3> hitPlayers = Maps.newHashMap();
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
/*     */   public Explosion(Level debug1, @Nullable Entity debug2, @Nullable DamageSource debug3, @Nullable ExplosionDamageCalculator debug4, double debug5, double debug7, double debug9, float debug11, boolean debug12, BlockInteraction debug13) {
/*  88 */     this.level = debug1;
/*  89 */     this.source = debug2;
/*  90 */     this.radius = debug11;
/*  91 */     this.x = debug5;
/*  92 */     this.y = debug7;
/*  93 */     this.z = debug9;
/*  94 */     this.fire = debug12;
/*  95 */     this.blockInteraction = debug13;
/*  96 */     this.damageSource = (debug3 == null) ? DamageSource.explosion(this) : debug3;
/*  97 */     this.damageCalculator = (debug4 == null) ? makeDamageCalculator(debug2) : debug4;
/*     */   }
/*     */   
/*     */   private ExplosionDamageCalculator makeDamageCalculator(@Nullable Entity debug1) {
/* 101 */     return (debug1 == null) ? EXPLOSION_DAMAGE_CALCULATOR : new EntityBasedExplosionDamageCalculator(debug1);
/*     */   }
/*     */   
/*     */   public static float getSeenPercent(Vec3 debug0, Entity debug1) {
/* 105 */     AABB debug2 = debug1.getBoundingBox();
/* 106 */     double debug3 = 1.0D / ((debug2.maxX - debug2.minX) * 2.0D + 1.0D);
/* 107 */     double debug5 = 1.0D / ((debug2.maxY - debug2.minY) * 2.0D + 1.0D);
/* 108 */     double debug7 = 1.0D / ((debug2.maxZ - debug2.minZ) * 2.0D + 1.0D);
/*     */     
/* 110 */     double debug9 = (1.0D - Math.floor(1.0D / debug3) * debug3) / 2.0D;
/* 111 */     double debug11 = (1.0D - Math.floor(1.0D / debug7) * debug7) / 2.0D;
/*     */     
/* 113 */     if (debug3 < 0.0D || debug5 < 0.0D || debug7 < 0.0D) {
/* 114 */       return 0.0F;
/*     */     }
/* 116 */     int debug13 = 0;
/* 117 */     int debug14 = 0; float debug15;
/* 118 */     for (debug15 = 0.0F; debug15 <= 1.0F; debug15 = (float)(debug15 + debug3)) {
/* 119 */       float debug16; for (debug16 = 0.0F; debug16 <= 1.0F; debug16 = (float)(debug16 + debug5)) {
/* 120 */         float debug17; for (debug17 = 0.0F; debug17 <= 1.0F; debug17 = (float)(debug17 + debug7)) {
/* 121 */           double debug18 = Mth.lerp(debug15, debug2.minX, debug2.maxX);
/* 122 */           double debug20 = Mth.lerp(debug16, debug2.minY, debug2.maxY);
/* 123 */           double debug22 = Mth.lerp(debug17, debug2.minZ, debug2.maxZ);
/* 124 */           Vec3 debug24 = new Vec3(debug18 + debug9, debug20, debug22 + debug11);
/* 125 */           if (debug1.level.clip(new ClipContext(debug24, debug0, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, debug1)).getType() == HitResult.Type.MISS) {
/* 126 */             debug13++;
/*     */           }
/* 128 */           debug14++;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 133 */     return debug13 / debug14;
/*     */   }
/*     */   
/*     */   public void explode() {
/* 137 */     Set<BlockPos> debug1 = Sets.newHashSet();
/*     */     
/* 139 */     int debug2 = 16;
/* 140 */     for (int i = 0; i < 16; i++) {
/* 141 */       for (int j = 0; j < 16; j++) {
/* 142 */         for (int k = 0; k < 16; k++) {
/* 143 */           if (i == 0 || i == 15 || j == 0 || j == 15 || k == 0 || k == 15) {
/*     */ 
/*     */ 
/*     */             
/* 147 */             double d1 = (i / 15.0F * 2.0F - 1.0F);
/* 148 */             double d2 = (j / 15.0F * 2.0F - 1.0F);
/* 149 */             double d3 = (k / 15.0F * 2.0F - 1.0F);
/* 150 */             double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
/*     */             
/* 152 */             d1 /= d4;
/* 153 */             d2 /= d4;
/* 154 */             d3 /= d4;
/*     */             
/* 156 */             float debug14 = this.radius * (0.7F + this.level.random.nextFloat() * 0.6F);
/* 157 */             double debug15 = this.x;
/* 158 */             double debug17 = this.y;
/* 159 */             double debug19 = this.z;
/*     */             
/* 161 */             float debug21 = 0.3F;
/* 162 */             while (debug14 > 0.0F) {
/* 163 */               BlockPos debug22 = new BlockPos(debug15, debug17, debug19);
/* 164 */               BlockState debug23 = this.level.getBlockState(debug22);
/* 165 */               FluidState debug24 = this.level.getFluidState(debug22);
/*     */               
/* 167 */               Optional<Float> debug25 = this.damageCalculator.getBlockExplosionResistance(this, this.level, debug22, debug23, debug24);
/* 168 */               if (debug25.isPresent()) {
/* 169 */                 debug14 -= (((Float)debug25.get()).floatValue() + 0.3F) * 0.3F;
/*     */               }
/*     */               
/* 172 */               if (debug14 > 0.0F && this.damageCalculator.shouldBlockExplode(this, this.level, debug22, debug23, debug14)) {
/* 173 */                 debug1.add(debug22);
/*     */               }
/*     */               
/* 176 */               debug15 += d1 * 0.30000001192092896D;
/* 177 */               debug17 += d2 * 0.30000001192092896D;
/* 178 */               debug19 += d3 * 0.30000001192092896D;
/* 179 */               debug14 -= 0.22500001F;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 185 */     this.toBlow.addAll(debug1);
/*     */     
/* 187 */     float debug3 = this.radius * 2.0F;
/*     */     
/* 189 */     int debug4 = Mth.floor(this.x - debug3 - 1.0D);
/* 190 */     int debug5 = Mth.floor(this.x + debug3 + 1.0D);
/* 191 */     int debug6 = Mth.floor(this.y - debug3 - 1.0D);
/* 192 */     int debug7 = Mth.floor(this.y + debug3 + 1.0D);
/* 193 */     int debug8 = Mth.floor(this.z - debug3 - 1.0D);
/* 194 */     int debug9 = Mth.floor(this.z + debug3 + 1.0D);
/* 195 */     List<Entity> debug10 = this.level.getEntities(this.source, new AABB(debug4, debug6, debug8, debug5, debug7, debug9));
/* 196 */     Vec3 debug11 = new Vec3(this.x, this.y, this.z);
/*     */     
/* 198 */     for (int debug12 = 0; debug12 < debug10.size(); debug12++) {
/* 199 */       Entity debug13 = debug10.get(debug12);
/* 200 */       if (!debug13.ignoreExplosion()) {
/*     */ 
/*     */         
/* 203 */         double debug14 = (Mth.sqrt(debug13.distanceToSqr(debug11)) / debug3);
/*     */         
/* 205 */         if (debug14 <= 1.0D) {
/* 206 */           double debug16 = debug13.getX() - this.x;
/* 207 */           double debug18 = ((debug13 instanceof PrimedTnt) ? debug13.getY() : debug13.getEyeY()) - this.y;
/* 208 */           double debug20 = debug13.getZ() - this.z;
/*     */           
/* 210 */           double debug22 = Mth.sqrt(debug16 * debug16 + debug18 * debug18 + debug20 * debug20);
/* 211 */           if (debug22 != 0.0D) {
/*     */ 
/*     */ 
/*     */             
/* 215 */             debug16 /= debug22;
/* 216 */             debug18 /= debug22;
/* 217 */             debug20 /= debug22;
/*     */             
/* 219 */             double debug24 = getSeenPercent(debug11, debug13);
/* 220 */             double debug26 = (1.0D - debug14) * debug24;
/* 221 */             debug13.hurt(getDamageSource(), (int)((debug26 * debug26 + debug26) / 2.0D * 7.0D * debug3 + 1.0D));
/*     */             
/* 223 */             double debug28 = debug26;
/* 224 */             if (debug13 instanceof LivingEntity) {
/* 225 */               debug28 = ProtectionEnchantment.getExplosionKnockbackAfterDampener((LivingEntity)debug13, debug26);
/*     */             }
/* 227 */             debug13.setDeltaMovement(debug13.getDeltaMovement().add(debug16 * debug28, debug18 * debug28, debug20 * debug28));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 233 */             if (debug13 instanceof Player) {
/* 234 */               Player debug30 = (Player)debug13;
/* 235 */               if (!debug30.isSpectator() && (!debug30.isCreative() || !debug30.abilities.flying))
/* 236 */                 this.hitPlayers.put(debug30, new Vec3(debug16 * debug26, debug18 * debug26, debug20 * debug26)); 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public void finalizeExplosion(boolean debug1) {
/* 244 */     if (this.level.isClientSide) {
/* 245 */       this.level.playLocalSound(this.x, this.y, this.z, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0F, (1.0F + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2F) * 0.7F, false);
/*     */     }
/* 247 */     boolean debug2 = (this.blockInteraction != BlockInteraction.NONE);
/* 248 */     if (debug1) {
/* 249 */       if (this.radius < 2.0F || !debug2) {
/* 250 */         this.level.addParticle((ParticleOptions)ParticleTypes.EXPLOSION, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
/*     */       } else {
/* 252 */         this.level.addParticle((ParticleOptions)ParticleTypes.EXPLOSION_EMITTER, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
/*     */       } 
/*     */     }
/*     */     
/* 256 */     if (debug2) {
/* 257 */       ObjectArrayList<Pair<ItemStack, BlockPos>> debug3 = new ObjectArrayList();
/*     */       
/* 259 */       Collections.shuffle(this.toBlow, this.level.random);
/*     */       
/* 261 */       for (BlockPos debug5 : this.toBlow) {
/* 262 */         BlockState debug6 = this.level.getBlockState(debug5);
/* 263 */         Block debug7 = debug6.getBlock();
/*     */         
/* 265 */         if (!debug6.isAir()) {
/* 266 */           BlockPos debug8 = debug5.immutable();
/* 267 */           this.level.getProfiler().push("explosion_blocks");
/* 268 */           if (debug7.dropFromExplosion(this) && this.level instanceof ServerLevel) {
/* 269 */             BlockEntity debug9 = debug7.isEntityBlock() ? this.level.getBlockEntity(debug5) : null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 275 */             LootContext.Builder debug10 = (new LootContext.Builder((ServerLevel)this.level)).withRandom(this.level.random).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf((Vec3i)debug5)).withParameter(LootContextParams.TOOL, ItemStack.EMPTY).withOptionalParameter(LootContextParams.BLOCK_ENTITY, debug9).withOptionalParameter(LootContextParams.THIS_ENTITY, this.source);
/*     */             
/* 277 */             if (this.blockInteraction == BlockInteraction.DESTROY) {
/* 278 */               debug10.withParameter(LootContextParams.EXPLOSION_RADIUS, Float.valueOf(this.radius));
/*     */             }
/*     */             
/* 281 */             debug6.getDrops(debug10).forEach(debug2 -> addBlockDrops(debug0, debug2, debug1));
/*     */           } 
/* 283 */           this.level.setBlock(debug5, Blocks.AIR.defaultBlockState(), 3);
/* 284 */           debug7.wasExploded(this.level, debug5, this);
/* 285 */           this.level.getProfiler().pop();
/*     */         } 
/*     */       } 
/*     */       
/* 289 */       for (ObjectListIterator<Pair<ItemStack, BlockPos>> objectListIterator = debug3.iterator(); objectListIterator.hasNext(); ) { Pair<ItemStack, BlockPos> debug5 = objectListIterator.next();
/* 290 */         Block.popResource(this.level, (BlockPos)debug5.getSecond(), (ItemStack)debug5.getFirst()); }
/*     */     
/*     */     } 
/* 293 */     if (this.fire) {
/* 294 */       for (BlockPos debug4 : this.toBlow) {
/* 295 */         if (this.random.nextInt(3) == 0 && this.level.getBlockState(debug4).isAir() && this.level.getBlockState(debug4.below()).isSolidRender(this.level, debug4.below())) {
/* 296 */           this.level.setBlockAndUpdate(debug4, BaseFireBlock.getState(this.level, debug4));
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static void addBlockDrops(ObjectArrayList<Pair<ItemStack, BlockPos>> debug0, ItemStack debug1, BlockPos debug2) {
/* 303 */     int debug3 = debug0.size();
/* 304 */     for (int debug4 = 0; debug4 < debug3; debug4++) {
/* 305 */       Pair<ItemStack, BlockPos> debug5 = (Pair<ItemStack, BlockPos>)debug0.get(debug4);
/* 306 */       ItemStack debug6 = (ItemStack)debug5.getFirst();
/* 307 */       if (ItemEntity.areMergable(debug6, debug1)) {
/* 308 */         ItemStack debug7 = ItemEntity.merge(debug6, debug1, 16);
/* 309 */         debug0.set(debug4, Pair.of(debug7, debug5.getSecond()));
/* 310 */         if (debug1.isEmpty()) {
/*     */           return;
/*     */         }
/*     */       } 
/*     */     } 
/* 315 */     debug0.add(Pair.of(debug1, debug2));
/*     */   }
/*     */   
/*     */   public DamageSource getDamageSource() {
/* 319 */     return this.damageSource;
/*     */   }
/*     */   
/*     */   public Map<Player, Vec3> getHitPlayers() {
/* 323 */     return this.hitPlayers;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public LivingEntity getSourceMob() {
/* 328 */     if (this.source == null) {
/* 329 */       return null;
/*     */     }
/* 331 */     if (this.source instanceof PrimedTnt) {
/* 332 */       return ((PrimedTnt)this.source).getOwner();
/*     */     }
/* 334 */     if (this.source instanceof LivingEntity) {
/* 335 */       return (LivingEntity)this.source;
/*     */     }
/* 337 */     if (this.source instanceof Projectile) {
/* 338 */       Entity debug1 = ((Projectile)this.source).getOwner();
/* 339 */       if (debug1 instanceof LivingEntity) {
/* 340 */         return (LivingEntity)debug1;
/*     */       }
/*     */     } 
/*     */     
/* 344 */     return null;
/*     */   }
/*     */   
/*     */   public void clearToBlow() {
/* 348 */     this.toBlow.clear();
/*     */   }
/*     */   
/*     */   public List<BlockPos> getToBlow() {
/* 352 */     return this.toBlow;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\Explosion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */