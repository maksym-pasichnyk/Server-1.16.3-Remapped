/*     */ package net.minecraft.resources;
/*     */ 
/*     */ import com.google.common.base.Supplier;
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonParser;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Decoder;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.Encoder;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenCustomHashMap;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Collection;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.MappedRegistry;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.WritableRegistry;
/*     */ import net.minecraft.server.packs.resources.Resource;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class RegistryReadOps<T>
/*     */   extends DelegatingOps<T>
/*     */ {
/*  42 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   private final ResourceAccess resources;
/*     */   
/*     */   private final RegistryAccess.RegistryHolder registryHolder;
/*     */   private final Map<ResourceKey<? extends Registry<?>>, ReadCache<?>> readCache;
/*     */   private final RegistryReadOps<JsonElement> jsonOps;
/*     */   
/*     */   public static <T> RegistryReadOps<T> create(DynamicOps<T> debug0, ResourceManager debug1, RegistryAccess.RegistryHolder debug2) {
/*  51 */     return create(debug0, ResourceAccess.forResourceManager(debug1), debug2);
/*     */   }
/*     */   
/*     */   public static <T> RegistryReadOps<T> create(DynamicOps<T> debug0, ResourceAccess debug1, RegistryAccess.RegistryHolder debug2) {
/*  55 */     RegistryReadOps<T> debug3 = new RegistryReadOps<>(debug0, debug1, debug2, Maps.newIdentityHashMap());
/*     */     
/*  57 */     RegistryAccess.load(debug2, debug3);
/*  58 */     return debug3;
/*     */   }
/*     */   
/*     */   static final class ReadCache<E> {
/*  62 */     private final Map<ResourceKey<E>, DataResult<Supplier<E>>> values = Maps.newIdentityHashMap();
/*     */     
/*     */     private ReadCache() {} }
/*     */   
/*     */   private RegistryReadOps(DynamicOps<T> debug1, ResourceAccess debug2, RegistryAccess.RegistryHolder debug3, IdentityHashMap<ResourceKey<? extends Registry<?>>, ReadCache<?>> debug4) {
/*  67 */     super(debug1);
/*  68 */     this.resources = debug2;
/*  69 */     this.registryHolder = debug3;
/*  70 */     this.readCache = debug4;
/*  71 */     this.jsonOps = (debug1 == JsonOps.INSTANCE) ? (RegistryReadOps)this : new RegistryReadOps((DynamicOps<T>)JsonOps.INSTANCE, debug2, debug3, debug4);
/*     */   }
/*     */   
/*     */   protected <E> DataResult<Pair<Supplier<E>, T>> decodeElement(T debug1, ResourceKey<? extends Registry<E>> debug2, Codec<E> debug3, boolean debug4) {
/*  75 */     Optional<WritableRegistry<E>> debug5 = this.registryHolder.registry(debug2);
/*  76 */     if (!debug5.isPresent()) {
/*  77 */       return DataResult.error("Unknown registry: " + debug2);
/*     */     }
/*     */     
/*  80 */     WritableRegistry<E> debug6 = debug5.get();
/*     */     
/*  82 */     DataResult<Pair<ResourceLocation, T>> debug7 = ResourceLocation.CODEC.decode(this.delegate, debug1);
/*  83 */     if (!debug7.result().isPresent()) {
/*  84 */       if (!debug4) {
/*  85 */         return DataResult.error("Inline definitions not allowed here");
/*     */       }
/*  87 */       return debug3.decode(this, debug1).map(debug0 -> debug0.mapFirst(()));
/*     */     } 
/*     */     
/*  90 */     Pair<ResourceLocation, T> debug8 = debug7.result().get();
/*  91 */     ResourceLocation debug9 = (ResourceLocation)debug8.getFirst();
/*  92 */     return readAndRegisterElement(debug2, debug6, debug3, debug9).map(debug1 -> Pair.of(debug1, debug0.getSecond()));
/*     */   }
/*     */ 
/*     */   
/*     */   public <E> DataResult<MappedRegistry<E>> decodeElements(MappedRegistry<E> debug1, ResourceKey<? extends Registry<E>> debug2, Codec<E> debug3) {
/*  97 */     Collection<ResourceLocation> debug4 = this.resources.listResources(debug2);
/*     */     
/*  99 */     DataResult<MappedRegistry<E>> debug5 = DataResult.success(debug1, Lifecycle.stable());
/*     */     
/* 101 */     String debug6 = debug2.location().getPath() + "/";
/* 102 */     for (ResourceLocation debug8 : debug4) {
/* 103 */       String debug9 = debug8.getPath();
/* 104 */       if (!debug9.endsWith(".json")) {
/* 105 */         LOGGER.warn("Skipping resource {} since it is not a json file", debug8);
/*     */         continue;
/*     */       } 
/* 108 */       if (!debug9.startsWith(debug6)) {
/* 109 */         LOGGER.warn("Skipping resource {} since it does not have a registry name prefix", debug8);
/*     */         continue;
/*     */       } 
/* 112 */       String debug10 = debug9.substring(debug6.length(), debug9.length() - ".json".length());
/* 113 */       ResourceLocation debug11 = new ResourceLocation(debug8.getNamespace(), debug10);
/*     */       
/* 115 */       debug5 = debug5.flatMap(debug4 -> readAndRegisterElement(debug1, (WritableRegistry<E>)debug4, debug2, debug3).map(()));
/*     */     } 
/*     */     
/* 118 */     return debug5.setPartial(debug1);
/*     */   }
/*     */   private <E> DataResult<Supplier<E>> readAndRegisterElement(ResourceKey<? extends Registry<E>> debug1, WritableRegistry<E> debug2, Codec<E> debug3, ResourceLocation debug4) {
/*     */     DataResult<Supplier<E>> debug11;
/* 122 */     ResourceKey<E> debug5 = ResourceKey.create(debug1, debug4);
/*     */     
/* 124 */     ReadCache<E> debug6 = readCache(debug1);
/* 125 */     DataResult<Supplier<E>> debug7 = (DataResult<Supplier<E>>)debug6.values.get(debug5);
/* 126 */     if (debug7 != null)
/*     */     {
/* 128 */       return debug7;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 134 */     Supplier supplier = Suppliers.memoize(() -> {
/*     */           E debug2 = (E)debug0.get(debug1);
/*     */           if (debug2 == null) {
/*     */             throw new RuntimeException("Error during recursive registry parsing, element resolved too early: " + debug1);
/*     */           }
/*     */           return (Supplier)debug2;
/*     */         });
/* 141 */     debug6.values.put(debug5, DataResult.success(supplier));
/*     */ 
/*     */     
/* 144 */     DataResult<Pair<E, OptionalInt>> debug9 = this.resources.parseElement(this.jsonOps, debug1, debug5, (Decoder<E>)debug3);
/*     */     
/* 146 */     Optional<Pair<E, OptionalInt>> debug10 = debug9.result();
/*     */     
/* 148 */     if (debug10.isPresent()) {
/*     */       
/* 150 */       Pair<E, OptionalInt> pair = debug10.get();
/* 151 */       debug2.registerOrOverride((OptionalInt)pair.getSecond(), debug5, pair.getFirst(), debug9.lifecycle());
/*     */     } 
/*     */ 
/*     */     
/* 155 */     if (!debug10.isPresent() && debug2.get(debug5) != null) {
/*     */       
/* 157 */       debug11 = DataResult.success(() -> debug0.get(debug1), Lifecycle.stable());
/*     */     } else {
/*     */       
/* 160 */       debug11 = debug9.map(debug2 -> ());
/*     */     } 
/*     */ 
/*     */     
/* 164 */     debug6.values.put(debug5, debug11);
/*     */     
/* 166 */     return debug11;
/*     */   }
/*     */ 
/*     */   
/*     */   private <E> ReadCache<E> readCache(ResourceKey<? extends Registry<E>> debug1) {
/* 171 */     return (ReadCache<E>)this.readCache.computeIfAbsent(debug1, debug0 -> new ReadCache());
/*     */   }
/*     */ 
/*     */   
/*     */   protected <E> DataResult<Registry<E>> registry(ResourceKey<? extends Registry<E>> debug1) {
/* 176 */     return this.registryHolder.registry(debug1)
/* 177 */       .map(debug0 -> DataResult.success(debug0, debug0.elementsLifecycle()))
/* 178 */       .orElseGet(() -> DataResult.error("Unknown registry: " + debug0));
/*     */   }
/*     */   
/*     */   public static interface ResourceAccess {
/*     */     Collection<ResourceLocation> listResources(ResourceKey<? extends Registry<?>> param1ResourceKey);
/*     */     
/*     */     <E> DataResult<Pair<E, OptionalInt>> parseElement(DynamicOps<JsonElement> param1DynamicOps, ResourceKey<? extends Registry<E>> param1ResourceKey, ResourceKey<E> param1ResourceKey1, Decoder<E> param1Decoder);
/*     */     
/*     */     static ResourceAccess forResourceManager(final ResourceManager manager) {
/* 187 */       return new ResourceAccess()
/*     */         {
/*     */           public Collection<ResourceLocation> listResources(ResourceKey<? extends Registry<?>> debug1) {
/* 190 */             return manager.listResources(debug1.location().getPath(), debug0 -> debug0.endsWith(".json"));
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           public <E> DataResult<Pair<E, OptionalInt>> parseElement(DynamicOps<JsonElement> debug1, ResourceKey<? extends Registry<E>> debug2, ResourceKey<E> debug3, Decoder<E> debug4) {
/* 196 */             ResourceLocation debug5 = debug3.location();
/* 197 */             ResourceLocation debug6 = new ResourceLocation(debug5.getNamespace(), debug2.location().getPath() + "/" + debug5.getPath() + ".json");
/*     */             
/* 199 */             try(Resource debug7 = manager.getResource(debug6); 
/* 200 */                 Reader debug9 = new InputStreamReader(debug7.getInputStream(), StandardCharsets.UTF_8)) {
/*     */               
/* 202 */               JsonParser debug11 = new JsonParser();
/* 203 */               JsonElement debug12 = debug11.parse(debug9);
/* 204 */               return debug4.parse(debug1, debug12).map(debug0 -> Pair.of(debug0, OptionalInt.empty()));
/* 205 */             } catch (IOException|com.google.gson.JsonIOException|com.google.gson.JsonSyntaxException debug7) {
/* 206 */               return DataResult.error("Failed to parse " + debug6 + " file: " + debug7.getMessage());
/*     */             } 
/*     */           }
/*     */ 
/*     */           
/*     */           public String toString() {
/* 212 */             return "ResourceAccess[" + manager + "]";
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     public static final class MemoryMap implements ResourceAccess {
/* 218 */       private final Map<ResourceKey<?>, JsonElement> data = Maps.newIdentityHashMap();
/* 219 */       private final Object2IntMap<ResourceKey<?>> ids = (Object2IntMap<ResourceKey<?>>)new Object2IntOpenCustomHashMap(Util.identityStrategy());
/* 220 */       private final Map<ResourceKey<?>, Lifecycle> lifecycles = Maps.newIdentityHashMap();
/*     */       
/*     */       public <E> void add(RegistryAccess.RegistryHolder debug1, ResourceKey<E> debug2, Encoder<E> debug3, int debug4, E debug5, Lifecycle debug6) {
/* 223 */         DataResult<JsonElement> debug7 = debug3.encodeStart(RegistryWriteOps.create((DynamicOps<?>)JsonOps.INSTANCE, (RegistryAccess)debug1), debug5);
/* 224 */         Optional<DataResult.PartialResult<JsonElement>> debug8 = debug7.error();
/* 225 */         if (debug8.isPresent()) {
/* 226 */           RegistryReadOps.LOGGER.error("Error adding element: {}", ((DataResult.PartialResult)debug8.get()).message());
/*     */           return;
/*     */         } 
/* 229 */         this.data.put(debug2, debug7.result().get());
/* 230 */         this.ids.put(debug2, debug4);
/* 231 */         this.lifecycles.put(debug2, debug6);
/*     */       }
/*     */ 
/*     */       
/*     */       public Collection<ResourceLocation> listResources(ResourceKey<? extends Registry<?>> debug1) {
/* 236 */         return (Collection<ResourceLocation>)this.data.keySet().stream().filter(debug1 -> debug1.isFor(debug0)).map(debug1 -> new ResourceLocation(debug1.location().getNamespace(), debug0.location().getPath() + "/" + debug1.location().getPath() + ".json")).collect(Collectors.toList());
/*     */       }
/*     */ 
/*     */       
/*     */       public <E> DataResult<Pair<E, OptionalInt>> parseElement(DynamicOps<JsonElement> debug1, ResourceKey<? extends Registry<E>> debug2, ResourceKey<E> debug3, Decoder<E> debug4) {
/* 241 */         JsonElement debug5 = this.data.get(debug3);
/* 242 */         if (debug5 == null) {
/* 243 */           return DataResult.error("Unknown element: " + debug3);
/*     */         }
/* 245 */         return debug4.parse(debug1, debug5).setLifecycle(this.lifecycles.get(debug3)).map(debug2 -> Pair.of(debug2, OptionalInt.of(this.ids.getInt(debug1))));
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\resources\RegistryReadOps.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */