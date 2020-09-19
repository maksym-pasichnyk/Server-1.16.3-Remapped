/*     */ package net.minecraft.world.level.lighting;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.longs.Long2ByteMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.LongArrayList;
/*     */ import it.unimi.dsi.fastutil.longs.LongLinkedOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongList;
/*     */ import java.util.function.LongPredicate;
/*     */ import net.minecraft.util.Mth;
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
/*     */ 
/*     */ public abstract class DynamicGraphMinFixedPoint
/*     */ {
/*     */   private final int levelCount;
/*     */   private final LongLinkedOpenHashSet[] queues;
/*     */   private final Long2ByteMap computedLevels;
/*     */   private int firstQueuedLevel;
/*     */   private volatile boolean hasWork;
/*     */   
/*     */   protected DynamicGraphMinFixedPoint(int debug1, final int minQueueSize, final int minMapSize) {
/*  42 */     if (debug1 >= 254) {
/*  43 */       throw new IllegalArgumentException("Level count must be < 254.");
/*     */     }
/*  45 */     this.levelCount = debug1;
/*  46 */     this.queues = new LongLinkedOpenHashSet[debug1];
/*  47 */     for (int debug4 = 0; debug4 < debug1; debug4++) {
/*  48 */       this.queues[debug4] = new LongLinkedOpenHashSet(minQueueSize, 0.5F)
/*     */         {
/*     */           protected void rehash(int debug1) {
/*  51 */             if (debug1 > minQueueSize) {
/*  52 */               super.rehash(debug1);
/*     */             }
/*     */           }
/*     */         };
/*     */     } 
/*  57 */     this.computedLevels = (Long2ByteMap)new Long2ByteOpenHashMap(minMapSize, 0.5F)
/*     */       {
/*     */         protected void rehash(int debug1) {
/*  60 */           if (debug1 > minMapSize) {
/*  61 */             super.rehash(debug1);
/*     */           }
/*     */         }
/*     */       };
/*  65 */     this.computedLevels.defaultReturnValue((byte)-1);
/*  66 */     this.firstQueuedLevel = debug1;
/*     */   }
/*     */   
/*     */   private int getKey(int debug1, int debug2) {
/*  70 */     int debug3 = debug1;
/*  71 */     if (debug3 > debug2) {
/*  72 */       debug3 = debug2;
/*     */     }
/*  74 */     if (debug3 > this.levelCount - 1) {
/*  75 */       debug3 = this.levelCount - 1;
/*     */     }
/*  77 */     return debug3;
/*     */   }
/*     */   
/*     */   private void checkFirstQueuedLevel(int debug1) {
/*  81 */     int debug2 = this.firstQueuedLevel;
/*  82 */     this.firstQueuedLevel = debug1;
/*  83 */     for (int debug3 = debug2 + 1; debug3 < debug1; debug3++) {
/*  84 */       if (!this.queues[debug3].isEmpty()) {
/*  85 */         this.firstQueuedLevel = debug3;
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void removeFromQueue(long debug1) {
/*  92 */     int debug3 = this.computedLevels.get(debug1) & 0xFF;
/*  93 */     if (debug3 == 255) {
/*     */       return;
/*     */     }
/*  96 */     int debug4 = getLevel(debug1);
/*  97 */     int debug5 = getKey(debug4, debug3);
/*  98 */     dequeue(debug1, debug5, this.levelCount, true);
/*  99 */     this.hasWork = (this.firstQueuedLevel < this.levelCount);
/*     */   }
/*     */   
/*     */   public void removeIf(LongPredicate debug1) {
/* 103 */     LongArrayList longArrayList = new LongArrayList();
/*     */     
/* 105 */     this.computedLevels.keySet().forEach(debug2 -> {
/*     */           if (debug0.test(debug2)) {
/*     */             debug1.add(debug2);
/*     */           }
/*     */         });
/*     */     
/* 111 */     longArrayList.forEach(this::removeFromQueue);
/*     */   }
/*     */   
/*     */   private void dequeue(long debug1, int debug3, int debug4, boolean debug5) {
/* 115 */     if (debug5) {
/* 116 */       this.computedLevels.remove(debug1);
/*     */     }
/* 118 */     this.queues[debug3].remove(debug1);
/* 119 */     if (this.queues[debug3].isEmpty() && this.firstQueuedLevel == debug3) {
/* 120 */       checkFirstQueuedLevel(debug4);
/*     */     }
/*     */   }
/*     */   
/*     */   private void enqueue(long debug1, int debug3, int debug4) {
/* 125 */     this.computedLevels.put(debug1, (byte)debug3);
/* 126 */     this.queues[debug4].add(debug1);
/* 127 */     if (this.firstQueuedLevel > debug4) {
/* 128 */       this.firstQueuedLevel = debug4;
/*     */     }
/*     */   }
/*     */   
/*     */   protected void checkNode(long debug1) {
/* 133 */     checkEdge(debug1, debug1, this.levelCount - 1, false);
/*     */   }
/*     */   
/*     */   protected void checkEdge(long debug1, long debug3, int debug5, boolean debug6) {
/* 137 */     checkEdge(debug1, debug3, debug5, getLevel(debug3), this.computedLevels.get(debug3) & 0xFF, debug6);
/* 138 */     this.hasWork = (this.firstQueuedLevel < this.levelCount);
/*     */   } private void checkEdge(long debug1, long debug3, int debug5, int debug6, int debug7, boolean debug8) {
/*     */     boolean debug9;
/*     */     int debug10;
/* 142 */     if (isSource(debug3)) {
/*     */       return;
/*     */     }
/* 145 */     debug5 = Mth.clamp(debug5, 0, this.levelCount - 1);
/* 146 */     debug6 = Mth.clamp(debug6, 0, this.levelCount - 1);
/*     */     
/* 148 */     if (debug7 == 255) {
/* 149 */       debug9 = true;
/* 150 */       debug7 = debug6;
/*     */     } else {
/* 152 */       debug9 = false;
/*     */     } 
/*     */     
/* 155 */     if (debug8) {
/*     */       
/* 157 */       debug10 = Math.min(debug7, debug5);
/*     */     } else {
/* 159 */       debug10 = Mth.clamp(getComputedLevel(debug3, debug1, debug5), 0, this.levelCount - 1);
/*     */     } 
/* 161 */     int debug11 = getKey(debug6, debug7);
/* 162 */     if (debug6 != debug10) {
/* 163 */       int debug12 = getKey(debug6, debug10);
/* 164 */       if (debug11 != debug12 && !debug9) {
/* 165 */         dequeue(debug3, debug11, debug12, false);
/*     */       }
/* 167 */       enqueue(debug3, debug10, debug12);
/* 168 */     } else if (!debug9) {
/* 169 */       dequeue(debug3, debug11, this.levelCount, true);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected final void checkNeighbor(long debug1, long debug3, int debug5, boolean debug6) {
/* 174 */     int debug7 = this.computedLevels.get(debug3) & 0xFF;
/* 175 */     int debug8 = Mth.clamp(computeLevelFromNeighbor(debug1, debug3, debug5), 0, this.levelCount - 1);
/* 176 */     if (debug6) {
/* 177 */       checkEdge(debug1, debug3, debug8, getLevel(debug3), debug7, true);
/*     */     } else {
/*     */       int debug9;
/*     */       boolean debug10;
/* 181 */       if (debug7 == 255) {
/* 182 */         debug10 = true;
/* 183 */         debug9 = Mth.clamp(getLevel(debug3), 0, this.levelCount - 1);
/*     */       } else {
/* 185 */         debug9 = debug7;
/* 186 */         debug10 = false;
/*     */       } 
/*     */       
/* 189 */       if (debug8 == debug9)
/*     */       {
/* 191 */         checkEdge(debug1, debug3, this.levelCount - 1, debug10 ? debug9 : getLevel(debug3), debug7, false);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected final boolean hasWork() {
/* 197 */     return this.hasWork;
/*     */   }
/*     */   
/*     */   protected final int runUpdates(int debug1) {
/* 201 */     if (this.firstQueuedLevel >= this.levelCount) {
/* 202 */       return debug1;
/*     */     }
/* 204 */     while (this.firstQueuedLevel < this.levelCount && debug1 > 0) {
/* 205 */       debug1--;
/* 206 */       LongLinkedOpenHashSet debug2 = this.queues[this.firstQueuedLevel];
/* 207 */       long debug3 = debug2.removeFirstLong();
/* 208 */       int debug5 = Mth.clamp(getLevel(debug3), 0, this.levelCount - 1);
/* 209 */       if (debug2.isEmpty()) {
/* 210 */         checkFirstQueuedLevel(this.levelCount);
/*     */       }
/* 212 */       int debug6 = this.computedLevels.remove(debug3) & 0xFF;
/* 213 */       if (debug6 < debug5) {
/*     */         
/* 215 */         setLevel(debug3, debug6);
/* 216 */         checkNeighborsAfterUpdate(debug3, debug6, true); continue;
/* 217 */       }  if (debug6 > debug5) {
/*     */         
/* 219 */         enqueue(debug3, debug6, getKey(this.levelCount - 1, debug6));
/* 220 */         setLevel(debug3, this.levelCount - 1);
/* 221 */         checkNeighborsAfterUpdate(debug3, debug5, false);
/*     */       } 
/*     */     } 
/* 224 */     this.hasWork = (this.firstQueuedLevel < this.levelCount);
/* 225 */     return debug1;
/*     */   }
/*     */   
/*     */   public int getQueueSize() {
/* 229 */     return this.computedLevels.size();
/*     */   }
/*     */   
/*     */   protected abstract boolean isSource(long paramLong);
/*     */   
/*     */   protected abstract int getComputedLevel(long paramLong1, long paramLong2, int paramInt);
/*     */   
/*     */   protected abstract void checkNeighborsAfterUpdate(long paramLong, int paramInt, boolean paramBoolean);
/*     */   
/*     */   protected abstract int getLevel(long paramLong);
/*     */   
/*     */   protected abstract void setLevel(long paramLong, int paramInt);
/*     */   
/*     */   protected abstract int computeLevelFromNeighbor(long paramLong1, long paramLong2, int paramInt);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\lighting\DynamicGraphMinFixedPoint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */