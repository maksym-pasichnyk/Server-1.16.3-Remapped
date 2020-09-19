/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.ListIterator;
/*    */ import java.util.Spliterator;
/*    */ import java.util.Spliterators;
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
/*    */ class RegularImmutableList<E>
/*    */   extends ImmutableList<E>
/*    */ {
/* 31 */   static final ImmutableList<Object> EMPTY = new RegularImmutableList(ObjectArrays.EMPTY_ARRAY);
/*    */   
/*    */   private final transient Object[] array;
/*    */ 
/*    */   
/*    */   RegularImmutableList(Object[] array) {
/* 37 */     this.array = array;
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 42 */     return this.array.length;
/*    */   }
/*    */ 
/*    */   
/*    */   boolean isPartialView() {
/* 47 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   int copyIntoArray(Object[] dst, int dstOff) {
/* 52 */     System.arraycopy(this.array, 0, dst, dstOff, this.array.length);
/* 53 */     return dstOff + this.array.length;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public E get(int index) {
/* 60 */     return (E)this.array[index];
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public UnmodifiableListIterator<E> listIterator(int index) {
/* 68 */     return Iterators.forArray((E[])this.array, 0, this.array.length, index);
/*    */   }
/*    */ 
/*    */   
/*    */   public Spliterator<E> spliterator() {
/* 73 */     return Spliterators.spliterator(this.array, 1296);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\RegularImmutableList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */