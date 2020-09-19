/*     */ package net.minecraft.data.models.blockstates;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import java.util.List;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ 
/*     */ public class MultiPartGenerator
/*     */   implements BlockStateGenerator {
/*     */   private final Block block;
/*  17 */   private final List<Entry> parts = Lists.newArrayList();
/*     */   
/*     */   private MultiPartGenerator(Block debug1) {
/*  20 */     this.block = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public Block getBlock() {
/*  25 */     return this.block;
/*     */   }
/*     */   
/*     */   public static MultiPartGenerator multiPart(Block debug0) {
/*  29 */     return new MultiPartGenerator(debug0);
/*     */   }
/*     */   
/*     */   public MultiPartGenerator with(List<Variant> debug1) {
/*  33 */     this.parts.add(new Entry(debug1));
/*  34 */     return this;
/*     */   }
/*     */   
/*     */   public MultiPartGenerator with(Variant debug1) {
/*  38 */     return with((List<Variant>)ImmutableList.of(debug1));
/*     */   }
/*     */   
/*     */   public MultiPartGenerator with(Condition debug1, List<Variant> debug2) {
/*  42 */     this.parts.add(new ConditionalEntry(debug1, debug2));
/*  43 */     return this;
/*     */   }
/*     */   
/*     */   public MultiPartGenerator with(Condition debug1, Variant... debug2) {
/*  47 */     return with(debug1, (List<Variant>)ImmutableList.copyOf((Object[])debug2));
/*     */   }
/*     */   
/*     */   public MultiPartGenerator with(Condition debug1, Variant debug2) {
/*  51 */     return with(debug1, (List<Variant>)ImmutableList.of(debug2));
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonElement get() {
/*  56 */     StateDefinition<Block, BlockState> debug1 = this.block.getStateDefinition();
/*  57 */     this.parts.forEach(debug1 -> debug1.validate(debug0));
/*     */     
/*  59 */     JsonArray debug2 = new JsonArray();
/*  60 */     this.parts.stream().map(Entry::get).forEach(debug2::add);
/*     */     
/*  62 */     JsonObject debug3 = new JsonObject();
/*  63 */     debug3.add("multipart", (JsonElement)debug2);
/*  64 */     return (JsonElement)debug3;
/*     */   }
/*     */   
/*     */   static class Entry implements Supplier<JsonElement> {
/*     */     private final List<Variant> variants;
/*     */     
/*     */     private Entry(List<Variant> debug1) {
/*  71 */       this.variants = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public void validate(StateDefinition<?, ?> debug1) {}
/*     */ 
/*     */     
/*     */     public void decorate(JsonObject debug1) {}
/*     */ 
/*     */     
/*     */     public JsonElement get() {
/*  82 */       JsonObject debug1 = new JsonObject();
/*  83 */       decorate(debug1);
/*  84 */       debug1.add("apply", Variant.convertList(this.variants));
/*  85 */       return (JsonElement)debug1;
/*     */     }
/*     */   }
/*     */   
/*     */   static class ConditionalEntry extends Entry {
/*     */     private final Condition condition;
/*     */     
/*     */     private ConditionalEntry(Condition debug1, List<Variant> debug2) {
/*  93 */       super(debug2);
/*  94 */       this.condition = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public void validate(StateDefinition<?, ?> debug1) {
/*  99 */       this.condition.validate(debug1);
/*     */     }
/*     */ 
/*     */     
/*     */     public void decorate(JsonObject debug1) {
/* 104 */       debug1.add("when", this.condition.get());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\models\blockstates\MultiPartGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */