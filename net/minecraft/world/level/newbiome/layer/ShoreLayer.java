/*    */ package net.minecraft.world.level.newbiome.layer;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
/*    */ import it.unimi.dsi.fastutil.ints.IntSet;
/*    */ import net.minecraft.world.level.newbiome.context.Context;
/*    */ import net.minecraft.world.level.newbiome.layer.traits.CastleTransformer;
/*    */ 
/*    */ public enum ShoreLayer implements CastleTransformer {
/*  9 */   INSTANCE;
/*    */   static {
/* 11 */     SNOWY = (IntSet)new IntOpenHashSet(new int[] { 26, 11, 12, 13, 140, 30, 31, 158, 10 });
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 23 */     JUNGLES = (IntSet)new IntOpenHashSet(new int[] { 168, 169, 21, 22, 23, 149, 151 });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static final IntSet JUNGLES;
/*    */ 
/*    */   
/*    */   private static final IntSet SNOWY;
/*    */ 
/*    */   
/*    */   public int apply(Context debug1, int debug2, int debug3, int debug4, int debug5, int debug6) {
/* 35 */     if (debug6 == 14) {
/* 36 */       if (Layers.isShallowOcean(debug2) || Layers.isShallowOcean(debug3) || Layers.isShallowOcean(debug4) || Layers.isShallowOcean(debug5)) {
/* 37 */         return 15;
/*    */       }
/* 39 */     } else if (JUNGLES.contains(debug6)) {
/* 40 */       if (!isJungleCompatible(debug2) || !isJungleCompatible(debug3) || !isJungleCompatible(debug4) || !isJungleCompatible(debug5))
/* 41 */         return 23; 
/* 42 */       if (Layers.isOcean(debug2) || Layers.isOcean(debug3) || Layers.isOcean(debug4) || Layers.isOcean(debug5)) {
/* 43 */         return 16;
/*    */       }
/* 45 */     } else if (debug6 == 3 || debug6 == 34 || debug6 == 20) {
/* 46 */       if (!Layers.isOcean(debug6) && (Layers.isOcean(debug2) || Layers.isOcean(debug3) || Layers.isOcean(debug4) || Layers.isOcean(debug5))) {
/* 47 */         return 25;
/*    */       }
/* 49 */     } else if (SNOWY.contains(debug6)) {
/* 50 */       if (!Layers.isOcean(debug6) && (Layers.isOcean(debug2) || Layers.isOcean(debug3) || Layers.isOcean(debug4) || Layers.isOcean(debug5))) {
/* 51 */         return 26;
/*    */       }
/* 53 */     } else if (debug6 == 37 || debug6 == 38) {
/* 54 */       if (!Layers.isOcean(debug2) && !Layers.isOcean(debug3) && !Layers.isOcean(debug4) && !Layers.isOcean(debug5) && (!isMesa(debug2) || !isMesa(debug3) || !isMesa(debug4) || !isMesa(debug5))) {
/* 55 */         return 2;
/*    */       }
/* 57 */     } else if (!Layers.isOcean(debug6) && debug6 != 7 && debug6 != 6 && (
/* 58 */       Layers.isOcean(debug2) || Layers.isOcean(debug3) || Layers.isOcean(debug4) || Layers.isOcean(debug5))) {
/* 59 */       return 16;
/*    */     } 
/*    */     
/* 62 */     return debug6;
/*    */   }
/*    */   
/*    */   private static boolean isJungleCompatible(int debug0) {
/* 66 */     return (JUNGLES.contains(debug0) || debug0 == 4 || debug0 == 5 || Layers.isOcean(debug0));
/*    */   }
/*    */   
/*    */   private boolean isMesa(int debug1) {
/* 70 */     return (debug1 == 37 || debug1 == 38 || debug1 == 39 || debug1 == 165 || debug1 == 166 || debug1 == 167);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\newbiome\layer\ShoreLayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */