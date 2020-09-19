/*     */ package net.minecraft.server.gui;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.util.QueueLogAppender;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.FocusAdapter;
/*     */ import java.awt.event.FocusEvent;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollBar;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTextArea;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.EtchedBorder;
/*     */ import javax.swing.border.TitledBorder;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Document;
/*     */ import net.minecraft.DefaultUncaughtExceptionHandler;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.dedicated.DedicatedServer;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class MinecraftServerGui extends JComponent {
/*  36 */   private static final Font MONOSPACED = new Font("Monospaced", 0, 12);
/*  37 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   private final DedicatedServer server;
/*     */   
/*     */   private Thread logAppenderThread;
/*     */   
/*  43 */   private final Collection<Runnable> finalizers = Lists.newArrayList();
/*  44 */   private final AtomicBoolean isClosing = new AtomicBoolean();
/*     */   
/*     */   public static MinecraftServerGui showFrameFor(final DedicatedServer server) {
/*     */     try {
/*  48 */       UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
/*  49 */     } catch (Exception exception) {}
/*     */ 
/*     */     
/*  52 */     final JFrame frame = new JFrame("Minecraft server");
/*  53 */     final MinecraftServerGui gui = new MinecraftServerGui(server);
/*  54 */     debug1.setDefaultCloseOperation(2);
/*  55 */     debug1.add(debug2);
/*  56 */     debug1.pack();
/*  57 */     debug1.setLocationRelativeTo((Component)null);
/*  58 */     debug1.setVisible(true);
/*  59 */     debug1.addWindowListener(new WindowAdapter()
/*     */         {
/*     */           public void windowClosing(WindowEvent debug1) {
/*  62 */             if (!gui.isClosing.getAndSet(true)) {
/*  63 */               frame.setTitle("Minecraft server - shutting down!");
/*  64 */               server.halt(true);
/*  65 */               gui.runFinalizers();
/*     */             } 
/*     */           }
/*     */         });
/*  69 */     debug2.addFinalizer(debug1::dispose);
/*  70 */     debug2.start();
/*  71 */     return debug2;
/*     */   }
/*     */   
/*     */   private MinecraftServerGui(DedicatedServer debug1) {
/*  75 */     this.server = debug1;
/*  76 */     setPreferredSize(new Dimension(854, 480));
/*     */     
/*  78 */     setLayout(new BorderLayout());
/*     */     try {
/*  80 */       add(buildChatPanel(), "Center");
/*  81 */       add(buildInfoPanel(), "West");
/*  82 */     } catch (Exception debug2) {
/*  83 */       LOGGER.error("Couldn't build server GUI", debug2);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addFinalizer(Runnable debug1) {
/*  88 */     this.finalizers.add(debug1);
/*     */   }
/*     */   
/*     */   private JComponent buildInfoPanel() {
/*  92 */     JPanel debug1 = new JPanel(new BorderLayout());
/*  93 */     StatsComponent debug2 = new StatsComponent((MinecraftServer)this.server);
/*  94 */     this.finalizers.add(debug2::close);
/*  95 */     debug1.add(debug2, "North");
/*  96 */     debug1.add(buildPlayerPanel(), "Center");
/*  97 */     debug1.setBorder(new TitledBorder(new EtchedBorder(), "Stats"));
/*  98 */     return debug1;
/*     */   }
/*     */   
/*     */   private JComponent buildPlayerPanel() {
/* 102 */     JList<?> debug1 = new PlayerListComponent((MinecraftServer)this.server);
/* 103 */     JScrollPane debug2 = new JScrollPane(debug1, 22, 30);
/* 104 */     debug2.setBorder(new TitledBorder(new EtchedBorder(), "Players"));
/*     */     
/* 106 */     return debug2;
/*     */   }
/*     */   
/*     */   private JComponent buildChatPanel() {
/* 110 */     JPanel debug1 = new JPanel(new BorderLayout());
/* 111 */     JTextArea debug2 = new JTextArea();
/* 112 */     JScrollPane debug3 = new JScrollPane(debug2, 22, 30);
/* 113 */     debug2.setEditable(false);
/* 114 */     debug2.setFont(MONOSPACED);
/*     */     
/* 116 */     JTextField debug4 = new JTextField();
/* 117 */     debug4.addActionListener(debug2 -> {
/*     */           String debug3 = debug1.getText().trim();
/*     */           
/*     */           if (!debug3.isEmpty()) {
/*     */             this.server.handleConsoleInput(debug3, this.server.createCommandSourceStack());
/*     */           }
/*     */           debug1.setText("");
/*     */         });
/* 125 */     debug2.addFocusListener(new FocusAdapter()
/*     */         {
/*     */           public void focusGained(FocusEvent debug1) {}
/*     */         });
/*     */ 
/*     */     
/* 131 */     debug1.add(debug3, "Center");
/* 132 */     debug1.add(debug4, "South");
/* 133 */     debug1.setBorder(new TitledBorder(new EtchedBorder(), "Log and chat"));
/*     */     
/* 135 */     this.logAppenderThread = new Thread(() -> {
/*     */           String debug3;
/*     */           while ((debug3 = QueueLogAppender.getNextLogEvent("ServerGuiConsole")) != null) {
/*     */             print(debug1, debug2, debug3);
/*     */           }
/*     */         });
/* 141 */     this.logAppenderThread.setUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new DefaultUncaughtExceptionHandler(LOGGER));
/* 142 */     this.logAppenderThread.setDaemon(true);
/* 143 */     return debug1;
/*     */   }
/*     */   
/*     */   public void start() {
/* 147 */     this.logAppenderThread.start();
/*     */   }
/*     */   
/*     */   public void close() {
/* 151 */     if (!this.isClosing.getAndSet(true)) {
/* 152 */       runFinalizers();
/*     */     }
/*     */   }
/*     */   
/*     */   private void runFinalizers() {
/* 157 */     this.finalizers.forEach(Runnable::run);
/*     */   }
/*     */   
/*     */   public void print(JTextArea debug1, JScrollPane debug2, String debug3) {
/* 161 */     if (!SwingUtilities.isEventDispatchThread()) {
/* 162 */       SwingUtilities.invokeLater(() -> print(debug1, debug2, debug3));
/*     */       
/*     */       return;
/*     */     } 
/* 166 */     Document debug4 = debug1.getDocument();
/* 167 */     JScrollBar debug5 = debug2.getVerticalScrollBar();
/* 168 */     boolean debug6 = false;
/*     */     
/* 170 */     if (debug2.getViewport().getView() == debug1) {
/* 171 */       debug6 = (debug5.getValue() + debug5.getSize().getHeight() + (MONOSPACED.getSize() * 4) > debug5.getMaximum());
/*     */     }
/*     */     
/*     */     try {
/* 175 */       debug4.insertString(debug4.getLength(), debug3, null);
/* 176 */     } catch (BadLocationException badLocationException) {}
/*     */ 
/*     */     
/* 179 */     if (debug6)
/* 180 */       debug5.setValue(2147483647); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\gui\MinecraftServerGui.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */