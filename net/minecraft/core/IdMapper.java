/*    */ package net.minecraft.core;
/*    */ 
/*    */ import com.google.common.base.Predicates;
/*    */ import com.google.common.collect.Iterators;
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.IdentityHashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ 
/*    */ public class IdMapper<T>
/*    */   implements IdMap<T>
/*    */ {
/*    */   private int nextId;
/*    */   private final IdentityHashMap<T, Integer> tToId;
/*    */   private final List<T> idToT;
/*    */   
/*    */   public IdMapper() {
/* 20 */     this(512);
/*    */   }
/*    */   
/*    */   public IdMapper(int debug1) {
/* 24 */     this.idToT = Lists.newArrayListWithExpectedSize(debug1);
/* 25 */     this.tToId = new IdentityHashMap<>(debug1);
/*    */   }
/*    */   
/*    */   public void addMapping(T debug1, int debug2) {
/* 29 */     this.tToId.put(debug1, Integer.valueOf(debug2));
/*    */ 
/*    */     
/* 32 */     while (this.idToT.size() <= debug2) {
/* 33 */       this.idToT.add(null);
/*    */     }
/*    */     
/* 36 */     this.idToT.set(debug2, debug1);
/*    */     
/* 38 */     if (this.nextId <= debug2) {
/* 39 */       this.nextId = debug2 + 1;
/*    */     }
/*    */   }
/*    */   
/*    */   public void add(T debug1) {
/* 44 */     addMapping(debug1, this.nextId);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getId(T debug1) {
/* 49 */     Integer debug2 = this.tToId.get(debug1);
/* 50 */     return (debug2 == null) ? -1 : debug2.intValue();
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public final T byId(int debug1) {
/* 56 */     if (debug1 >= 0 && debug1 < this.idToT.size()) {
/* 57 */       return this.idToT.get(debug1);
/*    */     }
/*    */     
/* 60 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Iterator<T> iterator() {
/* 65 */     return (Iterator<T>)Iterators.filter(this.idToT.iterator(), Predicates.notNull());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int size() {
/* 73 */     return this.tToId.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\core\IdMapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */