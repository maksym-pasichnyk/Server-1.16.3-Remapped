/*    */ package net.minecraft.world.level.storage;
/*    */ 
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.OptionalDynamic;
/*    */ import net.minecraft.SharedConstants;
/*    */ 
/*    */ public class LevelVersion {
/*    */   private final int levelDataVersion;
/*    */   private final long lastPlayed;
/*    */   private final String minecraftVersionName;
/*    */   private final int minecraftVersion;
/*    */   private final boolean snapshot;
/*    */   
/*    */   public LevelVersion(int debug1, long debug2, String debug4, int debug5, boolean debug6) {
/* 15 */     this.levelDataVersion = debug1;
/* 16 */     this.lastPlayed = debug2;
/* 17 */     this.minecraftVersionName = debug4;
/* 18 */     this.minecraftVersion = debug5;
/* 19 */     this.snapshot = debug6;
/*    */   }
/*    */   
/*    */   public static LevelVersion parse(Dynamic<?> debug0) {
/* 23 */     int debug1 = debug0.get("version").asInt(0);
/* 24 */     long debug2 = debug0.get("LastPlayed").asLong(0L);
/* 25 */     OptionalDynamic<?> debug4 = debug0.get("Version");
/*    */     
/* 27 */     if (debug4.result().isPresent()) {
/* 28 */       return new LevelVersion(debug1, debug2, debug4
/*    */ 
/*    */           
/* 31 */           .get("Name").asString(SharedConstants.getCurrentVersion().getName()), debug4
/* 32 */           .get("Id").asInt(SharedConstants.getCurrentVersion().getWorldVersion()), debug4
/* 33 */           .get("Snapshot").asBoolean(!SharedConstants.getCurrentVersion().isStable()));
/*    */     }
/*    */     
/* 36 */     return new LevelVersion(debug1, debug2, "", 0, false);
/*    */   }
/*    */   
/*    */   public int levelDataVersion() {
/* 40 */     return this.levelDataVersion;
/*    */   }
/*    */   
/*    */   public long lastPlayed() {
/* 44 */     return this.lastPlayed;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\LevelVersion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */