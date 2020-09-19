/*     */ package net.minecraft.tags;
/*     */ 
/*     */ import com.google.common.collect.BiMap;
/*     */ import com.google.common.collect.ImmutableBiMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.DefaultedRegistry;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ 
/*     */ 
/*     */ public interface TagCollection<T>
/*     */ {
/*     */   Map<ResourceLocation, Tag<T>> getAllTags();
/*     */   
/*     */   @Nullable
/*     */   default Tag<T> getTag(ResourceLocation debug1) {
/*  23 */     return getAllTags().get(debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Tag<T> getTagOrEmpty(ResourceLocation paramResourceLocation);
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   ResourceLocation getId(Tag<T> paramTag);
/*     */ 
/*     */   
/*     */   default ResourceLocation getIdOrThrow(Tag<T> debug1) {
/*  37 */     ResourceLocation debug2 = getId(debug1);
/*  38 */     if (debug2 == null) {
/*  39 */       throw new IllegalStateException("Unrecognized tag");
/*     */     }
/*  41 */     return debug2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default Collection<ResourceLocation> getAvailableTags() {
/*  49 */     return getAllTags().keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default void serializeToNetwork(FriendlyByteBuf debug1, DefaultedRegistry<T> debug2) {
/*  63 */     Map<ResourceLocation, Tag<T>> debug3 = getAllTags();
/*  64 */     debug1.writeVarInt(debug3.size());
/*  65 */     for (Map.Entry<ResourceLocation, Tag<T>> debug5 : debug3.entrySet()) {
/*  66 */       debug1.writeResourceLocation(debug5.getKey());
/*  67 */       debug1.writeVarInt(((Tag)debug5.getValue()).getValues().size());
/*  68 */       for (T debug7 : ((Tag)debug5.getValue()).getValues()) {
/*  69 */         debug1.writeVarInt(debug2.getId(debug7));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   static <T> TagCollection<T> loadFromNetwork(FriendlyByteBuf debug0, Registry<T> debug1) {
/*  75 */     Map<ResourceLocation, Tag<T>> debug2 = Maps.newHashMap();
/*  76 */     int debug3 = debug0.readVarInt();
/*  77 */     for (int debug4 = 0; debug4 < debug3; debug4++) {
/*  78 */       ResourceLocation debug5 = debug0.readResourceLocation();
/*  79 */       int debug6 = debug0.readVarInt();
/*  80 */       ImmutableSet.Builder<T> debug7 = ImmutableSet.builder();
/*  81 */       for (int debug8 = 0; debug8 < debug6; debug8++) {
/*  82 */         debug7.add(debug1.byId(debug0.readVarInt()));
/*     */       }
/*  84 */       debug2.put(debug5, Tag.fromSet((Set<T>)debug7.build()));
/*     */     } 
/*     */     
/*  87 */     return of(debug2);
/*     */   }
/*     */   
/*     */   static <T> TagCollection<T> empty() {
/*  91 */     return of((Map<ResourceLocation, Tag<T>>)ImmutableBiMap.of());
/*     */   }
/*     */   
/*     */   static <T> TagCollection<T> of(Map<ResourceLocation, Tag<T>> debug0) {
/*  95 */     final ImmutableBiMap tags = ImmutableBiMap.copyOf(debug0);
/*  96 */     return new TagCollection<T>() {
/*  97 */         private final Tag<T> empty = SetTag.empty();
/*     */ 
/*     */         
/*     */         public Tag<T> getTagOrEmpty(ResourceLocation debug1) {
/* 101 */           return (Tag<T>)tags.getOrDefault(debug1, this.empty);
/*     */         }
/*     */ 
/*     */         
/*     */         @Nullable
/*     */         public ResourceLocation getId(Tag<T> debug1) {
/* 107 */           if (debug1 instanceof Tag.Named) {
/* 108 */             return ((Tag.Named)debug1).getName();
/*     */           }
/* 110 */           return (ResourceLocation)tags.inverse().get(debug1);
/*     */         }
/*     */ 
/*     */         
/*     */         public Map<ResourceLocation, Tag<T>> getAllTags() {
/* 115 */           return (Map<ResourceLocation, Tag<T>>)tags;
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\tags\TagCollection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */