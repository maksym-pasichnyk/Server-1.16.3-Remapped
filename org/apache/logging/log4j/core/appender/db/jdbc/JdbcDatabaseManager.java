/*     */ package org.apache.logging.log4j.core.appender.db.jdbc;
/*     */ 
/*     */ import java.io.StringReader;
/*     */ import java.sql.Clob;
/*     */ import java.sql.Connection;
/*     */ import java.sql.DatabaseMetaData;
/*     */ import java.sql.NClob;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Timestamp;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.appender.AppenderLoggingException;
/*     */ import org.apache.logging.log4j.core.appender.ManagerFactory;
/*     */ import org.apache.logging.log4j.core.appender.db.AbstractDatabaseManager;
/*     */ import org.apache.logging.log4j.core.appender.db.ColumnMapping;
/*     */ import org.apache.logging.log4j.core.config.plugins.convert.DateTypeConverter;
/*     */ import org.apache.logging.log4j.core.config.plugins.convert.TypeConverters;
/*     */ import org.apache.logging.log4j.core.util.Closer;
/*     */ import org.apache.logging.log4j.spi.ThreadContextMap;
/*     */ import org.apache.logging.log4j.spi.ThreadContextStack;
/*     */ import org.apache.logging.log4j.util.ReadOnlyStringMap;
/*     */ import org.apache.logging.log4j.util.Strings;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class JdbcDatabaseManager
/*     */   extends AbstractDatabaseManager
/*     */ {
/*  50 */   private static final JdbcDatabaseManagerFactory INSTANCE = new JdbcDatabaseManagerFactory();
/*     */   
/*     */   private final List<ColumnMapping> columnMappings;
/*     */   
/*     */   private final List<ColumnConfig> columnConfigs;
/*     */   
/*     */   private final ConnectionSource connectionSource;
/*     */   
/*     */   private final String sqlStatement;
/*     */   
/*     */   private Connection connection;
/*     */   private PreparedStatement statement;
/*     */   private boolean isBatchSupported;
/*     */   
/*     */   private JdbcDatabaseManager(String name, int bufferSize, ConnectionSource connectionSource, String sqlStatement, List<ColumnConfig> columnConfigs, List<ColumnMapping> columnMappings) {
/*  65 */     super(name, bufferSize);
/*  66 */     this.connectionSource = connectionSource;
/*  67 */     this.sqlStatement = sqlStatement;
/*  68 */     this.columnConfigs = columnConfigs;
/*  69 */     this.columnMappings = columnMappings;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void startupInternal() throws Exception {
/*  74 */     this.connection = this.connectionSource.getConnection();
/*  75 */     DatabaseMetaData metaData = this.connection.getMetaData();
/*  76 */     this.isBatchSupported = metaData.supportsBatchUpdates();
/*  77 */     Closer.closeSilently(this.connection);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean shutdownInternal() {
/*  82 */     if (this.connection != null || this.statement != null) {
/*  83 */       return commitAndClose();
/*     */     }
/*  85 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void connectAndStart() {
/*     */     try {
/*  91 */       this.connection = this.connectionSource.getConnection();
/*  92 */       this.connection.setAutoCommit(false);
/*  93 */       this.statement = this.connection.prepareStatement(this.sqlStatement);
/*  94 */     } catch (SQLException e) {
/*  95 */       throw new AppenderLoggingException("Cannot write logging event or flush buffer; JDBC manager cannot connect to the database.", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeInternal(LogEvent event) {
/* 103 */     StringReader reader = null;
/*     */     try {
/* 105 */       if (!isRunning() || this.connection == null || this.connection.isClosed() || this.statement == null || this.statement.isClosed())
/*     */       {
/* 107 */         throw new AppenderLoggingException("Cannot write logging event; JDBC manager not connected to the database.");
/*     */       }
/*     */ 
/*     */       
/* 111 */       int i = 1;
/* 112 */       for (ColumnMapping mapping : this.columnMappings) {
/* 113 */         if (ThreadContextMap.class.isAssignableFrom(mapping.getType()) || ReadOnlyStringMap.class.isAssignableFrom(mapping.getType())) {
/*     */           
/* 115 */           this.statement.setObject(i++, event.getContextData().toMap()); continue;
/* 116 */         }  if (ThreadContextStack.class.isAssignableFrom(mapping.getType())) {
/* 117 */           this.statement.setObject(i++, event.getContextStack().asList()); continue;
/* 118 */         }  if (Date.class.isAssignableFrom(mapping.getType())) {
/* 119 */           this.statement.setObject(i++, DateTypeConverter.fromMillis(event.getTimeMillis(), mapping.getType().asSubclass(Date.class))); continue;
/*     */         } 
/* 121 */         if (Clob.class.isAssignableFrom(mapping.getType())) {
/* 122 */           this.statement.setClob(i++, new StringReader((String)mapping.getLayout().toSerializable(event))); continue;
/* 123 */         }  if (NClob.class.isAssignableFrom(mapping.getType())) {
/* 124 */           this.statement.setNClob(i++, new StringReader((String)mapping.getLayout().toSerializable(event))); continue;
/*     */         } 
/* 126 */         Object value = TypeConverters.convert((String)mapping.getLayout().toSerializable(event), mapping.getType(), null);
/*     */         
/* 128 */         if (value == null) {
/* 129 */           this.statement.setNull(i++, 0); continue;
/*     */         } 
/* 131 */         this.statement.setObject(i++, value);
/*     */       } 
/*     */ 
/*     */       
/* 135 */       for (ColumnConfig column : this.columnConfigs) {
/* 136 */         if (column.isEventTimestamp()) {
/* 137 */           this.statement.setTimestamp(i++, new Timestamp(event.getTimeMillis())); continue;
/* 138 */         }  if (column.isClob()) {
/* 139 */           reader = new StringReader(column.getLayout().toSerializable(event));
/* 140 */           if (column.isUnicode()) {
/* 141 */             this.statement.setNClob(i++, reader); continue;
/*     */           } 
/* 143 */           this.statement.setClob(i++, reader); continue;
/*     */         } 
/* 145 */         if (column.isUnicode()) {
/* 146 */           this.statement.setNString(i++, column.getLayout().toSerializable(event)); continue;
/*     */         } 
/* 148 */         this.statement.setString(i++, column.getLayout().toSerializable(event));
/*     */       } 
/*     */ 
/*     */       
/* 152 */       if (this.isBatchSupported) {
/* 153 */         this.statement.addBatch();
/* 154 */       } else if (this.statement.executeUpdate() == 0) {
/* 155 */         throw new AppenderLoggingException("No records inserted in database table for log event in JDBC manager.");
/*     */       }
/*     */     
/* 158 */     } catch (SQLException e) {
/* 159 */       throw new AppenderLoggingException("Failed to insert record for log event in JDBC manager: " + e.getMessage(), e);
/*     */     } finally {
/*     */       
/* 162 */       Closer.closeSilently(reader);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean commitAndClose() {
/* 168 */     boolean closed = true;
/*     */     try {
/* 170 */       if (this.connection != null && !this.connection.isClosed()) {
/* 171 */         if (this.isBatchSupported) {
/* 172 */           this.statement.executeBatch();
/*     */         }
/* 174 */         this.connection.commit();
/*     */       } 
/* 176 */     } catch (SQLException e) {
/* 177 */       throw new AppenderLoggingException("Failed to commit transaction logging event or flushing buffer.", e);
/*     */     } finally {
/*     */       try {
/* 180 */         Closer.close(this.statement);
/* 181 */       } catch (Exception e) {
/* 182 */         logWarn("Failed to close SQL statement logging event or flushing buffer", e);
/* 183 */         closed = false;
/*     */       } finally {
/* 185 */         this.statement = null;
/*     */       } 
/*     */       
/*     */       try {
/* 189 */         Closer.close(this.connection);
/* 190 */       } catch (Exception e) {
/* 191 */         logWarn("Failed to close database connection logging event or flushing buffer", e);
/* 192 */         closed = false;
/*     */       } finally {
/* 194 */         this.connection = null;
/*     */       } 
/*     */     } 
/* 197 */     return closed;
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
/*     */   @Deprecated
/*     */   public static JdbcDatabaseManager getJDBCDatabaseManager(String name, int bufferSize, ConnectionSource connectionSource, String tableName, ColumnConfig[] columnConfigs) {
/* 217 */     return (JdbcDatabaseManager)getManager(name, new FactoryData(bufferSize, connectionSource, tableName, columnConfigs, new ColumnMapping[0]), getFactory());
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
/*     */   public static JdbcDatabaseManager getManager(String name, int bufferSize, ConnectionSource connectionSource, String tableName, ColumnConfig[] columnConfigs, ColumnMapping[] columnMappings) {
/* 239 */     return (JdbcDatabaseManager)getManager(name, new FactoryData(bufferSize, connectionSource, tableName, columnConfigs, columnMappings), getFactory());
/*     */   }
/*     */ 
/*     */   
/*     */   private static JdbcDatabaseManagerFactory getFactory() {
/* 244 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class FactoryData
/*     */     extends AbstractDatabaseManager.AbstractFactoryData
/*     */   {
/*     */     private final ConnectionSource connectionSource;
/*     */     
/*     */     private final String tableName;
/*     */     private final ColumnConfig[] columnConfigs;
/*     */     private final ColumnMapping[] columnMappings;
/*     */     
/*     */     protected FactoryData(int bufferSize, ConnectionSource connectionSource, String tableName, ColumnConfig[] columnConfigs, ColumnMapping[] columnMappings) {
/* 258 */       super(bufferSize);
/* 259 */       this.connectionSource = connectionSource;
/* 260 */       this.tableName = tableName;
/* 261 */       this.columnConfigs = columnConfigs;
/* 262 */       this.columnMappings = columnMappings;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class JdbcDatabaseManagerFactory
/*     */     implements ManagerFactory<JdbcDatabaseManager, FactoryData>
/*     */   {
/*     */     private JdbcDatabaseManagerFactory() {}
/*     */     
/*     */     public JdbcDatabaseManager createManager(String name, JdbcDatabaseManager.FactoryData data) {
/* 272 */       StringBuilder sb = (new StringBuilder("INSERT INTO ")).append(data.tableName).append(" (");
/*     */ 
/*     */       
/* 275 */       for (ColumnMapping mapping : data.columnMappings) {
/* 276 */         sb.append(mapping.getName()).append(',');
/*     */       }
/* 278 */       for (ColumnConfig config : data.columnConfigs) {
/* 279 */         sb.append(config.getColumnName()).append(',');
/*     */       }
/*     */       
/* 282 */       sb.setCharAt(sb.length() - 1, ')');
/* 283 */       sb.append(" VALUES (");
/* 284 */       List<ColumnMapping> columnMappings = new ArrayList<>(data.columnMappings.length);
/* 285 */       for (ColumnMapping mapping : data.columnMappings) {
/* 286 */         if (Strings.isNotEmpty(mapping.getLiteralValue())) {
/* 287 */           sb.append(mapping.getLiteralValue());
/*     */         } else {
/* 289 */           sb.append('?');
/* 290 */           columnMappings.add(mapping);
/*     */         } 
/* 292 */         sb.append(',');
/*     */       } 
/* 294 */       List<ColumnConfig> columnConfigs = new ArrayList<>(data.columnConfigs.length);
/* 295 */       for (ColumnConfig config : data.columnConfigs) {
/* 296 */         if (Strings.isNotEmpty(config.getLiteralValue())) {
/* 297 */           sb.append(config.getLiteralValue());
/*     */         } else {
/* 299 */           sb.append('?');
/* 300 */           columnConfigs.add(config);
/*     */         } 
/* 302 */         sb.append(',');
/*     */       } 
/*     */       
/* 305 */       sb.setCharAt(sb.length() - 1, ')');
/* 306 */       String sqlStatement = sb.toString();
/*     */       
/* 308 */       return new JdbcDatabaseManager(name, data.getBufferSize(), data.connectionSource, sqlStatement, columnConfigs, columnMappings);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\db\jdbc\JdbcDatabaseManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */