/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.util.collection.IntCollections;
/*     */ import io.netty.util.collection.IntObjectHashMap;
/*     */ import io.netty.util.collection.IntObjectMap;
/*     */ import io.netty.util.internal.DefaultPriorityQueue;
/*     */ import io.netty.util.internal.EmptyPriorityQueue;
/*     */ import io.netty.util.internal.MathUtil;
/*     */ import io.netty.util.internal.PriorityQueue;
/*     */ import io.netty.util.internal.PriorityQueueNode;
/*     */ import io.netty.util.internal.SystemPropertyUtil;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
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
/*     */ public final class WeightedFairQueueByteDistributor
/*     */   implements StreamByteDistributor
/*     */ {
/*  68 */   static final int INITIAL_CHILDREN_MAP_SIZE = Math.max(1, SystemPropertyUtil.getInt("io.netty.http2.childrenMapSize", 2));
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int DEFAULT_MAX_STATE_ONLY_SIZE = 5;
/*     */ 
/*     */ 
/*     */   
/*     */   private final Http2Connection.PropertyKey stateKey;
/*     */ 
/*     */   
/*     */   private final IntObjectMap<State> stateOnlyMap;
/*     */ 
/*     */   
/*     */   private final PriorityQueue<State> stateOnlyRemovalQueue;
/*     */ 
/*     */   
/*     */   private final Http2Connection connection;
/*     */ 
/*     */   
/*     */   private final State connectionState;
/*     */ 
/*     */   
/*  91 */   private int allocationQuantum = 1024;
/*     */   private final int maxStateOnlySize;
/*     */   
/*     */   public WeightedFairQueueByteDistributor(Http2Connection connection) {
/*  95 */     this(connection, 5);
/*     */   }
/*     */   
/*     */   public WeightedFairQueueByteDistributor(Http2Connection connection, int maxStateOnlySize) {
/*  99 */     if (maxStateOnlySize < 0)
/* 100 */       throw new IllegalArgumentException("maxStateOnlySize: " + maxStateOnlySize + " (expected: >0)"); 
/* 101 */     if (maxStateOnlySize == 0) {
/* 102 */       this.stateOnlyMap = IntCollections.emptyMap();
/* 103 */       this.stateOnlyRemovalQueue = (PriorityQueue<State>)EmptyPriorityQueue.instance();
/*     */     } else {
/* 105 */       this.stateOnlyMap = (IntObjectMap<State>)new IntObjectHashMap(maxStateOnlySize);
/*     */ 
/*     */       
/* 108 */       this.stateOnlyRemovalQueue = (PriorityQueue<State>)new DefaultPriorityQueue(StateOnlyComparator.INSTANCE, maxStateOnlySize + 2);
/*     */     } 
/* 110 */     this.maxStateOnlySize = maxStateOnlySize;
/*     */     
/* 112 */     this.connection = connection;
/* 113 */     this.stateKey = connection.newKey();
/* 114 */     Http2Stream connectionStream = connection.connectionStream();
/* 115 */     connectionStream.setProperty(this.stateKey, this.connectionState = new State(connectionStream, 16));
/*     */ 
/*     */     
/* 118 */     connection.addListener(new Http2ConnectionAdapter()
/*     */         {
/*     */           public void onStreamAdded(Http2Stream stream) {
/* 121 */             WeightedFairQueueByteDistributor.State state = (WeightedFairQueueByteDistributor.State)WeightedFairQueueByteDistributor.this.stateOnlyMap.remove(stream.id());
/* 122 */             if (state == null) {
/* 123 */               state = new WeightedFairQueueByteDistributor.State(stream);
/*     */               
/* 125 */               List<WeightedFairQueueByteDistributor.ParentChangedEvent> events = new ArrayList<WeightedFairQueueByteDistributor.ParentChangedEvent>(1);
/* 126 */               WeightedFairQueueByteDistributor.this.connectionState.takeChild(state, false, events);
/* 127 */               WeightedFairQueueByteDistributor.this.notifyParentChanged(events);
/*     */             } else {
/* 129 */               WeightedFairQueueByteDistributor.this.stateOnlyRemovalQueue.removeTyped(state);
/* 130 */               state.stream = stream;
/*     */             } 
/* 132 */             switch (stream.state()) {
/*     */               case RESERVED_REMOTE:
/*     */               case RESERVED_LOCAL:
/* 135 */                 state.setStreamReservedOrActivated();
/*     */                 break;
/*     */             } 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 142 */             stream.setProperty(WeightedFairQueueByteDistributor.this.stateKey, state);
/*     */           }
/*     */ 
/*     */           
/*     */           public void onStreamActive(Http2Stream stream) {
/* 147 */             WeightedFairQueueByteDistributor.this.state(stream).setStreamReservedOrActivated();
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public void onStreamClosed(Http2Stream stream) {
/* 154 */             WeightedFairQueueByteDistributor.this.state(stream).close();
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public void onStreamRemoved(Http2Stream stream) {
/* 162 */             WeightedFairQueueByteDistributor.State state = WeightedFairQueueByteDistributor.this.state(stream);
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 167 */             state.stream = null;
/*     */             
/* 169 */             if (WeightedFairQueueByteDistributor.this.maxStateOnlySize == 0) {
/* 170 */               state.parent.removeChild(state);
/*     */               return;
/*     */             } 
/* 173 */             if (WeightedFairQueueByteDistributor.this.stateOnlyRemovalQueue.size() == WeightedFairQueueByteDistributor.this.maxStateOnlySize) {
/* 174 */               WeightedFairQueueByteDistributor.State stateToRemove = (WeightedFairQueueByteDistributor.State)WeightedFairQueueByteDistributor.this.stateOnlyRemovalQueue.peek();
/* 175 */               if (WeightedFairQueueByteDistributor.StateOnlyComparator.INSTANCE.compare(stateToRemove, state) >= 0) {
/*     */ 
/*     */                 
/* 178 */                 state.parent.removeChild(state);
/*     */                 return;
/*     */               } 
/* 181 */               WeightedFairQueueByteDistributor.this.stateOnlyRemovalQueue.poll();
/* 182 */               stateToRemove.parent.removeChild(stateToRemove);
/* 183 */               WeightedFairQueueByteDistributor.this.stateOnlyMap.remove(stateToRemove.streamId);
/*     */             } 
/* 185 */             WeightedFairQueueByteDistributor.this.stateOnlyRemovalQueue.add(state);
/* 186 */             WeightedFairQueueByteDistributor.this.stateOnlyMap.put(state.streamId, state);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateStreamableBytes(StreamByteDistributor.StreamState state) {
/* 193 */     state(state.stream()).updateStreamableBytes(Http2CodecUtil.streamableBytes(state), (state
/* 194 */         .hasFrame() && state.windowSize() >= 0));
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateDependencyTree(int childStreamId, int parentStreamId, short weight, boolean exclusive) {
/* 199 */     State state = state(childStreamId);
/* 200 */     if (state == null) {
/*     */ 
/*     */ 
/*     */       
/* 204 */       if (this.maxStateOnlySize == 0) {
/*     */         return;
/*     */       }
/* 207 */       state = new State(childStreamId);
/* 208 */       this.stateOnlyRemovalQueue.add(state);
/* 209 */       this.stateOnlyMap.put(childStreamId, state);
/*     */     } 
/*     */     
/* 212 */     State newParent = state(parentStreamId);
/* 213 */     if (newParent == null) {
/*     */ 
/*     */ 
/*     */       
/* 217 */       if (this.maxStateOnlySize == 0) {
/*     */         return;
/*     */       }
/* 220 */       newParent = new State(parentStreamId);
/* 221 */       this.stateOnlyRemovalQueue.add(newParent);
/* 222 */       this.stateOnlyMap.put(parentStreamId, newParent);
/*     */       
/* 224 */       List<ParentChangedEvent> events = new ArrayList<ParentChangedEvent>(1);
/* 225 */       this.connectionState.takeChild(newParent, false, events);
/* 226 */       notifyParentChanged(events);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 231 */     if (state.activeCountForTree != 0 && state.parent != null) {
/* 232 */       state.parent.totalQueuedWeights += (weight - state.weight);
/*     */     }
/* 234 */     state.weight = weight;
/*     */     
/* 236 */     if (newParent != state.parent || (exclusive && newParent.children.size() != 1)) {
/*     */       List<ParentChangedEvent> events;
/* 238 */       if (newParent.isDescendantOf(state)) {
/* 239 */         events = new ArrayList<ParentChangedEvent>(2 + (exclusive ? newParent.children.size() : 0));
/* 240 */         state.parent.takeChild(newParent, false, events);
/*     */       } else {
/* 242 */         events = new ArrayList<ParentChangedEvent>(1 + (exclusive ? newParent.children.size() : 0));
/*     */       } 
/* 244 */       newParent.takeChild(state, exclusive, events);
/* 245 */       notifyParentChanged(events);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 251 */     while (this.stateOnlyRemovalQueue.size() > this.maxStateOnlySize) {
/* 252 */       State stateToRemove = (State)this.stateOnlyRemovalQueue.poll();
/* 253 */       stateToRemove.parent.removeChild(stateToRemove);
/* 254 */       this.stateOnlyMap.remove(stateToRemove.streamId);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean distribute(int maxBytes, StreamByteDistributor.Writer writer) throws Http2Exception {
/*     */     int oldIsActiveCountForTree;
/* 261 */     if (this.connectionState.activeCountForTree == 0) {
/* 262 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     do {
/* 270 */       oldIsActiveCountForTree = this.connectionState.activeCountForTree;
/*     */       
/* 272 */       maxBytes -= distributeToChildren(maxBytes, writer, this.connectionState);
/* 273 */     } while (this.connectionState.activeCountForTree != 0 && (maxBytes > 0 || oldIsActiveCountForTree != this.connectionState.activeCountForTree));
/*     */ 
/*     */     
/* 276 */     return (this.connectionState.activeCountForTree != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void allocationQuantum(int allocationQuantum) {
/* 284 */     if (allocationQuantum <= 0) {
/* 285 */       throw new IllegalArgumentException("allocationQuantum must be > 0");
/*     */     }
/* 287 */     this.allocationQuantum = allocationQuantum;
/*     */   }
/*     */   
/*     */   private int distribute(int maxBytes, StreamByteDistributor.Writer writer, State state) throws Http2Exception {
/* 291 */     if (state.isActive()) {
/* 292 */       int nsent = Math.min(maxBytes, state.streamableBytes);
/* 293 */       state.write(nsent, writer);
/* 294 */       if (nsent == 0 && maxBytes != 0)
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 299 */         state.updateStreamableBytes(state.streamableBytes, false);
/*     */       }
/* 301 */       return nsent;
/*     */     } 
/*     */     
/* 304 */     return distributeToChildren(maxBytes, writer, state);
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
/*     */   private int distributeToChildren(int maxBytes, StreamByteDistributor.Writer writer, State state) throws Http2Exception {
/* 318 */     long oldTotalQueuedWeights = state.totalQueuedWeights;
/* 319 */     State childState = state.pollPseudoTimeQueue();
/* 320 */     State nextChildState = state.peekPseudoTimeQueue();
/* 321 */     childState.setDistributing();
/*     */     try {
/* 323 */       assert nextChildState == null || nextChildState.pseudoTimeToWrite >= childState.pseudoTimeToWrite : "nextChildState[" + nextChildState.streamId + "].pseudoTime(" + nextChildState.pseudoTimeToWrite + ") <  childState[" + childState.streamId + "].pseudoTime(" + childState.pseudoTimeToWrite + ")";
/*     */ 
/*     */       
/* 326 */       int nsent = distribute((nextChildState == null) ? maxBytes : 
/* 327 */           Math.min(maxBytes, (int)Math.min((nextChildState.pseudoTimeToWrite - childState.pseudoTimeToWrite) * childState.weight / oldTotalQueuedWeights + this.allocationQuantum, 2147483647L)), writer, childState);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 332 */       state.pseudoTime += nsent;
/* 333 */       childState.updatePseudoTime(state, nsent, oldTotalQueuedWeights);
/* 334 */       return nsent;
/*     */     } finally {
/* 336 */       childState.unsetDistributing();
/*     */ 
/*     */ 
/*     */       
/* 340 */       if (childState.activeCountForTree != 0) {
/* 341 */         state.offerPseudoTimeQueue(childState);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private State state(Http2Stream stream) {
/* 347 */     return stream.<State>getProperty(this.stateKey);
/*     */   }
/*     */   
/*     */   private State state(int streamId) {
/* 351 */     Http2Stream stream = this.connection.stream(streamId);
/* 352 */     return (stream != null) ? state(stream) : (State)this.stateOnlyMap.get(streamId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isChild(int childId, int parentId, short weight) {
/* 359 */     State parent = state(parentId);
/*     */     State child;
/* 361 */     return (parent.children.containsKey(childId) && 
/* 362 */       (child = state(childId)).parent == parent && child.weight == weight);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int numChildren(int streamId) {
/* 369 */     State state = state(streamId);
/* 370 */     return (state == null) ? 0 : state.children.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void notifyParentChanged(List<ParentChangedEvent> events) {
/* 378 */     for (int i = 0; i < events.size(); i++) {
/* 379 */       ParentChangedEvent event = events.get(i);
/* 380 */       this.stateOnlyRemovalQueue.priorityChanged(event.state);
/* 381 */       if (event.state.parent != null && event.state.activeCountForTree != 0) {
/* 382 */         event.state.parent.offerAndInitializePseudoTime(event.state);
/* 383 */         event.state.parent.activeCountChangeForTree(event.state.activeCountForTree);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class StateOnlyComparator
/*     */     implements Comparator<State>, Serializable
/*     */   {
/*     */     private static final long serialVersionUID = -4806936913002105966L;
/*     */ 
/*     */ 
/*     */     
/* 399 */     static final StateOnlyComparator INSTANCE = new StateOnlyComparator();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int compare(WeightedFairQueueByteDistributor.State o1, WeightedFairQueueByteDistributor.State o2) {
/* 407 */       boolean o1Actived = o1.wasStreamReservedOrActivated();
/* 408 */       if (o1Actived != o2.wasStreamReservedOrActivated()) {
/* 409 */         return o1Actived ? -1 : 1;
/*     */       }
/*     */       
/* 412 */       int x = o2.dependencyTreeDepth - o1.dependencyTreeDepth;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 421 */       return (x != 0) ? x : (o1.streamId - o2.streamId);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class StatePseudoTimeComparator
/*     */     implements Comparator<State>, Serializable {
/*     */     private static final long serialVersionUID = -1437548640227161828L;
/* 428 */     static final StatePseudoTimeComparator INSTANCE = new StatePseudoTimeComparator();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int compare(WeightedFairQueueByteDistributor.State o1, WeightedFairQueueByteDistributor.State o2) {
/* 435 */       return MathUtil.compare(o1.pseudoTimeToWrite, o2.pseudoTimeToWrite);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private final class State
/*     */     implements PriorityQueueNode
/*     */   {
/*     */     private static final byte STATE_IS_ACTIVE = 1;
/*     */     
/*     */     private static final byte STATE_IS_DISTRIBUTING = 2;
/*     */     
/*     */     private static final byte STATE_STREAM_ACTIVATED = 4;
/*     */     
/*     */     Http2Stream stream;
/*     */     
/*     */     State parent;
/* 452 */     IntObjectMap<State> children = IntCollections.emptyMap();
/*     */     
/*     */     private final PriorityQueue<State> pseudoTimeQueue;
/*     */     
/*     */     final int streamId;
/*     */     
/*     */     int streamableBytes;
/*     */     int dependencyTreeDepth;
/*     */     int activeCountForTree;
/* 461 */     private int pseudoTimeQueueIndex = -1;
/* 462 */     private int stateOnlyQueueIndex = -1;
/*     */ 
/*     */     
/*     */     long pseudoTimeToWrite;
/*     */     
/*     */     long pseudoTime;
/*     */     
/*     */     long totalQueuedWeights;
/*     */     
/*     */     private byte flags;
/*     */     
/* 473 */     short weight = 16;
/*     */     
/*     */     State(int streamId) {
/* 476 */       this(streamId, null, 0);
/*     */     }
/*     */     
/*     */     State(Http2Stream stream) {
/* 480 */       this(stream, 0);
/*     */     }
/*     */     
/*     */     State(Http2Stream stream, int initialSize) {
/* 484 */       this(stream.id(), stream, initialSize);
/*     */     }
/*     */     
/*     */     State(int streamId, Http2Stream stream, int initialSize) {
/* 488 */       this.stream = stream;
/* 489 */       this.streamId = streamId;
/* 490 */       this.pseudoTimeQueue = (PriorityQueue<State>)new DefaultPriorityQueue(WeightedFairQueueByteDistributor.StatePseudoTimeComparator.INSTANCE, initialSize);
/*     */     }
/*     */     
/*     */     boolean isDescendantOf(State state) {
/* 494 */       State next = this.parent;
/* 495 */       while (next != null) {
/* 496 */         if (next == state) {
/* 497 */           return true;
/*     */         }
/* 499 */         next = next.parent;
/*     */       } 
/* 501 */       return false;
/*     */     }
/*     */     
/*     */     void takeChild(State child, boolean exclusive, List<WeightedFairQueueByteDistributor.ParentChangedEvent> events) {
/* 505 */       takeChild(null, child, exclusive, events);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void takeChild(Iterator<IntObjectMap.PrimitiveEntry<State>> childItr, State child, boolean exclusive, List<WeightedFairQueueByteDistributor.ParentChangedEvent> events) {
/* 514 */       State oldParent = child.parent;
/*     */       
/* 516 */       if (oldParent != this) {
/* 517 */         events.add(new WeightedFairQueueByteDistributor.ParentChangedEvent(child, oldParent));
/* 518 */         child.setParent(this);
/*     */ 
/*     */ 
/*     */         
/* 522 */         if (childItr != null) {
/* 523 */           childItr.remove();
/* 524 */         } else if (oldParent != null) {
/* 525 */           oldParent.children.remove(child.streamId);
/*     */         } 
/*     */ 
/*     */         
/* 529 */         initChildrenIfEmpty();
/*     */         
/* 531 */         State oldChild = (State)this.children.put(child.streamId, child);
/* 532 */         assert oldChild == null : "A stream with the same stream ID was already in the child map.";
/*     */       } 
/*     */       
/* 535 */       if (exclusive && !this.children.isEmpty()) {
/*     */ 
/*     */         
/* 538 */         Iterator<IntObjectMap.PrimitiveEntry<State>> itr = removeAllChildrenExcept(child).entries().iterator();
/* 539 */         while (itr.hasNext()) {
/* 540 */           child.takeChild(itr, (State)((IntObjectMap.PrimitiveEntry)itr.next()).value(), false, events);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void removeChild(State child) {
/* 549 */       if (this.children.remove(child.streamId) != null) {
/* 550 */         List<WeightedFairQueueByteDistributor.ParentChangedEvent> events = new ArrayList<WeightedFairQueueByteDistributor.ParentChangedEvent>(1 + child.children.size());
/* 551 */         events.add(new WeightedFairQueueByteDistributor.ParentChangedEvent(child, child.parent));
/* 552 */         child.setParent(null);
/*     */ 
/*     */         
/* 555 */         Iterator<IntObjectMap.PrimitiveEntry<State>> itr = child.children.entries().iterator();
/* 556 */         while (itr.hasNext()) {
/* 557 */           takeChild(itr, (State)((IntObjectMap.PrimitiveEntry)itr.next()).value(), false, events);
/*     */         }
/*     */         
/* 560 */         WeightedFairQueueByteDistributor.this.notifyParentChanged(events);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private IntObjectMap<State> removeAllChildrenExcept(State stateToRetain) {
/* 570 */       stateToRetain = (State)this.children.remove(stateToRetain.streamId);
/* 571 */       IntObjectMap<State> prevChildren = this.children;
/*     */ 
/*     */       
/* 574 */       initChildren();
/* 575 */       if (stateToRetain != null) {
/* 576 */         this.children.put(stateToRetain.streamId, stateToRetain);
/*     */       }
/* 578 */       return prevChildren;
/*     */     }
/*     */ 
/*     */     
/*     */     private void setParent(State newParent) {
/* 583 */       if (this.activeCountForTree != 0 && this.parent != null) {
/* 584 */         this.parent.removePseudoTimeQueue(this);
/* 585 */         this.parent.activeCountChangeForTree(-this.activeCountForTree);
/*     */       } 
/* 587 */       this.parent = newParent;
/*     */       
/* 589 */       this.dependencyTreeDepth = (newParent == null) ? Integer.MAX_VALUE : (newParent.dependencyTreeDepth + 1);
/*     */     }
/*     */     
/*     */     private void initChildrenIfEmpty() {
/* 593 */       if (this.children == IntCollections.emptyMap()) {
/* 594 */         initChildren();
/*     */       }
/*     */     }
/*     */     
/*     */     private void initChildren() {
/* 599 */       this.children = (IntObjectMap<State>)new IntObjectHashMap(WeightedFairQueueByteDistributor.INITIAL_CHILDREN_MAP_SIZE);
/*     */     }
/*     */     
/*     */     void write(int numBytes, StreamByteDistributor.Writer writer) throws Http2Exception {
/* 603 */       assert this.stream != null;
/*     */       try {
/* 605 */         writer.write(this.stream, numBytes);
/* 606 */       } catch (Throwable t) {
/* 607 */         throw Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, t, "byte distribution write error", new Object[0]);
/*     */       } 
/*     */     }
/*     */     
/*     */     void activeCountChangeForTree(int increment) {
/* 612 */       assert this.activeCountForTree + increment >= 0;
/* 613 */       this.activeCountForTree += increment;
/* 614 */       if (this.parent != null) {
/* 615 */         assert this.activeCountForTree != increment || this.pseudoTimeQueueIndex == -1 || this.parent.pseudoTimeQueue
/*     */           
/* 617 */           .containsTyped(this) : "State[" + this.streamId + "].activeCountForTree changed from 0 to " + increment + " is in a pseudoTimeQueue, but not in parent[ " + this.parent.streamId + "]'s pseudoTimeQueue";
/*     */ 
/*     */         
/* 620 */         if (this.activeCountForTree == 0) {
/* 621 */           this.parent.removePseudoTimeQueue(this);
/* 622 */         } else if (this.activeCountForTree == increment && !isDistributing()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 631 */           this.parent.offerAndInitializePseudoTime(this);
/*     */         } 
/* 633 */         this.parent.activeCountChangeForTree(increment);
/*     */       } 
/*     */     }
/*     */     
/*     */     void updateStreamableBytes(int newStreamableBytes, boolean isActive) {
/* 638 */       if (isActive() != isActive) {
/* 639 */         if (isActive) {
/* 640 */           activeCountChangeForTree(1);
/* 641 */           setActive();
/*     */         } else {
/* 643 */           activeCountChangeForTree(-1);
/* 644 */           unsetActive();
/*     */         } 
/*     */       }
/*     */       
/* 648 */       this.streamableBytes = newStreamableBytes;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void updatePseudoTime(State parentState, int nsent, long totalQueuedWeights) {
/* 655 */       assert this.streamId != 0 && nsent >= 0;
/*     */ 
/*     */       
/* 658 */       this.pseudoTimeToWrite = Math.min(this.pseudoTimeToWrite, parentState.pseudoTime) + nsent * totalQueuedWeights / this.weight;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void offerAndInitializePseudoTime(State state) {
/* 667 */       state.pseudoTimeToWrite = this.pseudoTime;
/* 668 */       offerPseudoTimeQueue(state);
/*     */     }
/*     */     
/*     */     void offerPseudoTimeQueue(State state) {
/* 672 */       this.pseudoTimeQueue.offer(state);
/* 673 */       this.totalQueuedWeights += state.weight;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     State pollPseudoTimeQueue() {
/* 680 */       State state = (State)this.pseudoTimeQueue.poll();
/*     */       
/* 682 */       this.totalQueuedWeights -= state.weight;
/* 683 */       return state;
/*     */     }
/*     */     
/*     */     void removePseudoTimeQueue(State state) {
/* 687 */       if (this.pseudoTimeQueue.removeTyped(state)) {
/* 688 */         this.totalQueuedWeights -= state.weight;
/*     */       }
/*     */     }
/*     */     
/*     */     State peekPseudoTimeQueue() {
/* 693 */       return (State)this.pseudoTimeQueue.peek();
/*     */     }
/*     */     
/*     */     void close() {
/* 697 */       updateStreamableBytes(0, false);
/* 698 */       this.stream = null;
/*     */     }
/*     */     
/*     */     boolean wasStreamReservedOrActivated() {
/* 702 */       return ((this.flags & 0x4) != 0);
/*     */     }
/*     */     
/*     */     void setStreamReservedOrActivated() {
/* 706 */       this.flags = (byte)(this.flags | 0x4);
/*     */     }
/*     */     
/*     */     boolean isActive() {
/* 710 */       return ((this.flags & 0x1) != 0);
/*     */     }
/*     */     
/*     */     private void setActive() {
/* 714 */       this.flags = (byte)(this.flags | 0x1);
/*     */     }
/*     */     
/*     */     private void unsetActive() {
/* 718 */       this.flags = (byte)(this.flags & 0xFFFFFFFE);
/*     */     }
/*     */     
/*     */     boolean isDistributing() {
/* 722 */       return ((this.flags & 0x2) != 0);
/*     */     }
/*     */     
/*     */     void setDistributing() {
/* 726 */       this.flags = (byte)(this.flags | 0x2);
/*     */     }
/*     */     
/*     */     void unsetDistributing() {
/* 730 */       this.flags = (byte)(this.flags & 0xFFFFFFFD);
/*     */     }
/*     */ 
/*     */     
/*     */     public int priorityQueueIndex(DefaultPriorityQueue<?> queue) {
/* 735 */       return (queue == WeightedFairQueueByteDistributor.this.stateOnlyRemovalQueue) ? this.stateOnlyQueueIndex : this.pseudoTimeQueueIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     public void priorityQueueIndex(DefaultPriorityQueue<?> queue, int i) {
/* 740 */       if (queue == WeightedFairQueueByteDistributor.this.stateOnlyRemovalQueue) {
/* 741 */         this.stateOnlyQueueIndex = i;
/*     */       } else {
/* 743 */         this.pseudoTimeQueueIndex = i;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 750 */       StringBuilder sb = new StringBuilder(256 * ((this.activeCountForTree > 0) ? this.activeCountForTree : 1));
/* 751 */       toString(sb);
/* 752 */       return sb.toString();
/*     */     }
/*     */     
/*     */     private void toString(StringBuilder sb) {
/* 756 */       sb.append("{streamId ").append(this.streamId)
/* 757 */         .append(" streamableBytes ").append(this.streamableBytes)
/* 758 */         .append(" activeCountForTree ").append(this.activeCountForTree)
/* 759 */         .append(" pseudoTimeQueueIndex ").append(this.pseudoTimeQueueIndex)
/* 760 */         .append(" pseudoTimeToWrite ").append(this.pseudoTimeToWrite)
/* 761 */         .append(" pseudoTime ").append(this.pseudoTime)
/* 762 */         .append(" flags ").append(this.flags)
/* 763 */         .append(" pseudoTimeQueue.size() ").append(this.pseudoTimeQueue.size())
/* 764 */         .append(" stateOnlyQueueIndex ").append(this.stateOnlyQueueIndex)
/* 765 */         .append(" parent.streamId ").append((this.parent == null) ? -1 : this.parent.streamId).append("} [");
/*     */       
/* 767 */       if (!this.pseudoTimeQueue.isEmpty()) {
/* 768 */         for (State s : this.pseudoTimeQueue) {
/* 769 */           s.toString(sb);
/* 770 */           sb.append(", ");
/*     */         } 
/*     */         
/* 773 */         sb.setLength(sb.length() - 2);
/*     */       } 
/* 775 */       sb.append(']');
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class ParentChangedEvent
/*     */   {
/*     */     final WeightedFairQueueByteDistributor.State state;
/*     */ 
/*     */     
/*     */     final WeightedFairQueueByteDistributor.State oldParent;
/*     */ 
/*     */ 
/*     */     
/*     */     ParentChangedEvent(WeightedFairQueueByteDistributor.State state, WeightedFairQueueByteDistributor.State oldParent) {
/* 792 */       this.state = state;
/* 793 */       this.oldParent = oldParent;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\WeightedFairQueueByteDistributor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */