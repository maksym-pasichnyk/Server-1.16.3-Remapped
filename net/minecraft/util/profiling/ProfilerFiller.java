/*    */ package net.minecraft.util.profiling;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ProfilerFiller
/*    */ {
/*    */   void startTick();
/*    */   
/*    */   void endTick();
/*    */   
/*    */   void push(String paramString);
/*    */   
/*    */   void push(Supplier<String> paramSupplier);
/*    */   
/*    */   void pop();
/*    */   
/*    */   void popPush(String paramString);
/*    */   
/*    */   void incrementCounter(String paramString);
/*    */   
/*    */   void incrementCounter(Supplier<String> paramSupplier);
/*    */   
/*    */   static ProfilerFiller tee(final ProfilerFiller first, final ProfilerFiller second) {
/* 27 */     if (first == InactiveProfiler.INSTANCE) {
/* 28 */       return second;
/*    */     }
/* 30 */     if (second == InactiveProfiler.INSTANCE) {
/* 31 */       return first;
/*    */     }
/* 33 */     return new ProfilerFiller()
/*    */       {
/*    */         public void startTick() {
/* 36 */           first.startTick();
/* 37 */           second.startTick();
/*    */         }
/*    */ 
/*    */         
/*    */         public void endTick() {
/* 42 */           first.endTick();
/* 43 */           second.endTick();
/*    */         }
/*    */ 
/*    */         
/*    */         public void push(String debug1) {
/* 48 */           first.push(debug1);
/* 49 */           second.push(debug1);
/*    */         }
/*    */ 
/*    */         
/*    */         public void push(Supplier<String> debug1) {
/* 54 */           first.push(debug1);
/* 55 */           second.push(debug1);
/*    */         }
/*    */ 
/*    */         
/*    */         public void pop() {
/* 60 */           first.pop();
/* 61 */           second.pop();
/*    */         }
/*    */ 
/*    */         
/*    */         public void popPush(String debug1) {
/* 66 */           first.popPush(debug1);
/* 67 */           second.popPush(debug1);
/*    */         }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */         
/*    */         public void incrementCounter(String debug1) {
/* 78 */           first.incrementCounter(debug1);
/* 79 */           second.incrementCounter(debug1);
/*    */         }
/*    */ 
/*    */         
/*    */         public void incrementCounter(Supplier<String> debug1) {
/* 84 */           first.incrementCounter(debug1);
/* 85 */           second.incrementCounter(debug1);
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\profiling\ProfilerFiller.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */