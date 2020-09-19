/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.world.entity.player.Abilities;
/*    */ 
/*    */ public enum GameType {
/*  8 */   NOT_SET(-1, ""),
/*  9 */   SURVIVAL(0, "survival"),
/* 10 */   CREATIVE(1, "creative"),
/* 11 */   ADVENTURE(2, "adventure"),
/* 12 */   SPECTATOR(3, "spectator");
/*    */   
/*    */   private final int id;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   GameType(int debug3, String debug4) {
/* 19 */     this.id = debug3;
/* 20 */     this.name = debug4;
/*    */   }
/*    */   
/*    */   public int getId() {
/* 24 */     return this.id;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 28 */     return this.name;
/*    */   }
/*    */   
/*    */   public Component getDisplayName() {
/* 32 */     return (Component)new TranslatableComponent("gameMode." + this.name);
/*    */   }
/*    */   
/*    */   public void updatePlayerAbilities(Abilities debug1) {
/* 36 */     if (this == CREATIVE) {
/* 37 */       debug1.mayfly = true;
/* 38 */       debug1.instabuild = true;
/* 39 */       debug1.invulnerable = true;
/* 40 */     } else if (this == SPECTATOR) {
/* 41 */       debug1.mayfly = true;
/* 42 */       debug1.instabuild = false;
/* 43 */       debug1.invulnerable = true;
/* 44 */       debug1.flying = true;
/*    */     } else {
/* 46 */       debug1.mayfly = false;
/* 47 */       debug1.instabuild = false;
/* 48 */       debug1.invulnerable = false;
/* 49 */       debug1.flying = false;
/*    */     } 
/* 51 */     debug1.mayBuild = !isBlockPlacingRestricted();
/*    */   }
/*    */   
/*    */   public boolean isBlockPlacingRestricted() {
/* 55 */     return (this == ADVENTURE || this == SPECTATOR);
/*    */   }
/*    */   
/*    */   public boolean isCreative() {
/* 59 */     return (this == CREATIVE);
/*    */   }
/*    */   
/*    */   public boolean isSurvival() {
/* 63 */     return (this == SURVIVAL || this == ADVENTURE);
/*    */   }
/*    */   
/*    */   public static GameType byId(int debug0) {
/* 67 */     return byId(debug0, SURVIVAL);
/*    */   }
/*    */   
/*    */   public static GameType byId(int debug0, GameType debug1) {
/* 71 */     for (GameType debug5 : values()) {
/* 72 */       if (debug5.id == debug0) {
/* 73 */         return debug5;
/*    */       }
/*    */     } 
/* 76 */     return debug1;
/*    */   }
/*    */   
/*    */   public static GameType byName(String debug0) {
/* 80 */     return byName(debug0, SURVIVAL);
/*    */   }
/*    */   
/*    */   public static GameType byName(String debug0, GameType debug1) {
/* 84 */     for (GameType debug5 : values()) {
/* 85 */       if (debug5.name.equals(debug0)) {
/* 86 */         return debug5;
/*    */       }
/*    */     } 
/* 89 */     return debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\GameType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */