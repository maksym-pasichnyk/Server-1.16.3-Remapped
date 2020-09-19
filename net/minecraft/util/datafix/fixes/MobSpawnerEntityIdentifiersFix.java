/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class MobSpawnerEntityIdentifiersFix
/*    */   extends DataFix {
/*    */   public MobSpawnerEntityIdentifiersFix(Schema debug1, boolean debug2) {
/* 19 */     super(debug1, debug2);
/*    */   }
/*    */   
/*    */   private Dynamic<?> fix(Dynamic<?> debug1) {
/* 23 */     if (!"MobSpawner".equals(debug1.get("id").asString(""))) {
/* 24 */       return debug1;
/*    */     }
/*    */     
/* 27 */     Optional<String> debug2 = debug1.get("EntityId").asString().result();
/* 28 */     if (debug2.isPresent()) {
/* 29 */       Dynamic<?> dynamic = (Dynamic)DataFixUtils.orElse(debug1.get("SpawnData").result(), debug1.emptyMap());
/* 30 */       dynamic = dynamic.set("id", dynamic.createString(((String)debug2.get()).isEmpty() ? "Pig" : debug2.get()));
/* 31 */       debug1 = debug1.set("SpawnData", dynamic);
/*    */       
/* 33 */       debug1 = debug1.remove("EntityId");
/*    */     } 
/*    */     
/* 36 */     Optional<? extends Stream<? extends Dynamic<?>>> debug3 = debug1.get("SpawnPotentials").asStreamOpt().result();
/* 37 */     if (debug3.isPresent()) {
/* 38 */       debug1 = debug1.set("SpawnPotentials", debug1.createList(((Stream)debug3.get()).map(debug0 -> {
/*    */                 Optional<String> debug1 = debug0.get("Type").asString().result();
/*    */                 
/*    */                 if (debug1.isPresent()) {
/*    */                   Dynamic<?> debug2 = ((Dynamic)DataFixUtils.orElse(debug0.get("Properties").result(), debug0.emptyMap())).set("id", debug0.createString(debug1.get()));
/*    */                   
/*    */                   return debug0.set("Entity", debug2).remove("Type").remove("Properties");
/*    */                 } 
/*    */                 
/*    */                 return debug0;
/*    */               })));
/*    */     }
/*    */     
/* 51 */     return debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 56 */     Type<?> debug1 = getOutputSchema().getType(References.UNTAGGED_SPAWNER);
/* 57 */     return fixTypeEverywhereTyped("MobSpawnerEntityIdentifiersFix", getInputSchema().getType(References.UNTAGGED_SPAWNER), debug1, debug2 -> {
/*    */           Dynamic<?> debug3 = (Dynamic)debug2.get(DSL.remainderFinder());
/*    */           debug3 = debug3.set("id", debug3.createString("MobSpawner"));
/*    */           DataResult<? extends Pair<? extends Typed<?>, ?>> debug4 = debug1.readTyped(fix(debug3));
/*    */           return !debug4.result().isPresent() ? debug2 : (Typed)((Pair)debug4.result().get()).getFirst();
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\MobSpawnerEntityIdentifiersFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */