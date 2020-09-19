/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ public class WeightedList<U> {
/*     */   protected final List<WeightedEntry<U>> entries;
/*     */   
/*     */   public WeightedList() {
/*  20 */     this(Lists.newArrayList());
/*     */   }
/*     */   private final Random random;
/*     */   private WeightedList(List<WeightedEntry<U>> debug1) {
/*  24 */     this.random = new Random();
/*  25 */     this.entries = Lists.newArrayList(debug1);
/*     */   }
/*     */   
/*     */   public static <U> Codec<WeightedList<U>> codec(Codec<U> debug0) {
/*  29 */     return WeightedEntry.<E>codec(debug0).listOf().xmap(WeightedList::new, debug0 -> debug0.entries);
/*     */   }
/*     */   
/*     */   public WeightedList<U> add(U debug1, int debug2) {
/*  33 */     this.entries.add(new WeightedEntry<>(debug1, debug2));
/*  34 */     return this;
/*     */   }
/*     */   
/*     */   public WeightedList<U> shuffle() {
/*  38 */     return shuffle(this.random);
/*     */   }
/*     */   
/*     */   public WeightedList<U> shuffle(Random debug1) {
/*  42 */     this.entries.forEach(debug1 -> debug1.setRandom(debug0.nextFloat()));
/*  43 */     this.entries.sort(Comparator.comparingDouble(debug0 -> ((WeightedEntry)debug0).getRandWeight()));
/*  44 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  48 */     return this.entries.isEmpty();
/*     */   }
/*     */   
/*     */   public Stream<U> stream() {
/*  52 */     return this.entries.stream().map(WeightedEntry::getData);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public U getOne(Random debug1) {
/*  60 */     return (U)shuffle(debug1).stream().findFirst().orElseThrow(RuntimeException::new);
/*     */   }
/*     */   
/*     */   public static class WeightedEntry<T> {
/*     */     private final T data;
/*     */     private final int weight;
/*     */     private double randWeight;
/*     */     
/*     */     private WeightedEntry(T debug1, int debug2) {
/*  69 */       this.weight = debug2;
/*  70 */       this.data = debug1;
/*     */     }
/*     */     
/*     */     private double getRandWeight() {
/*  74 */       return this.randWeight;
/*     */     }
/*     */     
/*     */     private void setRandom(float debug1) {
/*  78 */       this.randWeight = -Math.pow(debug1, (1.0F / this.weight));
/*     */     }
/*     */     
/*     */     public T getData() {
/*  82 */       return this.data;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/*  91 */       return "" + this.weight + ":" + this.data;
/*     */     }
/*     */     
/*     */     public static <E> Codec<WeightedEntry<E>> codec(final Codec<E> elementCodec) {
/*  95 */       return new Codec<WeightedEntry<E>>()
/*     */         {
/*     */           public <T> DataResult<Pair<WeightedList.WeightedEntry<E>, T>> decode(DynamicOps<T> debug1, T debug2) {
/*  98 */             Dynamic<T> debug3 = new Dynamic(debug1, debug2);
/*  99 */             return debug3.get("data")
/* 100 */               .flatMap(elementCodec::parse)
/* 101 */               .map(debug1 -> new WeightedList.WeightedEntry(debug1, debug0.get("weight").asInt(1)))
/* 102 */               .map(debug1 -> Pair.of(debug1, debug0.empty()));
/*     */           }
/*     */ 
/*     */           
/*     */           public <T> DataResult<T> encode(WeightedList.WeightedEntry<E> debug1, DynamicOps<T> debug2, T debug3) {
/* 107 */             return debug2.mapBuilder()
/* 108 */               .add("weight", debug2.createInt(debug1.weight))
/* 109 */               .add("data", elementCodec.encodeStart(debug2, debug1.data))
/* 110 */               .build(debug3);
/*     */           }
/*     */         };
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 118 */     return "WeightedList[" + this.entries + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\WeightedList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */