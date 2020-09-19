/*     */ package net.minecraft.advancements.critereon;
/*     */ 
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonNull;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import java.util.Optional;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.CampfireBlock;
/*     */ import net.minecraft.world.level.levelgen.feature.StructureFeature;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class LocationPredicate {
/*  24 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*  26 */   public static final LocationPredicate ANY = new LocationPredicate(MinMaxBounds.Floats.ANY, MinMaxBounds.Floats.ANY, MinMaxBounds.Floats.ANY, null, null, null, null, LightPredicate.ANY, BlockPredicate.ANY, FluidPredicate.ANY);
/*     */   
/*     */   private final MinMaxBounds.Floats x;
/*     */   private final MinMaxBounds.Floats y;
/*     */   private final MinMaxBounds.Floats z;
/*     */   @Nullable
/*     */   private final ResourceKey<Biome> biome;
/*     */   @Nullable
/*     */   private final StructureFeature<?> feature;
/*     */   @Nullable
/*     */   private final ResourceKey<Level> dimension;
/*     */   @Nullable
/*     */   private final Boolean smokey;
/*     */   private final LightPredicate light;
/*     */   private final BlockPredicate block;
/*     */   private final FluidPredicate fluid;
/*     */   
/*     */   public LocationPredicate(MinMaxBounds.Floats debug1, MinMaxBounds.Floats debug2, MinMaxBounds.Floats debug3, @Nullable ResourceKey<Biome> debug4, @Nullable StructureFeature<?> debug5, @Nullable ResourceKey<Level> debug6, @Nullable Boolean debug7, LightPredicate debug8, BlockPredicate debug9, FluidPredicate debug10) {
/*  44 */     this.x = debug1;
/*  45 */     this.y = debug2;
/*  46 */     this.z = debug3;
/*  47 */     this.biome = debug4;
/*  48 */     this.feature = debug5;
/*  49 */     this.dimension = debug6;
/*  50 */     this.smokey = debug7;
/*  51 */     this.light = debug8;
/*  52 */     this.block = debug9;
/*  53 */     this.fluid = debug10;
/*     */   }
/*     */   
/*     */   public static LocationPredicate inBiome(ResourceKey<Biome> debug0) {
/*  57 */     return new LocationPredicate(MinMaxBounds.Floats.ANY, MinMaxBounds.Floats.ANY, MinMaxBounds.Floats.ANY, debug0, null, null, null, LightPredicate.ANY, BlockPredicate.ANY, FluidPredicate.ANY);
/*     */   }
/*     */   
/*     */   public static LocationPredicate inDimension(ResourceKey<Level> debug0) {
/*  61 */     return new LocationPredicate(MinMaxBounds.Floats.ANY, MinMaxBounds.Floats.ANY, MinMaxBounds.Floats.ANY, null, null, debug0, null, LightPredicate.ANY, BlockPredicate.ANY, FluidPredicate.ANY);
/*     */   }
/*     */   
/*     */   public static LocationPredicate inFeature(StructureFeature<?> debug0) {
/*  65 */     return new LocationPredicate(MinMaxBounds.Floats.ANY, MinMaxBounds.Floats.ANY, MinMaxBounds.Floats.ANY, null, debug0, null, null, LightPredicate.ANY, BlockPredicate.ANY, FluidPredicate.ANY);
/*     */   }
/*     */   
/*     */   public boolean matches(ServerLevel debug1, double debug2, double debug4, double debug6) {
/*  69 */     return matches(debug1, (float)debug2, (float)debug4, (float)debug6);
/*     */   }
/*     */   
/*     */   public boolean matches(ServerLevel debug1, float debug2, float debug3, float debug4) {
/*  73 */     if (!this.x.matches(debug2)) {
/*  74 */       return false;
/*     */     }
/*  76 */     if (!this.y.matches(debug3)) {
/*  77 */       return false;
/*     */     }
/*  79 */     if (!this.z.matches(debug4)) {
/*  80 */       return false;
/*     */     }
/*  82 */     if (this.dimension != null && this.dimension != debug1.dimension()) {
/*  83 */       return false;
/*     */     }
/*     */     
/*  86 */     BlockPos debug5 = new BlockPos(debug2, debug3, debug4);
/*  87 */     boolean debug6 = debug1.isLoaded(debug5);
/*     */     
/*  89 */     Optional<ResourceKey<Biome>> debug7 = debug1.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getResourceKey(debug1.getBiome(debug5));
/*  90 */     if (!debug7.isPresent()) {
/*  91 */       return false;
/*     */     }
/*  93 */     if (this.biome != null && (!debug6 || this.biome != debug7.get())) {
/*  94 */       return false;
/*     */     }
/*  96 */     if (this.feature != null && (!debug6 || !debug1.structureFeatureManager().getStructureAt(debug5, true, this.feature).isValid())) {
/*  97 */       return false;
/*     */     }
/*  99 */     if (this.smokey != null && (!debug6 || this.smokey.booleanValue() != CampfireBlock.isSmokeyPos((Level)debug1, debug5))) {
/* 100 */       return false;
/*     */     }
/* 102 */     if (!this.light.matches(debug1, debug5)) {
/* 103 */       return false;
/*     */     }
/* 105 */     if (!this.block.matches(debug1, debug5)) {
/* 106 */       return false;
/*     */     }
/* 108 */     if (!this.fluid.matches(debug1, debug5)) {
/* 109 */       return false;
/*     */     }
/*     */     
/* 112 */     return true;
/*     */   }
/*     */   
/*     */   public JsonElement serializeToJson() {
/* 116 */     if (this == ANY) {
/* 117 */       return (JsonElement)JsonNull.INSTANCE;
/*     */     }
/*     */     
/* 120 */     JsonObject debug1 = new JsonObject();
/*     */     
/* 122 */     if (!this.x.isAny() || !this.y.isAny() || !this.z.isAny()) {
/* 123 */       JsonObject debug2 = new JsonObject();
/* 124 */       debug2.add("x", this.x.serializeToJson());
/* 125 */       debug2.add("y", this.y.serializeToJson());
/* 126 */       debug2.add("z", this.z.serializeToJson());
/* 127 */       debug1.add("position", (JsonElement)debug2);
/*     */     } 
/* 129 */     if (this.dimension != null) {
/* 130 */       Level.RESOURCE_KEY_CODEC.encodeStart((DynamicOps)JsonOps.INSTANCE, this.dimension).resultOrPartial(LOGGER::error).ifPresent(debug1 -> debug0.add("dimension", debug1));
/*     */     }
/* 132 */     if (this.feature != null) {
/* 133 */       debug1.addProperty("feature", this.feature.getFeatureName());
/*     */     }
/* 135 */     if (this.biome != null) {
/* 136 */       debug1.addProperty("biome", this.biome.location().toString());
/*     */     }
/* 138 */     if (this.smokey != null) {
/* 139 */       debug1.addProperty("smokey", this.smokey);
/*     */     }
/* 141 */     debug1.add("light", this.light.serializeToJson());
/* 142 */     debug1.add("block", this.block.serializeToJson());
/* 143 */     debug1.add("fluid", this.fluid.serializeToJson());
/*     */     
/* 145 */     return (JsonElement)debug1;
/*     */   }
/*     */   
/*     */   public static LocationPredicate fromJson(@Nullable JsonElement debug0) {
/* 149 */     if (debug0 == null || debug0.isJsonNull()) {
/* 150 */       return ANY;
/*     */     }
/* 152 */     JsonObject debug1 = GsonHelper.convertToJsonObject(debug0, "location");
/* 153 */     JsonObject debug2 = GsonHelper.getAsJsonObject(debug1, "position", new JsonObject());
/* 154 */     MinMaxBounds.Floats debug3 = MinMaxBounds.Floats.fromJson(debug2.get("x"));
/* 155 */     MinMaxBounds.Floats debug4 = MinMaxBounds.Floats.fromJson(debug2.get("y"));
/* 156 */     MinMaxBounds.Floats debug5 = MinMaxBounds.Floats.fromJson(debug2.get("z"));
/* 157 */     ResourceKey<Level> debug6 = debug1.has("dimension") ? ResourceLocation.CODEC.parse((DynamicOps)JsonOps.INSTANCE, debug1.get("dimension")).resultOrPartial(LOGGER::error).map(debug0 -> ResourceKey.create(Registry.DIMENSION_REGISTRY, debug0)).orElse(null) : null;
/* 158 */     StructureFeature<?> debug7 = debug1.has("feature") ? (StructureFeature)StructureFeature.STRUCTURES_REGISTRY.get(GsonHelper.getAsString(debug1, "feature")) : null;
/* 159 */     ResourceKey<Biome> debug8 = null;
/* 160 */     if (debug1.has("biome")) {
/* 161 */       ResourceLocation resourceLocation = new ResourceLocation(GsonHelper.getAsString(debug1, "biome"));
/* 162 */       debug8 = ResourceKey.create(Registry.BIOME_REGISTRY, resourceLocation);
/*     */     } 
/* 164 */     Boolean debug9 = debug1.has("smokey") ? Boolean.valueOf(debug1.get("smokey").getAsBoolean()) : null;
/* 165 */     LightPredicate debug10 = LightPredicate.fromJson(debug1.get("light"));
/* 166 */     BlockPredicate debug11 = BlockPredicate.fromJson(debug1.get("block"));
/* 167 */     FluidPredicate debug12 = FluidPredicate.fromJson(debug1.get("fluid"));
/* 168 */     return new LocationPredicate(debug3, debug4, debug5, debug8, debug7, debug6, debug9, debug10, debug11, debug12);
/*     */   }
/*     */   
/*     */   public static class Builder {
/* 172 */     private MinMaxBounds.Floats x = MinMaxBounds.Floats.ANY;
/* 173 */     private MinMaxBounds.Floats y = MinMaxBounds.Floats.ANY;
/* 174 */     private MinMaxBounds.Floats z = MinMaxBounds.Floats.ANY;
/*     */     
/*     */     @Nullable
/*     */     private ResourceKey<Biome> biome;
/*     */     
/*     */     @Nullable
/*     */     private StructureFeature<?> feature;
/*     */     @Nullable
/*     */     private ResourceKey<Level> dimension;
/*     */     @Nullable
/*     */     private Boolean smokey;
/* 185 */     private LightPredicate light = LightPredicate.ANY;
/* 186 */     private BlockPredicate block = BlockPredicate.ANY;
/* 187 */     private FluidPredicate fluid = FluidPredicate.ANY;
/*     */     
/*     */     public static Builder location() {
/* 190 */       return new Builder();
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setBiome(@Nullable ResourceKey<Biome> debug1) {
/* 209 */       this.biome = debug1;
/* 210 */       return this;
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setBlock(BlockPredicate debug1) {
/* 229 */       this.block = debug1;
/* 230 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setSmokey(Boolean debug1) {
/* 239 */       this.smokey = debug1;
/* 240 */       return this;
/*     */     }
/*     */     
/*     */     public LocationPredicate build() {
/* 244 */       return new LocationPredicate(this.x, this.y, this.z, this.biome, this.feature, this.dimension, this.smokey, this.light, this.block, this.fluid);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\advancements\critereon\LocationPredicate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */