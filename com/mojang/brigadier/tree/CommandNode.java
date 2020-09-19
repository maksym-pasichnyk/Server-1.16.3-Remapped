/*     */ package com.mojang.brigadier.tree;
/*     */ 
/*     */ import com.mojang.brigadier.AmbiguityConsumer;
/*     */ import com.mojang.brigadier.Command;
/*     */ import com.mojang.brigadier.RedirectModifier;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.context.CommandContextBuilder;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ 
/*     */ 
/*     */ public abstract class CommandNode<S>
/*     */   implements Comparable<CommandNode<S>>
/*     */ {
/*  28 */   private Map<String, CommandNode<S>> children = new LinkedHashMap<>();
/*  29 */   private Map<String, LiteralCommandNode<S>> literals = new LinkedHashMap<>();
/*  30 */   private Map<String, ArgumentCommandNode<S, ?>> arguments = new LinkedHashMap<>();
/*     */   private final Predicate<S> requirement;
/*     */   private final CommandNode<S> redirect;
/*     */   private final RedirectModifier<S> modifier;
/*     */   private final boolean forks;
/*     */   private Command<S> command;
/*     */   
/*     */   protected CommandNode(Command<S> command, Predicate<S> requirement, CommandNode<S> redirect, RedirectModifier<S> modifier, boolean forks) {
/*  38 */     this.command = command;
/*  39 */     this.requirement = requirement;
/*  40 */     this.redirect = redirect;
/*  41 */     this.modifier = modifier;
/*  42 */     this.forks = forks;
/*     */   }
/*     */   
/*     */   public Command<S> getCommand() {
/*  46 */     return this.command;
/*     */   }
/*     */   
/*     */   public Collection<CommandNode<S>> getChildren() {
/*  50 */     return this.children.values();
/*     */   }
/*     */   
/*     */   public CommandNode<S> getChild(String name) {
/*  54 */     return this.children.get(name);
/*     */   }
/*     */   
/*     */   public CommandNode<S> getRedirect() {
/*  58 */     return this.redirect;
/*     */   }
/*     */   
/*     */   public RedirectModifier<S> getRedirectModifier() {
/*  62 */     return this.modifier;
/*     */   }
/*     */   
/*     */   public boolean canUse(S source) {
/*  66 */     return this.requirement.test(source);
/*     */   }
/*     */   
/*     */   public void addChild(CommandNode<S> node) {
/*  70 */     if (node instanceof RootCommandNode) {
/*  71 */       throw new UnsupportedOperationException("Cannot add a RootCommandNode as a child to any other CommandNode");
/*     */     }
/*     */     
/*  74 */     CommandNode<S> child = this.children.get(node.getName());
/*  75 */     if (child != null) {
/*     */       
/*  77 */       if (node.getCommand() != null) {
/*  78 */         child.command = node.getCommand();
/*     */       }
/*  80 */       for (CommandNode<S> grandchild : node.getChildren()) {
/*  81 */         child.addChild(grandchild);
/*     */       }
/*     */     } else {
/*  84 */       this.children.put(node.getName(), node);
/*  85 */       if (node instanceof LiteralCommandNode) {
/*  86 */         this.literals.put(node.getName(), (LiteralCommandNode<S>)node);
/*  87 */       } else if (node instanceof ArgumentCommandNode) {
/*  88 */         this.arguments.put(node.getName(), (ArgumentCommandNode)node);
/*     */       } 
/*     */     } 
/*     */     
/*  92 */     this.children = (Map<String, CommandNode<S>>)this.children.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
/*     */   }
/*     */   
/*     */   public void findAmbiguities(AmbiguityConsumer<S> consumer) {
/*  96 */     Set<String> matches = new HashSet<>();
/*     */     
/*  98 */     for (CommandNode<S> child : this.children.values()) {
/*  99 */       for (CommandNode<S> sibling : this.children.values()) {
/* 100 */         if (child == sibling) {
/*     */           continue;
/*     */         }
/*     */         
/* 104 */         for (String input : child.getExamples()) {
/* 105 */           if (sibling.isValidInput(input)) {
/* 106 */             matches.add(input);
/*     */           }
/*     */         } 
/*     */         
/* 110 */         if (matches.size() > 0) {
/* 111 */           consumer.ambiguous(this, child, sibling, matches);
/* 112 */           matches = new HashSet<>();
/*     */         } 
/*     */       } 
/*     */       
/* 116 */       child.findAmbiguities(consumer);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 124 */     if (this == o) return true; 
/* 125 */     if (!(o instanceof CommandNode)) return false;
/*     */     
/* 127 */     CommandNode<S> that = (CommandNode<S>)o;
/*     */     
/* 129 */     if (!this.children.equals(that.children)) return false; 
/* 130 */     if ((this.command != null) ? !this.command.equals(that.command) : (that.command != null)) return false;
/*     */     
/* 132 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 137 */     return 31 * this.children.hashCode() + ((this.command != null) ? this.command.hashCode() : 0);
/*     */   }
/*     */   
/*     */   public Predicate<S> getRequirement() {
/* 141 */     return this.requirement;
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
/*     */   public Collection<? extends CommandNode<S>> getRelevantNodes(StringReader input) {
/* 157 */     if (this.literals.size() > 0) {
/* 158 */       int cursor = input.getCursor();
/* 159 */       while (input.canRead() && input.peek() != ' ') {
/* 160 */         input.skip();
/*     */       }
/* 162 */       String text = input.getString().substring(cursor, input.getCursor());
/* 163 */       input.setCursor(cursor);
/* 164 */       LiteralCommandNode<S> literal = this.literals.get(text);
/* 165 */       if (literal != null) {
/* 166 */         return Collections.singleton(literal);
/*     */       }
/* 168 */       return this.arguments.values();
/*     */     } 
/*     */     
/* 171 */     return this.arguments.values();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(CommandNode<S> o) {
/* 177 */     if (this instanceof LiteralCommandNode == o instanceof LiteralCommandNode) {
/* 178 */       return getSortedKey().compareTo(o.getSortedKey());
/*     */     }
/*     */     
/* 181 */     return (o instanceof LiteralCommandNode) ? 1 : -1;
/*     */   }
/*     */   
/*     */   public boolean isFork() {
/* 185 */     return this.forks;
/*     */   }
/*     */   
/*     */   protected abstract boolean isValidInput(String paramString);
/*     */   
/*     */   public abstract String getName();
/*     */   
/*     */   public abstract String getUsageText();
/*     */   
/*     */   public abstract void parse(StringReader paramStringReader, CommandContextBuilder<S> paramCommandContextBuilder) throws CommandSyntaxException;
/*     */   
/*     */   public abstract CompletableFuture<Suggestions> listSuggestions(CommandContext<S> paramCommandContext, SuggestionsBuilder paramSuggestionsBuilder) throws CommandSyntaxException;
/*     */   
/*     */   public abstract ArgumentBuilder<S, ?> createBuilder();
/*     */   
/*     */   protected abstract String getSortedKey();
/*     */   
/*     */   public abstract Collection<String> getExamples();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\brigadier\tree\CommandNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */