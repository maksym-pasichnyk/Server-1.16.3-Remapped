/*     */ package net.minecraft.util.worldupdate;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.common.util.concurrent.ThreadFactoryBuilder;
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import it.unimi.dsi.fastutil.objects.Object2FloatMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2FloatMaps;
/*     */ import it.unimi.dsi.fastutil.objects.Object2FloatOpenCustomHashMap;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.chunk.storage.ChunkStorage;
/*     */ import net.minecraft.world.level.chunk.storage.RegionFile;
/*     */ import net.minecraft.world.level.storage.DimensionDataStorage;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class WorldUpgrader
/*     */ {
/*  38 */   private static final Logger LOGGER = LogManager.getLogger();
/*  39 */   private static final ThreadFactory THREAD_FACTORY = (new ThreadFactoryBuilder()).setDaemon(true).build();
/*     */   
/*     */   private final ImmutableSet<ResourceKey<Level>> levels;
/*     */   
/*     */   private final boolean eraseCache;
/*     */   private final LevelStorageSource.LevelStorageAccess levelStorage;
/*     */   private final Thread thread;
/*     */   private final DataFixer dataFixer;
/*     */   private volatile boolean running = true;
/*     */   private volatile boolean finished;
/*     */   private volatile float progress;
/*     */   private volatile int totalChunks;
/*     */   private volatile int converted;
/*     */   private volatile int skipped;
/*  53 */   private final Object2FloatMap<ResourceKey<Level>> progressMap = Object2FloatMaps.synchronize((Object2FloatMap)new Object2FloatOpenCustomHashMap(Util.identityStrategy()));
/*     */   
/*  55 */   private volatile Component status = (Component)new TranslatableComponent("optimizeWorld.stage.counting");
/*     */   
/*  57 */   private static final Pattern REGEX = Pattern.compile("^r\\.(-?[0-9]+)\\.(-?[0-9]+)\\.mca$");
/*     */   private final DimensionDataStorage overworldDataStorage;
/*     */   
/*     */   public WorldUpgrader(LevelStorageSource.LevelStorageAccess debug1, DataFixer debug2, ImmutableSet<ResourceKey<Level>> debug3, boolean debug4) {
/*  61 */     this.levels = debug3;
/*  62 */     this.eraseCache = debug4;
/*  63 */     this.dataFixer = debug2;
/*  64 */     this.levelStorage = debug1;
/*  65 */     this.overworldDataStorage = new DimensionDataStorage(new File(this.levelStorage.getDimensionPath(Level.OVERWORLD), "data"), debug2);
/*     */     
/*  67 */     this.thread = THREAD_FACTORY.newThread(this::work);
/*  68 */     this.thread.setUncaughtExceptionHandler((debug1, debug2) -> {
/*     */           LOGGER.error("Error upgrading world", debug2);
/*     */           this.status = (Component)new TranslatableComponent("optimizeWorld.stage.failed");
/*     */           this.finished = true;
/*     */         });
/*  73 */     this.thread.start();
/*     */   }
/*     */   
/*     */   public void cancel() {
/*  77 */     this.running = false;
/*     */     try {
/*  79 */       this.thread.join();
/*  80 */     } catch (InterruptedException interruptedException) {}
/*     */   }
/*     */ 
/*     */   
/*     */   private void work() {
/*  85 */     this.totalChunks = 0;
/*  86 */     ImmutableMap.Builder<ResourceKey<Level>, ListIterator<ChunkPos>> debug1 = ImmutableMap.builder();
/*  87 */     for (UnmodifiableIterator<ResourceKey<Level>> unmodifiableIterator1 = this.levels.iterator(); unmodifiableIterator1.hasNext(); ) { ResourceKey<Level> resourceKey = unmodifiableIterator1.next();
/*  88 */       List<ChunkPos> list = getAllChunkPos(resourceKey);
/*  89 */       debug1.put(resourceKey, list.listIterator());
/*  90 */       this.totalChunks += list.size(); }
/*     */     
/*  92 */     if (this.totalChunks == 0) {
/*  93 */       this.finished = true;
/*     */       return;
/*     */     } 
/*  96 */     float debug2 = this.totalChunks;
/*  97 */     ImmutableMap<ResourceKey<Level>, ListIterator<ChunkPos>> debug3 = debug1.build();
/*     */     
/*  99 */     ImmutableMap.Builder<ResourceKey<Level>, ChunkStorage> debug4 = ImmutableMap.builder();
/* 100 */     for (UnmodifiableIterator<ResourceKey<Level>> unmodifiableIterator2 = this.levels.iterator(); unmodifiableIterator2.hasNext(); ) { ResourceKey<Level> resourceKey = unmodifiableIterator2.next();
/* 101 */       File debug7 = this.levelStorage.getDimensionPath(resourceKey);
/* 102 */       debug4.put(resourceKey, new ChunkStorage(new File(debug7, "region"), this.dataFixer, true)); }
/*     */     
/* 104 */     ImmutableMap<ResourceKey<Level>, ChunkStorage> debug5 = debug4.build();
/* 105 */     long debug6 = Util.getMillis();
/*     */     
/* 107 */     this.status = (Component)new TranslatableComponent("optimizeWorld.stage.upgrading");
/*     */     
/* 109 */     while (this.running) {
/* 110 */       boolean debug8 = false;
/*     */       
/* 112 */       float debug9 = 0.0F;
/* 113 */       for (UnmodifiableIterator<ResourceKey<Level>> unmodifiableIterator3 = this.levels.iterator(); unmodifiableIterator3.hasNext(); ) { ResourceKey<Level> debug11 = unmodifiableIterator3.next();
/* 114 */         ListIterator<ChunkPos> debug12 = (ListIterator<ChunkPos>)debug3.get(debug11);
/* 115 */         ChunkStorage debug13 = (ChunkStorage)debug5.get(debug11);
/* 116 */         if (debug12.hasNext()) {
/* 117 */           ChunkPos chunkPos = debug12.next();
/* 118 */           boolean debug15 = false;
/*     */           try {
/* 120 */             CompoundTag debug16 = debug13.read(chunkPos);
/* 121 */             if (debug16 != null) {
/* 122 */               int debug17 = ChunkStorage.getVersion(debug16);
/* 123 */               CompoundTag debug18 = debug13.upgradeChunkTag(debug11, () -> this.overworldDataStorage, debug16);
/*     */               
/* 125 */               CompoundTag debug19 = debug18.getCompound("Level");
/*     */               
/* 127 */               ChunkPos debug20 = new ChunkPos(debug19.getInt("xPos"), debug19.getInt("zPos"));
/* 128 */               if (!debug20.equals(chunkPos)) {
/* 129 */                 LOGGER.warn("Chunk {} has invalid position {}", chunkPos, debug20);
/*     */               }
/*     */               
/* 132 */               boolean debug21 = (debug17 < SharedConstants.getCurrentVersion().getWorldVersion());
/* 133 */               if (this.eraseCache) {
/* 134 */                 debug21 = (debug21 || debug19.contains("Heightmaps"));
/* 135 */                 debug19.remove("Heightmaps");
/* 136 */                 debug21 = (debug21 || debug19.contains("isLightOn"));
/* 137 */                 debug19.remove("isLightOn");
/*     */               } 
/*     */               
/* 140 */               if (debug21) {
/* 141 */                 debug13.write(chunkPos, debug18);
/* 142 */                 debug15 = true;
/*     */               } 
/*     */             } 
/* 145 */           } catch (ReportedException debug16) {
/* 146 */             Throwable debug17 = debug16.getCause();
/* 147 */             if (debug17 instanceof IOException) {
/* 148 */               LOGGER.error("Error upgrading chunk {}", chunkPos, debug17);
/*     */             } else {
/* 150 */               throw debug16;
/*     */             } 
/* 152 */           } catch (IOException debug16) {
/* 153 */             LOGGER.error("Error upgrading chunk {}", chunkPos, debug16);
/*     */           } 
/*     */           
/* 156 */           if (debug15) {
/* 157 */             this.converted++;
/*     */           } else {
/* 159 */             this.skipped++;
/*     */           } 
/* 161 */           debug8 = true;
/*     */         } 
/* 163 */         float debug14 = debug12.nextIndex() / debug2;
/* 164 */         this.progressMap.put(debug11, debug14);
/* 165 */         debug9 += debug14; }
/*     */ 
/*     */       
/* 168 */       this.progress = debug9;
/*     */       
/* 170 */       if (!debug8) {
/* 171 */         this.running = false;
/*     */       }
/*     */     } 
/*     */     
/* 175 */     this.status = (Component)new TranslatableComponent("optimizeWorld.stage.finished");
/*     */     
/* 177 */     for (UnmodifiableIterator<ChunkStorage> unmodifiableIterator = debug5.values().iterator(); unmodifiableIterator.hasNext(); ) { ChunkStorage debug9 = unmodifiableIterator.next();
/*     */       try {
/* 179 */         debug9.close();
/* 180 */       } catch (IOException debug10) {
/* 181 */         LOGGER.error("Error upgrading chunk", debug10);
/*     */       }  }
/*     */     
/* 184 */     this.overworldDataStorage.save();
/* 185 */     debug6 = Util.getMillis() - debug6;
/* 186 */     LOGGER.info("World optimizaton finished after {} ms", Long.valueOf(debug6));
/* 187 */     this.finished = true;
/*     */   }
/*     */   
/*     */   private List<ChunkPos> getAllChunkPos(ResourceKey<Level> debug1) {
/* 191 */     File debug2 = this.levelStorage.getDimensionPath(debug1);
/*     */     
/* 193 */     File debug3 = new File(debug2, "region");
/* 194 */     File[] debug4 = debug3.listFiles((debug0, debug1) -> debug1.endsWith(".mca"));
/*     */     
/* 196 */     if (debug4 == null) {
/* 197 */       return (List<ChunkPos>)ImmutableList.of();
/*     */     }
/*     */     
/* 200 */     List<ChunkPos> debug5 = Lists.newArrayList();
/* 201 */     for (File debug9 : debug4) {
/* 202 */       Matcher debug10 = REGEX.matcher(debug9.getName());
/* 203 */       if (debug10.matches()) {
/*     */ 
/*     */ 
/*     */         
/* 207 */         int debug11 = Integer.parseInt(debug10.group(1)) << 5;
/* 208 */         int debug12 = Integer.parseInt(debug10.group(2)) << 5;
/*     */         
/* 210 */         try (RegionFile debug13 = new RegionFile(debug9, debug3, true)) {
/* 211 */           for (int debug15 = 0; debug15 < 32; debug15++) {
/* 212 */             for (int debug16 = 0; debug16 < 32; debug16++) {
/* 213 */               ChunkPos debug17 = new ChunkPos(debug15 + debug11, debug16 + debug12);
/* 214 */               if (debug13.doesChunkExist(debug17)) {
/* 215 */                 debug5.add(debug17);
/*     */               }
/*     */             } 
/*     */           } 
/* 219 */         } catch (Throwable throwable) {}
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 224 */     return debug5;
/*     */   }
/*     */   
/*     */   public boolean isFinished() {
/* 228 */     return this.finished;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTotalChunks() {
/* 244 */     return this.totalChunks;
/*     */   }
/*     */   
/*     */   public int getConverted() {
/* 248 */     return this.converted;
/*     */   }
/*     */   
/*     */   public int getSkipped() {
/* 252 */     return this.skipped;
/*     */   }
/*     */   
/*     */   public Component getStatus() {
/* 256 */     return this.status;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\worldupdate\WorldUpgrader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */