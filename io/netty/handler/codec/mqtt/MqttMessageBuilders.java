/*     */ package io.netty.handler.codec.mqtt;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
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
/*     */ public final class MqttMessageBuilders
/*     */ {
/*     */   public static final class PublishBuilder
/*     */   {
/*     */     private String topic;
/*     */     private boolean retained;
/*     */     private MqttQoS qos;
/*     */     private ByteBuf payload;
/*     */     private int messageId;
/*     */     
/*     */     public PublishBuilder topicName(String topic) {
/*  38 */       this.topic = topic;
/*  39 */       return this;
/*     */     }
/*     */     
/*     */     public PublishBuilder retained(boolean retained) {
/*  43 */       this.retained = retained;
/*  44 */       return this;
/*     */     }
/*     */     
/*     */     public PublishBuilder qos(MqttQoS qos) {
/*  48 */       this.qos = qos;
/*  49 */       return this;
/*     */     }
/*     */     
/*     */     public PublishBuilder payload(ByteBuf payload) {
/*  53 */       this.payload = payload;
/*  54 */       return this;
/*     */     }
/*     */     
/*     */     public PublishBuilder messageId(int messageId) {
/*  58 */       this.messageId = messageId;
/*  59 */       return this;
/*     */     }
/*     */     
/*     */     public MqttPublishMessage build() {
/*  63 */       MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.PUBLISH, false, this.qos, this.retained, 0);
/*  64 */       MqttPublishVariableHeader mqttVariableHeader = new MqttPublishVariableHeader(this.topic, this.messageId);
/*  65 */       return new MqttPublishMessage(mqttFixedHeader, mqttVariableHeader, Unpooled.buffer().writeBytes(this.payload));
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class ConnectBuilder
/*     */   {
/*  71 */     private MqttVersion version = MqttVersion.MQTT_3_1_1;
/*     */     private String clientId;
/*     */     private boolean cleanSession;
/*     */     private boolean hasUser;
/*     */     private boolean hasPassword;
/*     */     private int keepAliveSecs;
/*     */     private boolean willFlag;
/*     */     private boolean willRetain;
/*  79 */     private MqttQoS willQos = MqttQoS.AT_MOST_ONCE;
/*     */     
/*     */     private String willTopic;
/*     */     
/*     */     private byte[] willMessage;
/*     */     
/*     */     private String username;
/*     */     private byte[] password;
/*     */     
/*     */     public ConnectBuilder protocolVersion(MqttVersion version) {
/*  89 */       this.version = version;
/*  90 */       return this;
/*     */     }
/*     */     
/*     */     public ConnectBuilder clientId(String clientId) {
/*  94 */       this.clientId = clientId;
/*  95 */       return this;
/*     */     }
/*     */     
/*     */     public ConnectBuilder cleanSession(boolean cleanSession) {
/*  99 */       this.cleanSession = cleanSession;
/* 100 */       return this;
/*     */     }
/*     */     
/*     */     public ConnectBuilder keepAlive(int keepAliveSecs) {
/* 104 */       this.keepAliveSecs = keepAliveSecs;
/* 105 */       return this;
/*     */     }
/*     */     
/*     */     public ConnectBuilder willFlag(boolean willFlag) {
/* 109 */       this.willFlag = willFlag;
/* 110 */       return this;
/*     */     }
/*     */     
/*     */     public ConnectBuilder willQoS(MqttQoS willQos) {
/* 114 */       this.willQos = willQos;
/* 115 */       return this;
/*     */     }
/*     */     
/*     */     public ConnectBuilder willTopic(String willTopic) {
/* 119 */       this.willTopic = willTopic;
/* 120 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public ConnectBuilder willMessage(String willMessage) {
/* 128 */       willMessage((willMessage == null) ? null : willMessage.getBytes(CharsetUtil.UTF_8));
/* 129 */       return this;
/*     */     }
/*     */     
/*     */     public ConnectBuilder willMessage(byte[] willMessage) {
/* 133 */       this.willMessage = willMessage;
/* 134 */       return this;
/*     */     }
/*     */     
/*     */     public ConnectBuilder willRetain(boolean willRetain) {
/* 138 */       this.willRetain = willRetain;
/* 139 */       return this;
/*     */     }
/*     */     
/*     */     public ConnectBuilder hasUser(boolean value) {
/* 143 */       this.hasUser = value;
/* 144 */       return this;
/*     */     }
/*     */     
/*     */     public ConnectBuilder hasPassword(boolean value) {
/* 148 */       this.hasPassword = value;
/* 149 */       return this;
/*     */     }
/*     */     
/*     */     public ConnectBuilder username(String username) {
/* 153 */       this.hasUser = (username != null);
/* 154 */       this.username = username;
/* 155 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public ConnectBuilder password(String password) {
/* 163 */       password((password == null) ? null : password.getBytes(CharsetUtil.UTF_8));
/* 164 */       return this;
/*     */     }
/*     */     
/*     */     public ConnectBuilder password(byte[] password) {
/* 168 */       this.hasPassword = (password != null);
/* 169 */       this.password = password;
/* 170 */       return this;
/*     */     }
/*     */     
/*     */     public MqttConnectMessage build() {
/* 174 */       MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.CONNECT, false, MqttQoS.AT_MOST_ONCE, false, 0);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 183 */       MqttConnectVariableHeader mqttConnectVariableHeader = new MqttConnectVariableHeader(this.version.protocolName(), this.version.protocolLevel(), this.hasUser, this.hasPassword, this.willRetain, this.willQos.value(), this.willFlag, this.cleanSession, this.keepAliveSecs);
/*     */ 
/*     */ 
/*     */       
/* 187 */       MqttConnectPayload mqttConnectPayload = new MqttConnectPayload(this.clientId, this.willTopic, this.willMessage, this.username, this.password);
/*     */       
/* 189 */       return new MqttConnectMessage(mqttFixedHeader, mqttConnectVariableHeader, mqttConnectPayload);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class SubscribeBuilder
/*     */   {
/*     */     private List<MqttTopicSubscription> subscriptions;
/*     */     
/*     */     private int messageId;
/*     */ 
/*     */     
/*     */     public SubscribeBuilder addSubscription(MqttQoS qos, String topic) {
/* 202 */       if (this.subscriptions == null) {
/* 203 */         this.subscriptions = new ArrayList<MqttTopicSubscription>(5);
/*     */       }
/* 205 */       this.subscriptions.add(new MqttTopicSubscription(topic, qos));
/* 206 */       return this;
/*     */     }
/*     */     
/*     */     public SubscribeBuilder messageId(int messageId) {
/* 210 */       this.messageId = messageId;
/* 211 */       return this;
/*     */     }
/*     */     
/*     */     public MqttSubscribeMessage build() {
/* 215 */       MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.SUBSCRIBE, false, MqttQoS.AT_LEAST_ONCE, false, 0);
/*     */       
/* 217 */       MqttMessageIdVariableHeader mqttVariableHeader = MqttMessageIdVariableHeader.from(this.messageId);
/* 218 */       MqttSubscribePayload mqttSubscribePayload = new MqttSubscribePayload(this.subscriptions);
/* 219 */       return new MqttSubscribeMessage(mqttFixedHeader, mqttVariableHeader, mqttSubscribePayload);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class UnsubscribeBuilder
/*     */   {
/*     */     private List<String> topicFilters;
/*     */     
/*     */     private int messageId;
/*     */ 
/*     */     
/*     */     public UnsubscribeBuilder addTopicFilter(String topic) {
/* 232 */       if (this.topicFilters == null) {
/* 233 */         this.topicFilters = new ArrayList<String>(5);
/*     */       }
/* 235 */       this.topicFilters.add(topic);
/* 236 */       return this;
/*     */     }
/*     */     
/*     */     public UnsubscribeBuilder messageId(int messageId) {
/* 240 */       this.messageId = messageId;
/* 241 */       return this;
/*     */     }
/*     */     
/*     */     public MqttUnsubscribeMessage build() {
/* 245 */       MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.UNSUBSCRIBE, false, MqttQoS.AT_LEAST_ONCE, false, 0);
/*     */       
/* 247 */       MqttMessageIdVariableHeader mqttVariableHeader = MqttMessageIdVariableHeader.from(this.messageId);
/* 248 */       MqttUnsubscribePayload mqttSubscribePayload = new MqttUnsubscribePayload(this.topicFilters);
/* 249 */       return new MqttUnsubscribeMessage(mqttFixedHeader, mqttVariableHeader, mqttSubscribePayload);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class ConnAckBuilder
/*     */   {
/*     */     private MqttConnectReturnCode returnCode;
/*     */     
/*     */     private boolean sessionPresent;
/*     */ 
/*     */     
/*     */     public ConnAckBuilder returnCode(MqttConnectReturnCode returnCode) {
/* 262 */       this.returnCode = returnCode;
/* 263 */       return this;
/*     */     }
/*     */     
/*     */     public ConnAckBuilder sessionPresent(boolean sessionPresent) {
/* 267 */       this.sessionPresent = sessionPresent;
/* 268 */       return this;
/*     */     }
/*     */     
/*     */     public MqttConnAckMessage build() {
/* 272 */       MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.CONNACK, false, MqttQoS.AT_MOST_ONCE, false, 0);
/*     */       
/* 274 */       MqttConnAckVariableHeader mqttConnAckVariableHeader = new MqttConnAckVariableHeader(this.returnCode, this.sessionPresent);
/*     */       
/* 276 */       return new MqttConnAckMessage(mqttFixedHeader, mqttConnAckVariableHeader);
/*     */     }
/*     */   }
/*     */   
/*     */   public static ConnectBuilder connect() {
/* 281 */     return new ConnectBuilder();
/*     */   }
/*     */   
/*     */   public static ConnAckBuilder connAck() {
/* 285 */     return new ConnAckBuilder();
/*     */   }
/*     */   
/*     */   public static PublishBuilder publish() {
/* 289 */     return new PublishBuilder();
/*     */   }
/*     */   
/*     */   public static SubscribeBuilder subscribe() {
/* 293 */     return new SubscribeBuilder();
/*     */   }
/*     */   
/*     */   public static UnsubscribeBuilder unsubscribe() {
/* 297 */     return new UnsubscribeBuilder();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\mqtt\MqttMessageBuilders.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */