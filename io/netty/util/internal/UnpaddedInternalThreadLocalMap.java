/*    */ package io.netty.util.internal;
/*    */ 
/*    */ import java.nio.charset.Charset;
/*    */ import java.nio.charset.CharsetDecoder;
/*    */ import java.nio.charset.CharsetEncoder;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.atomic.AtomicInteger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class UnpaddedInternalThreadLocalMap
/*    */ {
/* 35 */   static final ThreadLocal<InternalThreadLocalMap> slowThreadLocalMap = new ThreadLocal<InternalThreadLocalMap>();
/* 36 */   static final AtomicInteger nextIndex = new AtomicInteger();
/*    */   
/*    */   Object[] indexedVariables;
/*    */   
/*    */   int futureListenerStackDepth;
/*    */   
/*    */   int localChannelReaderStackDepth;
/*    */   
/*    */   Map<Class<?>, Boolean> handlerSharableCache;
/*    */   
/*    */   IntegerHolder counterHashCode;
/*    */   
/*    */   ThreadLocalRandom random;
/*    */   
/*    */   Map<Class<?>, TypeParameterMatcher> typeParameterMatcherGetCache;
/*    */   
/*    */   Map<Class<?>, Map<String, TypeParameterMatcher>> typeParameterMatcherFindCache;
/*    */   StringBuilder stringBuilder;
/*    */   Map<Charset, CharsetEncoder> charsetEncoderCache;
/*    */   Map<Charset, CharsetDecoder> charsetDecoderCache;
/*    */   ArrayList<Object> arrayList;
/*    */   
/*    */   UnpaddedInternalThreadLocalMap(Object[] indexedVariables) {
/* 59 */     this.indexedVariables = indexedVariables;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\UnpaddedInternalThreadLocalMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */