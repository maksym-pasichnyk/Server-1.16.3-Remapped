/*    */ package net.minecraft.server;
/*    */ 
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.util.Properties;
/*    */ import net.minecraft.SharedConstants;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class Eula
/*    */ {
/* 14 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */   
/*    */   private final Path file;
/*    */   private final boolean agreed;
/*    */   
/*    */   public Eula(Path debug1) {
/* 20 */     this.file = debug1;
/* 21 */     this.agreed = (SharedConstants.IS_RUNNING_IN_IDE || readFile());
/*    */   }
/*    */   
/*    */   private boolean readFile() {
/* 25 */     try (InputStream debug1 = Files.newInputStream(this.file, new java.nio.file.OpenOption[0])) {
/* 26 */       Properties debug3 = new Properties();
/* 27 */       debug3.load(debug1);
/* 28 */       return Boolean.parseBoolean(debug3.getProperty("eula", "false"));
/* 29 */     } catch (Exception debug1) {
/* 30 */       LOGGER.warn("Failed to load {}", this.file);
/* 31 */       saveDefaults();
/*    */       
/* 33 */       return false;
/*    */     } 
/*    */   }
/*    */   public boolean hasAgreedToEULA() {
/* 37 */     return this.agreed;
/*    */   }
/*    */   
/*    */   private void saveDefaults() {
/* 41 */     if (SharedConstants.IS_RUNNING_IN_IDE) {
/*    */       return;
/*    */     }
/* 44 */     try (OutputStream debug1 = Files.newOutputStream(this.file, new java.nio.file.OpenOption[0])) {
/* 45 */       Properties debug3 = new Properties();
/* 46 */       debug3.setProperty("eula", "false");
/* 47 */       debug3.store(debug1, "By changing the setting below to TRUE you are indicating your agreement to our EULA (https://account.mojang.com/documents/minecraft_eula).");
/* 48 */     } catch (Exception debug1) {
/* 49 */       LOGGER.warn("Failed to save {}", this.file, debug1);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\Eula.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */