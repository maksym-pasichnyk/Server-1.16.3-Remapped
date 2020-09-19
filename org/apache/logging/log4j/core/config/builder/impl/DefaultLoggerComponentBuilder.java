/*    */ package org.apache.logging.log4j.core.config.builder.impl;
/*    */ 
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.config.builder.api.AppenderRefComponentBuilder;
/*    */ import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;
/*    */ import org.apache.logging.log4j.core.config.builder.api.FilterComponentBuilder;
/*    */ import org.apache.logging.log4j.core.config.builder.api.LoggerComponentBuilder;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ class DefaultLoggerComponentBuilder
/*    */   extends DefaultComponentAndConfigurationBuilder<LoggerComponentBuilder>
/*    */   implements LoggerComponentBuilder
/*    */ {
/*    */   public DefaultLoggerComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String name, String level) {
/* 38 */     super(builder, name, "Logger");
/* 39 */     addAttribute("level", level);
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
/*    */   public DefaultLoggerComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String name, String level, boolean includeLocation) {
/* 51 */     super(builder, name, "Logger");
/* 52 */     addAttribute("level", level);
/* 53 */     addAttribute("includeLocation", includeLocation);
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
/*    */   public DefaultLoggerComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String name, String level, String type) {
/* 65 */     super(builder, name, type);
/* 66 */     addAttribute("level", level);
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
/*    */   public DefaultLoggerComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String name, String level, String type, boolean includeLocation) {
/* 79 */     super(builder, name, type);
/* 80 */     addAttribute("level", level);
/* 81 */     addAttribute("includeLocation", includeLocation);
/*    */   }
/*    */ 
/*    */   
/*    */   public LoggerComponentBuilder add(AppenderRefComponentBuilder builder) {
/* 86 */     return addComponent((ComponentBuilder<?>)builder);
/*    */   }
/*    */ 
/*    */   
/*    */   public LoggerComponentBuilder add(FilterComponentBuilder builder) {
/* 91 */     return addComponent((ComponentBuilder<?>)builder);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\builder\impl\DefaultLoggerComponentBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */