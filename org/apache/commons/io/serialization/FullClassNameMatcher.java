/*    */ package org.apache.commons.io.serialization;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Collections;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
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
/*    */ final class FullClassNameMatcher
/*    */   implements ClassNameMatcher
/*    */ {
/*    */   private final Set<String> classesSet;
/*    */   
/*    */   public FullClassNameMatcher(String... classes) {
/* 42 */     this.classesSet = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(classes)));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(String className) {
/* 47 */     return this.classesSet.contains(className);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\io\serialization\FullClassNameMatcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */