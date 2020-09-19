/*    */ package net.minecraft.world.level.newbiome.layer;
/*    */ 
/*    */ import net.minecraft.world.level.newbiome.context.Context;
/*    */ import net.minecraft.world.level.newbiome.layer.traits.C0Transformer;
/*    */ import net.minecraft.world.level.newbiome.layer.traits.CastleTransformer;
/*    */ 
/*    */ public class AddEdgeLayer {
/*    */   public enum CoolWarm implements CastleTransformer {
/*  9 */     INSTANCE;
/*    */ 
/*    */     
/*    */     public int apply(Context debug1, int debug2, int debug3, int debug4, int debug5, int debug6) {
/* 13 */       if (debug6 == 1 && (debug2 == 3 || debug3 == 3 || debug5 == 3 || debug4 == 3 || debug2 == 4 || debug3 == 4 || debug5 == 4 || debug4 == 4))
/*    */       {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 23 */         return 2;
/*    */       }
/*    */       
/* 26 */       return debug6;
/*    */     }
/*    */   }
/*    */   
/*    */   public enum HeatIce implements CastleTransformer {
/* 31 */     INSTANCE;
/*    */ 
/*    */     
/*    */     public int apply(Context debug1, int debug2, int debug3, int debug4, int debug5, int debug6) {
/* 35 */       if (debug6 == 4 && (debug2 == 1 || debug3 == 1 || debug5 == 1 || debug4 == 1 || debug2 == 2 || debug3 == 2 || debug5 == 2 || debug4 == 2))
/*    */       {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 45 */         return 3;
/*    */       }
/*    */       
/* 48 */       return debug6;
/*    */     }
/*    */   }
/*    */   
/*    */   public enum IntroduceSpecial implements C0Transformer {
/* 53 */     INSTANCE;
/*    */ 
/*    */     
/*    */     public int apply(Context debug1, int debug2) {
/* 57 */       if (!Layers.isShallowOcean(debug2) && debug1.nextRandom(13) == 0) {
/* 58 */         debug2 |= 1 + debug1.nextRandom(15) << 8 & 0xF00;
/*    */       }
/*    */       
/* 61 */       return debug2;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\newbiome\layer\AddEdgeLayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */