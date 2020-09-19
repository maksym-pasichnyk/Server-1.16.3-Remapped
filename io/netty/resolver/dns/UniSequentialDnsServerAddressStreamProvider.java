/*    */ package io.netty.resolver.dns;
/*    */ 
/*    */ import io.netty.util.internal.ObjectUtil;
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
/*    */ abstract class UniSequentialDnsServerAddressStreamProvider
/*    */   implements DnsServerAddressStreamProvider
/*    */ {
/*    */   private final DnsServerAddresses addresses;
/*    */   
/*    */   UniSequentialDnsServerAddressStreamProvider(DnsServerAddresses addresses) {
/* 27 */     this.addresses = (DnsServerAddresses)ObjectUtil.checkNotNull(addresses, "addresses");
/*    */   }
/*    */ 
/*    */   
/*    */   public final DnsServerAddressStream nameServerAddressStream(String hostname) {
/* 32 */     return this.addresses.stream();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\dns\UniSequentialDnsServerAddressStreamProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */