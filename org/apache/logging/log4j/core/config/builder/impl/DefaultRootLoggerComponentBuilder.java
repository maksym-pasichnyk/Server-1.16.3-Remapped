/*    */ package org.apache.logging.log4j.core.config.builder.impl;
/*    */ 
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.config.builder.api.AppenderRefComponentBuilder;
/*    */ import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;
/*    */ import org.apache.logging.log4j.core.config.builder.api.FilterComponentBuilder;
/*    */ import org.apache.logging.log4j.core.config.builder.api.RootLoggerComponentBuilder;
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
/*    */ class DefaultRootLoggerComponentBuilder
/*    */   extends DefaultComponentAndConfigurationBuilder<RootLoggerComponentBuilder>
/*    */   implements RootLoggerComponentBuilder
/*    */ {
/*    */   public DefaultRootLoggerComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String level) {
/* 37 */     super(builder, "", "Root");
/* 38 */     addAttribute("level", level);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DefaultRootLoggerComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String level, boolean includeLocation) {
/* 49 */     super(builder, "", "Root");
/* 50 */     addAttribute("level", level);
/* 51 */     addAttribute("includeLocation", includeLocation);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DefaultRootLoggerComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String level, String type) {
/* 62 */     super(builder, "", type);
/* 63 */     addAttribute("level", level);
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
/*    */   public DefaultRootLoggerComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String level, String type, boolean includeLocation) {
/* 75 */     super(builder, "", type);
/* 76 */     addAttribute("level", level);
/* 77 */     addAttribute("includeLocation", includeLocation);
/*    */   }
/*    */ 
/*    */   
/*    */   public RootLoggerComponentBuilder add(AppenderRefComponentBuilder builder) {
/* 82 */     return addComponent((ComponentBuilder<?>)builder);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public RootLoggerComponentBuilder add(FilterComponentBuilder builder) {
/* 88 */     return addComponent((ComponentBuilder<?>)builder);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\builder\impl\DefaultRootLoggerComponentBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */