/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.UniformInt;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class DeltaFeatureConfiguration implements FeatureConfiguration {
/*    */   static {
/*  9 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)BlockState.CODEC.fieldOf("contents").forGetter(()), (App)BlockState.CODEC.fieldOf("rim").forGetter(()), (App)UniformInt.codec(0, 8, 8).fieldOf("size").forGetter(()), (App)UniformInt.codec(0, 8, 8).fieldOf("rim_size").forGetter(())).apply((Applicative)debug0, DeltaFeatureConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<DeltaFeatureConfiguration> CODEC;
/*    */   
/*    */   private final BlockState contents;
/*    */   
/*    */   private final BlockState rim;
/*    */   private final UniformInt size;
/*    */   private final UniformInt rimSize;
/*    */   
/*    */   public DeltaFeatureConfiguration(BlockState debug1, BlockState debug2, UniformInt debug3, UniformInt debug4) {
/* 22 */     this.contents = debug1;
/* 23 */     this.rim = debug2;
/* 24 */     this.size = debug3;
/* 25 */     this.rimSize = debug4;
/*    */   }
/*    */   
/*    */   public BlockState contents() {
/* 29 */     return this.contents;
/*    */   }
/*    */   
/*    */   public BlockState rim() {
/* 33 */     return this.rim;
/*    */   }
/*    */   
/*    */   public UniformInt size() {
/* 37 */     return this.size;
/*    */   }
/*    */   
/*    */   public UniformInt rimSize() {
/* 41 */     return this.rimSize;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\DeltaFeatureConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */