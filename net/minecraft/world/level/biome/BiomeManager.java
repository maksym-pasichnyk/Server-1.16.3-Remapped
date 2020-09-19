/*    */ package net.minecraft.world.level.biome;
/*    */ 
/*    */ import com.google.common.hash.Hashing;
/*    */ import net.minecraft.core.BlockPos;
/*    */ 
/*    */ public class BiomeManager
/*    */ {
/*    */   private final NoiseBiomeSource noiseBiomeSource;
/*    */   private final long biomeZoomSeed;
/*    */   private final BiomeZoomer zoomer;
/*    */   
/*    */   public BiomeManager(NoiseBiomeSource debug1, long debug2, BiomeZoomer debug4) {
/* 13 */     this.noiseBiomeSource = debug1;
/* 14 */     this.biomeZoomSeed = debug2;
/* 15 */     this.zoomer = debug4;
/*    */   }
/*    */   
/*    */   public static long obfuscateSeed(long debug0) {
/* 19 */     return Hashing.sha256().hashLong(debug0).asLong();
/*    */   }
/*    */   
/*    */   public BiomeManager withDifferentSource(BiomeSource debug1) {
/* 23 */     return new BiomeManager(debug1, this.biomeZoomSeed, this.zoomer);
/*    */   }
/*    */   
/*    */   public Biome getBiome(BlockPos debug1) {
/* 27 */     return this.zoomer.getBiome(this.biomeZoomSeed, debug1.getX(), debug1.getY(), debug1.getZ(), this.noiseBiomeSource);
/*    */   }
/*    */   
/*    */   public static interface NoiseBiomeSource {
/*    */     Biome getNoiseBiome(int param1Int1, int param1Int2, int param1Int3);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\biome\BiomeManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */