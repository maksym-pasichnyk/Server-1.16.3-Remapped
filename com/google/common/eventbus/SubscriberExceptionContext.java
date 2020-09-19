/*    */ package com.google.common.eventbus;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.lang.reflect.Method;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SubscriberExceptionContext
/*    */ {
/*    */   private final EventBus eventBus;
/*    */   private final Object event;
/*    */   private final Object subscriber;
/*    */   private final Method subscriberMethod;
/*    */   
/*    */   SubscriberExceptionContext(EventBus eventBus, Object event, Object subscriber, Method subscriberMethod) {
/* 40 */     this.eventBus = (EventBus)Preconditions.checkNotNull(eventBus);
/* 41 */     this.event = Preconditions.checkNotNull(event);
/* 42 */     this.subscriber = Preconditions.checkNotNull(subscriber);
/* 43 */     this.subscriberMethod = (Method)Preconditions.checkNotNull(subscriberMethod);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public EventBus getEventBus() {
/* 51 */     return this.eventBus;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getEvent() {
/* 58 */     return this.event;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getSubscriber() {
/* 65 */     return this.subscriber;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Method getSubscriberMethod() {
/* 72 */     return this.subscriberMethod;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\eventbus\SubscriberExceptionContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */