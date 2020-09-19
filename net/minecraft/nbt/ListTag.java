/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.Lists;
/*     */ import it.unimi.dsi.fastutil.bytes.ByteOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.bytes.ByteSet;
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TextComponent;
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
/*     */ public class ListTag
/*     */   extends CollectionTag<Tag>
/*     */ {
/*  32 */   public static final TagType<ListTag> TYPE = new TagType<ListTag>()
/*     */     {
/*     */       public ListTag load(DataInput debug1, int debug2, NbtAccounter debug3) throws IOException {
/*  35 */         debug3.accountBits(296L);
/*     */         
/*  37 */         if (debug2 > 512) {
/*  38 */           throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
/*     */         }
/*  40 */         byte debug4 = debug1.readByte();
/*  41 */         int debug5 = debug1.readInt();
/*  42 */         if (debug4 == 0 && debug5 > 0) {
/*  43 */           throw new RuntimeException("Missing type on ListTag");
/*     */         }
/*  45 */         debug3.accountBits(32L * debug5);
/*  46 */         TagType<?> debug6 = TagTypes.getType(debug4);
/*  47 */         List<Tag> debug7 = Lists.newArrayListWithCapacity(debug5);
/*  48 */         for (int debug8 = 0; debug8 < debug5; debug8++) {
/*  49 */           debug7.add((Tag)debug6.load(debug1, debug2 + 1, debug3));
/*     */         }
/*  51 */         return new ListTag(debug7, debug4);
/*     */       }
/*     */ 
/*     */       
/*     */       public String getName() {
/*  56 */         return "LIST";
/*     */       }
/*     */ 
/*     */       
/*     */       public String getPrettyName() {
/*  61 */         return "TAG_List";
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*  66 */   private static final ByteSet INLINE_ELEMENT_TYPES = (ByteSet)new ByteOpenHashSet(Arrays.asList(new Byte[] { Byte.valueOf((byte)1), Byte.valueOf((byte)2), Byte.valueOf((byte)3), Byte.valueOf((byte)4), Byte.valueOf((byte)5), Byte.valueOf((byte)6) }));
/*     */   
/*     */   private final List<Tag> list;
/*     */   private byte type;
/*     */   
/*     */   private ListTag(List<Tag> debug1, byte debug2) {
/*  72 */     this.list = debug1;
/*  73 */     this.type = debug2;
/*     */   }
/*     */   
/*     */   public ListTag() {
/*  77 */     this(Lists.newArrayList(), (byte)0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(DataOutput debug1) throws IOException {
/*  82 */     if (this.list.isEmpty()) {
/*  83 */       this.type = 0;
/*     */     } else {
/*  85 */       this.type = ((Tag)this.list.get(0)).getId();
/*     */     } 
/*     */     
/*  88 */     debug1.writeByte(this.type);
/*  89 */     debug1.writeInt(this.list.size());
/*  90 */     for (Tag debug3 : this.list) {
/*  91 */       debug3.write(debug1);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getId() {
/*  97 */     return 9;
/*     */   }
/*     */ 
/*     */   
/*     */   public TagType<ListTag> getType() {
/* 102 */     return TYPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 107 */     StringBuilder debug1 = new StringBuilder("[");
/* 108 */     for (int debug2 = 0; debug2 < this.list.size(); debug2++) {
/* 109 */       if (debug2 != 0) {
/* 110 */         debug1.append(',');
/*     */       }
/* 112 */       debug1.append(this.list.get(debug2));
/*     */     } 
/* 114 */     return debug1.append(']').toString();
/*     */   }
/*     */   
/*     */   private void updateTypeAfterRemove() {
/* 118 */     if (this.list.isEmpty()) {
/* 119 */       this.type = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Tag remove(int debug1) {
/* 125 */     Tag debug2 = this.list.remove(debug1);
/* 126 */     updateTypeAfterRemove();
/* 127 */     return debug2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 138 */     return this.list.isEmpty();
/*     */   }
/*     */   
/*     */   public CompoundTag getCompound(int debug1) {
/* 142 */     if (debug1 >= 0 && debug1 < this.list.size()) {
/* 143 */       Tag debug2 = this.list.get(debug1);
/* 144 */       if (debug2.getId() == 10) {
/* 145 */         return (CompoundTag)debug2;
/*     */       }
/*     */     } 
/* 148 */     return new CompoundTag();
/*     */   }
/*     */   
/*     */   public ListTag getList(int debug1) {
/* 152 */     if (debug1 >= 0 && debug1 < this.list.size()) {
/* 153 */       Tag debug2 = this.list.get(debug1);
/* 154 */       if (debug2.getId() == 9) {
/* 155 */         return (ListTag)debug2;
/*     */       }
/*     */     } 
/* 158 */     return new ListTag();
/*     */   }
/*     */   
/*     */   public short getShort(int debug1) {
/* 162 */     if (debug1 >= 0 && debug1 < this.list.size()) {
/* 163 */       Tag debug2 = this.list.get(debug1);
/* 164 */       if (debug2.getId() == 2) {
/* 165 */         return ((ShortTag)debug2).getAsShort();
/*     */       }
/*     */     } 
/* 168 */     return 0;
/*     */   }
/*     */   
/*     */   public int getInt(int debug1) {
/* 172 */     if (debug1 >= 0 && debug1 < this.list.size()) {
/* 173 */       Tag debug2 = this.list.get(debug1);
/* 174 */       if (debug2.getId() == 3) {
/* 175 */         return ((IntTag)debug2).getAsInt();
/*     */       }
/*     */     } 
/* 178 */     return 0;
/*     */   }
/*     */   
/*     */   public int[] getIntArray(int debug1) {
/* 182 */     if (debug1 >= 0 && debug1 < this.list.size()) {
/* 183 */       Tag debug2 = this.list.get(debug1);
/* 184 */       if (debug2.getId() == 11) {
/* 185 */         return ((IntArrayTag)debug2).getAsIntArray();
/*     */       }
/*     */     } 
/* 188 */     return new int[0];
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
/*     */   public double getDouble(int debug1) {
/* 202 */     if (debug1 >= 0 && debug1 < this.list.size()) {
/* 203 */       Tag debug2 = this.list.get(debug1);
/* 204 */       if (debug2.getId() == 6) {
/* 205 */         return ((DoubleTag)debug2).getAsDouble();
/*     */       }
/*     */     } 
/* 208 */     return 0.0D;
/*     */   }
/*     */   
/*     */   public float getFloat(int debug1) {
/* 212 */     if (debug1 >= 0 && debug1 < this.list.size()) {
/* 213 */       Tag debug2 = this.list.get(debug1);
/* 214 */       if (debug2.getId() == 5) {
/* 215 */         return ((FloatTag)debug2).getAsFloat();
/*     */       }
/*     */     } 
/* 218 */     return 0.0F;
/*     */   }
/*     */   
/*     */   public String getString(int debug1) {
/* 222 */     if (debug1 < 0 || debug1 >= this.list.size()) {
/* 223 */       return "";
/*     */     }
/* 225 */     Tag debug2 = this.list.get(debug1);
/* 226 */     if (debug2.getId() == 8) {
/* 227 */       return debug2.getAsString();
/*     */     }
/* 229 */     return debug2.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 234 */     return this.list.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public Tag get(int debug1) {
/* 239 */     return this.list.get(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public Tag set(int debug1, Tag debug2) {
/* 244 */     Tag debug3 = get(debug1);
/* 245 */     if (!setTag(debug1, debug2)) {
/* 246 */       throw new UnsupportedOperationException(String.format("Trying to add tag of type %d to list of %d", new Object[] { Byte.valueOf(debug2.getId()), Byte.valueOf(this.type) }));
/*     */     }
/* 248 */     return debug3;
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(int debug1, Tag debug2) {
/* 253 */     if (!addTag(debug1, debug2)) {
/* 254 */       throw new UnsupportedOperationException(String.format("Trying to add tag of type %d to list of %d", new Object[] { Byte.valueOf(debug2.getId()), Byte.valueOf(this.type) }));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setTag(int debug1, Tag debug2) {
/* 260 */     if (updateType(debug2)) {
/* 261 */       this.list.set(debug1, debug2);
/* 262 */       return true;
/*     */     } 
/* 264 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addTag(int debug1, Tag debug2) {
/* 269 */     if (updateType(debug2)) {
/* 270 */       this.list.add(debug1, debug2);
/* 271 */       return true;
/*     */     } 
/* 273 */     return false;
/*     */   }
/*     */   
/*     */   private boolean updateType(Tag debug1) {
/* 277 */     if (debug1.getId() == 0) {
/* 278 */       return false;
/*     */     }
/* 280 */     if (this.type == 0) {
/* 281 */       this.type = debug1.getId();
/* 282 */       return true;
/*     */     } 
/* 284 */     return (this.type == debug1.getId());
/*     */   }
/*     */ 
/*     */   
/*     */   public ListTag copy() {
/* 289 */     Iterable<Tag> debug1 = TagTypes.getType(this.type).isValue() ? this.list : Iterables.transform(this.list, Tag::copy);
/* 290 */     List<Tag> debug2 = Lists.newArrayList(debug1);
/* 291 */     return new ListTag(debug2, this.type);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object debug1) {
/* 296 */     if (this == debug1) {
/* 297 */       return true;
/*     */     }
/*     */     
/* 300 */     return (debug1 instanceof ListTag && Objects.equals(this.list, ((ListTag)debug1).list));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 305 */     return this.list.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getPrettyDisplay(String debug1, int debug2) {
/* 310 */     if (isEmpty()) {
/* 311 */       return (Component)new TextComponent("[]");
/*     */     }
/*     */     
/* 314 */     if (INLINE_ELEMENT_TYPES.contains(this.type) && size() <= 8) {
/* 315 */       String debug3 = ", ";
/* 316 */       TextComponent textComponent1 = new TextComponent("[");
/* 317 */       for (int i = 0; i < this.list.size(); i++) {
/* 318 */         if (i != 0) {
/* 319 */           textComponent1.append(", ");
/*     */         }
/* 321 */         textComponent1.append(((Tag)this.list.get(i)).getPrettyDisplay());
/*     */       } 
/* 323 */       textComponent1.append("]");
/* 324 */       return (Component)textComponent1;
/*     */     } 
/*     */     
/* 327 */     TextComponent textComponent = new TextComponent("[");
/* 328 */     if (!debug1.isEmpty()) {
/* 329 */       textComponent.append("\n");
/*     */     }
/* 331 */     String debug4 = String.valueOf(',');
/* 332 */     for (int debug5 = 0; debug5 < this.list.size(); debug5++) {
/* 333 */       TextComponent textComponent1 = new TextComponent(Strings.repeat(debug1, debug2 + 1));
/* 334 */       textComponent1.append(((Tag)this.list.get(debug5)).getPrettyDisplay(debug1, debug2 + 1));
/* 335 */       if (debug5 != this.list.size() - 1) {
/* 336 */         textComponent1.append(debug4).append(debug1.isEmpty() ? " " : "\n");
/*     */       }
/* 338 */       textComponent.append((Component)textComponent1);
/*     */     } 
/* 340 */     if (!debug1.isEmpty()) {
/* 341 */       textComponent.append("\n").append(Strings.repeat(debug1, debug2));
/*     */     }
/* 343 */     textComponent.append("]");
/*     */     
/* 345 */     return (Component)textComponent;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getElementType() {
/* 350 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 355 */     this.list.clear();
/* 356 */     this.type = 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\nbt\ListTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */