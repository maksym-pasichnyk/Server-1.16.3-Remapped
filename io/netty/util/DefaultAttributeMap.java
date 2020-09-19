/*     */ package io.netty.util;
/*     */ 
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import java.util.concurrent.atomic.AtomicReferenceArray;
/*     */ import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
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
/*     */ public class DefaultAttributeMap
/*     */   implements AttributeMap
/*     */ {
/*  30 */   private static final AtomicReferenceFieldUpdater<DefaultAttributeMap, AtomicReferenceArray> updater = AtomicReferenceFieldUpdater.newUpdater(DefaultAttributeMap.class, AtomicReferenceArray.class, "attributes");
/*     */ 
/*     */   
/*     */   private static final int BUCKET_SIZE = 4;
/*     */ 
/*     */   
/*     */   private static final int MASK = 3;
/*     */   
/*     */   private volatile AtomicReferenceArray<DefaultAttribute<?>> attributes;
/*     */ 
/*     */   
/*     */   public <T> Attribute<T> attr(AttributeKey<T> key) {
/*  42 */     if (key == null) {
/*  43 */       throw new NullPointerException("key");
/*     */     }
/*  45 */     AtomicReferenceArray<DefaultAttribute<?>> attributes = this.attributes;
/*  46 */     if (attributes == null) {
/*     */       
/*  48 */       attributes = new AtomicReferenceArray<DefaultAttribute<?>>(4);
/*     */       
/*  50 */       if (!updater.compareAndSet(this, null, attributes)) {
/*  51 */         attributes = this.attributes;
/*     */       }
/*     */     } 
/*     */     
/*  55 */     int i = index(key);
/*  56 */     DefaultAttribute<?> head = attributes.get(i);
/*  57 */     if (head == null) {
/*     */ 
/*     */       
/*  60 */       head = new DefaultAttribute();
/*  61 */       DefaultAttribute<T> attr = new DefaultAttribute<T>(head, key);
/*  62 */       head.next = attr;
/*  63 */       attr.prev = head;
/*  64 */       if (attributes.compareAndSet(i, null, head))
/*     */       {
/*  66 */         return attr;
/*     */       }
/*  68 */       head = attributes.get(i);
/*     */     } 
/*     */ 
/*     */     
/*  72 */     synchronized (head) {
/*  73 */       DefaultAttribute<?> curr = head;
/*     */       while (true) {
/*  75 */         DefaultAttribute<?> next = curr.next;
/*  76 */         if (next == null) {
/*  77 */           DefaultAttribute<T> attr = new DefaultAttribute<T>(head, key);
/*  78 */           curr.next = attr;
/*  79 */           attr.prev = curr;
/*  80 */           return attr;
/*     */         } 
/*     */         
/*  83 */         if (next.key == key && !next.removed) {
/*  84 */           return (Attribute)next;
/*     */         }
/*  86 */         curr = next;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> boolean hasAttr(AttributeKey<T> key) {
/*  93 */     if (key == null) {
/*  94 */       throw new NullPointerException("key");
/*     */     }
/*  96 */     AtomicReferenceArray<DefaultAttribute<?>> attributes = this.attributes;
/*  97 */     if (attributes == null)
/*     */     {
/*  99 */       return false;
/*     */     }
/*     */     
/* 102 */     int i = index(key);
/* 103 */     DefaultAttribute<?> head = attributes.get(i);
/* 104 */     if (head == null)
/*     */     {
/* 106 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 110 */     synchronized (head) {
/*     */       
/* 112 */       DefaultAttribute<?> curr = head.next;
/* 113 */       while (curr != null) {
/* 114 */         if (curr.key == key && !curr.removed) {
/* 115 */           return true;
/*     */         }
/* 117 */         curr = curr.next;
/*     */       } 
/* 119 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static int index(AttributeKey<?> key) {
/* 124 */     return key.id() & 0x3;
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class DefaultAttribute<T>
/*     */     extends AtomicReference<T>
/*     */     implements Attribute<T>
/*     */   {
/*     */     private static final long serialVersionUID = -2661411462200283011L;
/*     */     
/*     */     private final DefaultAttribute<?> head;
/*     */     
/*     */     private final AttributeKey<T> key;
/*     */     
/*     */     private DefaultAttribute<?> prev;
/*     */     
/*     */     private DefaultAttribute<?> next;
/*     */     private volatile boolean removed;
/*     */     
/*     */     DefaultAttribute(DefaultAttribute<?> head, AttributeKey<T> key) {
/* 144 */       this.head = head;
/* 145 */       this.key = key;
/*     */     }
/*     */ 
/*     */     
/*     */     DefaultAttribute() {
/* 150 */       this.head = this;
/* 151 */       this.key = null;
/*     */     }
/*     */ 
/*     */     
/*     */     public AttributeKey<T> key() {
/* 156 */       return this.key;
/*     */     }
/*     */ 
/*     */     
/*     */     public T setIfAbsent(T value) {
/* 161 */       while (!compareAndSet(null, value)) {
/* 162 */         T old = get();
/* 163 */         if (old != null) {
/* 164 */           return old;
/*     */         }
/*     */       } 
/* 167 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public T getAndRemove() {
/* 172 */       this.removed = true;
/* 173 */       T oldValue = getAndSet(null);
/* 174 */       remove0();
/* 175 */       return oldValue;
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 180 */       this.removed = true;
/* 181 */       set(null);
/* 182 */       remove0();
/*     */     }
/*     */     
/*     */     private void remove0() {
/* 186 */       synchronized (this.head) {
/* 187 */         if (this.prev == null) {
/*     */           return;
/*     */         }
/*     */ 
/*     */         
/* 192 */         this.prev.next = this.next;
/*     */         
/* 194 */         if (this.next != null) {
/* 195 */           this.next.prev = this.prev;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 200 */         this.prev = null;
/* 201 */         this.next = null;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\DefaultAttributeMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */