/*     */ package com.google.common.net;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Strings;
/*     */ import java.io.Serializable;
/*     */ import javax.annotation.Nullable;
/*     */ import javax.annotation.concurrent.Immutable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ @Immutable
/*     */ @GwtCompatible
/*     */ public final class HostAndPort
/*     */   implements Serializable
/*     */ {
/*     */   private static final int NO_PORT = -1;
/*     */   private final String host;
/*     */   private final int port;
/*     */   private final boolean hasBracketlessColons;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   private HostAndPort(String host, int port, boolean hasBracketlessColons) {
/*  78 */     this.host = host;
/*  79 */     this.port = port;
/*  80 */     this.hasBracketlessColons = hasBracketlessColons;
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
/*     */   public String getHost() {
/*  93 */     return this.host;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public String getHostText() {
/* 103 */     return this.host;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasPort() {
/* 108 */     return (this.port >= 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPort() {
/* 119 */     Preconditions.checkState(hasPort());
/* 120 */     return this.port;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPortOrDefault(int defaultPort) {
/* 127 */     return hasPort() ? this.port : defaultPort;
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
/*     */   
/*     */   public static HostAndPort fromParts(String host, int port) {
/* 143 */     Preconditions.checkArgument(isValidPort(port), "Port out of range: %s", port);
/* 144 */     HostAndPort parsedHost = fromString(host);
/* 145 */     Preconditions.checkArgument(!parsedHost.hasPort(), "Host has a port: %s", host);
/* 146 */     return new HostAndPort(parsedHost.host, port, parsedHost.hasBracketlessColons);
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
/*     */   public static HostAndPort fromHost(String host) {
/* 161 */     HostAndPort parsedHost = fromString(host);
/* 162 */     Preconditions.checkArgument(!parsedHost.hasPort(), "Host has a port: %s", host);
/* 163 */     return parsedHost;
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
/*     */   public static HostAndPort fromString(String hostPortString) {
/*     */     String host;
/* 177 */     Preconditions.checkNotNull(hostPortString);
/*     */     
/* 179 */     String portString = null;
/* 180 */     boolean hasBracketlessColons = false;
/*     */     
/* 182 */     if (hostPortString.startsWith("[")) {
/* 183 */       String[] hostAndPort = getHostAndPortFromBracketedHost(hostPortString);
/* 184 */       host = hostAndPort[0];
/* 185 */       portString = hostAndPort[1];
/*     */     } else {
/* 187 */       int colonPos = hostPortString.indexOf(':');
/* 188 */       if (colonPos >= 0 && hostPortString.indexOf(':', colonPos + 1) == -1) {
/*     */         
/* 190 */         host = hostPortString.substring(0, colonPos);
/* 191 */         portString = hostPortString.substring(colonPos + 1);
/*     */       } else {
/*     */         
/* 194 */         host = hostPortString;
/* 195 */         hasBracketlessColons = (colonPos >= 0);
/*     */       } 
/*     */     } 
/*     */     
/* 199 */     int port = -1;
/* 200 */     if (!Strings.isNullOrEmpty(portString)) {
/*     */ 
/*     */       
/* 203 */       Preconditions.checkArgument(!portString.startsWith("+"), "Unparseable port number: %s", hostPortString);
/*     */       try {
/* 205 */         port = Integer.parseInt(portString);
/* 206 */       } catch (NumberFormatException e) {
/* 207 */         throw new IllegalArgumentException("Unparseable port number: " + hostPortString);
/*     */       } 
/* 209 */       Preconditions.checkArgument(isValidPort(port), "Port number out of range: %s", hostPortString);
/*     */     } 
/*     */     
/* 212 */     return new HostAndPort(host, port, hasBracketlessColons);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String[] getHostAndPortFromBracketedHost(String hostPortString) {
/* 223 */     int colonIndex = 0;
/* 224 */     int closeBracketIndex = 0;
/* 225 */     Preconditions.checkArgument(
/* 226 */         (hostPortString.charAt(0) == '['), "Bracketed host-port string must start with a bracket: %s", hostPortString);
/*     */ 
/*     */     
/* 229 */     colonIndex = hostPortString.indexOf(':');
/* 230 */     closeBracketIndex = hostPortString.lastIndexOf(']');
/* 231 */     Preconditions.checkArgument((colonIndex > -1 && closeBracketIndex > colonIndex), "Invalid bracketed host/port: %s", hostPortString);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 236 */     String host = hostPortString.substring(1, closeBracketIndex);
/* 237 */     if (closeBracketIndex + 1 == hostPortString.length()) {
/* 238 */       return new String[] { host, "" };
/*     */     }
/* 240 */     Preconditions.checkArgument(
/* 241 */         (hostPortString.charAt(closeBracketIndex + 1) == ':'), "Only a colon may follow a close bracket: %s", hostPortString);
/*     */ 
/*     */     
/* 244 */     for (int i = closeBracketIndex + 2; i < hostPortString.length(); i++) {
/* 245 */       Preconditions.checkArgument(
/* 246 */           Character.isDigit(hostPortString.charAt(i)), "Port must be numeric: %s", hostPortString);
/*     */     }
/*     */ 
/*     */     
/* 250 */     return new String[] { host, hostPortString.substring(closeBracketIndex + 2) };
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
/*     */   public HostAndPort withDefaultPort(int defaultPort) {
/* 264 */     Preconditions.checkArgument(isValidPort(defaultPort));
/* 265 */     if (hasPort() || this.port == defaultPort) {
/* 266 */       return this;
/*     */     }
/* 268 */     return new HostAndPort(this.host, defaultPort, this.hasBracketlessColons);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public HostAndPort requireBracketsForIPv6() {
/* 286 */     Preconditions.checkArgument(!this.hasBracketlessColons, "Possible bracketless IPv6 literal: %s", this.host);
/* 287 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/* 292 */     if (this == other) {
/* 293 */       return true;
/*     */     }
/* 295 */     if (other instanceof HostAndPort) {
/* 296 */       HostAndPort that = (HostAndPort)other;
/* 297 */       return (Objects.equal(this.host, that.host) && this.port == that.port && this.hasBracketlessColons == that.hasBracketlessColons);
/*     */     } 
/*     */ 
/*     */     
/* 301 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 306 */     return Objects.hashCode(new Object[] { this.host, Integer.valueOf(this.port), Boolean.valueOf(this.hasBracketlessColons) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 313 */     StringBuilder builder = new StringBuilder(this.host.length() + 8);
/* 314 */     if (this.host.indexOf(':') >= 0) {
/* 315 */       builder.append('[').append(this.host).append(']');
/*     */     } else {
/* 317 */       builder.append(this.host);
/*     */     } 
/* 319 */     if (hasPort()) {
/* 320 */       builder.append(':').append(this.port);
/*     */     }
/* 322 */     return builder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isValidPort(int port) {
/* 327 */     return (port >= 0 && port <= 65535);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\net\HostAndPort.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */