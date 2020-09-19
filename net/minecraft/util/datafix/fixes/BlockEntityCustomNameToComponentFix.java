/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class BlockEntityCustomNameToComponentFix extends DataFix {
/*    */   public BlockEntityCustomNameToComponentFix(Schema debug1, boolean debug2) {
/* 16 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 21 */     OpticFinder<String> debug1 = DSL.fieldFinder("id", NamespacedSchema.namespacedString());
/* 22 */     return fixTypeEverywhereTyped("BlockEntityCustomNameToComponentFix", getInputSchema().getType(References.BLOCK_ENTITY), debug1 -> debug1.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\BlockEntityCustomNameToComponentFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */