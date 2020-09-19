/*     */ package com.google.common.eventbus;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.util.concurrent.MoreExecutors;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Iterator;
/*     */ import java.util.Locale;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ @Beta
/*     */ public class EventBus
/*     */ {
/*  97 */   private static final Logger logger = Logger.getLogger(EventBus.class.getName());
/*     */   
/*     */   private final String identifier;
/*     */   
/*     */   private final Executor executor;
/*     */   private final SubscriberExceptionHandler exceptionHandler;
/* 103 */   private final SubscriberRegistry subscribers = new SubscriberRegistry(this);
/*     */ 
/*     */   
/*     */   private final Dispatcher dispatcher;
/*     */ 
/*     */   
/*     */   public EventBus() {
/* 110 */     this("default");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EventBus(String identifier) {
/* 120 */     this(identifier, 
/*     */         
/* 122 */         MoreExecutors.directExecutor(), 
/* 123 */         Dispatcher.perThreadDispatchQueue(), LoggingHandler.INSTANCE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EventBus(SubscriberExceptionHandler exceptionHandler) {
/* 134 */     this("default", 
/*     */         
/* 136 */         MoreExecutors.directExecutor(), 
/* 137 */         Dispatcher.perThreadDispatchQueue(), exceptionHandler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   EventBus(String identifier, Executor executor, Dispatcher dispatcher, SubscriberExceptionHandler exceptionHandler) {
/* 146 */     this.identifier = (String)Preconditions.checkNotNull(identifier);
/* 147 */     this.executor = (Executor)Preconditions.checkNotNull(executor);
/* 148 */     this.dispatcher = (Dispatcher)Preconditions.checkNotNull(dispatcher);
/* 149 */     this.exceptionHandler = (SubscriberExceptionHandler)Preconditions.checkNotNull(exceptionHandler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String identifier() {
/* 158 */     return this.identifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final Executor executor() {
/* 165 */     return this.executor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void handleSubscriberException(Throwable e, SubscriberExceptionContext context) {
/* 172 */     Preconditions.checkNotNull(e);
/* 173 */     Preconditions.checkNotNull(context);
/*     */     try {
/* 175 */       this.exceptionHandler.handleException(e, context);
/* 176 */     } catch (Throwable e2) {
/*     */       
/* 178 */       logger.log(Level.SEVERE, 
/*     */           
/* 180 */           String.format(Locale.ROOT, "Exception %s thrown while handling exception: %s", new Object[] { e2, e }), e2);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void register(Object object) {
/* 191 */     this.subscribers.register(object);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unregister(Object object) {
/* 201 */     this.subscribers.unregister(object);
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
/*     */   public void post(Object event) {
/* 215 */     Iterator<Subscriber> eventSubscribers = this.subscribers.getSubscribers(event);
/* 216 */     if (eventSubscribers.hasNext()) {
/* 217 */       this.dispatcher.dispatch(event, eventSubscribers);
/* 218 */     } else if (!(event instanceof DeadEvent)) {
/*     */       
/* 220 */       post(new DeadEvent(this, event));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 226 */     return MoreObjects.toStringHelper(this).addValue(this.identifier).toString();
/*     */   }
/*     */ 
/*     */   
/*     */   static final class LoggingHandler
/*     */     implements SubscriberExceptionHandler
/*     */   {
/* 233 */     static final LoggingHandler INSTANCE = new LoggingHandler();
/*     */ 
/*     */     
/*     */     public void handleException(Throwable exception, SubscriberExceptionContext context) {
/* 237 */       Logger logger = logger(context);
/* 238 */       if (logger.isLoggable(Level.SEVERE)) {
/* 239 */         logger.log(Level.SEVERE, message(context), exception);
/*     */       }
/*     */     }
/*     */     
/*     */     private static Logger logger(SubscriberExceptionContext context) {
/* 244 */       return Logger.getLogger(EventBus.class.getName() + "." + context.getEventBus().identifier());
/*     */     }
/*     */     
/*     */     private static String message(SubscriberExceptionContext context) {
/* 248 */       Method method = context.getSubscriberMethod();
/* 249 */       return "Exception thrown by subscriber method " + method
/* 250 */         .getName() + '(' + method
/*     */         
/* 252 */         .getParameterTypes()[0].getName() + ')' + " on subscriber " + context
/*     */ 
/*     */         
/* 255 */         .getSubscriber() + " when dispatching event: " + context
/*     */         
/* 257 */         .getEvent();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\eventbus\EventBus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */