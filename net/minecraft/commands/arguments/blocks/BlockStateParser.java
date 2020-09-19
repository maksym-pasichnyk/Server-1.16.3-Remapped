/*     */ package net.minecraft.commands.arguments.blocks;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.mojang.brigadier.ImmutableStringReader;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.Dynamic3CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.BiFunction;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.TagParser;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.tags.TagCollection;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class BlockStateParser {
/*  33 */   public static final SimpleCommandExceptionType ERROR_NO_TAGS_ALLOWED = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.block.tag.disallowed")); public static final DynamicCommandExceptionType ERROR_UNKNOWN_BLOCK; public static final Dynamic2CommandExceptionType ERROR_UNKNOWN_PROPERTY; static {
/*  34 */     ERROR_UNKNOWN_BLOCK = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("argument.block.id.invalid", new Object[] { debug0 }));
/*  35 */     ERROR_UNKNOWN_PROPERTY = new Dynamic2CommandExceptionType((debug0, debug1) -> new TranslatableComponent("argument.block.property.unknown", new Object[] { debug0, debug1 }));
/*  36 */     ERROR_DUPLICATE_PROPERTY = new Dynamic2CommandExceptionType((debug0, debug1) -> new TranslatableComponent("argument.block.property.duplicate", new Object[] { debug1, debug0 }));
/*  37 */     ERROR_INVALID_VALUE = new Dynamic3CommandExceptionType((debug0, debug1, debug2) -> new TranslatableComponent("argument.block.property.invalid", new Object[] { debug0, debug2, debug1 }));
/*  38 */     ERROR_EXPECTED_VALUE = new Dynamic2CommandExceptionType((debug0, debug1) -> new TranslatableComponent("argument.block.property.novalue", new Object[] { debug0, debug1 }));
/*  39 */   } public static final Dynamic2CommandExceptionType ERROR_DUPLICATE_PROPERTY; public static final Dynamic3CommandExceptionType ERROR_INVALID_VALUE; public static final Dynamic2CommandExceptionType ERROR_EXPECTED_VALUE; public static final SimpleCommandExceptionType ERROR_EXPECTED_END_OF_PROPERTIES = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.block.property.unclosed"));
/*     */   
/*     */   private static final BiFunction<SuggestionsBuilder, TagCollection<Block>, CompletableFuture<Suggestions>> SUGGEST_NOTHING;
/*     */   
/*     */   private final StringReader reader;
/*     */   
/*     */   private final boolean forTesting;
/*     */   
/*     */   static {
/*  48 */     SUGGEST_NOTHING = ((debug0, debug1) -> debug0.buildFuture());
/*     */   }
/*     */ 
/*     */   
/*  52 */   private final Map<Property<?>, Comparable<?>> properties = Maps.newHashMap();
/*  53 */   private final Map<String, String> vagueProperties = Maps.newHashMap();
/*  54 */   private ResourceLocation id = new ResourceLocation("");
/*     */   private StateDefinition<Block, BlockState> definition;
/*     */   private BlockState state;
/*     */   @Nullable
/*     */   private CompoundTag nbt;
/*  59 */   private ResourceLocation tag = new ResourceLocation("");
/*     */   private int tagCursor;
/*  61 */   private BiFunction<SuggestionsBuilder, TagCollection<Block>, CompletableFuture<Suggestions>> suggestions = SUGGEST_NOTHING;
/*     */   
/*     */   public BlockStateParser(StringReader debug1, boolean debug2) {
/*  64 */     this.reader = debug1;
/*  65 */     this.forTesting = debug2;
/*     */   }
/*     */   
/*     */   public Map<Property<?>, Comparable<?>> getProperties() {
/*  69 */     return this.properties;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public BlockState getState() {
/*  74 */     return this.state;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public CompoundTag getNbt() {
/*  79 */     return this.nbt;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public ResourceLocation getTag() {
/*  84 */     return this.tag;
/*     */   }
/*     */   
/*     */   public BlockStateParser parse(boolean debug1) throws CommandSyntaxException {
/*  88 */     this.suggestions = this::suggestBlockIdOrTag;
/*  89 */     if (this.reader.canRead() && this.reader.peek() == '#') {
/*  90 */       readTag();
/*  91 */       this.suggestions = this::suggestOpenVaguePropertiesOrNbt;
/*  92 */       if (this.reader.canRead() && this.reader.peek() == '[') {
/*  93 */         readVagueProperties();
/*  94 */         this.suggestions = this::suggestOpenNbt;
/*     */       } 
/*     */     } else {
/*  97 */       readBlock();
/*  98 */       this.suggestions = this::suggestOpenPropertiesOrNbt;
/*  99 */       if (this.reader.canRead() && this.reader.peek() == '[') {
/* 100 */         readProperties();
/* 101 */         this.suggestions = this::suggestOpenNbt;
/*     */       } 
/*     */     } 
/* 104 */     if (debug1 && this.reader.canRead() && this.reader.peek() == '{') {
/* 105 */       this.suggestions = SUGGEST_NOTHING;
/* 106 */       readNbt();
/*     */     } 
/* 108 */     return this;
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestPropertyNameOrEnd(SuggestionsBuilder debug1, TagCollection<Block> debug2) {
/* 112 */     if (debug1.getRemaining().isEmpty()) {
/* 113 */       debug1.suggest(String.valueOf(']'));
/*     */     }
/*     */     
/* 116 */     return suggestPropertyName(debug1, debug2);
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestVaguePropertyNameOrEnd(SuggestionsBuilder debug1, TagCollection<Block> debug2) {
/* 120 */     if (debug1.getRemaining().isEmpty()) {
/* 121 */       debug1.suggest(String.valueOf(']'));
/*     */     }
/* 123 */     return suggestVaguePropertyName(debug1, debug2);
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestPropertyName(SuggestionsBuilder debug1, TagCollection<Block> debug2) {
/* 127 */     String debug3 = debug1.getRemaining().toLowerCase(Locale.ROOT);
/* 128 */     for (Property<?> debug5 : (Iterable<Property<?>>)this.state.getProperties()) {
/* 129 */       if (!this.properties.containsKey(debug5) && debug5.getName().startsWith(debug3)) {
/* 130 */         debug1.suggest(debug5.getName() + '=');
/*     */       }
/*     */     } 
/* 133 */     return debug1.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestVaguePropertyName(SuggestionsBuilder debug1, TagCollection<Block> debug2) {
/* 137 */     String debug3 = debug1.getRemaining().toLowerCase(Locale.ROOT);
/* 138 */     if (this.tag != null && !this.tag.getPath().isEmpty()) {
/* 139 */       Tag<Block> debug4 = debug2.getTag(this.tag);
/* 140 */       if (debug4 != null) {
/* 141 */         for (Block debug6 : debug4.getValues()) {
/* 142 */           for (Property<?> debug8 : (Iterable<Property<?>>)debug6.getStateDefinition().getProperties()) {
/* 143 */             if (!this.vagueProperties.containsKey(debug8.getName()) && debug8.getName().startsWith(debug3)) {
/* 144 */               debug1.suggest(debug8.getName() + '=');
/*     */             }
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/* 150 */     return debug1.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestOpenNbt(SuggestionsBuilder debug1, TagCollection<Block> debug2) {
/* 154 */     if (debug1.getRemaining().isEmpty() && hasBlockEntity(debug2)) {
/* 155 */       debug1.suggest(String.valueOf('{'));
/*     */     }
/* 157 */     return debug1.buildFuture();
/*     */   }
/*     */   
/*     */   private boolean hasBlockEntity(TagCollection<Block> debug1) {
/* 161 */     if (this.state != null) {
/* 162 */       return this.state.getBlock().isEntityBlock();
/*     */     }
/*     */     
/* 165 */     if (this.tag != null) {
/* 166 */       Tag<Block> debug2 = debug1.getTag(this.tag);
/*     */       
/* 168 */       if (debug2 != null) {
/* 169 */         for (Block debug4 : debug2.getValues()) {
/* 170 */           if (debug4.isEntityBlock()) {
/* 171 */             return true;
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 177 */     return false;
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestEquals(SuggestionsBuilder debug1, TagCollection<Block> debug2) {
/* 181 */     if (debug1.getRemaining().isEmpty()) {
/* 182 */       debug1.suggest(String.valueOf('='));
/*     */     }
/* 184 */     return debug1.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestNextPropertyOrEnd(SuggestionsBuilder debug1, TagCollection<Block> debug2) {
/* 188 */     if (debug1.getRemaining().isEmpty()) {
/* 189 */       debug1.suggest(String.valueOf(']'));
/*     */     }
/* 191 */     if (debug1.getRemaining().isEmpty() && this.properties.size() < this.state.getProperties().size()) {
/* 192 */       debug1.suggest(String.valueOf(','));
/*     */     }
/* 194 */     return debug1.buildFuture();
/*     */   }
/*     */   
/*     */   private static <T extends Comparable<T>> SuggestionsBuilder addSuggestions(SuggestionsBuilder debug0, Property<T> debug1) {
/* 198 */     for (Comparable comparable : debug1.getPossibleValues()) {
/* 199 */       if (comparable instanceof Integer) {
/* 200 */         debug0.suggest(((Integer)comparable).intValue()); continue;
/*     */       } 
/* 202 */       debug0.suggest(debug1.getName(comparable));
/*     */     } 
/*     */     
/* 205 */     return debug0;
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestVaguePropertyValue(SuggestionsBuilder debug1, TagCollection<Block> debug2, String debug3) {
/* 209 */     boolean debug4 = false;
/* 210 */     if (this.tag != null && !this.tag.getPath().isEmpty()) {
/* 211 */       Tag<Block> debug5 = debug2.getTag(this.tag);
/* 212 */       if (debug5 != null) {
/* 213 */         for (Block debug7 : debug5.getValues()) {
/* 214 */           Property<?> debug8 = debug7.getStateDefinition().getProperty(debug3);
/* 215 */           if (debug8 != null) {
/* 216 */             addSuggestions(debug1, debug8);
/*     */           }
/* 218 */           if (!debug4) {
/* 219 */             for (Property<?> debug10 : (Iterable<Property<?>>)debug7.getStateDefinition().getProperties()) {
/* 220 */               if (!this.vagueProperties.containsKey(debug10.getName())) {
/* 221 */                 debug4 = true;
/*     */               }
/*     */             } 
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 229 */     if (debug4) {
/* 230 */       debug1.suggest(String.valueOf(','));
/*     */     }
/* 232 */     debug1.suggest(String.valueOf(']'));
/* 233 */     return debug1.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestOpenVaguePropertiesOrNbt(SuggestionsBuilder debug1, TagCollection<Block> debug2) {
/* 237 */     if (debug1.getRemaining().isEmpty()) {
/* 238 */       Tag<Block> debug3 = debug2.getTag(this.tag);
/* 239 */       if (debug3 != null) {
/* 240 */         int i; boolean debug4 = false;
/* 241 */         boolean debug5 = false;
/*     */         
/* 243 */         for (Block debug7 : debug3.getValues()) {
/* 244 */           i = debug4 | (!debug7.getStateDefinition().getProperties().isEmpty() ? 1 : 0);
/* 245 */           debug5 |= debug7.isEntityBlock();
/*     */           
/* 247 */           if (i != 0 && debug5) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */         
/* 252 */         if (i != 0) {
/* 253 */           debug1.suggest(String.valueOf('['));
/*     */         }
/*     */         
/* 256 */         if (debug5) {
/* 257 */           debug1.suggest(String.valueOf('{'));
/*     */         }
/*     */       } 
/*     */     } 
/* 261 */     return suggestTag(debug1, debug2);
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestOpenPropertiesOrNbt(SuggestionsBuilder debug1, TagCollection<Block> debug2) {
/* 265 */     if (debug1.getRemaining().isEmpty()) {
/* 266 */       if (!this.state.getBlock().getStateDefinition().getProperties().isEmpty()) {
/* 267 */         debug1.suggest(String.valueOf('['));
/*     */       }
/* 269 */       if (this.state.getBlock().isEntityBlock()) {
/* 270 */         debug1.suggest(String.valueOf('{'));
/*     */       }
/*     */     } 
/* 273 */     return debug1.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestTag(SuggestionsBuilder debug1, TagCollection<Block> debug2) {
/* 277 */     return SharedSuggestionProvider.suggestResource(debug2.getAvailableTags(), debug1.createOffset(this.tagCursor).add(debug1));
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestBlockIdOrTag(SuggestionsBuilder debug1, TagCollection<Block> debug2) {
/* 281 */     if (this.forTesting) {
/* 282 */       SharedSuggestionProvider.suggestResource(debug2.getAvailableTags(), debug1, String.valueOf('#'));
/*     */     }
/* 284 */     SharedSuggestionProvider.suggestResource(Registry.BLOCK.keySet(), debug1);
/* 285 */     return debug1.buildFuture();
/*     */   }
/*     */   
/*     */   public void readBlock() throws CommandSyntaxException {
/* 289 */     int debug1 = this.reader.getCursor();
/* 290 */     this.id = ResourceLocation.read(this.reader);
/*     */     
/* 292 */     Block debug2 = (Block)Registry.BLOCK.getOptional(this.id).orElseThrow(() -> {
/*     */           this.reader.setCursor(debug1);
/*     */           
/*     */           return ERROR_UNKNOWN_BLOCK.createWithContext((ImmutableStringReader)this.reader, this.id.toString());
/*     */         });
/* 297 */     this.definition = debug2.getStateDefinition();
/* 298 */     this.state = debug2.defaultBlockState();
/*     */   }
/*     */   
/*     */   public void readTag() throws CommandSyntaxException {
/* 302 */     if (!this.forTesting) {
/* 303 */       throw ERROR_NO_TAGS_ALLOWED.create();
/*     */     }
/*     */     
/* 306 */     this.suggestions = this::suggestTag;
/* 307 */     this.reader.expect('#');
/* 308 */     this.tagCursor = this.reader.getCursor();
/* 309 */     this.tag = ResourceLocation.read(this.reader);
/*     */   }
/*     */   
/*     */   public void readProperties() throws CommandSyntaxException {
/* 313 */     this.reader.skip();
/* 314 */     this.suggestions = this::suggestPropertyNameOrEnd;
/*     */     
/* 316 */     this.reader.skipWhitespace();
/* 317 */     while (this.reader.canRead() && this.reader.peek() != ']') {
/* 318 */       this.reader.skipWhitespace();
/* 319 */       int debug1 = this.reader.getCursor();
/* 320 */       String debug2 = this.reader.readString();
/* 321 */       Property<?> debug3 = this.definition.getProperty(debug2);
/* 322 */       if (debug3 == null) {
/* 323 */         this.reader.setCursor(debug1);
/* 324 */         throw ERROR_UNKNOWN_PROPERTY.createWithContext(this.reader, this.id.toString(), debug2);
/*     */       } 
/* 326 */       if (this.properties.containsKey(debug3)) {
/* 327 */         this.reader.setCursor(debug1);
/* 328 */         throw ERROR_DUPLICATE_PROPERTY.createWithContext(this.reader, this.id.toString(), debug2);
/*     */       } 
/*     */       
/* 331 */       this.reader.skipWhitespace();
/* 332 */       this.suggestions = this::suggestEquals;
/* 333 */       if (!this.reader.canRead() || this.reader.peek() != '=') {
/* 334 */         throw ERROR_EXPECTED_VALUE.createWithContext(this.reader, this.id.toString(), debug2);
/*     */       }
/* 336 */       this.reader.skip();
/* 337 */       this.reader.skipWhitespace();
/*     */       
/* 339 */       this.suggestions = ((debug1, debug2) -> addSuggestions(debug1, debug0).buildFuture());
/* 340 */       int debug4 = this.reader.getCursor();
/* 341 */       setValue(debug3, this.reader.readString(), debug4);
/*     */       
/* 343 */       this.suggestions = this::suggestNextPropertyOrEnd;
/* 344 */       this.reader.skipWhitespace();
/* 345 */       if (this.reader.canRead()) {
/* 346 */         if (this.reader.peek() == ',') {
/* 347 */           this.reader.skip();
/* 348 */           this.suggestions = this::suggestPropertyName; continue;
/* 349 */         }  if (this.reader.peek() == ']') {
/*     */           break;
/*     */         }
/* 352 */         throw ERROR_EXPECTED_END_OF_PROPERTIES.createWithContext(this.reader);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 357 */     if (this.reader.canRead()) {
/* 358 */       this.reader.skip();
/*     */     } else {
/* 360 */       throw ERROR_EXPECTED_END_OF_PROPERTIES.createWithContext(this.reader);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void readVagueProperties() throws CommandSyntaxException {
/* 365 */     this.reader.skip();
/* 366 */     this.suggestions = this::suggestVaguePropertyNameOrEnd;
/* 367 */     int debug1 = -1;
/*     */     
/* 369 */     this.reader.skipWhitespace();
/* 370 */     while (this.reader.canRead() && this.reader.peek() != ']') {
/* 371 */       this.reader.skipWhitespace();
/* 372 */       int debug2 = this.reader.getCursor();
/* 373 */       String debug3 = this.reader.readString();
/* 374 */       if (this.vagueProperties.containsKey(debug3)) {
/* 375 */         this.reader.setCursor(debug2);
/* 376 */         throw ERROR_DUPLICATE_PROPERTY.createWithContext(this.reader, this.id.toString(), debug3);
/*     */       } 
/*     */       
/* 379 */       this.reader.skipWhitespace();
/* 380 */       if (!this.reader.canRead() || this.reader.peek() != '=') {
/* 381 */         this.reader.setCursor(debug2);
/* 382 */         throw ERROR_EXPECTED_VALUE.createWithContext(this.reader, this.id.toString(), debug3);
/*     */       } 
/* 384 */       this.reader.skip();
/*     */       
/* 386 */       this.reader.skipWhitespace();
/* 387 */       this.suggestions = ((debug2, debug3) -> suggestVaguePropertyValue(debug2, debug3, debug1));
/* 388 */       debug1 = this.reader.getCursor();
/* 389 */       String debug4 = this.reader.readString();
/* 390 */       this.vagueProperties.put(debug3, debug4);
/*     */       
/* 392 */       this.reader.skipWhitespace();
/* 393 */       if (this.reader.canRead()) {
/* 394 */         debug1 = -1;
/* 395 */         if (this.reader.peek() == ',') {
/* 396 */           this.reader.skip();
/* 397 */           this.suggestions = this::suggestVaguePropertyName; continue;
/* 398 */         }  if (this.reader.peek() == ']') {
/*     */           break;
/*     */         }
/* 401 */         throw ERROR_EXPECTED_END_OF_PROPERTIES.createWithContext(this.reader);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 406 */     if (this.reader.canRead()) {
/* 407 */       this.reader.skip();
/*     */     } else {
/* 409 */       if (debug1 >= 0) {
/* 410 */         this.reader.setCursor(debug1);
/*     */       }
/* 412 */       throw ERROR_EXPECTED_END_OF_PROPERTIES.createWithContext(this.reader);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void readNbt() throws CommandSyntaxException {
/* 417 */     this.nbt = (new TagParser(this.reader)).readStruct();
/*     */   }
/*     */   
/*     */   private <T extends Comparable<T>> void setValue(Property<T> debug1, String debug2, int debug3) throws CommandSyntaxException {
/* 421 */     Optional<T> debug4 = debug1.getValue(debug2);
/* 422 */     if (debug4.isPresent()) {
/* 423 */       this.state = (BlockState)this.state.setValue(debug1, (Comparable)debug4.get());
/* 424 */       this.properties.put(debug1, (Comparable<?>)debug4.get());
/*     */     } else {
/* 426 */       this.reader.setCursor(debug3);
/* 427 */       throw ERROR_INVALID_VALUE.createWithContext(this.reader, this.id.toString(), debug1.getName(), debug2);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String serialize(BlockState debug0) {
/* 432 */     StringBuilder debug1 = new StringBuilder(Registry.BLOCK.getKey(debug0.getBlock()).toString());
/* 433 */     if (!debug0.getProperties().isEmpty()) {
/* 434 */       debug1.append('[');
/* 435 */       boolean debug2 = false;
/* 436 */       for (UnmodifiableIterator<Map.Entry<Property<?>, Comparable<?>>> unmodifiableIterator = debug0.getValues().entrySet().iterator(); unmodifiableIterator.hasNext(); ) { Map.Entry<Property<?>, Comparable<?>> debug4 = unmodifiableIterator.next();
/* 437 */         if (debug2) {
/* 438 */           debug1.append(',');
/*     */         }
/*     */         
/* 441 */         appendProperty(debug1, (Property<Comparable>)debug4.getKey(), debug4.getValue());
/* 442 */         debug2 = true; }
/*     */       
/* 444 */       debug1.append(']');
/*     */     } 
/* 446 */     return debug1.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T extends Comparable<T>> void appendProperty(StringBuilder debug0, Property<T> debug1, Comparable<?> debug2) {
/* 451 */     debug0.append(debug1.getName());
/* 452 */     debug0.append('=');
/* 453 */     debug0.append(debug1.getName(debug2));
/*     */   }
/*     */   
/*     */   public CompletableFuture<Suggestions> fillSuggestions(SuggestionsBuilder debug1, TagCollection<Block> debug2) {
/* 457 */     return this.suggestions.apply(debug1.createOffset(this.reader.getCursor()), debug2);
/*     */   }
/*     */   
/*     */   public Map<String, String> getVagueProperties() {
/* 461 */     return this.vagueProperties;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\blocks\BlockStateParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */