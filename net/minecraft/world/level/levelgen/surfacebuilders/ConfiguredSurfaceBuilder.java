/*    */ package net.minecraft.world.level.levelgen.surfacebuilders;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.RegistryFileCodec;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.ChunkAccess;
/*    */ 
/*    */ public class ConfiguredSurfaceBuilder<SC extends SurfaceBuilderConfiguration> {
/*    */   static {
/* 14 */     DIRECT_CODEC = Registry.SURFACE_BUILDER.dispatch(debug0 -> debug0.surfaceBuilder, SurfaceBuilder::configuredCodec);
/*    */   }
/* 16 */   public static final Codec<Supplier<ConfiguredSurfaceBuilder<?>>> CODEC = (Codec<Supplier<ConfiguredSurfaceBuilder<?>>>)RegistryFileCodec.create(Registry.CONFIGURED_SURFACE_BUILDER_REGISTRY, DIRECT_CODEC);
/*    */   public static final Codec<ConfiguredSurfaceBuilder<?>> DIRECT_CODEC;
/*    */   public final SurfaceBuilder<SC> surfaceBuilder;
/*    */   public final SC config;
/*    */   
/*    */   public ConfiguredSurfaceBuilder(SurfaceBuilder<SC> debug1, SC debug2) {
/* 22 */     this.surfaceBuilder = debug1;
/* 23 */     this.config = debug2;
/*    */   }
/*    */   
/*    */   public void apply(Random debug1, ChunkAccess debug2, Biome debug3, int debug4, int debug5, int debug6, double debug7, BlockState debug9, BlockState debug10, int debug11, long debug12) {
/* 27 */     this.surfaceBuilder.apply(debug1, debug2, debug3, debug4, debug5, debug6, debug7, debug9, debug10, debug11, debug12, this.config);
/*    */   }
/*    */   
/*    */   public void initNoise(long debug1) {
/* 31 */     this.surfaceBuilder.initNoise(debug1);
/*    */   }
/*    */   
/*    */   public SC config() {
/* 35 */     return this.config;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\surfacebuilders\ConfiguredSurfaceBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */