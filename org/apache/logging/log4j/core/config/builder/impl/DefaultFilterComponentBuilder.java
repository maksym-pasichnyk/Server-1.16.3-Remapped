/*    */ package org.apache.logging.log4j.core.config.builder.impl;
/*    */ 
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.config.builder.api.FilterComponentBuilder;
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
/*    */ class DefaultFilterComponentBuilder
/*    */   extends DefaultComponentAndConfigurationBuilder<FilterComponentBuilder>
/*    */   implements FilterComponentBuilder
/*    */ {
/*    */   public DefaultFilterComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String type, String onMatch, String onMisMatch) {
/* 30 */     super(builder, type);
/* 31 */     addAttribute("onMatch", onMatch);
/* 32 */     addAttribute("onMisMatch", onMisMatch);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\builder\impl\DefaultFilterComponentBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */