/*    */ package org.apache.commons.lang3.text.translate;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.Writer;
/*    */ import org.apache.commons.lang3.ArrayUtils;
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
/*    */ public class AggregateTranslator
/*    */   extends CharSequenceTranslator
/*    */ {
/*    */   private final CharSequenceTranslator[] translators;
/*    */   
/*    */   public AggregateTranslator(CharSequenceTranslator... translators) {
/* 40 */     this.translators = (CharSequenceTranslator[])ArrayUtils.clone((Object[])translators);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int translate(CharSequence input, int index, Writer out) throws IOException {
/* 50 */     for (CharSequenceTranslator translator : this.translators) {
/* 51 */       int consumed = translator.translate(input, index, out);
/* 52 */       if (consumed != 0) {
/* 53 */         return consumed;
/*    */       }
/*    */     } 
/* 56 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\text\translate\AggregateTranslator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */