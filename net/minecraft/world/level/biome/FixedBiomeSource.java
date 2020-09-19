/*    */ package net.minecraft.world.level.biome;
/*    */ import com.google.common.collect.Sets;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import java.util.Set;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.core.BlockPos;
/*    */ 
/*    */ public class FixedBiomeSource extends BiomeSource {
/*    */   public static final Codec<FixedBiomeSource> CODEC;
/*    */   
/*    */   static {
/* 15 */     CODEC = Biome.CODEC.fieldOf("biome").xmap(FixedBiomeSource::new, debug0 -> debug0.biome).stable().codec();
/*    */   }
/*    */   private final Supplier<Biome> biome;
/*    */   
/*    */   public FixedBiomeSource(Biome debug1) {
/* 20 */     this(() -> debug0);
/*    */   }
/*    */   
/*    */   public FixedBiomeSource(Supplier<Biome> debug1) {
/* 24 */     super((List<Biome>)ImmutableList.of(debug1.get()));
/* 25 */     this.biome = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Codec<? extends BiomeSource> codec() {
/* 30 */     return (Codec)CODEC;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Biome getNoiseBiome(int debug1, int debug2, int debug3) {
/* 40 */     return this.biome.get();
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public BlockPos findBiomeHorizontal(int debug1, int debug2, int debug3, int debug4, int debug5, Predicate<Biome> debug6, Random debug7, boolean debug8) {
/* 46 */     if (debug6.test(this.biome.get())) {
/* 47 */       if (debug8) {
/* 48 */         return new BlockPos(debug1, debug2, debug3);
/*    */       }
/* 50 */       return new BlockPos(debug1 - debug4 + debug7.nextInt(debug4 * 2 + 1), debug2, debug3 - debug4 + debug7.nextInt(debug4 * 2 + 1));
/*    */     } 
/*    */     
/* 53 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<Biome> getBiomesWithin(int debug1, int debug2, int debug3, int debug4) {
/* 58 */     return Sets.newHashSet((Object[])new Biome[] { this.biome.get() });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\biome\FixedBiomeSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */