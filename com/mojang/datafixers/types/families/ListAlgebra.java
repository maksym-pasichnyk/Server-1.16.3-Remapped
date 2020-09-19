/*    */ package com.mojang.datafixers.types.families;
/*    */ 
/*    */ import com.mojang.datafixers.RewriteResult;
/*    */ import com.mojang.datafixers.functions.PointFree;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Collectors;
/*    */ 
/*    */ 
/*    */ public final class ListAlgebra
/*    */   implements Algebra
/*    */ {
/*    */   private final String name;
/*    */   private final List<RewriteResult<?, ?>> views;
/*    */   private int hashCode;
/*    */   
/*    */   public ListAlgebra(String name, List<RewriteResult<?, ?>> views) {
/* 18 */     this.name = name;
/* 19 */     this.views = views;
/*    */   }
/*    */ 
/*    */   
/*    */   public RewriteResult<?, ?> apply(int index) {
/* 24 */     return this.views.get(index);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 29 */     return toString(0);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString(int level) {
/* 34 */     String wrap = "\n" + PointFree.indent(level + 1);
/* 35 */     return "Algebra[" + this.name + wrap + (String)this.views.stream().map(view -> view.view().function().toString(level + 1)).collect(Collectors.joining(wrap)) + "\n" + PointFree.indent(level) + "]";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 40 */     if (this == o) {
/* 41 */       return true;
/*    */     }
/* 43 */     if (!(o instanceof ListAlgebra)) {
/* 44 */       return false;
/*    */     }
/* 46 */     ListAlgebra that = (ListAlgebra)o;
/* 47 */     return Objects.equals(this.views, that.views);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 52 */     if (this.hashCode == 0) {
/* 53 */       this.hashCode = this.views.hashCode();
/*    */     }
/* 55 */     return this.hashCode;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\types\families\ListAlgebra.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */