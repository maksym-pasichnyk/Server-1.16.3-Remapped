/*     */ package com.mojang.brigadier;
/*     */ 
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.context.CommandContextBuilder;
/*     */ import com.mojang.brigadier.context.SuggestionContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import com.mojang.brigadier.tree.CommandNode;
/*     */ import com.mojang.brigadier.tree.LiteralCommandNode;
/*     */ import com.mojang.brigadier.tree.RootCommandNode;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Collectors;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CommandDispatcher<S>
/*     */ {
/*     */   public static final String ARGUMENT_SEPARATOR = " ";
/*     */   public static final char ARGUMENT_SEPARATOR_CHAR = ' ';
/*     */   private static final String USAGE_OPTIONAL_OPEN = "[";
/*     */   private static final String USAGE_OPTIONAL_CLOSE = "]";
/*     */   private static final String USAGE_REQUIRED_OPEN = "(";
/*     */   private static final String USAGE_REQUIRED_CLOSE = ")";
/*     */   private static final String USAGE_OR = "|";
/*     */   private final RootCommandNode<S> root;
/*     */   
/*  58 */   private final Predicate<CommandNode<S>> hasCommand = new Predicate<CommandNode<S>>()
/*     */     {
/*     */       public boolean test(CommandNode<S> input) {
/*  61 */         return (input != null && (input.getCommand() != null || input.getChildren().stream().anyMatch(CommandDispatcher.this.hasCommand)));
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ResultConsumer<S> consumer = (c, s, r) -> {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*     */   public CommandDispatcher(RootCommandNode<S> root) {
/*  75 */     this.root = root;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CommandDispatcher() {
/*  82 */     this(new RootCommandNode());
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
/*     */   public LiteralCommandNode<S> register(LiteralArgumentBuilder<S> command) {
/*  96 */     LiteralCommandNode<S> build = command.build();
/*  97 */     this.root.addChild((CommandNode)build);
/*  98 */     return build;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConsumer(ResultConsumer<S> consumer) {
/* 107 */     this.consumer = consumer;
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
/*     */   public int execute(String input, S source) throws CommandSyntaxException {
/* 141 */     return execute(new StringReader(input), source);
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
/*     */   public int execute(StringReader input, S source) throws CommandSyntaxException {
/* 175 */     ParseResults<S> parse = parse(input, source);
/* 176 */     return execute(parse);
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
/*     */   public int execute(ParseResults<S> parse) throws CommandSyntaxException {
/* 206 */     if (parse.getReader().canRead()) {
/* 207 */       if (parse.getExceptions().size() == 1)
/* 208 */         throw (CommandSyntaxException)parse.getExceptions().values().iterator().next(); 
/* 209 */       if (parse.getContext().getRange().isEmpty()) {
/* 210 */         throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(parse.getReader());
/*     */       }
/* 212 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(parse.getReader());
/*     */     } 
/*     */ 
/*     */     
/* 216 */     int result = 0;
/* 217 */     int successfulForks = 0;
/* 218 */     boolean forked = false;
/* 219 */     boolean foundCommand = false;
/* 220 */     String command = parse.getReader().getString();
/* 221 */     CommandContext<S> original = parse.getContext().build(command);
/* 222 */     List<CommandContext<S>> contexts = Collections.singletonList(original);
/* 223 */     ArrayList<CommandContext<S>> next = null;
/*     */     
/* 225 */     while (contexts != null) {
/* 226 */       int size = contexts.size();
/* 227 */       for (int i = 0; i < size; i++) {
/* 228 */         CommandContext<S> context = contexts.get(i);
/* 229 */         CommandContext<S> child = context.getChild();
/* 230 */         if (child != null) {
/* 231 */           forked |= context.isForked();
/* 232 */           if (child.hasNodes()) {
/* 233 */             foundCommand = true;
/* 234 */             RedirectModifier<S> modifier = context.getRedirectModifier();
/* 235 */             if (modifier == null) {
/* 236 */               if (next == null) {
/* 237 */                 next = new ArrayList<>(1);
/*     */               }
/* 239 */               next.add(child.copyFor(context.getSource()));
/*     */             } else {
/*     */               try {
/* 242 */                 Collection<S> results = modifier.apply(context);
/* 243 */                 if (!results.isEmpty()) {
/* 244 */                   if (next == null) {
/* 245 */                     next = new ArrayList<>(results.size());
/*     */                   }
/* 247 */                   for (S source : results) {
/* 248 */                     next.add(child.copyFor(source));
/*     */                   }
/*     */                 } 
/* 251 */               } catch (CommandSyntaxException ex) {
/* 252 */                 this.consumer.onCommandComplete(context, false, 0);
/* 253 */                 if (!forked) {
/* 254 */                   throw ex;
/*     */                 }
/*     */               } 
/*     */             } 
/*     */           } 
/* 259 */         } else if (context.getCommand() != null) {
/* 260 */           foundCommand = true;
/*     */           try {
/* 262 */             int value = context.getCommand().run(context);
/* 263 */             result += value;
/* 264 */             this.consumer.onCommandComplete(context, true, value);
/* 265 */             successfulForks++;
/* 266 */           } catch (CommandSyntaxException ex) {
/* 267 */             this.consumer.onCommandComplete(context, false, 0);
/* 268 */             if (!forked) {
/* 269 */               throw ex;
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 275 */       contexts = next;
/* 276 */       next = null;
/*     */     } 
/*     */     
/* 279 */     if (!foundCommand) {
/* 280 */       this.consumer.onCommandComplete(original, false, 0);
/* 281 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(parse.getReader());
/*     */     } 
/*     */     
/* 284 */     return forked ? successfulForks : result;
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
/*     */   public ParseResults<S> parse(String command, S source) {
/* 315 */     return parse(new StringReader(command), source);
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
/*     */   public ParseResults<S> parse(StringReader command, S source) {
/* 346 */     CommandContextBuilder<S> context = new CommandContextBuilder(this, source, (CommandNode)this.root, command.getCursor());
/* 347 */     return parseNodes((CommandNode<S>)this.root, command, context);
/*     */   }
/*     */   
/*     */   private ParseResults<S> parseNodes(CommandNode<S> node, StringReader originalReader, CommandContextBuilder<S> contextSoFar) {
/* 351 */     S source = (S)contextSoFar.getSource();
/* 352 */     Map<CommandNode<S>, CommandSyntaxException> errors = null;
/* 353 */     List<ParseResults<S>> potentials = null;
/* 354 */     int cursor = originalReader.getCursor();
/*     */     
/* 356 */     for (CommandNode<S> child : (Iterable<CommandNode<S>>)node.getRelevantNodes(originalReader)) {
/* 357 */       if (!child.canUse(source)) {
/*     */         continue;
/*     */       }
/* 360 */       CommandContextBuilder<S> context = contextSoFar.copy();
/* 361 */       StringReader reader = new StringReader(originalReader);
/*     */       try {
/*     */         try {
/* 364 */           child.parse(reader, context);
/* 365 */         } catch (RuntimeException ex) {
/* 366 */           throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(reader, ex.getMessage());
/*     */         } 
/* 368 */         if (reader.canRead() && 
/* 369 */           reader.peek() != ' ') {
/* 370 */           throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherExpectedArgumentSeparator().createWithContext(reader);
/*     */         }
/*     */       }
/* 373 */       catch (CommandSyntaxException ex) {
/* 374 */         if (errors == null) {
/* 375 */           errors = new LinkedHashMap<>();
/*     */         }
/* 377 */         errors.put(child, ex);
/* 378 */         reader.setCursor(cursor);
/*     */         
/*     */         continue;
/*     */       } 
/* 382 */       context.withCommand(child.getCommand());
/* 383 */       if (reader.canRead((child.getRedirect() == null) ? 2 : 1)) {
/* 384 */         reader.skip();
/* 385 */         if (child.getRedirect() != null) {
/* 386 */           CommandContextBuilder<S> childContext = new CommandContextBuilder(this, source, child.getRedirect(), reader.getCursor());
/* 387 */           ParseResults<S> parseResults = parseNodes(child.getRedirect(), reader, childContext);
/* 388 */           context.withChild(parseResults.getContext());
/* 389 */           return new ParseResults<>(context, parseResults.getReader(), parseResults.getExceptions());
/*     */         } 
/* 391 */         ParseResults<S> parse = parseNodes(child, reader, context);
/* 392 */         if (potentials == null) {
/* 393 */           potentials = new ArrayList<>(1);
/*     */         }
/* 395 */         potentials.add(parse);
/*     */         continue;
/*     */       } 
/* 398 */       if (potentials == null) {
/* 399 */         potentials = new ArrayList<>(1);
/*     */       }
/* 401 */       potentials.add(new ParseResults<>(context, reader, Collections.emptyMap()));
/*     */     } 
/*     */ 
/*     */     
/* 405 */     if (potentials != null) {
/* 406 */       if (potentials.size() > 1) {
/* 407 */         potentials.sort((a, b) -> 
/* 408 */             (!a.getReader().canRead() && b.getReader().canRead()) ? -1 : (
/*     */ 
/*     */             
/* 411 */             (a.getReader().canRead() && !b.getReader().canRead()) ? 1 : (
/*     */ 
/*     */             
/* 414 */             (a.getExceptions().isEmpty() && !b.getExceptions().isEmpty()) ? -1 : (
/*     */ 
/*     */             
/* 417 */             (!a.getExceptions().isEmpty() && b.getExceptions().isEmpty()) ? 1 : 0))));
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 423 */       return potentials.get(0);
/*     */     } 
/*     */     
/* 426 */     return new ParseResults<>(contextSoFar, originalReader, (errors == null) ? Collections.<CommandNode<S>, CommandSyntaxException>emptyMap() : errors);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getAllUsage(CommandNode<S> node, S source, boolean restricted) {
/* 451 */     ArrayList<String> result = new ArrayList<>();
/* 452 */     getAllUsage(node, source, result, "", restricted);
/* 453 */     return result.<String>toArray(new String[result.size()]);
/*     */   }
/*     */   
/*     */   private void getAllUsage(CommandNode<S> node, S source, ArrayList<String> result, String prefix, boolean restricted) {
/* 457 */     if (restricted && !node.canUse(source)) {
/*     */       return;
/*     */     }
/*     */     
/* 461 */     if (node.getCommand() != null) {
/* 462 */       result.add(prefix);
/*     */     }
/*     */     
/* 465 */     if (node.getRedirect() != null) {
/* 466 */       String redirect = (node.getRedirect() == this.root) ? "..." : ("-> " + node.getRedirect().getUsageText());
/* 467 */       result.add(prefix.isEmpty() ? (node.getUsageText() + " " + redirect) : (prefix + " " + redirect));
/* 468 */     } else if (!node.getChildren().isEmpty()) {
/* 469 */       for (CommandNode<S> child : (Iterable<CommandNode<S>>)node.getChildren()) {
/* 470 */         getAllUsage(child, source, result, prefix.isEmpty() ? child.getUsageText() : (prefix + " " + child.getUsageText()), restricted);
/*     */       }
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<CommandNode<S>, String> getSmartUsage(CommandNode<S> node, S source) {
/* 497 */     Map<CommandNode<S>, String> result = new LinkedHashMap<>();
/*     */     
/* 499 */     boolean optional = (node.getCommand() != null);
/* 500 */     for (CommandNode<S> child : (Iterable<CommandNode<S>>)node.getChildren()) {
/* 501 */       String usage = getSmartUsage(child, source, optional, false);
/* 502 */       if (usage != null) {
/* 503 */         result.put(child, usage);
/*     */       }
/*     */     } 
/* 506 */     return result;
/*     */   }
/*     */   
/*     */   private String getSmartUsage(CommandNode<S> node, S source, boolean optional, boolean deep) {
/* 510 */     if (!node.canUse(source)) {
/* 511 */       return null;
/*     */     }
/*     */     
/* 514 */     String self = optional ? ("[" + node.getUsageText() + "]") : node.getUsageText();
/* 515 */     boolean childOptional = (node.getCommand() != null);
/* 516 */     String open = childOptional ? "[" : "(";
/* 517 */     String close = childOptional ? "]" : ")";
/*     */     
/* 519 */     if (!deep) {
/* 520 */       if (node.getRedirect() != null) {
/* 521 */         String redirect = (node.getRedirect() == this.root) ? "..." : ("-> " + node.getRedirect().getUsageText());
/* 522 */         return self + " " + redirect;
/*     */       } 
/* 524 */       Collection<CommandNode<S>> children = (Collection<CommandNode<S>>)node.getChildren().stream().filter(c -> c.canUse(source)).collect(Collectors.toList());
/* 525 */       if (children.size() == 1) {
/* 526 */         String usage = getSmartUsage(children.iterator().next(), source, childOptional, childOptional);
/* 527 */         if (usage != null) {
/* 528 */           return self + " " + usage;
/*     */         }
/* 530 */       } else if (children.size() > 1) {
/* 531 */         Set<String> childUsage = new LinkedHashSet<>();
/* 532 */         for (CommandNode<S> child : children) {
/* 533 */           String usage = getSmartUsage(child, source, childOptional, true);
/* 534 */           if (usage != null) {
/* 535 */             childUsage.add(usage);
/*     */           }
/*     */         } 
/* 538 */         if (childUsage.size() == 1) {
/* 539 */           String usage = childUsage.iterator().next();
/* 540 */           return self + " " + (childOptional ? ("[" + usage + "]") : usage);
/* 541 */         }  if (childUsage.size() > 1) {
/* 542 */           StringBuilder builder = new StringBuilder(open);
/* 543 */           int count = 0;
/* 544 */           for (CommandNode<S> child : children) {
/* 545 */             if (count > 0) {
/* 546 */               builder.append("|");
/*     */             }
/* 548 */             builder.append(child.getUsageText());
/* 549 */             count++;
/*     */           } 
/* 551 */           if (count > 0) {
/* 552 */             builder.append(close);
/* 553 */             return self + " " + builder.toString();
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 560 */     return self;
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
/*     */   public CompletableFuture<Suggestions> getCompletionSuggestions(ParseResults<S> parse) {
/* 579 */     return getCompletionSuggestions(parse, parse.getReader().getTotalLength());
/*     */   }
/*     */   
/*     */   public CompletableFuture<Suggestions> getCompletionSuggestions(ParseResults<S> parse, int cursor) {
/* 583 */     CommandContextBuilder<S> context = parse.getContext();
/*     */     
/* 585 */     SuggestionContext<S> nodeBeforeCursor = context.findSuggestionContext(cursor);
/* 586 */     CommandNode<S> parent = nodeBeforeCursor.parent;
/* 587 */     int start = Math.min(nodeBeforeCursor.startPos, cursor);
/*     */     
/* 589 */     String fullInput = parse.getReader().getString();
/* 590 */     String truncatedInput = fullInput.substring(0, cursor);
/* 591 */     CompletableFuture[] arrayOfCompletableFuture = new CompletableFuture[parent.getChildren().size()];
/* 592 */     int i = 0;
/* 593 */     for (CommandNode<S> node : (Iterable<CommandNode<S>>)parent.getChildren()) {
/* 594 */       CompletableFuture<Suggestions> future = Suggestions.empty();
/*     */       try {
/* 596 */         future = node.listSuggestions(context.build(truncatedInput), new SuggestionsBuilder(truncatedInput, start));
/* 597 */       } catch (CommandSyntaxException commandSyntaxException) {}
/*     */       
/* 599 */       arrayOfCompletableFuture[i++] = future;
/*     */     } 
/*     */     
/* 602 */     CompletableFuture<Suggestions> result = new CompletableFuture<>();
/* 603 */     CompletableFuture.allOf((CompletableFuture<?>[])arrayOfCompletableFuture).thenRun(() -> {
/*     */           List<Suggestions> suggestions = new ArrayList<>();
/*     */           
/*     */           for (CompletableFuture<Suggestions> future : futures) {
/*     */             suggestions.add(future.join());
/*     */           }
/*     */           result.complete(Suggestions.merge(fullInput, suggestions));
/*     */         });
/* 611 */     return result;
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
/*     */   public RootCommandNode<S> getRoot() {
/* 624 */     return this.root;
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
/*     */   public Collection<String> getPath(CommandNode<S> target) {
/* 642 */     List<List<CommandNode<S>>> nodes = new ArrayList<>();
/* 643 */     addPaths((CommandNode<S>)this.root, nodes, new ArrayList<>());
/*     */     
/* 645 */     for (List<CommandNode<S>> list : nodes) {
/* 646 */       if (list.get(list.size() - 1) == target) {
/* 647 */         List<String> result = new ArrayList<>(list.size());
/* 648 */         for (CommandNode<S> node : list) {
/* 649 */           if (node != this.root) {
/* 650 */             result.add(node.getName());
/*     */           }
/*     */         } 
/* 653 */         return result;
/*     */       } 
/*     */     } 
/*     */     
/* 657 */     return Collections.emptyList();
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
/*     */   public CommandNode<S> findNode(Collection<String> path) {
/*     */     CommandNode<S> commandNode;
/* 672 */     RootCommandNode<S> rootCommandNode = this.root;
/* 673 */     for (String name : path) {
/* 674 */       commandNode = rootCommandNode.getChild(name);
/* 675 */       if (commandNode == null) {
/* 676 */         return null;
/*     */       }
/*     */     } 
/* 679 */     return commandNode;
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
/*     */   public void findAmbiguities(AmbiguityConsumer<S> consumer) {
/* 693 */     this.root.findAmbiguities(consumer);
/*     */   }
/*     */   
/*     */   private void addPaths(CommandNode<S> node, List<List<CommandNode<S>>> result, List<CommandNode<S>> parents) {
/* 697 */     List<CommandNode<S>> current = new ArrayList<>(parents);
/* 698 */     current.add(node);
/* 699 */     result.add(current);
/*     */     
/* 701 */     for (CommandNode<S> child : (Iterable<CommandNode<S>>)node.getChildren())
/* 702 */       addPaths(child, result, current); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\brigadier\CommandDispatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */