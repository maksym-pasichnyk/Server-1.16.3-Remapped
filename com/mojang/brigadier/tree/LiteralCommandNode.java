/*     */ package com.mojang.brigadier.tree;
/*     */ 
/*     */ import com.mojang.brigadier.Command;
/*     */ import com.mojang.brigadier.RedirectModifier;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.context.CommandContextBuilder;
/*     */ import com.mojang.brigadier.context.StringRange;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Predicate;
/*     */ 
/*     */ 
/*     */ public class LiteralCommandNode<S>
/*     */   extends CommandNode<S>
/*     */ {
/*     */   private final String literal;
/*     */   
/*     */   public LiteralCommandNode(String literal, Command<S> command, Predicate<S> requirement, CommandNode<S> redirect, RedirectModifier<S> modifier, boolean forks) {
/*  26 */     super(command, requirement, redirect, modifier, forks);
/*  27 */     this.literal = literal;
/*     */   }
/*     */   
/*     */   public String getLiteral() {
/*  31 */     return this.literal;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  36 */     return this.literal;
/*     */   }
/*     */ 
/*     */   
/*     */   public void parse(StringReader reader, CommandContextBuilder<S> contextBuilder) throws CommandSyntaxException {
/*  41 */     int start = reader.getCursor();
/*  42 */     int end = parse(reader);
/*  43 */     if (end > -1) {
/*  44 */       contextBuilder.withNode(this, StringRange.between(start, end));
/*     */       
/*     */       return;
/*     */     } 
/*  48 */     throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.literalIncorrect().createWithContext(reader, this.literal);
/*     */   }
/*     */   
/*     */   private int parse(StringReader reader) {
/*  52 */     int start = reader.getCursor();
/*  53 */     if (reader.canRead(this.literal.length())) {
/*  54 */       int end = start + this.literal.length();
/*  55 */       if (reader.getString().substring(start, end).equals(this.literal)) {
/*  56 */         reader.setCursor(end);
/*  57 */         if (!reader.canRead() || reader.peek() == ' ') {
/*  58 */           return end;
/*     */         }
/*  60 */         reader.setCursor(start);
/*     */       } 
/*     */     } 
/*     */     
/*  64 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
/*  69 */     if (this.literal.toLowerCase().startsWith(builder.getRemaining().toLowerCase())) {
/*  70 */       return builder.suggest(this.literal).buildFuture();
/*     */     }
/*  72 */     return Suggestions.empty();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isValidInput(String input) {
/*  78 */     return (parse(new StringReader(input)) > -1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  83 */     if (this == o) return true; 
/*  84 */     if (!(o instanceof LiteralCommandNode)) return false;
/*     */     
/*  86 */     LiteralCommandNode that = (LiteralCommandNode)o;
/*     */     
/*  88 */     if (!this.literal.equals(that.literal)) return false; 
/*  89 */     return super.equals(o);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUsageText() {
/*  94 */     return this.literal;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  99 */     int result = this.literal.hashCode();
/* 100 */     result = 31 * result + super.hashCode();
/* 101 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public LiteralArgumentBuilder<S> createBuilder() {
/* 106 */     LiteralArgumentBuilder<S> builder = LiteralArgumentBuilder.literal(this.literal);
/* 107 */     builder.requires(getRequirement());
/* 108 */     builder.forward(getRedirect(), getRedirectModifier(), isFork());
/* 109 */     if (getCommand() != null) {
/* 110 */       builder.executes(getCommand());
/*     */     }
/* 112 */     return builder;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getSortedKey() {
/* 117 */     return this.literal;
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<String> getExamples() {
/* 122 */     return Collections.singleton(this.literal);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 127 */     return "<literal " + this.literal + ">";
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\brigadier\tree\LiteralCommandNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */