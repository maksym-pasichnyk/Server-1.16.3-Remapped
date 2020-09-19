/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.google.gson.JsonParseException;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.network.chat.TextComponent;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ 
/*    */ public class ItemWrittenBookPagesStrictJsonFix extends DataFix {
/*    */   public ItemWrittenBookPagesStrictJsonFix(Schema debug1, boolean debug2) {
/* 19 */     super(debug1, debug2);
/*    */   }
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> debug1) {
/* 23 */     return debug1.update("pages", debug1 -> (Dynamic)DataFixUtils.orElse(debug1.asStreamOpt().map(()).map(debug0::createList).result(), debug0.emptyList()));
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
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 72 */     Type<?> debug1 = getInputSchema().getType(References.ITEM_STACK);
/* 73 */     OpticFinder<?> debug2 = debug1.findField("tag");
/*    */     
/* 75 */     return fixTypeEverywhereTyped("ItemWrittenBookPagesStrictJsonFix", debug1, debug2 -> debug2.updateTyped(debug1, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\ItemWrittenBookPagesStrictJsonFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */