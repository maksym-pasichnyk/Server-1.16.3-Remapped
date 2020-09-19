/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum DoorHingeSide implements StringRepresentable {
/*  6 */   LEFT,
/*  7 */   RIGHT;
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 12 */     return getSerializedName();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 17 */     return (this == LEFT) ? "left" : "right";
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\state\properties\DoorHingeSide.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */