/*     */ package io.netty.util;
/*     */ 
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.PlatformDependent;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ConstantPool<T extends Constant<T>>
/*     */ {
/*  32 */   private final ConcurrentMap<String, T> constants = PlatformDependent.newConcurrentHashMap();
/*     */   
/*  34 */   private final AtomicInteger nextId = new AtomicInteger(1);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T valueOf(Class<?> firstNameComponent, String secondNameComponent) {
/*  40 */     if (firstNameComponent == null) {
/*  41 */       throw new NullPointerException("firstNameComponent");
/*     */     }
/*  43 */     if (secondNameComponent == null) {
/*  44 */       throw new NullPointerException("secondNameComponent");
/*     */     }
/*     */     
/*  47 */     return valueOf(firstNameComponent.getName() + '#' + secondNameComponent);
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
/*     */   public T valueOf(String name) {
/*  59 */     checkNotNullAndNotEmpty(name);
/*  60 */     return getOrCreate(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private T getOrCreate(String name) {
/*  69 */     Constant constant = (Constant)this.constants.get(name);
/*  70 */     if (constant == null) {
/*  71 */       T tempConstant = newConstant(nextId(), name);
/*  72 */       constant = (Constant)this.constants.putIfAbsent(name, tempConstant);
/*  73 */       if (constant == null) {
/*  74 */         return tempConstant;
/*     */       }
/*     */     } 
/*     */     
/*  78 */     return (T)constant;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean exists(String name) {
/*  85 */     checkNotNullAndNotEmpty(name);
/*  86 */     return this.constants.containsKey(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T newInstance(String name) {
/*  94 */     checkNotNullAndNotEmpty(name);
/*  95 */     return createOrThrow(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private T createOrThrow(String name) {
/* 104 */     Constant constant = (Constant)this.constants.get(name);
/* 105 */     if (constant == null) {
/* 106 */       T tempConstant = newConstant(nextId(), name);
/* 107 */       constant = (Constant)this.constants.putIfAbsent(name, tempConstant);
/* 108 */       if (constant == null) {
/* 109 */         return tempConstant;
/*     */       }
/*     */     } 
/*     */     
/* 113 */     throw new IllegalArgumentException(String.format("'%s' is already in use", new Object[] { name }));
/*     */   }
/*     */   
/*     */   private static String checkNotNullAndNotEmpty(String name) {
/* 117 */     ObjectUtil.checkNotNull(name, "name");
/*     */     
/* 119 */     if (name.isEmpty()) {
/* 120 */       throw new IllegalArgumentException("empty name");
/*     */     }
/*     */     
/* 123 */     return name;
/*     */   }
/*     */   
/*     */   protected abstract T newConstant(int paramInt, String paramString);
/*     */   
/*     */   @Deprecated
/*     */   public final int nextId() {
/* 130 */     return this.nextId.getAndIncrement();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\ConstantPool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */