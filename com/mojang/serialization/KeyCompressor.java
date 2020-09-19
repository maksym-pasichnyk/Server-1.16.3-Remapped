/*    */ package com.mojang.serialization;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class KeyCompressor<T>
/*    */ {
/* 13 */   private final Int2ObjectMap<T> decompress = (Int2ObjectMap<T>)new Int2ObjectArrayMap();
/* 14 */   private final Object2IntMap<T> compress = (Object2IntMap<T>)new Object2IntArrayMap();
/* 15 */   private final Object2IntMap<String> compressString = (Object2IntMap<String>)new Object2IntArrayMap();
/*    */   private final int size;
/*    */   private final DynamicOps<T> ops;
/*    */   
/*    */   public KeyCompressor(DynamicOps<T> ops, Stream<T> keyStream) {
/* 20 */     this.ops = ops;
/*    */     
/* 22 */     this.compressString.defaultReturnValue(-1);
/*    */     
/* 24 */     keyStream.forEach(key -> {
/*    */           if (this.compress.containsKey(key)) {
/*    */             return;
/*    */           }
/*    */           
/*    */           int next = this.compress.size();
/*    */           
/*    */           this.compress.put(key, next);
/*    */           
/*    */           ops.getStringValue(key).result().ifPresent(());
/*    */           this.decompress.put(next, key);
/*    */         });
/* 36 */     this.size = this.compress.size();
/*    */   }
/*    */   
/*    */   public T decompress(int key) {
/* 40 */     return (T)this.decompress.get(key);
/*    */   }
/*    */   
/*    */   public int compress(String key) {
/* 44 */     int id = this.compressString.getInt(key);
/* 45 */     return (id == -1) ? compress(this.ops.createString(key)) : id;
/*    */   }
/*    */   
/*    */   public int compress(T key) {
/* 49 */     return ((Integer)this.compress.get(key)).intValue();
/*    */   }
/*    */   
/*    */   public int size() {
/* 53 */     return this.size;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\serialization\KeyCompressor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */