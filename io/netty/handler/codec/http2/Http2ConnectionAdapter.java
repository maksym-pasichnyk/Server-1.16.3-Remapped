package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;

public class Http2ConnectionAdapter implements Http2Connection.Listener {
  public void onStreamAdded(Http2Stream stream) {}
  
  public void onStreamActive(Http2Stream stream) {}
  
  public void onStreamHalfClosed(Http2Stream stream) {}
  
  public void onStreamClosed(Http2Stream stream) {}
  
  public void onStreamRemoved(Http2Stream stream) {}
  
  public void onGoAwaySent(int lastStreamId, long errorCode, ByteBuf debugData) {}
  
  public void onGoAwayReceived(int lastStreamId, long errorCode, ByteBuf debugData) {}
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\Http2ConnectionAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */