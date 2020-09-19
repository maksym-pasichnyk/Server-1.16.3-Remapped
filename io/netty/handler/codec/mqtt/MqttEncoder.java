/*     */ package io.netty.handler.codec.mqtt;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelHandler.Sharable;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.DecoderException;
/*     */ import io.netty.handler.codec.MessageToMessageEncoder;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.internal.EmptyArrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ @Sharable
/*     */ public final class MqttEncoder
/*     */   extends MessageToMessageEncoder<MqttMessage>
/*     */ {
/*  39 */   public static final MqttEncoder INSTANCE = new MqttEncoder();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void encode(ChannelHandlerContext ctx, MqttMessage msg, List<Object> out) throws Exception {
/*  45 */     out.add(doEncode(ctx.alloc(), msg));
/*     */   }
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
/*     */   static ByteBuf doEncode(ByteBufAllocator byteBufAllocator, MqttMessage message) {
/*  58 */     switch (message.fixedHeader().messageType()) {
/*     */       case CONNECT:
/*  60 */         return encodeConnectMessage(byteBufAllocator, (MqttConnectMessage)message);
/*     */       
/*     */       case CONNACK:
/*  63 */         return encodeConnAckMessage(byteBufAllocator, (MqttConnAckMessage)message);
/*     */       
/*     */       case PUBLISH:
/*  66 */         return encodePublishMessage(byteBufAllocator, (MqttPublishMessage)message);
/*     */       
/*     */       case SUBSCRIBE:
/*  69 */         return encodeSubscribeMessage(byteBufAllocator, (MqttSubscribeMessage)message);
/*     */       
/*     */       case UNSUBSCRIBE:
/*  72 */         return encodeUnsubscribeMessage(byteBufAllocator, (MqttUnsubscribeMessage)message);
/*     */       
/*     */       case SUBACK:
/*  75 */         return encodeSubAckMessage(byteBufAllocator, (MqttSubAckMessage)message);
/*     */       
/*     */       case UNSUBACK:
/*     */       case PUBACK:
/*     */       case PUBREC:
/*     */       case PUBREL:
/*     */       case PUBCOMP:
/*  82 */         return encodeMessageWithOnlySingleByteFixedHeaderAndMessageId(byteBufAllocator, message);
/*     */       
/*     */       case PINGREQ:
/*     */       case PINGRESP:
/*     */       case DISCONNECT:
/*  87 */         return encodeMessageWithOnlySingleByteFixedHeader(byteBufAllocator, message);
/*     */     } 
/*     */     
/*  90 */     throw new IllegalArgumentException("Unknown message type: " + message
/*  91 */         .fixedHeader().messageType().value());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ByteBuf encodeConnectMessage(ByteBufAllocator byteBufAllocator, MqttConnectMessage message) {
/*  98 */     int payloadBufferSize = 0;
/*     */     
/* 100 */     MqttFixedHeader mqttFixedHeader = message.fixedHeader();
/* 101 */     MqttConnectVariableHeader variableHeader = message.variableHeader();
/* 102 */     MqttConnectPayload payload = message.payload();
/* 103 */     MqttVersion mqttVersion = MqttVersion.fromProtocolNameAndLevel(variableHeader.name(), 
/* 104 */         (byte)variableHeader.version());
/*     */ 
/*     */     
/* 107 */     if (!variableHeader.hasUserName() && variableHeader.hasPassword()) {
/* 108 */       throw new DecoderException("Without a username, the password MUST be not set");
/*     */     }
/*     */ 
/*     */     
/* 112 */     String clientIdentifier = payload.clientIdentifier();
/* 113 */     if (!MqttCodecUtil.isValidClientId(mqttVersion, clientIdentifier)) {
/* 114 */       throw new MqttIdentifierRejectedException("invalid clientIdentifier: " + clientIdentifier);
/*     */     }
/* 116 */     byte[] clientIdentifierBytes = encodeStringUtf8(clientIdentifier);
/* 117 */     payloadBufferSize += 2 + clientIdentifierBytes.length;
/*     */ 
/*     */     
/* 120 */     String willTopic = payload.willTopic();
/* 121 */     byte[] willTopicBytes = (willTopic != null) ? encodeStringUtf8(willTopic) : EmptyArrays.EMPTY_BYTES;
/* 122 */     byte[] willMessage = payload.willMessageInBytes();
/* 123 */     byte[] willMessageBytes = (willMessage != null) ? willMessage : EmptyArrays.EMPTY_BYTES;
/* 124 */     if (variableHeader.isWillFlag()) {
/* 125 */       payloadBufferSize += 2 + willTopicBytes.length;
/* 126 */       payloadBufferSize += 2 + willMessageBytes.length;
/*     */     } 
/*     */     
/* 129 */     String userName = payload.userName();
/* 130 */     byte[] userNameBytes = (userName != null) ? encodeStringUtf8(userName) : EmptyArrays.EMPTY_BYTES;
/* 131 */     if (variableHeader.hasUserName()) {
/* 132 */       payloadBufferSize += 2 + userNameBytes.length;
/*     */     }
/*     */     
/* 135 */     byte[] password = payload.passwordInBytes();
/* 136 */     byte[] passwordBytes = (password != null) ? password : EmptyArrays.EMPTY_BYTES;
/* 137 */     if (variableHeader.hasPassword()) {
/* 138 */       payloadBufferSize += 2 + passwordBytes.length;
/*     */     }
/*     */ 
/*     */     
/* 142 */     byte[] protocolNameBytes = mqttVersion.protocolNameBytes();
/* 143 */     int variableHeaderBufferSize = 2 + protocolNameBytes.length + 4;
/* 144 */     int variablePartSize = variableHeaderBufferSize + payloadBufferSize;
/* 145 */     int fixedHeaderBufferSize = 1 + getVariableLengthInt(variablePartSize);
/* 146 */     ByteBuf buf = byteBufAllocator.buffer(fixedHeaderBufferSize + variablePartSize);
/* 147 */     buf.writeByte(getFixedHeaderByte1(mqttFixedHeader));
/* 148 */     writeVariableLengthInt(buf, variablePartSize);
/*     */     
/* 150 */     buf.writeShort(protocolNameBytes.length);
/* 151 */     buf.writeBytes(protocolNameBytes);
/*     */     
/* 153 */     buf.writeByte(variableHeader.version());
/* 154 */     buf.writeByte(getConnVariableHeaderFlag(variableHeader));
/* 155 */     buf.writeShort(variableHeader.keepAliveTimeSeconds());
/*     */ 
/*     */     
/* 158 */     buf.writeShort(clientIdentifierBytes.length);
/* 159 */     buf.writeBytes(clientIdentifierBytes, 0, clientIdentifierBytes.length);
/* 160 */     if (variableHeader.isWillFlag()) {
/* 161 */       buf.writeShort(willTopicBytes.length);
/* 162 */       buf.writeBytes(willTopicBytes, 0, willTopicBytes.length);
/* 163 */       buf.writeShort(willMessageBytes.length);
/* 164 */       buf.writeBytes(willMessageBytes, 0, willMessageBytes.length);
/*     */     } 
/* 166 */     if (variableHeader.hasUserName()) {
/* 167 */       buf.writeShort(userNameBytes.length);
/* 168 */       buf.writeBytes(userNameBytes, 0, userNameBytes.length);
/*     */     } 
/* 170 */     if (variableHeader.hasPassword()) {
/* 171 */       buf.writeShort(passwordBytes.length);
/* 172 */       buf.writeBytes(passwordBytes, 0, passwordBytes.length);
/*     */     } 
/* 174 */     return buf;
/*     */   }
/*     */   
/*     */   private static int getConnVariableHeaderFlag(MqttConnectVariableHeader variableHeader) {
/* 178 */     int flagByte = 0;
/* 179 */     if (variableHeader.hasUserName()) {
/* 180 */       flagByte |= 0x80;
/*     */     }
/* 182 */     if (variableHeader.hasPassword()) {
/* 183 */       flagByte |= 0x40;
/*     */     }
/* 185 */     if (variableHeader.isWillRetain()) {
/* 186 */       flagByte |= 0x20;
/*     */     }
/* 188 */     flagByte |= (variableHeader.willQos() & 0x3) << 3;
/* 189 */     if (variableHeader.isWillFlag()) {
/* 190 */       flagByte |= 0x4;
/*     */     }
/* 192 */     if (variableHeader.isCleanSession()) {
/* 193 */       flagByte |= 0x2;
/*     */     }
/* 195 */     return flagByte;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static ByteBuf encodeConnAckMessage(ByteBufAllocator byteBufAllocator, MqttConnAckMessage message) {
/* 201 */     ByteBuf buf = byteBufAllocator.buffer(4);
/* 202 */     buf.writeByte(getFixedHeaderByte1(message.fixedHeader()));
/* 203 */     buf.writeByte(2);
/* 204 */     buf.writeByte(message.variableHeader().isSessionPresent() ? 1 : 0);
/* 205 */     buf.writeByte(message.variableHeader().connectReturnCode().byteValue());
/*     */     
/* 207 */     return buf;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static ByteBuf encodeSubscribeMessage(ByteBufAllocator byteBufAllocator, MqttSubscribeMessage message) {
/* 213 */     int variableHeaderBufferSize = 2;
/* 214 */     int payloadBufferSize = 0;
/*     */     
/* 216 */     MqttFixedHeader mqttFixedHeader = message.fixedHeader();
/* 217 */     MqttMessageIdVariableHeader variableHeader = message.variableHeader();
/* 218 */     MqttSubscribePayload payload = message.payload();
/*     */     
/* 220 */     for (MqttTopicSubscription topic : payload.topicSubscriptions()) {
/* 221 */       String topicName = topic.topicName();
/* 222 */       byte[] topicNameBytes = encodeStringUtf8(topicName);
/* 223 */       payloadBufferSize += 2 + topicNameBytes.length;
/* 224 */       payloadBufferSize++;
/*     */     } 
/*     */     
/* 227 */     int variablePartSize = variableHeaderBufferSize + payloadBufferSize;
/* 228 */     int fixedHeaderBufferSize = 1 + getVariableLengthInt(variablePartSize);
/*     */     
/* 230 */     ByteBuf buf = byteBufAllocator.buffer(fixedHeaderBufferSize + variablePartSize);
/* 231 */     buf.writeByte(getFixedHeaderByte1(mqttFixedHeader));
/* 232 */     writeVariableLengthInt(buf, variablePartSize);
/*     */ 
/*     */     
/* 235 */     int messageId = variableHeader.messageId();
/* 236 */     buf.writeShort(messageId);
/*     */ 
/*     */     
/* 239 */     for (MqttTopicSubscription topic : payload.topicSubscriptions()) {
/* 240 */       String topicName = topic.topicName();
/* 241 */       byte[] topicNameBytes = encodeStringUtf8(topicName);
/* 242 */       buf.writeShort(topicNameBytes.length);
/* 243 */       buf.writeBytes(topicNameBytes, 0, topicNameBytes.length);
/* 244 */       buf.writeByte(topic.qualityOfService().value());
/*     */     } 
/*     */     
/* 247 */     return buf;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static ByteBuf encodeUnsubscribeMessage(ByteBufAllocator byteBufAllocator, MqttUnsubscribeMessage message) {
/* 253 */     int variableHeaderBufferSize = 2;
/* 254 */     int payloadBufferSize = 0;
/*     */     
/* 256 */     MqttFixedHeader mqttFixedHeader = message.fixedHeader();
/* 257 */     MqttMessageIdVariableHeader variableHeader = message.variableHeader();
/* 258 */     MqttUnsubscribePayload payload = message.payload();
/*     */     
/* 260 */     for (String topicName : payload.topics()) {
/* 261 */       byte[] topicNameBytes = encodeStringUtf8(topicName);
/* 262 */       payloadBufferSize += 2 + topicNameBytes.length;
/*     */     } 
/*     */     
/* 265 */     int variablePartSize = variableHeaderBufferSize + payloadBufferSize;
/* 266 */     int fixedHeaderBufferSize = 1 + getVariableLengthInt(variablePartSize);
/*     */     
/* 268 */     ByteBuf buf = byteBufAllocator.buffer(fixedHeaderBufferSize + variablePartSize);
/* 269 */     buf.writeByte(getFixedHeaderByte1(mqttFixedHeader));
/* 270 */     writeVariableLengthInt(buf, variablePartSize);
/*     */ 
/*     */     
/* 273 */     int messageId = variableHeader.messageId();
/* 274 */     buf.writeShort(messageId);
/*     */ 
/*     */     
/* 277 */     for (String topicName : payload.topics()) {
/* 278 */       byte[] topicNameBytes = encodeStringUtf8(topicName);
/* 279 */       buf.writeShort(topicNameBytes.length);
/* 280 */       buf.writeBytes(topicNameBytes, 0, topicNameBytes.length);
/*     */     } 
/*     */     
/* 283 */     return buf;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static ByteBuf encodeSubAckMessage(ByteBufAllocator byteBufAllocator, MqttSubAckMessage message) {
/* 289 */     int variableHeaderBufferSize = 2;
/* 290 */     int payloadBufferSize = message.payload().grantedQoSLevels().size();
/* 291 */     int variablePartSize = variableHeaderBufferSize + payloadBufferSize;
/* 292 */     int fixedHeaderBufferSize = 1 + getVariableLengthInt(variablePartSize);
/* 293 */     ByteBuf buf = byteBufAllocator.buffer(fixedHeaderBufferSize + variablePartSize);
/* 294 */     buf.writeByte(getFixedHeaderByte1(message.fixedHeader()));
/* 295 */     writeVariableLengthInt(buf, variablePartSize);
/* 296 */     buf.writeShort(message.variableHeader().messageId());
/* 297 */     for (Iterator<Integer> iterator = message.payload().grantedQoSLevels().iterator(); iterator.hasNext(); ) { int qos = ((Integer)iterator.next()).intValue();
/* 298 */       buf.writeByte(qos); }
/*     */ 
/*     */     
/* 301 */     return buf;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static ByteBuf encodePublishMessage(ByteBufAllocator byteBufAllocator, MqttPublishMessage message) {
/* 307 */     MqttFixedHeader mqttFixedHeader = message.fixedHeader();
/* 308 */     MqttPublishVariableHeader variableHeader = message.variableHeader();
/* 309 */     ByteBuf payload = message.payload().duplicate();
/*     */     
/* 311 */     String topicName = variableHeader.topicName();
/* 312 */     byte[] topicNameBytes = encodeStringUtf8(topicName);
/*     */ 
/*     */     
/* 315 */     int variableHeaderBufferSize = 2 + topicNameBytes.length + ((mqttFixedHeader.qosLevel().value() > 0) ? 2 : 0);
/* 316 */     int payloadBufferSize = payload.readableBytes();
/* 317 */     int variablePartSize = variableHeaderBufferSize + payloadBufferSize;
/* 318 */     int fixedHeaderBufferSize = 1 + getVariableLengthInt(variablePartSize);
/*     */     
/* 320 */     ByteBuf buf = byteBufAllocator.buffer(fixedHeaderBufferSize + variablePartSize);
/* 321 */     buf.writeByte(getFixedHeaderByte1(mqttFixedHeader));
/* 322 */     writeVariableLengthInt(buf, variablePartSize);
/* 323 */     buf.writeShort(topicNameBytes.length);
/* 324 */     buf.writeBytes(topicNameBytes);
/* 325 */     if (mqttFixedHeader.qosLevel().value() > 0) {
/* 326 */       buf.writeShort(variableHeader.messageId());
/*     */     }
/* 328 */     buf.writeBytes(payload);
/*     */     
/* 330 */     return buf;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static ByteBuf encodeMessageWithOnlySingleByteFixedHeaderAndMessageId(ByteBufAllocator byteBufAllocator, MqttMessage message) {
/* 336 */     MqttFixedHeader mqttFixedHeader = message.fixedHeader();
/* 337 */     MqttMessageIdVariableHeader variableHeader = (MqttMessageIdVariableHeader)message.variableHeader();
/* 338 */     int msgId = variableHeader.messageId();
/*     */     
/* 340 */     int variableHeaderBufferSize = 2;
/* 341 */     int fixedHeaderBufferSize = 1 + getVariableLengthInt(variableHeaderBufferSize);
/* 342 */     ByteBuf buf = byteBufAllocator.buffer(fixedHeaderBufferSize + variableHeaderBufferSize);
/* 343 */     buf.writeByte(getFixedHeaderByte1(mqttFixedHeader));
/* 344 */     writeVariableLengthInt(buf, variableHeaderBufferSize);
/* 345 */     buf.writeShort(msgId);
/*     */     
/* 347 */     return buf;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static ByteBuf encodeMessageWithOnlySingleByteFixedHeader(ByteBufAllocator byteBufAllocator, MqttMessage message) {
/* 353 */     MqttFixedHeader mqttFixedHeader = message.fixedHeader();
/* 354 */     ByteBuf buf = byteBufAllocator.buffer(2);
/* 355 */     buf.writeByte(getFixedHeaderByte1(mqttFixedHeader));
/* 356 */     buf.writeByte(0);
/*     */     
/* 358 */     return buf;
/*     */   }
/*     */   
/*     */   private static int getFixedHeaderByte1(MqttFixedHeader header) {
/* 362 */     int ret = 0;
/* 363 */     ret |= header.messageType().value() << 4;
/* 364 */     if (header.isDup()) {
/* 365 */       ret |= 0x8;
/*     */     }
/* 367 */     ret |= header.qosLevel().value() << 1;
/* 368 */     if (header.isRetain()) {
/* 369 */       ret |= 0x1;
/*     */     }
/* 371 */     return ret;
/*     */   }
/*     */   
/*     */   private static void writeVariableLengthInt(ByteBuf buf, int num) {
/*     */     do {
/* 376 */       int digit = num % 128;
/* 377 */       num /= 128;
/* 378 */       if (num > 0) {
/* 379 */         digit |= 0x80;
/*     */       }
/* 381 */       buf.writeByte(digit);
/* 382 */     } while (num > 0);
/*     */   }
/*     */   
/*     */   private static int getVariableLengthInt(int num) {
/* 386 */     int count = 0;
/*     */     while (true) {
/* 388 */       num /= 128;
/* 389 */       count++;
/* 390 */       if (num <= 0)
/* 391 */         return count; 
/*     */     } 
/*     */   }
/*     */   private static byte[] encodeStringUtf8(String s) {
/* 395 */     return s.getBytes(CharsetUtil.UTF_8);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\mqtt\MqttEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */