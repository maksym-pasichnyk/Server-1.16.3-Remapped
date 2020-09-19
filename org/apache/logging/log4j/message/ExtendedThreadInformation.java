/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import java.lang.management.LockInfo;
/*     */ import java.lang.management.MonitorInfo;
/*     */ import java.lang.management.ThreadInfo;
/*     */ import org.apache.logging.log4j.util.StringBuilders;
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
/*     */ class ExtendedThreadInformation
/*     */   implements ThreadInformation
/*     */ {
/*     */   private final ThreadInfo threadInfo;
/*     */   
/*     */   ExtendedThreadInformation(ThreadInfo thread) {
/*  34 */     this.threadInfo = thread;
/*     */   }
/*     */ 
/*     */   
/*     */   public void printThreadInfo(StringBuilder sb) {
/*  39 */     StringBuilders.appendDqValue(sb, this.threadInfo.getThreadName());
/*  40 */     sb.append(" Id=").append(this.threadInfo.getThreadId()).append(' ');
/*  41 */     formatState(sb, this.threadInfo);
/*  42 */     if (this.threadInfo.isSuspended()) {
/*  43 */       sb.append(" (suspended)");
/*     */     }
/*  45 */     if (this.threadInfo.isInNative()) {
/*  46 */       sb.append(" (in native)");
/*     */     }
/*  48 */     sb.append('\n');
/*     */   }
/*     */ 
/*     */   
/*     */   public void printStack(StringBuilder sb, StackTraceElement[] stack) {
/*  53 */     int i = 0;
/*  54 */     for (StackTraceElement element : stack) {
/*  55 */       sb.append("\tat ").append(element.toString());
/*  56 */       sb.append('\n');
/*  57 */       if (i == 0 && this.threadInfo.getLockInfo() != null) {
/*  58 */         Thread.State ts = this.threadInfo.getThreadState();
/*  59 */         switch (ts) {
/*     */           case BLOCKED:
/*  61 */             sb.append("\t-  blocked on ");
/*  62 */             formatLock(sb, this.threadInfo.getLockInfo());
/*  63 */             sb.append('\n');
/*     */             break;
/*     */           case WAITING:
/*  66 */             sb.append("\t-  waiting on ");
/*  67 */             formatLock(sb, this.threadInfo.getLockInfo());
/*  68 */             sb.append('\n');
/*     */             break;
/*     */           case TIMED_WAITING:
/*  71 */             sb.append("\t-  waiting on ");
/*  72 */             formatLock(sb, this.threadInfo.getLockInfo());
/*  73 */             sb.append('\n');
/*     */             break;
/*     */         } 
/*     */ 
/*     */       
/*     */       } 
/*  79 */       for (MonitorInfo mi : this.threadInfo.getLockedMonitors()) {
/*  80 */         if (mi.getLockedStackDepth() == i) {
/*  81 */           sb.append("\t-  locked ");
/*  82 */           formatLock(sb, mi);
/*  83 */           sb.append('\n');
/*     */         } 
/*     */       } 
/*  86 */       i++;
/*     */     } 
/*     */     
/*  89 */     LockInfo[] locks = this.threadInfo.getLockedSynchronizers();
/*  90 */     if (locks.length > 0) {
/*  91 */       sb.append("\n\tNumber of locked synchronizers = ").append(locks.length).append('\n');
/*  92 */       for (LockInfo li : locks) {
/*  93 */         sb.append("\t- ");
/*  94 */         formatLock(sb, li);
/*  95 */         sb.append('\n');
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void formatLock(StringBuilder sb, LockInfo lock) {
/* 101 */     sb.append('<').append(lock.getIdentityHashCode()).append("> (a ");
/* 102 */     sb.append(lock.getClassName()).append(')');
/*     */   } private void formatState(StringBuilder sb, ThreadInfo info) {
/*     */     StackTraceElement element;
/*     */     String className, method;
/* 106 */     Thread.State state = info.getThreadState();
/* 107 */     sb.append(state);
/* 108 */     switch (state) {
/*     */       case BLOCKED:
/* 110 */         sb.append(" (on object monitor owned by \"");
/* 111 */         sb.append(info.getLockOwnerName()).append("\" Id=").append(info.getLockOwnerId()).append(')');
/*     */         break;
/*     */       
/*     */       case WAITING:
/* 115 */         element = info.getStackTrace()[0];
/* 116 */         className = element.getClassName();
/* 117 */         method = element.getMethodName();
/* 118 */         if (className.equals("java.lang.Object") && method.equals("wait")) {
/* 119 */           sb.append(" (on object monitor");
/* 120 */           if (info.getLockOwnerName() != null) {
/* 121 */             sb.append(" owned by \"");
/* 122 */             sb.append(info.getLockOwnerName()).append("\" Id=").append(info.getLockOwnerId());
/*     */           } 
/* 124 */           sb.append(')'); break;
/* 125 */         }  if (className.equals("java.lang.Thread") && method.equals("join")) {
/* 126 */           sb.append(" (on completion of thread ").append(info.getLockOwnerId()).append(')'); break;
/*     */         } 
/* 128 */         sb.append(" (parking for lock");
/* 129 */         if (info.getLockOwnerName() != null) {
/* 130 */           sb.append(" owned by \"");
/* 131 */           sb.append(info.getLockOwnerName()).append("\" Id=").append(info.getLockOwnerId());
/*     */         } 
/* 133 */         sb.append(')');
/*     */         break;
/*     */ 
/*     */       
/*     */       case TIMED_WAITING:
/* 138 */         element = info.getStackTrace()[0];
/* 139 */         className = element.getClassName();
/* 140 */         method = element.getMethodName();
/* 141 */         if (className.equals("java.lang.Object") && method.equals("wait")) {
/* 142 */           sb.append(" (on object monitor");
/* 143 */           if (info.getLockOwnerName() != null) {
/* 144 */             sb.append(" owned by \"");
/* 145 */             sb.append(info.getLockOwnerName()).append("\" Id=").append(info.getLockOwnerId());
/*     */           } 
/* 147 */           sb.append(')'); break;
/* 148 */         }  if (className.equals("java.lang.Thread") && method.equals("sleep")) {
/* 149 */           sb.append(" (sleeping)"); break;
/* 150 */         }  if (className.equals("java.lang.Thread") && method.equals("join")) {
/* 151 */           sb.append(" (on completion of thread ").append(info.getLockOwnerId()).append(')'); break;
/*     */         } 
/* 153 */         sb.append(" (parking for lock");
/* 154 */         if (info.getLockOwnerName() != null) {
/* 155 */           sb.append(" owned by \"");
/* 156 */           sb.append(info.getLockOwnerName()).append("\" Id=").append(info.getLockOwnerId());
/*     */         } 
/* 158 */         sb.append(')');
/*     */         break;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\message\ExtendedThreadInformation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */