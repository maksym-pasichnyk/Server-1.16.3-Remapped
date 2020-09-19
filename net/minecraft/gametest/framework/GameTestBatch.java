/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.function.Consumer;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GameTestBatch
/*    */ {
/*    */   private final String name;
/*    */   private final Collection<TestFunction> testFunctions;
/*    */   @Nullable
/*    */   private final Consumer<ServerLevel> beforeBatchFunction;
/*    */   
/*    */   public GameTestBatch(String debug1, Collection<TestFunction> debug2, @Nullable Consumer<ServerLevel> debug3) {
/* 19 */     if (debug2.isEmpty()) {
/* 20 */       throw new IllegalArgumentException("A GameTestBatch must include at least one TestFunction!");
/*    */     }
/*    */     
/* 23 */     this.name = debug1;
/* 24 */     this.testFunctions = debug2;
/* 25 */     this.beforeBatchFunction = debug3;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 29 */     return this.name;
/*    */   }
/*    */   
/*    */   public Collection<TestFunction> getTestFunctions() {
/* 33 */     return this.testFunctions;
/*    */   }
/*    */   
/*    */   public void runBeforeBatchFunction(ServerLevel debug1) {
/* 37 */     if (this.beforeBatchFunction != null)
/* 38 */       this.beforeBatchFunction.accept(debug1); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\gametest\framework\GameTestBatch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */