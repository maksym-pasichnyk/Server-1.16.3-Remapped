package io.netty.handler.codec.http;

public interface HttpRequest extends HttpMessage {
  @Deprecated
  HttpMethod getMethod();
  
  HttpMethod method();
  
  HttpRequest setMethod(HttpMethod paramHttpMethod);
  
  @Deprecated
  String getUri();
  
  String uri();
  
  HttpRequest setUri(String paramString);
  
  HttpRequest setProtocolVersion(HttpVersion paramHttpVersion);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\HttpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */