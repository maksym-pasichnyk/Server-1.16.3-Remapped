/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class SimpleBlockConfiguration implements FeatureConfiguration {
/*    */   static {
/* 10 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)BlockState.CODEC.fieldOf("to_place").forGetter(()), (App)BlockState.CODEC.listOf().fieldOf("place_on").forGetter(()), (App)BlockState.CODEC.listOf().fieldOf("place_in").forGetter(()), (App)BlockState.CODEC.listOf().fieldOf("place_under").forGetter(())).apply((Applicative)debug0, SimpleBlockConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<SimpleBlockConfiguration> CODEC;
/*    */   
/*    */   public final BlockState toPlace;
/*    */   
/*    */   public final List<BlockState> placeOn;
/*    */   public final List<BlockState> placeIn;
/*    */   public final List<BlockState> placeUnder;
/*    */   
/*    */   public SimpleBlockConfiguration(BlockState debug1, List<BlockState> debug2, List<BlockState> debug3, List<BlockState> debug4) {
/* 23 */     this.toPlace = debug1;
/* 24 */     this.placeOn = debug2;
/* 25 */     this.placeIn = debug3;
/* 26 */     this.placeUnder = debug4;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\SimpleBlockConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */