/*    */ package net.minecraft.world.level.block;
/*    */ import java.util.function.BiPredicate;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.DirectionProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class DoubleBlockCombiner {
/*    */   public enum BlockType {
/* 16 */     SINGLE,
/* 17 */     FIRST,
/* 18 */     SECOND;
/*    */   }
/*    */ 
/*    */   
/*    */   public static <S extends BlockEntity> NeighborCombineResult<S> combineWithNeigbour(BlockEntityType<S> debug0, Function<BlockState, BlockType> debug1, Function<BlockState, Direction> debug2, DirectionProperty debug3, BlockState debug4, LevelAccessor debug5, BlockPos debug6, BiPredicate<LevelAccessor, BlockPos> debug7) {
/* 23 */     BlockEntity blockEntity = debug0.getBlockEntity((BlockGetter)debug5, debug6);
/* 24 */     if (blockEntity == null) {
/* 25 */       return Combiner::acceptNone;
/*    */     }
/*    */     
/* 28 */     if (debug7.test(debug5, debug6)) {
/* 29 */       return Combiner::acceptNone;
/*    */     }
/*    */     
/* 32 */     BlockType debug9 = debug1.apply(debug4);
/*    */     
/* 34 */     boolean debug10 = (debug9 == BlockType.SINGLE);
/* 35 */     boolean debug11 = (debug9 == BlockType.FIRST);
/*    */     
/* 37 */     if (debug10) {
/* 38 */       return new NeighborCombineResult.Single<>((S)blockEntity);
/*    */     }
/*    */     
/* 41 */     BlockPos debug12 = debug6.relative(debug2.apply(debug4));
/* 42 */     BlockState debug13 = debug5.getBlockState(debug12);
/* 43 */     if (debug13.is(debug4.getBlock())) {
/* 44 */       BlockType debug14 = debug1.apply(debug13);
/* 45 */       if (debug14 != BlockType.SINGLE && debug9 != debug14 && debug13.getValue((Property)debug3) == debug4.getValue((Property)debug3)) {
/* 46 */         if (debug7.test(debug5, debug12)) {
/* 47 */           return Combiner::acceptNone;
/*    */         }
/*    */         
/* 50 */         BlockEntity blockEntity1 = debug0.getBlockEntity((BlockGetter)debug5, debug12);
/* 51 */         if (blockEntity1 != null) {
/* 52 */           BlockEntity blockEntity2 = debug11 ? blockEntity : blockEntity1;
/* 53 */           BlockEntity blockEntity3 = debug11 ? blockEntity1 : blockEntity;
/* 54 */           return new NeighborCombineResult.Double<>((S)blockEntity2, (S)blockEntity3);
/*    */         } 
/*    */       } 
/*    */     } 
/* 58 */     return new NeighborCombineResult.Single<>((S)blockEntity);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static final class Double<S>
/*    */     implements NeighborCombineResult<S>
/*    */   {
/*    */     private final S first;
/*    */ 
/*    */ 
/*    */     
/*    */     private final S second;
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public Double(S debug1, S debug2) {
/* 77 */       this.first = debug1;
/* 78 */       this.second = debug2;
/*    */     }
/*    */     
/*    */     public <T> T apply(DoubleBlockCombiner.Combiner<? super S, T> debug1)
/*    */     {
/* 83 */       return debug1.acceptDouble(this.first, this.second); } } public static interface NeighborCombineResult<S> { <T> T apply(DoubleBlockCombiner.Combiner<? super S, T> param1Combiner); public static final class Double<S> implements NeighborCombineResult<S> { private final S first; public <T> T apply(DoubleBlockCombiner.Combiner<? super S, T> debug1) { return debug1.acceptDouble(this.first, this.second); }
/*    */        private final S second;
/*    */       public Double(S debug1, S debug2) {
/*    */         this.first = debug1;
/*    */         this.second = debug2;
/*    */       } }
/*    */     public static final class Single<S> implements NeighborCombineResult<S> { private final S single;
/*    */       public Single(S debug1) {
/* 91 */         this.single = debug1;
/*    */       }
/*    */       
/*    */       public <T> T apply(DoubleBlockCombiner.Combiner<? super S, T> debug1)
/*    */       {
/* 96 */         return debug1.acceptSingle(this.single); } } } public static final class Single<S> implements NeighborCombineResult<S> { private final S single; public Single(S debug1) { this.single = debug1; } public <T> T apply(DoubleBlockCombiner.Combiner<? super S, T> debug1) { return debug1.acceptSingle(this.single); }
/*    */      }
/*    */ 
/*    */   
/*    */   public static interface Combiner<S, T> {
/*    */     T acceptDouble(S param1S1, S param1S2);
/*    */     
/*    */     T acceptSingle(S param1S);
/*    */     
/*    */     T acceptNone();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\DoubleBlockCombiner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */