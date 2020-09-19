/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.lang.management.ThreadInfo;
/*     */ import java.lang.management.ThreadMXBean;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.util.StringBuilderFormattable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @AsynchronouslyFormattable
/*     */ public class ThreadDumpMessage
/*     */   implements Message, StringBuilderFormattable
/*     */ {
/*     */   private static final long serialVersionUID = -1103400781608841088L;
/*     */   private static final ThreadInfoFactory FACTORY;
/*     */   private volatile Map<ThreadInformation, StackTraceElement[]> threads;
/*     */   private final String title;
/*     */   private String formattedMessage;
/*     */   
/*     */   static {
/*  49 */     Method[] methods = ThreadInfo.class.getMethods();
/*  50 */     boolean basic = true;
/*  51 */     for (Method method : methods) {
/*  52 */       if (method.getName().equals("getLockInfo")) {
/*  53 */         basic = false;
/*     */         break;
/*     */       } 
/*     */     } 
/*  57 */     FACTORY = basic ? new BasicThreadInfoFactory() : new ExtendedThreadInfoFactory();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ThreadDumpMessage(String title) {
/*  65 */     this.title = (title == null) ? "" : title;
/*  66 */     this.threads = FACTORY.createThreadInfo();
/*     */   }
/*     */   
/*     */   private ThreadDumpMessage(String formattedMsg, String title) {
/*  70 */     this.formattedMessage = formattedMsg;
/*  71 */     this.title = (title == null) ? "" : title;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  76 */     return getFormattedMessage();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormattedMessage() {
/*  85 */     if (this.formattedMessage != null) {
/*  86 */       return this.formattedMessage;
/*     */     }
/*  88 */     StringBuilder sb = new StringBuilder(255);
/*  89 */     formatTo(sb);
/*  90 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public void formatTo(StringBuilder sb) {
/*  95 */     sb.append(this.title);
/*  96 */     if (this.title.length() > 0) {
/*  97 */       sb.append('\n');
/*     */     }
/*  99 */     for (Map.Entry<ThreadInformation, StackTraceElement[]> entry : this.threads.entrySet()) {
/* 100 */       ThreadInformation info = entry.getKey();
/* 101 */       info.printThreadInfo(sb);
/* 102 */       info.printStack(sb, entry.getValue());
/* 103 */       sb.append('\n');
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormat() {
/* 113 */     return (this.title == null) ? "" : this.title;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] getParameters() {
/* 123 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object writeReplace() {
/* 131 */     return new ThreadDumpMessageProxy(this);
/*     */   }
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream stream) throws InvalidObjectException {
/* 136 */     throw new InvalidObjectException("Proxy required");
/*     */   }
/*     */ 
/*     */   
/*     */   private static class ThreadDumpMessageProxy
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = -3476620450287648269L;
/*     */     
/*     */     private final String formattedMsg;
/*     */     private final String title;
/*     */     
/*     */     ThreadDumpMessageProxy(ThreadDumpMessage msg) {
/* 149 */       this.formattedMsg = msg.getFormattedMessage();
/* 150 */       this.title = msg.title;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected Object readResolve() {
/* 158 */       return new ThreadDumpMessage(this.formattedMsg, this.title);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static interface ThreadInfoFactory
/*     */   {
/*     */     Map<ThreadInformation, StackTraceElement[]> createThreadInfo();
/*     */   }
/*     */ 
/*     */   
/*     */   private static class BasicThreadInfoFactory
/*     */     implements ThreadInfoFactory
/*     */   {
/*     */     private BasicThreadInfoFactory() {}
/*     */     
/*     */     public Map<ThreadInformation, StackTraceElement[]> createThreadInfo() {
/* 175 */       Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
/* 176 */       Map<ThreadInformation, StackTraceElement[]> threads = (Map)new HashMap<>(map.size());
/*     */       
/* 178 */       for (Map.Entry<Thread, StackTraceElement[]> entry : map.entrySet()) {
/* 179 */         threads.put(new BasicThreadInformation(entry.getKey()), entry.getValue());
/*     */       }
/* 181 */       return threads;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ExtendedThreadInfoFactory
/*     */     implements ThreadInfoFactory
/*     */   {
/*     */     private ExtendedThreadInfoFactory() {}
/*     */     
/*     */     public Map<ThreadInformation, StackTraceElement[]> createThreadInfo() {
/* 191 */       ThreadMXBean bean = ManagementFactory.getThreadMXBean();
/* 192 */       ThreadInfo[] array = bean.dumpAllThreads(true, true);
/*     */       
/* 194 */       Map<ThreadInformation, StackTraceElement[]> threads = (Map)new HashMap<>(array.length);
/*     */       
/* 196 */       for (ThreadInfo info : array) {
/* 197 */         threads.put(new ExtendedThreadInformation(info), info.getStackTrace());
/*     */       }
/* 199 */       return threads;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getThrowable() {
/* 210 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\message\ThreadDumpMessage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */