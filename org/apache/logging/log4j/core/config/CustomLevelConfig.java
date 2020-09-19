/*    */ package org.apache.logging.log4j.core.config;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import org.apache.logging.log4j.Level;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*    */ import org.apache.logging.log4j.status.StatusLogger;
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
/*    */ 
/*    */ 
/*    */ @Plugin(name = "CustomLevel", category = "Core", printObject = true)
/*    */ public final class CustomLevelConfig
/*    */ {
/*    */   private final String levelName;
/*    */   private final int intLevel;
/*    */   
/*    */   private CustomLevelConfig(String levelName, int intLevel) {
/* 38 */     this.levelName = Objects.<String>requireNonNull(levelName, "levelName is null");
/* 39 */     this.intLevel = intLevel;
/*    */   }
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
/*    */   @PluginFactory
/*    */   public static CustomLevelConfig createLevel(@PluginAttribute("name") String levelName, @PluginAttribute("intLevel") int intLevel) {
/* 56 */     StatusLogger.getLogger().debug("Creating CustomLevel(name='{}', intValue={})", levelName, Integer.valueOf(intLevel));
/* 57 */     Level.forName(levelName, intLevel);
/* 58 */     return new CustomLevelConfig(levelName, intLevel);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getLevelName() {
/* 67 */     return this.levelName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getIntLevel() {
/* 77 */     return this.intLevel;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 82 */     return this.intLevel ^ this.levelName.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object object) {
/* 87 */     if (this == object) {
/* 88 */       return true;
/*    */     }
/* 90 */     if (!(object instanceof CustomLevelConfig)) {
/* 91 */       return false;
/*    */     }
/* 93 */     CustomLevelConfig other = (CustomLevelConfig)object;
/* 94 */     return (this.intLevel == other.intLevel && this.levelName.equals(other.levelName));
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 99 */     return "CustomLevel[name=" + this.levelName + ", intLevel=" + this.intLevel + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\CustomLevelConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */