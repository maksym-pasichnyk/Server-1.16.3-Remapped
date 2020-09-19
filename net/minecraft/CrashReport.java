/*     */ package net.minecraft;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collector;
/*     */ import java.util.stream.Collectors;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class CrashReport
/*     */ {
/*  23 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   private final String title;
/*     */   private final Throwable exception;
/*  27 */   private final CrashReportCategory systemDetails = new CrashReportCategory(this, "System Details");
/*  28 */   private final List<CrashReportCategory> details = Lists.newArrayList();
/*     */   private File saveFile;
/*     */   private boolean trackingStackTrace = true;
/*  31 */   private StackTraceElement[] uncategorizedStackTrace = new StackTraceElement[0];
/*     */   
/*     */   public CrashReport(String debug1, Throwable debug2) {
/*  34 */     this.title = debug1;
/*  35 */     this.exception = debug2;
/*     */     
/*  37 */     initDetails();
/*     */   }
/*     */   
/*     */   private void initDetails() {
/*  41 */     this.systemDetails.setDetail("Minecraft Version", () -> SharedConstants.getCurrentVersion().getName());
/*  42 */     this.systemDetails.setDetail("Minecraft Version ID", () -> SharedConstants.getCurrentVersion().getId());
/*  43 */     this.systemDetails.setDetail("Operating System", () -> System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") version " + System.getProperty("os.version"));
/*  44 */     this.systemDetails.setDetail("Java Version", () -> System.getProperty("java.version") + ", " + System.getProperty("java.vendor"));
/*  45 */     this.systemDetails.setDetail("Java VM Version", () -> System.getProperty("java.vm.name") + " (" + System.getProperty("java.vm.info") + "), " + System.getProperty("java.vm.vendor"));
/*     */     
/*  47 */     this.systemDetails.setDetail("Memory", () -> {
/*     */           Runtime debug0 = Runtime.getRuntime();
/*     */           
/*     */           long debug1 = debug0.maxMemory();
/*     */           
/*     */           long debug3 = debug0.totalMemory();
/*     */           long debug5 = debug0.freeMemory();
/*     */           long debug7 = debug1 / 1024L / 1024L;
/*     */           long debug9 = debug3 / 1024L / 1024L;
/*     */           long debug11 = debug5 / 1024L / 1024L;
/*     */           return debug5 + " bytes (" + debug11 + " MB) / " + debug3 + " bytes (" + debug9 + " MB) up to " + debug1 + " bytes (" + debug7 + " MB)";
/*     */         });
/*  59 */     this.systemDetails.setDetail("CPUs", Integer.valueOf(Runtime.getRuntime().availableProcessors()));
/*     */     
/*  61 */     this.systemDetails.setDetail("JVM Flags", () -> {
/*     */           List<String> debug0 = Util.getVmArguments().collect((Collector)Collectors.toList());
/*     */           return String.format("%d total; %s", new Object[] { Integer.valueOf(debug0.size()), debug0.stream().collect(Collectors.joining(" ")) });
/*     */         });
/*     */   }
/*     */   
/*     */   public String getTitle() {
/*  68 */     return this.title;
/*     */   }
/*     */   
/*     */   public Throwable getException() {
/*  72 */     return this.exception;
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
/*     */   public void getDetails(StringBuilder debug1) {
/*  84 */     if ((this.uncategorizedStackTrace == null || this.uncategorizedStackTrace.length <= 0) && !this.details.isEmpty()) {
/*  85 */       this.uncategorizedStackTrace = (StackTraceElement[])ArrayUtils.subarray((Object[])((CrashReportCategory)this.details.get(0)).getStacktrace(), 0, 1);
/*     */     }
/*     */     
/*  88 */     if (this.uncategorizedStackTrace != null && this.uncategorizedStackTrace.length > 0) {
/*  89 */       debug1.append("-- Head --\n");
/*  90 */       debug1.append("Thread: ").append(Thread.currentThread().getName()).append("\n");
/*  91 */       debug1.append("Stacktrace:\n");
/*     */       
/*  93 */       for (StackTraceElement debug5 : this.uncategorizedStackTrace) {
/*  94 */         debug1.append("\t").append("at ").append(debug5);
/*  95 */         debug1.append("\n");
/*     */       } 
/*  97 */       debug1.append("\n");
/*     */     } 
/*     */     
/* 100 */     for (CrashReportCategory debug3 : this.details) {
/* 101 */       debug3.getDetails(debug1);
/* 102 */       debug1.append("\n\n");
/*     */     } 
/*     */     
/* 105 */     this.systemDetails.getDetails(debug1);
/*     */   }
/*     */   
/*     */   public String getExceptionMessage() {
/* 109 */     StringWriter debug1 = null;
/* 110 */     PrintWriter debug2 = null;
/* 111 */     Throwable debug3 = this.exception;
/*     */     
/* 113 */     if (debug3.getMessage() == null) {
/*     */       
/* 115 */       if (debug3 instanceof NullPointerException) {
/* 116 */         debug3 = new NullPointerException(this.title);
/* 117 */       } else if (debug3 instanceof StackOverflowError) {
/* 118 */         debug3 = new StackOverflowError(this.title);
/* 119 */       } else if (debug3 instanceof OutOfMemoryError) {
/* 120 */         debug3 = new OutOfMemoryError(this.title);
/*     */       } 
/*     */       
/* 123 */       debug3.setStackTrace(this.exception.getStackTrace());
/*     */     } 
/*     */     
/*     */     try {
/* 127 */       debug1 = new StringWriter();
/* 128 */       debug2 = new PrintWriter(debug1);
/* 129 */       debug3.printStackTrace(debug2);
/* 130 */       return debug1.toString();
/*     */     } finally {
/* 132 */       IOUtils.closeQuietly(debug1);
/* 133 */       IOUtils.closeQuietly(debug2);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getFriendlyReport() {
/* 138 */     StringBuilder debug1 = new StringBuilder();
/*     */     
/* 140 */     debug1.append("---- Minecraft Crash Report ----\n");
/* 141 */     debug1.append("// ");
/* 142 */     debug1.append(getErrorComment());
/* 143 */     debug1.append("\n\n");
/*     */     
/* 145 */     debug1.append("Time: ");
/* 146 */     debug1.append((new SimpleDateFormat()).format(new Date()));
/* 147 */     debug1.append("\n");
/*     */     
/* 149 */     debug1.append("Description: ");
/* 150 */     debug1.append(this.title);
/* 151 */     debug1.append("\n\n");
/*     */     
/* 153 */     debug1.append(getExceptionMessage());
/* 154 */     debug1.append("\n\nA detailed walkthrough of the error, its code path and all known details is as follows:\n");
/*     */     
/* 156 */     for (int debug2 = 0; debug2 < 87; debug2++) {
/* 157 */       debug1.append("-");
/*     */     }
/* 159 */     debug1.append("\n\n");
/* 160 */     getDetails(debug1);
/*     */     
/* 162 */     return debug1.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean saveToFile(File debug1) {
/* 170 */     if (this.saveFile != null) {
/* 171 */       return false;
/*     */     }
/* 173 */     if (debug1.getParentFile() != null) {
/* 174 */       debug1.getParentFile().mkdirs();
/*     */     }
/*     */     
/* 177 */     Writer debug2 = null;
/*     */     try {
/* 179 */       debug2 = new OutputStreamWriter(new FileOutputStream(debug1), StandardCharsets.UTF_8);
/* 180 */       debug2.write(getFriendlyReport());
/*     */       
/* 182 */       this.saveFile = debug1;
/* 183 */       return true;
/* 184 */     } catch (Throwable debug3) {
/* 185 */       LOGGER.error("Could not save crash report to {}", debug1, debug3);
/* 186 */       return false;
/*     */     } finally {
/* 188 */       IOUtils.closeQuietly(debug2);
/*     */     } 
/*     */   }
/*     */   
/*     */   public CrashReportCategory getSystemDetails() {
/* 193 */     return this.systemDetails;
/*     */   }
/*     */   
/*     */   public CrashReportCategory addCategory(String debug1) {
/* 197 */     return addCategory(debug1, 1);
/*     */   }
/*     */   
/*     */   public CrashReportCategory addCategory(String debug1, int debug2) {
/* 201 */     CrashReportCategory debug3 = new CrashReportCategory(this, debug1);
/*     */     
/* 203 */     if (this.trackingStackTrace) {
/* 204 */       int debug4 = debug3.fillInStackTrace(debug2);
/* 205 */       StackTraceElement[] debug5 = this.exception.getStackTrace();
/* 206 */       StackTraceElement debug6 = null;
/* 207 */       StackTraceElement debug7 = null;
/*     */       
/* 209 */       int debug8 = debug5.length - debug4;
/* 210 */       if (debug8 < 0) {
/* 211 */         System.out.println("Negative index in crash report handler (" + debug5.length + "/" + debug4 + ")");
/*     */       }
/*     */       
/* 214 */       if (debug5 != null && 0 <= debug8 && debug8 < debug5.length) {
/* 215 */         debug6 = debug5[debug8];
/*     */         
/* 217 */         if (debug5.length + 1 - debug4 < debug5.length) {
/* 218 */           debug7 = debug5[debug5.length + 1 - debug4];
/*     */         }
/*     */       } 
/*     */       
/* 222 */       this.trackingStackTrace = debug3.validateStackTrace(debug6, debug7);
/*     */       
/* 224 */       if (debug4 > 0 && !this.details.isEmpty()) {
/* 225 */         CrashReportCategory debug9 = this.details.get(this.details.size() - 1);
/* 226 */         debug9.trimStacktrace(debug4);
/* 227 */       } else if (debug5 != null && debug5.length >= debug4 && 0 <= debug8 && debug8 < debug5.length) {
/* 228 */         this.uncategorizedStackTrace = new StackTraceElement[debug8];
/* 229 */         System.arraycopy(debug5, 0, this.uncategorizedStackTrace, 0, this.uncategorizedStackTrace.length);
/*     */       } else {
/* 231 */         this.trackingStackTrace = false;
/*     */       } 
/*     */     } 
/*     */     
/* 235 */     this.details.add(debug3);
/* 236 */     return debug3;
/*     */   }
/*     */ 
/*     */   
/*     */   private static String getErrorComment() {
/* 241 */     String[] debug0 = { "Who set us up the TNT?", "Everything's going to plan. No, really, that was supposed to happen.", "Uh... Did I do that?", "Oops.", "Why did you do that?", "I feel sad now :(", "My bad.", "I'm sorry, Dave.", "I let you down. Sorry :(", "On the bright side, I bought you a teddy bear!", "Daisy, daisy...", "Oh - I know what I did wrong!", "Hey, that tickles! Hehehe!", "I blame Dinnerbone.", "You should try our sister game, Minceraft!", "Don't be sad. I'll do better next time, I promise!", "Don't be sad, have a hug! <3", "I just don't know what went wrong :(", "Shall we play a game?", "Quite honestly, I wouldn't worry myself about that.", "I bet Cylons wouldn't have this problem.", "Sorry :(", "Surprise! Haha. Well, this is awkward.", "Would you like a cupcake?", "Hi. I'm Minecraft, and I'm a crashaholic.", "Ooh. Shiny.", "This doesn't make any sense!", "Why is it breaking :(", "Don't do that.", "Ouch. That hurt :(", "You're mean.", "This is a token for 1 free hug. Redeem at your nearest Mojangsta: [~~HUG~~]", "There are four lights!", "But it works on my machine." };
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
/*     */     try {
/* 279 */       return debug0[(int)(Util.getNanos() % debug0.length)];
/* 280 */     } catch (Throwable debug1) {
/* 281 */       return "Witty comment unavailable :(";
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static CrashReport forThrowable(Throwable debug0, String debug1) {
/*     */     CrashReport debug2;
/* 288 */     while (debug0 instanceof java.util.concurrent.CompletionException && debug0.getCause() != null) {
/* 289 */       debug0 = debug0.getCause();
/*     */     }
/*     */     
/* 292 */     if (debug0 instanceof ReportedException) {
/* 293 */       debug2 = ((ReportedException)debug0).getReport();
/*     */     } else {
/* 295 */       debug2 = new CrashReport(debug1, debug0);
/*     */     } 
/*     */     
/* 298 */     return debug2;
/*     */   }
/*     */   
/*     */   public static void preload() {
/* 302 */     (new CrashReport("Don't panic!", new Throwable())).getFriendlyReport();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\CrashReport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */