/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Ascii;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.AbstractIterator;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class CharSource
/*     */ {
/*     */   @Beta
/*     */   public ByteSource asByteSource(Charset charset) {
/*  85 */     return new AsByteSource(charset);
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
/*     */   public abstract Reader openStream() throws IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BufferedReader openBufferedStream() throws IOException {
/* 107 */     Reader reader = openStream();
/* 108 */     return (reader instanceof BufferedReader) ? (BufferedReader)reader : new BufferedReader(reader);
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
/*     */   @Beta
/*     */   public Optional<Long> lengthIfKnown() {
/* 129 */     return Optional.absent();
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
/*     */   @Beta
/*     */   public long length() throws IOException {
/* 153 */     Optional<Long> lengthIfKnown = lengthIfKnown();
/* 154 */     if (lengthIfKnown.isPresent()) {
/* 155 */       return ((Long)lengthIfKnown.get()).longValue();
/*     */     }
/*     */     
/* 158 */     Closer closer = Closer.create();
/*     */     try {
/* 160 */       Reader reader = closer.<Reader>register(openStream());
/* 161 */       return countBySkipping(reader);
/* 162 */     } catch (Throwable e) {
/* 163 */       throw closer.rethrow(e);
/*     */     } finally {
/* 165 */       closer.close();
/*     */     } 
/*     */   }
/*     */   
/*     */   private long countBySkipping(Reader reader) throws IOException {
/* 170 */     long count = 0L;
/*     */     long read;
/* 172 */     while ((read = reader.skip(Long.MAX_VALUE)) != 0L) {
/* 173 */       count += read;
/*     */     }
/* 175 */     return count;
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
/*     */   public long copyTo(Appendable appendable) throws IOException {
/* 188 */     Preconditions.checkNotNull(appendable);
/*     */     
/* 190 */     Closer closer = Closer.create();
/*     */     try {
/* 192 */       Reader reader = closer.<Reader>register(openStream());
/* 193 */       return CharStreams.copy(reader, appendable);
/* 194 */     } catch (Throwable e) {
/* 195 */       throw closer.rethrow(e);
/*     */     } finally {
/* 197 */       closer.close();
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
/*     */   public long copyTo(CharSink sink) throws IOException {
/* 210 */     Preconditions.checkNotNull(sink);
/*     */     
/* 212 */     Closer closer = Closer.create();
/*     */     try {
/* 214 */       Reader reader = closer.<Reader>register(openStream());
/* 215 */       Writer writer = closer.<Writer>register(sink.openStream());
/* 216 */       return CharStreams.copy(reader, writer);
/* 217 */     } catch (Throwable e) {
/* 218 */       throw closer.rethrow(e);
/*     */     } finally {
/* 220 */       closer.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String read() throws IOException {
/* 230 */     Closer closer = Closer.create();
/*     */     try {
/* 232 */       Reader reader = closer.<Reader>register(openStream());
/* 233 */       return CharStreams.toString(reader);
/* 234 */     } catch (Throwable e) {
/* 235 */       throw closer.rethrow(e);
/*     */     } finally {
/* 237 */       closer.close();
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
/*     */   @Nullable
/*     */   public String readFirstLine() throws IOException {
/* 252 */     Closer closer = Closer.create();
/*     */     try {
/* 254 */       BufferedReader reader = closer.<BufferedReader>register(openBufferedStream());
/* 255 */       return reader.readLine();
/* 256 */     } catch (Throwable e) {
/* 257 */       throw closer.rethrow(e);
/*     */     } finally {
/* 259 */       closer.close();
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
/*     */   public ImmutableList<String> readLines() throws IOException {
/* 274 */     Closer closer = Closer.create();
/*     */     try {
/* 276 */       BufferedReader reader = closer.<BufferedReader>register(openBufferedStream());
/* 277 */       List<String> result = Lists.newArrayList();
/*     */       String line;
/* 279 */       while ((line = reader.readLine()) != null) {
/* 280 */         result.add(line);
/*     */       }
/* 282 */       return ImmutableList.copyOf(result);
/* 283 */     } catch (Throwable e) {
/* 284 */       throw closer.rethrow(e);
/*     */     } finally {
/* 286 */       closer.close();
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
/*     */   @CanIgnoreReturnValue
/*     */   public <T> T readLines(LineProcessor<T> processor) throws IOException {
/* 307 */     Preconditions.checkNotNull(processor);
/*     */     
/* 309 */     Closer closer = Closer.create();
/*     */     try {
/* 311 */       Reader reader = closer.<Reader>register(openStream());
/* 312 */       return (T)CharStreams.readLines(reader, (LineProcessor)processor);
/* 313 */     } catch (Throwable e) {
/* 314 */       throw closer.rethrow(e);
/*     */     } finally {
/* 316 */       closer.close();
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
/*     */   public boolean isEmpty() throws IOException {
/* 333 */     Optional<Long> lengthIfKnown = lengthIfKnown();
/* 334 */     if (lengthIfKnown.isPresent() && ((Long)lengthIfKnown.get()).longValue() == 0L) {
/* 335 */       return true;
/*     */     }
/* 337 */     Closer closer = Closer.create();
/*     */     try {
/* 339 */       Reader reader = closer.<Reader>register(openStream());
/* 340 */       return (reader.read() == -1);
/* 341 */     } catch (Throwable e) {
/* 342 */       throw closer.rethrow(e);
/*     */     } finally {
/* 344 */       closer.close();
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
/*     */   public static CharSource concat(Iterable<? extends CharSource> sources) {
/* 360 */     return new ConcatenatedCharSource(sources);
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
/*     */   public static CharSource concat(Iterator<? extends CharSource> sources) {
/* 382 */     return concat((Iterable<? extends CharSource>)ImmutableList.copyOf(sources));
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
/*     */   public static CharSource concat(CharSource... sources) {
/* 398 */     return concat((Iterable<? extends CharSource>)ImmutableList.copyOf((Object[])sources));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CharSource wrap(CharSequence charSequence) {
/* 409 */     return new CharSequenceCharSource(charSequence);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CharSource empty() {
/* 418 */     return EmptyCharSource.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   private final class AsByteSource
/*     */     extends ByteSource
/*     */   {
/*     */     final Charset charset;
/*     */ 
/*     */     
/*     */     AsByteSource(Charset charset) {
/* 429 */       this.charset = (Charset)Preconditions.checkNotNull(charset);
/*     */     }
/*     */ 
/*     */     
/*     */     public CharSource asCharSource(Charset charset) {
/* 434 */       if (charset.equals(this.charset)) {
/* 435 */         return CharSource.this;
/*     */       }
/* 437 */       return super.asCharSource(charset);
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream openStream() throws IOException {
/* 442 */       return new ReaderInputStream(CharSource.this.openStream(), this.charset, 8192);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 447 */       return CharSource.this.toString() + ".asByteSource(" + this.charset + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CharSequenceCharSource
/*     */     extends CharSource {
/* 453 */     private static final Splitter LINE_SPLITTER = Splitter.onPattern("\r\n|\n|\r");
/*     */     
/*     */     private final CharSequence seq;
/*     */     
/*     */     protected CharSequenceCharSource(CharSequence seq) {
/* 458 */       this.seq = (CharSequence)Preconditions.checkNotNull(seq);
/*     */     }
/*     */ 
/*     */     
/*     */     public Reader openStream() {
/* 463 */       return new CharSequenceReader(this.seq);
/*     */     }
/*     */ 
/*     */     
/*     */     public String read() {
/* 468 */       return this.seq.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 473 */       return (this.seq.length() == 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public long length() {
/* 478 */       return this.seq.length();
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Long> lengthIfKnown() {
/* 483 */       return Optional.of(Long.valueOf(this.seq.length()));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Iterable<String> lines() {
/* 491 */       return new Iterable<String>()
/*     */         {
/*     */           public Iterator<String> iterator() {
/* 494 */             return (Iterator<String>)new AbstractIterator<String>() {
/* 495 */                 Iterator<String> lines = CharSource.CharSequenceCharSource.LINE_SPLITTER.split(CharSource.CharSequenceCharSource.this.seq).iterator();
/*     */ 
/*     */                 
/*     */                 protected String computeNext() {
/* 499 */                   if (this.lines.hasNext()) {
/* 500 */                     String next = this.lines.next();
/*     */                     
/* 502 */                     if (this.lines.hasNext() || !next.isEmpty()) {
/* 503 */                       return next;
/*     */                     }
/*     */                   } 
/* 506 */                   return (String)endOfData();
/*     */                 }
/*     */               };
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public String readFirstLine() {
/* 515 */       Iterator<String> lines = lines().iterator();
/* 516 */       return lines.hasNext() ? lines.next() : null;
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableList<String> readLines() {
/* 521 */       return ImmutableList.copyOf(lines());
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> T readLines(LineProcessor<T> processor) throws IOException {
/* 526 */       for (String line : lines()) {
/* 527 */         if (!processor.processLine(line)) {
/*     */           break;
/*     */         }
/*     */       } 
/* 531 */       return processor.getResult();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 536 */       return "CharSource.wrap(" + Ascii.truncate(this.seq, 30, "...") + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class EmptyCharSource
/*     */     extends CharSequenceCharSource {
/* 542 */     private static final EmptyCharSource INSTANCE = new EmptyCharSource();
/*     */     
/*     */     private EmptyCharSource() {
/* 545 */       super("");
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 550 */       return "CharSource.empty()";
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class ConcatenatedCharSource
/*     */     extends CharSource {
/*     */     private final Iterable<? extends CharSource> sources;
/*     */     
/*     */     ConcatenatedCharSource(Iterable<? extends CharSource> sources) {
/* 559 */       this.sources = (Iterable<? extends CharSource>)Preconditions.checkNotNull(sources);
/*     */     }
/*     */ 
/*     */     
/*     */     public Reader openStream() throws IOException {
/* 564 */       return new MultiReader(this.sources.iterator());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() throws IOException {
/* 569 */       for (CharSource source : this.sources) {
/* 570 */         if (!source.isEmpty()) {
/* 571 */           return false;
/*     */         }
/*     */       } 
/* 574 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Long> lengthIfKnown() {
/* 579 */       long result = 0L;
/* 580 */       for (CharSource source : this.sources) {
/* 581 */         Optional<Long> lengthIfKnown = source.lengthIfKnown();
/* 582 */         if (!lengthIfKnown.isPresent()) {
/* 583 */           return Optional.absent();
/*     */         }
/* 585 */         result += ((Long)lengthIfKnown.get()).longValue();
/*     */       } 
/* 587 */       return Optional.of(Long.valueOf(result));
/*     */     }
/*     */ 
/*     */     
/*     */     public long length() throws IOException {
/* 592 */       long result = 0L;
/* 593 */       for (CharSource source : this.sources) {
/* 594 */         result += source.length();
/*     */       }
/* 596 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 601 */       return "CharSource.concat(" + this.sources + ")";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\io\CharSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */