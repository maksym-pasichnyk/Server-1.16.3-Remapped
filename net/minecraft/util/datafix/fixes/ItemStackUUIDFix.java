/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class ItemStackUUIDFix
/*    */   extends AbstractUUIDFix {
/*    */   public ItemStackUUIDFix(Schema debug1) {
/* 15 */     super(debug1, References.ITEM_STACK);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 20 */     OpticFinder<Pair<String, String>> debug1 = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/*    */     
/* 22 */     return fixTypeEverywhereTyped("ItemStackUUIDFix", getInputSchema().getType(this.typeReference), debug2 -> {
/*    */           OpticFinder<?> debug3 = debug2.getType().findField("tag");
/*    */           return debug2.updateTyped(debug3, ());
/*    */         });
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
/*    */   private Dynamic<?> updateAttributeModifiers(Dynamic<?> debug1) {
/* 38 */     return debug1.update("AttributeModifiers", debug1 -> debug0.createList(debug1.asStream().map(())));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Dynamic<?> updateSkullOwner(Dynamic<?> debug1) {
/* 46 */     return debug1.update("SkullOwner", debug0 -> (Dynamic)replaceUUIDString(debug0, "Id", "Id").orElse(debug0));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\ItemStackUUIDFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */