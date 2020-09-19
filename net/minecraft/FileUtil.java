/*    */ package net.minecraft;
/*    */ 
/*    */ import java.nio.file.InvalidPathException;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.Paths;
/*    */ import java.util.regex.Pattern;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FileUtil
/*    */ {
/* 13 */   private static final Pattern COPY_COUNTER_PATTERN = Pattern.compile("(<name>.*) \\((<count>\\d*)\\)", 66);
/*    */ 
/*    */   
/* 16 */   private static final Pattern RESERVED_WINDOWS_FILENAMES = Pattern.compile(".*\\.|(?:COM|CLOCK\\$|CON|PRN|AUX|NUL|COM[1-9]|LPT[1-9])(?:\\..*)?", 2);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean isPathNormalized(Path debug0) {
/* 66 */     Path debug1 = debug0.normalize();
/* 67 */     return debug1.equals(debug0);
/*    */   }
/*    */   
/*    */   public static boolean isPathPortable(Path debug0) {
/* 71 */     for (Path debug2 : debug0) {
/* 72 */       if (RESERVED_WINDOWS_FILENAMES.matcher(debug2.toString()).matches()) {
/* 73 */         return false;
/*    */       }
/*    */     } 
/*    */     
/* 77 */     return true;
/*    */   }
/*    */   
/*    */   public static Path createPathToResource(Path debug0, String debug1, String debug2) {
/* 81 */     String debug3 = debug1 + debug2;
/* 82 */     Path debug4 = Paths.get(debug3, new String[0]);
/*    */     
/* 84 */     if (debug4.endsWith(debug2)) {
/* 85 */       throw new InvalidPathException(debug3, "empty resource name");
/*    */     }
/*    */     
/* 88 */     return debug0.resolve(debug4);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\FileUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */