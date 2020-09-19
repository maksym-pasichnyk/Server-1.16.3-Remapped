/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import java.util.Random;
/*    */ 
/*    */ public class WorldgenRandom
/*    */   extends Random {
/*    */   private int count;
/*    */   
/*    */   public WorldgenRandom() {}
/*    */   
/*    */   public WorldgenRandom(long debug1) {
/* 12 */     super(debug1);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void consumeCount(int debug1) {
/* 20 */     for (int debug2 = 0; debug2 < debug1; debug2++) {
/* 21 */       next(1);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected int next(int debug1) {
/* 27 */     this.count++;
/* 28 */     return super.next(debug1);
/*    */   }
/*    */   
/*    */   public long setBaseChunkSeed(int debug1, int debug2) {
/* 32 */     long debug3 = debug1 * 341873128712L + debug2 * 132897987541L;
/* 33 */     setSeed(debug3);
/* 34 */     return debug3;
/*    */   }
/*    */   
/*    */   public long setDecorationSeed(long debug1, int debug3, int debug4) {
/* 38 */     setSeed(debug1);
/*    */     
/* 40 */     long debug5 = nextLong() | 0x1L;
/* 41 */     long debug7 = nextLong() | 0x1L;
/* 42 */     long debug9 = debug3 * debug5 + debug4 * debug7 ^ debug1;
/* 43 */     setSeed(debug9);
/* 44 */     return debug9;
/*    */   }
/*    */   
/*    */   public long setFeatureSeed(long debug1, int debug3, int debug4) {
/* 48 */     long debug5 = debug1 + debug3 + (10000 * debug4);
/* 49 */     setSeed(debug5);
/* 50 */     return debug5;
/*    */   }
/*    */   
/*    */   public long setLargeFeatureSeed(long debug1, int debug3, int debug4) {
/* 54 */     setSeed(debug1);
/* 55 */     long debug5 = nextLong();
/* 56 */     long debug7 = nextLong();
/* 57 */     long debug9 = debug3 * debug5 ^ debug4 * debug7 ^ debug1;
/* 58 */     setSeed(debug9);
/* 59 */     return debug9;
/*    */   }
/*    */   
/*    */   public long setLargeFeatureWithSalt(long debug1, int debug3, int debug4, int debug5) {
/* 63 */     long debug6 = debug3 * 341873128712L + debug4 * 132897987541L + debug1 + debug5;
/* 64 */     setSeed(debug6);
/* 65 */     return debug6;
/*    */   }
/*    */   
/*    */   public static Random seedSlimeChunk(int debug0, int debug1, long debug2, long debug4) {
/* 69 */     return new Random(debug2 + (debug0 * debug0 * 4987142) + (debug0 * 5947611) + (debug1 * debug1) * 4392871L + (debug1 * 389711) ^ debug4);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\WorldgenRandom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */