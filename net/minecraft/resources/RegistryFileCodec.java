/*    */ package net.minecraft.resources;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.List;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ public final class RegistryFileCodec<E>
/*    */   implements Codec<Supplier<E>> {
/*    */   private final ResourceKey<? extends Registry<E>> registryKey;
/*    */   private final Codec<E> elementCodec;
/*    */   private final boolean allowInline;
/*    */   
/*    */   public static <E> RegistryFileCodec<E> create(ResourceKey<? extends Registry<E>> debug0, Codec<E> debug1) {
/* 19 */     return create(debug0, debug1, true);
/*    */   }
/*    */   
/*    */   public static <E> Codec<List<Supplier<E>>> homogeneousList(ResourceKey<? extends Registry<E>> debug0, Codec<E> debug1) {
/* 23 */     return Codec.either(
/* 24 */         create(debug0, debug1, false).listOf(), debug1
/* 25 */         .xmap(debug0 -> (), Supplier::get).listOf())
/* 26 */       .xmap(debug0 -> (List)debug0.map((), ()), Either::left);
/*    */   }
/*    */   
/*    */   private static <E> RegistryFileCodec<E> create(ResourceKey<? extends Registry<E>> debug0, Codec<E> debug1, boolean debug2) {
/* 30 */     return new RegistryFileCodec<>(debug0, debug1, debug2);
/*    */   }
/*    */   
/*    */   private RegistryFileCodec(ResourceKey<? extends Registry<E>> debug1, Codec<E> debug2, boolean debug3) {
/* 34 */     this.registryKey = debug1;
/* 35 */     this.elementCodec = debug2;
/* 36 */     this.allowInline = debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> DataResult<T> encode(Supplier<E> debug1, DynamicOps<T> debug2, T debug3) {
/* 41 */     if (debug2 instanceof RegistryWriteOps) {
/* 42 */       return ((RegistryWriteOps<T>)debug2).encode(debug1.get(), debug3, this.registryKey, this.elementCodec);
/*    */     }
/* 44 */     return this.elementCodec.encode(debug1.get(), debug2, debug3);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> DataResult<Pair<Supplier<E>, T>> decode(DynamicOps<T> debug1, T debug2) {
/* 49 */     if (debug1 instanceof RegistryReadOps) {
/* 50 */       return ((RegistryReadOps<T>)debug1).decodeElement(debug2, this.registryKey, this.elementCodec, this.allowInline);
/*    */     }
/* 52 */     return this.elementCodec.decode(debug1, debug2).map(debug0 -> debug0.mapFirst(()));
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 57 */     return "RegistryFileCodec[" + this.registryKey + " " + this.elementCodec + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\resources\RegistryFileCodec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */