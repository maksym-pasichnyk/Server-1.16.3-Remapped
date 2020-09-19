/*    */ package com.mojang.datafixers.types.constant;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import com.mojang.datafixers.util.Unit;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Optional;
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class EmptyPart
/*    */   extends Type<Unit>
/*    */ {
/*    */   public String toString() {
/* 17 */     return "EmptyPart";
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<Unit> point(DynamicOps<?> ops) {
/* 22 */     return Optional.of(Unit.INSTANCE);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o, boolean ignoreRecursionPoints, boolean checkIndex) {
/* 27 */     return (this == o);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeTemplate buildTemplate() {
/* 32 */     return DSL.constType(this);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Codec<Unit> buildCodec() {
/* 37 */     return Codec.EMPTY.codec();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\types\constant\EmptyPart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */