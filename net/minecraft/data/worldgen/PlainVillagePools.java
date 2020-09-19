/*     */ package net.minecraft.data.worldgen;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
/*     */ import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
/*     */ 
/*     */ public class PlainVillagePools {
/*  10 */   public static final StructureTemplatePool START = Pools.register(new StructureTemplatePool(new ResourceLocation("village/plains/town_centers"), new ResourceLocation("empty"), 
/*     */ 
/*     */         
/*  13 */         (List)ImmutableList.of(
/*  14 */           Pair.of(StructurePoolElement.legacy("village/plains/town_centers/plains_fountain_01", ProcessorLists.MOSSIFY_20_PERCENT), Integer.valueOf(50)), 
/*  15 */           Pair.of(StructurePoolElement.legacy("village/plains/town_centers/plains_meeting_point_1", ProcessorLists.MOSSIFY_20_PERCENT), Integer.valueOf(50)), 
/*  16 */           Pair.of(StructurePoolElement.legacy("village/plains/town_centers/plains_meeting_point_2"), Integer.valueOf(50)), 
/*  17 */           Pair.of(StructurePoolElement.legacy("village/plains/town_centers/plains_meeting_point_3", ProcessorLists.MOSSIFY_70_PERCENT), Integer.valueOf(50)), 
/*  18 */           Pair.of(StructurePoolElement.legacy("village/plains/zombie/town_centers/plains_fountain_01", ProcessorLists.ZOMBIE_PLAINS), Integer.valueOf(1)), 
/*  19 */           Pair.of(StructurePoolElement.legacy("village/plains/zombie/town_centers/plains_meeting_point_1", ProcessorLists.ZOMBIE_PLAINS), Integer.valueOf(1)), 
/*  20 */           Pair.of(StructurePoolElement.legacy("village/plains/zombie/town_centers/plains_meeting_point_2", ProcessorLists.ZOMBIE_PLAINS), Integer.valueOf(1)), 
/*  21 */           Pair.of(StructurePoolElement.legacy("village/plains/zombie/town_centers/plains_meeting_point_3", ProcessorLists.ZOMBIE_PLAINS), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  27 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/plains/streets"), new ResourceLocation("village/plains/terminators"), 
/*     */ 
/*     */           
/*  30 */           (List)ImmutableList.of(
/*  31 */             Pair.of(StructurePoolElement.legacy("village/plains/streets/corner_01", ProcessorLists.STREET_PLAINS), Integer.valueOf(2)), 
/*  32 */             Pair.of(StructurePoolElement.legacy("village/plains/streets/corner_02", ProcessorLists.STREET_PLAINS), Integer.valueOf(2)), 
/*  33 */             Pair.of(StructurePoolElement.legacy("village/plains/streets/corner_03", ProcessorLists.STREET_PLAINS), Integer.valueOf(2)), 
/*  34 */             Pair.of(StructurePoolElement.legacy("village/plains/streets/straight_01", ProcessorLists.STREET_PLAINS), Integer.valueOf(4)), 
/*  35 */             Pair.of(StructurePoolElement.legacy("village/plains/streets/straight_02", ProcessorLists.STREET_PLAINS), Integer.valueOf(4)), 
/*  36 */             Pair.of(StructurePoolElement.legacy("village/plains/streets/straight_03", ProcessorLists.STREET_PLAINS), Integer.valueOf(7)), 
/*  37 */             Pair.of(StructurePoolElement.legacy("village/plains/streets/straight_04", ProcessorLists.STREET_PLAINS), Integer.valueOf(7)), 
/*  38 */             Pair.of(StructurePoolElement.legacy("village/plains/streets/straight_05", ProcessorLists.STREET_PLAINS), Integer.valueOf(3)), 
/*  39 */             Pair.of(StructurePoolElement.legacy("village/plains/streets/straight_06", ProcessorLists.STREET_PLAINS), Integer.valueOf(4)), 
/*  40 */             Pair.of(StructurePoolElement.legacy("village/plains/streets/crossroad_01", ProcessorLists.STREET_PLAINS), Integer.valueOf(2)), 
/*  41 */             Pair.of(StructurePoolElement.legacy("village/plains/streets/crossroad_02", ProcessorLists.STREET_PLAINS), Integer.valueOf(1)), 
/*  42 */             Pair.of(StructurePoolElement.legacy("village/plains/streets/crossroad_03", ProcessorLists.STREET_PLAINS), Integer.valueOf(2)), (Object[])new Pair[] {
/*  43 */               Pair.of(StructurePoolElement.legacy("village/plains/streets/crossroad_04", ProcessorLists.STREET_PLAINS), Integer.valueOf(2)), 
/*  44 */               Pair.of(StructurePoolElement.legacy("village/plains/streets/crossroad_05", ProcessorLists.STREET_PLAINS), Integer.valueOf(2)), 
/*  45 */               Pair.of(StructurePoolElement.legacy("village/plains/streets/crossroad_06", ProcessorLists.STREET_PLAINS), Integer.valueOf(2)), 
/*  46 */               Pair.of(StructurePoolElement.legacy("village/plains/streets/turn_01", ProcessorLists.STREET_PLAINS), Integer.valueOf(3))
/*     */             }), StructureTemplatePool.Projection.TERRAIN_MATCHING));
/*     */ 
/*     */ 
/*     */     
/*  51 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/plains/zombie/streets"), new ResourceLocation("village/plains/terminators"), 
/*     */ 
/*     */           
/*  54 */           (List)ImmutableList.of(
/*  55 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/streets/corner_01", ProcessorLists.STREET_PLAINS), Integer.valueOf(2)), 
/*  56 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/streets/corner_02", ProcessorLists.STREET_PLAINS), Integer.valueOf(2)), 
/*  57 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/streets/corner_03", ProcessorLists.STREET_PLAINS), Integer.valueOf(2)), 
/*  58 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/streets/straight_01", ProcessorLists.STREET_PLAINS), Integer.valueOf(4)), 
/*  59 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/streets/straight_02", ProcessorLists.STREET_PLAINS), Integer.valueOf(4)), 
/*  60 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/streets/straight_03", ProcessorLists.STREET_PLAINS), Integer.valueOf(7)), 
/*  61 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/streets/straight_04", ProcessorLists.STREET_PLAINS), Integer.valueOf(7)), 
/*  62 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/streets/straight_05", ProcessorLists.STREET_PLAINS), Integer.valueOf(3)), 
/*  63 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/streets/straight_06", ProcessorLists.STREET_PLAINS), Integer.valueOf(4)), 
/*  64 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/streets/crossroad_01", ProcessorLists.STREET_PLAINS), Integer.valueOf(2)), 
/*  65 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/streets/crossroad_02", ProcessorLists.STREET_PLAINS), Integer.valueOf(1)), 
/*  66 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/streets/crossroad_03", ProcessorLists.STREET_PLAINS), Integer.valueOf(2)), (Object[])new Pair[] {
/*  67 */               Pair.of(StructurePoolElement.legacy("village/plains/zombie/streets/crossroad_04", ProcessorLists.STREET_PLAINS), Integer.valueOf(2)), 
/*  68 */               Pair.of(StructurePoolElement.legacy("village/plains/zombie/streets/crossroad_05", ProcessorLists.STREET_PLAINS), Integer.valueOf(2)), 
/*  69 */               Pair.of(StructurePoolElement.legacy("village/plains/zombie/streets/crossroad_06", ProcessorLists.STREET_PLAINS), Integer.valueOf(2)), 
/*  70 */               Pair.of(StructurePoolElement.legacy("village/plains/zombie/streets/turn_01", ProcessorLists.STREET_PLAINS), Integer.valueOf(3))
/*     */             }), StructureTemplatePool.Projection.TERRAIN_MATCHING));
/*     */ 
/*     */ 
/*     */     
/*  75 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/plains/houses"), new ResourceLocation("village/plains/terminators"), 
/*     */ 
/*     */           
/*  78 */           (List)ImmutableList.of(
/*  79 */             Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_small_house_1", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(2)), 
/*  80 */             Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_small_house_2", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(2)), 
/*  81 */             Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_small_house_3", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(2)), 
/*  82 */             Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_small_house_4", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(2)), 
/*  83 */             Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_small_house_5", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(2)), 
/*  84 */             Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_small_house_6", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(1)), 
/*  85 */             Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_small_house_7", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(2)), 
/*  86 */             Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_small_house_8", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(3)), 
/*  87 */             Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_medium_house_1", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(2)), 
/*  88 */             Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_medium_house_2", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(2)), 
/*  89 */             Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_big_house_1", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(2)), 
/*  90 */             Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_butcher_shop_1", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(2)), (Object[])new Pair[] { 
/*  91 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_butcher_shop_2", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(2)), 
/*  92 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_tool_smith_1", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(2)), 
/*  93 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_fletcher_house_1", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(2)), 
/*  94 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_shepherds_house_1"), Integer.valueOf(2)), 
/*  95 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_armorer_house_1", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(2)), 
/*  96 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_fisher_cottage_1", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(2)), 
/*  97 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_tannery_1", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(2)), 
/*  98 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_cartographer_1", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(1)), 
/*  99 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_library_1", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(5)), 
/* 100 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_library_2", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(1)), 
/* 101 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_masons_house_1", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(2)), 
/* 102 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_weaponsmith_1", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(2)), 
/* 103 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_temple_3", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(2)), 
/* 104 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_temple_4", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(2)), 
/* 105 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_stable_1", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(2)), 
/* 106 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_stable_2"), Integer.valueOf(2)), 
/* 107 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_large_farm_1", ProcessorLists.FARM_PLAINS), Integer.valueOf(4)), 
/* 108 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_small_farm_1", ProcessorLists.FARM_PLAINS), Integer.valueOf(4)), 
/* 109 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_animal_pen_1"), Integer.valueOf(1)), 
/* 110 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_animal_pen_2"), Integer.valueOf(1)), 
/* 111 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_animal_pen_3"), Integer.valueOf(5)), 
/* 112 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_accessory_1"), Integer.valueOf(1)), 
/* 113 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_meeting_point_4", ProcessorLists.MOSSIFY_70_PERCENT), Integer.valueOf(3)), 
/* 114 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_meeting_point_5"), Integer.valueOf(1)), 
/* 115 */               Pair.of(StructurePoolElement.empty(), Integer.valueOf(10)) }), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 120 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/plains/zombie/houses"), new ResourceLocation("village/plains/terminators"), 
/*     */ 
/*     */           
/* 123 */           (List)ImmutableList.of(
/* 124 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/houses/plains_small_house_1", ProcessorLists.ZOMBIE_PLAINS), Integer.valueOf(2)), 
/* 125 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/houses/plains_small_house_2", ProcessorLists.ZOMBIE_PLAINS), Integer.valueOf(2)), 
/* 126 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/houses/plains_small_house_3", ProcessorLists.ZOMBIE_PLAINS), Integer.valueOf(2)), 
/* 127 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/houses/plains_small_house_4", ProcessorLists.ZOMBIE_PLAINS), Integer.valueOf(2)), 
/* 128 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/houses/plains_small_house_5", ProcessorLists.ZOMBIE_PLAINS), Integer.valueOf(2)), 
/* 129 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/houses/plains_small_house_6", ProcessorLists.ZOMBIE_PLAINS), Integer.valueOf(1)), 
/* 130 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/houses/plains_small_house_7", ProcessorLists.ZOMBIE_PLAINS), Integer.valueOf(2)), 
/* 131 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/houses/plains_small_house_8", ProcessorLists.ZOMBIE_PLAINS), Integer.valueOf(2)), 
/* 132 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/houses/plains_medium_house_1", ProcessorLists.ZOMBIE_PLAINS), Integer.valueOf(2)), 
/* 133 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/houses/plains_medium_house_2", ProcessorLists.ZOMBIE_PLAINS), Integer.valueOf(2)), 
/* 134 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/houses/plains_big_house_1", ProcessorLists.ZOMBIE_PLAINS), Integer.valueOf(2)), 
/* 135 */             Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_butcher_shop_1", ProcessorLists.ZOMBIE_PLAINS), Integer.valueOf(2)), (Object[])new Pair[] { 
/* 136 */               Pair.of(StructurePoolElement.legacy("village/plains/zombie/houses/plains_butcher_shop_2", ProcessorLists.ZOMBIE_PLAINS), Integer.valueOf(2)), 
/* 137 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_tool_smith_1", ProcessorLists.ZOMBIE_PLAINS), Integer.valueOf(2)), 
/* 138 */               Pair.of(StructurePoolElement.legacy("village/plains/zombie/houses/plains_fletcher_house_1", ProcessorLists.ZOMBIE_PLAINS), Integer.valueOf(2)), 
/* 139 */               Pair.of(StructurePoolElement.legacy("village/plains/zombie/houses/plains_shepherds_house_1", ProcessorLists.ZOMBIE_PLAINS), Integer.valueOf(2)), 
/* 140 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_armorer_house_1", ProcessorLists.ZOMBIE_PLAINS), Integer.valueOf(2)), 
/* 141 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_fisher_cottage_1", ProcessorLists.ZOMBIE_PLAINS), Integer.valueOf(2)), 
/* 142 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_tannery_1", ProcessorLists.ZOMBIE_PLAINS), Integer.valueOf(2)), 
/* 143 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_cartographer_1", ProcessorLists.ZOMBIE_PLAINS), Integer.valueOf(1)), 
/* 144 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_library_1", ProcessorLists.ZOMBIE_PLAINS), Integer.valueOf(3)), 
/* 145 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_library_2", ProcessorLists.ZOMBIE_PLAINS), Integer.valueOf(1)), 
/* 146 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_masons_house_1", ProcessorLists.ZOMBIE_PLAINS), Integer.valueOf(2)), 
/* 147 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_weaponsmith_1", ProcessorLists.ZOMBIE_PLAINS), Integer.valueOf(2)), 
/* 148 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_temple_3", ProcessorLists.ZOMBIE_PLAINS), Integer.valueOf(2)), 
/* 149 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_temple_4", ProcessorLists.ZOMBIE_PLAINS), Integer.valueOf(2)), 
/* 150 */               Pair.of(StructurePoolElement.legacy("village/plains/zombie/houses/plains_stable_1", ProcessorLists.ZOMBIE_PLAINS), Integer.valueOf(2)), 
/* 151 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_stable_2", ProcessorLists.ZOMBIE_PLAINS), Integer.valueOf(2)), 
/* 152 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_large_farm_1", ProcessorLists.ZOMBIE_PLAINS), Integer.valueOf(4)), 
/* 153 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_small_farm_1", ProcessorLists.ZOMBIE_PLAINS), Integer.valueOf(4)), 
/* 154 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_animal_pen_1", ProcessorLists.ZOMBIE_PLAINS), Integer.valueOf(1)), 
/* 155 */               Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_animal_pen_2", ProcessorLists.ZOMBIE_PLAINS), Integer.valueOf(1)), 
/* 156 */               Pair.of(StructurePoolElement.legacy("village/plains/zombie/houses/plains_animal_pen_3", ProcessorLists.ZOMBIE_PLAINS), Integer.valueOf(5)), 
/* 157 */               Pair.of(StructurePoolElement.legacy("village/plains/zombie/houses/plains_meeting_point_4", ProcessorLists.ZOMBIE_PLAINS), Integer.valueOf(3)), 
/* 158 */               Pair.of(StructurePoolElement.legacy("village/plains/zombie/houses/plains_meeting_point_5", ProcessorLists.ZOMBIE_PLAINS), Integer.valueOf(1)), 
/* 159 */               Pair.of(StructurePoolElement.empty(), Integer.valueOf(10)) }), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 164 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/plains/terminators"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 167 */           (List)ImmutableList.of(
/* 168 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_01", ProcessorLists.STREET_PLAINS), Integer.valueOf(1)), 
/* 169 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_02", ProcessorLists.STREET_PLAINS), Integer.valueOf(1)), 
/* 170 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_03", ProcessorLists.STREET_PLAINS), Integer.valueOf(1)), 
/* 171 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_04", ProcessorLists.STREET_PLAINS), Integer.valueOf(1))), StructureTemplatePool.Projection.TERRAIN_MATCHING));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 176 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/plains/trees"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 179 */           (List)ImmutableList.of(
/* 180 */             Pair.of(StructurePoolElement.feature(Features.OAK), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 185 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/plains/decor"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 188 */           (List)ImmutableList.of(
/* 189 */             Pair.of(StructurePoolElement.legacy("village/plains/plains_lamp_1"), Integer.valueOf(2)), 
/* 190 */             Pair.of(StructurePoolElement.feature(Features.OAK), Integer.valueOf(1)), 
/* 191 */             Pair.of(StructurePoolElement.feature(Features.FLOWER_PLAIN), Integer.valueOf(1)), 
/* 192 */             Pair.of(StructurePoolElement.feature(Features.PILE_HAY), Integer.valueOf(1)), 
/* 193 */             Pair.of(StructurePoolElement.empty(), Integer.valueOf(2))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 198 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/plains/zombie/decor"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 201 */           (List)ImmutableList.of(
/* 202 */             Pair.of(StructurePoolElement.legacy("village/plains/plains_lamp_1", ProcessorLists.ZOMBIE_PLAINS), Integer.valueOf(1)), 
/* 203 */             Pair.of(StructurePoolElement.feature(Features.OAK), Integer.valueOf(1)), 
/* 204 */             Pair.of(StructurePoolElement.feature(Features.FLOWER_PLAIN), Integer.valueOf(1)), 
/* 205 */             Pair.of(StructurePoolElement.feature(Features.PILE_HAY), Integer.valueOf(1)), 
/* 206 */             Pair.of(StructurePoolElement.empty(), Integer.valueOf(2))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 211 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/plains/villagers"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 214 */           (List)ImmutableList.of(
/* 215 */             Pair.of(StructurePoolElement.legacy("village/plains/villagers/nitwit"), Integer.valueOf(1)), 
/* 216 */             Pair.of(StructurePoolElement.legacy("village/plains/villagers/baby"), Integer.valueOf(1)), 
/* 217 */             Pair.of(StructurePoolElement.legacy("village/plains/villagers/unemployed"), Integer.valueOf(10))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 222 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/plains/zombie/villagers"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 225 */           (List)ImmutableList.of(
/* 226 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/villagers/nitwit"), Integer.valueOf(1)), 
/* 227 */             Pair.of(StructurePoolElement.legacy("village/plains/zombie/villagers/unemployed"), Integer.valueOf(10))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 234 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/common/animals"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 237 */           (List)ImmutableList.of(
/* 238 */             Pair.of(StructurePoolElement.legacy("village/common/animals/cows_1"), Integer.valueOf(7)), 
/* 239 */             Pair.of(StructurePoolElement.legacy("village/common/animals/pigs_1"), Integer.valueOf(7)), 
/* 240 */             Pair.of(StructurePoolElement.legacy("village/common/animals/horses_1"), Integer.valueOf(1)), 
/* 241 */             Pair.of(StructurePoolElement.legacy("village/common/animals/horses_2"), Integer.valueOf(1)), 
/* 242 */             Pair.of(StructurePoolElement.legacy("village/common/animals/horses_3"), Integer.valueOf(1)), 
/* 243 */             Pair.of(StructurePoolElement.legacy("village/common/animals/horses_4"), Integer.valueOf(1)), 
/* 244 */             Pair.of(StructurePoolElement.legacy("village/common/animals/horses_5"), Integer.valueOf(1)), 
/* 245 */             Pair.of(StructurePoolElement.legacy("village/common/animals/sheep_1"), Integer.valueOf(1)), 
/* 246 */             Pair.of(StructurePoolElement.legacy("village/common/animals/sheep_2"), Integer.valueOf(1)), 
/* 247 */             Pair.of(StructurePoolElement.empty(), Integer.valueOf(5))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 252 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/common/sheep"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 255 */           (List)ImmutableList.of(
/* 256 */             Pair.of(StructurePoolElement.legacy("village/common/animals/sheep_1"), Integer.valueOf(1)), 
/* 257 */             Pair.of(StructurePoolElement.legacy("village/common/animals/sheep_2"), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 262 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/common/cats"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 265 */           (List)ImmutableList.of(
/* 266 */             Pair.of(StructurePoolElement.legacy("village/common/animals/cat_black"), Integer.valueOf(1)), 
/* 267 */             Pair.of(StructurePoolElement.legacy("village/common/animals/cat_british"), Integer.valueOf(1)), 
/* 268 */             Pair.of(StructurePoolElement.legacy("village/common/animals/cat_calico"), Integer.valueOf(1)), 
/* 269 */             Pair.of(StructurePoolElement.legacy("village/common/animals/cat_persian"), Integer.valueOf(1)), 
/* 270 */             Pair.of(StructurePoolElement.legacy("village/common/animals/cat_ragdoll"), Integer.valueOf(1)), 
/* 271 */             Pair.of(StructurePoolElement.legacy("village/common/animals/cat_red"), Integer.valueOf(1)), 
/* 272 */             Pair.of(StructurePoolElement.legacy("village/common/animals/cat_siamese"), Integer.valueOf(1)), 
/* 273 */             Pair.of(StructurePoolElement.legacy("village/common/animals/cat_tabby"), Integer.valueOf(1)), 
/* 274 */             Pair.of(StructurePoolElement.legacy("village/common/animals/cat_white"), Integer.valueOf(1)), 
/* 275 */             Pair.of(StructurePoolElement.legacy("village/common/animals/cat_jellie"), Integer.valueOf(1)), 
/* 276 */             Pair.of(StructurePoolElement.empty(), Integer.valueOf(3))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 281 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/common/butcher_animals"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 284 */           (List)ImmutableList.of(
/* 285 */             Pair.of(StructurePoolElement.legacy("village/common/animals/cows_1"), Integer.valueOf(3)), 
/* 286 */             Pair.of(StructurePoolElement.legacy("village/common/animals/pigs_1"), Integer.valueOf(3)), 
/* 287 */             Pair.of(StructurePoolElement.legacy("village/common/animals/sheep_1"), Integer.valueOf(1)), 
/* 288 */             Pair.of(StructurePoolElement.legacy("village/common/animals/sheep_2"), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 293 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/common/iron_golem"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 296 */           (List)ImmutableList.of(
/* 297 */             Pair.of(StructurePoolElement.legacy("village/common/iron_golem"), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 302 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/common/well_bottoms"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 305 */           (List)ImmutableList.of(
/* 306 */             Pair.of(StructurePoolElement.legacy("village/common/well_bottom"), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */   }
/*     */   
/*     */   public static void bootstrap() {}
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\worldgen\PlainVillagePools.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */