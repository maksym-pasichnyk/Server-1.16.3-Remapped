/*    */ package com.mojang.datafixers;
/*    */ 
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import java.util.BitSet;
/*    */ import java.util.Objects;
/*    */ import org.apache.commons.lang3.ObjectUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class RewriteResult<A, B>
/*    */ {
/*    */   protected final View<A, B> view;
/*    */   protected final BitSet recData;
/*    */   
/*    */   public RewriteResult(View<A, B> view, BitSet recData) {
/* 17 */     this.view = view;
/* 18 */     this.recData = recData;
/*    */   }
/*    */   
/*    */   public static <A, B> RewriteResult<A, B> create(View<A, B> view, BitSet recData) {
/* 22 */     return new RewriteResult<>(view, recData);
/*    */   }
/*    */   
/*    */   public static <A> RewriteResult<A, A> nop(Type<A> type) {
/* 26 */     return new RewriteResult<>(View.nopView(type), new BitSet());
/*    */   }
/*    */   
/*    */   public <C> RewriteResult<C, B> compose(RewriteResult<C, A> that) {
/*    */     BitSet newData;
/* 31 */     if (this.view.type() instanceof com.mojang.datafixers.types.templates.RecursivePoint.RecursivePointType && that.view.type() instanceof com.mojang.datafixers.types.templates.RecursivePoint.RecursivePointType) {
/*    */       
/* 33 */       newData = (BitSet)ObjectUtils.clone(this.recData);
/* 34 */       newData.or(that.recData);
/*    */     } else {
/* 36 */       newData = this.recData;
/*    */     } 
/* 38 */     return create(this.view.compose(that.view), newData);
/*    */   }
/*    */   
/*    */   public BitSet recData() {
/* 42 */     return this.recData;
/*    */   }
/*    */   
/*    */   public View<A, B> view() {
/* 46 */     return this.view;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 51 */     return "RR[" + this.view + "]";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 56 */     if (this == o) {
/* 57 */       return true;
/*    */     }
/* 59 */     if (o == null || getClass() != o.getClass()) {
/* 60 */       return false;
/*    */     }
/* 62 */     RewriteResult<?, ?> that = (RewriteResult<?, ?>)o;
/* 63 */     return Objects.equals(this.view, that.view);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 68 */     return Objects.hash(new Object[] { this.view });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\RewriteResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */