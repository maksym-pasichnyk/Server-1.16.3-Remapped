/*    */ package io.netty.handler.timeout;
/*    */ 
/*    */ import io.netty.util.internal.ObjectUtil;
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
/*    */ public class IdleStateEvent
/*    */ {
/* 25 */   public static final IdleStateEvent FIRST_READER_IDLE_STATE_EVENT = new IdleStateEvent(IdleState.READER_IDLE, true);
/* 26 */   public static final IdleStateEvent READER_IDLE_STATE_EVENT = new IdleStateEvent(IdleState.READER_IDLE, false);
/* 27 */   public static final IdleStateEvent FIRST_WRITER_IDLE_STATE_EVENT = new IdleStateEvent(IdleState.WRITER_IDLE, true);
/* 28 */   public static final IdleStateEvent WRITER_IDLE_STATE_EVENT = new IdleStateEvent(IdleState.WRITER_IDLE, false);
/* 29 */   public static final IdleStateEvent FIRST_ALL_IDLE_STATE_EVENT = new IdleStateEvent(IdleState.ALL_IDLE, true);
/* 30 */   public static final IdleStateEvent ALL_IDLE_STATE_EVENT = new IdleStateEvent(IdleState.ALL_IDLE, false);
/*    */ 
/*    */ 
/*    */   
/*    */   private final IdleState state;
/*    */ 
/*    */   
/*    */   private final boolean first;
/*    */ 
/*    */ 
/*    */   
/*    */   protected IdleStateEvent(IdleState state, boolean first) {
/* 42 */     this.state = (IdleState)ObjectUtil.checkNotNull(state, "state");
/* 43 */     this.first = first;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public IdleState state() {
/* 50 */     return this.state;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isFirst() {
/* 57 */     return this.first;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\timeout\IdleStateEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */