/*     */ package net.minecraft.world.level.newbiome.layer;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.ints.Int2IntMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
/*     */ import java.util.function.LongFunction;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.world.level.newbiome.area.AreaFactory;
/*     */ import net.minecraft.world.level.newbiome.area.LazyArea;
/*     */ import net.minecraft.world.level.newbiome.context.BigContext;
/*     */ import net.minecraft.world.level.newbiome.context.LazyAreaContext;
/*     */ import net.minecraft.world.level.newbiome.layer.traits.AreaTransformer1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Layers
/*     */ {
/*     */   private static final Int2IntMap CATEGORIES;
/*     */   
/*     */   private static <T extends net.minecraft.world.level.newbiome.area.Area, C extends BigContext<T>> AreaFactory<T> zoom(long debug0, AreaTransformer1 debug2, AreaFactory<T> debug3, int debug4, LongFunction<C> debug5) {
/*  24 */     AreaFactory<T> debug6 = debug3;
/*  25 */     for (int debug7 = 0; debug7 < debug4; debug7++) {
/*  26 */       debug6 = debug2.run((BigContext)debug5.apply(debug0 + debug7), debug6);
/*     */     }
/*  28 */     return debug6;
/*     */   }
/*     */   
/*     */   private static <T extends net.minecraft.world.level.newbiome.area.Area, C extends BigContext<T>> AreaFactory<T> getDefaultLayer(boolean debug0, int debug1, int debug2, LongFunction<C> debug3) {
/*  32 */     AreaFactory<T> debug4 = IslandLayer.INSTANCE.run((BigContext)debug3.apply(1L));
/*  33 */     debug4 = ZoomLayer.FUZZY.run((BigContext)debug3.apply(2000L), debug4);
/*  34 */     debug4 = AddIslandLayer.INSTANCE.run((BigContext)debug3.apply(1L), debug4);
/*  35 */     debug4 = ZoomLayer.NORMAL.run((BigContext)debug3.apply(2001L), debug4);
/*  36 */     debug4 = AddIslandLayer.INSTANCE.run((BigContext)debug3.apply(2L), debug4);
/*  37 */     debug4 = AddIslandLayer.INSTANCE.run((BigContext)debug3.apply(50L), debug4);
/*  38 */     debug4 = AddIslandLayer.INSTANCE.run((BigContext)debug3.apply(70L), debug4);
/*  39 */     debug4 = RemoveTooMuchOceanLayer.INSTANCE.run((BigContext)debug3.apply(2L), debug4);
/*     */     
/*  41 */     AreaFactory<T> debug5 = OceanLayer.INSTANCE.run((BigContext)debug3.apply(2L));
/*  42 */     debug5 = zoom(2001L, ZoomLayer.NORMAL, debug5, 6, debug3);
/*     */     
/*  44 */     debug4 = AddSnowLayer.INSTANCE.run((BigContext)debug3.apply(2L), debug4);
/*  45 */     debug4 = AddIslandLayer.INSTANCE.run((BigContext)debug3.apply(3L), debug4);
/*  46 */     debug4 = AddEdgeLayer.CoolWarm.INSTANCE.run((BigContext)debug3.apply(2L), debug4);
/*  47 */     debug4 = AddEdgeLayer.HeatIce.INSTANCE.run((BigContext)debug3.apply(2L), debug4);
/*  48 */     debug4 = AddEdgeLayer.IntroduceSpecial.INSTANCE.run((BigContext)debug3.apply(3L), debug4);
/*  49 */     debug4 = ZoomLayer.NORMAL.run((BigContext)debug3.apply(2002L), debug4);
/*  50 */     debug4 = ZoomLayer.NORMAL.run((BigContext)debug3.apply(2003L), debug4);
/*  51 */     debug4 = AddIslandLayer.INSTANCE.run((BigContext)debug3.apply(4L), debug4);
/*  52 */     debug4 = AddMushroomIslandLayer.INSTANCE.run((BigContext)debug3.apply(5L), debug4);
/*  53 */     debug4 = AddDeepOceanLayer.INSTANCE.run((BigContext)debug3.apply(4L), debug4);
/*  54 */     debug4 = zoom(1000L, ZoomLayer.NORMAL, debug4, 0, debug3);
/*     */     
/*  56 */     AreaFactory<T> debug6 = debug4;
/*  57 */     debug6 = zoom(1000L, ZoomLayer.NORMAL, debug6, 0, debug3);
/*  58 */     debug6 = RiverInitLayer.INSTANCE.run((BigContext)debug3.apply(100L), debug6);
/*     */     
/*  60 */     AreaFactory<T> debug7 = debug4;
/*  61 */     debug7 = (new BiomeInitLayer(debug0)).run((BigContext)debug3.apply(200L), debug7);
/*  62 */     debug7 = RareBiomeLargeLayer.INSTANCE.run((BigContext)debug3.apply(1001L), debug7);
/*  63 */     debug7 = zoom(1000L, ZoomLayer.NORMAL, debug7, 2, debug3);
/*  64 */     debug7 = BiomeEdgeLayer.INSTANCE.run((BigContext)debug3.apply(1000L), debug7);
/*  65 */     AreaFactory<T> debug8 = debug6;
/*  66 */     debug8 = zoom(1000L, ZoomLayer.NORMAL, debug8, 2, debug3);
/*  67 */     debug7 = RegionHillsLayer.INSTANCE.run((BigContext)debug3.apply(1000L), debug7, debug8);
/*     */     
/*  69 */     debug6 = zoom(1000L, ZoomLayer.NORMAL, debug6, 2, debug3);
/*  70 */     debug6 = zoom(1000L, ZoomLayer.NORMAL, debug6, debug2, debug3);
/*  71 */     debug6 = RiverLayer.INSTANCE.run((BigContext)debug3.apply(1L), debug6);
/*  72 */     debug6 = SmoothLayer.INSTANCE.run((BigContext)debug3.apply(1000L), debug6);
/*     */     
/*  74 */     debug7 = RareBiomeSpotLayer.INSTANCE.run((BigContext)debug3.apply(1001L), debug7);
/*  75 */     for (int debug9 = 0; debug9 < debug1; debug9++) {
/*  76 */       debug7 = ZoomLayer.NORMAL.run((BigContext)debug3.apply((1000 + debug9)), debug7);
/*  77 */       if (debug9 == 0) {
/*  78 */         debug7 = AddIslandLayer.INSTANCE.run((BigContext)debug3.apply(3L), debug7);
/*     */       }
/*     */       
/*  81 */       if (debug9 == 1 || debug1 == 1) {
/*  82 */         debug7 = ShoreLayer.INSTANCE.run((BigContext)debug3.apply(1000L), debug7);
/*     */       }
/*     */     } 
/*     */     
/*  86 */     debug7 = SmoothLayer.INSTANCE.run((BigContext)debug3.apply(1000L), debug7);
/*     */     
/*  88 */     debug7 = RiverMixerLayer.INSTANCE.run((BigContext)debug3.apply(100L), debug7, debug6);
/*  89 */     debug7 = OceanMixerLayer.INSTANCE.run((BigContext)debug3.apply(100L), debug7, debug5);
/*     */     
/*  91 */     return debug7;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Layer getDefaultLayer(long debug0, boolean debug2, int debug3, int debug4) {
/*  96 */     int debug5 = 25;
/*  97 */     AreaFactory<LazyArea> debug6 = getDefaultLayer(debug2, debug3, debug4, debug2 -> new LazyAreaContext(25, debug0, debug2));
/*  98 */     return new Layer(debug6);
/*     */   }
/*     */   
/*     */   public static boolean isSame(int debug0, int debug1) {
/* 102 */     if (debug0 == debug1) {
/* 103 */       return true;
/*     */     }
/*     */     
/* 106 */     return (CATEGORIES.get(debug0) == CATEGORIES.get(debug1));
/*     */   }
/*     */   
/*     */   enum Category {
/* 110 */     NONE,
/* 111 */     TAIGA,
/* 112 */     EXTREME_HILLS,
/* 113 */     JUNGLE,
/* 114 */     MESA,
/* 115 */     BADLANDS_PLATEAU,
/* 116 */     PLAINS,
/* 117 */     SAVANNA,
/* 118 */     ICY,
/* 119 */     BEACH,
/* 120 */     FOREST,
/* 121 */     OCEAN,
/* 122 */     DESERT,
/* 123 */     RIVER,
/* 124 */     SWAMP,
/* 125 */     MUSHROOM;
/*     */   }
/*     */   
/*     */   static {
/* 129 */     CATEGORIES = (Int2IntMap)Util.make(new Int2IntOpenHashMap(), debug0 -> {
/*     */           register(debug0, Category.BEACH, 16);
/*     */           register(debug0, Category.BEACH, 26);
/*     */           register(debug0, Category.DESERT, 2);
/*     */           register(debug0, Category.DESERT, 17);
/*     */           register(debug0, Category.DESERT, 130);
/*     */           register(debug0, Category.EXTREME_HILLS, 131);
/*     */           register(debug0, Category.EXTREME_HILLS, 162);
/*     */           register(debug0, Category.EXTREME_HILLS, 20);
/*     */           register(debug0, Category.EXTREME_HILLS, 3);
/*     */           register(debug0, Category.EXTREME_HILLS, 34);
/*     */           register(debug0, Category.FOREST, 27);
/*     */           register(debug0, Category.FOREST, 28);
/*     */           register(debug0, Category.FOREST, 29);
/*     */           register(debug0, Category.FOREST, 157);
/*     */           register(debug0, Category.FOREST, 132);
/*     */           register(debug0, Category.FOREST, 4);
/*     */           register(debug0, Category.FOREST, 155);
/*     */           register(debug0, Category.FOREST, 156);
/*     */           register(debug0, Category.FOREST, 18);
/*     */           register(debug0, Category.ICY, 140);
/*     */           register(debug0, Category.ICY, 13);
/*     */           register(debug0, Category.ICY, 12);
/*     */           register(debug0, Category.JUNGLE, 168);
/*     */           register(debug0, Category.JUNGLE, 169);
/*     */           register(debug0, Category.JUNGLE, 21);
/*     */           register(debug0, Category.JUNGLE, 23);
/*     */           register(debug0, Category.JUNGLE, 22);
/*     */           register(debug0, Category.JUNGLE, 149);
/*     */           register(debug0, Category.JUNGLE, 151);
/*     */           register(debug0, Category.MESA, 37);
/*     */           register(debug0, Category.MESA, 165);
/*     */           register(debug0, Category.MESA, 167);
/*     */           register(debug0, Category.MESA, 166);
/*     */           register(debug0, Category.BADLANDS_PLATEAU, 39);
/*     */           register(debug0, Category.BADLANDS_PLATEAU, 38);
/*     */           register(debug0, Category.MUSHROOM, 14);
/*     */           register(debug0, Category.MUSHROOM, 15);
/*     */           register(debug0, Category.NONE, 25);
/*     */           register(debug0, Category.OCEAN, 46);
/*     */           register(debug0, Category.OCEAN, 49);
/*     */           register(debug0, Category.OCEAN, 50);
/*     */           register(debug0, Category.OCEAN, 48);
/*     */           register(debug0, Category.OCEAN, 24);
/*     */           register(debug0, Category.OCEAN, 47);
/*     */           register(debug0, Category.OCEAN, 10);
/*     */           register(debug0, Category.OCEAN, 45);
/*     */           register(debug0, Category.OCEAN, 0);
/*     */           register(debug0, Category.OCEAN, 44);
/*     */           register(debug0, Category.PLAINS, 1);
/*     */           register(debug0, Category.PLAINS, 129);
/*     */           register(debug0, Category.RIVER, 11);
/*     */           register(debug0, Category.RIVER, 7);
/*     */           register(debug0, Category.SAVANNA, 35);
/*     */           register(debug0, Category.SAVANNA, 36);
/*     */           register(debug0, Category.SAVANNA, 163);
/*     */           register(debug0, Category.SAVANNA, 164);
/*     */           register(debug0, Category.SWAMP, 6);
/*     */           register(debug0, Category.SWAMP, 134);
/*     */           register(debug0, Category.TAIGA, 160);
/*     */           register(debug0, Category.TAIGA, 161);
/*     */           register(debug0, Category.TAIGA, 32);
/*     */           register(debug0, Category.TAIGA, 33);
/*     */           register(debug0, Category.TAIGA, 30);
/*     */           register(debug0, Category.TAIGA, 31);
/*     */           register(debug0, Category.TAIGA, 158);
/*     */           register(debug0, Category.TAIGA, 5);
/*     */           register(debug0, Category.TAIGA, 19);
/*     */           register(debug0, Category.TAIGA, 133);
/*     */         });
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void register(Int2IntOpenHashMap debug0, Category debug1, int debug2) {
/* 216 */     debug0.put(debug2, debug1.ordinal());
/*     */   }
/*     */   
/*     */   protected static boolean isOcean(int debug0) {
/* 220 */     return (debug0 == 44 || debug0 == 45 || debug0 == 0 || debug0 == 46 || debug0 == 10 || debug0 == 47 || debug0 == 48 || debug0 == 24 || debug0 == 49 || debug0 == 50);
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
/*     */   protected static boolean isShallowOcean(int debug0) {
/* 234 */     return (debug0 == 44 || debug0 == 45 || debug0 == 0 || debug0 == 46 || debug0 == 10);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\newbiome\layer\Layers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */