/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.math.Quaternion;
/*     */ import com.mojang.math.Vector3f;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResultHolder;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.monster.CrossbowAttackMob;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.AbstractArrow;
/*     */ import net.minecraft.world.entity.projectile.FireworkRocketEntity;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.item.enchantment.Enchantments;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.Vec3;
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
/*     */ public class CrossbowItem
/*     */   extends ProjectileWeaponItem
/*     */   implements Vanishable
/*     */ {
/*     */   private boolean startSoundPlayed;
/*     */   private boolean midLoadSoundPlayed;
/*     */   
/*     */   public CrossbowItem(Item.Properties debug1) {
/*  57 */     super(debug1);
/*  58 */     this.startSoundPlayed = false;
/*  59 */     this.midLoadSoundPlayed = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Predicate<ItemStack> getSupportedHeldProjectiles() {
/*  64 */     return ARROW_OR_FIREWORK;
/*     */   }
/*     */ 
/*     */   
/*     */   public Predicate<ItemStack> getAllSupportedProjectiles() {
/*  69 */     return ARROW_ONLY;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResultHolder<ItemStack> use(Level debug1, Player debug2, InteractionHand debug3) {
/*  74 */     ItemStack debug4 = debug2.getItemInHand(debug3);
/*     */     
/*  76 */     if (isCharged(debug4)) {
/*  77 */       performShooting(debug1, (LivingEntity)debug2, debug3, debug4, getShootingPower(debug4), 1.0F);
/*  78 */       setCharged(debug4, false);
/*  79 */       return InteractionResultHolder.consume(debug4);
/*     */     } 
/*     */     
/*  82 */     if (!debug2.getProjectile(debug4).isEmpty()) {
/*  83 */       if (!isCharged(debug4)) {
/*  84 */         this.startSoundPlayed = false;
/*  85 */         this.midLoadSoundPlayed = false;
/*  86 */         debug2.startUsingItem(debug3);
/*     */       } 
/*  88 */       return InteractionResultHolder.consume(debug4);
/*     */     } 
/*  90 */     return InteractionResultHolder.fail(debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   public void releaseUsing(ItemStack debug1, Level debug2, LivingEntity debug3, int debug4) {
/*  95 */     int debug5 = getUseDuration(debug1) - debug4;
/*  96 */     float debug6 = getPowerForTime(debug5, debug1);
/*     */     
/*  98 */     if (debug6 >= 1.0F && !isCharged(debug1) && 
/*  99 */       tryLoadProjectiles(debug3, debug1)) {
/* 100 */       setCharged(debug1, true);
/* 101 */       SoundSource debug7 = (debug3 instanceof Player) ? SoundSource.PLAYERS : SoundSource.HOSTILE;
/* 102 */       debug2.playSound(null, debug3.getX(), debug3.getY(), debug3.getZ(), SoundEvents.CROSSBOW_LOADING_END, debug7, 1.0F, 1.0F / (random.nextFloat() * 0.5F + 1.0F) + 0.2F);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean tryLoadProjectiles(LivingEntity debug0, ItemStack debug1) {
/* 108 */     int debug2 = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT, debug1);
/* 109 */     int debug3 = (debug2 == 0) ? 1 : 3;
/* 110 */     boolean debug4 = (debug0 instanceof Player && ((Player)debug0).abilities.instabuild);
/*     */     
/* 112 */     ItemStack debug5 = debug0.getProjectile(debug1);
/* 113 */     ItemStack debug6 = debug5.copy();
/* 114 */     for (int debug7 = 0; debug7 < debug3; debug7++) {
/* 115 */       if (debug7 > 0) {
/* 116 */         debug5 = debug6.copy();
/*     */       }
/*     */       
/* 119 */       if (debug5.isEmpty() && debug4) {
/* 120 */         debug5 = new ItemStack(Items.ARROW);
/* 121 */         debug6 = debug5.copy();
/*     */       } 
/*     */       
/* 124 */       if (!loadProjectile(debug0, debug1, debug5, (debug7 > 0), debug4)) {
/* 125 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 129 */     return true;
/*     */   }
/*     */   private static boolean loadProjectile(LivingEntity debug0, ItemStack debug1, ItemStack debug2, boolean debug3, boolean debug4) {
/*     */     ItemStack debug6;
/* 133 */     if (debug2.isEmpty()) {
/* 134 */       return false;
/*     */     }
/* 136 */     boolean debug5 = (debug4 && debug2.getItem() instanceof ArrowItem);
/*     */ 
/*     */     
/* 139 */     if (!debug5 && !debug4 && !debug3) {
/* 140 */       debug6 = debug2.split(1);
/* 141 */       if (debug2.isEmpty() && debug0 instanceof Player)
/*     */       {
/* 143 */         ((Player)debug0).inventory.removeItem(debug2);
/*     */       }
/*     */     } else {
/* 146 */       debug6 = debug2.copy();
/*     */     } 
/*     */     
/* 149 */     addChargedProjectile(debug1, debug6);
/* 150 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean isCharged(ItemStack debug0) {
/* 154 */     CompoundTag debug1 = debug0.getTag();
/* 155 */     return (debug1 != null && debug1.getBoolean("Charged"));
/*     */   }
/*     */   
/*     */   public static void setCharged(ItemStack debug0, boolean debug1) {
/* 159 */     CompoundTag debug2 = debug0.getOrCreateTag();
/* 160 */     debug2.putBoolean("Charged", debug1);
/*     */   }
/*     */   private static void addChargedProjectile(ItemStack debug0, ItemStack debug1) {
/*     */     ListTag debug3;
/* 164 */     CompoundTag debug2 = debug0.getOrCreateTag();
/*     */     
/* 166 */     if (debug2.contains("ChargedProjectiles", 9)) {
/* 167 */       debug3 = debug2.getList("ChargedProjectiles", 10);
/*     */     } else {
/* 169 */       debug3 = new ListTag();
/*     */     } 
/* 171 */     CompoundTag debug4 = new CompoundTag();
/* 172 */     debug1.save(debug4);
/* 173 */     debug3.add(debug4);
/* 174 */     debug2.put("ChargedProjectiles", (Tag)debug3);
/*     */   }
/*     */   
/*     */   private static List<ItemStack> getChargedProjectiles(ItemStack debug0) {
/* 178 */     List<ItemStack> debug1 = Lists.newArrayList();
/* 179 */     CompoundTag debug2 = debug0.getTag();
/* 180 */     if (debug2 != null && 
/* 181 */       debug2.contains("ChargedProjectiles", 9)) {
/* 182 */       ListTag debug3 = debug2.getList("ChargedProjectiles", 10);
/* 183 */       if (debug3 != null) {
/* 184 */         for (int debug4 = 0; debug4 < debug3.size(); debug4++) {
/* 185 */           CompoundTag debug5 = debug3.getCompound(debug4);
/* 186 */           debug1.add(ItemStack.of(debug5));
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 191 */     return debug1;
/*     */   }
/*     */   
/*     */   private static void clearChargedProjectiles(ItemStack debug0) {
/* 195 */     CompoundTag debug1 = debug0.getTag();
/* 196 */     if (debug1 != null) {
/* 197 */       ListTag debug2 = debug1.getList("ChargedProjectiles", 9);
/* 198 */       debug2.clear();
/* 199 */       debug1.put("ChargedProjectiles", (Tag)debug2);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean containsChargedProjectile(ItemStack debug0, Item debug1) {
/* 204 */     return getChargedProjectiles(debug0).stream().anyMatch(debug1 -> (debug1.getItem() == debug0));
/*     */   }
/*     */   private static void shootProjectile(Level debug0, LivingEntity debug1, InteractionHand debug2, ItemStack debug3, ItemStack debug4, float debug5, boolean debug6, float debug7, float debug8, float debug9) {
/*     */     AbstractArrow abstractArrow;
/* 208 */     if (debug0.isClientSide) {
/*     */       return;
/*     */     }
/*     */     
/* 212 */     boolean debug10 = (debug4.getItem() == Items.FIREWORK_ROCKET);
/*     */ 
/*     */     
/* 215 */     if (debug10) {
/* 216 */       FireworkRocketEntity fireworkRocketEntity = new FireworkRocketEntity(debug0, debug4, (Entity)debug1, debug1.getX(), debug1.getEyeY() - 0.15000000596046448D, debug1.getZ(), true);
/*     */     } else {
/* 218 */       abstractArrow = getArrow(debug0, debug1, debug3, debug4);
/* 219 */       if (debug6 || debug9 != 0.0F) {
/* 220 */         abstractArrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
/*     */       }
/*     */     } 
/*     */     
/* 224 */     if (debug1 instanceof CrossbowAttackMob) {
/* 225 */       CrossbowAttackMob debug12 = (CrossbowAttackMob)debug1;
/* 226 */       debug12.shootCrossbowProjectile(debug12.getTarget(), debug3, (Projectile)abstractArrow, debug9);
/*     */     } else {
/* 228 */       Vec3 debug12 = debug1.getUpVector(1.0F);
/* 229 */       Quaternion debug13 = new Quaternion(new Vector3f(debug12), debug9, true);
/* 230 */       Vec3 debug14 = debug1.getViewVector(1.0F);
/* 231 */       Vector3f debug15 = new Vector3f(debug14);
/* 232 */       debug15.transform(debug13);
/* 233 */       abstractArrow.shoot(debug15.x(), debug15.y(), debug15.z(), debug7, debug8);
/*     */     } 
/*     */     
/* 236 */     debug3.hurtAndBreak(debug10 ? 3 : 1, debug1, debug1 -> debug1.broadcastBreakEvent(debug0));
/* 237 */     debug0.addFreshEntity((Entity)abstractArrow);
/* 238 */     debug0.playSound(null, debug1.getX(), debug1.getY(), debug1.getZ(), SoundEvents.CROSSBOW_SHOOT, SoundSource.PLAYERS, 1.0F, debug5);
/*     */   }
/*     */   
/*     */   private static AbstractArrow getArrow(Level debug0, LivingEntity debug1, ItemStack debug2, ItemStack debug3) {
/* 242 */     ArrowItem debug4 = (debug3.getItem() instanceof ArrowItem) ? (ArrowItem)debug3.getItem() : (ArrowItem)Items.ARROW;
/* 243 */     AbstractArrow debug5 = debug4.createArrow(debug0, debug3, debug1);
/* 244 */     if (debug1 instanceof Player) {
/* 245 */       debug5.setCritArrow(true);
/*     */     }
/* 247 */     debug5.setSoundEvent(SoundEvents.CROSSBOW_HIT);
/* 248 */     debug5.setShotFromCrossbow(true);
/*     */     
/* 250 */     int debug6 = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PIERCING, debug2);
/* 251 */     if (debug6 > 0) {
/* 252 */       debug5.setPierceLevel((byte)debug6);
/*     */     }
/*     */     
/* 255 */     return debug5;
/*     */   }
/*     */   
/*     */   public static void performShooting(Level debug0, LivingEntity debug1, InteractionHand debug2, ItemStack debug3, float debug4, float debug5) {
/* 259 */     List<ItemStack> debug6 = getChargedProjectiles(debug3);
/* 260 */     float[] debug7 = getShotPitches(debug1.getRandom());
/*     */     
/* 262 */     for (int debug8 = 0; debug8 < debug6.size(); debug8++) {
/* 263 */       ItemStack debug9 = debug6.get(debug8);
/* 264 */       boolean debug10 = (debug1 instanceof Player && ((Player)debug1).abilities.instabuild);
/*     */       
/* 266 */       if (!debug9.isEmpty())
/*     */       {
/*     */ 
/*     */         
/* 270 */         if (debug8 == 0) {
/* 271 */           shootProjectile(debug0, debug1, debug2, debug3, debug9, debug7[debug8], debug10, debug4, debug5, 0.0F);
/* 272 */         } else if (debug8 == 1) {
/* 273 */           shootProjectile(debug0, debug1, debug2, debug3, debug9, debug7[debug8], debug10, debug4, debug5, -10.0F);
/* 274 */         } else if (debug8 == 2) {
/* 275 */           shootProjectile(debug0, debug1, debug2, debug3, debug9, debug7[debug8], debug10, debug4, debug5, 10.0F);
/*     */         } 
/*     */       }
/*     */     } 
/* 279 */     onCrossbowShot(debug0, debug1, debug3);
/*     */   }
/*     */   
/*     */   private static float[] getShotPitches(Random debug0) {
/* 283 */     boolean debug1 = debug0.nextBoolean();
/* 284 */     return new float[] { 1.0F, getRandomShotPitch(debug1), getRandomShotPitch(!debug1) };
/*     */   }
/*     */   
/*     */   private static float getRandomShotPitch(boolean debug0) {
/* 288 */     float debug1 = debug0 ? 0.63F : 0.43F;
/* 289 */     return 1.0F / (random.nextFloat() * 0.5F + 1.8F) + debug1;
/*     */   }
/*     */   
/*     */   private static void onCrossbowShot(Level debug0, LivingEntity debug1, ItemStack debug2) {
/* 293 */     if (debug1 instanceof ServerPlayer) {
/* 294 */       ServerPlayer debug3 = (ServerPlayer)debug1;
/* 295 */       if (!debug0.isClientSide) {
/* 296 */         CriteriaTriggers.SHOT_CROSSBOW.trigger(debug3, debug2);
/*     */       }
/*     */       
/* 299 */       debug3.awardStat(Stats.ITEM_USED.get(debug2.getItem()));
/*     */     } 
/*     */     
/* 302 */     clearChargedProjectiles(debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUseTick(Level debug1, LivingEntity debug2, ItemStack debug3, int debug4) {
/* 307 */     if (!debug1.isClientSide) {
/* 308 */       int debug5 = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, debug3);
/* 309 */       SoundEvent debug6 = getStartSound(debug5);
/* 310 */       SoundEvent debug7 = (debug5 == 0) ? SoundEvents.CROSSBOW_LOADING_MIDDLE : null;
/* 311 */       float debug8 = (debug3.getUseDuration() - debug4) / getChargeDuration(debug3);
/*     */       
/* 313 */       if (debug8 < 0.2F) {
/* 314 */         this.startSoundPlayed = false;
/* 315 */         this.midLoadSoundPlayed = false;
/*     */       } 
/*     */       
/* 318 */       if (debug8 >= 0.2F && !this.startSoundPlayed) {
/* 319 */         this.startSoundPlayed = true;
/* 320 */         debug1.playSound(null, debug2.getX(), debug2.getY(), debug2.getZ(), debug6, SoundSource.PLAYERS, 0.5F, 1.0F);
/*     */       } 
/*     */       
/* 323 */       if (debug8 >= 0.5F && debug7 != null && !this.midLoadSoundPlayed) {
/* 324 */         this.midLoadSoundPlayed = true;
/* 325 */         debug1.playSound(null, debug2.getX(), debug2.getY(), debug2.getZ(), debug7, SoundSource.PLAYERS, 0.5F, 1.0F);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getUseDuration(ItemStack debug1) {
/* 332 */     return getChargeDuration(debug1) + 3;
/*     */   }
/*     */   
/*     */   public static int getChargeDuration(ItemStack debug0) {
/* 336 */     int debug1 = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, debug0);
/* 337 */     return (debug1 == 0) ? 25 : (25 - 5 * debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public UseAnim getUseAnimation(ItemStack debug1) {
/* 342 */     return UseAnim.CROSSBOW;
/*     */   }
/*     */   
/*     */   private SoundEvent getStartSound(int debug1) {
/* 346 */     switch (debug1) {
/*     */       case 1:
/* 348 */         return SoundEvents.CROSSBOW_QUICK_CHARGE_1;
/*     */       case 2:
/* 350 */         return SoundEvents.CROSSBOW_QUICK_CHARGE_2;
/*     */       case 3:
/* 352 */         return SoundEvents.CROSSBOW_QUICK_CHARGE_3;
/*     */     } 
/* 354 */     return SoundEvents.CROSSBOW_LOADING_START;
/*     */   }
/*     */ 
/*     */   
/*     */   private static float getPowerForTime(int debug0, ItemStack debug1) {
/* 359 */     float debug2 = debug0 / getChargeDuration(debug1);
/* 360 */     if (debug2 > 1.0F) {
/* 361 */       debug2 = 1.0F;
/*     */     }
/* 363 */     return debug2;
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
/*     */   private static float getShootingPower(ItemStack debug0) {
/* 389 */     if (debug0.getItem() == Items.CROSSBOW && containsChargedProjectile(debug0, Items.FIREWORK_ROCKET)) {
/* 390 */       return 1.6F;
/*     */     }
/* 392 */     return 3.15F;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDefaultProjectileRange() {
/* 397 */     return 8;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\CrossbowItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */