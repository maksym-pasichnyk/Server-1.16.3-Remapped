/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class EntityHorseSplitFix
/*    */   extends EntityRenameFix {
/*    */   public EntityHorseSplitFix(Schema debug1, boolean debug2) {
/* 14 */     super("EntityHorseSplitFix", debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Pair<String, Typed<?>> fix(String debug1, Typed<?> debug2) {
/* 19 */     Dynamic<?> debug3 = (Dynamic)debug2.get(DSL.remainderFinder());
/* 20 */     if (Objects.equals("EntityHorse", debug1)) {
/*    */       
/* 22 */       int debug5 = debug3.get("Type").asInt(0);
/* 23 */       switch (debug5)
/*    */       
/*    */       { default:
/* 26 */           debug4 = "Horse";
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
/* 41 */           debug3.remove("Type");
/*    */           
/* 43 */           debug6 = (Type)getOutputSchema().findChoiceType(References.ENTITY).types().get(debug4);
/* 44 */           return Pair.of(debug4, ((Pair)debug2.write().flatMap(debug6::readTyped).result().orElseThrow(() -> new IllegalStateException("Could not parse the new horse"))).getFirst());case 1: debug4 = "Donkey"; debug3.remove("Type"); debug6 = (Type)getOutputSchema().findChoiceType(References.ENTITY).types().get(debug4); return Pair.of(debug4, ((Pair)debug2.write().flatMap(debug6::readTyped).result().orElseThrow(() -> new IllegalStateException("Could not parse the new horse"))).getFirst());case 2: debug4 = "Mule"; debug3.remove("Type"); debug6 = (Type)getOutputSchema().findChoiceType(References.ENTITY).types().get(debug4); return Pair.of(debug4, ((Pair)debug2.write().flatMap(debug6::readTyped).result().orElseThrow(() -> new IllegalStateException("Could not parse the new horse"))).getFirst());case 3: debug4 = "ZombieHorse"; debug3.remove("Type"); debug6 = (Type)getOutputSchema().findChoiceType(References.ENTITY).types().get(debug4); return Pair.of(debug4, ((Pair)debug2.write().flatMap(debug6::readTyped).result().orElseThrow(() -> new IllegalStateException("Could not parse the new horse"))).getFirst());case 4: break; }  String debug4 = "SkeletonHorse"; debug3.remove("Type"); Type<?> debug6 = (Type)getOutputSchema().findChoiceType(References.ENTITY).types().get(debug4); return Pair.of(debug4, ((Pair)debug2.write().flatMap(debug6::readTyped).result().orElseThrow(() -> new IllegalStateException("Could not parse the new horse"))).getFirst());
/*    */     } 
/* 46 */     return Pair.of(debug1, debug2);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityHorseSplitFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */