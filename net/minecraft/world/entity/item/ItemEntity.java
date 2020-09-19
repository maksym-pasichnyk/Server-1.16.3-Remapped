/*     */ package net.minecraft.world.entity.item;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class ItemEntity extends Entity {
/*  35 */   private static final EntityDataAccessor<ItemStack> DATA_ITEM = SynchedEntityData.defineId(ItemEntity.class, EntityDataSerializers.ITEM_STACK);
/*     */ 
/*     */   
/*     */   private int age;
/*     */ 
/*     */   
/*     */   private int pickupDelay;
/*     */   
/*  43 */   private int health = 5;
/*     */   private UUID thrower;
/*     */   private UUID owner;
/*     */   public final float bobOffs;
/*     */   
/*     */   public ItemEntity(EntityType<? extends ItemEntity> debug1, Level debug2) {
/*  49 */     super(debug1, debug2);
/*  50 */     this.bobOffs = (float)(Math.random() * Math.PI * 2.0D);
/*     */   }
/*     */   
/*     */   public ItemEntity(Level debug1, double debug2, double debug4, double debug6) {
/*  54 */     this(EntityType.ITEM, debug1);
/*  55 */     setPos(debug2, debug4, debug6);
/*     */     
/*  57 */     this.yRot = this.random.nextFloat() * 360.0F;
/*     */     
/*  59 */     setDeltaMovement(this.random
/*  60 */         .nextDouble() * 0.2D - 0.1D, 0.2D, this.random
/*     */         
/*  62 */         .nextDouble() * 0.2D - 0.1D);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemEntity(Level debug1, double debug2, double debug4, double debug6, ItemStack debug8) {
/*  67 */     this(debug1, debug2, debug4, debug6);
/*  68 */     setItem(debug8);
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
/*     */   protected boolean isMovementNoisy() {
/*  81 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/*  86 */     getEntityData().define(DATA_ITEM, ItemStack.EMPTY);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  91 */     if (getItem().isEmpty()) {
/*  92 */       remove();
/*     */       return;
/*     */     } 
/*  95 */     super.tick();
/*  96 */     if (this.pickupDelay > 0 && this.pickupDelay != 32767) {
/*  97 */       this.pickupDelay--;
/*     */     }
/*  99 */     this.xo = getX();
/* 100 */     this.yo = getY();
/* 101 */     this.zo = getZ();
/*     */     
/* 103 */     Vec3 debug1 = getDeltaMovement();
/*     */ 
/*     */     
/* 106 */     float debug2 = getEyeHeight() - 0.11111111F;
/* 107 */     if (isInWater() && getFluidHeight((Tag)FluidTags.WATER) > debug2) {
/* 108 */       setUnderwaterMovement();
/* 109 */     } else if (isInLava() && getFluidHeight((Tag)FluidTags.LAVA) > debug2) {
/* 110 */       setUnderLavaMovement();
/* 111 */     } else if (!isNoGravity()) {
/* 112 */       setDeltaMovement(getDeltaMovement().add(0.0D, -0.04D, 0.0D));
/*     */     } 
/*     */     
/* 115 */     if (this.level.isClientSide) {
/* 116 */       this.noPhysics = false;
/*     */     } else {
/* 118 */       this.noPhysics = !this.level.noCollision(this);
/* 119 */       if (this.noPhysics) {
/* 120 */         moveTowardsClosestSpace(getX(), ((getBoundingBox()).minY + (getBoundingBox()).maxY) / 2.0D, getZ());
/*     */       }
/*     */     } 
/* 123 */     if (!this.onGround || getHorizontalDistanceSqr(getDeltaMovement()) > 9.999999747378752E-6D || (this.tickCount + getId()) % 4 == 0) {
/* 124 */       move(MoverType.SELF, getDeltaMovement());
/*     */       
/* 126 */       float f = 0.98F;
/* 127 */       if (this.onGround) {
/* 128 */         f = this.level.getBlockState(new BlockPos(getX(), getY() - 1.0D, getZ())).getBlock().getFriction() * 0.98F;
/*     */       }
/*     */       
/* 131 */       setDeltaMovement(getDeltaMovement().multiply(f, 0.98D, f));
/*     */ 
/*     */       
/* 134 */       if (this.onGround) {
/* 135 */         Vec3 vec3 = getDeltaMovement();
/* 136 */         if (vec3.y < 0.0D) {
/* 137 */           setDeltaMovement(vec3.multiply(1.0D, -0.5D, 1.0D));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 142 */     boolean debug3 = (Mth.floor(this.xo) != Mth.floor(getX()) || Mth.floor(this.yo) != Mth.floor(getY()) || Mth.floor(this.zo) != Mth.floor(getZ()));
/* 143 */     int debug4 = debug3 ? 2 : 40;
/*     */     
/* 145 */     if (this.tickCount % debug4 == 0) {
/* 146 */       if (this.level.getFluidState(blockPosition()).is((Tag)FluidTags.LAVA) && !fireImmune()) {
/* 147 */         playSound(SoundEvents.GENERIC_BURN, 0.4F, 2.0F + this.random.nextFloat() * 0.4F);
/*     */       }
/*     */       
/* 150 */       if (!this.level.isClientSide && isMergable()) {
/* 151 */         mergeWithNeighbours();
/*     */       }
/*     */     } 
/*     */     
/* 155 */     if (this.age != -32768) {
/* 156 */       this.age++;
/*     */     }
/*     */ 
/*     */     
/* 160 */     this.hasImpulse |= updateInWaterStateAndDoFluidPushing();
/*     */     
/* 162 */     if (!this.level.isClientSide) {
/*     */ 
/*     */ 
/*     */       
/* 166 */       double debug5 = getDeltaMovement().subtract(debug1).lengthSqr();
/* 167 */       if (debug5 > 0.01D) {
/* 168 */         this.hasImpulse = true;
/*     */       }
/*     */     } 
/*     */     
/* 172 */     if (!this.level.isClientSide && this.age >= 6000) {
/* 173 */       remove();
/*     */     }
/*     */   }
/*     */   
/*     */   private void setUnderwaterMovement() {
/* 178 */     Vec3 debug1 = getDeltaMovement();
/*     */ 
/*     */     
/* 181 */     setDeltaMovement(debug1.x * 0.9900000095367432D, debug1.y + ((debug1.y < 0.05999999865889549D) ? 5.0E-4F : 0.0F), debug1.z * 0.9900000095367432D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setUnderLavaMovement() {
/* 189 */     Vec3 debug1 = getDeltaMovement();
/*     */ 
/*     */     
/* 192 */     setDeltaMovement(debug1.x * 0.949999988079071D, debug1.y + ((debug1.y < 0.05999999865889549D) ? 5.0E-4F : 0.0F), debug1.z * 0.949999988079071D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void mergeWithNeighbours() {
/* 200 */     if (!isMergable()) {
/*     */       return;
/*     */     }
/* 203 */     List<ItemEntity> debug1 = this.level.getEntitiesOfClass(ItemEntity.class, getBoundingBox().inflate(0.5D, 0.0D, 0.5D), debug1 -> (debug1 != this && debug1.isMergable()));
/* 204 */     for (ItemEntity debug3 : debug1) {
/* 205 */       if (debug3.isMergable()) {
/* 206 */         tryToMerge(debug3);
/* 207 */         if (this.removed) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isMergable() {
/* 215 */     ItemStack debug1 = getItem();
/* 216 */     return (isAlive() && this.pickupDelay != 32767 && this.age != -32768 && this.age < 6000 && debug1.getCount() < debug1.getMaxStackSize());
/*     */   }
/*     */   
/*     */   private void tryToMerge(ItemEntity debug1) {
/* 220 */     ItemStack debug2 = getItem();
/* 221 */     ItemStack debug3 = debug1.getItem();
/*     */     
/* 223 */     if (!Objects.equals(getOwner(), debug1.getOwner()) || !areMergable(debug2, debug3)) {
/*     */       return;
/*     */     }
/*     */     
/* 227 */     if (debug3.getCount() < debug2.getCount()) {
/* 228 */       merge(this, debug2, debug1, debug3);
/*     */     } else {
/* 230 */       merge(debug1, debug3, this, debug2);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean areMergable(ItemStack debug0, ItemStack debug1) {
/* 235 */     if (debug1.getItem() != debug0.getItem()) {
/* 236 */       return false;
/*     */     }
/* 238 */     if (debug1.getCount() + debug0.getCount() > debug1.getMaxStackSize()) {
/* 239 */       return false;
/*     */     }
/* 241 */     if ((debug1.hasTag() ^ debug0.hasTag()) != 0) {
/* 242 */       return false;
/*     */     }
/* 244 */     if (debug1.hasTag() && !debug1.getTag().equals(debug0.getTag())) {
/* 245 */       return false;
/*     */     }
/* 247 */     return true;
/*     */   }
/*     */   
/*     */   public static ItemStack merge(ItemStack debug0, ItemStack debug1, int debug2) {
/* 251 */     int debug3 = Math.min(Math.min(debug0.getMaxStackSize(), debug2) - debug0.getCount(), debug1.getCount());
/* 252 */     ItemStack debug4 = debug0.copy();
/* 253 */     debug4.grow(debug3);
/* 254 */     debug1.shrink(debug3);
/* 255 */     return debug4;
/*     */   }
/*     */   
/*     */   private static void merge(ItemEntity debug0, ItemStack debug1, ItemStack debug2) {
/* 259 */     ItemStack debug3 = merge(debug1, debug2, 64);
/* 260 */     debug0.setItem(debug3);
/*     */   }
/*     */   
/*     */   private static void merge(ItemEntity debug0, ItemStack debug1, ItemEntity debug2, ItemStack debug3) {
/* 264 */     merge(debug0, debug1, debug3);
/* 265 */     debug0.pickupDelay = Math.max(debug0.pickupDelay, debug2.pickupDelay);
/* 266 */     debug0.age = Math.min(debug0.age, debug2.age);
/*     */     
/* 268 */     if (debug3.isEmpty()) {
/* 269 */       debug2.remove();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean fireImmune() {
/* 275 */     return (getItem().getItem().isFireResistant() || super.fireImmune());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hurt(DamageSource debug1, float debug2) {
/* 285 */     if (isInvulnerableTo(debug1)) {
/* 286 */       return false;
/*     */     }
/* 288 */     if (!getItem().isEmpty() && getItem().getItem() == Items.NETHER_STAR && debug1.isExplosion()) {
/* 289 */       return false;
/*     */     }
/* 291 */     if (!getItem().getItem().canBeHurtBy(debug1)) {
/* 292 */       return false;
/*     */     }
/* 294 */     markHurt();
/* 295 */     this.health = (int)(this.health - debug2);
/* 296 */     if (this.health <= 0) {
/* 297 */       remove();
/*     */     }
/* 299 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 304 */     debug1.putShort("Health", (short)this.health);
/* 305 */     debug1.putShort("Age", (short)this.age);
/* 306 */     debug1.putShort("PickupDelay", (short)this.pickupDelay);
/* 307 */     if (getThrower() != null) {
/* 308 */       debug1.putUUID("Thrower", getThrower());
/*     */     }
/* 310 */     if (getOwner() != null) {
/* 311 */       debug1.putUUID("Owner", getOwner());
/*     */     }
/* 313 */     if (!getItem().isEmpty()) {
/* 314 */       debug1.put("Item", (Tag)getItem().save(new CompoundTag()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 320 */     this.health = debug1.getShort("Health");
/* 321 */     this.age = debug1.getShort("Age");
/* 322 */     if (debug1.contains("PickupDelay")) {
/* 323 */       this.pickupDelay = debug1.getShort("PickupDelay");
/*     */     }
/* 325 */     if (debug1.hasUUID("Owner")) {
/* 326 */       this.owner = debug1.getUUID("Owner");
/*     */     }
/* 328 */     if (debug1.hasUUID("Thrower")) {
/* 329 */       this.thrower = debug1.getUUID("Thrower");
/*     */     }
/* 331 */     CompoundTag debug2 = debug1.getCompound("Item");
/* 332 */     setItem(ItemStack.of(debug2));
/* 333 */     if (getItem().isEmpty()) {
/* 334 */       remove();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerTouch(Player debug1) {
/* 340 */     if (this.level.isClientSide) {
/*     */       return;
/*     */     }
/*     */     
/* 344 */     ItemStack debug2 = getItem();
/* 345 */     Item debug3 = debug2.getItem();
/* 346 */     int debug4 = debug2.getCount();
/* 347 */     if (this.pickupDelay == 0 && (this.owner == null || this.owner.equals(debug1.getUUID())) && debug1.inventory.add(debug2)) {
/* 348 */       debug1.take(this, debug4);
/* 349 */       if (debug2.isEmpty()) {
/* 350 */         remove();
/*     */ 
/*     */         
/* 353 */         debug2.setCount(debug4);
/*     */       } 
/* 355 */       debug1.awardStat(Stats.ITEM_PICKED_UP.get(debug3), debug4);
/* 356 */       debug1.onItemPickup(this);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getName() {
/* 362 */     Component debug1 = getCustomName();
/* 363 */     if (debug1 != null) {
/* 364 */       return debug1;
/*     */     }
/*     */     
/* 367 */     return (Component)new TranslatableComponent(getItem().getDescriptionId());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAttackable() {
/* 372 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Entity changeDimension(ServerLevel debug1) {
/* 378 */     Entity debug2 = super.changeDimension(debug1);
/*     */     
/* 380 */     if (!this.level.isClientSide && debug2 instanceof ItemEntity) {
/* 381 */       ((ItemEntity)debug2).mergeWithNeighbours();
/*     */     }
/* 383 */     return debug2;
/*     */   }
/*     */   
/*     */   public ItemStack getItem() {
/* 387 */     return (ItemStack)getEntityData().get(DATA_ITEM);
/*     */   }
/*     */   
/*     */   public void setItem(ItemStack debug1) {
/* 391 */     getEntityData().set(DATA_ITEM, debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> debug1) {
/* 396 */     super.onSyncedDataUpdated(debug1);
/* 397 */     if (DATA_ITEM.equals(debug1)) {
/* 398 */       getItem().setEntityRepresentation(this);
/*     */     }
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public UUID getOwner() {
/* 404 */     return this.owner;
/*     */   }
/*     */   
/*     */   public void setOwner(@Nullable UUID debug1) {
/* 408 */     this.owner = debug1;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public UUID getThrower() {
/* 413 */     return this.thrower;
/*     */   }
/*     */   
/*     */   public void setThrower(@Nullable UUID debug1) {
/* 417 */     this.thrower = debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultPickUpDelay() {
/* 426 */     this.pickupDelay = 10;
/*     */   }
/*     */   
/*     */   public void setNoPickUpDelay() {
/* 430 */     this.pickupDelay = 0;
/*     */   }
/*     */   
/*     */   public void setNeverPickUp() {
/* 434 */     this.pickupDelay = 32767;
/*     */   }
/*     */   
/*     */   public void setPickUpDelay(int debug1) {
/* 438 */     this.pickupDelay = debug1;
/*     */   }
/*     */   
/*     */   public boolean hasPickUpDelay() {
/* 442 */     return (this.pickupDelay > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExtendedLifetime() {
/* 450 */     this.age = -6000;
/*     */   }
/*     */   
/*     */   public void makeFakeItem() {
/* 454 */     setNeverPickUp();
/* 455 */     this.age = 5999;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Packet<?> getAddEntityPacket() {
/* 464 */     return (Packet<?>)new ClientboundAddEntityPacket(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\item\ItemEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */