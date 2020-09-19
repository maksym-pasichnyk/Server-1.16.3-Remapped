/*    */ package net.minecraft.util.profiling;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public class InactiveProfiler implements ProfileCollector {
/*  6 */   public static final InactiveProfiler INSTANCE = new InactiveProfiler();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void startTick() {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void endTick() {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void push(String debug1) {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void push(Supplier<String> debug1) {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void pop() {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void popPush(String debug1) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void incrementCounter(String debug1) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void incrementCounter(Supplier<String> debug1) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public ProfileResults getResults() {
/* 49 */     return EmptyProfileResults.EMPTY;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\profiling\InactiveProfiler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */