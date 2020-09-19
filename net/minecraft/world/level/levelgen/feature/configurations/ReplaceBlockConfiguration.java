/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class ReplaceBlockConfiguration implements FeatureConfiguration {
/*    */   static {
/*  8 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)BlockState.CODEC.fieldOf("target").forGetter(()), (App)BlockState.CODEC.fieldOf("state").forGetter(())).apply((Applicative)debug0, ReplaceBlockConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<ReplaceBlockConfiguration> CODEC;
/*    */   public final BlockState target;
/*    */   public final BlockState state;
/*    */   
/*    */   public ReplaceBlockConfiguration(BlockState debug1, BlockState debug2) {
/* 17 */     this.target = debug1;
/* 18 */     this.state = debug2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\ReplaceBlockConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */