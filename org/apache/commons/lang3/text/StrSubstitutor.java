/*      */ package org.apache.commons.lang3.text;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import org.apache.commons.lang3.StringUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */ {
/*      */   public static final char DEFAULT_ESCAPE = '$';
/*  133 */   public static final StrMatcher DEFAULT_PREFIX = StrMatcher.stringMatcher("${");
/*      */ 
/*      */ 
/*      */   
/*  137 */   public static final StrMatcher DEFAULT_SUFFIX = StrMatcher.stringMatcher("}");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  142 */   public static final StrMatcher DEFAULT_VALUE_DELIMITER = StrMatcher.stringMatcher(":-");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private char escapeChar;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private StrMatcher prefixMatcher;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private StrMatcher suffixMatcher;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private StrMatcher valueDelimiterMatcher;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private StrLookup<?> variableResolver;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean enableSubstitutionInVariables;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean preserveEscapes = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V> String replace(Object source, Map<String, V> valueMap) {
/*  184 */     return (new StrSubstitutor(valueMap)).replace(source);
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
/*      */   public static <V> String replace(Object source, Map<String, V> valueMap, String prefix, String suffix) {
/*  201 */     return (new StrSubstitutor(valueMap, prefix, suffix)).replace(source);
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
/*  213 */     if (valueProperties == null) {
/*  214 */       return source.toString();
/*      */     }
/*  216 */     Map<String, String> valueMap = new HashMap<String, String>();
/*  217 */     Enumeration<?> propNames = valueProperties.propertyNames();
/*  218 */     while (propNames.hasMoreElements()) {
/*  219 */       String propName = (String)propNames.nextElement();
/*  220 */       String propValue = valueProperties.getProperty(propName);
/*  221 */       valueMap.put(propName, propValue);
/*      */     } 
/*  223 */     return replace(source, valueMap);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String replaceSystemProperties(Object source) {
/*  234 */     return (new StrSubstitutor(StrLookup.systemPropertiesLookup())).replace(source);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor() {
/*  243 */     this((StrLookup)null, DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <V> StrSubstitutor(Map<String, V> valueMap) {
/*  254 */     this(StrLookup.mapLookup(valueMap), DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
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
/*      */   public <V> StrSubstitutor(Map<String, V> valueMap, String prefix, String suffix) {
/*  267 */     this(StrLookup.mapLookup(valueMap), prefix, suffix, '$');
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
/*      */   public <V> StrSubstitutor(Map<String, V> valueMap, String prefix, String suffix, char escape) {
/*  282 */     this(StrLookup.mapLookup(valueMap), prefix, suffix, escape);
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
/*      */   public <V> StrSubstitutor(Map<String, V> valueMap, String prefix, String suffix, char escape, String valueDelimiter) {
/*  299 */     this(StrLookup.mapLookup(valueMap), prefix, suffix, escape, valueDelimiter);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor(StrLookup<?> variableResolver) {
/*  308 */     this(variableResolver, DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
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
/*      */   public StrSubstitutor(StrLookup<?> variableResolver, String prefix, String suffix, char escape) {
/*  322 */     setVariableResolver(variableResolver);
/*  323 */     setVariablePrefix(prefix);
/*  324 */     setVariableSuffix(suffix);
/*  325 */     setEscapeChar(escape);
/*  326 */     setValueDelimiterMatcher(DEFAULT_VALUE_DELIMITER);
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
/*      */   public StrSubstitutor(StrLookup<?> variableResolver, String prefix, String suffix, char escape, String valueDelimiter) {
/*  342 */     setVariableResolver(variableResolver);
/*  343 */     setVariablePrefix(prefix);
/*  344 */     setVariableSuffix(suffix);
/*  345 */     setEscapeChar(escape);
/*  346 */     setValueDelimiter(valueDelimiter);
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
/*      */   public StrSubstitutor(StrLookup<?> variableResolver, StrMatcher prefixMatcher, StrMatcher suffixMatcher, char escape) {
/*  361 */     this(variableResolver, prefixMatcher, suffixMatcher, escape, DEFAULT_VALUE_DELIMITER);
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
/*      */   public StrSubstitutor(StrLookup<?> variableResolver, StrMatcher prefixMatcher, StrMatcher suffixMatcher, char escape, StrMatcher valueDelimiterMatcher) {
/*  378 */     setVariableResolver(variableResolver);
/*  379 */     setVariablePrefixMatcher(prefixMatcher);
/*  380 */     setVariableSuffixMatcher(suffixMatcher);
/*  381 */     setEscapeChar(escape);
/*  382 */     setValueDelimiterMatcher(valueDelimiterMatcher);
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
/*  394 */     if (source == null) {
/*  395 */       return null;
/*      */     }
/*  397 */     StrBuilder buf = new StrBuilder(source);
/*  398 */     if (!substitute(buf, 0, source.length())) {
/*  399 */       return source;
/*      */     }
/*  401 */     return buf.toString();
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
/*      */   public String replace(String source, int offset, int length) {
/*  417 */     if (source == null) {
/*  418 */       return null;
/*      */     }
/*  420 */     StrBuilder buf = (new StrBuilder(length)).append(source, offset, length);
/*  421 */     if (!substitute(buf, 0, length)) {
/*  422 */       return source.substring(offset, offset + length);
/*      */     }
/*  424 */     return buf.toString();
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
/*  437 */     if (source == null) {
/*  438 */       return null;
/*      */     }
/*  440 */     StrBuilder buf = (new StrBuilder(source.length)).append(source);
/*  441 */     substitute(buf, 0, source.length);
/*  442 */     return buf.toString();
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
/*      */   public String replace(char[] source, int offset, int length) {
/*  459 */     if (source == null) {
/*  460 */       return null;
/*      */     }
/*  462 */     StrBuilder buf = (new StrBuilder(length)).append(source, offset, length);
/*  463 */     substitute(buf, 0, length);
/*  464 */     return buf.toString();
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
/*  477 */     if (source == null) {
/*  478 */       return null;
/*      */     }
/*  480 */     StrBuilder buf = (new StrBuilder(source.length())).append(source);
/*  481 */     substitute(buf, 0, buf.length());
/*  482 */     return buf.toString();
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
/*      */   public String replace(StringBuffer source, int offset, int length) {
/*  499 */     if (source == null) {
/*  500 */       return null;
/*      */     }
/*  502 */     StrBuilder buf = (new StrBuilder(length)).append(source, offset, length);
/*  503 */     substitute(buf, 0, length);
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
/*      */   public String replace(CharSequence source) {
/*  517 */     if (source == null) {
/*  518 */       return null;
/*      */     }
/*  520 */     return replace(source, 0, source.length());
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
/*      */   public String replace(CharSequence source, int offset, int length) {
/*  538 */     if (source == null) {
/*  539 */       return null;
/*      */     }
/*  541 */     StrBuilder buf = (new StrBuilder(length)).append(source, offset, length);
/*  542 */     substitute(buf, 0, length);
/*  543 */     return buf.toString();
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
/*      */   public String replace(StrBuilder source) {
/*  556 */     if (source == null) {
/*  557 */       return null;
/*      */     }
/*  559 */     StrBuilder buf = (new StrBuilder(source.length())).append(source);
/*  560 */     substitute(buf, 0, buf.length());
/*  561 */     return buf.toString();
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
/*      */   public String replace(StrBuilder source, int offset, int length) {
/*  578 */     if (source == null) {
/*  579 */       return null;
/*      */     }
/*  581 */     StrBuilder buf = (new StrBuilder(length)).append(source, offset, length);
/*  582 */     substitute(buf, 0, length);
/*  583 */     return buf.toString();
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
/*  596 */     if (source == null) {
/*  597 */       return null;
/*      */     }
/*  599 */     StrBuilder buf = (new StrBuilder()).append(source);
/*  600 */     substitute(buf, 0, buf.length());
/*  601 */     return buf.toString();
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
/*  614 */     if (source == null) {
/*  615 */       return false;
/*      */     }
/*  617 */     return replaceIn(source, 0, source.length());
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
/*      */   public boolean replaceIn(StringBuffer source, int offset, int length) {
/*  634 */     if (source == null) {
/*  635 */       return false;
/*      */     }
/*  637 */     StrBuilder buf = (new StrBuilder(length)).append(source, offset, length);
/*  638 */     if (!substitute(buf, 0, length)) {
/*  639 */       return false;
/*      */     }
/*  641 */     source.replace(offset, offset + length, buf.toString());
/*  642 */     return true;
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
/*      */   public boolean replaceIn(StringBuilder source) {
/*  656 */     if (source == null) {
/*  657 */       return false;
/*      */     }
/*  659 */     return replaceIn(source, 0, source.length());
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
/*      */   public boolean replaceIn(StringBuilder source, int offset, int length) {
/*  677 */     if (source == null) {
/*  678 */       return false;
/*      */     }
/*  680 */     StrBuilder buf = (new StrBuilder(length)).append(source, offset, length);
/*  681 */     if (!substitute(buf, 0, length)) {
/*  682 */       return false;
/*      */     }
/*  684 */     source.replace(offset, offset + length, buf.toString());
/*  685 */     return true;
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
/*      */   public boolean replaceIn(StrBuilder source) {
/*  697 */     if (source == null) {
/*  698 */       return false;
/*      */     }
/*  700 */     return substitute(source, 0, source.length());
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
/*      */   public boolean replaceIn(StrBuilder source, int offset, int length) {
/*  716 */     if (source == null) {
/*  717 */       return false;
/*      */     }
/*  719 */     return substitute(source, offset, length);
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
/*      */   protected boolean substitute(StrBuilder buf, int offset, int length) {
/*  738 */     return (substitute(buf, offset, length, null) > 0);
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
/*      */   private int substitute(StrBuilder buf, int offset, int length, List<String> priorVariables) {
/*  754 */     StrMatcher pfxMatcher = getVariablePrefixMatcher();
/*  755 */     StrMatcher suffMatcher = getVariableSuffixMatcher();
/*  756 */     char escape = getEscapeChar();
/*  757 */     StrMatcher valueDelimMatcher = getValueDelimiterMatcher();
/*  758 */     boolean substitutionInVariablesEnabled = isEnableSubstitutionInVariables();
/*      */     
/*  760 */     boolean top = (priorVariables == null);
/*  761 */     boolean altered = false;
/*  762 */     int lengthChange = 0;
/*  763 */     char[] chars = buf.buffer;
/*  764 */     int bufEnd = offset + length;
/*  765 */     int pos = offset;
/*  766 */     while (pos < bufEnd) {
/*  767 */       int startMatchLen = pfxMatcher.isMatch(chars, pos, offset, bufEnd);
/*      */       
/*  769 */       if (startMatchLen == 0) {
/*  770 */         pos++;
/*      */         continue;
/*      */       } 
/*  773 */       if (pos > offset && chars[pos - 1] == escape) {
/*      */         
/*  775 */         if (this.preserveEscapes) {
/*  776 */           pos++;
/*      */           continue;
/*      */         } 
/*  779 */         buf.deleteCharAt(pos - 1);
/*  780 */         chars = buf.buffer;
/*  781 */         lengthChange--;
/*  782 */         altered = true;
/*  783 */         bufEnd--;
/*      */         continue;
/*      */       } 
/*  786 */       int startPos = pos;
/*  787 */       pos += startMatchLen;
/*  788 */       int endMatchLen = 0;
/*  789 */       int nestedVarCount = 0;
/*  790 */       while (pos < bufEnd) {
/*  791 */         if (substitutionInVariablesEnabled && (
/*  792 */           endMatchLen = pfxMatcher.isMatch(chars, pos, offset, bufEnd)) != 0) {
/*      */ 
/*      */           
/*  795 */           nestedVarCount++;
/*  796 */           pos += endMatchLen;
/*      */           
/*      */           continue;
/*      */         } 
/*  800 */         endMatchLen = suffMatcher.isMatch(chars, pos, offset, bufEnd);
/*      */         
/*  802 */         if (endMatchLen == 0) {
/*  803 */           pos++;
/*      */           continue;
/*      */         } 
/*  806 */         if (nestedVarCount == 0) {
/*  807 */           String varNameExpr = new String(chars, startPos + startMatchLen, pos - startPos - startMatchLen);
/*      */ 
/*      */           
/*  810 */           if (substitutionInVariablesEnabled) {
/*  811 */             StrBuilder bufName = new StrBuilder(varNameExpr);
/*  812 */             substitute(bufName, 0, bufName.length());
/*  813 */             varNameExpr = bufName.toString();
/*      */           } 
/*  815 */           pos += endMatchLen;
/*  816 */           int endPos = pos;
/*      */           
/*  818 */           String varName = varNameExpr;
/*  819 */           String varDefaultValue = null;
/*      */           
/*  821 */           if (valueDelimMatcher != null) {
/*  822 */             char[] varNameExprChars = varNameExpr.toCharArray();
/*  823 */             int valueDelimiterMatchLen = 0;
/*  824 */             for (int i = 0; i < varNameExprChars.length; i++) {
/*      */               
/*  826 */               if (!substitutionInVariablesEnabled && pfxMatcher
/*  827 */                 .isMatch(varNameExprChars, i, i, varNameExprChars.length) != 0) {
/*      */                 break;
/*      */               }
/*  830 */               if ((valueDelimiterMatchLen = valueDelimMatcher.isMatch(varNameExprChars, i)) != 0) {
/*  831 */                 varName = varNameExpr.substring(0, i);
/*  832 */                 varDefaultValue = varNameExpr.substring(i + valueDelimiterMatchLen);
/*      */                 
/*      */                 break;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */           
/*  839 */           if (priorVariables == null) {
/*  840 */             priorVariables = new ArrayList<String>();
/*  841 */             priorVariables.add(new String(chars, offset, length));
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/*  846 */           checkCyclicSubstitution(varName, priorVariables);
/*  847 */           priorVariables.add(varName);
/*      */ 
/*      */           
/*  850 */           String varValue = resolveVariable(varName, buf, startPos, endPos);
/*      */           
/*  852 */           if (varValue == null) {
/*  853 */             varValue = varDefaultValue;
/*      */           }
/*  855 */           if (varValue != null) {
/*      */             
/*  857 */             int varLen = varValue.length();
/*  858 */             buf.replace(startPos, endPos, varValue);
/*  859 */             altered = true;
/*  860 */             int change = substitute(buf, startPos, varLen, priorVariables);
/*      */             
/*  862 */             change = change + varLen - endPos - startPos;
/*      */             
/*  864 */             pos += change;
/*  865 */             bufEnd += change;
/*  866 */             lengthChange += change;
/*  867 */             chars = buf.buffer;
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/*  872 */           priorVariables
/*  873 */             .remove(priorVariables.size() - 1);
/*      */           break;
/*      */         } 
/*  876 */         nestedVarCount--;
/*  877 */         pos += endMatchLen;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  883 */     if (top) {
/*  884 */       return altered ? 1 : 0;
/*      */     }
/*  886 */     return lengthChange;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void checkCyclicSubstitution(String varName, List<String> priorVariables) {
/*  896 */     if (!priorVariables.contains(varName)) {
/*      */       return;
/*      */     }
/*  899 */     StrBuilder buf = new StrBuilder(256);
/*  900 */     buf.append("Infinite loop in property interpolation of ");
/*  901 */     buf.append(priorVariables.remove(0));
/*  902 */     buf.append(": ");
/*  903 */     buf.appendWithSeparators(priorVariables, "->");
/*  904 */     throw new IllegalStateException(buf.toString());
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
/*      */   protected String resolveVariable(String variableName, StrBuilder buf, int startPos, int endPos) {
/*  925 */     StrLookup<?> resolver = getVariableResolver();
/*  926 */     if (resolver == null) {
/*  927 */       return null;
/*      */     }
/*  929 */     return resolver.lookup(variableName);
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
/*  940 */     return this.escapeChar;
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
/*  951 */     this.escapeChar = escapeCharacter;
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
/*      */   public StrMatcher getVariablePrefixMatcher() {
/*  966 */     return this.prefixMatcher;
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
/*      */   public StrSubstitutor setVariablePrefixMatcher(StrMatcher prefixMatcher) {
/*  981 */     if (prefixMatcher == null) {
/*  982 */       throw new IllegalArgumentException("Variable prefix matcher must not be null!");
/*      */     }
/*  984 */     this.prefixMatcher = prefixMatcher;
/*  985 */     return this;
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
/*      */   public StrSubstitutor setVariablePrefix(char prefix) {
/*  999 */     return setVariablePrefixMatcher(StrMatcher.charMatcher(prefix));
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
/*      */   public StrSubstitutor setVariablePrefix(String prefix) {
/* 1013 */     if (prefix == null) {
/* 1014 */       throw new IllegalArgumentException("Variable prefix must not be null!");
/*      */     }
/* 1016 */     return setVariablePrefixMatcher(StrMatcher.stringMatcher(prefix));
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
/*      */   public StrMatcher getVariableSuffixMatcher() {
/* 1031 */     return this.suffixMatcher;
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
/*      */   public StrSubstitutor setVariableSuffixMatcher(StrMatcher suffixMatcher) {
/* 1046 */     if (suffixMatcher == null) {
/* 1047 */       throw new IllegalArgumentException("Variable suffix matcher must not be null!");
/*      */     }
/* 1049 */     this.suffixMatcher = suffixMatcher;
/* 1050 */     return this;
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
/*      */   public StrSubstitutor setVariableSuffix(char suffix) {
/* 1064 */     return setVariableSuffixMatcher(StrMatcher.charMatcher(suffix));
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
/*      */   public StrSubstitutor setVariableSuffix(String suffix) {
/* 1078 */     if (suffix == null) {
/* 1079 */       throw new IllegalArgumentException("Variable suffix must not be null!");
/*      */     }
/* 1081 */     return setVariableSuffixMatcher(StrMatcher.stringMatcher(suffix));
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
/*      */   public StrMatcher getValueDelimiterMatcher() {
/* 1099 */     return this.valueDelimiterMatcher;
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
/*      */   public StrSubstitutor setValueDelimiterMatcher(StrMatcher valueDelimiterMatcher) {
/* 1117 */     this.valueDelimiterMatcher = valueDelimiterMatcher;
/* 1118 */     return this;
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
/* 1133 */     return setValueDelimiterMatcher(StrMatcher.charMatcher(valueDelimiter));
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
/*      */   public StrSubstitutor setValueDelimiter(String valueDelimiter) {
/* 1151 */     if (StringUtils.isEmpty(valueDelimiter)) {
/* 1152 */       setValueDelimiterMatcher(null);
/* 1153 */       return this;
/*      */     } 
/* 1155 */     return setValueDelimiterMatcher(StrMatcher.stringMatcher(valueDelimiter));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrLookup<?> getVariableResolver() {
/* 1166 */     return this.variableResolver;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setVariableResolver(StrLookup<?> variableResolver) {
/* 1175 */     this.variableResolver = variableResolver;
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
/*      */   public boolean isEnableSubstitutionInVariables() {
/* 1187 */     return this.enableSubstitutionInVariables;
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
/*      */   public void setEnableSubstitutionInVariables(boolean enableSubstitutionInVariables) {
/* 1201 */     this.enableSubstitutionInVariables = enableSubstitutionInVariables;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPreserveEscapes() {
/* 1212 */     return this.preserveEscapes;
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
/*      */   public void setPreserveEscapes(boolean preserveEscapes) {
/* 1228 */     this.preserveEscapes = preserveEscapes;
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\text\StrSubstitutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */