/*    */ package net.minecraft.core;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum FrontAndTop implements StringRepresentable {
/*  8 */   DOWN_EAST("down_east", Direction.DOWN, Direction.EAST),
/*  9 */   DOWN_NORTH("down_north", Direction.DOWN, Direction.NORTH),
/* 10 */   DOWN_SOUTH("down_south", Direction.DOWN, Direction.SOUTH),
/* 11 */   DOWN_WEST("down_west", Direction.DOWN, Direction.WEST),
/*    */   
/* 13 */   UP_EAST("up_east", Direction.UP, Direction.EAST),
/* 14 */   UP_NORTH("up_north", Direction.UP, Direction.NORTH),
/* 15 */   UP_SOUTH("up_south", Direction.UP, Direction.SOUTH),
/* 16 */   UP_WEST("up_west", Direction.UP, Direction.WEST),
/*    */   
/* 18 */   WEST_UP("west_up", Direction.WEST, Direction.UP),
/* 19 */   EAST_UP("east_up", Direction.EAST, Direction.UP),
/* 20 */   NORTH_UP("north_up", Direction.NORTH, Direction.UP),
/* 21 */   SOUTH_UP("south_up", Direction.SOUTH, Direction.UP);
/*    */   
/*    */   static {
/* 24 */     LOOKUP_TOP_FRONT = (Int2ObjectMap<FrontAndTop>)new Int2ObjectOpenHashMap((values()).length);
/*    */ 
/*    */     
/* 27 */     for (FrontAndTop debug3 : values())
/* 28 */       LOOKUP_TOP_FRONT.put(lookupKey(debug3.top, debug3.front), debug3); 
/*    */   }
/*    */   
/*    */   private static final Int2ObjectMap<FrontAndTop> LOOKUP_TOP_FRONT;
/*    */   private final String name;
/*    */   private final Direction top;
/*    */   private final Direction front;
/*    */   
/*    */   private static int lookupKey(Direction debug0, Direction debug1) {
/* 37 */     return debug0.ordinal() << 3 | debug1.ordinal();
/*    */   }
/*    */   
/*    */   FrontAndTop(String debug3, Direction debug4, Direction debug5) {
/* 41 */     this.name = debug3;
/* 42 */     this.front = debug4;
/* 43 */     this.top = debug5;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 48 */     return this.name;
/*    */   }
/*    */   
/*    */   public static FrontAndTop fromFrontAndTop(Direction debug0, Direction debug1) {
/* 52 */     int debug2 = lookupKey(debug1, debug0);
/* 53 */     return (FrontAndTop)LOOKUP_TOP_FRONT.get(debug2);
/*    */   }
/*    */   
/*    */   public Direction front() {
/* 57 */     return this.front;
/*    */   }
/*    */   
/*    */   public Direction top() {
/* 61 */     return this.top;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\core\FrontAndTop.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */