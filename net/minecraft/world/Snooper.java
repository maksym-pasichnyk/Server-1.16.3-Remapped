/*     */ package net.minecraft.world;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.Map;
/*     */ import java.util.Timer;
/*     */ import java.util.UUID;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Snooper
/*     */ {
/*  21 */   private final Map<String, Object> fixedData = Maps.newHashMap();
/*  22 */   private final Map<String, Object> dynamicData = Maps.newHashMap();
/*     */   
/*  24 */   private final String token = UUID.randomUUID().toString();
/*     */   private final URL url;
/*     */   private final SnooperPopulator populator;
/*  27 */   private final Timer timer = new Timer("Snooper Timer", true);
/*  28 */   private final Object lock = new Object();
/*     */   
/*     */   private final long startupTime;
/*     */   private boolean started;
/*     */   
/*     */   public Snooper(String debug1, SnooperPopulator debug2, long debug3) {
/*     */     try {
/*  35 */       this.url = new URL("http://snoop.minecraft.net/" + debug1 + "?version=" + '\002');
/*  36 */     } catch (MalformedURLException debug5) {
/*  37 */       throw new IllegalArgumentException();
/*     */     } 
/*     */     
/*  40 */     this.populator = debug2;
/*  41 */     this.startupTime = debug3;
/*     */   }
/*     */   
/*     */   public void start() {
/*  45 */     if (!this.started);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void prepare() {
/*  96 */     setFixedData("memory_total", Long.valueOf(Runtime.getRuntime().totalMemory()));
/*  97 */     setFixedData("memory_max", Long.valueOf(Runtime.getRuntime().maxMemory()));
/*  98 */     setFixedData("memory_free", Long.valueOf(Runtime.getRuntime().freeMemory()));
/*  99 */     setFixedData("cpu_cores", Integer.valueOf(Runtime.getRuntime().availableProcessors()));
/*     */     
/* 101 */     this.populator.populateSnooper(this);
/*     */   }
/*     */   
/*     */   public void setDynamicData(String debug1, Object debug2) {
/* 105 */     synchronized (this.lock) {
/* 106 */       this.dynamicData.put(debug1, debug2);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setFixedData(String debug1, Object debug2) {
/* 111 */     synchronized (this.lock) {
/* 112 */       this.fixedData.put(debug1, debug2);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStarted() {
/* 135 */     return this.started;
/*     */   }
/*     */   
/*     */   public void interrupt() {
/* 139 */     this.timer.cancel();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getStartupTime() {
/* 147 */     return this.startupTime;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\Snooper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */