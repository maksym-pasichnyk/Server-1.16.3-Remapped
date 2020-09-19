/*     */ package io.netty.channel.group;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufHolder;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelId;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ public class DefaultChannelGroup
/*     */   extends AbstractSet<Channel>
/*     */   implements ChannelGroup
/*     */ {
/*  44 */   private static final AtomicInteger nextId = new AtomicInteger();
/*     */   private final String name;
/*     */   private final EventExecutor executor;
/*  47 */   private final ConcurrentMap<ChannelId, Channel> serverChannels = PlatformDependent.newConcurrentHashMap();
/*  48 */   private final ConcurrentMap<ChannelId, Channel> nonServerChannels = PlatformDependent.newConcurrentHashMap();
/*  49 */   private final ChannelFutureListener remover = new ChannelFutureListener()
/*     */     {
/*     */       public void operationComplete(ChannelFuture future) throws Exception {
/*  52 */         DefaultChannelGroup.this.remove(future.channel());
/*     */       }
/*     */     };
/*  55 */   private final VoidChannelGroupFuture voidFuture = new VoidChannelGroupFuture(this);
/*     */ 
/*     */   
/*     */   private final boolean stayClosed;
/*     */   
/*     */   private volatile boolean closed;
/*     */ 
/*     */   
/*     */   public DefaultChannelGroup(EventExecutor executor) {
/*  64 */     this(executor, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultChannelGroup(String name, EventExecutor executor) {
/*  73 */     this(name, executor, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultChannelGroup(EventExecutor executor, boolean stayClosed) {
/*  83 */     this("group-0x" + Integer.toHexString(nextId.incrementAndGet()), executor, stayClosed);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultChannelGroup(String name, EventExecutor executor, boolean stayClosed) {
/*  94 */     if (name == null) {
/*  95 */       throw new NullPointerException("name");
/*     */     }
/*  97 */     this.name = name;
/*  98 */     this.executor = executor;
/*  99 */     this.stayClosed = stayClosed;
/*     */   }
/*     */ 
/*     */   
/*     */   public String name() {
/* 104 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public Channel find(ChannelId id) {
/* 109 */     Channel c = this.nonServerChannels.get(id);
/* 110 */     if (c != null) {
/* 111 */       return c;
/*     */     }
/* 113 */     return this.serverChannels.get(id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 119 */     return (this.nonServerChannels.isEmpty() && this.serverChannels.isEmpty());
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 124 */     return this.nonServerChannels.size() + this.serverChannels.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object o) {
/* 129 */     if (o instanceof io.netty.channel.ServerChannel)
/* 130 */       return this.serverChannels.containsValue(o); 
/* 131 */     if (o instanceof Channel) {
/* 132 */       return this.nonServerChannels.containsValue(o);
/*     */     }
/* 134 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean add(Channel channel) {
/* 139 */     ConcurrentMap<ChannelId, Channel> map = (channel instanceof io.netty.channel.ServerChannel) ? this.serverChannels : this.nonServerChannels;
/*     */ 
/*     */     
/* 142 */     boolean added = (map.putIfAbsent(channel.id(), channel) == null);
/* 143 */     if (added) {
/* 144 */       channel.closeFuture().addListener((GenericFutureListener)this.remover);
/*     */     }
/*     */     
/* 147 */     if (this.stayClosed && this.closed)
/*     */     {
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
/* 160 */       channel.close();
/*     */     }
/*     */     
/* 163 */     return added;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(Object o) {
/* 168 */     Channel c = null;
/* 169 */     if (o instanceof ChannelId) {
/* 170 */       c = this.nonServerChannels.remove(o);
/* 171 */       if (c == null) {
/* 172 */         c = this.serverChannels.remove(o);
/*     */       }
/* 174 */     } else if (o instanceof Channel) {
/* 175 */       c = (Channel)o;
/* 176 */       if (c instanceof io.netty.channel.ServerChannel) {
/* 177 */         c = this.serverChannels.remove(c.id());
/*     */       } else {
/* 179 */         c = this.nonServerChannels.remove(c.id());
/*     */       } 
/*     */     } 
/*     */     
/* 183 */     if (c == null) {
/* 184 */       return false;
/*     */     }
/*     */     
/* 187 */     c.closeFuture().removeListener((GenericFutureListener)this.remover);
/* 188 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 193 */     this.nonServerChannels.clear();
/* 194 */     this.serverChannels.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<Channel> iterator() {
/* 199 */     return new CombinedIterator<Channel>(this.serverChannels
/* 200 */         .values().iterator(), this.nonServerChannels
/* 201 */         .values().iterator());
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 206 */     Collection<Channel> channels = new ArrayList<Channel>(size());
/* 207 */     channels.addAll(this.serverChannels.values());
/* 208 */     channels.addAll(this.nonServerChannels.values());
/* 209 */     return channels.toArray();
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T[] toArray(T[] a) {
/* 214 */     Collection<Channel> channels = new ArrayList<Channel>(size());
/* 215 */     channels.addAll(this.serverChannels.values());
/* 216 */     channels.addAll(this.nonServerChannels.values());
/* 217 */     return channels.toArray(a);
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelGroupFuture close() {
/* 222 */     return close(ChannelMatchers.all());
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelGroupFuture disconnect() {
/* 227 */     return disconnect(ChannelMatchers.all());
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelGroupFuture deregister() {
/* 232 */     return deregister(ChannelMatchers.all());
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelGroupFuture write(Object message) {
/* 237 */     return write(message, ChannelMatchers.all());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Object safeDuplicate(Object message) {
/* 243 */     if (message instanceof ByteBuf)
/* 244 */       return ((ByteBuf)message).retainedDuplicate(); 
/* 245 */     if (message instanceof ByteBufHolder) {
/* 246 */       return ((ByteBufHolder)message).retainedDuplicate();
/*     */     }
/* 248 */     return ReferenceCountUtil.retain(message);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelGroupFuture write(Object message, ChannelMatcher matcher) {
/* 254 */     return write(message, matcher, false);
/*     */   }
/*     */   
/*     */   public ChannelGroupFuture write(Object message, ChannelMatcher matcher, boolean voidPromise) {
/*     */     ChannelGroupFuture future;
/* 259 */     if (message == null) {
/* 260 */       throw new NullPointerException("message");
/*     */     }
/* 262 */     if (matcher == null) {
/* 263 */       throw new NullPointerException("matcher");
/*     */     }
/*     */ 
/*     */     
/* 267 */     if (voidPromise) {
/* 268 */       for (Channel c : this.nonServerChannels.values()) {
/* 269 */         if (matcher.matches(c)) {
/* 270 */           c.write(safeDuplicate(message), c.voidPromise());
/*     */         }
/*     */       } 
/* 273 */       future = this.voidFuture;
/*     */     } else {
/* 275 */       Map<Channel, ChannelFuture> futures = new LinkedHashMap<Channel, ChannelFuture>(size());
/* 276 */       for (Channel c : this.nonServerChannels.values()) {
/* 277 */         if (matcher.matches(c)) {
/* 278 */           futures.put(c, c.write(safeDuplicate(message)));
/*     */         }
/*     */       } 
/* 281 */       future = new DefaultChannelGroupFuture(this, futures, this.executor);
/*     */     } 
/* 283 */     ReferenceCountUtil.release(message);
/* 284 */     return future;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelGroup flush() {
/* 289 */     return flush(ChannelMatchers.all());
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelGroupFuture flushAndWrite(Object message) {
/* 294 */     return writeAndFlush(message);
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelGroupFuture writeAndFlush(Object message) {
/* 299 */     return writeAndFlush(message, ChannelMatchers.all());
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelGroupFuture disconnect(ChannelMatcher matcher) {
/* 304 */     if (matcher == null) {
/* 305 */       throw new NullPointerException("matcher");
/*     */     }
/*     */ 
/*     */     
/* 309 */     Map<Channel, ChannelFuture> futures = new LinkedHashMap<Channel, ChannelFuture>(size());
/*     */     
/* 311 */     for (Channel c : this.serverChannels.values()) {
/* 312 */       if (matcher.matches(c)) {
/* 313 */         futures.put(c, c.disconnect());
/*     */       }
/*     */     } 
/* 316 */     for (Channel c : this.nonServerChannels.values()) {
/* 317 */       if (matcher.matches(c)) {
/* 318 */         futures.put(c, c.disconnect());
/*     */       }
/*     */     } 
/*     */     
/* 322 */     return new DefaultChannelGroupFuture(this, futures, this.executor);
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelGroupFuture close(ChannelMatcher matcher) {
/* 327 */     if (matcher == null) {
/* 328 */       throw new NullPointerException("matcher");
/*     */     }
/*     */ 
/*     */     
/* 332 */     Map<Channel, ChannelFuture> futures = new LinkedHashMap<Channel, ChannelFuture>(size());
/*     */     
/* 334 */     if (this.stayClosed)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 341 */       this.closed = true;
/*     */     }
/*     */     
/* 344 */     for (Channel c : this.serverChannels.values()) {
/* 345 */       if (matcher.matches(c)) {
/* 346 */         futures.put(c, c.close());
/*     */       }
/*     */     } 
/* 349 */     for (Channel c : this.nonServerChannels.values()) {
/* 350 */       if (matcher.matches(c)) {
/* 351 */         futures.put(c, c.close());
/*     */       }
/*     */     } 
/*     */     
/* 355 */     return new DefaultChannelGroupFuture(this, futures, this.executor);
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelGroupFuture deregister(ChannelMatcher matcher) {
/* 360 */     if (matcher == null) {
/* 361 */       throw new NullPointerException("matcher");
/*     */     }
/*     */ 
/*     */     
/* 365 */     Map<Channel, ChannelFuture> futures = new LinkedHashMap<Channel, ChannelFuture>(size());
/*     */     
/* 367 */     for (Channel c : this.serverChannels.values()) {
/* 368 */       if (matcher.matches(c)) {
/* 369 */         futures.put(c, c.deregister());
/*     */       }
/*     */     } 
/* 372 */     for (Channel c : this.nonServerChannels.values()) {
/* 373 */       if (matcher.matches(c)) {
/* 374 */         futures.put(c, c.deregister());
/*     */       }
/*     */     } 
/*     */     
/* 378 */     return new DefaultChannelGroupFuture(this, futures, this.executor);
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelGroup flush(ChannelMatcher matcher) {
/* 383 */     for (Channel c : this.nonServerChannels.values()) {
/* 384 */       if (matcher.matches(c)) {
/* 385 */         c.flush();
/*     */       }
/*     */     } 
/* 388 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelGroupFuture flushAndWrite(Object message, ChannelMatcher matcher) {
/* 393 */     return writeAndFlush(message, matcher);
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelGroupFuture writeAndFlush(Object message, ChannelMatcher matcher) {
/* 398 */     return writeAndFlush(message, matcher, false);
/*     */   }
/*     */   
/*     */   public ChannelGroupFuture writeAndFlush(Object message, ChannelMatcher matcher, boolean voidPromise) {
/*     */     ChannelGroupFuture future;
/* 403 */     if (message == null) {
/* 404 */       throw new NullPointerException("message");
/*     */     }
/*     */ 
/*     */     
/* 408 */     if (voidPromise) {
/* 409 */       for (Channel c : this.nonServerChannels.values()) {
/* 410 */         if (matcher.matches(c)) {
/* 411 */           c.writeAndFlush(safeDuplicate(message), c.voidPromise());
/*     */         }
/*     */       } 
/* 414 */       future = this.voidFuture;
/*     */     } else {
/* 416 */       Map<Channel, ChannelFuture> futures = new LinkedHashMap<Channel, ChannelFuture>(size());
/* 417 */       for (Channel c : this.nonServerChannels.values()) {
/* 418 */         if (matcher.matches(c)) {
/* 419 */           futures.put(c, c.writeAndFlush(safeDuplicate(message)));
/*     */         }
/*     */       } 
/* 422 */       future = new DefaultChannelGroupFuture(this, futures, this.executor);
/*     */     } 
/* 424 */     ReferenceCountUtil.release(message);
/* 425 */     return future;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelGroupFuture newCloseFuture() {
/* 430 */     return newCloseFuture(ChannelMatchers.all());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelGroupFuture newCloseFuture(ChannelMatcher matcher) {
/* 436 */     Map<Channel, ChannelFuture> futures = new LinkedHashMap<Channel, ChannelFuture>(size());
/*     */     
/* 438 */     for (Channel c : this.serverChannels.values()) {
/* 439 */       if (matcher.matches(c)) {
/* 440 */         futures.put(c, c.closeFuture());
/*     */       }
/*     */     } 
/* 443 */     for (Channel c : this.nonServerChannels.values()) {
/* 444 */       if (matcher.matches(c)) {
/* 445 */         futures.put(c, c.closeFuture());
/*     */       }
/*     */     } 
/*     */     
/* 449 */     return new DefaultChannelGroupFuture(this, futures, this.executor);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 454 */     return System.identityHashCode(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 459 */     return (this == o);
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(ChannelGroup o) {
/* 464 */     int v = name().compareTo(o.name());
/* 465 */     if (v != 0) {
/* 466 */       return v;
/*     */     }
/*     */     
/* 469 */     return System.identityHashCode(this) - System.identityHashCode(o);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 474 */     return StringUtil.simpleClassName(this) + "(name: " + name() + ", size: " + size() + ')';
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\group\DefaultChannelGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */