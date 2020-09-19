/*     */ package org.apache.logging.log4j.core.appender.rolling;
/*     */ 
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.util.Integers;
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
/*     */ @Plugin(name = "TimeBasedTriggeringPolicy", category = "Core", printObject = true)
/*     */ public final class TimeBasedTriggeringPolicy
/*     */   extends AbstractTriggeringPolicy
/*     */ {
/*     */   private long nextRolloverMillis;
/*     */   private final int interval;
/*     */   private final boolean modulate;
/*     */   private RollingFileManager manager;
/*     */   
/*     */   private TimeBasedTriggeringPolicy(int interval, boolean modulate) {
/*  39 */     this.interval = interval;
/*  40 */     this.modulate = modulate;
/*     */   }
/*     */   
/*     */   public int getInterval() {
/*  44 */     return this.interval;
/*     */   }
/*     */   
/*     */   public long getNextRolloverMillis() {
/*  48 */     return this.nextRolloverMillis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initialize(RollingFileManager aManager) {
/*  57 */     this.manager = aManager;
/*     */ 
/*     */     
/*  60 */     aManager.getPatternProcessor().getNextTime(aManager.getFileTime(), this.interval, this.modulate);
/*     */     
/*  62 */     this.nextRolloverMillis = aManager.getPatternProcessor().getNextTime(aManager.getFileTime(), this.interval, this.modulate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTriggeringEvent(LogEvent event) {
/*  72 */     if (this.manager.getFileSize() == 0L) {
/*  73 */       return false;
/*     */     }
/*  75 */     long nowMillis = event.getTimeMillis();
/*  76 */     if (nowMillis >= this.nextRolloverMillis) {
/*  77 */       this.nextRolloverMillis = this.manager.getPatternProcessor().getNextTime(nowMillis, this.interval, this.modulate);
/*  78 */       return true;
/*     */     } 
/*  80 */     return false;
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
/*     */   @PluginFactory
/*     */   public static TimeBasedTriggeringPolicy createPolicy(@PluginAttribute("interval") String interval, @PluginAttribute("modulate") String modulate) {
/*  93 */     int increment = Integers.parseInt(interval, 1);
/*  94 */     boolean mod = Boolean.parseBoolean(modulate);
/*  95 */     return new TimeBasedTriggeringPolicy(increment, mod);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 100 */     return "TimeBasedTriggeringPolicy(nextRolloverMillis=" + this.nextRolloverMillis + ", interval=" + this.interval + ", modulate=" + this.modulate + ")";
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\rolling\TimeBasedTriggeringPolicy.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */