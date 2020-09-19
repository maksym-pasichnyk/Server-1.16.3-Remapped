/*      */ package io.netty.handler.codec.http2;
/*      */ 
/*      */ import io.netty.buffer.ByteBuf;
/*      */ import io.netty.channel.ChannelPromise;
/*      */ import io.netty.util.collection.IntObjectHashMap;
/*      */ import io.netty.util.collection.IntObjectMap;
/*      */ import io.netty.util.concurrent.Future;
/*      */ import io.netty.util.concurrent.GenericFutureListener;
/*      */ import io.netty.util.concurrent.Promise;
/*      */ import io.netty.util.concurrent.UnaryPromiseNotifier;
/*      */ import io.netty.util.internal.EmptyArrays;
/*      */ import io.netty.util.internal.ObjectUtil;
/*      */ import io.netty.util.internal.PlatformDependent;
/*      */ import io.netty.util.internal.logging.InternalLogger;
/*      */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*      */ import java.util.ArrayDeque;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Queue;
/*      */ import java.util.Set;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class DefaultHttp2Connection
/*      */   implements Http2Connection
/*      */ {
/*   66 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(DefaultHttp2Connection.class);
/*      */   
/*   68 */   final IntObjectMap<Http2Stream> streamMap = (IntObjectMap<Http2Stream>)new IntObjectHashMap();
/*   69 */   final PropertyKeyRegistry propertyKeyRegistry = new PropertyKeyRegistry();
/*   70 */   final ConnectionStream connectionStream = new ConnectionStream();
/*      */ 
/*      */ 
/*      */   
/*      */   final DefaultEndpoint<Http2LocalFlowController> localEndpoint;
/*      */ 
/*      */ 
/*      */   
/*      */   final DefaultEndpoint<Http2RemoteFlowController> remoteEndpoint;
/*      */ 
/*      */ 
/*      */   
/*   82 */   final List<Http2Connection.Listener> listeners = new ArrayList<Http2Connection.Listener>(4);
/*      */ 
/*      */   
/*      */   final ActiveStreams activeStreams;
/*      */   
/*      */   Promise<Void> closePromise;
/*      */ 
/*      */   
/*      */   public DefaultHttp2Connection(boolean server) {
/*   91 */     this(server, 100);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DefaultHttp2Connection(boolean server, int maxReservedStreams) {
/*  100 */     this.activeStreams = new ActiveStreams(this.listeners);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  106 */     this.localEndpoint = new DefaultEndpoint<Http2LocalFlowController>(server, server ? Integer.MAX_VALUE : maxReservedStreams);
/*  107 */     this.remoteEndpoint = new DefaultEndpoint<Http2RemoteFlowController>(!server, maxReservedStreams);
/*      */ 
/*      */     
/*  110 */     this.streamMap.put(this.connectionStream.id(), this.connectionStream);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final boolean isClosed() {
/*  117 */     return (this.closePromise != null);
/*      */   }
/*      */ 
/*      */   
/*      */   public Future<Void> close(Promise<Void> promise) {
/*  122 */     ObjectUtil.checkNotNull(promise, "promise");
/*      */ 
/*      */     
/*  125 */     if (this.closePromise != null) {
/*  126 */       if (this.closePromise != promise)
/*      */       {
/*  128 */         if (promise instanceof ChannelPromise && ((ChannelPromise)this.closePromise).isVoid()) {
/*  129 */           this.closePromise = promise;
/*      */         } else {
/*  131 */           this.closePromise.addListener((GenericFutureListener)new UnaryPromiseNotifier(promise));
/*      */         }  } 
/*      */     } else {
/*  134 */       this.closePromise = promise;
/*      */     } 
/*  136 */     if (isStreamMapEmpty()) {
/*  137 */       promise.trySuccess(null);
/*  138 */       return (Future<Void>)promise;
/*      */     } 
/*      */     
/*  141 */     Iterator<IntObjectMap.PrimitiveEntry<Http2Stream>> itr = this.streamMap.entries().iterator();
/*      */ 
/*      */     
/*  144 */     if (this.activeStreams.allowModifications()) {
/*  145 */       this.activeStreams.incrementPendingIterations();
/*      */       try {
/*  147 */         while (itr.hasNext()) {
/*  148 */           DefaultStream stream = (DefaultStream)((IntObjectMap.PrimitiveEntry)itr.next()).value();
/*  149 */           if (stream.id() != 0)
/*      */           {
/*      */ 
/*      */             
/*  153 */             stream.close(itr);
/*      */           }
/*      */         } 
/*      */       } finally {
/*  157 */         this.activeStreams.decrementPendingIterations();
/*      */       } 
/*      */     } else {
/*  160 */       while (itr.hasNext()) {
/*  161 */         Http2Stream stream = (Http2Stream)((IntObjectMap.PrimitiveEntry)itr.next()).value();
/*  162 */         if (stream.id() != 0)
/*      */         {
/*      */           
/*  165 */           stream.close();
/*      */         }
/*      */       } 
/*      */     } 
/*  169 */     return (Future<Void>)this.closePromise;
/*      */   }
/*      */ 
/*      */   
/*      */   public void addListener(Http2Connection.Listener listener) {
/*  174 */     this.listeners.add(listener);
/*      */   }
/*      */ 
/*      */   
/*      */   public void removeListener(Http2Connection.Listener listener) {
/*  179 */     this.listeners.remove(listener);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isServer() {
/*  184 */     return this.localEndpoint.isServer();
/*      */   }
/*      */ 
/*      */   
/*      */   public Http2Stream connectionStream() {
/*  189 */     return this.connectionStream;
/*      */   }
/*      */ 
/*      */   
/*      */   public Http2Stream stream(int streamId) {
/*  194 */     return (Http2Stream)this.streamMap.get(streamId);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean streamMayHaveExisted(int streamId) {
/*  199 */     return (this.remoteEndpoint.mayHaveCreatedStream(streamId) || this.localEndpoint.mayHaveCreatedStream(streamId));
/*      */   }
/*      */ 
/*      */   
/*      */   public int numActiveStreams() {
/*  204 */     return this.activeStreams.size();
/*      */   }
/*      */ 
/*      */   
/*      */   public Http2Stream forEachActiveStream(Http2StreamVisitor visitor) throws Http2Exception {
/*  209 */     return this.activeStreams.forEachActiveStream(visitor);
/*      */   }
/*      */ 
/*      */   
/*      */   public Http2Connection.Endpoint<Http2LocalFlowController> local() {
/*  214 */     return this.localEndpoint;
/*      */   }
/*      */ 
/*      */   
/*      */   public Http2Connection.Endpoint<Http2RemoteFlowController> remote() {
/*  219 */     return this.remoteEndpoint;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean goAwayReceived() {
/*  224 */     return (this.localEndpoint.lastStreamKnownByPeer >= 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public void goAwayReceived(final int lastKnownStream, long errorCode, ByteBuf debugData) {
/*  229 */     this.localEndpoint.lastStreamKnownByPeer(lastKnownStream);
/*  230 */     for (int i = 0; i < this.listeners.size(); i++) {
/*      */       try {
/*  232 */         ((Http2Connection.Listener)this.listeners.get(i)).onGoAwayReceived(lastKnownStream, errorCode, debugData);
/*  233 */       } catch (Throwable cause) {
/*  234 */         logger.error("Caught Throwable from listener onGoAwayReceived.", cause);
/*      */       } 
/*      */     } 
/*      */     
/*      */     try {
/*  239 */       forEachActiveStream(new Http2StreamVisitor()
/*      */           {
/*      */             public boolean visit(Http2Stream stream) {
/*  242 */               if (stream.id() > lastKnownStream && DefaultHttp2Connection.this.localEndpoint.isValidStreamId(stream.id())) {
/*  243 */                 stream.close();
/*      */               }
/*  245 */               return true;
/*      */             }
/*      */           });
/*  248 */     } catch (Http2Exception e) {
/*  249 */       PlatformDependent.throwException(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean goAwaySent() {
/*  255 */     return (this.remoteEndpoint.lastStreamKnownByPeer >= 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public void goAwaySent(final int lastKnownStream, long errorCode, ByteBuf debugData) {
/*  260 */     this.remoteEndpoint.lastStreamKnownByPeer(lastKnownStream);
/*  261 */     for (int i = 0; i < this.listeners.size(); i++) {
/*      */       try {
/*  263 */         ((Http2Connection.Listener)this.listeners.get(i)).onGoAwaySent(lastKnownStream, errorCode, debugData);
/*  264 */       } catch (Throwable cause) {
/*  265 */         logger.error("Caught Throwable from listener onGoAwaySent.", cause);
/*      */       } 
/*      */     } 
/*      */     
/*      */     try {
/*  270 */       forEachActiveStream(new Http2StreamVisitor()
/*      */           {
/*      */             public boolean visit(Http2Stream stream) {
/*  273 */               if (stream.id() > lastKnownStream && DefaultHttp2Connection.this.remoteEndpoint.isValidStreamId(stream.id())) {
/*  274 */                 stream.close();
/*      */               }
/*  276 */               return true;
/*      */             }
/*      */           });
/*  279 */     } catch (Http2Exception e) {
/*  280 */       PlatformDependent.throwException(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isStreamMapEmpty() {
/*  288 */     return (this.streamMap.size() == 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void removeStream(DefaultStream stream, Iterator<?> itr) {
/*      */     boolean removed;
/*  299 */     if (itr == null) {
/*  300 */       removed = (this.streamMap.remove(stream.id()) != null);
/*      */     } else {
/*  302 */       itr.remove();
/*  303 */       removed = true;
/*      */     } 
/*      */     
/*  306 */     if (removed) {
/*  307 */       for (int i = 0; i < this.listeners.size(); i++) {
/*      */         try {
/*  309 */           ((Http2Connection.Listener)this.listeners.get(i)).onStreamRemoved(stream);
/*  310 */         } catch (Throwable cause) {
/*  311 */           logger.error("Caught Throwable from listener onStreamRemoved.", cause);
/*      */         } 
/*      */       } 
/*      */       
/*  315 */       if (this.closePromise != null && isStreamMapEmpty()) {
/*  316 */         this.closePromise.trySuccess(null);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   static Http2Stream.State activeState(int streamId, Http2Stream.State initialState, boolean isLocal, boolean halfClosed) throws Http2Exception {
/*  323 */     switch (initialState) {
/*      */       case IDLE:
/*  325 */         return halfClosed ? (isLocal ? Http2Stream.State.HALF_CLOSED_LOCAL : Http2Stream.State.HALF_CLOSED_REMOTE) : Http2Stream.State.OPEN;
/*      */       case RESERVED_LOCAL:
/*  327 */         return Http2Stream.State.HALF_CLOSED_REMOTE;
/*      */       case RESERVED_REMOTE:
/*  329 */         return Http2Stream.State.HALF_CLOSED_LOCAL;
/*      */     } 
/*  331 */     throw Http2Exception.streamError(streamId, Http2Error.PROTOCOL_ERROR, "Attempting to open a stream in an invalid state: " + initialState, new Object[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void notifyHalfClosed(Http2Stream stream) {
/*  337 */     for (int i = 0; i < this.listeners.size(); i++) {
/*      */       try {
/*  339 */         ((Http2Connection.Listener)this.listeners.get(i)).onStreamHalfClosed(stream);
/*  340 */       } catch (Throwable cause) {
/*  341 */         logger.error("Caught Throwable from listener onStreamHalfClosed.", cause);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   void notifyClosed(Http2Stream stream) {
/*  347 */     for (int i = 0; i < this.listeners.size(); i++) {
/*      */       try {
/*  349 */         ((Http2Connection.Listener)this.listeners.get(i)).onStreamClosed(stream);
/*  350 */       } catch (Throwable cause) {
/*  351 */         logger.error("Caught Throwable from listener onStreamClosed.", cause);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public Http2Connection.PropertyKey newKey() {
/*  358 */     return this.propertyKeyRegistry.newKey();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final DefaultPropertyKey verifyKey(Http2Connection.PropertyKey key) {
/*  369 */     return ((DefaultPropertyKey)ObjectUtil.checkNotNull(key, "key")).verifyConnection(this);
/*      */   }
/*      */ 
/*      */   
/*      */   private class DefaultStream
/*      */     implements Http2Stream
/*      */   {
/*      */     private static final byte META_STATE_SENT_RST = 1;
/*      */     private static final byte META_STATE_SENT_HEADERS = 2;
/*      */     private static final byte META_STATE_SENT_TRAILERS = 4;
/*      */     private static final byte META_STATE_SENT_PUSHPROMISE = 8;
/*      */     private static final byte META_STATE_RECV_HEADERS = 16;
/*      */     private static final byte META_STATE_RECV_TRAILERS = 32;
/*      */     private final int id;
/*  383 */     private final PropertyMap properties = new PropertyMap();
/*      */     private Http2Stream.State state;
/*      */     private byte metaState;
/*      */     
/*      */     DefaultStream(int id, Http2Stream.State state) {
/*  388 */       this.id = id;
/*  389 */       this.state = state;
/*      */     }
/*      */ 
/*      */     
/*      */     public final int id() {
/*  394 */       return this.id;
/*      */     }
/*      */ 
/*      */     
/*      */     public final Http2Stream.State state() {
/*  399 */       return this.state;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isResetSent() {
/*  404 */       return ((this.metaState & 0x1) != 0);
/*      */     }
/*      */ 
/*      */     
/*      */     public Http2Stream resetSent() {
/*  409 */       this.metaState = (byte)(this.metaState | 0x1);
/*  410 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public Http2Stream headersSent(boolean isInformational) {
/*  415 */       if (!isInformational) {
/*  416 */         this.metaState = (byte)(this.metaState | (isHeadersSent() ? 4 : 2));
/*      */       }
/*  418 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isHeadersSent() {
/*  423 */       return ((this.metaState & 0x2) != 0);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isTrailersSent() {
/*  428 */       return ((this.metaState & 0x4) != 0);
/*      */     }
/*      */ 
/*      */     
/*      */     public Http2Stream headersReceived(boolean isInformational) {
/*  433 */       if (!isInformational) {
/*  434 */         this.metaState = (byte)(this.metaState | (isHeadersReceived() ? 32 : 16));
/*      */       }
/*  436 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isHeadersReceived() {
/*  441 */       return ((this.metaState & 0x10) != 0);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isTrailersReceived() {
/*  446 */       return ((this.metaState & 0x20) != 0);
/*      */     }
/*      */ 
/*      */     
/*      */     public Http2Stream pushPromiseSent() {
/*  451 */       this.metaState = (byte)(this.metaState | 0x8);
/*  452 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isPushPromiseSent() {
/*  457 */       return ((this.metaState & 0x8) != 0);
/*      */     }
/*      */ 
/*      */     
/*      */     public final <V> V setProperty(Http2Connection.PropertyKey key, V value) {
/*  462 */       return this.properties.add(DefaultHttp2Connection.this.verifyKey(key), value);
/*      */     }
/*      */ 
/*      */     
/*      */     public final <V> V getProperty(Http2Connection.PropertyKey key) {
/*  467 */       return this.properties.get(DefaultHttp2Connection.this.verifyKey(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public final <V> V removeProperty(Http2Connection.PropertyKey key) {
/*  472 */       return this.properties.remove(DefaultHttp2Connection.this.verifyKey(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public Http2Stream open(boolean halfClosed) throws Http2Exception {
/*  477 */       this.state = DefaultHttp2Connection.activeState(this.id, this.state, isLocal(), halfClosed);
/*  478 */       if (!createdBy().canOpenStream()) {
/*  479 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Maximum active streams violated for this endpoint.", new Object[0]);
/*      */       }
/*      */       
/*  482 */       activate();
/*  483 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     void activate() {
/*  489 */       if (this.state == Http2Stream.State.HALF_CLOSED_LOCAL) {
/*  490 */         headersSent(false);
/*  491 */       } else if (this.state == Http2Stream.State.HALF_CLOSED_REMOTE) {
/*  492 */         headersReceived(false);
/*      */       } 
/*  494 */       DefaultHttp2Connection.this.activeStreams.activate(this);
/*      */     }
/*      */     
/*      */     Http2Stream close(Iterator<?> itr) {
/*  498 */       if (this.state == Http2Stream.State.CLOSED) {
/*  499 */         return this;
/*      */       }
/*      */       
/*  502 */       this.state = Http2Stream.State.CLOSED;
/*      */       
/*  504 */       (createdBy()).numStreams--;
/*  505 */       DefaultHttp2Connection.this.activeStreams.deactivate(this, itr);
/*  506 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public Http2Stream close() {
/*  511 */       return close(null);
/*      */     }
/*      */ 
/*      */     
/*      */     public Http2Stream closeLocalSide() {
/*  516 */       switch (this.state) {
/*      */         case OPEN:
/*  518 */           this.state = Http2Stream.State.HALF_CLOSED_LOCAL;
/*  519 */           DefaultHttp2Connection.this.notifyHalfClosed(this);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case HALF_CLOSED_LOCAL:
/*  527 */           return this;
/*      */       } 
/*      */       close();
/*      */     }
/*      */     public Http2Stream closeRemoteSide() {
/*  532 */       switch (this.state) {
/*      */         case OPEN:
/*  534 */           this.state = Http2Stream.State.HALF_CLOSED_REMOTE;
/*  535 */           DefaultHttp2Connection.this.notifyHalfClosed(this);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case HALF_CLOSED_REMOTE:
/*  543 */           return this;
/*      */       } 
/*      */       close();
/*      */     } DefaultHttp2Connection.DefaultEndpoint<? extends Http2FlowController> createdBy() {
/*  547 */       return DefaultHttp2Connection.this.localEndpoint.isValidStreamId(this.id) ? (DefaultHttp2Connection.DefaultEndpoint)DefaultHttp2Connection.this.localEndpoint : (DefaultHttp2Connection.DefaultEndpoint)DefaultHttp2Connection.this.remoteEndpoint;
/*      */     }
/*      */     
/*      */     final boolean isLocal() {
/*  551 */       return DefaultHttp2Connection.this.localEndpoint.isValidStreamId(this.id);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private class PropertyMap
/*      */     {
/*  558 */       Object[] values = EmptyArrays.EMPTY_OBJECTS;
/*      */       
/*      */       <V> V add(DefaultHttp2Connection.DefaultPropertyKey key, V value) {
/*  561 */         resizeIfNecessary(key.index);
/*      */         
/*  563 */         V prevValue = (V)this.values[key.index];
/*  564 */         this.values[key.index] = value;
/*  565 */         return prevValue;
/*      */       }
/*      */ 
/*      */       
/*      */       <V> V get(DefaultHttp2Connection.DefaultPropertyKey key) {
/*  570 */         if (key.index >= this.values.length) {
/*  571 */           return null;
/*      */         }
/*  573 */         return (V)this.values[key.index];
/*      */       }
/*      */ 
/*      */       
/*      */       <V> V remove(DefaultHttp2Connection.DefaultPropertyKey key) {
/*  578 */         V prevValue = null;
/*  579 */         if (key.index < this.values.length) {
/*  580 */           prevValue = (V)this.values[key.index];
/*  581 */           this.values[key.index] = null;
/*      */         } 
/*  583 */         return prevValue;
/*      */       }
/*      */       
/*      */       void resizeIfNecessary(int index) {
/*  587 */         if (index >= this.values.length)
/*  588 */           this.values = Arrays.copyOf(this.values, DefaultHttp2Connection.this.propertyKeyRegistry.size()); 
/*      */       }
/*      */       
/*      */       private PropertyMap() {}
/*      */     }
/*      */   }
/*      */   
/*      */   private final class ConnectionStream
/*      */     extends DefaultStream
/*      */   {
/*      */     ConnectionStream() {
/*  599 */       super(0, Http2Stream.State.IDLE);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isResetSent() {
/*  604 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     DefaultHttp2Connection.DefaultEndpoint<? extends Http2FlowController> createdBy() {
/*  609 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     public Http2Stream resetSent() {
/*  614 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public Http2Stream open(boolean halfClosed) {
/*  619 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public Http2Stream close() {
/*  624 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public Http2Stream closeLocalSide() {
/*  629 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public Http2Stream closeRemoteSide() {
/*  634 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public Http2Stream headersSent(boolean isInformational) {
/*  639 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isHeadersSent() {
/*  644 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public Http2Stream pushPromiseSent() {
/*  649 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isPushPromiseSent() {
/*  654 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final class DefaultEndpoint<F extends Http2FlowController>
/*      */     implements Http2Connection.Endpoint<F>
/*      */   {
/*      */     private final boolean server;
/*      */ 
/*      */ 
/*      */     
/*      */     private int nextStreamIdToCreate;
/*      */ 
/*      */ 
/*      */     
/*      */     private int nextReservationStreamId;
/*      */ 
/*      */ 
/*      */     
/*  676 */     private int lastStreamKnownByPeer = -1;
/*      */     
/*      */     private boolean pushToAllowed = true;
/*      */     private F flowController;
/*      */     private int maxStreams;
/*      */     private int maxActiveStreams;
/*      */     private final int maxReservedStreams;
/*      */     int numActiveStreams;
/*      */     int numStreams;
/*      */     
/*      */     DefaultEndpoint(boolean server, int maxReservedStreams) {
/*  687 */       this.server = server;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  693 */       if (server) {
/*  694 */         this.nextStreamIdToCreate = 2;
/*  695 */         this.nextReservationStreamId = 0;
/*      */       } else {
/*  697 */         this.nextStreamIdToCreate = 1;
/*      */         
/*  699 */         this.nextReservationStreamId = 1;
/*      */       } 
/*      */ 
/*      */       
/*  703 */       this.pushToAllowed = !server;
/*  704 */       this.maxActiveStreams = Integer.MAX_VALUE;
/*  705 */       this.maxReservedStreams = ObjectUtil.checkPositiveOrZero(maxReservedStreams, "maxReservedStreams");
/*  706 */       updateMaxStreams();
/*      */     }
/*      */ 
/*      */     
/*      */     public int incrementAndGetNextStreamId() {
/*  711 */       return (this.nextReservationStreamId >= 0) ? (this.nextReservationStreamId += 2) : this.nextReservationStreamId;
/*      */     }
/*      */     
/*      */     private void incrementExpectedStreamId(int streamId) {
/*  715 */       if (streamId > this.nextReservationStreamId && this.nextReservationStreamId >= 0) {
/*  716 */         this.nextReservationStreamId = streamId;
/*      */       }
/*  718 */       this.nextStreamIdToCreate = streamId + 2;
/*  719 */       this.numStreams++;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isValidStreamId(int streamId) {
/*  724 */       return (streamId > 0 && this.server == (((streamId & 0x1) == 0)));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean mayHaveCreatedStream(int streamId) {
/*  729 */       return (isValidStreamId(streamId) && streamId <= lastStreamCreated());
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canOpenStream() {
/*  734 */       return (this.numActiveStreams < this.maxActiveStreams);
/*      */     }
/*      */ 
/*      */     
/*      */     public DefaultHttp2Connection.DefaultStream createStream(int streamId, boolean halfClosed) throws Http2Exception {
/*  739 */       Http2Stream.State state = DefaultHttp2Connection.activeState(streamId, Http2Stream.State.IDLE, isLocal(), halfClosed);
/*      */       
/*  741 */       checkNewStreamAllowed(streamId, state);
/*      */ 
/*      */       
/*  744 */       DefaultHttp2Connection.DefaultStream stream = new DefaultHttp2Connection.DefaultStream(streamId, state);
/*      */       
/*  746 */       incrementExpectedStreamId(streamId);
/*      */       
/*  748 */       addStream(stream);
/*      */       
/*  750 */       stream.activate();
/*  751 */       return stream;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean created(Http2Stream stream) {
/*  756 */       return (stream instanceof DefaultHttp2Connection.DefaultStream && ((DefaultHttp2Connection.DefaultStream)stream).createdBy() == this);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isServer() {
/*  761 */       return this.server;
/*      */     }
/*      */ 
/*      */     
/*      */     public DefaultHttp2Connection.DefaultStream reservePushStream(int streamId, Http2Stream parent) throws Http2Exception {
/*  766 */       if (parent == null) {
/*  767 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Parent stream missing", new Object[0]);
/*      */       }
/*  769 */       if (isLocal() ? !parent.state().localSideOpen() : !parent.state().remoteSideOpen()) {
/*  770 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Stream %d is not open for sending push promise", new Object[] { Integer.valueOf(parent.id()) });
/*      */       }
/*  772 */       if (!opposite().allowPushTo()) {
/*  773 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Server push not allowed to opposite endpoint", new Object[0]);
/*      */       }
/*  775 */       Http2Stream.State state = isLocal() ? Http2Stream.State.RESERVED_LOCAL : Http2Stream.State.RESERVED_REMOTE;
/*  776 */       checkNewStreamAllowed(streamId, state);
/*      */ 
/*      */       
/*  779 */       DefaultHttp2Connection.DefaultStream stream = new DefaultHttp2Connection.DefaultStream(streamId, state);
/*      */       
/*  781 */       incrementExpectedStreamId(streamId);
/*      */ 
/*      */       
/*  784 */       addStream(stream);
/*  785 */       return stream;
/*      */     }
/*      */ 
/*      */     
/*      */     private void addStream(DefaultHttp2Connection.DefaultStream stream) {
/*  790 */       DefaultHttp2Connection.this.streamMap.put(stream.id(), stream);
/*      */ 
/*      */       
/*  793 */       for (int i = 0; i < DefaultHttp2Connection.this.listeners.size(); i++) {
/*      */         try {
/*  795 */           ((Http2Connection.Listener)DefaultHttp2Connection.this.listeners.get(i)).onStreamAdded(stream);
/*  796 */         } catch (Throwable cause) {
/*  797 */           DefaultHttp2Connection.logger.error("Caught Throwable from listener onStreamAdded.", cause);
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void allowPushTo(boolean allow) {
/*  804 */       if (allow && this.server) {
/*  805 */         throw new IllegalArgumentException("Servers do not allow push");
/*      */       }
/*  807 */       this.pushToAllowed = allow;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean allowPushTo() {
/*  812 */       return this.pushToAllowed;
/*      */     }
/*      */ 
/*      */     
/*      */     public int numActiveStreams() {
/*  817 */       return this.numActiveStreams;
/*      */     }
/*      */ 
/*      */     
/*      */     public int maxActiveStreams() {
/*  822 */       return this.maxActiveStreams;
/*      */     }
/*      */ 
/*      */     
/*      */     public void maxActiveStreams(int maxActiveStreams) {
/*  827 */       this.maxActiveStreams = maxActiveStreams;
/*  828 */       updateMaxStreams();
/*      */     }
/*      */ 
/*      */     
/*      */     public int lastStreamCreated() {
/*  833 */       return (this.nextStreamIdToCreate > 1) ? (this.nextStreamIdToCreate - 2) : 0;
/*      */     }
/*      */ 
/*      */     
/*      */     public int lastStreamKnownByPeer() {
/*  838 */       return this.lastStreamKnownByPeer;
/*      */     }
/*      */     
/*      */     private void lastStreamKnownByPeer(int lastKnownStream) {
/*  842 */       this.lastStreamKnownByPeer = lastKnownStream;
/*      */     }
/*      */ 
/*      */     
/*      */     public F flowController() {
/*  847 */       return this.flowController;
/*      */     }
/*      */ 
/*      */     
/*      */     public void flowController(F flowController) {
/*  852 */       this.flowController = (F)ObjectUtil.checkNotNull(flowController, "flowController");
/*      */     }
/*      */ 
/*      */     
/*      */     public Http2Connection.Endpoint<? extends Http2FlowController> opposite() {
/*  857 */       return isLocal() ? (Http2Connection.Endpoint)DefaultHttp2Connection.this.remoteEndpoint : (Http2Connection.Endpoint)DefaultHttp2Connection.this.localEndpoint;
/*      */     }
/*      */     
/*      */     private void updateMaxStreams() {
/*  861 */       this.maxStreams = (int)Math.min(2147483647L, this.maxActiveStreams + this.maxReservedStreams);
/*      */     }
/*      */     
/*      */     private void checkNewStreamAllowed(int streamId, Http2Stream.State state) throws Http2Exception {
/*  865 */       assert state != Http2Stream.State.IDLE;
/*  866 */       if (DefaultHttp2Connection.this.goAwayReceived() && streamId > DefaultHttp2Connection.this.localEndpoint.lastStreamKnownByPeer())
/*  867 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Cannot create stream %d since this endpoint has received a GOAWAY frame with last stream id %d.", new Object[] {
/*  868 */               Integer.valueOf(streamId), 
/*  869 */               Integer.valueOf(this.this$0.localEndpoint.lastStreamKnownByPeer())
/*      */             }); 
/*  871 */       if (!isValidStreamId(streamId)) {
/*  872 */         if (streamId < 0) {
/*  873 */           throw new Http2NoMoreStreamIdsException();
/*      */         }
/*  875 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Request stream %d is not correct for %s connection", new Object[] { Integer.valueOf(streamId), this.server ? "server" : "client" });
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  880 */       if (streamId < this.nextStreamIdToCreate)
/*  881 */         throw Http2Exception.closedStreamError(Http2Error.PROTOCOL_ERROR, "Request stream %d is behind the next expected stream %d", new Object[] {
/*  882 */               Integer.valueOf(streamId), Integer.valueOf(this.nextStreamIdToCreate)
/*      */             }); 
/*  884 */       if (this.nextStreamIdToCreate <= 0) {
/*  885 */         throw Http2Exception.connectionError(Http2Error.REFUSED_STREAM, "Stream IDs are exhausted for this endpoint.", new Object[0]);
/*      */       }
/*  887 */       boolean isReserved = (state == Http2Stream.State.RESERVED_LOCAL || state == Http2Stream.State.RESERVED_REMOTE);
/*  888 */       if ((!isReserved && !canOpenStream()) || (isReserved && this.numStreams >= this.maxStreams)) {
/*  889 */         throw Http2Exception.streamError(streamId, Http2Error.REFUSED_STREAM, "Maximum active streams violated for this endpoint.", new Object[0]);
/*      */       }
/*  891 */       if (DefaultHttp2Connection.this.isClosed())
/*  892 */         throw Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, "Attempted to create stream id %d after connection was closed", new Object[] {
/*  893 */               Integer.valueOf(streamId)
/*      */             }); 
/*      */     }
/*      */     
/*      */     private boolean isLocal() {
/*  898 */       return (this == DefaultHttp2Connection.this.localEndpoint);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final class ActiveStreams
/*      */   {
/*      */     private final List<Http2Connection.Listener> listeners;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  922 */     private final Queue<DefaultHttp2Connection.Event> pendingEvents = new ArrayDeque<DefaultHttp2Connection.Event>(4);
/*  923 */     private final Set<Http2Stream> streams = new LinkedHashSet<Http2Stream>();
/*      */     private int pendingIterations;
/*      */     
/*      */     public ActiveStreams(List<Http2Connection.Listener> listeners) {
/*  927 */       this.listeners = listeners;
/*      */     }
/*      */     
/*      */     public int size() {
/*  931 */       return this.streams.size();
/*      */     }
/*      */     
/*      */     public void activate(final DefaultHttp2Connection.DefaultStream stream) {
/*  935 */       if (allowModifications()) {
/*  936 */         addToActiveStreams(stream);
/*      */       } else {
/*  938 */         this.pendingEvents.add(new DefaultHttp2Connection.Event()
/*      */             {
/*      */               public void process() {
/*  941 */                 DefaultHttp2Connection.ActiveStreams.this.addToActiveStreams(stream);
/*      */               }
/*      */             });
/*      */       } 
/*      */     }
/*      */     
/*      */     public void deactivate(final DefaultHttp2Connection.DefaultStream stream, final Iterator<?> itr) {
/*  948 */       if (allowModifications() || itr != null) {
/*  949 */         removeFromActiveStreams(stream, itr);
/*      */       } else {
/*  951 */         this.pendingEvents.add(new DefaultHttp2Connection.Event()
/*      */             {
/*      */               public void process() {
/*  954 */                 DefaultHttp2Connection.ActiveStreams.this.removeFromActiveStreams(stream, itr);
/*      */               }
/*      */             });
/*      */       } 
/*      */     }
/*      */     
/*      */     public Http2Stream forEachActiveStream(Http2StreamVisitor visitor) throws Http2Exception {
/*  961 */       incrementPendingIterations();
/*      */       try {
/*  963 */         for (Http2Stream stream : this.streams) {
/*  964 */           if (!visitor.visit(stream)) {
/*  965 */             return stream;
/*      */           }
/*      */         } 
/*  968 */         return null;
/*      */       } finally {
/*  970 */         decrementPendingIterations();
/*      */       } 
/*      */     }
/*      */     
/*      */     void addToActiveStreams(DefaultHttp2Connection.DefaultStream stream) {
/*  975 */       if (this.streams.add(stream)) {
/*      */         
/*  977 */         (stream.createdBy()).numActiveStreams++;
/*      */         
/*  979 */         for (int i = 0; i < this.listeners.size(); i++) {
/*      */           try {
/*  981 */             ((Http2Connection.Listener)this.listeners.get(i)).onStreamActive(stream);
/*  982 */           } catch (Throwable cause) {
/*  983 */             DefaultHttp2Connection.logger.error("Caught Throwable from listener onStreamActive.", cause);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*      */     void removeFromActiveStreams(DefaultHttp2Connection.DefaultStream stream, Iterator<?> itr) {
/*  990 */       if (this.streams.remove(stream)) {
/*      */         
/*  992 */         (stream.createdBy()).numActiveStreams--;
/*  993 */         DefaultHttp2Connection.this.notifyClosed(stream);
/*      */       } 
/*  995 */       DefaultHttp2Connection.this.removeStream(stream, itr);
/*      */     }
/*      */     
/*      */     boolean allowModifications() {
/*  999 */       return (this.pendingIterations == 0);
/*      */     }
/*      */     
/*      */     void incrementPendingIterations() {
/* 1003 */       this.pendingIterations++;
/*      */     }
/*      */     
/*      */     void decrementPendingIterations() {
/* 1007 */       this.pendingIterations--;
/* 1008 */       if (allowModifications()) {
/*      */         while (true) {
/* 1010 */           DefaultHttp2Connection.Event event = this.pendingEvents.poll();
/* 1011 */           if (event == null) {
/*      */             break;
/*      */           }
/*      */           try {
/* 1015 */             event.process();
/* 1016 */           } catch (Throwable cause) {
/* 1017 */             DefaultHttp2Connection.logger.error("Caught Throwable while processing pending ActiveStreams$Event.", cause);
/*      */           } 
/*      */         } 
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   final class DefaultPropertyKey
/*      */     implements Http2Connection.PropertyKey
/*      */   {
/*      */     final int index;
/*      */     
/*      */     DefaultPropertyKey(int index) {
/* 1031 */       this.index = index;
/*      */     }
/*      */     
/*      */     DefaultPropertyKey verifyConnection(Http2Connection connection) {
/* 1035 */       if (connection != DefaultHttp2Connection.this) {
/* 1036 */         throw new IllegalArgumentException("Using a key that was not created by this connection");
/*      */       }
/* 1038 */       return this;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final class PropertyKeyRegistry
/*      */   {
/* 1051 */     final List<DefaultHttp2Connection.DefaultPropertyKey> keys = new ArrayList<DefaultHttp2Connection.DefaultPropertyKey>(4);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     DefaultHttp2Connection.DefaultPropertyKey newKey() {
/* 1057 */       DefaultHttp2Connection.DefaultPropertyKey key = new DefaultHttp2Connection.DefaultPropertyKey(this.keys.size());
/* 1058 */       this.keys.add(key);
/* 1059 */       return key;
/*      */     }
/*      */     
/*      */     int size() {
/* 1063 */       return this.keys.size();
/*      */     }
/*      */     
/*      */     private PropertyKeyRegistry() {}
/*      */   }
/*      */   
/*      */   static interface Event {
/*      */     void process();
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\DefaultHttp2Connection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */