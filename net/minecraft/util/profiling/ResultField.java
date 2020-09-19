/*    */ package net.minecraft.util.profiling;
/*    */ 
/*    */ public final class ResultField implements Comparable<ResultField> {
/*    */   public final double percentage;
/*    */   public final double globalPercentage;
/*    */   public final long count;
/*    */   public final String name;
/*    */   
/*    */   public ResultField(String debug1, double debug2, double debug4, long debug6) {
/* 10 */     this.name = debug1;
/* 11 */     this.percentage = debug2;
/* 12 */     this.globalPercentage = debug4;
/* 13 */     this.count = debug6;
/*    */   }
/*    */ 
/*    */   
/*    */   public int compareTo(ResultField debug1) {
/* 18 */     if (debug1.percentage < this.percentage) {
/* 19 */       return -1;
/*    */     }
/* 21 */     if (debug1.percentage > this.percentage) {
/* 22 */       return 1;
/*    */     }
/* 24 */     return debug1.name.compareTo(this.name);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\profiling\ResultField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */