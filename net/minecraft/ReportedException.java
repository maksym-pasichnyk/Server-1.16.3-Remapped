/*    */ package net.minecraft;
/*    */ 
/*    */ public class ReportedException extends RuntimeException {
/*    */   private final CrashReport report;
/*    */   
/*    */   public ReportedException(CrashReport debug1) {
/*  7 */     this.report = debug1;
/*    */   }
/*    */   
/*    */   public CrashReport getReport() {
/* 11 */     return this.report;
/*    */   }
/*    */ 
/*    */   
/*    */   public Throwable getCause() {
/* 16 */     return this.report.getException();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 21 */     return this.report.getTitle();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\ReportedException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */