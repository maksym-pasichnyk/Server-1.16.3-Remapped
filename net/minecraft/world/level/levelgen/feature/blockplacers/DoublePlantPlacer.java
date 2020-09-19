/*    */ package net.minecraft.world.level.levelgen.feature.blockplacers;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.DoublePlantBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class DoublePlantPlacer
/*    */   extends BlockPlacer
/*    */ {
/* 13 */   public static final Codec<DoublePlantPlacer> CODEC = Codec.unit(() -> INSTANCE);
/*    */   
/* 15 */   public static final DoublePlantPlacer INSTANCE = new DoublePlantPlacer();
/*    */ 
/*    */   
/*    */   protected BlockPlacerType<?> type() {
/* 19 */     return BlockPlacerType.DOUBLE_PLANT_PLACER;
/*    */   }
/*    */ 
/*    */   
/*    */   public void place(LevelAccessor debug1, BlockPos debug2, BlockState debug3, Random debug4) {
/* 24 */     ((DoublePlantBlock)debug3.getBlock()).placeAt(debug1, debug2, 2);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\blockplacers\DoublePlantPlacer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */