/*    */ package org.apache.commons.lang3.text.translate;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.Writer;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class OctalUnescaper
/*    */   extends CharSequenceTranslator
/*    */ {
/*    */   public int translate(CharSequence input, int index, Writer out) throws IOException {
/* 39 */     int remaining = input.length() - index - 1;
/* 40 */     StringBuilder builder = new StringBuilder();
/* 41 */     if (input.charAt(index) == '\\' && remaining > 0 && isOctalDigit(input.charAt(index + 1))) {
/* 42 */       int next = index + 1;
/* 43 */       int next2 = index + 2;
/* 44 */       int next3 = index + 3;
/*    */ 
/*    */       
/* 47 */       builder.append(input.charAt(next));
/*    */       
/* 49 */       if (remaining > 1 && isOctalDigit(input.charAt(next2))) {
/* 50 */         builder.append(input.charAt(next2));
/* 51 */         if (remaining > 2 && isZeroToThree(input.charAt(next)) && isOctalDigit(input.charAt(next3))) {
/* 52 */           builder.append(input.charAt(next3));
/*    */         }
/*    */       } 
/*    */       
/* 56 */       out.write(Integer.parseInt(builder.toString(), 8));
/* 57 */       return 1 + builder.length();
/*    */     } 
/* 59 */     return 0;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private boolean isOctalDigit(char ch) {
/* 68 */     return (ch >= '0' && ch <= '7');
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private boolean isZeroToThree(char ch) {
/* 77 */     return (ch >= '0' && ch <= '3');
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\text\translate\OctalUnescaper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */