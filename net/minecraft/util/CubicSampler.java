/*    */ package net.minecraft.util;
/*    */ 
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CubicSampler
/*    */ {
/* 11 */   private static final double[] GAUSSIAN_SAMPLE_KERNEL = new double[] { 0.0D, 1.0D, 4.0D, 6.0D, 4.0D, 1.0D, 0.0D };
/*    */   
/*    */   public static interface Vec3Fetcher {
/*    */     Vec3 fetch(int param1Int1, int param1Int2, int param1Int3);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\CubicSampler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */