/*    */ package net.minecraft.world.level.levelgen.feature.blockplacers;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Random;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class ColumnPlacer extends BlockPlacer {
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)Codec.INT.fieldOf("min_size").forGetter(()), (App)Codec.INT.fieldOf("extra_size").forGetter(())).apply((Applicative)debug0, ColumnPlacer::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<ColumnPlacer> CODEC;
/*    */   private final int minSize;
/*    */   private final int extraSize;
/*    */   
/*    */   public ColumnPlacer(int debug1, int debug2) {
/* 23 */     this.minSize = debug1;
/* 24 */     this.extraSize = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockPlacerType<?> type() {
/* 29 */     return BlockPlacerType.COLUMN_PLACER;
/*    */   }
/*    */ 
/*    */   
/*    */   public void place(LevelAccessor debug1, BlockPos debug2, BlockState debug3, Random debug4) {
/* 34 */     BlockPos.MutableBlockPos debug5 = debug2.mutable();
/* 35 */     int debug6 = this.minSize + debug4.nextInt(debug4.nextInt(this.extraSize + 1) + 1);
/* 36 */     for (int debug7 = 0; debug7 < debug6; debug7++) {
/* 37 */       debug1.setBlock((BlockPos)debug5, debug3, 2);
/* 38 */       debug5.move(Direction.UP);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\blockplacers\ColumnPlacer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */