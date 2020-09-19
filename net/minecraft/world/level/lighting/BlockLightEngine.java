/*     */ package net.minecraft.world.level.lighting;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.DataLayer;
/*     */ import net.minecraft.world.level.chunk.LightChunkGetter;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ import org.apache.commons.lang3.mutable.MutableInt;
/*     */ 
/*     */ public final class BlockLightEngine extends LayerLightEngine<BlockLightSectionStorage.BlockDataLayerStorageMap, BlockLightSectionStorage> {
/*  16 */   private static final Direction[] DIRECTIONS = Direction.values();
/*  17 */   private final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/*     */   
/*     */   public BlockLightEngine(LightChunkGetter debug1) {
/*  20 */     super(debug1, LightLayer.BLOCK, new BlockLightSectionStorage(debug1));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int getLightEmission(long debug1) {
/*  26 */     int debug3 = BlockPos.getX(debug1);
/*  27 */     int debug4 = BlockPos.getY(debug1);
/*  28 */     int debug5 = BlockPos.getZ(debug1);
/*  29 */     BlockGetter debug6 = this.chunkSource.getChunkForLighting(debug3 >> 4, debug5 >> 4);
/*  30 */     if (debug6 != null) {
/*  31 */       return debug6.getLightEmission((BlockPos)this.pos.set(debug3, debug4, debug5));
/*     */     }
/*  33 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int computeLevelFromNeighbor(long debug1, long debug3, int debug5) {
/*  38 */     if (debug3 == Long.MAX_VALUE) {
/*  39 */       return 15;
/*     */     }
/*  41 */     if (debug1 == Long.MAX_VALUE) {
/*  42 */       return debug5 + 15 - getLightEmission(debug3);
/*     */     }
/*  44 */     if (debug5 >= 15) {
/*  45 */       return debug5;
/*     */     }
/*     */     
/*  48 */     int debug6 = Integer.signum(BlockPos.getX(debug3) - BlockPos.getX(debug1));
/*  49 */     int debug7 = Integer.signum(BlockPos.getY(debug3) - BlockPos.getY(debug1));
/*  50 */     int debug8 = Integer.signum(BlockPos.getZ(debug3) - BlockPos.getZ(debug1));
/*  51 */     Direction debug9 = Direction.fromNormal(debug6, debug7, debug8);
/*  52 */     if (debug9 == null) {
/*  53 */       return 15;
/*     */     }
/*     */     
/*  56 */     MutableInt debug10 = new MutableInt();
/*  57 */     BlockState debug11 = getStateAndOpacity(debug3, debug10);
/*     */     
/*  59 */     if (debug10.getValue().intValue() >= 15) {
/*  60 */       return 15;
/*     */     }
/*     */     
/*  63 */     BlockState debug12 = getStateAndOpacity(debug1, (MutableInt)null);
/*     */     
/*  65 */     VoxelShape debug13 = getShape(debug12, debug1, debug9);
/*  66 */     VoxelShape debug14 = getShape(debug11, debug3, debug9.getOpposite());
/*     */     
/*  68 */     if (Shapes.faceShapeOccludes(debug13, debug14)) {
/*  69 */       return 15;
/*     */     }
/*  71 */     return debug5 + Math.max(1, debug10.getValue().intValue());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void checkNeighborsAfterUpdate(long debug1, int debug3, boolean debug4) {
/*  76 */     long debug5 = SectionPos.blockToSection(debug1);
/*  77 */     for (Direction debug10 : DIRECTIONS) {
/*  78 */       long debug11 = BlockPos.offset(debug1, debug10);
/*  79 */       long debug13 = SectionPos.blockToSection(debug11);
/*  80 */       if (debug5 == debug13 || this.storage.storingLightForSection(debug13)) {
/*  81 */         checkNeighbor(debug1, debug11, debug3, debug4);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getComputedLevel(long debug1, long debug3, int debug5) {
/*  88 */     int debug6 = debug5;
/*  89 */     if (Long.MAX_VALUE != debug3) {
/*  90 */       int i = computeLevelFromNeighbor(Long.MAX_VALUE, debug1, 0);
/*  91 */       if (debug6 > i) {
/*  92 */         debug6 = i;
/*     */       }
/*  94 */       if (debug6 == 0) {
/*  95 */         return debug6;
/*     */       }
/*     */     } 
/*  98 */     long debug7 = SectionPos.blockToSection(debug1);
/*  99 */     DataLayer debug9 = this.storage.getDataLayer(debug7, true);
/* 100 */     for (Direction debug13 : DIRECTIONS) {
/* 101 */       long debug14 = BlockPos.offset(debug1, debug13);
/* 102 */       if (debug14 != debug3) {
/*     */         DataLayer debug18;
/*     */         
/* 105 */         long debug16 = SectionPos.blockToSection(debug14);
/*     */         
/* 107 */         if (debug7 == debug16) {
/* 108 */           debug18 = debug9;
/*     */         } else {
/* 110 */           debug18 = this.storage.getDataLayer(debug16, true);
/*     */         } 
/* 112 */         if (debug18 != null) {
/* 113 */           int debug19 = computeLevelFromNeighbor(debug14, debug1, getLevel(debug18, debug14));
/* 114 */           if (debug6 > debug19) {
/* 115 */             debug6 = debug19;
/*     */           }
/* 117 */           if (debug6 == 0)
/* 118 */             return debug6; 
/*     */         } 
/*     */       } 
/*     */     } 
/* 122 */     return debug6;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onBlockEmissionIncrease(BlockPos debug1, int debug2) {
/* 128 */     this.storage.runAllUpdates();
/* 129 */     checkEdge(Long.MAX_VALUE, debug1.asLong(), 15 - debug2, true);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\lighting\BlockLightEngine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */