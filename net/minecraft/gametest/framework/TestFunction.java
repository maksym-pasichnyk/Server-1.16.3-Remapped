/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TestFunction
/*    */ {
/*    */   private final String batchName;
/*    */   private final String testName;
/*    */   private final String structureName;
/*    */   private final boolean required;
/*    */   private final Consumer<GameTestHelper> function;
/*    */   private final int maxTicks;
/*    */   private final long setupTicks;
/*    */   private final Rotation rotation;
/*    */   
/*    */   public void run(GameTestHelper debug1) {
/* 40 */     this.function.accept(debug1);
/*    */   }
/*    */   
/*    */   public String getTestName() {
/* 44 */     return this.testName;
/*    */   }
/*    */   
/*    */   public String getStructureName() {
/* 48 */     return this.structureName;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 53 */     return this.testName;
/*    */   }
/*    */   
/*    */   public int getMaxTicks() {
/* 57 */     return this.maxTicks;
/*    */   }
/*    */   
/*    */   public boolean isRequired() {
/* 61 */     return this.required;
/*    */   }
/*    */   
/*    */   public String getBatchName() {
/* 65 */     return this.batchName;
/*    */   }
/*    */   
/*    */   public long getSetupTicks() {
/* 69 */     return this.setupTicks;
/*    */   }
/*    */   
/*    */   public Rotation getRotation() {
/* 73 */     return this.rotation;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\gametest\framework\TestFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */