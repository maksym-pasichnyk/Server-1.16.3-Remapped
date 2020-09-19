/*    */ package net.minecraft.server.rcon;
/*    */ 
/*    */ import java.nio.charset.StandardCharsets;
/*    */ 
/*    */ public class PktUtils
/*    */ {
/*  7 */   public static final char[] HEX_CHAR = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
/*    */ 
/*    */ 
/*    */   
/*    */   public static String stringFromByteArray(byte[] debug0, int debug1, int debug2) {
/* 12 */     int debug3 = debug2 - 1;
/* 13 */     int debug4 = (debug1 > debug3) ? debug3 : debug1;
/* 14 */     while (0 != debug0[debug4] && debug4 < debug3) {
/* 15 */       debug4++;
/*    */     }
/*    */     
/* 18 */     return new String(debug0, debug1, debug4 - debug1, StandardCharsets.UTF_8);
/*    */   }
/*    */   
/*    */   public static int intFromByteArray(byte[] debug0, int debug1) {
/* 22 */     return intFromByteArray(debug0, debug1, debug0.length);
/*    */   }
/*    */   
/*    */   public static int intFromByteArray(byte[] debug0, int debug1, int debug2) {
/* 26 */     if (0 > debug2 - debug1 - 4)
/*    */     {
/*    */       
/* 29 */       return 0;
/*    */     }
/* 31 */     return debug0[debug1 + 3] << 24 | (debug0[debug1 + 2] & 0xFF) << 16 | (debug0[debug1 + 1] & 0xFF) << 8 | debug0[debug1] & 0xFF;
/*    */   }
/*    */   
/*    */   public static int intFromNetworkByteArray(byte[] debug0, int debug1, int debug2) {
/* 35 */     if (0 > debug2 - debug1 - 4)
/*    */     {
/*    */       
/* 38 */       return 0;
/*    */     }
/* 40 */     return debug0[debug1] << 24 | (debug0[debug1 + 1] & 0xFF) << 16 | (debug0[debug1 + 2] & 0xFF) << 8 | debug0[debug1 + 3] & 0xFF;
/*    */   }
/*    */   
/*    */   public static String toHexString(byte debug0) {
/* 44 */     return "" + HEX_CHAR[(debug0 & 0xF0) >>> 4] + HEX_CHAR[debug0 & 0xF];
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\rcon\PktUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */