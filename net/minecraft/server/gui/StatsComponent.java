/*    */ package net.minecraft.server.gui;
/*    */ import java.awt.Color;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.text.DecimalFormat;
/*    */ import java.text.DecimalFormatSymbols;
/*    */ import java.util.Locale;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.Timer;
/*    */ import net.minecraft.Util;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ 
/*    */ public class StatsComponent extends JComponent {
/*    */   static {
/* 16 */     DECIMAL_FORMAT = (DecimalFormat)Util.make(new DecimalFormat("########0.000"), debug0 -> debug0.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT)));
/*    */   }
/* 18 */   private final int[] values = new int[256]; private static final DecimalFormat DECIMAL_FORMAT;
/*    */   private int vp;
/* 20 */   private final String[] msgs = new String[11];
/*    */   private final MinecraftServer server;
/*    */   private final Timer timer;
/*    */   
/*    */   public StatsComponent(MinecraftServer debug1) {
/* 25 */     this.server = debug1;
/* 26 */     setPreferredSize(new Dimension(456, 246));
/* 27 */     setMinimumSize(new Dimension(456, 246));
/* 28 */     setMaximumSize(new Dimension(456, 246));
/* 29 */     this.timer = new Timer(500, debug1 -> tick());
/* 30 */     this.timer.start();
/* 31 */     setBackground(Color.BLACK);
/*    */   }
/*    */   
/*    */   private void tick() {
/* 35 */     long debug1 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
/* 36 */     this.msgs[0] = "Memory use: " + (debug1 / 1024L / 1024L) + " mb (" + (Runtime.getRuntime().freeMemory() * 100L / Runtime.getRuntime().maxMemory()) + "% free)";
/* 37 */     this.msgs[1] = "Avg tick: " + DECIMAL_FORMAT.format(getAverage(this.server.tickTimes) * 1.0E-6D) + " ms";
/* 38 */     this.values[this.vp++ & 0xFF] = (int)(debug1 * 100L / Runtime.getRuntime().maxMemory());
/* 39 */     repaint();
/*    */   }
/*    */   
/*    */   private double getAverage(long[] debug1) {
/* 43 */     long debug2 = 0L;
/* 44 */     for (long debug7 : debug1) {
/* 45 */       debug2 += debug7;
/*    */     }
/* 47 */     return debug2 / debug1.length;
/*    */   }
/*    */ 
/*    */   
/*    */   public void paint(Graphics debug1) {
/* 52 */     debug1.setColor(new Color(16777215));
/* 53 */     debug1.fillRect(0, 0, 456, 246);
/*    */     int debug2;
/* 55 */     for (debug2 = 0; debug2 < 256; debug2++) {
/* 56 */       int debug3 = this.values[debug2 + this.vp & 0xFF];
/* 57 */       debug1.setColor(new Color(debug3 + 28 << 16));
/* 58 */       debug1.fillRect(debug2, 100 - debug3, 1, debug3);
/*    */     } 
/* 60 */     debug1.setColor(Color.BLACK);
/* 61 */     for (debug2 = 0; debug2 < this.msgs.length; debug2++) {
/* 62 */       String debug3 = this.msgs[debug2];
/* 63 */       if (debug3 != null) {
/* 64 */         debug1.drawString(debug3, 32, 116 + debug2 * 16);
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   public void close() {
/* 70 */     this.timer.stop();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\gui\StatsComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */