/*     */ package com.mojang.brigadier.tree;
/*     */ 
/*     */ import com.mojang.brigadier.Command;
/*     */ import com.mojang.brigadier.RedirectModifier;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.context.CommandContextBuilder;
/*     */ import com.mojang.brigadier.context.ParsedArgument;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.suggestion.SuggestionProvider;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Predicate;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ArgumentCommandNode<S, T>
/*     */   extends CommandNode<S>
/*     */ {
/*     */   private static final String USAGE_ARGUMENT_OPEN = "<";
/*     */   private static final String USAGE_ARGUMENT_CLOSE = ">";
/*     */   private final String name;
/*     */   private final ArgumentType<T> type;
/*     */   private final SuggestionProvider<S> customSuggestions;
/*     */   
/*     */   public ArgumentCommandNode(String name, ArgumentType<T> type, Command<S> command, Predicate<S> requirement, CommandNode<S> redirect, RedirectModifier<S> modifier, boolean forks, SuggestionProvider<S> customSuggestions) {
/*  32 */     super(command, requirement, redirect, modifier, forks);
/*  33 */     this.name = name;
/*  34 */     this.type = type;
/*  35 */     this.customSuggestions = customSuggestions;
/*     */   }
/*     */   
/*     */   public ArgumentType<T> getType() {
/*  39 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  44 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUsageText() {
/*  49 */     return "<" + this.name + ">";
/*     */   }
/*     */   
/*     */   public SuggestionProvider<S> getCustomSuggestions() {
/*  53 */     return this.customSuggestions;
/*     */   }
/*     */ 
/*     */   
/*     */   public void parse(StringReader reader, CommandContextBuilder<S> contextBuilder) throws CommandSyntaxException {
/*  58 */     int start = reader.getCursor();
/*  59 */     T result = (T)this.type.parse(reader);
/*  60 */     ParsedArgument<S, T> parsed = new ParsedArgument(start, reader.getCursor(), result);
/*     */     
/*  62 */     contextBuilder.withArgument(this.name, parsed);
/*  63 */     contextBuilder.withNode(this, parsed.getRange());
/*     */   }
/*     */ 
/*     */   
/*     */   public CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) throws CommandSyntaxException {
/*  68 */     if (this.customSuggestions == null) {
/*  69 */       return this.type.listSuggestions(context, builder);
/*     */     }
/*  71 */     return this.customSuggestions.getSuggestions(context, builder);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public RequiredArgumentBuilder<S, T> createBuilder() {
/*  77 */     RequiredArgumentBuilder<S, T> builder = RequiredArgumentBuilder.argument(this.name, this.type);
/*  78 */     builder.requires(getRequirement());
/*  79 */     builder.forward(getRedirect(), getRedirectModifier(), isFork());
/*  80 */     builder.suggests(this.customSuggestions);
/*  81 */     if (getCommand() != null) {
/*  82 */       builder.executes(getCommand());
/*     */     }
/*  84 */     return builder;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidInput(String input) {
/*     */     try {
/*  90 */       StringReader reader = new StringReader(input);
/*  91 */       this.type.parse(reader);
/*  92 */       return (!reader.canRead() || reader.peek() == ' ');
/*  93 */     } catch (CommandSyntaxException ignored) {
/*  94 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 100 */     if (this == o) return true; 
/* 101 */     if (!(o instanceof ArgumentCommandNode)) return false;
/*     */     
/* 103 */     ArgumentCommandNode that = (ArgumentCommandNode)o;
/*     */     
/* 105 */     if (!this.name.equals(that.name)) return false; 
/* 106 */     if (!this.type.equals(that.type)) return false; 
/* 107 */     return super.equals(o);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 112 */     int result = this.name.hashCode();
/* 113 */     result = 31 * result + this.type.hashCode();
/* 114 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getSortedKey() {
/* 119 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<String> getExamples() {
/* 124 */     return this.type.getExamples();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 129 */     return "<argument " + this.name + ":" + this.type + ">";
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\brigadier\tree\ArgumentCommandNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */