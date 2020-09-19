/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public abstract class ItemRenameFix
/*    */   extends DataFix {
/*    */   private final String name;
/*    */   
/*    */   public ItemRenameFix(Schema debug1, String debug2) {
/* 19 */     super(debug1, false);
/* 20 */     this.name = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 25 */     Type<Pair<String, String>> debug1 = DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString());
/* 26 */     if (!Objects.equals(getInputSchema().getType(References.ITEM_NAME), debug1)) {
/* 27 */       throw new IllegalStateException("item name type is not what was expected.");
/*    */     }
/* 29 */     return fixTypeEverywhere(this.name, debug1, debug1 -> ());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static DataFix create(Schema debug0, String debug1, final Function<String, String> fixItem) {
/* 35 */     return new ItemRenameFix(debug0, debug1)
/*    */       {
/*    */         protected String fixItem(String debug1) {
/* 38 */           return fixItem.apply(debug1);
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   protected abstract String fixItem(String paramString);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\ItemRenameFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */