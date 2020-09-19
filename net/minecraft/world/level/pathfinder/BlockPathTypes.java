/*    */ package net.minecraft.world.level.pathfinder;
/*    */ 
/*    */ public enum BlockPathTypes {
/*  4 */   BLOCKED(-1.0F),
/*  5 */   OPEN(0.0F),
/*  6 */   WALKABLE(0.0F),
/*  7 */   WALKABLE_DOOR(0.0F),
/*  8 */   TRAPDOOR(0.0F),
/*  9 */   FENCE(-1.0F),
/* 10 */   LAVA(-1.0F),
/* 11 */   WATER(8.0F),
/* 12 */   WATER_BORDER(8.0F),
/* 13 */   RAIL(0.0F),
/* 14 */   UNPASSABLE_RAIL(-1.0F),
/* 15 */   DANGER_FIRE(8.0F),
/* 16 */   DAMAGE_FIRE(16.0F),
/* 17 */   DANGER_CACTUS(8.0F),
/* 18 */   DAMAGE_CACTUS(-1.0F),
/* 19 */   DANGER_OTHER(8.0F),
/* 20 */   DAMAGE_OTHER(-1.0F),
/* 21 */   DOOR_OPEN(0.0F),
/* 22 */   DOOR_WOOD_CLOSED(-1.0F),
/* 23 */   DOOR_IRON_CLOSED(-1.0F),
/* 24 */   BREACH(4.0F),
/* 25 */   LEAVES(-1.0F),
/* 26 */   STICKY_HONEY(8.0F),
/* 27 */   COCOA(0.0F);
/*    */   
/*    */   private final float malus;
/*    */ 
/*    */   
/*    */   BlockPathTypes(float debug3) {
/* 33 */     this.malus = debug3;
/*    */   }
/*    */   
/*    */   public float getMalus() {
/* 37 */     return this.malus;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\pathfinder\BlockPathTypes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */