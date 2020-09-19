/*    */ package net.minecraft.world.level.storage;
/*    */ 
/*    */ import com.mojang.datafixers.DataFixer;
/*    */ import java.io.File;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.Util;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.NbtIo;
/*    */ import net.minecraft.nbt.NbtUtils;
/*    */ import net.minecraft.util.datafix.DataFixTypes;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PlayerDataStorage
/*    */ {
/* 20 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */   private final File playerDir;
/*    */   protected final DataFixer fixerUpper;
/*    */   
/*    */   public PlayerDataStorage(LevelStorageSource.LevelStorageAccess debug1, DataFixer debug2) {
/* 25 */     this.fixerUpper = debug2;
/* 26 */     this.playerDir = debug1.getLevelPath(LevelResource.PLAYER_DATA_DIR).toFile();
/* 27 */     this.playerDir.mkdirs();
/*    */   }
/*    */   
/*    */   public void save(Player debug1) {
/*    */     try {
/* 32 */       CompoundTag debug2 = debug1.saveWithoutId(new CompoundTag());
/* 33 */       File debug3 = File.createTempFile(debug1.getStringUUID() + "-", ".dat", this.playerDir);
/* 34 */       NbtIo.writeCompressed(debug2, debug3);
/*    */       
/* 36 */       File debug4 = new File(this.playerDir, debug1.getStringUUID() + ".dat");
/* 37 */       File debug5 = new File(this.playerDir, debug1.getStringUUID() + ".dat_old");
/* 38 */       Util.safeReplaceFile(debug4, debug3, debug5);
/* 39 */     } catch (Exception debug2) {
/* 40 */       LOGGER.warn("Failed to save player data for {}", debug1.getName().getString());
/*    */     } 
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public CompoundTag load(Player debug1) {
/* 46 */     CompoundTag debug2 = null;
/*    */     try {
/* 48 */       File debug3 = new File(this.playerDir, debug1.getStringUUID() + ".dat");
/* 49 */       if (debug3.exists() && debug3.isFile()) {
/* 50 */         debug2 = NbtIo.readCompressed(debug3);
/*    */       }
/* 52 */     } catch (Exception debug3) {
/* 53 */       LOGGER.warn("Failed to load player data for {}", debug1.getName().getString());
/*    */     } 
/* 55 */     if (debug2 != null) {
/* 56 */       int debug3 = debug2.contains("DataVersion", 3) ? debug2.getInt("DataVersion") : -1;
/* 57 */       debug1.load(NbtUtils.update(this.fixerUpper, DataFixTypes.PLAYER, debug2, debug3));
/*    */     } 
/* 59 */     return debug2;
/*    */   }
/*    */   
/*    */   public String[] getSeenPlayers() {
/* 63 */     String[] debug1 = this.playerDir.list();
/* 64 */     if (debug1 == null) {
/* 65 */       debug1 = new String[0];
/*    */     }
/*    */     
/* 68 */     for (int debug2 = 0; debug2 < debug1.length; debug2++) {
/* 69 */       if (debug1[debug2].endsWith(".dat")) {
/* 70 */         debug1[debug2] = debug1[debug2].substring(0, debug1[debug2].length() - 4);
/*    */       }
/*    */     } 
/*    */     
/* 74 */     return debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\PlayerDataStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */