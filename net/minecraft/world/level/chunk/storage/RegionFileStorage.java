/*    */ package net.minecraft.world.level.chunk.storage;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*    */ import java.io.DataInputStream;
/*    */ import java.io.DataOutputStream;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.NbtIo;
/*    */ import net.minecraft.util.ExceptionCollector;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ 
/*    */ public final class RegionFileStorage
/*    */   implements AutoCloseable
/*    */ {
/* 18 */   private final Long2ObjectLinkedOpenHashMap<RegionFile> regionCache = new Long2ObjectLinkedOpenHashMap();
/*    */   private final File folder;
/*    */   private final boolean sync;
/*    */   
/*    */   RegionFileStorage(File debug1, boolean debug2) {
/* 23 */     this.folder = debug1;
/* 24 */     this.sync = debug2;
/*    */   }
/*    */   
/*    */   private RegionFile getRegionFile(ChunkPos debug1) throws IOException {
/* 28 */     long debug2 = ChunkPos.asLong(debug1.getRegionX(), debug1.getRegionZ());
/* 29 */     RegionFile debug4 = (RegionFile)this.regionCache.getAndMoveToFirst(debug2);
/* 30 */     if (debug4 != null) {
/* 31 */       return debug4;
/*    */     }
/*    */     
/* 34 */     if (this.regionCache.size() >= 256) {
/* 35 */       ((RegionFile)this.regionCache.removeLast()).close();
/*    */     }
/*    */     
/* 38 */     if (!this.folder.exists()) {
/* 39 */       this.folder.mkdirs();
/*    */     }
/*    */     
/* 42 */     File debug5 = new File(this.folder, "r." + debug1.getRegionX() + "." + debug1.getRegionZ() + ".mca");
/* 43 */     RegionFile debug6 = new RegionFile(debug5, this.folder, this.sync);
/* 44 */     this.regionCache.putAndMoveToFirst(debug2, debug6);
/* 45 */     return debug6;
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public CompoundTag read(ChunkPos debug1) throws IOException {
/* 50 */     RegionFile debug2 = getRegionFile(debug1);
/* 51 */     try (DataInputStream debug3 = debug2.getChunkDataInputStream(debug1)) {
/* 52 */       if (debug3 == null) {
/* 53 */         return null;
/*    */       }
/*    */       
/* 56 */       return NbtIo.read(debug3);
/*    */     } 
/*    */   }
/*    */   
/*    */   protected void write(ChunkPos debug1, CompoundTag debug2) throws IOException {
/* 61 */     RegionFile debug3 = getRegionFile(debug1);
/* 62 */     try (DataOutputStream debug4 = debug3.getChunkDataOutputStream(debug1)) {
/* 63 */       NbtIo.write(debug2, debug4);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 69 */     ExceptionCollector<IOException> debug1 = new ExceptionCollector();
/* 70 */     for (ObjectIterator<RegionFile> objectIterator = this.regionCache.values().iterator(); objectIterator.hasNext(); ) { RegionFile debug3 = objectIterator.next();
/*    */       try {
/* 72 */         debug3.close();
/* 73 */       } catch (IOException debug4) {
/* 74 */         debug1.add(debug4);
/*    */       }  }
/*    */     
/* 77 */     debug1.throwIfPresent();
/*    */   }
/*    */   
/*    */   public void flush() throws IOException {
/* 81 */     for (ObjectIterator<RegionFile> objectIterator = this.regionCache.values().iterator(); objectIterator.hasNext(); ) { RegionFile debug2 = objectIterator.next();
/* 82 */       debug2.flush(); }
/*    */   
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\chunk\storage\RegionFileStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */