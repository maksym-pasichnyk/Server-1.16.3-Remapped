/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TextComponent;
/*    */ 
/*    */ public class ItemLoreFix extends DataFix {
/*    */   public ItemLoreFix(Schema debug1, boolean debug2) {
/* 18 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 23 */     Type<?> debug1 = getInputSchema().getType(References.ITEM_STACK);
/* 24 */     OpticFinder<?> debug2 = debug1.findField("tag");
/*    */     
/* 26 */     return fixTypeEverywhereTyped("Item Lore componentize", debug1, debug1 -> debug1.updateTyped(debug0, ()));
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
/*    */   
/*    */   private static <T> Stream<Dynamic<T>> fixLoreList(Stream<Dynamic<T>> debug0) {
/* 40 */     return debug0.map(debug0 -> (Dynamic)DataFixUtils.orElse(debug0.asString().map(ItemLoreFix::fixLoreEntry).map(debug0::createString).result(), debug0));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static String fixLoreEntry(String debug0) {
/* 46 */     return Component.Serializer.toJson((Component)new TextComponent(debug0));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\ItemLoreFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */