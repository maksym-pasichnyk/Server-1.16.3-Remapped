/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GameTestSequence
/*     */ {
/*     */   private final GameTestInfo parent;
/*     */   private final List<GameTestEvent> events;
/*     */   private long lastTick;
/*     */   
/*     */   public void tickAndContinue(long debug1) {
/*     */     try {
/*  88 */       tick(debug1);
/*  89 */     } catch (Exception exception) {}
/*     */   }
/*     */ 
/*     */   
/*     */   public void tickAndFailIfNotComplete(long debug1) {
/*     */     try {
/*  95 */       tick(debug1);
/*  96 */     } catch (Exception debug3) {
/*  97 */       this.parent.fail(debug3);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void tick(long debug1) {
/* 110 */     Iterator<GameTestEvent> debug3 = this.events.iterator();
/* 111 */     while (debug3.hasNext()) {
/* 112 */       GameTestEvent debug4 = debug3.next();
/* 113 */       debug4.assertion.run();
/* 114 */       debug3.remove();
/* 115 */       long debug5 = debug1 - this.lastTick;
/* 116 */       long debug7 = this.lastTick;
/* 117 */       this.lastTick = debug1;
/* 118 */       if (debug4.expectedDelay != null && debug4.expectedDelay.longValue() != debug5) {
/* 119 */         this.parent.fail(new GameTestAssertException("Succeeded in invalid tick: expected " + (debug7 + debug4.expectedDelay.longValue()) + ", but current tick is " + debug1));
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\gametest\framework\GameTestSequence.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */