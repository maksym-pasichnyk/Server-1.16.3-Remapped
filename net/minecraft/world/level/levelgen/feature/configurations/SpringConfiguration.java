/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ 
/*    */ public class SpringConfiguration implements FeatureConfiguration {
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)FluidState.CODEC.fieldOf("state").forGetter(()), (App)Codec.BOOL.fieldOf("requires_block_below").orElse(Boolean.valueOf(true)).forGetter(()), (App)Codec.INT.fieldOf("rock_count").orElse(Integer.valueOf(4)).forGetter(()), (App)Codec.INT.fieldOf("hole_count").orElse(Integer.valueOf(1)).forGetter(()), (App)Registry.BLOCK.listOf().fieldOf("valid_blocks").xmap(ImmutableSet::copyOf, ImmutableList::copyOf).forGetter(())).apply((Applicative)debug0, SpringConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<SpringConfiguration> CODEC;
/*    */   
/*    */   public final FluidState state;
/*    */   
/*    */   public final boolean requiresBlockBelow;
/*    */   
/*    */   public final int rockCount;
/*    */   
/*    */   public final int holeCount;
/*    */   
/*    */   public final Set<Block> validBlocks;
/*    */   
/*    */   public SpringConfiguration(FluidState debug1, boolean debug2, int debug3, int debug4, Set<Block> debug5) {
/* 31 */     this.state = debug1;
/* 32 */     this.requiresBlockBelow = debug2;
/* 33 */     this.rockCount = debug3;
/* 34 */     this.holeCount = debug4;
/* 35 */     this.validBlocks = debug5;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\SpringConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */