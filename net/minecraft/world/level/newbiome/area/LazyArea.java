/*    */ package net.minecraft.world.level.newbiome.area;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.longs.Long2IntLinkedOpenHashMap;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.newbiome.layer.traits.PixelTransformer;
/*    */ 
/*    */ public final class LazyArea implements Area {
/*    */   private final PixelTransformer transformer;
/*    */   private final Long2IntLinkedOpenHashMap cache;
/*    */   private final int maxCache;
/*    */   
/*    */   public LazyArea(Long2IntLinkedOpenHashMap debug1, int debug2, PixelTransformer debug3) {
/* 13 */     this.cache = debug1;
/* 14 */     this.maxCache = debug2;
/* 15 */     this.transformer = debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   public int get(int debug1, int debug2) {
/* 20 */     long debug3 = ChunkPos.asLong(debug1, debug2);
/* 21 */     synchronized (this.cache) {
/* 22 */       int debug6 = this.cache.get(debug3);
/* 23 */       if (debug6 != Integer.MIN_VALUE) {
/* 24 */         return debug6;
/*    */       }
/* 26 */       int debug7 = this.transformer.apply(debug1, debug2);
/* 27 */       this.cache.put(debug3, debug7);
/* 28 */       if (this.cache.size() > this.maxCache) {
/* 29 */         for (int debug8 = 0; debug8 < this.maxCache / 16; debug8++) {
/* 30 */           this.cache.removeFirstInt();
/*    */         }
/*    */       }
/* 33 */       return debug7;
/*    */     } 
/*    */   }
/*    */   
/*    */   public int getMaxCache() {
/* 38 */     return this.maxCache;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\newbiome\area\LazyArea.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */