/*    */ package net.minecraft.server;
/*    */ 
/*    */ import java.io.OutputStream;
/*    */ import java.io.PrintStream;
/*    */ import javax.annotation.Nullable;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class LoggedPrintStream
/*    */   extends PrintStream {
/* 11 */   protected static final Logger LOGGER = LogManager.getLogger();
/*    */   
/*    */   protected final String name;
/*    */   
/*    */   public LoggedPrintStream(String debug1, OutputStream debug2) {
/* 16 */     super(debug2);
/* 17 */     this.name = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void println(@Nullable String debug1) {
/* 22 */     logLine(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void println(Object debug1) {
/* 27 */     logLine(String.valueOf(debug1));
/*    */   }
/*    */   
/*    */   protected void logLine(@Nullable String debug1) {
/* 31 */     LOGGER.info("[{}]: {}", this.name, debug1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\LoggedPrintStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */