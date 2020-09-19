/*     */ package net.minecraft;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ public enum ChatFormatting {
/*     */   private static final Map<String, ChatFormatting> FORMATTING_BY_NAME;
/*     */   private static final Pattern STRIP_FORMATTING_PATTERN;
/*     */   private final String name;
/*     */   private final char code;
/*  15 */   BLACK("BLACK", '0', 0, Integer.valueOf(0)),
/*  16 */   DARK_BLUE("DARK_BLUE", '1', 1, Integer.valueOf(170)),
/*  17 */   DARK_GREEN("DARK_GREEN", '2', 2, Integer.valueOf(43520)),
/*  18 */   DARK_AQUA("DARK_AQUA", '3', 3, Integer.valueOf(43690)),
/*  19 */   DARK_RED("DARK_RED", '4', 4, Integer.valueOf(11141120)),
/*  20 */   DARK_PURPLE("DARK_PURPLE", '5', 5, Integer.valueOf(11141290)),
/*  21 */   GOLD("GOLD", '6', 6, Integer.valueOf(16755200)),
/*  22 */   GRAY("GRAY", '7', 7, Integer.valueOf(11184810)),
/*  23 */   DARK_GRAY("DARK_GRAY", '8', 8, Integer.valueOf(5592405)),
/*  24 */   BLUE("BLUE", '9', 9, Integer.valueOf(5592575)),
/*  25 */   GREEN("GREEN", 'a', 10, Integer.valueOf(5635925)),
/*  26 */   AQUA("AQUA", 'b', 11, Integer.valueOf(5636095)),
/*  27 */   RED("RED", 'c', 12, Integer.valueOf(16733525)),
/*  28 */   LIGHT_PURPLE("LIGHT_PURPLE", 'd', 13, Integer.valueOf(16733695)),
/*  29 */   YELLOW("YELLOW", 'e', 14, Integer.valueOf(16777045)),
/*  30 */   WHITE("WHITE", 'f', 15, Integer.valueOf(16777215)),
/*  31 */   OBFUSCATED("OBFUSCATED", 'k', true),
/*  32 */   BOLD("BOLD", 'l', true),
/*  33 */   STRIKETHROUGH("STRIKETHROUGH", 'm', true),
/*  34 */   UNDERLINE("UNDERLINE", 'n', true),
/*  35 */   ITALIC("ITALIC", 'o', true),
/*  36 */   RESET("RESET", 'r', -1, null); private final boolean isFormat; private final String toString;
/*     */   
/*     */   static {
/*  39 */     FORMATTING_BY_NAME = (Map<String, ChatFormatting>)Arrays.<ChatFormatting>stream(values()).collect(Collectors.toMap(debug0 -> cleanName(debug0.name), debug0 -> debug0));
/*  40 */     STRIP_FORMATTING_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]");
/*     */   } private final int id; @Nullable
/*     */   private final Integer color; private static String cleanName(String debug0) {
/*  43 */     return debug0.toLowerCase(Locale.ROOT).replaceAll("[^a-z]", "");
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
/*     */   ChatFormatting(String debug3, char debug4, @Nullable boolean debug5, int debug6, Integer debug7) {
/*  63 */     this.name = debug3;
/*  64 */     this.code = debug4;
/*  65 */     this.isFormat = debug5;
/*  66 */     this.id = debug6;
/*  67 */     this.color = debug7;
/*     */     
/*  69 */     this.toString = "§" + debug4;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getId() {
/*  77 */     return this.id;
/*     */   }
/*     */   
/*     */   public boolean isFormat() {
/*  81 */     return this.isFormat;
/*     */   }
/*     */   
/*     */   public boolean isColor() {
/*  85 */     return (!this.isFormat && this != RESET);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Integer getColor() {
/*  90 */     return this.color;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  94 */     return name().toLowerCase(Locale.ROOT);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  99 */     return this.toString;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static String stripFormatting(@Nullable String debug0) {
/* 104 */     return (debug0 == null) ? null : STRIP_FORMATTING_PATTERN.matcher(debug0).replaceAll("");
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static ChatFormatting getByName(@Nullable String debug0) {
/* 109 */     if (debug0 == null) {
/* 110 */       return null;
/*     */     }
/* 112 */     return FORMATTING_BY_NAME.get(cleanName(debug0));
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static ChatFormatting getById(int debug0) {
/* 117 */     if (debug0 < 0) {
/* 118 */       return RESET;
/*     */     }
/* 120 */     for (ChatFormatting debug4 : values()) {
/* 121 */       if (debug4.getId() == debug0) {
/* 122 */         return debug4;
/*     */       }
/*     */     } 
/* 125 */     return null;
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
/*     */   public static Collection<String> getNames(boolean debug0, boolean debug1) {
/* 140 */     List<String> debug2 = Lists.newArrayList();
/*     */     
/* 142 */     for (ChatFormatting debug6 : values()) {
/* 143 */       if (!debug6.isColor() || debug0)
/*     */       {
/*     */         
/* 146 */         if (!debug6.isFormat() || debug1)
/*     */         {
/*     */           
/* 149 */           debug2.add(debug6.getName()); } 
/*     */       }
/*     */     } 
/* 152 */     return debug2;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\ChatFormatting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */