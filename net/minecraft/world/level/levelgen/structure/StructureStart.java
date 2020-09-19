/*     */ package net.minecraft.world.level.levelgen.structure;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.StructureFeatureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*     */ import net.minecraft.world.level.levelgen.feature.StructureFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.MineshaftConfiguration;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*     */ 
/*     */ public abstract class StructureStart<C extends FeatureConfiguration> {
/*  26 */   public static final StructureStart<?> INVALID_START = new StructureStart<MineshaftConfiguration>(StructureFeature.MINESHAFT, 0, 0, BoundingBox.getUnknownBox(), 0, 0L)
/*     */     {
/*     */       public void generatePieces(RegistryAccess debug1, ChunkGenerator debug2, StructureManager debug3, int debug4, int debug5, Biome debug6, MineshaftConfiguration debug7) {}
/*     */     };
/*     */ 
/*     */   
/*     */   private final StructureFeature<C> feature;
/*  33 */   protected final List<StructurePiece> pieces = Lists.newArrayList();
/*     */   
/*     */   protected BoundingBox boundingBox;
/*     */   private final int chunkX;
/*     */   private final int chunkZ;
/*     */   private int references;
/*     */   protected final WorldgenRandom random;
/*     */   
/*     */   public StructureStart(StructureFeature<C> debug1, int debug2, int debug3, BoundingBox debug4, int debug5, long debug6) {
/*  42 */     this.feature = debug1;
/*  43 */     this.chunkX = debug2;
/*  44 */     this.chunkZ = debug3;
/*  45 */     this.references = debug5;
/*  46 */     this.random = new WorldgenRandom();
/*  47 */     this.random.setLargeFeatureSeed(debug6, debug2, debug3);
/*  48 */     this.boundingBox = debug4;
/*     */   }
/*     */   
/*     */   public abstract void generatePieces(RegistryAccess paramRegistryAccess, ChunkGenerator paramChunkGenerator, StructureManager paramStructureManager, int paramInt1, int paramInt2, Biome paramBiome, C paramC);
/*     */   
/*     */   public BoundingBox getBoundingBox() {
/*  54 */     return this.boundingBox;
/*     */   }
/*     */   
/*     */   public List<StructurePiece> getPieces() {
/*  58 */     return this.pieces;
/*     */   }
/*     */   
/*     */   public void placeInChunk(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6) {
/*  62 */     synchronized (this.pieces) {
/*  63 */       if (this.pieces.isEmpty()) {
/*     */         return;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  69 */       BoundingBox debug8 = ((StructurePiece)this.pieces.get(0)).boundingBox;
/*  70 */       Vec3i debug9 = debug8.getCenter();
/*  71 */       BlockPos debug10 = new BlockPos(debug9.getX(), debug8.y0, debug9.getZ());
/*  72 */       Iterator<StructurePiece> debug11 = this.pieces.iterator();
/*  73 */       while (debug11.hasNext()) {
/*  74 */         StructurePiece debug12 = debug11.next();
/*  75 */         if (debug12.getBoundingBox().intersects(debug5) && 
/*  76 */           !debug12.postProcess(debug1, debug2, debug3, debug4, debug5, debug6, debug10)) {
/*  77 */           debug11.remove();
/*     */         }
/*     */       } 
/*     */       
/*  81 */       calculateBoundingBox();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void calculateBoundingBox() {
/*  86 */     this.boundingBox = BoundingBox.getUnknownBox();
/*     */     
/*  88 */     for (StructurePiece debug2 : this.pieces) {
/*  89 */       this.boundingBox.expand(debug2.getBoundingBox());
/*     */     }
/*     */   }
/*     */   
/*     */   public CompoundTag createTag(int debug1, int debug2) {
/*  94 */     CompoundTag debug3 = new CompoundTag();
/*     */     
/*  96 */     if (isValid()) {
/*  97 */       debug3.putString("id", Registry.STRUCTURE_FEATURE.getKey(getFeature()).toString());
/*     */     } else {
/*  99 */       debug3.putString("id", "INVALID");
/* 100 */       return debug3;
/*     */     } 
/* 102 */     debug3.putInt("ChunkX", debug1);
/* 103 */     debug3.putInt("ChunkZ", debug2);
/* 104 */     debug3.putInt("references", this.references);
/* 105 */     debug3.put("BB", (Tag)this.boundingBox.createTag());
/*     */     
/* 107 */     ListTag debug4 = new ListTag();
/* 108 */     synchronized (this.pieces) {
/* 109 */       for (StructurePiece debug7 : this.pieces) {
/* 110 */         debug4.add(debug7.createTag());
/*     */       }
/*     */     } 
/* 113 */     debug3.put("Children", (Tag)debug4);
/*     */     
/* 115 */     return debug3;
/*     */   }
/*     */   
/*     */   protected void moveBelowSeaLevel(int debug1, Random debug2, int debug3) {
/* 119 */     int debug4 = debug1 - debug3;
/*     */ 
/*     */     
/* 122 */     int debug5 = this.boundingBox.getYSpan() + 1;
/*     */     
/* 124 */     if (debug5 < debug4) {
/* 125 */       debug5 += debug2.nextInt(debug4 - debug5);
/*     */     }
/*     */ 
/*     */     
/* 129 */     int debug6 = debug5 - this.boundingBox.y1;
/* 130 */     this.boundingBox.move(0, debug6, 0);
/* 131 */     for (StructurePiece debug8 : this.pieces) {
/* 132 */       debug8.move(0, debug6, 0);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void moveInsideHeights(Random debug1, int debug2, int debug3) {
/* 137 */     int debug5, debug4 = debug3 - debug2 + 1 - this.boundingBox.getYSpan();
/*     */ 
/*     */     
/* 140 */     if (debug4 > 1) {
/* 141 */       debug5 = debug2 + debug1.nextInt(debug4);
/*     */     } else {
/* 143 */       debug5 = debug2;
/*     */     } 
/*     */ 
/*     */     
/* 147 */     int debug6 = debug5 - this.boundingBox.y0;
/* 148 */     this.boundingBox.move(0, debug6, 0);
/* 149 */     for (StructurePiece debug8 : this.pieces) {
/* 150 */       debug8.move(0, debug6, 0);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isValid() {
/* 155 */     return !this.pieces.isEmpty();
/*     */   }
/*     */   
/*     */   public int getChunkX() {
/* 159 */     return this.chunkX;
/*     */   }
/*     */   
/*     */   public int getChunkZ() {
/* 163 */     return this.chunkZ;
/*     */   }
/*     */   
/*     */   public BlockPos getLocatePos() {
/* 167 */     return new BlockPos(this.chunkX << 4, 0, this.chunkZ << 4);
/*     */   }
/*     */   
/*     */   public boolean canBeReferenced() {
/* 171 */     return (this.references < getMaxReferences());
/*     */   }
/*     */   
/*     */   public void addReference() {
/* 175 */     this.references++;
/*     */   }
/*     */   
/*     */   public int getReferences() {
/* 179 */     return this.references;
/*     */   }
/*     */   
/*     */   protected int getMaxReferences() {
/* 183 */     return 1;
/*     */   }
/*     */   
/*     */   public StructureFeature<?> getFeature() {
/* 187 */     return this.feature;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\StructureStart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */