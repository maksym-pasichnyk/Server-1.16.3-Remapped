/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public abstract class BlockRenameFix
/*    */   extends DataFix {
/*    */   public BlockRenameFix(Schema debug1, String debug2) {
/* 20 */     super(debug1, false);
/* 21 */     this.name = debug2;
/*    */   }
/*    */   private final String name;
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 26 */     Type<?> debug1 = getInputSchema().getType(References.BLOCK_NAME);
/* 27 */     Type<Pair<String, String>> debug2 = DSL.named(References.BLOCK_NAME.typeName(), NamespacedSchema.namespacedString());
/* 28 */     if (!Objects.equals(debug1, debug2)) {
/* 29 */       throw new IllegalStateException("block type is not what was expected.");
/*    */     }
/*    */     
/* 32 */     TypeRewriteRule debug3 = fixTypeEverywhere(this.name + " for block", debug2, debug1 -> ());
/*    */     
/* 34 */     TypeRewriteRule debug4 = fixTypeEverywhereTyped(this.name + " for block_state", getInputSchema().getType(References.BLOCK_STATE), debug1 -> debug1.update(DSL.remainderFinder(), ()));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 42 */     return TypeRewriteRule.seq(debug3, debug4);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static DataFix create(Schema debug0, String debug1, final Function<String, String> fixBlock) {
/* 48 */     return new BlockRenameFix(debug0, debug1)
/*    */       {
/*    */         protected String fixBlock(String debug1) {
/* 51 */           return fixBlock.apply(debug1);
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   protected abstract String fixBlock(String paramString);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\BlockRenameFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */