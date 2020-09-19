/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum ChestType implements StringRepresentable {
/*    */   public static final ChestType[] BY_ID;
/*  6 */   SINGLE("single", 0),
/*  7 */   LEFT("left", 2),
/*  8 */   RIGHT("right", 1);
/*    */   
/*    */   static {
/* 11 */     BY_ID = values();
/*    */   }
/*    */   private final String name;
/*    */   private final int opposite;
/*    */   
/*    */   ChestType(String debug3, int debug4) {
/* 17 */     this.name = debug3;
/* 18 */     this.opposite = debug4;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 23 */     return this.name;
/*    */   }
/*    */   
/*    */   public ChestType getOpposite() {
/* 27 */     return BY_ID[this.opposite];
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\state\properties\ChestType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */