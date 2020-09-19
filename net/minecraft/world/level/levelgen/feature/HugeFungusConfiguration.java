/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class HugeFungusConfiguration implements FeatureConfiguration {
/*    */   static {
/* 10 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)BlockState.CODEC.fieldOf("valid_base_block").forGetter(()), (App)BlockState.CODEC.fieldOf("stem_state").forGetter(()), (App)BlockState.CODEC.fieldOf("hat_state").forGetter(()), (App)BlockState.CODEC.fieldOf("decor_state").forGetter(()), (App)Codec.BOOL.fieldOf("planted").orElse(Boolean.valueOf(false)).forGetter(())).apply((Applicative)debug0, HugeFungusConfiguration::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static final Codec<HugeFungusConfiguration> CODEC;
/*    */ 
/*    */   
/* 18 */   public static final HugeFungusConfiguration HUGE_CRIMSON_FUNGI_PLANTED_CONFIG = new HugeFungusConfiguration(Blocks.CRIMSON_NYLIUM.defaultBlockState(), Blocks.CRIMSON_STEM.defaultBlockState(), Blocks.NETHER_WART_BLOCK.defaultBlockState(), Blocks.SHROOMLIGHT.defaultBlockState(), true);
/* 19 */   public static final HugeFungusConfiguration HUGE_CRIMSON_FUNGI_NOT_PLANTED_CONFIG = new HugeFungusConfiguration(HUGE_CRIMSON_FUNGI_PLANTED_CONFIG.validBaseState, HUGE_CRIMSON_FUNGI_PLANTED_CONFIG.stemState, HUGE_CRIMSON_FUNGI_PLANTED_CONFIG.hatState, HUGE_CRIMSON_FUNGI_PLANTED_CONFIG.decorState, false);
/* 20 */   public static final HugeFungusConfiguration HUGE_WARPED_FUNGI_PLANTED_CONFIG = new HugeFungusConfiguration(Blocks.WARPED_NYLIUM.defaultBlockState(), Blocks.WARPED_STEM.defaultBlockState(), Blocks.WARPED_WART_BLOCK.defaultBlockState(), Blocks.SHROOMLIGHT.defaultBlockState(), true);
/* 21 */   public static final HugeFungusConfiguration HUGE_WARPED_FUNGI_NOT_PLANTED_CONFIG = new HugeFungusConfiguration(HUGE_WARPED_FUNGI_PLANTED_CONFIG.validBaseState, HUGE_WARPED_FUNGI_PLANTED_CONFIG.stemState, HUGE_WARPED_FUNGI_PLANTED_CONFIG.hatState, HUGE_WARPED_FUNGI_PLANTED_CONFIG.decorState, false);
/*    */   
/*    */   public final BlockState validBaseState;
/*    */   public final BlockState stemState;
/*    */   public final BlockState hatState;
/*    */   public final BlockState decorState;
/*    */   public final boolean planted;
/*    */   
/*    */   public HugeFungusConfiguration(BlockState debug1, BlockState debug2, BlockState debug3, BlockState debug4, boolean debug5) {
/* 30 */     this.validBaseState = debug1;
/* 31 */     this.stemState = debug2;
/* 32 */     this.hatState = debug3;
/* 33 */     this.decorState = debug4;
/* 34 */     this.planted = debug5;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\HugeFungusConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */