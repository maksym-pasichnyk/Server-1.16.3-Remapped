/*     */ package org.apache.logging.log4j.core.impl;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.ThreadContext;
/*     */ import org.apache.logging.log4j.core.ContextDataInjector;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.spi.ReadOnlyThreadContextMap;
/*     */ import org.apache.logging.log4j.util.ReadOnlyStringMap;
/*     */ import org.apache.logging.log4j.util.StringMap;
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
/*     */ public class ThreadContextDataInjector
/*     */ {
/*     */   public static class ForDefaultThreadContextMap
/*     */     implements ContextDataInjector
/*     */   {
/*     */     public StringMap injectContextData(List<Property> props, StringMap ignore) {
/*  66 */       Map<String, String> copy = ThreadContext.getImmutableContext();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  72 */       if (props == null || props.isEmpty())
/*     */       {
/*     */         
/*  75 */         return copy.isEmpty() ? ContextDataFactory.emptyFrozenContextData() : frozenStringMap(copy);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  81 */       StringMap result = new JdkMapAdapterStringMap(new HashMap<>(copy));
/*  82 */       for (int i = 0; i < props.size(); i++) {
/*  83 */         Property prop = props.get(i);
/*  84 */         if (!copy.containsKey(prop.getName())) {
/*  85 */           result.putValue(prop.getName(), prop.getValue());
/*     */         }
/*     */       } 
/*  88 */       result.freeze();
/*  89 */       return result;
/*     */     }
/*     */     
/*     */     private static JdkMapAdapterStringMap frozenStringMap(Map<String, String> copy) {
/*  93 */       JdkMapAdapterStringMap result = new JdkMapAdapterStringMap(copy);
/*  94 */       result.freeze();
/*  95 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public ReadOnlyStringMap rawContextData() {
/* 100 */       ReadOnlyThreadContextMap map = ThreadContext.getThreadContextMap();
/* 101 */       if (map instanceof ReadOnlyStringMap) {
/* 102 */         return (ReadOnlyStringMap)map;
/*     */       }
/*     */       
/* 105 */       Map<String, String> copy = ThreadContext.getImmutableContext();
/* 106 */       return copy.isEmpty() ? (ReadOnlyStringMap)ContextDataFactory.emptyFrozenContextData() : (ReadOnlyStringMap)new JdkMapAdapterStringMap(copy);
/*     */     }
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
/*     */   public static class ForGarbageFreeThreadContextMap
/*     */     implements ContextDataInjector
/*     */   {
/*     */     public StringMap injectContextData(List<Property> props, StringMap reusable) {
/* 130 */       ThreadContextDataInjector.copyProperties(props, reusable);
/*     */       
/* 132 */       StringMap stringMap = ThreadContext.getThreadContextMap().getReadOnlyContextData();
/* 133 */       reusable.putAll((ReadOnlyStringMap)stringMap);
/* 134 */       return reusable;
/*     */     }
/*     */ 
/*     */     
/*     */     public ReadOnlyStringMap rawContextData() {
/* 139 */       return (ReadOnlyStringMap)ThreadContext.getThreadContextMap().getReadOnlyContextData();
/*     */     }
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
/*     */   public static class ForCopyOnWriteThreadContextMap
/*     */     implements ContextDataInjector
/*     */   {
/*     */     public StringMap injectContextData(List<Property> props, StringMap ignore) {
/* 165 */       StringMap immutableCopy = ThreadContext.getThreadContextMap().getReadOnlyContextData();
/* 166 */       if (props == null || props.isEmpty()) {
/* 167 */         return immutableCopy;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 173 */       StringMap result = ContextDataFactory.createContextData(props.size() + immutableCopy.size());
/* 174 */       ThreadContextDataInjector.copyProperties(props, result);
/* 175 */       result.putAll((ReadOnlyStringMap)immutableCopy);
/* 176 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public ReadOnlyStringMap rawContextData() {
/* 181 */       return (ReadOnlyStringMap)ThreadContext.getThreadContextMap().getReadOnlyContextData();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void copyProperties(List<Property> properties, StringMap result) {
/* 192 */     if (properties != null)
/* 193 */       for (int i = 0; i < properties.size(); i++) {
/* 194 */         Property prop = properties.get(i);
/* 195 */         result.putValue(prop.getName(), prop.getValue());
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\impl\ThreadContextDataInjector.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */