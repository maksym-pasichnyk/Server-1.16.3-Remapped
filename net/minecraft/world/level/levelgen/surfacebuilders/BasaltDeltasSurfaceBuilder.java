/*    */ package net.minecraft.world.level.levelgen.surfacebuilders;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BasaltDeltasSurfaceBuilder extends NetherCappedSurfaceBuilder {
/*  9 */   private static final BlockState BASALT = Blocks.BASALT.defaultBlockState();
/* 10 */   private static final BlockState BLACKSTONE = Blocks.BLACKSTONE.defaultBlockState();
/* 11 */   private static final BlockState GRAVEL = Blocks.GRAVEL.defaultBlockState();
/*    */   
/* 13 */   private static final ImmutableList<BlockState> FLOOR_BLOCK_STATES = ImmutableList.of(BASALT, BLACKSTONE);
/* 14 */   private static final ImmutableList<BlockState> CEILING_BLOCK_STATES = ImmutableList.of(BASALT);
/*    */   
/*    */   public BasaltDeltasSurfaceBuilder(Codec<SurfaceBuilderBaseConfiguration> debug1) {
/* 17 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ImmutableList<BlockState> getFloorBlockStates() {
/* 22 */     return FLOOR_BLOCK_STATES;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ImmutableList<BlockState> getCeilingBlockStates() {
/* 27 */     return CEILING_BLOCK_STATES;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState getPatchBlockState() {
/* 32 */     return GRAVEL;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\surfacebuilders\BasaltDeltasSurfaceBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */