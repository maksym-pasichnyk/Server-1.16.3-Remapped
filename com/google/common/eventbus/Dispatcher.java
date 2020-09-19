/*     */ package com.google.common.eventbus;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.Queues;
/*     */ import java.util.Iterator;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
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
/*     */ abstract class Dispatcher
/*     */ {
/*     */   static Dispatcher perThreadDispatchQueue() {
/*  47 */     return new PerThreadQueuedDispatcher();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Dispatcher legacyAsync() {
/*  57 */     return new LegacyAsyncDispatcher();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Dispatcher immediate() {
/*  66 */     return ImmediateDispatcher.INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract void dispatch(Object paramObject, Iterator<Subscriber> paramIterator);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class PerThreadQueuedDispatcher
/*     */     extends Dispatcher
/*     */   {
/*  84 */     private final ThreadLocal<Queue<Event>> queue = new ThreadLocal<Queue<Event>>()
/*     */       {
/*     */         protected Queue<Dispatcher.PerThreadQueuedDispatcher.Event> initialValue()
/*     */         {
/*  88 */           return Queues.newArrayDeque();
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  95 */     private final ThreadLocal<Boolean> dispatching = new ThreadLocal<Boolean>()
/*     */       {
/*     */         protected Boolean initialValue()
/*     */         {
/*  99 */           return Boolean.valueOf(false);
/*     */         }
/*     */       };
/*     */ 
/*     */     
/*     */     void dispatch(Object event, Iterator<Subscriber> subscribers) {
/* 105 */       Preconditions.checkNotNull(event);
/* 106 */       Preconditions.checkNotNull(subscribers);
/* 107 */       Queue<Event> queueForThread = this.queue.get();
/* 108 */       queueForThread.offer(new Event(event, subscribers));
/*     */       
/* 110 */       if (!((Boolean)this.dispatching.get()).booleanValue()) {
/* 111 */         this.dispatching.set(Boolean.valueOf(true));
/*     */         try {
/*     */           Event nextEvent;
/* 114 */           while ((nextEvent = queueForThread.poll()) != null) {
/* 115 */             while (nextEvent.subscribers.hasNext()) {
/* 116 */               ((Subscriber)nextEvent.subscribers.next()).dispatchEvent(nextEvent.event);
/*     */             }
/*     */           } 
/*     */         } finally {
/* 120 */           this.dispatching.remove();
/* 121 */           this.queue.remove();
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private PerThreadQueuedDispatcher() {}
/*     */     
/*     */     private static final class Event
/*     */     {
/*     */       private Event(Object event, Iterator<Subscriber> subscribers) {
/* 131 */         this.event = event;
/* 132 */         this.subscribers = subscribers;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       private final Object event;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       private final Iterator<Subscriber> subscribers;
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
/*     */   private static final class LegacyAsyncDispatcher
/*     */     extends Dispatcher
/*     */   {
/* 164 */     private final ConcurrentLinkedQueue<EventWithSubscriber> queue = Queues.newConcurrentLinkedQueue();
/*     */ 
/*     */     
/*     */     void dispatch(Object event, Iterator<Subscriber> subscribers) {
/* 168 */       Preconditions.checkNotNull(event);
/* 169 */       while (subscribers.hasNext()) {
/* 170 */         this.queue.add(new EventWithSubscriber(event, subscribers.next()));
/*     */       }
/*     */       
/*     */       EventWithSubscriber e;
/* 174 */       while ((e = this.queue.poll()) != null)
/* 175 */         e.subscriber.dispatchEvent(e.event); 
/*     */     }
/*     */     
/*     */     private LegacyAsyncDispatcher() {}
/*     */     
/*     */     private static final class EventWithSubscriber {
/*     */       private final Object event;
/*     */       
/*     */       private EventWithSubscriber(Object event, Subscriber subscriber) {
/* 184 */         this.event = event;
/* 185 */         this.subscriber = subscriber;
/*     */       }
/*     */       
/*     */       private final Subscriber subscriber;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class ImmediateDispatcher
/*     */     extends Dispatcher {
/* 194 */     private static final ImmediateDispatcher INSTANCE = new ImmediateDispatcher();
/*     */ 
/*     */     
/*     */     void dispatch(Object event, Iterator<Subscriber> subscribers) {
/* 198 */       Preconditions.checkNotNull(event);
/* 199 */       while (subscribers.hasNext())
/* 200 */         ((Subscriber)subscribers.next()).dispatchEvent(event); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\eventbus\Dispatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */