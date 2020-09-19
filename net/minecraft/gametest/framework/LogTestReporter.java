/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import net.minecraft.Util;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class LogTestReporter implements TestReporter {
/*  8 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */ 
/*    */   
/*    */   public void onTestFailed(GameTestInfo debug1) {
/* 12 */     if (debug1.isRequired()) {
/* 13 */       LOGGER.error(debug1.getTestName() + " failed! " + Util.describeError(debug1.getError()));
/*    */     } else {
/* 15 */       LOGGER.warn("(optional) " + debug1.getTestName() + " failed. " + Util.describeError(debug1.getError()));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\gametest\framework\LogTestReporter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */