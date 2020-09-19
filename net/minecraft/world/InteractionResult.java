/*    */ package net.minecraft.world;
/*    */ 
/*    */ public enum InteractionResult {
/*  4 */   SUCCESS,
/*  5 */   CONSUME,
/*  6 */   PASS,
/*  7 */   FAIL;
/*    */   
/*    */   public boolean consumesAction() {
/* 10 */     return (this == SUCCESS || this == CONSUME);
/*    */   }
/*    */   
/*    */   public boolean shouldSwing() {
/* 14 */     return (this == SUCCESS);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static InteractionResult sidedSuccess(boolean debug0) {
/* 22 */     return debug0 ? SUCCESS : CONSUME;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\InteractionResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */