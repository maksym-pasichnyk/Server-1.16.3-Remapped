/*    */ package net.minecraft.world.level.levelgen.feature.featuresize;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.OptionalInt;
/*    */ 
/*    */ public class TwoLayersFeatureSize extends FeatureSize {
/*    */   static {
/* 11 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)Codec.intRange(0, 81).fieldOf("limit").orElse(Integer.valueOf(1)).forGetter(()), (App)Codec.intRange(0, 16).fieldOf("lower_size").orElse(Integer.valueOf(0)).forGetter(()), (App)Codec.intRange(0, 16).fieldOf("upper_size").orElse(Integer.valueOf(1)).forGetter(()), (App)minClippedHeightCodec()).apply((Applicative)debug0, TwoLayersFeatureSize::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<TwoLayersFeatureSize> CODEC;
/*    */   
/*    */   private final int limit;
/*    */   
/*    */   private final int lowerSize;
/*    */   
/*    */   private final int upperSize;
/*    */   
/*    */   public TwoLayersFeatureSize(int debug1, int debug2, int debug3) {
/* 24 */     this(debug1, debug2, debug3, OptionalInt.empty());
/*    */   }
/*    */   
/*    */   public TwoLayersFeatureSize(int debug1, int debug2, int debug3, OptionalInt debug4) {
/* 28 */     super(debug4);
/* 29 */     this.limit = debug1;
/* 30 */     this.lowerSize = debug2;
/* 31 */     this.upperSize = debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   protected FeatureSizeType<?> type() {
/* 36 */     return FeatureSizeType.TWO_LAYERS_FEATURE_SIZE;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getSizeAtHeight(int debug1, int debug2) {
/* 41 */     return (debug2 < this.limit) ? this.lowerSize : this.upperSize;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\featuresize\TwoLayersFeatureSize.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */