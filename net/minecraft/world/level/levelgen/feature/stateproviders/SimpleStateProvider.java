/*    */ package net.minecraft.world.level.levelgen.feature.stateproviders;
/*    */ import java.util.Random;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class SimpleStateProvider extends BlockStateProvider {
/*    */   public static final Codec<SimpleStateProvider> CODEC;
/*    */   
/*    */   static {
/* 10 */     CODEC = BlockState.CODEC.fieldOf("state").xmap(SimpleStateProvider::new, debug0 -> debug0.state).codec();
/*    */   }
/*    */   private final BlockState state;
/*    */   
/*    */   public SimpleStateProvider(BlockState debug1) {
/* 15 */     this.state = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockStateProviderType<?> type() {
/* 20 */     return BlockStateProviderType.SIMPLE_STATE_PROVIDER;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getState(Random debug1, BlockPos debug2) {
/* 25 */     return this.state;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\stateproviders\SimpleStateProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */