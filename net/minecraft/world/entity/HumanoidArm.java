/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ 
/*    */ public enum HumanoidArm {
/*  7 */   LEFT((Component)new TranslatableComponent("options.mainHand.left")),
/*  8 */   RIGHT((Component)new TranslatableComponent("options.mainHand.right"));
/*    */   
/*    */   private final Component name;
/*    */ 
/*    */   
/*    */   HumanoidArm(Component debug3) {
/* 14 */     this.name = debug3;
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
/*    */   public String toString() {
/* 26 */     return this.name.getString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\HumanoidArm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */