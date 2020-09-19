/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ 
/*    */ public class EndGatewayConfiguration implements FeatureConfiguration {
/*    */   static {
/* 10 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)BlockPos.CODEC.optionalFieldOf("exit").forGetter(()), (App)Codec.BOOL.fieldOf("exact").forGetter(())).apply((Applicative)debug0, EndGatewayConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<EndGatewayConfiguration> CODEC;
/*    */   private final Optional<BlockPos> exit;
/*    */   private final boolean exact;
/*    */   
/*    */   private EndGatewayConfiguration(Optional<BlockPos> debug1, boolean debug2) {
/* 19 */     this.exit = debug1;
/* 20 */     this.exact = debug2;
/*    */   }
/*    */   
/*    */   public static EndGatewayConfiguration knownExit(BlockPos debug0, boolean debug1) {
/* 24 */     return new EndGatewayConfiguration(Optional.of(debug0), debug1);
/*    */   }
/*    */   
/*    */   public static EndGatewayConfiguration delayedExitSearch() {
/* 28 */     return new EndGatewayConfiguration(Optional.empty(), false);
/*    */   }
/*    */   
/*    */   public Optional<BlockPos> getExit() {
/* 32 */     return this.exit;
/*    */   }
/*    */   
/*    */   public boolean isExitExact() {
/* 36 */     return this.exact;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\EndGatewayConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */