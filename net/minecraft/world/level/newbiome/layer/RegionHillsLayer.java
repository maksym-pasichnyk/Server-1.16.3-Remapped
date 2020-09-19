/*     */ package net.minecraft.world.level.newbiome.layer;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.ints.Int2IntMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.world.level.newbiome.area.Area;
/*     */ import net.minecraft.world.level.newbiome.context.Context;
/*     */ import net.minecraft.world.level.newbiome.layer.traits.AreaTransformer2;
/*     */ import net.minecraft.world.level.newbiome.layer.traits.DimensionOffset1Transformer;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public enum RegionHillsLayer implements AreaTransformer2, DimensionOffset1Transformer {
/*  14 */   INSTANCE; private static final Int2IntMap MUTATIONS;
/*     */   static {
/*  16 */     LOGGER = LogManager.getLogger();
/*     */     
/*  18 */     MUTATIONS = (Int2IntMap)Util.make(new Int2IntOpenHashMap(), debug0 -> {
/*     */           debug0.put(1, 129);
/*     */           debug0.put(2, 130);
/*     */           debug0.put(3, 131);
/*     */           debug0.put(4, 132);
/*     */           debug0.put(5, 133);
/*     */           debug0.put(6, 134);
/*     */           debug0.put(12, 140);
/*     */           debug0.put(21, 149);
/*     */           debug0.put(23, 151);
/*     */           debug0.put(27, 155);
/*     */           debug0.put(28, 156);
/*     */           debug0.put(29, 157);
/*     */           debug0.put(30, 158);
/*     */           debug0.put(32, 160);
/*     */           debug0.put(33, 161);
/*     */           debug0.put(34, 162);
/*     */           debug0.put(35, 163);
/*     */           debug0.put(36, 164);
/*     */           debug0.put(37, 165);
/*     */           debug0.put(38, 166);
/*     */           debug0.put(39, 167);
/*     */         });
/*     */   }
/*     */   private static final Logger LOGGER;
/*     */   public int applyPixel(Context debug1, Area debug2, Area debug3, int debug4, int debug5) {
/*  44 */     int debug6 = debug2.get(getParentX(debug4 + 1), getParentY(debug5 + 1));
/*  45 */     int debug7 = debug3.get(getParentX(debug4 + 1), getParentY(debug5 + 1));
/*     */     
/*  47 */     if (debug6 > 255) {
/*  48 */       LOGGER.debug("old! {}", Integer.valueOf(debug6));
/*     */     }
/*     */     
/*  51 */     int debug8 = (debug7 - 2) % 29;
/*  52 */     if (!Layers.isShallowOcean(debug6) && debug7 >= 2 && debug8 == 1) {
/*  53 */       return MUTATIONS.getOrDefault(debug6, debug6);
/*     */     }
/*     */     
/*  56 */     if (debug1.nextRandom(3) == 0 || debug8 == 0) {
/*  57 */       int debug9 = debug6;
/*  58 */       if (debug6 == 2) {
/*  59 */         debug9 = 17;
/*  60 */       } else if (debug6 == 4) {
/*  61 */         debug9 = 18;
/*  62 */       } else if (debug6 == 27) {
/*  63 */         debug9 = 28;
/*  64 */       } else if (debug6 == 29) {
/*  65 */         debug9 = 1;
/*  66 */       } else if (debug6 == 5) {
/*  67 */         debug9 = 19;
/*  68 */       } else if (debug6 == 32) {
/*  69 */         debug9 = 33;
/*  70 */       } else if (debug6 == 30) {
/*  71 */         debug9 = 31;
/*  72 */       } else if (debug6 == 1) {
/*  73 */         debug9 = (debug1.nextRandom(3) == 0) ? 18 : 4;
/*  74 */       } else if (debug6 == 12) {
/*  75 */         debug9 = 13;
/*  76 */       } else if (debug6 == 21) {
/*  77 */         debug9 = 22;
/*  78 */       } else if (debug6 == 168) {
/*  79 */         debug9 = 169;
/*  80 */       } else if (debug6 == 0) {
/*  81 */         debug9 = 24;
/*  82 */       } else if (debug6 == 45) {
/*  83 */         debug9 = 48;
/*  84 */       } else if (debug6 == 46) {
/*  85 */         debug9 = 49;
/*  86 */       } else if (debug6 == 10) {
/*  87 */         debug9 = 50;
/*  88 */       } else if (debug6 == 3) {
/*  89 */         debug9 = 34;
/*  90 */       } else if (debug6 == 35) {
/*  91 */         debug9 = 36;
/*  92 */       } else if (Layers.isSame(debug6, 38)) {
/*  93 */         debug9 = 37;
/*  94 */       } else if ((debug6 == 24 || debug6 == 48 || debug6 == 49 || debug6 == 50) && 
/*  95 */         debug1.nextRandom(3) == 0) {
/*  96 */         debug9 = (debug1.nextRandom(2) == 0) ? 1 : 4;
/*     */       } 
/*     */       
/*  99 */       if (debug8 == 0 && debug9 != debug6) {
/* 100 */         debug9 = MUTATIONS.getOrDefault(debug9, debug6);
/*     */       }
/*     */       
/* 103 */       if (debug9 != debug6) {
/* 104 */         int debug10 = 0;
/* 105 */         if (Layers.isSame(debug2.get(getParentX(debug4 + 1), getParentY(debug5 + 0)), debug6)) {
/* 106 */           debug10++;
/*     */         }
/* 108 */         if (Layers.isSame(debug2.get(getParentX(debug4 + 2), getParentY(debug5 + 1)), debug6)) {
/* 109 */           debug10++;
/*     */         }
/* 111 */         if (Layers.isSame(debug2.get(getParentX(debug4 + 0), getParentY(debug5 + 1)), debug6)) {
/* 112 */           debug10++;
/*     */         }
/* 114 */         if (Layers.isSame(debug2.get(getParentX(debug4 + 1), getParentY(debug5 + 2)), debug6)) {
/* 115 */           debug10++;
/*     */         }
/* 117 */         if (debug10 >= 3) {
/* 118 */           return debug9;
/*     */         }
/*     */       } 
/*     */     } 
/* 122 */     return debug6;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\newbiome\layer\RegionHillsLayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */