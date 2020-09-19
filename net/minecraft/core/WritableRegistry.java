/*    */ package net.minecraft.core;
/*    */ 
/*    */ import com.mojang.serialization.Lifecycle;
/*    */ import java.util.OptionalInt;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public abstract class WritableRegistry<T>
/*    */   extends Registry<T> {
/*    */   public WritableRegistry(ResourceKey<? extends Registry<T>> debug1, Lifecycle debug2) {
/* 10 */     super(debug1, debug2);
/*    */   }
/*    */   
/*    */   public abstract <V extends T> V registerMapping(int paramInt, ResourceKey<T> paramResourceKey, V paramV, Lifecycle paramLifecycle);
/*    */   
/*    */   public abstract <V extends T> V register(ResourceKey<T> paramResourceKey, V paramV, Lifecycle paramLifecycle);
/*    */   
/*    */   public abstract <V extends T> V registerOrOverride(OptionalInt paramOptionalInt, ResourceKey<T> paramResourceKey, V paramV, Lifecycle paramLifecycle);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\core\WritableRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */