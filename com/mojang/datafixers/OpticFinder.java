/*    */ package com.mojang.datafixers;
/*    */ 
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface OpticFinder<FT>
/*    */ {
/*    */   Type<FT> type();
/*    */   
/*    */   <A, FR> Either<TypedOptic<A, ?, FT, FR>, Type.FieldNotFoundException> findType(Type<A> paramType, Type<FR> paramType1, boolean paramBoolean);
/*    */   
/*    */   default <A> Either<TypedOptic<A, ?, FT, FT>, Type.FieldNotFoundException> findType(Type<A> containerType, boolean recurse) {
/* 16 */     return findType(containerType, type(), recurse);
/*    */   }
/*    */   
/*    */   default <GT> OpticFinder<FT> inField(@Nullable final String name, final Type<GT> type) {
/* 20 */     final OpticFinder<FT> outer = this;
/* 21 */     return new OpticFinder<FT>()
/*    */       {
/*    */         public Type<FT> type() {
/* 24 */           return outer.type();
/*    */         }
/*    */ 
/*    */         
/*    */         public <A, FR> Either<TypedOptic<A, ?, FT, FR>, Type.FieldNotFoundException> findType(Type<A> containerType, Type<FR> resultType, boolean recurse) {
/* 29 */           Either<TypedOptic<GT, ?, FT, FR>, Type.FieldNotFoundException> secondOptic = outer.findType(type, resultType, recurse);
/* 30 */           return (Either<TypedOptic<A, ?, FT, FR>, Type.FieldNotFoundException>)secondOptic.map(l -> cap(containerType, l, recurse), Either::right);
/*    */         }
/*    */         
/*    */         private <A, FR, GR> Either<TypedOptic<A, ?, FT, FR>, Type.FieldNotFoundException> cap(Type<A> containterType, TypedOptic<GT, GR, FT, FR> l1, boolean recurse) {
/* 34 */           Either<TypedOptic<A, ?, GT, GR>, Type.FieldNotFoundException> first = DSL.<GT>fieldFinder(name, type).findType(containterType, l1.tType(), recurse);
/* 35 */           return first.mapLeft(l -> l.compose(l1));
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\OpticFinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */