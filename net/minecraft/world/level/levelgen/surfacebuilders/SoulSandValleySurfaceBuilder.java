/*    */ package net.minecraft.world.level.levelgen.surfacebuilders;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class SoulSandValleySurfaceBuilder extends NetherCappedSurfaceBuilder {
/*  9 */   private static final BlockState SOUL_SAND = Blocks.SOUL_SAND.defaultBlockState();
/* 10 */   private static final BlockState SOUL_SOIL = Blocks.SOUL_SOIL.defaultBlockState();
/* 11 */   private static final BlockState GRAVEL = Blocks.GRAVEL.defaultBlockState();
/*    */   
/* 13 */   private static final ImmutableList<BlockState> BLOCK_STATES = ImmutableList.of(SOUL_SAND, SOUL_SOIL);
/*    */   
/*    */   public SoulSandValleySurfaceBuilder(Codec<SurfaceBuilderBaseConfiguration> debug1) {
/* 16 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ImmutableList<BlockState> getFloorBlockStates() {
/* 21 */     return BLOCK_STATES;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ImmutableList<BlockState> getCeilingBlockStates() {
/* 26 */     return BLOCK_STATES;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState getPatchBlockState() {
/* 31 */     return GRAVEL;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\surfacebuilders\SoulSandValleySurfaceBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */