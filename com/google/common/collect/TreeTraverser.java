/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Deque;
/*     */ import java.util.Iterator;
/*     */ import java.util.Queue;
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
/*     */ public abstract class TreeTraverser<T>
/*     */ {
/*     */   public static <T> TreeTraverser<T> using(final Function<T, ? extends Iterable<T>> nodeToChildrenFunction) {
/*  82 */     Preconditions.checkNotNull(nodeToChildrenFunction);
/*  83 */     return new TreeTraverser<T>()
/*     */       {
/*     */         public Iterable<T> children(T root) {
/*  86 */           return (Iterable<T>)nodeToChildrenFunction.apply(root);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Iterable<T> children(T paramT);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final FluentIterable<T> preOrderTraversal(final T root) {
/* 104 */     Preconditions.checkNotNull(root);
/* 105 */     return new FluentIterable<T>()
/*     */       {
/*     */         public UnmodifiableIterator<T> iterator() {
/* 108 */           return TreeTraverser.this.preOrderIterator((T)root);
/*     */         }
/*     */ 
/*     */         
/*     */         public void forEach(final Consumer<? super T> action) {
/* 113 */           Preconditions.checkNotNull(action);
/* 114 */           (new Consumer<T>()
/*     */             {
/*     */               public void accept(T t) {
/* 117 */                 action.accept(t);
/* 118 */                 TreeTraverser.this.children(t).forEach(this);
/*     */               }
/* 120 */             }).accept((T)root);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   UnmodifiableIterator<T> preOrderIterator(T root) {
/* 127 */     return new PreOrderIterator(root);
/*     */   }
/*     */   
/*     */   private final class PreOrderIterator extends UnmodifiableIterator<T> {
/*     */     private final Deque<Iterator<T>> stack;
/*     */     
/*     */     PreOrderIterator(T root) {
/* 134 */       this.stack = new ArrayDeque<>();
/* 135 */       this.stack.addLast(Iterators.singletonIterator((T)Preconditions.checkNotNull(root)));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 140 */       return !this.stack.isEmpty();
/*     */     }
/*     */ 
/*     */     
/*     */     public T next() {
/* 145 */       Iterator<T> itr = this.stack.getLast();
/* 146 */       T result = (T)Preconditions.checkNotNull(itr.next());
/* 147 */       if (!itr.hasNext()) {
/* 148 */         this.stack.removeLast();
/*     */       }
/* 150 */       Iterator<T> childItr = TreeTraverser.this.children(result).iterator();
/* 151 */       if (childItr.hasNext()) {
/* 152 */         this.stack.addLast(childItr);
/*     */       }
/* 154 */       return result;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final FluentIterable<T> postOrderTraversal(final T root) {
/* 166 */     Preconditions.checkNotNull(root);
/* 167 */     return new FluentIterable<T>()
/*     */       {
/*     */         public UnmodifiableIterator<T> iterator() {
/* 170 */           return TreeTraverser.this.postOrderIterator((T)root);
/*     */         }
/*     */ 
/*     */         
/*     */         public void forEach(final Consumer<? super T> action) {
/* 175 */           Preconditions.checkNotNull(action);
/* 176 */           (new Consumer<T>()
/*     */             {
/*     */               public void accept(T t) {
/* 179 */                 TreeTraverser.this.children(t).forEach(this);
/* 180 */                 action.accept(t);
/*     */               }
/* 182 */             }).accept((T)root);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   UnmodifiableIterator<T> postOrderIterator(T root) {
/* 189 */     return new PostOrderIterator(root);
/*     */   }
/*     */   
/*     */   private static final class PostOrderNode<T> {
/*     */     final T root;
/*     */     final Iterator<T> childIterator;
/*     */     
/*     */     PostOrderNode(T root, Iterator<T> childIterator) {
/* 197 */       this.root = (T)Preconditions.checkNotNull(root);
/* 198 */       this.childIterator = (Iterator<T>)Preconditions.checkNotNull(childIterator);
/*     */     }
/*     */   }
/*     */   
/*     */   private final class PostOrderIterator extends AbstractIterator<T> {
/*     */     private final ArrayDeque<TreeTraverser.PostOrderNode<T>> stack;
/*     */     
/*     */     PostOrderIterator(T root) {
/* 206 */       this.stack = new ArrayDeque<>();
/* 207 */       this.stack.addLast(expand(root));
/*     */     }
/*     */ 
/*     */     
/*     */     protected T computeNext() {
/* 212 */       while (!this.stack.isEmpty()) {
/* 213 */         TreeTraverser.PostOrderNode<T> top = this.stack.getLast();
/* 214 */         if (top.childIterator.hasNext()) {
/* 215 */           T child = top.childIterator.next();
/* 216 */           this.stack.addLast(expand(child)); continue;
/*     */         } 
/* 218 */         this.stack.removeLast();
/* 219 */         return top.root;
/*     */       } 
/*     */       
/* 222 */       return endOfData();
/*     */     }
/*     */     
/*     */     private TreeTraverser.PostOrderNode<T> expand(T t) {
/* 226 */       return new TreeTraverser.PostOrderNode<>(t, TreeTraverser.this.children(t).iterator());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final FluentIterable<T> breadthFirstTraversal(final T root) {
/* 238 */     Preconditions.checkNotNull(root);
/* 239 */     return new FluentIterable<T>()
/*     */       {
/*     */         public UnmodifiableIterator<T> iterator() {
/* 242 */           return new TreeTraverser.BreadthFirstIterator((T)root);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private final class BreadthFirstIterator
/*     */     extends UnmodifiableIterator<T> implements PeekingIterator<T> {
/*     */     private final Queue<T> queue;
/*     */     
/*     */     BreadthFirstIterator(T root) {
/* 252 */       this.queue = new ArrayDeque<>();
/* 253 */       this.queue.add(root);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 258 */       return !this.queue.isEmpty();
/*     */     }
/*     */ 
/*     */     
/*     */     public T peek() {
/* 263 */       return this.queue.element();
/*     */     }
/*     */ 
/*     */     
/*     */     public T next() {
/* 268 */       T result = this.queue.remove();
/* 269 */       Iterables.addAll(this.queue, TreeTraverser.this.children(result));
/* 270 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\TreeTraverser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */