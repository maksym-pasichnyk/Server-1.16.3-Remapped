/*    */ package net.minecraft.data.structures;
/*    */ import java.io.BufferedWriter;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.attribute.FileAttribute;
/*    */ import java.util.Iterator;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.data.DataGenerator;
/*    */ import net.minecraft.data.DataProvider;
/*    */ import net.minecraft.data.HashCache;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.NbtIo;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class NbtToSnbt implements DataProvider {
/* 19 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */   
/*    */   private final DataGenerator generator;
/*    */   
/*    */   public NbtToSnbt(DataGenerator debug1) {
/* 24 */     this.generator = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void run(HashCache debug1) throws IOException {
/* 29 */     Path debug2 = this.generator.getOutputFolder();
/*    */     
/* 31 */     for (Iterator<Path> iterator = this.generator.getInputFolders().iterator(); iterator.hasNext(); ) { Path debug4 = iterator.next();
/* 32 */       Files.walk(debug4, new java.nio.file.FileVisitOption[0]).filter(debug0 -> debug0.toString().endsWith(".nbt")).forEach(debug3 -> convertStructure(debug3, getName(debug1, debug3), debug2)); }
/*    */   
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 38 */     return "NBT to SNBT";
/*    */   }
/*    */   
/*    */   private String getName(Path debug1, Path debug2) {
/* 42 */     String debug3 = debug1.relativize(debug2).toString().replaceAll("\\\\", "/");
/* 43 */     return debug3.substring(0, debug3.length() - ".nbt".length());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public static Path convertStructure(Path debug0, String debug1, Path debug2) {
/*    */     try {
/* 53 */       CompoundTag debug3 = NbtIo.readCompressed(Files.newInputStream(debug0, new java.nio.file.OpenOption[0]));
/* 54 */       Component debug4 = debug3.getPrettyDisplay("    ", 0);
/* 55 */       String debug5 = debug4.getString() + "\n";
/* 56 */       Path debug6 = debug2.resolve(debug1 + ".snbt");
/* 57 */       Files.createDirectories(debug6.getParent(), (FileAttribute<?>[])new FileAttribute[0]);
/* 58 */       try (BufferedWriter debug7 = Files.newBufferedWriter(debug6, new java.nio.file.OpenOption[0])) {
/* 59 */         debug7.write(debug5);
/*    */       } 
/* 61 */       LOGGER.info("Converted {} from NBT to SNBT", debug1);
/* 62 */       return debug6;
/* 63 */     } catch (IOException debug3) {
/* 64 */       LOGGER.error("Couldn't convert {} from NBT to SNBT at {}", debug1, debug0, debug3);
/* 65 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\structures\NbtToSnbt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */