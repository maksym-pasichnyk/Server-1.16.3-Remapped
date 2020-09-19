/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum StairsShape implements StringRepresentable {
/*  6 */   STRAIGHT("straight"),
/*  7 */   INNER_LEFT("inner_left"),
/*  8 */   INNER_RIGHT("inner_right"),
/*  9 */   OUTER_LEFT("outer_left"),
/* 10 */   OUTER_RIGHT("outer_right");
/*    */   
/*    */   private final String name;
/*    */ 
/*    */   
/*    */   StairsShape(String debug3) {
/* 16 */     this.name = debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 21 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 26 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\state\properties\StairsShape.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */