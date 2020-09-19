/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
/*    */ 
/*    */ public class OreConfiguration implements FeatureConfiguration {
/*    */   public static final Codec<OreConfiguration> CODEC;
/*    */   
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)RuleTest.CODEC.fieldOf("target").forGetter(()), (App)BlockState.CODEC.fieldOf("state").forGetter(()), (App)Codec.intRange(0, 64).fieldOf("size").forGetter(())).apply((Applicative)debug0, OreConfiguration::new));
/*    */   }
/*    */   
/*    */   public final RuleTest target;
/*    */   public final int size;
/*    */   public final BlockState state;
/*    */   
/* 20 */   public static final class Predicates { public static final RuleTest NATURAL_STONE = (RuleTest)new TagMatchTest((Tag)BlockTags.BASE_STONE_OVERWORLD);
/* 21 */     public static final RuleTest NETHERRACK = (RuleTest)new BlockMatchTest(Blocks.NETHERRACK);
/* 22 */     public static final RuleTest NETHER_ORE_REPLACEABLES = (RuleTest)new TagMatchTest((Tag)BlockTags.BASE_STONE_NETHER); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public OreConfiguration(RuleTest debug1, BlockState debug2, int debug3) {
/* 30 */     this.size = debug3;
/* 31 */     this.state = debug2;
/* 32 */     this.target = debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\OreConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */