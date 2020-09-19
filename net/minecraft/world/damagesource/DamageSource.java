/*     */ package net.minecraft.world.damagesource;
/*     */ 
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.AbstractArrow;
/*     */ import net.minecraft.world.entity.projectile.Fireball;
/*     */ import net.minecraft.world.entity.projectile.FireworkRocketEntity;
/*     */ import net.minecraft.world.entity.projectile.WitherSkull;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public class DamageSource
/*     */ {
/*  19 */   public static final DamageSource IN_FIRE = (new DamageSource("inFire")).bypassArmor().setIsFire();
/*  20 */   public static final DamageSource LIGHTNING_BOLT = new DamageSource("lightningBolt");
/*  21 */   public static final DamageSource ON_FIRE = (new DamageSource("onFire")).bypassArmor().setIsFire();
/*  22 */   public static final DamageSource LAVA = (new DamageSource("lava")).setIsFire();
/*  23 */   public static final DamageSource HOT_FLOOR = (new DamageSource("hotFloor")).setIsFire();
/*  24 */   public static final DamageSource IN_WALL = (new DamageSource("inWall")).bypassArmor();
/*  25 */   public static final DamageSource CRAMMING = (new DamageSource("cramming")).bypassArmor();
/*  26 */   public static final DamageSource DROWN = (new DamageSource("drown")).bypassArmor();
/*  27 */   public static final DamageSource STARVE = (new DamageSource("starve")).bypassArmor().bypassMagic();
/*  28 */   public static final DamageSource CACTUS = new DamageSource("cactus");
/*  29 */   public static final DamageSource FALL = (new DamageSource("fall")).bypassArmor();
/*  30 */   public static final DamageSource FLY_INTO_WALL = (new DamageSource("flyIntoWall")).bypassArmor();
/*  31 */   public static final DamageSource OUT_OF_WORLD = (new DamageSource("outOfWorld")).bypassArmor().bypassInvul();
/*  32 */   public static final DamageSource GENERIC = (new DamageSource("generic")).bypassArmor();
/*  33 */   public static final DamageSource MAGIC = (new DamageSource("magic")).bypassArmor().setMagic();
/*  34 */   public static final DamageSource WITHER = (new DamageSource("wither")).bypassArmor();
/*  35 */   public static final DamageSource ANVIL = new DamageSource("anvil");
/*  36 */   public static final DamageSource FALLING_BLOCK = new DamageSource("fallingBlock");
/*  37 */   public static final DamageSource DRAGON_BREATH = (new DamageSource("dragonBreath")).bypassArmor();
/*  38 */   public static final DamageSource DRY_OUT = new DamageSource("dryout");
/*  39 */   public static final DamageSource SWEET_BERRY_BUSH = new DamageSource("sweetBerryBush"); private boolean bypassArmor;
/*     */   
/*     */   public static DamageSource sting(LivingEntity debug0) {
/*  42 */     return new EntityDamageSource("sting", (Entity)debug0);
/*     */   }
/*     */   private boolean bypassInvul; private boolean bypassMagic;
/*     */   public static DamageSource mobAttack(LivingEntity debug0) {
/*  46 */     return new EntityDamageSource("mob", (Entity)debug0);
/*     */   }
/*     */   
/*     */   public static DamageSource indirectMobAttack(Entity debug0, LivingEntity debug1) {
/*  50 */     return new IndirectEntityDamageSource("mob", debug0, (Entity)debug1);
/*     */   }
/*     */   
/*     */   public static DamageSource playerAttack(Player debug0) {
/*  54 */     return new EntityDamageSource("player", (Entity)debug0);
/*     */   }
/*     */   
/*     */   public static DamageSource arrow(AbstractArrow debug0, @Nullable Entity debug1) {
/*  58 */     return (new IndirectEntityDamageSource("arrow", (Entity)debug0, debug1)).setProjectile();
/*     */   }
/*     */   
/*     */   public static DamageSource trident(Entity debug0, @Nullable Entity debug1) {
/*  62 */     return (new IndirectEntityDamageSource("trident", debug0, debug1)).setProjectile();
/*     */   }
/*     */   
/*     */   public static DamageSource fireworks(FireworkRocketEntity debug0, @Nullable Entity debug1) {
/*  66 */     return (new IndirectEntityDamageSource("fireworks", (Entity)debug0, debug1)).setExplosion();
/*     */   }
/*     */   
/*     */   public static DamageSource fireball(Fireball debug0, @Nullable Entity debug1) {
/*  70 */     if (debug1 == null) {
/*  71 */       return (new IndirectEntityDamageSource("onFire", (Entity)debug0, (Entity)debug0)).setIsFire().setProjectile();
/*     */     }
/*  73 */     return (new IndirectEntityDamageSource("fireball", (Entity)debug0, debug1)).setIsFire().setProjectile();
/*     */   }
/*     */   
/*     */   public static DamageSource witherSkull(WitherSkull debug0, Entity debug1) {
/*  77 */     return (new IndirectEntityDamageSource("witherSkull", (Entity)debug0, debug1)).setProjectile();
/*     */   }
/*     */   
/*     */   public static DamageSource thrown(Entity debug0, @Nullable Entity debug1) {
/*  81 */     return (new IndirectEntityDamageSource("thrown", debug0, debug1)).setProjectile();
/*     */   }
/*     */   
/*     */   public static DamageSource indirectMagic(Entity debug0, @Nullable Entity debug1) {
/*  85 */     return (new IndirectEntityDamageSource("indirectMagic", debug0, debug1)).bypassArmor().setMagic();
/*     */   }
/*     */   
/*     */   public static DamageSource thorns(Entity debug0) {
/*  89 */     return (new EntityDamageSource("thorns", debug0)).setThorns().setMagic();
/*     */   }
/*     */   
/*     */   public static DamageSource explosion(@Nullable Explosion debug0) {
/*  93 */     return explosion((debug0 != null) ? debug0.getSourceMob() : null);
/*     */   }
/*     */   
/*     */   public static DamageSource explosion(@Nullable LivingEntity debug0) {
/*  97 */     if (debug0 != null) {
/*  98 */       return (new EntityDamageSource("explosion.player", (Entity)debug0)).setScalesWithDifficulty().setExplosion();
/*     */     }
/* 100 */     return (new DamageSource("explosion")).setScalesWithDifficulty().setExplosion();
/*     */   }
/*     */ 
/*     */   
/*     */   public static DamageSource badRespawnPointExplosion() {
/* 105 */     return new BadRespawnPointDamage();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 112 */   private float exhaustion = 0.1F;
/*     */   private boolean isFireSource;
/*     */   private boolean isProjectile;
/*     */   private boolean scalesWithDifficulty;
/*     */   private boolean isMagic;
/*     */   private boolean isExplosion;
/*     */   public final String msgId;
/*     */   
/*     */   public String toString() {
/* 121 */     return "DamageSource (" + this.msgId + ")";
/*     */   }
/*     */   
/*     */   public boolean isProjectile() {
/* 125 */     return this.isProjectile;
/*     */   }
/*     */   
/*     */   public DamageSource setProjectile() {
/* 129 */     this.isProjectile = true;
/* 130 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isExplosion() {
/* 134 */     return this.isExplosion;
/*     */   }
/*     */   
/*     */   public DamageSource setExplosion() {
/* 138 */     this.isExplosion = true;
/* 139 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isBypassArmor() {
/* 143 */     return this.bypassArmor;
/*     */   }
/*     */   
/*     */   public float getFoodExhaustion() {
/* 147 */     return this.exhaustion;
/*     */   }
/*     */   
/*     */   public boolean isBypassInvul() {
/* 151 */     return this.bypassInvul;
/*     */   }
/*     */   
/*     */   public boolean isBypassMagic() {
/* 155 */     return this.bypassMagic;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected DamageSource(String debug1) {
/* 161 */     this.msgId = debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Entity getDirectEntity() {
/* 170 */     return getEntity();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Entity getEntity() {
/* 179 */     return null;
/*     */   }
/*     */   
/*     */   protected DamageSource bypassArmor() {
/* 183 */     this.bypassArmor = true;
/*     */     
/* 185 */     this.exhaustion = 0.0F;
/* 186 */     return this;
/*     */   }
/*     */   
/*     */   protected DamageSource bypassInvul() {
/* 190 */     this.bypassInvul = true;
/* 191 */     return this;
/*     */   }
/*     */   
/*     */   protected DamageSource bypassMagic() {
/* 195 */     this.bypassMagic = true;
/*     */     
/* 197 */     this.exhaustion = 0.0F;
/* 198 */     return this;
/*     */   }
/*     */   
/*     */   protected DamageSource setIsFire() {
/* 202 */     this.isFireSource = true;
/* 203 */     return this;
/*     */   }
/*     */   
/*     */   public Component getLocalizedDeathMessage(LivingEntity debug1) {
/* 207 */     LivingEntity debug2 = debug1.getKillCredit();
/* 208 */     String debug3 = "death.attack." + this.msgId;
/* 209 */     String debug4 = debug3 + ".player";
/*     */     
/* 211 */     if (debug2 != null) {
/* 212 */       return (Component)new TranslatableComponent(debug4, new Object[] { debug1.getDisplayName(), debug2.getDisplayName() });
/*     */     }
/* 214 */     return (Component)new TranslatableComponent(debug3, new Object[] { debug1.getDisplayName() });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFire() {
/* 219 */     return this.isFireSource;
/*     */   }
/*     */   
/*     */   public String getMsgId() {
/* 223 */     return this.msgId;
/*     */   }
/*     */   
/*     */   public DamageSource setScalesWithDifficulty() {
/* 227 */     this.scalesWithDifficulty = true;
/* 228 */     return this;
/*     */   }
/*     */   
/*     */   public boolean scalesWithDifficulty() {
/* 232 */     return this.scalesWithDifficulty;
/*     */   }
/*     */   
/*     */   public boolean isMagic() {
/* 236 */     return this.isMagic;
/*     */   }
/*     */   
/*     */   public DamageSource setMagic() {
/* 240 */     this.isMagic = true;
/* 241 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isCreativePlayer() {
/* 245 */     Entity debug1 = getEntity();
/* 246 */     return (debug1 instanceof Player && ((Player)debug1).abilities.instabuild);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Vec3 getSourcePosition() {
/* 251 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\damagesource\DamageSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */