/*    */ package net.minecraft.world.level.storage;
/*    */ 
/*    */ import java.io.File;
/*    */ import net.minecraft.world.level.LevelSettings;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LevelSummary
/*    */   implements Comparable<LevelSummary>
/*    */ {
/*    */   private final LevelSettings settings;
/*    */   private final LevelVersion levelVersion;
/*    */   private final String levelId;
/*    */   private final boolean requiresConversion;
/*    */   private final boolean locked;
/*    */   private final File icon;
/*    */   
/*    */   public LevelSummary(LevelSettings debug1, LevelVersion debug2, String debug3, boolean debug4, boolean debug5, File debug6) {
/* 28 */     this.settings = debug1;
/* 29 */     this.levelVersion = debug2;
/* 30 */     this.levelId = debug3;
/* 31 */     this.locked = debug5;
/* 32 */     this.icon = debug6;
/* 33 */     this.requiresConversion = debug4;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int compareTo(LevelSummary debug1) {
/* 58 */     if (this.levelVersion.lastPlayed() < debug1.levelVersion.lastPlayed()) {
/* 59 */       return 1;
/*    */     }
/* 61 */     if (this.levelVersion.lastPlayed() > debug1.levelVersion.lastPlayed()) {
/* 62 */       return -1;
/*    */     }
/* 64 */     return this.levelId.compareTo(debug1.levelId);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public LevelVersion levelVersion() {
/* 91 */     return this.levelVersion;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\LevelSummary.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */