/*    */ package io.netty.handler.codec.smtp;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
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
/*    */ final class SmtpUtils
/*    */ {
/*    */   static List<CharSequence> toUnmodifiableList(CharSequence... sequences) {
/* 25 */     if (sequences == null || sequences.length == 0) {
/* 26 */       return Collections.emptyList();
/*    */     }
/* 28 */     return Collections.unmodifiableList(Arrays.asList(sequences));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\smtp\SmtpUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */