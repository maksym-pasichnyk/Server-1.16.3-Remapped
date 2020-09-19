/*     */ package com.google.common.eventbus;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.j2objc.annotations.Weak;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.concurrent.Executor;
/*     */ import javax.annotation.Nullable;
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
/*     */ class Subscriber
/*     */ {
/*     */   @Weak
/*     */   private EventBus bus;
/*     */   @VisibleForTesting
/*     */   final Object target;
/*     */   private final Method method;
/*     */   private final Executor executor;
/*     */   
/*     */   static Subscriber create(EventBus bus, Object listener, Method method) {
/*  41 */     return isDeclaredThreadSafe(method) ? new Subscriber(bus, listener, method) : new SynchronizedSubscriber(bus, listener, method);
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
/*     */   private Subscriber(EventBus bus, Object target, Method method) {
/*  59 */     this.bus = bus;
/*  60 */     this.target = Preconditions.checkNotNull(target);
/*  61 */     this.method = method;
/*  62 */     method.setAccessible(true);
/*     */     
/*  64 */     this.executor = bus.executor();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void dispatchEvent(final Object event) {
/*  71 */     this.executor.execute(new Runnable()
/*     */         {
/*     */           public void run()
/*     */           {
/*     */             try {
/*  76 */               Subscriber.this.invokeSubscriberMethod(event);
/*  77 */             } catch (InvocationTargetException e) {
/*  78 */               Subscriber.this.bus.handleSubscriberException(e.getCause(), Subscriber.this.context(event));
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   void invokeSubscriberMethod(Object event) throws InvocationTargetException {
/*     */     try {
/*  91 */       this.method.invoke(this.target, new Object[] { Preconditions.checkNotNull(event) });
/*  92 */     } catch (IllegalArgumentException e) {
/*  93 */       throw new Error("Method rejected target/argument: " + event, e);
/*  94 */     } catch (IllegalAccessException e) {
/*  95 */       throw new Error("Method became inaccessible: " + event, e);
/*  96 */     } catch (InvocationTargetException e) {
/*  97 */       if (e.getCause() instanceof Error) {
/*  98 */         throw (Error)e.getCause();
/*     */       }
/* 100 */       throw e;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SubscriberExceptionContext context(Object event) {
/* 108 */     return new SubscriberExceptionContext(this.bus, event, this.target, this.method);
/*     */   }
/*     */ 
/*     */   
/*     */   public final int hashCode() {
/* 113 */     return (31 + this.method.hashCode()) * 31 + System.identityHashCode(this.target);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean equals(@Nullable Object obj) {
/* 118 */     if (obj instanceof Subscriber) {
/* 119 */       Subscriber that = (Subscriber)obj;
/*     */ 
/*     */ 
/*     */       
/* 123 */       return (this.target == that.target && this.method.equals(that.method));
/*     */     } 
/* 125 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isDeclaredThreadSafe(Method method) {
/* 133 */     return (method.getAnnotation(AllowConcurrentEvents.class) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static final class SynchronizedSubscriber
/*     */     extends Subscriber
/*     */   {
/*     */     private SynchronizedSubscriber(EventBus bus, Object target, Method method) {
/* 144 */       super(bus, target, method);
/*     */     }
/*     */ 
/*     */     
/*     */     void invokeSubscriberMethod(Object event) throws InvocationTargetException {
/* 149 */       synchronized (this) {
/* 150 */         super.invokeSubscriberMethod(event);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\eventbus\Subscriber.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */