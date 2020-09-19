/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.util.UniformInt;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class DiskConfiguration implements FeatureConfiguration {
/*    */   static {
/* 11 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)BlockState.CODEC.fieldOf("state").forGetter(()), (App)UniformInt.codec(0, 4, 4).fieldOf("radius").forGetter(()), (App)Codec.intRange(0, 4).fieldOf("half_height").forGetter(()), (App)BlockState.CODEC.listOf().fieldOf("targets").forGetter(())).apply((Applicative)debug0, DiskConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<DiskConfiguration> CODEC;
/*    */   
/*    */   public final BlockState state;
/*    */   
/*    */   public final UniformInt radius;
/*    */   public final int halfHeight;
/*    */   public final List<BlockState> targets;
/*    */   
/*    */   public DiskConfiguration(BlockState debug1, UniformInt debug2, int debug3, List<BlockState> debug4) {
/* 24 */     this.state = debug1;
/* 25 */     this.radius = debug2;
/* 26 */     this.halfHeight = debug3;
/* 27 */     this.targets = debug4;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\DiskConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */