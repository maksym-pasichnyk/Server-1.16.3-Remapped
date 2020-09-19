/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import java.util.Spliterators;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.stream.StreamSupport;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ 
/*     */ 
/*     */ public class ChunkPos
/*     */ {
/*  13 */   public static final long INVALID_CHUNK_POS = asLong(1875016, 1875016);
/*     */ 
/*     */   
/*     */   public final int x;
/*     */ 
/*     */   
/*     */   public final int z;
/*     */ 
/*     */   
/*     */   public ChunkPos(int debug1, int debug2) {
/*  23 */     this.x = debug1;
/*  24 */     this.z = debug2;
/*     */   }
/*     */   
/*     */   public ChunkPos(BlockPos debug1) {
/*  28 */     this.x = debug1.getX() >> 4;
/*  29 */     this.z = debug1.getZ() >> 4;
/*     */   }
/*     */   
/*     */   public ChunkPos(long debug1) {
/*  33 */     this.x = (int)debug1;
/*  34 */     this.z = (int)(debug1 >> 32L);
/*     */   }
/*     */   
/*     */   public long toLong() {
/*  38 */     return asLong(this.x, this.z);
/*     */   }
/*     */   
/*     */   public static long asLong(int debug0, int debug1) {
/*  42 */     return debug0 & 0xFFFFFFFFL | (debug1 & 0xFFFFFFFFL) << 32L;
/*     */   }
/*     */   
/*     */   public static int getX(long debug0) {
/*  46 */     return (int)(debug0 & 0xFFFFFFFFL);
/*     */   }
/*     */   
/*     */   public static int getZ(long debug0) {
/*  50 */     return (int)(debug0 >>> 32L & 0xFFFFFFFFL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  59 */     int debug1 = 1664525 * this.x + 1013904223;
/*  60 */     int debug2 = 1664525 * (this.z ^ 0xDEADBEEF) + 1013904223;
/*  61 */     return debug1 ^ debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object debug1) {
/*  66 */     if (this == debug1) {
/*  67 */       return true;
/*     */     }
/*     */     
/*  70 */     if (debug1 instanceof ChunkPos) {
/*  71 */       ChunkPos debug2 = (ChunkPos)debug1;
/*     */       
/*  73 */       return (this.x == debug2.x && this.z == debug2.z);
/*     */     } 
/*     */     
/*  76 */     return false;
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
/*     */   public int getMinBlockX() {
/*  88 */     return this.x << 4;
/*     */   }
/*     */   
/*     */   public int getMinBlockZ() {
/*  92 */     return this.z << 4;
/*     */   }
/*     */   
/*     */   public int getMaxBlockX() {
/*  96 */     return (this.x << 4) + 15;
/*     */   }
/*     */   
/*     */   public int getMaxBlockZ() {
/* 100 */     return (this.z << 4) + 15;
/*     */   }
/*     */   
/*     */   public int getRegionX() {
/* 104 */     return this.x >> 5;
/*     */   }
/*     */   
/*     */   public int getRegionZ() {
/* 108 */     return this.z >> 5;
/*     */   }
/*     */   
/*     */   public int getRegionLocalX() {
/* 112 */     return this.x & 0x1F;
/*     */   }
/*     */   
/*     */   public int getRegionLocalZ() {
/* 116 */     return this.z & 0x1F;
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
/*     */   public String toString() {
/* 129 */     return "[" + this.x + ", " + this.z + "]";
/*     */   }
/*     */   
/*     */   public BlockPos getWorldPosition() {
/* 133 */     return new BlockPos(getMinBlockX(), 0, getMinBlockZ());
/*     */   }
/*     */   
/*     */   public int getChessboardDistance(ChunkPos debug1) {
/* 137 */     return Math.max(Math.abs(this.x - debug1.x), Math.abs(this.z - debug1.z));
/*     */   }
/*     */   
/*     */   public static Stream<ChunkPos> rangeClosed(ChunkPos debug0, int debug1) {
/* 141 */     return rangeClosed(new ChunkPos(debug0.x - debug1, debug0.z - debug1), new ChunkPos(debug0.x + debug1, debug0.z + debug1));
/*     */   }
/*     */   
/*     */   public static Stream<ChunkPos> rangeClosed(final ChunkPos from, final ChunkPos to) {
/* 145 */     int debug2 = Math.abs(from.x - to.x) + 1;
/* 146 */     int debug3 = Math.abs(from.z - to.z) + 1;
/* 147 */     final int xDiff = (from.x < to.x) ? 1 : -1;
/* 148 */     final int zDiff = (from.z < to.z) ? 1 : -1;
/* 149 */     return StreamSupport.stream(new Spliterators.AbstractSpliterator<ChunkPos>((debug2 * debug3), 64) {
/*     */           @Nullable
/*     */           private ChunkPos pos;
/*     */           
/*     */           public boolean tryAdvance(Consumer<? super ChunkPos> debug1) {
/* 154 */             if (this.pos == null) {
/* 155 */               this.pos = from;
/*     */             } else {
/* 157 */               int debug2 = this.pos.x;
/* 158 */               int debug3 = this.pos.z;
/* 159 */               if (debug2 == to.x) {
/* 160 */                 if (debug3 == to.z) {
/* 161 */                   return false;
/*     */                 }
/* 163 */                 this.pos = new ChunkPos(from.x, debug3 + zDiff);
/*     */               } else {
/* 165 */                 this.pos = new ChunkPos(debug2 + xDiff, debug3);
/*     */               } 
/*     */             } 
/* 168 */             debug1.accept(this.pos);
/* 169 */             return true;
/*     */           }
/*     */         }false);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\ChunkPos.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */