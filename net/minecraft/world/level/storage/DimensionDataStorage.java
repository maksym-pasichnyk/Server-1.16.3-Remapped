/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PushbackInputStream;
/*     */ import java.util.Map;
/*     */ import java.util.function.Supplier;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.world.level.saveddata.SavedData;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DimensionDataStorage
/*     */ {
/*  26 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*  28 */   private final Map<String, SavedData> cache = Maps.newHashMap();
/*     */   private final DataFixer fixerUpper;
/*     */   private final File dataFolder;
/*     */   
/*     */   public DimensionDataStorage(File debug1, DataFixer debug2) {
/*  33 */     this.fixerUpper = debug2;
/*  34 */     this.dataFolder = debug1;
/*     */   }
/*     */   
/*     */   private File getDataFile(String debug1) {
/*  38 */     return new File(this.dataFolder, debug1 + ".dat");
/*     */   }
/*     */   
/*     */   public <T extends SavedData> T computeIfAbsent(Supplier<T> debug1, String debug2) {
/*  42 */     T debug3 = get(debug1, debug2);
/*  43 */     if (debug3 != null) {
/*  44 */       return debug3;
/*     */     }
/*     */     
/*  47 */     SavedData savedData = (SavedData)debug1.get();
/*  48 */     set(savedData);
/*  49 */     return (T)savedData;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <T extends SavedData> T get(Supplier<T> debug1, String debug2) {
/*  55 */     SavedData debug3 = this.cache.get(debug2);
/*  56 */     if (debug3 == null && 
/*  57 */       !this.cache.containsKey(debug2)) {
/*  58 */       debug3 = readSavedData(debug1, debug2);
/*  59 */       this.cache.put(debug2, debug3);
/*     */     } 
/*     */     
/*  62 */     return (T)debug3;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private <T extends SavedData> T readSavedData(Supplier<T> debug1, String debug2) {
/*     */     try {
/*  68 */       File debug3 = getDataFile(debug2);
/*  69 */       if (debug3.exists()) {
/*  70 */         SavedData savedData = (SavedData)debug1.get();
/*  71 */         CompoundTag debug5 = readTagFromDisk(debug2, SharedConstants.getCurrentVersion().getWorldVersion());
/*  72 */         savedData.load(debug5.getCompound("data"));
/*  73 */         return (T)savedData;
/*     */       } 
/*  75 */     } catch (Exception debug3) {
/*  76 */       LOGGER.error("Error loading saved data: {}", debug2, debug3);
/*     */     } 
/*  78 */     return null;
/*     */   }
/*     */   
/*     */   public void set(SavedData debug1) {
/*  82 */     this.cache.put(debug1.getId(), debug1);
/*     */   }
/*     */   
/*     */   public CompoundTag readTagFromDisk(String debug1, int debug2) throws IOException {
/*  86 */     File debug3 = getDataFile(debug1);
/*     */     
/*  88 */     try (FileInputStream debug4 = new FileInputStream(debug3)) {
/*  89 */       PushbackInputStream debug6 = new PushbackInputStream(debug4, 2);
/*     */       Throwable throwable = null;
/*     */     } 
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
/*     */   private boolean isGzip(PushbackInputStream debug1) throws IOException {
/* 106 */     byte[] debug2 = new byte[2];
/* 107 */     boolean debug3 = false;
/* 108 */     int debug4 = debug1.read(debug2, 0, 2);
/* 109 */     if (debug4 == 2) {
/* 110 */       int debug5 = (debug2[1] & 0xFF) << 8 | debug2[0] & 0xFF;
/* 111 */       if (debug5 == 35615) {
/* 112 */         debug3 = true;
/*     */       }
/*     */     } 
/* 115 */     if (debug4 != 0) {
/* 116 */       debug1.unread(debug2, 0, debug4);
/*     */     }
/* 118 */     return debug3;
/*     */   }
/*     */   
/*     */   public void save() {
/* 122 */     for (SavedData debug2 : this.cache.values()) {
/* 123 */       if (debug2 != null)
/* 124 */         debug2.save(getDataFile(debug2.getId())); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\DimensionDataStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */