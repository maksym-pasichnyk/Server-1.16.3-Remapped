/*    */ package net.minecraft.resources;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Collections;
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ResourceKey<T>
/*    */ {
/* 16 */   private static final Map<String, ResourceKey<?>> VALUES = Collections.synchronizedMap(Maps.newIdentityHashMap());
/*    */   
/*    */   private final ResourceLocation registryName;
/*    */   private final ResourceLocation location;
/*    */   
/*    */   public static <T> ResourceKey<T> create(ResourceKey<? extends Registry<T>> debug0, ResourceLocation debug1) {
/* 22 */     return create(debug0.location, debug1);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T> ResourceKey<Registry<T>> createRegistryKey(ResourceLocation debug0) {
/* 29 */     return create(Registry.ROOT_REGISTRY_NAME, debug0);
/*    */   }
/*    */ 
/*    */   
/*    */   private static <T> ResourceKey<T> create(ResourceLocation debug0, ResourceLocation debug1) {
/* 34 */     String debug2 = (debug0 + ":" + debug1).intern();
/* 35 */     return (ResourceKey<T>)VALUES.computeIfAbsent(debug2, debug2 -> new ResourceKey(debug0, debug1));
/*    */   }
/*    */   
/*    */   private ResourceKey(ResourceLocation debug1, ResourceLocation debug2) {
/* 39 */     this.registryName = debug1;
/* 40 */     this.location = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 45 */     return "ResourceKey[" + this.registryName + " / " + this.location + ']';
/*    */   }
/*    */   
/*    */   public boolean isFor(ResourceKey<? extends Registry<?>> debug1) {
/* 49 */     return this.registryName.equals(debug1.location());
/*    */   }
/*    */   
/*    */   public ResourceLocation location() {
/* 53 */     return this.location;
/*    */   }
/*    */   
/*    */   public static <T> Function<ResourceLocation, ResourceKey<T>> elementKey(ResourceKey<? extends Registry<T>> debug0) {
/* 57 */     return debug1 -> create(debug0, debug1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\resources\ResourceKey.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */