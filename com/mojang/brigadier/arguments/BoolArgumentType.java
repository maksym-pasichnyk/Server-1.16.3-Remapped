/*    */ package com.mojang.brigadier.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BoolArgumentType
/*    */   implements ArgumentType<Boolean>
/*    */ {
/* 17 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "true", "false" });
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static BoolArgumentType bool() {
/* 23 */     return new BoolArgumentType();
/*    */   }
/*    */   
/*    */   public static boolean getBool(CommandContext<?> context, String name) {
/* 27 */     return ((Boolean)context.getArgument(name, Boolean.class)).booleanValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public Boolean parse(StringReader reader) throws CommandSyntaxException {
/* 32 */     return Boolean.valueOf(reader.readBoolean());
/*    */   }
/*    */ 
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
/* 37 */     if ("true".startsWith(builder.getRemaining().toLowerCase())) {
/* 38 */       builder.suggest("true");
/*    */     }
/* 40 */     if ("false".startsWith(builder.getRemaining().toLowerCase())) {
/* 41 */       builder.suggest("false");
/*    */     }
/* 43 */     return builder.buildFuture();
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 48 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\brigadier\arguments\BoolArgumentType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */