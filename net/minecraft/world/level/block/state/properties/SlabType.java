/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum SlabType implements StringRepresentable {
/*  6 */   TOP("top"),
/*  7 */   BOTTOM("bottom"),
/*  8 */   DOUBLE("double");
/*    */   
/*    */   private final String name;
/*    */ 
/*    */   
/*    */   SlabType(String debug3) {
/* 14 */     this.name = debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 19 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 24 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\state\properties\SlabType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */