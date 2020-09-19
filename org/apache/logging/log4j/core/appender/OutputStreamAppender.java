/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.layout.PatternLayout;
/*     */ import org.apache.logging.log4j.core.util.CloseShieldOutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "OutputStream", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class OutputStreamAppender
/*     */   extends AbstractOutputStreamAppender<OutputStreamManager>
/*     */ {
/*     */   public static class Builder
/*     */     implements org.apache.logging.log4j.core.util.Builder<OutputStreamAppender>
/*     */   {
/*     */     private Filter filter;
/*     */     private boolean follow = false;
/*     */     private boolean ignoreExceptions = true;
/*  52 */     private Layout<? extends Serializable> layout = (Layout<? extends Serializable>)PatternLayout.createDefaultLayout();
/*     */     
/*     */     private String name;
/*     */     
/*     */     private OutputStream target;
/*     */ 
/*     */     
/*     */     public OutputStreamAppender build() {
/*  60 */       return new OutputStreamAppender(this.name, this.layout, this.filter, OutputStreamAppender.getManager(this.target, this.follow, this.layout), this.ignoreExceptions);
/*     */     }
/*     */     
/*     */     public Builder setFilter(Filter aFilter) {
/*  64 */       this.filter = aFilter;
/*  65 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setFollow(boolean shouldFollow) {
/*  69 */       this.follow = shouldFollow;
/*  70 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setIgnoreExceptions(boolean shouldIgnoreExceptions) {
/*  74 */       this.ignoreExceptions = shouldIgnoreExceptions;
/*  75 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setLayout(Layout<? extends Serializable> aLayout) {
/*  79 */       this.layout = aLayout;
/*  80 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setName(String aName) {
/*  84 */       this.name = aName;
/*  85 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setTarget(OutputStream aTarget) {
/*  89 */       this.target = aTarget;
/*  90 */       return this;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class FactoryData
/*     */   {
/*     */     private final Layout<? extends Serializable> layout;
/*     */ 
/*     */ 
/*     */     
/*     */     private final String name;
/*     */ 
/*     */ 
/*     */     
/*     */     private final OutputStream os;
/*     */ 
/*     */ 
/*     */     
/*     */     public FactoryData(OutputStream os, String type, Layout<? extends Serializable> layout) {
/* 112 */       this.os = os;
/* 113 */       this.name = type;
/* 114 */       this.layout = layout;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class OutputStreamManagerFactory
/*     */     implements ManagerFactory<OutputStreamManager, FactoryData>
/*     */   {
/*     */     private OutputStreamManagerFactory() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public OutputStreamManager createManager(String name, OutputStreamAppender.FactoryData data) {
/* 134 */       return new OutputStreamManager(data.os, data.name, data.layout, true);
/*     */     }
/*     */   }
/*     */   
/* 138 */   private static OutputStreamManagerFactory factory = new OutputStreamManagerFactory();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public static OutputStreamAppender createAppender(Layout<? extends Serializable> layout, Filter filter, OutputStream target, String name, boolean follow, boolean ignore) {
/*     */     PatternLayout patternLayout;
/* 163 */     if (name == null) {
/* 164 */       LOGGER.error("No name provided for OutputStreamAppender");
/* 165 */       return null;
/*     */     } 
/* 167 */     if (layout == null) {
/* 168 */       patternLayout = PatternLayout.createDefaultLayout();
/*     */     }
/* 170 */     return new OutputStreamAppender(name, (Layout<? extends Serializable>)patternLayout, filter, getManager(target, follow, (Layout<? extends Serializable>)patternLayout), ignore);
/*     */   }
/*     */ 
/*     */   
/*     */   private static OutputStreamManager getManager(OutputStream target, boolean follow, Layout<? extends Serializable> layout) {
/* 175 */     CloseShieldOutputStream closeShieldOutputStream = new CloseShieldOutputStream(target);
/* 176 */     String managerName = target.getClass().getName() + "@" + Integer.toHexString(target.hashCode()) + '.' + follow;
/*     */     
/* 178 */     return OutputStreamManager.getManager(managerName, new FactoryData((OutputStream)closeShieldOutputStream, managerName, layout), factory);
/*     */   }
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static Builder newBuilder() {
/* 183 */     return new Builder();
/*     */   }
/*     */ 
/*     */   
/*     */   private OutputStreamAppender(String name, Layout<? extends Serializable> layout, Filter filter, OutputStreamManager manager, boolean ignoreExceptions) {
/* 188 */     super(name, layout, filter, ignoreExceptions, true, manager);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\OutputStreamAppender.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */