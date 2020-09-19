/*    */ package net.minecraft.server.level;
/*    */ 
/*    */ import java.util.Objects;
/*    */ 
/*    */ public final class Ticket<T> implements Comparable<Ticket<?>> {
/*    */   private final TicketType<T> type;
/*    */   private final int ticketLevel;
/*    */   private final T key;
/*    */   private long createdTick;
/*    */   
/*    */   protected Ticket(TicketType<T> debug1, int debug2, T debug3) {
/* 12 */     this.type = debug1;
/* 13 */     this.ticketLevel = debug2;
/* 14 */     this.key = debug3;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int compareTo(Ticket<?> debug1) {
/* 20 */     int debug2 = Integer.compare(this.ticketLevel, debug1.ticketLevel);
/* 21 */     if (debug2 != 0) {
/* 22 */       return debug2;
/*    */     }
/*    */     
/* 25 */     int debug3 = Integer.compare(System.identityHashCode(this.type), System.identityHashCode(debug1.type));
/* 26 */     if (debug3 != 0) {
/* 27 */       return debug3;
/*    */     }
/*    */     
/* 30 */     return this.type.getComparator().compare(this.key, debug1.key);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object debug1) {
/* 35 */     if (this == debug1) {
/* 36 */       return true;
/*    */     }
/* 38 */     if (!(debug1 instanceof Ticket)) {
/* 39 */       return false;
/*    */     }
/* 41 */     Ticket<?> debug2 = (Ticket)debug1;
/* 42 */     return (this.ticketLevel == debug2.ticketLevel && Objects.equals(this.type, debug2.type) && Objects.equals(this.key, debug2.key));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 47 */     return Objects.hash(new Object[] { this.type, Integer.valueOf(this.ticketLevel), this.key });
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 52 */     return "Ticket[" + this.type + " " + this.ticketLevel + " (" + this.key + ")] at " + this.createdTick;
/*    */   }
/*    */   
/*    */   public TicketType<T> getType() {
/* 56 */     return this.type;
/*    */   }
/*    */   
/*    */   public int getTicketLevel() {
/* 60 */     return this.ticketLevel;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void setCreatedTick(long debug1) {
/* 65 */     this.createdTick = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean timedOut(long debug1) {
/* 70 */     long debug3 = this.type.timeout();
/* 71 */     return (debug3 != 0L && debug1 - this.createdTick > debug3);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\level\Ticket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */