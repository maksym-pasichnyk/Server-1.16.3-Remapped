/*     */ package io.netty.handler.codec.memcache;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufHolder;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.handler.codec.DecoderResult;
/*     */ import io.netty.util.ReferenceCounted;
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
/*     */ public interface LastMemcacheContent
/*     */   extends MemcacheContent
/*     */ {
/*  33 */   public static final LastMemcacheContent EMPTY_LAST_CONTENT = new LastMemcacheContent()
/*     */     {
/*     */       public LastMemcacheContent copy()
/*     */       {
/*  37 */         return EMPTY_LAST_CONTENT;
/*     */       }
/*     */ 
/*     */       
/*     */       public LastMemcacheContent duplicate() {
/*  42 */         return this;
/*     */       }
/*     */ 
/*     */       
/*     */       public LastMemcacheContent retainedDuplicate() {
/*  47 */         return this;
/*     */       }
/*     */ 
/*     */       
/*     */       public LastMemcacheContent replace(ByteBuf content) {
/*  52 */         return new DefaultLastMemcacheContent(content);
/*     */       }
/*     */ 
/*     */       
/*     */       public LastMemcacheContent retain(int increment) {
/*  57 */         return this;
/*     */       }
/*     */ 
/*     */       
/*     */       public LastMemcacheContent retain() {
/*  62 */         return this;
/*     */       }
/*     */ 
/*     */       
/*     */       public LastMemcacheContent touch() {
/*  67 */         return this;
/*     */       }
/*     */ 
/*     */       
/*     */       public LastMemcacheContent touch(Object hint) {
/*  72 */         return this;
/*     */       }
/*     */ 
/*     */       
/*     */       public ByteBuf content() {
/*  77 */         return Unpooled.EMPTY_BUFFER;
/*     */       }
/*     */ 
/*     */       
/*     */       public DecoderResult decoderResult() {
/*  82 */         return DecoderResult.SUCCESS;
/*     */       }
/*     */ 
/*     */       
/*     */       public void setDecoderResult(DecoderResult result) {
/*  87 */         throw new UnsupportedOperationException("read only");
/*     */       }
/*     */ 
/*     */       
/*     */       public int refCnt() {
/*  92 */         return 1;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean release() {
/*  97 */         return false;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean release(int decrement) {
/* 102 */         return false;
/*     */       }
/*     */     };
/*     */   
/*     */   LastMemcacheContent copy();
/*     */   
/*     */   LastMemcacheContent duplicate();
/*     */   
/*     */   LastMemcacheContent retainedDuplicate();
/*     */   
/*     */   LastMemcacheContent replace(ByteBuf paramByteBuf);
/*     */   
/*     */   LastMemcacheContent retain(int paramInt);
/*     */   
/*     */   LastMemcacheContent retain();
/*     */   
/*     */   LastMemcacheContent touch();
/*     */   
/*     */   LastMemcacheContent touch(Object paramObject);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\memcache\LastMemcacheContent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */