/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.PeekingIterator;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.MapLike;
/*     */ import com.mojang.serialization.RecordBuilder;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.LongStream;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ public class NbtOps implements DynamicOps<Tag> {
/*  27 */   public static final NbtOps INSTANCE = new NbtOps();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Tag empty() {
/*  34 */     return EndTag.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public <U> U convertTo(DynamicOps<U> debug1, Tag debug2) {
/*  39 */     switch (debug2.getId()) {
/*     */       case 0:
/*  41 */         return (U)debug1.empty();
/*     */       case 1:
/*  43 */         return (U)debug1.createByte(((NumericTag)debug2).getAsByte());
/*     */       case 2:
/*  45 */         return (U)debug1.createShort(((NumericTag)debug2).getAsShort());
/*     */       case 3:
/*  47 */         return (U)debug1.createInt(((NumericTag)debug2).getAsInt());
/*     */       case 4:
/*  49 */         return (U)debug1.createLong(((NumericTag)debug2).getAsLong());
/*     */       case 5:
/*  51 */         return (U)debug1.createFloat(((NumericTag)debug2).getAsFloat());
/*     */       case 6:
/*  53 */         return (U)debug1.createDouble(((NumericTag)debug2).getAsDouble());
/*     */       case 7:
/*  55 */         return (U)debug1.createByteList(ByteBuffer.wrap(((ByteArrayTag)debug2).getAsByteArray()));
/*     */       case 8:
/*  57 */         return (U)debug1.createString(debug2.getAsString());
/*     */       case 9:
/*  59 */         return (U)convertList(debug1, debug2);
/*     */       case 10:
/*  61 */         return (U)convertMap(debug1, debug2);
/*     */       case 11:
/*  63 */         return (U)debug1.createIntList(Arrays.stream(((IntArrayTag)debug2).getAsIntArray()));
/*     */       case 12:
/*  65 */         return (U)debug1.createLongList(Arrays.stream(((LongArrayTag)debug2).getAsLongArray()));
/*     */     } 
/*  67 */     throw new IllegalStateException("Unknown tag type: " + debug2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DataResult<Number> getNumberValue(Tag debug1) {
/*  73 */     if (debug1 instanceof NumericTag) {
/*  74 */       return DataResult.success(((NumericTag)debug1).getAsNumber());
/*     */     }
/*  76 */     return DataResult.error("Not a number");
/*     */   }
/*     */ 
/*     */   
/*     */   public Tag createNumeric(Number debug1) {
/*  81 */     return DoubleTag.valueOf(debug1.doubleValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public Tag createByte(byte debug1) {
/*  86 */     return ByteTag.valueOf(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public Tag createShort(short debug1) {
/*  91 */     return ShortTag.valueOf(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public Tag createInt(int debug1) {
/*  96 */     return IntTag.valueOf(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public Tag createLong(long debug1) {
/* 101 */     return LongTag.valueOf(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public Tag createFloat(float debug1) {
/* 106 */     return FloatTag.valueOf(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public Tag createDouble(double debug1) {
/* 111 */     return DoubleTag.valueOf(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public Tag createBoolean(boolean debug1) {
/* 116 */     return ByteTag.valueOf(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<String> getStringValue(Tag debug1) {
/* 121 */     if (debug1 instanceof StringTag) {
/* 122 */       return DataResult.success(debug1.getAsString());
/*     */     }
/* 124 */     return DataResult.error("Not a string");
/*     */   }
/*     */ 
/*     */   
/*     */   public Tag createString(String debug1) {
/* 129 */     return StringTag.valueOf(debug1);
/*     */   }
/*     */   
/*     */   private static CollectionTag<?> createGenericList(byte debug0, byte debug1) {
/* 133 */     if (typesMatch(debug0, debug1, (byte)4)) {
/* 134 */       return new LongArrayTag(new long[0]);
/*     */     }
/* 136 */     if (typesMatch(debug0, debug1, (byte)1)) {
/* 137 */       return new ByteArrayTag(new byte[0]);
/*     */     }
/* 139 */     if (typesMatch(debug0, debug1, (byte)3)) {
/* 140 */       return new IntArrayTag(new int[0]);
/*     */     }
/* 142 */     return new ListTag();
/*     */   }
/*     */   
/*     */   private static boolean typesMatch(byte debug0, byte debug1, byte debug2) {
/* 146 */     return (debug0 == debug2 && (debug1 == debug2 || debug1 == 0));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T extends Tag> void fillOne(CollectionTag<T> debug0, Tag debug1, Tag debug2) {
/* 153 */     if (debug1 instanceof CollectionTag) {
/* 154 */       CollectionTag<?> debug3 = (CollectionTag)debug1;
/* 155 */       debug3.forEach(debug1 -> debug0.add(debug1));
/*     */     } 
/*     */     
/* 158 */     debug0.add((T)debug2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T extends Tag> void fillMany(CollectionTag<T> debug0, Tag debug1, List<Tag> debug2) {
/* 164 */     if (debug1 instanceof CollectionTag) {
/* 165 */       CollectionTag<?> debug3 = (CollectionTag)debug1;
/* 166 */       debug3.forEach(debug1 -> debug0.add(debug1));
/*     */     } 
/*     */     
/* 169 */     debug2.forEach(debug1 -> debug0.add(debug1));
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Tag> mergeToList(Tag debug1, Tag debug2) {
/* 174 */     if (!(debug1 instanceof CollectionTag) && !(debug1 instanceof EndTag)) {
/* 175 */       return DataResult.error("mergeToList called with not a list: " + debug1, debug1);
/*     */     }
/*     */     
/* 178 */     CollectionTag<?> debug3 = createGenericList((debug1 instanceof CollectionTag) ? ((CollectionTag)debug1)
/* 179 */         .getElementType() : 0, debug2
/* 180 */         .getId());
/*     */     
/* 182 */     fillOne(debug3, debug1, debug2);
/* 183 */     return DataResult.success(debug3);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Tag> mergeToList(Tag debug1, List<Tag> debug2) {
/* 188 */     if (!(debug1 instanceof CollectionTag) && !(debug1 instanceof EndTag)) {
/* 189 */       return DataResult.error("mergeToList called with not a list: " + debug1, debug1);
/*     */     }
/*     */     
/* 192 */     CollectionTag<?> debug3 = createGenericList((debug1 instanceof CollectionTag) ? ((CollectionTag)debug1)
/* 193 */         .getElementType() : 0, ((Byte)debug2
/* 194 */         .stream().findFirst().map(Tag::getId).orElse(Byte.valueOf((byte)0))).byteValue());
/*     */     
/* 196 */     fillMany(debug3, debug1, debug2);
/* 197 */     return DataResult.success(debug3);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Tag> mergeToMap(Tag debug1, Tag debug2, Tag debug3) {
/* 202 */     if (!(debug1 instanceof CompoundTag) && !(debug1 instanceof EndTag)) {
/* 203 */       return DataResult.error("mergeToMap called with not a map: " + debug1, debug1);
/*     */     }
/* 205 */     if (!(debug2 instanceof StringTag)) {
/* 206 */       return DataResult.error("key is not a string: " + debug2, debug1);
/*     */     }
/*     */     
/* 209 */     CompoundTag debug4 = new CompoundTag();
/* 210 */     if (debug1 instanceof CompoundTag) {
/* 211 */       CompoundTag debug5 = (CompoundTag)debug1;
/* 212 */       debug5.getAllKeys().forEach(debug2 -> debug0.put(debug2, debug1.get(debug2)));
/*     */     } 
/* 214 */     debug4.put(debug2.getAsString(), debug3);
/* 215 */     return DataResult.success(debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Tag> mergeToMap(Tag debug1, MapLike<Tag> debug2) {
/* 220 */     if (!(debug1 instanceof CompoundTag) && !(debug1 instanceof EndTag)) {
/* 221 */       return DataResult.error("mergeToMap called with not a map: " + debug1, debug1);
/*     */     }
/*     */     
/* 224 */     CompoundTag debug3 = new CompoundTag();
/* 225 */     if (debug1 instanceof CompoundTag) {
/* 226 */       CompoundTag compoundTag = (CompoundTag)debug1;
/* 227 */       compoundTag.getAllKeys().forEach(debug2 -> debug0.put(debug2, debug1.get(debug2)));
/*     */     } 
/*     */     
/* 230 */     List<Tag> debug4 = Lists.newArrayList();
/*     */     
/* 232 */     debug2.entries().forEach(debug2 -> {
/*     */           Tag debug3 = (Tag)debug2.getFirst();
/*     */           
/*     */           if (!(debug3 instanceof StringTag)) {
/*     */             debug0.add(debug3);
/*     */             
/*     */             return;
/*     */           } 
/*     */           debug1.put(debug3.getAsString(), (Tag)debug2.getSecond());
/*     */         });
/* 242 */     if (!debug4.isEmpty()) {
/* 243 */       return DataResult.error("some keys are not strings: " + debug4, debug3);
/*     */     }
/*     */     
/* 246 */     return DataResult.success(debug3);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Stream<Pair<Tag, Tag>>> getMapValues(Tag debug1) {
/* 251 */     if (!(debug1 instanceof CompoundTag)) {
/* 252 */       return DataResult.error("Not a map: " + debug1);
/*     */     }
/* 254 */     CompoundTag debug2 = (CompoundTag)debug1;
/* 255 */     return DataResult.success(debug2.getAllKeys().stream().map(debug2 -> Pair.of(createString(debug2), debug1.get(debug2))));
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Consumer<BiConsumer<Tag, Tag>>> getMapEntries(Tag debug1) {
/* 260 */     if (!(debug1 instanceof CompoundTag)) {
/* 261 */       return DataResult.error("Not a map: " + debug1);
/*     */     }
/* 263 */     CompoundTag debug2 = (CompoundTag)debug1;
/* 264 */     return DataResult.success(debug2 -> debug1.getAllKeys().forEach(()));
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<MapLike<Tag>> getMap(Tag debug1) {
/* 269 */     if (!(debug1 instanceof CompoundTag)) {
/* 270 */       return DataResult.error("Not a map: " + debug1);
/*     */     }
/* 272 */     final CompoundTag tag = (CompoundTag)debug1;
/* 273 */     return DataResult.success(new MapLike<Tag>()
/*     */         {
/*     */           @Nullable
/*     */           public Tag get(Tag debug1) {
/* 277 */             return tag.get(debug1.getAsString());
/*     */           }
/*     */ 
/*     */           
/*     */           @Nullable
/*     */           public Tag get(String debug1) {
/* 283 */             return tag.get(debug1);
/*     */           }
/*     */ 
/*     */           
/*     */           public Stream<Pair<Tag, Tag>> entries() {
/* 288 */             return tag.getAllKeys().stream().map(debug2 -> Pair.of(NbtOps.this.createString(debug2), debug1.get(debug2)));
/*     */           }
/*     */ 
/*     */           
/*     */           public String toString() {
/* 293 */             return "MapLike[" + tag + "]";
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public Tag createMap(Stream<Pair<Tag, Tag>> debug1) {
/* 300 */     CompoundTag debug2 = new CompoundTag();
/* 301 */     debug1.forEach(debug1 -> debug0.put(((Tag)debug1.getFirst()).getAsString(), (Tag)debug1.getSecond()));
/*     */ 
/*     */     
/* 304 */     return debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Stream<Tag>> getStream(Tag debug1) {
/* 309 */     if (debug1 instanceof CollectionTag) {
/* 310 */       return DataResult.success(((CollectionTag)debug1).stream().map(debug0 -> debug0));
/*     */     }
/* 312 */     return DataResult.error("Not a list");
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Consumer<Consumer<Tag>>> getList(Tag debug1) {
/* 317 */     if (debug1 instanceof CollectionTag) {
/* 318 */       CollectionTag<?> debug2 = (CollectionTag)debug1;
/* 319 */       return DataResult.success(debug2::forEach);
/*     */     } 
/* 321 */     return DataResult.error("Not a list: " + debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<ByteBuffer> getByteBuffer(Tag debug1) {
/* 326 */     if (debug1 instanceof ByteArrayTag) {
/* 327 */       return DataResult.success(ByteBuffer.wrap(((ByteArrayTag)debug1).getAsByteArray()));
/*     */     }
/* 329 */     return super.getByteBuffer(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public Tag createByteList(ByteBuffer debug1) {
/* 334 */     return new ByteArrayTag(DataFixUtils.toArray(debug1));
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<IntStream> getIntStream(Tag debug1) {
/* 339 */     if (debug1 instanceof IntArrayTag) {
/* 340 */       return DataResult.success(Arrays.stream(((IntArrayTag)debug1).getAsIntArray()));
/*     */     }
/* 342 */     return super.getIntStream(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public Tag createIntList(IntStream debug1) {
/* 347 */     return new IntArrayTag(debug1.toArray());
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<LongStream> getLongStream(Tag debug1) {
/* 352 */     if (debug1 instanceof LongArrayTag) {
/* 353 */       return DataResult.success(Arrays.stream(((LongArrayTag)debug1).getAsLongArray()));
/*     */     }
/* 355 */     return super.getLongStream(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public Tag createLongList(LongStream debug1) {
/* 360 */     return new LongArrayTag(debug1.toArray());
/*     */   }
/*     */ 
/*     */   
/*     */   public Tag createList(Stream<Tag> debug1) {
/* 365 */     PeekingIterator<Tag> debug2 = Iterators.peekingIterator(debug1.iterator());
/* 366 */     if (!debug2.hasNext()) {
/* 367 */       return new ListTag();
/*     */     }
/* 369 */     Tag debug3 = (Tag)debug2.peek();
/* 370 */     if (debug3 instanceof ByteTag) {
/* 371 */       List<Byte> list = Lists.newArrayList(Iterators.transform((Iterator)debug2, debug0 -> Byte.valueOf(((ByteTag)debug0).getAsByte())));
/* 372 */       return new ByteArrayTag(list);
/*     */     } 
/* 374 */     if (debug3 instanceof IntTag) {
/* 375 */       List<Integer> list = Lists.newArrayList(Iterators.transform((Iterator)debug2, debug0 -> Integer.valueOf(((IntTag)debug0).getAsInt())));
/* 376 */       return new IntArrayTag(list);
/*     */     } 
/* 378 */     if (debug3 instanceof LongTag) {
/* 379 */       List<Long> list = Lists.newArrayList(Iterators.transform((Iterator)debug2, debug0 -> Long.valueOf(((LongTag)debug0).getAsLong())));
/* 380 */       return new LongArrayTag(list);
/*     */     } 
/* 382 */     ListTag debug4 = new ListTag();
/* 383 */     while (debug2.hasNext()) {
/* 384 */       Tag debug5 = (Tag)debug2.next();
/* 385 */       if (debug5 instanceof EndTag) {
/*     */         continue;
/*     */       }
/* 388 */       debug4.add(debug5);
/*     */     } 
/* 390 */     return debug4;
/*     */   }
/*     */ 
/*     */   
/*     */   public Tag remove(Tag debug1, String debug2) {
/* 395 */     if (debug1 instanceof CompoundTag) {
/* 396 */       CompoundTag debug3 = (CompoundTag)debug1;
/* 397 */       CompoundTag debug4 = new CompoundTag();
/* 398 */       debug3.getAllKeys().stream().filter(debug1 -> !Objects.equals(debug1, debug0)).forEach(debug2 -> debug0.put(debug2, debug1.get(debug2)));
/* 399 */       return debug4;
/*     */     } 
/* 401 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 406 */     return "NBT";
/*     */   }
/*     */ 
/*     */   
/*     */   public RecordBuilder<Tag> mapBuilder() {
/* 411 */     return (RecordBuilder<Tag>)new NbtRecordBuilder();
/*     */   }
/*     */   
/*     */   class NbtRecordBuilder extends RecordBuilder.AbstractStringBuilder<Tag, CompoundTag> {
/*     */     protected NbtRecordBuilder() {
/* 416 */       super(NbtOps.this);
/*     */     }
/*     */ 
/*     */     
/*     */     protected CompoundTag initBuilder() {
/* 421 */       return new CompoundTag();
/*     */     }
/*     */ 
/*     */     
/*     */     protected CompoundTag append(String debug1, Tag debug2, CompoundTag debug3) {
/* 426 */       debug3.put(debug1, debug2);
/* 427 */       return debug3;
/*     */     }
/*     */ 
/*     */     
/*     */     protected DataResult<Tag> build(CompoundTag debug1, Tag debug2) {
/* 432 */       if (debug2 == null || debug2 == EndTag.INSTANCE) {
/* 433 */         return DataResult.success(debug1);
/*     */       }
/* 435 */       if (debug2 instanceof CompoundTag) {
/* 436 */         CompoundTag debug3 = new CompoundTag(Maps.newHashMap(((CompoundTag)debug2).entries()));
/* 437 */         for (Map.Entry<String, Tag> debug5 : debug1.entries().entrySet()) {
/* 438 */           debug3.put(debug5.getKey(), debug5.getValue());
/*     */         }
/* 440 */         return DataResult.success(debug3);
/*     */       } 
/* 442 */       return DataResult.error("mergeToMap called with not a map: " + debug2, debug2);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\nbt\NbtOps.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */