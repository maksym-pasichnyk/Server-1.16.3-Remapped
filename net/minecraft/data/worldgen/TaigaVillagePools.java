/*     */ package net.minecraft.data.worldgen;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
/*     */ import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
/*     */ 
/*     */ public class TaigaVillagePools {
/*  10 */   public static final StructureTemplatePool START = Pools.register(new StructureTemplatePool(new ResourceLocation("village/taiga/town_centers"), new ResourceLocation("empty"), 
/*     */ 
/*     */         
/*  13 */         (List)ImmutableList.of(
/*  14 */           Pair.of(StructurePoolElement.legacy("village/taiga/town_centers/taiga_meeting_point_1", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(49)), 
/*  15 */           Pair.of(StructurePoolElement.legacy("village/taiga/town_centers/taiga_meeting_point_2", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(49)), 
/*  16 */           Pair.of(StructurePoolElement.legacy("village/taiga/zombie/town_centers/taiga_meeting_point_1", ProcessorLists.ZOMBIE_TAIGA), Integer.valueOf(1)), 
/*  17 */           Pair.of(StructurePoolElement.legacy("village/taiga/zombie/town_centers/taiga_meeting_point_2", ProcessorLists.ZOMBIE_TAIGA), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  23 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/taiga/streets"), new ResourceLocation("village/taiga/terminators"), 
/*     */ 
/*     */           
/*  26 */           (List)ImmutableList.of(
/*  27 */             Pair.of(StructurePoolElement.legacy("village/taiga/streets/corner_01", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(2)), 
/*  28 */             Pair.of(StructurePoolElement.legacy("village/taiga/streets/corner_02", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(2)), 
/*  29 */             Pair.of(StructurePoolElement.legacy("village/taiga/streets/corner_03", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(2)), 
/*  30 */             Pair.of(StructurePoolElement.legacy("village/taiga/streets/straight_01", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(4)), 
/*  31 */             Pair.of(StructurePoolElement.legacy("village/taiga/streets/straight_02", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(4)), 
/*  32 */             Pair.of(StructurePoolElement.legacy("village/taiga/streets/straight_03", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(4)), 
/*  33 */             Pair.of(StructurePoolElement.legacy("village/taiga/streets/straight_04", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(7)), 
/*  34 */             Pair.of(StructurePoolElement.legacy("village/taiga/streets/straight_05", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(7)), 
/*  35 */             Pair.of(StructurePoolElement.legacy("village/taiga/streets/straight_06", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(4)), 
/*  36 */             Pair.of(StructurePoolElement.legacy("village/taiga/streets/crossroad_01", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(1)), 
/*  37 */             Pair.of(StructurePoolElement.legacy("village/taiga/streets/crossroad_02", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(1)), 
/*  38 */             Pair.of(StructurePoolElement.legacy("village/taiga/streets/crossroad_03", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(2)), (Object[])new Pair[] {
/*  39 */               Pair.of(StructurePoolElement.legacy("village/taiga/streets/crossroad_04", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(2)), 
/*  40 */               Pair.of(StructurePoolElement.legacy("village/taiga/streets/crossroad_05", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(2)), 
/*  41 */               Pair.of(StructurePoolElement.legacy("village/taiga/streets/crossroad_06", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(2)), 
/*  42 */               Pair.of(StructurePoolElement.legacy("village/taiga/streets/turn_01", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(3))
/*     */             }), StructureTemplatePool.Projection.TERRAIN_MATCHING));
/*     */ 
/*     */ 
/*     */     
/*  47 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/taiga/zombie/streets"), new ResourceLocation("village/taiga/terminators"), 
/*     */ 
/*     */           
/*  50 */           (List)ImmutableList.of(
/*  51 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/corner_01", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(2)), 
/*  52 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/corner_02", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(2)), 
/*  53 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/corner_03", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(2)), 
/*  54 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/straight_01", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(4)), 
/*  55 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/straight_02", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(4)), 
/*  56 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/straight_03", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(4)), 
/*  57 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/straight_04", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(7)), 
/*  58 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/straight_05", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(7)), 
/*  59 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/straight_06", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(4)), 
/*  60 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/crossroad_01", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(1)), 
/*  61 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/crossroad_02", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(1)), 
/*  62 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/crossroad_03", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(2)), (Object[])new Pair[] {
/*  63 */               Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/crossroad_04", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(2)), 
/*  64 */               Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/crossroad_05", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(2)), 
/*  65 */               Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/crossroad_06", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(2)), 
/*  66 */               Pair.of(StructurePoolElement.legacy("village/taiga/zombie/streets/turn_01", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(3))
/*     */             }), StructureTemplatePool.Projection.TERRAIN_MATCHING));
/*     */ 
/*     */ 
/*     */     
/*  71 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/taiga/houses"), new ResourceLocation("village/taiga/terminators"), 
/*     */ 
/*     */           
/*  74 */           (List)ImmutableList.of(
/*  75 */             Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_small_house_1", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(4)), 
/*  76 */             Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_small_house_2", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(4)), 
/*  77 */             Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_small_house_3", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(4)), 
/*  78 */             Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_small_house_4", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(4)), 
/*  79 */             Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_small_house_5", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(4)), 
/*  80 */             Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_medium_house_1", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(2)), 
/*  81 */             Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_medium_house_2", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(2)), 
/*  82 */             Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_medium_house_3", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(2)), 
/*  83 */             Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_medium_house_4", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(2)), 
/*  84 */             Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_butcher_shop_1", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(2)), 
/*  85 */             Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_tool_smith_1", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(2)), 
/*  86 */             Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_fletcher_house_1", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(2)), (Object[])new Pair[] { 
/*  87 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_shepherds_house_1", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(2)), 
/*  88 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_armorer_house_1", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(1)), 
/*  89 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_armorer_2", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(1)), 
/*  90 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_fisher_cottage_1", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(3)), 
/*  91 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_tannery_1", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(2)), 
/*  92 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_cartographer_house_1", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(2)), 
/*  93 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_library_1", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(2)), 
/*  94 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_masons_house_1", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(2)), 
/*  95 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_weaponsmith_1", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(2)), 
/*  96 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_weaponsmith_2", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(2)), 
/*  97 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_temple_1", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(2)), 
/*  98 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_large_farm_1", ProcessorLists.FARM_TAIGA), Integer.valueOf(6)), 
/*  99 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_large_farm_2", ProcessorLists.FARM_TAIGA), Integer.valueOf(6)), 
/* 100 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_small_farm_1", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(1)), 
/* 101 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_animal_pen_1", ProcessorLists.MOSSIFY_10_PERCENT), Integer.valueOf(2)), 
/* 102 */               Pair.of(StructurePoolElement.empty(), Integer.valueOf(6)) }), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 107 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/taiga/zombie/houses"), new ResourceLocation("village/taiga/terminators"), 
/*     */ 
/*     */           
/* 110 */           (List)ImmutableList.of(
/* 111 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_small_house_1", ProcessorLists.ZOMBIE_TAIGA), Integer.valueOf(4)), 
/* 112 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_small_house_2", ProcessorLists.ZOMBIE_TAIGA), Integer.valueOf(4)), 
/* 113 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_small_house_3", ProcessorLists.ZOMBIE_TAIGA), Integer.valueOf(4)), 
/* 114 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_small_house_4", ProcessorLists.ZOMBIE_TAIGA), Integer.valueOf(4)), 
/* 115 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_small_house_5", ProcessorLists.ZOMBIE_TAIGA), Integer.valueOf(4)), 
/* 116 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_medium_house_1", ProcessorLists.ZOMBIE_TAIGA), Integer.valueOf(2)), 
/* 117 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_medium_house_2", ProcessorLists.ZOMBIE_TAIGA), Integer.valueOf(2)), 
/* 118 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_medium_house_3", ProcessorLists.ZOMBIE_TAIGA), Integer.valueOf(2)), 
/* 119 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_medium_house_4", ProcessorLists.ZOMBIE_TAIGA), Integer.valueOf(2)), 
/* 120 */             Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_butcher_shop_1", ProcessorLists.ZOMBIE_TAIGA), Integer.valueOf(2)), 
/* 121 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_tool_smith_1", ProcessorLists.ZOMBIE_TAIGA), Integer.valueOf(2)), 
/* 122 */             Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_fletcher_house_1", ProcessorLists.ZOMBIE_TAIGA), Integer.valueOf(2)), (Object[])new Pair[] { 
/* 123 */               Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_shepherds_house_1", ProcessorLists.ZOMBIE_TAIGA), Integer.valueOf(2)), 
/* 124 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_armorer_house_1", ProcessorLists.ZOMBIE_TAIGA), Integer.valueOf(1)), 
/* 125 */               Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_fisher_cottage_1", ProcessorLists.ZOMBIE_TAIGA), Integer.valueOf(2)), 
/* 126 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_tannery_1", ProcessorLists.ZOMBIE_TAIGA), Integer.valueOf(2)), 
/* 127 */               Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_cartographer_house_1", ProcessorLists.ZOMBIE_TAIGA), Integer.valueOf(2)), 
/* 128 */               Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_library_1", ProcessorLists.ZOMBIE_TAIGA), Integer.valueOf(2)), 
/* 129 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_masons_house_1", ProcessorLists.ZOMBIE_TAIGA), Integer.valueOf(2)), 
/* 130 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_weaponsmith_1", ProcessorLists.ZOMBIE_TAIGA), Integer.valueOf(2)), 
/* 131 */               Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_weaponsmith_2", ProcessorLists.ZOMBIE_TAIGA), Integer.valueOf(2)), 
/* 132 */               Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_temple_1", ProcessorLists.ZOMBIE_TAIGA), Integer.valueOf(2)), 
/* 133 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_large_farm_1", ProcessorLists.ZOMBIE_TAIGA), Integer.valueOf(6)), 
/* 134 */               Pair.of(StructurePoolElement.legacy("village/taiga/zombie/houses/taiga_large_farm_2", ProcessorLists.ZOMBIE_TAIGA), Integer.valueOf(6)), 
/* 135 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_small_farm_1", ProcessorLists.ZOMBIE_TAIGA), Integer.valueOf(1)), 
/* 136 */               Pair.of(StructurePoolElement.legacy("village/taiga/houses/taiga_animal_pen_1", ProcessorLists.ZOMBIE_TAIGA), Integer.valueOf(2)), 
/* 137 */               Pair.of(StructurePoolElement.empty(), Integer.valueOf(6)) }), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 142 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/taiga/terminators"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 145 */           (List)ImmutableList.of(
/* 146 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_01", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(1)), 
/* 147 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_02", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(1)), 
/* 148 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_03", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(1)), 
/* 149 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_04", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(1))), StructureTemplatePool.Projection.TERRAIN_MATCHING));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 154 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/taiga/decor"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 157 */           (List)ImmutableList.of(
/* 158 */             Pair.of(StructurePoolElement.legacy("village/taiga/taiga_lamp_post_1"), Integer.valueOf(10)), 
/* 159 */             Pair.of(StructurePoolElement.legacy("village/taiga/taiga_decoration_1"), Integer.valueOf(4)), 
/* 160 */             Pair.of(StructurePoolElement.legacy("village/taiga/taiga_decoration_2"), Integer.valueOf(1)), 
/* 161 */             Pair.of(StructurePoolElement.legacy("village/taiga/taiga_decoration_3"), Integer.valueOf(1)), 
/* 162 */             Pair.of(StructurePoolElement.legacy("village/taiga/taiga_decoration_4"), Integer.valueOf(1)), 
/* 163 */             Pair.of(StructurePoolElement.legacy("village/taiga/taiga_decoration_5"), Integer.valueOf(2)), 
/* 164 */             Pair.of(StructurePoolElement.legacy("village/taiga/taiga_decoration_6"), Integer.valueOf(1)), 
/* 165 */             Pair.of(StructurePoolElement.feature(Features.SPRUCE), Integer.valueOf(4)), 
/* 166 */             Pair.of(StructurePoolElement.feature(Features.PINE), Integer.valueOf(4)), 
/* 167 */             Pair.of(StructurePoolElement.feature(Features.PILE_PUMPKIN), Integer.valueOf(2)), 
/* 168 */             Pair.of(StructurePoolElement.feature(Features.PATCH_TAIGA_GRASS), Integer.valueOf(4)), 
/* 169 */             Pair.of(StructurePoolElement.feature(Features.PATCH_BERRY_BUSH), Integer.valueOf(1)), (Object[])new Pair[] {
/* 170 */               Pair.of(StructurePoolElement.empty(), Integer.valueOf(4))
/*     */             }), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */     
/* 175 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/taiga/zombie/decor"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 178 */           (List)ImmutableList.of(
/* 179 */             Pair.of(StructurePoolElement.legacy("village/taiga/taiga_decoration_1"), Integer.valueOf(4)), 
/* 180 */             Pair.of(StructurePoolElement.legacy("village/taiga/taiga_decoration_2"), Integer.valueOf(1)), 
/* 181 */             Pair.of(StructurePoolElement.legacy("village/taiga/taiga_decoration_3"), Integer.valueOf(1)), 
/* 182 */             Pair.of(StructurePoolElement.legacy("village/taiga/taiga_decoration_4"), Integer.valueOf(1)), 
/* 183 */             Pair.of(StructurePoolElement.feature(Features.SPRUCE), Integer.valueOf(4)), 
/* 184 */             Pair.of(StructurePoolElement.feature(Features.PINE), Integer.valueOf(4)), 
/* 185 */             Pair.of(StructurePoolElement.feature(Features.PILE_PUMPKIN), Integer.valueOf(2)), 
/* 186 */             Pair.of(StructurePoolElement.feature(Features.PATCH_TAIGA_GRASS), Integer.valueOf(4)), 
/* 187 */             Pair.of(StructurePoolElement.feature(Features.PATCH_BERRY_BUSH), Integer.valueOf(1)), 
/* 188 */             Pair.of(StructurePoolElement.empty(), Integer.valueOf(4))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 193 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/taiga/villagers"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 196 */           (List)ImmutableList.of(
/* 197 */             Pair.of(StructurePoolElement.legacy("village/taiga/villagers/nitwit"), Integer.valueOf(1)), 
/* 198 */             Pair.of(StructurePoolElement.legacy("village/taiga/villagers/baby"), Integer.valueOf(1)), 
/* 199 */             Pair.of(StructurePoolElement.legacy("village/taiga/villagers/unemployed"), Integer.valueOf(10))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 204 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/taiga/zombie/villagers"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 207 */           (List)ImmutableList.of(
/* 208 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/villagers/nitwit"), Integer.valueOf(1)), 
/* 209 */             Pair.of(StructurePoolElement.legacy("village/taiga/zombie/villagers/unemployed"), Integer.valueOf(10))), StructureTemplatePool.Projection.RIGID));
/*     */   }
/*     */   
/*     */   public static void bootstrap() {}
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\worldgen\TaigaVillagePools.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */