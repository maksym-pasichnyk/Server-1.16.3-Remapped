/*     */ package com.mojang.brigadier.context;
/*     */ 
/*     */ import com.mojang.brigadier.Command;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.RedirectModifier;
/*     */ import com.mojang.brigadier.tree.CommandNode;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CommandContextBuilder<S>
/*     */ {
/*  17 */   private final Map<String, ParsedArgument<S, ?>> arguments = new LinkedHashMap<>();
/*     */   private final CommandNode<S> rootNode;
/*  19 */   private final List<ParsedCommandNode<S>> nodes = new ArrayList<>();
/*     */   private final CommandDispatcher<S> dispatcher;
/*     */   private S source;
/*     */   private Command<S> command;
/*     */   private CommandContextBuilder<S> child;
/*     */   private StringRange range;
/*  25 */   private RedirectModifier<S> modifier = null;
/*     */   private boolean forks;
/*     */   
/*     */   public CommandContextBuilder(CommandDispatcher<S> dispatcher, S source, CommandNode<S> rootNode, int start) {
/*  29 */     this.rootNode = rootNode;
/*  30 */     this.dispatcher = dispatcher;
/*  31 */     this.source = source;
/*  32 */     this.range = StringRange.at(start);
/*     */   }
/*     */   
/*     */   public CommandContextBuilder<S> withSource(S source) {
/*  36 */     this.source = source;
/*  37 */     return this;
/*     */   }
/*     */   
/*     */   public S getSource() {
/*  41 */     return this.source;
/*     */   }
/*     */   
/*     */   public CommandNode<S> getRootNode() {
/*  45 */     return this.rootNode;
/*     */   }
/*     */   
/*     */   public CommandContextBuilder<S> withArgument(String name, ParsedArgument<S, ?> argument) {
/*  49 */     this.arguments.put(name, argument);
/*  50 */     return this;
/*     */   }
/*     */   
/*     */   public Map<String, ParsedArgument<S, ?>> getArguments() {
/*  54 */     return this.arguments;
/*     */   }
/*     */   
/*     */   public CommandContextBuilder<S> withCommand(Command<S> command) {
/*  58 */     this.command = command;
/*  59 */     return this;
/*     */   }
/*     */   
/*     */   public CommandContextBuilder<S> withNode(CommandNode<S> node, StringRange range) {
/*  63 */     this.nodes.add(new ParsedCommandNode<>(node, range));
/*  64 */     this.range = StringRange.encompassing(this.range, range);
/*  65 */     this.modifier = node.getRedirectModifier();
/*  66 */     this.forks = node.isFork();
/*  67 */     return this;
/*     */   }
/*     */   
/*     */   public CommandContextBuilder<S> copy() {
/*  71 */     CommandContextBuilder<S> copy = new CommandContextBuilder(this.dispatcher, this.source, this.rootNode, this.range.getStart());
/*  72 */     copy.command = this.command;
/*  73 */     copy.arguments.putAll(this.arguments);
/*  74 */     copy.nodes.addAll(this.nodes);
/*  75 */     copy.child = this.child;
/*  76 */     copy.range = this.range;
/*  77 */     copy.forks = this.forks;
/*  78 */     return copy;
/*     */   }
/*     */   
/*     */   public CommandContextBuilder<S> withChild(CommandContextBuilder<S> child) {
/*  82 */     this.child = child;
/*  83 */     return this;
/*     */   }
/*     */   
/*     */   public CommandContextBuilder<S> getChild() {
/*  87 */     return this.child;
/*     */   }
/*     */   
/*     */   public CommandContextBuilder<S> getLastChild() {
/*  91 */     CommandContextBuilder<S> result = this;
/*  92 */     while (result.getChild() != null) {
/*  93 */       result = result.getChild();
/*     */     }
/*  95 */     return result;
/*     */   }
/*     */   
/*     */   public Command<S> getCommand() {
/*  99 */     return this.command;
/*     */   }
/*     */   
/*     */   public List<ParsedCommandNode<S>> getNodes() {
/* 103 */     return this.nodes;
/*     */   }
/*     */   
/*     */   public CommandContext<S> build(String input) {
/* 107 */     return new CommandContext<>(this.source, input, this.arguments, this.command, this.rootNode, this.nodes, this.range, (this.child == null) ? null : this.child.build(input), this.modifier, this.forks);
/*     */   }
/*     */   
/*     */   public CommandDispatcher<S> getDispatcher() {
/* 111 */     return this.dispatcher;
/*     */   }
/*     */   
/*     */   public StringRange getRange() {
/* 115 */     return this.range;
/*     */   }
/*     */   
/*     */   public SuggestionContext<S> findSuggestionContext(int cursor) {
/* 119 */     if (this.range.getStart() <= cursor) {
/* 120 */       if (this.range.getEnd() < cursor) {
/* 121 */         if (this.child != null)
/* 122 */           return this.child.findSuggestionContext(cursor); 
/* 123 */         if (!this.nodes.isEmpty()) {
/* 124 */           ParsedCommandNode<S> last = this.nodes.get(this.nodes.size() - 1);
/* 125 */           return new SuggestionContext<>(last.getNode(), last.getRange().getEnd() + 1);
/*     */         } 
/* 127 */         return new SuggestionContext<>(this.rootNode, this.range.getStart());
/*     */       } 
/*     */       
/* 130 */       CommandNode<S> prev = this.rootNode;
/* 131 */       for (ParsedCommandNode<S> node : this.nodes) {
/* 132 */         StringRange nodeRange = node.getRange();
/* 133 */         if (nodeRange.getStart() <= cursor && cursor <= nodeRange.getEnd()) {
/* 134 */           return new SuggestionContext<>(prev, nodeRange.getStart());
/*     */         }
/* 136 */         prev = node.getNode();
/*     */       } 
/* 138 */       if (prev == null) {
/* 139 */         throw new IllegalStateException("Can't find node before cursor");
/*     */       }
/* 141 */       return new SuggestionContext<>(prev, this.range.getStart());
/*     */     } 
/*     */     
/* 144 */     throw new IllegalStateException("Can't find node before cursor");
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\brigadier\context\CommandContextBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */