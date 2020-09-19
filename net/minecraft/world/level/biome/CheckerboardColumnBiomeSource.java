/*    */ package net.minecraft.world.level.biome;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public class CheckerboardColumnBiomeSource extends BiomeSource {
/*    */   static {
/* 10 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)Biome.LIST_CODEC.fieldOf("biomes").forGetter(()), (App)Codec.intRange(0, 62).fieldOf("scale").orElse(Integer.valueOf(2)).forGetter(())).apply((Applicative)debug0, CheckerboardColumnBiomeSource::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<CheckerboardColumnBiomeSource> CODEC;
/*    */   private final List<Supplier<Biome>> allowedBiomes;
/*    */   private final int bitShift;
/*    */   private final int size;
/*    */   
/*    */   public CheckerboardColumnBiomeSource(List<Supplier<Biome>> debug1, int debug2) {
/* 20 */     super(debug1.stream());
/* 21 */     this.allowedBiomes = debug1;
/* 22 */     this.bitShift = debug2 + 2;
/* 23 */     this.size = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Codec<? extends BiomeSource> codec() {
/* 28 */     return (Codec)CODEC;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Biome getNoiseBiome(int debug1, int debug2, int debug3) {
/* 38 */     return ((Supplier<Biome>)this.allowedBiomes.get(Math.floorMod((debug1 >> this.bitShift) + (debug3 >> this.bitShift), this.allowedBiomes.size()))).get();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\biome\CheckerboardColumnBiomeSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */