/*     */ package net.minecraft.world.level.lighting;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMaps;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.LongIterator;
/*     */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.server.level.SectionTracker;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.chunk.DataLayer;
/*     */ import net.minecraft.world.level.chunk.LightChunkGetter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class LayerLightSectionStorage<M extends DataLayerStorageMap<M>>
/*     */   extends SectionTracker
/*     */ {
/*  25 */   protected static final DataLayer EMPTY_DATA = new DataLayer();
/*  26 */   private static final Direction[] DIRECTIONS = Direction.values();
/*     */   
/*     */   private final LightLayer layer;
/*     */   
/*     */   private final LightChunkGetter chunkSource;
/*     */   
/*  32 */   protected final LongSet dataSectionSet = (LongSet)new LongOpenHashSet();
/*     */   
/*  34 */   protected final LongSet toMarkNoData = (LongSet)new LongOpenHashSet();
/*  35 */   protected final LongSet toMarkData = (LongSet)new LongOpenHashSet();
/*     */   
/*     */   protected volatile M visibleSectionData;
/*     */   
/*     */   protected final M updatingSectionData;
/*     */   
/*  41 */   protected final LongSet changedSections = (LongSet)new LongOpenHashSet();
/*  42 */   protected final LongSet sectionsAffectedByLightUpdates = (LongSet)new LongOpenHashSet();
/*     */ 
/*     */   
/*  45 */   protected final Long2ObjectMap<DataLayer> queuedSections = Long2ObjectMaps.synchronize((Long2ObjectMap)new Long2ObjectOpenHashMap());
/*     */   
/*  47 */   private final LongSet untrustedSections = (LongSet)new LongOpenHashSet();
/*     */   
/*  49 */   private final LongSet columnsToRetainQueuedDataFor = (LongSet)new LongOpenHashSet();
/*     */   
/*  51 */   private final LongSet toRemove = (LongSet)new LongOpenHashSet();
/*     */   
/*     */   protected volatile boolean hasToRemove;
/*     */   
/*     */   protected LayerLightSectionStorage(LightLayer debug1, LightChunkGetter debug2, M debug3) {
/*  56 */     super(3, 16, 256);
/*  57 */     this.layer = debug1;
/*  58 */     this.chunkSource = debug2;
/*  59 */     this.updatingSectionData = debug3;
/*  60 */     this.visibleSectionData = debug3.copy();
/*  61 */     this.visibleSectionData.disableCache();
/*     */   }
/*     */   
/*     */   protected boolean storingLightForSection(long debug1) {
/*  65 */     return (getDataLayer(debug1, true) != null);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected DataLayer getDataLayer(long debug1, boolean debug3) {
/*  70 */     return getDataLayer(debug3 ? this.updatingSectionData : this.visibleSectionData, debug1);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected DataLayer getDataLayer(M debug1, long debug2) {
/*  75 */     return debug1.getLayer(debug2);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public DataLayer getDataLayerData(long debug1) {
/*  80 */     DataLayer debug3 = (DataLayer)this.queuedSections.get(debug1);
/*  81 */     if (debug3 != null) {
/*  82 */       return debug3;
/*     */     }
/*  84 */     return getDataLayer(debug1, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getStoredLevel(long debug1) {
/*  94 */     long debug3 = SectionPos.blockToSection(debug1);
/*  95 */     DataLayer debug5 = getDataLayer(debug3, true);
/*  96 */     return debug5.get(
/*  97 */         SectionPos.sectionRelative(BlockPos.getX(debug1)), 
/*  98 */         SectionPos.sectionRelative(BlockPos.getY(debug1)), 
/*  99 */         SectionPos.sectionRelative(BlockPos.getZ(debug1)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setStoredLevel(long debug1, int debug3) {
/* 106 */     long debug4 = SectionPos.blockToSection(debug1);
/* 107 */     if (this.changedSections.add(debug4)) {
/* 108 */       this.updatingSectionData.copyDataLayer(debug4);
/*     */     }
/* 110 */     DataLayer debug6 = getDataLayer(debug4, true);
/* 111 */     debug6.set(
/* 112 */         SectionPos.sectionRelative(BlockPos.getX(debug1)), 
/* 113 */         SectionPos.sectionRelative(BlockPos.getY(debug1)), 
/* 114 */         SectionPos.sectionRelative(BlockPos.getZ(debug1)), debug3);
/*     */ 
/*     */     
/* 117 */     for (int debug7 = -1; debug7 <= 1; debug7++) {
/* 118 */       for (int debug8 = -1; debug8 <= 1; debug8++) {
/* 119 */         for (int debug9 = -1; debug9 <= 1; debug9++) {
/* 120 */           this.sectionsAffectedByLightUpdates.add(SectionPos.blockToSection(BlockPos.offset(debug1, debug8, debug9, debug7)));
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getLevel(long debug1) {
/* 128 */     if (debug1 == Long.MAX_VALUE) {
/* 129 */       return 2;
/*     */     }
/* 131 */     if (this.dataSectionSet.contains(debug1)) {
/* 132 */       return 0;
/*     */     }
/* 134 */     if (!this.toRemove.contains(debug1) && this.updatingSectionData.hasLayer(debug1)) {
/* 135 */       return 1;
/*     */     }
/* 137 */     return 2;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getLevelFromSource(long debug1) {
/* 142 */     if (this.toMarkNoData.contains(debug1)) {
/* 143 */       return 2;
/*     */     }
/* 145 */     if (this.dataSectionSet.contains(debug1) || this.toMarkData.contains(debug1)) {
/* 146 */       return 0;
/*     */     }
/* 148 */     return 2;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setLevel(long debug1, int debug3) {
/* 153 */     int debug4 = getLevel(debug1);
/* 154 */     if (debug4 != 0 && debug3 == 0) {
/* 155 */       this.dataSectionSet.add(debug1);
/* 156 */       this.toMarkData.remove(debug1);
/*     */     } 
/* 158 */     if (debug4 == 0 && debug3 != 0) {
/* 159 */       this.dataSectionSet.remove(debug1);
/* 160 */       this.toMarkNoData.remove(debug1);
/*     */     } 
/* 162 */     if (debug4 >= 2 && debug3 != 2) {
/* 163 */       if (this.toRemove.contains(debug1)) {
/* 164 */         this.toRemove.remove(debug1);
/*     */       } else {
/* 166 */         this.updatingSectionData.setLayer(debug1, createDataLayer(debug1));
/* 167 */         this.changedSections.add(debug1);
/* 168 */         onNodeAdded(debug1);
/* 169 */         for (int debug5 = -1; debug5 <= 1; debug5++) {
/* 170 */           for (int debug6 = -1; debug6 <= 1; debug6++) {
/* 171 */             for (int debug7 = -1; debug7 <= 1; debug7++) {
/* 172 */               this.sectionsAffectedByLightUpdates.add(SectionPos.blockToSection(BlockPos.offset(debug1, debug6, debug7, debug5)));
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/* 178 */     if (debug4 != 2 && debug3 >= 2) {
/* 179 */       this.toRemove.add(debug1);
/*     */     }
/* 181 */     this.hasToRemove = !this.toRemove.isEmpty();
/*     */   }
/*     */   
/*     */   protected DataLayer createDataLayer(long debug1) {
/* 185 */     DataLayer debug3 = (DataLayer)this.queuedSections.get(debug1);
/* 186 */     if (debug3 != null) {
/* 187 */       return debug3;
/*     */     }
/* 189 */     return new DataLayer();
/*     */   }
/*     */   
/*     */   protected void clearQueuedSectionBlocks(LayerLightEngine<?, ?> debug1, long debug2) {
/* 193 */     if (debug1.getQueueSize() < 8192) {
/* 194 */       debug1.removeIf(debug2 -> (SectionPos.blockToSection(debug2) == debug0));
/*     */       
/*     */       return;
/*     */     } 
/* 198 */     int debug4 = SectionPos.sectionToBlockCoord(SectionPos.x(debug2));
/* 199 */     int debug5 = SectionPos.sectionToBlockCoord(SectionPos.y(debug2));
/* 200 */     int debug6 = SectionPos.sectionToBlockCoord(SectionPos.z(debug2));
/* 201 */     for (int debug7 = 0; debug7 < 16; debug7++) {
/* 202 */       for (int debug8 = 0; debug8 < 16; debug8++) {
/* 203 */         for (int debug9 = 0; debug9 < 16; debug9++) {
/* 204 */           long debug10 = BlockPos.asLong(debug4 + debug7, debug5 + debug8, debug6 + debug9);
/* 205 */           debug1.removeFromQueue(debug10);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean hasInconsistencies() {
/* 212 */     return this.hasToRemove;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void markNewInconsistencies(LayerLightEngine<M, ?> debug1, boolean debug2, boolean debug3) {
/* 217 */     if (!hasInconsistencies() && this.queuedSections.isEmpty())
/*     */       return; 
/*     */     LongIterator<Long> longIterator;
/* 220 */     for (longIterator = this.toRemove.iterator(); longIterator.hasNext(); ) { long debug5 = ((Long)longIterator.next()).longValue();
/* 221 */       clearQueuedSectionBlocks(debug1, debug5);
/* 222 */       DataLayer debug7 = (DataLayer)this.queuedSections.remove(debug5);
/* 223 */       DataLayer debug8 = this.updatingSectionData.removeLayer(debug5);
/* 224 */       if (this.columnsToRetainQueuedDataFor.contains(SectionPos.getZeroNode(debug5))) {
/* 225 */         if (debug7 != null) {
/* 226 */           this.queuedSections.put(debug5, debug7); continue;
/* 227 */         }  if (debug8 != null) {
/* 228 */           this.queuedSections.put(debug5, debug8);
/*     */         }
/*     */       }  }
/*     */     
/* 232 */     this.updatingSectionData.clearCache();
/* 233 */     for (longIterator = this.toRemove.iterator(); longIterator.hasNext(); ) { long debug5 = ((Long)longIterator.next()).longValue();
/* 234 */       onNodeRemoved(debug5); }
/*     */     
/* 236 */     this.toRemove.clear();
/* 237 */     this.hasToRemove = false;
/*     */     
/* 239 */     for (ObjectIterator<Long2ObjectMap.Entry<DataLayer>> objectIterator1 = this.queuedSections.long2ObjectEntrySet().iterator(); objectIterator1.hasNext(); ) { Long2ObjectMap.Entry<DataLayer> debug5 = objectIterator1.next();
/* 240 */       long debug6 = debug5.getLongKey();
/* 241 */       if (!storingLightForSection(debug6)) {
/*     */         continue;
/*     */       }
/* 244 */       DataLayer debug8 = (DataLayer)debug5.getValue();
/*     */       
/* 246 */       if (this.updatingSectionData.getLayer(debug6) != debug8) {
/*     */         
/* 248 */         clearQueuedSectionBlocks(debug1, debug6);
/*     */ 
/*     */         
/* 251 */         this.updatingSectionData.setLayer(debug6, debug8);
/* 252 */         this.changedSections.add(debug6);
/*     */       }  }
/*     */     
/* 255 */     this.updatingSectionData.clearCache();
/* 256 */     if (!debug3) {
/*     */       
/* 258 */       for (LongIterator<Long> longIterator1 = this.queuedSections.keySet().iterator(); longIterator1.hasNext(); ) { long debug5 = ((Long)longIterator1.next()).longValue();
/* 259 */         checkEdgesForSection(debug1, debug5); }
/*     */     
/*     */     } else {
/* 262 */       for (LongIterator<Long> longIterator1 = this.untrustedSections.iterator(); longIterator1.hasNext(); ) { long debug5 = ((Long)longIterator1.next()).longValue();
/* 263 */         checkEdgesForSection(debug1, debug5); }
/*     */     
/*     */     } 
/* 266 */     this.untrustedSections.clear();
/*     */     
/* 268 */     for (ObjectIterator<Long2ObjectMap.Entry<DataLayer>> debug4 = this.queuedSections.long2ObjectEntrySet().iterator(); debug4.hasNext(); ) {
/* 269 */       Long2ObjectMap.Entry<DataLayer> debug5 = (Long2ObjectMap.Entry<DataLayer>)debug4.next();
/* 270 */       long debug6 = debug5.getLongKey();
/* 271 */       if (storingLightForSection(debug6)) {
/* 272 */         debug4.remove();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void checkEdgesForSection(LayerLightEngine<M, ?> debug1, long debug2) {
/* 278 */     if (!storingLightForSection(debug2)) {
/*     */       return;
/*     */     }
/* 281 */     int debug4 = SectionPos.sectionToBlockCoord(SectionPos.x(debug2));
/* 282 */     int debug5 = SectionPos.sectionToBlockCoord(SectionPos.y(debug2));
/* 283 */     int debug6 = SectionPos.sectionToBlockCoord(SectionPos.z(debug2));
/* 284 */     for (Direction debug10 : DIRECTIONS) {
/* 285 */       long debug11 = SectionPos.offset(debug2, debug10);
/*     */ 
/*     */       
/* 288 */       if (!this.queuedSections.containsKey(debug11) && storingLightForSection(debug11))
/*     */       {
/*     */ 
/*     */         
/* 292 */         for (int debug13 = 0; debug13 < 16; debug13++) {
/* 293 */           for (int debug14 = 0; debug14 < 16; debug14++) {
/*     */             long debug15, debug17;
/*     */             
/* 296 */             switch (debug10) {
/*     */               case DOWN:
/* 298 */                 debug15 = BlockPos.asLong(debug4 + debug14, debug5, debug6 + debug13);
/* 299 */                 debug17 = BlockPos.asLong(debug4 + debug14, debug5 - 1, debug6 + debug13);
/*     */                 break;
/*     */               case UP:
/* 302 */                 debug15 = BlockPos.asLong(debug4 + debug14, debug5 + 16 - 1, debug6 + debug13);
/* 303 */                 debug17 = BlockPos.asLong(debug4 + debug14, debug5 + 16, debug6 + debug13);
/*     */                 break;
/*     */               case NORTH:
/* 306 */                 debug15 = BlockPos.asLong(debug4 + debug13, debug5 + debug14, debug6);
/* 307 */                 debug17 = BlockPos.asLong(debug4 + debug13, debug5 + debug14, debug6 - 1);
/*     */                 break;
/*     */               case SOUTH:
/* 310 */                 debug15 = BlockPos.asLong(debug4 + debug13, debug5 + debug14, debug6 + 16 - 1);
/* 311 */                 debug17 = BlockPos.asLong(debug4 + debug13, debug5 + debug14, debug6 + 16);
/*     */                 break;
/*     */               case WEST:
/* 314 */                 debug15 = BlockPos.asLong(debug4, debug5 + debug13, debug6 + debug14);
/* 315 */                 debug17 = BlockPos.asLong(debug4 - 1, debug5 + debug13, debug6 + debug14);
/*     */                 break;
/*     */               
/*     */               default:
/* 319 */                 debug15 = BlockPos.asLong(debug4 + 16 - 1, debug5 + debug13, debug6 + debug14);
/* 320 */                 debug17 = BlockPos.asLong(debug4 + 16, debug5 + debug13, debug6 + debug14);
/*     */                 break;
/*     */             } 
/*     */             
/* 324 */             debug1.checkEdge(debug15, debug17, debug1.computeLevelFromNeighbor(debug15, debug17, debug1.getLevel(debug15)), false);
/* 325 */             debug1.checkEdge(debug17, debug15, debug1.computeLevelFromNeighbor(debug17, debug15, debug1.getLevel(debug17)), false);
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onNodeAdded(long debug1) {}
/*     */ 
/*     */   
/*     */   protected void onNodeRemoved(long debug1) {}
/*     */   
/*     */   protected void enableLightSources(long debug1, boolean debug3) {}
/*     */   
/*     */   public void retainData(long debug1, boolean debug3) {
/* 341 */     if (debug3) {
/* 342 */       this.columnsToRetainQueuedDataFor.add(debug1);
/*     */     } else {
/* 344 */       this.columnsToRetainQueuedDataFor.remove(debug1);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void queueSectionData(long debug1, @Nullable DataLayer debug3, boolean debug4) {
/* 349 */     if (debug3 != null) {
/* 350 */       this.queuedSections.put(debug1, debug3);
/* 351 */       if (!debug4) {
/* 352 */         this.untrustedSections.add(debug1);
/*     */       }
/*     */     } else {
/* 355 */       this.queuedSections.remove(debug1);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void updateSectionStatus(long debug1, boolean debug3) {
/* 360 */     boolean debug4 = this.dataSectionSet.contains(debug1);
/* 361 */     if (!debug4 && !debug3) {
/* 362 */       this.toMarkData.add(debug1);
/* 363 */       checkEdge(Long.MAX_VALUE, debug1, 0, true);
/*     */     } 
/* 365 */     if (debug4 && debug3) {
/* 366 */       this.toMarkNoData.add(debug1);
/* 367 */       checkEdge(Long.MAX_VALUE, debug1, 2, false);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void runAllUpdates() {
/* 372 */     if (hasWork()) {
/* 373 */       runUpdates(2147483647);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void swapSectionMap() {
/* 378 */     if (!this.changedSections.isEmpty()) {
/* 379 */       M debug1 = this.updatingSectionData.copy();
/* 380 */       debug1.disableCache();
/* 381 */       this.visibleSectionData = debug1;
/* 382 */       this.changedSections.clear();
/*     */     } 
/* 384 */     if (!this.sectionsAffectedByLightUpdates.isEmpty()) {
/* 385 */       LongIterator debug1 = this.sectionsAffectedByLightUpdates.iterator();
/* 386 */       while (debug1.hasNext()) {
/* 387 */         long debug2 = debug1.nextLong();
/* 388 */         this.chunkSource.onLightUpdate(this.layer, SectionPos.of(debug2));
/*     */       } 
/* 390 */       this.sectionsAffectedByLightUpdates.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract int getLightValue(long paramLong);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\lighting\LayerLightSectionStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */