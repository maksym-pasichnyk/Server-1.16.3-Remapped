/*     */ package net.minecraft.world.entity.decoration;
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LightningBolt;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.DiodeBlock;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.apache.commons.lang3.Validate;
/*     */ 
/*     */ public abstract class HangingEntity extends Entity {
/*     */   static {
/*  29 */     HANGING_ENTITY = (debug0 -> debug0 instanceof HangingEntity);
/*     */   }
/*     */   
/*     */   protected static final Predicate<Entity> HANGING_ENTITY;
/*  33 */   protected Direction direction = Direction.SOUTH; private int checkInterval; protected BlockPos pos;
/*     */   
/*     */   protected HangingEntity(EntityType<? extends HangingEntity> debug1, Level debug2) {
/*  36 */     super(debug1, debug2);
/*     */   }
/*     */   
/*     */   protected HangingEntity(EntityType<? extends HangingEntity> debug1, Level debug2, BlockPos debug3) {
/*  40 */     this(debug1, debug2);
/*  41 */     this.pos = debug3;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {}
/*     */ 
/*     */   
/*     */   protected void setDirection(Direction debug1) {
/*  49 */     Validate.notNull(debug1);
/*  50 */     Validate.isTrue(debug1.getAxis().isHorizontal());
/*     */     
/*  52 */     this.direction = debug1;
/*  53 */     this.yRot = (this.direction.get2DDataValue() * 90);
/*  54 */     this.yRotO = this.yRot;
/*     */     
/*  56 */     recalculateBoundingBox();
/*     */   }
/*     */   
/*     */   protected void recalculateBoundingBox() {
/*  60 */     if (this.direction == null) {
/*     */       return;
/*     */     }
/*     */     
/*  64 */     double debug1 = this.pos.getX() + 0.5D;
/*  65 */     double debug3 = this.pos.getY() + 0.5D;
/*  66 */     double debug5 = this.pos.getZ() + 0.5D;
/*     */     
/*  68 */     double debug7 = 0.46875D;
/*  69 */     double debug9 = offs(getWidth());
/*  70 */     double debug11 = offs(getHeight());
/*     */     
/*  72 */     debug1 -= this.direction.getStepX() * 0.46875D;
/*  73 */     debug5 -= this.direction.getStepZ() * 0.46875D;
/*  74 */     debug3 += debug11;
/*     */     
/*  76 */     Direction debug13 = this.direction.getCounterClockWise();
/*  77 */     debug1 += debug9 * debug13.getStepX();
/*  78 */     debug5 += debug9 * debug13.getStepZ();
/*     */     
/*  80 */     setPosRaw(debug1, debug3, debug5);
/*     */     
/*  82 */     double debug14 = getWidth();
/*  83 */     double debug16 = getHeight();
/*  84 */     double debug18 = getWidth();
/*     */     
/*  86 */     if (this.direction.getAxis() == Direction.Axis.Z) {
/*  87 */       debug18 = 1.0D;
/*     */     } else {
/*  89 */       debug14 = 1.0D;
/*     */     } 
/*     */     
/*  92 */     debug14 /= 32.0D;
/*  93 */     debug16 /= 32.0D;
/*  94 */     debug18 /= 32.0D;
/*  95 */     setBoundingBox(new AABB(debug1 - debug14, debug3 - debug16, debug5 - debug18, debug1 + debug14, debug3 + debug16, debug5 + debug18));
/*     */   }
/*     */   
/*     */   private double offs(int debug1) {
/*  99 */     return (debug1 % 32 == 0) ? 0.5D : 0.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 104 */     if (!this.level.isClientSide) {
/* 105 */       if (getY() < -64.0D) {
/* 106 */         outOfWorld();
/*     */       }
/* 108 */       if (this.checkInterval++ == 100) {
/* 109 */         this.checkInterval = 0;
/* 110 */         if (!this.removed && !survives()) {
/* 111 */           remove();
/* 112 */           dropItem((Entity)null);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean survives() {
/* 119 */     if (!this.level.noCollision(this)) {
/* 120 */       return false;
/*     */     }
/*     */     
/* 123 */     int debug1 = Math.max(1, getWidth() / 16);
/* 124 */     int debug2 = Math.max(1, getHeight() / 16);
/*     */     
/* 126 */     BlockPos debug3 = this.pos.relative(this.direction.getOpposite());
/* 127 */     Direction debug4 = this.direction.getCounterClockWise();
/*     */     
/* 129 */     BlockPos.MutableBlockPos debug5 = new BlockPos.MutableBlockPos();
/* 130 */     for (int debug6 = 0; debug6 < debug1; debug6++) {
/* 131 */       for (int debug7 = 0; debug7 < debug2; debug7++) {
/* 132 */         int debug8 = (debug1 - 1) / -2;
/* 133 */         int debug9 = (debug2 - 1) / -2;
/*     */         
/* 135 */         debug5.set((Vec3i)debug3).move(debug4, debug6 + debug8).move(Direction.UP, debug7 + debug9);
/*     */         
/* 137 */         BlockState debug10 = this.level.getBlockState((BlockPos)debug5);
/* 138 */         if (!debug10.getMaterial().isSolid() && !DiodeBlock.isDiode(debug10)) {
/* 139 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 144 */     return this.level.getEntities(this, getBoundingBox(), HANGING_ENTITY).isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPickable() {
/* 149 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean skipAttackInteraction(Entity debug1) {
/* 154 */     if (debug1 instanceof Player) {
/* 155 */       Player debug2 = (Player)debug1;
/* 156 */       if (!this.level.mayInteract(debug2, this.pos)) {
/* 157 */         return true;
/*     */       }
/* 159 */       return hurt(DamageSource.playerAttack(debug2), 0.0F);
/*     */     } 
/* 161 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Direction getDirection() {
/* 166 */     return this.direction;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurt(DamageSource debug1, float debug2) {
/* 171 */     if (isInvulnerableTo(debug1)) {
/* 172 */       return false;
/*     */     }
/* 174 */     if (!this.removed && !this.level.isClientSide) {
/* 175 */       remove();
/* 176 */       markHurt();
/* 177 */       dropItem(debug1.getEntity());
/*     */     } 
/* 179 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void move(MoverType debug1, Vec3 debug2) {
/* 184 */     if (!this.level.isClientSide && !this.removed && debug2.lengthSqr() > 0.0D) {
/* 185 */       remove();
/* 186 */       dropItem((Entity)null);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void push(double debug1, double debug3, double debug5) {
/* 192 */     if (!this.level.isClientSide && !this.removed && debug1 * debug1 + debug3 * debug3 + debug5 * debug5 > 0.0D) {
/* 193 */       remove();
/* 194 */       dropItem((Entity)null);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 200 */     BlockPos debug2 = getPos();
/* 201 */     debug1.putInt("TileX", debug2.getX());
/* 202 */     debug1.putInt("TileY", debug2.getY());
/* 203 */     debug1.putInt("TileZ", debug2.getZ());
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 208 */     this.pos = new BlockPos(debug1.getInt("TileX"), debug1.getInt("TileY"), debug1.getInt("TileZ"));
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
/*     */   public ItemEntity spawnAtLocation(ItemStack debug1, float debug2) {
/* 221 */     ItemEntity debug3 = new ItemEntity(this.level, getX() + (this.direction.getStepX() * 0.15F), getY() + debug2, getZ() + (this.direction.getStepZ() * 0.15F), debug1);
/* 222 */     debug3.setDefaultPickUpDelay();
/* 223 */     this.level.addFreshEntity((Entity)debug3);
/* 224 */     return debug3;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean repositionEntityAfterLoad() {
/* 229 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPos(double debug1, double debug3, double debug5) {
/* 234 */     this.pos = new BlockPos(debug1, debug3, debug5);
/* 235 */     recalculateBoundingBox();
/* 236 */     this.hasImpulse = true;
/*     */   }
/*     */   
/*     */   public BlockPos getPos() {
/* 240 */     return this.pos;
/*     */   }
/*     */ 
/*     */   
/*     */   public float rotate(Rotation debug1) {
/* 245 */     if (this.direction.getAxis() != Direction.Axis.Y) {
/* 246 */       switch (debug1) {
/*     */         case CLOCKWISE_180:
/* 248 */           this.direction = this.direction.getOpposite();
/*     */           break;
/*     */         case COUNTERCLOCKWISE_90:
/* 251 */           this.direction = this.direction.getCounterClockWise();
/*     */           break;
/*     */         case CLOCKWISE_90:
/* 254 */           this.direction = this.direction.getClockWise();
/*     */           break;
/*     */       } 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 261 */     float debug2 = Mth.wrapDegrees(this.yRot);
/* 262 */     switch (debug1) {
/*     */       case CLOCKWISE_180:
/* 264 */         return debug2 + 180.0F;
/*     */       case COUNTERCLOCKWISE_90:
/* 266 */         return debug2 + 90.0F;
/*     */       case CLOCKWISE_90:
/* 268 */         return debug2 + 270.0F;
/*     */     } 
/* 270 */     return debug2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float mirror(Mirror debug1) {
/* 276 */     return rotate(debug1.getRotation(this.direction));
/*     */   }
/*     */   
/*     */   public void thunderHit(ServerLevel debug1, LightningBolt debug2) {}
/*     */   
/*     */   public void refreshDimensions() {}
/*     */   
/*     */   public abstract int getWidth();
/*     */   
/*     */   public abstract int getHeight();
/*     */   
/*     */   public abstract void dropItem(@Nullable Entity paramEntity);
/*     */   
/*     */   public abstract void playPlacementSound();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\decoration\HangingEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */