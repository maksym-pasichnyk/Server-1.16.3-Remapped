/*     */ package net.minecraft.data.worldgen;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
/*     */ import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
/*     */ 
/*     */ public class SnowyVillagePools {
/*  10 */   public static final StructureTemplatePool START = Pools.register(new StructureTemplatePool(new ResourceLocation("village/snowy/town_centers"), new ResourceLocation("empty"), 
/*     */ 
/*     */         
/*  13 */         (List)ImmutableList.of(
/*  14 */           Pair.of(StructurePoolElement.legacy("village/snowy/town_centers/snowy_meeting_point_1"), Integer.valueOf(100)), 
/*  15 */           Pair.of(StructurePoolElement.legacy("village/snowy/town_centers/snowy_meeting_point_2"), Integer.valueOf(50)), 
/*  16 */           Pair.of(StructurePoolElement.legacy("village/snowy/town_centers/snowy_meeting_point_3"), Integer.valueOf(150)), 
/*  17 */           Pair.of(StructurePoolElement.legacy("village/snowy/zombie/town_centers/snowy_meeting_point_1"), Integer.valueOf(2)), 
/*  18 */           Pair.of(StructurePoolElement.legacy("village/snowy/zombie/town_centers/snowy_meeting_point_2"), Integer.valueOf(1)), 
/*  19 */           Pair.of(StructurePoolElement.legacy("village/snowy/zombie/town_centers/snowy_meeting_point_3"), Integer.valueOf(3))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  25 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/snowy/streets"), new ResourceLocation("village/snowy/terminators"), 
/*     */ 
/*     */           
/*  28 */           (List)ImmutableList.of(
/*  29 */             Pair.of(StructurePoolElement.legacy("village/snowy/streets/corner_01", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(2)), 
/*  30 */             Pair.of(StructurePoolElement.legacy("village/snowy/streets/corner_02", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(2)), 
/*  31 */             Pair.of(StructurePoolElement.legacy("village/snowy/streets/corner_03", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(2)), 
/*  32 */             Pair.of(StructurePoolElement.legacy("village/snowy/streets/square_01", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(2)), 
/*  33 */             Pair.of(StructurePoolElement.legacy("village/snowy/streets/straight_01", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(4)), 
/*  34 */             Pair.of(StructurePoolElement.legacy("village/snowy/streets/straight_02", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(4)), 
/*  35 */             Pair.of(StructurePoolElement.legacy("village/snowy/streets/straight_03", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(4)), 
/*  36 */             Pair.of(StructurePoolElement.legacy("village/snowy/streets/straight_04", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(7)), 
/*  37 */             Pair.of(StructurePoolElement.legacy("village/snowy/streets/straight_06", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(4)), 
/*  38 */             Pair.of(StructurePoolElement.legacy("village/snowy/streets/straight_08", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(4)), 
/*  39 */             Pair.of(StructurePoolElement.legacy("village/snowy/streets/crossroad_02", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(1)), 
/*  40 */             Pair.of(StructurePoolElement.legacy("village/snowy/streets/crossroad_03", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(2)), (Object[])new Pair[] {
/*  41 */               Pair.of(StructurePoolElement.legacy("village/snowy/streets/crossroad_04", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(2)), 
/*  42 */               Pair.of(StructurePoolElement.legacy("village/snowy/streets/crossroad_05", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(2)), 
/*  43 */               Pair.of(StructurePoolElement.legacy("village/snowy/streets/crossroad_06", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(2)), 
/*  44 */               Pair.of(StructurePoolElement.legacy("village/snowy/streets/turn_01", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(3))
/*     */             }), StructureTemplatePool.Projection.TERRAIN_MATCHING));
/*     */ 
/*     */ 
/*     */     
/*  49 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/snowy/zombie/streets"), new ResourceLocation("village/snowy/terminators"), 
/*     */ 
/*     */           
/*  52 */           (List)ImmutableList.of(
/*  53 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/streets/corner_01", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(2)), 
/*  54 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/streets/corner_02", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(2)), 
/*  55 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/streets/corner_03", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(2)), 
/*  56 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/streets/square_01", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(2)), 
/*  57 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/streets/straight_01", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(4)), 
/*  58 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/streets/straight_02", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(4)), 
/*  59 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/streets/straight_03", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(4)), 
/*  60 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/streets/straight_04", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(7)), 
/*  61 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/streets/straight_06", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(4)), 
/*  62 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/streets/straight_08", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(4)), 
/*  63 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/streets/crossroad_02", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(1)), 
/*  64 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/streets/crossroad_03", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(2)), (Object[])new Pair[] {
/*  65 */               Pair.of(StructurePoolElement.legacy("village/snowy/zombie/streets/crossroad_04", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(2)), 
/*  66 */               Pair.of(StructurePoolElement.legacy("village/snowy/zombie/streets/crossroad_05", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(2)), 
/*  67 */               Pair.of(StructurePoolElement.legacy("village/snowy/zombie/streets/crossroad_06", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(2)), 
/*  68 */               Pair.of(StructurePoolElement.legacy("village/snowy/zombie/streets/turn_01", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(3))
/*     */             }), StructureTemplatePool.Projection.TERRAIN_MATCHING));
/*     */ 
/*     */ 
/*     */     
/*  73 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/snowy/houses"), new ResourceLocation("village/snowy/terminators"), 
/*     */ 
/*     */           
/*  76 */           (List)ImmutableList.of(
/*  77 */             Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_small_house_1"), Integer.valueOf(2)), 
/*  78 */             Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_small_house_2"), Integer.valueOf(2)), 
/*  79 */             Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_small_house_3"), Integer.valueOf(2)), 
/*  80 */             Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_small_house_4"), Integer.valueOf(3)), 
/*  81 */             Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_small_house_5"), Integer.valueOf(2)), 
/*  82 */             Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_small_house_6"), Integer.valueOf(2)), 
/*  83 */             Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_small_house_7"), Integer.valueOf(2)), 
/*  84 */             Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_small_house_8"), Integer.valueOf(2)), 
/*  85 */             Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_medium_house_1"), Integer.valueOf(2)), 
/*  86 */             Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_medium_house_2"), Integer.valueOf(2)), 
/*  87 */             Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_medium_house_3"), Integer.valueOf(2)), 
/*  88 */             Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_butchers_shop_1"), Integer.valueOf(2)), (Object[])new Pair[] { 
/*  89 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_butchers_shop_2"), Integer.valueOf(2)), 
/*  90 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_tool_smith_1"), Integer.valueOf(2)), 
/*  91 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_fletcher_house_1"), Integer.valueOf(2)), 
/*  92 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_shepherds_house_1"), Integer.valueOf(3)), 
/*  93 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_armorer_house_1"), Integer.valueOf(1)), 
/*  94 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_armorer_house_2"), Integer.valueOf(1)), 
/*  95 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_fisher_cottage"), Integer.valueOf(2)), 
/*  96 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_tannery_1"), Integer.valueOf(2)), 
/*  97 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_cartographer_house_1"), Integer.valueOf(2)), 
/*  98 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_library_1"), Integer.valueOf(2)), 
/*  99 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_masons_house_1"), Integer.valueOf(2)), 
/* 100 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_masons_house_2"), Integer.valueOf(2)), 
/* 101 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_weapon_smith_1"), Integer.valueOf(2)), 
/* 102 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_temple_1"), Integer.valueOf(2)), 
/* 103 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_farm_1", ProcessorLists.FARM_SNOWY), Integer.valueOf(3)), 
/* 104 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_farm_2", ProcessorLists.FARM_SNOWY), Integer.valueOf(3)), 
/* 105 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_animal_pen_1"), Integer.valueOf(2)), 
/* 106 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_animal_pen_2"), Integer.valueOf(2)), 
/* 107 */               Pair.of(StructurePoolElement.empty(), Integer.valueOf(6)) }), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 112 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/snowy/zombie/houses"), new ResourceLocation("village/snowy/terminators"), 
/*     */ 
/*     */           
/* 115 */           (List)ImmutableList.of(
/* 116 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/houses/snowy_small_house_1", ProcessorLists.ZOMBIE_SNOWY), Integer.valueOf(2)), 
/* 117 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/houses/snowy_small_house_2", ProcessorLists.ZOMBIE_SNOWY), Integer.valueOf(2)), 
/* 118 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/houses/snowy_small_house_3", ProcessorLists.ZOMBIE_SNOWY), Integer.valueOf(2)), 
/* 119 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/houses/snowy_small_house_4", ProcessorLists.ZOMBIE_SNOWY), Integer.valueOf(2)), 
/* 120 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/houses/snowy_small_house_5", ProcessorLists.ZOMBIE_SNOWY), Integer.valueOf(2)), 
/* 121 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/houses/snowy_small_house_6", ProcessorLists.ZOMBIE_SNOWY), Integer.valueOf(2)), 
/* 122 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/houses/snowy_small_house_7", ProcessorLists.ZOMBIE_SNOWY), Integer.valueOf(2)), 
/* 123 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/houses/snowy_small_house_8", ProcessorLists.ZOMBIE_SNOWY), Integer.valueOf(2)), 
/* 124 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/houses/snowy_medium_house_1", ProcessorLists.ZOMBIE_SNOWY), Integer.valueOf(2)), 
/* 125 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/houses/snowy_medium_house_2", ProcessorLists.ZOMBIE_SNOWY), Integer.valueOf(2)), 
/* 126 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/houses/snowy_medium_house_3", ProcessorLists.ZOMBIE_SNOWY), Integer.valueOf(1)), 
/* 127 */             Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_butchers_shop_1", ProcessorLists.ZOMBIE_SNOWY), Integer.valueOf(2)), (Object[])new Pair[] { 
/* 128 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_butchers_shop_2", ProcessorLists.ZOMBIE_SNOWY), Integer.valueOf(2)), 
/* 129 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_tool_smith_1", ProcessorLists.ZOMBIE_SNOWY), Integer.valueOf(2)), 
/* 130 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_fletcher_house_1", ProcessorLists.ZOMBIE_SNOWY), Integer.valueOf(2)), 
/* 131 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_shepherds_house_1", ProcessorLists.ZOMBIE_SNOWY), Integer.valueOf(2)), 
/* 132 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_armorer_house_1", ProcessorLists.ZOMBIE_SNOWY), Integer.valueOf(1)), 
/* 133 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_armorer_house_2", ProcessorLists.ZOMBIE_SNOWY), Integer.valueOf(1)), 
/* 134 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_fisher_cottage", ProcessorLists.ZOMBIE_SNOWY), Integer.valueOf(2)), 
/* 135 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_tannery_1", ProcessorLists.ZOMBIE_SNOWY), Integer.valueOf(2)), 
/* 136 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_cartographer_house_1", ProcessorLists.ZOMBIE_SNOWY), Integer.valueOf(2)), 
/* 137 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_library_1", ProcessorLists.ZOMBIE_SNOWY), Integer.valueOf(2)), 
/* 138 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_masons_house_1", ProcessorLists.ZOMBIE_SNOWY), Integer.valueOf(2)), 
/* 139 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_masons_house_2", ProcessorLists.ZOMBIE_SNOWY), Integer.valueOf(2)), 
/* 140 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_weapon_smith_1", ProcessorLists.ZOMBIE_SNOWY), Integer.valueOf(2)), 
/* 141 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_temple_1", ProcessorLists.ZOMBIE_SNOWY), Integer.valueOf(2)), 
/* 142 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_farm_1", ProcessorLists.ZOMBIE_SNOWY), Integer.valueOf(3)), 
/* 143 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_farm_2", ProcessorLists.ZOMBIE_SNOWY), Integer.valueOf(3)), 
/* 144 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_animal_pen_1", ProcessorLists.ZOMBIE_SNOWY), Integer.valueOf(2)), 
/* 145 */               Pair.of(StructurePoolElement.legacy("village/snowy/houses/snowy_animal_pen_2", ProcessorLists.ZOMBIE_SNOWY), Integer.valueOf(2)), 
/* 146 */               Pair.of(StructurePoolElement.empty(), Integer.valueOf(6)) }), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 151 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/snowy/terminators"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 154 */           (List)ImmutableList.of(
/* 155 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_01", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(1)), 
/* 156 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_02", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(1)), 
/* 157 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_03", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(1)), 
/* 158 */             Pair.of(StructurePoolElement.legacy("village/plains/terminators/terminator_04", ProcessorLists.STREET_SNOWY_OR_TAIGA), Integer.valueOf(1))), StructureTemplatePool.Projection.TERRAIN_MATCHING));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 163 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/snowy/trees"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 166 */           (List)ImmutableList.of(
/* 167 */             Pair.of(StructurePoolElement.feature(Features.SPRUCE), Integer.valueOf(1))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 172 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/snowy/decor"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 175 */           (List)ImmutableList.of(
/* 176 */             Pair.of(StructurePoolElement.legacy("village/snowy/snowy_lamp_post_01"), Integer.valueOf(4)), 
/* 177 */             Pair.of(StructurePoolElement.legacy("village/snowy/snowy_lamp_post_02"), Integer.valueOf(4)), 
/* 178 */             Pair.of(StructurePoolElement.legacy("village/snowy/snowy_lamp_post_03"), Integer.valueOf(1)), 
/* 179 */             Pair.of(StructurePoolElement.feature(Features.SPRUCE), Integer.valueOf(4)), 
/* 180 */             Pair.of(StructurePoolElement.feature(Features.PILE_SNOW), Integer.valueOf(4)), 
/* 181 */             Pair.of(StructurePoolElement.feature(Features.PILE_ICE), Integer.valueOf(1)), 
/* 182 */             Pair.of(StructurePoolElement.empty(), Integer.valueOf(9))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 187 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/snowy/zombie/decor"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 190 */           (List)ImmutableList.of(
/* 191 */             Pair.of(StructurePoolElement.legacy("village/snowy/snowy_lamp_post_01", ProcessorLists.ZOMBIE_SNOWY), Integer.valueOf(1)), 
/* 192 */             Pair.of(StructurePoolElement.legacy("village/snowy/snowy_lamp_post_02", ProcessorLists.ZOMBIE_SNOWY), Integer.valueOf(1)), 
/* 193 */             Pair.of(StructurePoolElement.legacy("village/snowy/snowy_lamp_post_03", ProcessorLists.ZOMBIE_SNOWY), Integer.valueOf(1)), 
/* 194 */             Pair.of(StructurePoolElement.feature(Features.SPRUCE), Integer.valueOf(4)), 
/* 195 */             Pair.of(StructurePoolElement.feature(Features.PILE_SNOW), Integer.valueOf(4)), 
/* 196 */             Pair.of(StructurePoolElement.feature(Features.PILE_ICE), Integer.valueOf(4)), 
/* 197 */             Pair.of(StructurePoolElement.empty(), Integer.valueOf(7))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 202 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/snowy/villagers"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 205 */           (List)ImmutableList.of(
/* 206 */             Pair.of(StructurePoolElement.legacy("village/snowy/villagers/nitwit"), Integer.valueOf(1)), 
/* 207 */             Pair.of(StructurePoolElement.legacy("village/snowy/villagers/baby"), Integer.valueOf(1)), 
/* 208 */             Pair.of(StructurePoolElement.legacy("village/snowy/villagers/unemployed"), Integer.valueOf(10))), StructureTemplatePool.Projection.RIGID));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 213 */     Pools.register(new StructureTemplatePool(new ResourceLocation("village/snowy/zombie/villagers"), new ResourceLocation("empty"), 
/*     */ 
/*     */           
/* 216 */           (List)ImmutableList.of(
/* 217 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/villagers/nitwit"), Integer.valueOf(1)), 
/* 218 */             Pair.of(StructurePoolElement.legacy("village/snowy/zombie/villagers/unemployed"), Integer.valueOf(10))), StructureTemplatePool.Projection.RIGID));
/*     */   }
/*     */   
/*     */   public static void bootstrap() {}
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\worldgen\SnowyVillagePools.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */