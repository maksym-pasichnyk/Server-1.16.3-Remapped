/*    */ package io.netty.resolver;
/*    */ 
/*    */ import java.net.Inet4Address;
/*    */ import java.net.Inet6Address;
/*    */ import java.net.InetAddress;
/*    */ import java.util.Locale;
/*    */ import java.util.Map;
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
/*    */ public final class DefaultHostsFileEntriesResolver
/*    */   implements HostsFileEntriesResolver
/*    */ {
/*    */   private final Map<String, Inet4Address> inet4Entries;
/*    */   private final Map<String, Inet6Address> inet6Entries;
/*    */   
/*    */   public DefaultHostsFileEntriesResolver() {
/* 36 */     this(HostsFileParser.parseSilently());
/*    */   }
/*    */ 
/*    */   
/*    */   DefaultHostsFileEntriesResolver(HostsFileEntries entries) {
/* 41 */     this.inet4Entries = entries.inet4Entries();
/* 42 */     this.inet6Entries = entries.inet6Entries();
/*    */   }
/*    */   public InetAddress address(String inetHost, ResolvedAddressTypes resolvedAddressTypes) {
/*    */     Inet4Address inet4Address;
/*    */     Inet6Address inet6Address;
/* 47 */     String normalized = normalize(inetHost);
/* 48 */     switch (resolvedAddressTypes) {
/*    */       case IPV4_ONLY:
/* 50 */         return this.inet4Entries.get(normalized);
/*    */       case IPV6_ONLY:
/* 52 */         return this.inet6Entries.get(normalized);
/*    */       case IPV4_PREFERRED:
/* 54 */         inet4Address = this.inet4Entries.get(normalized);
/* 55 */         return (inet4Address != null) ? inet4Address : this.inet6Entries.get(normalized);
/*    */       case IPV6_PREFERRED:
/* 57 */         inet6Address = this.inet6Entries.get(normalized);
/* 58 */         return (inet6Address != null) ? inet6Address : this.inet4Entries.get(normalized);
/*    */     } 
/* 60 */     throw new IllegalArgumentException("Unknown ResolvedAddressTypes " + resolvedAddressTypes);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   String normalize(String inetHost) {
/* 66 */     return inetHost.toLowerCase(Locale.ENGLISH);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\DefaultHostsFileEntriesResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */