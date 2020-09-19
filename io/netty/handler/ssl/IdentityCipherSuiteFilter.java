/*    */ package io.netty.handler.ssl;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Set;
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
/*    */ public final class IdentityCipherSuiteFilter
/*    */   implements CipherSuiteFilter
/*    */ {
/* 30 */   public static final IdentityCipherSuiteFilter INSTANCE = new IdentityCipherSuiteFilter(true);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   public static final IdentityCipherSuiteFilter INSTANCE_DEFAULTING_TO_SUPPORTED_CIPHERS = new IdentityCipherSuiteFilter(false);
/*    */   
/*    */   private final boolean defaultToDefaultCiphers;
/*    */ 
/*    */   
/*    */   private IdentityCipherSuiteFilter(boolean defaultToDefaultCiphers) {
/* 41 */     this.defaultToDefaultCiphers = defaultToDefaultCiphers;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String[] filterCipherSuites(Iterable<String> ciphers, List<String> defaultCiphers, Set<String> supportedCiphers) {
/* 47 */     if (ciphers == null) {
/* 48 */       return this.defaultToDefaultCiphers ? defaultCiphers
/* 49 */         .<String>toArray(new String[defaultCiphers.size()]) : supportedCiphers
/* 50 */         .<String>toArray(new String[supportedCiphers.size()]);
/*    */     }
/* 52 */     List<String> newCiphers = new ArrayList<String>(supportedCiphers.size());
/* 53 */     for (String c : ciphers) {
/* 54 */       if (c == null) {
/*    */         break;
/*    */       }
/* 57 */       newCiphers.add(c);
/*    */     } 
/* 59 */     return newCiphers.<String>toArray(new String[newCiphers.size()]);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\ssl\IdentityCipherSuiteFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */