/*     */ package net.minecraft.commands;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.ParseResults;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.ServerFunctionManager;
/*     */ 
/*     */ public class CommandFunction
/*     */ {
/*     */   private final Entry[] entries;
/*     */   private final ResourceLocation id;
/*     */   
/*     */   public CommandFunction(ResourceLocation debug1, Entry[] debug2) {
/*  21 */     this.id = debug1;
/*  22 */     this.entries = debug2;
/*     */   }
/*     */   
/*     */   public ResourceLocation getId() {
/*  26 */     return this.id;
/*     */   }
/*     */   
/*     */   public Entry[] getEntries() {
/*  30 */     return this.entries;
/*     */   }
/*     */   
/*     */   public static CommandFunction fromLines(ResourceLocation debug0, CommandDispatcher<CommandSourceStack> debug1, CommandSourceStack debug2, List<String> debug3) {
/*  34 */     List<Entry> debug4 = Lists.newArrayListWithCapacity(debug3.size());
/*  35 */     for (int debug5 = 0; debug5 < debug3.size(); debug5++) {
/*  36 */       int debug6 = debug5 + 1;
/*  37 */       String debug7 = ((String)debug3.get(debug5)).trim();
/*  38 */       StringReader debug8 = new StringReader(debug7);
/*     */       
/*  40 */       if (debug8.canRead() && debug8.peek() != '#') {
/*     */ 
/*     */ 
/*     */         
/*  44 */         if (debug8.peek() == '/') {
/*  45 */           debug8.skip();
/*  46 */           if (debug8.peek() == '/') {
/*  47 */             throw new IllegalArgumentException("Unknown or invalid command '" + debug7 + "' on line " + debug6 + " (if you intended to make a comment, use '#' not '//')");
/*     */           }
/*  49 */           String debug9 = debug8.readUnquotedString();
/*  50 */           throw new IllegalArgumentException("Unknown or invalid command '" + debug7 + "' on line " + debug6 + " (did you mean '" + debug9 + "'? Do not use a preceding forwards slash.)");
/*     */         } 
/*     */ 
/*     */         
/*     */         try {
/*  55 */           ParseResults<CommandSourceStack> debug9 = debug1.parse(debug8, debug2);
/*  56 */           if (debug9.getReader().canRead()) {
/*  57 */             throw Commands.getParseException(debug9);
/*     */           }
/*  59 */           debug4.add(new CommandEntry(debug9));
/*     */         }
/*  61 */         catch (CommandSyntaxException debug9) {
/*  62 */           throw new IllegalArgumentException("Whilst parsing command on line " + debug6 + ": " + debug9.getMessage());
/*     */         } 
/*     */       } 
/*     */     } 
/*  66 */     return new CommandFunction(debug0, debug4.<Entry>toArray(new Entry[0]));
/*     */   }
/*     */   
/*     */   public static interface Entry {
/*     */     void execute(ServerFunctionManager param1ServerFunctionManager, CommandSourceStack param1CommandSourceStack, ArrayDeque<ServerFunctionManager.QueuedCommand> param1ArrayDeque, int param1Int) throws CommandSyntaxException;
/*     */   }
/*     */   
/*     */   public static class CommandEntry implements Entry {
/*     */     private final ParseResults<CommandSourceStack> parse;
/*     */     
/*     */     public CommandEntry(ParseResults<CommandSourceStack> debug1) {
/*  77 */       this.parse = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public void execute(ServerFunctionManager debug1, CommandSourceStack debug2, ArrayDeque<ServerFunctionManager.QueuedCommand> debug3, int debug4) throws CommandSyntaxException {
/*  82 */       debug1.getDispatcher().execute(new ParseResults(this.parse.getContext().withSource(debug2), this.parse.getReader(), this.parse.getExceptions()));
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  87 */       return this.parse.getReader().getString();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class FunctionEntry implements Entry {
/*     */     private final CommandFunction.CacheableFunction function;
/*     */     
/*     */     public FunctionEntry(CommandFunction debug1) {
/*  95 */       this.function = new CommandFunction.CacheableFunction(debug1);
/*     */     }
/*     */ 
/*     */     
/*     */     public void execute(ServerFunctionManager debug1, CommandSourceStack debug2, ArrayDeque<ServerFunctionManager.QueuedCommand> debug3, int debug4) {
/* 100 */       this.function.get(debug1).ifPresent(debug4 -> {
/*     */             CommandFunction.Entry[] debug5 = debug4.getEntries();
/*     */             int debug6 = debug0 - debug1.size();
/*     */             int debug7 = Math.min(debug5.length, debug6);
/*     */             for (int debug8 = debug7 - 1; debug8 >= 0; debug8--) {
/*     */               debug1.addFirst(new ServerFunctionManager.QueuedCommand(debug2, debug3, debug5[debug8]));
/*     */             }
/*     */           });
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 112 */       return "function " + this.function.getId();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class CacheableFunction {
/* 117 */     public static final CacheableFunction NONE = new CacheableFunction((ResourceLocation)null);
/*     */     
/*     */     @Nullable
/*     */     private final ResourceLocation id;
/*     */     private boolean resolved;
/* 122 */     private Optional<CommandFunction> function = Optional.empty();
/*     */     
/*     */     public CacheableFunction(@Nullable ResourceLocation debug1) {
/* 125 */       this.id = debug1;
/*     */     }
/*     */     
/*     */     public CacheableFunction(CommandFunction debug1) {
/* 129 */       this.resolved = true;
/* 130 */       this.id = null;
/* 131 */       this.function = Optional.of(debug1);
/*     */     }
/*     */     
/*     */     public Optional<CommandFunction> get(ServerFunctionManager debug1) {
/* 135 */       if (!this.resolved) {
/* 136 */         if (this.id != null) {
/* 137 */           this.function = debug1.get(this.id);
/*     */         }
/* 139 */         this.resolved = true;
/*     */       } 
/* 141 */       return this.function;
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     public ResourceLocation getId() {
/* 146 */       return this.function.<ResourceLocation>map(debug0 -> debug0.id).orElse(this.id);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\CommandFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */