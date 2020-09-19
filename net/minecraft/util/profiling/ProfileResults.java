/*    */ package net.minecraft.util.profiling;
/*    */ 
/*    */ import java.io.File;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ProfileResults
/*    */ {
/*    */   boolean saveResults(File paramFile);
/*    */   
/*    */   long getStartTimeNano();
/*    */   
/*    */   int getStartTimeTicks();
/*    */   
/*    */   long getEndTimeNano();
/*    */   
/*    */   int getEndTimeTicks();
/*    */   
/*    */   default long getNanoDuration() {
/* 22 */     return getEndTimeNano() - getStartTimeNano();
/*    */   }
/*    */   
/*    */   default int getTickDuration() {
/* 26 */     return getEndTimeTicks() - getStartTimeTicks();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   static String demanglePath(String debug0) {
/* 32 */     return debug0.replace('\036', '.');
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\profiling\ProfileResults.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */