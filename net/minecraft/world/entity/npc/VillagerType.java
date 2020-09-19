/*    */ package net.minecraft.world.entity.npc;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.Util;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.biome.Biomes;
/*    */ 
/*    */ public final class VillagerType {
/* 15 */   public static final VillagerType DESERT = register("desert");
/* 16 */   public static final VillagerType JUNGLE = register("jungle");
/* 17 */   public static final VillagerType PLAINS = register("plains");
/* 18 */   public static final VillagerType SAVANNA = register("savanna");
/* 19 */   public static final VillagerType SNOW = register("snow");
/* 20 */   public static final VillagerType SWAMP = register("swamp");
/* 21 */   public static final VillagerType TAIGA = register("taiga");
/*    */   private final String name;
/*    */   private static final Map<ResourceKey<Biome>, VillagerType> BY_BIOME;
/*    */   
/*    */   private VillagerType(String debug1) {
/* 26 */     this.name = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 31 */     return this.name;
/*    */   }
/*    */   
/*    */   private static VillagerType register(String debug0) {
/* 35 */     return (VillagerType)Registry.register((Registry)Registry.VILLAGER_TYPE, new ResourceLocation(debug0), new VillagerType(debug0));
/*    */   }
/*    */   static {
/* 38 */     BY_BIOME = (Map<ResourceKey<Biome>, VillagerType>)Util.make(Maps.newHashMap(), debug0 -> {
/*    */           debug0.put(Biomes.BADLANDS, DESERT);
/*    */           debug0.put(Biomes.BADLANDS_PLATEAU, DESERT);
/*    */           debug0.put(Biomes.DESERT, DESERT);
/*    */           debug0.put(Biomes.DESERT_HILLS, DESERT);
/*    */           debug0.put(Biomes.DESERT_LAKES, DESERT);
/*    */           debug0.put(Biomes.ERODED_BADLANDS, DESERT);
/*    */           debug0.put(Biomes.MODIFIED_BADLANDS_PLATEAU, DESERT);
/*    */           debug0.put(Biomes.MODIFIED_WOODED_BADLANDS_PLATEAU, DESERT);
/*    */           debug0.put(Biomes.WOODED_BADLANDS_PLATEAU, DESERT);
/*    */           debug0.put(Biomes.BAMBOO_JUNGLE, JUNGLE);
/*    */           debug0.put(Biomes.BAMBOO_JUNGLE_HILLS, JUNGLE);
/*    */           debug0.put(Biomes.JUNGLE, JUNGLE);
/*    */           debug0.put(Biomes.JUNGLE_EDGE, JUNGLE);
/*    */           debug0.put(Biomes.JUNGLE_HILLS, JUNGLE);
/*    */           debug0.put(Biomes.MODIFIED_JUNGLE, JUNGLE);
/*    */           debug0.put(Biomes.MODIFIED_JUNGLE_EDGE, JUNGLE);
/*    */           debug0.put(Biomes.SAVANNA_PLATEAU, SAVANNA);
/*    */           debug0.put(Biomes.SAVANNA, SAVANNA);
/*    */           debug0.put(Biomes.SHATTERED_SAVANNA, SAVANNA);
/*    */           debug0.put(Biomes.SHATTERED_SAVANNA_PLATEAU, SAVANNA);
/*    */           debug0.put(Biomes.DEEP_FROZEN_OCEAN, SNOW);
/*    */           debug0.put(Biomes.FROZEN_OCEAN, SNOW);
/*    */           debug0.put(Biomes.FROZEN_RIVER, SNOW);
/*    */           debug0.put(Biomes.ICE_SPIKES, SNOW);
/*    */           debug0.put(Biomes.SNOWY_BEACH, SNOW);
/*    */           debug0.put(Biomes.SNOWY_MOUNTAINS, SNOW);
/*    */           debug0.put(Biomes.SNOWY_TAIGA, SNOW);
/*    */           debug0.put(Biomes.SNOWY_TAIGA_HILLS, SNOW);
/*    */           debug0.put(Biomes.SNOWY_TAIGA_MOUNTAINS, SNOW);
/*    */           debug0.put(Biomes.SNOWY_TUNDRA, SNOW);
/*    */           debug0.put(Biomes.SWAMP, SWAMP);
/*    */           debug0.put(Biomes.SWAMP_HILLS, SWAMP);
/*    */           debug0.put(Biomes.GIANT_SPRUCE_TAIGA, TAIGA);
/*    */           debug0.put(Biomes.GIANT_SPRUCE_TAIGA_HILLS, TAIGA);
/*    */           debug0.put(Biomes.GIANT_TREE_TAIGA, TAIGA);
/*    */           debug0.put(Biomes.GIANT_TREE_TAIGA_HILLS, TAIGA);
/*    */           debug0.put(Biomes.GRAVELLY_MOUNTAINS, TAIGA);
/*    */           debug0.put(Biomes.MODIFIED_GRAVELLY_MOUNTAINS, TAIGA);
/*    */           debug0.put(Biomes.MOUNTAIN_EDGE, TAIGA);
/*    */           debug0.put(Biomes.MOUNTAINS, TAIGA);
/*    */           debug0.put(Biomes.TAIGA, TAIGA);
/*    */           debug0.put(Biomes.TAIGA_HILLS, TAIGA);
/*    */           debug0.put(Biomes.TAIGA_MOUNTAINS, TAIGA);
/*    */           debug0.put(Biomes.WOODED_MOUNTAINS, TAIGA);
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static VillagerType byBiome(Optional<ResourceKey<Biome>> debug0) {
/* 93 */     return debug0.<VillagerType>flatMap(debug0 -> Optional.ofNullable(BY_BIOME.get(debug0))).orElse(PLAINS);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\npc\VillagerType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */