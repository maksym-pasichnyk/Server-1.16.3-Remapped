/*    */ package net.minecraft.world.level.newbiome.context;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.longs.Long2IntLinkedOpenHashMap;
/*    */ import java.util.Random;
/*    */ import net.minecraft.util.LinearCongruentialGenerator;
/*    */ import net.minecraft.world.level.levelgen.synth.ImprovedNoise;
/*    */ import net.minecraft.world.level.newbiome.area.Area;
/*    */ import net.minecraft.world.level.newbiome.area.LazyArea;
/*    */ import net.minecraft.world.level.newbiome.layer.traits.PixelTransformer;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LazyAreaContext
/*    */   implements BigContext<LazyArea>
/*    */ {
/*    */   private final Long2IntLinkedOpenHashMap cache;
/*    */   private final int maxCache;
/*    */   private final ImprovedNoise biomeNoise;
/*    */   private final long seed;
/*    */   private long rval;
/*    */   
/*    */   public LazyAreaContext(int debug1, long debug2, long debug4) {
/* 23 */     this.seed = mixSeed(debug2, debug4);
/* 24 */     this.biomeNoise = new ImprovedNoise(new Random(debug2));
/*    */     
/* 26 */     this.cache = new Long2IntLinkedOpenHashMap(16, 0.25F);
/* 27 */     this.cache.defaultReturnValue(-2147483648);
/* 28 */     this.maxCache = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public LazyArea createResult(PixelTransformer debug1) {
/* 33 */     return new LazyArea(this.cache, this.maxCache, debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public LazyArea createResult(PixelTransformer debug1, LazyArea debug2) {
/* 38 */     return new LazyArea(this.cache, Math.min(1024, debug2.getMaxCache() * 4), debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public LazyArea createResult(PixelTransformer debug1, LazyArea debug2, LazyArea debug3) {
/* 43 */     return new LazyArea(this.cache, Math.min(1024, Math.max(debug2.getMaxCache(), debug3.getMaxCache()) * 4), debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void initRandom(long debug1, long debug3) {
/* 48 */     long debug5 = this.seed;
/* 49 */     debug5 = LinearCongruentialGenerator.next(debug5, debug1);
/* 50 */     debug5 = LinearCongruentialGenerator.next(debug5, debug3);
/* 51 */     debug5 = LinearCongruentialGenerator.next(debug5, debug1);
/* 52 */     debug5 = LinearCongruentialGenerator.next(debug5, debug3);
/* 53 */     this.rval = debug5;
/*    */   }
/*    */ 
/*    */   
/*    */   public int nextRandom(int debug1) {
/* 58 */     int debug2 = (int)Math.floorMod(this.rval >> 24L, debug1);
/* 59 */     this.rval = LinearCongruentialGenerator.next(this.rval, this.seed);
/* 60 */     return debug2;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ImprovedNoise getBiomeNoise() {
/* 70 */     return this.biomeNoise;
/*    */   }
/*    */   
/*    */   private static long mixSeed(long debug0, long debug2) {
/* 74 */     long debug4 = debug2;
/* 75 */     debug4 = LinearCongruentialGenerator.next(debug4, debug2);
/* 76 */     debug4 = LinearCongruentialGenerator.next(debug4, debug2);
/* 77 */     debug4 = LinearCongruentialGenerator.next(debug4, debug2);
/*    */     
/* 79 */     long debug6 = debug0;
/* 80 */     debug6 = LinearCongruentialGenerator.next(debug6, debug4);
/* 81 */     debug6 = LinearCongruentialGenerator.next(debug6, debug4);
/* 82 */     debug6 = LinearCongruentialGenerator.next(debug6, debug4);
/* 83 */     return debug6;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\newbiome\context\LazyAreaContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */