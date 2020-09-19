/*    */ package org.apache.logging.log4j.core.net.server;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.ObjectMapper;
/*    */ import java.nio.charset.Charset;
/*    */ import org.apache.logging.log4j.core.jackson.Log4jJsonObjectMapper;
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
/*    */ public class JsonInputStreamLogEventBridge
/*    */   extends InputStreamLogEventBridge
/*    */ {
/* 31 */   private static final int[] END_PAIR = new int[] { -1, -1 };
/*    */   private static final char EVENT_END_MARKER = '}';
/*    */   private static final char EVENT_START_MARKER = '{';
/*    */   private static final char JSON_ESC = '\\';
/*    */   private static final char JSON_STR_DELIM = '"';
/*    */   private static final boolean THREAD_CONTEXT_MAP_AS_LIST = false;
/*    */   
/*    */   public JsonInputStreamLogEventBridge() {
/* 39 */     this(1024, Charset.defaultCharset());
/*    */   }
/*    */   
/*    */   public JsonInputStreamLogEventBridge(int bufferSize, Charset charset) {
/* 43 */     super((ObjectMapper)new Log4jJsonObjectMapper(false, true), bufferSize, charset, String.valueOf('}'));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected int[] getEventIndices(String text, int beginIndex) {
/* 50 */     int start = text.indexOf('{', beginIndex);
/* 51 */     if (start == -1) {
/* 52 */       return END_PAIR;
/*    */     }
/* 54 */     char[] charArray = text.toCharArray();
/* 55 */     int stack = 0;
/* 56 */     boolean inStr = false;
/* 57 */     boolean inEsc = false;
/* 58 */     for (int i = start; i < charArray.length; i++) {
/* 59 */       char c = charArray[i];
/* 60 */       if (inEsc) {
/*    */         
/* 62 */         inEsc = false;
/*    */       } else {
/* 64 */         switch (c) {
/*    */           case '{':
/* 66 */             if (!inStr) {
/* 67 */               stack++;
/*    */             }
/*    */             break;
/*    */           case '}':
/* 71 */             if (!inStr) {
/* 72 */               stack--;
/*    */             }
/*    */             break;
/*    */           case '"':
/* 76 */             inStr = !inStr;
/*    */             break;
/*    */           case '\\':
/* 79 */             inEsc = true;
/*    */             break;
/*    */         } 
/* 82 */         if (stack == 0) {
/* 83 */           return new int[] { start, i };
/*    */         }
/*    */       } 
/*    */     } 
/* 87 */     return END_PAIR;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\net\server\JsonInputStreamLogEventBridge.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */