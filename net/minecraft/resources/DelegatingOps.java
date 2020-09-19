/*     */ package net.minecraft.resources;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.ListBuilder;
/*     */ import com.mojang.serialization.MapLike;
/*     */ import com.mojang.serialization.RecordBuilder;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.LongStream;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ public abstract class DelegatingOps<T>
/*     */   implements DynamicOps<T> {
/*     */   protected final DynamicOps<T> delegate;
/*     */   
/*     */   protected DelegatingOps(DynamicOps<T> debug1) {
/*  22 */     this.delegate = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public T empty() {
/*  27 */     return (T)this.delegate.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public <U> U convertTo(DynamicOps<U> debug1, T debug2) {
/*  32 */     return (U)this.delegate.convertTo(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Number> getNumberValue(T debug1) {
/*  37 */     return this.delegate.getNumberValue(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public T createNumeric(Number debug1) {
/*  42 */     return (T)this.delegate.createNumeric(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public T createByte(byte debug1) {
/*  47 */     return (T)this.delegate.createByte(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public T createShort(short debug1) {
/*  52 */     return (T)this.delegate.createShort(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public T createInt(int debug1) {
/*  57 */     return (T)this.delegate.createInt(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public T createLong(long debug1) {
/*  62 */     return (T)this.delegate.createLong(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public T createFloat(float debug1) {
/*  67 */     return (T)this.delegate.createFloat(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public T createDouble(double debug1) {
/*  72 */     return (T)this.delegate.createDouble(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Boolean> getBooleanValue(T debug1) {
/*  77 */     return this.delegate.getBooleanValue(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public T createBoolean(boolean debug1) {
/*  82 */     return (T)this.delegate.createBoolean(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<String> getStringValue(T debug1) {
/*  87 */     return this.delegate.getStringValue(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public T createString(String debug1) {
/*  92 */     return (T)this.delegate.createString(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<T> mergeToList(T debug1, T debug2) {
/*  97 */     return this.delegate.mergeToList(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<T> mergeToList(T debug1, List<T> debug2) {
/* 102 */     return this.delegate.mergeToList(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<T> mergeToMap(T debug1, T debug2, T debug3) {
/* 107 */     return this.delegate.mergeToMap(debug1, debug2, debug3);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<T> mergeToMap(T debug1, MapLike<T> debug2) {
/* 112 */     return this.delegate.mergeToMap(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Stream<Pair<T, T>>> getMapValues(T debug1) {
/* 117 */     return this.delegate.getMapValues(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Consumer<BiConsumer<T, T>>> getMapEntries(T debug1) {
/* 122 */     return this.delegate.getMapEntries(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public T createMap(Stream<Pair<T, T>> debug1) {
/* 127 */     return (T)this.delegate.createMap(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<MapLike<T>> getMap(T debug1) {
/* 132 */     return this.delegate.getMap(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Stream<T>> getStream(T debug1) {
/* 137 */     return this.delegate.getStream(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Consumer<Consumer<T>>> getList(T debug1) {
/* 142 */     return this.delegate.getList(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public T createList(Stream<T> debug1) {
/* 147 */     return (T)this.delegate.createList(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<ByteBuffer> getByteBuffer(T debug1) {
/* 152 */     return this.delegate.getByteBuffer(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public T createByteList(ByteBuffer debug1) {
/* 157 */     return (T)this.delegate.createByteList(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<IntStream> getIntStream(T debug1) {
/* 162 */     return this.delegate.getIntStream(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public T createIntList(IntStream debug1) {
/* 167 */     return (T)this.delegate.createIntList(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<LongStream> getLongStream(T debug1) {
/* 172 */     return this.delegate.getLongStream(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public T createLongList(LongStream debug1) {
/* 177 */     return (T)this.delegate.createLongList(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public T remove(T debug1, String debug2) {
/* 182 */     return (T)this.delegate.remove(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean compressMaps() {
/* 187 */     return this.delegate.compressMaps();
/*     */   }
/*     */ 
/*     */   
/*     */   public ListBuilder<T> listBuilder() {
/* 192 */     return this.delegate.listBuilder();
/*     */   }
/*     */ 
/*     */   
/*     */   public RecordBuilder<T> mapBuilder() {
/* 197 */     return this.delegate.mapBuilder();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\resources\DelegatingOps.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */