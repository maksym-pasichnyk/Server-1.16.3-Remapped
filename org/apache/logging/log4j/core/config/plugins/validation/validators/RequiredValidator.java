/*    */ package org.apache.logging.log4j.core.config.plugins.validation.validators;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.apache.logging.log4j.core.config.plugins.validation.ConstraintValidator;
/*    */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
/*    */ import org.apache.logging.log4j.core.util.Assert;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RequiredValidator
/*    */   implements ConstraintValidator<Required>
/*    */ {
/* 42 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*    */   
/*    */   private Required annotation;
/*    */ 
/*    */   
/*    */   public void initialize(Required anAnnotation) {
/* 48 */     this.annotation = anAnnotation;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValid(String name, Object value) {
/* 53 */     return (Assert.isNonEmpty(value) || err(name));
/*    */   }
/*    */   
/*    */   private boolean err(String name) {
/* 57 */     LOGGER.error(this.annotation.message() + ": " + name);
/* 58 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\plugins\validation\validators\RequiredValidator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */