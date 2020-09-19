/*    */ package com.google.common.reflect;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.lang.reflect.Type;
/*    */ import java.lang.reflect.TypeVariable;
/*    */ import javax.annotation.Nullable;
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
/*    */ 
/*    */ @Beta
/*    */ public abstract class TypeParameter<T>
/*    */   extends TypeCapture<T>
/*    */ {
/*    */   final TypeVariable<?> typeVariable;
/*    */   
/*    */   protected TypeParameter() {
/* 42 */     Type type = capture();
/* 43 */     Preconditions.checkArgument(type instanceof TypeVariable, "%s should be a type variable.", type);
/* 44 */     this.typeVariable = (TypeVariable)type;
/*    */   }
/*    */ 
/*    */   
/*    */   public final int hashCode() {
/* 49 */     return this.typeVariable.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean equals(@Nullable Object o) {
/* 54 */     if (o instanceof TypeParameter) {
/* 55 */       TypeParameter<?> that = (TypeParameter)o;
/* 56 */       return this.typeVariable.equals(that.typeVariable);
/*    */     } 
/* 58 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 63 */     return this.typeVariable.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\reflect\TypeParameter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */