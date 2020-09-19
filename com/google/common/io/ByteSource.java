/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Ascii;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.hash.Funnels;
/*     */ import com.google.common.hash.HashCode;
/*     */ import com.google.common.hash.HashFunction;
/*     */ import com.google.common.hash.Hasher;
/*     */ import com.google.common.hash.PrimitiveSink;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Reader;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
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
/*     */ @GwtIncompatible
/*     */ public abstract class ByteSource
/*     */ {
/*     */   public CharSource asCharSource(Charset charset) {
/*  79 */     return new AsCharSource(charset);
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
/*     */   public abstract InputStream openStream() throws IOException;
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
/*     */   public InputStream openBufferedStream() throws IOException {
/* 105 */     InputStream in = openStream();
/* 106 */     return (in instanceof BufferedInputStream) ? in : new BufferedInputStream(in);
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
/*     */   public ByteSource slice(long offset, long length) {
/* 121 */     return new SlicedByteSource(offset, length);
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
/*     */   public boolean isEmpty() throws IOException {
/* 138 */     Optional<Long> sizeIfKnown = sizeIfKnown();
/* 139 */     if (sizeIfKnown.isPresent() && ((Long)sizeIfKnown.get()).longValue() == 0L) {
/* 140 */       return true;
/*     */     }
/* 142 */     Closer closer = Closer.create();
/*     */     try {
/* 144 */       InputStream in = closer.<InputStream>register(openStream());
/* 145 */       return (in.read() == -1);
/* 146 */     } catch (Throwable e) {
/* 147 */       throw closer.rethrow(e);
/*     */     } finally {
/* 149 */       closer.close();
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
/*     */   @Beta
/*     */   public Optional<Long> sizeIfKnown() {
/* 169 */     return Optional.absent();
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
/*     */   public long size() throws IOException {
/* 192 */     Optional<Long> sizeIfKnown = sizeIfKnown();
/* 193 */     if (sizeIfKnown.isPresent()) {
/* 194 */       return ((Long)sizeIfKnown.get()).longValue();
/*     */     }
/*     */     
/* 197 */     Closer closer = Closer.create();
/*     */     try {
/* 199 */       InputStream in = closer.<InputStream>register(openStream());
/* 200 */       return countBySkipping(in);
/* 201 */     } catch (IOException iOException) {
/*     */     
/*     */     } finally {
/* 204 */       closer.close();
/*     */     } 
/*     */     
/* 207 */     closer = Closer.create();
/*     */     try {
/* 209 */       InputStream in = closer.<InputStream>register(openStream());
/* 210 */       return ByteStreams.exhaust(in);
/* 211 */     } catch (Throwable e) {
/* 212 */       throw closer.rethrow(e);
/*     */     } finally {
/* 214 */       closer.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long countBySkipping(InputStream in) throws IOException {
/* 223 */     long count = 0L;
/*     */     long skipped;
/* 225 */     while ((skipped = ByteStreams.skipUpTo(in, 2147483647L)) > 0L) {
/* 226 */       count += skipped;
/*     */     }
/* 228 */     return count;
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
/*     */   @CanIgnoreReturnValue
/*     */   public long copyTo(OutputStream output) throws IOException {
/* 241 */     Preconditions.checkNotNull(output);
/*     */     
/* 243 */     Closer closer = Closer.create();
/*     */     try {
/* 245 */       InputStream in = closer.<InputStream>register(openStream());
/* 246 */       return ByteStreams.copy(in, output);
/* 247 */     } catch (Throwable e) {
/* 248 */       throw closer.rethrow(e);
/*     */     } finally {
/* 250 */       closer.close();
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
/*     */   @CanIgnoreReturnValue
/*     */   public long copyTo(ByteSink sink) throws IOException {
/* 263 */     Preconditions.checkNotNull(sink);
/*     */     
/* 265 */     Closer closer = Closer.create();
/*     */     try {
/* 267 */       InputStream in = closer.<InputStream>register(openStream());
/* 268 */       OutputStream out = closer.<OutputStream>register(sink.openStream());
/* 269 */       return ByteStreams.copy(in, out);
/* 270 */     } catch (Throwable e) {
/* 271 */       throw closer.rethrow(e);
/*     */     } finally {
/* 273 */       closer.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] read() throws IOException {
/* 283 */     Closer closer = Closer.create();
/*     */     try {
/* 285 */       InputStream in = closer.<InputStream>register(openStream());
/* 286 */       return ByteStreams.toByteArray(in);
/* 287 */     } catch (Throwable e) {
/* 288 */       throw closer.rethrow(e);
/*     */     } finally {
/* 290 */       closer.close();
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
/*     */   @Beta
/*     */   @CanIgnoreReturnValue
/*     */   public <T> T read(ByteProcessor<T> processor) throws IOException {
/* 306 */     Preconditions.checkNotNull(processor);
/*     */     
/* 308 */     Closer closer = Closer.create();
/*     */     try {
/* 310 */       InputStream in = closer.<InputStream>register(openStream());
/* 311 */       return (T)ByteStreams.readBytes(in, (ByteProcessor)processor);
/* 312 */     } catch (Throwable e) {
/* 313 */       throw closer.rethrow(e);
/*     */     } finally {
/* 315 */       closer.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HashCode hash(HashFunction hashFunction) throws IOException {
/* 325 */     Hasher hasher = hashFunction.newHasher();
/* 326 */     copyTo(Funnels.asOutputStream((PrimitiveSink)hasher));
/* 327 */     return hasher.hash();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contentEquals(ByteSource other) throws IOException {
/* 338 */     Preconditions.checkNotNull(other);
/*     */     
/* 340 */     byte[] buf1 = ByteStreams.createBuffer();
/* 341 */     byte[] buf2 = ByteStreams.createBuffer();
/*     */     
/* 343 */     Closer closer = Closer.create();
/*     */     try {
/* 345 */       InputStream in1 = closer.<InputStream>register(openStream());
/* 346 */       InputStream in2 = closer.<InputStream>register(other.openStream());
/*     */       while (true) {
/* 348 */         int read1 = ByteStreams.read(in1, buf1, 0, buf1.length);
/* 349 */         int read2 = ByteStreams.read(in2, buf2, 0, buf2.length);
/* 350 */         if (read1 != read2 || !Arrays.equals(buf1, buf2))
/* 351 */           return false; 
/* 352 */         if (read1 != buf1.length) {
/* 353 */           return true;
/*     */         }
/*     */       } 
/* 356 */     } catch (Throwable e) {
/* 357 */       throw closer.rethrow(e);
/*     */     } finally {
/* 359 */       closer.close();
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
/*     */   public static ByteSource concat(Iterable<? extends ByteSource> sources) {
/* 375 */     return new ConcatenatedByteSource(sources);
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
/*     */   public static ByteSource concat(Iterator<? extends ByteSource> sources) {
/* 397 */     return concat((Iterable<? extends ByteSource>)ImmutableList.copyOf(sources));
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
/*     */   public static ByteSource concat(ByteSource... sources) {
/* 413 */     return concat((Iterable<? extends ByteSource>)ImmutableList.copyOf((Object[])sources));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteSource wrap(byte[] b) {
/* 423 */     return new ByteArrayByteSource(b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteSource empty() {
/* 432 */     return EmptyByteSource.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   private final class AsCharSource
/*     */     extends CharSource
/*     */   {
/*     */     final Charset charset;
/*     */ 
/*     */     
/*     */     AsCharSource(Charset charset) {
/* 443 */       this.charset = (Charset)Preconditions.checkNotNull(charset);
/*     */     }
/*     */ 
/*     */     
/*     */     public ByteSource asByteSource(Charset charset) {
/* 448 */       if (charset.equals(this.charset)) {
/* 449 */         return ByteSource.this;
/*     */       }
/* 451 */       return super.asByteSource(charset);
/*     */     }
/*     */ 
/*     */     
/*     */     public Reader openStream() throws IOException {
/* 456 */       return new InputStreamReader(ByteSource.this.openStream(), this.charset);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 461 */       return ByteSource.this.toString() + ".asCharSource(" + this.charset + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private final class SlicedByteSource
/*     */     extends ByteSource
/*     */   {
/*     */     final long offset;
/*     */     
/*     */     final long length;
/*     */     
/*     */     SlicedByteSource(long offset, long length) {
/* 474 */       Preconditions.checkArgument((offset >= 0L), "offset (%s) may not be negative", offset);
/* 475 */       Preconditions.checkArgument((length >= 0L), "length (%s) may not be negative", length);
/* 476 */       this.offset = offset;
/* 477 */       this.length = length;
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream openStream() throws IOException {
/* 482 */       return sliceStream(ByteSource.this.openStream());
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream openBufferedStream() throws IOException {
/* 487 */       return sliceStream(ByteSource.this.openBufferedStream());
/*     */     }
/*     */     
/*     */     private InputStream sliceStream(InputStream in) throws IOException {
/* 491 */       if (this.offset > 0L) {
/*     */         long skipped;
/*     */         try {
/* 494 */           skipped = ByteStreams.skipUpTo(in, this.offset);
/* 495 */         } catch (Throwable e) {
/* 496 */           Closer closer = Closer.create();
/* 497 */           closer.register(in);
/*     */           try {
/* 499 */             throw closer.rethrow(e);
/*     */           } finally {
/* 501 */             closer.close();
/*     */           } 
/*     */         } 
/*     */         
/* 505 */         if (skipped < this.offset) {
/*     */           
/* 507 */           in.close();
/* 508 */           return new ByteArrayInputStream(new byte[0]);
/*     */         } 
/*     */       } 
/* 511 */       return ByteStreams.limit(in, this.length);
/*     */     }
/*     */ 
/*     */     
/*     */     public ByteSource slice(long offset, long length) {
/* 516 */       Preconditions.checkArgument((offset >= 0L), "offset (%s) may not be negative", offset);
/* 517 */       Preconditions.checkArgument((length >= 0L), "length (%s) may not be negative", length);
/* 518 */       long maxLength = this.length - offset;
/* 519 */       return ByteSource.this.slice(this.offset + offset, Math.min(length, maxLength));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() throws IOException {
/* 524 */       return (this.length == 0L || super.isEmpty());
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Long> sizeIfKnown() {
/* 529 */       Optional<Long> optionalUnslicedSize = ByteSource.this.sizeIfKnown();
/* 530 */       if (optionalUnslicedSize.isPresent()) {
/* 531 */         long unslicedSize = ((Long)optionalUnslicedSize.get()).longValue();
/* 532 */         long off = Math.min(this.offset, unslicedSize);
/* 533 */         return Optional.of(Long.valueOf(Math.min(this.length, unslicedSize - off)));
/*     */       } 
/* 535 */       return Optional.absent();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 540 */       return ByteSource.this.toString() + ".slice(" + this.offset + ", " + this.length + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ByteArrayByteSource
/*     */     extends ByteSource {
/*     */     final byte[] bytes;
/*     */     final int offset;
/*     */     final int length;
/*     */     
/*     */     ByteArrayByteSource(byte[] bytes) {
/* 551 */       this(bytes, 0, bytes.length);
/*     */     }
/*     */ 
/*     */     
/*     */     ByteArrayByteSource(byte[] bytes, int offset, int length) {
/* 556 */       this.bytes = bytes;
/* 557 */       this.offset = offset;
/* 558 */       this.length = length;
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream openStream() {
/* 563 */       return new ByteArrayInputStream(this.bytes, this.offset, this.length);
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream openBufferedStream() throws IOException {
/* 568 */       return openStream();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 573 */       return (this.length == 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public long size() {
/* 578 */       return this.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Long> sizeIfKnown() {
/* 583 */       return Optional.of(Long.valueOf(this.length));
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] read() {
/* 588 */       return Arrays.copyOfRange(this.bytes, this.offset, this.offset + this.length);
/*     */     }
/*     */ 
/*     */     
/*     */     public long copyTo(OutputStream output) throws IOException {
/* 593 */       output.write(this.bytes, this.offset, this.length);
/* 594 */       return this.length;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> T read(ByteProcessor<T> processor) throws IOException {
/* 600 */       processor.processBytes(this.bytes, this.offset, this.length);
/* 601 */       return processor.getResult();
/*     */     }
/*     */ 
/*     */     
/*     */     public HashCode hash(HashFunction hashFunction) throws IOException {
/* 606 */       return hashFunction.hashBytes(this.bytes, this.offset, this.length);
/*     */     }
/*     */ 
/*     */     
/*     */     public ByteSource slice(long offset, long length) {
/* 611 */       Preconditions.checkArgument((offset >= 0L), "offset (%s) may not be negative", offset);
/* 612 */       Preconditions.checkArgument((length >= 0L), "length (%s) may not be negative", length);
/*     */       
/* 614 */       offset = Math.min(offset, this.length);
/* 615 */       length = Math.min(length, this.length - offset);
/* 616 */       int newOffset = this.offset + (int)offset;
/* 617 */       return new ByteArrayByteSource(this.bytes, newOffset, (int)length);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 622 */       return "ByteSource.wrap(" + 
/* 623 */         Ascii.truncate(BaseEncoding.base16().encode(this.bytes, this.offset, this.length), 30, "...") + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class EmptyByteSource
/*     */     extends ByteArrayByteSource {
/* 629 */     static final EmptyByteSource INSTANCE = new EmptyByteSource();
/*     */     
/*     */     EmptyByteSource() {
/* 632 */       super(new byte[0]);
/*     */     }
/*     */ 
/*     */     
/*     */     public CharSource asCharSource(Charset charset) {
/* 637 */       Preconditions.checkNotNull(charset);
/* 638 */       return CharSource.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] read() {
/* 643 */       return this.bytes;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 648 */       return "ByteSource.empty()";
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class ConcatenatedByteSource
/*     */     extends ByteSource {
/*     */     final Iterable<? extends ByteSource> sources;
/*     */     
/*     */     ConcatenatedByteSource(Iterable<? extends ByteSource> sources) {
/* 657 */       this.sources = (Iterable<? extends ByteSource>)Preconditions.checkNotNull(sources);
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream openStream() throws IOException {
/* 662 */       return new MultiInputStream(this.sources.iterator());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() throws IOException {
/* 667 */       for (ByteSource source : this.sources) {
/* 668 */         if (!source.isEmpty()) {
/* 669 */           return false;
/*     */         }
/*     */       } 
/* 672 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Long> sizeIfKnown() {
/* 677 */       long result = 0L;
/* 678 */       for (ByteSource source : this.sources) {
/* 679 */         Optional<Long> sizeIfKnown = source.sizeIfKnown();
/* 680 */         if (!sizeIfKnown.isPresent()) {
/* 681 */           return Optional.absent();
/*     */         }
/* 683 */         result += ((Long)sizeIfKnown.get()).longValue();
/*     */       } 
/* 685 */       return Optional.of(Long.valueOf(result));
/*     */     }
/*     */ 
/*     */     
/*     */     public long size() throws IOException {
/* 690 */       long result = 0L;
/* 691 */       for (ByteSource source : this.sources) {
/* 692 */         result += source.size();
/*     */       }
/* 694 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 699 */       return "ByteSource.concat(" + this.sources + ")";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\io\ByteSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */