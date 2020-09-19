/*    */ package net.minecraft.stats;
/*    */ import java.text.DecimalFormat;
/*    */ import java.text.DecimalFormatSymbols;
/*    */ import java.text.NumberFormat;
/*    */ import java.util.Locale;
/*    */ 
/*    */ public interface StatFormatter {
/*    */   public static final DecimalFormat DECIMAL_FORMAT;
/*    */   
/*    */   static {
/* 11 */     DECIMAL_FORMAT = (DecimalFormat)Util.make(new DecimalFormat("########0.00"), debug0 -> debug0.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT)));
/*    */   }
/* 13 */   public static final StatFormatter DEFAULT = NumberFormat.getIntegerInstance(Locale.US)::format; static {
/* 14 */     DIVIDE_BY_TEN = (debug0 -> DECIMAL_FORMAT.format(debug0 * 0.1D));
/* 15 */     DISTANCE = (debug0 -> {
/*    */         double debug1 = debug0 / 100.0D;
/*    */ 
/*    */         
/*    */         double debug3 = debug1 / 1000.0D;
/*    */ 
/*    */         
/*    */         return (debug3 > 0.5D) ? (DECIMAL_FORMAT.format(debug3) + " km") : ((debug1 > 0.5D) ? (DECIMAL_FORMAT.format(debug1) + " m") : (debug0 + " cm"));
/*    */       });
/*    */ 
/*    */     
/* 26 */     TIME = (debug0 -> {
/*    */         double debug1 = debug0 / 20.0D;
/*    */         double debug3 = debug1 / 60.0D;
/*    */         double debug5 = debug3 / 60.0D;
/*    */         double debug7 = debug5 / 24.0D;
/*    */         double debug9 = debug7 / 365.0D;
/*    */         return (debug9 > 0.5D) ? (DECIMAL_FORMAT.format(debug9) + " y") : ((debug7 > 0.5D) ? (DECIMAL_FORMAT.format(debug7) + " d") : ((debug5 > 0.5D) ? (DECIMAL_FORMAT.format(debug5) + " h") : ((debug3 > 0.5D) ? (DECIMAL_FORMAT.format(debug3) + " m") : (debug1 + " s"))));
/*    */       });
/*    */   }
/*    */   
/*    */   public static final StatFormatter DIVIDE_BY_TEN;
/*    */   public static final StatFormatter DISTANCE;
/*    */   public static final StatFormatter TIME;
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\stats\StatFormatter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */