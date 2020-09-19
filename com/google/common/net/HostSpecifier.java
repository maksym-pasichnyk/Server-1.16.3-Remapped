/*     */ package com.google.common.net;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.net.InetAddress;
/*     */ import java.text.ParseException;
/*     */ import javax.annotation.Nullable;
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
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public final class HostSpecifier
/*     */ {
/*     */   private final String canonicalForm;
/*     */   
/*     */   private HostSpecifier(String canonicalForm) {
/*  51 */     this.canonicalForm = canonicalForm;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HostSpecifier fromValid(String specifier) {
/*  72 */     HostAndPort parsedHost = HostAndPort.fromString(specifier);
/*  73 */     Preconditions.checkArgument(!parsedHost.hasPort());
/*  74 */     String host = parsedHost.getHost();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  80 */     InetAddress addr = null;
/*     */     try {
/*  82 */       addr = InetAddresses.forString(host);
/*  83 */     } catch (IllegalArgumentException illegalArgumentException) {}
/*     */ 
/*     */ 
/*     */     
/*  87 */     if (addr != null) {
/*  88 */       return new HostSpecifier(InetAddresses.toUriString(addr));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  94 */     InternetDomainName domain = InternetDomainName.from(host);
/*     */     
/*  96 */     if (domain.hasPublicSuffix()) {
/*  97 */       return new HostSpecifier(domain.toString());
/*     */     }
/*     */     
/* 100 */     throw new IllegalArgumentException("Domain name does not have a recognized public suffix: " + host);
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
/*     */   public static HostSpecifier from(String specifier) throws ParseException {
/*     */     try {
/* 113 */       return fromValid(specifier);
/* 114 */     } catch (IllegalArgumentException e) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 119 */       ParseException parseException = new ParseException("Invalid host specifier: " + specifier, 0);
/* 120 */       parseException.initCause(e);
/* 121 */       throw parseException;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isValid(String specifier) {
/*     */     try {
/* 131 */       fromValid(specifier);
/* 132 */       return true;
/* 133 */     } catch (IllegalArgumentException e) {
/* 134 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/* 140 */     if (this == other) {
/* 141 */       return true;
/*     */     }
/*     */     
/* 144 */     if (other instanceof HostSpecifier) {
/* 145 */       HostSpecifier that = (HostSpecifier)other;
/* 146 */       return this.canonicalForm.equals(that.canonicalForm);
/*     */     } 
/*     */     
/* 149 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 154 */     return this.canonicalForm.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 165 */     return this.canonicalForm;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\net\HostSpecifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */