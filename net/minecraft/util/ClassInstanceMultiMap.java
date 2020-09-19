/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.common.collect.Iterators;
/*    */ import com.google.common.collect.Lists;
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.AbstractCollection;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.stream.Collectors;
/*    */ 
/*    */ public class ClassInstanceMultiMap<T>
/*    */   extends AbstractCollection<T> {
/* 17 */   private final Map<Class<?>, List<T>> byClass = Maps.newHashMap();
/*    */   
/*    */   private final Class<T> baseClass;
/* 20 */   private final List<T> allInstances = Lists.newArrayList();
/*    */   
/*    */   public ClassInstanceMultiMap(Class<T> debug1) {
/* 23 */     this.baseClass = debug1;
/* 24 */     this.byClass.put(debug1, this.allInstances);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean add(T debug1) {
/* 29 */     boolean debug2 = false;
/* 30 */     for (Map.Entry<Class<?>, List<T>> debug4 : this.byClass.entrySet()) {
/* 31 */       if (((Class)debug4.getKey()).isInstance(debug1)) {
/* 32 */         debug2 |= ((List<T>)debug4.getValue()).add(debug1);
/*    */       }
/*    */     } 
/* 35 */     return debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean remove(Object debug1) {
/* 40 */     boolean debug2 = false;
/* 41 */     for (Map.Entry<Class<?>, List<T>> debug4 : this.byClass.entrySet()) {
/* 42 */       if (((Class)debug4.getKey()).isInstance(debug1)) {
/* 43 */         List<T> debug5 = debug4.getValue();
/* 44 */         debug2 |= debug5.remove(debug1);
/*    */       } 
/*    */     } 
/* 47 */     return debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean contains(Object debug1) {
/* 52 */     return find(debug1.getClass()).contains(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public <S> Collection<S> find(Class<S> debug1) {
/* 57 */     if (!this.baseClass.isAssignableFrom(debug1)) {
/* 58 */       throw new IllegalArgumentException("Don't know how to search for " + debug1);
/*    */     }
/* 60 */     List<T> debug2 = this.byClass.computeIfAbsent(debug1, debug1 -> (List)this.allInstances.stream().filter(debug1::isInstance).collect(Collectors.toList()));
/* 61 */     return Collections.unmodifiableCollection(debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public Iterator<T> iterator() {
/* 66 */     if (this.allInstances.isEmpty()) {
/* 67 */       return Collections.emptyIterator();
/*    */     }
/* 69 */     return (Iterator<T>)Iterators.unmodifiableIterator(this.allInstances.iterator());
/*    */   }
/*    */   
/*    */   public List<T> getAllInstances() {
/* 73 */     return (List<T>)ImmutableList.copyOf(this.allInstances);
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 78 */     return this.allInstances.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\ClassInstanceMultiMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */