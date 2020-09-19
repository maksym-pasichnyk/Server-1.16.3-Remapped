/*    */ package io.netty.resolver.dns;
/*    */ 
/*    */ import io.netty.channel.ChannelFactory;
/*    */ import io.netty.channel.EventLoop;
/*    */ import io.netty.channel.socket.DatagramChannel;
/*    */ import io.netty.resolver.AddressResolver;
/*    */ import io.netty.resolver.NameResolver;
/*    */ import io.netty.resolver.RoundRobinInetAddressResolver;
/*    */ import io.netty.util.concurrent.EventExecutor;
/*    */ import java.net.InetAddress;
/*    */ import java.net.InetSocketAddress;
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
/*    */ public class RoundRobinDnsAddressResolverGroup
/*    */   extends DnsAddressResolverGroup
/*    */ {
/*    */   public RoundRobinDnsAddressResolverGroup(DnsNameResolverBuilder dnsResolverBuilder) {
/* 40 */     super(dnsResolverBuilder);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public RoundRobinDnsAddressResolverGroup(Class<? extends DatagramChannel> channelType, DnsServerAddressStreamProvider nameServerProvider) {
/* 46 */     super(channelType, nameServerProvider);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public RoundRobinDnsAddressResolverGroup(ChannelFactory<? extends DatagramChannel> channelFactory, DnsServerAddressStreamProvider nameServerProvider) {
/* 52 */     super(channelFactory, nameServerProvider);
/*    */   }
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
/*    */   protected final AddressResolver<InetSocketAddress> newAddressResolver(EventLoop eventLoop, NameResolver<InetAddress> resolver) throws Exception {
/* 66 */     return (new RoundRobinInetAddressResolver((EventExecutor)eventLoop, resolver)).asAddressResolver();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\dns\RoundRobinDnsAddressResolverGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */