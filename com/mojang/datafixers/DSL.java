/*     */ package com.mojang.datafixers;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Func;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.constant.EmptyPart;
/*     */ import com.mojang.datafixers.types.constant.EmptyPartPassthrough;
/*     */ import com.mojang.datafixers.types.templates.Check;
/*     */ import com.mojang.datafixers.types.templates.CompoundList;
/*     */ import com.mojang.datafixers.types.templates.Const;
/*     */ import com.mojang.datafixers.types.templates.Hook;
/*     */ import com.mojang.datafixers.types.templates.List;
/*     */ import com.mojang.datafixers.types.templates.Named;
/*     */ import com.mojang.datafixers.types.templates.Product;
/*     */ import com.mojang.datafixers.types.templates.RecursivePoint;
/*     */ import com.mojang.datafixers.types.templates.Sum;
/*     */ import com.mojang.datafixers.types.templates.Tag;
/*     */ import com.mojang.datafixers.types.templates.TaggedChoice;
/*     */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.datafixers.util.Unit;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ import org.apache.commons.lang3.tuple.Triple;
/*     */ 
/*     */ 
/*     */ public interface DSL
/*     */ {
/*     */   public static interface TypeReference
/*     */   {
/*     */     String typeName();
/*     */     
/*     */     default TypeTemplate in(Schema schema) {
/*  40 */       return schema.id(typeName());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static Type<Boolean> bool() {
/*  47 */     return Instances.BOOL_TYPE;
/*     */   }
/*     */   
/*     */   static Type<Integer> intType() {
/*  51 */     return Instances.INT_TYPE;
/*     */   }
/*     */   
/*     */   static Type<Long> longType() {
/*  55 */     return Instances.LONG_TYPE;
/*     */   }
/*     */   
/*     */   static Type<Byte> byteType() {
/*  59 */     return Instances.BYTE_TYPE;
/*     */   }
/*     */   
/*     */   static Type<Short> shortType() {
/*  63 */     return Instances.SHORT_TYPE;
/*     */   }
/*     */   
/*     */   static Type<Float> floatType() {
/*  67 */     return Instances.FLOAT_TYPE;
/*     */   }
/*     */   
/*     */   static Type<Double> doubleType() {
/*  71 */     return Instances.DOUBLE_TYPE;
/*     */   }
/*     */   
/*     */   static Type<String> string() {
/*  75 */     return Instances.STRING_TYPE;
/*     */   }
/*     */   
/*     */   static TypeTemplate emptyPart() {
/*  79 */     return constType(Instances.EMPTY_PART);
/*     */   }
/*     */   
/*     */   static Type<Unit> emptyPartType() {
/*  83 */     return Instances.EMPTY_PART;
/*     */   }
/*     */   
/*     */   static TypeTemplate remainder() {
/*  87 */     return constType(Instances.EMPTY_PASSTHROUGH);
/*     */   }
/*     */   
/*     */   static Type<Dynamic<?>> remainderType() {
/*  91 */     return Instances.EMPTY_PASSTHROUGH;
/*     */   }
/*     */   
/*     */   static TypeTemplate check(String name, int index, TypeTemplate element) {
/*  95 */     return (TypeTemplate)new Check(name, index, element);
/*     */   }
/*     */   
/*     */   static TypeTemplate compoundList(TypeTemplate element) {
/*  99 */     return compoundList(constType(string()), element);
/*     */   }
/*     */   
/*     */   static <V> CompoundList.CompoundListType<String, V> compoundList(Type<V> value) {
/* 103 */     return compoundList(string(), value);
/*     */   }
/*     */   
/*     */   static TypeTemplate compoundList(TypeTemplate key, TypeTemplate element) {
/* 107 */     return and((TypeTemplate)new CompoundList(key, element), remainder());
/*     */   }
/*     */   
/*     */   static <K, V> CompoundList.CompoundListType<K, V> compoundList(Type<K> key, Type<V> value) {
/* 111 */     return new CompoundList.CompoundListType(key, value);
/*     */   }
/*     */   
/*     */   static TypeTemplate constType(Type<?> type) {
/* 115 */     return (TypeTemplate)new Const(type);
/*     */   }
/*     */   
/*     */   static TypeTemplate hook(TypeTemplate template, Hook.HookFunction preRead, Hook.HookFunction postWrite) {
/* 119 */     return (TypeTemplate)new Hook(template, preRead, postWrite);
/*     */   }
/*     */   
/*     */   static <A> Type<A> hook(Type<A> type, Hook.HookFunction preRead, Hook.HookFunction postWrite) {
/* 123 */     return (Type<A>)new Hook.HookType(type, preRead, postWrite);
/*     */   }
/*     */   
/*     */   static TypeTemplate list(TypeTemplate element) {
/* 127 */     return (TypeTemplate)new List(element);
/*     */   }
/*     */   
/*     */   static <A> List.ListType<A> list(Type<A> first) {
/* 131 */     return new List.ListType(first);
/*     */   }
/*     */   
/*     */   static TypeTemplate named(String name, TypeTemplate element) {
/* 135 */     return (TypeTemplate)new Named(name, element);
/*     */   }
/*     */   
/*     */   static <A> Type<Pair<String, A>> named(String name, Type<A> element) {
/* 139 */     return (Type<Pair<String, A>>)new Named.NamedType(name, element);
/*     */   }
/*     */   
/*     */   static TypeTemplate and(TypeTemplate first, TypeTemplate second) {
/* 143 */     return (TypeTemplate)new Product(first, second);
/*     */   }
/*     */   
/*     */   static TypeTemplate and(TypeTemplate first, TypeTemplate... rest) {
/* 147 */     if (rest.length == 0) {
/* 148 */       return first;
/*     */     }
/* 150 */     TypeTemplate result = rest[rest.length - 1];
/* 151 */     for (int i = rest.length - 2; i >= 0; i--) {
/* 152 */       result = and(rest[i], result);
/*     */     }
/* 154 */     return and(first, result);
/*     */   }
/*     */   
/*     */   static TypeTemplate allWithRemainder(TypeTemplate first, TypeTemplate... rest) {
/* 158 */     return and(first, (TypeTemplate[])ArrayUtils.add((Object[])rest, remainder()));
/*     */   }
/*     */   
/*     */   static <F, G> Type<Pair<F, G>> and(Type<F> first, Type<G> second) {
/* 162 */     return (Type<Pair<F, G>>)new Product.ProductType(first, second);
/*     */   }
/*     */   
/*     */   static <F, G, H> Type<Pair<F, Pair<G, H>>> and(Type<F> first, Type<G> second, Type<H> third) {
/* 166 */     return and(first, and(second, third));
/*     */   }
/*     */   
/*     */   static <F, G, H, I> Type<Pair<F, Pair<G, Pair<H, I>>>> and(Type<F> first, Type<G> second, Type<H> third, Type<I> forth) {
/* 170 */     return and(first, and(second, and(third, forth)));
/*     */   }
/*     */   
/*     */   static TypeTemplate id(int index) {
/* 174 */     return (TypeTemplate)new RecursivePoint(index);
/*     */   }
/*     */   
/*     */   static TypeTemplate or(TypeTemplate left, TypeTemplate right) {
/* 178 */     return (TypeTemplate)new Sum(left, right);
/*     */   }
/*     */   
/*     */   static <F, G> Type<Either<F, G>> or(Type<F> first, Type<G> second) {
/* 182 */     return (Type<Either<F, G>>)new Sum.SumType(first, second);
/*     */   }
/*     */   
/*     */   static TypeTemplate field(String name, TypeTemplate element) {
/* 186 */     return (TypeTemplate)new Tag(name, element);
/*     */   }
/*     */   
/*     */   static <A> Tag.TagType<A> field(String name, Type<A> element) {
/* 190 */     return new Tag.TagType(name, element);
/*     */   }
/*     */   
/*     */   static <K> TaggedChoice<K> taggedChoice(String name, Type<K> keyType, Map<K, TypeTemplate> templates) {
/* 194 */     return new TaggedChoice(name, keyType, templates);
/*     */   }
/*     */   
/*     */   static <K> TaggedChoice<K> taggedChoiceLazy(String name, Type<K> keyType, Map<K, Supplier<TypeTemplate>> templates) {
/* 198 */     return taggedChoice(name, keyType, (Map<K, TypeTemplate>)templates.entrySet().stream().map(e -> Pair.of(e.getKey(), ((Supplier)e.getValue()).get())).collect(Pair.toMap()));
/*     */   }
/*     */ 
/*     */   
/*     */   static <K> Type<Pair<K, ?>> taggedChoiceType(String name, Type<K> keyType, Map<K, ? extends Type<?>> types) {
/* 203 */     return (Type<Pair<K, ?>>)Instances.TAGGED_CHOICE_TYPE_CACHE.computeIfAbsent(Triple.of(name, keyType, types), k -> new TaggedChoice.TaggedChoiceType((String)k.getLeft(), (Type)k.getMiddle(), (Map)k.getRight()));
/*     */   }
/*     */   
/*     */   static <A, B> Type<Function<A, B>> func(Type<A> input, Type<B> output) {
/* 207 */     return (Type<Function<A, B>>)new Func(input, output);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static <A> Type<Either<A, Unit>> optional(Type<A> type) {
/* 213 */     return or(type, emptyPartType());
/*     */   }
/*     */   
/*     */   static TypeTemplate optional(TypeTemplate value) {
/* 217 */     return or(value, emptyPart());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static TypeTemplate fields(String name1, TypeTemplate element1) {
/* 223 */     return allWithRemainder(
/* 224 */         field(name1, element1), new TypeTemplate[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static TypeTemplate fields(String name1, TypeTemplate element1, String name2, TypeTemplate element2) {
/* 232 */     return allWithRemainder(
/* 233 */         field(name1, element1), new TypeTemplate[] {
/* 234 */           field(name2, element2)
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static TypeTemplate fields(String name1, TypeTemplate element1, String name2, TypeTemplate element2, String name3, TypeTemplate element3) {
/* 243 */     return allWithRemainder(
/* 244 */         field(name1, element1), new TypeTemplate[] {
/* 245 */           field(name2, element2), 
/* 246 */           field(name3, element3)
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static TypeTemplate fields(String name, TypeTemplate element, TypeTemplate rest) {
/* 254 */     return and(
/* 255 */         field(name, element), rest);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static TypeTemplate fields(String name1, TypeTemplate element1, String name2, TypeTemplate element2, TypeTemplate rest) {
/* 265 */     return and(
/* 266 */         field(name1, element1), new TypeTemplate[] {
/* 267 */           field(name2, element2), rest
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static TypeTemplate fields(String name1, TypeTemplate element1, String name2, TypeTemplate element2, String name3, TypeTemplate element3, TypeTemplate rest) {
/* 278 */     return and(
/* 279 */         field(name1, element1), new TypeTemplate[] {
/* 280 */           field(name2, element2), 
/* 281 */           field(name3, element3), rest
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   static TypeTemplate optionalFields(String name, TypeTemplate element) {
/* 287 */     return allWithRemainder(
/* 288 */         optional(field(name, element)), new TypeTemplate[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static TypeTemplate optionalFields(String name1, TypeTemplate element1, String name2, TypeTemplate element2) {
/* 296 */     return allWithRemainder(
/* 297 */         optional(field(name1, element1)), new TypeTemplate[] {
/* 298 */           optional(field(name2, element2))
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static TypeTemplate optionalFields(String name1, TypeTemplate element1, String name2, TypeTemplate element2, String name3, TypeTemplate element3) {
/* 307 */     return allWithRemainder(
/* 308 */         optional(field(name1, element1)), new TypeTemplate[] {
/* 309 */           optional(field(name2, element2)), 
/* 310 */           optional(field(name3, element3))
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static TypeTemplate optionalFields(String name1, TypeTemplate element1, String name2, TypeTemplate element2, String name3, TypeTemplate element3, String name4, TypeTemplate element4) {
/* 320 */     return allWithRemainder(
/* 321 */         optional(field(name1, element1)), new TypeTemplate[] {
/* 322 */           optional(field(name2, element2)), 
/* 323 */           optional(field(name3, element3)), 
/* 324 */           optional(field(name4, element4))
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static TypeTemplate optionalFields(String name1, TypeTemplate element1, String name2, TypeTemplate element2, String name3, TypeTemplate element3, String name4, TypeTemplate element4, String name5, TypeTemplate element5) {
/* 335 */     return allWithRemainder(
/* 336 */         optional(field(name1, element1)), new TypeTemplate[] {
/* 337 */           optional(field(name2, element2)), 
/* 338 */           optional(field(name3, element3)), 
/* 339 */           optional(field(name4, element4)), 
/* 340 */           optional(field(name5, element5))
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static TypeTemplate optionalFields(String name, TypeTemplate element, TypeTemplate rest) {
/* 348 */     return and(
/* 349 */         optional(field(name, element)), rest);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static TypeTemplate optionalFields(String name1, TypeTemplate element1, String name2, TypeTemplate element2, TypeTemplate rest) {
/* 359 */     return and(
/* 360 */         optional(field(name1, element1)), new TypeTemplate[] {
/* 361 */           optional(field(name2, element2)), rest
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static TypeTemplate optionalFields(String name1, TypeTemplate element1, String name2, TypeTemplate element2, String name3, TypeTemplate element3, TypeTemplate rest) {
/* 372 */     return and(
/* 373 */         optional(field(name1, element1)), new TypeTemplate[] {
/* 374 */           optional(field(name2, element2)), 
/* 375 */           optional(field(name3, element3)), rest
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static TypeTemplate optionalFields(String name1, TypeTemplate element1, String name2, TypeTemplate element2, String name3, TypeTemplate element3, String name4, TypeTemplate element4, TypeTemplate rest) {
/* 387 */     return and(
/* 388 */         optional(field(name1, element1)), new TypeTemplate[] {
/* 389 */           optional(field(name2, element2)), 
/* 390 */           optional(field(name3, element3)), 
/* 391 */           optional(field(name4, element4)), rest
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static TypeTemplate optionalFields(String name1, TypeTemplate element1, String name2, TypeTemplate element2, String name3, TypeTemplate element3, String name4, TypeTemplate element4, String name5, TypeTemplate element5, TypeTemplate rest) {
/* 404 */     return and(
/* 405 */         optional(field(name1, element1)), new TypeTemplate[] {
/* 406 */           optional(field(name2, element2)), 
/* 407 */           optional(field(name3, element3)), 
/* 408 */           optional(field(name4, element4)), 
/* 409 */           optional(field(name5, element5)), rest
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static OpticFinder<Dynamic<?>> remainderFinder() {
/* 417 */     return Instances.REMAINDER_FINDER;
/*     */   }
/*     */   
/*     */   static <FT> OpticFinder<FT> typeFinder(Type<FT> type) {
/* 421 */     return new FieldFinder<>(null, type);
/*     */   }
/*     */   
/*     */   static <FT> OpticFinder<FT> fieldFinder(String name, Type<FT> type) {
/* 425 */     return new FieldFinder<>(name, type);
/*     */   }
/*     */   
/*     */   static <FT> OpticFinder<FT> namedChoice(String name, Type<FT> type) {
/* 429 */     return new NamedChoiceFinder<>(name, type);
/*     */   }
/*     */   
/*     */   static Unit unit() {
/* 433 */     return Unit.INSTANCE;
/*     */   }
/*     */   
/*     */   public static final class Instances {
/* 437 */     private static final Type<Boolean> BOOL_TYPE = (Type<Boolean>)new Const.PrimitiveType((Codec)Codec.BOOL);
/* 438 */     private static final Type<Integer> INT_TYPE = (Type<Integer>)new Const.PrimitiveType((Codec)Codec.INT);
/* 439 */     private static final Type<Long> LONG_TYPE = (Type<Long>)new Const.PrimitiveType((Codec)Codec.LONG);
/* 440 */     private static final Type<Byte> BYTE_TYPE = (Type<Byte>)new Const.PrimitiveType((Codec)Codec.BYTE);
/* 441 */     private static final Type<Short> SHORT_TYPE = (Type<Short>)new Const.PrimitiveType((Codec)Codec.SHORT);
/* 442 */     private static final Type<Float> FLOAT_TYPE = (Type<Float>)new Const.PrimitiveType((Codec)Codec.FLOAT);
/* 443 */     private static final Type<Double> DOUBLE_TYPE = (Type<Double>)new Const.PrimitiveType((Codec)Codec.DOUBLE);
/* 444 */     private static final Type<String> STRING_TYPE = (Type<String>)new Const.PrimitiveType((Codec)Codec.STRING);
/* 445 */     private static final Type<Unit> EMPTY_PART = (Type<Unit>)new EmptyPart();
/* 446 */     private static final Type<Dynamic<?>> EMPTY_PASSTHROUGH = (Type<Dynamic<?>>)new EmptyPartPassthrough();
/*     */     
/* 448 */     private static final OpticFinder<Dynamic<?>> REMAINDER_FINDER = DSL.remainderType().finder();
/*     */     
/* 450 */     private static final Map<Triple<String, Type<?>, Map<?, ? extends Type<?>>>, Type<? extends Pair<?, ?>>> TAGGED_CHOICE_TYPE_CACHE = Maps.newConcurrentMap();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\DSL.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */