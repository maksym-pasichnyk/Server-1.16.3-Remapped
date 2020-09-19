/*    */ package net.minecraft.world.level.material;
/*    */ 
/*    */ public class MaterialColor {
/*  4 */   public static final MaterialColor[] MATERIAL_COLORS = new MaterialColor[64];
/*    */   
/*  6 */   public static final MaterialColor NONE = new MaterialColor(0, 0);
/*  7 */   public static final MaterialColor GRASS = new MaterialColor(1, 8368696);
/*  8 */   public static final MaterialColor SAND = new MaterialColor(2, 16247203);
/*  9 */   public static final MaterialColor WOOL = new MaterialColor(3, 13092807);
/* 10 */   public static final MaterialColor FIRE = new MaterialColor(4, 16711680);
/* 11 */   public static final MaterialColor ICE = new MaterialColor(5, 10526975);
/* 12 */   public static final MaterialColor METAL = new MaterialColor(6, 10987431);
/* 13 */   public static final MaterialColor PLANT = new MaterialColor(7, 31744);
/* 14 */   public static final MaterialColor SNOW = new MaterialColor(8, 16777215);
/* 15 */   public static final MaterialColor CLAY = new MaterialColor(9, 10791096);
/* 16 */   public static final MaterialColor DIRT = new MaterialColor(10, 9923917);
/* 17 */   public static final MaterialColor STONE = new MaterialColor(11, 7368816);
/* 18 */   public static final MaterialColor WATER = new MaterialColor(12, 4210943);
/* 19 */   public static final MaterialColor WOOD = new MaterialColor(13, 9402184);
/* 20 */   public static final MaterialColor QUARTZ = new MaterialColor(14, 16776437);
/* 21 */   public static final MaterialColor COLOR_ORANGE = new MaterialColor(15, 14188339);
/* 22 */   public static final MaterialColor COLOR_MAGENTA = new MaterialColor(16, 11685080);
/* 23 */   public static final MaterialColor COLOR_LIGHT_BLUE = new MaterialColor(17, 6724056);
/* 24 */   public static final MaterialColor COLOR_YELLOW = new MaterialColor(18, 15066419);
/* 25 */   public static final MaterialColor COLOR_LIGHT_GREEN = new MaterialColor(19, 8375321);
/* 26 */   public static final MaterialColor COLOR_PINK = new MaterialColor(20, 15892389);
/* 27 */   public static final MaterialColor COLOR_GRAY = new MaterialColor(21, 5000268);
/* 28 */   public static final MaterialColor COLOR_LIGHT_GRAY = new MaterialColor(22, 10066329);
/* 29 */   public static final MaterialColor COLOR_CYAN = new MaterialColor(23, 5013401);
/* 30 */   public static final MaterialColor COLOR_PURPLE = new MaterialColor(24, 8339378);
/* 31 */   public static final MaterialColor COLOR_BLUE = new MaterialColor(25, 3361970);
/* 32 */   public static final MaterialColor COLOR_BROWN = new MaterialColor(26, 6704179);
/* 33 */   public static final MaterialColor COLOR_GREEN = new MaterialColor(27, 6717235);
/* 34 */   public static final MaterialColor COLOR_RED = new MaterialColor(28, 10040115);
/* 35 */   public static final MaterialColor COLOR_BLACK = new MaterialColor(29, 1644825);
/* 36 */   public static final MaterialColor GOLD = new MaterialColor(30, 16445005);
/* 37 */   public static final MaterialColor DIAMOND = new MaterialColor(31, 6085589);
/* 38 */   public static final MaterialColor LAPIS = new MaterialColor(32, 4882687);
/* 39 */   public static final MaterialColor EMERALD = new MaterialColor(33, 55610);
/* 40 */   public static final MaterialColor PODZOL = new MaterialColor(34, 8476209);
/* 41 */   public static final MaterialColor NETHER = new MaterialColor(35, 7340544);
/*    */   
/* 43 */   public static final MaterialColor TERRACOTTA_WHITE = new MaterialColor(36, 13742497);
/* 44 */   public static final MaterialColor TERRACOTTA_ORANGE = new MaterialColor(37, 10441252);
/* 45 */   public static final MaterialColor TERRACOTTA_MAGENTA = new MaterialColor(38, 9787244);
/* 46 */   public static final MaterialColor TERRACOTTA_LIGHT_BLUE = new MaterialColor(39, 7367818);
/* 47 */   public static final MaterialColor TERRACOTTA_YELLOW = new MaterialColor(40, 12223780);
/* 48 */   public static final MaterialColor TERRACOTTA_LIGHT_GREEN = new MaterialColor(41, 6780213);
/* 49 */   public static final MaterialColor TERRACOTTA_PINK = new MaterialColor(42, 10505550);
/* 50 */   public static final MaterialColor TERRACOTTA_GRAY = new MaterialColor(43, 3746083);
/* 51 */   public static final MaterialColor TERRACOTTA_LIGHT_GRAY = new MaterialColor(44, 8874850);
/* 52 */   public static final MaterialColor TERRACOTTA_CYAN = new MaterialColor(45, 5725276);
/* 53 */   public static final MaterialColor TERRACOTTA_PURPLE = new MaterialColor(46, 8014168);
/* 54 */   public static final MaterialColor TERRACOTTA_BLUE = new MaterialColor(47, 4996700);
/* 55 */   public static final MaterialColor TERRACOTTA_BROWN = new MaterialColor(48, 4993571);
/* 56 */   public static final MaterialColor TERRACOTTA_GREEN = new MaterialColor(49, 5001770);
/* 57 */   public static final MaterialColor TERRACOTTA_RED = new MaterialColor(50, 9321518);
/* 58 */   public static final MaterialColor TERRACOTTA_BLACK = new MaterialColor(51, 2430480);
/*    */   
/* 60 */   public static final MaterialColor CRIMSON_NYLIUM = new MaterialColor(52, 12398641);
/* 61 */   public static final MaterialColor CRIMSON_STEM = new MaterialColor(53, 9715553);
/* 62 */   public static final MaterialColor CRIMSON_HYPHAE = new MaterialColor(54, 6035741);
/* 63 */   public static final MaterialColor WARPED_NYLIUM = new MaterialColor(55, 1474182);
/* 64 */   public static final MaterialColor WARPED_STEM = new MaterialColor(56, 3837580);
/* 65 */   public static final MaterialColor WARPED_HYPHAE = new MaterialColor(57, 5647422);
/* 66 */   public static final MaterialColor WARPED_WART_BLOCK = new MaterialColor(58, 1356933);
/*    */   
/*    */   public final int col;
/*    */   public final int id;
/*    */   
/*    */   private MaterialColor(int debug1, int debug2) {
/* 72 */     if (debug1 < 0 || debug1 > 63) {
/* 73 */       throw new IndexOutOfBoundsException("Map colour ID must be between 0 and 63 (inclusive)");
/*    */     }
/* 75 */     this.id = debug1;
/* 76 */     this.col = debug2;
/* 77 */     MATERIAL_COLORS[debug1] = this;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\material\MaterialColor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */