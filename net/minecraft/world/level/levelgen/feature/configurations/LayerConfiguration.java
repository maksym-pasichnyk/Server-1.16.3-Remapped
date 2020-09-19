/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class LayerConfiguration implements FeatureConfiguration {
/*    */   static {
/*  9 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)Codec.intRange(0, 255).fieldOf("height").forGetter(()), (App)BlockState.CODEC.fieldOf("state").forGetter(())).apply((Applicative)debug0, LayerConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<LayerConfiguration> CODEC;
/*    */   public final int height;
/*    */   public final BlockState state;
/*    */   
/*    */   public LayerConfiguration(int debug1, BlockState debug2) {
/* 18 */     this.height = debug1;
/* 19 */     this.state = debug2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\LayerConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */