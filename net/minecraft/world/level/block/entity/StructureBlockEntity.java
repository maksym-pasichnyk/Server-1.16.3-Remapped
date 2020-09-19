/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Random;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.ResourceLocationException;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.StructureBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.StructureMode;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockRotProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StructureBlockEntity
/*     */   extends BlockEntity
/*     */ {
/*     */   private ResourceLocation structureName;
/*  46 */   private String author = "";
/*  47 */   private String metaData = "";
/*  48 */   private BlockPos structurePos = new BlockPos(0, 1, 0);
/*  49 */   private BlockPos structureSize = BlockPos.ZERO;
/*  50 */   private Mirror mirror = Mirror.NONE;
/*  51 */   private Rotation rotation = Rotation.NONE;
/*  52 */   private StructureMode mode = StructureMode.DATA;
/*     */   private boolean ignoreEntities = true;
/*     */   private boolean powered;
/*     */   private boolean showAir;
/*     */   private boolean showBoundingBox = true;
/*  57 */   private float integrity = 1.0F;
/*     */   private long seed;
/*     */   
/*     */   public StructureBlockEntity() {
/*  61 */     super(BlockEntityType.STRUCTURE_BLOCK);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompoundTag save(CompoundTag debug1) {
/*  71 */     super.save(debug1);
/*  72 */     debug1.putString("name", getStructureName());
/*  73 */     debug1.putString("author", this.author);
/*  74 */     debug1.putString("metadata", this.metaData);
/*  75 */     debug1.putInt("posX", this.structurePos.getX());
/*  76 */     debug1.putInt("posY", this.structurePos.getY());
/*  77 */     debug1.putInt("posZ", this.structurePos.getZ());
/*  78 */     debug1.putInt("sizeX", this.structureSize.getX());
/*  79 */     debug1.putInt("sizeY", this.structureSize.getY());
/*  80 */     debug1.putInt("sizeZ", this.structureSize.getZ());
/*  81 */     debug1.putString("rotation", this.rotation.toString());
/*  82 */     debug1.putString("mirror", this.mirror.toString());
/*  83 */     debug1.putString("mode", this.mode.toString());
/*  84 */     debug1.putBoolean("ignoreEntities", this.ignoreEntities);
/*  85 */     debug1.putBoolean("powered", this.powered);
/*  86 */     debug1.putBoolean("showair", this.showAir);
/*  87 */     debug1.putBoolean("showboundingbox", this.showBoundingBox);
/*  88 */     debug1.putFloat("integrity", this.integrity);
/*  89 */     debug1.putLong("seed", this.seed);
/*  90 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void load(BlockState debug1, CompoundTag debug2) {
/*  95 */     super.load(debug1, debug2);
/*  96 */     setStructureName(debug2.getString("name"));
/*  97 */     this.author = debug2.getString("author");
/*  98 */     this.metaData = debug2.getString("metadata");
/*  99 */     int debug3 = Mth.clamp(debug2.getInt("posX"), -48, 48);
/* 100 */     int debug4 = Mth.clamp(debug2.getInt("posY"), -48, 48);
/* 101 */     int debug5 = Mth.clamp(debug2.getInt("posZ"), -48, 48);
/* 102 */     this.structurePos = new BlockPos(debug3, debug4, debug5);
/* 103 */     int debug6 = Mth.clamp(debug2.getInt("sizeX"), 0, 48);
/* 104 */     int debug7 = Mth.clamp(debug2.getInt("sizeY"), 0, 48);
/* 105 */     int debug8 = Mth.clamp(debug2.getInt("sizeZ"), 0, 48);
/* 106 */     this.structureSize = new BlockPos(debug6, debug7, debug8);
/*     */     try {
/* 108 */       this.rotation = Rotation.valueOf(debug2.getString("rotation"));
/* 109 */     } catch (IllegalArgumentException debug9) {
/* 110 */       this.rotation = Rotation.NONE;
/*     */     } 
/*     */     try {
/* 113 */       this.mirror = Mirror.valueOf(debug2.getString("mirror"));
/* 114 */     } catch (IllegalArgumentException debug9) {
/* 115 */       this.mirror = Mirror.NONE;
/*     */     } 
/*     */     try {
/* 118 */       this.mode = StructureMode.valueOf(debug2.getString("mode"));
/* 119 */     } catch (IllegalArgumentException debug9) {
/* 120 */       this.mode = StructureMode.DATA;
/*     */     } 
/* 122 */     this.ignoreEntities = debug2.getBoolean("ignoreEntities");
/* 123 */     this.powered = debug2.getBoolean("powered");
/* 124 */     this.showAir = debug2.getBoolean("showair");
/* 125 */     this.showBoundingBox = debug2.getBoolean("showboundingbox");
/* 126 */     if (debug2.contains("integrity")) {
/* 127 */       this.integrity = debug2.getFloat("integrity");
/*     */     } else {
/* 129 */       this.integrity = 1.0F;
/*     */     } 
/* 131 */     this.seed = debug2.getLong("seed");
/* 132 */     updateBlockState();
/*     */   }
/*     */   
/*     */   private void updateBlockState() {
/* 136 */     if (this.level == null) {
/*     */       return;
/*     */     }
/* 139 */     BlockPos debug1 = getBlockPos();
/* 140 */     BlockState debug2 = this.level.getBlockState(debug1);
/* 141 */     if (debug2.is(Blocks.STRUCTURE_BLOCK)) {
/* 142 */       this.level.setBlock(debug1, (BlockState)debug2.setValue((Property)StructureBlock.MODE, (Comparable)this.mode), 2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ClientboundBlockEntityDataPacket getUpdatePacket() {
/* 149 */     return new ClientboundBlockEntityDataPacket(this.worldPosition, 7, getUpdateTag());
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag getUpdateTag() {
/* 154 */     return save(new CompoundTag());
/*     */   }
/*     */   
/*     */   public boolean usedBy(Player debug1) {
/* 158 */     if (!debug1.canUseGameMasterBlocks()) {
/* 159 */       return false;
/*     */     }
/* 161 */     if ((debug1.getCommandSenderWorld()).isClientSide) {
/* 162 */       debug1.openStructureBlock(this);
/*     */     }
/* 164 */     return true;
/*     */   }
/*     */   
/*     */   public String getStructureName() {
/* 168 */     return (this.structureName == null) ? "" : this.structureName.toString();
/*     */   }
/*     */   
/*     */   public String getStructurePath() {
/* 172 */     return (this.structureName == null) ? "" : this.structureName.getPath();
/*     */   }
/*     */   
/*     */   public boolean hasStructureName() {
/* 176 */     return (this.structureName != null);
/*     */   }
/*     */   
/*     */   public void setStructureName(@Nullable String debug1) {
/* 180 */     setStructureName(StringUtil.isNullOrEmpty(debug1) ? null : ResourceLocation.tryParse(debug1));
/*     */   }
/*     */   
/*     */   public void setStructureName(@Nullable ResourceLocation debug1) {
/* 184 */     this.structureName = debug1;
/*     */   }
/*     */   
/*     */   public void createdBy(LivingEntity debug1) {
/* 188 */     this.author = debug1.getName().getString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStructurePos(BlockPos debug1) {
/* 196 */     this.structurePos = debug1;
/*     */   }
/*     */   
/*     */   public BlockPos getStructureSize() {
/* 200 */     return this.structureSize;
/*     */   }
/*     */   
/*     */   public void setStructureSize(BlockPos debug1) {
/* 204 */     this.structureSize = debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMirror(Mirror debug1) {
/* 212 */     this.mirror = debug1;
/*     */   }
/*     */   
/*     */   public Rotation getRotation() {
/* 216 */     return this.rotation;
/*     */   }
/*     */   
/*     */   public void setRotation(Rotation debug1) {
/* 220 */     this.rotation = debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMetaData(String debug1) {
/* 228 */     this.metaData = debug1;
/*     */   }
/*     */   
/*     */   public StructureMode getMode() {
/* 232 */     return this.mode;
/*     */   }
/*     */   
/*     */   public void setMode(StructureMode debug1) {
/* 236 */     this.mode = debug1;
/* 237 */     BlockState debug2 = this.level.getBlockState(getBlockPos());
/* 238 */     if (debug2.is(Blocks.STRUCTURE_BLOCK)) {
/* 239 */       this.level.setBlock(getBlockPos(), (BlockState)debug2.setValue((Property)StructureBlock.MODE, (Comparable)debug1), 2);
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
/*     */   public void setIgnoreEntities(boolean debug1) {
/* 265 */     this.ignoreEntities = debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIntegrity(float debug1) {
/* 273 */     this.integrity = debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSeed(long debug1) {
/* 281 */     this.seed = debug1;
/*     */   }
/*     */   
/*     */   public boolean detectSize() {
/* 285 */     if (this.mode != StructureMode.SAVE) {
/* 286 */       return false;
/*     */     }
/* 288 */     BlockPos debug1 = getBlockPos();
/* 289 */     int debug2 = 80;
/* 290 */     BlockPos debug3 = new BlockPos(debug1.getX() - 80, 0, debug1.getZ() - 80);
/* 291 */     BlockPos debug4 = new BlockPos(debug1.getX() + 80, 255, debug1.getZ() + 80);
/*     */     
/* 293 */     List<StructureBlockEntity> debug5 = getNearbyCornerBlocks(debug3, debug4);
/* 294 */     List<StructureBlockEntity> debug6 = filterRelatedCornerBlocks(debug5);
/* 295 */     if (debug6.size() < 1) {
/* 296 */       return false;
/*     */     }
/*     */     
/* 299 */     BoundingBox debug7 = calculateEnclosingBoundingBox(debug1, debug6);
/* 300 */     if (debug7.x1 - debug7.x0 > 1 && debug7.y1 - debug7.y0 > 1 && debug7.z1 - debug7.z0 > 1) {
/* 301 */       this.structurePos = new BlockPos(debug7.x0 - debug1.getX() + 1, debug7.y0 - debug1.getY() + 1, debug7.z0 - debug1.getZ() + 1);
/* 302 */       this.structureSize = new BlockPos(debug7.x1 - debug7.x0 - 1, debug7.y1 - debug7.y0 - 1, debug7.z1 - debug7.z0 - 1);
/* 303 */       setChanged();
/* 304 */       BlockState debug8 = this.level.getBlockState(debug1);
/* 305 */       this.level.sendBlockUpdated(debug1, debug8, debug8, 3);
/* 306 */       return true;
/*     */     } 
/* 308 */     return false;
/*     */   }
/*     */   
/*     */   private List<StructureBlockEntity> filterRelatedCornerBlocks(List<StructureBlockEntity> debug1) {
/* 312 */     Predicate<StructureBlockEntity> debug2 = debug1 -> (debug1.mode == StructureMode.CORNER && Objects.equals(this.structureName, debug1.structureName));
/* 313 */     return (List<StructureBlockEntity>)debug1.stream().filter(debug2).collect(Collectors.toList());
/*     */   }
/*     */   
/*     */   private List<StructureBlockEntity> getNearbyCornerBlocks(BlockPos debug1, BlockPos debug2) {
/* 317 */     List<StructureBlockEntity> debug3 = Lists.newArrayList();
/* 318 */     for (BlockPos debug5 : BlockPos.betweenClosed(debug1, debug2)) {
/* 319 */       BlockState debug6 = this.level.getBlockState(debug5);
/* 320 */       if (!debug6.is(Blocks.STRUCTURE_BLOCK)) {
/*     */         continue;
/*     */       }
/* 323 */       BlockEntity debug7 = this.level.getBlockEntity(debug5);
/* 324 */       if (debug7 != null && debug7 instanceof StructureBlockEntity) {
/* 325 */         debug3.add((StructureBlockEntity)debug7);
/*     */       }
/*     */     } 
/* 328 */     return debug3;
/*     */   }
/*     */   
/*     */   private BoundingBox calculateEnclosingBoundingBox(BlockPos debug1, List<StructureBlockEntity> debug2) {
/*     */     BoundingBox debug3;
/* 333 */     if (debug2.size() > 1) {
/* 334 */       BlockPos debug4 = ((StructureBlockEntity)debug2.get(0)).getBlockPos();
/* 335 */       debug3 = new BoundingBox((Vec3i)debug4, (Vec3i)debug4);
/*     */     } else {
/* 337 */       debug3 = new BoundingBox((Vec3i)debug1, (Vec3i)debug1);
/*     */     } 
/*     */     
/* 340 */     for (StructureBlockEntity debug5 : debug2) {
/* 341 */       BlockPos debug6 = debug5.getBlockPos();
/* 342 */       if (debug6.getX() < debug3.x0) {
/* 343 */         debug3.x0 = debug6.getX();
/* 344 */       } else if (debug6.getX() > debug3.x1) {
/* 345 */         debug3.x1 = debug6.getX();
/*     */       } 
/* 347 */       if (debug6.getY() < debug3.y0) {
/* 348 */         debug3.y0 = debug6.getY();
/* 349 */       } else if (debug6.getY() > debug3.y1) {
/* 350 */         debug3.y1 = debug6.getY();
/*     */       } 
/* 352 */       if (debug6.getZ() < debug3.z0) {
/* 353 */         debug3.z0 = debug6.getZ(); continue;
/* 354 */       }  if (debug6.getZ() > debug3.z1) {
/* 355 */         debug3.z1 = debug6.getZ();
/*     */       }
/*     */     } 
/* 358 */     return debug3;
/*     */   }
/*     */   
/*     */   public boolean saveStructure() {
/* 362 */     return saveStructure(true);
/*     */   }
/*     */   public boolean saveStructure(boolean debug1) {
/*     */     StructureTemplate debug5;
/* 366 */     if (this.mode != StructureMode.SAVE || this.level.isClientSide || this.structureName == null) {
/* 367 */       return false;
/*     */     }
/* 369 */     BlockPos debug2 = getBlockPos().offset((Vec3i)this.structurePos);
/*     */     
/* 371 */     ServerLevel debug3 = (ServerLevel)this.level;
/* 372 */     StructureManager debug4 = debug3.getStructureManager();
/*     */     
/*     */     try {
/* 375 */       debug5 = debug4.getOrCreate(this.structureName);
/* 376 */     } catch (ResourceLocationException debug6) {
/* 377 */       return false;
/*     */     } 
/*     */     
/* 380 */     debug5.fillFromWorld(this.level, debug2, this.structureSize, !this.ignoreEntities, Blocks.STRUCTURE_VOID);
/* 381 */     debug5.setAuthor(this.author);
/* 382 */     if (debug1) {
/*     */       try {
/* 384 */         return debug4.save(this.structureName);
/* 385 */       } catch (ResourceLocationException debug6) {
/* 386 */         return false;
/*     */       } 
/*     */     }
/* 389 */     return true;
/*     */   }
/*     */   
/*     */   public boolean loadStructure(ServerLevel debug1) {
/* 393 */     return loadStructure(debug1, true);
/*     */   }
/*     */   
/*     */   private static Random createRandom(long debug0) {
/* 397 */     if (debug0 == 0L) {
/* 398 */       return new Random(Util.getMillis());
/*     */     }
/* 400 */     return new Random(debug0);
/*     */   }
/*     */   public boolean loadStructure(ServerLevel debug1, boolean debug2) {
/*     */     StructureTemplate debug4;
/* 404 */     if (this.mode != StructureMode.LOAD || this.structureName == null) {
/* 405 */       return false;
/*     */     }
/* 407 */     StructureManager debug3 = debug1.getStructureManager();
/*     */     
/*     */     try {
/* 410 */       debug4 = debug3.get(this.structureName);
/* 411 */     } catch (ResourceLocationException debug5) {
/* 412 */       return false;
/*     */     } 
/*     */     
/* 415 */     if (debug4 == null) {
/* 416 */       return false;
/*     */     }
/*     */     
/* 419 */     return loadStructure(debug1, debug2, debug4);
/*     */   }
/*     */   
/*     */   public boolean loadStructure(ServerLevel debug1, boolean debug2, StructureTemplate debug3) {
/* 423 */     BlockPos debug4 = getBlockPos();
/*     */     
/* 425 */     if (!StringUtil.isNullOrEmpty(debug3.getAuthor())) {
/* 426 */       this.author = debug3.getAuthor();
/*     */     }
/*     */     
/* 429 */     BlockPos debug5 = debug3.getSize();
/* 430 */     boolean debug6 = this.structureSize.equals(debug5);
/*     */     
/* 432 */     if (!debug6) {
/* 433 */       this.structureSize = debug5;
/* 434 */       setChanged();
/* 435 */       BlockState debug7 = debug1.getBlockState(debug4);
/* 436 */       debug1.sendBlockUpdated(debug4, debug7, debug7, 3);
/*     */     } 
/*     */     
/* 439 */     if (!debug2 || debug6) {
/* 440 */       StructurePlaceSettings debug7 = (new StructurePlaceSettings()).setMirror(this.mirror).setRotation(this.rotation).setIgnoreEntities(this.ignoreEntities).setChunkPos(null);
/* 441 */       if (this.integrity < 1.0F) {
/* 442 */         debug7.clearProcessors().addProcessor((StructureProcessor)new BlockRotProcessor(Mth.clamp(this.integrity, 0.0F, 1.0F))).setRandom(createRandom(this.seed));
/*     */       }
/* 444 */       BlockPos debug8 = debug4.offset((Vec3i)this.structurePos);
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
/* 458 */       debug3.placeInWorldChunk((ServerLevelAccessor)debug1, debug8, debug7, createRandom(this.seed));
/* 459 */       return true;
/*     */     } 
/* 461 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void unloadStructure() {
/* 466 */     if (this.structureName == null) {
/*     */       return;
/*     */     }
/* 469 */     ServerLevel debug1 = (ServerLevel)this.level;
/* 470 */     StructureManager debug2 = debug1.getStructureManager();
/* 471 */     debug2.remove(this.structureName);
/*     */   }
/*     */   
/*     */   public boolean isStructureLoadable() {
/* 475 */     if (this.mode != StructureMode.LOAD || this.level.isClientSide || this.structureName == null) {
/* 476 */       return false;
/*     */     }
/* 478 */     ServerLevel debug1 = (ServerLevel)this.level;
/* 479 */     StructureManager debug2 = debug1.getStructureManager();
/*     */     try {
/* 481 */       return (debug2.get(this.structureName) != null);
/* 482 */     } catch (ResourceLocationException debug3) {
/* 483 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isPowered() {
/* 488 */     return this.powered;
/*     */   }
/*     */   
/*     */   public void setPowered(boolean debug1) {
/* 492 */     this.powered = debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setShowAir(boolean debug1) {
/* 500 */     this.showAir = debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setShowBoundingBox(boolean debug1) {
/* 508 */     this.showBoundingBox = debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum UpdateType
/*     */   {
/* 516 */     UPDATE_DATA,
/* 517 */     SAVE_AREA,
/* 518 */     LOAD_AREA,
/* 519 */     SCAN_AREA;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\entity\StructureBlockEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */