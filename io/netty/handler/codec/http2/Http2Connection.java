package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;

public interface Http2Connection {
  Future<Void> close(Promise<Void> paramPromise);
  
  PropertyKey newKey();
  
  void addListener(Listener paramListener);
  
  void removeListener(Listener paramListener);
  
  Http2Stream stream(int paramInt);
  
  boolean streamMayHaveExisted(int paramInt);
  
  Http2Stream connectionStream();
  
  int numActiveStreams();
  
  Http2Stream forEachActiveStream(Http2StreamVisitor paramHttp2StreamVisitor) throws Http2Exception;
  
  boolean isServer();
  
  Endpoint<Http2LocalFlowController> local();
  
  Endpoint<Http2RemoteFlowController> remote();
  
  boolean goAwayReceived();
  
  void goAwayReceived(int paramInt, long paramLong, ByteBuf paramByteBuf);
  
  boolean goAwaySent();
  
  void goAwaySent(int paramInt, long paramLong, ByteBuf paramByteBuf);
  
  public static interface PropertyKey {}
  
  public static interface Endpoint<F extends Http2FlowController> {
    int incrementAndGetNextStreamId();
    
    boolean isValidStreamId(int param1Int);
    
    boolean mayHaveCreatedStream(int param1Int);
    
    boolean created(Http2Stream param1Http2Stream);
    
    boolean canOpenStream();
    
    Http2Stream createStream(int param1Int, boolean param1Boolean) throws Http2Exception;
    
    Http2Stream reservePushStream(int param1Int, Http2Stream param1Http2Stream) throws Http2Exception;
    
    boolean isServer();
    
    void allowPushTo(boolean param1Boolean);
    
    boolean allowPushTo();
    
    int numActiveStreams();
    
    int maxActiveStreams();
    
    void maxActiveStreams(int param1Int);
    
    int lastStreamCreated();
    
    int lastStreamKnownByPeer();
    
    F flowController();
    
    void flowController(F param1F);
    
    Endpoint<? extends Http2FlowController> opposite();
  }
  
  public static interface Listener {
    void onStreamAdded(Http2Stream param1Http2Stream);
    
    void onStreamActive(Http2Stream param1Http2Stream);
    
    void onStreamHalfClosed(Http2Stream param1Http2Stream);
    
    void onStreamClosed(Http2Stream param1Http2Stream);
    
    void onStreamRemoved(Http2Stream param1Http2Stream);
    
    void onGoAwaySent(int param1Int, long param1Long, ByteBuf param1ByteBuf);
    
    void onGoAwayReceived(int param1Int, long param1Long, ByteBuf param1ByteBuf);
  }
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\Http2Connection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */