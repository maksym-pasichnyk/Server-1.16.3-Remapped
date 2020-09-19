/*    */ package net.minecraft.resources;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.Lifecycle;
/*    */ import net.minecraft.core.MappedRegistry;
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ public final class RegistryDataPackCodec<E> implements Codec<MappedRegistry<E>> {
/*    */   private final Codec<MappedRegistry<E>> directCodec;
/*    */   private final ResourceKey<? extends Registry<E>> registryKey;
/*    */   private final Codec<E> elementCodec;
/*    */   
/*    */   public static <E> RegistryDataPackCodec<E> create(ResourceKey<? extends Registry<E>> debug0, Lifecycle debug1, Codec<E> debug2) {
/* 17 */     return new RegistryDataPackCodec<>(debug0, debug1, debug2);
/*    */   }
/*    */   
/*    */   private RegistryDataPackCodec(ResourceKey<? extends Registry<E>> debug1, Lifecycle debug2, Codec<E> debug3) {
/* 21 */     this.directCodec = MappedRegistry.directCodec(debug1, debug2, debug3);
/* 22 */     this.registryKey = debug1;
/* 23 */     this.elementCodec = debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> DataResult<T> encode(MappedRegistry<E> debug1, DynamicOps<T> debug2, T debug3) {
/* 28 */     return this.directCodec.encode(debug1, debug2, debug3);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> DataResult<Pair<MappedRegistry<E>, T>> decode(DynamicOps<T> debug1, T debug2) {
/* 33 */     DataResult<Pair<MappedRegistry<E>, T>> debug3 = this.directCodec.decode(debug1, debug2);
/* 34 */     if (debug1 instanceof RegistryReadOps) {
/* 35 */       return debug3.flatMap(debug2 -> ((RegistryReadOps)debug1).decodeElements((MappedRegistry)debug2.getFirst(), this.registryKey, this.elementCodec).map(()));
/*    */     }
/* 37 */     return debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 42 */     return "RegistryDataPackCodec[" + this.directCodec + " " + this.registryKey + " " + this.elementCodec + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\resources\RegistryDataPackCodec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */