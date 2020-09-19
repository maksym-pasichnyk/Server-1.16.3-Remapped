/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.datafixers.util.Unit;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EntityEquipmentToArmorAndHandFix
/*    */   extends DataFix
/*    */ {
/*    */   public EntityEquipmentToArmorAndHandFix(Schema debug1, boolean debug2) {
/* 32 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 37 */     return cap(getInputSchema().getTypeRaw(References.ITEM_STACK));
/*    */   }
/*    */ 
/*    */   
/*    */   private <IS> TypeRewriteRule cap(Type<IS> debug1) {
/* 42 */     Type<Pair<Either<List<IS>, Unit>, Dynamic<?>>> debug2 = DSL.and(
/* 43 */         DSL.optional((Type)DSL.field("Equipment", (Type)DSL.list(debug1))), 
/* 44 */         DSL.remainderType());
/*    */ 
/*    */     
/* 47 */     Type<Pair<Either<List<IS>, Unit>, Pair<Either<List<IS>, Unit>, Dynamic<?>>>> debug3 = DSL.and(
/* 48 */         DSL.optional((Type)DSL.field("ArmorItems", (Type)DSL.list(debug1))), 
/* 49 */         DSL.optional((Type)DSL.field("HandItems", (Type)DSL.list(debug1))), 
/* 50 */         DSL.remainderType());
/*    */     
/* 52 */     OpticFinder<Pair<Either<List<IS>, Unit>, Dynamic<?>>> debug4 = DSL.typeFinder(debug2);
/*    */     
/* 54 */     OpticFinder<List<IS>> debug5 = DSL.fieldFinder("Equipment", (Type)DSL.list(debug1));
/*    */     
/* 56 */     return fixTypeEverywhereTyped("EntityEquipmentToArmorAndHandFix", getInputSchema().getType(References.ENTITY), getOutputSchema().getType(References.ENTITY), debug4 -> {
/*    */           Either<List<IS>, Unit> debug5 = Either.right(DSL.unit());
/*    */           Either<List<IS>, Unit> debug6 = Either.right(DSL.unit());
/*    */           Dynamic<?> debug7 = (Dynamic)debug4.getOrCreate(DSL.remainderFinder());
/*    */           Optional<List<IS>> debug8 = debug4.getOptional(debug0);
/*    */           if (debug8.isPresent()) {
/*    */             List<IS> list = debug8.get();
/*    */             IS iS = (IS)((Pair)debug1.read(debug7.emptyMap()).result().orElseThrow(())).getFirst();
/*    */             if (!list.isEmpty())
/*    */               debug5 = Either.left(Lists.newArrayList(new Object[] { list.get(0), iS })); 
/*    */             if (list.size() > 1) {
/*    */               List<IS> debug11 = Lists.newArrayList(new Object[] { iS, iS, iS, iS });
/*    */               for (int debug12 = 1; debug12 < Math.min(list.size(), 5); debug12++)
/*    */                 debug11.set(debug12 - 1, list.get(debug12)); 
/*    */               debug6 = Either.left(debug11);
/*    */             } 
/*    */           } 
/*    */           Dynamic<?> debug9 = debug7;
/*    */           Optional<? extends Stream<? extends Dynamic<?>>> debug10 = debug7.get("DropChances").asStreamOpt().result();
/*    */           if (debug10.isPresent()) {
/*    */             Iterator<? extends Dynamic<?>> debug11 = Stream.<Dynamic<?>>concat(debug10.get(), Stream.generate(())).iterator();
/*    */             float debug12 = ((Dynamic)debug11.next()).asFloat(0.0F);
/*    */             if (!debug7.get("HandDropChances").result().isPresent()) {
/*    */               Dynamic<?> debug13 = debug7.createList(Stream.<Float>of(new Float[] { Float.valueOf(debug12), Float.valueOf(0.0F) }).map(debug7::createFloat));
/*    */               debug7 = debug7.set("HandDropChances", debug13);
/*    */             } 
/*    */             if (!debug7.get("ArmorDropChances").result().isPresent()) {
/*    */               Dynamic<?> debug13 = debug7.createList(Stream.<Float>of(new Float[] { Float.valueOf(((Dynamic)debug11.next()).asFloat(0.0F)), Float.valueOf(((Dynamic)debug11.next()).asFloat(0.0F)), Float.valueOf(((Dynamic)debug11.next()).asFloat(0.0F)), Float.valueOf(((Dynamic)debug11.next()).asFloat(0.0F)) }).map(debug7::createFloat));
/*    */               debug7 = debug7.set("ArmorDropChances", debug13);
/*    */             } 
/*    */             debug7 = debug7.remove("DropChances");
/*    */           } 
/*    */           return debug4.set(debug2, debug3, Pair.of(debug5, Pair.of(debug6, debug7)));
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityEquipmentToArmorAndHandFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */