/*     */ package org.apache.logging.log4j.core.layout;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
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
/*     */ @Plugin(name = "SerializedLayout", category = "Core", elementType = "layout", printObject = true)
/*     */ public final class SerializedLayout
/*     */   extends AbstractLayout<LogEvent>
/*     */ {
/*     */   private static byte[] serializedHeader;
/*     */   
/*     */   static {
/*  39 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
/*     */     try {
/*  41 */       (new ObjectOutputStream(baos)).close();
/*  42 */       serializedHeader = baos.toByteArray();
/*  43 */     } catch (Exception ex) {
/*  44 */       LOGGER.error("Unable to generate Object stream header", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private SerializedLayout() {
/*  49 */     super(null, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] toByteArray(LogEvent event) {
/*  60 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
/*  61 */     try (ObjectOutputStream oos = new PrivateObjectOutputStream(baos)) {
/*  62 */       oos.writeObject(event);
/*  63 */       oos.reset();
/*  64 */     } catch (IOException ioe) {
/*  65 */       LOGGER.error("Serialization of LogEvent failed.", ioe);
/*     */     } 
/*  67 */     return baos.toByteArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LogEvent toSerializable(LogEvent event) {
/*  78 */     return event;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginFactory
/*     */   public static SerializedLayout createLayout() {
/*  87 */     return new SerializedLayout();
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getHeader() {
/*  92 */     return serializedHeader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getContentType() {
/* 101 */     return "application/octet-stream";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class PrivateObjectOutputStream
/*     */     extends ObjectOutputStream
/*     */   {
/*     */     public PrivateObjectOutputStream(OutputStream os) throws IOException {
/* 110 */       super(os);
/*     */     }
/*     */     
/*     */     protected void writeStreamHeader() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\layout\SerializedLayout.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */