/*    */ package net.minecraft.world.level.storage;
/*    */ 
/*    */ public class LevelResource {
/*  4 */   public static final LevelResource PLAYER_ADVANCEMENTS_DIR = new LevelResource("advancements");
/*  5 */   public static final LevelResource PLAYER_STATS_DIR = new LevelResource("stats");
/*  6 */   public static final LevelResource PLAYER_DATA_DIR = new LevelResource("playerdata");
/*  7 */   public static final LevelResource PLAYER_OLD_DATA_DIR = new LevelResource("players");
/*  8 */   public static final LevelResource LEVEL_DATA_FILE = new LevelResource("level.dat");
/*  9 */   public static final LevelResource GENERATED_DIR = new LevelResource("generated");
/* 10 */   public static final LevelResource DATAPACK_DIR = new LevelResource("datapacks");
/* 11 */   public static final LevelResource MAP_RESOURCE_FILE = new LevelResource("resources.zip");
/* 12 */   public static final LevelResource ROOT = new LevelResource(".");
/*    */   
/*    */   private final String id;
/*    */   
/*    */   private LevelResource(String debug1) {
/* 17 */     this.id = debug1;
/*    */   }
/*    */   
/*    */   public String getId() {
/* 21 */     return this.id;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 26 */     return "/" + this.id;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\storage\LevelResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */