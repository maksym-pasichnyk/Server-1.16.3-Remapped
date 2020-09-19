/*     */ package org.apache.logging.log4j.core;
/*     */ 
/*     */ import java.io.ObjectStreamException;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.LoggerConfig;
/*     */ import org.apache.logging.log4j.core.config.ReliabilityStrategy;
/*     */ import org.apache.logging.log4j.core.filter.CompositeFilter;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.message.MessageFactory;
/*     */ import org.apache.logging.log4j.message.SimpleMessage;
/*     */ import org.apache.logging.log4j.spi.AbstractLogger;
/*     */ import org.apache.logging.log4j.util.Supplier;
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
/*     */ public class Logger
/*     */   extends AbstractLogger
/*     */   implements Supplier<LoggerConfig>
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected volatile PrivateConfig privateConfig;
/*     */   private final LoggerContext context;
/*     */   
/*     */   protected Logger(LoggerContext context, String name, MessageFactory messageFactory) {
/*  71 */     super(name, messageFactory);
/*  72 */     this.context = context;
/*  73 */     this.privateConfig = new PrivateConfig(context.getConfiguration(), this);
/*     */   }
/*     */   
/*     */   protected Object writeReplace() throws ObjectStreamException {
/*  77 */     return new LoggerProxy(getName(), getMessageFactory());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Logger getParent() {
/*  87 */     LoggerConfig lc = this.privateConfig.loggerConfig.getName().equals(getName()) ? this.privateConfig.loggerConfig.getParent() : this.privateConfig.loggerConfig;
/*     */     
/*  89 */     if (lc == null) {
/*  90 */       return null;
/*     */     }
/*  92 */     String lcName = lc.getName();
/*  93 */     MessageFactory messageFactory = getMessageFactory();
/*  94 */     if (this.context.hasLogger(lcName, messageFactory)) {
/*  95 */       return this.context.getLogger(lcName, messageFactory);
/*     */     }
/*  97 */     return new Logger(this.context, lcName, messageFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggerContext getContext() {
/* 106 */     return this.context;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setLevel(Level level) {
/*     */     Level actualLevel;
/* 118 */     if (level == getLevel()) {
/*     */       return;
/*     */     }
/*     */     
/* 122 */     if (level != null) {
/* 123 */       actualLevel = level;
/*     */     } else {
/* 125 */       Logger parent = getParent();
/* 126 */       actualLevel = (parent != null) ? parent.getLevel() : this.privateConfig.loggerConfigLevel;
/*     */     } 
/* 128 */     this.privateConfig = new PrivateConfig(this.privateConfig, actualLevel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggerConfig get() {
/* 138 */     return this.privateConfig.loggerConfig;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void logMessage(String fqcn, Level level, Marker marker, Message message, Throwable t) {
/* 144 */     Message msg = (message == null) ? (Message)new SimpleMessage("") : message;
/* 145 */     ReliabilityStrategy strategy = this.privateConfig.loggerConfig.getReliabilityStrategy();
/* 146 */     strategy.log(this, getName(), fqcn, marker, level, msg, t);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Throwable t) {
/* 151 */     return this.privateConfig.filter(level, marker, message, t);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message) {
/* 156 */     return this.privateConfig.filter(level, marker, message);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object... params) {
/* 161 */     return this.privateConfig.filter(level, marker, message, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0) {
/* 166 */     return this.privateConfig.filter(level, marker, message, p0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1) {
/* 172 */     return this.privateConfig.filter(level, marker, message, p0, p1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2) {
/* 178 */     return this.privateConfig.filter(level, marker, message, p0, p1, p2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
/* 184 */     return this.privateConfig.filter(level, marker, message, p0, p1, p2, p3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 191 */     return this.privateConfig.filter(level, marker, message, p0, p1, p2, p3, p4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 198 */     return this.privateConfig.filter(level, marker, message, p0, p1, p2, p3, p4, p5);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 205 */     return this.privateConfig.filter(level, marker, message, p0, p1, p2, p3, p4, p5, p6);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 213 */     return this.privateConfig.filter(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 221 */     return this.privateConfig.filter(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 229 */     return this.privateConfig.filter(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, CharSequence message, Throwable t) {
/* 234 */     return this.privateConfig.filter(level, marker, message, t);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, Object message, Throwable t) {
/* 239 */     return this.privateConfig.filter(level, marker, message, t);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, Message message, Throwable t) {
/* 244 */     return this.privateConfig.filter(level, marker, message, t);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAppender(Appender appender) {
/* 253 */     this.privateConfig.config.addLoggerAppender(this, appender);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeAppender(Appender appender) {
/* 262 */     this.privateConfig.loggerConfig.removeAppender(appender.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Appender> getAppenders() {
/* 271 */     return this.privateConfig.loggerConfig.getAppenders();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<Filter> getFilters() {
/* 281 */     Filter filter = this.privateConfig.loggerConfig.getFilter();
/* 282 */     if (filter == null)
/* 283 */       return (new ArrayList<>()).iterator(); 
/* 284 */     if (filter instanceof CompositeFilter) {
/* 285 */       return ((CompositeFilter)filter).iterator();
/*     */     }
/* 287 */     List<Filter> filters = new ArrayList<>();
/* 288 */     filters.add(filter);
/* 289 */     return filters.iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Level getLevel() {
/* 300 */     return this.privateConfig.loggerConfigLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int filterCount() {
/* 309 */     Filter filter = this.privateConfig.loggerConfig.getFilter();
/* 310 */     if (filter == null)
/* 311 */       return 0; 
/* 312 */     if (filter instanceof CompositeFilter) {
/* 313 */       return ((CompositeFilter)filter).size();
/*     */     }
/* 315 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFilter(Filter filter) {
/* 324 */     this.privateConfig.config.addLoggerFilter(this, filter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAdditive() {
/* 334 */     return this.privateConfig.loggerConfig.isAdditive();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAdditive(boolean additive) {
/* 344 */     this.privateConfig.config.setLoggerAdditive(this, additive);
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
/*     */   protected void updateConfiguration(Configuration newConfig) {
/* 365 */     this.privateConfig = new PrivateConfig(newConfig, this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected class PrivateConfig
/*     */   {
/*     */     public final LoggerConfig loggerConfig;
/*     */     
/*     */     public final Configuration config;
/*     */     
/*     */     private final Level loggerConfigLevel;
/*     */     
/*     */     private final int intLevel;
/*     */     
/*     */     private final Logger logger;
/*     */     
/*     */     public PrivateConfig(Configuration config, Logger logger) {
/* 382 */       this.config = config;
/* 383 */       this.loggerConfig = config.getLoggerConfig(Logger.this.getName());
/* 384 */       this.loggerConfigLevel = this.loggerConfig.getLevel();
/* 385 */       this.intLevel = this.loggerConfigLevel.intLevel();
/* 386 */       this.logger = logger;
/*     */     }
/*     */     
/*     */     public PrivateConfig(PrivateConfig pc, Level level) {
/* 390 */       this.config = pc.config;
/* 391 */       this.loggerConfig = pc.loggerConfig;
/* 392 */       this.loggerConfigLevel = level;
/* 393 */       this.intLevel = this.loggerConfigLevel.intLevel();
/* 394 */       this.logger = pc.logger;
/*     */     }
/*     */     
/*     */     public PrivateConfig(PrivateConfig pc, LoggerConfig lc) {
/* 398 */       this.config = pc.config;
/* 399 */       this.loggerConfig = lc;
/* 400 */       this.loggerConfigLevel = lc.getLevel();
/* 401 */       this.intLevel = this.loggerConfigLevel.intLevel();
/* 402 */       this.logger = pc.logger;
/*     */     }
/*     */ 
/*     */     
/*     */     public void logEvent(LogEvent event) {
/* 407 */       this.loggerConfig.log(event);
/*     */     }
/*     */     
/*     */     boolean filter(Level level, Marker marker, String msg) {
/* 411 */       Filter filter = this.config.getFilter();
/* 412 */       if (filter != null) {
/* 413 */         Filter.Result r = filter.filter(this.logger, level, marker, msg, new Object[0]);
/* 414 */         if (r != Filter.Result.NEUTRAL) {
/* 415 */           return (r == Filter.Result.ACCEPT);
/*     */         }
/*     */       } 
/* 418 */       return (level != null && this.intLevel >= level.intLevel());
/*     */     }
/*     */     
/*     */     boolean filter(Level level, Marker marker, String msg, Throwable t) {
/* 422 */       Filter filter = this.config.getFilter();
/* 423 */       if (filter != null) {
/* 424 */         Filter.Result r = filter.filter(this.logger, level, marker, msg, t);
/* 425 */         if (r != Filter.Result.NEUTRAL) {
/* 426 */           return (r == Filter.Result.ACCEPT);
/*     */         }
/*     */       } 
/* 429 */       return (level != null && this.intLevel >= level.intLevel());
/*     */     }
/*     */     
/*     */     boolean filter(Level level, Marker marker, String msg, Object... p1) {
/* 433 */       Filter filter = this.config.getFilter();
/* 434 */       if (filter != null) {
/* 435 */         Filter.Result r = filter.filter(this.logger, level, marker, msg, p1);
/* 436 */         if (r != Filter.Result.NEUTRAL) {
/* 437 */           return (r == Filter.Result.ACCEPT);
/*     */         }
/*     */       } 
/* 440 */       return (level != null && this.intLevel >= level.intLevel());
/*     */     }
/*     */     
/*     */     boolean filter(Level level, Marker marker, String msg, Object p0) {
/* 444 */       Filter filter = this.config.getFilter();
/* 445 */       if (filter != null) {
/* 446 */         Filter.Result r = filter.filter(this.logger, level, marker, msg, p0);
/* 447 */         if (r != Filter.Result.NEUTRAL) {
/* 448 */           return (r == Filter.Result.ACCEPT);
/*     */         }
/*     */       } 
/* 451 */       return (level != null && this.intLevel >= level.intLevel());
/*     */     }
/*     */ 
/*     */     
/*     */     boolean filter(Level level, Marker marker, String msg, Object p0, Object p1) {
/* 456 */       Filter filter = this.config.getFilter();
/* 457 */       if (filter != null) {
/* 458 */         Filter.Result r = filter.filter(this.logger, level, marker, msg, p0, p1);
/* 459 */         if (r != Filter.Result.NEUTRAL) {
/* 460 */           return (r == Filter.Result.ACCEPT);
/*     */         }
/*     */       } 
/* 463 */       return (level != null && this.intLevel >= level.intLevel());
/*     */     }
/*     */ 
/*     */     
/*     */     boolean filter(Level level, Marker marker, String msg, Object p0, Object p1, Object p2) {
/* 468 */       Filter filter = this.config.getFilter();
/* 469 */       if (filter != null) {
/* 470 */         Filter.Result r = filter.filter(this.logger, level, marker, msg, p0, p1, p2);
/* 471 */         if (r != Filter.Result.NEUTRAL) {
/* 472 */           return (r == Filter.Result.ACCEPT);
/*     */         }
/*     */       } 
/* 475 */       return (level != null && this.intLevel >= level.intLevel());
/*     */     }
/*     */ 
/*     */     
/*     */     boolean filter(Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3) {
/* 480 */       Filter filter = this.config.getFilter();
/* 481 */       if (filter != null) {
/* 482 */         Filter.Result r = filter.filter(this.logger, level, marker, msg, p0, p1, p2, p3);
/* 483 */         if (r != Filter.Result.NEUTRAL) {
/* 484 */           return (r == Filter.Result.ACCEPT);
/*     */         }
/*     */       } 
/* 487 */       return (level != null && this.intLevel >= level.intLevel());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     boolean filter(Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 493 */       Filter filter = this.config.getFilter();
/* 494 */       if (filter != null) {
/* 495 */         Filter.Result r = filter.filter(this.logger, level, marker, msg, p0, p1, p2, p3, p4);
/* 496 */         if (r != Filter.Result.NEUTRAL) {
/* 497 */           return (r == Filter.Result.ACCEPT);
/*     */         }
/*     */       } 
/* 500 */       return (level != null && this.intLevel >= level.intLevel());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     boolean filter(Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 506 */       Filter filter = this.config.getFilter();
/* 507 */       if (filter != null) {
/* 508 */         Filter.Result r = filter.filter(this.logger, level, marker, msg, p0, p1, p2, p3, p4, p5);
/* 509 */         if (r != Filter.Result.NEUTRAL) {
/* 510 */           return (r == Filter.Result.ACCEPT);
/*     */         }
/*     */       } 
/* 513 */       return (level != null && this.intLevel >= level.intLevel());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     boolean filter(Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 519 */       Filter filter = this.config.getFilter();
/* 520 */       if (filter != null) {
/* 521 */         Filter.Result r = filter.filter(this.logger, level, marker, msg, p0, p1, p2, p3, p4, p5, p6);
/* 522 */         if (r != Filter.Result.NEUTRAL) {
/* 523 */           return (r == Filter.Result.ACCEPT);
/*     */         }
/*     */       } 
/* 526 */       return (level != null && this.intLevel >= level.intLevel());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean filter(Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 533 */       Filter filter = this.config.getFilter();
/* 534 */       if (filter != null) {
/* 535 */         Filter.Result r = filter.filter(this.logger, level, marker, msg, p0, p1, p2, p3, p4, p5, p6, p7);
/* 536 */         if (r != Filter.Result.NEUTRAL) {
/* 537 */           return (r == Filter.Result.ACCEPT);
/*     */         }
/*     */       } 
/* 540 */       return (level != null && this.intLevel >= level.intLevel());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean filter(Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 547 */       Filter filter = this.config.getFilter();
/* 548 */       if (filter != null) {
/* 549 */         Filter.Result r = filter.filter(this.logger, level, marker, msg, p0, p1, p2, p3, p4, p5, p6, p7, p8);
/* 550 */         if (r != Filter.Result.NEUTRAL) {
/* 551 */           return (r == Filter.Result.ACCEPT);
/*     */         }
/*     */       } 
/* 554 */       return (level != null && this.intLevel >= level.intLevel());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean filter(Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 561 */       Filter filter = this.config.getFilter();
/* 562 */       if (filter != null) {
/* 563 */         Filter.Result r = filter.filter(this.logger, level, marker, msg, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
/*     */         
/* 565 */         if (r != Filter.Result.NEUTRAL) {
/* 566 */           return (r == Filter.Result.ACCEPT);
/*     */         }
/*     */       } 
/* 569 */       return (level != null && this.intLevel >= level.intLevel());
/*     */     }
/*     */     
/*     */     boolean filter(Level level, Marker marker, CharSequence msg, Throwable t) {
/* 573 */       Filter filter = this.config.getFilter();
/* 574 */       if (filter != null) {
/* 575 */         Filter.Result r = filter.filter(this.logger, level, marker, msg, t);
/* 576 */         if (r != Filter.Result.NEUTRAL) {
/* 577 */           return (r == Filter.Result.ACCEPT);
/*     */         }
/*     */       } 
/* 580 */       return (level != null && this.intLevel >= level.intLevel());
/*     */     }
/*     */     
/*     */     boolean filter(Level level, Marker marker, Object msg, Throwable t) {
/* 584 */       Filter filter = this.config.getFilter();
/* 585 */       if (filter != null) {
/* 586 */         Filter.Result r = filter.filter(this.logger, level, marker, msg, t);
/* 587 */         if (r != Filter.Result.NEUTRAL) {
/* 588 */           return (r == Filter.Result.ACCEPT);
/*     */         }
/*     */       } 
/* 591 */       return (level != null && this.intLevel >= level.intLevel());
/*     */     }
/*     */     
/*     */     boolean filter(Level level, Marker marker, Message msg, Throwable t) {
/* 595 */       Filter filter = this.config.getFilter();
/* 596 */       if (filter != null) {
/* 597 */         Filter.Result r = filter.filter(this.logger, level, marker, msg, t);
/* 598 */         if (r != Filter.Result.NEUTRAL) {
/* 599 */           return (r == Filter.Result.ACCEPT);
/*     */         }
/*     */       } 
/* 602 */       return (level != null && this.intLevel >= level.intLevel());
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 607 */       StringBuilder builder = new StringBuilder();
/* 608 */       builder.append("PrivateConfig [loggerConfig=");
/* 609 */       builder.append(this.loggerConfig);
/* 610 */       builder.append(", config=");
/* 611 */       builder.append(this.config);
/* 612 */       builder.append(", loggerConfigLevel=");
/* 613 */       builder.append(this.loggerConfigLevel);
/* 614 */       builder.append(", intLevel=");
/* 615 */       builder.append(this.intLevel);
/* 616 */       builder.append(", logger=");
/* 617 */       builder.append(this.logger);
/* 618 */       builder.append("]");
/* 619 */       return builder.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class LoggerProxy
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     private final String name;
/*     */     
/*     */     private final MessageFactory messageFactory;
/*     */ 
/*     */     
/*     */     public LoggerProxy(String name, MessageFactory messageFactory) {
/* 636 */       this.name = name;
/* 637 */       this.messageFactory = messageFactory;
/*     */     }
/*     */     
/*     */     protected Object readResolve() throws ObjectStreamException {
/* 641 */       return new Logger(LoggerContext.getContext(), this.name, this.messageFactory);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 652 */     String nameLevel = "" + getName() + ':' + getLevel();
/* 653 */     if (this.context == null) {
/* 654 */       return nameLevel;
/*     */     }
/* 656 */     String contextName = this.context.getName();
/* 657 */     return (contextName == null) ? nameLevel : (nameLevel + " in " + contextName);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 662 */     if (this == o) {
/* 663 */       return true;
/*     */     }
/* 665 */     if (o == null || getClass() != o.getClass()) {
/* 666 */       return false;
/*     */     }
/* 668 */     Logger that = (Logger)o;
/* 669 */     return getName().equals(that.getName());
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 674 */     return getName().hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\Logger.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */