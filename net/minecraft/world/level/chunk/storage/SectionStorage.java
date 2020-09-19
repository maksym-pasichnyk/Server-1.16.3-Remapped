/*     */ package net.minecraft.world.level.chunk.storage;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.OptionalDynamic;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.LongLinkedOpenHashSet;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.function.Function;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class SectionStorage<R>
/*     */   implements AutoCloseable
/*     */ {
/*  36 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */ 
/*     */ 
/*     */   
/*     */   private final IOWorker worker;
/*     */ 
/*     */   
/*  43 */   private final Long2ObjectMap<Optional<R>> storage = (Long2ObjectMap<Optional<R>>)new Long2ObjectOpenHashMap();
/*  44 */   private final LongLinkedOpenHashSet dirty = new LongLinkedOpenHashSet();
/*     */   private final Function<Runnable, Codec<R>> codec;
/*     */   private final Function<Runnable, R> factory;
/*     */   private final DataFixer fixerUpper;
/*     */   private final DataFixTypes type;
/*     */   
/*     */   public SectionStorage(File debug1, Function<Runnable, Codec<R>> debug2, Function<Runnable, R> debug3, DataFixer debug4, DataFixTypes debug5, boolean debug6) {
/*  51 */     this.codec = debug2;
/*  52 */     this.factory = debug3;
/*  53 */     this.fixerUpper = debug4;
/*  54 */     this.type = debug5;
/*  55 */     this.worker = new IOWorker(debug1, debug6, debug1.getName());
/*     */   }
/*     */   
/*     */   protected void tick(BooleanSupplier debug1) {
/*  59 */     while (!this.dirty.isEmpty() && debug1.getAsBoolean()) {
/*  60 */       ChunkPos debug2 = SectionPos.of(this.dirty.firstLong()).chunk();
/*  61 */       writeColumn(debug2);
/*     */     } 
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected Optional<R> get(long debug1) {
/*  67 */     return (Optional<R>)this.storage.get(debug1);
/*     */   }
/*     */   
/*     */   protected Optional<R> getOrLoad(long debug1) {
/*  71 */     SectionPos debug3 = SectionPos.of(debug1);
/*  72 */     if (outsideStoredRange(debug3)) {
/*  73 */       return Optional.empty();
/*     */     }
/*  75 */     Optional<R> debug4 = get(debug1);
/*  76 */     if (debug4 != null) {
/*  77 */       return debug4;
/*     */     }
/*  79 */     readColumn(debug3.chunk());
/*     */     
/*  81 */     debug4 = get(debug1);
/*  82 */     if (debug4 == null) {
/*  83 */       throw (IllegalStateException)Util.pauseInIde(new IllegalStateException());
/*     */     }
/*  85 */     return debug4;
/*     */   }
/*     */   
/*     */   protected boolean outsideStoredRange(SectionPos debug1) {
/*  89 */     return Level.isOutsideBuildHeight(SectionPos.sectionToBlockCoord(debug1.y()));
/*     */   }
/*     */   
/*     */   protected R getOrCreate(long debug1) {
/*  93 */     Optional<R> debug3 = getOrLoad(debug1);
/*  94 */     if (debug3.isPresent()) {
/*  95 */       return debug3.get();
/*     */     }
/*  97 */     R debug4 = this.factory.apply(() -> setDirty(debug1));
/*  98 */     this.storage.put(debug1, Optional.of(debug4));
/*  99 */     return debug4;
/*     */   }
/*     */   
/*     */   private void readColumn(ChunkPos debug1) {
/* 103 */     readColumn(debug1, (DynamicOps<CompoundTag>)NbtOps.INSTANCE, tryRead(debug1));
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private CompoundTag tryRead(ChunkPos debug1) {
/*     */     try {
/* 109 */       return this.worker.load(debug1);
/* 110 */     } catch (IOException debug2) {
/* 111 */       LOGGER.error("Error reading chunk {} data from disk", debug1, debug2);
/* 112 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private <T> void readColumn(ChunkPos debug1, DynamicOps<T> debug2, @Nullable T debug3) {
/* 117 */     if (debug3 == null) {
/* 118 */       for (int debug4 = 0; debug4 < 16; debug4++) {
/* 119 */         this.storage.put(SectionPos.of(debug1, debug4).asLong(), Optional.empty());
/*     */       }
/*     */     } else {
/* 122 */       Dynamic<T> debug4 = new Dynamic(debug2, debug3);
/* 123 */       int debug5 = getVersion(debug4);
/* 124 */       int debug6 = SharedConstants.getCurrentVersion().getWorldVersion();
/* 125 */       boolean debug7 = (debug5 != debug6);
/* 126 */       Dynamic<T> debug8 = this.fixerUpper.update(this.type.getType(), debug4, debug5, debug6);
/* 127 */       OptionalDynamic<T> debug9 = debug8.get("Sections");
/* 128 */       for (int debug10 = 0; debug10 < 16; debug10++) {
/* 129 */         long debug11 = SectionPos.of(debug1, debug10).asLong();
/* 130 */         Optional<R> debug13 = debug9.get(Integer.toString(debug10)).result().flatMap(debug3 -> ((Codec)this.codec.apply(())).parse(debug3).resultOrPartial(LOGGER::error));
/*     */ 
/*     */         
/* 133 */         this.storage.put(debug11, debug13);
/* 134 */         debug13.ifPresent(debug4 -> {
/*     */               onSectionLoad(debug1);
/*     */               if (debug3) {
/*     */                 setDirty(debug1);
/*     */               }
/*     */             });
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void writeColumn(ChunkPos debug1) {
/* 145 */     Dynamic<Tag> debug2 = writeColumn(debug1, (DynamicOps<Tag>)NbtOps.INSTANCE);
/* 146 */     Tag debug3 = (Tag)debug2.getValue();
/* 147 */     if (debug3 instanceof CompoundTag) {
/* 148 */       this.worker.store(debug1, (CompoundTag)debug3);
/*     */     } else {
/* 150 */       LOGGER.error("Expected compound tag, got {}", debug3);
/*     */     } 
/*     */   }
/*     */   
/*     */   private <T> Dynamic<T> writeColumn(ChunkPos debug1, DynamicOps<T> debug2) {
/* 155 */     Map<T, T> debug3 = Maps.newHashMap();
/* 156 */     for (int debug4 = 0; debug4 < 16; debug4++) {
/* 157 */       long debug5 = SectionPos.of(debug1, debug4).asLong();
/* 158 */       this.dirty.remove(debug5);
/* 159 */       Optional<R> debug7 = (Optional<R>)this.storage.get(debug5);
/* 160 */       if (debug7 != null && debug7.isPresent()) {
/*     */ 
/*     */         
/* 163 */         DataResult<T> debug8 = ((Codec)this.codec.apply(() -> setDirty(debug1))).encodeStart(debug2, debug7.get());
/* 164 */         String debug9 = Integer.toString(debug4);
/* 165 */         debug8.resultOrPartial(LOGGER::error).ifPresent(debug3 -> debug0.put(debug1.createString(debug2), debug3));
/*     */       } 
/*     */     } 
/* 168 */     return new Dynamic(debug2, debug2.createMap((Map)ImmutableMap.of(debug2
/* 169 */             .createString("Sections"), debug2.createMap(debug3), debug2
/* 170 */             .createString("DataVersion"), debug2.createInt(SharedConstants.getCurrentVersion().getWorldVersion()))));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onSectionLoad(long debug1) {}
/*     */   
/*     */   protected void setDirty(long debug1) {
/* 177 */     Optional<R> debug3 = (Optional<R>)this.storage.get(debug1);
/* 178 */     if (debug3 == null || !debug3.isPresent()) {
/* 179 */       LOGGER.warn("No data for position: {}", SectionPos.of(debug1));
/*     */       return;
/*     */     } 
/* 182 */     this.dirty.add(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int getVersion(Dynamic<?> debug0) {
/* 187 */     return debug0.get("DataVersion").asInt(1945);
/*     */   }
/*     */   
/*     */   public void flush(ChunkPos debug1) {
/* 191 */     if (!this.dirty.isEmpty()) {
/* 192 */       for (int debug2 = 0; debug2 < 16; debug2++) {
/* 193 */         long debug3 = SectionPos.of(debug1, debug2).asLong();
/* 194 */         if (this.dirty.contains(debug3)) {
/* 195 */           writeColumn(debug1);
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 204 */     this.worker.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\chunk\storage\SectionStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */