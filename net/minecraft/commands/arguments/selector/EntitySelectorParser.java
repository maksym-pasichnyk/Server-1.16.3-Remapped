/*     */ package net.minecraft.commands.arguments.selector;
/*     */ 
/*     */ import com.google.common.primitives.Doubles;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.ToDoubleFunction;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.advancements.critereon.MinMaxBounds;
/*     */ import net.minecraft.advancements.critereon.WrappedMinMaxBounds;
/*     */ import net.minecraft.commands.arguments.selector.options.EntitySelectorOptions;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
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
/*     */ public class EntitySelectorParser
/*     */ {
/*  49 */   public static final SimpleCommandExceptionType ERROR_INVALID_NAME_OR_UUID = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.entity.invalid")); public static final DynamicCommandExceptionType ERROR_UNKNOWN_SELECTOR_TYPE; static {
/*  50 */     ERROR_UNKNOWN_SELECTOR_TYPE = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("argument.entity.selector.unknown", new Object[] { debug0 }));
/*  51 */   } public static final SimpleCommandExceptionType ERROR_SELECTORS_NOT_ALLOWED = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.entity.selector.not_allowed"));
/*  52 */   public static final SimpleCommandExceptionType ERROR_MISSING_SELECTOR_TYPE = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.entity.selector.missing"));
/*  53 */   public static final SimpleCommandExceptionType ERROR_EXPECTED_END_OF_OPTIONS = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.entity.options.unterminated")); public static final DynamicCommandExceptionType ERROR_EXPECTED_OPTION_VALUE;
/*  54 */   static { ERROR_EXPECTED_OPTION_VALUE = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("argument.entity.options.valueless", new Object[] { debug0 })); } public static final BiConsumer<Vec3, List<? extends Entity>> ORDER_ARBITRARY = (debug0, debug1) -> {
/*     */     
/*     */     }; public static final BiConsumer<Vec3, List<? extends Entity>> ORDER_NEAREST; static {
/*  57 */     ORDER_NEAREST = ((debug0, debug1) -> debug1.sort(()));
/*  58 */     ORDER_FURTHEST = ((debug0, debug1) -> debug1.sort(()));
/*  59 */     ORDER_RANDOM = ((debug0, debug1) -> Collections.shuffle(debug1));
/*     */     
/*  61 */     SUGGEST_NOTHING = ((debug0, debug1) -> debug0.buildFuture());
/*     */   }
/*     */   public static final BiConsumer<Vec3, List<? extends Entity>> ORDER_FURTHEST; public static final BiConsumer<Vec3, List<? extends Entity>> ORDER_RANDOM; public static final BiFunction<SuggestionsBuilder, Consumer<SuggestionsBuilder>, CompletableFuture<Suggestions>> SUGGEST_NOTHING; private final StringReader reader;
/*     */   private final boolean allowSelectors;
/*     */   private int maxResults;
/*     */   private boolean includesEntities;
/*     */   private boolean worldLimited;
/*  68 */   private MinMaxBounds.Floats distance = MinMaxBounds.Floats.ANY;
/*  69 */   private MinMaxBounds.Ints level = MinMaxBounds.Ints.ANY;
/*     */   @Nullable
/*     */   private Double x;
/*     */   @Nullable
/*     */   private Double y;
/*     */   @Nullable
/*     */   private Double z;
/*     */   @Nullable
/*     */   private Double deltaX;
/*     */   @Nullable
/*     */   private Double deltaY;
/*     */   @Nullable
/*     */   private Double deltaZ;
/*  82 */   private WrappedMinMaxBounds rotX = WrappedMinMaxBounds.ANY;
/*  83 */   private WrappedMinMaxBounds rotY = WrappedMinMaxBounds.ANY;
/*     */   private Predicate<Entity> predicate = debug0 -> true;
/*  85 */   private BiConsumer<Vec3, List<? extends Entity>> order = ORDER_ARBITRARY;
/*     */   private boolean currentEntity;
/*     */   @Nullable
/*     */   private String playerName;
/*     */   private int startPosition;
/*     */   @Nullable
/*     */   private UUID entityUUID;
/*  92 */   private BiFunction<SuggestionsBuilder, Consumer<SuggestionsBuilder>, CompletableFuture<Suggestions>> suggestions = SUGGEST_NOTHING;
/*     */   private boolean hasNameEquals;
/*     */   private boolean hasNameNotEquals;
/*     */   private boolean isLimited;
/*     */   private boolean isSorted;
/*     */   private boolean hasGamemodeEquals;
/*     */   private boolean hasGamemodeNotEquals;
/*     */   private boolean hasTeamEquals;
/*     */   private boolean hasTeamNotEquals;
/*     */   @Nullable
/*     */   private EntityType<?> type;
/*     */   private boolean typeInverse;
/*     */   private boolean hasScores;
/*     */   private boolean hasAdvancements;
/*     */   private boolean usesSelectors;
/*     */   
/*     */   public EntitySelectorParser(StringReader debug1) {
/* 109 */     this(debug1, true);
/*     */   }
/*     */   
/*     */   public EntitySelectorParser(StringReader debug1, boolean debug2) {
/* 113 */     this.reader = debug1;
/* 114 */     this.allowSelectors = debug2;
/*     */   }
/*     */   public EntitySelector getSelector() {
/*     */     AABB debug1;
/*     */     Function<Vec3, Vec3> debug2;
/* 119 */     if (this.deltaX != null || this.deltaY != null || this.deltaZ != null) {
/* 120 */       debug1 = createAabb((this.deltaX == null) ? 0.0D : this.deltaX.doubleValue(), (this.deltaY == null) ? 0.0D : this.deltaY.doubleValue(), (this.deltaZ == null) ? 0.0D : this.deltaZ.doubleValue());
/* 121 */     } else if (this.distance.getMax() != null) {
/* 122 */       float f = ((Float)this.distance.getMax()).floatValue();
/* 123 */       debug1 = new AABB(-f, -f, -f, (f + 1.0F), (f + 1.0F), (f + 1.0F));
/*     */     } else {
/* 125 */       debug1 = null;
/*     */     } 
/*     */     
/* 128 */     if (this.x == null && this.y == null && this.z == null) {
/* 129 */       debug2 = (debug0 -> debug0);
/*     */     } else {
/* 131 */       debug2 = (debug1 -> new Vec3((this.x == null) ? debug1.x : this.x.doubleValue(), (this.y == null) ? debug1.y : this.y.doubleValue(), (this.z == null) ? debug1.z : this.z.doubleValue()));
/*     */     } 
/* 133 */     return new EntitySelector(this.maxResults, this.includesEntities, this.worldLimited, this.predicate, this.distance, debug2, debug1, this.order, this.currentEntity, this.playerName, this.entityUUID, this.type, this.usesSelectors);
/*     */   }
/*     */   
/*     */   private AABB createAabb(double debug1, double debug3, double debug5) {
/* 137 */     boolean debug7 = (debug1 < 0.0D);
/* 138 */     boolean debug8 = (debug3 < 0.0D);
/* 139 */     boolean debug9 = (debug5 < 0.0D);
/* 140 */     double debug10 = debug7 ? debug1 : 0.0D;
/* 141 */     double debug12 = debug8 ? debug3 : 0.0D;
/* 142 */     double debug14 = debug9 ? debug5 : 0.0D;
/* 143 */     double debug16 = (debug7 ? 0.0D : debug1) + 1.0D;
/* 144 */     double debug18 = (debug8 ? 0.0D : debug3) + 1.0D;
/* 145 */     double debug20 = (debug9 ? 0.0D : debug5) + 1.0D;
/* 146 */     return new AABB(debug10, debug12, debug14, debug16, debug18, debug20);
/*     */   }
/*     */   
/*     */   private void finalizePredicates() {
/* 150 */     if (this.rotX != WrappedMinMaxBounds.ANY) {
/* 151 */       this.predicate = this.predicate.and(createRotationPredicate(this.rotX, debug0 -> debug0.xRot));
/*     */     }
/* 153 */     if (this.rotY != WrappedMinMaxBounds.ANY) {
/* 154 */       this.predicate = this.predicate.and(createRotationPredicate(this.rotY, debug0 -> debug0.yRot));
/*     */     }
/* 156 */     if (!this.level.isAny()) {
/* 157 */       this.predicate = this.predicate.and(debug1 -> !(debug1 instanceof ServerPlayer) ? false : this.level.matches(((ServerPlayer)debug1).experienceLevel));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Predicate<Entity> createRotationPredicate(WrappedMinMaxBounds debug1, ToDoubleFunction<Entity> debug2) {
/* 167 */     double debug3 = Mth.wrapDegrees((debug1.getMin() == null) ? 0.0F : debug1.getMin().floatValue());
/* 168 */     double debug5 = Mth.wrapDegrees((debug1.getMax() == null) ? 359.0F : debug1.getMax().floatValue());
/* 169 */     return debug5 -> {
/*     */         double debug6 = Mth.wrapDegrees(debug0.applyAsDouble(debug5));
/*     */         
/* 172 */         return (debug1 > debug3) ? ((debug6 >= debug1 || debug6 <= debug3)) : (
/*     */           
/* 174 */           (debug6 >= debug1 && debug6 <= debug3));
/*     */       };
/*     */   }
/*     */   
/*     */   protected void parseSelector() throws CommandSyntaxException {
/* 179 */     this.usesSelectors = true;
/* 180 */     this.suggestions = this::suggestSelector;
/* 181 */     if (!this.reader.canRead()) {
/* 182 */       throw ERROR_MISSING_SELECTOR_TYPE.createWithContext(this.reader);
/*     */     }
/* 184 */     int debug1 = this.reader.getCursor();
/* 185 */     char debug2 = this.reader.read();
/* 186 */     if (debug2 == 'p') {
/* 187 */       this.maxResults = 1;
/* 188 */       this.includesEntities = false;
/* 189 */       this.order = ORDER_NEAREST;
/* 190 */       limitToType(EntityType.PLAYER);
/* 191 */     } else if (debug2 == 'a') {
/* 192 */       this.maxResults = Integer.MAX_VALUE;
/* 193 */       this.includesEntities = false;
/* 194 */       this.order = ORDER_ARBITRARY;
/* 195 */       limitToType(EntityType.PLAYER);
/* 196 */     } else if (debug2 == 'r') {
/* 197 */       this.maxResults = 1;
/* 198 */       this.includesEntities = false;
/* 199 */       this.order = ORDER_RANDOM;
/* 200 */       limitToType(EntityType.PLAYER);
/* 201 */     } else if (debug2 == 's') {
/* 202 */       this.maxResults = 1;
/* 203 */       this.includesEntities = true;
/* 204 */       this.currentEntity = true;
/* 205 */     } else if (debug2 == 'e') {
/* 206 */       this.maxResults = Integer.MAX_VALUE;
/* 207 */       this.includesEntities = true;
/* 208 */       this.order = ORDER_ARBITRARY;
/* 209 */       this.predicate = Entity::isAlive;
/*     */     } else {
/* 211 */       this.reader.setCursor(debug1);
/* 212 */       throw ERROR_UNKNOWN_SELECTOR_TYPE.createWithContext(this.reader, '@' + String.valueOf(debug2));
/*     */     } 
/*     */     
/* 215 */     this.suggestions = this::suggestOpenOptions;
/* 216 */     if (this.reader.canRead() && this.reader.peek() == '[') {
/* 217 */       this.reader.skip();
/* 218 */       this.suggestions = this::suggestOptionsKeyOrClose;
/* 219 */       parseOptions();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void parseNameOrUUID() throws CommandSyntaxException {
/* 224 */     if (this.reader.canRead()) {
/* 225 */       this.suggestions = this::suggestName;
/*     */     }
/* 227 */     int debug1 = this.reader.getCursor();
/* 228 */     String debug2 = this.reader.readString();
/*     */     
/*     */     try {
/* 231 */       this.entityUUID = UUID.fromString(debug2);
/* 232 */       this.includesEntities = true;
/* 233 */     } catch (IllegalArgumentException debug3) {
/* 234 */       if (debug2.isEmpty() || debug2.length() > 16) {
/* 235 */         this.reader.setCursor(debug1);
/* 236 */         throw ERROR_INVALID_NAME_OR_UUID.createWithContext(this.reader);
/*     */       } 
/* 238 */       this.includesEntities = false;
/* 239 */       this.playerName = debug2;
/*     */     } 
/*     */     
/* 242 */     this.maxResults = 1;
/*     */   }
/*     */   
/*     */   protected void parseOptions() throws CommandSyntaxException {
/* 246 */     this.suggestions = this::suggestOptionsKey;
/* 247 */     this.reader.skipWhitespace();
/* 248 */     while (this.reader.canRead() && this.reader.peek() != ']') {
/* 249 */       this.reader.skipWhitespace();
/* 250 */       int debug1 = this.reader.getCursor();
/* 251 */       String debug2 = this.reader.readString();
/* 252 */       EntitySelectorOptions.Modifier debug3 = EntitySelectorOptions.get(this, debug2, debug1);
/* 253 */       this.reader.skipWhitespace();
/* 254 */       if (!this.reader.canRead() || this.reader.peek() != '=') {
/* 255 */         this.reader.setCursor(debug1);
/* 256 */         throw ERROR_EXPECTED_OPTION_VALUE.createWithContext(this.reader, debug2);
/*     */       } 
/* 258 */       this.reader.skip();
/* 259 */       this.reader.skipWhitespace();
/*     */       
/* 261 */       this.suggestions = SUGGEST_NOTHING;
/* 262 */       debug3.handle(this);
/* 263 */       this.reader.skipWhitespace();
/*     */       
/* 265 */       this.suggestions = this::suggestOptionsNextOrClose;
/* 266 */       if (this.reader.canRead()) {
/* 267 */         if (this.reader.peek() == ',') {
/* 268 */           this.reader.skip();
/* 269 */           this.suggestions = this::suggestOptionsKey; continue;
/* 270 */         }  if (this.reader.peek() == ']') {
/*     */           break;
/*     */         }
/* 273 */         throw ERROR_EXPECTED_END_OF_OPTIONS.createWithContext(this.reader);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 278 */     if (this.reader.canRead()) {
/* 279 */       this.reader.skip();
/* 280 */       this.suggestions = SUGGEST_NOTHING;
/*     */     } else {
/* 282 */       throw ERROR_EXPECTED_END_OF_OPTIONS.createWithContext(this.reader);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean shouldInvertValue() {
/* 287 */     this.reader.skipWhitespace();
/* 288 */     if (this.reader.canRead() && this.reader.peek() == '!') {
/* 289 */       this.reader.skip();
/* 290 */       this.reader.skipWhitespace();
/* 291 */       return true;
/*     */     } 
/* 293 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isTag() {
/* 297 */     this.reader.skipWhitespace();
/* 298 */     if (this.reader.canRead() && this.reader.peek() == '#') {
/* 299 */       this.reader.skip();
/* 300 */       this.reader.skipWhitespace();
/* 301 */       return true;
/*     */     } 
/* 303 */     return false;
/*     */   }
/*     */   
/*     */   public StringReader getReader() {
/* 307 */     return this.reader;
/*     */   }
/*     */   
/*     */   public void addPredicate(Predicate<Entity> debug1) {
/* 311 */     this.predicate = this.predicate.and(debug1);
/*     */   }
/*     */   
/*     */   public void setWorldLimited() {
/* 315 */     this.worldLimited = true;
/*     */   }
/*     */   
/*     */   public MinMaxBounds.Floats getDistance() {
/* 319 */     return this.distance;
/*     */   }
/*     */   
/*     */   public void setDistance(MinMaxBounds.Floats debug1) {
/* 323 */     this.distance = debug1;
/*     */   }
/*     */   
/*     */   public MinMaxBounds.Ints getLevel() {
/* 327 */     return this.level;
/*     */   }
/*     */   
/*     */   public void setLevel(MinMaxBounds.Ints debug1) {
/* 331 */     this.level = debug1;
/*     */   }
/*     */   
/*     */   public WrappedMinMaxBounds getRotX() {
/* 335 */     return this.rotX;
/*     */   }
/*     */   
/*     */   public void setRotX(WrappedMinMaxBounds debug1) {
/* 339 */     this.rotX = debug1;
/*     */   }
/*     */   
/*     */   public WrappedMinMaxBounds getRotY() {
/* 343 */     return this.rotY;
/*     */   }
/*     */   
/*     */   public void setRotY(WrappedMinMaxBounds debug1) {
/* 347 */     this.rotY = debug1;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Double getX() {
/* 352 */     return this.x;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Double getY() {
/* 357 */     return this.y;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Double getZ() {
/* 362 */     return this.z;
/*     */   }
/*     */   
/*     */   public void setX(double debug1) {
/* 366 */     this.x = Double.valueOf(debug1);
/*     */   }
/*     */   
/*     */   public void setY(double debug1) {
/* 370 */     this.y = Double.valueOf(debug1);
/*     */   }
/*     */   
/*     */   public void setZ(double debug1) {
/* 374 */     this.z = Double.valueOf(debug1);
/*     */   }
/*     */   
/*     */   public void setDeltaX(double debug1) {
/* 378 */     this.deltaX = Double.valueOf(debug1);
/*     */   }
/*     */   
/*     */   public void setDeltaY(double debug1) {
/* 382 */     this.deltaY = Double.valueOf(debug1);
/*     */   }
/*     */   
/*     */   public void setDeltaZ(double debug1) {
/* 386 */     this.deltaZ = Double.valueOf(debug1);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Double getDeltaX() {
/* 391 */     return this.deltaX;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Double getDeltaY() {
/* 396 */     return this.deltaY;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Double getDeltaZ() {
/* 401 */     return this.deltaZ;
/*     */   }
/*     */   
/*     */   public void setMaxResults(int debug1) {
/* 405 */     this.maxResults = debug1;
/*     */   }
/*     */   
/*     */   public void setIncludesEntities(boolean debug1) {
/* 409 */     this.includesEntities = debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOrder(BiConsumer<Vec3, List<? extends Entity>> debug1) {
/* 417 */     this.order = debug1;
/*     */   }
/*     */   
/*     */   public EntitySelector parse() throws CommandSyntaxException {
/* 421 */     this.startPosition = this.reader.getCursor();
/* 422 */     this.suggestions = this::suggestNameOrSelector;
/* 423 */     if (this.reader.canRead() && this.reader.peek() == '@') {
/* 424 */       if (!this.allowSelectors) {
/* 425 */         throw ERROR_SELECTORS_NOT_ALLOWED.createWithContext(this.reader);
/*     */       }
/* 427 */       this.reader.skip();
/* 428 */       parseSelector();
/*     */     } else {
/* 430 */       parseNameOrUUID();
/*     */     } 
/* 432 */     finalizePredicates();
/* 433 */     return getSelector();
/*     */   }
/*     */   
/*     */   private static void fillSelectorSuggestions(SuggestionsBuilder debug0) {
/* 437 */     debug0.suggest("@p", (Message)new TranslatableComponent("argument.entity.selector.nearestPlayer"));
/* 438 */     debug0.suggest("@a", (Message)new TranslatableComponent("argument.entity.selector.allPlayers"));
/* 439 */     debug0.suggest("@r", (Message)new TranslatableComponent("argument.entity.selector.randomPlayer"));
/* 440 */     debug0.suggest("@s", (Message)new TranslatableComponent("argument.entity.selector.self"));
/* 441 */     debug0.suggest("@e", (Message)new TranslatableComponent("argument.entity.selector.allEntities"));
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestNameOrSelector(SuggestionsBuilder debug1, Consumer<SuggestionsBuilder> debug2) {
/* 445 */     debug2.accept(debug1);
/* 446 */     if (this.allowSelectors) {
/* 447 */       fillSelectorSuggestions(debug1);
/*     */     }
/* 449 */     return debug1.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestName(SuggestionsBuilder debug1, Consumer<SuggestionsBuilder> debug2) {
/* 453 */     SuggestionsBuilder debug3 = debug1.createOffset(this.startPosition);
/* 454 */     debug2.accept(debug3);
/* 455 */     return debug1.add(debug3).buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestSelector(SuggestionsBuilder debug1, Consumer<SuggestionsBuilder> debug2) {
/* 459 */     SuggestionsBuilder debug3 = debug1.createOffset(debug1.getStart() - 1);
/* 460 */     fillSelectorSuggestions(debug3);
/* 461 */     debug1.add(debug3);
/* 462 */     return debug1.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestOpenOptions(SuggestionsBuilder debug1, Consumer<SuggestionsBuilder> debug2) {
/* 466 */     debug1.suggest(String.valueOf('['));
/* 467 */     return debug1.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestOptionsKeyOrClose(SuggestionsBuilder debug1, Consumer<SuggestionsBuilder> debug2) {
/* 471 */     debug1.suggest(String.valueOf(']'));
/* 472 */     EntitySelectorOptions.suggestNames(this, debug1);
/* 473 */     return debug1.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestOptionsKey(SuggestionsBuilder debug1, Consumer<SuggestionsBuilder> debug2) {
/* 477 */     EntitySelectorOptions.suggestNames(this, debug1);
/* 478 */     return debug1.buildFuture();
/*     */   }
/*     */   
/*     */   private CompletableFuture<Suggestions> suggestOptionsNextOrClose(SuggestionsBuilder debug1, Consumer<SuggestionsBuilder> debug2) {
/* 482 */     debug1.suggest(String.valueOf(','));
/* 483 */     debug1.suggest(String.valueOf(']'));
/* 484 */     return debug1.buildFuture();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCurrentEntity() {
/* 493 */     return this.currentEntity;
/*     */   }
/*     */   
/*     */   public void setSuggestions(BiFunction<SuggestionsBuilder, Consumer<SuggestionsBuilder>, CompletableFuture<Suggestions>> debug1) {
/* 497 */     this.suggestions = debug1;
/*     */   }
/*     */   
/*     */   public CompletableFuture<Suggestions> fillSuggestions(SuggestionsBuilder debug1, Consumer<SuggestionsBuilder> debug2) {
/* 501 */     return this.suggestions.apply(debug1.createOffset(this.reader.getCursor()), debug2);
/*     */   }
/*     */   
/*     */   public boolean hasNameEquals() {
/* 505 */     return this.hasNameEquals;
/*     */   }
/*     */   
/*     */   public void setHasNameEquals(boolean debug1) {
/* 509 */     this.hasNameEquals = debug1;
/*     */   }
/*     */   
/*     */   public boolean hasNameNotEquals() {
/* 513 */     return this.hasNameNotEquals;
/*     */   }
/*     */   
/*     */   public void setHasNameNotEquals(boolean debug1) {
/* 517 */     this.hasNameNotEquals = debug1;
/*     */   }
/*     */   
/*     */   public boolean isLimited() {
/* 521 */     return this.isLimited;
/*     */   }
/*     */   
/*     */   public void setLimited(boolean debug1) {
/* 525 */     this.isLimited = debug1;
/*     */   }
/*     */   
/*     */   public boolean isSorted() {
/* 529 */     return this.isSorted;
/*     */   }
/*     */   
/*     */   public void setSorted(boolean debug1) {
/* 533 */     this.isSorted = debug1;
/*     */   }
/*     */   
/*     */   public boolean hasGamemodeEquals() {
/* 537 */     return this.hasGamemodeEquals;
/*     */   }
/*     */   
/*     */   public void setHasGamemodeEquals(boolean debug1) {
/* 541 */     this.hasGamemodeEquals = debug1;
/*     */   }
/*     */   
/*     */   public boolean hasGamemodeNotEquals() {
/* 545 */     return this.hasGamemodeNotEquals;
/*     */   }
/*     */   
/*     */   public void setHasGamemodeNotEquals(boolean debug1) {
/* 549 */     this.hasGamemodeNotEquals = debug1;
/*     */   }
/*     */   
/*     */   public boolean hasTeamEquals() {
/* 553 */     return this.hasTeamEquals;
/*     */   }
/*     */   
/*     */   public void setHasTeamEquals(boolean debug1) {
/* 557 */     this.hasTeamEquals = debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHasTeamNotEquals(boolean debug1) {
/* 565 */     this.hasTeamNotEquals = debug1;
/*     */   }
/*     */   
/*     */   public void limitToType(EntityType<?> debug1) {
/* 569 */     this.type = debug1;
/*     */   }
/*     */   
/*     */   public void setTypeLimitedInversely() {
/* 573 */     this.typeInverse = true;
/*     */   }
/*     */   
/*     */   public boolean isTypeLimited() {
/* 577 */     return (this.type != null);
/*     */   }
/*     */   
/*     */   public boolean isTypeLimitedInversely() {
/* 581 */     return this.typeInverse;
/*     */   }
/*     */   
/*     */   public boolean hasScores() {
/* 585 */     return this.hasScores;
/*     */   }
/*     */   
/*     */   public void setHasScores(boolean debug1) {
/* 589 */     this.hasScores = debug1;
/*     */   }
/*     */   
/*     */   public boolean hasAdvancements() {
/* 593 */     return this.hasAdvancements;
/*     */   }
/*     */   
/*     */   public void setHasAdvancements(boolean debug1) {
/* 597 */     this.hasAdvancements = debug1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\selector\EntitySelectorParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */