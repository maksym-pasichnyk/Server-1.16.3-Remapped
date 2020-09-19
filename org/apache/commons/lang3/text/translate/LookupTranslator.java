/*    */ package org.apache.commons.lang3.text.translate;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.Writer;
/*    */ import java.util.HashMap;
/*    */ import java.util.HashSet;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LookupTranslator
/*    */   extends CharSequenceTranslator
/*    */ {
/* 46 */   private final HashMap<String, String> lookupMap = new HashMap<String, String>();
/* 47 */   private final HashSet<Character> prefixSet = new HashSet<Character>(); public LookupTranslator(CharSequence[]... lookup) {
/* 48 */     int _shortest = Integer.MAX_VALUE;
/* 49 */     int _longest = 0;
/* 50 */     if (lookup != null) {
/* 51 */       for (CharSequence[] seq : lookup) {
/* 52 */         this.lookupMap.put(seq[0].toString(), seq[1].toString());
/* 53 */         this.prefixSet.add(Character.valueOf(seq[0].charAt(0)));
/* 54 */         int sz = seq[0].length();
/* 55 */         if (sz < _shortest) {
/* 56 */           _shortest = sz;
/*    */         }
/* 58 */         if (sz > _longest) {
/* 59 */           _longest = sz;
/*    */         }
/*    */       } 
/*    */     }
/* 63 */     this.shortest = _shortest;
/* 64 */     this.longest = _longest;
/*    */   }
/*    */ 
/*    */   
/*    */   private final int shortest;
/*    */   
/*    */   private final int longest;
/*    */   
/*    */   public int translate(CharSequence input, int index, Writer out) throws IOException {
/* 73 */     if (this.prefixSet.contains(Character.valueOf(input.charAt(index)))) {
/* 74 */       int max = this.longest;
/* 75 */       if (index + this.longest > input.length()) {
/* 76 */         max = input.length() - index;
/*    */       }
/*    */       
/* 79 */       for (int i = max; i >= this.shortest; i--) {
/* 80 */         CharSequence subSeq = input.subSequence(index, index + i);
/* 81 */         String result = this.lookupMap.get(subSeq.toString());
/*    */         
/* 83 */         if (result != null) {
/* 84 */           out.write(result);
/* 85 */           return i;
/*    */         } 
/*    */       } 
/*    */     } 
/* 89 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\text\translate\LookupTranslator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */