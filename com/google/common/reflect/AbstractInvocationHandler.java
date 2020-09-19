/*     */ package com.google.common.reflect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.Arrays;
/*     */ import javax.annotation.Nullable;
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
/*     */ @Beta
/*     */ public abstract class AbstractInvocationHandler
/*     */   implements InvocationHandler
/*     */ {
/*  42 */   private static final Object[] NO_ARGS = new Object[0];
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
/*     */   public final Object invoke(Object proxy, Method method, @Nullable Object[] args) throws Throwable {
/*  62 */     if (args == null) {
/*  63 */       args = NO_ARGS;
/*     */     }
/*  65 */     if (args.length == 0 && method.getName().equals("hashCode")) {
/*  66 */       return Integer.valueOf(hashCode());
/*     */     }
/*  68 */     if (args.length == 1 && method
/*  69 */       .getName().equals("equals") && method
/*  70 */       .getParameterTypes()[0] == Object.class) {
/*  71 */       Object arg = args[0];
/*  72 */       if (arg == null) {
/*  73 */         return Boolean.valueOf(false);
/*     */       }
/*  75 */       if (proxy == arg) {
/*  76 */         return Boolean.valueOf(true);
/*     */       }
/*  78 */       return Boolean.valueOf((isProxyOfSameInterfaces(arg, proxy.getClass()) && 
/*  79 */           equals(Proxy.getInvocationHandler(arg))));
/*     */     } 
/*  81 */     if (args.length == 0 && method.getName().equals("toString")) {
/*  82 */       return toString();
/*     */     }
/*  84 */     return handleInvocation(proxy, method, args);
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
/*     */   protected abstract Object handleInvocation(Object paramObject, Method paramMethod, Object[] paramArrayOfObject) throws Throwable;
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
/*     */   public boolean equals(Object obj) {
/* 111 */     return super.equals(obj);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 120 */     return super.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 130 */     return super.toString();
/*     */   }
/*     */   
/*     */   private static boolean isProxyOfSameInterfaces(Object arg, Class<?> proxyClass) {
/* 134 */     return (proxyClass.isInstance(arg) || (
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 140 */       Proxy.isProxyClass(arg.getClass()) && 
/* 141 */       Arrays.equals((Object[])arg.getClass().getInterfaces(), (Object[])proxyClass.getInterfaces())));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\reflect\AbstractInvocationHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */