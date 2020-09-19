/*     */ package net.minecraft.data.models.blockstates;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public interface Condition
/*     */   extends Supplier<JsonElement> {
/*     */   void validate(StateDefinition<?, ?> paramStateDefinition);
/*     */   
/*     */   public enum Operation {
/*  21 */     AND("AND"),
/*  22 */     OR("OR");
/*     */     
/*     */     private final String id;
/*     */ 
/*     */     
/*     */     Operation(String debug3) {
/*  28 */       this.id = debug3;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class CompositeCondition implements Condition {
/*     */     private final Condition.Operation operation;
/*     */     private final List<Condition> subconditions;
/*     */     
/*     */     private CompositeCondition(Condition.Operation debug1, List<Condition> debug2) {
/*  37 */       this.operation = debug1;
/*  38 */       this.subconditions = debug2;
/*     */     }
/*     */ 
/*     */     
/*     */     public void validate(StateDefinition<?, ?> debug1) {
/*  43 */       this.subconditions.forEach(debug1 -> debug1.validate(debug0));
/*     */     }
/*     */ 
/*     */     
/*     */     public JsonElement get() {
/*  48 */       JsonArray debug1 = new JsonArray();
/*  49 */       this.subconditions.stream().map(Supplier::get).forEach(debug1::add);
/*     */       
/*  51 */       JsonObject debug2 = new JsonObject();
/*  52 */       debug2.add(this.operation.id, (JsonElement)debug1);
/*  53 */       return (JsonElement)debug2;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class TerminalCondition implements Condition {
/*  58 */     private final Map<Property<?>, String> terms = Maps.newHashMap();
/*     */     
/*     */     private static <T extends Comparable<T>> String joinValues(Property<T> debug0, Stream<T> debug1) {
/*  61 */       return debug1.<CharSequence>map(debug0::getName).collect(Collectors.joining("|"));
/*     */     }
/*     */     
/*     */     private static <T extends Comparable<T>> String getTerm(Property<T> debug0, T debug1, T[] debug2) {
/*  65 */       return joinValues(debug0, Stream.concat(Stream.of(debug1), Stream.of(debug2)));
/*     */     }
/*     */     
/*     */     private <T extends Comparable<T>> void putValue(Property<T> debug1, String debug2) {
/*  69 */       String debug3 = this.terms.put(debug1, debug2);
/*  70 */       if (debug3 != null) {
/*  71 */         throw new IllegalStateException("Tried to replace " + debug1 + " value from " + debug3 + " to " + debug2);
/*     */       }
/*     */     }
/*     */     
/*     */     public final <T extends Comparable<T>> TerminalCondition term(Property<T> debug1, T debug2) {
/*  76 */       putValue(debug1, debug1.getName((Comparable)debug2));
/*  77 */       return this;
/*     */     }
/*     */     
/*     */     @SafeVarargs
/*     */     public final <T extends Comparable<T>> TerminalCondition term(Property<T> debug1, T debug2, T... debug3) {
/*  82 */       putValue(debug1, getTerm(debug1, debug2, debug3));
/*  83 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public JsonElement get() {
/* 100 */       JsonObject debug1 = new JsonObject();
/* 101 */       this.terms.forEach((debug1, debug2) -> debug0.addProperty(debug1.getName(), debug2));
/* 102 */       return (JsonElement)debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public void validate(StateDefinition<?, ?> debug1) {
/* 107 */       List<Property<?>> debug2 = (List<Property<?>>)this.terms.keySet().stream().filter(debug1 -> (debug0.getProperty(debug1.getName()) != debug1)).collect(Collectors.toList());
/* 108 */       if (!debug2.isEmpty()) {
/* 109 */         throw new IllegalStateException("Properties " + debug2 + " are missing from " + debug1);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   static TerminalCondition condition() {
/* 115 */     return new TerminalCondition();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Condition or(Condition... debug0) {
/* 123 */     return new CompositeCondition(Operation.OR, Arrays.asList(debug0));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\models\blockstates\Condition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */