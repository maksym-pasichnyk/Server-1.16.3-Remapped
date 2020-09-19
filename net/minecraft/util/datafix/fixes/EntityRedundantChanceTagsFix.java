/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.OptionalDynamic;
/*    */ import java.util.List;
/*    */ 
/*    */ public class EntityRedundantChanceTagsFix extends DataFix {
/* 13 */   private static final Codec<List<Float>> FLOAT_LIST_CODEC = Codec.FLOAT.listOf();
/*    */   
/*    */   public EntityRedundantChanceTagsFix(Schema debug1, boolean debug2) {
/* 16 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 22 */     return fixTypeEverywhereTyped("EntityRedundantChanceTagsFix", getInputSchema().getType(References.ENTITY), debug0 -> debug0.update(DSL.remainderFinder(), ()));
/*    */   }
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
/*    */   private static boolean isZeroList(OptionalDynamic<?> debug0, int debug1) {
/* 35 */     return ((Boolean)debug0.flatMap(FLOAT_LIST_CODEC::parse).map(debug1 -> Boolean.valueOf((debug1.size() == debug0 && debug1.stream().allMatch(())))).result().orElse(Boolean.valueOf(false))).booleanValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityRedundantChanceTagsFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */