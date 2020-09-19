/*     */ package com.mojang.datafixers.types.templates;
/*     */ 
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.common.reflect.TypeToken;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.FamilyOptic;
/*     */ import com.mojang.datafixers.FunctionType;
/*     */ import com.mojang.datafixers.RewriteResult;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.TypedOptic;
/*     */ import com.mojang.datafixers.View;
/*     */ import com.mojang.datafixers.functions.Functions;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.kinds.K1;
/*     */ import com.mojang.datafixers.optics.Affine;
/*     */ import com.mojang.datafixers.optics.Lens;
/*     */ import com.mojang.datafixers.optics.Optic;
/*     */ import com.mojang.datafixers.optics.Optics;
/*     */ import com.mojang.datafixers.optics.Traversal;
/*     */ import com.mojang.datafixers.optics.profunctors.AffineP;
/*     */ import com.mojang.datafixers.optics.profunctors.Cartesian;
/*     */ import com.mojang.datafixers.optics.profunctors.TraversalP;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.families.RecursiveTypeFamily;
/*     */ import com.mojang.datafixers.types.families.TypeFamily;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Decoder;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.Encoder;
/*     */ import com.mojang.serialization.codecs.KeyDispatchCodec;
/*     */ import java.util.Arrays;
/*     */ import java.util.BitSet;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.IntFunction;
/*     */ import java.util.function.Supplier;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ public final class TaggedChoice<K>
/*     */   implements TypeTemplate {
/*     */   private final String name;
/*     */   private final Type<K> keyType;
/*     */   private final Map<K, TypeTemplate> templates;
/*  54 */   private final Map<Pair<TypeFamily, Integer>, Type<?>> types = Maps.newConcurrentMap();
/*     */   private final int size;
/*     */   
/*     */   public TaggedChoice(String name, Type<K> keyType, Map<K, TypeTemplate> templates) {
/*  58 */     this.name = name;
/*  59 */     this.keyType = keyType;
/*  60 */     this.templates = templates;
/*  61 */     this.size = templates.values().stream().mapToInt(TypeTemplate::size).max().orElse(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  66 */     return this.size;
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeFamily apply(TypeFamily family) {
/*  71 */     return index -> (Type)this.types.computeIfAbsent(Pair.of(family, Integer.valueOf(index)), ());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A, B> FamilyOptic<A, B> applyO(FamilyOptic<A, B> input, Type<A> aType, Type<B> bType) {
/*  78 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public <A, B> Either<TypeTemplate, Type.FieldNotFoundException> findFieldOrType(int index, @Nullable String name, Type<A> type, Type<B> resultType) {
/*  83 */     return Either.right(new Type.FieldNotFoundException("Not implemented"));
/*     */   }
/*     */ 
/*     */   
/*     */   public IntFunction<RewriteResult<?, ?>> hmap(TypeFamily family, IntFunction<RewriteResult<?, ?>> function) {
/*  88 */     return index -> {
/*     */         RewriteResult<Pair<K, ?>, Pair<K, ?>> result = RewriteResult.nop(apply(family).apply(index));
/*     */         for (Map.Entry<K, TypeTemplate> entry : this.templates.entrySet()) {
/*     */           RewriteResult<?, ?> elementResult = ((TypeTemplate)entry.getValue()).hmap(family, function).apply(index);
/*     */           result = TaggedChoiceType.elementResult(entry.getKey(), (TaggedChoiceType<K>)result.view().newType(), elementResult).compose(result);
/*     */         } 
/*     */         return result;
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 100 */     if (this == obj) {
/* 101 */       return true;
/*     */     }
/* 103 */     if (!(obj instanceof TaggedChoice)) {
/* 104 */       return false;
/*     */     }
/* 106 */     TaggedChoice<?> other = (TaggedChoice)obj;
/* 107 */     return (Objects.equals(this.name, other.name) && Objects.equals(this.keyType, other.keyType) && Objects.equals(this.templates, other.templates));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 112 */     return Objects.hash(new Object[] { this.name, this.keyType, this.templates });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 117 */     return "TaggedChoice[" + this.name + ", " + Joiner.on(", ").withKeyValueSeparator(" -> ").join(this.templates) + "]";
/*     */   }
/*     */   
/*     */   public static final class TaggedChoiceType<K> extends Type<Pair<K, ?>> {
/*     */     private final String name;
/*     */     private final Type<K> keyType;
/*     */     protected final Map<K, Type<?>> types;
/*     */     private final int hashCode;
/*     */     
/*     */     public TaggedChoiceType(String name, Type<K> keyType, Map<K, Type<?>> types) {
/* 127 */       this.name = name;
/* 128 */       this.keyType = keyType;
/* 129 */       this.types = types;
/* 130 */       this.hashCode = Objects.hash(new Object[] { name, keyType, types });
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public RewriteResult<Pair<K, ?>, ?> all(TypeRewriteRule rule, boolean recurse, boolean checkIndex) {
/* 139 */       Map<K, ? extends RewriteResult<?, ?>> results = (Map<K, ? extends RewriteResult<?, ?>>)this.types.entrySet().stream().map(e -> rule.rewrite((Type)e.getValue()).map(())).filter(e -> (e.isPresent() && !Objects.equals(((RewriteResult)((Pair)e.get()).getSecond()).view().function(), Functions.id()))).map(Optional::get).collect(Pair.toMap());
/*     */ 
/*     */       
/* 142 */       if (results.isEmpty())
/* 143 */         return RewriteResult.nop(this); 
/* 144 */       if (results.size() == 1) {
/* 145 */         Map.Entry<K, ? extends RewriteResult<?, ?>> entry = results.entrySet().iterator().next();
/* 146 */         return elementResult(entry.getKey(), this, entry.getValue());
/*     */       } 
/* 148 */       Map<K, Type<?>> newTypes = Maps.newHashMap(this.types);
/* 149 */       BitSet recData = new BitSet();
/* 150 */       for (Map.Entry<K, ? extends RewriteResult<?, ?>> entry : results.entrySet()) {
/* 151 */         newTypes.put(entry.getKey(), ((RewriteResult)entry.getValue()).view().newType());
/* 152 */         recData.or(((RewriteResult)entry.getValue()).recData());
/*     */       } 
/* 154 */       return RewriteResult.create(View.create(this, DSL.taggedChoiceType(this.name, this.keyType, newTypes), Functions.fun("TaggedChoiceTypeRewriteResult " + results.size(), new RewriteFunc<>(results))), recData);
/*     */     }
/*     */     
/*     */     public static <K, FT, FR> RewriteResult<Pair<K, ?>, Pair<K, ?>> elementResult(K key, TaggedChoiceType<K> type, RewriteResult<FT, FR> result) {
/* 158 */       return opticView(type, result, TypedOptic.tagged(type, key, result.view().type(), result.view().newType()));
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<RewriteResult<Pair<K, ?>, ?>> one(TypeRewriteRule rule) {
/* 163 */       for (Map.Entry<K, Type<?>> entry : this.types.entrySet()) {
/* 164 */         Optional<? extends RewriteResult<?, ?>> elementResult = rule.rewrite(entry.getValue());
/* 165 */         if (elementResult.isPresent()) {
/* 166 */           return Optional.of(elementResult(entry.getKey(), this, elementResult.get()));
/*     */         }
/*     */       } 
/* 169 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public Type<?> updateMu(RecursiveTypeFamily newFamily) {
/* 174 */       return DSL.taggedChoiceType(this.name, this.keyType, (Map)this.types.entrySet().stream().map(e -> Pair.of(e.getKey(), ((Type)e.getValue()).updateMu(newFamily))).collect(Pair.toMap()));
/*     */     }
/*     */ 
/*     */     
/*     */     public TypeTemplate buildTemplate() {
/* 179 */       return DSL.taggedChoice(this.name, this.keyType, (Map)this.types.entrySet().stream().map(e -> Pair.of(e.getKey(), ((Type)e.getValue()).template())).collect(Pair.toMap()));
/*     */     }
/*     */ 
/*     */     
/*     */     private <V> DataResult<? extends Encoder<Pair<K, ?>>> encoder(Pair<K, V> pair) {
/* 184 */       return getCodec((K)pair.getFirst()).map(c -> c.comap(()));
/*     */     }
/*     */ 
/*     */     
/*     */     protected Codec<Pair<K, ?>> buildCodec() {
/* 189 */       return KeyDispatchCodec.unsafe(this.name, this.keyType
/*     */           
/* 191 */           .codec(), p -> DataResult.success(p.getFirst()), k -> getCodec((K)k).map(()), this::encoder)
/*     */ 
/*     */ 
/*     */         
/* 195 */         .codec();
/*     */     }
/*     */     
/*     */     private DataResult<? extends Codec<?>> getCodec(K k) {
/* 199 */       return Optional.ofNullable(this.types.get(k)).map(t -> DataResult.success(t.codec())).orElseGet(() -> DataResult.error("Unsupported key: " + k));
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Type<?>> findFieldTypeOpt(String name) {
/* 204 */       return this.types.values().stream().map(t -> t.findFieldTypeOpt(name)).filter(Optional::isPresent).findFirst().flatMap(Function.identity());
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Pair<K, ?>> point(DynamicOps<?> ops) {
/* 209 */       return this.types.entrySet().stream().map(e -> ((Type)e.getValue()).point(ops).map(())).filter(Optional::isPresent).findFirst().flatMap(Function.identity()).map(p -> p);
/*     */     }
/*     */     
/*     */     public Optional<Typed<Pair<K, ?>>> point(DynamicOps<?> ops, K key, Object value) {
/* 213 */       if (!this.types.containsKey(key)) {
/* 214 */         return Optional.empty();
/*     */       }
/* 216 */       return Optional.of(new Typed(this, ops, Pair.of(key, value)));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <FT, FR> Either<TypedOptic<Pair<K, ?>, ?, FT, FR>, Type.FieldNotFoundException> findTypeInChildren(Type<FT> type, Type<FR> resultType, Type.TypeMatcher<FT, FR> matcher, boolean recurse) {
/*     */       Traversal<Pair<K, ?>, Pair<K, ?>, FT, FR> traversal;
/*     */       TypeToken<? extends K1> bound;
/* 225 */       final Map<K, ? extends TypedOptic<?, ?, FT, FR>> optics = (Map<K, ? extends TypedOptic<?, ?, FT, FR>>)this.types.entrySet().stream().map(e -> Pair.of(e.getKey(), ((Type)e.getValue()).findType(type, resultType, matcher, recurse))).filter(e -> ((Either)e.getSecond()).left().isPresent()).map(e -> e.mapSecond(())).collect(Pair.toMap());
/*     */ 
/*     */       
/* 228 */       if (optics.isEmpty())
/* 229 */         return Either.right(new Type.FieldNotFoundException("Not found in any choices")); 
/* 230 */       if (optics.size() == 1) {
/* 231 */         Map.Entry<K, ? extends TypedOptic<?, ?, FT, FR>> entry = optics.entrySet().iterator().next();
/* 232 */         return Either.left(cap(this, entry.getKey(), entry.getValue()));
/*     */       } 
/* 234 */       Set<TypeToken<? extends K1>> bounds = Sets.newHashSet();
/* 235 */       optics.values().forEach(o -> bounds.addAll(o.bounds()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 242 */       if (TypedOptic.instanceOf(bounds, Cartesian.Mu.TYPE_TOKEN) && optics.size() == this.types.size()) {
/* 243 */         bound = Cartesian.Mu.TYPE_TOKEN;
/*     */         
/* 245 */         Lens<Pair<K, ?>, Pair<K, ?>, FT, FR> lens = new Lens<Pair<K, ?>, Pair<K, ?>, FT, FR>()
/*     */           {
/*     */             public FT view(Pair<K, ?> s) {
/* 248 */               TypedOptic<?, ?, FT, FR> optic = (TypedOptic<?, ?, FT, FR>)optics.get(s.getFirst());
/* 249 */               return (FT)capView(s, optic);
/*     */             }
/*     */ 
/*     */             
/*     */             private <S, T> FT capView(Pair<K, ?> s, TypedOptic<S, T, FT, FR> optic) {
/* 254 */               return (FT)Optics.toLens((Optic)optic.upCast(Cartesian.Mu.TYPE_TOKEN).orElseThrow(IllegalArgumentException::new)).view(s.getSecond());
/*     */             }
/*     */ 
/*     */             
/*     */             public Pair<K, ?> update(FR b, Pair<K, ?> s) {
/* 259 */               TypedOptic<?, ?, FT, FR> optic = (TypedOptic<?, ?, FT, FR>)optics.get(s.getFirst());
/* 260 */               return capUpdate(b, s, optic);
/*     */             }
/*     */ 
/*     */             
/*     */             private <S, T> Pair<K, ?> capUpdate(FR b, Pair<K, ?> s, TypedOptic<S, T, FT, FR> optic) {
/* 265 */               return Pair.of(s.getFirst(), Optics.toLens((Optic)optic.upCast(Cartesian.Mu.TYPE_TOKEN).orElseThrow(IllegalArgumentException::new)).update(b, s.getSecond()));
/*     */             }
/*     */           };
/* 268 */       } else if (TypedOptic.instanceOf(bounds, AffineP.Mu.TYPE_TOKEN)) {
/* 269 */         bound = AffineP.Mu.TYPE_TOKEN;
/*     */         
/* 271 */         Affine<Pair<K, ?>, Pair<K, ?>, FT, FR> affine = new Affine<Pair<K, ?>, Pair<K, ?>, FT, FR>()
/*     */           {
/*     */             public Either<Pair<K, ?>, FT> preview(Pair<K, ?> s) {
/* 274 */               if (!optics.containsKey(s.getFirst())) {
/* 275 */                 return Either.left(s);
/*     */               }
/* 277 */               TypedOptic<?, ?, FT, FR> optic = (TypedOptic<?, ?, FT, FR>)optics.get(s.getFirst());
/* 278 */               return capPreview(s, optic);
/*     */             }
/*     */ 
/*     */             
/*     */             private <S, T> Either<Pair<K, ?>, FT> capPreview(Pair<K, ?> s, TypedOptic<S, T, FT, FR> optic) {
/* 283 */               return Optics.toAffine((Optic)optic.upCast(AffineP.Mu.TYPE_TOKEN).orElseThrow(IllegalArgumentException::new)).preview(s.getSecond()).mapLeft(t -> Pair.of(s.getFirst(), t));
/*     */             }
/*     */ 
/*     */             
/*     */             public Pair<K, ?> set(FR b, Pair<K, ?> s) {
/* 288 */               if (!optics.containsKey(s.getFirst())) {
/* 289 */                 return s;
/*     */               }
/* 291 */               TypedOptic<?, ?, FT, FR> optic = (TypedOptic<?, ?, FT, FR>)optics.get(s.getFirst());
/* 292 */               return capSet(b, s, optic);
/*     */             }
/*     */ 
/*     */             
/*     */             private <S, T> Pair<K, ?> capSet(FR b, Pair<K, ?> s, TypedOptic<S, T, FT, FR> optic) {
/* 297 */               return Pair.of(s.getFirst(), Optics.toAffine((Optic)optic.upCast(AffineP.Mu.TYPE_TOKEN).orElseThrow(IllegalArgumentException::new)).set(b, s.getSecond()));
/*     */             }
/*     */           };
/* 300 */       } else if (TypedOptic.instanceOf(bounds, TraversalP.Mu.TYPE_TOKEN)) {
/* 301 */         bound = TraversalP.Mu.TYPE_TOKEN;
/*     */         
/* 303 */         traversal = new Traversal<Pair<K, ?>, Pair<K, ?>, FT, FR>()
/*     */           {
/*     */             public <F extends K1> FunctionType<Pair<K, ?>, App<F, Pair<K, ?>>> wander(Applicative<F, ?> applicative, FunctionType<FT, App<F, FR>> input) {
/* 306 */               return pair -> {
/*     */                   if (!optics.containsKey(pair.getFirst())) {
/*     */                     return applicative.point(pair);
/*     */                   }
/*     */                   TypedOptic<?, ?, FT, FR> optic = (TypedOptic<?, ?, FT, FR>)optics.get(pair.getFirst());
/*     */                   return capTraversal(applicative, input, pair, optic);
/*     */                 };
/*     */             }
/*     */ 
/*     */             
/*     */             private <S, T, F extends K1> App<F, Pair<K, ?>> capTraversal(Applicative<F, ?> applicative, FunctionType<FT, App<F, FR>> input, Pair<K, ?> pair, TypedOptic<S, T, FT, FR> optic) {
/* 317 */               Traversal<S, T, FT, FR> traversal = Optics.toTraversal((Optic)optic.upCast(TraversalP.Mu.TYPE_TOKEN).orElseThrow(IllegalArgumentException::new));
/* 318 */               return applicative.ap(value -> Pair.of(pair.getFirst(), value), (App)traversal.wander(applicative, input).apply(pair.getSecond()));
/*     */             }
/*     */           };
/*     */       } else {
/* 322 */         throw new IllegalStateException("Could not merge TaggedChoiceType optics, unknown bound: " + Arrays.toString(bounds.toArray()));
/*     */       } 
/*     */       
/* 325 */       Map<K, Type<?>> newTypes = (Map<K, Type<?>>)this.types.entrySet().stream().map(e -> Pair.of(e.getKey(), optics.containsKey(e.getKey()) ? ((TypedOptic)optics.get(e.getKey())).tType() : e.getValue())).collect(Pair.toMap());
/*     */       
/* 327 */       return Either.left(new TypedOptic(bound, this, 
/*     */ 
/*     */             
/* 330 */             DSL.taggedChoiceType(this.name, this.keyType, newTypes), type, resultType, (Optic)traversal));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private <S, T, FT, FR> TypedOptic<Pair<K, ?>, Pair<K, ?>, FT, FR> cap(TaggedChoiceType<K> choiceType, K key, TypedOptic<S, T, FT, FR> optic) {
/* 339 */       return TypedOptic.tagged(choiceType, key, optic.sType(), optic.tType()).compose(optic);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<TaggedChoiceType<?>> findChoiceType(String name, int index) {
/* 344 */       if (Objects.equals(name, this.name)) {
/* 345 */         return Optional.of(this);
/*     */       }
/* 347 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Type<?>> findCheckedType(int index) {
/* 352 */       return this.types.values().stream().map(type -> type.findCheckedType(index)).filter(Optional::isPresent).findFirst().flatMap(Function.identity());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj, boolean ignoreRecursionPoints, boolean checkIndex) {
/* 357 */       if (this == obj) {
/* 358 */         return true;
/*     */       }
/* 360 */       if (!(obj instanceof TaggedChoiceType)) {
/* 361 */         return false;
/*     */       }
/* 363 */       TaggedChoiceType<?> other = (TaggedChoiceType)obj;
/* 364 */       if (!Objects.equals(this.name, other.name)) {
/* 365 */         return false;
/*     */       }
/* 367 */       if (!this.keyType.equals(other.keyType, ignoreRecursionPoints, checkIndex)) {
/* 368 */         return false;
/*     */       }
/* 370 */       if (this.types.size() != other.types.size()) {
/* 371 */         return false;
/*     */       }
/* 373 */       for (Map.Entry<K, Type<?>> entry : this.types.entrySet()) {
/* 374 */         if (!((Type)entry.getValue()).equals(other.types.get(entry.getKey()), ignoreRecursionPoints, checkIndex)) {
/* 375 */           return false;
/*     */         }
/*     */       } 
/* 378 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 383 */       return this.hashCode;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 388 */       return "TaggedChoiceType[" + this.name + ", " + Joiner.on(", \n").withKeyValueSeparator(" -> ").join(this.types) + "]\n";
/*     */     }
/*     */     
/*     */     public String getName() {
/* 392 */       return this.name;
/*     */     }
/*     */     
/*     */     public Type<K> getKeyType() {
/* 396 */       return this.keyType;
/*     */     }
/*     */     
/*     */     public boolean hasType(K key) {
/* 400 */       return this.types.containsKey(key);
/*     */     }
/*     */     
/*     */     public Map<K, Type<?>> types() {
/* 404 */       return this.types;
/*     */     }
/*     */     
/*     */     private static final class RewriteFunc<K> implements Function<DynamicOps<?>, Function<Pair<K, ?>, Pair<K, ?>>> {
/*     */       private final Map<K, ? extends RewriteResult<?, ?>> results;
/*     */       
/*     */       public RewriteFunc(Map<K, ? extends RewriteResult<?, ?>> results) {
/* 411 */         this.results = results;
/*     */       }
/*     */ 
/*     */       
/*     */       public FunctionType<Pair<K, ?>, Pair<K, ?>> apply(DynamicOps<?> ops) {
/* 416 */         return input -> {
/*     */             RewriteResult<?, ?> result = this.results.get(input.getFirst());
/*     */             return (result == null) ? input : capRuleApply(ops, input, result);
/*     */           };
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       private <A, B> Pair<K, B> capRuleApply(DynamicOps<?> ops, Pair<K, ?> input, RewriteResult<A, B> result) {
/* 427 */         return input.mapSecond(v -> ((Function)result.view().function().evalCached().apply(ops)).apply(v));
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean equals(Object o) {
/* 432 */         if (this == o) {
/* 433 */           return true;
/*     */         }
/* 435 */         if (o == null || getClass() != o.getClass()) {
/* 436 */           return false;
/*     */         }
/* 438 */         RewriteFunc<?> that = (RewriteFunc)o;
/* 439 */         return Objects.equals(this.results, that.results);
/*     */       }
/*     */ 
/*     */       
/*     */       public int hashCode() {
/* 444 */         return Objects.hash(new Object[] { this.results });
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\types\templates\TaggedChoice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */