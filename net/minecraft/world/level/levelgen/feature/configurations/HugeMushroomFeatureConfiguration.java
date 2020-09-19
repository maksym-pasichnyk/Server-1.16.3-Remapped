/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ 
/*    */ public class HugeMushroomFeatureConfiguration implements FeatureConfiguration {
/*    */   static {
/*  8 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)BlockStateProvider.CODEC.fieldOf("cap_provider").forGetter(()), (App)BlockStateProvider.CODEC.fieldOf("stem_provider").forGetter(()), (App)Codec.INT.fieldOf("foliage_radius").orElse(Integer.valueOf(2)).forGetter(())).apply((Applicative)debug0, HugeMushroomFeatureConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<HugeMushroomFeatureConfiguration> CODEC;
/*    */   
/*    */   public final BlockStateProvider capProvider;
/*    */   public final BlockStateProvider stemProvider;
/*    */   public final int foliageRadius;
/*    */   
/*    */   public HugeMushroomFeatureConfiguration(BlockStateProvider debug1, BlockStateProvider debug2, int debug3) {
/* 19 */     this.capProvider = debug1;
/* 20 */     this.stemProvider = debug2;
/* 21 */     this.foliageRadius = debug3;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\HugeMushroomFeatureConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */