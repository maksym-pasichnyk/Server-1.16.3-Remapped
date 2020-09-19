/*    */ package com.google.thirdparty.publicsuffix;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
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
/*    */ @GwtCompatible
/*    */ enum PublicSuffixType
/*    */ {
/* 26 */   PRIVATE(':', ','),
/*    */   
/* 28 */   ICANN('!', '?');
/*    */ 
/*    */   
/*    */   private final char innerNodeCode;
/*    */   
/*    */   private final char leafNodeCode;
/*    */ 
/*    */   
/*    */   PublicSuffixType(char innerNodeCode, char leafNodeCode) {
/* 37 */     this.innerNodeCode = innerNodeCode;
/* 38 */     this.leafNodeCode = leafNodeCode;
/*    */   }
/*    */   
/*    */   char getLeafNodeCode() {
/* 42 */     return this.leafNodeCode;
/*    */   }
/*    */   
/*    */   char getInnerNodeCode() {
/* 46 */     return this.innerNodeCode;
/*    */   }
/*    */ 
/*    */   
/*    */   static PublicSuffixType fromCode(char code) {
/* 51 */     for (PublicSuffixType value : values()) {
/* 52 */       if (value.getInnerNodeCode() == code || value.getLeafNodeCode() == code) {
/* 53 */         return value;
/*    */       }
/*    */     } 
/* 56 */     throw new IllegalArgumentException("No enum corresponding to given code: " + code);
/*    */   }
/*    */   
/*    */   static PublicSuffixType fromIsPrivate(boolean isPrivate) {
/* 60 */     return isPrivate ? PRIVATE : ICANN;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\thirdparty\publicsuffix\PublicSuffixType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */