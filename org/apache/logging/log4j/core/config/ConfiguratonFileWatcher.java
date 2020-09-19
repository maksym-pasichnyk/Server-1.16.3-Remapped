/*    */ package org.apache.logging.log4j.core.config;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.util.List;
/*    */ import org.apache.logging.log4j.core.util.FileWatcher;
/*    */ import org.apache.logging.log4j.core.util.Log4jThreadFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ConfiguratonFileWatcher
/*    */   implements FileWatcher
/*    */ {
/*    */   private final Reconfigurable reconfigurable;
/*    */   private final List<ConfigurationListener> configurationListeners;
/*    */   private final Log4jThreadFactory threadFactory;
/*    */   
/*    */   public ConfiguratonFileWatcher(Reconfigurable reconfigurable, List<ConfigurationListener> configurationListeners) {
/* 35 */     this.reconfigurable = reconfigurable;
/* 36 */     this.configurationListeners = configurationListeners;
/* 37 */     this.threadFactory = Log4jThreadFactory.createDaemonThreadFactory("ConfiguratonFileWatcher");
/*    */   }
/*    */   
/*    */   public List<ConfigurationListener> getListeners() {
/* 41 */     return this.configurationListeners;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void fileModified(File file) {
/* 47 */     for (ConfigurationListener configurationListener : this.configurationListeners) {
/* 48 */       Thread thread = this.threadFactory.newThread(new ReconfigurationRunnable(configurationListener, this.reconfigurable));
/* 49 */       thread.start();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   private static class ReconfigurationRunnable
/*    */     implements Runnable
/*    */   {
/*    */     private final ConfigurationListener configurationListener;
/*    */     
/*    */     private final Reconfigurable reconfigurable;
/*    */     
/*    */     public ReconfigurationRunnable(ConfigurationListener configurationListener, Reconfigurable reconfigurable) {
/* 62 */       this.configurationListener = configurationListener;
/* 63 */       this.reconfigurable = reconfigurable;
/*    */     }
/*    */ 
/*    */     
/*    */     public void run() {
/* 68 */       this.configurationListener.onChange(this.reconfigurable);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\ConfiguratonFileWatcher.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */