/*    */ package com.mojang.brigadier;
/*    */ 
/*    */ import com.mojang.brigadier.context.CommandContextBuilder;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.tree.CommandNode;
/*    */ import java.util.Collections;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ParseResults<S>
/*    */ {
/*    */   private final CommandContextBuilder<S> context;
/*    */   private final Map<CommandNode<S>, CommandSyntaxException> exceptions;
/*    */   private final ImmutableStringReader reader;
/*    */   
/*    */   public ParseResults(CommandContextBuilder<S> context, ImmutableStringReader reader, Map<CommandNode<S>, CommandSyntaxException> exceptions) {
/* 19 */     this.context = context;
/* 20 */     this.reader = reader;
/* 21 */     this.exceptions = exceptions;
/*    */   }
/*    */   
/*    */   public ParseResults(CommandContextBuilder<S> context) {
/* 25 */     this(context, new StringReader(""), Collections.emptyMap());
/*    */   }
/*    */   
/*    */   public CommandContextBuilder<S> getContext() {
/* 29 */     return this.context;
/*    */   }
/*    */   
/*    */   public ImmutableStringReader getReader() {
/* 33 */     return this.reader;
/*    */   }
/*    */   
/*    */   public Map<CommandNode<S>, CommandSyntaxException> getExceptions() {
/* 37 */     return this.exceptions;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\brigadier\ParseResults.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */