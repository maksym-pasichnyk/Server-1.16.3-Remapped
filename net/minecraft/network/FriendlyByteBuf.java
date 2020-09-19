/*      */ package net.minecraft.network;
/*      */ 
/*      */ import com.mojang.serialization.Codec;
/*      */ import com.mojang.serialization.DataResult;
/*      */ import com.mojang.serialization.DynamicOps;
/*      */ import io.netty.buffer.ByteBuf;
/*      */ import io.netty.buffer.ByteBufAllocator;
/*      */ import io.netty.buffer.ByteBufInputStream;
/*      */ import io.netty.buffer.ByteBufOutputStream;
/*      */ import io.netty.handler.codec.DecoderException;
/*      */ import io.netty.handler.codec.EncoderException;
/*      */ import io.netty.util.ByteProcessor;
/*      */ import io.netty.util.ReferenceCounted;
/*      */ import java.io.DataInput;
/*      */ import java.io.DataOutput;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.ByteOrder;
/*      */ import java.nio.channels.FileChannel;
/*      */ import java.nio.channels.GatheringByteChannel;
/*      */ import java.nio.channels.ScatteringByteChannel;
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import java.util.Date;
/*      */ import java.util.UUID;
/*      */ import javax.annotation.Nullable;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.nbt.NbtAccounter;
/*      */ import net.minecraft.nbt.NbtIo;
/*      */ import net.minecraft.nbt.NbtOps;
/*      */ import net.minecraft.nbt.Tag;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.resources.ResourceLocation;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.level.ItemLike;
/*      */ import net.minecraft.world.phys.BlockHitResult;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class FriendlyByteBuf
/*      */   extends ByteBuf
/*      */ {
/*      */   private final ByteBuf source;
/*      */   
/*      */   public FriendlyByteBuf(ByteBuf debug1) {
/*   58 */     this.source = debug1;
/*      */   }
/*      */   
/*      */   public static int getVarIntSize(int debug0) {
/*   62 */     for (int debug1 = 1; debug1 < 5; debug1++) {
/*   63 */       if ((debug0 & -1 << debug1 * 7) == 0) {
/*   64 */         return debug1;
/*      */       }
/*      */     } 
/*   67 */     return 5;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T readWithCodec(Codec<T> debug1) throws IOException {
/*   81 */     CompoundTag debug2 = readAnySizeNbt();
/*   82 */     DataResult<T> debug3 = debug1.parse((DynamicOps)NbtOps.INSTANCE, debug2);
/*   83 */     if (debug3.error().isPresent()) {
/*   84 */       throw new IOException("Failed to decode: " + ((DataResult.PartialResult)debug3.error().get()).message() + " " + debug2);
/*      */     }
/*      */     
/*   87 */     return debug3.result().get();
/*      */   }
/*      */ 
/*      */   
/*      */   public <T> void writeWithCodec(Codec<T> debug1, T debug2) throws IOException {
/*   92 */     DataResult<Tag> debug3 = debug1.encodeStart((DynamicOps)NbtOps.INSTANCE, debug2);
/*   93 */     if (debug3.error().isPresent()) {
/*   94 */       throw new IOException("Failed to encode: " + ((DataResult.PartialResult)debug3.error().get()).message() + " " + debug2);
/*      */     }
/*   96 */     writeNbt(debug3.result().get());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeByteArray(byte[] debug1) {
/*  105 */     writeVarInt(debug1.length);
/*  106 */     writeBytes(debug1);
/*      */     
/*  108 */     return this;
/*      */   }
/*      */   
/*      */   public byte[] readByteArray() {
/*  112 */     return readByteArray(readableBytes());
/*      */   }
/*      */   
/*      */   public byte[] readByteArray(int debug1) {
/*  116 */     int debug2 = readVarInt();
/*  117 */     if (debug2 > debug1) {
/*  118 */       throw new DecoderException("ByteArray with size " + debug2 + " is bigger than allowed " + debug1);
/*      */     }
/*  120 */     byte[] debug3 = new byte[debug2];
/*  121 */     readBytes(debug3);
/*      */     
/*  123 */     return debug3;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeVarIntArray(int[] debug1) {
/*  127 */     writeVarInt(debug1.length);
/*      */     
/*  129 */     for (int debug5 : debug1) {
/*  130 */       writeVarInt(debug5);
/*      */     }
/*      */     
/*  133 */     return this;
/*      */   }
/*      */   
/*      */   public int[] readVarIntArray() {
/*  137 */     return readVarIntArray(readableBytes());
/*      */   }
/*      */   
/*      */   public int[] readVarIntArray(int debug1) {
/*  141 */     int debug2 = readVarInt();
/*  142 */     if (debug2 > debug1) {
/*  143 */       throw new DecoderException("VarIntArray with size " + debug2 + " is bigger than allowed " + debug1);
/*      */     }
/*  145 */     int[] debug3 = new int[debug2];
/*      */     
/*  147 */     for (int debug4 = 0; debug4 < debug3.length; debug4++) {
/*  148 */       debug3[debug4] = readVarInt();
/*      */     }
/*      */     
/*  151 */     return debug3;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeLongArray(long[] debug1) {
/*  155 */     writeVarInt(debug1.length);
/*      */     
/*  157 */     for (long debug5 : debug1) {
/*  158 */       writeLong(debug5);
/*      */     }
/*      */     
/*  161 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BlockPos readBlockPos() {
/*  201 */     return BlockPos.of(readLong());
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeBlockPos(BlockPos debug1) {
/*  205 */     writeLong(debug1.asLong());
/*  206 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Component readComponent() {
/*  228 */     return (Component)Component.Serializer.fromJson(readUtf(262144));
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeComponent(Component debug1) {
/*  232 */     return writeUtf(Component.Serializer.toJson(debug1), 262144);
/*      */   }
/*      */   
/*      */   public <T extends Enum<T>> T readEnum(Class<T> debug1) {
/*  236 */     return (T)((Enum[])debug1.getEnumConstants())[readVarInt()];
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeEnum(Enum<?> debug1) {
/*  240 */     return writeVarInt(debug1.ordinal());
/*      */   }
/*      */   public int readVarInt() {
/*      */     byte debug3;
/*  244 */     int debug1 = 0;
/*  245 */     int debug2 = 0;
/*      */     
/*      */     do {
/*  248 */       debug3 = readByte();
/*      */       
/*  250 */       debug1 |= (debug3 & Byte.MAX_VALUE) << debug2++ * 7;
/*      */       
/*  252 */       if (debug2 > 5) {
/*  253 */         throw new RuntimeException("VarInt too big");
/*      */       }
/*      */     }
/*  256 */     while ((debug3 & 0x80) == 128);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  261 */     return debug1;
/*      */   }
/*      */   public long readVarLong() {
/*      */     byte debug4;
/*  265 */     long debug1 = 0L;
/*  266 */     int debug3 = 0;
/*      */     
/*      */     do {
/*  269 */       debug4 = readByte();
/*      */       
/*  271 */       debug1 |= (debug4 & Byte.MAX_VALUE) << debug3++ * 7;
/*      */       
/*  273 */       if (debug3 > 10) {
/*  274 */         throw new RuntimeException("VarLong too big");
/*      */       }
/*      */     }
/*  277 */     while ((debug4 & 0x80) == 128);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  282 */     return debug1;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeUUID(UUID debug1) {
/*  286 */     writeLong(debug1.getMostSignificantBits());
/*  287 */     writeLong(debug1.getLeastSignificantBits());
/*      */     
/*  289 */     return this;
/*      */   }
/*      */   
/*      */   public UUID readUUID() {
/*  293 */     return new UUID(readLong(), readLong());
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeVarInt(int debug1) {
/*      */     while (true) {
/*  298 */       if ((debug1 & 0xFFFFFF80) == 0) {
/*  299 */         writeByte(debug1);
/*  300 */         return this;
/*      */       } 
/*      */       
/*  303 */       writeByte(debug1 & 0x7F | 0x80);
/*  304 */       debug1 >>>= 7;
/*      */     } 
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeVarLong(long debug1) {
/*      */     while (true) {
/*  310 */       if ((debug1 & 0xFFFFFFFFFFFFFF80L) == 0L) {
/*  311 */         writeByte((int)debug1);
/*  312 */         return this;
/*      */       } 
/*      */       
/*  315 */       writeByte((int)(debug1 & 0x7FL) | 0x80);
/*  316 */       debug1 >>>= 7L;
/*      */     } 
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeNbt(@Nullable CompoundTag debug1) {
/*  321 */     if (debug1 == null) {
/*  322 */       writeByte(0);
/*      */     } else {
/*      */       try {
/*  325 */         NbtIo.write(debug1, (DataOutput)new ByteBufOutputStream(this));
/*  326 */       } catch (IOException debug2) {
/*  327 */         throw new EncoderException(debug2);
/*      */       } 
/*      */     } 
/*      */     
/*  331 */     return this;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public CompoundTag readNbt() {
/*  336 */     return readNbt(new NbtAccounter(2097152L));
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public CompoundTag readAnySizeNbt() {
/*  341 */     return readNbt(NbtAccounter.UNLIMITED);
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public CompoundTag readNbt(NbtAccounter debug1) {
/*  346 */     int debug2 = readerIndex();
/*  347 */     byte debug3 = readByte();
/*      */     
/*  349 */     if (debug3 == 0) {
/*  350 */       return null;
/*      */     }
/*  352 */     readerIndex(debug2);
/*      */     try {
/*  354 */       return NbtIo.read((DataInput)new ByteBufInputStream(this), debug1);
/*  355 */     } catch (IOException debug4) {
/*  356 */       throw new EncoderException(debug4);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeItem(ItemStack debug1) {
/*  362 */     if (debug1.isEmpty()) {
/*  363 */       writeBoolean(false);
/*      */     } else {
/*  365 */       writeBoolean(true);
/*  366 */       Item debug2 = debug1.getItem();
/*  367 */       writeVarInt(Item.getId(debug2));
/*  368 */       writeByte(debug1.getCount());
/*      */       
/*  370 */       CompoundTag debug3 = null;
/*  371 */       if (debug2.canBeDepleted() || debug2.shouldOverrideMultiplayerNbt()) {
/*  372 */         debug3 = debug1.getTag();
/*      */       }
/*  374 */       writeNbt(debug3);
/*      */     } 
/*      */     
/*  377 */     return this;
/*      */   }
/*      */   
/*      */   public ItemStack readItem() {
/*  381 */     if (!readBoolean()) {
/*  382 */       return ItemStack.EMPTY;
/*      */     }
/*      */     
/*  385 */     int debug1 = readVarInt();
/*  386 */     int debug2 = readByte();
/*      */     
/*  388 */     ItemStack debug3 = new ItemStack((ItemLike)Item.byId(debug1), debug2);
/*  389 */     debug3.setTag(readNbt());
/*      */     
/*  391 */     return debug3;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String readUtf(int debug1) {
/*  399 */     int debug2 = readVarInt();
/*  400 */     if (debug2 > debug1 * 4) {
/*  401 */       throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + debug2 + " > " + (debug1 * 4) + ")");
/*      */     }
/*  403 */     if (debug2 < 0) {
/*  404 */       throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");
/*      */     }
/*      */     
/*  407 */     String debug3 = toString(readerIndex(), debug2, StandardCharsets.UTF_8);
/*  408 */     readerIndex(readerIndex() + debug2);
/*  409 */     if (debug3.length() > debug1) {
/*  410 */       throw new DecoderException("The received string length is longer than maximum allowed (" + debug2 + " > " + debug1 + ")");
/*      */     }
/*      */     
/*  413 */     return debug3;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeUtf(String debug1) {
/*  417 */     return writeUtf(debug1, 32767);
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeUtf(String debug1, int debug2) {
/*  421 */     byte[] debug3 = debug1.getBytes(StandardCharsets.UTF_8);
/*  422 */     if (debug3.length > debug2) {
/*  423 */       throw new EncoderException("String too big (was " + debug3.length + " bytes encoded, max " + debug2 + ")");
/*      */     }
/*  425 */     writeVarInt(debug3.length);
/*  426 */     writeBytes(debug3);
/*  427 */     return this;
/*      */   }
/*      */   
/*      */   public ResourceLocation readResourceLocation() {
/*  431 */     return new ResourceLocation(readUtf(32767));
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeResourceLocation(ResourceLocation debug1) {
/*  435 */     writeUtf(debug1.toString());
/*  436 */     return this;
/*      */   }
/*      */   
/*      */   public Date readDate() {
/*  440 */     return new Date(readLong());
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeDate(Date debug1) {
/*  444 */     writeLong(debug1.getTime());
/*  445 */     return this;
/*      */   }
/*      */   
/*      */   public BlockHitResult readBlockHitResult() {
/*  449 */     BlockPos debug1 = readBlockPos();
/*  450 */     Direction debug2 = readEnum(Direction.class);
/*  451 */     float debug3 = readFloat();
/*  452 */     float debug4 = readFloat();
/*  453 */     float debug5 = readFloat();
/*  454 */     boolean debug6 = readBoolean();
/*      */     
/*  456 */     return new BlockHitResult(new Vec3(debug1
/*  457 */           .getX() + debug3, debug1
/*  458 */           .getY() + debug4, debug1
/*  459 */           .getZ() + debug5), debug2, debug1, debug6);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeBlockHitResult(BlockHitResult debug1) {
/*  467 */     BlockPos debug2 = debug1.getBlockPos();
/*  468 */     writeBlockPos(debug2);
/*  469 */     writeEnum((Enum<?>)debug1.getDirection());
/*  470 */     Vec3 debug3 = debug1.getLocation();
/*  471 */     writeFloat((float)(debug3.x - debug2.getX()));
/*  472 */     writeFloat((float)(debug3.y - debug2.getY()));
/*  473 */     writeFloat((float)(debug3.z - debug2.getZ()));
/*  474 */     writeBoolean(debug1.isInside());
/*      */   }
/*      */ 
/*      */   
/*      */   public int capacity() {
/*  479 */     return this.source.capacity();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf capacity(int debug1) {
/*  484 */     return this.source.capacity(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public int maxCapacity() {
/*  489 */     return this.source.maxCapacity();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBufAllocator alloc() {
/*  494 */     return this.source.alloc();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteOrder order() {
/*  499 */     return this.source.order();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf order(ByteOrder debug1) {
/*  504 */     return this.source.order(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf unwrap() {
/*  509 */     return this.source.unwrap();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isDirect() {
/*  514 */     return this.source.isDirect();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isReadOnly() {
/*  519 */     return this.source.isReadOnly();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf asReadOnly() {
/*  524 */     return this.source.asReadOnly();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readerIndex() {
/*  529 */     return this.source.readerIndex();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readerIndex(int debug1) {
/*  534 */     return this.source.readerIndex(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public int writerIndex() {
/*  539 */     return this.source.writerIndex();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writerIndex(int debug1) {
/*  544 */     return this.source.writerIndex(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setIndex(int debug1, int debug2) {
/*  549 */     return this.source.setIndex(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public int readableBytes() {
/*  554 */     return this.source.readableBytes();
/*      */   }
/*      */ 
/*      */   
/*      */   public int writableBytes() {
/*  559 */     return this.source.writableBytes();
/*      */   }
/*      */ 
/*      */   
/*      */   public int maxWritableBytes() {
/*  564 */     return this.source.maxWritableBytes();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isReadable() {
/*  569 */     return this.source.isReadable();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isReadable(int debug1) {
/*  574 */     return this.source.isReadable(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isWritable() {
/*  579 */     return this.source.isWritable();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isWritable(int debug1) {
/*  584 */     return this.source.isWritable(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf clear() {
/*  589 */     return this.source.clear();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf markReaderIndex() {
/*  594 */     return this.source.markReaderIndex();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf resetReaderIndex() {
/*  599 */     return this.source.resetReaderIndex();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf markWriterIndex() {
/*  604 */     return this.source.markWriterIndex();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf resetWriterIndex() {
/*  609 */     return this.source.resetWriterIndex();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf discardReadBytes() {
/*  614 */     return this.source.discardReadBytes();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf discardSomeReadBytes() {
/*  619 */     return this.source.discardSomeReadBytes();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf ensureWritable(int debug1) {
/*  624 */     return this.source.ensureWritable(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public int ensureWritable(int debug1, boolean debug2) {
/*  629 */     return this.source.ensureWritable(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean getBoolean(int debug1) {
/*  634 */     return this.source.getBoolean(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public byte getByte(int debug1) {
/*  639 */     return this.source.getByte(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public short getUnsignedByte(int debug1) {
/*  644 */     return this.source.getUnsignedByte(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public short getShort(int debug1) {
/*  649 */     return this.source.getShort(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public short getShortLE(int debug1) {
/*  654 */     return this.source.getShortLE(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getUnsignedShort(int debug1) {
/*  659 */     return this.source.getUnsignedShort(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getUnsignedShortLE(int debug1) {
/*  664 */     return this.source.getUnsignedShortLE(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMedium(int debug1) {
/*  669 */     return this.source.getMedium(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMediumLE(int debug1) {
/*  674 */     return this.source.getMediumLE(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getUnsignedMedium(int debug1) {
/*  679 */     return this.source.getUnsignedMedium(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getUnsignedMediumLE(int debug1) {
/*  684 */     return this.source.getUnsignedMediumLE(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getInt(int debug1) {
/*  689 */     return this.source.getInt(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getIntLE(int debug1) {
/*  694 */     return this.source.getIntLE(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public long getUnsignedInt(int debug1) {
/*  699 */     return this.source.getUnsignedInt(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public long getUnsignedIntLE(int debug1) {
/*  704 */     return this.source.getUnsignedIntLE(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public long getLong(int debug1) {
/*  709 */     return this.source.getLong(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public long getLongLE(int debug1) {
/*  714 */     return this.source.getLongLE(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public char getChar(int debug1) {
/*  719 */     return this.source.getChar(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public float getFloat(int debug1) {
/*  724 */     return this.source.getFloat(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public double getDouble(int debug1) {
/*  729 */     return this.source.getDouble(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf getBytes(int debug1, ByteBuf debug2) {
/*  734 */     return this.source.getBytes(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf getBytes(int debug1, ByteBuf debug2, int debug3) {
/*  739 */     return this.source.getBytes(debug1, debug2, debug3);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf getBytes(int debug1, ByteBuf debug2, int debug3, int debug4) {
/*  744 */     return this.source.getBytes(debug1, debug2, debug3, debug4);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf getBytes(int debug1, byte[] debug2) {
/*  749 */     return this.source.getBytes(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf getBytes(int debug1, byte[] debug2, int debug3, int debug4) {
/*  754 */     return this.source.getBytes(debug1, debug2, debug3, debug4);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf getBytes(int debug1, ByteBuffer debug2) {
/*  759 */     return this.source.getBytes(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf getBytes(int debug1, OutputStream debug2, int debug3) throws IOException {
/*  764 */     return this.source.getBytes(debug1, debug2, debug3);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getBytes(int debug1, GatheringByteChannel debug2, int debug3) throws IOException {
/*  769 */     return this.source.getBytes(debug1, debug2, debug3);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getBytes(int debug1, FileChannel debug2, long debug3, int debug5) throws IOException {
/*  774 */     return this.source.getBytes(debug1, debug2, debug3, debug5);
/*      */   }
/*      */ 
/*      */   
/*      */   public CharSequence getCharSequence(int debug1, int debug2, Charset debug3) {
/*  779 */     return this.source.getCharSequence(debug1, debug2, debug3);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setBoolean(int debug1, boolean debug2) {
/*  784 */     return this.source.setBoolean(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setByte(int debug1, int debug2) {
/*  789 */     return this.source.setByte(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setShort(int debug1, int debug2) {
/*  794 */     return this.source.setShort(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setShortLE(int debug1, int debug2) {
/*  799 */     return this.source.setShortLE(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setMedium(int debug1, int debug2) {
/*  804 */     return this.source.setMedium(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setMediumLE(int debug1, int debug2) {
/*  809 */     return this.source.setMediumLE(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setInt(int debug1, int debug2) {
/*  814 */     return this.source.setInt(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setIntLE(int debug1, int debug2) {
/*  819 */     return this.source.setIntLE(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setLong(int debug1, long debug2) {
/*  824 */     return this.source.setLong(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setLongLE(int debug1, long debug2) {
/*  829 */     return this.source.setLongLE(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setChar(int debug1, int debug2) {
/*  834 */     return this.source.setChar(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setFloat(int debug1, float debug2) {
/*  839 */     return this.source.setFloat(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setDouble(int debug1, double debug2) {
/*  844 */     return this.source.setDouble(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setBytes(int debug1, ByteBuf debug2) {
/*  849 */     return this.source.setBytes(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setBytes(int debug1, ByteBuf debug2, int debug3) {
/*  854 */     return this.source.setBytes(debug1, debug2, debug3);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setBytes(int debug1, ByteBuf debug2, int debug3, int debug4) {
/*  859 */     return this.source.setBytes(debug1, debug2, debug3, debug4);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setBytes(int debug1, byte[] debug2) {
/*  864 */     return this.source.setBytes(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setBytes(int debug1, byte[] debug2, int debug3, int debug4) {
/*  869 */     return this.source.setBytes(debug1, debug2, debug3, debug4);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setBytes(int debug1, ByteBuffer debug2) {
/*  874 */     return this.source.setBytes(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public int setBytes(int debug1, InputStream debug2, int debug3) throws IOException {
/*  879 */     return this.source.setBytes(debug1, debug2, debug3);
/*      */   }
/*      */ 
/*      */   
/*      */   public int setBytes(int debug1, ScatteringByteChannel debug2, int debug3) throws IOException {
/*  884 */     return this.source.setBytes(debug1, debug2, debug3);
/*      */   }
/*      */ 
/*      */   
/*      */   public int setBytes(int debug1, FileChannel debug2, long debug3, int debug5) throws IOException {
/*  889 */     return this.source.setBytes(debug1, debug2, debug3, debug5);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf setZero(int debug1, int debug2) {
/*  894 */     return this.source.setZero(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public int setCharSequence(int debug1, CharSequence debug2, Charset debug3) {
/*  899 */     return this.source.setCharSequence(debug1, debug2, debug3);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean readBoolean() {
/*  904 */     return this.source.readBoolean();
/*      */   }
/*      */ 
/*      */   
/*      */   public byte readByte() {
/*  909 */     return this.source.readByte();
/*      */   }
/*      */ 
/*      */   
/*      */   public short readUnsignedByte() {
/*  914 */     return this.source.readUnsignedByte();
/*      */   }
/*      */ 
/*      */   
/*      */   public short readShort() {
/*  919 */     return this.source.readShort();
/*      */   }
/*      */ 
/*      */   
/*      */   public short readShortLE() {
/*  924 */     return this.source.readShortLE();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readUnsignedShort() {
/*  929 */     return this.source.readUnsignedShort();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readUnsignedShortLE() {
/*  934 */     return this.source.readUnsignedShortLE();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readMedium() {
/*  939 */     return this.source.readMedium();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readMediumLE() {
/*  944 */     return this.source.readMediumLE();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readUnsignedMedium() {
/*  949 */     return this.source.readUnsignedMedium();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readUnsignedMediumLE() {
/*  954 */     return this.source.readUnsignedMediumLE();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readInt() {
/*  959 */     return this.source.readInt();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readIntLE() {
/*  964 */     return this.source.readIntLE();
/*      */   }
/*      */ 
/*      */   
/*      */   public long readUnsignedInt() {
/*  969 */     return this.source.readUnsignedInt();
/*      */   }
/*      */ 
/*      */   
/*      */   public long readUnsignedIntLE() {
/*  974 */     return this.source.readUnsignedIntLE();
/*      */   }
/*      */ 
/*      */   
/*      */   public long readLong() {
/*  979 */     return this.source.readLong();
/*      */   }
/*      */ 
/*      */   
/*      */   public long readLongLE() {
/*  984 */     return this.source.readLongLE();
/*      */   }
/*      */ 
/*      */   
/*      */   public char readChar() {
/*  989 */     return this.source.readChar();
/*      */   }
/*      */ 
/*      */   
/*      */   public float readFloat() {
/*  994 */     return this.source.readFloat();
/*      */   }
/*      */ 
/*      */   
/*      */   public double readDouble() {
/*  999 */     return this.source.readDouble();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(int debug1) {
/* 1004 */     return this.source.readBytes(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readSlice(int debug1) {
/* 1009 */     return this.source.readSlice(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readRetainedSlice(int debug1) {
/* 1014 */     return this.source.readRetainedSlice(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(ByteBuf debug1) {
/* 1019 */     return this.source.readBytes(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(ByteBuf debug1, int debug2) {
/* 1024 */     return this.source.readBytes(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(ByteBuf debug1, int debug2, int debug3) {
/* 1029 */     return this.source.readBytes(debug1, debug2, debug3);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(byte[] debug1) {
/* 1034 */     return this.source.readBytes(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(byte[] debug1, int debug2, int debug3) {
/* 1039 */     return this.source.readBytes(debug1, debug2, debug3);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(ByteBuffer debug1) {
/* 1044 */     return this.source.readBytes(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(OutputStream debug1, int debug2) throws IOException {
/* 1049 */     return this.source.readBytes(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public int readBytes(GatheringByteChannel debug1, int debug2) throws IOException {
/* 1054 */     return this.source.readBytes(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public CharSequence readCharSequence(int debug1, Charset debug2) {
/* 1059 */     return this.source.readCharSequence(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public int readBytes(FileChannel debug1, long debug2, int debug4) throws IOException {
/* 1064 */     return this.source.readBytes(debug1, debug2, debug4);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf skipBytes(int debug1) {
/* 1069 */     return this.source.skipBytes(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeBoolean(boolean debug1) {
/* 1074 */     return this.source.writeBoolean(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeByte(int debug1) {
/* 1079 */     return this.source.writeByte(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeShort(int debug1) {
/* 1084 */     return this.source.writeShort(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeShortLE(int debug1) {
/* 1089 */     return this.source.writeShortLE(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeMedium(int debug1) {
/* 1094 */     return this.source.writeMedium(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeMediumLE(int debug1) {
/* 1099 */     return this.source.writeMediumLE(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeInt(int debug1) {
/* 1104 */     return this.source.writeInt(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeIntLE(int debug1) {
/* 1109 */     return this.source.writeIntLE(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeLong(long debug1) {
/* 1114 */     return this.source.writeLong(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeLongLE(long debug1) {
/* 1119 */     return this.source.writeLongLE(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeChar(int debug1) {
/* 1124 */     return this.source.writeChar(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeFloat(float debug1) {
/* 1129 */     return this.source.writeFloat(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeDouble(double debug1) {
/* 1134 */     return this.source.writeDouble(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeBytes(ByteBuf debug1) {
/* 1139 */     return this.source.writeBytes(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeBytes(ByteBuf debug1, int debug2) {
/* 1144 */     return this.source.writeBytes(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeBytes(ByteBuf debug1, int debug2, int debug3) {
/* 1149 */     return this.source.writeBytes(debug1, debug2, debug3);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeBytes(byte[] debug1) {
/* 1154 */     return this.source.writeBytes(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeBytes(byte[] debug1, int debug2, int debug3) {
/* 1159 */     return this.source.writeBytes(debug1, debug2, debug3);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeBytes(ByteBuffer debug1) {
/* 1164 */     return this.source.writeBytes(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public int writeBytes(InputStream debug1, int debug2) throws IOException {
/* 1169 */     return this.source.writeBytes(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public int writeBytes(ScatteringByteChannel debug1, int debug2) throws IOException {
/* 1174 */     return this.source.writeBytes(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public int writeBytes(FileChannel debug1, long debug2, int debug4) throws IOException {
/* 1179 */     return this.source.writeBytes(debug1, debug2, debug4);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf writeZero(int debug1) {
/* 1184 */     return this.source.writeZero(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public int writeCharSequence(CharSequence debug1, Charset debug2) {
/* 1189 */     return this.source.writeCharSequence(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public int indexOf(int debug1, int debug2, byte debug3) {
/* 1194 */     return this.source.indexOf(debug1, debug2, debug3);
/*      */   }
/*      */ 
/*      */   
/*      */   public int bytesBefore(byte debug1) {
/* 1199 */     return this.source.bytesBefore(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public int bytesBefore(int debug1, byte debug2) {
/* 1204 */     return this.source.bytesBefore(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public int bytesBefore(int debug1, int debug2, byte debug3) {
/* 1209 */     return this.source.bytesBefore(debug1, debug2, debug3);
/*      */   }
/*      */ 
/*      */   
/*      */   public int forEachByte(ByteProcessor debug1) {
/* 1214 */     return this.source.forEachByte(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public int forEachByte(int debug1, int debug2, ByteProcessor debug3) {
/* 1219 */     return this.source.forEachByte(debug1, debug2, debug3);
/*      */   }
/*      */ 
/*      */   
/*      */   public int forEachByteDesc(ByteProcessor debug1) {
/* 1224 */     return this.source.forEachByteDesc(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public int forEachByteDesc(int debug1, int debug2, ByteProcessor debug3) {
/* 1229 */     return this.source.forEachByteDesc(debug1, debug2, debug3);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf copy() {
/* 1234 */     return this.source.copy();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf copy(int debug1, int debug2) {
/* 1239 */     return this.source.copy(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf slice() {
/* 1244 */     return this.source.slice();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf retainedSlice() {
/* 1249 */     return this.source.retainedSlice();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf slice(int debug1, int debug2) {
/* 1254 */     return this.source.slice(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf retainedSlice(int debug1, int debug2) {
/* 1259 */     return this.source.retainedSlice(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf duplicate() {
/* 1264 */     return this.source.duplicate();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf retainedDuplicate() {
/* 1269 */     return this.source.retainedDuplicate();
/*      */   }
/*      */ 
/*      */   
/*      */   public int nioBufferCount() {
/* 1274 */     return this.source.nioBufferCount();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuffer nioBuffer() {
/* 1279 */     return this.source.nioBuffer();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuffer nioBuffer(int debug1, int debug2) {
/* 1284 */     return this.source.nioBuffer(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuffer internalNioBuffer(int debug1, int debug2) {
/* 1289 */     return this.source.internalNioBuffer(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuffer[] nioBuffers() {
/* 1294 */     return this.source.nioBuffers();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuffer[] nioBuffers(int debug1, int debug2) {
/* 1299 */     return this.source.nioBuffers(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasArray() {
/* 1304 */     return this.source.hasArray();
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] array() {
/* 1309 */     return this.source.array();
/*      */   }
/*      */ 
/*      */   
/*      */   public int arrayOffset() {
/* 1314 */     return this.source.arrayOffset();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasMemoryAddress() {
/* 1319 */     return this.source.hasMemoryAddress();
/*      */   }
/*      */ 
/*      */   
/*      */   public long memoryAddress() {
/* 1324 */     return this.source.memoryAddress();
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString(Charset debug1) {
/* 1329 */     return this.source.toString(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString(int debug1, int debug2, Charset debug3) {
/* 1334 */     return this.source.toString(debug1, debug2, debug3);
/*      */   }
/*      */ 
/*      */   
/*      */   public int hashCode() {
/* 1339 */     return this.source.hashCode();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean equals(Object debug1) {
/* 1344 */     return this.source.equals(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public int compareTo(ByteBuf debug1) {
/* 1349 */     return this.source.compareTo(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1354 */     return this.source.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf retain(int debug1) {
/* 1359 */     return this.source.retain(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf retain() {
/* 1364 */     return this.source.retain();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf touch() {
/* 1369 */     return this.source.touch();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf touch(Object debug1) {
/* 1374 */     return this.source.touch(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public int refCnt() {
/* 1379 */     return this.source.refCnt();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean release() {
/* 1384 */     return this.source.release();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean release(int debug1) {
/* 1389 */     return this.source.release(debug1);
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\FriendlyByteBuf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */