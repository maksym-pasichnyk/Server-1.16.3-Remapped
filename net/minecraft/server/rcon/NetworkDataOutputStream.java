/*    */ package net.minecraft.server.rcon;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.DataOutputStream;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class NetworkDataOutputStream {
/*    */   private final ByteArrayOutputStream outputStream;
/*    */   private final DataOutputStream dataOutputStream;
/*    */   
/*    */   public NetworkDataOutputStream(int debug1) {
/* 12 */     this.outputStream = new ByteArrayOutputStream(debug1);
/* 13 */     this.dataOutputStream = new DataOutputStream(this.outputStream);
/*    */   }
/*    */   
/*    */   public void writeBytes(byte[] debug1) throws IOException {
/* 17 */     this.dataOutputStream.write(debug1, 0, debug1.length);
/*    */   }
/*    */   
/*    */   public void writeString(String debug1) throws IOException {
/* 21 */     this.dataOutputStream.writeBytes(debug1);
/* 22 */     this.dataOutputStream.write(0);
/*    */   }
/*    */   
/*    */   public void write(int debug1) throws IOException {
/* 26 */     this.dataOutputStream.write(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeShort(short debug1) throws IOException {
/* 31 */     this.dataOutputStream.writeShort(Short.reverseBytes(debug1));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public byte[] toByteArray() {
/* 43 */     return this.outputStream.toByteArray();
/*    */   }
/*    */   
/*    */   public void reset() {
/* 47 */     this.outputStream.reset();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\rcon\NetworkDataOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */