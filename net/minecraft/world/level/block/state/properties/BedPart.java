/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum BedPart implements StringRepresentable {
/*  6 */   HEAD("head"),
/*  7 */   FOOT("foot");
/*    */   
/*    */   private final String name;
/*    */ 
/*    */   
/*    */   BedPart(String debug3) {
/* 13 */     this.name = debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 18 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 23 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\state\properties\BedPart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */