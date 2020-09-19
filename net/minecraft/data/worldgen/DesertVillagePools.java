/*     */ package net.minecraft.data.worldgen;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
/*     */ import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
/*     */ 
/*     */ public class DesertVillagePools {
/*  10 */   public static final StructureTemplatePool START = Pools.register(new StructureTemplatePool(new ResourceLocation("village/desert/town_centers"), new ResourceLocation("empty"), 
/*     */ 
/*     */         
/*  13 */         (List)ImmutableList.of(
/*  14 */           Pair.of(StructurePoolElement.legacy("village/desert/town_centers/desert_meeting_point_1"), Integer.valueOf(98)), 
/*  15 */           Pair.of(StructurePoolElement.legacy("village/desert/town_centers/desert_meeting_point_2"), Integer.valueOf(98)), 
/*  16 */           Pair.of(StructurePoolElement.legacy("village/desert/town_centers/desert_meeting_point_3"), Integer.valueOf(49)), 
/*  17 */           Pair.of(StructurePoolElement.legacy("village/desert/zombie/town_centers/desert_meeting_point_1", ProcessorLists.ZOMBIE_DESERT), Integer.valueOf(2)), 
/*  18 */           Pair.of(StructurePoolElement.legacy("village/desert/zombie/town_centers/desert_meeting_point_2", ProcessorLists.ZOMBIE_DESERT), Integer.valueOf(2)), 
/*  19 */           Pair.of(StructurePoolElement.legacy("village/desert/zombie/town_centers/desert_meeting_point_3", ProcessorLists.ZOMBIE_DESERT), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  25 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/desert/streets"), new ResourceLocation("village/desert/terminators"), 
/*     */ 
/*     */           
/*  28 */           (List)ImmutableList.of(
/*  29 */             Pair.of(StructurePoolElement.legacy("village/desert/streets/corner_01"), Integer.valueOf(3)), 
/*  30 */             Pair.of(StructurePoolElement.legacy("village/desert/streets/corner_02"), Integer.valueOf(3)), 
/*  31 */             Pair.of(StructurePoolElement.legacy("village/desert/streets/straight_01"), Integer.valueOf(4)), 
/*  32 */             Pair.of(StructurePoolElement.legacy("village/desert/streets/straight_02"), Integer.valueOf(4)), 
/*  33 */             Pair.of(StructurePoolElement.legacy("village/desert/streets/straight_03"), Integer.valueOf(3)), 
/*  34 */             Pair.of(StructurePoolElement.legacy("village/desert/streets/crossroad_01"), Integer.valueOf(3)), 
/*  35 */             Pair.of(StructurePoolElement.legacy("village/desert/streets/crossroad_02"), Integer.valueOf(3)), 
/*  36 */             Pair.of(StructurePoolElement.legacy("village/desert/streets/crossroad_03"), Integer.valueOf(3)), 
/*  37 */             Pair.of(StructurePoolElement.legacy("village/desert/streets/square_01"), Integer.valueOf(3)), 
/*  38 */             Pair.of(StructurePoolElement.legacy("village/desert/streets/square_02"), Integer.valueOf(3)), 
/*  39 */             Pair.of(StructurePoolElement.legacy("village/desert/streets/turn_01"), Integer.valueOf(3))), StructureTemplatePool.Projection.TERRAIN_MATCHING));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  44 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/desert/zombie/streets"), new ResourceLocation("village/desert/zombie/terminators"), 
/*     */ 
/*     */           
/*  47 */           (List)ImmutableList.of(
/*  48 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/streets/corner_01"), Integer.valueOf(3)), 
/*  49 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/streets/corner_02"), Integer.valueOf(3)), 
/*  50 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/streets/straight_01"), Integer.valueOf(4)), 
/*  51 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/streets/straight_02"), Integer.valueOf(4)), 
/*  52 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/streets/straight_03"), Integer.valueOf(3)), 
/*  53 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/streets/crossroad_01"), Integer.valueOf(3)), 
/*  54 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/streets/crossroad_02"), Integer.valueOf(3)), 
/*  55 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/streets/crossroad_03"), Integer.valueOf(3)), 
/*  56 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/streets/square_01"), Integer.valueOf(3)), 
/*  57 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/streets/square_02"), Integer.valueOf(3)), 
/*  58 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/streets/turn_01"), Integer.valueOf(3))), StructureTemplatePool.Projection.TERRAIN_MATCHING));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  63 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/desert/houses"), new ResourceLocation("village/desert/terminators"), 
/*     */ 
/*     */           
/*  66 */           (List)ImmutableList.of(
/*  67 */             Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_small_house_1"), Integer.valueOf(2)), 
/*  68 */             Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_small_house_2"), Integer.valueOf(2)), 
/*  69 */             Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_small_house_3"), Integer.valueOf(2)), 
/*  70 */             Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_small_house_4"), Integer.valueOf(2)), 
/*  71 */             Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_small_house_5"), Integer.valueOf(2)), 
/*  72 */             Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_small_house_6"), Integer.valueOf(1)), 
/*  73 */             Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_small_house_7"), Integer.valueOf(2)), 
/*  74 */             Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_small_house_8"), Integer.valueOf(2)), 
/*  75 */             Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_medium_house_1"), Integer.valueOf(2)), 
/*  76 */             Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_medium_house_2"), Integer.valueOf(2)), 
/*  77 */             Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_butcher_shop_1"), Integer.valueOf(2)), 
/*  78 */             Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_tool_smith_1"), Integer.valueOf(2)), (Object[])new Pair[] { 
/*  79 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_fletcher_house_1"), Integer.valueOf(2)), 
/*  80 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_shepherd_house_1"), Integer.valueOf(2)), 
/*  81 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_armorer_1"), Integer.valueOf(1)), 
/*  82 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_fisher_1"), Integer.valueOf(2)), 
/*  83 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_tannery_1"), Integer.valueOf(2)), 
/*  84 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_cartographer_house_1"), Integer.valueOf(2)), 
/*  85 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_library_1"), Integer.valueOf(2)), 
/*  86 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_mason_1"), Integer.valueOf(2)), 
/*  87 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_weaponsmith_1"), Integer.valueOf(2)), 
/*  88 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_temple_1"), Integer.valueOf(2)), 
/*  89 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_temple_2"), Integer.valueOf(2)), 
/*  90 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_large_farm_1", ProcessorLists.FARM_DESERT), Integer.valueOf(11)), 
/*  91 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_farm_1", ProcessorLists.FARM_DESERT), Integer.valueOf(4)), 
/*  92 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_farm_2", ProcessorLists.FARM_DESERT), Integer.valueOf(4)), 
/*  93 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_animal_pen_1"), Integer.valueOf(2)), 
/*  94 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_animal_pen_2"), Integer.valueOf(2)), 
/*  95 */               Pair.of(StructurePoolElement.empty(), Integer.valueOf(5)) }), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 100 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/desert/zombie/houses"), new ResourceLocation("village/desert/zombie/terminators"), 
/*     */ 
/*     */           
/* 103 */           (List)ImmutableList.of(
/* 104 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/houses/desert_small_house_1", ProcessorLists.ZOMBIE_DESERT), Integer.valueOf(2)), 
/* 105 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/houses/desert_small_house_2", ProcessorLists.ZOMBIE_DESERT), Integer.valueOf(2)), 
/* 106 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/houses/desert_small_house_3", ProcessorLists.ZOMBIE_DESERT), Integer.valueOf(2)), 
/* 107 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/houses/desert_small_house_4", ProcessorLists.ZOMBIE_DESERT), Integer.valueOf(2)), 
/* 108 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/houses/desert_small_house_5", ProcessorLists.ZOMBIE_DESERT), Integer.valueOf(2)), 
/* 109 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/houses/desert_small_house_6", ProcessorLists.ZOMBIE_DESERT), Integer.valueOf(1)), 
/* 110 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/houses/desert_small_house_7", ProcessorLists.ZOMBIE_DESERT), Integer.valueOf(2)), 
/* 111 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/houses/desert_small_house_8", ProcessorLists.ZOMBIE_DESERT), Integer.valueOf(2)), 
/* 112 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/houses/desert_medium_house_1", ProcessorLists.ZOMBIE_DESERT), Integer.valueOf(2)), 
/* 113 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/houses/desert_medium_house_2", ProcessorLists.ZOMBIE_DESERT), Integer.valueOf(2)), 
/* 114 */             Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_butcher_shop_1", ProcessorLists.ZOMBIE_DESERT), Integer.valueOf(2)), 
/* 115 */             Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_tool_smith_1", ProcessorLists.ZOMBIE_DESERT), Integer.valueOf(2)), (Object[])new Pair[] { 
/* 116 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_fletcher_house_1", ProcessorLists.ZOMBIE_DESERT), Integer.valueOf(2)), 
/* 117 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_shepherd_house_1", ProcessorLists.ZOMBIE_DESERT), Integer.valueOf(2)), 
/* 118 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_armorer_1", ProcessorLists.ZOMBIE_DESERT), Integer.valueOf(1)), 
/* 119 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_fisher_1", ProcessorLists.ZOMBIE_DESERT), Integer.valueOf(2)), 
/* 120 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_tannery_1", ProcessorLists.ZOMBIE_DESERT), Integer.valueOf(2)), 
/* 121 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_cartographer_house_1", ProcessorLists.ZOMBIE_DESERT), Integer.valueOf(2)), 
/* 122 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_library_1", ProcessorLists.ZOMBIE_DESERT), Integer.valueOf(2)), 
/* 123 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_mason_1", ProcessorLists.ZOMBIE_DESERT), Integer.valueOf(2)), 
/* 124 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_weaponsmith_1", ProcessorLists.ZOMBIE_DESERT), Integer.valueOf(2)), 
/* 125 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_temple_1", ProcessorLists.ZOMBIE_DESERT), Integer.valueOf(2)), 
/* 126 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_temple_2", ProcessorLists.ZOMBIE_DESERT), Integer.valueOf(2)), 
/* 127 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_large_farm_1", ProcessorLists.ZOMBIE_DESERT), Integer.valueOf(7)), 
/* 128 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_farm_1", ProcessorLists.ZOMBIE_DESERT), Integer.valueOf(4)), 
/* 129 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_farm_2", ProcessorLists.ZOMBIE_DESERT), Integer.valueOf(4)), 
/* 130 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_animal_pen_1", ProcessorLists.ZOMBIE_DESERT), Integer.valueOf(2)), 
/* 131 */               Pair.of(StructurePoolElement.legacy("village/desert/houses/desert_animal_pen_2", ProcessorLists.ZOMBIE_DESERT), Integer.valueOf(2)), 
/* 132 */               Pair.of(StructurePoolElement.empty(), Integer.valueOf(5)) }), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 137 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/desert/terminators"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 140 */           (List)ImmutableList.of(
/* 141 */             Pair.of(StructurePoolElement.legacy("village/desert/terminators/terminator_01"), Integer.valueOf(1)), 
/* 142 */             Pair.of(StructurePoolElement.legacy("village/desert/terminators/terminator_02"), Integer.valueOf(1))), StructureTemplatePool.Projection.TERRAIN_MATCHING));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 147 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/desert/zombie/terminators"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 150 */           (List)ImmutableList.of(
/* 151 */             Pair.of(StructurePoolElement.legacy("village/desert/terminators/terminator_01"), Integer.valueOf(1)), 
/* 152 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/terminators/terminator_02"), Integer.valueOf(1))), StructureTemplatePool.Projection.TERRAIN_MATCHING));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 157 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/desert/decor"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 160 */           (List)ImmutableList.of(
/* 161 */             Pair.of(StructurePoolElement.legacy("village/desert/desert_lamp_1"), Integer.valueOf(10)), 
/* 162 */             Pair.of(StructurePoolElement.feature(Features.PATCH_CACTUS), Integer.valueOf(4)), 
/* 163 */             Pair.of(StructurePoolElement.feature(Features.PILE_HAY), Integer.valueOf(4)), 
/* 164 */             Pair.of(StructurePoolElement.empty(), Integer.valueOf(10))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 169 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/desert/zombie/decor"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 172 */           (List)ImmutableList.of(
/* 173 */             Pair.of(StructurePoolElement.legacy("village/desert/desert_lamp_1", ProcessorLists.ZOMBIE_DESERT), Integer.valueOf(10)), 
/* 174 */             Pair.of(StructurePoolElement.feature(Features.PATCH_CACTUS), Integer.valueOf(4)), 
/* 175 */             Pair.of(StructurePoolElement.feature(Features.PILE_HAY), Integer.valueOf(4)), 
/* 176 */             Pair.of(StructurePoolElement.empty(), Integer.valueOf(10))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 181 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/desert/villagers"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 184 */           (List)ImmutableList.of(
/* 185 */             Pair.of(StructurePoolElement.legacy("village/desert/villagers/nitwit"), Integer.valueOf(1)), 
/* 186 */             Pair.of(StructurePoolElement.legacy("village/desert/villagers/baby"), Integer.valueOf(1)), 
/* 187 */             Pair.of(StructurePoolElement.legacy("village/desert/villagers/unemployed"), Integer.valueOf(10))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 192 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/desert/zombie/villagers"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 195 */           (List)ImmutableList.of(
/* 196 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/villagers/nitwit"), Integer.valueOf(1)), 
/* 197 */             Pair.of(StructurePoolElement.legacy("village/desert/zombie/villagers/unemployed"), Integer.valueOf(10))), StructureTemplatePool.Projection.RIGID));
/*     */   }
/*     */   
/*     */   public static void bootstrap() {}
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\worldgen\DesertVillagePools.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */