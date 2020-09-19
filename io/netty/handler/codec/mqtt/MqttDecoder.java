/*     */ package io.netty.handler.codec.mqtt;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.DecoderException;
/*     */ import io.netty.handler.codec.ReplayingDecoder;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import java.util.ArrayList;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MqttDecoder
/*     */   extends ReplayingDecoder<MqttDecoder.DecoderState>
/*     */ {
/*     */   private static final int DEFAULT_MAX_BYTES_IN_MESSAGE = 8092;
/*     */   private MqttFixedHeader mqttFixedHeader;
/*     */   private Object variableHeader;
/*     */   private int bytesRemainingInVariablePart;
/*     */   private final int maxBytesInMessage;
/*     */   
/*     */   enum DecoderState
/*     */   {
/*  50 */     READ_FIXED_HEADER,
/*  51 */     READ_VARIABLE_HEADER,
/*  52 */     READ_PAYLOAD,
/*  53 */     BAD_MESSAGE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MqttDecoder() {
/*  63 */     this(8092);
/*     */   }
/*     */   
/*     */   public MqttDecoder(int maxBytesInMessage) {
/*  67 */     super(DecoderState.READ_FIXED_HEADER);
/*  68 */     this.maxBytesInMessage = maxBytesInMessage;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
/*  73 */     switch ((DecoderState)state()) { case CONNECT:
/*     */         try {
/*  75 */           this.mqttFixedHeader = decodeFixedHeader(buffer);
/*  76 */           this.bytesRemainingInVariablePart = this.mqttFixedHeader.remainingLength();
/*  77 */           checkpoint(DecoderState.READ_VARIABLE_HEADER);
/*     */         }
/*  79 */         catch (Exception cause) {
/*  80 */           out.add(invalidMessage(cause));
/*     */           return;
/*     */         } 
/*     */       case CONNACK:
/*     */         try {
/*  85 */           if (this.bytesRemainingInVariablePart > this.maxBytesInMessage) {
/*  86 */             throw new DecoderException("too large message: " + this.bytesRemainingInVariablePart + " bytes");
/*     */           }
/*  88 */           Result<?> decodedVariableHeader = decodeVariableHeader(buffer, this.mqttFixedHeader);
/*  89 */           this.variableHeader = decodedVariableHeader.value;
/*  90 */           this.bytesRemainingInVariablePart -= decodedVariableHeader.numberOfBytesConsumed;
/*  91 */           checkpoint(DecoderState.READ_PAYLOAD);
/*     */         }
/*  93 */         catch (Exception cause) {
/*  94 */           out.add(invalidMessage(cause));
/*     */           return;
/*     */         } 
/*     */       
/*     */       case SUBSCRIBE:
/*     */         try {
/* 100 */           Result<?> decodedPayload = decodePayload(buffer, this.mqttFixedHeader
/*     */               
/* 102 */               .messageType(), this.bytesRemainingInVariablePart, this.variableHeader);
/*     */ 
/*     */           
/* 105 */           this.bytesRemainingInVariablePart -= decodedPayload.numberOfBytesConsumed;
/* 106 */           if (this.bytesRemainingInVariablePart != 0) {
/* 107 */             throw new DecoderException("non-zero remaining payload bytes: " + this.bytesRemainingInVariablePart + " (" + this.mqttFixedHeader
/*     */                 
/* 109 */                 .messageType() + ')');
/*     */           }
/* 111 */           checkpoint(DecoderState.READ_FIXED_HEADER);
/* 112 */           MqttMessage message = MqttMessageFactory.newMessage(this.mqttFixedHeader, this.variableHeader, decodedPayload
/* 113 */               .value);
/* 114 */           this.mqttFixedHeader = null;
/* 115 */           this.variableHeader = null;
/* 116 */           out.add(message);
/*     */         }
/* 118 */         catch (Exception cause) {
/* 119 */           out.add(invalidMessage(cause));
/*     */           return;
/*     */         } 
/*     */         return;
/*     */       
/*     */       case UNSUBSCRIBE:
/* 125 */         buffer.skipBytes(actualReadableBytes());
/*     */         return; }
/*     */ 
/*     */ 
/*     */     
/* 130 */     throw new Error();
/*     */   }
/*     */ 
/*     */   
/*     */   private MqttMessage invalidMessage(Throwable cause) {
/* 135 */     checkpoint(DecoderState.BAD_MESSAGE);
/* 136 */     return MqttMessageFactory.newInvalidMessage(cause);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static MqttFixedHeader decodeFixedHeader(ByteBuf buffer) {
/* 146 */     short digit, b1 = buffer.readUnsignedByte();
/*     */     
/* 148 */     MqttMessageType messageType = MqttMessageType.valueOf(b1 >> 4);
/* 149 */     boolean dupFlag = ((b1 & 0x8) == 8);
/* 150 */     int qosLevel = (b1 & 0x6) >> 1;
/* 151 */     boolean retain = ((b1 & 0x1) != 0);
/*     */     
/* 153 */     int remainingLength = 0;
/* 154 */     int multiplier = 1;
/*     */     
/* 156 */     int loops = 0;
/*     */     do {
/* 158 */       digit = buffer.readUnsignedByte();
/* 159 */       remainingLength += (digit & 0x7F) * multiplier;
/* 160 */       multiplier *= 128;
/* 161 */       loops++;
/* 162 */     } while ((digit & 0x80) != 0 && loops < 4);
/*     */ 
/*     */     
/* 165 */     if (loops == 4 && (digit & 0x80) != 0) {
/* 166 */       throw new DecoderException("remaining length exceeds 4 digits (" + messageType + ')');
/*     */     }
/*     */     
/* 169 */     MqttFixedHeader decodedFixedHeader = new MqttFixedHeader(messageType, dupFlag, MqttQoS.valueOf(qosLevel), retain, remainingLength);
/* 170 */     return MqttCodecUtil.validateFixedHeader(MqttCodecUtil.resetUnusedFields(decodedFixedHeader));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Result<?> decodeVariableHeader(ByteBuf buffer, MqttFixedHeader mqttFixedHeader) {
/* 180 */     switch (mqttFixedHeader.messageType()) {
/*     */       case CONNECT:
/* 182 */         return decodeConnectionVariableHeader(buffer);
/*     */       
/*     */       case CONNACK:
/* 185 */         return decodeConnAckVariableHeader(buffer);
/*     */       
/*     */       case SUBSCRIBE:
/*     */       case UNSUBSCRIBE:
/*     */       case SUBACK:
/*     */       case UNSUBACK:
/*     */       case PUBACK:
/*     */       case PUBREC:
/*     */       case PUBCOMP:
/*     */       case PUBREL:
/* 195 */         return decodeMessageIdVariableHeader(buffer);
/*     */       
/*     */       case PUBLISH:
/* 198 */         return decodePublishVariableHeader(buffer, mqttFixedHeader);
/*     */ 
/*     */       
/*     */       case PINGREQ:
/*     */       case PINGRESP:
/*     */       case DISCONNECT:
/* 204 */         return new Result(null, 0);
/*     */     } 
/* 206 */     return new Result(null, 0);
/*     */   }
/*     */   
/*     */   private static Result<MqttConnectVariableHeader> decodeConnectionVariableHeader(ByteBuf buffer) {
/* 210 */     Result<String> protoString = decodeString(buffer);
/* 211 */     int numberOfBytesConsumed = protoString.numberOfBytesConsumed;
/*     */     
/* 213 */     byte protocolLevel = buffer.readByte();
/* 214 */     numberOfBytesConsumed++;
/*     */     
/* 216 */     MqttVersion mqttVersion = MqttVersion.fromProtocolNameAndLevel((String)protoString.value, protocolLevel);
/*     */     
/* 218 */     int b1 = buffer.readUnsignedByte();
/* 219 */     numberOfBytesConsumed++;
/*     */     
/* 221 */     Result<Integer> keepAlive = decodeMsbLsb(buffer);
/* 222 */     numberOfBytesConsumed += keepAlive.numberOfBytesConsumed;
/*     */     
/* 224 */     boolean hasUserName = ((b1 & 0x80) == 128);
/* 225 */     boolean hasPassword = ((b1 & 0x40) == 64);
/* 226 */     boolean willRetain = ((b1 & 0x20) == 32);
/* 227 */     int willQos = (b1 & 0x18) >> 3;
/* 228 */     boolean willFlag = ((b1 & 0x4) == 4);
/* 229 */     boolean cleanSession = ((b1 & 0x2) == 2);
/* 230 */     if (mqttVersion == MqttVersion.MQTT_3_1_1) {
/* 231 */       boolean zeroReservedFlag = ((b1 & 0x1) == 0);
/* 232 */       if (!zeroReservedFlag)
/*     */       {
/*     */ 
/*     */         
/* 236 */         throw new DecoderException("non-zero reserved flag");
/*     */       }
/*     */     } 
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
/* 249 */     MqttConnectVariableHeader mqttConnectVariableHeader = new MqttConnectVariableHeader(mqttVersion.protocolName(), mqttVersion.protocolLevel(), hasUserName, hasPassword, willRetain, willQos, willFlag, cleanSession, ((Integer)keepAlive.value).intValue());
/* 250 */     return new Result<MqttConnectVariableHeader>(mqttConnectVariableHeader, numberOfBytesConsumed);
/*     */   }
/*     */   
/*     */   private static Result<MqttConnAckVariableHeader> decodeConnAckVariableHeader(ByteBuf buffer) {
/* 254 */     boolean sessionPresent = ((buffer.readUnsignedByte() & 0x1) == 1);
/* 255 */     byte returnCode = buffer.readByte();
/* 256 */     int numberOfBytesConsumed = 2;
/*     */     
/* 258 */     MqttConnAckVariableHeader mqttConnAckVariableHeader = new MqttConnAckVariableHeader(MqttConnectReturnCode.valueOf(returnCode), sessionPresent);
/* 259 */     return new Result<MqttConnAckVariableHeader>(mqttConnAckVariableHeader, 2);
/*     */   }
/*     */   
/*     */   private static Result<MqttMessageIdVariableHeader> decodeMessageIdVariableHeader(ByteBuf buffer) {
/* 263 */     Result<Integer> messageId = decodeMessageId(buffer);
/* 264 */     return new Result<MqttMessageIdVariableHeader>(
/* 265 */         MqttMessageIdVariableHeader.from(((Integer)messageId.value).intValue()), messageId
/* 266 */         .numberOfBytesConsumed);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Result<MqttPublishVariableHeader> decodePublishVariableHeader(ByteBuf buffer, MqttFixedHeader mqttFixedHeader) {
/* 272 */     Result<String> decodedTopic = decodeString(buffer);
/* 273 */     if (!MqttCodecUtil.isValidPublishTopicName((String)decodedTopic.value)) {
/* 274 */       throw new DecoderException("invalid publish topic name: " + (String)decodedTopic.value + " (contains wildcards)");
/*     */     }
/* 276 */     int numberOfBytesConsumed = decodedTopic.numberOfBytesConsumed;
/*     */     
/* 278 */     int messageId = -1;
/* 279 */     if (mqttFixedHeader.qosLevel().value() > 0) {
/* 280 */       Result<Integer> decodedMessageId = decodeMessageId(buffer);
/* 281 */       messageId = ((Integer)decodedMessageId.value).intValue();
/* 282 */       numberOfBytesConsumed += decodedMessageId.numberOfBytesConsumed;
/*     */     } 
/*     */     
/* 285 */     MqttPublishVariableHeader mqttPublishVariableHeader = new MqttPublishVariableHeader((String)decodedTopic.value, messageId);
/* 286 */     return new Result<MqttPublishVariableHeader>(mqttPublishVariableHeader, numberOfBytesConsumed);
/*     */   }
/*     */   
/*     */   private static Result<Integer> decodeMessageId(ByteBuf buffer) {
/* 290 */     Result<Integer> messageId = decodeMsbLsb(buffer);
/* 291 */     if (!MqttCodecUtil.isValidMessageId(((Integer)messageId.value).intValue())) {
/* 292 */       throw new DecoderException("invalid messageId: " + messageId.value);
/*     */     }
/* 294 */     return messageId;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Result<?> decodePayload(ByteBuf buffer, MqttMessageType messageType, int bytesRemainingInVariablePart, Object variableHeader) {
/* 311 */     switch (messageType) {
/*     */       case CONNECT:
/* 313 */         return decodeConnectionPayload(buffer, (MqttConnectVariableHeader)variableHeader);
/*     */       
/*     */       case SUBSCRIBE:
/* 316 */         return decodeSubscribePayload(buffer, bytesRemainingInVariablePart);
/*     */       
/*     */       case SUBACK:
/* 319 */         return decodeSubackPayload(buffer, bytesRemainingInVariablePart);
/*     */       
/*     */       case UNSUBSCRIBE:
/* 322 */         return decodeUnsubscribePayload(buffer, bytesRemainingInVariablePart);
/*     */       
/*     */       case PUBLISH:
/* 325 */         return decodePublishPayload(buffer, bytesRemainingInVariablePart);
/*     */     } 
/*     */ 
/*     */     
/* 329 */     return new Result(null, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Result<MqttConnectPayload> decodeConnectionPayload(ByteBuf buffer, MqttConnectVariableHeader mqttConnectVariableHeader) {
/* 336 */     Result<String> decodedClientId = decodeString(buffer);
/* 337 */     String decodedClientIdValue = (String)decodedClientId.value;
/* 338 */     MqttVersion mqttVersion = MqttVersion.fromProtocolNameAndLevel(mqttConnectVariableHeader.name(), 
/* 339 */         (byte)mqttConnectVariableHeader.version());
/* 340 */     if (!MqttCodecUtil.isValidClientId(mqttVersion, decodedClientIdValue)) {
/* 341 */       throw new MqttIdentifierRejectedException("invalid clientIdentifier: " + decodedClientIdValue);
/*     */     }
/* 343 */     int numberOfBytesConsumed = decodedClientId.numberOfBytesConsumed;
/*     */     
/* 345 */     Result<String> decodedWillTopic = null;
/* 346 */     Result<byte[]> decodedWillMessage = null;
/* 347 */     if (mqttConnectVariableHeader.isWillFlag()) {
/* 348 */       decodedWillTopic = decodeString(buffer, 0, 32767);
/* 349 */       numberOfBytesConsumed += decodedWillTopic.numberOfBytesConsumed;
/* 350 */       decodedWillMessage = decodeByteArray(buffer);
/* 351 */       numberOfBytesConsumed += decodedWillMessage.numberOfBytesConsumed;
/*     */     } 
/* 353 */     Result<String> decodedUserName = null;
/* 354 */     Result<byte[]> decodedPassword = null;
/* 355 */     if (mqttConnectVariableHeader.hasUserName()) {
/* 356 */       decodedUserName = decodeString(buffer);
/* 357 */       numberOfBytesConsumed += decodedUserName.numberOfBytesConsumed;
/*     */     } 
/* 359 */     if (mqttConnectVariableHeader.hasPassword()) {
/* 360 */       decodedPassword = decodeByteArray(buffer);
/* 361 */       numberOfBytesConsumed += decodedPassword.numberOfBytesConsumed;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 370 */     MqttConnectPayload mqttConnectPayload = new MqttConnectPayload((String)decodedClientId.value, (decodedWillTopic != null) ? (String)decodedWillTopic.value : null, (decodedWillMessage != null) ? (byte[])decodedWillMessage.value : null, (decodedUserName != null) ? (String)decodedUserName.value : null, (decodedPassword != null) ? (byte[])decodedPassword.value : null);
/* 371 */     return new Result<MqttConnectPayload>(mqttConnectPayload, numberOfBytesConsumed);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Result<MqttSubscribePayload> decodeSubscribePayload(ByteBuf buffer, int bytesRemainingInVariablePart) {
/* 377 */     List<MqttTopicSubscription> subscribeTopics = new ArrayList<MqttTopicSubscription>();
/* 378 */     int numberOfBytesConsumed = 0;
/* 379 */     while (numberOfBytesConsumed < bytesRemainingInVariablePart) {
/* 380 */       Result<String> decodedTopicName = decodeString(buffer);
/* 381 */       numberOfBytesConsumed += decodedTopicName.numberOfBytesConsumed;
/* 382 */       int qos = buffer.readUnsignedByte() & 0x3;
/* 383 */       numberOfBytesConsumed++;
/* 384 */       subscribeTopics.add(new MqttTopicSubscription((String)decodedTopicName.value, MqttQoS.valueOf(qos)));
/*     */     } 
/* 386 */     return new Result<MqttSubscribePayload>(new MqttSubscribePayload(subscribeTopics), numberOfBytesConsumed);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Result<MqttSubAckPayload> decodeSubackPayload(ByteBuf buffer, int bytesRemainingInVariablePart) {
/* 392 */     List<Integer> grantedQos = new ArrayList<Integer>();
/* 393 */     int numberOfBytesConsumed = 0;
/* 394 */     while (numberOfBytesConsumed < bytesRemainingInVariablePart) {
/* 395 */       int qos = buffer.readUnsignedByte();
/* 396 */       if (qos != MqttQoS.FAILURE.value()) {
/* 397 */         qos &= 0x3;
/*     */       }
/* 399 */       numberOfBytesConsumed++;
/* 400 */       grantedQos.add(Integer.valueOf(qos));
/*     */     } 
/* 402 */     return new Result<MqttSubAckPayload>(new MqttSubAckPayload(grantedQos), numberOfBytesConsumed);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Result<MqttUnsubscribePayload> decodeUnsubscribePayload(ByteBuf buffer, int bytesRemainingInVariablePart) {
/* 408 */     List<String> unsubscribeTopics = new ArrayList<String>();
/* 409 */     int numberOfBytesConsumed = 0;
/* 410 */     while (numberOfBytesConsumed < bytesRemainingInVariablePart) {
/* 411 */       Result<String> decodedTopicName = decodeString(buffer);
/* 412 */       numberOfBytesConsumed += decodedTopicName.numberOfBytesConsumed;
/* 413 */       unsubscribeTopics.add(decodedTopicName.value);
/*     */     } 
/* 415 */     return new Result<MqttUnsubscribePayload>(new MqttUnsubscribePayload(unsubscribeTopics), numberOfBytesConsumed);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Result<ByteBuf> decodePublishPayload(ByteBuf buffer, int bytesRemainingInVariablePart) {
/* 421 */     ByteBuf b = buffer.readRetainedSlice(bytesRemainingInVariablePart);
/* 422 */     return new Result<ByteBuf>(b, bytesRemainingInVariablePart);
/*     */   }
/*     */   
/*     */   private static Result<String> decodeString(ByteBuf buffer) {
/* 426 */     return decodeString(buffer, 0, 2147483647);
/*     */   }
/*     */   
/*     */   private static Result<String> decodeString(ByteBuf buffer, int minBytes, int maxBytes) {
/* 430 */     Result<Integer> decodedSize = decodeMsbLsb(buffer);
/* 431 */     int size = ((Integer)decodedSize.value).intValue();
/* 432 */     int numberOfBytesConsumed = decodedSize.numberOfBytesConsumed;
/* 433 */     if (size < minBytes || size > maxBytes) {
/* 434 */       buffer.skipBytes(size);
/* 435 */       numberOfBytesConsumed += size;
/* 436 */       return new Result<String>(null, numberOfBytesConsumed);
/*     */     } 
/* 438 */     String s = buffer.toString(buffer.readerIndex(), size, CharsetUtil.UTF_8);
/* 439 */     buffer.skipBytes(size);
/* 440 */     numberOfBytesConsumed += size;
/* 441 */     return new Result<String>(s, numberOfBytesConsumed);
/*     */   }
/*     */   
/*     */   private static Result<byte[]> decodeByteArray(ByteBuf buffer) {
/* 445 */     Result<Integer> decodedSize = decodeMsbLsb(buffer);
/* 446 */     int size = ((Integer)decodedSize.value).intValue();
/* 447 */     byte[] bytes = new byte[size];
/* 448 */     buffer.readBytes(bytes);
/* 449 */     return (Result)new Result<byte>(bytes, decodedSize.numberOfBytesConsumed + size);
/*     */   }
/*     */   
/*     */   private static Result<Integer> decodeMsbLsb(ByteBuf buffer) {
/* 453 */     return decodeMsbLsb(buffer, 0, 65535);
/*     */   }
/*     */   
/*     */   private static Result<Integer> decodeMsbLsb(ByteBuf buffer, int min, int max) {
/* 457 */     short msbSize = buffer.readUnsignedByte();
/* 458 */     short lsbSize = buffer.readUnsignedByte();
/* 459 */     int numberOfBytesConsumed = 2;
/* 460 */     int result = msbSize << 8 | lsbSize;
/* 461 */     if (result < min || result > max) {
/* 462 */       result = -1;
/*     */     }
/* 464 */     return new Result<Integer>(Integer.valueOf(result), 2);
/*     */   }
/*     */   
/*     */   private static final class Result<T>
/*     */   {
/*     */     private final T value;
/*     */     private final int numberOfBytesConsumed;
/*     */     
/*     */     Result(T value, int numberOfBytesConsumed) {
/* 473 */       this.value = value;
/* 474 */       this.numberOfBytesConsumed = numberOfBytesConsumed;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\mqtt\MqttDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */