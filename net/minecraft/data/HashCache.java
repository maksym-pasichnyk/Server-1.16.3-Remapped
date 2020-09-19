/*     */ package net.minecraft.data;
/*     */ import com.google.common.base.Charsets;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nullable;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class HashCache {
/*  22 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   private final Path path;
/*     */   
/*     */   private final Path cachePath;
/*     */   
/*     */   private int hits;
/*  29 */   private final Map<Path, String> oldCache = Maps.newHashMap();
/*  30 */   private final Map<Path, String> newCache = Maps.newHashMap();
/*  31 */   private final Set<Path> keep = Sets.newHashSet();
/*     */   
/*     */   public HashCache(Path debug1, String debug2) throws IOException {
/*  34 */     this.path = debug1;
/*     */     
/*  36 */     Path debug3 = debug1.resolve(".cache");
/*  37 */     Files.createDirectories(debug3, (FileAttribute<?>[])new FileAttribute[0]);
/*  38 */     this.cachePath = debug3.resolve(debug2);
/*     */     
/*  40 */     walkOutputFiles().forEach(debug1 -> (String)this.oldCache.put(debug1, ""));
/*     */     
/*  42 */     if (Files.isReadable(this.cachePath))
/*  43 */       IOUtils.readLines(Files.newInputStream(this.cachePath, new java.nio.file.OpenOption[0]), Charsets.UTF_8).forEach(debug2 -> {
/*     */             int debug3 = debug2.indexOf(' ');
/*     */             this.oldCache.put(debug1.resolve(debug2.substring(debug3 + 1)), debug2.substring(0, debug3));
/*     */           }); 
/*     */   }
/*     */   
/*     */   public void purgeStaleAndWrite() throws IOException {
/*     */     Writer debug1;
/*  51 */     removeStale();
/*     */ 
/*     */     
/*     */     try {
/*  55 */       debug1 = Files.newBufferedWriter(this.cachePath, new java.nio.file.OpenOption[0]);
/*  56 */     } catch (IOException debug2) {
/*  57 */       LOGGER.warn("Unable write cachefile {}: {}", this.cachePath, debug2.toString());
/*     */       
/*     */       return;
/*     */     } 
/*  61 */     IOUtils.writeLines((Collection)this.newCache
/*  62 */         .entrySet().stream().map(debug1 -> (String)debug1.getValue() + ' ' + this.path.relativize((Path)debug1.getKey())).collect(Collectors.toList()), 
/*  63 */         System.lineSeparator(), debug1);
/*     */ 
/*     */ 
/*     */     
/*  67 */     debug1.close();
/*     */     
/*  69 */     LOGGER.debug("Caching: cache hits: {}, created: {} removed: {}", 
/*  70 */         Integer.valueOf(this.hits), 
/*  71 */         Integer.valueOf(this.newCache.size() - this.hits), 
/*  72 */         Integer.valueOf(this.oldCache.size()));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getHash(Path debug1) {
/*  78 */     return this.oldCache.get(debug1);
/*     */   }
/*     */   
/*     */   public void putNew(Path debug1, String debug2) {
/*  82 */     this.newCache.put(debug1, debug2);
/*  83 */     if (Objects.equals(this.oldCache.remove(debug1), debug2)) {
/*  84 */       this.hits++;
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean had(Path debug1) {
/*  89 */     return this.oldCache.containsKey(debug1);
/*     */   }
/*     */   
/*     */   public void keep(Path debug1) {
/*  93 */     this.keep.add(debug1);
/*     */   }
/*     */   
/*     */   private void removeStale() throws IOException {
/*  97 */     walkOutputFiles().forEach(debug1 -> {
/*     */           if (had(debug1) && !this.keep.contains(debug1)) {
/*     */             try {
/*     */               Files.delete(debug1);
/* 101 */             } catch (IOException debug2) {
/*     */               LOGGER.debug("Unable to delete: {} ({})", debug1, debug2.toString());
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private Stream<Path> walkOutputFiles() throws IOException {
/* 109 */     return Files.walk(this.path, new java.nio.file.FileVisitOption[0]).filter(debug1 -> (!Objects.equals(this.cachePath, debug1) && !Files.isDirectory(debug1, new java.nio.file.LinkOption[0])));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\HashCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */