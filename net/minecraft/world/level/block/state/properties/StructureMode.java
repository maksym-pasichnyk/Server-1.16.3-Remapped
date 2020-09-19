/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum StructureMode implements StringRepresentable {
/*  8 */   SAVE("save"),
/*  9 */   LOAD("load"),
/* 10 */   CORNER("corner"),
/* 11 */   DATA("data");
/*    */   
/*    */   private final String name;
/*    */   
/*    */   private final Component displayName;
/*    */   
/*    */   StructureMode(String debug3) {
/* 18 */     this.name = debug3;
/* 19 */     this.displayName = (Component)new TranslatableComponent("structure_block.mode_info." + debug3);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 24 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\state\properties\StructureMode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */