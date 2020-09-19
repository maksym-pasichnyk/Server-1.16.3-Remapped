/*    */ package net.minecraft.world.level.levelgen.surfacebuilders;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class SurfaceBuilderBaseConfiguration implements SurfaceBuilderConfiguration {
/*    */   static {
/*  9 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)BlockState.CODEC.fieldOf("top_material").forGetter(()), (App)BlockState.CODEC.fieldOf("under_material").forGetter(()), (App)BlockState.CODEC.fieldOf("underwater_material").forGetter(())).apply((Applicative)debug0, SurfaceBuilderBaseConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<SurfaceBuilderBaseConfiguration> CODEC;
/*    */   
/*    */   private final BlockState topMaterial;
/*    */   private final BlockState underMaterial;
/*    */   private final BlockState underwaterMaterial;
/*    */   
/*    */   public SurfaceBuilderBaseConfiguration(BlockState debug1, BlockState debug2, BlockState debug3) {
/* 20 */     this.topMaterial = debug1;
/* 21 */     this.underMaterial = debug2;
/* 22 */     this.underwaterMaterial = debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getTopMaterial() {
/* 27 */     return this.topMaterial;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getUnderMaterial() {
/* 32 */     return this.underMaterial;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getUnderwaterMaterial() {
/* 37 */     return this.underwaterMaterial;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\surfacebuilders\SurfaceBuilderBaseConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */