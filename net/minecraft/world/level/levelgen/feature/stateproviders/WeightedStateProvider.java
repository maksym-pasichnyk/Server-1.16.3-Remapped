/*    */ package net.minecraft.world.level.levelgen.feature.stateproviders;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.ai.behavior.WeightedList;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class WeightedStateProvider extends BlockStateProvider {
/*    */   public static final Codec<WeightedStateProvider> CODEC;
/*    */   
/*    */   static {
/* 12 */     CODEC = WeightedList.codec(BlockState.CODEC).comapFlatMap(WeightedStateProvider::create, debug0 -> debug0.weightedList).fieldOf("entries").codec();
/*    */   }
/*    */   private final WeightedList<BlockState> weightedList;
/*    */   private static DataResult<WeightedStateProvider> create(WeightedList<BlockState> debug0) {
/* 16 */     if (debug0.isEmpty()) {
/* 17 */       return DataResult.error("WeightedStateProvider with no states");
/*    */     }
/* 19 */     return DataResult.success(new WeightedStateProvider(debug0));
/*    */   }
/*    */   
/*    */   private WeightedStateProvider(WeightedList<BlockState> debug1) {
/* 23 */     this.weightedList = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockStateProviderType<?> type() {
/* 28 */     return BlockStateProviderType.WEIGHTED_STATE_PROVIDER;
/*    */   }
/*    */   
/*    */   public WeightedStateProvider() {
/* 32 */     this(new WeightedList());
/*    */   }
/*    */   
/*    */   public WeightedStateProvider add(BlockState debug1, int debug2) {
/* 36 */     this.weightedList.add(debug1, debug2);
/* 37 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getState(Random debug1, BlockPos debug2) {
/* 42 */     return (BlockState)this.weightedList.getOne(debug1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\stateproviders\WeightedStateProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */