/*    */ package net.minecraft.core;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.Arrays;
/*    */ import java.util.Set;
/*    */ 
/*    */ public enum Direction8
/*    */ {
/*  9 */   NORTH(new Direction[] { Direction.NORTH }),
/* 10 */   NORTH_EAST(new Direction[] { Direction.NORTH, Direction.EAST }),
/* 11 */   EAST(new Direction[] { Direction.EAST }),
/* 12 */   SOUTH_EAST(new Direction[] { Direction.SOUTH, Direction.EAST }),
/* 13 */   SOUTH(new Direction[] { Direction.SOUTH }),
/* 14 */   SOUTH_WEST(new Direction[] { Direction.SOUTH, Direction.WEST }),
/* 15 */   WEST(new Direction[] { Direction.WEST }),
/* 16 */   NORTH_WEST(new Direction[] { Direction.NORTH, Direction.WEST }); private static final int NORTH_WEST_MASK; private static final int WEST_MASK; private static final int SOUTH_WEST_MASK; private static final int SOUTH_MASK;
/*    */   static {
/* 18 */     NORTH_WEST_MASK = 1 << NORTH_WEST.ordinal();
/* 19 */     WEST_MASK = 1 << WEST.ordinal();
/* 20 */     SOUTH_WEST_MASK = 1 << SOUTH_WEST.ordinal();
/* 21 */     SOUTH_MASK = 1 << SOUTH.ordinal();
/* 22 */     SOUTH_EAST_MASK = 1 << SOUTH_EAST.ordinal();
/* 23 */     EAST_MASK = 1 << EAST.ordinal();
/* 24 */     NORTH_EAST_MASK = 1 << NORTH_EAST.ordinal();
/* 25 */     NORTH_MASK = 1 << NORTH.ordinal();
/*    */   }
/*    */   private static final int SOUTH_EAST_MASK; private static final int EAST_MASK; private static final int NORTH_EAST_MASK; private static final int NORTH_MASK; private final Set<Direction> directions;
/*    */   
/*    */   Direction8(Direction... debug3) {
/* 30 */     this.directions = (Set<Direction>)Sets.immutableEnumSet(Arrays.asList(debug3));
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
/*    */ 
/*    */ 
/*    */   
/*    */   public Set<Direction> getDirections() {
/* 60 */     return this.directions;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\core\Direction8.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */