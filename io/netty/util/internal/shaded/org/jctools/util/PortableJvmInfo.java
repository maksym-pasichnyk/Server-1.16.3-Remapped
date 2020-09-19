/*    */ package io.netty.util.internal.shaded.org.jctools.util;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface PortableJvmInfo
/*    */ {
/*  8 */   public static final int CACHE_LINE_SIZE = Integer.getInteger("jctools.cacheLineSize", 64).intValue();
/*  9 */   public static final int CPUs = Runtime.getRuntime().availableProcessors();
/* 10 */   public static final int RECOMENDED_OFFER_BATCH = CPUs * 4;
/* 11 */   public static final int RECOMENDED_POLL_BATCH = CPUs * 4;
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\shaded\org\jctool\\util\PortableJvmInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */