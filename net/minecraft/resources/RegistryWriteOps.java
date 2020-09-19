/*    */ package net.minecraft.resources;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.core.WritableRegistry;
/*    */ 
/*    */ public class RegistryWriteOps<T>
/*    */   extends DelegatingOps<T> {
/*    */   private final RegistryAccess registryHolder;
/*    */   
/*    */   public static <T> RegistryWriteOps<T> create(DynamicOps<T> debug0, RegistryAccess debug1) {
/* 16 */     return new RegistryWriteOps<>(debug0, debug1);
/*    */   }
/*    */   
/*    */   private RegistryWriteOps(DynamicOps<T> debug1, RegistryAccess debug2) {
/* 20 */     super(debug1);
/* 21 */     this.registryHolder = debug2;
/*    */   }
/*    */   
/*    */   protected <E> DataResult<T> encode(E debug1, T debug2, ResourceKey<? extends Registry<E>> debug3, Codec<E> debug4) {
/* 25 */     Optional<WritableRegistry<E>> debug5 = this.registryHolder.registry(debug3);
/* 26 */     if (debug5.isPresent()) {
/* 27 */       WritableRegistry<E> debug6 = debug5.get();
/* 28 */       Optional<ResourceKey<E>> debug7 = debug6.getResourceKey(debug1);
/* 29 */       if (debug7.isPresent()) {
/* 30 */         ResourceKey<E> debug8 = debug7.get();
/* 31 */         return ResourceLocation.CODEC.encode(debug8.location(), this.delegate, debug2);
/*    */       } 
/*    */     } 
/*    */     
/* 35 */     return debug4.encode(debug1, this, debug2);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\resources\RegistryWriteOps.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */