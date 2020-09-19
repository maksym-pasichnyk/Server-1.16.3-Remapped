/*     */ package org.apache.commons.lang3.concurrent;
/*     */ 
/*     */ import java.util.EnumMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicReference;
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
/*     */ public class EventCountCircuitBreaker
/*     */   extends AbstractCircuitBreaker<Integer>
/*     */ {
/* 141 */   private static final Map<AbstractCircuitBreaker.State, StateStrategy> STRATEGY_MAP = createStrategyMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final AtomicReference<CheckIntervalData> checkIntervalData;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int openingThreshold;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final long openingInterval;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int closingThreshold;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final long closingInterval;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EventCountCircuitBreaker(int openingThreshold, long openingInterval, TimeUnit openingUnit, int closingThreshold, long closingInterval, TimeUnit closingUnit) {
/* 178 */     this.checkIntervalData = new AtomicReference<CheckIntervalData>(new CheckIntervalData(0, 0L));
/* 179 */     this.openingThreshold = openingThreshold;
/* 180 */     this.openingInterval = openingUnit.toNanos(openingInterval);
/* 181 */     this.closingThreshold = closingThreshold;
/* 182 */     this.closingInterval = closingUnit.toNanos(closingInterval);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EventCountCircuitBreaker(int openingThreshold, long checkInterval, TimeUnit checkUnit, int closingThreshold) {
/* 200 */     this(openingThreshold, checkInterval, checkUnit, closingThreshold, checkInterval, checkUnit);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public EventCountCircuitBreaker(int threshold, long checkInterval, TimeUnit checkUnit) {
/* 215 */     this(threshold, checkInterval, checkUnit, threshold);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getOpeningThreshold() {
/* 226 */     return this.openingThreshold;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getOpeningInterval() {
/* 235 */     return this.openingInterval;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getClosingThreshold() {
/* 246 */     return this.closingThreshold;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getClosingInterval() {
/* 255 */     return this.closingInterval;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean checkState() {
/* 265 */     return performStateCheck(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean incrementAndCheckState(Integer increment) throws CircuitBreakingException {
/* 274 */     return performStateCheck(1);
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
/*     */   public boolean incrementAndCheckState() {
/* 286 */     return incrementAndCheckState(Integer.valueOf(1));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void open() {
/* 297 */     super.open();
/* 298 */     this.checkIntervalData.set(new CheckIntervalData(0, now()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 309 */     super.close();
/* 310 */     this.checkIntervalData.set(new CheckIntervalData(0, now()));
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
/*     */   private boolean performStateCheck(int increment) {
/*     */     CheckIntervalData currentData;
/*     */     CheckIntervalData nextData;
/*     */     AbstractCircuitBreaker.State currentState;
/*     */     do {
/* 326 */       long time = now();
/* 327 */       currentState = this.state.get();
/* 328 */       currentData = this.checkIntervalData.get();
/* 329 */       nextData = nextCheckIntervalData(increment, currentData, currentState, time);
/* 330 */     } while (!updateCheckIntervalData(currentData, nextData));
/*     */ 
/*     */ 
/*     */     
/* 334 */     if (stateStrategy(currentState).isStateTransition(this, currentData, nextData)) {
/* 335 */       currentState = currentState.oppositeState();
/* 336 */       changeStateAndStartNewCheckInterval(currentState);
/*     */     } 
/* 338 */     return !isOpen(currentState);
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
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean updateCheckIntervalData(CheckIntervalData currentData, CheckIntervalData nextData) {
/* 353 */     return (currentData == nextData || this.checkIntervalData
/* 354 */       .compareAndSet(currentData, nextData));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void changeStateAndStartNewCheckInterval(AbstractCircuitBreaker.State newState) {
/* 364 */     changeState(newState);
/* 365 */     this.checkIntervalData.set(new CheckIntervalData(0, now()));
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CheckIntervalData nextCheckIntervalData(int increment, CheckIntervalData currentData, AbstractCircuitBreaker.State currentState, long time) {
/*     */     CheckIntervalData nextData;
/* 382 */     if (stateStrategy(currentState).isCheckIntervalFinished(this, currentData, time)) {
/* 383 */       nextData = new CheckIntervalData(increment, time);
/*     */     } else {
/* 385 */       nextData = currentData.increment(increment);
/*     */     } 
/* 387 */     return nextData;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   long now() {
/* 397 */     return System.nanoTime();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static StateStrategy stateStrategy(AbstractCircuitBreaker.State state) {
/* 408 */     StateStrategy strategy = STRATEGY_MAP.get(state);
/* 409 */     return strategy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Map<AbstractCircuitBreaker.State, StateStrategy> createStrategyMap() {
/* 419 */     Map<AbstractCircuitBreaker.State, StateStrategy> map = new EnumMap<AbstractCircuitBreaker.State, StateStrategy>(AbstractCircuitBreaker.State.class);
/* 420 */     map.put(AbstractCircuitBreaker.State.CLOSED, new StateStrategyClosed());
/* 421 */     map.put(AbstractCircuitBreaker.State.OPEN, new StateStrategyOpen());
/* 422 */     return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class CheckIntervalData
/*     */   {
/*     */     private final int eventCount;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final long checkIntervalStart;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public CheckIntervalData(int count, long intervalStart) {
/* 444 */       this.eventCount = count;
/* 445 */       this.checkIntervalStart = intervalStart;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getEventCount() {
/* 454 */       return this.eventCount;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public long getCheckIntervalStart() {
/* 463 */       return this.checkIntervalStart;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public CheckIntervalData increment(int delta) {
/* 474 */       return (delta != 0) ? new CheckIntervalData(getEventCount() + delta, 
/* 475 */           getCheckIntervalStart()) : this;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static abstract class StateStrategy
/*     */   {
/*     */     private StateStrategy() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isCheckIntervalFinished(EventCountCircuitBreaker breaker, EventCountCircuitBreaker.CheckIntervalData currentData, long now) {
/* 495 */       return (now - currentData.getCheckIntervalStart() > fetchCheckInterval(breaker));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public abstract boolean isStateTransition(EventCountCircuitBreaker param1EventCountCircuitBreaker, EventCountCircuitBreaker.CheckIntervalData param1CheckIntervalData1, EventCountCircuitBreaker.CheckIntervalData param1CheckIntervalData2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected abstract long fetchCheckInterval(EventCountCircuitBreaker param1EventCountCircuitBreaker);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class StateStrategyClosed
/*     */     extends StateStrategy
/*     */   {
/*     */     private StateStrategyClosed() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isStateTransition(EventCountCircuitBreaker breaker, EventCountCircuitBreaker.CheckIntervalData currentData, EventCountCircuitBreaker.CheckIntervalData nextData) {
/* 532 */       return (nextData.getEventCount() > breaker.getOpeningThreshold());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected long fetchCheckInterval(EventCountCircuitBreaker breaker) {
/* 540 */       return breaker.getOpeningInterval();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class StateStrategyOpen
/*     */     extends StateStrategy
/*     */   {
/*     */     private StateStrategyOpen() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isStateTransition(EventCountCircuitBreaker breaker, EventCountCircuitBreaker.CheckIntervalData currentData, EventCountCircuitBreaker.CheckIntervalData nextData) {
/* 554 */       return (nextData.getCheckIntervalStart() != currentData
/* 555 */         .getCheckIntervalStart() && currentData
/* 556 */         .getEventCount() < breaker.getClosingThreshold());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected long fetchCheckInterval(EventCountCircuitBreaker breaker) {
/* 564 */       return breaker.getClosingInterval();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\concurrent\EventCountCircuitBreaker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */