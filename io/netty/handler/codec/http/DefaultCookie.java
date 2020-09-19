/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import io.netty.handler.codec.http.cookie.DefaultCookie;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
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
/*     */ @Deprecated
/*     */ public class DefaultCookie
/*     */   extends DefaultCookie
/*     */   implements Cookie
/*     */ {
/*     */   private String comment;
/*     */   private String commentUrl;
/*     */   private boolean discard;
/*  33 */   private Set<Integer> ports = Collections.emptySet();
/*  34 */   private Set<Integer> unmodifiablePorts = this.ports;
/*     */ 
/*     */   
/*     */   private int version;
/*     */ 
/*     */   
/*     */   public DefaultCookie(String name, String value) {
/*  41 */     super(name, value);
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public String getName() {
/*  47 */     return name();
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public String getValue() {
/*  53 */     return value();
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public String getDomain() {
/*  59 */     return domain();
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public String getPath() {
/*  65 */     return path();
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public String getComment() {
/*  71 */     return comment();
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public String comment() {
/*  77 */     return this.comment;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setComment(String comment) {
/*  83 */     this.comment = validateValue("comment", comment);
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public String getCommentUrl() {
/*  89 */     return commentUrl();
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public String commentUrl() {
/*  95 */     return this.commentUrl;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setCommentUrl(String commentUrl) {
/* 101 */     this.commentUrl = validateValue("commentUrl", commentUrl);
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public boolean isDiscard() {
/* 107 */     return this.discard;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setDiscard(boolean discard) {
/* 113 */     this.discard = discard;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Set<Integer> getPorts() {
/* 119 */     return ports();
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Set<Integer> ports() {
/* 125 */     if (this.unmodifiablePorts == null) {
/* 126 */       this.unmodifiablePorts = Collections.unmodifiableSet(this.ports);
/*     */     }
/* 128 */     return this.unmodifiablePorts;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setPorts(int... ports) {
/* 134 */     if (ports == null) {
/* 135 */       throw new NullPointerException("ports");
/*     */     }
/*     */     
/* 138 */     int[] portsCopy = (int[])ports.clone();
/* 139 */     if (portsCopy.length == 0) {
/* 140 */       this.unmodifiablePorts = this.ports = Collections.emptySet();
/*     */     } else {
/* 142 */       Set<Integer> newPorts = new TreeSet<Integer>();
/* 143 */       for (int p : portsCopy) {
/* 144 */         if (p <= 0 || p > 65535) {
/* 145 */           throw new IllegalArgumentException("port out of range: " + p);
/*     */         }
/* 147 */         newPorts.add(Integer.valueOf(p));
/*     */       } 
/* 149 */       this.ports = newPorts;
/* 150 */       this.unmodifiablePorts = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setPorts(Iterable<Integer> ports) {
/* 157 */     Set<Integer> newPorts = new TreeSet<Integer>();
/* 158 */     for (Iterator<Integer> iterator = ports.iterator(); iterator.hasNext(); ) { int p = ((Integer)iterator.next()).intValue();
/* 159 */       if (p <= 0 || p > 65535) {
/* 160 */         throw new IllegalArgumentException("port out of range: " + p);
/*     */       }
/* 162 */       newPorts.add(Integer.valueOf(p)); }
/*     */     
/* 164 */     if (newPorts.isEmpty()) {
/* 165 */       this.unmodifiablePorts = this.ports = Collections.emptySet();
/*     */     } else {
/* 167 */       this.ports = newPorts;
/* 168 */       this.unmodifiablePorts = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public long getMaxAge() {
/* 175 */     return maxAge();
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public int getVersion() {
/* 181 */     return version();
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public int version() {
/* 187 */     return this.version;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setVersion(int version) {
/* 193 */     this.version = version;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\DefaultCookie.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */