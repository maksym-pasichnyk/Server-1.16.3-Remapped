/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class ItemBannerColorFix
/*    */   extends DataFix {
/*    */   public ItemBannerColorFix(Schema debug1, boolean debug2) {
/* 22 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 27 */     Type<?> debug1 = getInputSchema().getType(References.ITEM_STACK);
/*    */     
/* 29 */     OpticFinder<Pair<String, String>> debug2 = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/* 30 */     OpticFinder<?> debug3 = debug1.findField("tag");
/* 31 */     OpticFinder<?> debug4 = debug3.type().findField("BlockEntityTag");
/*    */     
/* 33 */     return fixTypeEverywhereTyped("ItemBannerColorFix", debug1, debug3 -> {
/*    */           Optional<Pair<String, String>> debug4 = debug3.getOptional(debug0);
/*    */           if (debug4.isPresent() && Objects.equals(((Pair)debug4.get()).getSecond(), "minecraft:banner")) {
/*    */             Dynamic<?> debug5 = (Dynamic)debug3.get(DSL.remainderFinder());
/*    */             Optional<? extends Typed<?>> debug6 = debug3.getOptionalTyped(debug1);
/*    */             if (debug6.isPresent()) {
/*    */               Typed<?> debug7 = debug6.get();
/*    */               Optional<? extends Typed<?>> debug8 = debug7.getOptionalTyped(debug2);
/*    */               if (debug8.isPresent()) {
/*    */                 Typed<?> debug9 = debug8.get();
/*    */                 Dynamic<?> debug10 = (Dynamic)debug7.get(DSL.remainderFinder());
/*    */                 Dynamic<?> debug11 = (Dynamic)debug9.getOrCreate(DSL.remainderFinder());
/*    */                 if (debug11.get("Base").asNumber().result().isPresent()) {
/*    */                   debug5 = debug5.set("Damage", debug5.createShort((short)(debug11.get("Base").asInt(0) & 0xF)));
/*    */                   Optional<? extends Dynamic<?>> debug12 = debug10.get("display").result();
/*    */                   if (debug12.isPresent()) {
/*    */                     Dynamic<?> debug13 = debug12.get();
/*    */                     Dynamic<?> debug14 = debug13.createMap((Map)ImmutableMap.of(debug13.createString("Lore"), debug13.createList(Stream.of(debug13.createString("(+NBT")))));
/*    */                     if (Objects.equals(debug13, debug14))
/*    */                       return debug3.set(DSL.remainderFinder(), debug5); 
/*    */                   } 
/*    */                   debug11.remove("Base");
/*    */                   return debug3.set(DSL.remainderFinder(), debug5).set(debug1, debug7.set(debug2, debug9.set(DSL.remainderFinder(), debug11)));
/*    */                 } 
/*    */               } 
/*    */             } 
/*    */             return debug3.set(DSL.remainderFinder(), debug5);
/*    */           } 
/*    */           return debug3;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\ItemBannerColorFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */