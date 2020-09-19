/*    */ package org.apache.commons.lang3.time;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.Calendar;
/*    */ import org.apache.commons.lang3.exception.ExceptionUtils;
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
/*    */ 
/*    */ 
/*    */ class CalendarReflection
/*    */ {
/* 30 */   private static final Method IS_WEEK_DATE_SUPPORTED = getCalendarMethod("isWeekDateSupported", new Class[0]);
/* 31 */   private static final Method GET_WEEK_YEAR = getCalendarMethod("getWeekYear", new Class[0]);
/*    */   
/*    */   private static Method getCalendarMethod(String methodName, Class<?>... argTypes) {
/*    */     try {
/* 35 */       Method m = Calendar.class.getMethod(methodName, argTypes);
/* 36 */       return m;
/* 37 */     } catch (Exception e) {
/* 38 */       return null;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static boolean isWeekDateSupported(Calendar calendar) {
/*    */     try {
/* 49 */       return (IS_WEEK_DATE_SUPPORTED != null && ((Boolean)IS_WEEK_DATE_SUPPORTED.invoke(calendar, new Object[0])).booleanValue());
/* 50 */     } catch (Exception e) {
/* 51 */       return ((Boolean)ExceptionUtils.rethrow(e)).booleanValue();
/*    */     } 
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static int getWeekYear(Calendar calendar) {
/*    */     try {
/* 73 */       if (isWeekDateSupported(calendar)) {
/* 74 */         return ((Integer)GET_WEEK_YEAR.invoke(calendar, new Object[0])).intValue();
/*    */       }
/* 76 */     } catch (Exception e) {
/* 77 */       return ((Integer)ExceptionUtils.rethrow(e)).intValue();
/*    */     } 
/*    */     
/* 80 */     int year = calendar.get(1);
/* 81 */     if (IS_WEEK_DATE_SUPPORTED == null && calendar instanceof java.util.GregorianCalendar)
/*    */     {
/*    */       
/* 84 */       switch (calendar.get(2)) {
/*    */         case 0:
/* 86 */           if (calendar.get(3) >= 52) {
/* 87 */             year--;
/*    */           }
/*    */           break;
/*    */         case 11:
/* 91 */           if (calendar.get(3) == 1) {
/* 92 */             year++;
/*    */           }
/*    */           break;
/*    */       } 
/*    */     }
/* 97 */     return year;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\time\CalendarReflection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */