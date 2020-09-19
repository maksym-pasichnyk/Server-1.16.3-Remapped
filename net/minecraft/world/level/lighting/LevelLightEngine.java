/*     */ package net.minecraft.world.level.lighting;
/*     */ 
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.chunk.DataLayer;
/*     */ import net.minecraft.world.level.chunk.LightChunkGetter;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LevelLightEngine
/*     */   implements LightEventListener
/*     */ {
/*     */   @Nullable
/*     */   private final LayerLightEngine<?, ?> blockEngine;
/*     */   @Nullable
/*     */   private final LayerLightEngine<?, ?> skyEngine;
/*     */   
/*     */   public LevelLightEngine(LightChunkGetter debug1, boolean debug2, boolean debug3) {
/*  22 */     this.blockEngine = debug2 ? new BlockLightEngine(debug1) : null;
/*  23 */     this.skyEngine = debug3 ? new SkyLightEngine(debug1) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkBlock(BlockPos debug1) {
/*  29 */     if (this.blockEngine != null) {
/*  30 */       this.blockEngine.checkBlock(debug1);
/*     */     }
/*  32 */     if (this.skyEngine != null) {
/*  33 */       this.skyEngine.checkBlock(debug1);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onBlockEmissionIncrease(BlockPos debug1, int debug2) {
/*  40 */     if (this.blockEngine != null) {
/*  41 */       this.blockEngine.onBlockEmissionIncrease(debug1, debug2);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasLightWork() {
/*  48 */     if (this.skyEngine != null && this.skyEngine.hasLightWork()) {
/*  49 */       return true;
/*     */     }
/*  51 */     return (this.blockEngine != null && this.blockEngine.hasLightWork());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int runUpdates(int debug1, boolean debug2, boolean debug3) {
/*  57 */     if (this.blockEngine != null && this.skyEngine != null) {
/*  58 */       int debug4 = debug1 / 2;
/*  59 */       int debug5 = this.blockEngine.runUpdates(debug4, debug2, debug3);
/*  60 */       int debug6 = debug1 - debug4 + debug5;
/*  61 */       int debug7 = this.skyEngine.runUpdates(debug6, debug2, debug3);
/*  62 */       if (debug5 == 0 && debug7 > 0) {
/*  63 */         return this.blockEngine.runUpdates(debug7, debug2, debug3);
/*     */       }
/*  65 */       return debug7;
/*     */     } 
/*  67 */     if (this.blockEngine != null) {
/*  68 */       return this.blockEngine.runUpdates(debug1, debug2, debug3);
/*     */     }
/*  70 */     if (this.skyEngine != null) {
/*  71 */       return this.skyEngine.runUpdates(debug1, debug2, debug3);
/*     */     }
/*  73 */     return debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateSectionStatus(SectionPos debug1, boolean debug2) {
/*  83 */     if (this.blockEngine != null) {
/*  84 */       this.blockEngine.updateSectionStatus(debug1, debug2);
/*     */     }
/*  86 */     if (this.skyEngine != null) {
/*  87 */       this.skyEngine.updateSectionStatus(debug1, debug2);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void enableLightSources(ChunkPos debug1, boolean debug2) {
/*  94 */     if (this.blockEngine != null) {
/*  95 */       this.blockEngine.enableLightSources(debug1, debug2);
/*     */     }
/*  97 */     if (this.skyEngine != null) {
/*  98 */       this.skyEngine.enableLightSources(debug1, debug2);
/*     */     }
/*     */   }
/*     */   
/*     */   public LayerLightEventListener getLayerListener(LightLayer debug1) {
/* 103 */     if (debug1 == LightLayer.BLOCK) {
/* 104 */       if (this.blockEngine == null) {
/* 105 */         return LayerLightEventListener.DummyLightLayerEventListener.INSTANCE;
/*     */       }
/* 107 */       return this.blockEngine;
/*     */     } 
/* 109 */     if (this.skyEngine == null) {
/* 110 */       return LayerLightEventListener.DummyLightLayerEventListener.INSTANCE;
/*     */     }
/* 112 */     return this.skyEngine;
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
/*     */   public void queueSectionData(LightLayer debug1, SectionPos debug2, @Nullable DataLayer debug3, boolean debug4) {
/* 134 */     if (debug1 == LightLayer.BLOCK) {
/* 135 */       if (this.blockEngine != null) {
/* 136 */         this.blockEngine.queueSectionData(debug2.asLong(), debug3, debug4);
/*     */       }
/*     */     }
/* 139 */     else if (this.skyEngine != null) {
/* 140 */       this.skyEngine.queueSectionData(debug2.asLong(), debug3, debug4);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void retainData(ChunkPos debug1, boolean debug2) {
/* 146 */     if (this.blockEngine != null) {
/* 147 */       this.blockEngine.retainData(debug1, debug2);
/*     */     }
/* 149 */     if (this.skyEngine != null) {
/* 150 */       this.skyEngine.retainData(debug1, debug2);
/*     */     }
/*     */   }
/*     */   
/*     */   public int getRawBrightness(BlockPos debug1, int debug2) {
/* 155 */     int debug3 = (this.skyEngine == null) ? 0 : (this.skyEngine.getLightValue(debug1) - debug2);
/* 156 */     int debug4 = (this.blockEngine == null) ? 0 : this.blockEngine.getLightValue(debug1);
/*     */     
/* 158 */     return Math.max(debug4, debug3);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\lighting\LevelLightEngine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */