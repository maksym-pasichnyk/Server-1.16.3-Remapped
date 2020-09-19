/*    */ package net.minecraft.resources;
/*    */ 
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.MapLike;
/*    */ import com.mojang.serialization.RecordBuilder;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ public final class RegistryLookupCodec<E>
/*    */   extends MapCodec<Registry<E>> {
/*    */   private final ResourceKey<? extends Registry<E>> registryKey;
/*    */   
/*    */   public static <E> RegistryLookupCodec<E> create(ResourceKey<? extends Registry<E>> debug0) {
/* 16 */     return new RegistryLookupCodec<>(debug0);
/*    */   }
/*    */   
/*    */   private RegistryLookupCodec(ResourceKey<? extends Registry<E>> debug1) {
/* 20 */     this.registryKey = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> RecordBuilder<T> encode(Registry<E> debug1, DynamicOps<T> debug2, RecordBuilder<T> debug3) {
/* 25 */     return debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> DataResult<Registry<E>> decode(DynamicOps<T> debug1, MapLike<T> debug2) {
/* 30 */     if (debug1 instanceof RegistryReadOps) {
/* 31 */       return ((RegistryReadOps)debug1).registry(this.registryKey);
/*    */     }
/* 33 */     return DataResult.error("Not a registry ops");
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 38 */     return "RegistryLookupCodec[" + this.registryKey + "]";
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> Stream<T> keys(DynamicOps<T> debug1) {
/* 43 */     return Stream.empty();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\resources\RegistryLookupCodec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */