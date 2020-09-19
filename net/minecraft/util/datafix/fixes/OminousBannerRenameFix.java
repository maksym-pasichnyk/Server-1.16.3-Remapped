/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class OminousBannerRenameFix
/*    */   extends DataFix
/*    */ {
/*    */   public OminousBannerRenameFix(Schema debug1, boolean debug2) {
/* 23 */     super(debug1, debug2);
/*    */   }
/*    */   
/*    */   private Dynamic<?> fixTag(Dynamic<?> debug1) {
/* 27 */     Optional<? extends Dynamic<?>> debug2 = debug1.get("display").result();
/* 28 */     if (debug2.isPresent()) {
/* 29 */       Dynamic<?> debug3 = debug2.get();
/* 30 */       Optional<String> debug4 = debug3.get("Name").asString().result();
/* 31 */       if (debug4.isPresent()) {
/* 32 */         String debug5 = debug4.get();
/* 33 */         debug5 = debug5.replace("\"translate\":\"block.minecraft.illager_banner\"", "\"translate\":\"block.minecraft.ominous_banner\"");
/* 34 */         debug3 = debug3.set("Name", debug3.createString(debug5));
/*    */       } 
/* 36 */       return debug1.set("display", debug3);
/*    */     } 
/* 38 */     return debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 43 */     Type<?> debug1 = getInputSchema().getType(References.ITEM_STACK);
/* 44 */     OpticFinder<Pair<String, String>> debug2 = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/* 45 */     OpticFinder<?> debug3 = debug1.findField("tag");
/*    */     
/* 47 */     return fixTypeEverywhereTyped("OminousBannerRenameFix", debug1, debug3 -> {
/*    */           Optional<Pair<String, String>> debug4 = debug3.getOptional(debug1);
/*    */           if (debug4.isPresent() && Objects.equals(((Pair)debug4.get()).getSecond(), "minecraft:white_banner")) {
/*    */             Optional<? extends Typed<?>> debug5 = debug3.getOptionalTyped(debug2);
/*    */             if (debug5.isPresent()) {
/*    */               Typed<?> debug6 = debug5.get();
/*    */               Dynamic<?> debug7 = (Dynamic)debug6.get(DSL.remainderFinder());
/*    */               return debug3.set(debug2, debug6.set(DSL.remainderFinder(), fixTag(debug7)));
/*    */             } 
/*    */           } 
/*    */           return debug3;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\OminousBannerRenameFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */