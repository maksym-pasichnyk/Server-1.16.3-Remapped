/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicLike;
/*    */ import net.minecraft.world.Difficulty;
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class LevelSettings
/*    */ {
/*    */   private final String levelName;
/*    */   private final GameType gameType;
/*    */   private final boolean hardcore;
/*    */   
/*    */   public LevelSettings(String debug1, GameType debug2, boolean debug3, Difficulty debug4, boolean debug5, GameRules debug6, DataPackConfig debug7) {
/* 16 */     this.levelName = debug1;
/* 17 */     this.gameType = debug2;
/* 18 */     this.hardcore = debug3;
/* 19 */     this.difficulty = debug4;
/* 20 */     this.allowCommands = debug5;
/* 21 */     this.gameRules = debug6;
/* 22 */     this.dataPackConfig = debug7;
/*    */   }
/*    */   private final Difficulty difficulty; private final boolean allowCommands; private final GameRules gameRules; private final DataPackConfig dataPackConfig;
/*    */   public static LevelSettings parse(Dynamic<?> debug0, DataPackConfig debug1) {
/* 26 */     GameType debug2 = GameType.byId(debug0.get("GameType").asInt(0));
/* 27 */     return new LevelSettings(debug0.get("LevelName").asString(""), debug2, debug0
/*    */         
/* 29 */         .get("hardcore").asBoolean(false), debug0
/* 30 */         .get("Difficulty").asNumber().map(debug0 -> Difficulty.byId(debug0.byteValue())).result().orElse(Difficulty.NORMAL), debug0
/* 31 */         .get("allowCommands").asBoolean((debug2 == GameType.CREATIVE)), new GameRules((DynamicLike<?>)debug0
/* 32 */           .get("GameRules")), debug1);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String levelName() {
/* 38 */     return this.levelName;
/*    */   }
/*    */   
/*    */   public GameType gameType() {
/* 42 */     return this.gameType;
/*    */   }
/*    */   
/*    */   public boolean hardcore() {
/* 46 */     return this.hardcore;
/*    */   }
/*    */   
/*    */   public Difficulty difficulty() {
/* 50 */     return this.difficulty;
/*    */   }
/*    */   
/*    */   public boolean allowCommands() {
/* 54 */     return this.allowCommands;
/*    */   }
/*    */   
/*    */   public GameRules gameRules() {
/* 58 */     return this.gameRules;
/*    */   }
/*    */   
/*    */   public DataPackConfig getDataPackConfig() {
/* 62 */     return this.dataPackConfig;
/*    */   }
/*    */   
/*    */   public LevelSettings withGameType(GameType debug1) {
/* 66 */     return new LevelSettings(this.levelName, debug1, this.hardcore, this.difficulty, this.allowCommands, this.gameRules, this.dataPackConfig);
/*    */   }
/*    */   
/*    */   public LevelSettings withDifficulty(Difficulty debug1) {
/* 70 */     return new LevelSettings(this.levelName, this.gameType, this.hardcore, debug1, this.allowCommands, this.gameRules, this.dataPackConfig);
/*    */   }
/*    */   
/*    */   public LevelSettings withDataPackConfig(DataPackConfig debug1) {
/* 74 */     return new LevelSettings(this.levelName, this.gameType, this.hardcore, this.difficulty, this.allowCommands, this.gameRules, debug1);
/*    */   }
/*    */   
/*    */   public LevelSettings copy() {
/* 78 */     return new LevelSettings(this.levelName, this.gameType, this.hardcore, this.difficulty, this.allowCommands, this.gameRules.copy(), this.dataPackConfig);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\LevelSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */