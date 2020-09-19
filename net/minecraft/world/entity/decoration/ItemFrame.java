/*     */ package net.minecraft.world.entity.decoration;
/*     */ 
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.MapItem;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.DiodeBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.apache.commons.lang3.Validate;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class ItemFrame extends HangingEntity {
/*  40 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*  42 */   private static final EntityDataAccessor<ItemStack> DATA_ITEM = SynchedEntityData.defineId(ItemFrame.class, EntityDataSerializers.ITEM_STACK);
/*  43 */   private static final EntityDataAccessor<Integer> DATA_ROTATION = SynchedEntityData.defineId(ItemFrame.class, EntityDataSerializers.INT);
/*     */ 
/*     */   
/*  46 */   private float dropChance = 1.0F;
/*     */   private boolean fixed;
/*     */   
/*     */   public ItemFrame(EntityType<? extends ItemFrame> debug1, Level debug2) {
/*  50 */     super((EntityType)debug1, debug2);
/*     */   }
/*     */   
/*     */   public ItemFrame(Level debug1, BlockPos debug2, Direction debug3) {
/*  54 */     super(EntityType.ITEM_FRAME, debug1, debug2);
/*  55 */     setDirection(debug3);
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getEyeHeight(Pose debug1, EntityDimensions debug2) {
/*  60 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/*  65 */     getEntityData().define(DATA_ITEM, ItemStack.EMPTY);
/*  66 */     getEntityData().define(DATA_ROTATION, Integer.valueOf(0));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setDirection(Direction debug1) {
/*  71 */     Validate.notNull(debug1);
/*     */     
/*  73 */     this.direction = debug1;
/*  74 */     if (debug1.getAxis().isHorizontal()) {
/*  75 */       this.xRot = 0.0F;
/*  76 */       this.yRot = (this.direction.get2DDataValue() * 90);
/*     */     } else {
/*  78 */       this.xRot = (-90 * debug1.getAxisDirection().getStep());
/*  79 */       this.yRot = 0.0F;
/*     */     } 
/*  81 */     this.xRotO = this.xRot;
/*  82 */     this.yRotO = this.yRot;
/*     */     
/*  84 */     recalculateBoundingBox();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void recalculateBoundingBox() {
/*  89 */     if (this.direction == null) {
/*     */       return;
/*     */     }
/*     */     
/*  93 */     double debug1 = 0.46875D;
/*  94 */     double debug3 = this.pos.getX() + 0.5D - this.direction.getStepX() * 0.46875D;
/*  95 */     double debug5 = this.pos.getY() + 0.5D - this.direction.getStepY() * 0.46875D;
/*  96 */     double debug7 = this.pos.getZ() + 0.5D - this.direction.getStepZ() * 0.46875D;
/*  97 */     setPosRaw(debug3, debug5, debug7);
/*     */     
/*  99 */     double debug9 = getWidth();
/* 100 */     double debug11 = getHeight();
/* 101 */     double debug13 = getWidth();
/*     */     
/* 103 */     Direction.Axis debug15 = this.direction.getAxis();
/* 104 */     switch (debug15) {
/*     */       case X:
/* 106 */         debug9 = 1.0D;
/*     */         break;
/*     */       case Y:
/* 109 */         debug11 = 1.0D;
/*     */         break;
/*     */       case Z:
/* 112 */         debug13 = 1.0D;
/*     */         break;
/*     */     } 
/*     */     
/* 116 */     debug9 /= 32.0D;
/* 117 */     debug11 /= 32.0D;
/* 118 */     debug13 /= 32.0D;
/* 119 */     setBoundingBox(new AABB(debug3 - debug9, debug5 - debug11, debug7 - debug13, debug3 + debug9, debug5 + debug11, debug7 + debug13));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean survives() {
/* 124 */     if (this.fixed) {
/* 125 */       return true;
/*     */     }
/*     */     
/* 128 */     if (!this.level.noCollision(this)) {
/* 129 */       return false;
/*     */     }
/*     */     
/* 132 */     BlockState debug1 = this.level.getBlockState(this.pos.relative(this.direction.getOpposite()));
/* 133 */     if (!debug1.getMaterial().isSolid() && (!this.direction.getAxis().isHorizontal() || !DiodeBlock.isDiode(debug1))) {
/* 134 */       return false;
/*     */     }
/*     */     
/* 137 */     return this.level.getEntities(this, getBoundingBox(), HANGING_ENTITY).isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void move(MoverType debug1, Vec3 debug2) {
/* 142 */     if (!this.fixed) {
/* 143 */       super.move(debug1, debug2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void push(double debug1, double debug3, double debug5) {
/* 149 */     if (!this.fixed) {
/* 150 */       super.push(debug1, debug3, debug5);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPickRadius() {
/* 156 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public void kill() {
/* 161 */     removeFramedMap(getItem());
/* 162 */     super.kill();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurt(DamageSource debug1, float debug2) {
/* 167 */     if (this.fixed) {
/* 168 */       if (debug1 == DamageSource.OUT_OF_WORLD || debug1.isCreativePlayer()) {
/* 169 */         return super.hurt(debug1, debug2);
/*     */       }
/* 171 */       return false;
/*     */     } 
/*     */     
/* 174 */     if (isInvulnerableTo(debug1)) {
/* 175 */       return false;
/*     */     }
/*     */     
/* 178 */     if (!debug1.isExplosion() && !getItem().isEmpty()) {
/* 179 */       if (!this.level.isClientSide) {
/* 180 */         dropItem(debug1.getEntity(), false);
/* 181 */         playSound(SoundEvents.ITEM_FRAME_REMOVE_ITEM, 1.0F, 1.0F);
/*     */       } 
/* 183 */       return true;
/*     */     } 
/* 185 */     return super.hurt(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWidth() {
/* 190 */     return 12;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/* 195 */     return 12;
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
/*     */   public void dropItem(@Nullable Entity debug1) {
/* 207 */     playSound(SoundEvents.ITEM_FRAME_BREAK, 1.0F, 1.0F);
/* 208 */     dropItem(debug1, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void playPlacementSound() {
/* 213 */     playSound(SoundEvents.ITEM_FRAME_PLACE, 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   private void dropItem(@Nullable Entity debug1, boolean debug2) {
/* 217 */     if (this.fixed) {
/*     */       return;
/*     */     }
/*     */     
/* 221 */     ItemStack debug3 = getItem();
/* 222 */     setItem(ItemStack.EMPTY);
/*     */     
/* 224 */     if (!this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
/* 225 */       if (debug1 == null) {
/* 226 */         removeFramedMap(debug3);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 231 */     if (debug1 instanceof Player) {
/* 232 */       Player debug4 = (Player)debug1;
/*     */       
/* 234 */       if (debug4.abilities.instabuild) {
/* 235 */         removeFramedMap(debug3);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 240 */     if (debug2) {
/* 241 */       spawnAtLocation((ItemLike)Items.ITEM_FRAME);
/*     */     }
/* 243 */     if (!debug3.isEmpty()) {
/* 244 */       debug3 = debug3.copy();
/* 245 */       removeFramedMap(debug3);
/* 246 */       if (this.random.nextFloat() < this.dropChance) {
/* 247 */         spawnAtLocation(debug3);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void removeFramedMap(ItemStack debug1) {
/* 253 */     if (debug1.getItem() == Items.FILLED_MAP) {
/* 254 */       MapItemSavedData debug2 = MapItem.getOrCreateSavedData(debug1, this.level);
/* 255 */       debug2.removedFromFrame(this.pos, getId());
/* 256 */       debug2.setDirty(true);
/*     */     } 
/* 258 */     debug1.setEntityRepresentation(null);
/*     */   }
/*     */   
/*     */   public ItemStack getItem() {
/* 262 */     return (ItemStack)getEntityData().get(DATA_ITEM);
/*     */   }
/*     */   
/*     */   public void setItem(ItemStack debug1) {
/* 266 */     setItem(debug1, true);
/*     */   }
/*     */   
/*     */   public void setItem(ItemStack debug1, boolean debug2) {
/* 270 */     if (!debug1.isEmpty()) {
/* 271 */       debug1 = debug1.copy();
/* 272 */       debug1.setCount(1);
/* 273 */       debug1.setEntityRepresentation(this);
/*     */     } 
/* 275 */     getEntityData().set(DATA_ITEM, debug1);
/* 276 */     if (!debug1.isEmpty()) {
/* 277 */       playSound(SoundEvents.ITEM_FRAME_ADD_ITEM, 1.0F, 1.0F);
/*     */     }
/*     */     
/* 280 */     if (debug2 && this.pos != null) {
/* 281 */       this.level.updateNeighbourForOutputSignal(this.pos, Blocks.AIR);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setSlot(int debug1, ItemStack debug2) {
/* 287 */     if (debug1 == 0) {
/* 288 */       setItem(debug2);
/* 289 */       return true;
/*     */     } 
/*     */     
/* 292 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> debug1) {
/* 297 */     if (debug1.equals(DATA_ITEM)) {
/* 298 */       ItemStack debug2 = getItem();
/* 299 */       if (!debug2.isEmpty() && debug2.getFrame() != this) {
/* 300 */         debug2.setEntityRepresentation(this);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getRotation() {
/* 306 */     return ((Integer)getEntityData().get(DATA_ROTATION)).intValue();
/*     */   }
/*     */   
/*     */   public void setRotation(int debug1) {
/* 310 */     setRotation(debug1, true);
/*     */   }
/*     */   
/*     */   private void setRotation(int debug1, boolean debug2) {
/* 314 */     getEntityData().set(DATA_ROTATION, Integer.valueOf(debug1 % 8));
/*     */     
/* 316 */     if (debug2 && this.pos != null) {
/* 317 */       this.level.updateNeighbourForOutputSignal(this.pos, Blocks.AIR);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 323 */     super.addAdditionalSaveData(debug1);
/*     */     
/* 325 */     if (!getItem().isEmpty()) {
/* 326 */       debug1.put("Item", (Tag)getItem().save(new CompoundTag()));
/* 327 */       debug1.putByte("ItemRotation", (byte)getRotation());
/* 328 */       debug1.putFloat("ItemDropChance", this.dropChance);
/*     */     } 
/*     */     
/* 331 */     debug1.putByte("Facing", (byte)this.direction.get3DDataValue());
/* 332 */     debug1.putBoolean("Invisible", isInvisible());
/* 333 */     debug1.putBoolean("Fixed", this.fixed);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 338 */     super.readAdditionalSaveData(debug1);
/* 339 */     CompoundTag debug2 = debug1.getCompound("Item");
/* 340 */     if (debug2 != null && !debug2.isEmpty()) {
/* 341 */       ItemStack debug3 = ItemStack.of(debug2);
/* 342 */       if (debug3.isEmpty()) {
/* 343 */         LOGGER.warn("Unable to load item from: {}", debug2);
/*     */       }
/*     */ 
/*     */       
/* 347 */       ItemStack debug4 = getItem();
/* 348 */       if (!debug4.isEmpty() && 
/* 349 */         !ItemStack.matches(debug3, debug4)) {
/* 350 */         removeFramedMap(debug4);
/*     */       }
/*     */ 
/*     */       
/* 354 */       setItem(debug3, false);
/* 355 */       setRotation(debug1.getByte("ItemRotation"), false);
/*     */       
/* 357 */       if (debug1.contains("ItemDropChance", 99)) {
/* 358 */         this.dropChance = debug1.getFloat("ItemDropChance");
/*     */       }
/*     */     } 
/* 361 */     setDirection(Direction.from3DDataValue(debug1.getByte("Facing")));
/* 362 */     setInvisible(debug1.getBoolean("Invisible"));
/* 363 */     this.fixed = debug1.getBoolean("Fixed");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public InteractionResult interact(Player debug1, InteractionHand debug2) {
/* 369 */     ItemStack debug3 = debug1.getItemInHand(debug2);
/* 370 */     boolean debug4 = !getItem().isEmpty();
/* 371 */     boolean debug5 = !debug3.isEmpty();
/*     */ 
/*     */     
/* 374 */     if (this.fixed) {
/* 375 */       return InteractionResult.PASS;
/*     */     }
/*     */     
/* 378 */     if (this.level.isClientSide) {
/* 379 */       return (debug4 || debug5) ? InteractionResult.SUCCESS : InteractionResult.PASS;
/*     */     }
/*     */     
/* 382 */     if (!debug4) {
/* 383 */       if (debug5 && !this.removed) {
/* 384 */         setItem(debug3);
/*     */         
/* 386 */         if (!debug1.abilities.instabuild) {
/* 387 */           debug3.shrink(1);
/*     */         }
/*     */       } 
/*     */     } else {
/* 391 */       playSound(SoundEvents.ITEM_FRAME_ROTATE_ITEM, 1.0F, 1.0F);
/* 392 */       setRotation(getRotation() + 1);
/*     */     } 
/*     */     
/* 395 */     return InteractionResult.CONSUME;
/*     */   }
/*     */   
/*     */   public int getAnalogOutput() {
/* 399 */     if (getItem().isEmpty()) {
/* 400 */       return 0;
/*     */     }
/*     */     
/* 403 */     return getRotation() % 8 + 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public Packet<?> getAddEntityPacket() {
/* 408 */     return (Packet<?>)new ClientboundAddEntityPacket(this, getType(), this.direction.get3DDataValue(), getPos());
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\decoration\ItemFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */