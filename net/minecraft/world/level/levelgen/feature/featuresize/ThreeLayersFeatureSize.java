/*    */ package net.minecraft.world.level.levelgen.feature.featuresize;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function6;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.OptionalInt;
/*    */ 
/*    */ public class ThreeLayersFeatureSize extends FeatureSize {
/*    */   static {
/* 10 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)Codec.intRange(0, 80).fieldOf("limit").orElse(Integer.valueOf(1)).forGetter(()), (App)Codec.intRange(0, 80).fieldOf("upper_limit").orElse(Integer.valueOf(1)).forGetter(()), (App)Codec.intRange(0, 16).fieldOf("lower_size").orElse(Integer.valueOf(0)).forGetter(()), (App)Codec.intRange(0, 16).fieldOf("middle_size").orElse(Integer.valueOf(1)).forGetter(()), (App)Codec.intRange(0, 16).fieldOf("upper_size").orElse(Integer.valueOf(1)).forGetter(()), (App)minClippedHeightCodec()).apply((Applicative)debug0, ThreeLayersFeatureSize::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<ThreeLayersFeatureSize> CODEC;
/*    */   
/*    */   private final int limit;
/*    */   
/*    */   private final int upperLimit;
/*    */   
/*    */   private final int lowerSize;
/*    */   
/*    */   private final int middleSize;
/*    */   private final int upperSize;
/*    */   
/*    */   public ThreeLayersFeatureSize(int debug1, int debug2, int debug3, int debug4, int debug5, OptionalInt debug6) {
/* 26 */     super(debug6);
/* 27 */     this.limit = debug1;
/* 28 */     this.upperLimit = debug2;
/* 29 */     this.lowerSize = debug3;
/* 30 */     this.middleSize = debug4;
/* 31 */     this.upperSize = debug5;
/*    */   }
/*    */ 
/*    */   
/*    */   protected FeatureSizeType<?> type() {
/* 36 */     return FeatureSizeType.THREE_LAYERS_FEATURE_SIZE;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getSizeAtHeight(int debug1, int debug2) {
/* 41 */     if (debug2 < this.limit) {
/* 42 */       return this.lowerSize;
/*    */     }
/* 44 */     if (debug2 >= debug1 - this.upperLimit) {
/* 45 */       return this.upperSize;
/*    */     }
/* 47 */     return this.middleSize;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\featuresize\ThreeLayersFeatureSize.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */