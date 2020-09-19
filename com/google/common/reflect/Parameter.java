/*     */ package com.google.common.reflect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.FluentIterable;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AnnotatedElement;
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
/*     */ @Beta
/*     */ public final class Parameter
/*     */   implements AnnotatedElement
/*     */ {
/*     */   private final Invokable<?, ?> declaration;
/*     */   private final int position;
/*     */   private final TypeToken<?> type;
/*     */   private final ImmutableList<Annotation> annotations;
/*     */   
/*     */   Parameter(Invokable<?, ?> declaration, int position, TypeToken<?> type, Annotation[] annotations) {
/*  42 */     this.declaration = declaration;
/*  43 */     this.position = position;
/*  44 */     this.type = type;
/*  45 */     this.annotations = ImmutableList.copyOf((Object[])annotations);
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeToken<?> getType() {
/*  50 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public Invokable<?, ?> getDeclaringInvokable() {
/*  55 */     return this.declaration;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
/*  60 */     return (getAnnotation(annotationType) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
/*  66 */     Preconditions.checkNotNull(annotationType);
/*  67 */     for (UnmodifiableIterator<Annotation> unmodifiableIterator = this.annotations.iterator(); unmodifiableIterator.hasNext(); ) { Annotation annotation = unmodifiableIterator.next();
/*  68 */       if (annotationType.isInstance(annotation)) {
/*  69 */         return annotationType.cast(annotation);
/*     */       } }
/*     */     
/*  72 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Annotation[] getAnnotations() {
/*  77 */     return getDeclaredAnnotations();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends Annotation> A[] getAnnotationsByType(Class<A> annotationType) {
/*  85 */     return getDeclaredAnnotationsByType(annotationType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Annotation[] getDeclaredAnnotations() {
/*  94 */     return (Annotation[])this.annotations.toArray((Object[])new Annotation[this.annotations.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <A extends Annotation> A getDeclaredAnnotation(Class<A> annotationType) {
/* 103 */     Preconditions.checkNotNull(annotationType);
/* 104 */     return (A)FluentIterable.from((Iterable)this.annotations).filter(annotationType).first().orNull();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends Annotation> A[] getDeclaredAnnotationsByType(Class<A> annotationType) {
/* 112 */     return (A[])FluentIterable.from((Iterable)this.annotations).filter(annotationType).toArray(annotationType);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object obj) {
/* 117 */     if (obj instanceof Parameter) {
/* 118 */       Parameter that = (Parameter)obj;
/* 119 */       return (this.position == that.position && this.declaration.equals(that.declaration));
/*     */     } 
/* 121 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 126 */     return this.position;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 131 */     return this.type + " arg" + this.position;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\reflect\Parameter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */