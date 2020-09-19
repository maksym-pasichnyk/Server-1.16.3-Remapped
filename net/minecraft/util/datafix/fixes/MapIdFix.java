/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class MapIdFix extends DataFix {
/*    */   public MapIdFix(Schema debug1, boolean debug2) {
/* 16 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 21 */     Type<?> debug1 = getInputSchema().getType(References.SAVED_DATA);
/* 22 */     OpticFinder<?> debug2 = debug1.findField("data");
/* 23 */     return fixTypeEverywhereTyped("Map id fix", debug1, debug1 -> {
/*    */           Optional<? extends Typed<?>> debug2 = debug1.getOptionalTyped(debug0);
/*    */           return debug2.isPresent() ? debug1 : debug1.update(DSL.remainderFinder(), ());
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\MapIdFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */