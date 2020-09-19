package io.netty.handler.codec.http.websocketx.extensions;

public interface WebSocketClientExtensionHandshaker {
  WebSocketExtensionData newRequestData();
  
  WebSocketClientExtension handshakeExtension(WebSocketExtensionData paramWebSocketExtensionData);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\websocketx\extensions\WebSocketClientExtensionHandshaker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */