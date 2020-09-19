/*     */ package io.netty.resolver.dns;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufHolder;
/*     */ import io.netty.channel.AddressedEnvelope;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.handler.codec.CorruptedFrameException;
/*     */ import io.netty.handler.codec.dns.DefaultDnsQuestion;
/*     */ import io.netty.handler.codec.dns.DefaultDnsRecordDecoder;
/*     */ import io.netty.handler.codec.dns.DnsQuestion;
/*     */ import io.netty.handler.codec.dns.DnsRecord;
/*     */ import io.netty.handler.codec.dns.DnsRecordType;
/*     */ import io.netty.handler.codec.dns.DnsResponse;
/*     */ import io.netty.handler.codec.dns.DnsResponseCode;
/*     */ import io.netty.handler.codec.dns.DnsSection;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.FutureListener;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import io.netty.util.concurrent.Promise;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import io.netty.util.internal.ThrowableUtil;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ abstract class DnsResolveContext<T>
/*     */ {
/*  64 */   private static final FutureListener<AddressedEnvelope<DnsResponse, InetSocketAddress>> RELEASE_RESPONSE = new FutureListener<AddressedEnvelope<DnsResponse, InetSocketAddress>>()
/*     */     {
/*     */       public void operationComplete(Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> future)
/*     */       {
/*  68 */         if (future.isSuccess())
/*  69 */           ((AddressedEnvelope)future.getNow()).release(); 
/*     */       }
/*     */     };
/*     */   
/*  73 */   private static final RuntimeException NXDOMAIN_QUERY_FAILED_EXCEPTION = (RuntimeException)ThrowableUtil.unknownStackTrace(new RuntimeException("No answer found and NXDOMAIN response code returned"), DnsResolveContext.class, "onResponse(..)");
/*     */ 
/*     */ 
/*     */   
/*  77 */   private static final RuntimeException CNAME_NOT_FOUND_QUERY_FAILED_EXCEPTION = (RuntimeException)ThrowableUtil.unknownStackTrace(new RuntimeException("No matching CNAME record found"), DnsResolveContext.class, "onResponseCNAME(..)");
/*     */ 
/*     */ 
/*     */   
/*  81 */   private static final RuntimeException NO_MATCHING_RECORD_QUERY_FAILED_EXCEPTION = (RuntimeException)ThrowableUtil.unknownStackTrace(new RuntimeException("No matching record type found"), DnsResolveContext.class, "onResponseAorAAAA(..)");
/*     */ 
/*     */ 
/*     */   
/*  85 */   private static final RuntimeException UNRECOGNIZED_TYPE_QUERY_FAILED_EXCEPTION = (RuntimeException)ThrowableUtil.unknownStackTrace(new RuntimeException("Response type was unrecognized"), DnsResolveContext.class, "onResponse(..)");
/*     */ 
/*     */ 
/*     */   
/*  89 */   private static final RuntimeException NAME_SERVERS_EXHAUSTED_EXCEPTION = (RuntimeException)ThrowableUtil.unknownStackTrace(new RuntimeException("No name servers returned an answer"), DnsResolveContext.class, "tryToFinishResolve(..)");
/*     */   
/*     */   final DnsNameResolver parent;
/*     */   
/*     */   private final DnsServerAddressStream nameServerAddrs;
/*     */   
/*     */   private final String hostname;
/*     */   
/*     */   private final int dnsClass;
/*     */   
/*     */   private final DnsRecordType[] expectedTypes;
/*     */   
/*     */   private final int maxAllowedQueries;
/*     */   private final DnsRecord[] additionals;
/* 103 */   private final Set<Future<AddressedEnvelope<DnsResponse, InetSocketAddress>>> queriesInProgress = Collections.newSetFromMap(new IdentityHashMap<Future<AddressedEnvelope<DnsResponse, InetSocketAddress>>, Boolean>());
/*     */ 
/*     */   
/*     */   private List<T> finalResult;
/*     */   
/*     */   private int allowedQueries;
/*     */   
/*     */   private boolean triedCNAME;
/*     */ 
/*     */   
/*     */   DnsResolveContext(DnsNameResolver parent, String hostname, int dnsClass, DnsRecordType[] expectedTypes, DnsRecord[] additionals, DnsServerAddressStream nameServerAddrs) {
/* 114 */     assert expectedTypes.length > 0;
/*     */     
/* 116 */     this.parent = parent;
/* 117 */     this.hostname = hostname;
/* 118 */     this.dnsClass = dnsClass;
/* 119 */     this.expectedTypes = expectedTypes;
/* 120 */     this.additionals = additionals;
/*     */     
/* 122 */     this.nameServerAddrs = (DnsServerAddressStream)ObjectUtil.checkNotNull(nameServerAddrs, "nameServerAddrs");
/* 123 */     this.maxAllowedQueries = parent.maxQueriesPerResolve();
/* 124 */     this.allowedQueries = this.maxAllowedQueries;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void resolve(final Promise<List<T>> promise) {
/* 159 */     final String[] searchDomains = this.parent.searchDomains();
/* 160 */     if (searchDomains.length == 0 || this.parent.ndots() == 0 || StringUtil.endsWith(this.hostname, '.')) {
/* 161 */       internalResolve(promise);
/*     */     } else {
/* 163 */       final boolean startWithoutSearchDomain = hasNDots();
/* 164 */       String initialHostname = startWithoutSearchDomain ? this.hostname : (this.hostname + '.' + searchDomains[0]);
/* 165 */       final int initialSearchDomainIdx = startWithoutSearchDomain ? 0 : 1;
/*     */       
/* 167 */       doSearchDomainQuery(initialHostname, new FutureListener<List<T>>() {
/* 168 */             private int searchDomainIdx = initialSearchDomainIdx;
/*     */             
/*     */             public void operationComplete(Future<List<T>> future) {
/* 171 */               Throwable cause = future.cause();
/* 172 */               if (cause == null) {
/* 173 */                 promise.trySuccess(future.getNow());
/*     */               }
/* 175 */               else if (DnsNameResolver.isTransportOrTimeoutError(cause)) {
/* 176 */                 promise.tryFailure(new DnsResolveContext.SearchDomainUnknownHostException(cause, DnsResolveContext.this.hostname));
/* 177 */               } else if (this.searchDomainIdx < searchDomains.length) {
/* 178 */                 DnsResolveContext.this.doSearchDomainQuery(DnsResolveContext.this.hostname + '.' + searchDomains[this.searchDomainIdx++], this);
/* 179 */               } else if (!startWithoutSearchDomain) {
/* 180 */                 DnsResolveContext.this.internalResolve(promise);
/*     */               } else {
/* 182 */                 promise.tryFailure(new DnsResolveContext.SearchDomainUnknownHostException(cause, DnsResolveContext.this.hostname));
/*     */               } 
/*     */             }
/*     */           });
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean hasNDots() {
/* 191 */     for (int idx = this.hostname.length() - 1, dots = 0; idx >= 0; idx--) {
/* 192 */       if (this.hostname.charAt(idx) == '.' && ++dots >= this.parent.ndots()) {
/* 193 */         return true;
/*     */       }
/*     */     } 
/* 196 */     return false;
/*     */   }
/*     */   
/*     */   private static final class SearchDomainUnknownHostException extends UnknownHostException {
/*     */     private static final long serialVersionUID = -8573510133644997085L;
/*     */     
/*     */     SearchDomainUnknownHostException(Throwable cause, String originalHostname) {
/* 203 */       super("Search domain query failed. Original hostname: '" + originalHostname + "' " + cause.getMessage());
/* 204 */       setStackTrace(cause.getStackTrace());
/*     */ 
/*     */       
/* 207 */       initCause(cause.getCause());
/*     */     }
/*     */ 
/*     */     
/*     */     public Throwable fillInStackTrace() {
/* 212 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   private void doSearchDomainQuery(String hostname, FutureListener<List<T>> listener) {
/* 217 */     DnsResolveContext<T> nextContext = newResolverContext(this.parent, hostname, this.dnsClass, this.expectedTypes, this.additionals, this.nameServerAddrs);
/*     */     
/* 219 */     Promise<List<T>> nextPromise = this.parent.executor().newPromise();
/* 220 */     nextContext.internalResolve(nextPromise);
/* 221 */     nextPromise.addListener((GenericFutureListener)listener);
/*     */   }
/*     */   
/*     */   private void internalResolve(Promise<List<T>> promise) {
/* 225 */     DnsServerAddressStream nameServerAddressStream = getNameServers(this.hostname);
/*     */     
/* 227 */     int end = this.expectedTypes.length - 1;
/* 228 */     for (int i = 0; i < end; i++) {
/* 229 */       if (!query(this.hostname, this.expectedTypes[i], nameServerAddressStream.duplicate(), promise)) {
/*     */         return;
/*     */       }
/*     */     } 
/* 233 */     query(this.hostname, this.expectedTypes[end], nameServerAddressStream, promise);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addNameServerToCache(AuthoritativeNameServer name, InetAddress resolved, long ttl) {
/* 241 */     if (!name.isRootServer())
/*     */     {
/* 243 */       this.parent.authoritativeDnsServerCache().cache(name.domainName(), this.additionals, resolved, ttl, this.parent.ch
/* 244 */           .eventLoop());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DnsServerAddressStream getNameServersFromCache(String hostname) {
/* 253 */     int len = hostname.length();
/*     */     
/* 255 */     if (len == 0)
/*     */     {
/* 257 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 261 */     if (hostname.charAt(len - 1) != '.') {
/* 262 */       hostname = hostname + ".";
/*     */     }
/*     */     
/* 265 */     int idx = hostname.indexOf('.');
/* 266 */     if (idx == hostname.length() - 1)
/*     */     {
/* 268 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/* 274 */       hostname = hostname.substring(idx + 1);
/*     */       
/* 276 */       int idx2 = hostname.indexOf('.');
/* 277 */       if (idx2 <= 0 || idx2 == hostname.length() - 1)
/*     */       {
/* 279 */         return null;
/*     */       }
/* 281 */       idx = idx2;
/*     */       
/* 283 */       List<? extends DnsCacheEntry> entries = this.parent.authoritativeDnsServerCache().get(hostname, this.additionals);
/* 284 */       if (entries != null && !entries.isEmpty())
/* 285 */         return DnsServerAddresses.sequential(new DnsCacheIterable(entries)).stream(); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private final class DnsCacheIterable
/*     */     implements Iterable<InetSocketAddress> {
/*     */     private final List<? extends DnsCacheEntry> entries;
/*     */     
/*     */     DnsCacheIterable(List<? extends DnsCacheEntry> entries) {
/* 294 */       this.entries = entries;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<InetSocketAddress> iterator() {
/* 299 */       return new Iterator<InetSocketAddress>() {
/* 300 */           Iterator<? extends DnsCacheEntry> entryIterator = DnsResolveContext.DnsCacheIterable.this.entries.iterator();
/*     */ 
/*     */           
/*     */           public boolean hasNext() {
/* 304 */             return this.entryIterator.hasNext();
/*     */           }
/*     */ 
/*     */           
/*     */           public InetSocketAddress next() {
/* 309 */             InetAddress address = ((DnsCacheEntry)this.entryIterator.next()).address();
/* 310 */             return new InetSocketAddress(address, DnsResolveContext.this.parent.dnsRedirectPort(address));
/*     */           }
/*     */ 
/*     */           
/*     */           public void remove() {
/* 315 */             this.entryIterator.remove();
/*     */           }
/*     */         };
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void query(DnsServerAddressStream nameServerAddrStream, int nameServerAddrStreamIndex, DnsQuestion question, Promise<List<T>> promise, Throwable cause) {
/* 324 */     query(nameServerAddrStream, nameServerAddrStreamIndex, question, this.parent
/* 325 */         .dnsQueryLifecycleObserverFactory().newDnsQueryLifecycleObserver(question), promise, cause);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void query(final DnsServerAddressStream nameServerAddrStream, final int nameServerAddrStreamIndex, final DnsQuestion question, final DnsQueryLifecycleObserver queryLifecycleObserver, final Promise<List<T>> promise, Throwable cause) {
/* 334 */     if (nameServerAddrStreamIndex >= nameServerAddrStream.size() || this.allowedQueries == 0 || promise.isCancelled()) {
/* 335 */       tryToFinishResolve(nameServerAddrStream, nameServerAddrStreamIndex, question, queryLifecycleObserver, promise, cause);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 340 */     this.allowedQueries--;
/* 341 */     InetSocketAddress nameServerAddr = nameServerAddrStream.next();
/* 342 */     ChannelPromise writePromise = this.parent.ch.newPromise();
/* 343 */     Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> f = this.parent.query0(nameServerAddr, question, this.additionals, writePromise, this.parent.ch
/*     */         
/* 345 */         .eventLoop().newPromise());
/* 346 */     this.queriesInProgress.add(f);
/*     */     
/* 348 */     queryLifecycleObserver.queryWritten(nameServerAddr, (ChannelFuture)writePromise);
/*     */     
/* 350 */     f.addListener((GenericFutureListener)new FutureListener<AddressedEnvelope<DnsResponse, InetSocketAddress>>()
/*     */         {
/*     */           public void operationComplete(Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> future) {
/* 353 */             DnsResolveContext.this.queriesInProgress.remove(future);
/*     */             
/* 355 */             if (promise.isDone() || future.isCancelled()) {
/* 356 */               queryLifecycleObserver.queryCancelled(DnsResolveContext.this.allowedQueries);
/*     */ 
/*     */ 
/*     */               
/* 360 */               AddressedEnvelope<DnsResponse, InetSocketAddress> result = (AddressedEnvelope<DnsResponse, InetSocketAddress>)future.getNow();
/* 361 */               if (result != null) {
/* 362 */                 result.release();
/*     */               }
/*     */               
/*     */               return;
/*     */             } 
/* 367 */             Throwable queryCause = future.cause();
/*     */             try {
/* 369 */               if (queryCause == null) {
/* 370 */                 DnsResolveContext.this.onResponse(nameServerAddrStream, nameServerAddrStreamIndex, question, (AddressedEnvelope)future.getNow(), queryLifecycleObserver, promise);
/*     */               }
/*     */               else {
/*     */                 
/* 374 */                 queryLifecycleObserver.queryFailed(queryCause);
/* 375 */                 DnsResolveContext.this.query(nameServerAddrStream, nameServerAddrStreamIndex + 1, question, promise, queryCause);
/*     */               } 
/*     */             } finally {
/* 378 */               DnsResolveContext.this.tryToFinishResolve(nameServerAddrStream, nameServerAddrStreamIndex, question, NoopDnsQueryLifecycleObserver.INSTANCE, promise, queryCause);
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void onResponse(DnsServerAddressStream nameServerAddrStream, int nameServerAddrStreamIndex, DnsQuestion question, AddressedEnvelope<DnsResponse, InetSocketAddress> envelope, DnsQueryLifecycleObserver queryLifecycleObserver, Promise<List<T>> promise) {
/*     */     try {
/* 393 */       DnsResponse res = (DnsResponse)envelope.content();
/* 394 */       DnsResponseCode code = res.code();
/* 395 */       if (code == DnsResponseCode.NOERROR) {
/* 396 */         if (handleRedirect(question, envelope, queryLifecycleObserver, promise)) {
/*     */           return;
/*     */         }
/*     */         
/* 400 */         DnsRecordType type = question.type();
/*     */         
/* 402 */         if (type == DnsRecordType.CNAME) {
/* 403 */           onResponseCNAME(question, buildAliasMap((DnsResponse)envelope.content()), queryLifecycleObserver, promise);
/*     */           
/*     */           return;
/*     */         } 
/* 407 */         for (DnsRecordType expectedType : this.expectedTypes) {
/* 408 */           if (type == expectedType) {
/* 409 */             onExpectedResponse(question, envelope, queryLifecycleObserver, promise);
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 414 */         queryLifecycleObserver.queryFailed(UNRECOGNIZED_TYPE_QUERY_FAILED_EXCEPTION);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 419 */       if (code != DnsResponseCode.NXDOMAIN) {
/* 420 */         query(nameServerAddrStream, nameServerAddrStreamIndex + 1, question, queryLifecycleObserver
/* 421 */             .queryNoAnswer(code), promise, null);
/*     */       } else {
/* 423 */         queryLifecycleObserver.queryFailed(NXDOMAIN_QUERY_FAILED_EXCEPTION);
/*     */       } 
/*     */     } finally {
/* 426 */       ReferenceCountUtil.safeRelease(envelope);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean handleRedirect(DnsQuestion question, AddressedEnvelope<DnsResponse, InetSocketAddress> envelope, DnsQueryLifecycleObserver queryLifecycleObserver, Promise<List<T>> promise) {
/* 436 */     DnsResponse res = (DnsResponse)envelope.content();
/*     */ 
/*     */     
/* 439 */     if (res.count(DnsSection.ANSWER) == 0) {
/* 440 */       AuthoritativeNameServerList serverNames = extractAuthoritativeNameServers(question.name(), res);
/*     */       
/* 442 */       if (serverNames != null) {
/* 443 */         List<InetSocketAddress> nameServers = new ArrayList<InetSocketAddress>(serverNames.size());
/* 444 */         int additionalCount = res.count(DnsSection.ADDITIONAL);
/*     */         
/* 446 */         for (int i = 0; i < additionalCount; i++) {
/* 447 */           DnsRecord r = res.recordAt(DnsSection.ADDITIONAL, i);
/*     */           
/* 449 */           if ((r.type() != DnsRecordType.A || this.parent.supportsARecords()) && (r
/* 450 */             .type() != DnsRecordType.AAAA || this.parent.supportsAAAARecords())) {
/*     */ 
/*     */ 
/*     */             
/* 454 */             String recordName = r.name();
/* 455 */             AuthoritativeNameServer authoritativeNameServer = serverNames.remove(recordName);
/*     */             
/* 457 */             if (authoritativeNameServer != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 462 */               InetAddress resolved = DnsAddressDecoder.decodeAddress(r, recordName, this.parent.isDecodeIdn());
/* 463 */               if (resolved != null)
/*     */               
/*     */               { 
/*     */ 
/*     */                 
/* 468 */                 nameServers.add(new InetSocketAddress(resolved, this.parent.dnsRedirectPort(resolved)));
/* 469 */                 addNameServerToCache(authoritativeNameServer, resolved, r.timeToLive()); } 
/*     */             } 
/*     */           } 
/* 472 */         }  if (!nameServers.isEmpty()) {
/* 473 */           query(this.parent.uncachedRedirectDnsServerStream(nameServers), 0, question, queryLifecycleObserver
/* 474 */               .queryRedirected(Collections.unmodifiableList(nameServers)), promise, null);
/* 475 */           return true;
/*     */         } 
/*     */       } 
/*     */     } 
/* 479 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static AuthoritativeNameServerList extractAuthoritativeNameServers(String questionName, DnsResponse res) {
/* 487 */     int authorityCount = res.count(DnsSection.AUTHORITY);
/* 488 */     if (authorityCount == 0) {
/* 489 */       return null;
/*     */     }
/*     */     
/* 492 */     AuthoritativeNameServerList serverNames = new AuthoritativeNameServerList(questionName);
/* 493 */     for (int i = 0; i < authorityCount; i++) {
/* 494 */       serverNames.add(res.recordAt(DnsSection.AUTHORITY, i));
/*     */     }
/* 496 */     return serverNames;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void onExpectedResponse(DnsQuestion question, AddressedEnvelope<DnsResponse, InetSocketAddress> envelope, DnsQueryLifecycleObserver queryLifecycleObserver, Promise<List<T>> promise) {
/* 504 */     DnsResponse response = (DnsResponse)envelope.content();
/* 505 */     Map<String, String> cnames = buildAliasMap(response);
/* 506 */     int answerCount = response.count(DnsSection.ANSWER);
/*     */     
/* 508 */     boolean found = false;
/* 509 */     for (int i = 0; i < answerCount; i++) {
/* 510 */       DnsRecord r = response.recordAt(DnsSection.ANSWER, i);
/* 511 */       DnsRecordType type = r.type();
/* 512 */       boolean matches = false;
/* 513 */       for (DnsRecordType expectedType : this.expectedTypes) {
/* 514 */         if (type == expectedType) {
/* 515 */           matches = true;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 520 */       if (!matches) {
/*     */         continue;
/*     */       }
/*     */       
/* 524 */       String questionName = question.name().toLowerCase(Locale.US);
/* 525 */       String recordName = r.name().toLowerCase(Locale.US);
/*     */ 
/*     */       
/* 528 */       if (!recordName.equals(questionName)) {
/*     */         
/* 530 */         String resolved = questionName;
/*     */         do {
/* 532 */           resolved = cnames.get(resolved);
/* 533 */           if (recordName.equals(resolved)) {
/*     */             break;
/*     */           }
/* 536 */         } while (resolved != null);
/*     */         
/* 538 */         if (resolved == null) {
/*     */           continue;
/*     */         }
/*     */       } 
/*     */       
/* 543 */       T converted = convertRecord(r, this.hostname, this.additionals, this.parent.ch.eventLoop());
/* 544 */       if (converted != null) {
/*     */ 
/*     */ 
/*     */         
/* 548 */         if (this.finalResult == null) {
/* 549 */           this.finalResult = new ArrayList<T>(8);
/*     */         }
/* 551 */         this.finalResult.add(converted);
/*     */         
/* 553 */         cache(this.hostname, this.additionals, r, converted);
/* 554 */         found = true;
/*     */       } 
/*     */       
/*     */       continue;
/*     */     } 
/* 559 */     if (cnames.isEmpty()) {
/* 560 */       if (found) {
/* 561 */         queryLifecycleObserver.querySucceed();
/*     */         return;
/*     */       } 
/* 564 */       queryLifecycleObserver.queryFailed(NO_MATCHING_RECORD_QUERY_FAILED_EXCEPTION);
/*     */     } else {
/* 566 */       queryLifecycleObserver.querySucceed();
/*     */       
/* 568 */       onResponseCNAME(question, cnames, this.parent
/* 569 */           .dnsQueryLifecycleObserverFactory().newDnsQueryLifecycleObserver(question), promise);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void onResponseCNAME(DnsQuestion question, Map<String, String> cnames, DnsQueryLifecycleObserver queryLifecycleObserver, Promise<List<T>> promise) {
/* 579 */     String resolved = question.name().toLowerCase(Locale.US);
/* 580 */     boolean found = false;
/* 581 */     while (!cnames.isEmpty()) {
/*     */ 
/*     */       
/* 584 */       String next = cnames.remove(resolved);
/* 585 */       if (next != null) {
/* 586 */         found = true;
/* 587 */         resolved = next;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 593 */     if (found) {
/* 594 */       followCname(question, resolved, queryLifecycleObserver, promise);
/*     */     } else {
/* 596 */       queryLifecycleObserver.queryFailed(CNAME_NOT_FOUND_QUERY_FAILED_EXCEPTION);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Map<String, String> buildAliasMap(DnsResponse response) {
/* 601 */     int answerCount = response.count(DnsSection.ANSWER);
/* 602 */     Map<String, String> cnames = null;
/* 603 */     for (int i = 0; i < answerCount; i++) {
/* 604 */       DnsRecord r = response.recordAt(DnsSection.ANSWER, i);
/* 605 */       DnsRecordType type = r.type();
/* 606 */       if (type == DnsRecordType.CNAME)
/*     */       {
/*     */ 
/*     */         
/* 610 */         if (r instanceof io.netty.handler.codec.dns.DnsRawRecord) {
/*     */ 
/*     */ 
/*     */           
/* 614 */           ByteBuf recordContent = ((ByteBufHolder)r).content();
/* 615 */           String domainName = decodeDomainName(recordContent);
/* 616 */           if (domainName != null) {
/*     */ 
/*     */ 
/*     */             
/* 620 */             if (cnames == null) {
/* 621 */               cnames = new HashMap<String, String>(Math.min(8, answerCount));
/*     */             }
/*     */             
/* 624 */             cnames.put(r.name().toLowerCase(Locale.US), domainName.toLowerCase(Locale.US));
/*     */           } 
/*     */         }  } 
/* 627 */     }  return (cnames != null) ? cnames : Collections.<String, String>emptyMap();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void tryToFinishResolve(DnsServerAddressStream nameServerAddrStream, int nameServerAddrStreamIndex, DnsQuestion question, DnsQueryLifecycleObserver queryLifecycleObserver, Promise<List<T>> promise, Throwable cause) {
/* 638 */     if (!this.queriesInProgress.isEmpty()) {
/* 639 */       queryLifecycleObserver.queryCancelled(this.allowedQueries);
/*     */ 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */     
/* 647 */     if (this.finalResult == null) {
/* 648 */       if (nameServerAddrStreamIndex < nameServerAddrStream.size()) {
/* 649 */         if (queryLifecycleObserver == NoopDnsQueryLifecycleObserver.INSTANCE) {
/*     */ 
/*     */           
/* 652 */           query(nameServerAddrStream, nameServerAddrStreamIndex + 1, question, promise, cause);
/*     */         } else {
/* 654 */           query(nameServerAddrStream, nameServerAddrStreamIndex + 1, question, queryLifecycleObserver, promise, cause);
/*     */         } 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 660 */       queryLifecycleObserver.queryFailed(NAME_SERVERS_EXHAUSTED_EXCEPTION);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 667 */       if (cause == null && !this.triedCNAME) {
/*     */         
/* 669 */         this.triedCNAME = true;
/*     */         
/* 671 */         query(this.hostname, DnsRecordType.CNAME, getNameServers(this.hostname), promise);
/*     */         return;
/*     */       } 
/*     */     } else {
/* 675 */       queryLifecycleObserver.queryCancelled(this.allowedQueries);
/*     */     } 
/*     */ 
/*     */     
/* 679 */     finishResolve(promise, cause);
/*     */   }
/*     */   
/*     */   private void finishResolve(Promise<List<T>> promise, Throwable cause) {
/* 683 */     if (!this.queriesInProgress.isEmpty()) {
/*     */       
/* 685 */       Iterator<Future<AddressedEnvelope<DnsResponse, InetSocketAddress>>> i = this.queriesInProgress.iterator();
/* 686 */       while (i.hasNext()) {
/* 687 */         Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> f = i.next();
/* 688 */         i.remove();
/*     */         
/* 690 */         if (!f.cancel(false)) {
/* 691 */           f.addListener((GenericFutureListener)RELEASE_RESPONSE);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 696 */     if (this.finalResult != null) {
/*     */       
/* 698 */       DnsNameResolver.trySuccess(promise, filterResults(this.finalResult));
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 703 */     int tries = this.maxAllowedQueries - this.allowedQueries;
/* 704 */     StringBuilder buf = new StringBuilder(64);
/*     */     
/* 706 */     buf.append("failed to resolve '").append(this.hostname).append('\'');
/* 707 */     if (tries > 1) {
/* 708 */       if (tries < this.maxAllowedQueries) {
/* 709 */         buf.append(" after ")
/* 710 */           .append(tries)
/* 711 */           .append(" queries ");
/*     */       } else {
/* 713 */         buf.append(". Exceeded max queries per resolve ")
/* 714 */           .append(this.maxAllowedQueries)
/* 715 */           .append(' ');
/*     */       } 
/*     */     }
/* 718 */     UnknownHostException unknownHostException = new UnknownHostException(buf.toString());
/* 719 */     if (cause == null) {
/*     */ 
/*     */       
/* 722 */       cache(this.hostname, this.additionals, unknownHostException);
/*     */     } else {
/* 724 */       unknownHostException.initCause(cause);
/*     */     } 
/* 726 */     promise.tryFailure(unknownHostException);
/*     */   }
/*     */   
/*     */   static String decodeDomainName(ByteBuf in) {
/* 730 */     in.markReaderIndex();
/*     */     try {
/* 732 */       return DefaultDnsRecordDecoder.decodeName(in);
/* 733 */     } catch (CorruptedFrameException e) {
/*     */       
/* 735 */       return null;
/*     */     } finally {
/* 737 */       in.resetReaderIndex();
/*     */     } 
/*     */   }
/*     */   
/*     */   private DnsServerAddressStream getNameServers(String hostname) {
/* 742 */     DnsServerAddressStream stream = getNameServersFromCache(hostname);
/* 743 */     return (stream == null) ? this.nameServerAddrs.duplicate() : stream;
/*     */   }
/*     */   
/*     */   private void followCname(DnsQuestion question, String cname, DnsQueryLifecycleObserver queryLifecycleObserver, Promise<List<T>> promise) {
/*     */     DnsQuestion cnameQuestion;
/* 748 */     DnsServerAddressStream stream = getNameServers(cname);
/*     */ 
/*     */     
/*     */     try {
/* 752 */       cnameQuestion = newQuestion(cname, question.type());
/* 753 */     } catch (Throwable cause) {
/* 754 */       queryLifecycleObserver.queryFailed(cause);
/* 755 */       PlatformDependent.throwException(cause);
/*     */       return;
/*     */     } 
/* 758 */     query(stream, 0, cnameQuestion, queryLifecycleObserver.queryCNAMEd(cnameQuestion), promise, null);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean query(String hostname, DnsRecordType type, DnsServerAddressStream dnsServerAddressStream, Promise<List<T>> promise) {
/* 763 */     DnsQuestion question = newQuestion(hostname, type);
/* 764 */     if (question == null) {
/* 765 */       return false;
/*     */     }
/* 767 */     query(dnsServerAddressStream, 0, question, promise, null);
/* 768 */     return true;
/*     */   }
/*     */   
/*     */   private DnsQuestion newQuestion(String hostname, DnsRecordType type) {
/*     */     try {
/* 773 */       return (DnsQuestion)new DefaultDnsQuestion(hostname, type, this.dnsClass);
/* 774 */     } catch (IllegalArgumentException e) {
/*     */       
/* 776 */       return null;
/*     */     } 
/*     */   }
/*     */   abstract DnsResolveContext<T> newResolverContext(DnsNameResolver paramDnsNameResolver, String paramString, int paramInt, DnsRecordType[] paramArrayOfDnsRecordType, DnsRecord[] paramArrayOfDnsRecord, DnsServerAddressStream paramDnsServerAddressStream);
/*     */   
/*     */   abstract T convertRecord(DnsRecord paramDnsRecord, String paramString, DnsRecord[] paramArrayOfDnsRecord, EventLoop paramEventLoop);
/*     */   
/*     */   abstract List<T> filterResults(List<T> paramList);
/*     */   
/*     */   abstract void cache(String paramString, DnsRecord[] paramArrayOfDnsRecord, DnsRecord paramDnsRecord, T paramT);
/*     */   
/*     */   abstract void cache(String paramString, DnsRecord[] paramArrayOfDnsRecord, UnknownHostException paramUnknownHostException);
/*     */   
/*     */   private static final class AuthoritativeNameServerList { private final String questionName;
/*     */     
/*     */     AuthoritativeNameServerList(String questionName) {
/* 792 */       this.questionName = questionName.toLowerCase(Locale.US);
/*     */     }
/*     */     private DnsResolveContext.AuthoritativeNameServer head; private int count;
/*     */     void add(DnsRecord r) {
/* 796 */       if (r.type() != DnsRecordType.NS || !(r instanceof io.netty.handler.codec.dns.DnsRawRecord)) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 801 */       if (this.questionName.length() < r.name().length()) {
/*     */         return;
/*     */       }
/*     */       
/* 805 */       String recordName = r.name().toLowerCase(Locale.US);
/*     */       
/* 807 */       int dots = 0;
/* 808 */       for (int a = recordName.length() - 1, b = this.questionName.length() - 1; a >= 0; a--, b--) {
/* 809 */         char c = recordName.charAt(a);
/* 810 */         if (this.questionName.charAt(b) != c) {
/*     */           return;
/*     */         }
/* 813 */         if (c == '.') {
/* 814 */           dots++;
/*     */         }
/*     */       } 
/*     */       
/* 818 */       if (this.head != null && this.head.dots > dots) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 823 */       ByteBuf recordContent = ((ByteBufHolder)r).content();
/* 824 */       String domainName = DnsResolveContext.decodeDomainName(recordContent);
/* 825 */       if (domainName == null) {
/*     */         return;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 832 */       if (this.head == null || this.head.dots < dots) {
/* 833 */         this.count = 1;
/* 834 */         this.head = new DnsResolveContext.AuthoritativeNameServer(dots, recordName, domainName);
/* 835 */       } else if (this.head.dots == dots) {
/* 836 */         DnsResolveContext.AuthoritativeNameServer serverName = this.head;
/* 837 */         while (serverName.next != null) {
/* 838 */           serverName = serverName.next;
/*     */         }
/* 840 */         serverName.next = new DnsResolveContext.AuthoritativeNameServer(dots, recordName, domainName);
/* 841 */         this.count++;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     DnsResolveContext.AuthoritativeNameServer remove(String nsName) {
/* 848 */       DnsResolveContext.AuthoritativeNameServer serverName = this.head;
/*     */       
/* 850 */       while (serverName != null) {
/* 851 */         if (!serverName.removed && serverName.nsName.equalsIgnoreCase(nsName)) {
/* 852 */           serverName.removed = true;
/* 853 */           return serverName;
/*     */         } 
/* 855 */         serverName = serverName.next;
/*     */       } 
/* 857 */       return null;
/*     */     }
/*     */     
/*     */     int size() {
/* 861 */       return this.count;
/*     */     } }
/*     */ 
/*     */   
/*     */   static final class AuthoritativeNameServer
/*     */   {
/*     */     final int dots;
/*     */     final String nsName;
/*     */     final String domainName;
/*     */     AuthoritativeNameServer next;
/*     */     boolean removed;
/*     */     
/*     */     AuthoritativeNameServer(int dots, String domainName, String nsName) {
/* 874 */       this.dots = dots;
/* 875 */       this.nsName = nsName;
/* 876 */       this.domainName = domainName;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean isRootServer() {
/* 883 */       return (this.dots == 1);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     String domainName() {
/* 890 */       return this.domainName;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\dns\DnsResolveContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */