/*     */ package io.netty.resolver.dns;
/*     */ 
/*     */ import io.netty.channel.ChannelFactory;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.socket.DatagramChannel;
/*     */ import io.netty.resolver.AddressResolver;
/*     */ import io.netty.resolver.AddressResolverGroup;
/*     */ import io.netty.resolver.InetSocketAddressResolver;
/*     */ import io.netty.resolver.NameResolver;
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.concurrent.Promise;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.ConcurrentMap;
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
/*     */ public class DnsAddressResolverGroup
/*     */   extends AddressResolverGroup<InetSocketAddress>
/*     */ {
/*     */   private final DnsNameResolverBuilder dnsResolverBuilder;
/*  47 */   private final ConcurrentMap<String, Promise<InetAddress>> resolvesInProgress = PlatformDependent.newConcurrentHashMap();
/*  48 */   private final ConcurrentMap<String, Promise<List<InetAddress>>> resolveAllsInProgress = PlatformDependent.newConcurrentHashMap();
/*     */   
/*     */   public DnsAddressResolverGroup(DnsNameResolverBuilder dnsResolverBuilder) {
/*  51 */     this.dnsResolverBuilder = dnsResolverBuilder.copy();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DnsAddressResolverGroup(Class<? extends DatagramChannel> channelType, DnsServerAddressStreamProvider nameServerProvider) {
/*  57 */     this(new DnsNameResolverBuilder());
/*  58 */     this.dnsResolverBuilder.channelType(channelType).nameServerProvider(nameServerProvider);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DnsAddressResolverGroup(ChannelFactory<? extends DatagramChannel> channelFactory, DnsServerAddressStreamProvider nameServerProvider) {
/*  64 */     this(new DnsNameResolverBuilder());
/*  65 */     this.dnsResolverBuilder.channelFactory(channelFactory).nameServerProvider(nameServerProvider);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final AddressResolver<InetSocketAddress> newResolver(EventExecutor executor) throws Exception {
/*  71 */     if (!(executor instanceof EventLoop)) {
/*  72 */       throw new IllegalStateException("unsupported executor type: " + 
/*  73 */           StringUtil.simpleClassName(executor) + " (expected: " + 
/*  74 */           StringUtil.simpleClassName(EventLoop.class));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  79 */     return newResolver((EventLoop)executor, this.dnsResolverBuilder
/*  80 */         .channelFactory(), this.dnsResolverBuilder
/*  81 */         .nameServerProvider());
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
/*     */   @Deprecated
/*     */   protected AddressResolver<InetSocketAddress> newResolver(EventLoop eventLoop, ChannelFactory<? extends DatagramChannel> channelFactory, DnsServerAddressStreamProvider nameServerProvider) throws Exception {
/*  94 */     NameResolver<InetAddress> resolver = new InflightNameResolver<InetAddress>((EventExecutor)eventLoop, newNameResolver(eventLoop, channelFactory, nameServerProvider), this.resolvesInProgress, this.resolveAllsInProgress);
/*     */ 
/*     */ 
/*     */     
/*  98 */     return newAddressResolver(eventLoop, resolver);
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
/*     */   protected NameResolver<InetAddress> newNameResolver(EventLoop eventLoop, ChannelFactory<? extends DatagramChannel> channelFactory, DnsServerAddressStreamProvider nameServerProvider) throws Exception {
/* 111 */     return (NameResolver<InetAddress>)this.dnsResolverBuilder.eventLoop(eventLoop)
/* 112 */       .channelFactory(channelFactory)
/* 113 */       .nameServerProvider(nameServerProvider)
/* 114 */       .build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AddressResolver<InetSocketAddress> newAddressResolver(EventLoop eventLoop, NameResolver<InetAddress> resolver) throws Exception {
/* 124 */     return (AddressResolver<InetSocketAddress>)new InetSocketAddressResolver((EventExecutor)eventLoop, resolver);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\dns\DnsAddressResolverGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */