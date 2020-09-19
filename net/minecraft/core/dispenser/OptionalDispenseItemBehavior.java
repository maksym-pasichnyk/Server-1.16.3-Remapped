/*    */ package net.minecraft.core.dispenser;
/*    */ 
/*    */ import net.minecraft.core.BlockSource;
/*    */ 
/*    */ public abstract class OptionalDispenseItemBehavior
/*    */   extends DefaultDispenseItemBehavior {
/*    */   private boolean success = true;
/*    */   
/*    */   public boolean isSuccess() {
/* 10 */     return this.success;
/*    */   }
/*    */   
/*    */   public void setSuccess(boolean debug1) {
/* 14 */     this.success = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void playSound(BlockSource debug1) {
/* 19 */     debug1.getLevel().levelEvent(isSuccess() ? 1000 : 1001, debug1.getPos(), 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\core\dispenser\OptionalDispenseItemBehavior.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */