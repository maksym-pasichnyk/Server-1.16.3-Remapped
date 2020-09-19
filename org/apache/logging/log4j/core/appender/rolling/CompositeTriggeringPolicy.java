/*     */ package org.apache.logging.log4j.core.appender.rolling;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.core.LifeCycle;
/*     */ import org.apache.logging.log4j.core.LifeCycle2;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
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
/*     */ @Plugin(name = "Policies", category = "Core", printObject = true)
/*     */ public final class CompositeTriggeringPolicy
/*     */   extends AbstractTriggeringPolicy
/*     */ {
/*     */   private final TriggeringPolicy[] triggeringPolicies;
/*     */   
/*     */   private CompositeTriggeringPolicy(TriggeringPolicy... triggeringPolicies) {
/*  39 */     this.triggeringPolicies = triggeringPolicies;
/*     */   }
/*     */   
/*     */   public TriggeringPolicy[] getTriggeringPolicies() {
/*  43 */     return this.triggeringPolicies;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initialize(RollingFileManager manager) {
/*  52 */     for (TriggeringPolicy triggeringPolicy : this.triggeringPolicies) {
/*  53 */       triggeringPolicy.initialize(manager);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTriggeringEvent(LogEvent event) {
/*  64 */     for (TriggeringPolicy triggeringPolicy : this.triggeringPolicies) {
/*  65 */       if (triggeringPolicy.isTriggeringEvent(event)) {
/*  66 */         return true;
/*     */       }
/*     */     } 
/*  69 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginFactory
/*     */   public static CompositeTriggeringPolicy createPolicy(@PluginElement("Policies") TriggeringPolicy... triggeringPolicy) {
/*  80 */     return new CompositeTriggeringPolicy(triggeringPolicy);
/*     */   }
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/*     */     int i;
/*  85 */     setStopping();
/*  86 */     boolean stopped = true;
/*  87 */     for (TriggeringPolicy triggeringPolicy : this.triggeringPolicies) {
/*  88 */       if (triggeringPolicy instanceof LifeCycle2) {
/*  89 */         stopped &= ((LifeCycle2)triggeringPolicy).stop(timeout, timeUnit);
/*  90 */       } else if (triggeringPolicy instanceof LifeCycle) {
/*  91 */         ((LifeCycle)triggeringPolicy).stop();
/*  92 */         i = stopped & true;
/*     */       } 
/*     */     } 
/*  95 */     setStopped();
/*  96 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 101 */     return "CompositeTriggeringPolicy(policies=" + Arrays.toString((Object[])this.triggeringPolicies) + ")";
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\rolling\CompositeTriggeringPolicy.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */