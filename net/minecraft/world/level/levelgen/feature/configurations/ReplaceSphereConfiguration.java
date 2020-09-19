/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.UniformInt;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class ReplaceSphereConfiguration implements FeatureConfiguration {
/*    */   static {
/*  9 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)BlockState.CODEC.fieldOf("target").forGetter(()), (App)BlockState.CODEC.fieldOf("state").forGetter(()), (App)UniformInt.CODEC.fieldOf("radius").forGetter(())).apply((Applicative)debug0, ReplaceSphereConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<ReplaceSphereConfiguration> CODEC;
/*    */   
/*    */   public final BlockState targetState;
/*    */   
/*    */   public final BlockState replaceState;
/*    */   private final UniformInt radius;
/*    */   
/*    */   public ReplaceSphereConfiguration(BlockState debug1, BlockState debug2, UniformInt debug3) {
/* 21 */     this.targetState = debug1;
/* 22 */     this.replaceState = debug2;
/* 23 */     this.radius = debug3;
/*    */   }
/*    */   
/*    */   public UniformInt radius() {
/* 27 */     return this.radius;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\ReplaceSphereConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */