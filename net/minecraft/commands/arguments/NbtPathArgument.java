/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.nbt.CollectionTag;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.nbt.TagParser;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*     */ 
/*     */ public class NbtPathArgument implements ArgumentType<NbtPathArgument.NbtPath> {
/*  32 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "foo", "foo.bar", "foo[0]", "[0]", "[]", "{foo=bar}" });
/*  33 */   public static final SimpleCommandExceptionType ERROR_INVALID_NODE = new SimpleCommandExceptionType((Message)new TranslatableComponent("arguments.nbtpath.node.invalid")); public static final DynamicCommandExceptionType ERROR_NOTHING_FOUND; static {
/*  34 */     ERROR_NOTHING_FOUND = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("arguments.nbtpath.nothing_found", new Object[] { debug0 }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static NbtPathArgument nbtPath() {
/*  42 */     return new NbtPathArgument();
/*     */   }
/*     */   
/*     */   public static NbtPath getPath(CommandContext<CommandSourceStack> debug0, String debug1) {
/*  46 */     return (NbtPath)debug0.getArgument(debug1, NbtPath.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public NbtPath parse(StringReader debug1) throws CommandSyntaxException {
/*  51 */     List<Node> debug2 = Lists.newArrayList();
/*  52 */     int debug3 = debug1.getCursor();
/*     */     
/*  54 */     Object2IntOpenHashMap object2IntOpenHashMap = new Object2IntOpenHashMap();
/*  55 */     boolean debug5 = true;
/*  56 */     while (debug1.canRead() && debug1.peek() != ' ') {
/*  57 */       Node debug6 = parseNode(debug1, debug5);
/*  58 */       debug2.add(debug6);
/*  59 */       object2IntOpenHashMap.put(debug6, debug1.getCursor() - debug3);
/*  60 */       debug5 = false;
/*  61 */       if (debug1.canRead()) {
/*  62 */         char debug7 = debug1.peek();
/*  63 */         if (debug7 != ' ' && debug7 != '[' && debug7 != '{') {
/*  64 */           debug1.expect('.');
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  69 */     return new NbtPath(debug1.getString().substring(debug3, debug1.getCursor()), debug2.<Node>toArray(new Node[0]), (Object2IntMap<Node>)object2IntOpenHashMap);
/*     */   } private static Node parseNode(StringReader debug0, boolean debug1) throws CommandSyntaxException {
/*     */     CompoundTag compoundTag;
/*     */     int i, debug3;
/*  73 */     switch (debug0.peek()) {
/*     */       case '{':
/*  75 */         if (!debug1) {
/*  76 */           throw ERROR_INVALID_NODE.createWithContext(debug0);
/*     */         }
/*  78 */         compoundTag = (new TagParser(debug0)).readStruct();
/*  79 */         return new MatchRootObjectNode(compoundTag);
/*     */       
/*     */       case '[':
/*  82 */         debug0.skip();
/*  83 */         i = debug0.peek();
/*  84 */         if (i == 123) {
/*  85 */           CompoundTag compoundTag1 = (new TagParser(debug0)).readStruct();
/*  86 */           debug0.expect(']');
/*  87 */           return new MatchElementNode(compoundTag1);
/*  88 */         }  if (i == 93) {
/*  89 */           debug0.skip();
/*  90 */           return AllElementsNode.INSTANCE;
/*     */         } 
/*     */         
/*  93 */         debug3 = debug0.readInt();
/*  94 */         debug0.expect(']');
/*  95 */         return new IndexedElementNode(debug3);
/*     */       
/*     */       case '"':
/*  98 */         debug2 = debug0.readString();
/*  99 */         return readObjectNode(debug0, debug2);
/*     */     } 
/*     */     
/* 102 */     String debug2 = readUnquotedName(debug0);
/* 103 */     return readObjectNode(debug0, debug2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Node readObjectNode(StringReader debug0, String debug1) throws CommandSyntaxException {
/* 109 */     if (debug0.canRead() && debug0.peek() == '{') {
/* 110 */       CompoundTag debug2 = (new TagParser(debug0)).readStruct();
/* 111 */       return new MatchObjectNode(debug1, debug2);
/*     */     } 
/* 113 */     return new CompoundChildNode(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   private static String readUnquotedName(StringReader debug0) throws CommandSyntaxException {
/* 118 */     int debug1 = debug0.getCursor();
/* 119 */     while (debug0.canRead() && isAllowedInUnquotedName(debug0.peek())) {
/* 120 */       debug0.skip();
/*     */     }
/* 122 */     if (debug0.getCursor() == debug1) {
/* 123 */       throw ERROR_INVALID_NODE.createWithContext(debug0);
/*     */     }
/* 125 */     return debug0.getString().substring(debug1, debug0.getCursor());
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<String> getExamples() {
/* 130 */     return EXAMPLES;
/*     */   }
/*     */   
/*     */   private static boolean isAllowedInUnquotedName(char debug0) {
/* 134 */     return (debug0 != ' ' && debug0 != '"' && debug0 != '[' && debug0 != ']' && debug0 != '.' && debug0 != '{' && debug0 != '}');
/*     */   }
/*     */   
/*     */   public static class NbtPath {
/*     */     private final String original;
/*     */     private final Object2IntMap<NbtPathArgument.Node> nodeToOriginalPosition;
/*     */     private final NbtPathArgument.Node[] nodes;
/*     */     
/*     */     public NbtPath(String debug1, NbtPathArgument.Node[] debug2, Object2IntMap<NbtPathArgument.Node> debug3) {
/* 143 */       this.original = debug1;
/* 144 */       this.nodes = debug2;
/* 145 */       this.nodeToOriginalPosition = debug3;
/*     */     }
/*     */     
/*     */     public List<Tag> get(Tag debug1) throws CommandSyntaxException {
/* 149 */       List<Tag> debug2 = Collections.singletonList(debug1);
/* 150 */       for (NbtPathArgument.Node debug6 : this.nodes) {
/* 151 */         debug2 = debug6.get(debug2);
/* 152 */         if (debug2.isEmpty()) {
/* 153 */           throw createNotFoundException(debug6);
/*     */         }
/*     */       } 
/* 156 */       return debug2;
/*     */     }
/*     */     
/*     */     public int countMatching(Tag debug1) {
/* 160 */       List<Tag> debug2 = Collections.singletonList(debug1);
/* 161 */       for (NbtPathArgument.Node debug6 : this.nodes) {
/* 162 */         debug2 = debug6.get(debug2);
/* 163 */         if (debug2.isEmpty()) {
/* 164 */           return 0;
/*     */         }
/*     */       } 
/* 167 */       return debug2.size();
/*     */     }
/*     */     
/*     */     private List<Tag> getOrCreateParents(Tag debug1) throws CommandSyntaxException {
/* 171 */       List<Tag> debug2 = Collections.singletonList(debug1);
/*     */       
/* 173 */       for (int debug3 = 0; debug3 < this.nodes.length - 1; debug3++) {
/* 174 */         NbtPathArgument.Node debug4 = this.nodes[debug3];
/* 175 */         int debug5 = debug3 + 1;
/* 176 */         debug2 = debug4.getOrCreate(debug2, this.nodes[debug5]::createPreferredParentTag);
/* 177 */         if (debug2.isEmpty()) {
/* 178 */           throw createNotFoundException(debug4);
/*     */         }
/*     */       } 
/* 181 */       return debug2;
/*     */     }
/*     */     
/*     */     public List<Tag> getOrCreate(Tag debug1, Supplier<Tag> debug2) throws CommandSyntaxException {
/* 185 */       List<Tag> debug3 = getOrCreateParents(debug1);
/*     */       
/* 187 */       NbtPathArgument.Node debug4 = this.nodes[this.nodes.length - 1];
/* 188 */       return debug4.getOrCreate(debug3, debug2);
/*     */     }
/*     */     
/*     */     private static int apply(List<Tag> debug0, Function<Tag, Integer> debug1) {
/* 192 */       return ((Integer)debug0.stream().<Integer>map(debug1).reduce(Integer.valueOf(0), (debug0, debug1) -> Integer.valueOf(debug0.intValue() + debug1.intValue()))).intValue();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int set(Tag debug1, Supplier<Tag> debug2) throws CommandSyntaxException {
/* 200 */       List<Tag> debug3 = getOrCreateParents(debug1);
/*     */       
/* 202 */       NbtPathArgument.Node debug4 = this.nodes[this.nodes.length - 1];
/* 203 */       return apply(debug3, debug2 -> Integer.valueOf(debug0.setTag(debug2, debug1)));
/*     */     }
/*     */     
/*     */     public int remove(Tag debug1) {
/* 207 */       List<Tag> debug2 = Collections.singletonList(debug1);
/*     */       
/* 209 */       for (int i = 0; i < this.nodes.length - 1; i++) {
/* 210 */         debug2 = this.nodes[i].get(debug2);
/*     */       }
/*     */       
/* 213 */       NbtPathArgument.Node debug3 = this.nodes[this.nodes.length - 1];
/* 214 */       return apply(debug2, debug3::removeTag);
/*     */     }
/*     */     
/*     */     private CommandSyntaxException createNotFoundException(NbtPathArgument.Node debug1) {
/* 218 */       int debug2 = this.nodeToOriginalPosition.getInt(debug1);
/* 219 */       return NbtPathArgument.ERROR_NOTHING_FOUND.create(this.original.substring(0, debug2));
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 224 */       return this.original;
/*     */     }
/*     */   }
/*     */   
/*     */   private static Predicate<Tag> createTagPredicate(CompoundTag debug0) {
/* 229 */     return debug1 -> NbtUtils.compareNbt((Tag)debug0, debug1, true);
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
/*     */   static interface Node
/*     */   {
/*     */     default List<Tag> get(List<Tag> debug1) {
/* 244 */       return collect(debug1, this::getTag);
/*     */     }
/*     */     
/*     */     default List<Tag> getOrCreate(List<Tag> debug1, Supplier<Tag> debug2) {
/* 248 */       return collect(debug1, (debug2, debug3) -> getOrCreateTag(debug2, debug1, debug3));
/*     */     } void getTag(Tag param1Tag, List<Tag> param1List); void getOrCreateTag(Tag param1Tag, Supplier<Tag> param1Supplier, List<Tag> param1List);
/*     */     Tag createPreferredParentTag();
/*     */     default List<Tag> collect(List<Tag> debug1, BiConsumer<Tag, List<Tag>> debug2) {
/* 252 */       List<Tag> debug3 = Lists.newArrayList();
/*     */       
/* 254 */       for (Tag debug5 : debug1) {
/* 255 */         debug2.accept(debug5, debug3);
/*     */       }
/*     */       
/* 258 */       return debug3;
/*     */     }
/*     */     
/*     */     int setTag(Tag param1Tag, Supplier<Tag> param1Supplier);
/*     */     
/*     */     int removeTag(Tag param1Tag); }
/*     */   
/*     */   static class CompoundChildNode implements Node { public CompoundChildNode(String debug1) {
/* 266 */       this.name = debug1;
/*     */     }
/*     */     private final String name;
/*     */     
/*     */     public void getTag(Tag debug1, List<Tag> debug2) {
/* 271 */       if (debug1 instanceof CompoundTag) {
/* 272 */         Tag debug3 = ((CompoundTag)debug1).get(this.name);
/* 273 */         if (debug3 != null) {
/* 274 */           debug2.add(debug3);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void getOrCreateTag(Tag debug1, Supplier<Tag> debug2, List<Tag> debug3) {
/* 281 */       if (debug1 instanceof CompoundTag) {
/* 282 */         Tag debug5; CompoundTag debug4 = (CompoundTag)debug1;
/*     */         
/* 284 */         if (debug4.contains(this.name)) {
/* 285 */           debug5 = debug4.get(this.name);
/*     */         } else {
/* 287 */           debug5 = debug2.get();
/* 288 */           debug4.put(this.name, debug5);
/*     */         } 
/*     */         
/* 291 */         debug3.add(debug5);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public Tag createPreferredParentTag() {
/* 297 */       return (Tag)new CompoundTag();
/*     */     }
/*     */ 
/*     */     
/*     */     public int setTag(Tag debug1, Supplier<Tag> debug2) {
/* 302 */       if (debug1 instanceof CompoundTag) {
/* 303 */         CompoundTag debug3 = (CompoundTag)debug1;
/* 304 */         Tag debug4 = debug2.get();
/* 305 */         Tag debug5 = debug3.put(this.name, debug4);
/* 306 */         if (!debug4.equals(debug5)) {
/* 307 */           return 1;
/*     */         }
/*     */       } 
/*     */       
/* 311 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public int removeTag(Tag debug1) {
/* 316 */       if (debug1 instanceof CompoundTag) {
/* 317 */         CompoundTag debug2 = (CompoundTag)debug1;
/* 318 */         if (debug2.contains(this.name)) {
/* 319 */           debug2.remove(this.name);
/* 320 */           return 1;
/*     */         } 
/*     */       } 
/*     */       
/* 324 */       return 0;
/*     */     } }
/*     */ 
/*     */   
/*     */   static class IndexedElementNode implements Node {
/*     */     private final int index;
/*     */     
/*     */     public IndexedElementNode(int debug1) {
/* 332 */       this.index = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public void getTag(Tag debug1, List<Tag> debug2) {
/* 337 */       if (debug1 instanceof CollectionTag) {
/* 338 */         CollectionTag<?> debug3 = (CollectionTag)debug1;
/*     */         
/* 340 */         int debug4 = debug3.size();
/* 341 */         int debug5 = (this.index < 0) ? (debug4 + this.index) : this.index;
/*     */         
/* 343 */         if (0 <= debug5 && debug5 < debug4) {
/* 344 */           debug2.add(debug3.get(debug5));
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void getOrCreateTag(Tag debug1, Supplier<Tag> debug2, List<Tag> debug3) {
/* 351 */       getTag(debug1, debug3);
/*     */     }
/*     */ 
/*     */     
/*     */     public Tag createPreferredParentTag() {
/* 356 */       return (Tag)new ListTag();
/*     */     }
/*     */ 
/*     */     
/*     */     public int setTag(Tag debug1, Supplier<Tag> debug2) {
/* 361 */       if (debug1 instanceof CollectionTag) {
/* 362 */         CollectionTag<?> debug3 = (CollectionTag)debug1;
/* 363 */         int debug4 = debug3.size();
/* 364 */         int debug5 = (this.index < 0) ? (debug4 + this.index) : this.index;
/*     */         
/* 366 */         if (0 <= debug5 && debug5 < debug4) {
/* 367 */           Tag debug6 = (Tag)debug3.get(debug5);
/* 368 */           Tag debug7 = debug2.get();
/* 369 */           if (!debug7.equals(debug6) && debug3.setTag(debug5, debug7)) {
/* 370 */             return 1;
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 375 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public int removeTag(Tag debug1) {
/* 380 */       if (debug1 instanceof CollectionTag) {
/* 381 */         CollectionTag<?> debug2 = (CollectionTag)debug1;
/* 382 */         int debug3 = debug2.size();
/* 383 */         int debug4 = (this.index < 0) ? (debug3 + this.index) : this.index;
/*     */         
/* 385 */         if (0 <= debug4 && debug4 < debug3) {
/* 386 */           debug2.remove(debug4);
/* 387 */           return 1;
/*     */         } 
/*     */       } 
/*     */       
/* 391 */       return 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class MatchElementNode
/*     */     implements Node
/*     */   {
/*     */     private final CompoundTag pattern;
/*     */     
/*     */     private final Predicate<Tag> predicate;
/*     */     
/*     */     public MatchElementNode(CompoundTag debug1) {
/* 404 */       this.pattern = debug1;
/* 405 */       this.predicate = NbtPathArgument.createTagPredicate(debug1);
/*     */     }
/*     */ 
/*     */     
/*     */     public void getTag(Tag debug1, List<Tag> debug2) {
/* 410 */       if (debug1 instanceof ListTag) {
/* 411 */         ListTag debug3 = (ListTag)debug1;
/* 412 */         debug3.stream().filter(this.predicate).forEach(debug2::add);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void getOrCreateTag(Tag debug1, Supplier<Tag> debug2, List<Tag> debug3) {
/* 418 */       MutableBoolean debug4 = new MutableBoolean();
/* 419 */       if (debug1 instanceof ListTag) {
/* 420 */         ListTag debug5 = (ListTag)debug1;
/* 421 */         debug5.stream().filter(this.predicate).forEach(debug2 -> {
/*     */               debug0.add(debug2);
/*     */               
/*     */               debug1.setTrue();
/*     */             });
/* 426 */         if (debug4.isFalse()) {
/* 427 */           CompoundTag debug6 = this.pattern.copy();
/* 428 */           debug5.add(debug6);
/* 429 */           debug3.add(debug6);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public Tag createPreferredParentTag() {
/* 436 */       return (Tag)new ListTag();
/*     */     }
/*     */ 
/*     */     
/*     */     public int setTag(Tag debug1, Supplier<Tag> debug2) {
/* 441 */       int debug3 = 0;
/* 442 */       if (debug1 instanceof ListTag) {
/* 443 */         ListTag debug4 = (ListTag)debug1;
/* 444 */         int debug5 = debug4.size();
/* 445 */         if (debug5 == 0) {
/* 446 */           debug4.add(debug2.get());
/* 447 */           debug3++;
/*     */         } else {
/* 449 */           for (int debug6 = 0; debug6 < debug5; debug6++) {
/* 450 */             Tag debug7 = debug4.get(debug6);
/* 451 */             if (this.predicate.test(debug7)) {
/* 452 */               Tag debug8 = debug2.get();
/* 453 */               if (!debug8.equals(debug7) && debug4.setTag(debug6, debug8)) {
/* 454 */                 debug3++;
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 461 */       return debug3;
/*     */     }
/*     */ 
/*     */     
/*     */     public int removeTag(Tag debug1) {
/* 466 */       int debug2 = 0;
/* 467 */       if (debug1 instanceof ListTag) {
/* 468 */         ListTag debug3 = (ListTag)debug1;
/* 469 */         for (int debug4 = debug3.size() - 1; debug4 >= 0; debug4--) {
/* 470 */           if (this.predicate.test(debug3.get(debug4))) {
/* 471 */             debug3.remove(debug4);
/* 472 */             debug2++;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 477 */       return debug2;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class AllElementsNode
/*     */     implements Node
/*     */   {
/* 485 */     public static final AllElementsNode INSTANCE = new AllElementsNode();
/*     */ 
/*     */     
/*     */     public void getTag(Tag debug1, List<Tag> debug2) {
/* 489 */       if (debug1 instanceof CollectionTag) {
/* 490 */         debug2.addAll((Collection<? extends Tag>)debug1);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void getOrCreateTag(Tag debug1, Supplier<Tag> debug2, List<Tag> debug3) {
/* 496 */       if (debug1 instanceof CollectionTag) {
/* 497 */         CollectionTag<?> debug4 = (CollectionTag)debug1;
/* 498 */         if (debug4.isEmpty()) {
/* 499 */           Tag debug5 = debug2.get();
/* 500 */           if (debug4.addTag(0, debug5)) {
/* 501 */             debug3.add(debug5);
/*     */           }
/*     */         } else {
/* 504 */           debug3.addAll((Collection<?>)debug4);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public Tag createPreferredParentTag() {
/* 511 */       return (Tag)new ListTag();
/*     */     }
/*     */ 
/*     */     
/*     */     public int setTag(Tag debug1, Supplier<Tag> debug2) {
/* 516 */       if (debug1 instanceof CollectionTag) {
/* 517 */         CollectionTag<?> debug3 = (CollectionTag)debug1;
/* 518 */         int debug4 = debug3.size();
/* 519 */         if (debug4 == 0) {
/* 520 */           debug3.addTag(0, debug2.get());
/* 521 */           return 1;
/*     */         } 
/* 523 */         Tag debug5 = debug2.get();
/* 524 */         int debug6 = debug4 - (int)debug3.stream().filter(debug5::equals).count();
/* 525 */         if (debug6 == 0) {
/* 526 */           return 0;
/*     */         }
/* 528 */         debug3.clear();
/* 529 */         if (!debug3.addTag(0, debug5)) {
/* 530 */           return 0;
/*     */         }
/* 532 */         for (int debug7 = 1; debug7 < debug4; debug7++) {
/* 533 */           debug3.addTag(debug7, debug2.get());
/*     */         }
/*     */         
/* 536 */         return debug6;
/*     */       } 
/*     */       
/* 539 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public int removeTag(Tag debug1) {
/* 544 */       if (debug1 instanceof CollectionTag) {
/* 545 */         CollectionTag<?> debug2 = (CollectionTag)debug1;
/* 546 */         int debug3 = debug2.size();
/* 547 */         if (debug3 > 0) {
/* 548 */           debug2.clear();
/* 549 */           return debug3;
/*     */         } 
/*     */       } 
/*     */       
/* 553 */       return 0;
/*     */     }
/*     */   }
/*     */   
/*     */   static class MatchObjectNode implements Node {
/*     */     private final String name;
/*     */     private final CompoundTag pattern;
/*     */     private final Predicate<Tag> predicate;
/*     */     
/*     */     public MatchObjectNode(String debug1, CompoundTag debug2) {
/* 563 */       this.name = debug1;
/* 564 */       this.pattern = debug2;
/* 565 */       this.predicate = NbtPathArgument.createTagPredicate(debug2);
/*     */     }
/*     */ 
/*     */     
/*     */     public void getTag(Tag debug1, List<Tag> debug2) {
/* 570 */       if (debug1 instanceof CompoundTag) {
/* 571 */         Tag debug3 = ((CompoundTag)debug1).get(this.name);
/* 572 */         if (this.predicate.test(debug3)) {
/* 573 */           debug2.add(debug3);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void getOrCreateTag(Tag debug1, Supplier<Tag> debug2, List<Tag> debug3) {
/* 580 */       if (debug1 instanceof CompoundTag) {
/* 581 */         CompoundTag compoundTag1, debug4 = (CompoundTag)debug1;
/* 582 */         Tag debug5 = debug4.get(this.name);
/* 583 */         if (debug5 == null) {
/* 584 */           compoundTag1 = this.pattern.copy();
/* 585 */           debug4.put(this.name, (Tag)compoundTag1);
/* 586 */           debug3.add(compoundTag1);
/* 587 */         } else if (this.predicate.test(compoundTag1)) {
/* 588 */           debug3.add(compoundTag1);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public Tag createPreferredParentTag() {
/* 595 */       return (Tag)new CompoundTag();
/*     */     }
/*     */ 
/*     */     
/*     */     public int setTag(Tag debug1, Supplier<Tag> debug2) {
/* 600 */       if (debug1 instanceof CompoundTag) {
/* 601 */         CompoundTag debug3 = (CompoundTag)debug1;
/* 602 */         Tag debug4 = debug3.get(this.name);
/* 603 */         if (this.predicate.test(debug4)) {
/* 604 */           Tag debug5 = debug2.get();
/* 605 */           if (!debug5.equals(debug4)) {
/* 606 */             debug3.put(this.name, debug5);
/* 607 */             return 1;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 612 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public int removeTag(Tag debug1) {
/* 617 */       if (debug1 instanceof CompoundTag) {
/* 618 */         CompoundTag debug2 = (CompoundTag)debug1;
/* 619 */         Tag debug3 = debug2.get(this.name);
/* 620 */         if (this.predicate.test(debug3)) {
/* 621 */           debug2.remove(this.name);
/* 622 */           return 1;
/*     */         } 
/*     */       } 
/*     */       
/* 626 */       return 0;
/*     */     }
/*     */   }
/*     */   
/*     */   static class MatchRootObjectNode implements Node {
/*     */     private final Predicate<Tag> predicate;
/*     */     
/*     */     public MatchRootObjectNode(CompoundTag debug1) {
/* 634 */       this.predicate = NbtPathArgument.createTagPredicate(debug1);
/*     */     }
/*     */ 
/*     */     
/*     */     public void getTag(Tag debug1, List<Tag> debug2) {
/* 639 */       if (debug1 instanceof CompoundTag && this.predicate.test(debug1)) {
/* 640 */         debug2.add(debug1);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void getOrCreateTag(Tag debug1, Supplier<Tag> debug2, List<Tag> debug3) {
/* 646 */       getTag(debug1, debug3);
/*     */     }
/*     */ 
/*     */     
/*     */     public Tag createPreferredParentTag() {
/* 651 */       return (Tag)new CompoundTag();
/*     */     }
/*     */ 
/*     */     
/*     */     public int setTag(Tag debug1, Supplier<Tag> debug2) {
/* 656 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public int removeTag(Tag debug1) {
/* 661 */       return 0;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\NbtPathArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */