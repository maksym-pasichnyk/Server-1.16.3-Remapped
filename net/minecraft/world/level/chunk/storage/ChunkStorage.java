/*    */ package net.minecraft.world.level.chunk.storage;
/*    */ 
/*    */ import com.mojang.datafixers.DataFixer;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.util.function.Supplier;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.SharedConstants;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.NbtUtils;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.datafix.DataFixTypes;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.levelgen.structure.LegacyStructureDataHandler;
/*    */ import net.minecraft.world.level.storage.DimensionDataStorage;
/*    */ 
/*    */ 
/*    */ public class ChunkStorage
/*    */   implements AutoCloseable
/*    */ {
/*    */   private final IOWorker worker;
/*    */   protected final DataFixer fixerUpper;
/*    */   @Nullable
/*    */   private LegacyStructureDataHandler legacyStructureHandler;
/*    */   
/*    */   public ChunkStorage(File debug1, DataFixer debug2, boolean debug3) {
/* 28 */     this.fixerUpper = debug2;
/* 29 */     this.worker = new IOWorker(debug1, debug3, "chunk");
/*    */   }
/*    */   
/*    */   public CompoundTag upgradeChunkTag(ResourceKey<Level> debug1, Supplier<DimensionDataStorage> debug2, CompoundTag debug3) {
/* 33 */     int debug4 = getVersion(debug3);
/*    */     
/* 35 */     int debug5 = 1493;
/* 36 */     if (debug4 < 1493) {
/* 37 */       debug3 = NbtUtils.update(this.fixerUpper, DataFixTypes.CHUNK, debug3, debug4, 1493);
/*    */       
/* 39 */       if (debug3.getCompound("Level").getBoolean("hasLegacyStructureData")) {
/* 40 */         if (this.legacyStructureHandler == null) {
/* 41 */           this.legacyStructureHandler = LegacyStructureDataHandler.getLegacyStructureHandler(debug1, debug2.get());
/*    */         }
/* 43 */         debug3 = this.legacyStructureHandler.updateFromLegacy(debug3);
/*    */       } 
/*    */     } 
/*    */     
/* 47 */     debug3 = NbtUtils.update(this.fixerUpper, DataFixTypes.CHUNK, debug3, Math.max(1493, debug4));
/* 48 */     if (debug4 < SharedConstants.getCurrentVersion().getWorldVersion()) {
/* 49 */       debug3.putInt("DataVersion", SharedConstants.getCurrentVersion().getWorldVersion());
/*    */     }
/*    */     
/* 52 */     return debug3;
/*    */   }
/*    */   
/*    */   public static int getVersion(CompoundTag debug0) {
/* 56 */     return debug0.contains("DataVersion", 99) ? debug0.getInt("DataVersion") : -1;
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public CompoundTag read(ChunkPos debug1) throws IOException {
/* 61 */     return this.worker.load(debug1);
/*    */   }
/*    */   
/*    */   public void write(ChunkPos debug1, CompoundTag debug2) {
/* 65 */     this.worker.store(debug1, debug2);
/*    */     
/* 67 */     if (this.legacyStructureHandler != null) {
/* 68 */       this.legacyStructureHandler.removeIndex(debug1.toLong());
/*    */     }
/*    */   }
/*    */   
/*    */   public void flushWorker() {
/* 73 */     this.worker.synchronize().join();
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 78 */     this.worker.close();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\chunk\storage\ChunkStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */