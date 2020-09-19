/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.DataInput;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutput;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.zip.GZIPInputStream;
/*     */ import java.util.zip.GZIPOutputStream;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ 
/*     */ 
/*     */ public class NbtIo
/*     */ {
/*     */   public static CompoundTag readCompressed(File debug0) throws IOException {
/*  25 */     try (InputStream debug1 = new FileInputStream(debug0)) {
/*  26 */       return readCompressed(debug1);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static CompoundTag readCompressed(InputStream debug0) throws IOException {
/*  31 */     try (DataInputStream debug1 = new DataInputStream(new BufferedInputStream(new GZIPInputStream(debug0)))) {
/*  32 */       return read(debug1, NbtAccounter.UNLIMITED);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void writeCompressed(CompoundTag debug0, File debug1) throws IOException {
/*  37 */     try (OutputStream debug2 = new FileOutputStream(debug1)) {
/*  38 */       writeCompressed(debug0, debug2);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void writeCompressed(CompoundTag debug0, OutputStream debug1) throws IOException {
/*  43 */     try (DataOutputStream debug2 = new DataOutputStream(new BufferedOutputStream(new GZIPOutputStream(debug1)))) {
/*  44 */       write(debug0, debug2);
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
/*     */   
/*     */   public static CompoundTag read(DataInput debug0) throws IOException {
/*  71 */     return read(debug0, NbtAccounter.UNLIMITED);
/*     */   }
/*     */   
/*     */   public static CompoundTag read(DataInput debug0, NbtAccounter debug1) throws IOException {
/*  75 */     Tag debug2 = readUnnamedTag(debug0, 0, debug1);
/*  76 */     if (debug2 instanceof CompoundTag) {
/*  77 */       return (CompoundTag)debug2;
/*     */     }
/*  79 */     throw new IOException("Root tag must be a named compound tag");
/*     */   }
/*     */   
/*     */   public static void write(CompoundTag debug0, DataOutput debug1) throws IOException {
/*  83 */     writeUnnamedTag(debug0, debug1);
/*     */   }
/*     */   
/*     */   private static void writeUnnamedTag(Tag debug0, DataOutput debug1) throws IOException {
/*  87 */     debug1.writeByte(debug0.getId());
/*  88 */     if (debug0.getId() == 0) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  93 */     debug1.writeUTF("");
/*     */     
/*  95 */     debug0.write(debug1);
/*     */   }
/*     */   
/*     */   private static Tag readUnnamedTag(DataInput debug0, int debug1, NbtAccounter debug2) throws IOException {
/*  99 */     byte debug3 = debug0.readByte();
/* 100 */     if (debug3 == 0) {
/* 101 */       return EndTag.INSTANCE;
/*     */     }
/*     */ 
/*     */     
/* 105 */     debug0.readUTF();
/*     */     
/*     */     try {
/* 108 */       return (Tag)TagTypes.getType(debug3).load(debug0, debug1, debug2);
/* 109 */     } catch (IOException debug4) {
/* 110 */       CrashReport debug5 = CrashReport.forThrowable(debug4, "Loading NBT data");
/* 111 */       CrashReportCategory debug6 = debug5.addCategory("NBT Tag");
/* 112 */       debug6.setDetail("Tag type", Byte.valueOf(debug3));
/* 113 */       throw new ReportedException(debug5);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\nbt\NbtIo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */