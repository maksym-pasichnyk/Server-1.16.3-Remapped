/*     */ package io.netty.util.internal;
/*     */ 
/*     */ import io.netty.util.Recycler;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
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
/*     */ public final class RecyclableArrayList
/*     */   extends ArrayList<Object>
/*     */ {
/*     */   private static final long serialVersionUID = -8605125654176467947L;
/*     */   private static final int DEFAULT_INITIAL_CAPACITY = 8;
/*     */   
/*  36 */   private static final Recycler<RecyclableArrayList> RECYCLER = new Recycler<RecyclableArrayList>()
/*     */     {
/*     */       protected RecyclableArrayList newObject(Recycler.Handle<RecyclableArrayList> handle) {
/*  39 */         return new RecyclableArrayList(handle);
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   private boolean insertSinceRecycled;
/*     */   
/*     */   private final Recycler.Handle<RecyclableArrayList> handle;
/*     */   
/*     */   public static RecyclableArrayList newInstance() {
/*  49 */     return newInstance(8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RecyclableArrayList newInstance(int minCapacity) {
/*  56 */     RecyclableArrayList ret = (RecyclableArrayList)RECYCLER.get();
/*  57 */     ret.ensureCapacity(minCapacity);
/*  58 */     return ret;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private RecyclableArrayList(Recycler.Handle<RecyclableArrayList> handle) {
/*  64 */     this(handle, 8);
/*     */   }
/*     */   
/*     */   private RecyclableArrayList(Recycler.Handle<RecyclableArrayList> handle, int initialCapacity) {
/*  68 */     super(initialCapacity);
/*  69 */     this.handle = handle;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addAll(Collection<?> c) {
/*  74 */     checkNullElements(c);
/*  75 */     if (super.addAll(c)) {
/*  76 */       this.insertSinceRecycled = true;
/*  77 */       return true;
/*     */     } 
/*  79 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addAll(int index, Collection<?> c) {
/*  84 */     checkNullElements(c);
/*  85 */     if (super.addAll(index, c)) {
/*  86 */       this.insertSinceRecycled = true;
/*  87 */       return true;
/*     */     } 
/*  89 */     return false;
/*     */   }
/*     */   
/*     */   private static void checkNullElements(Collection<?> c) {
/*  93 */     if (c instanceof java.util.RandomAccess && c instanceof List) {
/*     */       
/*  95 */       List<?> list = (List)c;
/*  96 */       int size = list.size();
/*  97 */       for (int i = 0; i < size; i++) {
/*  98 */         if (list.get(i) == null) {
/*  99 */           throw new IllegalArgumentException("c contains null values");
/*     */         }
/*     */       } 
/*     */     } else {
/* 103 */       for (Object element : c) {
/* 104 */         if (element == null) {
/* 105 */           throw new IllegalArgumentException("c contains null values");
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean add(Object element) {
/* 113 */     if (element == null) {
/* 114 */       throw new NullPointerException("element");
/*     */     }
/* 116 */     if (super.add(element)) {
/* 117 */       this.insertSinceRecycled = true;
/* 118 */       return true;
/*     */     } 
/* 120 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(int index, Object element) {
/* 125 */     if (element == null) {
/* 126 */       throw new NullPointerException("element");
/*     */     }
/* 128 */     super.add(index, element);
/* 129 */     this.insertSinceRecycled = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object set(int index, Object element) {
/* 134 */     if (element == null) {
/* 135 */       throw new NullPointerException("element");
/*     */     }
/* 137 */     Object old = super.set(index, element);
/* 138 */     this.insertSinceRecycled = true;
/* 139 */     return old;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean insertSinceRecycled() {
/* 146 */     return this.insertSinceRecycled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean recycle() {
/* 153 */     clear();
/* 154 */     this.insertSinceRecycled = false;
/* 155 */     this.handle.recycle(this);
/* 156 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\RecyclableArrayList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */