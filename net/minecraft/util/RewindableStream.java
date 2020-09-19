/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import java.util.Spliterator;
/*    */ import java.util.Spliterators;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.stream.Stream;
/*    */ import java.util.stream.StreamSupport;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RewindableStream<T>
/*    */ {
/* 16 */   private final List<T> cache = Lists.newArrayList();
/*    */   private final Spliterator<T> source;
/*    */   
/*    */   public RewindableStream(Stream<T> debug1) {
/* 20 */     this.source = debug1.spliterator();
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<T> getStream() {
/* 25 */     return StreamSupport.stream(new Spliterators.AbstractSpliterator<T>(Long.MAX_VALUE, 0)
/*    */         {
/*    */           private int index;
/*    */           
/*    */           public boolean tryAdvance(Consumer<? super T> debug1) {
/* 30 */             while (this.index >= RewindableStream.this.cache.size()) {
/* 31 */               if (!RewindableStream.this.source.tryAdvance(RewindableStream.this.cache::add)) {
/* 32 */                 return false;
/*    */               }
/*    */             } 
/* 35 */             debug1.accept((T)RewindableStream.this.cache.get(this.index++));
/* 36 */             return true;
/*    */           }
/*    */         }false);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\RewindableStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */