/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum DoubleBlockHalf implements StringRepresentable {
/*  6 */   UPPER,
/*  7 */   LOWER;
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 12 */     return getSerializedName();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 17 */     return (this == UPPER) ? "upper" : "lower";
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\state\properties\DoubleBlockHalf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */