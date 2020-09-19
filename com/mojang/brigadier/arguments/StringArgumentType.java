/*    */ package com.mojang.brigadier.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StringArgumentType
/*    */   implements ArgumentType<String>
/*    */ {
/*    */   private final StringType type;
/*    */   
/*    */   private StringArgumentType(StringType type) {
/* 17 */     this.type = type;
/*    */   }
/*    */   
/*    */   public static StringArgumentType word() {
/* 21 */     return new StringArgumentType(StringType.SINGLE_WORD);
/*    */   }
/*    */   
/*    */   public static StringArgumentType string() {
/* 25 */     return new StringArgumentType(StringType.QUOTABLE_PHRASE);
/*    */   }
/*    */   
/*    */   public static StringArgumentType greedyString() {
/* 29 */     return new StringArgumentType(StringType.GREEDY_PHRASE);
/*    */   }
/*    */   
/*    */   public static String getString(CommandContext<?> context, String name) {
/* 33 */     return (String)context.getArgument(name, String.class);
/*    */   }
/*    */   
/*    */   public StringType getType() {
/* 37 */     return this.type;
/*    */   }
/*    */ 
/*    */   
/*    */   public String parse(StringReader reader) throws CommandSyntaxException {
/* 42 */     if (this.type == StringType.GREEDY_PHRASE) {
/* 43 */       String text = reader.getRemaining();
/* 44 */       reader.setCursor(reader.getTotalLength());
/* 45 */       return text;
/* 46 */     }  if (this.type == StringType.SINGLE_WORD) {
/* 47 */       return reader.readUnquotedString();
/*    */     }
/* 49 */     return reader.readString();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 55 */     return "string()";
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 60 */     return this.type.getExamples();
/*    */   }
/*    */   
/*    */   public static String escapeIfRequired(String input) {
/* 64 */     for (char c : input.toCharArray()) {
/* 65 */       if (!StringReader.isAllowedInUnquotedString(c)) {
/* 66 */         return escape(input);
/*    */       }
/*    */     } 
/* 69 */     return input;
/*    */   }
/*    */   
/*    */   private static String escape(String input) {
/* 73 */     StringBuilder result = new StringBuilder("\"");
/*    */     
/* 75 */     for (int i = 0; i < input.length(); i++) {
/* 76 */       char c = input.charAt(i);
/* 77 */       if (c == '\\' || c == '"') {
/* 78 */         result.append('\\');
/*    */       }
/* 80 */       result.append(c);
/*    */     } 
/*    */     
/* 83 */     result.append("\"");
/* 84 */     return result.toString();
/*    */   }
/*    */   
/*    */   public enum StringType {
/* 88 */     SINGLE_WORD((String)new String[] { "word", "words_with_underscores" }),
/* 89 */     QUOTABLE_PHRASE((String)new String[] { "\"quoted phrase\"", "word", "\"\"" }),
/* 90 */     GREEDY_PHRASE((String)new String[] { "word", "words with spaces", "\"and symbols\"" });
/*    */     
/*    */     private final Collection<String> examples;
/*    */     
/*    */     StringType(String... examples) {
/* 95 */       this.examples = Arrays.asList(examples);
/*    */     }
/*    */     
/*    */     public Collection<String> getExamples() {
/* 99 */       return this.examples;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\brigadier\arguments\StringArgumentType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */