/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Arrays;
/*    */ import java.util.Optional;
/*    */ import java.util.UUID;
/*    */ import java.util.function.Function;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ 
/*    */ public abstract class AbstractUUIDFix
/*    */   extends DataFix
/*    */ {
/* 20 */   protected static final Logger LOGGER = LogManager.getLogger();
/*    */   protected DSL.TypeReference typeReference;
/*    */   
/*    */   public AbstractUUIDFix(Schema debug1, DSL.TypeReference debug2) {
/* 24 */     super(debug1, false);
/* 25 */     this.typeReference = debug2;
/*    */   }
/*    */   
/*    */   protected Typed<?> updateNamedChoice(Typed<?> debug1, String debug2, Function<Dynamic<?>, Dynamic<?>> debug3) {
/* 29 */     Type<?> debug4 = getInputSchema().getChoiceType(this.typeReference, debug2);
/* 30 */     Type<?> debug5 = getOutputSchema().getChoiceType(this.typeReference, debug2);
/* 31 */     return debug1.updateTyped(DSL.namedChoice(debug2, debug4), debug5, debug1 -> debug1.update(DSL.remainderFinder(), debug0));
/*    */   }
/*    */   
/*    */   protected static Optional<Dynamic<?>> replaceUUIDString(Dynamic<?> debug0, String debug1, String debug2) {
/* 35 */     return createUUIDFromString(debug0, debug1).map(debug3 -> debug0.remove(debug1).set(debug2, debug3));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected static Optional<Dynamic<?>> replaceUUIDMLTag(Dynamic<?> debug0, String debug1, String debug2) {
/* 41 */     return debug0.get(debug1).result().flatMap(AbstractUUIDFix::createUUIDFromML).map(debug3 -> debug0.remove(debug1).set(debug2, debug3));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected static Optional<Dynamic<?>> replaceUUIDLeastMost(Dynamic<?> debug0, String debug1, String debug2) {
/* 47 */     String debug3 = debug1 + "Most";
/* 48 */     String debug4 = debug1 + "Least";
/* 49 */     return createUUIDFromLongs(debug0, debug3, debug4).map(debug4 -> debug0.remove(debug1).remove(debug2).set(debug3, debug4));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected static Optional<Dynamic<?>> createUUIDFromString(Dynamic<?> debug0, String debug1) {
/* 55 */     return debug0.get(debug1).result().flatMap(debug1 -> {
/*    */           String debug2 = debug1.asString(null);
/*    */           if (debug2 != null) {
/*    */             try {
/*    */               UUID debug3 = UUID.fromString(debug2);
/*    */               return createUUIDTag(debug0, debug3.getMostSignificantBits(), debug3.getLeastSignificantBits());
/* 61 */             } catch (IllegalArgumentException illegalArgumentException) {}
/*    */           }
/*    */           return Optional.empty();
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected static Optional<Dynamic<?>> createUUIDFromML(Dynamic<?> debug0) {
/* 70 */     return createUUIDFromLongs(debug0, "M", "L");
/*    */   }
/*    */   
/*    */   protected static Optional<Dynamic<?>> createUUIDFromLongs(Dynamic<?> debug0, String debug1, String debug2) {
/* 74 */     long debug3 = debug0.get(debug1).asLong(0L);
/* 75 */     long debug5 = debug0.get(debug2).asLong(0L);
/* 76 */     if (debug3 == 0L || debug5 == 0L) {
/* 77 */       return Optional.empty();
/*    */     }
/* 79 */     return createUUIDTag(debug0, debug3, debug5);
/*    */   }
/*    */   
/*    */   protected static Optional<Dynamic<?>> createUUIDTag(Dynamic<?> debug0, long debug1, long debug3) {
/* 83 */     return Optional.of(debug0.createIntList(Arrays.stream(new int[] { (int)(debug1 >> 32L), (int)debug1, (int)(debug3 >> 32L), (int)debug3 })));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\AbstractUUIDFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */