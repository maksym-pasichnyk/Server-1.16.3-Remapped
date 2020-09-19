/*     */ package org.apache.commons.lang3.concurrent;
/*     */ 
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyChangeSupport;
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
/*     */ public abstract class AbstractCircuitBreaker<T>
/*     */   implements CircuitBreaker<T>
/*     */ {
/*     */   public static final String PROPERTY_NAME = "open";
/*  37 */   protected final AtomicReference<State> state = new AtomicReference<State>(State.CLOSED);
/*     */ 
/*     */ 
/*     */   
/*     */   private final PropertyChangeSupport changeSupport;
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractCircuitBreaker() {
/*  46 */     this.changeSupport = new PropertyChangeSupport(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/*  53 */     return isOpen(this.state.get());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isClosed() {
/*  60 */     return !isOpen();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean checkState();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean incrementAndCheckState(T paramT);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/*  77 */     changeState(State.CLOSED);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void open() {
/*  84 */     changeState(State.OPEN);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static boolean isOpen(State state) {
/*  94 */     return (state == State.OPEN);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void changeState(State newState) {
/* 104 */     if (this.state.compareAndSet(newState.oppositeState(), newState)) {
/* 105 */       this.changeSupport.firePropertyChange("open", !isOpen(newState), isOpen(newState));
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
/*     */   public void addChangeListener(PropertyChangeListener listener) {
/* 117 */     this.changeSupport.addPropertyChangeListener(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeChangeListener(PropertyChangeListener listener) {
/* 126 */     this.changeSupport.removePropertyChangeListener(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected enum State
/*     */   {
/* 136 */     CLOSED
/*     */     {
/*     */ 
/*     */       
/*     */       public State oppositeState()
/*     */       {
/* 142 */         return OPEN;
/*     */       }
/*     */     },
/*     */     
/* 146 */     OPEN
/*     */     {
/*     */ 
/*     */       
/*     */       public State oppositeState()
/*     */       {
/* 152 */         return CLOSED;
/*     */       }
/*     */     };
/*     */     
/*     */     public abstract State oppositeState();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\concurrent\AbstractCircuitBreaker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */