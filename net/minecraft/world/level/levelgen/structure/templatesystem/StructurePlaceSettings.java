/*     */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ 
/*     */ public class StructurePlaceSettings
/*     */ {
/*  18 */   private Mirror mirror = Mirror.NONE;
/*  19 */   private Rotation rotation = Rotation.NONE;
/*  20 */   private BlockPos rotationPivot = BlockPos.ZERO;
/*     */   private boolean ignoreEntities;
/*     */   @Nullable
/*     */   private ChunkPos chunkPos;
/*     */   @Nullable
/*     */   private BoundingBox boundingBox;
/*     */   private boolean keepLiquids = true;
/*     */   @Nullable
/*     */   private Random random;
/*     */   @Nullable
/*     */   private int palette;
/*  31 */   private final List<StructureProcessor> processors = Lists.newArrayList();
/*     */   private boolean knownShape;
/*     */   private boolean finalizeEntities;
/*     */   
/*     */   public StructurePlaceSettings copy() {
/*  36 */     StructurePlaceSettings debug1 = new StructurePlaceSettings();
/*  37 */     debug1.mirror = this.mirror;
/*  38 */     debug1.rotation = this.rotation;
/*  39 */     debug1.rotationPivot = this.rotationPivot;
/*  40 */     debug1.ignoreEntities = this.ignoreEntities;
/*  41 */     debug1.chunkPos = this.chunkPos;
/*  42 */     debug1.boundingBox = this.boundingBox;
/*  43 */     debug1.keepLiquids = this.keepLiquids;
/*  44 */     debug1.random = this.random;
/*  45 */     debug1.palette = this.palette;
/*  46 */     debug1.processors.addAll(this.processors);
/*  47 */     debug1.knownShape = this.knownShape;
/*  48 */     debug1.finalizeEntities = this.finalizeEntities;
/*  49 */     return debug1;
/*     */   }
/*     */   
/*     */   public StructurePlaceSettings setMirror(Mirror debug1) {
/*  53 */     this.mirror = debug1;
/*  54 */     return this;
/*     */   }
/*     */   
/*     */   public StructurePlaceSettings setRotation(Rotation debug1) {
/*  58 */     this.rotation = debug1;
/*  59 */     return this;
/*     */   }
/*     */   
/*     */   public StructurePlaceSettings setRotationPivot(BlockPos debug1) {
/*  63 */     this.rotationPivot = debug1;
/*  64 */     return this;
/*     */   }
/*     */   
/*     */   public StructurePlaceSettings setIgnoreEntities(boolean debug1) {
/*  68 */     this.ignoreEntities = debug1;
/*  69 */     return this;
/*     */   }
/*     */   
/*     */   public StructurePlaceSettings setChunkPos(ChunkPos debug1) {
/*  73 */     this.chunkPos = debug1;
/*  74 */     return this;
/*     */   }
/*     */   
/*     */   public StructurePlaceSettings setBoundingBox(BoundingBox debug1) {
/*  78 */     this.boundingBox = debug1;
/*  79 */     return this;
/*     */   }
/*     */   
/*     */   public StructurePlaceSettings setRandom(@Nullable Random debug1) {
/*  83 */     this.random = debug1;
/*  84 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StructurePlaceSettings setKnownShape(boolean debug1) {
/*  93 */     this.knownShape = debug1;
/*  94 */     return this;
/*     */   }
/*     */   
/*     */   public StructurePlaceSettings clearProcessors() {
/*  98 */     this.processors.clear();
/*  99 */     return this;
/*     */   }
/*     */   
/*     */   public StructurePlaceSettings addProcessor(StructureProcessor debug1) {
/* 103 */     this.processors.add(debug1);
/* 104 */     return this;
/*     */   }
/*     */   
/*     */   public StructurePlaceSettings popProcessor(StructureProcessor debug1) {
/* 108 */     this.processors.remove(debug1);
/* 109 */     return this;
/*     */   }
/*     */   
/*     */   public Mirror getMirror() {
/* 113 */     return this.mirror;
/*     */   }
/*     */   
/*     */   public Rotation getRotation() {
/* 117 */     return this.rotation;
/*     */   }
/*     */   
/*     */   public BlockPos getRotationPivot() {
/* 121 */     return this.rotationPivot;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Random getRandom(@Nullable BlockPos debug1) {
/* 130 */     if (this.random != null) {
/* 131 */       return this.random;
/*     */     }
/*     */     
/* 134 */     if (debug1 == null) {
/* 135 */       return new Random(Util.getMillis());
/*     */     }
/*     */     
/* 138 */     return new Random(Mth.getSeed((Vec3i)debug1));
/*     */   }
/*     */   
/*     */   public boolean isIgnoreEntities() {
/* 142 */     return this.ignoreEntities;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public BoundingBox getBoundingBox() {
/* 147 */     if (this.boundingBox == null && this.chunkPos != null) {
/* 148 */       updateBoundingBoxFromChunkPos();
/*     */     }
/* 150 */     return this.boundingBox;
/*     */   }
/*     */   
/*     */   public boolean getKnownShape() {
/* 154 */     return this.knownShape;
/*     */   }
/*     */   
/*     */   public List<StructureProcessor> getProcessors() {
/* 158 */     return this.processors;
/*     */   }
/*     */   
/*     */   void updateBoundingBoxFromChunkPos() {
/* 162 */     if (this.chunkPos != null) {
/* 163 */       this.boundingBox = calculateBoundingBox(this.chunkPos);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean shouldKeepLiquids() {
/* 168 */     return this.keepLiquids;
/*     */   }
/*     */   
/*     */   public StructureTemplate.Palette getRandomPalette(List<StructureTemplate.Palette> debug1, @Nullable BlockPos debug2) {
/* 172 */     int debug3 = debug1.size();
/* 173 */     if (debug3 == 0)
/*     */     {
/* 175 */       throw new IllegalStateException("No palettes");
/*     */     }
/* 177 */     return debug1.get(getRandom(debug2).nextInt(debug3));
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private BoundingBox calculateBoundingBox(@Nullable ChunkPos debug1) {
/* 182 */     if (debug1 == null) {
/* 183 */       return this.boundingBox;
/*     */     }
/* 185 */     int debug2 = debug1.x * 16;
/* 186 */     int debug3 = debug1.z * 16;
/* 187 */     return new BoundingBox(debug2, 0, debug3, debug2 + 16 - 1, 255, debug3 + 16 - 1);
/*     */   }
/*     */   
/*     */   public StructurePlaceSettings setFinalizeEntities(boolean debug1) {
/* 191 */     this.finalizeEntities = debug1;
/* 192 */     return this;
/*     */   }
/*     */   
/*     */   public boolean shouldFinalizeEntities() {
/* 196 */     return this.finalizeEntities;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\StructurePlaceSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */