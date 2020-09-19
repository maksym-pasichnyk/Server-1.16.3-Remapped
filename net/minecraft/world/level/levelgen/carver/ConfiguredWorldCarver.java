/*    */ package net.minecraft.world.level.levelgen.carver;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.BitSet;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.RegistryFileCodec;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.chunk.ChunkAccess;
/*    */ 
/*    */ public class ConfiguredWorldCarver<WC extends CarverConfiguration> {
/*    */   static {
/* 17 */     DIRECT_CODEC = Registry.CARVER.dispatch(debug0 -> debug0.worldCarver, WorldCarver::configuredCodec);
/*    */   }
/* 19 */   public static final Codec<Supplier<ConfiguredWorldCarver<?>>> CODEC = (Codec<Supplier<ConfiguredWorldCarver<?>>>)RegistryFileCodec.create(Registry.CONFIGURED_CARVER_REGISTRY, DIRECT_CODEC); public static final Codec<ConfiguredWorldCarver<?>> DIRECT_CODEC;
/* 20 */   public static final Codec<List<Supplier<ConfiguredWorldCarver<?>>>> LIST_CODEC = RegistryFileCodec.homogeneousList(Registry.CONFIGURED_CARVER_REGISTRY, DIRECT_CODEC);
/*    */   
/*    */   private final WorldCarver<WC> worldCarver;
/*    */   private final WC config;
/*    */   
/*    */   public ConfiguredWorldCarver(WorldCarver<WC> debug1, WC debug2) {
/* 26 */     this.worldCarver = debug1;
/* 27 */     this.config = debug2;
/*    */   }
/*    */   
/*    */   public WC config() {
/* 31 */     return this.config;
/*    */   }
/*    */   
/*    */   public boolean isStartChunk(Random debug1, int debug2, int debug3) {
/* 35 */     return this.worldCarver.isStartChunk(debug1, debug2, debug3, this.config);
/*    */   }
/*    */   
/*    */   public boolean carve(ChunkAccess debug1, Function<BlockPos, Biome> debug2, Random debug3, int debug4, int debug5, int debug6, int debug7, int debug8, BitSet debug9) {
/* 39 */     return this.worldCarver.carve(debug1, debug2, debug3, debug4, debug5, debug6, debug7, debug8, debug9, this.config);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\carver\ConfiguredWorldCarver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */