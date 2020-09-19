/*    */ package com.mojang.brigadier.builder;
/*    */ 
/*    */ import com.mojang.brigadier.Command;
/*    */ import com.mojang.brigadier.RedirectModifier;
/*    */ import com.mojang.brigadier.SingleRedirectModifier;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.tree.CommandNode;
/*    */ import com.mojang.brigadier.tree.RootCommandNode;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.function.Predicate;
/*    */ 
/*    */ 
/*    */ public abstract class ArgumentBuilder<S, T extends ArgumentBuilder<S, T>>
/*    */ {
/* 17 */   private final RootCommandNode<S> arguments = new RootCommandNode();
/*    */   private Command<S> command;
/*    */   private Predicate<S> requirement = s -> true;
/*    */   private CommandNode<S> target;
/* 21 */   private RedirectModifier<S> modifier = null;
/*    */   
/*    */   private boolean forks;
/*    */ 
/*    */   
/*    */   public T then(ArgumentBuilder<S, ?> argument) {
/* 27 */     if (this.target != null) {
/* 28 */       throw new IllegalStateException("Cannot add children to a redirected node");
/*    */     }
/* 30 */     this.arguments.addChild(argument.build());
/* 31 */     return getThis();
/*    */   }
/*    */   
/*    */   public T then(CommandNode<S> argument) {
/* 35 */     if (this.target != null) {
/* 36 */       throw new IllegalStateException("Cannot add children to a redirected node");
/*    */     }
/* 38 */     this.arguments.addChild(argument);
/* 39 */     return getThis();
/*    */   }
/*    */   
/*    */   public Collection<CommandNode<S>> getArguments() {
/* 43 */     return this.arguments.getChildren();
/*    */   }
/*    */   
/*    */   public T executes(Command<S> command) {
/* 47 */     this.command = command;
/* 48 */     return getThis();
/*    */   }
/*    */   
/*    */   public Command<S> getCommand() {
/* 52 */     return this.command;
/*    */   }
/*    */   
/*    */   public T requires(Predicate<S> requirement) {
/* 56 */     this.requirement = requirement;
/* 57 */     return getThis();
/*    */   }
/*    */   
/*    */   public Predicate<S> getRequirement() {
/* 61 */     return this.requirement;
/*    */   }
/*    */   
/*    */   public T redirect(CommandNode<S> target) {
/* 65 */     return forward(target, null, false);
/*    */   }
/*    */   
/*    */   public T redirect(CommandNode<S> target, SingleRedirectModifier<S> modifier) {
/* 69 */     return forward(target, (modifier == null) ? null : (o -> Collections.singleton(modifier.apply(o))), false);
/*    */   }
/*    */   
/*    */   public T fork(CommandNode<S> target, RedirectModifier<S> modifier) {
/* 73 */     return forward(target, modifier, true);
/*    */   }
/*    */   
/*    */   public T forward(CommandNode<S> target, RedirectModifier<S> modifier, boolean fork) {
/* 77 */     if (!this.arguments.getChildren().isEmpty()) {
/* 78 */       throw new IllegalStateException("Cannot forward a node with children");
/*    */     }
/* 80 */     this.target = target;
/* 81 */     this.modifier = modifier;
/* 82 */     this.forks = fork;
/* 83 */     return getThis();
/*    */   }
/*    */   
/*    */   public CommandNode<S> getRedirect() {
/* 87 */     return this.target;
/*    */   }
/*    */   
/*    */   public RedirectModifier<S> getRedirectModifier() {
/* 91 */     return this.modifier;
/*    */   }
/*    */   
/*    */   public boolean isFork() {
/* 95 */     return this.forks;
/*    */   }
/*    */   
/*    */   protected abstract T getThis();
/*    */   
/*    */   public abstract CommandNode<S> build();
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\brigadier\builder\ArgumentBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */