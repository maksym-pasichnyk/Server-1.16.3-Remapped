/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GameTestAssertPosException
/*    */   extends GameTestAssertException
/*    */ {
/*    */   private final BlockPos absolutePos;
/*    */   private final BlockPos relativePos;
/*    */   private final long tick;
/*    */   
/*    */   public String getMessage() {
/* 21 */     String debug1 = "" + this.absolutePos.getX() + "," + this.absolutePos.getY() + "," + this.absolutePos.getZ() + " (relative: " + this.relativePos.getX() + "," + this.relativePos.getY() + "," + this.relativePos.getZ() + ")";
/* 22 */     return super.getMessage() + " at " + debug1 + " (t=" + this.tick + ")";
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public String getMessageToShowAtBlock() {
/* 27 */     return super.getMessage() + " here";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public BlockPos getAbsolutePos() {
/* 37 */     return this.absolutePos;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\gametest\framework\GameTestAssertPosException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */