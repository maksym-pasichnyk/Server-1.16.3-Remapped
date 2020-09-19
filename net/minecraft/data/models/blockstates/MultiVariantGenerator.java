/*    */ package net.minecraft.data.models.blockstates;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.common.collect.Lists;
/*    */ import com.google.common.collect.Sets;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import java.util.TreeMap;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.Util;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class MultiVariantGenerator
/*    */   implements BlockStateGenerator {
/*    */   private final Block block;
/*    */   private final List<Variant> baseVariants;
/* 22 */   private final Set<Property<?>> seenProperties = Sets.newHashSet();
/* 23 */   private final List<PropertyDispatch> declaredPropertySets = Lists.newArrayList();
/*    */   
/*    */   private MultiVariantGenerator(Block debug1, List<Variant> debug2) {
/* 26 */     this.block = debug1;
/* 27 */     this.baseVariants = debug2;
/*    */   }
/*    */   
/*    */   public MultiVariantGenerator with(PropertyDispatch debug1) {
/* 31 */     debug1.getDefinedProperties().forEach(debug1 -> {
/*    */           if (this.block.getStateDefinition().getProperty(debug1.getName()) != debug1) {
/*    */             throw new IllegalStateException("Property " + debug1 + " is not defined for block " + this.block);
/*    */           }
/*    */           
/*    */           if (!this.seenProperties.add(debug1)) {
/*    */             throw new IllegalStateException("Values of property " + debug1 + " already defined for block " + this.block);
/*    */           }
/*    */         });
/* 40 */     this.declaredPropertySets.add(debug1);
/* 41 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public JsonElement get() {
/* 46 */     Stream<Pair<Selector, List<Variant>>> debug1 = Stream.of(Pair.of(Selector.empty(), this.baseVariants));
/*    */     
/* 48 */     for (PropertyDispatch propertyDispatch : this.declaredPropertySets) {
/* 49 */       Map<Selector, List<Variant>> debug4 = propertyDispatch.getEntries();
/* 50 */       debug1 = debug1.flatMap(debug1 -> debug0.entrySet().stream().map(()));
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 57 */     Map<String, JsonElement> debug2 = new TreeMap<>();
/* 58 */     debug1.forEach(debug1 -> (JsonElement)debug0.put(((Selector)debug1.getFirst()).getKey(), Variant.convertList((List<Variant>)debug1.getSecond())));
/*    */ 
/*    */ 
/*    */     
/* 62 */     JsonObject debug3 = new JsonObject();
/* 63 */     debug3.add("variants", (JsonElement)Util.make(new JsonObject(), debug1 -> debug0.forEach(debug1::add)));
/* 64 */     return (JsonElement)debug3;
/*    */   }
/*    */   
/*    */   private static List<Variant> mergeVariants(List<Variant> debug0, List<Variant> debug1) {
/* 68 */     ImmutableList.Builder<Variant> debug2 = ImmutableList.builder();
/*    */     
/* 70 */     debug0.forEach(debug2 -> debug0.forEach(()));
/* 71 */     return (List<Variant>)debug2.build();
/*    */   }
/*    */ 
/*    */   
/*    */   public Block getBlock() {
/* 76 */     return this.block;
/*    */   }
/*    */   
/*    */   public static MultiVariantGenerator multiVariant(Block debug0) {
/* 80 */     return new MultiVariantGenerator(debug0, (List<Variant>)ImmutableList.of(Variant.variant()));
/*    */   }
/*    */   
/*    */   public static MultiVariantGenerator multiVariant(Block debug0, Variant debug1) {
/* 84 */     return new MultiVariantGenerator(debug0, (List<Variant>)ImmutableList.of(debug1));
/*    */   }
/*    */   
/*    */   public static MultiVariantGenerator multiVariant(Block debug0, Variant... debug1) {
/* 88 */     return new MultiVariantGenerator(debug0, (List<Variant>)ImmutableList.copyOf((Object[])debug1));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\models\blockstates\MultiVariantGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */