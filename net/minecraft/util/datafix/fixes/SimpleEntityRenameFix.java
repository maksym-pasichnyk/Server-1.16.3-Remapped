/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public abstract class SimpleEntityRenameFix extends EntityRenameFix {
/*    */   public SimpleEntityRenameFix(String debug1, Schema debug2, boolean debug3) {
/* 11 */     super(debug1, debug2, debug3);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Pair<String, Typed<?>> fix(String debug1, Typed<?> debug2) {
/* 16 */     Pair<String, Dynamic<?>> debug3 = getNewNameAndTag(debug1, (Dynamic)debug2.getOrCreate(DSL.remainderFinder()));
/* 17 */     return Pair.of(debug3.getFirst(), debug2.set(DSL.remainderFinder(), debug3.getSecond()));
/*    */   }
/*    */   
/*    */   protected abstract Pair<String, Dynamic<?>> getNewNameAndTag(String paramString, Dynamic<?> paramDynamic);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\SimpleEntityRenameFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */