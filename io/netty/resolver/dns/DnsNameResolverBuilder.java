/*     */ package io.netty.resolver.dns;
/*     */ 
/*     */ import io.netty.channel.ChannelFactory;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.ReflectiveChannelFactory;
/*     */ import io.netty.channel.socket.DatagramChannel;
/*     */ import io.netty.channel.socket.InternetProtocolFamily;
/*     */ import io.netty.resolver.HostsFileEntriesResolver;
/*     */ import io.netty.resolver.ResolvedAddressTypes;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class DnsNameResolverBuilder
/*     */ {
/*     */   private EventLoop eventLoop;
/*     */   private ChannelFactory<? extends DatagramChannel> channelFactory;
/*     */   private DnsCache resolveCache;
/*     */   private DnsCache authoritativeDnsServerCache;
/*     */   private Integer minTtl;
/*     */   private Integer maxTtl;
/*     */   private Integer negativeTtl;
/*  47 */   private long queryTimeoutMillis = 5000L;
/*  48 */   private ResolvedAddressTypes resolvedAddressTypes = DnsNameResolver.DEFAULT_RESOLVE_ADDRESS_TYPES;
/*     */   private boolean recursionDesired = true;
/*  50 */   private int maxQueriesPerResolve = 16;
/*     */   private boolean traceEnabled;
/*  52 */   private int maxPayloadSize = 4096;
/*     */   private boolean optResourceEnabled = true;
/*  54 */   private HostsFileEntriesResolver hostsFileEntriesResolver = HostsFileEntriesResolver.DEFAULT;
/*  55 */   private DnsServerAddressStreamProvider dnsServerAddressStreamProvider = DnsServerAddressStreamProviders.platformDefault();
/*  56 */   private DnsQueryLifecycleObserverFactory dnsQueryLifecycleObserverFactory = NoopDnsQueryLifecycleObserverFactory.INSTANCE;
/*     */   
/*     */   private String[] searchDomains;
/*  59 */   private int ndots = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean decodeIdn = true;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DnsNameResolverBuilder() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DnsNameResolverBuilder(EventLoop eventLoop) {
/*  75 */     eventLoop(eventLoop);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DnsNameResolverBuilder eventLoop(EventLoop eventLoop) {
/*  85 */     this.eventLoop = eventLoop;
/*  86 */     return this;
/*     */   }
/*     */   
/*     */   protected ChannelFactory<? extends DatagramChannel> channelFactory() {
/*  90 */     return this.channelFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DnsNameResolverBuilder channelFactory(ChannelFactory<? extends DatagramChannel> channelFactory) {
/* 100 */     this.channelFactory = channelFactory;
/* 101 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DnsNameResolverBuilder channelType(Class<? extends DatagramChannel> channelType) {
/* 112 */     return channelFactory((ChannelFactory<? extends DatagramChannel>)new ReflectiveChannelFactory(channelType));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DnsNameResolverBuilder resolveCache(DnsCache resolveCache) {
/* 122 */     this.resolveCache = resolveCache;
/* 123 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DnsNameResolverBuilder dnsQueryLifecycleObserverFactory(DnsQueryLifecycleObserverFactory lifecycleObserverFactory) {
/* 133 */     this.dnsQueryLifecycleObserverFactory = (DnsQueryLifecycleObserverFactory)ObjectUtil.checkNotNull(lifecycleObserverFactory, "lifecycleObserverFactory");
/* 134 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DnsNameResolverBuilder authoritativeDnsServerCache(DnsCache authoritativeDnsServerCache) {
/* 144 */     this.authoritativeDnsServerCache = authoritativeDnsServerCache;
/* 145 */     return this;
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
/*     */   public DnsNameResolverBuilder ttl(int minTtl, int maxTtl) {
/* 161 */     this.maxTtl = Integer.valueOf(maxTtl);
/* 162 */     this.minTtl = Integer.valueOf(minTtl);
/* 163 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DnsNameResolverBuilder negativeTtl(int negativeTtl) {
/* 173 */     this.negativeTtl = Integer.valueOf(negativeTtl);
/* 174 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DnsNameResolverBuilder queryTimeoutMillis(long queryTimeoutMillis) {
/* 184 */     this.queryTimeoutMillis = queryTimeoutMillis;
/* 185 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ResolvedAddressTypes computeResolvedAddressTypes(InternetProtocolFamily... internetProtocolFamilies) {
/* 196 */     if (internetProtocolFamilies == null || internetProtocolFamilies.length == 0) {
/* 197 */       return DnsNameResolver.DEFAULT_RESOLVE_ADDRESS_TYPES;
/*     */     }
/* 199 */     if (internetProtocolFamilies.length > 2) {
/* 200 */       throw new IllegalArgumentException("No more than 2 InternetProtocolFamilies");
/*     */     }
/*     */     
/* 203 */     switch (internetProtocolFamilies[0]) {
/*     */       case IPv4:
/* 205 */         return (internetProtocolFamilies.length >= 2 && internetProtocolFamilies[1] == InternetProtocolFamily.IPv6) ? ResolvedAddressTypes.IPV4_PREFERRED : ResolvedAddressTypes.IPV4_ONLY;
/*     */ 
/*     */       
/*     */       case IPv6:
/* 209 */         return (internetProtocolFamilies.length >= 2 && internetProtocolFamilies[1] == InternetProtocolFamily.IPv4) ? ResolvedAddressTypes.IPV6_PREFERRED : ResolvedAddressTypes.IPV6_ONLY;
/*     */     } 
/*     */ 
/*     */     
/* 213 */     throw new IllegalArgumentException("Couldn't resolve ResolvedAddressTypes from InternetProtocolFamily array");
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
/*     */   public DnsNameResolverBuilder resolvedAddressTypes(ResolvedAddressTypes resolvedAddressTypes) {
/* 227 */     this.resolvedAddressTypes = resolvedAddressTypes;
/* 228 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DnsNameResolverBuilder recursionDesired(boolean recursionDesired) {
/* 238 */     this.recursionDesired = recursionDesired;
/* 239 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DnsNameResolverBuilder maxQueriesPerResolve(int maxQueriesPerResolve) {
/* 249 */     this.maxQueriesPerResolve = maxQueriesPerResolve;
/* 250 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DnsNameResolverBuilder traceEnabled(boolean traceEnabled) {
/* 261 */     this.traceEnabled = traceEnabled;
/* 262 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DnsNameResolverBuilder maxPayloadSize(int maxPayloadSize) {
/* 272 */     this.maxPayloadSize = maxPayloadSize;
/* 273 */     return this;
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
/*     */   public DnsNameResolverBuilder optResourceEnabled(boolean optResourceEnabled) {
/* 285 */     this.optResourceEnabled = optResourceEnabled;
/* 286 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DnsNameResolverBuilder hostsFileEntriesResolver(HostsFileEntriesResolver hostsFileEntriesResolver) {
/* 295 */     this.hostsFileEntriesResolver = hostsFileEntriesResolver;
/* 296 */     return this;
/*     */   }
/*     */   
/*     */   protected DnsServerAddressStreamProvider nameServerProvider() {
/* 300 */     return this.dnsServerAddressStreamProvider;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DnsNameResolverBuilder nameServerProvider(DnsServerAddressStreamProvider dnsServerAddressStreamProvider) {
/* 309 */     this
/* 310 */       .dnsServerAddressStreamProvider = (DnsServerAddressStreamProvider)ObjectUtil.checkNotNull(dnsServerAddressStreamProvider, "dnsServerAddressStreamProvider");
/* 311 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DnsNameResolverBuilder searchDomains(Iterable<String> searchDomains) {
/* 321 */     ObjectUtil.checkNotNull(searchDomains, "searchDomains");
/*     */     
/* 323 */     List<String> list = new ArrayList<String>(4);
/*     */     
/* 325 */     for (String f : searchDomains) {
/* 326 */       if (f == null) {
/*     */         break;
/*     */       }
/*     */ 
/*     */       
/* 331 */       if (list.contains(f)) {
/*     */         continue;
/*     */       }
/*     */       
/* 335 */       list.add(f);
/*     */     } 
/*     */     
/* 338 */     this.searchDomains = list.<String>toArray(new String[list.size()]);
/* 339 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DnsNameResolverBuilder ndots(int ndots) {
/* 350 */     this.ndots = ndots;
/* 351 */     return this;
/*     */   }
/*     */   
/*     */   private DnsCache newCache() {
/* 355 */     return new DefaultDnsCache(ObjectUtil.intValue(this.minTtl, 0), ObjectUtil.intValue(this.maxTtl, 2147483647), ObjectUtil.intValue(this.negativeTtl, 0));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DnsNameResolverBuilder decodeIdn(boolean decodeIdn) {
/* 366 */     this.decodeIdn = decodeIdn;
/* 367 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DnsNameResolver build() {
/* 376 */     if (this.eventLoop == null) {
/* 377 */       throw new IllegalStateException("eventLoop should be specified to build a DnsNameResolver.");
/*     */     }
/*     */     
/* 380 */     if (this.resolveCache != null && (this.minTtl != null || this.maxTtl != null || this.negativeTtl != null)) {
/* 381 */       throw new IllegalStateException("resolveCache and TTLs are mutually exclusive");
/*     */     }
/*     */     
/* 384 */     if (this.authoritativeDnsServerCache != null && (this.minTtl != null || this.maxTtl != null || this.negativeTtl != null)) {
/* 385 */       throw new IllegalStateException("authoritativeDnsServerCache and TTLs are mutually exclusive");
/*     */     }
/*     */     
/* 388 */     DnsCache resolveCache = (this.resolveCache != null) ? this.resolveCache : newCache();
/*     */     
/* 390 */     DnsCache authoritativeDnsServerCache = (this.authoritativeDnsServerCache != null) ? this.authoritativeDnsServerCache : newCache();
/* 391 */     return new DnsNameResolver(this.eventLoop, this.channelFactory, resolveCache, authoritativeDnsServerCache, this.dnsQueryLifecycleObserverFactory, this.queryTimeoutMillis, this.resolvedAddressTypes, this.recursionDesired, this.maxQueriesPerResolve, this.traceEnabled, this.maxPayloadSize, this.optResourceEnabled, this.hostsFileEntriesResolver, this.dnsServerAddressStreamProvider, this.searchDomains, this.ndots, this.decodeIdn);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DnsNameResolverBuilder copy() {
/* 417 */     DnsNameResolverBuilder copiedBuilder = new DnsNameResolverBuilder();
/*     */     
/* 419 */     if (this.eventLoop != null) {
/* 420 */       copiedBuilder.eventLoop(this.eventLoop);
/*     */     }
/*     */     
/* 423 */     if (this.channelFactory != null) {
/* 424 */       copiedBuilder.channelFactory(this.channelFactory);
/*     */     }
/*     */     
/* 427 */     if (this.resolveCache != null) {
/* 428 */       copiedBuilder.resolveCache(this.resolveCache);
/*     */     }
/*     */     
/* 431 */     if (this.maxTtl != null && this.minTtl != null) {
/* 432 */       copiedBuilder.ttl(this.minTtl.intValue(), this.maxTtl.intValue());
/*     */     }
/*     */     
/* 435 */     if (this.negativeTtl != null) {
/* 436 */       copiedBuilder.negativeTtl(this.negativeTtl.intValue());
/*     */     }
/*     */     
/* 439 */     if (this.authoritativeDnsServerCache != null) {
/* 440 */       copiedBuilder.authoritativeDnsServerCache(this.authoritativeDnsServerCache);
/*     */     }
/*     */     
/* 443 */     if (this.dnsQueryLifecycleObserverFactory != null) {
/* 444 */       copiedBuilder.dnsQueryLifecycleObserverFactory(this.dnsQueryLifecycleObserverFactory);
/*     */     }
/*     */     
/* 447 */     copiedBuilder.queryTimeoutMillis(this.queryTimeoutMillis);
/* 448 */     copiedBuilder.resolvedAddressTypes(this.resolvedAddressTypes);
/* 449 */     copiedBuilder.recursionDesired(this.recursionDesired);
/* 450 */     copiedBuilder.maxQueriesPerResolve(this.maxQueriesPerResolve);
/* 451 */     copiedBuilder.traceEnabled(this.traceEnabled);
/* 452 */     copiedBuilder.maxPayloadSize(this.maxPayloadSize);
/* 453 */     copiedBuilder.optResourceEnabled(this.optResourceEnabled);
/* 454 */     copiedBuilder.hostsFileEntriesResolver(this.hostsFileEntriesResolver);
/*     */     
/* 456 */     if (this.dnsServerAddressStreamProvider != null) {
/* 457 */       copiedBuilder.nameServerProvider(this.dnsServerAddressStreamProvider);
/*     */     }
/*     */     
/* 460 */     if (this.searchDomains != null) {
/* 461 */       copiedBuilder.searchDomains(Arrays.asList(this.searchDomains));
/*     */     }
/*     */     
/* 464 */     copiedBuilder.ndots(this.ndots);
/* 465 */     copiedBuilder.decodeIdn(this.decodeIdn);
/*     */     
/* 467 */     return copiedBuilder;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\dns\DnsNameResolverBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */