/*    */ package net.minecraft.world.level.levelgen.feature.featuresize;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.OptionalInt;
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ 
/*    */ public abstract class FeatureSize
/*    */ {
/* 12 */   public static final Codec<FeatureSize> CODEC = Registry.FEATURE_SIZE_TYPES.dispatch(FeatureSize::type, FeatureSizeType::codec);
/*    */   protected final OptionalInt minClippedHeight;
/*    */   
/*    */   protected static <S extends FeatureSize> RecordCodecBuilder<S, OptionalInt> minClippedHeightCodec() {
/* 16 */     return Codec.intRange(0, 80).optionalFieldOf("min_clipped_height")
/* 17 */       .xmap(debug0 -> (OptionalInt)debug0.map(OptionalInt::of).orElse(OptionalInt.empty()), debug0 -> debug0.isPresent() ? Optional.<Integer>of(Integer.valueOf(debug0.getAsInt())) : Optional.empty()).forGetter(debug0 -> debug0.minClippedHeight);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public FeatureSize(OptionalInt debug1) {
/* 23 */     this.minClippedHeight = debug1;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public OptionalInt minClippedHeight() {
/* 31 */     return this.minClippedHeight;
/*    */   }
/*    */   
/*    */   protected abstract FeatureSizeType<?> type();
/*    */   
/*    */   public abstract int getSizeAtHeight(int paramInt1, int paramInt2);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\featuresize\FeatureSize.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */