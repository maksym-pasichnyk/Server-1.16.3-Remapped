/*     */ package net.minecraft.world.level.border;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.serialization.DynamicLike;
/*     */ import java.util.List;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WorldBorder
/*     */ {
/*     */   static interface BorderExtent
/*     */   {
/*     */     double getMinX();
/*     */     
/*     */     double getMaxX();
/*     */     
/*     */     double getMinZ();
/*     */     
/*     */     double getMaxZ();
/*     */     
/*     */     double getSize();
/*     */     
/*     */     long getLerpRemainingTime();
/*     */     
/*     */     double getLerpTarget();
/*     */     
/*     */     void onAbsoluteMaxSizeChange();
/*     */     
/*     */     void onCenterChange();
/*     */     
/*     */     BorderExtent update();
/*     */     
/*     */     VoxelShape getCollisionShape();
/*     */   }
/*     */   
/*     */   class MovingBorderExtent
/*     */     implements BorderExtent
/*     */   {
/*     */     private final double from;
/*     */     private final double to;
/*     */     private final long lerpEnd;
/*     */     private final long lerpBegin;
/*     */     private final double lerpDuration;
/*     */     
/*     */     private MovingBorderExtent(double debug2, double debug4, long debug6) {
/*  59 */       this.from = debug2;
/*  60 */       this.to = debug4;
/*     */       
/*  62 */       this.lerpDuration = debug6;
/*  63 */       this.lerpBegin = Util.getMillis();
/*  64 */       this.lerpEnd = this.lerpBegin + debug6;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getMinX() {
/*  69 */       return Math.max(WorldBorder.this.getCenterX() - getSize() / 2.0D, -WorldBorder.this.absoluteMaxSize);
/*     */     }
/*     */ 
/*     */     
/*     */     public double getMinZ() {
/*  74 */       return Math.max(WorldBorder.this.getCenterZ() - getSize() / 2.0D, -WorldBorder.this.absoluteMaxSize);
/*     */     }
/*     */ 
/*     */     
/*     */     public double getMaxX() {
/*  79 */       return Math.min(WorldBorder.this.getCenterX() + getSize() / 2.0D, WorldBorder.this.absoluteMaxSize);
/*     */     }
/*     */ 
/*     */     
/*     */     public double getMaxZ() {
/*  84 */       return Math.min(WorldBorder.this.getCenterZ() + getSize() / 2.0D, WorldBorder.this.absoluteMaxSize);
/*     */     }
/*     */ 
/*     */     
/*     */     public double getSize() {
/*  89 */       double debug1 = (Util.getMillis() - this.lerpBegin) / this.lerpDuration;
/*  90 */       return (debug1 < 1.0D) ? Mth.lerp(debug1, this.from, this.to) : this.to;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public long getLerpRemainingTime() {
/* 100 */       return this.lerpEnd - Util.getMillis();
/*     */     }
/*     */ 
/*     */     
/*     */     public double getLerpTarget() {
/* 105 */       return this.to;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void onCenterChange() {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void onAbsoluteMaxSizeChange() {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public WorldBorder.BorderExtent update() {
/* 123 */       if (getLerpRemainingTime() <= 0L) {
/* 124 */         return new WorldBorder.StaticBorderExtent(this.to);
/*     */       }
/*     */       
/* 127 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public VoxelShape getCollisionShape() {
/* 132 */       return Shapes.join(Shapes.INFINITY, Shapes.box(
/* 133 */             Math.floor(getMinX()), Double.NEGATIVE_INFINITY, Math.floor(getMinZ()), 
/* 134 */             Math.ceil(getMaxX()), Double.POSITIVE_INFINITY, Math.ceil(getMaxZ())), BooleanOp.ONLY_FIRST);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   class StaticBorderExtent
/*     */     implements BorderExtent
/*     */   {
/*     */     private final double size;
/*     */     private double minX;
/*     */     private double minZ;
/*     */     private double maxX;
/*     */     private double maxZ;
/*     */     private VoxelShape shape;
/*     */     
/*     */     public StaticBorderExtent(double debug2) {
/* 150 */       this.size = debug2;
/* 151 */       updateBox();
/*     */     }
/*     */ 
/*     */     
/*     */     public double getMinX() {
/* 156 */       return this.minX;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getMaxX() {
/* 161 */       return this.maxX;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getMinZ() {
/* 166 */       return this.minZ;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getMaxZ() {
/* 171 */       return this.maxZ;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getSize() {
/* 176 */       return this.size;
/*     */     }
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
/*     */     public long getLerpRemainingTime() {
/* 191 */       return 0L;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getLerpTarget() {
/* 196 */       return this.size;
/*     */     }
/*     */     
/*     */     private void updateBox() {
/* 200 */       this.minX = Math.max(WorldBorder.this.getCenterX() - this.size / 2.0D, -WorldBorder.this.absoluteMaxSize);
/* 201 */       this.minZ = Math.max(WorldBorder.this.getCenterZ() - this.size / 2.0D, -WorldBorder.this.absoluteMaxSize);
/* 202 */       this.maxX = Math.min(WorldBorder.this.getCenterX() + this.size / 2.0D, WorldBorder.this.absoluteMaxSize);
/* 203 */       this.maxZ = Math.min(WorldBorder.this.getCenterZ() + this.size / 2.0D, WorldBorder.this.absoluteMaxSize);
/*     */       
/* 205 */       this.shape = Shapes.join(Shapes.INFINITY, Shapes.box(
/* 206 */             Math.floor(getMinX()), Double.NEGATIVE_INFINITY, Math.floor(getMinZ()), 
/* 207 */             Math.ceil(getMaxX()), Double.POSITIVE_INFINITY, Math.ceil(getMaxZ())), BooleanOp.ONLY_FIRST);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onAbsoluteMaxSizeChange() {
/* 213 */       updateBox();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onCenterChange() {
/* 218 */       updateBox();
/*     */     }
/*     */ 
/*     */     
/*     */     public WorldBorder.BorderExtent update() {
/* 223 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public VoxelShape getCollisionShape() {
/* 228 */       return this.shape;
/*     */     }
/*     */   }
/*     */   
/* 232 */   private final List<BorderChangeListener> listeners = Lists.newArrayList();
/*     */   
/* 234 */   private double damagePerBlock = 0.2D;
/* 235 */   private double damageSafeZone = 5.0D;
/* 236 */   private int warningTime = 15;
/* 237 */   private int warningBlocks = 5;
/*     */   
/*     */   private double centerX;
/*     */   
/*     */   private double centerZ;
/* 242 */   private int absoluteMaxSize = 29999984;
/*     */   
/* 244 */   private BorderExtent extent = new StaticBorderExtent(6.0E7D);
/*     */   
/*     */   public boolean isWithinBounds(BlockPos debug1) {
/* 247 */     return ((debug1.getX() + 1) > getMinX() && debug1.getX() < getMaxX() && (debug1.getZ() + 1) > getMinZ() && debug1.getZ() < getMaxZ());
/*     */   }
/*     */   
/*     */   public boolean isWithinBounds(ChunkPos debug1) {
/* 251 */     return (debug1.getMaxBlockX() > getMinX() && debug1.getMinBlockX() < getMaxX() && debug1.getMaxBlockZ() > getMinZ() && debug1.getMinBlockZ() < getMaxZ());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWithinBounds(AABB debug1) {
/* 259 */     return (debug1.maxX > getMinX() && debug1.minX < getMaxX() && debug1.maxZ > getMinZ() && debug1.minZ < getMaxZ());
/*     */   }
/*     */   
/*     */   public double getDistanceToBorder(Entity debug1) {
/* 263 */     return getDistanceToBorder(debug1.getX(), debug1.getZ());
/*     */   }
/*     */   
/*     */   public VoxelShape getCollisionShape() {
/* 267 */     return this.extent.getCollisionShape();
/*     */   }
/*     */   
/*     */   public double getDistanceToBorder(double debug1, double debug3) {
/* 271 */     double debug5 = debug3 - getMinZ();
/* 272 */     double debug7 = getMaxZ() - debug3;
/* 273 */     double debug9 = debug1 - getMinX();
/* 274 */     double debug11 = getMaxX() - debug1;
/* 275 */     double debug13 = Math.min(debug9, debug11);
/* 276 */     debug13 = Math.min(debug13, debug5);
/* 277 */     return Math.min(debug13, debug7);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getMinX() {
/* 285 */     return this.extent.getMinX();
/*     */   }
/*     */   
/*     */   public double getMinZ() {
/* 289 */     return this.extent.getMinZ();
/*     */   }
/*     */   
/*     */   public double getMaxX() {
/* 293 */     return this.extent.getMaxX();
/*     */   }
/*     */   
/*     */   public double getMaxZ() {
/* 297 */     return this.extent.getMaxZ();
/*     */   }
/*     */   
/*     */   public double getCenterX() {
/* 301 */     return this.centerX;
/*     */   }
/*     */   
/*     */   public double getCenterZ() {
/* 305 */     return this.centerZ;
/*     */   }
/*     */   
/*     */   public void setCenter(double debug1, double debug3) {
/* 309 */     this.centerX = debug1;
/* 310 */     this.centerZ = debug3;
/*     */     
/* 312 */     this.extent.onCenterChange();
/*     */     
/* 314 */     for (BorderChangeListener debug6 : getListeners()) {
/* 315 */       debug6.onBorderCenterSet(this, debug1, debug3);
/*     */     }
/*     */   }
/*     */   
/*     */   public double getSize() {
/* 320 */     return this.extent.getSize();
/*     */   }
/*     */   
/*     */   public long getLerpRemainingTime() {
/* 324 */     return this.extent.getLerpRemainingTime();
/*     */   }
/*     */   
/*     */   public double getLerpTarget() {
/* 328 */     return this.extent.getLerpTarget();
/*     */   }
/*     */   
/*     */   public void setSize(double debug1) {
/* 332 */     this.extent = new StaticBorderExtent(debug1);
/*     */     
/* 334 */     for (BorderChangeListener debug4 : getListeners()) {
/* 335 */       debug4.onBorderSizeSet(this, debug1);
/*     */     }
/*     */   }
/*     */   
/*     */   public void lerpSizeBetween(double debug1, double debug3, long debug5) {
/* 340 */     this.extent = (debug1 == debug3) ? new StaticBorderExtent(debug3) : new MovingBorderExtent(debug1, debug3, debug5);
/*     */     
/* 342 */     for (BorderChangeListener debug8 : getListeners()) {
/* 343 */       debug8.onBorderSizeLerping(this, debug1, debug3, debug5);
/*     */     }
/*     */   }
/*     */   
/*     */   protected List<BorderChangeListener> getListeners() {
/* 348 */     return Lists.newArrayList(this.listeners);
/*     */   }
/*     */   
/*     */   public void addListener(BorderChangeListener debug1) {
/* 352 */     this.listeners.add(debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAbsoluteMaxSize(int debug1) {
/* 360 */     this.absoluteMaxSize = debug1;
/* 361 */     this.extent.onAbsoluteMaxSizeChange();
/*     */   }
/*     */   
/*     */   public int getAbsoluteMaxSize() {
/* 365 */     return this.absoluteMaxSize;
/*     */   }
/*     */   
/*     */   public double getDamageSafeZone() {
/* 369 */     return this.damageSafeZone;
/*     */   }
/*     */   
/*     */   public void setDamageSafeZone(double debug1) {
/* 373 */     this.damageSafeZone = debug1;
/*     */     
/* 375 */     for (BorderChangeListener debug4 : getListeners()) {
/* 376 */       debug4.onBorderSetDamageSafeZOne(this, debug1);
/*     */     }
/*     */   }
/*     */   
/*     */   public double getDamagePerBlock() {
/* 381 */     return this.damagePerBlock;
/*     */   }
/*     */   
/*     */   public void setDamagePerBlock(double debug1) {
/* 385 */     this.damagePerBlock = debug1;
/*     */     
/* 387 */     for (BorderChangeListener debug4 : getListeners()) {
/* 388 */       debug4.onBorderSetDamagePerBlock(this, debug1);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getWarningTime() {
/* 397 */     return this.warningTime;
/*     */   }
/*     */   
/*     */   public void setWarningTime(int debug1) {
/* 401 */     this.warningTime = debug1;
/*     */     
/* 403 */     for (BorderChangeListener debug3 : getListeners()) {
/* 404 */       debug3.onBorderSetWarningTime(this, debug1);
/*     */     }
/*     */   }
/*     */   
/*     */   public int getWarningBlocks() {
/* 409 */     return this.warningBlocks;
/*     */   }
/*     */   
/*     */   public void setWarningBlocks(int debug1) {
/* 413 */     this.warningBlocks = debug1;
/*     */     
/* 415 */     for (BorderChangeListener debug3 : getListeners()) {
/* 416 */       debug3.onBorderSetWarningBlocks(this, debug1);
/*     */     }
/*     */   }
/*     */   
/*     */   public void tick() {
/* 421 */     this.extent = this.extent.update();
/*     */   }
/*     */   
/*     */   public Settings createSettings() {
/* 425 */     return new Settings(this);
/*     */   }
/*     */   
/*     */   public void applySettings(Settings debug1) {
/* 429 */     setCenter(debug1.getCenterX(), debug1.getCenterZ());
/* 430 */     setDamagePerBlock(debug1.getDamagePerBlock());
/* 431 */     setDamageSafeZone(debug1.getSafeZone());
/* 432 */     setWarningBlocks(debug1.getWarningBlocks());
/* 433 */     setWarningTime(debug1.getWarningTime());
/*     */     
/* 435 */     if (debug1.getSizeLerpTime() > 0L) {
/* 436 */       lerpSizeBetween(debug1.getSize(), debug1.getSizeLerpTarget(), debug1.getSizeLerpTime());
/*     */     } else {
/* 438 */       setSize(debug1.getSize());
/*     */     } 
/*     */   }
/*     */   
/* 442 */   public static final Settings DEFAULT_SETTINGS = new Settings(0.0D, 0.0D, 0.2D, 5.0D, 5, 15, 6.0E7D, 0L, 0.0D);
/*     */   
/*     */   public static class Settings {
/*     */     private final double centerX;
/*     */     private final double centerZ;
/*     */     private final double damagePerBlock;
/*     */     private final double safeZone;
/*     */     private final int warningBlocks;
/*     */     private final int warningTime;
/*     */     private final double size;
/*     */     private final long sizeLerpTime;
/*     */     private final double sizeLerpTarget;
/*     */     
/*     */     private Settings(double debug1, double debug3, double debug5, double debug7, int debug9, int debug10, double debug11, long debug13, double debug15) {
/* 456 */       this.centerX = debug1;
/* 457 */       this.centerZ = debug3;
/* 458 */       this.damagePerBlock = debug5;
/* 459 */       this.safeZone = debug7;
/* 460 */       this.warningBlocks = debug9;
/* 461 */       this.warningTime = debug10;
/* 462 */       this.size = debug11;
/* 463 */       this.sizeLerpTime = debug13;
/* 464 */       this.sizeLerpTarget = debug15;
/*     */     }
/*     */     
/*     */     private Settings(WorldBorder debug1) {
/* 468 */       this.centerX = debug1.getCenterX();
/* 469 */       this.centerZ = debug1.getCenterZ();
/* 470 */       this.damagePerBlock = debug1.getDamagePerBlock();
/* 471 */       this.safeZone = debug1.getDamageSafeZone();
/* 472 */       this.warningBlocks = debug1.getWarningBlocks();
/* 473 */       this.warningTime = debug1.getWarningTime();
/* 474 */       this.size = debug1.getSize();
/* 475 */       this.sizeLerpTime = debug1.getLerpRemainingTime();
/* 476 */       this.sizeLerpTarget = debug1.getLerpTarget();
/*     */     }
/*     */     
/*     */     public double getCenterX() {
/* 480 */       return this.centerX;
/*     */     }
/*     */     
/*     */     public double getCenterZ() {
/* 484 */       return this.centerZ;
/*     */     }
/*     */     
/*     */     public double getDamagePerBlock() {
/* 488 */       return this.damagePerBlock;
/*     */     }
/*     */     
/*     */     public double getSafeZone() {
/* 492 */       return this.safeZone;
/*     */     }
/*     */     
/*     */     public int getWarningBlocks() {
/* 496 */       return this.warningBlocks;
/*     */     }
/*     */     
/*     */     public int getWarningTime() {
/* 500 */       return this.warningTime;
/*     */     }
/*     */     
/*     */     public double getSize() {
/* 504 */       return this.size;
/*     */     }
/*     */     
/*     */     public long getSizeLerpTime() {
/* 508 */       return this.sizeLerpTime;
/*     */     }
/*     */     
/*     */     public double getSizeLerpTarget() {
/* 512 */       return this.sizeLerpTarget;
/*     */     }
/*     */     
/*     */     public static Settings read(DynamicLike<?> debug0, Settings debug1) {
/* 516 */       double debug2 = debug0.get("BorderCenterX").asDouble(debug1.centerX);
/* 517 */       double debug4 = debug0.get("BorderCenterZ").asDouble(debug1.centerZ);
/* 518 */       double debug6 = debug0.get("BorderSize").asDouble(debug1.size);
/* 519 */       long debug8 = debug0.get("BorderSizeLerpTime").asLong(debug1.sizeLerpTime);
/* 520 */       double debug10 = debug0.get("BorderSizeLerpTarget").asDouble(debug1.sizeLerpTarget);
/* 521 */       double debug12 = debug0.get("BorderSafeZone").asDouble(debug1.safeZone);
/* 522 */       double debug14 = debug0.get("BorderDamagePerBlock").asDouble(debug1.damagePerBlock);
/* 523 */       int debug16 = debug0.get("BorderWarningBlocks").asInt(debug1.warningBlocks);
/* 524 */       int debug17 = debug0.get("BorderWarningTime").asInt(debug1.warningTime);
/*     */       
/* 526 */       return new Settings(debug2, debug4, debug14, debug12, debug16, debug17, debug6, debug8, debug10);
/*     */     }
/*     */     
/*     */     public void write(CompoundTag debug1) {
/* 530 */       debug1.putDouble("BorderCenterX", this.centerX);
/* 531 */       debug1.putDouble("BorderCenterZ", this.centerZ);
/* 532 */       debug1.putDouble("BorderSize", this.size);
/* 533 */       debug1.putLong("BorderSizeLerpTime", this.sizeLerpTime);
/* 534 */       debug1.putDouble("BorderSafeZone", this.safeZone);
/* 535 */       debug1.putDouble("BorderDamagePerBlock", this.damagePerBlock);
/* 536 */       debug1.putDouble("BorderSizeLerpTarget", this.sizeLerpTarget);
/* 537 */       debug1.putDouble("BorderWarningBlocks", this.warningBlocks);
/* 538 */       debug1.putDouble("BorderWarningTime", this.warningTime);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\border\WorldBorder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */