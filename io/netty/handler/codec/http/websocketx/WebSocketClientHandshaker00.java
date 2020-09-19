/*     */ package io.netty.handler.codec.http.websocketx;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.handler.codec.http.DefaultFullHttpRequest;
/*     */ import io.netty.handler.codec.http.FullHttpRequest;
/*     */ import io.netty.handler.codec.http.FullHttpResponse;
/*     */ import io.netty.handler.codec.http.HttpHeaderNames;
/*     */ import io.netty.handler.codec.http.HttpHeaderValues;
/*     */ import io.netty.handler.codec.http.HttpHeaders;
/*     */ import io.netty.handler.codec.http.HttpMethod;
/*     */ import io.netty.handler.codec.http.HttpResponseStatus;
/*     */ import io.netty.handler.codec.http.HttpVersion;
/*     */ import io.netty.util.AsciiString;
/*     */ import java.net.URI;
/*     */ import java.nio.ByteBuffer;
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
/*     */ public class WebSocketClientHandshaker00
/*     */   extends WebSocketClientHandshaker
/*     */ {
/*  46 */   private static final AsciiString WEBSOCKET = AsciiString.cached("WebSocket");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ByteBuf expectedChallengeResponseBytes;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WebSocketClientHandshaker00(URI webSocketURL, WebSocketVersion version, String subprotocol, HttpHeaders customHeaders, int maxFramePayloadLength) {
/*  67 */     super(webSocketURL, version, subprotocol, customHeaders, maxFramePayloadLength);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FullHttpRequest newHandshakeRequest() {
/*  91 */     int spaces1 = WebSocketUtil.randomNumber(1, 12);
/*  92 */     int spaces2 = WebSocketUtil.randomNumber(1, 12);
/*     */     
/*  94 */     int max1 = Integer.MAX_VALUE / spaces1;
/*  95 */     int max2 = Integer.MAX_VALUE / spaces2;
/*     */     
/*  97 */     int number1 = WebSocketUtil.randomNumber(0, max1);
/*  98 */     int number2 = WebSocketUtil.randomNumber(0, max2);
/*     */     
/* 100 */     int product1 = number1 * spaces1;
/* 101 */     int product2 = number2 * spaces2;
/*     */     
/* 103 */     String key1 = Integer.toString(product1);
/* 104 */     String key2 = Integer.toString(product2);
/*     */     
/* 106 */     key1 = insertRandomCharacters(key1);
/* 107 */     key2 = insertRandomCharacters(key2);
/*     */     
/* 109 */     key1 = insertSpaces(key1, spaces1);
/* 110 */     key2 = insertSpaces(key2, spaces2);
/*     */     
/* 112 */     byte[] key3 = WebSocketUtil.randomBytes(8);
/*     */     
/* 114 */     ByteBuffer buffer = ByteBuffer.allocate(4);
/* 115 */     buffer.putInt(number1);
/* 116 */     byte[] number1Array = buffer.array();
/* 117 */     buffer = ByteBuffer.allocate(4);
/* 118 */     buffer.putInt(number2);
/* 119 */     byte[] number2Array = buffer.array();
/*     */     
/* 121 */     byte[] challenge = new byte[16];
/* 122 */     System.arraycopy(number1Array, 0, challenge, 0, 4);
/* 123 */     System.arraycopy(number2Array, 0, challenge, 4, 4);
/* 124 */     System.arraycopy(key3, 0, challenge, 8, 8);
/* 125 */     this.expectedChallengeResponseBytes = Unpooled.wrappedBuffer(WebSocketUtil.md5(challenge));
/*     */ 
/*     */     
/* 128 */     URI wsURL = uri();
/* 129 */     String path = rawPath(wsURL);
/*     */ 
/*     */     
/* 132 */     DefaultFullHttpRequest defaultFullHttpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, path);
/* 133 */     HttpHeaders headers = defaultFullHttpRequest.headers();
/* 134 */     headers.add((CharSequence)HttpHeaderNames.UPGRADE, WEBSOCKET)
/* 135 */       .add((CharSequence)HttpHeaderNames.CONNECTION, HttpHeaderValues.UPGRADE)
/* 136 */       .add((CharSequence)HttpHeaderNames.HOST, websocketHostValue(wsURL))
/* 137 */       .add((CharSequence)HttpHeaderNames.ORIGIN, websocketOriginValue(wsURL))
/* 138 */       .add((CharSequence)HttpHeaderNames.SEC_WEBSOCKET_KEY1, key1)
/* 139 */       .add((CharSequence)HttpHeaderNames.SEC_WEBSOCKET_KEY2, key2);
/*     */     
/* 141 */     String expectedSubprotocol = expectedSubprotocol();
/* 142 */     if (expectedSubprotocol != null && !expectedSubprotocol.isEmpty()) {
/* 143 */       headers.add((CharSequence)HttpHeaderNames.SEC_WEBSOCKET_PROTOCOL, expectedSubprotocol);
/*     */     }
/*     */     
/* 146 */     if (this.customHeaders != null) {
/* 147 */       headers.add(this.customHeaders);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 152 */     headers.set((CharSequence)HttpHeaderNames.CONTENT_LENGTH, Integer.valueOf(key3.length));
/* 153 */     defaultFullHttpRequest.content().writeBytes(key3);
/* 154 */     return (FullHttpRequest)defaultFullHttpRequest;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void verify(FullHttpResponse response) {
/* 179 */     if (!response.status().equals(HttpResponseStatus.SWITCHING_PROTOCOLS)) {
/* 180 */       throw new WebSocketHandshakeException("Invalid handshake response getStatus: " + response.status());
/*     */     }
/*     */     
/* 183 */     HttpHeaders headers = response.headers();
/*     */     
/* 185 */     CharSequence upgrade = headers.get((CharSequence)HttpHeaderNames.UPGRADE);
/* 186 */     if (!WEBSOCKET.contentEqualsIgnoreCase(upgrade)) {
/* 187 */       throw new WebSocketHandshakeException("Invalid handshake response upgrade: " + upgrade);
/*     */     }
/*     */ 
/*     */     
/* 191 */     if (!headers.containsValue((CharSequence)HttpHeaderNames.CONNECTION, (CharSequence)HttpHeaderValues.UPGRADE, true)) {
/* 192 */       throw new WebSocketHandshakeException("Invalid handshake response connection: " + headers
/* 193 */           .get(HttpHeaderNames.CONNECTION));
/*     */     }
/*     */     
/* 196 */     ByteBuf challenge = response.content();
/* 197 */     if (!challenge.equals(this.expectedChallengeResponseBytes)) {
/* 198 */       throw new WebSocketHandshakeException("Invalid challenge");
/*     */     }
/*     */   }
/*     */   
/*     */   private static String insertRandomCharacters(String key) {
/* 203 */     int count = WebSocketUtil.randomNumber(1, 12);
/*     */     
/* 205 */     char[] randomChars = new char[count];
/* 206 */     int randCount = 0;
/* 207 */     while (randCount < count) {
/* 208 */       int rand = (int)(Math.random() * 126.0D + 33.0D);
/* 209 */       if ((33 < rand && rand < 47) || (58 < rand && rand < 126)) {
/* 210 */         randomChars[randCount] = (char)rand;
/* 211 */         randCount++;
/*     */       } 
/*     */     } 
/*     */     
/* 215 */     for (int i = 0; i < count; i++) {
/* 216 */       int split = WebSocketUtil.randomNumber(0, key.length());
/* 217 */       String part1 = key.substring(0, split);
/* 218 */       String part2 = key.substring(split);
/* 219 */       key = part1 + randomChars[i] + part2;
/*     */     } 
/*     */     
/* 222 */     return key;
/*     */   }
/*     */   
/*     */   private static String insertSpaces(String key, int spaces) {
/* 226 */     for (int i = 0; i < spaces; i++) {
/* 227 */       int split = WebSocketUtil.randomNumber(1, key.length() - 1);
/* 228 */       String part1 = key.substring(0, split);
/* 229 */       String part2 = key.substring(split);
/* 230 */       key = part1 + ' ' + part2;
/*     */     } 
/*     */     
/* 233 */     return key;
/*     */   }
/*     */ 
/*     */   
/*     */   protected WebSocketFrameDecoder newWebsocketDecoder() {
/* 238 */     return new WebSocket00FrameDecoder(maxFramePayloadLength());
/*     */   }
/*     */ 
/*     */   
/*     */   protected WebSocketFrameEncoder newWebSocketEncoder() {
/* 243 */     return new WebSocket00FrameEncoder();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\websocketx\WebSocketClientHandshaker00.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */