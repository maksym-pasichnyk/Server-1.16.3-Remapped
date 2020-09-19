/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.levelgen.feature.SpikeFeature;
/*    */ 
/*    */ public class SpikeConfiguration implements FeatureConfiguration {
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)Codec.BOOL.fieldOf("crystal_invulnerable").orElse(Boolean.valueOf(false)).forGetter(()), (App)SpikeFeature.EndSpike.CODEC.listOf().fieldOf("spikes").forGetter(()), (App)BlockPos.CODEC.optionalFieldOf("crystal_beam_target").forGetter(())).apply((Applicative)debug0, SpikeConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<SpikeConfiguration> CODEC;
/*    */   
/*    */   private final boolean crystalInvulnerable;
/*    */   private final List<SpikeFeature.EndSpike> spikes;
/*    */   @Nullable
/*    */   private final BlockPos crystalBeamTarget;
/*    */   
/*    */   public SpikeConfiguration(boolean debug1, List<SpikeFeature.EndSpike> debug2, @Nullable BlockPos debug3) {
/* 25 */     this(debug1, debug2, Optional.ofNullable(debug3));
/*    */   }
/*    */   
/*    */   private SpikeConfiguration(boolean debug1, List<SpikeFeature.EndSpike> debug2, Optional<BlockPos> debug3) {
/* 29 */     this.crystalInvulnerable = debug1;
/* 30 */     this.spikes = debug2;
/* 31 */     this.crystalBeamTarget = debug3.orElse(null);
/*    */   }
/*    */   
/*    */   public boolean isCrystalInvulnerable() {
/* 35 */     return this.crystalInvulnerable;
/*    */   }
/*    */   
/*    */   public List<SpikeFeature.EndSpike> getSpikes() {
/* 39 */     return this.spikes;
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public BlockPos getCrystalBeamTarget() {
/* 44 */     return this.crystalBeamTarget;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\SpikeConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */