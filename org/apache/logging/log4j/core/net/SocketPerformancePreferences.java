/*    */ package org.apache.logging.log4j.core.net;
/*    */ 
/*    */ import java.net.Socket;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*    */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
/*    */ import org.apache.logging.log4j.core.util.Builder;
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
/*    */ @Plugin(name = "SocketPerformancePreferences", category = "Core", printObject = true)
/*    */ public class SocketPerformancePreferences
/*    */   implements Builder<SocketPerformancePreferences>, Cloneable
/*    */ {
/*    */   @PluginBuilderAttribute
/*    */   @Required
/*    */   private int bandwidth;
/*    */   @PluginBuilderAttribute
/*    */   @Required
/*    */   private int connectionTime;
/*    */   @PluginBuilderAttribute
/*    */   @Required
/*    */   private int latency;
/*    */   
/*    */   @PluginBuilderFactory
/*    */   public static SocketPerformancePreferences newBuilder() {
/* 40 */     return new SocketPerformancePreferences();
/*    */   }
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
/*    */   public void apply(Socket socket) {
/* 56 */     socket.setPerformancePreferences(this.connectionTime, this.latency, this.bandwidth);
/*    */   }
/*    */ 
/*    */   
/*    */   public SocketPerformancePreferences build() {
/*    */     try {
/* 62 */       return (SocketPerformancePreferences)clone();
/* 63 */     } catch (CloneNotSupportedException e) {
/* 64 */       throw new IllegalStateException(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public int getBandwidth() {
/* 69 */     return this.bandwidth;
/*    */   }
/*    */   
/*    */   public int getConnectionTime() {
/* 73 */     return this.connectionTime;
/*    */   }
/*    */   
/*    */   public int getLatency() {
/* 77 */     return this.latency;
/*    */   }
/*    */   
/*    */   public void setBandwidth(int bandwidth) {
/* 81 */     this.bandwidth = bandwidth;
/*    */   }
/*    */   
/*    */   public void setConnectionTime(int connectionTime) {
/* 85 */     this.connectionTime = connectionTime;
/*    */   }
/*    */   
/*    */   public void setLatency(int latency) {
/* 89 */     this.latency = latency;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 94 */     return "SocketPerformancePreferences [bandwidth=" + this.bandwidth + ", connectionTime=" + this.connectionTime + ", latency=" + this.latency + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\net\SocketPerformancePreferences.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */