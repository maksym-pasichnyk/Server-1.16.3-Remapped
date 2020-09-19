/*    */ package net.minecraft.world.level.levelgen.surfacebuilders;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.ChunkAccess;
/*    */ 
/*    */ public class NopeSurfaceBuilder
/*    */   extends SurfaceBuilder<SurfaceBuilderBaseConfiguration> {
/*    */   public NopeSurfaceBuilder(Codec<SurfaceBuilderBaseConfiguration> debug1) {
/* 12 */     super(debug1);
/*    */   }
/*    */   
/*    */   public void apply(Random debug1, ChunkAccess debug2, Biome debug3, int debug4, int debug5, int debug6, double debug7, BlockState debug9, BlockState debug10, int debug11, long debug12, SurfaceBuilderBaseConfiguration debug14) {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\surfacebuilders\NopeSurfaceBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */