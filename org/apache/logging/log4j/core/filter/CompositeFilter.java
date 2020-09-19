/*     */ package org.apache.logging.log4j.core.filter;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.core.AbstractLifeCycle;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.LifeCycle2;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.Logger;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.util.ObjectArrayIterator;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.util.PerformanceSensitive;
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
/*     */ @Plugin(name = "filters", category = "Core", printObject = true)
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public final class CompositeFilter
/*     */   extends AbstractLifeCycle
/*     */   implements Iterable<Filter>, Filter
/*     */ {
/*  47 */   private static final Filter[] EMPTY_FILTERS = new Filter[0];
/*     */   private final Filter[] filters;
/*     */   
/*     */   private CompositeFilter() {
/*  51 */     this.filters = EMPTY_FILTERS;
/*     */   }
/*     */   
/*     */   private CompositeFilter(Filter[] filters) {
/*  55 */     this.filters = (filters == null) ? EMPTY_FILTERS : filters;
/*     */   }
/*     */   
/*     */   public CompositeFilter addFilter(Filter filter) {
/*  59 */     if (filter == null)
/*     */     {
/*  61 */       return this;
/*     */     }
/*  63 */     if (filter instanceof CompositeFilter) {
/*  64 */       int size = this.filters.length + ((CompositeFilter)filter).size();
/*  65 */       Filter[] arrayOfFilter = Arrays.<Filter>copyOf(this.filters, size);
/*  66 */       int index = this.filters.length;
/*  67 */       for (Filter currentFilter : ((CompositeFilter)filter).filters) {
/*  68 */         arrayOfFilter[index] = currentFilter;
/*     */       }
/*  70 */       return new CompositeFilter(arrayOfFilter);
/*     */     } 
/*  72 */     Filter[] copy = Arrays.<Filter>copyOf(this.filters, this.filters.length + 1);
/*  73 */     copy[this.filters.length] = filter;
/*  74 */     return new CompositeFilter(copy);
/*     */   }
/*     */   
/*     */   public CompositeFilter removeFilter(Filter filter) {
/*  78 */     if (filter == null)
/*     */     {
/*  80 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  85 */     List<Filter> filterList = new ArrayList<>(Arrays.asList(this.filters));
/*  86 */     if (filter instanceof CompositeFilter) {
/*  87 */       for (Filter currentFilter : ((CompositeFilter)filter).filters) {
/*  88 */         filterList.remove(currentFilter);
/*     */       }
/*     */     } else {
/*  91 */       filterList.remove(filter);
/*     */     } 
/*  93 */     return new CompositeFilter(filterList.<Filter>toArray(new Filter[this.filters.length - 1]));
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<Filter> iterator() {
/*  98 */     return (Iterator<Filter>)new ObjectArrayIterator((Object[])this.filters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public List<Filter> getFilters() {
/* 109 */     return Arrays.asList(this.filters);
/*     */   }
/*     */   
/*     */   public Filter[] getFiltersArray() {
/* 113 */     return this.filters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 122 */     return (this.filters.length == 0);
/*     */   }
/*     */   
/*     */   public int size() {
/* 126 */     return this.filters.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 131 */     setStarting();
/* 132 */     for (Filter filter : this.filters) {
/* 133 */       filter.start();
/*     */     }
/* 135 */     setStarted();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 140 */     setStopping();
/* 141 */     for (Filter filter : this.filters) {
/* 142 */       if (filter instanceof LifeCycle2) {
/* 143 */         ((LifeCycle2)filter).stop(timeout, timeUnit);
/*     */       } else {
/* 145 */         filter.stop();
/*     */       } 
/*     */     } 
/* 148 */     setStopped();
/* 149 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result getOnMismatch() {
/* 159 */     return Filter.Result.NEUTRAL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result getOnMatch() {
/* 169 */     return Filter.Result.NEUTRAL;
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
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
/* 190 */     Filter.Result result = Filter.Result.NEUTRAL;
/* 191 */     for (int i = 0; i < this.filters.length; i++) {
/* 192 */       result = this.filters[i].filter(logger, level, marker, msg, params);
/* 193 */       if (result == Filter.Result.ACCEPT || result == Filter.Result.DENY) {
/* 194 */         return result;
/*     */       }
/*     */     } 
/* 197 */     return result;
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
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0) {
/* 217 */     Filter.Result result = Filter.Result.NEUTRAL;
/* 218 */     for (int i = 0; i < this.filters.length; i++) {
/* 219 */       result = this.filters[i].filter(logger, level, marker, msg, p0);
/* 220 */       if (result == Filter.Result.ACCEPT || result == Filter.Result.DENY) {
/* 221 */         return result;
/*     */       }
/*     */     } 
/* 224 */     return result;
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
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1) {
/* 245 */     Filter.Result result = Filter.Result.NEUTRAL;
/* 246 */     for (int i = 0; i < this.filters.length; i++) {
/* 247 */       result = this.filters[i].filter(logger, level, marker, msg, p0, p1);
/* 248 */       if (result == Filter.Result.ACCEPT || result == Filter.Result.DENY) {
/* 249 */         return result;
/*     */       }
/*     */     } 
/* 252 */     return result;
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
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2) {
/* 274 */     Filter.Result result = Filter.Result.NEUTRAL;
/* 275 */     for (int i = 0; i < this.filters.length; i++) {
/* 276 */       result = this.filters[i].filter(logger, level, marker, msg, p0, p1, p2);
/* 277 */       if (result == Filter.Result.ACCEPT || result == Filter.Result.DENY) {
/* 278 */         return result;
/*     */       }
/*     */     } 
/* 281 */     return result;
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
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3) {
/* 304 */     Filter.Result result = Filter.Result.NEUTRAL;
/* 305 */     for (int i = 0; i < this.filters.length; i++) {
/* 306 */       result = this.filters[i].filter(logger, level, marker, msg, p0, p1, p2, p3);
/* 307 */       if (result == Filter.Result.ACCEPT || result == Filter.Result.DENY) {
/* 308 */         return result;
/*     */       }
/*     */     } 
/* 311 */     return result;
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
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 336 */     Filter.Result result = Filter.Result.NEUTRAL;
/* 337 */     for (int i = 0; i < this.filters.length; i++) {
/* 338 */       result = this.filters[i].filter(logger, level, marker, msg, p0, p1, p2, p3, p4);
/* 339 */       if (result == Filter.Result.ACCEPT || result == Filter.Result.DENY) {
/* 340 */         return result;
/*     */       }
/*     */     } 
/* 343 */     return result;
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
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 369 */     Filter.Result result = Filter.Result.NEUTRAL;
/* 370 */     for (int i = 0; i < this.filters.length; i++) {
/* 371 */       result = this.filters[i].filter(logger, level, marker, msg, p0, p1, p2, p3, p4, p5);
/* 372 */       if (result == Filter.Result.ACCEPT || result == Filter.Result.DENY) {
/* 373 */         return result;
/*     */       }
/*     */     } 
/* 376 */     return result;
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
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 403 */     Filter.Result result = Filter.Result.NEUTRAL;
/* 404 */     for (int i = 0; i < this.filters.length; i++) {
/* 405 */       result = this.filters[i].filter(logger, level, marker, msg, p0, p1, p2, p3, p4, p5, p6);
/* 406 */       if (result == Filter.Result.ACCEPT || result == Filter.Result.DENY) {
/* 407 */         return result;
/*     */       }
/*     */     } 
/* 410 */     return result;
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
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 439 */     Filter.Result result = Filter.Result.NEUTRAL;
/* 440 */     for (int i = 0; i < this.filters.length; i++) {
/* 441 */       result = this.filters[i].filter(logger, level, marker, msg, p0, p1, p2, p3, p4, p5, p6, p7);
/* 442 */       if (result == Filter.Result.ACCEPT || result == Filter.Result.DENY) {
/* 443 */         return result;
/*     */       }
/*     */     } 
/* 446 */     return result;
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
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 476 */     Filter.Result result = Filter.Result.NEUTRAL;
/* 477 */     for (int i = 0; i < this.filters.length; i++) {
/* 478 */       result = this.filters[i].filter(logger, level, marker, msg, p0, p1, p2, p3, p4, p5, p6, p7, p8);
/* 479 */       if (result == Filter.Result.ACCEPT || result == Filter.Result.DENY) {
/* 480 */         return result;
/*     */       }
/*     */     } 
/* 483 */     return result;
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
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 514 */     Filter.Result result = Filter.Result.NEUTRAL;
/* 515 */     for (int i = 0; i < this.filters.length; i++) {
/* 516 */       result = this.filters[i].filter(logger, level, marker, msg, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
/* 517 */       if (result == Filter.Result.ACCEPT || result == Filter.Result.DENY) {
/* 518 */         return result;
/*     */       }
/*     */     } 
/* 521 */     return result;
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
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
/* 542 */     Filter.Result result = Filter.Result.NEUTRAL;
/* 543 */     for (int i = 0; i < this.filters.length; i++) {
/* 544 */       result = this.filters[i].filter(logger, level, marker, msg, t);
/* 545 */       if (result == Filter.Result.ACCEPT || result == Filter.Result.DENY) {
/* 546 */         return result;
/*     */       }
/*     */     } 
/* 549 */     return result;
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
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
/* 570 */     Filter.Result result = Filter.Result.NEUTRAL;
/* 571 */     for (int i = 0; i < this.filters.length; i++) {
/* 572 */       result = this.filters[i].filter(logger, level, marker, msg, t);
/* 573 */       if (result == Filter.Result.ACCEPT || result == Filter.Result.DENY) {
/* 574 */         return result;
/*     */       }
/*     */     } 
/* 577 */     return result;
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
/*     */   public Filter.Result filter(LogEvent event) {
/* 589 */     Filter.Result result = Filter.Result.NEUTRAL;
/* 590 */     for (int i = 0; i < this.filters.length; i++) {
/* 591 */       result = this.filters[i].filter(event);
/* 592 */       if (result == Filter.Result.ACCEPT || result == Filter.Result.DENY) {
/* 593 */         return result;
/*     */       }
/*     */     } 
/* 596 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 601 */     StringBuilder sb = new StringBuilder();
/* 602 */     for (int i = 0; i < this.filters.length; i++) {
/* 603 */       if (sb.length() == 0) {
/* 604 */         sb.append('{');
/*     */       } else {
/* 606 */         sb.append(", ");
/*     */       } 
/* 608 */       sb.append(this.filters[i].toString());
/*     */     } 
/* 610 */     if (sb.length() > 0) {
/* 611 */       sb.append('}');
/*     */     }
/* 613 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginFactory
/*     */   public static CompositeFilter createFilters(@PluginElement("Filters") Filter[] filters) {
/* 625 */     return new CompositeFilter(filters);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\filter\CompositeFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */