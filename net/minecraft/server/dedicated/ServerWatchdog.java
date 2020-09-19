/*    */ package net.minecraft.server.dedicated;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.lang.management.ManagementFactory;
/*    */ import java.lang.management.ThreadInfo;
/*    */ import java.lang.management.ThreadMXBean;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ import java.util.Locale;
/*    */ import java.util.Timer;
/*    */ import java.util.TimerTask;
/*    */ import net.minecraft.CrashReport;
/*    */ import net.minecraft.CrashReportCategory;
/*    */ import net.minecraft.Util;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class ServerWatchdog
/*    */   implements Runnable
/*    */ {
/* 21 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */   
/*    */   private final DedicatedServer server;
/*    */   
/*    */   private final long maxTickTime;
/*    */ 
/*    */   
/*    */   public ServerWatchdog(DedicatedServer debug1) {
/* 29 */     this.server = debug1;
/* 30 */     this.maxTickTime = debug1.getMaxTickLength();
/*    */   }
/*    */ 
/*    */   
/*    */   public void run() {
/* 35 */     while (this.server.isRunning()) {
/* 36 */       long debug1 = this.server.getNextTickTime();
/* 37 */       long debug3 = Util.getMillis();
/* 38 */       long debug5 = debug3 - debug1;
/*    */       
/* 40 */       if (debug5 > this.maxTickTime) {
/* 41 */         LOGGER.fatal("A single server tick took {} seconds (should be max {})", String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf((float)debug5 / 1000.0F) }), String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf(0.05F) }));
/* 42 */         LOGGER.fatal("Considering it to be crashed, server will forcibly shutdown.");
/*    */         
/* 44 */         ThreadMXBean debug7 = ManagementFactory.getThreadMXBean();
/* 45 */         ThreadInfo[] debug8 = debug7.dumpAllThreads(true, true);
/*    */         
/* 47 */         StringBuilder debug9 = new StringBuilder();
/* 48 */         Error debug10 = new Error();
/*    */         
/* 50 */         for (ThreadInfo debug14 : debug8) {
/* 51 */           if (debug14.getThreadId() == this.server.getRunningThread().getId()) {
/* 52 */             debug10.setStackTrace(debug14.getStackTrace());
/*    */           }
/*    */           
/* 55 */           debug9.append(debug14);
/* 56 */           debug9.append("\n");
/*    */         } 
/*    */         
/* 59 */         CrashReport debug11 = new CrashReport("Watching Server", debug10);
/* 60 */         this.server.fillReport(debug11);
/* 61 */         CrashReportCategory debug12 = debug11.addCategory("Thread Dump");
/* 62 */         debug12.setDetail("Threads", debug9);
/*    */         
/* 64 */         File debug13 = new File(new File(this.server.getServerDirectory(), "crash-reports"), "crash-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-server.txt");
/* 65 */         if (debug11.saveToFile(debug13)) {
/* 66 */           LOGGER.error("This crash report has been saved to: {}", debug13.getAbsolutePath());
/*    */         } else {
/* 68 */           LOGGER.error("We were unable to save this crash report to disk.");
/*    */         } 
/*    */         
/* 71 */         exit();
/*    */       } 
/*    */       
/*    */       try {
/* 75 */         Thread.sleep(debug1 + this.maxTickTime - debug3);
/* 76 */       } catch (InterruptedException interruptedException) {}
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   private void exit() {
/*    */     try {
/* 83 */       Timer debug1 = new Timer();
/* 84 */       debug1.schedule(new TimerTask()
/*    */           {
/*    */             public void run() {
/* 87 */               Runtime.getRuntime().halt(1);
/*    */             }
/*    */           },  10000L);
/*    */       
/* 91 */       System.exit(1);
/* 92 */     } catch (Throwable debug1) {
/* 93 */       Runtime.getRuntime().halt(1);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\dedicated\ServerWatchdog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */