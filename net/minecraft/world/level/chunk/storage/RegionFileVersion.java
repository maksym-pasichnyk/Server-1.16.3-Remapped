/*    */ package net.minecraft.world.level.chunk.storage;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RegionFileVersion
/*    */ {
/* 16 */   private static final Int2ObjectMap<RegionFileVersion> VERSIONS = (Int2ObjectMap<RegionFileVersion>)new Int2ObjectOpenHashMap();
/*    */   
/* 18 */   public static final RegionFileVersion VERSION_GZIP = register(new RegionFileVersion(1, java.util.zip.GZIPInputStream::new, java.util.zip.GZIPOutputStream::new));
/* 19 */   public static final RegionFileVersion VERSION_DEFLATE = register(new RegionFileVersion(2, java.util.zip.InflaterInputStream::new, java.util.zip.DeflaterOutputStream::new)); public static final RegionFileVersion VERSION_NONE; static {
/* 20 */     VERSION_NONE = register(new RegionFileVersion(3, debug0 -> debug0, debug0 -> debug0));
/*    */   }
/*    */   private final int id;
/*    */   private final StreamWrapper<InputStream> inputWrapper;
/*    */   private final StreamWrapper<OutputStream> outputWrapper;
/*    */   
/*    */   private RegionFileVersion(int debug1, StreamWrapper<InputStream> debug2, StreamWrapper<OutputStream> debug3) {
/* 27 */     this.id = debug1;
/* 28 */     this.inputWrapper = debug2;
/* 29 */     this.outputWrapper = debug3;
/*    */   }
/*    */   
/*    */   private static RegionFileVersion register(RegionFileVersion debug0) {
/* 33 */     VERSIONS.put(debug0.id, debug0);
/* 34 */     return debug0;
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public static RegionFileVersion fromId(int debug0) {
/* 39 */     return (RegionFileVersion)VERSIONS.get(debug0);
/*    */   }
/*    */   
/*    */   public static boolean isValidVersion(int debug0) {
/* 43 */     return VERSIONS.containsKey(debug0);
/*    */   }
/*    */   
/*    */   public int getId() {
/* 47 */     return this.id;
/*    */   }
/*    */   
/*    */   public OutputStream wrap(OutputStream debug1) throws IOException {
/* 51 */     return this.outputWrapper.wrap(debug1);
/*    */   }
/*    */   
/*    */   public InputStream wrap(InputStream debug1) throws IOException {
/* 55 */     return this.inputWrapper.wrap(debug1);
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   static interface StreamWrapper<O> {
/*    */     O wrap(O param1O) throws IOException;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\chunk\storage\RegionFileVersion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */