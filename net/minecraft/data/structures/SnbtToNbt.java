/*     */ package net.minecraft.data.structures;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.data.DataGenerator;
/*     */ import net.minecraft.data.DataProvider;
/*     */ import net.minecraft.data.HashCache;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtIo;
/*     */ import net.minecraft.nbt.TagParser;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class SnbtToNbt
/*     */   implements DataProvider {
/*     */   @Nullable
/*  32 */   private static final Path dumpSnbtTo = null;
/*     */   
/*  34 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   private final DataGenerator generator;
/*     */   
/*  38 */   private final List<Filter> filters = Lists.newArrayList();
/*     */   
/*     */   public SnbtToNbt(DataGenerator debug1) {
/*  41 */     this.generator = debug1;
/*     */   }
/*     */   
/*     */   public SnbtToNbt addFilter(Filter debug1) {
/*  45 */     this.filters.add(debug1);
/*  46 */     return this;
/*     */   }
/*     */   
/*     */   private CompoundTag applyFilters(String debug1, CompoundTag debug2) {
/*  50 */     CompoundTag debug3 = debug2;
/*  51 */     for (Filter debug5 : this.filters) {
/*  52 */       debug3 = debug5.apply(debug1, debug3);
/*     */     }
/*  54 */     return debug3;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface Filter {
/*     */     CompoundTag apply(String param1String, CompoundTag param1CompoundTag); }
/*     */   
/*     */   static class TaskResult { private final String name;
/*     */     private final byte[] payload;
/*     */     
/*     */     public TaskResult(String debug1, byte[] debug2, @Nullable String debug3, String debug4) {
/*  65 */       this.name = debug1;
/*  66 */       this.payload = debug2;
/*  67 */       this.snbtPayload = debug3;
/*  68 */       this.hash = debug4;
/*     */     }
/*     */     @Nullable
/*     */     private final String snbtPayload; private final String hash; }
/*     */   
/*     */   public void run(HashCache debug1) throws IOException {
/*  74 */     Path debug2 = this.generator.getOutputFolder();
/*     */     
/*  76 */     List<CompletableFuture<TaskResult>> debug3 = Lists.newArrayList();
/*     */     
/*  78 */     for (Path debug5 : this.generator.getInputFolders()) {
/*  79 */       Files.walk(debug5, new java.nio.file.FileVisitOption[0]).filter(debug0 -> debug0.toString().endsWith(".snbt")).forEach(debug3 -> debug1.add(CompletableFuture.supplyAsync((), Util.backgroundExecutor())));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  84 */     ((List)Util.sequence(debug3).join()).stream().filter(Objects::nonNull).forEach(debug3 -> storeStructureIfChanged(debug1, debug3, debug2));
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  89 */     return "SNBT -> NBT";
/*     */   }
/*     */   
/*     */   private String getName(Path debug1, Path debug2) {
/*  93 */     String debug3 = debug1.relativize(debug2).toString().replaceAll("\\\\", "/");
/*  94 */     return debug3.substring(0, debug3.length() - ".snbt".length());
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private TaskResult readStructure(Path debug1, String debug2) {
/*  99 */     try (BufferedReader debug3 = Files.newBufferedReader(debug1)) {
/* 100 */       String debug10, debug5 = IOUtils.toString(debug3);
/* 101 */       CompoundTag debug6 = applyFilters(debug2, TagParser.parseTag(debug5));
/* 102 */       ByteArrayOutputStream debug7 = new ByteArrayOutputStream();
/* 103 */       NbtIo.writeCompressed(debug6, debug7);
/* 104 */       byte[] debug8 = debug7.toByteArray();
/* 105 */       String debug9 = SHA1.hashBytes(debug8).toString();
/*     */       
/* 107 */       if (dumpSnbtTo != null) {
/* 108 */         debug10 = debug6.getPrettyDisplay("    ", 0).getString() + "\n";
/*     */       } else {
/* 110 */         debug10 = null;
/*     */       } 
/* 112 */       return new TaskResult(debug2, debug8, debug10, debug9);
/* 113 */     } catch (CommandSyntaxException debug3) {
/* 114 */       LOGGER.error("Couldn't convert {} from SNBT to NBT at {} as it's invalid SNBT", debug2, debug1, debug3);
/* 115 */     } catch (IOException debug3) {
/* 116 */       LOGGER.error("Couldn't convert {} from SNBT to NBT at {}", debug2, debug1, debug3);
/*     */     } 
/* 118 */     return null;
/*     */   }
/*     */   
/*     */   private void storeStructureIfChanged(HashCache debug1, TaskResult debug2, Path debug3) {
/* 122 */     if (debug2.snbtPayload != null) {
/* 123 */       Path path = dumpSnbtTo.resolve(debug2.name + ".snbt");
/*     */       try {
/* 125 */         FileUtils.write(path.toFile(), debug2.snbtPayload, StandardCharsets.UTF_8);
/* 126 */       } catch (IOException debug5) {
/* 127 */         LOGGER.error("Couldn't write structure SNBT {} at {}", debug2.name, path, debug5);
/*     */       } 
/*     */     } 
/*     */     
/* 131 */     Path debug4 = debug3.resolve(debug2.name + ".nbt");
/*     */     try {
/* 133 */       if (!Objects.equals(debug1.getHash(debug4), debug2.hash) || !Files.exists(debug4, new java.nio.file.LinkOption[0])) {
/* 134 */         Files.createDirectories(debug4.getParent(), (FileAttribute<?>[])new FileAttribute[0]);
/* 135 */         try (OutputStream debug5 = Files.newOutputStream(debug4, new java.nio.file.OpenOption[0])) {
/* 136 */           debug5.write(debug2.payload);
/*     */         } 
/*     */       } 
/* 139 */       debug1.putNew(debug4, debug2.hash);
/* 140 */     } catch (IOException debug5) {
/* 141 */       LOGGER.error("Couldn't write structure {} at {}", debug2.name, debug4, debug5);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\structures\SnbtToNbt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */