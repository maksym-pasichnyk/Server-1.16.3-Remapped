/*    */ package net.minecraft.util.thread;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ProcessorHandle<Msg>
/*    */   extends AutoCloseable
/*    */ {
/*    */   default void close() {}
/*    */   
/*    */   default <Source> CompletableFuture<Source> ask(Function<? super ProcessorHandle<Source>, ? extends Msg> debug1) {
/* 19 */     CompletableFuture<Source> debug2 = new CompletableFuture<>();
/* 20 */     Msg debug3 = debug1.apply(of("ask future procesor handle", debug2::complete));
/* 21 */     tell(debug3);
/* 22 */     return debug2;
/*    */   }
/*    */   
/*    */   default <Source> CompletableFuture<Source> askEither(Function<? super ProcessorHandle<Either<Source, Exception>>, ? extends Msg> debug1) {
/* 26 */     CompletableFuture<Source> debug2 = new CompletableFuture<>();
/* 27 */     Msg debug3 = debug1.apply(of("ask future procesor handle", debug1 -> {
/*    */             debug1.ifLeft(debug0::complete);
/*    */             debug1.ifRight(debug0::completeExceptionally);
/*    */           }));
/* 31 */     tell(debug3);
/* 32 */     return debug2;
/*    */   }
/*    */   
/*    */   static <Msg> ProcessorHandle<Msg> of(final String name, final Consumer<Msg> tell) {
/* 36 */     return new ProcessorHandle<Msg>()
/*    */       {
/*    */         public String name() {
/* 39 */           return name;
/*    */         }
/*    */ 
/*    */         
/*    */         public void tell(Msg debug1) {
/* 44 */           tell.accept(debug1);
/*    */         }
/*    */ 
/*    */         
/*    */         public String toString() {
/* 49 */           return name;
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   String name();
/*    */   
/*    */   void tell(Msg paramMsg);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\thread\ProcessorHandle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */