/*    */ package net.minecraft.data;
/*    */ 
/*    */ import com.google.common.hash.HashFunction;
/*    */ import com.google.common.hash.Hashing;
/*    */ import com.google.gson.Gson;
/*    */ import com.google.gson.JsonElement;
/*    */ import java.io.BufferedWriter;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.attribute.FileAttribute;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public interface DataProvider {
/* 15 */   public static final HashFunction SHA1 = Hashing.sha1();
/*    */   
/*    */   void run(HashCache paramHashCache) throws IOException;
/*    */   
/*    */   String getName();
/*    */   
/*    */   static void save(Gson debug0, HashCache debug1, JsonElement debug2, Path debug3) throws IOException {
/* 22 */     String debug4 = debug0.toJson(debug2);
/* 23 */     String debug5 = SHA1.hashUnencodedChars(debug4).toString();
/*    */     
/* 25 */     if (!Objects.equals(debug1.getHash(debug3), debug5) || !Files.exists(debug3, new java.nio.file.LinkOption[0])) {
/* 26 */       Files.createDirectories(debug3.getParent(), (FileAttribute<?>[])new FileAttribute[0]);
/* 27 */       try (BufferedWriter debug6 = Files.newBufferedWriter(debug3, new java.nio.file.OpenOption[0])) {
/* 28 */         debug6.write(debug4);
/*    */       } 
/*    */     } 
/* 31 */     debug1.putNew(debug3, debug5);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\DataProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */