/*     */ package io.netty.resolver.dns;
/*     */ 
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.handler.codec.dns.DnsRecord;
/*     */ import io.netty.util.concurrent.ScheduledFuture;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.net.InetAddress;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicReference;
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
/*     */ public class DefaultDnsCache
/*     */   implements DnsCache
/*     */ {
/*  44 */   private final ConcurrentMap<String, Entries> resolveCache = PlatformDependent.newConcurrentHashMap();
/*     */ 
/*     */ 
/*     */   
/*  48 */   private static final int MAX_SUPPORTED_TTL_SECS = (int)TimeUnit.DAYS.toSeconds(730L);
/*     */   
/*     */   private final int minTtl;
/*     */   
/*     */   private final int maxTtl;
/*     */   
/*     */   private final int negativeTtl;
/*     */ 
/*     */   
/*     */   public DefaultDnsCache() {
/*  58 */     this(0, MAX_SUPPORTED_TTL_SECS, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultDnsCache(int minTtl, int maxTtl, int negativeTtl) {
/*  68 */     this.minTtl = Math.min(MAX_SUPPORTED_TTL_SECS, ObjectUtil.checkPositiveOrZero(minTtl, "minTtl"));
/*  69 */     this.maxTtl = Math.min(MAX_SUPPORTED_TTL_SECS, ObjectUtil.checkPositiveOrZero(maxTtl, "maxTtl"));
/*  70 */     if (minTtl > maxTtl) {
/*  71 */       throw new IllegalArgumentException("minTtl: " + minTtl + ", maxTtl: " + maxTtl + " (expected: 0 <= minTtl <= maxTtl)");
/*     */     }
/*     */     
/*  74 */     this.negativeTtl = ObjectUtil.checkPositiveOrZero(negativeTtl, "negativeTtl");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int minTtl() {
/*  83 */     return this.minTtl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int maxTtl() {
/*  92 */     return this.maxTtl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int negativeTtl() {
/* 100 */     return this.negativeTtl;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 105 */     while (!this.resolveCache.isEmpty()) {
/* 106 */       for (Iterator<Map.Entry<String, Entries>> i = this.resolveCache.entrySet().iterator(); i.hasNext(); ) {
/* 107 */         Map.Entry<String, Entries> e = i.next();
/* 108 */         i.remove();
/*     */         
/* 110 */         ((Entries)e.getValue()).clearAndCancel();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean clear(String hostname) {
/* 117 */     ObjectUtil.checkNotNull(hostname, "hostname");
/* 118 */     Entries entries = this.resolveCache.remove(hostname);
/* 119 */     return (entries != null && entries.clearAndCancel());
/*     */   }
/*     */   
/*     */   private static boolean emptyAdditionals(DnsRecord[] additionals) {
/* 123 */     return (additionals == null || additionals.length == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<? extends DnsCacheEntry> get(String hostname, DnsRecord[] additionals) {
/* 128 */     ObjectUtil.checkNotNull(hostname, "hostname");
/* 129 */     if (!emptyAdditionals(additionals)) {
/* 130 */       return Collections.emptyList();
/*     */     }
/*     */     
/* 133 */     Entries entries = this.resolveCache.get(hostname);
/* 134 */     return (entries == null) ? null : (List)entries.get();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DnsCacheEntry cache(String hostname, DnsRecord[] additionals, InetAddress address, long originalTtl, EventLoop loop) {
/* 140 */     ObjectUtil.checkNotNull(hostname, "hostname");
/* 141 */     ObjectUtil.checkNotNull(address, "address");
/* 142 */     ObjectUtil.checkNotNull(loop, "loop");
/* 143 */     DefaultDnsCacheEntry e = new DefaultDnsCacheEntry(hostname, address);
/* 144 */     if (this.maxTtl == 0 || !emptyAdditionals(additionals)) {
/* 145 */       return e;
/*     */     }
/* 147 */     cache0(e, Math.max(this.minTtl, Math.min(MAX_SUPPORTED_TTL_SECS, (int)Math.min(this.maxTtl, originalTtl))), loop);
/* 148 */     return e;
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsCacheEntry cache(String hostname, DnsRecord[] additionals, Throwable cause, EventLoop loop) {
/* 153 */     ObjectUtil.checkNotNull(hostname, "hostname");
/* 154 */     ObjectUtil.checkNotNull(cause, "cause");
/* 155 */     ObjectUtil.checkNotNull(loop, "loop");
/*     */     
/* 157 */     DefaultDnsCacheEntry e = new DefaultDnsCacheEntry(hostname, cause);
/* 158 */     if (this.negativeTtl == 0 || !emptyAdditionals(additionals)) {
/* 159 */       return e;
/*     */     }
/*     */     
/* 162 */     cache0(e, Math.min(MAX_SUPPORTED_TTL_SECS, this.negativeTtl), loop);
/* 163 */     return e;
/*     */   }
/*     */   
/*     */   private void cache0(DefaultDnsCacheEntry e, int ttl, EventLoop loop) {
/* 167 */     Entries entries = this.resolveCache.get(e.hostname());
/* 168 */     if (entries == null) {
/* 169 */       entries = new Entries(e);
/* 170 */       Entries oldEntries = this.resolveCache.putIfAbsent(e.hostname(), entries);
/* 171 */       if (oldEntries != null) {
/* 172 */         entries = oldEntries;
/*     */       }
/*     */     } 
/* 175 */     entries.add(e);
/*     */     
/* 177 */     scheduleCacheExpiration(e, ttl, loop);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void scheduleCacheExpiration(final DefaultDnsCacheEntry e, int ttl, EventLoop loop) {
/* 183 */     e.scheduleExpiration(loop, new Runnable()
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public void run()
/*     */           {
/* 195 */             DefaultDnsCache.Entries entries = (DefaultDnsCache.Entries)DefaultDnsCache.this.resolveCache.remove(e.hostname);
/* 196 */             if (entries != null) {
/* 197 */               entries.clearAndCancel();
/*     */             }
/*     */           }
/*     */         },  ttl, TimeUnit.SECONDS);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 205 */     return "DefaultDnsCache(minTtl=" + 
/* 206 */       this.minTtl + 
/* 207 */       ", maxTtl=" + this.maxTtl + 
/* 208 */       ", negativeTtl=" + this.negativeTtl + 
/* 209 */       ", cached resolved hostname=" + this.resolveCache
/* 210 */       .size() + ")";
/*     */   }
/*     */   
/*     */   private static final class DefaultDnsCacheEntry
/*     */     implements DnsCacheEntry {
/*     */     private final String hostname;
/*     */     private final InetAddress address;
/*     */     private final Throwable cause;
/*     */     private volatile ScheduledFuture<?> expirationFuture;
/*     */     
/*     */     DefaultDnsCacheEntry(String hostname, InetAddress address) {
/* 221 */       this.hostname = (String)ObjectUtil.checkNotNull(hostname, "hostname");
/* 222 */       this.address = (InetAddress)ObjectUtil.checkNotNull(address, "address");
/* 223 */       this.cause = null;
/*     */     }
/*     */     
/*     */     DefaultDnsCacheEntry(String hostname, Throwable cause) {
/* 227 */       this.hostname = (String)ObjectUtil.checkNotNull(hostname, "hostname");
/* 228 */       this.cause = (Throwable)ObjectUtil.checkNotNull(cause, "cause");
/* 229 */       this.address = null;
/*     */     }
/*     */ 
/*     */     
/*     */     public InetAddress address() {
/* 234 */       return this.address;
/*     */     }
/*     */ 
/*     */     
/*     */     public Throwable cause() {
/* 239 */       return this.cause;
/*     */     }
/*     */     
/*     */     String hostname() {
/* 243 */       return this.hostname;
/*     */     }
/*     */     
/*     */     void scheduleExpiration(EventLoop loop, Runnable task, long delay, TimeUnit unit) {
/* 247 */       assert this.expirationFuture == null : "expiration task scheduled already";
/* 248 */       this.expirationFuture = loop.schedule(task, delay, unit);
/*     */     }
/*     */     
/*     */     void cancelExpiration() {
/* 252 */       ScheduledFuture<?> expirationFuture = this.expirationFuture;
/* 253 */       if (expirationFuture != null) {
/* 254 */         expirationFuture.cancel(false);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 260 */       if (this.cause != null) {
/* 261 */         return this.hostname + '/' + this.cause;
/*     */       }
/* 263 */       return this.address.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class Entries
/*     */     extends AtomicReference<List<DefaultDnsCacheEntry>>
/*     */   {
/*     */     Entries(DefaultDnsCache.DefaultDnsCacheEntry entry) {
/* 272 */       super(Collections.singletonList(entry));
/*     */     }
/*     */     
/*     */     void add(DefaultDnsCache.DefaultDnsCacheEntry e) {
/* 276 */       if (e.cause() == null) {
/*     */         while (true) {
/* 278 */           List<DefaultDnsCache.DefaultDnsCacheEntry> list = get();
/* 279 */           if (!list.isEmpty()) {
/* 280 */             DefaultDnsCache.DefaultDnsCacheEntry firstEntry = list.get(0);
/* 281 */             if (firstEntry.cause() != null) {
/* 282 */               assert list.size() == 1;
/* 283 */               if (compareAndSet(list, Collections.singletonList(e))) {
/* 284 */                 firstEntry.cancelExpiration();
/*     */ 
/*     */                 
/*     */                 return;
/*     */               } 
/*     */               
/*     */               continue;
/*     */             } 
/*     */             
/* 293 */             List<DefaultDnsCache.DefaultDnsCacheEntry> newEntries = new ArrayList<DefaultDnsCache.DefaultDnsCacheEntry>(list.size() + 1);
/* 294 */             DefaultDnsCache.DefaultDnsCacheEntry replacedEntry = null;
/* 295 */             for (int i = 0; i < list.size(); i++) {
/* 296 */               DefaultDnsCache.DefaultDnsCacheEntry entry = list.get(i);
/*     */ 
/*     */ 
/*     */               
/* 300 */               if (!e.address().equals(entry.address())) {
/* 301 */                 newEntries.add(entry);
/*     */               } else {
/* 303 */                 assert replacedEntry == null;
/* 304 */                 replacedEntry = entry;
/*     */               } 
/*     */             } 
/* 307 */             newEntries.add(e);
/* 308 */             if (compareAndSet(list, newEntries)) {
/* 309 */               if (replacedEntry != null)
/* 310 */                 replacedEntry.cancelExpiration();  return;
/*     */             } 
/*     */             continue;
/*     */           } 
/* 314 */           if (compareAndSet(list, Collections.singletonList(e)))
/*     */             break; 
/*     */         } 
/*     */         return;
/*     */       } 
/* 319 */       List<DefaultDnsCache.DefaultDnsCacheEntry> entries = getAndSet(Collections.singletonList(e));
/* 320 */       cancelExpiration(entries);
/*     */     }
/*     */ 
/*     */     
/*     */     boolean clearAndCancel() {
/* 325 */       List<DefaultDnsCache.DefaultDnsCacheEntry> entries = getAndSet(Collections.emptyList());
/* 326 */       if (entries.isEmpty()) {
/* 327 */         return false;
/*     */       }
/*     */       
/* 330 */       cancelExpiration(entries);
/* 331 */       return true;
/*     */     }
/*     */     
/*     */     private static void cancelExpiration(List<DefaultDnsCache.DefaultDnsCacheEntry> entryList) {
/* 335 */       int numEntries = entryList.size();
/* 336 */       for (int i = 0; i < numEntries; i++)
/* 337 */         ((DefaultDnsCache.DefaultDnsCacheEntry)entryList.get(i)).cancelExpiration(); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\dns\DefaultDnsCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */