/*     */ package net.minecraft.world.level.lighting;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.LongIterator;
/*     */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import java.util.Arrays;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.chunk.DataLayer;
/*     */ import net.minecraft.world.level.chunk.LightChunkGetter;
/*     */ 
/*     */ public class SkyLightSectionStorage extends LayerLightSectionStorage<SkyLightSectionStorage.SkyDataLayerStorageMap> {
/*  17 */   private static final Direction[] HORIZONTALS = new Direction[] { Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST };
/*     */   
/*  19 */   private final LongSet sectionsWithSources = (LongSet)new LongOpenHashSet();
/*  20 */   private final LongSet sectionsToAddSourcesTo = (LongSet)new LongOpenHashSet();
/*  21 */   private final LongSet sectionsToRemoveSourcesFrom = (LongSet)new LongOpenHashSet();
/*  22 */   private final LongSet columnsWithSkySources = (LongSet)new LongOpenHashSet();
/*     */   private volatile boolean hasSourceInconsistencies;
/*     */   
/*     */   protected SkyLightSectionStorage(LightChunkGetter debug1) {
/*  26 */     super(LightLayer.SKY, debug1, new SkyDataLayerStorageMap(new Long2ObjectOpenHashMap(), new Long2IntOpenHashMap(), 2147483647));
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getLightValue(long debug1) {
/*  31 */     long debug3 = SectionPos.blockToSection(debug1);
/*  32 */     int debug5 = SectionPos.y(debug3);
/*  33 */     SkyDataLayerStorageMap debug6 = this.visibleSectionData;
/*  34 */     int debug7 = debug6.topSections.get(SectionPos.getZeroNode(debug3));
/*  35 */     if (debug7 == debug6.currentLowestY || debug5 >= debug7) {
/*  36 */       return 15;
/*     */     }
/*  38 */     DataLayer debug8 = getDataLayer(debug6, debug3);
/*  39 */     if (debug8 == null) {
/*  40 */       debug1 = BlockPos.getFlatIndex(debug1);
/*  41 */       while (debug8 == null) {
/*  42 */         debug3 = SectionPos.offset(debug3, Direction.UP);
/*  43 */         debug5++;
/*  44 */         if (debug5 >= debug7) {
/*  45 */           return 15;
/*     */         }
/*  47 */         debug1 = BlockPos.offset(debug1, 0, 16, 0);
/*  48 */         debug8 = getDataLayer(debug6, debug3);
/*     */       } 
/*     */     } 
/*  51 */     return debug8.get(
/*  52 */         SectionPos.sectionRelative(BlockPos.getX(debug1)), 
/*  53 */         SectionPos.sectionRelative(BlockPos.getY(debug1)), 
/*  54 */         SectionPos.sectionRelative(BlockPos.getZ(debug1)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onNodeAdded(long debug1) {
/*  60 */     int debug3 = SectionPos.y(debug1);
/*  61 */     if (this.updatingSectionData.currentLowestY > debug3) {
/*  62 */       this.updatingSectionData.currentLowestY = debug3;
/*  63 */       this.updatingSectionData.topSections.defaultReturnValue(this.updatingSectionData.currentLowestY);
/*     */     } 
/*  65 */     long debug4 = SectionPos.getZeroNode(debug1);
/*  66 */     int debug6 = this.updatingSectionData.topSections.get(debug4);
/*  67 */     if (debug6 < debug3 + 1) {
/*  68 */       this.updatingSectionData.topSections.put(debug4, debug3 + 1);
/*  69 */       if (this.columnsWithSkySources.contains(debug4)) {
/*  70 */         queueAddSource(debug1);
/*  71 */         if (debug6 > this.updatingSectionData.currentLowestY) {
/*  72 */           long debug7 = SectionPos.asLong(SectionPos.x(debug1), debug6 - 1, SectionPos.z(debug1));
/*  73 */           queueRemoveSource(debug7);
/*     */         } 
/*  75 */         recheckInconsistencyFlag();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void queueRemoveSource(long debug1) {
/*  81 */     this.sectionsToRemoveSourcesFrom.add(debug1);
/*  82 */     this.sectionsToAddSourcesTo.remove(debug1);
/*     */   }
/*     */   
/*     */   private void queueAddSource(long debug1) {
/*  86 */     this.sectionsToAddSourcesTo.add(debug1);
/*  87 */     this.sectionsToRemoveSourcesFrom.remove(debug1);
/*     */   }
/*     */   
/*     */   private void recheckInconsistencyFlag() {
/*  91 */     this.hasSourceInconsistencies = (!this.sectionsToAddSourcesTo.isEmpty() || !this.sectionsToRemoveSourcesFrom.isEmpty());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onNodeRemoved(long debug1) {
/*  96 */     long debug3 = SectionPos.getZeroNode(debug1);
/*  97 */     boolean debug5 = this.columnsWithSkySources.contains(debug3);
/*  98 */     if (debug5) {
/*  99 */       queueRemoveSource(debug1);
/*     */     }
/* 101 */     int debug6 = SectionPos.y(debug1);
/* 102 */     if (this.updatingSectionData.topSections.get(debug3) == debug6 + 1) {
/* 103 */       long debug7 = debug1;
/* 104 */       while (!storingLightForSection(debug7) && hasSectionsBelow(debug6)) {
/* 105 */         debug6--;
/* 106 */         debug7 = SectionPos.offset(debug7, Direction.DOWN);
/*     */       } 
/* 108 */       if (storingLightForSection(debug7)) {
/* 109 */         this.updatingSectionData.topSections.put(debug3, debug6 + 1);
/* 110 */         if (debug5) {
/* 111 */           queueAddSource(debug7);
/*     */         }
/*     */       } else {
/* 114 */         this.updatingSectionData.topSections.remove(debug3);
/*     */       } 
/*     */     } 
/* 117 */     if (debug5) {
/* 118 */       recheckInconsistencyFlag();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void enableLightSources(long debug1, boolean debug3) {
/* 124 */     runAllUpdates();
/* 125 */     if (debug3 && this.columnsWithSkySources.add(debug1)) {
/* 126 */       int debug4 = this.updatingSectionData.topSections.get(debug1);
/* 127 */       if (debug4 != this.updatingSectionData.currentLowestY) {
/* 128 */         long debug5 = SectionPos.asLong(SectionPos.x(debug1), debug4 - 1, SectionPos.z(debug1));
/* 129 */         queueAddSource(debug5);
/* 130 */         recheckInconsistencyFlag();
/*     */       } 
/* 132 */     } else if (!debug3) {
/* 133 */       this.columnsWithSkySources.remove(debug1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean hasInconsistencies() {
/* 139 */     return (super.hasInconsistencies() || this.hasSourceInconsistencies);
/*     */   }
/*     */ 
/*     */   
/*     */   protected DataLayer createDataLayer(long debug1) {
/* 144 */     DataLayer debug3 = (DataLayer)this.queuedSections.get(debug1);
/* 145 */     if (debug3 != null) {
/* 146 */       return debug3;
/*     */     }
/*     */     
/* 149 */     long debug4 = SectionPos.offset(debug1, Direction.UP);
/* 150 */     int debug6 = this.updatingSectionData.topSections.get(SectionPos.getZeroNode(debug1));
/*     */     
/* 152 */     if (debug6 == this.updatingSectionData.currentLowestY || SectionPos.y(debug4) >= debug6)
/*     */     {
/*     */       
/* 155 */       return new DataLayer();
/*     */     }
/*     */     
/*     */     DataLayer debug7;
/* 159 */     while ((debug7 = getDataLayer(debug4, true)) == null) {
/* 160 */       debug4 = SectionPos.offset(debug4, Direction.UP);
/*     */     }
/*     */     
/* 163 */     return new DataLayer((new FlatDataLayer(debug7, 0)).getData());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void markNewInconsistencies(LayerLightEngine<SkyDataLayerStorageMap, ?> debug1, boolean debug2, boolean debug3) {
/* 168 */     super.markNewInconsistencies(debug1, debug2, debug3);
/* 169 */     if (!debug2) {
/*     */       return;
/*     */     }
/* 172 */     if (!this.sectionsToAddSourcesTo.isEmpty()) {
/* 173 */       for (LongIterator<Long> longIterator = this.sectionsToAddSourcesTo.iterator(); longIterator.hasNext(); ) { long debug5 = ((Long)longIterator.next()).longValue();
/* 174 */         int debug7 = getLevel(debug5);
/* 175 */         if (debug7 == 2) {
/*     */           continue;
/*     */         }
/* 178 */         if (!this.sectionsToRemoveSourcesFrom.contains(debug5) && this.sectionsWithSources.add(debug5)) {
/* 179 */           if (debug7 == 1) {
/*     */             
/* 181 */             clearQueuedSectionBlocks(debug1, debug5);
/* 182 */             if (this.changedSections.add(debug5)) {
/* 183 */               this.updatingSectionData.copyDataLayer(debug5);
/*     */             }
/* 185 */             Arrays.fill(getDataLayer(debug5, true).getData(), (byte)-1);
/*     */             
/* 187 */             int i = SectionPos.sectionToBlockCoord(SectionPos.x(debug5));
/* 188 */             int debug9 = SectionPos.sectionToBlockCoord(SectionPos.y(debug5));
/* 189 */             int debug10 = SectionPos.sectionToBlockCoord(SectionPos.z(debug5));
/*     */             
/* 191 */             for (Direction debug14 : HORIZONTALS) {
/* 192 */               long debug15 = SectionPos.offset(debug5, debug14);
/* 193 */               if ((this.sectionsToRemoveSourcesFrom.contains(debug15) || (!this.sectionsWithSources.contains(debug15) && !this.sectionsToAddSourcesTo.contains(debug15))) && storingLightForSection(debug15))
/*     */               {
/*     */ 
/*     */                 
/* 197 */                 for (int debug17 = 0; debug17 < 16; debug17++) {
/* 198 */                   for (int debug18 = 0; debug18 < 16; debug18++) {
/*     */                     long debug19, debug21;
/*     */                     
/* 201 */                     switch (debug14) {
/*     */                       case NORTH:
/* 203 */                         debug19 = BlockPos.asLong(i + debug17, debug9 + debug18, debug10);
/* 204 */                         debug21 = BlockPos.asLong(i + debug17, debug9 + debug18, debug10 - 1);
/*     */                         break;
/*     */                       case SOUTH:
/* 207 */                         debug19 = BlockPos.asLong(i + debug17, debug9 + debug18, debug10 + 16 - 1);
/* 208 */                         debug21 = BlockPos.asLong(i + debug17, debug9 + debug18, debug10 + 16);
/*     */                         break;
/*     */                       case WEST:
/* 211 */                         debug19 = BlockPos.asLong(i, debug9 + debug17, debug10 + debug18);
/* 212 */                         debug21 = BlockPos.asLong(i - 1, debug9 + debug17, debug10 + debug18);
/*     */                         break;
/*     */                       
/*     */                       default:
/* 216 */                         debug19 = BlockPos.asLong(i + 16 - 1, debug9 + debug17, debug10 + debug18);
/* 217 */                         debug21 = BlockPos.asLong(i + 16, debug9 + debug17, debug10 + debug18);
/*     */                         break;
/*     */                     } 
/* 220 */                     debug1.checkEdge(debug19, debug21, debug1.computeLevelFromNeighbor(debug19, debug21, 0), true);
/*     */                   } 
/*     */                 } 
/*     */               }
/*     */             } 
/* 225 */             for (int debug11 = 0; debug11 < 16; debug11++) {
/* 226 */               for (int debug12 = 0; debug12 < 16; debug12++) {
/* 227 */                 long debug13 = BlockPos.asLong(
/* 228 */                     SectionPos.sectionToBlockCoord(SectionPos.x(debug5)) + debug11, 
/* 229 */                     SectionPos.sectionToBlockCoord(SectionPos.y(debug5)), 
/* 230 */                     SectionPos.sectionToBlockCoord(SectionPos.z(debug5)) + debug12);
/*     */                 
/* 232 */                 long debug15 = BlockPos.asLong(
/* 233 */                     SectionPos.sectionToBlockCoord(SectionPos.x(debug5)) + debug11, 
/* 234 */                     SectionPos.sectionToBlockCoord(SectionPos.y(debug5)) - 1, 
/* 235 */                     SectionPos.sectionToBlockCoord(SectionPos.z(debug5)) + debug12);
/*     */                 
/* 237 */                 debug1.checkEdge(debug13, debug15, debug1.computeLevelFromNeighbor(debug13, debug15, 0), true);
/*     */               } 
/*     */             } 
/*     */             continue;
/*     */           } 
/* 242 */           for (int debug8 = 0; debug8 < 16; debug8++) {
/* 243 */             for (int debug9 = 0; debug9 < 16; debug9++) {
/* 244 */               long debug10 = BlockPos.asLong(
/* 245 */                   SectionPos.sectionToBlockCoord(SectionPos.x(debug5)) + debug8, 
/* 246 */                   SectionPos.sectionToBlockCoord(SectionPos.y(debug5)) + 16 - 1, 
/* 247 */                   SectionPos.sectionToBlockCoord(SectionPos.z(debug5)) + debug9);
/*     */               
/* 249 */               debug1.checkEdge(Long.MAX_VALUE, debug10, 0, true);
/*     */             } 
/*     */           } 
/*     */         }  }
/*     */     
/*     */     }
/*     */     
/* 256 */     this.sectionsToAddSourcesTo.clear();
/* 257 */     if (!this.sectionsToRemoveSourcesFrom.isEmpty()) {
/* 258 */       for (LongIterator<Long> longIterator = this.sectionsToRemoveSourcesFrom.iterator(); longIterator.hasNext(); ) { long debug5 = ((Long)longIterator.next()).longValue();
/* 259 */         if (!this.sectionsWithSources.remove(debug5) || 
/* 260 */           !storingLightForSection(debug5)) {
/*     */           continue;
/*     */         }
/* 263 */         for (int debug7 = 0; debug7 < 16; debug7++) {
/* 264 */           for (int debug8 = 0; debug8 < 16; debug8++) {
/* 265 */             long debug9 = BlockPos.asLong(
/* 266 */                 SectionPos.sectionToBlockCoord(SectionPos.x(debug5)) + debug7, 
/* 267 */                 SectionPos.sectionToBlockCoord(SectionPos.y(debug5)) + 16 - 1, 
/* 268 */                 SectionPos.sectionToBlockCoord(SectionPos.z(debug5)) + debug8);
/*     */             
/* 270 */             debug1.checkEdge(Long.MAX_VALUE, debug9, 15, false);
/*     */           } 
/*     */         }  }
/*     */     
/*     */     }
/*     */     
/* 276 */     this.sectionsToRemoveSourcesFrom.clear();
/* 277 */     this.hasSourceInconsistencies = false;
/*     */   }
/*     */   
/*     */   protected boolean hasSectionsBelow(int debug1) {
/* 281 */     return (debug1 >= this.updatingSectionData.currentLowestY);
/*     */   }
/*     */   
/*     */   protected boolean hasLightSource(long debug1) {
/* 285 */     int debug3 = BlockPos.getY(debug1);
/*     */     
/* 287 */     if ((debug3 & 0xF) != 15) {
/* 288 */       return false;
/*     */     }
/* 290 */     long debug4 = SectionPos.blockToSection(debug1);
/* 291 */     long debug6 = SectionPos.getZeroNode(debug4);
/* 292 */     if (!this.columnsWithSkySources.contains(debug6)) {
/* 293 */       return false;
/*     */     }
/* 295 */     int debug8 = this.updatingSectionData.topSections.get(debug6);
/* 296 */     return (SectionPos.sectionToBlockCoord(debug8) == debug3 + 16);
/*     */   }
/*     */   
/*     */   protected boolean isAboveData(long debug1) {
/* 300 */     long debug3 = SectionPos.getZeroNode(debug1);
/* 301 */     int debug5 = this.updatingSectionData.topSections.get(debug3);
/* 302 */     return (debug5 == this.updatingSectionData.currentLowestY || SectionPos.y(debug1) >= debug5);
/*     */   }
/*     */   
/*     */   protected boolean lightOnInSection(long debug1) {
/* 306 */     long debug3 = SectionPos.getZeroNode(debug1);
/* 307 */     return this.columnsWithSkySources.contains(debug3);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class SkyDataLayerStorageMap
/*     */     extends DataLayerStorageMap<SkyDataLayerStorageMap>
/*     */   {
/*     */     private int currentLowestY;
/*     */     private final Long2IntOpenHashMap topSections;
/*     */     
/*     */     public SkyDataLayerStorageMap(Long2ObjectOpenHashMap<DataLayer> debug1, Long2IntOpenHashMap debug2, int debug3) {
/* 318 */       super(debug1);
/* 319 */       this.topSections = debug2;
/* 320 */       debug2.defaultReturnValue(debug3);
/* 321 */       this.currentLowestY = debug3;
/*     */     }
/*     */ 
/*     */     
/*     */     public SkyDataLayerStorageMap copy() {
/* 326 */       return new SkyDataLayerStorageMap(this.map.clone(), this.topSections.clone(), this.currentLowestY);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\lighting\SkyLightSectionStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */