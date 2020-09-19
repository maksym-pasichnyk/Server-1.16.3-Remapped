/*    */ package net.minecraft.core;
/*    */ 
/*    */ import com.mojang.serialization.Lifecycle;
/*    */ import java.util.Optional;
/*    */ import java.util.Random;
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ 
/*    */ public class DefaultedRegistry<T>
/*    */   extends MappedRegistry<T>
/*    */ {
/*    */   private final ResourceLocation defaultKey;
/*    */   private T defaultValue;
/*    */   
/*    */   public DefaultedRegistry(String debug1, ResourceKey<? extends Registry<T>> debug2, Lifecycle debug3) {
/* 18 */     super(debug2, debug3);
/* 19 */     this.defaultKey = new ResourceLocation(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public <V extends T> V registerMapping(int debug1, ResourceKey<T> debug2, V debug3, Lifecycle debug4) {
/* 24 */     if (this.defaultKey.equals(debug2.location())) {
/* 25 */       this.defaultValue = (T)debug3;
/*    */     }
/*    */     
/* 28 */     return super.registerMapping(debug1, debug2, debug3, debug4);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getId(@Nullable T debug1) {
/* 33 */     int debug2 = super.getId(debug1);
/* 34 */     return (debug2 == -1) ? super.getId(this.defaultValue) : debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public ResourceLocation getKey(T debug1) {
/* 40 */     ResourceLocation debug2 = super.getKey(debug1);
/* 41 */     return (debug2 == null) ? this.defaultKey : debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public T get(@Nullable ResourceLocation debug1) {
/* 47 */     T debug2 = super.get(debug1);
/* 48 */     return (debug2 == null) ? this.defaultValue : debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<T> getOptional(@Nullable ResourceLocation debug1) {
/* 53 */     return Optional.ofNullable(super.get(debug1));
/*    */   }
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public T byId(int debug1) {
/* 59 */     T debug2 = super.byId(debug1);
/* 60 */     return (debug2 == null) ? this.defaultValue : debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public T getRandom(Random debug1) {
/* 66 */     T debug2 = super.getRandom(debug1);
/* 67 */     return (debug2 == null) ? this.defaultValue : debug2;
/*    */   }
/*    */   
/*    */   public ResourceLocation getDefaultKey() {
/* 71 */     return this.defaultKey;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\core\DefaultedRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */