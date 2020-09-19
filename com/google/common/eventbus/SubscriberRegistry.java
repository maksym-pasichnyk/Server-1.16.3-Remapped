/*     */ package com.google.common.eventbus;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.common.cache.CacheBuilder;
/*     */ import com.google.common.cache.CacheLoader;
/*     */ import com.google.common.cache.LoadingCache;
/*     */ import com.google.common.collect.HashMultimap;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Multimap;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.common.reflect.TypeToken;
/*     */ import com.google.common.util.concurrent.UncheckedExecutionException;
/*     */ import com.google.j2objc.annotations.Weak;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.CopyOnWriteArraySet;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class SubscriberRegistry
/*     */ {
/*  62 */   private final ConcurrentMap<Class<?>, CopyOnWriteArraySet<Subscriber>> subscribers = Maps.newConcurrentMap();
/*     */ 
/*     */   
/*     */   @Weak
/*     */   private final EventBus bus;
/*     */ 
/*     */   
/*     */   SubscriberRegistry(EventBus bus) {
/*  70 */     this.bus = (EventBus)Preconditions.checkNotNull(bus);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void register(Object listener) {
/*  77 */     Multimap<Class<?>, Subscriber> listenerMethods = findAllSubscribers(listener);
/*     */     
/*  79 */     for (Map.Entry<Class<?>, Collection<Subscriber>> entry : (Iterable<Map.Entry<Class<?>, Collection<Subscriber>>>)listenerMethods.asMap().entrySet()) {
/*  80 */       Class<?> eventType = entry.getKey();
/*  81 */       Collection<Subscriber> eventMethodsInListener = entry.getValue();
/*     */       
/*  83 */       CopyOnWriteArraySet<Subscriber> eventSubscribers = this.subscribers.get(eventType);
/*     */       
/*  85 */       if (eventSubscribers == null) {
/*  86 */         CopyOnWriteArraySet<Subscriber> newSet = new CopyOnWriteArraySet<>();
/*     */         
/*  88 */         eventSubscribers = (CopyOnWriteArraySet<Subscriber>)MoreObjects.firstNonNull(this.subscribers.putIfAbsent(eventType, newSet), newSet);
/*     */       } 
/*     */       
/*  91 */       eventSubscribers.addAll(eventMethodsInListener);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void unregister(Object listener) {
/*  99 */     Multimap<Class<?>, Subscriber> listenerMethods = findAllSubscribers(listener);
/*     */     
/* 101 */     for (Map.Entry<Class<?>, Collection<Subscriber>> entry : (Iterable<Map.Entry<Class<?>, Collection<Subscriber>>>)listenerMethods.asMap().entrySet()) {
/* 102 */       Class<?> eventType = entry.getKey();
/* 103 */       Collection<Subscriber> listenerMethodsForType = entry.getValue();
/*     */       
/* 105 */       CopyOnWriteArraySet<Subscriber> currentSubscribers = this.subscribers.get(eventType);
/* 106 */       if (currentSubscribers == null || !currentSubscribers.removeAll(listenerMethodsForType))
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 111 */         throw new IllegalArgumentException("missing event subscriber for an annotated method. Is " + listener + " registered?");
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   Set<Subscriber> getSubscribersForTesting(Class<?> eventType) {
/* 122 */     return (Set<Subscriber>)MoreObjects.firstNonNull(this.subscribers.get(eventType), ImmutableSet.of());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Iterator<Subscriber> getSubscribers(Object event) {
/* 130 */     ImmutableSet<Class<?>> eventTypes = flattenHierarchy(event.getClass());
/*     */ 
/*     */     
/* 133 */     List<Iterator<Subscriber>> subscriberIterators = Lists.newArrayListWithCapacity(eventTypes.size());
/*     */     
/* 135 */     for (UnmodifiableIterator<Class<?>> unmodifiableIterator = eventTypes.iterator(); unmodifiableIterator.hasNext(); ) { Class<?> eventType = unmodifiableIterator.next();
/* 136 */       CopyOnWriteArraySet<Subscriber> eventSubscribers = this.subscribers.get(eventType);
/* 137 */       if (eventSubscribers != null)
/*     */       {
/* 139 */         subscriberIterators.add(eventSubscribers.iterator());
/*     */       } }
/*     */ 
/*     */     
/* 143 */     return Iterators.concat(subscriberIterators.iterator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 153 */   private static final LoadingCache<Class<?>, ImmutableList<Method>> subscriberMethodsCache = CacheBuilder.newBuilder()
/* 154 */     .weakKeys()
/* 155 */     .build(new CacheLoader<Class<?>, ImmutableList<Method>>()
/*     */       {
/*     */         public ImmutableList<Method> load(Class<?> concreteClass) throws Exception
/*     */         {
/* 159 */           return SubscriberRegistry.getAnnotatedMethodsNotCached(concreteClass);
/*     */         }
/*     */       });
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Multimap<Class<?>, Subscriber> findAllSubscribers(Object listener) {
/* 167 */     HashMultimap hashMultimap = HashMultimap.create();
/* 168 */     Class<?> clazz = listener.getClass();
/* 169 */     for (UnmodifiableIterator<Method> unmodifiableIterator = getAnnotatedMethods(clazz).iterator(); unmodifiableIterator.hasNext(); ) { Method method = unmodifiableIterator.next();
/* 170 */       Class<?>[] parameterTypes = method.getParameterTypes();
/* 171 */       Class<?> eventType = parameterTypes[0];
/* 172 */       hashMultimap.put(eventType, Subscriber.create(this.bus, listener, method)); }
/*     */     
/* 174 */     return (Multimap<Class<?>, Subscriber>)hashMultimap;
/*     */   }
/*     */   
/*     */   private static ImmutableList<Method> getAnnotatedMethods(Class<?> clazz) {
/* 178 */     return (ImmutableList<Method>)subscriberMethodsCache.getUnchecked(clazz);
/*     */   }
/*     */   
/*     */   private static ImmutableList<Method> getAnnotatedMethodsNotCached(Class<?> clazz) {
/* 182 */     Set<? extends Class<?>> supertypes = TypeToken.of(clazz).getTypes().rawTypes();
/* 183 */     Map<MethodIdentifier, Method> identifiers = Maps.newHashMap();
/* 184 */     for (Class<?> supertype : supertypes) {
/* 185 */       for (Method method : supertype.getDeclaredMethods()) {
/* 186 */         if (method.isAnnotationPresent((Class)Subscribe.class) && !method.isSynthetic()) {
/*     */           
/* 188 */           Class<?>[] parameterTypes = method.getParameterTypes();
/* 189 */           Preconditions.checkArgument((parameterTypes.length == 1), "Method %s has @Subscribe annotation but has %s parameters.Subscriber methods must have exactly 1 parameter.", method, parameterTypes.length);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 196 */           MethodIdentifier ident = new MethodIdentifier(method);
/* 197 */           if (!identifiers.containsKey(ident)) {
/* 198 */             identifiers.put(ident, method);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 203 */     return ImmutableList.copyOf(identifiers.values());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 210 */   private static final LoadingCache<Class<?>, ImmutableSet<Class<?>>> flattenHierarchyCache = CacheBuilder.newBuilder()
/* 211 */     .weakKeys()
/* 212 */     .build(new CacheLoader<Class<?>, ImmutableSet<Class<?>>>()
/*     */       {
/*     */ 
/*     */         
/*     */         public ImmutableSet<Class<?>> load(Class<?> concreteClass)
/*     */         {
/* 218 */           return ImmutableSet.copyOf(
/* 219 */               TypeToken.of(concreteClass).getTypes().rawTypes());
/*     */         }
/*     */       });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static ImmutableSet<Class<?>> flattenHierarchy(Class<?> concreteClass) {
/*     */     try {
/* 230 */       return (ImmutableSet<Class<?>>)flattenHierarchyCache.getUnchecked(concreteClass);
/* 231 */     } catch (UncheckedExecutionException e) {
/* 232 */       throw Throwables.propagate(e.getCause());
/*     */     } 
/*     */   }
/*     */   
/*     */   private static final class MethodIdentifier
/*     */   {
/*     */     private final String name;
/*     */     private final List<Class<?>> parameterTypes;
/*     */     
/*     */     MethodIdentifier(Method method) {
/* 242 */       this.name = method.getName();
/* 243 */       this.parameterTypes = Arrays.asList(method.getParameterTypes());
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 248 */       return Objects.hashCode(new Object[] { this.name, this.parameterTypes });
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object o) {
/* 253 */       if (o instanceof MethodIdentifier) {
/* 254 */         MethodIdentifier ident = (MethodIdentifier)o;
/* 255 */         return (this.name.equals(ident.name) && this.parameterTypes.equals(ident.parameterTypes));
/*     */       } 
/* 257 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\eventbus\SubscriberRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */