/*    */ package net.minecraft.world.level.levelgen.feature.blockplacers;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class SimpleBlockPlacer
/*    */   extends BlockPlacer
/*    */ {
/* 12 */   public static final Codec<SimpleBlockPlacer> CODEC = Codec.unit(() -> INSTANCE);
/* 13 */   public static final SimpleBlockPlacer INSTANCE = new SimpleBlockPlacer();
/*    */ 
/*    */   
/*    */   protected BlockPlacerType<?> type() {
/* 17 */     return BlockPlacerType.SIMPLE_BLOCK_PLACER;
/*    */   }
/*    */ 
/*    */   
/*    */   public void place(LevelAccessor debug1, BlockPos debug2, BlockState debug3, Random debug4) {
/* 22 */     debug1.setBlock(debug2, debug3, 2);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\blockplacers\SimpleBlockPlacer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */