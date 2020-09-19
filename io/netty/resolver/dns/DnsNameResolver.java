/*     */ package io.netty.resolver.dns;
/*     */ 
/*     */ import io.netty.bootstrap.Bootstrap;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.AddressedEnvelope;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelFactory;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelInboundHandlerAdapter;
/*     */ import io.netty.channel.ChannelInitializer;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.EventLoopGroup;
/*     */ import io.netty.channel.FixedRecvByteBufAllocator;
/*     */ import io.netty.channel.RecvByteBufAllocator;
/*     */ import io.netty.channel.socket.DatagramChannel;
/*     */ import io.netty.channel.socket.InternetProtocolFamily;
/*     */ import io.netty.handler.codec.dns.DatagramDnsQueryEncoder;
/*     */ import io.netty.handler.codec.dns.DatagramDnsResponse;
/*     */ import io.netty.handler.codec.dns.DatagramDnsResponseDecoder;
/*     */ import io.netty.handler.codec.dns.DefaultDnsRawRecord;
/*     */ import io.netty.handler.codec.dns.DnsQuestion;
/*     */ import io.netty.handler.codec.dns.DnsRecord;
/*     */ import io.netty.handler.codec.dns.DnsRecordType;
/*     */ import io.netty.handler.codec.dns.DnsResponse;
/*     */ import io.netty.resolver.HostsFileEntriesResolver;
/*     */ import io.netty.resolver.InetNameResolver;
/*     */ import io.netty.resolver.ResolvedAddressTypes;
/*     */ import io.netty.util.NetUtil;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.concurrent.FastThreadLocal;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.FutureListener;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import io.netty.util.concurrent.Promise;
/*     */ import io.netty.util.internal.EmptyArrays;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.IDN;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
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
/*     */ public class DnsNameResolver
/*     */   extends InetNameResolver
/*     */ {
/*     */   static {
/*     */     String[] searchDomains;
/*     */     int ndots;
/*     */   }
/*     */   
/*     */   static {
/*  84 */     logger = InternalLoggerFactory.getInstance(DnsNameResolver.class);
/*     */ 
/*     */     
/*  87 */     EMPTY_ADDITIONALS = new DnsRecord[0];
/*  88 */     IPV4_ONLY_RESOLVED_RECORD_TYPES = new DnsRecordType[] { DnsRecordType.A };
/*     */     
/*  90 */     IPV4_ONLY_RESOLVED_PROTOCOL_FAMILIES = new InternetProtocolFamily[] { InternetProtocolFamily.IPv4 };
/*     */     
/*  92 */     IPV4_PREFERRED_RESOLVED_RECORD_TYPES = new DnsRecordType[] { DnsRecordType.A, DnsRecordType.AAAA };
/*     */     
/*  94 */     IPV4_PREFERRED_RESOLVED_PROTOCOL_FAMILIES = new InternetProtocolFamily[] { InternetProtocolFamily.IPv4, InternetProtocolFamily.IPv6 };
/*     */     
/*  96 */     IPV6_ONLY_RESOLVED_RECORD_TYPES = new DnsRecordType[] { DnsRecordType.AAAA };
/*     */     
/*  98 */     IPV6_ONLY_RESOLVED_PROTOCOL_FAMILIES = new InternetProtocolFamily[] { InternetProtocolFamily.IPv6 };
/*     */     
/* 100 */     IPV6_PREFERRED_RESOLVED_RECORD_TYPES = new DnsRecordType[] { DnsRecordType.AAAA, DnsRecordType.A };
/*     */     
/* 102 */     IPV6_PREFERRED_RESOLVED_PROTOCOL_FAMILIES = new InternetProtocolFamily[] { InternetProtocolFamily.IPv6, InternetProtocolFamily.IPv4 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 110 */     if (NetUtil.isIpV4StackPreferred()) {
/* 111 */       DEFAULT_RESOLVE_ADDRESS_TYPES = ResolvedAddressTypes.IPV4_ONLY;
/* 112 */       LOCALHOST_ADDRESS = NetUtil.LOCALHOST4;
/*     */     }
/* 114 */     else if (NetUtil.isIpV6AddressesPreferred()) {
/* 115 */       DEFAULT_RESOLVE_ADDRESS_TYPES = ResolvedAddressTypes.IPV6_PREFERRED;
/* 116 */       LOCALHOST_ADDRESS = NetUtil.LOCALHOST6;
/*     */     } else {
/* 118 */       DEFAULT_RESOLVE_ADDRESS_TYPES = ResolvedAddressTypes.IPV4_PREFERRED;
/* 119 */       LOCALHOST_ADDRESS = NetUtil.LOCALHOST4;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 127 */       Class<?> configClass = Class.forName("sun.net.dns.ResolverConfiguration");
/* 128 */       Method open = configClass.getMethod("open", new Class[0]);
/* 129 */       Method nameservers = configClass.getMethod("searchlist", new Class[0]);
/* 130 */       Object instance = open.invoke(null, new Object[0]);
/*     */ 
/*     */       
/* 133 */       List<String> list = (List<String>)nameservers.invoke(instance, new Object[0]);
/* 134 */       searchDomains = list.<String>toArray(new String[list.size()]);
/* 135 */     } catch (Exception ignore) {
/*     */       
/* 137 */       searchDomains = EmptyArrays.EMPTY_STRINGS;
/*     */     } 
/* 139 */     DEFAULT_SEARCH_DOMAINS = searchDomains;
/*     */ 
/*     */     
/*     */     try {
/* 143 */       ndots = UnixResolverDnsServerAddressStreamProvider.parseEtcResolverFirstNdots();
/* 144 */     } catch (Exception ignore) {
/* 145 */       ndots = 1;
/*     */     } 
/* 147 */     DEFAULT_NDOTS = ndots;
/*     */ 
/*     */     
/* 150 */     DECODER = new DatagramDnsResponseDecoder();
/* 151 */     ENCODER = new DatagramDnsQueryEncoder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 159 */   final DnsQueryContextManager queryContextManager = new DnsQueryContextManager();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 167 */   private final FastThreadLocal<DnsServerAddressStream> nameServerAddrStream = new FastThreadLocal<DnsServerAddressStream>()
/*     */     {
/*     */       protected DnsServerAddressStream initialValue() throws Exception
/*     */       {
/* 171 */         return DnsNameResolver.this.dnsServerAddressStreamProvider.nameServerAddressStream("");
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   private static final InternalLogger logger;
/*     */   
/*     */   private static final String LOCALHOST = "localhost";
/*     */   
/*     */   private static final InetAddress LOCALHOST_ADDRESS;
/*     */   
/*     */   private static final DnsRecord[] EMPTY_ADDITIONALS;
/*     */   
/*     */   private static final DnsRecordType[] IPV4_ONLY_RESOLVED_RECORD_TYPES;
/*     */   
/*     */   private static final InternetProtocolFamily[] IPV4_ONLY_RESOLVED_PROTOCOL_FAMILIES;
/*     */   
/*     */   private static final DnsRecordType[] IPV4_PREFERRED_RESOLVED_RECORD_TYPES;
/*     */   
/*     */   private static final InternetProtocolFamily[] IPV4_PREFERRED_RESOLVED_PROTOCOL_FAMILIES;
/*     */   
/*     */   private static final DnsRecordType[] IPV6_ONLY_RESOLVED_RECORD_TYPES;
/*     */   
/*     */   private static final InternetProtocolFamily[] IPV6_ONLY_RESOLVED_PROTOCOL_FAMILIES;
/*     */   
/*     */   private static final DnsRecordType[] IPV6_PREFERRED_RESOLVED_RECORD_TYPES;
/*     */   
/*     */   private static final InternetProtocolFamily[] IPV6_PREFERRED_RESOLVED_PROTOCOL_FAMILIES;
/*     */   
/*     */   static final ResolvedAddressTypes DEFAULT_RESOLVE_ADDRESS_TYPES;
/*     */   
/*     */   static final String[] DEFAULT_SEARCH_DOMAINS;
/*     */   
/*     */   private static final int DEFAULT_NDOTS;
/*     */   
/*     */   private static final DatagramDnsResponseDecoder DECODER;
/*     */   
/*     */   private static final DatagramDnsQueryEncoder ENCODER;
/*     */   
/*     */   final Future<Channel> channelFuture;
/*     */   
/*     */   final DatagramChannel ch;
/*     */   
/*     */   private final DnsCache resolveCache;
/*     */   
/*     */   private final DnsCache authoritativeDnsServerCache;
/*     */   private final long queryTimeoutMillis;
/*     */   private final int maxQueriesPerResolve;
/*     */   private final ResolvedAddressTypes resolvedAddressTypes;
/*     */   private final InternetProtocolFamily[] resolvedInternetProtocolFamilies;
/*     */   private final boolean recursionDesired;
/*     */   private final int maxPayloadSize;
/*     */   private final boolean optResourceEnabled;
/*     */   private final HostsFileEntriesResolver hostsFileEntriesResolver;
/*     */   private final DnsServerAddressStreamProvider dnsServerAddressStreamProvider;
/*     */   private final String[] searchDomains;
/*     */   private final int ndots;
/*     */   private final boolean supportsAAAARecords;
/*     */   private final boolean supportsARecords;
/*     */   private final InternetProtocolFamily preferredAddressType;
/*     */   private final DnsRecordType[] resolveRecordTypes;
/*     */   private final boolean decodeIdn;
/*     */   private final DnsQueryLifecycleObserverFactory dnsQueryLifecycleObserverFactory;
/*     */   
/*     */   public DnsNameResolver(EventLoop eventLoop, ChannelFactory<? extends DatagramChannel> channelFactory, final DnsCache resolveCache, DnsCache authoritativeDnsServerCache, DnsQueryLifecycleObserverFactory dnsQueryLifecycleObserverFactory, long queryTimeoutMillis, ResolvedAddressTypes resolvedAddressTypes, boolean recursionDesired, int maxQueriesPerResolve, boolean traceEnabled, int maxPayloadSize, boolean optResourceEnabled, HostsFileEntriesResolver hostsFileEntriesResolver, DnsServerAddressStreamProvider dnsServerAddressStreamProvider, String[] searchDomains, int ndots, boolean decodeIdn) {
/* 236 */     super((EventExecutor)eventLoop);
/* 237 */     this.queryTimeoutMillis = ObjectUtil.checkPositive(queryTimeoutMillis, "queryTimeoutMillis");
/* 238 */     this.resolvedAddressTypes = (resolvedAddressTypes != null) ? resolvedAddressTypes : DEFAULT_RESOLVE_ADDRESS_TYPES;
/* 239 */     this.recursionDesired = recursionDesired;
/* 240 */     this.maxQueriesPerResolve = ObjectUtil.checkPositive(maxQueriesPerResolve, "maxQueriesPerResolve");
/* 241 */     this.maxPayloadSize = ObjectUtil.checkPositive(maxPayloadSize, "maxPayloadSize");
/* 242 */     this.optResourceEnabled = optResourceEnabled;
/* 243 */     this.hostsFileEntriesResolver = (HostsFileEntriesResolver)ObjectUtil.checkNotNull(hostsFileEntriesResolver, "hostsFileEntriesResolver");
/* 244 */     this
/* 245 */       .dnsServerAddressStreamProvider = (DnsServerAddressStreamProvider)ObjectUtil.checkNotNull(dnsServerAddressStreamProvider, "dnsServerAddressStreamProvider");
/* 246 */     this.resolveCache = (DnsCache)ObjectUtil.checkNotNull(resolveCache, "resolveCache");
/* 247 */     this.authoritativeDnsServerCache = (DnsCache)ObjectUtil.checkNotNull(authoritativeDnsServerCache, "authoritativeDnsServerCache");
/* 248 */     this
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 253 */       .dnsQueryLifecycleObserverFactory = traceEnabled ? ((dnsQueryLifecycleObserverFactory instanceof NoopDnsQueryLifecycleObserverFactory) ? new TraceDnsQueryLifeCycleObserverFactory() : new BiDnsQueryLifecycleObserverFactory(new TraceDnsQueryLifeCycleObserverFactory(), dnsQueryLifecycleObserverFactory)) : (DnsQueryLifecycleObserverFactory)ObjectUtil.checkNotNull(dnsQueryLifecycleObserverFactory, "dnsQueryLifecycleObserverFactory");
/* 254 */     this.searchDomains = (searchDomains != null) ? (String[])searchDomains.clone() : DEFAULT_SEARCH_DOMAINS;
/* 255 */     this.ndots = (ndots >= 0) ? ndots : DEFAULT_NDOTS;
/* 256 */     this.decodeIdn = decodeIdn;
/*     */     
/* 258 */     switch (this.resolvedAddressTypes) {
/*     */       case IPV4_ONLY:
/* 260 */         this.supportsAAAARecords = false;
/* 261 */         this.supportsARecords = true;
/* 262 */         this.resolveRecordTypes = IPV4_ONLY_RESOLVED_RECORD_TYPES;
/* 263 */         this.resolvedInternetProtocolFamilies = IPV4_ONLY_RESOLVED_PROTOCOL_FAMILIES;
/* 264 */         this.preferredAddressType = InternetProtocolFamily.IPv4;
/*     */         break;
/*     */       case IPV4_PREFERRED:
/* 267 */         this.supportsAAAARecords = true;
/* 268 */         this.supportsARecords = true;
/* 269 */         this.resolveRecordTypes = IPV4_PREFERRED_RESOLVED_RECORD_TYPES;
/* 270 */         this.resolvedInternetProtocolFamilies = IPV4_PREFERRED_RESOLVED_PROTOCOL_FAMILIES;
/* 271 */         this.preferredAddressType = InternetProtocolFamily.IPv4;
/*     */         break;
/*     */       case IPV6_ONLY:
/* 274 */         this.supportsAAAARecords = true;
/* 275 */         this.supportsARecords = false;
/* 276 */         this.resolveRecordTypes = IPV6_ONLY_RESOLVED_RECORD_TYPES;
/* 277 */         this.resolvedInternetProtocolFamilies = IPV6_ONLY_RESOLVED_PROTOCOL_FAMILIES;
/* 278 */         this.preferredAddressType = InternetProtocolFamily.IPv6;
/*     */         break;
/*     */       case IPV6_PREFERRED:
/* 281 */         this.supportsAAAARecords = true;
/* 282 */         this.supportsARecords = true;
/* 283 */         this.resolveRecordTypes = IPV6_PREFERRED_RESOLVED_RECORD_TYPES;
/* 284 */         this.resolvedInternetProtocolFamilies = IPV6_PREFERRED_RESOLVED_PROTOCOL_FAMILIES;
/* 285 */         this.preferredAddressType = InternetProtocolFamily.IPv6;
/*     */         break;
/*     */       default:
/* 288 */         throw new IllegalArgumentException("Unknown ResolvedAddressTypes " + resolvedAddressTypes);
/*     */     } 
/*     */     
/* 291 */     Bootstrap b = new Bootstrap();
/* 292 */     b.group((EventLoopGroup)executor());
/* 293 */     b.channelFactory(channelFactory);
/* 294 */     b.option(ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION, Boolean.valueOf(true));
/* 295 */     final DnsResponseHandler responseHandler = new DnsResponseHandler(executor().newPromise());
/* 296 */     b.handler((ChannelHandler)new ChannelInitializer<DatagramChannel>()
/*     */         {
/*     */           protected void initChannel(DatagramChannel ch) throws Exception {
/* 299 */             ch.pipeline().addLast(new ChannelHandler[] { (ChannelHandler)DnsNameResolver.access$100(), (ChannelHandler)DnsNameResolver.access$200(), (ChannelHandler)this.val$responseHandler });
/*     */           }
/*     */         });
/*     */     
/* 303 */     this.channelFuture = (Future<Channel>)responseHandler.channelActivePromise;
/* 304 */     this.ch = (DatagramChannel)b.register().channel();
/* 305 */     this.ch.config().setRecvByteBufAllocator((RecvByteBufAllocator)new FixedRecvByteBufAllocator(maxPayloadSize));
/*     */     
/* 307 */     this.ch.closeFuture().addListener((GenericFutureListener)new ChannelFutureListener()
/*     */         {
/*     */           public void operationComplete(ChannelFuture future) throws Exception {
/* 310 */             resolveCache.clear();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   int dnsRedirectPort(InetAddress server) {
/* 317 */     return 53;
/*     */   }
/*     */   
/*     */   final DnsQueryLifecycleObserverFactory dnsQueryLifecycleObserverFactory() {
/* 321 */     return this.dnsQueryLifecycleObserverFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DnsServerAddressStream uncachedRedirectDnsServerStream(List<InetSocketAddress> nameServers) {
/* 330 */     return DnsServerAddresses.sequential(nameServers).stream();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DnsCache resolveCache() {
/* 337 */     return this.resolveCache;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DnsCache authoritativeDnsServerCache() {
/* 344 */     return this.authoritativeDnsServerCache;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long queryTimeoutMillis() {
/* 352 */     return this.queryTimeoutMillis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResolvedAddressTypes resolvedAddressTypes() {
/* 360 */     return this.resolvedAddressTypes;
/*     */   }
/*     */   
/*     */   InternetProtocolFamily[] resolvedInternetProtocolFamiliesUnsafe() {
/* 364 */     return this.resolvedInternetProtocolFamilies;
/*     */   }
/*     */   
/*     */   final String[] searchDomains() {
/* 368 */     return this.searchDomains;
/*     */   }
/*     */   
/*     */   final int ndots() {
/* 372 */     return this.ndots;
/*     */   }
/*     */   
/*     */   final boolean supportsAAAARecords() {
/* 376 */     return this.supportsAAAARecords;
/*     */   }
/*     */   
/*     */   final boolean supportsARecords() {
/* 380 */     return this.supportsARecords;
/*     */   }
/*     */   
/*     */   final InternetProtocolFamily preferredAddressType() {
/* 384 */     return this.preferredAddressType;
/*     */   }
/*     */   
/*     */   final DnsRecordType[] resolveRecordTypes() {
/* 388 */     return this.resolveRecordTypes;
/*     */   }
/*     */   
/*     */   final boolean isDecodeIdn() {
/* 392 */     return this.decodeIdn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRecursionDesired() {
/* 400 */     return this.recursionDesired;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int maxQueriesPerResolve() {
/* 408 */     return this.maxQueriesPerResolve;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int maxPayloadSize() {
/* 415 */     return this.maxPayloadSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOptResourceEnabled() {
/* 423 */     return this.optResourceEnabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HostsFileEntriesResolver hostsFileEntriesResolver() {
/* 431 */     return this.hostsFileEntriesResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 441 */     if (this.ch.isOpen()) {
/* 442 */       this.ch.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected EventLoop executor() {
/* 448 */     return (EventLoop)super.executor();
/*     */   }
/*     */   
/*     */   private InetAddress resolveHostsFileEntry(String hostname) {
/* 452 */     if (this.hostsFileEntriesResolver == null) {
/* 453 */       return null;
/*     */     }
/* 455 */     InetAddress address = this.hostsFileEntriesResolver.address(hostname, this.resolvedAddressTypes);
/* 456 */     if (address == null && PlatformDependent.isWindows() && "localhost".equalsIgnoreCase(hostname))
/*     */     {
/*     */ 
/*     */       
/* 460 */       return LOCALHOST_ADDRESS;
/*     */     }
/* 462 */     return address;
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
/*     */   public final Future<InetAddress> resolve(String inetHost, Iterable<DnsRecord> additionals) {
/* 475 */     return resolve(inetHost, additionals, executor().newPromise());
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
/*     */   public final Future<InetAddress> resolve(String inetHost, Iterable<DnsRecord> additionals, Promise<InetAddress> promise) {
/* 489 */     ObjectUtil.checkNotNull(promise, "promise");
/* 490 */     DnsRecord[] additionalsArray = toArray(additionals, true);
/*     */     try {
/* 492 */       doResolve(inetHost, additionalsArray, promise, this.resolveCache);
/* 493 */       return (Future<InetAddress>)promise;
/* 494 */     } catch (Exception e) {
/* 495 */       return (Future<InetAddress>)promise.setFailure(e);
/*     */     } 
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
/*     */   public final Future<List<InetAddress>> resolveAll(String inetHost, Iterable<DnsRecord> additionals) {
/* 508 */     return resolveAll(inetHost, additionals, executor().newPromise());
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
/*     */   public final Future<List<InetAddress>> resolveAll(String inetHost, Iterable<DnsRecord> additionals, Promise<List<InetAddress>> promise) {
/* 522 */     ObjectUtil.checkNotNull(promise, "promise");
/* 523 */     DnsRecord[] additionalsArray = toArray(additionals, true);
/*     */     try {
/* 525 */       doResolveAll(inetHost, additionalsArray, promise, this.resolveCache);
/* 526 */       return (Future<List<InetAddress>>)promise;
/* 527 */     } catch (Exception e) {
/* 528 */       return (Future<List<InetAddress>>)promise.setFailure(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doResolve(String inetHost, Promise<InetAddress> promise) throws Exception {
/* 534 */     doResolve(inetHost, EMPTY_ADDITIONALS, promise, this.resolveCache);
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
/*     */   public final Future<List<DnsRecord>> resolveAll(DnsQuestion question) {
/* 549 */     return resolveAll(question, EMPTY_ADDITIONALS, executor().newPromise());
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
/*     */   public final Future<List<DnsRecord>> resolveAll(DnsQuestion question, Iterable<DnsRecord> additionals) {
/* 565 */     return resolveAll(question, additionals, executor().newPromise());
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
/*     */   public final Future<List<DnsRecord>> resolveAll(DnsQuestion question, Iterable<DnsRecord> additionals, Promise<List<DnsRecord>> promise) {
/* 583 */     DnsRecord[] additionalsArray = toArray(additionals, true);
/* 584 */     return resolveAll(question, additionalsArray, promise);
/*     */   }
/*     */ 
/*     */   
/*     */   private Future<List<DnsRecord>> resolveAll(DnsQuestion question, DnsRecord[] additionals, Promise<List<DnsRecord>> promise) {
/* 589 */     ObjectUtil.checkNotNull(question, "question");
/* 590 */     ObjectUtil.checkNotNull(promise, "promise");
/*     */ 
/*     */     
/* 593 */     DnsRecordType type = question.type();
/* 594 */     String hostname = question.name();
/*     */     
/* 596 */     if (type == DnsRecordType.A || type == DnsRecordType.AAAA) {
/* 597 */       InetAddress hostsFileEntry = resolveHostsFileEntry(hostname);
/* 598 */       if (hostsFileEntry != null) {
/* 599 */         ByteBuf content = null;
/* 600 */         if (hostsFileEntry instanceof java.net.Inet4Address) {
/* 601 */           if (type == DnsRecordType.A) {
/* 602 */             content = Unpooled.wrappedBuffer(hostsFileEntry.getAddress());
/*     */           }
/* 604 */         } else if (hostsFileEntry instanceof java.net.Inet6Address && 
/* 605 */           type == DnsRecordType.AAAA) {
/* 606 */           content = Unpooled.wrappedBuffer(hostsFileEntry.getAddress());
/*     */         } 
/*     */ 
/*     */         
/* 610 */         if (content != null) {
/*     */ 
/*     */           
/* 613 */           trySuccess(promise, (List)Collections.singletonList(new DefaultDnsRawRecord(hostname, type, 86400L, content)));
/*     */           
/* 615 */           return (Future<List<DnsRecord>>)promise;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 622 */     DnsServerAddressStream nameServerAddrs = this.dnsServerAddressStreamProvider.nameServerAddressStream(hostname);
/* 623 */     (new DnsRecordResolveContext(this, question, additionals, nameServerAddrs)).resolve(promise);
/* 624 */     return (Future<List<DnsRecord>>)promise;
/*     */   }
/*     */   
/*     */   private static DnsRecord[] toArray(Iterable<DnsRecord> additionals, boolean validateType) {
/* 628 */     ObjectUtil.checkNotNull(additionals, "additionals");
/* 629 */     if (additionals instanceof Collection) {
/* 630 */       Collection<DnsRecord> collection = (Collection<DnsRecord>)additionals;
/* 631 */       for (DnsRecord r : additionals) {
/* 632 */         validateAdditional(r, validateType);
/*     */       }
/* 634 */       return collection.<DnsRecord>toArray(new DnsRecord[collection.size()]);
/*     */     } 
/*     */     
/* 637 */     Iterator<DnsRecord> additionalsIt = additionals.iterator();
/* 638 */     if (!additionalsIt.hasNext()) {
/* 639 */       return EMPTY_ADDITIONALS;
/*     */     }
/* 641 */     List<DnsRecord> records = new ArrayList<DnsRecord>();
/*     */     do {
/* 643 */       DnsRecord r = additionalsIt.next();
/* 644 */       validateAdditional(r, validateType);
/* 645 */       records.add(r);
/* 646 */     } while (additionalsIt.hasNext());
/*     */     
/* 648 */     return records.<DnsRecord>toArray(new DnsRecord[records.size()]);
/*     */   }
/*     */   
/*     */   private static void validateAdditional(DnsRecord record, boolean validateType) {
/* 652 */     ObjectUtil.checkNotNull(record, "record");
/* 653 */     if (validateType && record instanceof io.netty.handler.codec.dns.DnsRawRecord) {
/* 654 */       throw new IllegalArgumentException("DnsRawRecord implementations not allowed: " + record);
/*     */     }
/*     */   }
/*     */   
/*     */   private InetAddress loopbackAddress() {
/* 659 */     return preferredAddressType().localhost();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doResolve(String inetHost, DnsRecord[] additionals, Promise<InetAddress> promise, DnsCache resolveCache) throws Exception {
/* 670 */     if (inetHost == null || inetHost.isEmpty()) {
/*     */       
/* 672 */       promise.setSuccess(loopbackAddress());
/*     */       return;
/*     */     } 
/* 675 */     byte[] bytes = NetUtil.createByteArrayFromIpAddressString(inetHost);
/* 676 */     if (bytes != null) {
/*     */       
/* 678 */       promise.setSuccess(InetAddress.getByAddress(bytes));
/*     */       
/*     */       return;
/*     */     } 
/* 682 */     String hostname = hostname(inetHost);
/*     */     
/* 684 */     InetAddress hostsFileEntry = resolveHostsFileEntry(hostname);
/* 685 */     if (hostsFileEntry != null) {
/* 686 */       promise.setSuccess(hostsFileEntry);
/*     */       
/*     */       return;
/*     */     } 
/* 690 */     if (!doResolveCached(hostname, additionals, promise, resolveCache)) {
/* 691 */       doResolveUncached(hostname, additionals, promise, resolveCache);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean doResolveCached(String hostname, DnsRecord[] additionals, Promise<InetAddress> promise, DnsCache resolveCache) {
/* 699 */     List<? extends DnsCacheEntry> cachedEntries = resolveCache.get(hostname, additionals);
/* 700 */     if (cachedEntries == null || cachedEntries.isEmpty()) {
/* 701 */       return false;
/*     */     }
/*     */     
/* 704 */     Throwable cause = ((DnsCacheEntry)cachedEntries.get(0)).cause();
/* 705 */     if (cause == null) {
/* 706 */       int numEntries = cachedEntries.size();
/*     */       
/* 708 */       for (InternetProtocolFamily f : this.resolvedInternetProtocolFamilies) {
/* 709 */         for (int i = 0; i < numEntries; i++) {
/* 710 */           DnsCacheEntry e = cachedEntries.get(i);
/* 711 */           if (f.addressType().isInstance(e.address())) {
/* 712 */             trySuccess(promise, e.address());
/* 713 */             return true;
/*     */           } 
/*     */         } 
/*     */       } 
/* 717 */       return false;
/*     */     } 
/* 719 */     tryFailure(promise, cause);
/* 720 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   static <T> void trySuccess(Promise<T> promise, T result) {
/* 725 */     if (!promise.trySuccess(result)) {
/* 726 */       logger.warn("Failed to notify success ({}) to a promise: {}", result, promise);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void tryFailure(Promise<?> promise, Throwable cause) {
/* 731 */     if (!promise.tryFailure(cause)) {
/* 732 */       logger.warn("Failed to notify failure to a promise: {}", promise, cause);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void doResolveUncached(String hostname, DnsRecord[] additionals, final Promise<InetAddress> promise, DnsCache resolveCache) {
/* 740 */     Promise<List<InetAddress>> allPromise = executor().newPromise();
/* 741 */     doResolveAllUncached(hostname, additionals, allPromise, resolveCache);
/* 742 */     allPromise.addListener((GenericFutureListener)new FutureListener<List<InetAddress>>()
/*     */         {
/*     */           public void operationComplete(Future<List<InetAddress>> future) {
/* 745 */             if (future.isSuccess()) {
/* 746 */               DnsNameResolver.trySuccess(promise, ((List)future.getNow()).get(0));
/*     */             } else {
/* 748 */               DnsNameResolver.tryFailure(promise, future.cause());
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doResolveAll(String inetHost, Promise<List<InetAddress>> promise) throws Exception {
/* 756 */     doResolveAll(inetHost, EMPTY_ADDITIONALS, promise, this.resolveCache);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doResolveAll(String inetHost, DnsRecord[] additionals, Promise<List<InetAddress>> promise, DnsCache resolveCache) throws Exception {
/* 767 */     if (inetHost == null || inetHost.isEmpty()) {
/*     */       
/* 769 */       promise.setSuccess(Collections.singletonList(loopbackAddress()));
/*     */       return;
/*     */     } 
/* 772 */     byte[] bytes = NetUtil.createByteArrayFromIpAddressString(inetHost);
/* 773 */     if (bytes != null) {
/*     */       
/* 775 */       promise.setSuccess(Collections.singletonList(InetAddress.getByAddress(bytes)));
/*     */       
/*     */       return;
/*     */     } 
/* 779 */     String hostname = hostname(inetHost);
/*     */     
/* 781 */     InetAddress hostsFileEntry = resolveHostsFileEntry(hostname);
/* 782 */     if (hostsFileEntry != null) {
/* 783 */       promise.setSuccess(Collections.singletonList(hostsFileEntry));
/*     */       
/*     */       return;
/*     */     } 
/* 787 */     if (!doResolveAllCached(hostname, additionals, promise, resolveCache)) {
/* 788 */       doResolveAllUncached(hostname, additionals, promise, resolveCache);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean doResolveAllCached(String hostname, DnsRecord[] additionals, Promise<List<InetAddress>> promise, DnsCache resolveCache) {
/* 796 */     List<? extends DnsCacheEntry> cachedEntries = resolveCache.get(hostname, additionals);
/* 797 */     if (cachedEntries == null || cachedEntries.isEmpty()) {
/* 798 */       return false;
/*     */     }
/*     */     
/* 801 */     Throwable cause = ((DnsCacheEntry)cachedEntries.get(0)).cause();
/* 802 */     if (cause == null) {
/* 803 */       List<InetAddress> result = null;
/* 804 */       int numEntries = cachedEntries.size();
/* 805 */       for (InternetProtocolFamily f : this.resolvedInternetProtocolFamilies) {
/* 806 */         for (int i = 0; i < numEntries; i++) {
/* 807 */           DnsCacheEntry e = cachedEntries.get(i);
/* 808 */           if (f.addressType().isInstance(e.address())) {
/* 809 */             if (result == null) {
/* 810 */               result = new ArrayList<InetAddress>(numEntries);
/*     */             }
/* 812 */             result.add(e.address());
/*     */           } 
/*     */         } 
/*     */       } 
/* 816 */       if (result != null) {
/* 817 */         trySuccess(promise, result);
/* 818 */         return true;
/*     */       } 
/* 820 */       return false;
/*     */     } 
/* 822 */     tryFailure(promise, cause);
/* 823 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void doResolveAllUncached(String hostname, DnsRecord[] additionals, Promise<List<InetAddress>> promise, DnsCache resolveCache) {
/* 832 */     DnsServerAddressStream nameServerAddrs = this.dnsServerAddressStreamProvider.nameServerAddressStream(hostname);
/* 833 */     (new DnsAddressResolveContext(this, hostname, additionals, nameServerAddrs, resolveCache)).resolve(promise);
/*     */   }
/*     */   
/*     */   private static String hostname(String inetHost) {
/* 837 */     String hostname = IDN.toASCII(inetHost);
/*     */     
/* 839 */     if (StringUtil.endsWith(inetHost, '.') && !StringUtil.endsWith(hostname, '.')) {
/* 840 */       hostname = hostname + ".";
/*     */     }
/* 842 */     return hostname;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query(DnsQuestion question) {
/* 849 */     return query(nextNameServerAddress(), question);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query(DnsQuestion question, Iterable<DnsRecord> additionals) {
/* 857 */     return query(nextNameServerAddress(), question, additionals);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query(DnsQuestion question, Promise<AddressedEnvelope<? extends DnsResponse, InetSocketAddress>> promise) {
/* 865 */     return query(nextNameServerAddress(), question, Collections.emptyList(), promise);
/*     */   }
/*     */   
/*     */   private InetSocketAddress nextNameServerAddress() {
/* 869 */     return ((DnsServerAddressStream)this.nameServerAddrStream.get()).next();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query(InetSocketAddress nameServerAddr, DnsQuestion question) {
/* 878 */     return query0(nameServerAddr, question, EMPTY_ADDITIONALS, this.ch
/* 879 */         .eventLoop().newPromise());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query(InetSocketAddress nameServerAddr, DnsQuestion question, Iterable<DnsRecord> additionals) {
/* 888 */     return query0(nameServerAddr, question, toArray(additionals, false), this.ch
/* 889 */         .eventLoop().newPromise());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query(InetSocketAddress nameServerAddr, DnsQuestion question, Promise<AddressedEnvelope<? extends DnsResponse, InetSocketAddress>> promise) {
/* 899 */     return query0(nameServerAddr, question, EMPTY_ADDITIONALS, promise);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query(InetSocketAddress nameServerAddr, DnsQuestion question, Iterable<DnsRecord> additionals, Promise<AddressedEnvelope<? extends DnsResponse, InetSocketAddress>> promise) {
/* 910 */     return query0(nameServerAddr, question, toArray(additionals, false), promise);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isTransportOrTimeoutError(Throwable cause) {
/* 919 */     return (cause != null && cause.getCause() instanceof DnsNameResolverException);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isTimeoutError(Throwable cause) {
/* 928 */     return (cause != null && cause.getCause() instanceof DnsNameResolverTimeoutException);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query0(InetSocketAddress nameServerAddr, DnsQuestion question, DnsRecord[] additionals, Promise<AddressedEnvelope<? extends DnsResponse, InetSocketAddress>> promise) {
/* 935 */     return query0(nameServerAddr, question, additionals, this.ch.newPromise(), promise);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query0(InetSocketAddress nameServerAddr, DnsQuestion question, DnsRecord[] additionals, ChannelPromise writePromise, Promise<AddressedEnvelope<? extends DnsResponse, InetSocketAddress>> promise) {
/* 943 */     assert !writePromise.isVoid();
/*     */     
/* 945 */     Promise<AddressedEnvelope<DnsResponse, InetSocketAddress>> castPromise = cast(
/* 946 */         (Promise)ObjectUtil.checkNotNull(promise, "promise"));
/*     */     try {
/* 948 */       (new DnsQueryContext(this, nameServerAddr, question, additionals, castPromise)).query(writePromise);
/* 949 */       return (Future<AddressedEnvelope<DnsResponse, InetSocketAddress>>)castPromise;
/* 950 */     } catch (Exception e) {
/* 951 */       return (Future<AddressedEnvelope<DnsResponse, InetSocketAddress>>)castPromise.setFailure(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static Promise<AddressedEnvelope<DnsResponse, InetSocketAddress>> cast(Promise<?> promise) {
/* 957 */     return (Promise)promise;
/*     */   }
/*     */   
/*     */   private final class DnsResponseHandler
/*     */     extends ChannelInboundHandlerAdapter {
/*     */     private final Promise<Channel> channelActivePromise;
/*     */     
/*     */     DnsResponseHandler(Promise<Channel> channelActivePromise) {
/* 965 */       this.channelActivePromise = channelActivePromise;
/*     */     }
/*     */ 
/*     */     
/*     */     public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
/*     */       try {
/* 971 */         DatagramDnsResponse res = (DatagramDnsResponse)msg;
/* 972 */         int queryId = res.id();
/*     */         
/* 974 */         if (DnsNameResolver.logger.isDebugEnabled()) {
/* 975 */           DnsNameResolver.logger.debug("{} RECEIVED: [{}: {}], {}", new Object[] { this.this$0.ch, Integer.valueOf(queryId), res.sender(), res });
/*     */         }
/*     */         
/* 978 */         DnsQueryContext qCtx = DnsNameResolver.this.queryContextManager.get(res.sender(), queryId);
/* 979 */         if (qCtx == null) {
/* 980 */           DnsNameResolver.logger.warn("{} Received a DNS response with an unknown ID: {}", DnsNameResolver.this.ch, Integer.valueOf(queryId));
/*     */           
/*     */           return;
/*     */         } 
/* 984 */         qCtx.finish((AddressedEnvelope<? extends DnsResponse, InetSocketAddress>)res);
/*     */       } finally {
/* 986 */         ReferenceCountUtil.safeRelease(msg);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void channelActive(ChannelHandlerContext ctx) throws Exception {
/* 992 */       super.channelActive(ctx);
/* 993 */       this.channelActivePromise.setSuccess(ctx.channel());
/*     */     }
/*     */ 
/*     */     
/*     */     public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
/* 998 */       DnsNameResolver.logger.warn("{} Unexpected exception: ", DnsNameResolver.this.ch, cause);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\dns\DnsNameResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */