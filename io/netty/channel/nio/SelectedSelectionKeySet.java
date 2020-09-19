/*    */ package io.netty.channel.nio;
/*    */ 
/*    */ import java.nio.channels.SelectionKey;
/*    */ import java.util.AbstractSet;
/*    */ import java.util.Arrays;
/*    */ import java.util.Iterator;
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
/*    */ final class SelectedSelectionKeySet
/*    */   extends AbstractSet<SelectionKey>
/*    */ {
/* 29 */   SelectionKey[] keys = new SelectionKey[1024];
/*    */   
/*    */   int size;
/*    */   
/*    */   public boolean add(SelectionKey o) {
/* 34 */     if (o == null) {
/* 35 */       return false;
/*    */     }
/*    */     
/* 38 */     this.keys[this.size++] = o;
/* 39 */     if (this.size == this.keys.length) {
/* 40 */       increaseCapacity();
/*    */     }
/*    */     
/* 43 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 48 */     return this.size;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean remove(Object o) {
/* 53 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean contains(Object o) {
/* 58 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public Iterator<SelectionKey> iterator() {
/* 63 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   void reset() {
/* 67 */     reset(0);
/*    */   }
/*    */   
/*    */   void reset(int start) {
/* 71 */     Arrays.fill((Object[])this.keys, start, this.size, (Object)null);
/* 72 */     this.size = 0;
/*    */   }
/*    */   
/*    */   private void increaseCapacity() {
/* 76 */     SelectionKey[] newKeys = new SelectionKey[this.keys.length << 1];
/* 77 */     System.arraycopy(this.keys, 0, newKeys, 0, this.size);
/* 78 */     this.keys = newKeys;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\nio\SelectedSelectionKeySet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */