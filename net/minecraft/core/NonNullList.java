/*    */ package net.minecraft.core;
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.AbstractList;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
/*    */ import org.apache.commons.lang3.Validate;
/*    */ 
/*    */ public class NonNullList<E> extends AbstractList<E> {
/*    */   private final List<E> list;
/*    */   
/*    */   public static <E> NonNullList<E> create() {
/* 14 */     return new NonNullList<>();
/*    */   }
/*    */   private final E defaultValue;
/*    */   
/*    */   public static <E> NonNullList<E> withSize(int debug0, E debug1) {
/* 19 */     Validate.notNull(debug1);
/*    */     
/* 21 */     Object[] debug2 = new Object[debug0];
/* 22 */     Arrays.fill(debug2, debug1);
/* 23 */     return new NonNullList<>(Arrays.asList((E[])debug2), debug1);
/*    */   }
/*    */   
/*    */   @SafeVarargs
/*    */   public static <E> NonNullList<E> of(E debug0, E... debug1) {
/* 28 */     return new NonNullList<>(Arrays.asList(debug1), debug0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected NonNullList() {
/* 35 */     this(Lists.newArrayList(), null);
/*    */   }
/*    */   
/*    */   protected NonNullList(List<E> debug1, @Nullable E debug2) {
/* 39 */     this.list = debug1;
/* 40 */     this.defaultValue = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public E get(int debug1) {
/* 46 */     return this.list.get(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public E set(int debug1, E debug2) {
/* 51 */     Validate.notNull(debug2);
/*    */     
/* 53 */     return this.list.set(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public void add(int debug1, E debug2) {
/* 58 */     Validate.notNull(debug2);
/*    */     
/* 60 */     this.list.add(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public E remove(int debug1) {
/* 65 */     return this.list.remove(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 70 */     return this.list.size();
/*    */   }
/*    */ 
/*    */   
/*    */   public void clear() {
/* 75 */     if (this.defaultValue == null) {
/* 76 */       super.clear();
/*    */     } else {
/* 78 */       for (int debug1 = 0; debug1 < size(); debug1++)
/* 79 */         set(debug1, this.defaultValue); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\core\NonNullList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */