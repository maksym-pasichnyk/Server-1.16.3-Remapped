/*     */ package net.minecraft.world.level.lighting;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.DataLayer;
/*     */ import net.minecraft.world.level.chunk.LightChunkGetter;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ import org.apache.commons.lang3.mutable.MutableInt;
/*     */ 
/*     */ public abstract class LayerLightEngine<M extends DataLayerStorageMap<M>, S extends LayerLightSectionStorage<M>>
/*     */   extends DynamicGraphMinFixedPoint
/*     */   implements LayerLightEventListener {
/*  22 */   private static final Direction[] DIRECTIONS = Direction.values();
/*     */   protected final LightChunkGetter chunkSource;
/*     */   protected final LightLayer layer;
/*     */   protected final S storage;
/*     */   private boolean runningLightUpdates;
/*  27 */   protected final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/*     */ 
/*     */   
/*  30 */   private final long[] lastChunkPos = new long[2];
/*  31 */   private final BlockGetter[] lastChunk = new BlockGetter[2];
/*     */   
/*     */   public LayerLightEngine(LightChunkGetter debug1, LightLayer debug2, S debug3) {
/*  34 */     super(16, 256, 8192);
/*  35 */     this.chunkSource = debug1;
/*  36 */     this.layer = debug2;
/*  37 */     this.storage = debug3;
/*  38 */     clearCache();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkNode(long debug1) {
/*  44 */     this.storage.runAllUpdates();
/*     */     
/*  46 */     if (this.storage.storingLightForSection(SectionPos.blockToSection(debug1))) {
/*  47 */       super.checkNode(debug1);
/*     */     }
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private BlockGetter getChunk(int debug1, int debug2) {
/*  53 */     long debug3 = ChunkPos.asLong(debug1, debug2);
/*  54 */     for (int i = 0; i < 2; i++) {
/*  55 */       if (debug3 == this.lastChunkPos[i]) {
/*  56 */         return this.lastChunk[i];
/*     */       }
/*     */     } 
/*  59 */     BlockGetter debug5 = this.chunkSource.getChunkForLighting(debug1, debug2);
/*  60 */     for (int debug6 = 1; debug6 > 0; debug6--) {
/*  61 */       this.lastChunkPos[debug6] = this.lastChunkPos[debug6 - 1];
/*  62 */       this.lastChunk[debug6] = this.lastChunk[debug6 - 1];
/*     */     } 
/*  64 */     this.lastChunkPos[0] = debug3;
/*  65 */     this.lastChunk[0] = debug5;
/*  66 */     return debug5;
/*     */   }
/*     */   
/*     */   private void clearCache() {
/*  70 */     Arrays.fill(this.lastChunkPos, ChunkPos.INVALID_CHUNK_POS);
/*  71 */     Arrays.fill((Object[])this.lastChunk, (Object)null);
/*     */   }
/*     */   
/*     */   protected BlockState getStateAndOpacity(long debug1, @Nullable MutableInt debug3) {
/*  75 */     if (debug1 == Long.MAX_VALUE) {
/*  76 */       if (debug3 != null) {
/*  77 */         debug3.setValue(0);
/*     */       }
/*  79 */       return Blocks.AIR.defaultBlockState();
/*     */     } 
/*     */     
/*  82 */     int debug4 = SectionPos.blockToSectionCoord(BlockPos.getX(debug1));
/*  83 */     int debug5 = SectionPos.blockToSectionCoord(BlockPos.getZ(debug1));
/*     */     
/*  85 */     BlockGetter debug6 = getChunk(debug4, debug5);
/*     */     
/*  87 */     if (debug6 == null) {
/*  88 */       if (debug3 != null) {
/*  89 */         debug3.setValue(16);
/*     */       }
/*     */       
/*  92 */       return Blocks.BEDROCK.defaultBlockState();
/*     */     } 
/*  94 */     this.pos.set(debug1);
/*  95 */     BlockState debug7 = debug6.getBlockState((BlockPos)this.pos);
/*  96 */     boolean debug8 = (debug7.canOcclude() && debug7.useShapeForLightOcclusion());
/*  97 */     if (debug3 != null) {
/*  98 */       debug3.setValue(debug7.getLightBlock(this.chunkSource.getLevel(), (BlockPos)this.pos));
/*     */     }
/* 100 */     return debug8 ? debug7 : Blocks.AIR.defaultBlockState();
/*     */   }
/*     */   
/*     */   protected VoxelShape getShape(BlockState debug1, long debug2, Direction debug4) {
/* 104 */     return debug1.canOcclude() ? debug1.getFaceOcclusionShape(this.chunkSource.getLevel(), (BlockPos)this.pos.set(debug2), debug4) : Shapes.empty();
/*     */   }
/*     */   
/*     */   public static int getLightBlockInto(BlockGetter debug0, BlockState debug1, BlockPos debug2, BlockState debug3, BlockPos debug4, Direction debug5, int debug6) {
/* 108 */     boolean debug7 = (debug1.canOcclude() && debug1.useShapeForLightOcclusion());
/* 109 */     boolean debug8 = (debug3.canOcclude() && debug3.useShapeForLightOcclusion());
/*     */     
/* 111 */     if (!debug7 && !debug8) {
/* 112 */       return debug6;
/*     */     }
/*     */     
/* 115 */     VoxelShape debug9 = debug7 ? debug1.getOcclusionShape(debug0, debug2) : Shapes.empty();
/* 116 */     VoxelShape debug10 = debug8 ? debug3.getOcclusionShape(debug0, debug4) : Shapes.empty();
/*     */     
/* 118 */     if (Shapes.mergedFaceOccludes(debug9, debug10, debug5)) {
/* 119 */       return 16;
/*     */     }
/*     */     
/* 122 */     return debug6;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isSource(long debug1) {
/* 127 */     return (debug1 == Long.MAX_VALUE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getComputedLevel(long debug1, long debug3, int debug5) {
/* 135 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getLevel(long debug1) {
/* 140 */     if (debug1 == Long.MAX_VALUE) {
/* 141 */       return 0;
/*     */     }
/* 143 */     return 15 - this.storage.getStoredLevel(debug1);
/*     */   }
/*     */   
/*     */   protected int getLevel(DataLayer debug1, long debug2) {
/* 147 */     return 15 - debug1.get(
/* 148 */         SectionPos.sectionRelative(BlockPos.getX(debug2)), 
/* 149 */         SectionPos.sectionRelative(BlockPos.getY(debug2)), 
/* 150 */         SectionPos.sectionRelative(BlockPos.getZ(debug2)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setLevel(long debug1, int debug3) {
/* 156 */     this.storage.setStoredLevel(debug1, Math.min(15, 15 - debug3));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int computeLevelFromNeighbor(long debug1, long debug3, int debug5) {
/* 164 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasLightWork() {
/* 169 */     return (hasWork() || this.storage.hasWork() || this.storage.hasInconsistencies());
/*     */   }
/*     */ 
/*     */   
/*     */   public int runUpdates(int debug1, boolean debug2, boolean debug3) {
/* 174 */     if (!this.runningLightUpdates) {
/*     */ 
/*     */       
/* 177 */       if (this.storage.hasWork()) {
/* 178 */         debug1 = this.storage.runUpdates(debug1);
/*     */         
/* 180 */         if (debug1 == 0) {
/* 181 */           return debug1;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 186 */       this.storage.markNewInconsistencies(this, debug2, debug3);
/*     */     } 
/*     */     
/* 189 */     this.runningLightUpdates = true;
/*     */     
/* 191 */     if (hasWork()) {
/*     */       
/* 193 */       debug1 = runUpdates(debug1);
/* 194 */       clearCache();
/*     */       
/* 196 */       if (debug1 == 0) {
/* 197 */         return debug1;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 202 */     this.runningLightUpdates = false;
/* 203 */     this.storage.swapSectionMap();
/*     */     
/* 205 */     return debug1;
/*     */   }
/*     */   
/*     */   protected void queueSectionData(long debug1, @Nullable DataLayer debug3, boolean debug4) {
/* 209 */     this.storage.queueSectionData(debug1, debug3, debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public DataLayer getDataLayerData(SectionPos debug1) {
/* 215 */     return this.storage.getDataLayerData(debug1.asLong());
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLightValue(BlockPos debug1) {
/* 220 */     return this.storage.getLightValue(debug1.asLong());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkBlock(BlockPos debug1) {
/* 229 */     long debug2 = debug1.asLong();
/* 230 */     checkNode(debug2);
/* 231 */     for (Direction debug7 : DIRECTIONS) {
/* 232 */       checkNode(BlockPos.offset(debug2, debug7));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onBlockEmissionIncrease(BlockPos debug1, int debug2) {}
/*     */ 
/*     */   
/*     */   public void updateSectionStatus(SectionPos debug1, boolean debug2) {
/* 242 */     this.storage.updateSectionStatus(debug1.asLong(), debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void enableLightSources(ChunkPos debug1, boolean debug2) {
/* 247 */     long debug3 = SectionPos.getZeroNode(SectionPos.asLong(debug1.x, 0, debug1.z));
/* 248 */     this.storage.enableLightSources(debug3, debug2);
/*     */   }
/*     */   
/*     */   public void retainData(ChunkPos debug1, boolean debug2) {
/* 252 */     long debug3 = SectionPos.getZeroNode(SectionPos.asLong(debug1.x, 0, debug1.z));
/* 253 */     this.storage.retainData(debug3, debug2);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\lighting\LayerLightEngine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */