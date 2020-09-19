/*     */ package net.minecraft.server.network;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelInboundHandlerAdapter;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class LegacyQueryHandler extends ChannelInboundHandlerAdapter {
/*  16 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   private final ServerConnectionListener serverConnectionListener;
/*     */ 
/*     */   
/*     */   public LegacyQueryHandler(ServerConnectionListener debug1) {
/*  22 */     this.serverConnectionListener = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelRead(ChannelHandlerContext debug1, Object debug2) throws Exception {
/*  27 */     ByteBuf debug3 = (ByteBuf)debug2;
/*     */     
/*  29 */     debug3.markReaderIndex();
/*     */     
/*  31 */     boolean debug4 = true; 
/*     */     try { String str1; boolean debug8; int j; boolean bool1; int i, debug9; String debug10; ByteBuf debug11;
/*  33 */       if (debug3.readUnsignedByte() != 254) {
/*     */         return;
/*     */       }
/*     */       
/*  37 */       InetSocketAddress debug5 = (InetSocketAddress)debug1.channel().remoteAddress();
/*  38 */       MinecraftServer debug6 = this.serverConnectionListener.getServer();
/*     */       
/*  40 */       int debug7 = debug3.readableBytes();
/*  41 */       switch (debug7) {
/*     */         case 0:
/*  43 */           LOGGER.debug("Ping: (<1.3.x) from {}:{}", debug5.getAddress(), Integer.valueOf(debug5.getPort()));
/*     */           
/*  45 */           str1 = String.format("%s§%d§%d", new Object[] { debug6.getMotd(), Integer.valueOf(debug6.getPlayerCount()), Integer.valueOf(debug6.getMaxPlayers()) });
/*  46 */           sendFlushAndClose(debug1, createReply(str1));
/*     */           break;
/*     */ 
/*     */         
/*     */         case 1:
/*  51 */           if (debug3.readUnsignedByte() != 1) {
/*     */             return;
/*     */           }
/*     */           
/*  55 */           LOGGER.debug("Ping: (1.4-1.5.x) from {}:{}", debug5.getAddress(), Integer.valueOf(debug5.getPort()));
/*     */           
/*  57 */           str1 = String.format("§1\000%d\000%s\000%s\000%d\000%d", new Object[] { Integer.valueOf(127), debug6.getServerVersion(), debug6.getMotd(), Integer.valueOf(debug6.getPlayerCount()), Integer.valueOf(debug6.getMaxPlayers()) });
/*  58 */           sendFlushAndClose(debug1, createReply(str1));
/*     */           break;
/*     */ 
/*     */         
/*     */         default:
/*  63 */           debug8 = (debug3.readUnsignedByte() == 1);
/*  64 */           j = debug8 & ((debug3.readUnsignedByte() == 250) ? 1 : 0);
/*  65 */           bool1 = j & "MC|PingHost".equals(new String(debug3.readBytes(debug3.readShort() * 2).array(), StandardCharsets.UTF_16BE));
/*  66 */           debug9 = debug3.readUnsignedShort();
/*  67 */           i = bool1 & ((debug3.readUnsignedByte() >= 73) ? 1 : 0);
/*  68 */           i &= (3 + (debug3.readBytes(debug3.readShort() * 2).array()).length + 4 == debug9) ? 1 : 0;
/*  69 */           i &= (debug3.readInt() <= 65535) ? 1 : 0;
/*  70 */           i &= (debug3.readableBytes() == 0) ? 1 : 0;
/*     */           
/*  72 */           if (i == 0) {
/*     */             return;
/*     */           }
/*     */           
/*  76 */           LOGGER.debug("Ping: (1.6) from {}:{}", debug5.getAddress(), Integer.valueOf(debug5.getPort()));
/*     */           
/*  78 */           debug10 = String.format("§1\000%d\000%s\000%s\000%d\000%d", new Object[] { Integer.valueOf(127), debug6.getServerVersion(), debug6.getMotd(), Integer.valueOf(debug6.getPlayerCount()), Integer.valueOf(debug6.getMaxPlayers()) });
/*  79 */           debug11 = createReply(debug10);
/*     */           try {
/*  81 */             sendFlushAndClose(debug1, debug11);
/*     */           } finally {
/*  83 */             debug11.release();
/*     */           } 
/*     */           break;
/*     */       } 
/*  87 */       debug3.release();
/*  88 */       debug4 = false; }
/*  89 */     catch (RuntimeException runtimeException) {  }
/*     */     finally
/*  91 */     { if (debug4) {
/*  92 */         debug3.resetReaderIndex();
/*  93 */         debug1.channel().pipeline().remove("legacy_query");
/*  94 */         debug1.fireChannelRead(debug2);
/*     */       }  }
/*     */   
/*     */   }
/*     */   
/*     */   private void sendFlushAndClose(ChannelHandlerContext debug1, ByteBuf debug2) {
/* 100 */     debug1.pipeline().firstContext().writeAndFlush(debug2).addListener((GenericFutureListener)ChannelFutureListener.CLOSE);
/*     */   }
/*     */   
/*     */   private ByteBuf createReply(String debug1) {
/* 104 */     ByteBuf debug2 = Unpooled.buffer();
/* 105 */     debug2.writeByte(255);
/*     */     
/* 107 */     char[] debug3 = debug1.toCharArray();
/* 108 */     debug2.writeShort(debug3.length);
/* 109 */     for (char debug7 : debug3) {
/* 110 */       debug2.writeChar(debug7);
/*     */     }
/*     */     
/* 113 */     return debug2;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\network\LegacyQueryHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */