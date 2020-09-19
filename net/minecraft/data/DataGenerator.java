/*    */ package net.minecraft.data;
/*    */ 
/*    */ import com.google.common.base.Stopwatch;
/*    */ import com.google.common.collect.Lists;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Path;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import net.minecraft.server.Bootstrap;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class DataGenerator
/*    */ {
/* 16 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */   
/*    */   private final Collection<Path> inputFolders;
/*    */   private final Path outputFolder;
/* 20 */   private final List<DataProvider> providers = Lists.newArrayList();
/*    */   
/*    */   static {
/* 23 */     Bootstrap.bootStrap();
/*    */   }
/*    */   
/*    */   public DataGenerator(Path debug1, Collection<Path> debug2) {
/* 27 */     this.outputFolder = debug1;
/* 28 */     this.inputFolders = debug2;
/*    */   }
/*    */   
/*    */   public Collection<Path> getInputFolders() {
/* 32 */     return this.inputFolders;
/*    */   }
/*    */   
/*    */   public Path getOutputFolder() {
/* 36 */     return this.outputFolder;
/*    */   }
/*    */   
/*    */   public void run() throws IOException {
/* 40 */     HashCache debug1 = new HashCache(this.outputFolder, "cache");
/* 41 */     debug1.keep(getOutputFolder().resolve("version.json"));
/*    */     
/* 43 */     Stopwatch debug2 = Stopwatch.createStarted();
/* 44 */     Stopwatch debug3 = Stopwatch.createUnstarted();
/* 45 */     for (DataProvider debug5 : this.providers) {
/* 46 */       LOGGER.info("Starting provider: {}", debug5.getName());
/* 47 */       debug3.start();
/* 48 */       debug5.run(debug1);
/* 49 */       debug3.stop();
/* 50 */       LOGGER.info("{} finished after {} ms", debug5.getName(), Long.valueOf(debug3.elapsed(TimeUnit.MILLISECONDS)));
/* 51 */       debug3.reset();
/*    */     } 
/* 53 */     LOGGER.info("All providers took: {} ms", Long.valueOf(debug2.elapsed(TimeUnit.MILLISECONDS)));
/*    */     
/* 55 */     debug1.purgeStaleAndWrite();
/*    */   }
/*    */   
/*    */   public void addProvider(DataProvider debug1) {
/* 59 */     this.providers.add(debug1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\DataGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */