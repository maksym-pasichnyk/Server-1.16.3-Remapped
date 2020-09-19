/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.TextComponent;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class CompoundTag implements Tag {
/*     */   static {
/*  33 */     CODEC = Codec.PASSTHROUGH.comapFlatMap(debug0 -> {
/*     */           Tag debug1 = (Tag)debug0.convert(NbtOps.INSTANCE).getValue();
/*     */           return (debug1 instanceof CompoundTag) ? DataResult.success(debug1) : DataResult.error("Not a compound tag: " + debug1);
/*     */         }debug0 -> new Dynamic(NbtOps.INSTANCE, debug0));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final Codec<CompoundTag> CODEC;
/*     */ 
/*     */   
/*  44 */   private static final Logger LOGGER = LogManager.getLogger();
/*  45 */   private static final Pattern SIMPLE_VALUE = Pattern.compile("[A-Za-z0-9._+-]+");
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
/*  69 */   public static final TagType<CompoundTag> TYPE = new TagType<CompoundTag>()
/*     */     {
/*     */       public CompoundTag load(DataInput debug1, int debug2, NbtAccounter debug3) throws IOException {
/*  72 */         debug3.accountBits(384L);
/*     */         
/*  74 */         if (debug2 > 512) {
/*  75 */           throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
/*     */         }
/*  77 */         Map<String, Tag> debug4 = Maps.newHashMap();
/*     */         byte debug5;
/*  79 */         while ((debug5 = CompoundTag.readNamedTagType(debug1, debug3)) != 0) {
/*  80 */           String debug6 = CompoundTag.readNamedTagName(debug1, debug3);
/*  81 */           debug3.accountBits((224 + 16 * debug6.length()));
/*     */           
/*  83 */           Tag debug7 = CompoundTag.readNamedTagData(TagTypes.getType(debug5), debug6, debug1, debug2 + 1, debug3);
/*  84 */           if (debug4.put(debug6, debug7) != null) {
/*  85 */             debug3.accountBits(288L);
/*     */           }
/*     */         } 
/*  88 */         return new CompoundTag(debug4);
/*     */       }
/*     */ 
/*     */       
/*     */       public String getName() {
/*  93 */         return "COMPOUND";
/*     */       }
/*     */ 
/*     */       
/*     */       public String getPrettyName() {
/*  98 */         return "TAG_Compound";
/*     */       }
/*     */     };
/*     */   
/*     */   private final Map<String, Tag> tags;
/*     */   
/*     */   protected CompoundTag(Map<String, Tag> debug1) {
/* 105 */     this.tags = debug1;
/*     */   }
/*     */   
/*     */   public CompoundTag() {
/* 109 */     this(Maps.newHashMap());
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(DataOutput debug1) throws IOException {
/* 114 */     for (String debug3 : this.tags.keySet()) {
/* 115 */       Tag debug4 = this.tags.get(debug3);
/* 116 */       writeNamedTag(debug3, debug4, debug1);
/*     */     } 
/* 118 */     debug1.writeByte(0);
/*     */   }
/*     */   
/*     */   public Set<String> getAllKeys() {
/* 122 */     return this.tags.keySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getId() {
/* 127 */     return 10;
/*     */   }
/*     */ 
/*     */   
/*     */   public TagType<CompoundTag> getType() {
/* 132 */     return TYPE;
/*     */   }
/*     */   
/*     */   public int size() {
/* 136 */     return this.tags.size();
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Tag put(String debug1, Tag debug2) {
/* 141 */     return this.tags.put(debug1, debug2);
/*     */   }
/*     */   
/*     */   public void putByte(String debug1, byte debug2) {
/* 145 */     this.tags.put(debug1, ByteTag.valueOf(debug2));
/*     */   }
/*     */   
/*     */   public void putShort(String debug1, short debug2) {
/* 149 */     this.tags.put(debug1, ShortTag.valueOf(debug2));
/*     */   }
/*     */   
/*     */   public void putInt(String debug1, int debug2) {
/* 153 */     this.tags.put(debug1, IntTag.valueOf(debug2));
/*     */   }
/*     */   
/*     */   public void putLong(String debug1, long debug2) {
/* 157 */     this.tags.put(debug1, LongTag.valueOf(debug2));
/*     */   }
/*     */   
/*     */   public void putUUID(String debug1, UUID debug2) {
/* 161 */     this.tags.put(debug1, NbtUtils.createUUID(debug2));
/*     */   }
/*     */   
/*     */   public UUID getUUID(String debug1) {
/* 165 */     return NbtUtils.loadUUID(get(debug1));
/*     */   }
/*     */   
/*     */   public boolean hasUUID(String debug1) {
/* 169 */     Tag debug2 = get(debug1);
/* 170 */     return (debug2 != null && debug2.getType() == IntArrayTag.TYPE && (((IntArrayTag)debug2).getAsIntArray()).length == 4);
/*     */   }
/*     */   
/*     */   public void putFloat(String debug1, float debug2) {
/* 174 */     this.tags.put(debug1, FloatTag.valueOf(debug2));
/*     */   }
/*     */   
/*     */   public void putDouble(String debug1, double debug2) {
/* 178 */     this.tags.put(debug1, DoubleTag.valueOf(debug2));
/*     */   }
/*     */   
/*     */   public void putString(String debug1, String debug2) {
/* 182 */     this.tags.put(debug1, StringTag.valueOf(debug2));
/*     */   }
/*     */   
/*     */   public void putByteArray(String debug1, byte[] debug2) {
/* 186 */     this.tags.put(debug1, new ByteArrayTag(debug2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void putIntArray(String debug1, int[] debug2) {
/* 194 */     this.tags.put(debug1, new IntArrayTag(debug2));
/*     */   }
/*     */   
/*     */   public void putIntArray(String debug1, List<Integer> debug2) {
/* 198 */     this.tags.put(debug1, new IntArrayTag(debug2));
/*     */   }
/*     */   
/*     */   public void putLongArray(String debug1, long[] debug2) {
/* 202 */     this.tags.put(debug1, new LongArrayTag(debug2));
/*     */   }
/*     */   
/*     */   public void putLongArray(String debug1, List<Long> debug2) {
/* 206 */     this.tags.put(debug1, new LongArrayTag(debug2));
/*     */   }
/*     */   
/*     */   public void putBoolean(String debug1, boolean debug2) {
/* 210 */     this.tags.put(debug1, ByteTag.valueOf(debug2));
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Tag get(String debug1) {
/* 215 */     return this.tags.get(debug1);
/*     */   }
/*     */   
/*     */   public byte getTagType(String debug1) {
/* 219 */     Tag debug2 = this.tags.get(debug1);
/* 220 */     if (debug2 == null) {
/* 221 */       return 0;
/*     */     }
/* 223 */     return debug2.getId();
/*     */   }
/*     */   
/*     */   public boolean contains(String debug1) {
/* 227 */     return this.tags.containsKey(debug1);
/*     */   }
/*     */   
/*     */   public boolean contains(String debug1, int debug2) {
/* 231 */     int debug3 = getTagType(debug1);
/* 232 */     if (debug3 == debug2) {
/* 233 */       return true;
/*     */     }
/* 235 */     if (debug2 == 99) {
/* 236 */       return (debug3 == 1 || debug3 == 2 || debug3 == 3 || debug3 == 4 || debug3 == 5 || debug3 == 6);
/*     */     }
/*     */     
/* 239 */     return false;
/*     */   }
/*     */   
/*     */   public byte getByte(String debug1) {
/*     */     try {
/* 244 */       if (contains(debug1, 99)) {
/* 245 */         return ((NumericTag)this.tags.get(debug1)).getAsByte();
/*     */       }
/* 247 */     } catch (ClassCastException classCastException) {}
/*     */     
/* 249 */     return 0;
/*     */   }
/*     */   
/*     */   public short getShort(String debug1) {
/*     */     try {
/* 254 */       if (contains(debug1, 99)) {
/* 255 */         return ((NumericTag)this.tags.get(debug1)).getAsShort();
/*     */       }
/* 257 */     } catch (ClassCastException classCastException) {}
/*     */     
/* 259 */     return 0;
/*     */   }
/*     */   
/*     */   public int getInt(String debug1) {
/*     */     try {
/* 264 */       if (contains(debug1, 99)) {
/* 265 */         return ((NumericTag)this.tags.get(debug1)).getAsInt();
/*     */       }
/* 267 */     } catch (ClassCastException classCastException) {}
/*     */     
/* 269 */     return 0;
/*     */   }
/*     */   
/*     */   public long getLong(String debug1) {
/*     */     try {
/* 274 */       if (contains(debug1, 99)) {
/* 275 */         return ((NumericTag)this.tags.get(debug1)).getAsLong();
/*     */       }
/* 277 */     } catch (ClassCastException classCastException) {}
/*     */     
/* 279 */     return 0L;
/*     */   }
/*     */   
/*     */   public float getFloat(String debug1) {
/*     */     try {
/* 284 */       if (contains(debug1, 99)) {
/* 285 */         return ((NumericTag)this.tags.get(debug1)).getAsFloat();
/*     */       }
/* 287 */     } catch (ClassCastException classCastException) {}
/*     */     
/* 289 */     return 0.0F;
/*     */   }
/*     */   
/*     */   public double getDouble(String debug1) {
/*     */     try {
/* 294 */       if (contains(debug1, 99)) {
/* 295 */         return ((NumericTag)this.tags.get(debug1)).getAsDouble();
/*     */       }
/* 297 */     } catch (ClassCastException classCastException) {}
/*     */     
/* 299 */     return 0.0D;
/*     */   }
/*     */   
/*     */   public String getString(String debug1) {
/*     */     try {
/* 304 */       if (contains(debug1, 8)) {
/* 305 */         return ((Tag)this.tags.get(debug1)).getAsString();
/*     */       }
/* 307 */     } catch (ClassCastException classCastException) {}
/*     */     
/* 309 */     return "";
/*     */   }
/*     */   
/*     */   public byte[] getByteArray(String debug1) {
/*     */     try {
/* 314 */       if (contains(debug1, 7)) {
/* 315 */         return ((ByteArrayTag)this.tags.get(debug1)).getAsByteArray();
/*     */       }
/* 317 */     } catch (ClassCastException debug2) {
/* 318 */       throw new ReportedException(createReport(debug1, ByteArrayTag.TYPE, debug2));
/*     */     } 
/* 320 */     return new byte[0];
/*     */   }
/*     */   
/*     */   public int[] getIntArray(String debug1) {
/*     */     try {
/* 325 */       if (contains(debug1, 11)) {
/* 326 */         return ((IntArrayTag)this.tags.get(debug1)).getAsIntArray();
/*     */       }
/* 328 */     } catch (ClassCastException debug2) {
/* 329 */       throw new ReportedException(createReport(debug1, IntArrayTag.TYPE, debug2));
/*     */     } 
/* 331 */     return new int[0];
/*     */   }
/*     */   
/*     */   public long[] getLongArray(String debug1) {
/*     */     try {
/* 336 */       if (contains(debug1, 12)) {
/* 337 */         return ((LongArrayTag)this.tags.get(debug1)).getAsLongArray();
/*     */       }
/* 339 */     } catch (ClassCastException debug2) {
/* 340 */       throw new ReportedException(createReport(debug1, LongArrayTag.TYPE, debug2));
/*     */     } 
/* 342 */     return new long[0];
/*     */   }
/*     */   
/*     */   public CompoundTag getCompound(String debug1) {
/*     */     try {
/* 347 */       if (contains(debug1, 10)) {
/* 348 */         return (CompoundTag)this.tags.get(debug1);
/*     */       }
/* 350 */     } catch (ClassCastException debug2) {
/* 351 */       throw new ReportedException(createReport(debug1, TYPE, debug2));
/*     */     } 
/* 353 */     return new CompoundTag();
/*     */   }
/*     */   
/*     */   public ListTag getList(String debug1, int debug2) {
/*     */     try {
/* 358 */       if (getTagType(debug1) == 9) {
/* 359 */         ListTag debug3 = (ListTag)this.tags.get(debug1);
/* 360 */         if (debug3.isEmpty() || debug3.getElementType() == debug2) {
/* 361 */           return debug3;
/*     */         }
/* 363 */         return new ListTag();
/*     */       } 
/* 365 */     } catch (ClassCastException debug3) {
/* 366 */       throw new ReportedException(createReport(debug1, ListTag.TYPE, debug3));
/*     */     } 
/* 368 */     return new ListTag();
/*     */   }
/*     */   
/*     */   public boolean getBoolean(String debug1) {
/* 372 */     return (getByte(debug1) != 0);
/*     */   }
/*     */   
/*     */   public void remove(String debug1) {
/* 376 */     this.tags.remove(debug1);
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
/*     */   public String toString() {
/* 396 */     StringBuilder debug1 = new StringBuilder("{");
/*     */     
/* 398 */     Collection<String> debug2 = this.tags.keySet();
/* 399 */     if (LOGGER.isDebugEnabled()) {
/* 400 */       List<String> debug3 = Lists.newArrayList(this.tags.keySet());
/* 401 */       Collections.sort(debug3);
/* 402 */       debug2 = debug3;
/*     */     } 
/* 404 */     for (String debug4 : debug2) {
/* 405 */       if (debug1.length() != 1) {
/* 406 */         debug1.append(',');
/*     */       }
/* 408 */       debug1.append(handleEscape(debug4)).append(':').append(this.tags.get(debug4));
/*     */     } 
/*     */     
/* 411 */     return debug1.append('}').toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 416 */     return this.tags.isEmpty();
/*     */   }
/*     */   
/*     */   private CrashReport createReport(String debug1, TagType<?> debug2, ClassCastException debug3) {
/* 420 */     CrashReport debug4 = CrashReport.forThrowable(debug3, "Reading NBT data");
/* 421 */     CrashReportCategory debug5 = debug4.addCategory("Corrupt NBT tag", 1);
/*     */     
/* 423 */     debug5.setDetail("Tag type found", () -> ((Tag)this.tags.get(debug1)).getType().getName());
/* 424 */     debug5.setDetail("Tag type expected", debug2::getName);
/* 425 */     debug5.setDetail("Tag name", debug1);
/*     */     
/* 427 */     return debug4;
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag copy() {
/* 432 */     Map<String, Tag> debug1 = Maps.newHashMap(Maps.transformValues(this.tags, Tag::copy));
/* 433 */     return new CompoundTag(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object debug1) {
/* 438 */     if (this == debug1) {
/* 439 */       return true;
/*     */     }
/*     */     
/* 442 */     return (debug1 instanceof CompoundTag && Objects.equals(this.tags, ((CompoundTag)debug1).tags));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 447 */     return this.tags.hashCode();
/*     */   }
/*     */   
/*     */   private static void writeNamedTag(String debug0, Tag debug1, DataOutput debug2) throws IOException {
/* 451 */     debug2.writeByte(debug1.getId());
/* 452 */     if (debug1.getId() == 0) {
/*     */       return;
/*     */     }
/*     */     
/* 456 */     debug2.writeUTF(debug0);
/*     */     
/* 458 */     debug1.write(debug2);
/*     */   }
/*     */   
/*     */   private static byte readNamedTagType(DataInput debug0, NbtAccounter debug1) throws IOException {
/* 462 */     return debug0.readByte();
/*     */   }
/*     */   
/*     */   private static String readNamedTagName(DataInput debug0, NbtAccounter debug1) throws IOException {
/* 466 */     return debug0.readUTF();
/*     */   }
/*     */   
/*     */   private static Tag readNamedTagData(TagType<?> debug0, String debug1, DataInput debug2, int debug3, NbtAccounter debug4) {
/*     */     try {
/* 471 */       return (Tag)debug0.load(debug2, debug3, debug4);
/* 472 */     } catch (IOException debug5) {
/* 473 */       CrashReport debug6 = CrashReport.forThrowable(debug5, "Loading NBT data");
/* 474 */       CrashReportCategory debug7 = debug6.addCategory("NBT Tag");
/* 475 */       debug7.setDetail("Tag name", debug1);
/* 476 */       debug7.setDetail("Tag type", debug0.getName());
/* 477 */       throw new ReportedException(debug6);
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
/*     */   public CompoundTag merge(CompoundTag debug1) {
/* 495 */     for (String debug3 : debug1.tags.keySet()) {
/* 496 */       Tag debug4 = debug1.tags.get(debug3);
/*     */ 
/*     */       
/* 499 */       if (debug4.getId() == 10) {
/* 500 */         if (contains(debug3, 10)) {
/* 501 */           CompoundTag debug5 = getCompound(debug3);
/* 502 */           debug5.merge((CompoundTag)debug4); continue;
/*     */         } 
/* 504 */         put(debug3, debug4.copy());
/*     */         continue;
/*     */       } 
/* 507 */       put(debug3, debug4.copy());
/*     */     } 
/*     */     
/* 510 */     return this;
/*     */   }
/*     */   
/*     */   protected static String handleEscape(String debug0) {
/* 514 */     if (SIMPLE_VALUE.matcher(debug0).matches()) {
/* 515 */       return debug0;
/*     */     }
/*     */     
/* 518 */     return StringTag.quoteAndEscape(debug0);
/*     */   }
/*     */   
/*     */   protected static Component handleEscapePretty(String debug0) {
/* 522 */     if (SIMPLE_VALUE.matcher(debug0).matches()) {
/* 523 */       return (Component)(new TextComponent(debug0)).withStyle(SYNTAX_HIGHLIGHTING_KEY);
/*     */     }
/*     */     
/* 526 */     String debug1 = StringTag.quoteAndEscape(debug0);
/* 527 */     String debug2 = debug1.substring(0, 1);
/* 528 */     MutableComponent mutableComponent = (new TextComponent(debug1.substring(1, debug1.length() - 1))).withStyle(SYNTAX_HIGHLIGHTING_KEY);
/* 529 */     return (Component)(new TextComponent(debug2)).append((Component)mutableComponent).append(debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getPrettyDisplay(String debug1, int debug2) {
/* 534 */     if (this.tags.isEmpty()) {
/* 535 */       return (Component)new TextComponent("{}");
/*     */     }
/*     */     
/* 538 */     TextComponent textComponent = new TextComponent("{");
/*     */     
/* 540 */     Collection<String> debug4 = this.tags.keySet();
/* 541 */     if (LOGGER.isDebugEnabled()) {
/* 542 */       List<String> list = Lists.newArrayList(this.tags.keySet());
/* 543 */       Collections.sort(list);
/* 544 */       debug4 = list;
/*     */     } 
/*     */     
/* 547 */     if (!debug1.isEmpty()) {
/* 548 */       textComponent.append("\n");
/*     */     }
/*     */     
/* 551 */     for (Iterator<String> debug5 = debug4.iterator(); debug5.hasNext(); ) {
/* 552 */       String debug6 = debug5.next();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 557 */       MutableComponent debug7 = (new TextComponent(Strings.repeat(debug1, debug2 + 1))).append(handleEscapePretty(debug6)).append(String.valueOf(':')).append(" ").append(((Tag)this.tags.get(debug6)).getPrettyDisplay(debug1, debug2 + 1));
/*     */       
/* 559 */       if (debug5.hasNext()) {
/* 560 */         debug7.append(String.valueOf(',')).append(debug1.isEmpty() ? " " : "\n");
/*     */       }
/* 562 */       textComponent.append((Component)debug7);
/*     */     } 
/* 564 */     if (!debug1.isEmpty()) {
/* 565 */       textComponent.append("\n").append(Strings.repeat(debug1, debug2));
/*     */     }
/* 567 */     textComponent.append("}");
/* 568 */     return (Component)textComponent;
/*     */   }
/*     */   
/*     */   protected Map<String, Tag> entries() {
/* 572 */     return Collections.unmodifiableMap(this.tags);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\nbt\CompoundTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */