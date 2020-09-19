/*     */ package net.minecraft.world.entity.projectile;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class EyeOfEnder extends Entity {
/*  25 */   private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(EyeOfEnder.class, EntityDataSerializers.ITEM_STACK);
/*     */   
/*     */   private double tx;
/*     */   private double ty;
/*     */   private double tz;
/*     */   private int life;
/*     */   private boolean surviveAfterDeath;
/*     */   
/*     */   public EyeOfEnder(EntityType<? extends EyeOfEnder> debug1, Level debug2) {
/*  34 */     super(debug1, debug2);
/*     */   }
/*     */   
/*     */   public EyeOfEnder(Level debug1, double debug2, double debug4, double debug6) {
/*  38 */     this(EntityType.EYE_OF_ENDER, debug1);
/*  39 */     this.life = 0;
/*     */     
/*  41 */     setPos(debug2, debug4, debug6);
/*     */   }
/*     */   
/*     */   public void setItem(ItemStack debug1) {
/*  45 */     if (debug1.getItem() != Items.ENDER_EYE || debug1.hasTag()) {
/*  46 */       getEntityData().set(DATA_ITEM_STACK, Util.make(debug1.copy(), debug0 -> debug0.setCount(1)));
/*     */     }
/*     */   }
/*     */   
/*     */   private ItemStack getItemRaw() {
/*  51 */     return (ItemStack)getEntityData().get(DATA_ITEM_STACK);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItem() {
/*  56 */     ItemStack debug1 = getItemRaw();
/*  57 */     return debug1.isEmpty() ? new ItemStack((ItemLike)Items.ENDER_EYE) : debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/*  62 */     getEntityData().define(DATA_ITEM_STACK, ItemStack.EMPTY);
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
/*     */   public void signalTo(BlockPos debug1) {
/*  76 */     double debug2 = debug1.getX();
/*  77 */     int debug4 = debug1.getY();
/*  78 */     double debug5 = debug1.getZ();
/*     */     
/*  80 */     double debug7 = debug2 - getX();
/*  81 */     double debug9 = debug5 - getZ();
/*  82 */     float debug11 = Mth.sqrt(debug7 * debug7 + debug9 * debug9);
/*     */     
/*  84 */     if (debug11 > 12.0F) {
/*  85 */       this.tx = getX() + debug7 / debug11 * 12.0D;
/*  86 */       this.tz = getZ() + debug9 / debug11 * 12.0D;
/*  87 */       this.ty = getY() + 8.0D;
/*     */     } else {
/*  89 */       this.tx = debug2;
/*  90 */       this.ty = debug4;
/*  91 */       this.tz = debug5;
/*     */     } 
/*     */     
/*  94 */     this.life = 0;
/*  95 */     this.surviveAfterDeath = (this.random.nextInt(5) > 0);
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
/*     */   public void tick() {
/* 112 */     super.tick();
/*     */     
/* 114 */     Vec3 debug1 = getDeltaMovement();
/* 115 */     double debug2 = getX() + debug1.x;
/* 116 */     double debug4 = getY() + debug1.y;
/* 117 */     double debug6 = getZ() + debug1.z;
/*     */     
/* 119 */     float debug8 = Mth.sqrt(getHorizontalDistanceSqr(debug1));
/* 120 */     this.xRot = Projectile.lerpRotation(this.xRotO, (float)(Mth.atan2(debug1.y, debug8) * 57.2957763671875D));
/* 121 */     this.yRot = Projectile.lerpRotation(this.yRotO, (float)(Mth.atan2(debug1.x, debug1.z) * 57.2957763671875D));
/*     */     
/* 123 */     if (!this.level.isClientSide) {
/* 124 */       double d1 = this.tx - debug2;
/* 125 */       double debug11 = this.tz - debug6;
/* 126 */       float debug13 = (float)Math.sqrt(d1 * d1 + debug11 * debug11);
/* 127 */       float debug14 = (float)Mth.atan2(debug11, d1);
/* 128 */       double debug15 = Mth.lerp(0.0025D, debug8, debug13);
/* 129 */       double debug17 = debug1.y;
/* 130 */       if (debug13 < 1.0F) {
/* 131 */         debug15 *= 0.8D;
/* 132 */         debug17 *= 0.8D;
/*     */       } 
/* 134 */       int debug19 = (getY() < this.ty) ? 1 : -1;
/* 135 */       debug1 = new Vec3(Math.cos(debug14) * debug15, debug17 + (debug19 - debug17) * 0.014999999664723873D, Math.sin(debug14) * debug15);
/* 136 */       setDeltaMovement(debug1);
/*     */     } 
/*     */     
/* 139 */     float debug9 = 0.25F;
/* 140 */     if (isInWater()) {
/* 141 */       for (int debug10 = 0; debug10 < 4; debug10++) {
/* 142 */         this.level.addParticle((ParticleOptions)ParticleTypes.BUBBLE, debug2 - debug1.x * 0.25D, debug4 - debug1.y * 0.25D, debug6 - debug1.z * 0.25D, debug1.x, debug1.y, debug1.z);
/*     */       }
/*     */     } else {
/* 145 */       this.level.addParticle((ParticleOptions)ParticleTypes.PORTAL, debug2 - debug1.x * 0.25D + this.random.nextDouble() * 0.6D - 0.3D, debug4 - debug1.y * 0.25D - 0.5D, debug6 - debug1.z * 0.25D + this.random.nextDouble() * 0.6D - 0.3D, debug1.x, debug1.y, debug1.z);
/*     */     } 
/*     */     
/* 148 */     if (!this.level.isClientSide) {
/* 149 */       setPos(debug2, debug4, debug6);
/*     */       
/* 151 */       this.life++;
/* 152 */       if (this.life > 80 && !this.level.isClientSide) {
/* 153 */         playSound(SoundEvents.ENDER_EYE_DEATH, 1.0F, 1.0F);
/* 154 */         remove();
/* 155 */         if (this.surviveAfterDeath) {
/* 156 */           this.level.addFreshEntity((Entity)new ItemEntity(this.level, getX(), getY(), getZ(), getItem()));
/*     */         } else {
/* 158 */           this.level.levelEvent(2003, blockPosition(), 0);
/*     */         } 
/*     */       } 
/*     */     } else {
/* 162 */       setPosRaw(debug2, debug4, debug6);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 168 */     ItemStack debug2 = getItemRaw();
/* 169 */     if (!debug2.isEmpty()) {
/* 170 */       debug1.put("Item", (Tag)debug2.save(new CompoundTag()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 176 */     ItemStack debug2 = ItemStack.of(debug1.getCompound("Item"));
/* 177 */     setItem(debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getBrightness() {
/* 182 */     return 1.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAttackable() {
/* 187 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Packet<?> getAddEntityPacket() {
/* 192 */     return (Packet<?>)new ClientboundAddEntityPacket(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\projectile\EyeOfEnder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */