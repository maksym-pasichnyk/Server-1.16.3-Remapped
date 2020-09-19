/*    */ package net.minecraft.world.level.newbiome.layer;
/*    */ 
/*    */ import net.minecraft.world.level.newbiome.context.Context;
/*    */ import net.minecraft.world.level.newbiome.layer.traits.C0Transformer;
/*    */ 
/*    */ public class BiomeInitLayer implements C0Transformer {
/*  7 */   private static final int[] LEGACY_WARM_BIOMES = new int[] { 2, 4, 3, 6, 1, 5 };
/*  8 */   private static final int[] WARM_BIOMES = new int[] { 2, 2, 2, 35, 35, 1 };
/*  9 */   private static final int[] MEDIUM_BIOMES = new int[] { 4, 29, 3, 1, 27, 6 };
/* 10 */   private static final int[] COLD_BIOMES = new int[] { 4, 3, 5, 1 };
/* 11 */   private static final int[] ICE_BIOMES = new int[] { 12, 12, 12, 30 };
/*    */   
/* 13 */   private int[] warmBiomes = WARM_BIOMES;
/*    */   
/*    */   public BiomeInitLayer(boolean debug1) {
/* 16 */     if (debug1) {
/* 17 */       this.warmBiomes = LEGACY_WARM_BIOMES;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public int apply(Context debug1, int debug2) {
/* 23 */     int debug3 = (debug2 & 0xF00) >> 8;
/* 24 */     debug2 &= 0xFFFFF0FF;
/*    */     
/* 26 */     if (Layers.isOcean(debug2) || debug2 == 14) {
/* 27 */       return debug2;
/*    */     }
/*    */     
/* 30 */     switch (debug2) {
/*    */       case 1:
/* 32 */         if (debug3 > 0) {
/* 33 */           return (debug1.nextRandom(3) == 0) ? 39 : 38;
/*    */         }
/* 35 */         return this.warmBiomes[debug1.nextRandom(this.warmBiomes.length)];
/*    */       case 2:
/* 37 */         if (debug3 > 0) {
/* 38 */           return 21;
/*    */         }
/* 40 */         return MEDIUM_BIOMES[debug1.nextRandom(MEDIUM_BIOMES.length)];
/*    */       case 3:
/* 42 */         if (debug3 > 0) {
/* 43 */           return 32;
/*    */         }
/* 45 */         return COLD_BIOMES[debug1.nextRandom(COLD_BIOMES.length)];
/*    */       case 4:
/* 47 */         return ICE_BIOMES[debug1.nextRandom(ICE_BIOMES.length)];
/*    */     } 
/* 49 */     return 14;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\newbiome\layer\BiomeInitLayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */