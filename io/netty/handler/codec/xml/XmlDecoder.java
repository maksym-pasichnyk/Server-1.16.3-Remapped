/*     */ package io.netty.handler.codec.xml;
/*     */ 
/*     */ import com.fasterxml.aalto.AsyncByteArrayFeeder;
/*     */ import com.fasterxml.aalto.AsyncXMLInputFactory;
/*     */ import com.fasterxml.aalto.AsyncXMLStreamReader;
/*     */ import com.fasterxml.aalto.stax.InputFactoryImpl;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.ByteToMessageDecoder;
/*     */ import java.util.List;
/*     */ import javax.xml.stream.XMLStreamException;
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
/*     */ public class XmlDecoder
/*     */   extends ByteToMessageDecoder
/*     */ {
/*  38 */   private static final AsyncXMLInputFactory XML_INPUT_FACTORY = (AsyncXMLInputFactory)new InputFactoryImpl();
/*  39 */   private static final XmlDocumentEnd XML_DOCUMENT_END = XmlDocumentEnd.INSTANCE;
/*     */   
/*  41 */   private final AsyncXMLStreamReader<AsyncByteArrayFeeder> streamReader = XML_INPUT_FACTORY.createAsyncForByteArray();
/*  42 */   private final AsyncByteArrayFeeder streamFeeder = (AsyncByteArrayFeeder)this.streamReader.getInputFeeder();
/*     */ 
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
/*  46 */     byte[] buffer = new byte[in.readableBytes()];
/*  47 */     in.readBytes(buffer);
/*     */     try {
/*  49 */       this.streamFeeder.feedInput(buffer, 0, buffer.length);
/*  50 */     } catch (XMLStreamException exception) {
/*  51 */       in.skipBytes(in.readableBytes());
/*  52 */       throw exception;
/*     */     } 
/*     */     
/*  55 */     while (!this.streamFeeder.needMoreInput()) {
/*  56 */       XmlElementStart elementStart; int x; XmlElementEnd elementEnd; int i, type = this.streamReader.next();
/*  57 */       switch (type) {
/*     */         case 7:
/*  59 */           out.add(new XmlDocumentStart(this.streamReader.getEncoding(), this.streamReader.getVersion(), this.streamReader
/*  60 */                 .isStandalone(), this.streamReader.getCharacterEncodingScheme()));
/*     */         
/*     */         case 8:
/*  63 */           out.add(XML_DOCUMENT_END);
/*     */ 
/*     */         
/*     */         case 1:
/*  67 */           elementStart = new XmlElementStart(this.streamReader.getLocalName(), this.streamReader.getName().getNamespaceURI(), this.streamReader.getPrefix());
/*  68 */           for (x = 0; x < this.streamReader.getAttributeCount(); x++) {
/*     */ 
/*     */             
/*  71 */             XmlAttribute attribute = new XmlAttribute(this.streamReader.getAttributeType(x), this.streamReader.getAttributeLocalName(x), this.streamReader.getAttributePrefix(x), this.streamReader.getAttributeNamespace(x), this.streamReader.getAttributeValue(x));
/*  72 */             elementStart.attributes().add(attribute);
/*     */           } 
/*  74 */           for (x = 0; x < this.streamReader.getNamespaceCount(); x++) {
/*     */             
/*  76 */             XmlNamespace namespace = new XmlNamespace(this.streamReader.getNamespacePrefix(x), this.streamReader.getNamespaceURI(x));
/*  77 */             elementStart.namespaces().add(namespace);
/*     */           } 
/*  79 */           out.add(elementStart);
/*     */ 
/*     */         
/*     */         case 2:
/*  83 */           elementEnd = new XmlElementEnd(this.streamReader.getLocalName(), this.streamReader.getName().getNamespaceURI(), this.streamReader.getPrefix());
/*  84 */           for (i = 0; i < this.streamReader.getNamespaceCount(); i++) {
/*     */             
/*  86 */             XmlNamespace namespace = new XmlNamespace(this.streamReader.getNamespacePrefix(i), this.streamReader.getNamespaceURI(i));
/*  87 */             elementEnd.namespaces().add(namespace);
/*     */           } 
/*  89 */           out.add(elementEnd);
/*     */         
/*     */         case 3:
/*  92 */           out.add(new XmlProcessingInstruction(this.streamReader.getPIData(), this.streamReader.getPITarget()));
/*     */         
/*     */         case 4:
/*  95 */           out.add(new XmlCharacters(this.streamReader.getText()));
/*     */         
/*     */         case 5:
/*  98 */           out.add(new XmlComment(this.streamReader.getText()));
/*     */         
/*     */         case 6:
/* 101 */           out.add(new XmlSpace(this.streamReader.getText()));
/*     */         
/*     */         case 9:
/* 104 */           out.add(new XmlEntityReference(this.streamReader.getLocalName(), this.streamReader.getText()));
/*     */         
/*     */         case 11:
/* 107 */           out.add(new XmlDTD(this.streamReader.getText()));
/*     */         
/*     */         case 12:
/* 110 */           out.add(new XmlCdata(this.streamReader.getText()));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\xml\XmlDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */