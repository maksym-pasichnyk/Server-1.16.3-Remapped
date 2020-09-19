/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableCollection;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ @GwtCompatible
/*     */ abstract class AggregateFuture<InputT, OutputT>
/*     */   extends AbstractFuture.TrustedFuture<OutputT>
/*     */ {
/*  39 */   private static final Logger logger = Logger.getLogger(AggregateFuture.class.getName());
/*     */ 
/*     */ 
/*     */   
/*     */   private RunningState runningState;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void afterDone() {
/*  49 */     super.afterDone();
/*  50 */     RunningState localRunningState = this.runningState;
/*  51 */     if (localRunningState != null) {
/*     */       
/*  53 */       this.runningState = null;
/*     */       
/*  55 */       ImmutableCollection<? extends ListenableFuture<? extends InputT>> futures = localRunningState.futures;
/*  56 */       boolean wasInterrupted = wasInterrupted();
/*     */       
/*  58 */       if (wasInterrupted()) {
/*  59 */         localRunningState.interruptTask();
/*     */       }
/*     */       
/*  62 */       if ((isCancelled() & ((futures != null) ? 1 : 0)) != 0) {
/*  63 */         for (UnmodifiableIterator<ListenableFuture> unmodifiableIterator = futures.iterator(); unmodifiableIterator.hasNext(); ) { ListenableFuture<?> future = unmodifiableIterator.next();
/*  64 */           future.cancel(wasInterrupted); }
/*     */       
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void init(RunningState runningState) {
/*  74 */     this.runningState = runningState;
/*  75 */     runningState.init();
/*     */   }
/*     */   
/*     */   abstract class RunningState
/*     */     extends AggregateFutureState
/*     */     implements Runnable
/*     */   {
/*     */     private ImmutableCollection<? extends ListenableFuture<? extends InputT>> futures;
/*     */     private final boolean allMustSucceed;
/*     */     private final boolean collectsValues;
/*     */     
/*     */     RunningState(ImmutableCollection<? extends ListenableFuture<? extends InputT>> futures, boolean allMustSucceed, boolean collectsValues) {
/*  87 */       super(futures.size());
/*  88 */       this.futures = (ImmutableCollection<? extends ListenableFuture<? extends InputT>>)Preconditions.checkNotNull(futures);
/*  89 */       this.allMustSucceed = allMustSucceed;
/*  90 */       this.collectsValues = collectsValues;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public final void run() {
/*  96 */       decrementCountAndMaybeComplete();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void init() {
/* 107 */       if (this.futures.isEmpty()) {
/* 108 */         handleAllCompleted();
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */ 
/*     */       
/* 115 */       if (this.allMustSucceed) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 125 */         int i = 0;
/* 126 */         for (UnmodifiableIterator<ListenableFuture<? extends InputT>> unmodifiableIterator = this.futures.iterator(); unmodifiableIterator.hasNext(); ) { final ListenableFuture<? extends InputT> listenable = unmodifiableIterator.next();
/* 127 */           final int index = i++;
/* 128 */           listenable.addListener(new Runnable()
/*     */               {
/*     */                 public void run()
/*     */                 {
/*     */                   try {
/* 133 */                     AggregateFuture.RunningState.this.handleOneInputDone(index, listenable);
/*     */                   } finally {
/* 135 */                     AggregateFuture.RunningState.this.decrementCountAndMaybeComplete();
/*     */                   }
/*     */                 
/*     */                 }
/* 139 */               }MoreExecutors.directExecutor()); }
/*     */ 
/*     */       
/*     */       } else {
/*     */         
/* 144 */         for (UnmodifiableIterator<ListenableFuture<? extends InputT>> unmodifiableIterator = this.futures.iterator(); unmodifiableIterator.hasNext(); ) { final ListenableFuture<? extends InputT> listenable = unmodifiableIterator.next();
/* 145 */           listenable.addListener(this, MoreExecutors.directExecutor()); }
/*     */       
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void handleException(Throwable throwable) {
/* 157 */       Preconditions.checkNotNull(throwable);
/*     */       
/* 159 */       boolean completedWithFailure = false;
/* 160 */       boolean firstTimeSeeingThisException = true;
/* 161 */       if (this.allMustSucceed) {
/*     */ 
/*     */         
/* 164 */         completedWithFailure = AggregateFuture.this.setException(throwable);
/* 165 */         if (completedWithFailure) {
/* 166 */           releaseResourcesAfterFailure();
/*     */         }
/*     */         else {
/*     */           
/* 170 */           firstTimeSeeingThisException = AggregateFuture.addCausalChain(getOrInitSeenExceptions(), throwable);
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 175 */       if ((throwable instanceof Error | this.allMustSucceed & (!completedWithFailure ? 1 : 0) & firstTimeSeeingThisException) != 0) {
/*     */         
/* 177 */         String message = (throwable instanceof Error) ? "Input Future failed with Error" : "Got more than one input Future failure. Logging failures after the first";
/*     */ 
/*     */ 
/*     */         
/* 181 */         AggregateFuture.logger.log(Level.SEVERE, message, throwable);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     final void addInitialException(Set<Throwable> seen) {
/* 187 */       if (!AggregateFuture.this.isCancelled())
/*     */       {
/* 189 */         boolean bool = AggregateFuture.addCausalChain(seen, AggregateFuture.this.trustedGetException());
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void handleOneInputDone(int index, Future<? extends InputT> future) {
/* 199 */       Preconditions.checkState((this.allMustSucceed || 
/* 200 */           !AggregateFuture.this.isDone() || AggregateFuture.this.isCancelled()), "Future was done before all dependencies completed");
/*     */ 
/*     */       
/*     */       try {
/* 204 */         Preconditions.checkState(future.isDone(), "Tried to set value from future which is not done");
/* 205 */         if (this.allMustSucceed) {
/* 206 */           if (future.isCancelled()) {
/*     */ 
/*     */             
/* 209 */             AggregateFuture.this.runningState = null;
/* 210 */             AggregateFuture.this.cancel(false);
/*     */           } else {
/*     */             
/* 213 */             InputT result = Futures.getDone((Future)future);
/* 214 */             if (this.collectsValues) {
/* 215 */               collectOneValue(this.allMustSucceed, index, result);
/*     */             }
/*     */           } 
/* 218 */         } else if (this.collectsValues && !future.isCancelled()) {
/* 219 */           collectOneValue(this.allMustSucceed, index, Futures.getDone((Future)future));
/*     */         } 
/* 221 */       } catch (ExecutionException e) {
/* 222 */         handleException(e.getCause());
/* 223 */       } catch (Throwable t) {
/* 224 */         handleException(t);
/*     */       } 
/*     */     }
/*     */     
/*     */     private void decrementCountAndMaybeComplete() {
/* 229 */       int newRemaining = decrementRemainingAndGet();
/* 230 */       Preconditions.checkState((newRemaining >= 0), "Less than 0 remaining futures");
/* 231 */       if (newRemaining == 0) {
/* 232 */         processCompleted();
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private void processCompleted() {
/* 239 */       if ((this.collectsValues & (!this.allMustSucceed ? 1 : 0)) != 0) {
/* 240 */         int i = 0;
/* 241 */         for (UnmodifiableIterator<ListenableFuture<? extends InputT>> unmodifiableIterator = this.futures.iterator(); unmodifiableIterator.hasNext(); ) { ListenableFuture<? extends InputT> listenable = unmodifiableIterator.next();
/* 242 */           handleOneInputDone(i++, listenable); }
/*     */       
/*     */       } 
/* 245 */       handleAllCompleted();
/*     */     }
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
/*     */     void releaseResourcesAfterFailure() {
/* 258 */       this.futures = null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     void interruptTask() {}
/*     */ 
/*     */ 
/*     */     
/*     */     abstract void collectOneValue(boolean param1Boolean, int param1Int, @Nullable InputT param1InputT);
/*     */ 
/*     */ 
/*     */     
/*     */     abstract void handleAllCompleted();
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean addCausalChain(Set<Throwable> seen, Throwable t) {
/* 276 */     for (; t != null; t = t.getCause()) {
/* 277 */       boolean firstTimeSeen = seen.add(t);
/* 278 */       if (!firstTimeSeen)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 285 */         return false;
/*     */       }
/*     */     } 
/* 288 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\commo\\util\concurrent\AggregateFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */