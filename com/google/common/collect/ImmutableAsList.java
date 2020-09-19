/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import java.io.InvalidObjectException;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.Serializable;
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
/*    */ @GwtCompatible(serializable = true, emulated = true)
/*    */ abstract class ImmutableAsList<E>
/*    */   extends ImmutableList<E>
/*    */ {
/*    */   abstract ImmutableCollection<E> delegateCollection();
/*    */   
/*    */   public boolean contains(Object target) {
/* 41 */     return delegateCollection().contains(target);
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 46 */     return delegateCollection().size();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isEmpty() {
/* 51 */     return delegateCollection().isEmpty();
/*    */   }
/*    */ 
/*    */   
/*    */   boolean isPartialView() {
/* 56 */     return delegateCollection().isPartialView();
/*    */   }
/*    */   
/*    */   @GwtIncompatible
/*    */   static class SerializedForm
/*    */     implements Serializable
/*    */   {
/*    */     final ImmutableCollection<?> collection;
/*    */     private static final long serialVersionUID = 0L;
/*    */     
/*    */     SerializedForm(ImmutableCollection<?> collection) {
/* 67 */       this.collection = collection;
/*    */     }
/*    */     
/*    */     Object readResolve() {
/* 71 */       return this.collection.asList();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @GwtIncompatible
/*    */   private void readObject(ObjectInputStream stream) throws InvalidObjectException {
/* 79 */     throw new InvalidObjectException("Use SerializedForm");
/*    */   }
/*    */ 
/*    */   
/*    */   @GwtIncompatible
/*    */   Object writeReplace() {
/* 85 */     return new SerializedForm(delegateCollection());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\ImmutableAsList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */