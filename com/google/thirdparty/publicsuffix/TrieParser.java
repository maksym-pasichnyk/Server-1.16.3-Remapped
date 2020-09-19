/*     */ package com.google.thirdparty.publicsuffix;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ final class TrieParser
/*     */ {
/*  30 */   private static final Joiner PREFIX_JOINER = Joiner.on("");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static ImmutableMap<String, PublicSuffixType> parseTrie(CharSequence encoded) {
/*  37 */     ImmutableMap.Builder<String, PublicSuffixType> builder = ImmutableMap.builder();
/*  38 */     int encodedLen = encoded.length();
/*  39 */     int idx = 0;
/*  40 */     while (idx < encodedLen) {
/*  41 */       idx += 
/*  42 */         doParseTrieToBuilder(
/*  43 */           Lists.newLinkedList(), encoded.subSequence(idx, encodedLen), builder);
/*     */     }
/*  45 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int doParseTrieToBuilder(List<CharSequence> stack, CharSequence encoded, ImmutableMap.Builder<String, PublicSuffixType> builder) {
/*  62 */     int encodedLen = encoded.length();
/*  63 */     int idx = 0;
/*  64 */     char c = Character.MIN_VALUE;
/*     */ 
/*     */     
/*  67 */     for (; idx < encodedLen; idx++) {
/*  68 */       c = encoded.charAt(idx);
/*  69 */       if (c == '&' || c == '?' || c == '!' || c == ':' || c == ',') {
/*     */         break;
/*     */       }
/*     */     } 
/*     */     
/*  74 */     stack.add(0, reverse(encoded.subSequence(0, idx)));
/*     */     
/*  76 */     if (c == '!' || c == '?' || c == ':' || c == ',') {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  81 */       String domain = PREFIX_JOINER.join(stack);
/*  82 */       if (domain.length() > 0) {
/*  83 */         builder.put(domain, PublicSuffixType.fromCode(c));
/*     */       }
/*     */     } 
/*  86 */     idx++;
/*     */     
/*  88 */     if (c != '?' && c != ',') {
/*  89 */       while (idx < encodedLen) {
/*     */         
/*  91 */         idx += doParseTrieToBuilder(stack, encoded.subSequence(idx, encodedLen), builder);
/*  92 */         if (encoded.charAt(idx) == '?' || encoded.charAt(idx) == ',') {
/*     */           
/*  94 */           idx++;
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/*  99 */     stack.remove(0);
/* 100 */     return idx;
/*     */   }
/*     */   
/*     */   private static CharSequence reverse(CharSequence s) {
/* 104 */     return (new StringBuilder(s)).reverse();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\thirdparty\publicsuffix\TrieParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */