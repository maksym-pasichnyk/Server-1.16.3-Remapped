/*    */ package com.mojang.datafixers.types.templates;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.FamilyOptic;
/*    */ import com.mojang.datafixers.RewriteResult;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.families.TypeFamily;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import java.util.function.IntFunction;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface TypeTemplate
/*    */ {
/*    */   int size();
/*    */   
/*    */   TypeFamily apply(TypeFamily paramTypeFamily);
/*    */   
/*    */   default Type<?> toSimpleType() {
/* 21 */     return apply(new TypeFamily()
/*    */         {
/*    */           public Type<?> apply(int index) {
/* 24 */             return DSL.emptyPartType();
/*    */           }
/* 31 */         }).apply(-1);
/*    */   }
/*    */   
/*    */   <A, B> Either<TypeTemplate, Type.FieldNotFoundException> findFieldOrType(int paramInt, @Nullable String paramString, Type<A> paramType, Type<B> paramType1);
/*    */   
/*    */   IntFunction<RewriteResult<?, ?>> hmap(TypeFamily paramTypeFamily, IntFunction<RewriteResult<?, ?>> paramIntFunction);
/*    */   
/*    */   <A, B> FamilyOptic<A, B> applyO(FamilyOptic<A, B> paramFamilyOptic, Type<A> paramType, Type<B> paramType1);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\types\templates\TypeTemplate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */