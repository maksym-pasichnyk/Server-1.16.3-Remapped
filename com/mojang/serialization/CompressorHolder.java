/*    */ package com.mojang.serialization;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ public abstract class CompressorHolder
/*    */   implements Compressable
/*    */ {
/* 10 */   private final Map<DynamicOps<?>, KeyCompressor<?>> compressors = (Map<DynamicOps<?>, KeyCompressor<?>>)new Object2ObjectArrayMap();
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> KeyCompressor<T> compressor(DynamicOps<T> ops) {
/* 15 */     return (KeyCompressor<T>)this.compressors.computeIfAbsent(ops, k -> new KeyCompressor(ops, keys(ops)));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\serialization\CompressorHolder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */