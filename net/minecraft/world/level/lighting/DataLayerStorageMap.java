/*    */ package net.minecraft.world.level.lighting;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.world.level.chunk.DataLayer;
/*    */ 
/*    */ 
/*    */ public abstract class DataLayerStorageMap<M extends DataLayerStorageMap<M>>
/*    */ {
/* 10 */   private final long[] lastSectionKeys = new long[2];
/* 11 */   private final DataLayer[] lastSections = new DataLayer[2];
/*    */   private boolean cacheEnabled;
/*    */   protected final Long2ObjectOpenHashMap<DataLayer> map;
/*    */   
/*    */   protected DataLayerStorageMap(Long2ObjectOpenHashMap<DataLayer> debug1) {
/* 16 */     this.map = debug1;
/* 17 */     clearCache();
/* 18 */     this.cacheEnabled = true;
/*    */   }
/*    */   
/*    */   public abstract M copy();
/*    */   
/*    */   public void copyDataLayer(long debug1) {
/* 24 */     this.map.put(debug1, ((DataLayer)this.map.get(debug1)).copy());
/* 25 */     clearCache();
/*    */   }
/*    */   
/*    */   public boolean hasLayer(long debug1) {
/* 29 */     return this.map.containsKey(debug1);
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public DataLayer getLayer(long debug1) {
/* 34 */     if (this.cacheEnabled) {
/* 35 */       for (int i = 0; i < 2; i++) {
/* 36 */         if (debug1 == this.lastSectionKeys[i]) {
/* 37 */           return this.lastSections[i];
/*    */         }
/*    */       } 
/*    */     }
/* 41 */     DataLayer debug3 = (DataLayer)this.map.get(debug1);
/* 42 */     if (debug3 != null) {
/* 43 */       if (this.cacheEnabled) {
/* 44 */         for (int debug4 = 1; debug4 > 0; debug4--) {
/* 45 */           this.lastSectionKeys[debug4] = this.lastSectionKeys[debug4 - 1];
/* 46 */           this.lastSections[debug4] = this.lastSections[debug4 - 1];
/*    */         } 
/* 48 */         this.lastSectionKeys[0] = debug1;
/* 49 */         this.lastSections[0] = debug3;
/*    */       } 
/* 51 */       return debug3;
/*    */     } 
/* 53 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public DataLayer removeLayer(long debug1) {
/* 59 */     return (DataLayer)this.map.remove(debug1);
/*    */   }
/*    */   
/*    */   public void setLayer(long debug1, DataLayer debug3) {
/* 63 */     this.map.put(debug1, debug3);
/*    */   }
/*    */   
/*    */   public void clearCache() {
/* 67 */     for (int debug1 = 0; debug1 < 2; debug1++) {
/* 68 */       this.lastSectionKeys[debug1] = Long.MAX_VALUE;
/* 69 */       this.lastSections[debug1] = null;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void disableCache() {
/* 74 */     this.cacheEnabled = false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\lighting\DataLayerStorageMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */