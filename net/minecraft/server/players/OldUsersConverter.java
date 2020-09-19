/*     */ package net.minecraft.server.players;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.io.Files;
/*     */ import com.mojang.authlib.Agent;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.ProfileLookupCallback;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.text.ParseException;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.dedicated.DedicatedServer;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.storage.LevelResource;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ public class OldUsersConverter
/*     */ {
/*  31 */   private static final Logger LOGGER = LogManager.getLogger();
/*  32 */   public static final File OLD_IPBANLIST = new File("banned-ips.txt");
/*  33 */   public static final File OLD_USERBANLIST = new File("banned-players.txt");
/*  34 */   public static final File OLD_OPLIST = new File("ops.txt");
/*  35 */   public static final File OLD_WHITELIST = new File("white-list.txt");
/*     */   
/*     */   static List<String> readOldListFormat(File debug0, Map<String, String[]> debug1) throws IOException {
/*  38 */     List<String> debug2 = Files.readLines(debug0, StandardCharsets.UTF_8);
/*  39 */     for (String debug4 : debug2) {
/*  40 */       debug4 = debug4.trim();
/*  41 */       if (debug4.startsWith("#") || debug4.length() < 1) {
/*     */         continue;
/*     */       }
/*  44 */       String[] debug5 = debug4.split("\\|");
/*  45 */       debug1.put(debug5[0].toLowerCase(Locale.ROOT), debug5);
/*     */     } 
/*  47 */     return debug2;
/*     */   }
/*     */   
/*     */   private static void lookupPlayers(MinecraftServer debug0, Collection<String> debug1, ProfileLookupCallback debug2) {
/*  51 */     String[] debug3 = (String[])debug1.stream().filter(debug0 -> !StringUtil.isNullOrEmpty(debug0)).toArray(debug0 -> new String[debug0]);
/*  52 */     if (debug0.usesAuthentication()) {
/*  53 */       debug0.getProfileRepository().findProfilesByNames(debug3, Agent.MINECRAFT, debug2);
/*     */     } else {
/*  55 */       for (String debug7 : debug3) {
/*  56 */         UUID debug8 = Player.createPlayerUUID(new GameProfile(null, debug7));
/*  57 */         GameProfile debug9 = new GameProfile(debug8, debug7);
/*  58 */         debug2.onProfileLookupSucceeded(debug9);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean convertUserBanlist(final MinecraftServer server) {
/*  64 */     final UserBanList bans = new UserBanList(PlayerList.USERBANLIST_FILE);
/*  65 */     if (OLD_USERBANLIST.exists() && OLD_USERBANLIST.isFile()) {
/*  66 */       if (debug1.getFile().exists()) {
/*     */         try {
/*  68 */           debug1.load();
/*  69 */         } catch (IOException debug2) {
/*  70 */           LOGGER.warn("Could not load existing file {}", debug1.getFile().getName(), debug2);
/*     */         } 
/*     */       }
/*     */       try {
/*  74 */         final Map<String, String[]> userMap = Maps.newHashMap();
/*  75 */         readOldListFormat(OLD_USERBANLIST, debug2);
/*     */         
/*  77 */         ProfileLookupCallback debug3 = new ProfileLookupCallback()
/*     */           {
/*     */             public void onProfileLookupSucceeded(GameProfile debug1) {
/*  80 */               server.getProfileCache().add(debug1);
/*  81 */               String[] debug2 = (String[])userMap.get(debug1.getName().toLowerCase(Locale.ROOT));
/*  82 */               if (debug2 == null) {
/*  83 */                 OldUsersConverter.LOGGER.warn("Could not convert user banlist entry for {}", debug1.getName());
/*  84 */                 throw new OldUsersConverter.ConversionError("Profile not in the conversionlist");
/*     */               } 
/*     */               
/*  87 */               Date debug3 = (debug2.length > 1) ? OldUsersConverter.parseDate(debug2[1], null) : null;
/*  88 */               String debug4 = (debug2.length > 2) ? debug2[2] : null;
/*  89 */               Date debug5 = (debug2.length > 3) ? OldUsersConverter.parseDate(debug2[3], null) : null;
/*  90 */               String debug6 = (debug2.length > 4) ? debug2[4] : null;
/*  91 */               bans.add(new UserBanListEntry(debug1, debug3, debug4, debug5, debug6));
/*     */             }
/*     */ 
/*     */             
/*     */             public void onProfileLookupFailed(GameProfile debug1, Exception debug2) {
/*  96 */               OldUsersConverter.LOGGER.warn("Could not lookup user banlist entry for {}", debug1.getName(), debug2);
/*  97 */               if (!(debug2 instanceof com.mojang.authlib.yggdrasil.ProfileNotFoundException)) {
/*  98 */                 throw new OldUsersConverter.ConversionError("Could not request user " + debug1.getName() + " from backend systems", debug2);
/*     */               }
/*     */             }
/*     */           };
/* 102 */         lookupPlayers(server, debug2.keySet(), debug3);
/* 103 */         debug1.save();
/* 104 */         renameOldFile(OLD_USERBANLIST);
/* 105 */       } catch (IOException debug2) {
/* 106 */         LOGGER.warn("Could not read old user banlist to convert it!", debug2);
/* 107 */         return false;
/* 108 */       } catch (ConversionError debug2) {
/* 109 */         LOGGER.error("Conversion failed, please try again later", debug2);
/* 110 */         return false;
/*     */       } 
/* 112 */       return true;
/*     */     } 
/* 114 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean convertIpBanlist(MinecraftServer debug0) {
/* 118 */     IpBanList debug1 = new IpBanList(PlayerList.IPBANLIST_FILE);
/* 119 */     if (OLD_IPBANLIST.exists() && OLD_IPBANLIST.isFile()) {
/* 120 */       if (debug1.getFile().exists()) {
/*     */         try {
/* 122 */           debug1.load();
/* 123 */         } catch (IOException debug2) {
/* 124 */           LOGGER.warn("Could not load existing file {}", debug1.getFile().getName(), debug2);
/*     */         } 
/*     */       }
/*     */       try {
/* 128 */         Map<String, String[]> debug2 = Maps.newHashMap();
/* 129 */         readOldListFormat(OLD_IPBANLIST, debug2);
/*     */         
/* 131 */         for (String debug4 : debug2.keySet()) {
/* 132 */           String[] debug5 = debug2.get(debug4);
/* 133 */           Date debug6 = (debug5.length > 1) ? parseDate(debug5[1], null) : null;
/* 134 */           String debug7 = (debug5.length > 2) ? debug5[2] : null;
/* 135 */           Date debug8 = (debug5.length > 3) ? parseDate(debug5[3], null) : null;
/* 136 */           String debug9 = (debug5.length > 4) ? debug5[4] : null;
/* 137 */           debug1.add(new IpBanListEntry(debug4, debug6, debug7, debug8, debug9));
/*     */         } 
/* 139 */         debug1.save();
/* 140 */         renameOldFile(OLD_IPBANLIST);
/* 141 */       } catch (IOException debug2) {
/* 142 */         LOGGER.warn("Could not parse old ip banlist to convert it!", debug2);
/* 143 */         return false;
/*     */       } 
/* 145 */       return true;
/*     */     } 
/* 147 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean convertOpsList(final MinecraftServer server) {
/* 151 */     final ServerOpList opsList = new ServerOpList(PlayerList.OPLIST_FILE);
/* 152 */     if (OLD_OPLIST.exists() && OLD_OPLIST.isFile()) {
/* 153 */       if (debug1.getFile().exists()) {
/*     */         try {
/* 155 */           debug1.load();
/* 156 */         } catch (IOException debug2) {
/* 157 */           LOGGER.warn("Could not load existing file {}", debug1.getFile().getName(), debug2);
/*     */         } 
/*     */       }
/*     */       try {
/* 161 */         List<String> debug2 = Files.readLines(OLD_OPLIST, StandardCharsets.UTF_8);
/* 162 */         ProfileLookupCallback debug3 = new ProfileLookupCallback()
/*     */           {
/*     */             public void onProfileLookupSucceeded(GameProfile debug1) {
/* 165 */               server.getProfileCache().add(debug1);
/* 166 */               opsList.add(new ServerOpListEntry(debug1, server.getOperatorUserPermissionLevel(), false));
/*     */             }
/*     */ 
/*     */             
/*     */             public void onProfileLookupFailed(GameProfile debug1, Exception debug2) {
/* 171 */               OldUsersConverter.LOGGER.warn("Could not lookup oplist entry for {}", debug1.getName(), debug2);
/* 172 */               if (!(debug2 instanceof com.mojang.authlib.yggdrasil.ProfileNotFoundException)) {
/* 173 */                 throw new OldUsersConverter.ConversionError("Could not request user " + debug1.getName() + " from backend systems", debug2);
/*     */               }
/*     */             }
/*     */           };
/* 177 */         lookupPlayers(server, debug2, debug3);
/* 178 */         debug1.save();
/* 179 */         renameOldFile(OLD_OPLIST);
/* 180 */       } catch (IOException debug2) {
/* 181 */         LOGGER.warn("Could not read old oplist to convert it!", debug2);
/* 182 */         return false;
/* 183 */       } catch (ConversionError debug2) {
/* 184 */         LOGGER.error("Conversion failed, please try again later", debug2);
/* 185 */         return false;
/*     */       } 
/* 187 */       return true;
/*     */     } 
/* 189 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean convertWhiteList(final MinecraftServer server) {
/* 193 */     final UserWhiteList whitelist = new UserWhiteList(PlayerList.WHITELIST_FILE);
/* 194 */     if (OLD_WHITELIST.exists() && OLD_WHITELIST.isFile()) {
/* 195 */       if (debug1.getFile().exists()) {
/*     */         try {
/* 197 */           debug1.load();
/* 198 */         } catch (IOException debug2) {
/* 199 */           LOGGER.warn("Could not load existing file {}", debug1.getFile().getName(), debug2);
/*     */         } 
/*     */       }
/*     */       try {
/* 203 */         List<String> debug2 = Files.readLines(OLD_WHITELIST, StandardCharsets.UTF_8);
/* 204 */         ProfileLookupCallback debug3 = new ProfileLookupCallback()
/*     */           {
/*     */             public void onProfileLookupSucceeded(GameProfile debug1) {
/* 207 */               server.getProfileCache().add(debug1);
/* 208 */               whitelist.add(new UserWhiteListEntry(debug1));
/*     */             }
/*     */ 
/*     */             
/*     */             public void onProfileLookupFailed(GameProfile debug1, Exception debug2) {
/* 213 */               OldUsersConverter.LOGGER.warn("Could not lookup user whitelist entry for {}", debug1.getName(), debug2);
/* 214 */               if (!(debug2 instanceof com.mojang.authlib.yggdrasil.ProfileNotFoundException)) {
/* 215 */                 throw new OldUsersConverter.ConversionError("Could not request user " + debug1.getName() + " from backend systems", debug2);
/*     */               }
/*     */             }
/*     */           };
/* 219 */         lookupPlayers(server, debug2, debug3);
/* 220 */         debug1.save();
/* 221 */         renameOldFile(OLD_WHITELIST);
/* 222 */       } catch (IOException debug2) {
/* 223 */         LOGGER.warn("Could not read old whitelist to convert it!", debug2);
/* 224 */         return false;
/* 225 */       } catch (ConversionError debug2) {
/* 226 */         LOGGER.error("Conversion failed, please try again later", debug2);
/* 227 */         return false;
/*     */       } 
/* 229 */       return true;
/*     */     } 
/* 231 */     return true;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static UUID convertMobOwnerIfNecessary(final MinecraftServer server, String debug1) {
/* 236 */     if (StringUtil.isNullOrEmpty(debug1) || debug1.length() > 16) {
/*     */       try {
/* 238 */         return UUID.fromString(debug1);
/* 239 */       } catch (IllegalArgumentException illegalArgumentException) {
/* 240 */         return null;
/*     */       } 
/*     */     }
/*     */     
/* 244 */     GameProfile debug2 = server.getProfileCache().get(debug1);
/* 245 */     if (debug2 != null && debug2.getId() != null) {
/* 246 */       return debug2.getId();
/*     */     }
/* 248 */     if (server.isSingleplayer() || !server.usesAuthentication()) {
/* 249 */       return Player.createPlayerUUID(new GameProfile(null, debug1));
/*     */     }
/* 251 */     final List<GameProfile> profiles = Lists.newArrayList();
/* 252 */     ProfileLookupCallback debug4 = new ProfileLookupCallback()
/*     */       {
/*     */         public void onProfileLookupSucceeded(GameProfile debug1) {
/* 255 */           server.getProfileCache().add(debug1);
/* 256 */           profiles.add(debug1);
/*     */         }
/*     */ 
/*     */         
/*     */         public void onProfileLookupFailed(GameProfile debug1, Exception debug2) {
/* 261 */           OldUsersConverter.LOGGER.warn("Could not lookup user whitelist entry for {}", debug1.getName(), debug2);
/*     */         }
/*     */       };
/* 264 */     lookupPlayers(server, Lists.newArrayList((Object[])new String[] { debug1 }, ), debug4);
/* 265 */     if (!debug3.isEmpty() && ((GameProfile)debug3.get(0)).getId() != null) {
/* 266 */       return ((GameProfile)debug3.get(0)).getId();
/*     */     }
/*     */     
/* 269 */     return null;
/*     */   }
/*     */   
/*     */   static class ConversionError extends RuntimeException {
/*     */     private ConversionError(String debug1, Throwable debug2) {
/* 274 */       super(debug1, debug2);
/*     */     }
/*     */     
/*     */     private ConversionError(String debug1) {
/* 278 */       super(debug1);
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean convertPlayers(final DedicatedServer server) {
/* 283 */     final File worldPlayerDirectory = getWorldPlayersDirectory((MinecraftServer)server);
/* 284 */     final File worldNewPlayerDirectory = new File(debug1.getParentFile(), "playerdata");
/* 285 */     final File unknownPlayerDirectory = new File(debug1.getParentFile(), "unknownplayers");
/* 286 */     if (!debug1.exists() || !debug1.isDirectory()) {
/* 287 */       return true;
/*     */     }
/* 289 */     File[] debug4 = debug1.listFiles();
/* 290 */     List<String> debug5 = Lists.newArrayList();
/* 291 */     for (File debug9 : debug4) {
/* 292 */       String debug10 = debug9.getName();
/* 293 */       if (debug10.toLowerCase(Locale.ROOT).endsWith(".dat")) {
/*     */ 
/*     */         
/* 296 */         String debug11 = debug10.substring(0, debug10.length() - ".dat".length());
/* 297 */         if (!debug11.isEmpty()) {
/* 298 */           debug5.add(debug11);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     try {
/* 303 */       final String[] names = debug5.<String>toArray(new String[debug5.size()]);
/* 304 */       ProfileLookupCallback debug7 = new ProfileLookupCallback()
/*     */         {
/*     */           public void onProfileLookupSucceeded(GameProfile debug1) {
/* 307 */             server.getProfileCache().add(debug1);
/* 308 */             UUID debug2 = debug1.getId();
/* 309 */             if (debug2 == null) {
/* 310 */               throw new OldUsersConverter.ConversionError("Missing UUID for user profile " + debug1.getName());
/*     */             }
/* 312 */             movePlayerFile(worldNewPlayerDirectory, getFileNameForProfile(debug1), debug2.toString());
/*     */           }
/*     */ 
/*     */           
/*     */           public void onProfileLookupFailed(GameProfile debug1, Exception debug2) {
/* 317 */             OldUsersConverter.LOGGER.warn("Could not lookup user uuid for {}", debug1.getName(), debug2);
/* 318 */             if (debug2 instanceof com.mojang.authlib.yggdrasil.ProfileNotFoundException) {
/* 319 */               String debug3 = getFileNameForProfile(debug1);
/* 320 */               movePlayerFile(unknownPlayerDirectory, debug3, debug3);
/*     */             } else {
/* 322 */               throw new OldUsersConverter.ConversionError("Could not request user " + debug1.getName() + " from backend systems", debug2);
/*     */             } 
/*     */           }
/*     */           
/*     */           private void movePlayerFile(File debug1, String debug2, String debug3) {
/* 327 */             File debug4 = new File(worldPlayerDirectory, debug2 + ".dat");
/* 328 */             File debug5 = new File(debug1, debug3 + ".dat");
/* 329 */             OldUsersConverter.ensureDirectoryExists(debug1);
/* 330 */             if (!debug4.renameTo(debug5)) {
/* 331 */               throw new OldUsersConverter.ConversionError("Could not convert file for " + debug2);
/*     */             }
/*     */           }
/*     */           
/*     */           private String getFileNameForProfile(GameProfile debug1) {
/* 336 */             String debug2 = null;
/* 337 */             for (String debug6 : names) {
/* 338 */               if (debug6 != null && debug6.equalsIgnoreCase(debug1.getName())) {
/* 339 */                 debug2 = debug6;
/*     */                 break;
/*     */               } 
/*     */             } 
/* 343 */             if (debug2 == null) {
/* 344 */               throw new OldUsersConverter.ConversionError("Could not find the filename for " + debug1.getName() + " anymore");
/*     */             }
/* 346 */             return debug2;
/*     */           }
/*     */         };
/* 349 */       lookupPlayers((MinecraftServer)server, Lists.newArrayList((Object[])debug6), debug7);
/* 350 */     } catch (ConversionError debug6) {
/* 351 */       LOGGER.error("Conversion failed, please try again later", debug6);
/* 352 */       return false;
/*     */     } 
/*     */     
/* 355 */     return true;
/*     */   }
/*     */   
/*     */   private static void ensureDirectoryExists(File debug0) {
/* 359 */     if (debug0.exists()) {
/* 360 */       if (debug0.isDirectory()) {
/*     */         return;
/*     */       }
/* 363 */       throw new ConversionError("Can't create directory " + debug0.getName() + " in world save directory.");
/*     */     } 
/*     */     
/* 366 */     if (!debug0.mkdirs()) {
/* 367 */       throw new ConversionError("Can't create directory " + debug0.getName() + " in world save directory.");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean serverReadyAfterUserconversion(MinecraftServer debug0) {
/* 373 */     boolean debug1 = areOldUserlistsRemoved();
/* 374 */     debug1 = (debug1 && areOldPlayersConverted(debug0));
/* 375 */     return debug1;
/*     */   }
/*     */   
/*     */   private static boolean areOldUserlistsRemoved() {
/* 379 */     boolean debug0 = false;
/* 380 */     if (OLD_USERBANLIST.exists() && OLD_USERBANLIST.isFile()) {
/* 381 */       debug0 = true;
/*     */     }
/* 383 */     boolean debug1 = false;
/* 384 */     if (OLD_IPBANLIST.exists() && OLD_IPBANLIST.isFile()) {
/* 385 */       debug1 = true;
/*     */     }
/* 387 */     boolean debug2 = false;
/* 388 */     if (OLD_OPLIST.exists() && OLD_OPLIST.isFile()) {
/* 389 */       debug2 = true;
/*     */     }
/* 391 */     boolean debug3 = false;
/* 392 */     if (OLD_WHITELIST.exists() && OLD_WHITELIST.isFile()) {
/* 393 */       debug3 = true;
/*     */     }
/*     */     
/* 396 */     if (debug0 || debug1 || debug2 || debug3) {
/* 397 */       LOGGER.warn("**** FAILED TO START THE SERVER AFTER ACCOUNT CONVERSION!");
/* 398 */       LOGGER.warn("** please remove the following files and restart the server:");
/* 399 */       if (debug0) {
/* 400 */         LOGGER.warn("* {}", OLD_USERBANLIST.getName());
/*     */       }
/* 402 */       if (debug1) {
/* 403 */         LOGGER.warn("* {}", OLD_IPBANLIST.getName());
/*     */       }
/* 405 */       if (debug2) {
/* 406 */         LOGGER.warn("* {}", OLD_OPLIST.getName());
/*     */       }
/* 408 */       if (debug3) {
/* 409 */         LOGGER.warn("* {}", OLD_WHITELIST.getName());
/*     */       }
/* 411 */       return false;
/*     */     } 
/* 413 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean areOldPlayersConverted(MinecraftServer debug0) {
/* 417 */     File debug1 = getWorldPlayersDirectory(debug0);
/* 418 */     if (debug1.exists() && debug1.isDirectory() && ((
/* 419 */       debug1.list()).length > 0 || !debug1.delete())) {
/* 420 */       LOGGER.warn("**** DETECTED OLD PLAYER DIRECTORY IN THE WORLD SAVE");
/* 421 */       LOGGER.warn("**** THIS USUALLY HAPPENS WHEN THE AUTOMATIC CONVERSION FAILED IN SOME WAY");
/* 422 */       LOGGER.warn("** please restart the server and if the problem persists, remove the directory '{}'", debug1.getPath());
/* 423 */       return false;
/*     */     } 
/*     */     
/* 426 */     return true;
/*     */   }
/*     */   
/*     */   private static File getWorldPlayersDirectory(MinecraftServer debug0) {
/* 430 */     return debug0.getWorldPath(LevelResource.PLAYER_OLD_DATA_DIR).toFile();
/*     */   }
/*     */   
/*     */   private static void renameOldFile(File debug0) {
/* 434 */     File debug1 = new File(debug0.getName() + ".converted");
/* 435 */     debug0.renameTo(debug1);
/*     */   }
/*     */   
/*     */   private static Date parseDate(String debug0, Date debug1) {
/*     */     Date debug2;
/*     */     try {
/* 441 */       debug2 = BanListEntry.DATE_FORMAT.parse(debug0);
/* 442 */     } catch (ParseException debug3) {
/* 443 */       debug2 = debug1;
/*     */     } 
/* 445 */     return debug2;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\players\OldUsersConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */