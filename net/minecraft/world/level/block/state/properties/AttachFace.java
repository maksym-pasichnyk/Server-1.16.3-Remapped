/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum AttachFace implements StringRepresentable {
/*  6 */   FLOOR("floor"),
/*  7 */   WALL("wall"),
/*  8 */   CEILING("ceiling");
/*    */   
/*    */   private final String name;
/*    */ 
/*    */   
/*    */   AttachFace(String debug3) {
/* 14 */     this.name = debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 19 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\state\properties\AttachFace.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */