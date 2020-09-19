/*    */ package com.mojang.datafixers.types.families;
/*    */ 
/*    */ import com.mojang.datafixers.FamilyOptic;
/*    */ import com.mojang.datafixers.OpticParts;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import java.util.function.IntFunction;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface TypeFamily
/*    */ {
/*    */   Type<?> apply(int paramInt);
/*    */   
/*    */   static <A, B> FamilyOptic<A, B> familyOptic(IntFunction<OpticParts<A, B>> optics) {
/* 15 */     return optics::apply;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\types\families\TypeFamily.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */