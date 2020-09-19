/*     */ package org.apache.commons.io.output;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileWriterWithEncoding
/*     */   extends Writer
/*     */ {
/*     */   private final Writer out;
/*     */   
/*     */   public FileWriterWithEncoding(String filename, String encoding) throws IOException {
/*  66 */     this(new File(filename), encoding, false);
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
/*     */   public FileWriterWithEncoding(String filename, String encoding, boolean append) throws IOException {
/*  80 */     this(new File(filename), encoding, append);
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
/*     */   public FileWriterWithEncoding(String filename, Charset encoding) throws IOException {
/*  92 */     this(new File(filename), encoding, false);
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
/*     */   public FileWriterWithEncoding(String filename, Charset encoding, boolean append) throws IOException {
/* 106 */     this(new File(filename), encoding, append);
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
/*     */   public FileWriterWithEncoding(String filename, CharsetEncoder encoding) throws IOException {
/* 118 */     this(new File(filename), encoding, false);
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
/*     */   public FileWriterWithEncoding(String filename, CharsetEncoder encoding, boolean append) throws IOException {
/* 132 */     this(new File(filename), encoding, append);
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
/*     */   public FileWriterWithEncoding(File file, String encoding) throws IOException {
/* 144 */     this(file, encoding, false);
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
/*     */   public FileWriterWithEncoding(File file, String encoding, boolean append) throws IOException {
/* 158 */     this.out = initWriter(file, encoding, append);
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
/*     */   public FileWriterWithEncoding(File file, Charset encoding) throws IOException {
/* 170 */     this(file, encoding, false);
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
/*     */   public FileWriterWithEncoding(File file, Charset encoding, boolean append) throws IOException {
/* 184 */     this.out = initWriter(file, encoding, append);
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
/*     */   public FileWriterWithEncoding(File file, CharsetEncoder encoding) throws IOException {
/* 196 */     this(file, encoding, false);
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
/*     */   public FileWriterWithEncoding(File file, CharsetEncoder encoding, boolean append) throws IOException {
/* 211 */     this.out = initWriter(file, encoding, append);
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
/*     */   private static Writer initWriter(File file, Object encoding, boolean append) throws IOException {
/* 227 */     if (file == null) {
/* 228 */       throw new NullPointerException("File is missing");
/*     */     }
/* 230 */     if (encoding == null) {
/* 231 */       throw new NullPointerException("Encoding is missing");
/*     */     }
/* 233 */     boolean fileExistedAlready = file.exists();
/* 234 */     OutputStream stream = null;
/* 235 */     Writer writer = null;
/*     */     try {
/* 237 */       stream = new FileOutputStream(file, append);
/* 238 */       if (encoding instanceof Charset) {
/* 239 */         writer = new OutputStreamWriter(stream, (Charset)encoding);
/* 240 */       } else if (encoding instanceof CharsetEncoder) {
/* 241 */         writer = new OutputStreamWriter(stream, (CharsetEncoder)encoding);
/*     */       } else {
/* 243 */         writer = new OutputStreamWriter(stream, (String)encoding);
/*     */       } 
/* 245 */     } catch (IOException ex) {
/* 246 */       IOUtils.closeQuietly(writer);
/* 247 */       IOUtils.closeQuietly(stream);
/* 248 */       if (!fileExistedAlready) {
/* 249 */         FileUtils.deleteQuietly(file);
/*     */       }
/* 251 */       throw ex;
/* 252 */     } catch (RuntimeException ex) {
/* 253 */       IOUtils.closeQuietly(writer);
/* 254 */       IOUtils.closeQuietly(stream);
/* 255 */       if (!fileExistedAlready) {
/* 256 */         FileUtils.deleteQuietly(file);
/*     */       }
/* 258 */       throw ex;
/*     */     } 
/* 260 */     return writer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(int idx) throws IOException {
/* 271 */     this.out.write(idx);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(char[] chr) throws IOException {
/* 281 */     this.out.write(chr);
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
/*     */   public void write(char[] chr, int st, int end) throws IOException {
/* 293 */     this.out.write(chr, st, end);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(String str) throws IOException {
/* 303 */     this.out.write(str);
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
/*     */   public void write(String str, int st, int end) throws IOException {
/* 315 */     this.out.write(str, st, end);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/* 324 */     this.out.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 333 */     this.out.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\io\output\FileWriterWithEncoding.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */