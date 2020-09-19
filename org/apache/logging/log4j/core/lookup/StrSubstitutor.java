/*      */ package org.apache.logging.log4j.core.lookup;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import org.apache.logging.log4j.core.LogEvent;
/*      */ import org.apache.logging.log4j.core.config.Configuration;
/*      */ import org.apache.logging.log4j.core.config.ConfigurationAware;
/*      */ import org.apache.logging.log4j.util.Strings;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class StrSubstitutor
/*      */   implements ConfigurationAware
/*      */ {
/*      */   public static final char DEFAULT_ESCAPE = '$';
/*  151 */   public static final StrMatcher DEFAULT_PREFIX = StrMatcher.stringMatcher("${");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  156 */   public static final StrMatcher DEFAULT_SUFFIX = StrMatcher.stringMatcher("}");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  161 */   public static final StrMatcher DEFAULT_VALUE_DELIMITER = StrMatcher.stringMatcher(":-");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int BUF_SIZE = 256;
/*      */ 
/*      */ 
/*      */   
/*      */   private char escapeChar;
/*      */ 
/*      */ 
/*      */   
/*      */   private StrMatcher prefixMatcher;
/*      */ 
/*      */ 
/*      */   
/*      */   private StrMatcher suffixMatcher;
/*      */ 
/*      */ 
/*      */   
/*      */   private StrMatcher valueDelimiterMatcher;
/*      */ 
/*      */ 
/*      */   
/*      */   private StrLookup variableResolver;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean enableSubstitutionInVariables = true;
/*      */ 
/*      */ 
/*      */   
/*      */   private Configuration configuration;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor() {
/*  200 */     this((StrLookup)null, DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor(Map<String, String> valueMap) {
/*  210 */     this(new MapLookup(valueMap), DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor(Map<String, String> valueMap, String prefix, String suffix) {
/*  222 */     this(new MapLookup(valueMap), prefix, suffix, '$');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor(Map<String, String> valueMap, String prefix, String suffix, char escape) {
/*  236 */     this(new MapLookup(valueMap), prefix, suffix, escape);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor(Map<String, String> valueMap, String prefix, String suffix, char escape, String valueDelimiter) {
/*  251 */     this(new MapLookup(valueMap), prefix, suffix, escape, valueDelimiter);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor(Properties properties) {
/*  261 */     this(toTypeSafeMap(properties));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor(StrLookup variableResolver) {
/*  270 */     this(variableResolver, DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor(StrLookup variableResolver, String prefix, String suffix, char escape) {
/*  284 */     setVariableResolver(variableResolver);
/*  285 */     setVariablePrefix(prefix);
/*  286 */     setVariableSuffix(suffix);
/*  287 */     setEscapeChar(escape);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor(StrLookup variableResolver, String prefix, String suffix, char escape, String valueDelimiter) {
/*  301 */     setVariableResolver(variableResolver);
/*  302 */     setVariablePrefix(prefix);
/*  303 */     setVariableSuffix(suffix);
/*  304 */     setEscapeChar(escape);
/*  305 */     setValueDelimiter(valueDelimiter);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor(StrLookup variableResolver, StrMatcher prefixMatcher, StrMatcher suffixMatcher, char escape) {
/*  320 */     this(variableResolver, prefixMatcher, suffixMatcher, escape, DEFAULT_VALUE_DELIMITER);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor(StrLookup variableResolver, StrMatcher prefixMatcher, StrMatcher suffixMatcher, char escape, StrMatcher valueDelimiterMatcher) {
/*  335 */     setVariableResolver(variableResolver);
/*  336 */     setVariablePrefixMatcher(prefixMatcher);
/*  337 */     setVariableSuffixMatcher(suffixMatcher);
/*  338 */     setEscapeChar(escape);
/*  339 */     setValueDelimiterMatcher(valueDelimiterMatcher);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String replace(Object source, Map<String, String> valueMap) {
/*  352 */     return (new StrSubstitutor(valueMap)).replace(source);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String replace(Object source, Map<String, String> valueMap, String prefix, String suffix) {
/*  369 */     return (new StrSubstitutor(valueMap, prefix, suffix)).replace(source);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String replace(Object source, Properties valueProperties) {
/*  381 */     if (valueProperties == null) {
/*  382 */       return source.toString();
/*      */     }
/*  384 */     Map<String, String> valueMap = new HashMap<>();
/*  385 */     Enumeration<?> propNames = valueProperties.propertyNames();
/*  386 */     while (propNames.hasMoreElements()) {
/*  387 */       String propName = (String)propNames.nextElement();
/*  388 */       String propValue = valueProperties.getProperty(propName);
/*  389 */       valueMap.put(propName, propValue);
/*      */     } 
/*  391 */     return replace(source, valueMap);
/*      */   }
/*      */   
/*      */   private static Map<String, String> toTypeSafeMap(Properties properties) {
/*  395 */     Map<String, String> map = new HashMap<>(properties.size());
/*  396 */     for (String name : properties.stringPropertyNames()) {
/*  397 */       map.put(name, properties.getProperty(name));
/*      */     }
/*  399 */     return map;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(String source) {
/*  411 */     return replace((LogEvent)null, source);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(LogEvent event, String source) {
/*  423 */     if (source == null) {
/*  424 */       return null;
/*      */     }
/*  426 */     StringBuilder buf = new StringBuilder(source);
/*  427 */     if (!substitute(event, buf, 0, source.length())) {
/*  428 */       return source;
/*      */     }
/*  430 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(String source, int offset, int length) {
/*  447 */     return replace((LogEvent)null, source, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(LogEvent event, String source, int offset, int length) {
/*  465 */     if (source == null) {
/*  466 */       return null;
/*      */     }
/*  468 */     StringBuilder buf = (new StringBuilder(length)).append(source, offset, length);
/*  469 */     if (!substitute(event, buf, 0, length)) {
/*  470 */       return source.substring(offset, offset + length);
/*      */     }
/*  472 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(char[] source) {
/*  485 */     return replace((LogEvent)null, source);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(LogEvent event, char[] source) {
/*  499 */     if (source == null) {
/*  500 */       return null;
/*      */     }
/*  502 */     StringBuilder buf = (new StringBuilder(source.length)).append(source);
/*  503 */     substitute(event, buf, 0, source.length);
/*  504 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(char[] source, int offset, int length) {
/*  522 */     return replace((LogEvent)null, source, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(LogEvent event, char[] source, int offset, int length) {
/*  541 */     if (source == null) {
/*  542 */       return null;
/*      */     }
/*  544 */     StringBuilder buf = (new StringBuilder(length)).append(source, offset, length);
/*  545 */     substitute(event, buf, 0, length);
/*  546 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(StringBuffer source) {
/*  559 */     return replace((LogEvent)null, source);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(LogEvent event, StringBuffer source) {
/*  573 */     if (source == null) {
/*  574 */       return null;
/*      */     }
/*  576 */     StringBuilder buf = (new StringBuilder(source.length())).append(source);
/*  577 */     substitute(event, buf, 0, buf.length());
/*  578 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(StringBuffer source, int offset, int length) {
/*  596 */     return replace((LogEvent)null, source, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(LogEvent event, StringBuffer source, int offset, int length) {
/*  615 */     if (source == null) {
/*  616 */       return null;
/*      */     }
/*  618 */     StringBuilder buf = (new StringBuilder(length)).append(source, offset, length);
/*  619 */     substitute(event, buf, 0, length);
/*  620 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(StringBuilder source) {
/*  633 */     return replace((LogEvent)null, source);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(LogEvent event, StringBuilder source) {
/*  647 */     if (source == null) {
/*  648 */       return null;
/*      */     }
/*  650 */     StringBuilder buf = (new StringBuilder(source.length())).append(source);
/*  651 */     substitute(event, buf, 0, buf.length());
/*  652 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(StringBuilder source, int offset, int length) {
/*  669 */     return replace((LogEvent)null, source, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(LogEvent event, StringBuilder source, int offset, int length) {
/*  688 */     if (source == null) {
/*  689 */       return null;
/*      */     }
/*  691 */     StringBuilder buf = (new StringBuilder(length)).append(source, offset, length);
/*  692 */     substitute(event, buf, 0, length);
/*  693 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(Object source) {
/*  706 */     return replace((LogEvent)null, source);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String replace(LogEvent event, Object source) {
/*  719 */     if (source == null) {
/*  720 */       return null;
/*      */     }
/*  722 */     StringBuilder buf = (new StringBuilder()).append(source);
/*  723 */     substitute(event, buf, 0, buf.length());
/*  724 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean replaceIn(StringBuffer source) {
/*  737 */     if (source == null) {
/*  738 */       return false;
/*      */     }
/*  740 */     return replaceIn(source, 0, source.length());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean replaceIn(StringBuffer source, int offset, int length) {
/*  758 */     return replaceIn((LogEvent)null, source, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean replaceIn(LogEvent event, StringBuffer source, int offset, int length) {
/*  777 */     if (source == null) {
/*  778 */       return false;
/*      */     }
/*  780 */     StringBuilder buf = (new StringBuilder(length)).append(source, offset, length);
/*  781 */     if (!substitute(event, buf, 0, length)) {
/*  782 */       return false;
/*      */     }
/*  784 */     source.replace(offset, offset + length, buf.toString());
/*  785 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean replaceIn(StringBuilder source) {
/*  797 */     return replaceIn(null, source);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean replaceIn(LogEvent event, StringBuilder source) {
/*  810 */     if (source == null) {
/*  811 */       return false;
/*      */     }
/*  813 */     return substitute(event, source, 0, source.length());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean replaceIn(StringBuilder source, int offset, int length) {
/*  829 */     return replaceIn((LogEvent)null, source, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean replaceIn(LogEvent event, StringBuilder source, int offset, int length) {
/*  847 */     if (source == null) {
/*  848 */       return false;
/*      */     }
/*  850 */     return substitute(event, source, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean substitute(LogEvent event, StringBuilder buf, int offset, int length) {
/*  872 */     return (substitute(event, buf, offset, length, null) > 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int substitute(LogEvent event, StringBuilder buf, int offset, int length, List<String> priorVariables) {
/*  890 */     StrMatcher prefixMatcher = getVariablePrefixMatcher();
/*  891 */     StrMatcher suffixMatcher = getVariableSuffixMatcher();
/*  892 */     char escape = getEscapeChar();
/*  893 */     StrMatcher valueDelimiterMatcher = getValueDelimiterMatcher();
/*  894 */     boolean substitutionInVariablesEnabled = isEnableSubstitutionInVariables();
/*      */     
/*  896 */     boolean top = (priorVariables == null);
/*  897 */     boolean altered = false;
/*  898 */     int lengthChange = 0;
/*  899 */     char[] chars = getChars(buf);
/*  900 */     int bufEnd = offset + length;
/*  901 */     int pos = offset;
/*  902 */     while (pos < bufEnd) {
/*  903 */       int startMatchLen = prefixMatcher.isMatch(chars, pos, offset, bufEnd);
/*  904 */       if (startMatchLen == 0) {
/*  905 */         pos++;
/*      */         continue;
/*      */       } 
/*  908 */       if (pos > offset && chars[pos - 1] == escape) {
/*      */         
/*  910 */         buf.deleteCharAt(pos - 1);
/*  911 */         chars = getChars(buf);
/*  912 */         lengthChange--;
/*  913 */         altered = true;
/*  914 */         bufEnd--;
/*      */         continue;
/*      */       } 
/*  917 */       int startPos = pos;
/*  918 */       pos += startMatchLen;
/*  919 */       int endMatchLen = 0;
/*  920 */       int nestedVarCount = 0;
/*  921 */       while (pos < bufEnd) {
/*  922 */         if (substitutionInVariablesEnabled && (endMatchLen = prefixMatcher.isMatch(chars, pos, offset, bufEnd)) != 0) {
/*      */ 
/*      */           
/*  925 */           nestedVarCount++;
/*  926 */           pos += endMatchLen;
/*      */           
/*      */           continue;
/*      */         } 
/*  930 */         endMatchLen = suffixMatcher.isMatch(chars, pos, offset, bufEnd);
/*  931 */         if (endMatchLen == 0) {
/*  932 */           pos++;
/*      */           continue;
/*      */         } 
/*  935 */         if (nestedVarCount == 0) {
/*  936 */           String varNameExpr = new String(chars, startPos + startMatchLen, pos - startPos - startMatchLen);
/*  937 */           if (substitutionInVariablesEnabled) {
/*  938 */             StringBuilder bufName = new StringBuilder(varNameExpr);
/*  939 */             substitute(event, bufName, 0, bufName.length());
/*  940 */             varNameExpr = bufName.toString();
/*      */           } 
/*  942 */           pos += endMatchLen;
/*  943 */           int endPos = pos;
/*      */           
/*  945 */           String varName = varNameExpr;
/*  946 */           String varDefaultValue = null;
/*      */           
/*  948 */           if (valueDelimiterMatcher != null) {
/*  949 */             char[] varNameExprChars = varNameExpr.toCharArray();
/*  950 */             int valueDelimiterMatchLen = 0;
/*  951 */             for (int i = 0; i < varNameExprChars.length; i++) {
/*      */               
/*  953 */               if (!substitutionInVariablesEnabled && prefixMatcher.isMatch(varNameExprChars, i, i, varNameExprChars.length) != 0) {
/*      */                 break;
/*      */               }
/*      */               
/*  957 */               if ((valueDelimiterMatchLen = valueDelimiterMatcher.isMatch(varNameExprChars, i)) != 0) {
/*  958 */                 varName = varNameExpr.substring(0, i);
/*  959 */                 varDefaultValue = varNameExpr.substring(i + valueDelimiterMatchLen);
/*      */                 
/*      */                 break;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */           
/*  966 */           if (priorVariables == null) {
/*  967 */             priorVariables = new ArrayList<>();
/*  968 */             priorVariables.add(new String(chars, offset, length + lengthChange));
/*      */           } 
/*      */ 
/*      */           
/*  972 */           checkCyclicSubstitution(varName, priorVariables);
/*  973 */           priorVariables.add(varName);
/*      */ 
/*      */           
/*  976 */           String varValue = resolveVariable(event, varName, buf, startPos, endPos);
/*  977 */           if (varValue == null) {
/*  978 */             varValue = varDefaultValue;
/*      */           }
/*  980 */           if (varValue != null) {
/*      */             
/*  982 */             int varLen = varValue.length();
/*  983 */             buf.replace(startPos, endPos, varValue);
/*  984 */             altered = true;
/*  985 */             int change = substitute(event, buf, startPos, varLen, priorVariables);
/*  986 */             change += varLen - endPos - startPos;
/*  987 */             pos += change;
/*  988 */             bufEnd += change;
/*  989 */             lengthChange += change;
/*  990 */             chars = getChars(buf);
/*      */           } 
/*      */ 
/*      */           
/*  994 */           priorVariables.remove(priorVariables.size() - 1);
/*      */           break;
/*      */         } 
/*  997 */         nestedVarCount--;
/*  998 */         pos += endMatchLen;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1004 */     if (top) {
/* 1005 */       return altered ? 1 : 0;
/*      */     }
/* 1007 */     return lengthChange;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void checkCyclicSubstitution(String varName, List<String> priorVariables) {
/* 1017 */     if (!priorVariables.contains(varName)) {
/*      */       return;
/*      */     }
/* 1020 */     StringBuilder buf = new StringBuilder(256);
/* 1021 */     buf.append("Infinite loop in property interpolation of ");
/* 1022 */     buf.append(priorVariables.remove(0));
/* 1023 */     buf.append(": ");
/* 1024 */     appendWithSeparators(buf, priorVariables, "->");
/* 1025 */     throw new IllegalStateException(buf.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String resolveVariable(LogEvent event, String variableName, StringBuilder buf, int startPos, int endPos) {
/* 1050 */     StrLookup resolver = getVariableResolver();
/* 1051 */     if (resolver == null) {
/* 1052 */       return null;
/*      */     }
/* 1054 */     return resolver.lookup(event, variableName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public char getEscapeChar() {
/* 1065 */     return this.escapeChar;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEscapeChar(char escapeCharacter) {
/* 1076 */     this.escapeChar = escapeCharacter;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrMatcher getVariablePrefixMatcher() {
/* 1092 */     return this.prefixMatcher;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor setVariablePrefixMatcher(StrMatcher prefixMatcher) {
/* 1108 */     if (prefixMatcher == null) {
/* 1109 */       throw new IllegalArgumentException("Variable prefix matcher must not be null!");
/*      */     }
/* 1111 */     this.prefixMatcher = prefixMatcher;
/* 1112 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor setVariablePrefix(char prefix) {
/* 1127 */     return setVariablePrefixMatcher(StrMatcher.charMatcher(prefix));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor setVariablePrefix(String prefix) {
/* 1142 */     if (prefix == null) {
/* 1143 */       throw new IllegalArgumentException("Variable prefix must not be null!");
/*      */     }
/* 1145 */     return setVariablePrefixMatcher(StrMatcher.stringMatcher(prefix));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrMatcher getVariableSuffixMatcher() {
/* 1161 */     return this.suffixMatcher;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor setVariableSuffixMatcher(StrMatcher suffixMatcher) {
/* 1177 */     if (suffixMatcher == null) {
/* 1178 */       throw new IllegalArgumentException("Variable suffix matcher must not be null!");
/*      */     }
/* 1180 */     this.suffixMatcher = suffixMatcher;
/* 1181 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor setVariableSuffix(char suffix) {
/* 1196 */     return setVariableSuffixMatcher(StrMatcher.charMatcher(suffix));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor setVariableSuffix(String suffix) {
/* 1211 */     if (suffix == null) {
/* 1212 */       throw new IllegalArgumentException("Variable suffix must not be null!");
/*      */     }
/* 1214 */     return setVariableSuffixMatcher(StrMatcher.stringMatcher(suffix));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrMatcher getValueDelimiterMatcher() {
/* 1233 */     return this.valueDelimiterMatcher;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor setValueDelimiterMatcher(StrMatcher valueDelimiterMatcher) {
/* 1252 */     this.valueDelimiterMatcher = valueDelimiterMatcher;
/* 1253 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor setValueDelimiter(char valueDelimiter) {
/* 1268 */     return setValueDelimiterMatcher(StrMatcher.charMatcher(valueDelimiter));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor setValueDelimiter(String valueDelimiter) {
/* 1287 */     if (Strings.isEmpty(valueDelimiter)) {
/* 1288 */       setValueDelimiterMatcher(null);
/* 1289 */       return this;
/*      */     } 
/* 1291 */     return setValueDelimiterMatcher(StrMatcher.stringMatcher(valueDelimiter));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrLookup getVariableResolver() {
/* 1302 */     return this.variableResolver;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setVariableResolver(StrLookup variableResolver) {
/* 1311 */     if (variableResolver instanceof ConfigurationAware && this.configuration != null) {
/* 1312 */       ((ConfigurationAware)variableResolver).setConfiguration(this.configuration);
/*      */     }
/* 1314 */     this.variableResolver = variableResolver;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEnableSubstitutionInVariables() {
/* 1325 */     return this.enableSubstitutionInVariables;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEnableSubstitutionInVariables(boolean enableSubstitutionInVariables) {
/* 1337 */     this.enableSubstitutionInVariables = enableSubstitutionInVariables;
/*      */   }
/*      */   
/*      */   private char[] getChars(StringBuilder sb) {
/* 1341 */     char[] chars = new char[sb.length()];
/* 1342 */     sb.getChars(0, sb.length(), chars, 0);
/* 1343 */     return chars;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void appendWithSeparators(StringBuilder sb, Iterable<?> iterable, String separator) {
/* 1356 */     if (iterable != null) {
/* 1357 */       separator = (separator == null) ? "" : separator;
/* 1358 */       Iterator<?> it = iterable.iterator();
/* 1359 */       while (it.hasNext()) {
/* 1360 */         sb.append(it.next());
/* 1361 */         if (it.hasNext()) {
/* 1362 */           sb.append(separator);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1370 */     return "StrSubstitutor(" + this.variableResolver.toString() + ')';
/*      */   }
/*      */ 
/*      */   
/*      */   public void setConfiguration(Configuration configuration) {
/* 1375 */     this.configuration = configuration;
/* 1376 */     if (this.variableResolver instanceof ConfigurationAware)
/* 1377 */       ((ConfigurationAware)this.variableResolver).setConfiguration(this.configuration); 
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\lookup\StrSubstitutor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */