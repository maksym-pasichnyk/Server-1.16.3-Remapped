/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import java.util.Map;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddExperienceOrbPacket;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.item.enchantment.Enchantments;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public class ExperienceOrb
/*     */   extends Entity
/*     */ {
/*     */   public int tickCount;
/*     */   public int age;
/*     */   public int throwTime;
/*  27 */   private int health = 5;
/*     */   private int value;
/*     */   private Player followingPlayer;
/*     */   private int followingTime;
/*     */   
/*     */   public ExperienceOrb(Level debug1, double debug2, double debug4, double debug6, int debug8) {
/*  33 */     this(EntityType.EXPERIENCE_ORB, debug1);
/*  34 */     setPos(debug2, debug4, debug6);
/*     */     
/*  36 */     this.yRot = (float)(this.random.nextDouble() * 360.0D);
/*     */     
/*  38 */     setDeltaMovement((this.random
/*  39 */         .nextDouble() * 0.20000000298023224D - 0.10000000149011612D) * 2.0D, this.random
/*  40 */         .nextDouble() * 0.2D * 2.0D, (this.random
/*  41 */         .nextDouble() * 0.20000000298023224D - 0.10000000149011612D) * 2.0D);
/*     */ 
/*     */     
/*  44 */     this.value = debug8;
/*     */   }
/*     */   
/*     */   public ExperienceOrb(EntityType<? extends ExperienceOrb> debug1, Level debug2) {
/*  48 */     super(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isMovementNoisy() {
/*  53 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {}
/*     */ 
/*     */   
/*     */   public void tick() {
/*  62 */     super.tick();
/*  63 */     if (this.throwTime > 0) {
/*  64 */       this.throwTime--;
/*     */     }
/*  66 */     this.xo = getX();
/*  67 */     this.yo = getY();
/*  68 */     this.zo = getZ();
/*     */     
/*  70 */     if (isEyeInFluid((Tag<Fluid>)FluidTags.WATER)) {
/*  71 */       setUnderwaterMovement();
/*  72 */     } else if (!isNoGravity()) {
/*  73 */       setDeltaMovement(getDeltaMovement().add(0.0D, -0.03D, 0.0D));
/*     */     } 
/*     */     
/*  76 */     if (this.level.getFluidState(blockPosition()).is((Tag)FluidTags.LAVA)) {
/*  77 */       setDeltaMovement(((this.random
/*  78 */           .nextFloat() - this.random.nextFloat()) * 0.2F), 0.20000000298023224D, ((this.random
/*     */           
/*  80 */           .nextFloat() - this.random.nextFloat()) * 0.2F));
/*     */       
/*  82 */       playSound(SoundEvents.GENERIC_BURN, 0.4F, 2.0F + this.random.nextFloat() * 0.4F);
/*     */     } 
/*  84 */     if (!this.level.noCollision(getBoundingBox())) {
/*  85 */       moveTowardsClosestSpace(getX(), ((getBoundingBox()).minY + (getBoundingBox()).maxY) / 2.0D, getZ());
/*     */     }
/*     */     
/*  88 */     double debug1 = 8.0D;
/*     */ 
/*     */     
/*  91 */     if (this.followingTime < this.tickCount - 20 + getId() % 100) {
/*  92 */       if (this.followingPlayer == null || this.followingPlayer.distanceToSqr(this) > 64.0D) {
/*  93 */         this.followingPlayer = this.level.getNearestPlayer(this, 8.0D);
/*     */       }
/*  95 */       this.followingTime = this.tickCount;
/*     */     } 
/*     */     
/*  98 */     if (this.followingPlayer != null && this.followingPlayer.isSpectator()) {
/*  99 */       this.followingPlayer = null;
/*     */     }
/*     */     
/* 102 */     if (this.followingPlayer != null) {
/*     */ 
/*     */ 
/*     */       
/* 106 */       Vec3 vec3 = new Vec3(this.followingPlayer.getX() - getX(), this.followingPlayer.getY() + this.followingPlayer.getEyeHeight() / 2.0D - getY(), this.followingPlayer.getZ() - getZ());
/*     */       
/* 108 */       double debug4 = vec3.lengthSqr();
/* 109 */       if (debug4 < 64.0D) {
/* 110 */         double debug6 = 1.0D - Math.sqrt(debug4) / 8.0D;
/*     */         
/* 112 */         setDeltaMovement(getDeltaMovement().add(vec3.normalize().scale(debug6 * debug6 * 0.1D)));
/*     */       } 
/*     */     } 
/*     */     
/* 116 */     move(MoverType.SELF, getDeltaMovement());
/*     */     
/* 118 */     float debug3 = 0.98F;
/* 119 */     if (this.onGround) {
/* 120 */       debug3 = this.level.getBlockState(new BlockPos(getX(), getY() - 1.0D, getZ())).getBlock().getFriction() * 0.98F;
/*     */     }
/*     */     
/* 123 */     setDeltaMovement(getDeltaMovement().multiply(debug3, 0.98D, debug3));
/*     */     
/* 125 */     if (this.onGround) {
/* 126 */       setDeltaMovement(getDeltaMovement().multiply(1.0D, -0.9D, 1.0D));
/*     */     }
/*     */     
/* 129 */     this.tickCount++;
/*     */     
/* 131 */     this.age++;
/* 132 */     if (this.age >= 6000) {
/* 133 */       remove();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void setUnderwaterMovement() {
/* 139 */     Vec3 debug1 = getDeltaMovement();
/*     */     
/* 141 */     setDeltaMovement(debug1.x * 0.9900000095367432D, 
/*     */         
/* 143 */         Math.min(debug1.y + 5.000000237487257E-4D, 0.05999999865889549D), debug1.z * 0.9900000095367432D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doWaterSplashEffect() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hurt(DamageSource debug1, float debug2) {
/* 159 */     if (isInvulnerableTo(debug1)) {
/* 160 */       return false;
/*     */     }
/* 162 */     markHurt();
/* 163 */     this.health = (int)(this.health - debug2);
/* 164 */     if (this.health <= 0) {
/* 165 */       remove();
/*     */     }
/* 167 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 172 */     debug1.putShort("Health", (short)this.health);
/* 173 */     debug1.putShort("Age", (short)this.age);
/* 174 */     debug1.putShort("Value", (short)this.value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 179 */     this.health = debug1.getShort("Health");
/* 180 */     this.age = debug1.getShort("Age");
/* 181 */     this.value = debug1.getShort("Value");
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerTouch(Player debug1) {
/* 186 */     if (this.level.isClientSide) {
/*     */       return;
/*     */     }
/*     */     
/* 190 */     if (this.throwTime == 0 && debug1.takeXpDelay == 0) {
/* 191 */       debug1.takeXpDelay = 2;
/* 192 */       debug1.take(this, 1);
/* 193 */       Map.Entry<EquipmentSlot, ItemStack> debug2 = EnchantmentHelper.getRandomItemWith(Enchantments.MENDING, (LivingEntity)debug1, ItemStack::isDamaged);
/* 194 */       if (debug2 != null) {
/* 195 */         ItemStack debug3 = debug2.getValue();
/* 196 */         if (!debug3.isEmpty() && debug3.isDamaged()) {
/* 197 */           int debug4 = Math.min(xpToDurability(this.value), debug3.getDamageValue());
/* 198 */           this.value -= durabilityToXp(debug4);
/* 199 */           debug3.setDamageValue(debug3.getDamageValue() - debug4);
/*     */         } 
/*     */       } 
/* 202 */       if (this.value > 0) {
/* 203 */         debug1.giveExperiencePoints(this.value);
/*     */       }
/* 205 */       remove();
/*     */     } 
/*     */   }
/*     */   
/*     */   private int durabilityToXp(int debug1) {
/* 210 */     return debug1 / 2;
/*     */   }
/*     */   
/*     */   private int xpToDurability(int debug1) {
/* 214 */     return debug1 * 2;
/*     */   }
/*     */   
/*     */   public int getValue() {
/* 218 */     return this.value;
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
/*     */   public static int getExperienceValue(int debug0) {
/* 256 */     if (debug0 >= 2477)
/* 257 */       return 2477; 
/* 258 */     if (debug0 >= 1237)
/* 259 */       return 1237; 
/* 260 */     if (debug0 >= 617)
/* 261 */       return 617; 
/* 262 */     if (debug0 >= 307)
/* 263 */       return 307; 
/* 264 */     if (debug0 >= 149)
/* 265 */       return 149; 
/* 266 */     if (debug0 >= 73)
/* 267 */       return 73; 
/* 268 */     if (debug0 >= 37)
/* 269 */       return 37; 
/* 270 */     if (debug0 >= 17)
/* 271 */       return 17; 
/* 272 */     if (debug0 >= 7)
/* 273 */       return 7; 
/* 274 */     if (debug0 >= 3) {
/* 275 */       return 3;
/*     */     }
/*     */     
/* 278 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAttackable() {
/* 283 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Packet<?> getAddEntityPacket() {
/* 288 */     return (Packet<?>)new ClientboundAddExperienceOrbPacket(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ExperienceOrb.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */