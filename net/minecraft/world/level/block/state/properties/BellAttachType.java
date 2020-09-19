/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum BellAttachType implements StringRepresentable {
/*  6 */   FLOOR("floor"),
/*  7 */   CEILING("ceiling"),
/*  8 */   SINGLE_WALL("single_wall"),
/*  9 */   DOUBLE_WALL("double_wall");
/*    */   
/*    */   private final String name;
/*    */ 
/*    */   
/*    */   BellAttachType(String debug3) {
/* 15 */     this.name = debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 20 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\state\properties\BellAttachType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */