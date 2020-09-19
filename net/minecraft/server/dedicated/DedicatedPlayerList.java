/*     */ package net.minecraft.server.dedicated;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import java.io.IOException;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.players.PlayerList;
/*     */ import net.minecraft.world.level.storage.PlayerDataStorage;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class DedicatedPlayerList extends PlayerList {
/*  13 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   public DedicatedPlayerList(DedicatedServer debug1, RegistryAccess.RegistryHolder debug2, PlayerDataStorage debug3) {
/*  16 */     super(debug1, debug2, debug3, (debug1.getProperties()).maxPlayers);
/*     */     
/*  18 */     DedicatedServerProperties debug4 = debug1.getProperties();
/*  19 */     setViewDistance(debug4.viewDistance);
/*  20 */     super.setUsingWhiteList(((Boolean)debug4.whiteList.get()).booleanValue());
/*     */     
/*  22 */     loadUserBanList();
/*  23 */     saveUserBanList();
/*  24 */     loadIpBanList();
/*  25 */     saveIpBanList();
/*  26 */     loadOps();
/*  27 */     loadWhiteList();
/*  28 */     saveOps();
/*  29 */     if (!getWhiteList().getFile().exists()) {
/*  30 */       saveWhiteList();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUsingWhiteList(boolean debug1) {
/*  36 */     super.setUsingWhiteList(debug1);
/*  37 */     getServer().storeUsingWhiteList(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void op(GameProfile debug1) {
/*  42 */     super.op(debug1);
/*  43 */     saveOps();
/*     */   }
/*     */ 
/*     */   
/*     */   public void deop(GameProfile debug1) {
/*  48 */     super.deop(debug1);
/*  49 */     saveOps();
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
/*     */   
/*     */   public void reloadWhiteList() {
/*  66 */     loadWhiteList();
/*     */   }
/*     */   
/*     */   private void saveIpBanList() {
/*     */     try {
/*  71 */       getIpBans().save();
/*  72 */     } catch (IOException debug1) {
/*  73 */       LOGGER.warn("Failed to save ip banlist: ", debug1);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void saveUserBanList() {
/*     */     try {
/*  79 */       getBans().save();
/*  80 */     } catch (IOException debug1) {
/*  81 */       LOGGER.warn("Failed to save user banlist: ", debug1);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void loadIpBanList() {
/*     */     try {
/*  87 */       getIpBans().load();
/*  88 */     } catch (IOException debug1) {
/*  89 */       LOGGER.warn("Failed to load ip banlist: ", debug1);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void loadUserBanList() {
/*     */     try {
/*  95 */       getBans().load();
/*  96 */     } catch (IOException debug1) {
/*  97 */       LOGGER.warn("Failed to load user banlist: ", debug1);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void loadOps() {
/*     */     try {
/* 103 */       getOps().load();
/* 104 */     } catch (Exception debug1) {
/* 105 */       LOGGER.warn("Failed to load operators list: ", debug1);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void saveOps() {
/*     */     try {
/* 111 */       getOps().save();
/* 112 */     } catch (Exception debug1) {
/* 113 */       LOGGER.warn("Failed to save operators list: ", debug1);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void loadWhiteList() {
/*     */     try {
/* 119 */       getWhiteList().load();
/* 120 */     } catch (Exception debug1) {
/* 121 */       LOGGER.warn("Failed to load white-list: ", debug1);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void saveWhiteList() {
/*     */     try {
/* 127 */       getWhiteList().save();
/* 128 */     } catch (Exception debug1) {
/* 129 */       LOGGER.warn("Failed to save white-list: ", debug1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWhiteListed(GameProfile debug1) {
/* 135 */     return (!isUsingWhitelist() || isOp(debug1) || getWhiteList().isWhiteListed(debug1));
/*     */   }
/*     */ 
/*     */   
/*     */   public DedicatedServer getServer() {
/* 140 */     return (DedicatedServer)super.getServer();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBypassPlayerLimit(GameProfile debug1) {
/* 145 */     return getOps().canBypassPlayerLimit(debug1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\dedicated\DedicatedPlayerList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */