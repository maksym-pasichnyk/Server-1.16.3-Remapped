/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public interface ContainerLevelAccess
/*    */ {
/* 11 */   public static final ContainerLevelAccess NULL = new ContainerLevelAccess()
/*    */     {
/*    */       public <T> Optional<T> evaluate(BiFunction<Level, BlockPos, T> debug1) {
/* 14 */         return Optional.empty();
/*    */       }
/*    */     };
/*    */   
/*    */   static ContainerLevelAccess create(final Level level, final BlockPos pos) {
/* 19 */     return new ContainerLevelAccess()
/*    */       {
/*    */         public <T> Optional<T> evaluate(BiFunction<Level, BlockPos, T> debug1) {
/* 22 */           return Optional.of(debug1.apply(level, pos));
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   <T> Optional<T> evaluate(BiFunction<Level, BlockPos, T> paramBiFunction);
/*    */   
/*    */   default <T> T evaluate(BiFunction<Level, BlockPos, T> debug1, T debug2) {
/* 30 */     return evaluate(debug1).orElse(debug2);
/*    */   }
/*    */   
/*    */   default void execute(BiConsumer<Level, BlockPos> debug1) {
/* 34 */     evaluate((debug1, debug2) -> {
/*    */           debug0.accept(debug1, debug2);
/*    */           return Optional.empty();
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\inventory\ContainerLevelAccess.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */