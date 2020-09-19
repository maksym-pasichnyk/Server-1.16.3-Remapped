/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.BitSet;
/*     */ import java.util.Deque;
/*     */ import java.util.Iterator;
/*     */ import java.util.function.Consumer;
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
/*     */ @GwtCompatible
/*     */ public abstract class BinaryTreeTraverser<T>
/*     */   extends TreeTraverser<T>
/*     */ {
/*     */   public final Iterable<T> children(final T root) {
/*  58 */     Preconditions.checkNotNull(root);
/*  59 */     return new FluentIterable<T>()
/*     */       {
/*     */         public Iterator<T> iterator() {
/*  62 */           return new AbstractIterator<T>()
/*     */             {
/*     */               boolean doneLeft;
/*     */               boolean doneRight;
/*     */               
/*     */               protected T computeNext() {
/*  68 */                 if (!this.doneLeft) {
/*  69 */                   this.doneLeft = true;
/*  70 */                   Optional<T> left = BinaryTreeTraverser.this.leftChild((T)root);
/*  71 */                   if (left.isPresent()) {
/*  72 */                     return (T)left.get();
/*     */                   }
/*     */                 } 
/*  75 */                 if (!this.doneRight) {
/*  76 */                   this.doneRight = true;
/*  77 */                   Optional<T> right = BinaryTreeTraverser.this.rightChild((T)root);
/*  78 */                   if (right.isPresent()) {
/*  79 */                     return (T)right.get();
/*     */                   }
/*     */                 } 
/*  82 */                 return endOfData();
/*     */               }
/*     */             };
/*     */         }
/*     */ 
/*     */         
/*     */         public void forEach(Consumer<? super T> action) {
/*  89 */           BinaryTreeTraverser.acceptIfPresent(action, BinaryTreeTraverser.this.leftChild(root));
/*  90 */           BinaryTreeTraverser.acceptIfPresent(action, BinaryTreeTraverser.this.rightChild(root));
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   UnmodifiableIterator<T> preOrderIterator(T root) {
/*  97 */     return new PreOrderIterator(root);
/*     */   }
/*     */ 
/*     */   
/*     */   private final class PreOrderIterator
/*     */     extends UnmodifiableIterator<T>
/*     */     implements PeekingIterator<T>
/*     */   {
/*     */     private final Deque<T> stack;
/*     */     
/*     */     PreOrderIterator(T root) {
/* 108 */       this.stack = new ArrayDeque<>(8);
/* 109 */       this.stack.addLast(root);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 114 */       return !this.stack.isEmpty();
/*     */     }
/*     */ 
/*     */     
/*     */     public T next() {
/* 119 */       T result = this.stack.removeLast();
/* 120 */       BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.rightChild(result));
/* 121 */       BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.leftChild(result));
/* 122 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public T peek() {
/* 127 */       return this.stack.getLast();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   UnmodifiableIterator<T> postOrderIterator(T root) {
/* 133 */     return new PostOrderIterator(root);
/*     */   }
/*     */ 
/*     */   
/*     */   private final class PostOrderIterator
/*     */     extends UnmodifiableIterator<T>
/*     */   {
/*     */     private final Deque<T> stack;
/*     */     private final BitSet hasExpanded;
/*     */     
/*     */     PostOrderIterator(T root) {
/* 144 */       this.stack = new ArrayDeque<>(8);
/* 145 */       this.stack.addLast(root);
/* 146 */       this.hasExpanded = new BitSet();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 151 */       return !this.stack.isEmpty();
/*     */     }
/*     */ 
/*     */     
/*     */     public T next() {
/*     */       while (true) {
/* 157 */         T node = this.stack.getLast();
/* 158 */         boolean expandedNode = this.hasExpanded.get(this.stack.size() - 1);
/* 159 */         if (expandedNode) {
/* 160 */           this.stack.removeLast();
/* 161 */           this.hasExpanded.clear(this.stack.size());
/* 162 */           return node;
/*     */         } 
/* 164 */         this.hasExpanded.set(this.stack.size() - 1);
/* 165 */         BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.rightChild(node));
/* 166 */         BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.leftChild(node));
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final FluentIterable<T> inOrderTraversal(final T root) {
/* 175 */     Preconditions.checkNotNull(root);
/* 176 */     return new FluentIterable<T>()
/*     */       {
/*     */         public UnmodifiableIterator<T> iterator() {
/* 179 */           return new BinaryTreeTraverser.InOrderIterator((T)root);
/*     */         }
/*     */ 
/*     */         
/*     */         public void forEach(final Consumer<? super T> action) {
/* 184 */           Preconditions.checkNotNull(action);
/* 185 */           (new Consumer<T>()
/*     */             {
/*     */               public void accept(T t) {
/* 188 */                 BinaryTreeTraverser.acceptIfPresent(this, BinaryTreeTraverser.this.leftChild(t));
/* 189 */                 action.accept(t);
/* 190 */                 BinaryTreeTraverser.acceptIfPresent(this, BinaryTreeTraverser.this.rightChild(t));
/*     */               }
/* 192 */             }).accept((T)root);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private final class InOrderIterator extends AbstractIterator<T> {
/*     */     private final Deque<T> stack;
/*     */     private final BitSet hasExpandedLeft;
/*     */     
/*     */     InOrderIterator(T root) {
/* 202 */       this.stack = new ArrayDeque<>(8);
/* 203 */       this.hasExpandedLeft = new BitSet();
/* 204 */       this.stack.addLast(root);
/*     */     }
/*     */ 
/*     */     
/*     */     protected T computeNext() {
/* 209 */       while (!this.stack.isEmpty()) {
/* 210 */         T node = this.stack.getLast();
/* 211 */         if (this.hasExpandedLeft.get(this.stack.size() - 1)) {
/* 212 */           this.stack.removeLast();
/* 213 */           this.hasExpandedLeft.clear(this.stack.size());
/* 214 */           BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.rightChild(node));
/* 215 */           return node;
/*     */         } 
/* 217 */         this.hasExpandedLeft.set(this.stack.size() - 1);
/* 218 */         BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.leftChild(node));
/*     */       } 
/*     */       
/* 221 */       return endOfData();
/*     */     }
/*     */   }
/*     */   
/*     */   private static <T> void pushIfPresent(Deque<T> stack, Optional<T> node) {
/* 226 */     if (node.isPresent()) {
/* 227 */       stack.addLast((T)node.get());
/*     */     }
/*     */   }
/*     */   
/*     */   private static <T> void acceptIfPresent(Consumer<? super T> action, Optional<T> node) {
/* 232 */     if (node.isPresent())
/* 233 */       action.accept((T)node.get()); 
/*     */   }
/*     */   
/*     */   public abstract Optional<T> leftChild(T paramT);
/*     */   
/*     */   public abstract Optional<T> rightChild(T paramT);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\BinaryTreeTraverser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */