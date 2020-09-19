/*    */ package net.minecraft.util.profiling;
/*    */ 
/*    */ import java.io.File;
/*    */ 
/*    */ public class EmptyProfileResults
/*    */   implements ProfileResults
/*    */ {
/*  8 */   public static final EmptyProfileResults EMPTY = new EmptyProfileResults();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean saveResults(File debug1) {
/* 20 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getStartTimeNano() {
/* 25 */     return 0L;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getStartTimeTicks() {
/* 30 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getEndTimeNano() {
/* 35 */     return 0L;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getEndTimeTicks() {
/* 40 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\profiling\EmptyProfileResults.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */