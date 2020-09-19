/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SetTag<T>
/*    */   implements Tag<T>
/*    */ {
/*    */   private final ImmutableList<T> valuesList;
/*    */   private final Set<T> values;
/*    */   @VisibleForTesting
/*    */   protected final Class<?> closestCommonSuperType;
/*    */   
/*    */   protected SetTag(Set<T> debug1, Class<?> debug2) {
/* 22 */     this.closestCommonSuperType = debug2;
/* 23 */     this.values = debug1;
/* 24 */     this.valuesList = ImmutableList.copyOf(debug1);
/*    */   }
/*    */   
/*    */   public static <T> SetTag<T> empty() {
/* 28 */     return new SetTag<>((Set<T>)ImmutableSet.of(), Void.class);
/*    */   }
/*    */   
/*    */   public static <T> SetTag<T> create(Set<T> debug0) {
/* 32 */     return new SetTag<>(debug0, findCommonSuperClass(debug0));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean contains(T debug1) {
/* 38 */     return (this.closestCommonSuperType.isInstance(debug1) && this.values.contains(debug1));
/*    */   }
/*    */ 
/*    */   
/*    */   public List<T> getValues() {
/* 43 */     return (List<T>)this.valuesList;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static <T> Class<?> findCommonSuperClass(Set<T> debug0) {
/* 54 */     if (debug0.isEmpty()) {
/* 55 */       return Void.class;
/*    */     }
/*    */     
/* 58 */     Class<?> debug1 = null;
/* 59 */     for (T debug3 : debug0) {
/* 60 */       if (debug1 == null) {
/* 61 */         debug1 = debug3.getClass(); continue;
/*    */       } 
/* 63 */       debug1 = findClosestAncestor(debug1, debug3.getClass());
/*    */     } 
/*    */     
/* 66 */     return debug1;
/*    */   }
/*    */   
/*    */   private static Class<?> findClosestAncestor(Class<?> debug0, Class<?> debug1) {
/* 70 */     while (!debug0.isAssignableFrom(debug1)) {
/* 71 */       debug0 = debug0.getSuperclass();
/*    */     }
/* 73 */     return debug0;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\tags\SetTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */