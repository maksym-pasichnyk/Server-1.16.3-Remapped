/*     */ package org.apache.logging.log4j.core.filter;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.core.AbstractLifeCycle;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.LifeCycle2;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
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
/*     */ public abstract class AbstractFilterable
/*     */   extends AbstractLifeCycle
/*     */   implements Filterable
/*     */ {
/*     */   private volatile Filter filter;
/*     */   
/*     */   public static abstract class Builder<B extends Builder<B>>
/*     */   {
/*     */     @PluginElement("Filter")
/*     */     private Filter filter;
/*     */     
/*     */     public Filter getFilter() {
/*  44 */       return this.filter;
/*     */     }
/*     */ 
/*     */     
/*     */     public B asBuilder() {
/*  49 */       return (B)this;
/*     */     }
/*     */     
/*     */     public B withFilter(Filter filter) {
/*  53 */       this.filter = filter;
/*  54 */       return asBuilder();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractFilterable(Filter filter) {
/*  65 */     this.filter = filter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractFilterable() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter getFilter() {
/*  77 */     return this.filter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void addFilter(Filter filter) {
/*  86 */     if (filter == null) {
/*     */       return;
/*     */     }
/*  89 */     if (this.filter == null) {
/*  90 */       this.filter = filter;
/*  91 */     } else if (this.filter instanceof CompositeFilter) {
/*  92 */       this.filter = ((CompositeFilter)this.filter).addFilter(filter);
/*     */     } else {
/*  94 */       Filter[] filters = { this.filter, filter };
/*  95 */       this.filter = CompositeFilter.createFilters(filters);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void removeFilter(Filter filter) {
/* 105 */     if (this.filter == null || filter == null) {
/*     */       return;
/*     */     }
/* 108 */     if (this.filter == filter || this.filter.equals(filter)) {
/* 109 */       this.filter = null;
/* 110 */     } else if (this.filter instanceof CompositeFilter) {
/* 111 */       CompositeFilter composite = (CompositeFilter)this.filter;
/* 112 */       composite = composite.removeFilter(filter);
/* 113 */       if (composite.size() > 1) {
/* 114 */         this.filter = composite;
/* 115 */       } else if (composite.size() == 1) {
/* 116 */         Iterator<Filter> iter = composite.iterator();
/* 117 */         this.filter = iter.next();
/*     */       } else {
/* 119 */         this.filter = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasFilter() {
/* 130 */     return (this.filter != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/* 138 */     setStarting();
/* 139 */     if (this.filter != null) {
/* 140 */       this.filter.start();
/*     */     }
/* 142 */     setStarted();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 150 */     return stop(timeout, timeUnit, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean stop(long timeout, TimeUnit timeUnit, boolean changeLifeCycleState) {
/* 157 */     if (changeLifeCycleState) {
/* 158 */       setStopping();
/*     */     }
/* 160 */     boolean stopped = true;
/* 161 */     if (this.filter != null) {
/* 162 */       if (this.filter instanceof LifeCycle2) {
/* 163 */         stopped = ((LifeCycle2)this.filter).stop(timeout, timeUnit);
/*     */       } else {
/* 165 */         this.filter.stop();
/* 166 */         stopped = true;
/*     */       } 
/*     */     }
/* 169 */     if (changeLifeCycleState) {
/* 170 */       setStopped();
/*     */     }
/* 172 */     return stopped;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFiltered(LogEvent event) {
/* 182 */     return (this.filter != null && this.filter.filter(event) == Filter.Result.DENY);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\filter\AbstractFilterable.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */