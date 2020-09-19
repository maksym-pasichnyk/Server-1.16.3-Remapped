/*     */ package org.apache.logging.log4j.core.net;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Date;
/*     */ import java.util.Properties;
/*     */ import javax.activation.DataSource;
/*     */ import javax.mail.Authenticator;
/*     */ import javax.mail.BodyPart;
/*     */ import javax.mail.Message;
/*     */ import javax.mail.MessagingException;
/*     */ import javax.mail.Multipart;
/*     */ import javax.mail.PasswordAuthentication;
/*     */ import javax.mail.Session;
/*     */ import javax.mail.Transport;
/*     */ import javax.mail.internet.InternetHeaders;
/*     */ import javax.mail.internet.MimeBodyPart;
/*     */ import javax.mail.internet.MimeMessage;
/*     */ import javax.mail.internet.MimeMultipart;
/*     */ import javax.mail.internet.MimeUtility;
/*     */ import javax.mail.util.ByteArrayDataSource;
/*     */ import org.apache.logging.log4j.LoggingException;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.appender.AbstractManager;
/*     */ import org.apache.logging.log4j.core.appender.ManagerFactory;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.impl.Log4jLogEvent;
/*     */ import org.apache.logging.log4j.core.impl.MutableLogEvent;
/*     */ import org.apache.logging.log4j.core.layout.AbstractStringLayout;
/*     */ import org.apache.logging.log4j.core.layout.PatternLayout;
/*     */ import org.apache.logging.log4j.core.util.CyclicBuffer;
/*     */ import org.apache.logging.log4j.core.util.NameUtil;
/*     */ import org.apache.logging.log4j.core.util.NetUtils;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
/*     */ import org.apache.logging.log4j.util.Strings;
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
/*     */ public class SmtpManager
/*     */   extends AbstractManager
/*     */ {
/*  60 */   private static final SMTPManagerFactory FACTORY = new SMTPManagerFactory();
/*     */   
/*     */   private final Session session;
/*     */   
/*     */   private final CyclicBuffer<LogEvent> buffer;
/*     */   
/*     */   private volatile MimeMessage message;
/*     */   
/*     */   private final FactoryData data;
/*     */ 
/*     */   
/*     */   private static MimeMessage createMimeMessage(FactoryData data, Session session, LogEvent appendEvent) throws MessagingException {
/*  72 */     return (new MimeMessageBuilder(session)).setFrom(data.from).setReplyTo(data.replyto).setRecipients(Message.RecipientType.TO, data.to).setRecipients(Message.RecipientType.CC, data.cc).setRecipients(Message.RecipientType.BCC, data.bcc).setSubject(data.subject.toSerializable(appendEvent)).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SmtpManager(String name, Session session, MimeMessage message, FactoryData data) {
/*  80 */     super(null, name);
/*  81 */     this.session = session;
/*  82 */     this.message = message;
/*  83 */     this.data = data;
/*  84 */     this.buffer = new CyclicBuffer(LogEvent.class, data.numElements);
/*     */   }
/*     */   public void add(LogEvent event) {
/*     */     Log4jLogEvent log4jLogEvent;
/*  88 */     if (event instanceof Log4jLogEvent && event.getMessage() instanceof org.apache.logging.log4j.message.ReusableMessage) {
/*  89 */       ((Log4jLogEvent)event).makeMessageImmutable();
/*  90 */     } else if (event instanceof MutableLogEvent) {
/*  91 */       log4jLogEvent = ((MutableLogEvent)event).createMemento();
/*     */     } 
/*  93 */     this.buffer.add(log4jLogEvent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SmtpManager getSmtpManager(Configuration config, String to, String cc, String bcc, String from, String replyTo, String subject, String protocol, String host, int port, String username, String password, boolean isDebug, String filterName, int numElements) {
/* 103 */     if (Strings.isEmpty(protocol)) {
/* 104 */       protocol = "smtp";
/*     */     }
/*     */     
/* 107 */     StringBuilder sb = new StringBuilder();
/* 108 */     if (to != null) {
/* 109 */       sb.append(to);
/*     */     }
/* 111 */     sb.append(':');
/* 112 */     if (cc != null) {
/* 113 */       sb.append(cc);
/*     */     }
/* 115 */     sb.append(':');
/* 116 */     if (bcc != null) {
/* 117 */       sb.append(bcc);
/*     */     }
/* 119 */     sb.append(':');
/* 120 */     if (from != null) {
/* 121 */       sb.append(from);
/*     */     }
/* 123 */     sb.append(':');
/* 124 */     if (replyTo != null) {
/* 125 */       sb.append(replyTo);
/*     */     }
/* 127 */     sb.append(':');
/* 128 */     if (subject != null) {
/* 129 */       sb.append(subject);
/*     */     }
/* 131 */     sb.append(':');
/* 132 */     sb.append(protocol).append(':').append(host).append(':').append("port").append(':');
/* 133 */     if (username != null) {
/* 134 */       sb.append(username);
/*     */     }
/* 136 */     sb.append(':');
/* 137 */     if (password != null) {
/* 138 */       sb.append(password);
/*     */     }
/* 140 */     sb.append(isDebug ? ":debug:" : "::");
/* 141 */     sb.append(filterName);
/*     */     
/* 143 */     String name = "SMTP:" + NameUtil.md5(sb.toString());
/* 144 */     AbstractStringLayout.Serializer subjectSerializer = PatternLayout.newSerializerBuilder().setConfiguration(config).setPattern(subject).build();
/*     */     
/* 146 */     return (SmtpManager)getManager(name, FACTORY, new FactoryData(to, cc, bcc, from, replyTo, subjectSerializer, protocol, host, port, username, password, isDebug, numElements));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendEvents(Layout<?> layout, LogEvent appendEvent) {
/* 156 */     if (this.message == null) {
/* 157 */       connect(appendEvent);
/*     */     }
/*     */     try {
/* 160 */       LogEvent[] priorEvents = (LogEvent[])this.buffer.removeAll();
/*     */ 
/*     */       
/* 163 */       byte[] rawBytes = formatContentToBytes(priorEvents, appendEvent, layout);
/*     */       
/* 165 */       String contentType = layout.getContentType();
/* 166 */       String encoding = getEncoding(rawBytes, contentType);
/* 167 */       byte[] encodedBytes = encodeContentToBytes(rawBytes, encoding);
/*     */       
/* 169 */       InternetHeaders headers = getHeaders(contentType, encoding);
/* 170 */       MimeMultipart mp = getMimeMultipart(encodedBytes, headers);
/*     */       
/* 172 */       sendMultipartMessage(this.message, mp);
/* 173 */     } catch (MessagingException|IOException|RuntimeException e) {
/* 174 */       logError("Caught exception while sending e-mail notification.", e);
/* 175 */       throw new LoggingException("Error occurred while sending email", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected byte[] formatContentToBytes(LogEvent[] priorEvents, LogEvent appendEvent, Layout<?> layout) throws IOException {
/* 181 */     ByteArrayOutputStream raw = new ByteArrayOutputStream();
/* 182 */     writeContent(priorEvents, appendEvent, layout, raw);
/* 183 */     return raw.toByteArray();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeContent(LogEvent[] priorEvents, LogEvent appendEvent, Layout<?> layout, ByteArrayOutputStream out) throws IOException {
/* 189 */     writeHeader(layout, out);
/* 190 */     writeBuffer(priorEvents, appendEvent, layout, out);
/* 191 */     writeFooter(layout, out);
/*     */   }
/*     */   
/*     */   protected void writeHeader(Layout<?> layout, OutputStream out) throws IOException {
/* 195 */     byte[] header = layout.getHeader();
/* 196 */     if (header != null) {
/* 197 */       out.write(header);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeBuffer(LogEvent[] priorEvents, LogEvent appendEvent, Layout<?> layout, OutputStream out) throws IOException {
/* 203 */     for (LogEvent priorEvent : priorEvents) {
/* 204 */       byte[] arrayOfByte = layout.toByteArray(priorEvent);
/* 205 */       out.write(arrayOfByte);
/*     */     } 
/*     */     
/* 208 */     byte[] bytes = layout.toByteArray(appendEvent);
/* 209 */     out.write(bytes);
/*     */   }
/*     */   
/*     */   protected void writeFooter(Layout<?> layout, OutputStream out) throws IOException {
/* 213 */     byte[] footer = layout.getFooter();
/* 214 */     if (footer != null) {
/* 215 */       out.write(footer);
/*     */     }
/*     */   }
/*     */   
/*     */   protected String getEncoding(byte[] rawBytes, String contentType) {
/* 220 */     ByteArrayDataSource byteArrayDataSource = new ByteArrayDataSource(rawBytes, contentType);
/* 221 */     return MimeUtility.getEncoding((DataSource)byteArrayDataSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected byte[] encodeContentToBytes(byte[] rawBytes, String encoding) throws MessagingException, IOException {
/* 226 */     ByteArrayOutputStream encoded = new ByteArrayOutputStream();
/* 227 */     encodeContent(rawBytes, encoding, encoded);
/* 228 */     return encoded.toByteArray();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void encodeContent(byte[] bytes, String encoding, ByteArrayOutputStream out) throws MessagingException, IOException {
/* 233 */     try (OutputStream encoder = MimeUtility.encode(out, encoding)) {
/* 234 */       encoder.write(bytes);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected InternetHeaders getHeaders(String contentType, String encoding) {
/* 239 */     InternetHeaders headers = new InternetHeaders();
/* 240 */     headers.setHeader("Content-Type", contentType + "; charset=UTF-8");
/* 241 */     headers.setHeader("Content-Transfer-Encoding", encoding);
/* 242 */     return headers;
/*     */   }
/*     */ 
/*     */   
/*     */   protected MimeMultipart getMimeMultipart(byte[] encodedBytes, InternetHeaders headers) throws MessagingException {
/* 247 */     MimeMultipart mp = new MimeMultipart();
/* 248 */     MimeBodyPart part = new MimeBodyPart(headers, encodedBytes);
/* 249 */     mp.addBodyPart((BodyPart)part);
/* 250 */     return mp;
/*     */   }
/*     */   
/*     */   protected void sendMultipartMessage(MimeMessage msg, MimeMultipart mp) throws MessagingException {
/* 254 */     synchronized (msg) {
/* 255 */       msg.setContent((Multipart)mp);
/* 256 */       msg.setSentDate(new Date());
/* 257 */       Transport.send((Message)msg);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static class FactoryData
/*     */   {
/*     */     private final String to;
/*     */     
/*     */     private final String cc;
/*     */     
/*     */     private final String bcc;
/*     */     
/*     */     private final String from;
/*     */     private final String replyto;
/*     */     private final AbstractStringLayout.Serializer subject;
/*     */     private final String protocol;
/*     */     private final String host;
/*     */     private final int port;
/*     */     private final String username;
/*     */     private final String password;
/*     */     private final boolean isDebug;
/*     */     private final int numElements;
/*     */     
/*     */     public FactoryData(String to, String cc, String bcc, String from, String replyTo, AbstractStringLayout.Serializer subjectSerializer, String protocol, String host, int port, String username, String password, boolean isDebug, int numElements) {
/* 282 */       this.to = to;
/* 283 */       this.cc = cc;
/* 284 */       this.bcc = bcc;
/* 285 */       this.from = from;
/* 286 */       this.replyto = replyTo;
/* 287 */       this.subject = subjectSerializer;
/* 288 */       this.protocol = protocol;
/* 289 */       this.host = host;
/* 290 */       this.port = port;
/* 291 */       this.username = username;
/* 292 */       this.password = password;
/* 293 */       this.isDebug = isDebug;
/* 294 */       this.numElements = numElements;
/*     */     }
/*     */   }
/*     */   
/*     */   private synchronized void connect(LogEvent appendEvent) {
/* 299 */     if (this.message != null) {
/*     */       return;
/*     */     }
/*     */     try {
/* 303 */       this.message = createMimeMessage(this.data, this.session, appendEvent);
/* 304 */     } catch (MessagingException e) {
/* 305 */       logError("Could not set SmtpAppender message options", (Throwable)e);
/* 306 */       this.message = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static class SMTPManagerFactory
/*     */     implements ManagerFactory<SmtpManager, FactoryData>
/*     */   {
/*     */     private SMTPManagerFactory() {}
/*     */     
/*     */     public SmtpManager createManager(String name, SmtpManager.FactoryData data) {
/* 317 */       String prefix = "mail." + data.protocol;
/*     */       
/* 319 */       Properties properties = PropertiesUtil.getSystemProperties();
/* 320 */       properties.put("mail.transport.protocol", data.protocol);
/* 321 */       if (properties.getProperty("mail.host") == null)
/*     */       {
/* 323 */         properties.put("mail.host", NetUtils.getLocalHostname());
/*     */       }
/*     */       
/* 326 */       if (null != data.host) {
/* 327 */         properties.put(prefix + ".host", data.host);
/*     */       }
/* 329 */       if (data.port > 0) {
/* 330 */         properties.put(prefix + ".port", String.valueOf(data.port));
/*     */       }
/*     */       
/* 333 */       Authenticator authenticator = buildAuthenticator(data.username, data.password);
/* 334 */       if (null != authenticator) {
/* 335 */         properties.put(prefix + ".auth", "true");
/*     */       }
/*     */       
/* 338 */       Session session = Session.getInstance(properties, authenticator);
/* 339 */       session.setProtocolForAddress("rfc822", data.protocol);
/* 340 */       session.setDebug(data.isDebug);
/* 341 */       return new SmtpManager(name, session, null, data);
/*     */     }
/*     */     
/*     */     private Authenticator buildAuthenticator(final String username, final String password) {
/* 345 */       if (null != password && null != username) {
/* 346 */         return new Authenticator() {
/* 347 */             private final PasswordAuthentication passwordAuthentication = new PasswordAuthentication(username, password);
/*     */ 
/*     */ 
/*     */             
/*     */             protected PasswordAuthentication getPasswordAuthentication() {
/* 352 */               return this.passwordAuthentication;
/*     */             }
/*     */           };
/*     */       }
/* 356 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\net\SmtpManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */