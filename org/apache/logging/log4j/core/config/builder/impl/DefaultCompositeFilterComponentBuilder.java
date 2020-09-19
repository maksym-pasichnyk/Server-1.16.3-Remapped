/*    */ package org.apache.logging.log4j.core.config.builder.impl;
/*    */ 
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;
/*    */ import org.apache.logging.log4j.core.config.builder.api.CompositeFilterComponentBuilder;
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
/*    */ class DefaultCompositeFilterComponentBuilder
/*    */   extends DefaultComponentAndConfigurationBuilder<CompositeFilterComponentBuilder>
/*    */   implements CompositeFilterComponentBuilder
/*    */ {
/*    */   public DefaultCompositeFilterComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String onMatch, String onMisMatch) {
/* 32 */     super(builder, "Filters");
/* 33 */     addAttribute("onMatch", onMatch);
/* 34 */     addAttribute("onMisMatch", onMisMatch);
/*    */   }
/*    */ 
/*    */   
/*    */   public CompositeFilterComponentBuilder add(FilterComponentBuilder builder) {
/* 39 */     return addComponent((ComponentBuilder<?>)builder);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\builder\impl\DefaultCompositeFilterComponentBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */