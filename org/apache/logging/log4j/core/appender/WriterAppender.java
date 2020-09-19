/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.Writer;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.StringLayout;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.layout.PatternLayout;
/*     */ import org.apache.logging.log4j.core.util.CloseShieldWriter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "Writer", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class WriterAppender
/*     */   extends AbstractWriterAppender<WriterManager>
/*     */ {
/*     */   public static class Builder
/*     */     implements org.apache.logging.log4j.core.util.Builder<WriterAppender>
/*     */   {
/*     */     private Filter filter;
/*     */     private boolean follow = false;
/*     */     private boolean ignoreExceptions = true;
/*  48 */     private StringLayout layout = (StringLayout)PatternLayout.createDefaultLayout();
/*     */     
/*     */     private String name;
/*     */     
/*     */     private Writer target;
/*     */ 
/*     */     
/*     */     public WriterAppender build() {
/*  56 */       return new WriterAppender(this.name, this.layout, this.filter, WriterAppender.getManager(this.target, this.follow, this.layout), this.ignoreExceptions);
/*     */     }
/*     */     
/*     */     public Builder setFilter(Filter aFilter) {
/*  60 */       this.filter = aFilter;
/*  61 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setFollow(boolean shouldFollow) {
/*  65 */       this.follow = shouldFollow;
/*  66 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setIgnoreExceptions(boolean shouldIgnoreExceptions) {
/*  70 */       this.ignoreExceptions = shouldIgnoreExceptions;
/*  71 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setLayout(StringLayout aLayout) {
/*  75 */       this.layout = aLayout;
/*  76 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setName(String aName) {
/*  80 */       this.name = aName;
/*  81 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setTarget(Writer aTarget) {
/*  85 */       this.target = aTarget;
/*  86 */       return this;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class FactoryData
/*     */   {
/*     */     private final StringLayout layout;
/*     */ 
/*     */ 
/*     */     
/*     */     private final String name;
/*     */ 
/*     */ 
/*     */     
/*     */     private final Writer writer;
/*     */ 
/*     */ 
/*     */     
/*     */     public FactoryData(Writer writer, String type, StringLayout layout) {
/* 108 */       this.writer = writer;
/* 109 */       this.name = type;
/* 110 */       this.layout = layout;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class WriterManagerFactory
/*     */     implements ManagerFactory<WriterManager, FactoryData>
/*     */   {
/*     */     private WriterManagerFactory() {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public WriterManager createManager(String name, WriterAppender.FactoryData data) {
/* 127 */       return new WriterManager(data.writer, data.name, data.layout, true);
/*     */     }
/*     */   }
/*     */   
/* 131 */   private static WriterManagerFactory factory = new WriterManagerFactory();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public static WriterAppender createAppender(StringLayout layout, Filter filter, Writer target, String name, boolean follow, boolean ignore) {
/*     */     PatternLayout patternLayout;
/* 156 */     if (name == null) {
/* 157 */       LOGGER.error("No name provided for WriterAppender");
/* 158 */       return null;
/*     */     } 
/* 160 */     if (layout == null) {
/* 161 */       patternLayout = PatternLayout.createDefaultLayout();
/*     */     }
/* 163 */     return new WriterAppender(name, (StringLayout)patternLayout, filter, getManager(target, follow, (StringLayout)patternLayout), ignore);
/*     */   }
/*     */   
/*     */   private static WriterManager getManager(Writer target, boolean follow, StringLayout layout) {
/* 167 */     CloseShieldWriter closeShieldWriter = new CloseShieldWriter(target);
/* 168 */     String managerName = target.getClass().getName() + "@" + Integer.toHexString(target.hashCode()) + '.' + follow;
/*     */     
/* 170 */     return WriterManager.getManager(managerName, new FactoryData((Writer)closeShieldWriter, managerName, layout), factory);
/*     */   }
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static Builder newBuilder() {
/* 175 */     return new Builder();
/*     */   }
/*     */ 
/*     */   
/*     */   private WriterAppender(String name, StringLayout layout, Filter filter, WriterManager manager, boolean ignoreExceptions) {
/* 180 */     super(name, layout, filter, ignoreExceptions, true, manager);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\WriterAppender.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */