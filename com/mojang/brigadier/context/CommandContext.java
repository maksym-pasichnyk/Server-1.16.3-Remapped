/*     */ package com.mojang.brigadier.context;
/*     */ 
/*     */ import com.mojang.brigadier.Command;
/*     */ import com.mojang.brigadier.RedirectModifier;
/*     */ import com.mojang.brigadier.tree.CommandNode;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CommandContext<S>
/*     */ {
/*  16 */   private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER = new HashMap<>();
/*     */   
/*     */   static {
/*  19 */     PRIMITIVE_TO_WRAPPER.put(boolean.class, Boolean.class);
/*  20 */     PRIMITIVE_TO_WRAPPER.put(byte.class, Byte.class);
/*  21 */     PRIMITIVE_TO_WRAPPER.put(short.class, Short.class);
/*  22 */     PRIMITIVE_TO_WRAPPER.put(char.class, Character.class);
/*  23 */     PRIMITIVE_TO_WRAPPER.put(int.class, Integer.class);
/*  24 */     PRIMITIVE_TO_WRAPPER.put(long.class, Long.class);
/*  25 */     PRIMITIVE_TO_WRAPPER.put(float.class, Float.class);
/*  26 */     PRIMITIVE_TO_WRAPPER.put(double.class, Double.class);
/*     */   }
/*     */   
/*     */   private final S source;
/*     */   private final String input;
/*     */   private final Command<S> command;
/*     */   private final Map<String, ParsedArgument<S, ?>> arguments;
/*     */   private final CommandNode<S> rootNode;
/*     */   private final List<ParsedCommandNode<S>> nodes;
/*     */   private final StringRange range;
/*     */   private final CommandContext<S> child;
/*     */   private final RedirectModifier<S> modifier;
/*     */   private final boolean forks;
/*     */   
/*     */   public CommandContext(S source, String input, Map<String, ParsedArgument<S, ?>> arguments, Command<S> command, CommandNode<S> rootNode, List<ParsedCommandNode<S>> nodes, StringRange range, CommandContext<S> child, RedirectModifier<S> modifier, boolean forks) {
/*  41 */     this.source = source;
/*  42 */     this.input = input;
/*  43 */     this.arguments = arguments;
/*  44 */     this.command = command;
/*  45 */     this.rootNode = rootNode;
/*  46 */     this.nodes = nodes;
/*  47 */     this.range = range;
/*  48 */     this.child = child;
/*  49 */     this.modifier = modifier;
/*  50 */     this.forks = forks;
/*     */   }
/*     */   
/*     */   public CommandContext<S> copyFor(S source) {
/*  54 */     if (this.source == source) {
/*  55 */       return this;
/*     */     }
/*  57 */     return new CommandContext(source, this.input, this.arguments, this.command, this.rootNode, this.nodes, this.range, this.child, this.modifier, this.forks);
/*     */   }
/*     */   
/*     */   public CommandContext<S> getChild() {
/*  61 */     return this.child;
/*     */   }
/*     */   
/*     */   public CommandContext<S> getLastChild() {
/*  65 */     CommandContext<S> result = this;
/*  66 */     while (result.getChild() != null) {
/*  67 */       result = result.getChild();
/*     */     }
/*  69 */     return result;
/*     */   }
/*     */   
/*     */   public Command<S> getCommand() {
/*  73 */     return this.command;
/*     */   }
/*     */   
/*     */   public S getSource() {
/*  77 */     return this.source;
/*     */   }
/*     */ 
/*     */   
/*     */   public <V> V getArgument(String name, Class<V> clazz) {
/*  82 */     ParsedArgument<S, ?> argument = this.arguments.get(name);
/*     */     
/*  84 */     if (argument == null) {
/*  85 */       throw new IllegalArgumentException("No such argument '" + name + "' exists on this command");
/*     */     }
/*     */     
/*  88 */     Object result = argument.getResult();
/*  89 */     if (((Class)PRIMITIVE_TO_WRAPPER.getOrDefault(clazz, clazz)).isAssignableFrom(result.getClass())) {
/*  90 */       return (V)result;
/*     */     }
/*  92 */     throw new IllegalArgumentException("Argument '" + name + "' is defined as " + result.getClass().getSimpleName() + ", not " + clazz);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  98 */     if (this == o) return true; 
/*  99 */     if (!(o instanceof CommandContext)) return false;
/*     */     
/* 101 */     CommandContext that = (CommandContext)o;
/*     */     
/* 103 */     if (!this.arguments.equals(that.arguments)) return false; 
/* 104 */     if (!this.rootNode.equals(that.rootNode)) return false; 
/* 105 */     if (this.nodes.size() != that.nodes.size() || !this.nodes.equals(that.nodes)) return false; 
/* 106 */     if ((this.command != null) ? !this.command.equals(that.command) : (that.command != null)) return false; 
/* 107 */     if (!this.source.equals(that.source)) return false; 
/* 108 */     if ((this.child != null) ? !this.child.equals(that.child) : (that.child != null)) return false;
/*     */     
/* 110 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 115 */     int result = this.source.hashCode();
/* 116 */     result = 31 * result + this.arguments.hashCode();
/* 117 */     result = 31 * result + ((this.command != null) ? this.command.hashCode() : 0);
/* 118 */     result = 31 * result + this.rootNode.hashCode();
/* 119 */     result = 31 * result + this.nodes.hashCode();
/* 120 */     result = 31 * result + ((this.child != null) ? this.child.hashCode() : 0);
/* 121 */     return result;
/*     */   }
/*     */   
/*     */   public RedirectModifier<S> getRedirectModifier() {
/* 125 */     return this.modifier;
/*     */   }
/*     */   
/*     */   public StringRange getRange() {
/* 129 */     return this.range;
/*     */   }
/*     */   
/*     */   public String getInput() {
/* 133 */     return this.input;
/*     */   }
/*     */   
/*     */   public CommandNode<S> getRootNode() {
/* 137 */     return this.rootNode;
/*     */   }
/*     */   
/*     */   public List<ParsedCommandNode<S>> getNodes() {
/* 141 */     return this.nodes;
/*     */   }
/*     */   
/*     */   public boolean hasNodes() {
/* 145 */     return !this.nodes.isEmpty();
/*     */   }
/*     */   
/*     */   public boolean isForked() {
/* 149 */     return this.forks;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\brigadier\context\CommandContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */