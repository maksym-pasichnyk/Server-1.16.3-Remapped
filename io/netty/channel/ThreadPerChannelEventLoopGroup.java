/*     */ package io.netty.channel;
/*     */ 
/*     */ import io.netty.util.concurrent.AbstractEventExecutorGroup;
/*     */ import io.netty.util.concurrent.DefaultPromise;
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.FutureListener;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import io.netty.util.concurrent.GlobalEventExecutor;
/*     */ import io.netty.util.concurrent.Promise;
/*     */ import io.netty.util.concurrent.ThreadPerTaskExecutor;
/*     */ import io.netty.util.internal.EmptyArrays;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.ReadOnlyIterator;
/*     */ import io.netty.util.internal.ThrowableUtil;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ public class ThreadPerChannelEventLoopGroup
/*     */   extends AbstractEventExecutorGroup
/*     */   implements EventLoopGroup
/*     */ {
/*     */   private final Object[] childArgs;
/*     */   private final int maxChannels;
/*     */   final Executor executor;
/*  52 */   final Set<EventLoop> activeChildren = Collections.newSetFromMap(PlatformDependent.newConcurrentHashMap());
/*  53 */   final Queue<EventLoop> idleChildren = new ConcurrentLinkedQueue<EventLoop>();
/*     */   
/*     */   private final ChannelException tooManyChannels;
/*     */   private volatile boolean shuttingDown;
/*  57 */   private final Promise<?> terminationFuture = (Promise<?>)new DefaultPromise((EventExecutor)GlobalEventExecutor.INSTANCE);
/*  58 */   private final FutureListener<Object> childTerminationListener = new FutureListener<Object>()
/*     */     {
/*     */       public void operationComplete(Future<Object> future) throws Exception
/*     */       {
/*  62 */         if (ThreadPerChannelEventLoopGroup.this.isTerminated()) {
/*  63 */           ThreadPerChannelEventLoopGroup.this.terminationFuture.trySuccess(null);
/*     */         }
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ThreadPerChannelEventLoopGroup() {
/*  72 */     this(0);
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
/*     */   protected ThreadPerChannelEventLoopGroup(int maxChannels) {
/*  85 */     this(maxChannels, Executors.defaultThreadFactory(), new Object[0]);
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
/*     */   protected ThreadPerChannelEventLoopGroup(int maxChannels, ThreadFactory threadFactory, Object... args) {
/* 101 */     this(maxChannels, (Executor)new ThreadPerTaskExecutor(threadFactory), args);
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
/*     */   protected ThreadPerChannelEventLoopGroup(int maxChannels, Executor executor, Object... args) {
/* 117 */     if (maxChannels < 0)
/* 118 */       throw new IllegalArgumentException(String.format("maxChannels: %d (expected: >= 0)", new Object[] {
/* 119 */               Integer.valueOf(maxChannels)
/*     */             })); 
/* 121 */     if (executor == null) {
/* 122 */       throw new NullPointerException("executor");
/*     */     }
/*     */     
/* 125 */     if (args == null) {
/* 126 */       this.childArgs = EmptyArrays.EMPTY_OBJECTS;
/*     */     } else {
/* 128 */       this.childArgs = (Object[])args.clone();
/*     */     } 
/*     */     
/* 131 */     this.maxChannels = maxChannels;
/* 132 */     this.executor = executor;
/*     */     
/* 134 */     this.tooManyChannels = (ChannelException)ThrowableUtil.unknownStackTrace(new ChannelException("too many channels (max: " + maxChannels + ')'), ThreadPerChannelEventLoopGroup.class, "nextChild()");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected EventLoop newChild(Object... args) throws Exception {
/* 143 */     return new ThreadPerChannelEventLoop(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<EventExecutor> iterator() {
/* 148 */     return (Iterator<EventExecutor>)new ReadOnlyIterator(this.activeChildren.iterator());
/*     */   }
/*     */ 
/*     */   
/*     */   public EventLoop next() {
/* 153 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public Future<?> shutdownGracefully(long quietPeriod, long timeout, TimeUnit unit) {
/* 158 */     this.shuttingDown = true;
/*     */     
/* 160 */     for (EventLoop l : this.activeChildren) {
/* 161 */       l.shutdownGracefully(quietPeriod, timeout, unit);
/*     */     }
/* 163 */     for (EventLoop l : this.idleChildren) {
/* 164 */       l.shutdownGracefully(quietPeriod, timeout, unit);
/*     */     }
/*     */ 
/*     */     
/* 168 */     if (isTerminated()) {
/* 169 */       this.terminationFuture.trySuccess(null);
/*     */     }
/*     */     
/* 172 */     return terminationFuture();
/*     */   }
/*     */ 
/*     */   
/*     */   public Future<?> terminationFuture() {
/* 177 */     return (Future<?>)this.terminationFuture;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void shutdown() {
/* 183 */     this.shuttingDown = true;
/*     */     
/* 185 */     for (EventLoop l : this.activeChildren) {
/* 186 */       l.shutdown();
/*     */     }
/* 188 */     for (EventLoop l : this.idleChildren) {
/* 189 */       l.shutdown();
/*     */     }
/*     */ 
/*     */     
/* 193 */     if (isTerminated()) {
/* 194 */       this.terminationFuture.trySuccess(null);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isShuttingDown() {
/* 200 */     for (EventLoop l : this.activeChildren) {
/* 201 */       if (!l.isShuttingDown()) {
/* 202 */         return false;
/*     */       }
/*     */     } 
/* 205 */     for (EventLoop l : this.idleChildren) {
/* 206 */       if (!l.isShuttingDown()) {
/* 207 */         return false;
/*     */       }
/*     */     } 
/* 210 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isShutdown() {
/* 215 */     for (EventLoop l : this.activeChildren) {
/* 216 */       if (!l.isShutdown()) {
/* 217 */         return false;
/*     */       }
/*     */     } 
/* 220 */     for (EventLoop l : this.idleChildren) {
/* 221 */       if (!l.isShutdown()) {
/* 222 */         return false;
/*     */       }
/*     */     } 
/* 225 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTerminated() {
/* 230 */     for (EventLoop l : this.activeChildren) {
/* 231 */       if (!l.isTerminated()) {
/* 232 */         return false;
/*     */       }
/*     */     } 
/* 235 */     for (EventLoop l : this.idleChildren) {
/* 236 */       if (!l.isTerminated()) {
/* 237 */         return false;
/*     */       }
/*     */     } 
/* 240 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
/* 246 */     long deadline = System.nanoTime() + unit.toNanos(timeout);
/* 247 */     label26: for (EventLoop l : this.activeChildren) {
/*     */       while (true) {
/* 249 */         long timeLeft = deadline - System.nanoTime();
/* 250 */         if (timeLeft <= 0L) {
/* 251 */           return isTerminated();
/*     */         }
/* 253 */         if (l.awaitTermination(timeLeft, TimeUnit.NANOSECONDS)) {
/*     */           continue label26;
/*     */         }
/*     */       } 
/*     */     } 
/* 258 */     label27: for (EventLoop l : this.idleChildren) {
/*     */       while (true) {
/* 260 */         long timeLeft = deadline - System.nanoTime();
/* 261 */         if (timeLeft <= 0L) {
/* 262 */           return isTerminated();
/*     */         }
/* 264 */         if (l.awaitTermination(timeLeft, TimeUnit.NANOSECONDS)) {
/*     */           continue label27;
/*     */         }
/*     */       } 
/*     */     } 
/* 269 */     return isTerminated();
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture register(Channel channel) {
/* 274 */     if (channel == null) {
/* 275 */       throw new NullPointerException("channel");
/*     */     }
/*     */     try {
/* 278 */       EventLoop l = nextChild();
/* 279 */       return l.register(new DefaultChannelPromise(channel, (EventExecutor)l));
/* 280 */     } catch (Throwable t) {
/* 281 */       return new FailedChannelFuture(channel, (EventExecutor)GlobalEventExecutor.INSTANCE, t);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture register(ChannelPromise promise) {
/*     */     try {
/* 288 */       return nextChild().register(promise);
/* 289 */     } catch (Throwable t) {
/* 290 */       promise.setFailure(t);
/* 291 */       return promise;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public ChannelFuture register(Channel channel, ChannelPromise promise) {
/* 298 */     if (channel == null) {
/* 299 */       throw new NullPointerException("channel");
/*     */     }
/*     */     try {
/* 302 */       return nextChild().register(channel, promise);
/* 303 */     } catch (Throwable t) {
/* 304 */       promise.setFailure(t);
/* 305 */       return promise;
/*     */     } 
/*     */   }
/*     */   
/*     */   private EventLoop nextChild() throws Exception {
/* 310 */     if (this.shuttingDown) {
/* 311 */       throw new RejectedExecutionException("shutting down");
/*     */     }
/*     */     
/* 314 */     EventLoop loop = this.idleChildren.poll();
/* 315 */     if (loop == null) {
/* 316 */       if (this.maxChannels > 0 && this.activeChildren.size() >= this.maxChannels) {
/* 317 */         throw this.tooManyChannels;
/*     */       }
/* 319 */       loop = newChild(this.childArgs);
/* 320 */       loop.terminationFuture().addListener((GenericFutureListener)this.childTerminationListener);
/*     */     } 
/* 322 */     this.activeChildren.add(loop);
/* 323 */     return loop;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\ThreadPerChannelEventLoopGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */