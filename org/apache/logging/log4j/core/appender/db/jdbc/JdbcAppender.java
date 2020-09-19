/*     */ package org.apache.logging.log4j.core.appender.db.jdbc;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Arrays;
/*     */ import java.util.Objects;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.appender.AbstractAppender;
/*     */ import org.apache.logging.log4j.core.appender.db.AbstractDatabaseAppender;
/*     */ import org.apache.logging.log4j.core.appender.db.ColumnMapping;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
/*     */ import org.apache.logging.log4j.core.util.Assert;
/*     */ import org.apache.logging.log4j.core.util.Booleans;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "JDBC", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class JdbcAppender
/*     */   extends AbstractDatabaseAppender<JdbcDatabaseManager>
/*     */ {
/*     */   private final String description;
/*     */   
/*     */   private JdbcAppender(String name, Filter filter, boolean ignoreExceptions, JdbcDatabaseManager manager) {
/*  60 */     super(name, filter, ignoreExceptions, manager);
/*  61 */     this.description = getName() + "{ manager=" + getManager() + " }";
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  66 */     return this.description;
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
/*     */   @Deprecated
/*     */   public static <B extends Builder<B>> JdbcAppender createAppender(String name, String ignore, Filter filter, ConnectionSource connectionSource, String bufferSize, String tableName, ColumnConfig[] columnConfigs) {
/*  81 */     Assert.requireNonEmpty(name, "Name cannot be empty");
/*  82 */     Objects.requireNonNull(connectionSource, "ConnectionSource cannot be null");
/*  83 */     Assert.requireNonEmpty(tableName, "Table name cannot be empty");
/*  84 */     Assert.requireNonEmpty(columnConfigs, "ColumnConfigs cannot be empty");
/*     */     
/*  86 */     int bufferSizeInt = AbstractAppender.parseInt(bufferSize, 0);
/*  87 */     boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
/*     */     
/*  89 */     return ((Builder)((Builder)((Builder)newBuilder().setBufferSize(bufferSizeInt).setColumnConfigs(columnConfigs).setConnectionSource(connectionSource).setTableName(tableName).withName(name)).withIgnoreExceptions(ignoreExceptions)).withFilter(filter)).build();
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
/*     */   @PluginBuilderFactory
/*     */   public static <B extends Builder<B>> B newBuilder() {
/* 102 */     return (B)(new Builder<>()).asBuilder();
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Builder<B extends Builder<B>>
/*     */     extends AbstractAppender.Builder<B>
/*     */     implements org.apache.logging.log4j.core.util.Builder<JdbcAppender>
/*     */   {
/*     */     @PluginElement("ConnectionSource")
/*     */     @Required(message = "No ConnectionSource provided")
/*     */     private ConnectionSource connectionSource;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private int bufferSize;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     @Required(message = "No table name provided")
/*     */     private String tableName;
/*     */     
/*     */     @PluginElement("ColumnConfigs")
/*     */     private ColumnConfig[] columnConfigs;
/*     */     
/*     */     @PluginElement("ColumnMappings")
/*     */     private ColumnMapping[] columnMappings;
/*     */ 
/*     */     
/*     */     public B setConnectionSource(ConnectionSource connectionSource) {
/* 129 */       this.connectionSource = connectionSource;
/* 130 */       return (B)asBuilder();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setBufferSize(int bufferSize) {
/* 138 */       this.bufferSize = bufferSize;
/* 139 */       return (B)asBuilder();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setTableName(String tableName) {
/* 146 */       this.tableName = tableName;
/* 147 */       return (B)asBuilder();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setColumnConfigs(ColumnConfig... columnConfigs) {
/* 154 */       this.columnConfigs = columnConfigs;
/* 155 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setColumnMappings(ColumnMapping... columnMappings) {
/* 159 */       this.columnMappings = columnMappings;
/* 160 */       return (B)asBuilder();
/*     */     }
/*     */ 
/*     */     
/*     */     public JdbcAppender build() {
/* 165 */       if (Assert.isEmpty(this.columnConfigs) && Assert.isEmpty(this.columnMappings)) {
/* 166 */         JdbcAppender.LOGGER.error("Cannot create JdbcAppender without any columns configured.");
/* 167 */         return null;
/*     */       } 
/* 169 */       String managerName = "JdbcManager{name=" + getName() + ", bufferSize=" + this.bufferSize + ", tableName=" + this.tableName + ", columnConfigs=" + Arrays.toString((Object[])this.columnConfigs) + ", columnMappings=" + Arrays.toString((Object[])this.columnMappings) + '}';
/*     */ 
/*     */       
/* 172 */       JdbcDatabaseManager manager = JdbcDatabaseManager.getManager(managerName, this.bufferSize, this.connectionSource, this.tableName, this.columnConfigs, this.columnMappings);
/*     */       
/* 174 */       if (manager == null) {
/* 175 */         return null;
/*     */       }
/* 177 */       return new JdbcAppender(getName(), getFilter(), isIgnoreExceptions(), manager);
/*     */     }
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public Layout<? extends Serializable> getLayout() {
/* 183 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public B withLayout(Layout<? extends Serializable> layout) {
/* 189 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public Layout<? extends Serializable> getOrCreateLayout() {
/* 195 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public Layout<? extends Serializable> getOrCreateLayout(Charset charset) {
/* 201 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\db\jdbc\JdbcAppender.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */