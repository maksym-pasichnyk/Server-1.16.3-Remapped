/*    */ package io.netty.resolver.dns;
/*    */ 
/*    */ import io.netty.util.internal.PlatformDependent;
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
/*    */ public final class DnsServerAddressStreamProviders
/*    */ {
/* 31 */   private static final DnsServerAddressStreamProvider DEFAULT_DNS_SERVER_ADDRESS_STREAM_PROVIDER = PlatformDependent.isWindows() ? DefaultDnsServerAddressStreamProvider.INSTANCE : 
/* 32 */     UnixResolverDnsServerAddressStreamProvider.parseSilently();
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
/*    */   public static DnsServerAddressStreamProvider platformDefault() {
/* 45 */     return DEFAULT_DNS_SERVER_ADDRESS_STREAM_PROVIDER;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\dns\DnsServerAddressStreamProviders.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */