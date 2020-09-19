/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Collection;
/*     */ import java.util.function.Consumer;
/*     */ import javax.annotation.Nullable;
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
/*     */ public class MultipleTestTracker
/*     */ {
/*  20 */   private final Collection<GameTestInfo> tests = Lists.newArrayList();
/*     */   
/*     */   @Nullable
/*  23 */   private Collection<GameTestListener> listeners = Lists.newArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MultipleTestTracker(Collection<GameTestInfo> debug1) {
/*  29 */     this.tests.addAll(debug1);
/*     */   }
/*     */   
/*     */   public void addTestToTrack(GameTestInfo debug1) {
/*  33 */     this.tests.add(debug1);
/*  34 */     this.listeners.forEach(debug1::addListener);
/*     */   }
/*     */   
/*     */   public void addListener(GameTestListener debug1) {
/*  38 */     this.listeners.add(debug1);
/*  39 */     this.tests.forEach(debug1 -> debug1.addListener(debug0));
/*     */   }
/*     */   
/*     */   public void addFailureListener(final Consumer<GameTestInfo> listener) {
/*  43 */     addListener(new GameTestListener()
/*     */         {
/*     */           public void testStructureLoaded(GameTestInfo debug1) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public void testFailed(GameTestInfo debug1) {
/*  55 */             listener.accept(debug1);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public int getFailedRequiredCount() {
/*  61 */     return (int)this.tests.stream().filter(GameTestInfo::hasFailed).filter(GameTestInfo::isRequired).count();
/*     */   }
/*     */   
/*     */   public int getFailedOptionalCount() {
/*  65 */     return (int)this.tests.stream().filter(GameTestInfo::hasFailed).filter(GameTestInfo::isOptional).count();
/*     */   }
/*     */   
/*     */   public int getDoneCount() {
/*  69 */     return (int)this.tests.stream().filter(GameTestInfo::isDone).count();
/*     */   }
/*     */   
/*     */   public boolean hasFailedRequired() {
/*  73 */     return (getFailedRequiredCount() > 0);
/*     */   }
/*     */   
/*     */   public boolean hasFailedOptional() {
/*  77 */     return (getFailedOptionalCount() > 0);
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
/*     */   public int getTotalCount() {
/*  89 */     return this.tests.size();
/*     */   }
/*     */   
/*     */   public boolean isDone() {
/*  93 */     return (getDoneCount() == getTotalCount());
/*     */   }
/*     */   
/*     */   public String getProgressBar() {
/*  97 */     StringBuffer debug1 = new StringBuffer();
/*  98 */     debug1.append('[');
/*  99 */     this.tests.forEach(debug1 -> {
/*     */           if (!debug1.hasStarted()) {
/*     */             debug0.append(' ');
/*     */           } else if (debug1.hasSucceeded()) {
/*     */             debug0.append('+');
/*     */           } else if (debug1.hasFailed()) {
/*     */             debug0.append(debug1.isRequired() ? 88 : 120);
/*     */           } else {
/*     */             debug0.append('_');
/*     */           } 
/*     */         });
/* 110 */     debug1.append(']');
/* 111 */     return debug1.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 116 */     return getProgressBar();
/*     */   }
/*     */   
/*     */   public MultipleTestTracker() {}
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\gametest\framework\MultipleTestTracker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */