/*     */ package net.minecraft.core;
/*     */ import com.google.common.collect.BiMap;
/*     */ import com.google.common.collect.HashBiMap;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenCustomHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.resources.RegistryDataPackCodec;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import org.apache.commons.lang3.Validate;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class MappedRegistry<T> extends WritableRegistry<T> {
/*  38 */   protected static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   private final ObjectList<T> byId;
/*     */   
/*     */   private final Object2IntMap<T> toId;
/*     */   
/*     */   private final BiMap<ResourceLocation, T> storage;
/*     */   private final BiMap<ResourceKey<T>, T> keyStorage;
/*     */   private final Map<T, Lifecycle> lifecycles;
/*     */   private Lifecycle elementsLifecycle;
/*     */   protected Object[] randomCache;
/*     */   private int nextId;
/*     */   
/*     */   public MappedRegistry(ResourceKey<? extends Registry<T>> debug1, Lifecycle debug2) {
/*  52 */     super(debug1, debug2);
/*  53 */     this.byId = (ObjectList<T>)new ObjectArrayList(256);
/*  54 */     this.toId = (Object2IntMap<T>)new Object2IntOpenCustomHashMap(Util.identityStrategy());
/*  55 */     this.toId.defaultReturnValue(-1);
/*  56 */     this.storage = (BiMap<ResourceLocation, T>)HashBiMap.create();
/*  57 */     this.keyStorage = (BiMap<ResourceKey<T>, T>)HashBiMap.create();
/*  58 */     this.lifecycles = Maps.newIdentityHashMap();
/*  59 */     this.elementsLifecycle = debug2;
/*     */   }
/*     */   
/*     */   public static <T> MapCodec<RegistryEntry<T>> withNameAndId(ResourceKey<? extends Registry<T>> debug0, MapCodec<T> debug1) {
/*  63 */     return RecordCodecBuilder.mapCodec(debug2 -> debug2.group((App)ResourceLocation.CODEC.xmap(ResourceKey.elementKey(debug0), ResourceKey::location).fieldOf("name").forGetter(()), (App)Codec.INT.fieldOf("id").forGetter(()), (App)debug1.forGetter(())).apply((Applicative)debug2, RegistryEntry::new));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <V extends T> V registerMapping(int debug1, ResourceKey<T> debug2, V debug3, Lifecycle debug4) {
/*  72 */     return registerMapping(debug1, debug2, debug3, debug4, true);
/*     */   }
/*     */   
/*     */   private <V extends T> V registerMapping(int debug1, ResourceKey<T> debug2, V debug3, Lifecycle debug4, boolean debug5) {
/*  76 */     Validate.notNull(debug2);
/*  77 */     Validate.notNull(debug3);
/*     */     
/*  79 */     this.byId.size(Math.max(this.byId.size(), debug1 + 1));
/*  80 */     this.byId.set(debug1, debug3);
/*  81 */     this.toId.put(debug3, debug1);
/*  82 */     this.randomCache = null;
/*     */     
/*  84 */     if (debug5 && this.keyStorage.containsKey(debug2)) {
/*  85 */       LOGGER.debug("Adding duplicate key '{}' to registry", debug2);
/*     */     }
/*  87 */     if (this.storage.containsValue(debug3)) {
/*  88 */       LOGGER.error("Adding duplicate value '{}' to registry", debug3);
/*     */     }
/*  90 */     this.storage.put(debug2.location(), debug3);
/*  91 */     this.keyStorage.put(debug2, debug3);
/*  92 */     this.lifecycles.put((T)debug3, debug4);
/*  93 */     this.elementsLifecycle = this.elementsLifecycle.add(debug4);
/*  94 */     if (this.nextId <= debug1) {
/*  95 */       this.nextId = debug1 + 1;
/*     */     }
/*     */     
/*  98 */     return debug3;
/*     */   }
/*     */ 
/*     */   
/*     */   public <V extends T> V register(ResourceKey<T> debug1, V debug2, Lifecycle debug3) {
/* 103 */     return registerMapping(this.nextId, debug1, debug2, debug3);
/*     */   }
/*     */   
/*     */   public <V extends T> V registerOrOverride(OptionalInt debug1, ResourceKey<T> debug2, V debug3, Lifecycle debug4) {
/*     */     int debug6;
/* 108 */     Validate.notNull(debug2);
/* 109 */     Validate.notNull(debug3);
/*     */     
/* 111 */     T debug5 = (T)this.keyStorage.get(debug2);
/*     */     
/* 113 */     if (debug5 == null) {
/* 114 */       debug6 = debug1.isPresent() ? debug1.getAsInt() : this.nextId;
/*     */     } else {
/* 116 */       debug6 = this.toId.getInt(debug5);
/* 117 */       if (debug1.isPresent() && debug1.getAsInt() != debug6) {
/* 118 */         throw new IllegalStateException("ID mismatch");
/*     */       }
/* 120 */       this.toId.removeInt(debug5);
/* 121 */       this.lifecycles.remove(debug5);
/*     */     } 
/*     */     
/* 124 */     return registerMapping(debug6, debug2, debug3, debug4, false);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ResourceLocation getKey(T debug1) {
/* 130 */     return (ResourceLocation)this.storage.inverse().get(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<ResourceKey<T>> getResourceKey(T debug1) {
/* 135 */     return Optional.ofNullable(this.keyStorage.inverse().get(debug1));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getId(@Nullable T debug1) {
/* 140 */     return this.toId.getInt(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public T get(@Nullable ResourceKey<T> debug1) {
/* 146 */     return (T)this.keyStorage.get(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public T byId(int debug1) {
/* 152 */     if (debug1 < 0 || debug1 >= this.byId.size()) {
/* 153 */       return null;
/*     */     }
/* 155 */     return (T)this.byId.get(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public Lifecycle lifecycle(T debug1) {
/* 160 */     return this.lifecycles.get(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public Lifecycle elementsLifecycle() {
/* 165 */     return this.elementsLifecycle;
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<T> iterator() {
/* 170 */     return (Iterator<T>)Iterators.filter((Iterator)this.byId.iterator(), Objects::nonNull);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public T get(@Nullable ResourceLocation debug1) {
/* 176 */     return (T)this.storage.get(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<ResourceLocation> keySet() {
/* 181 */     return Collections.unmodifiableSet(this.storage.keySet());
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<ResourceKey<T>, T>> entrySet() {
/* 186 */     return Collections.<ResourceKey<T>, T>unmodifiableMap((Map<? extends ResourceKey<T>, ? extends T>)this.keyStorage).entrySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public T getRandom(Random debug1) {
/* 198 */     if (this.randomCache == null) {
/* 199 */       Collection<?> debug2 = this.storage.values();
/* 200 */       if (debug2.isEmpty()) {
/* 201 */         return null;
/*     */       }
/* 203 */       this.randomCache = debug2.toArray(new Object[debug2.size()]);
/*     */     } 
/* 205 */     return (T)Util.getRandom(this.randomCache, debug1);
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
/*     */   public static <T> Codec<MappedRegistry<T>> networkCodec(ResourceKey<? extends Registry<T>> debug0, Lifecycle debug1, Codec<T> debug2) {
/* 219 */     return withNameAndId(debug0, debug2.fieldOf("element")).codec().listOf().xmap(debug2 -> {
/*     */           MappedRegistry<T> debug3 = new MappedRegistry<>(debug0, debug1);
/*     */           for (RegistryEntry<T> debug5 : (Iterable<RegistryEntry<T>>)debug2) {
/*     */             debug3.registerMapping(debug5.id, debug5.key, debug5.value, debug1);
/*     */           }
/*     */           return debug3;
/*     */         }debug0 -> {
/*     */           ImmutableList.Builder<RegistryEntry<T>> debug1 = ImmutableList.builder();
/*     */           for (T debug3 : debug0) {
/*     */             debug1.add(new RegistryEntry<>(debug0.getResourceKey(debug3).get(), debug0.getId(debug3), debug3));
/*     */           }
/*     */           return (List)debug1.build();
/*     */         });
/*     */   }
/*     */   
/*     */   public static <T> Codec<MappedRegistry<T>> dataPackCodec(ResourceKey<? extends Registry<T>> debug0, Lifecycle debug1, Codec<T> debug2) {
/* 235 */     return (Codec<MappedRegistry<T>>)RegistryDataPackCodec.create(debug0, debug1, debug2);
/*     */   }
/*     */   
/*     */   public static <T> Codec<MappedRegistry<T>> directCodec(ResourceKey<? extends Registry<T>> debug0, Lifecycle debug1, Codec<T> debug2) {
/* 239 */     return Codec.unboundedMap(ResourceLocation.CODEC
/* 240 */         .xmap(ResourceKey.elementKey(debug0), ResourceKey::location), debug2)
/*     */       
/* 242 */       .xmap(debug2 -> {
/*     */           MappedRegistry<T> debug3 = new MappedRegistry<>(debug0, debug1);
/*     */           debug2.forEach(());
/*     */           return debug3;
/*     */         }debug0 -> ImmutableMap.copyOf((Map)debug0.keyStorage));
/*     */   }
/*     */   
/*     */   public static class RegistryEntry<T> {
/*     */     public final ResourceKey<T> key;
/*     */     public final int id;
/*     */     public final T value;
/*     */     
/*     */     public RegistryEntry(ResourceKey<T> debug1, int debug2, T debug3) {
/* 255 */       this.key = debug1;
/* 256 */       this.id = debug2;
/* 257 */       this.value = debug3;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\core\MappedRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */