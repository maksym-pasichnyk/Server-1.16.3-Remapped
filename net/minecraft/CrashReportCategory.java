/*     */ package net.minecraft;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ 
/*     */ public class CrashReportCategory
/*     */ {
/*     */   private final CrashReport report;
/*     */   private final String title;
/*  14 */   private final List<Entry> entries = Lists.newArrayList();
/*  15 */   private StackTraceElement[] stackTrace = new StackTraceElement[0];
/*     */   
/*     */   public CrashReportCategory(CrashReport debug1, String debug2) {
/*  18 */     this.report = debug1;
/*  19 */     this.title = debug2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String formatLocation(BlockPos debug0) {
/*  27 */     return formatLocation(debug0.getX(), debug0.getY(), debug0.getZ());
/*     */   }
/*     */   
/*     */   public static String formatLocation(int debug0, int debug1, int debug2) {
/*  31 */     StringBuilder debug3 = new StringBuilder();
/*     */     
/*     */     try {
/*  34 */       debug3.append(String.format("World: (%d,%d,%d)", new Object[] { Integer.valueOf(debug0), Integer.valueOf(debug1), Integer.valueOf(debug2) }));
/*  35 */     } catch (Throwable debug4) {
/*  36 */       debug3.append("(Error finding world loc)");
/*     */     } 
/*     */     
/*  39 */     debug3.append(", ");
/*     */     
/*     */     try {
/*  42 */       int debug4 = debug0 >> 4;
/*  43 */       int debug5 = debug2 >> 4;
/*  44 */       int debug6 = debug0 & 0xF;
/*  45 */       int debug7 = debug1 >> 4;
/*  46 */       int debug8 = debug2 & 0xF;
/*  47 */       int debug9 = debug4 << 4;
/*  48 */       int debug10 = debug5 << 4;
/*  49 */       int debug11 = (debug4 + 1 << 4) - 1;
/*  50 */       int debug12 = (debug5 + 1 << 4) - 1;
/*  51 */       debug3.append(String.format("Chunk: (at %d,%d,%d in %d,%d; contains blocks %d,0,%d to %d,255,%d)", new Object[] { Integer.valueOf(debug6), Integer.valueOf(debug7), Integer.valueOf(debug8), Integer.valueOf(debug4), Integer.valueOf(debug5), Integer.valueOf(debug9), Integer.valueOf(debug10), Integer.valueOf(debug11), Integer.valueOf(debug12) }));
/*  52 */     } catch (Throwable debug4) {
/*  53 */       debug3.append("(Error finding chunk loc)");
/*     */     } 
/*     */     
/*  56 */     debug3.append(", ");
/*     */     
/*     */     try {
/*  59 */       int debug4 = debug0 >> 9;
/*  60 */       int debug5 = debug2 >> 9;
/*  61 */       int debug6 = debug4 << 5;
/*  62 */       int debug7 = debug5 << 5;
/*  63 */       int debug8 = (debug4 + 1 << 5) - 1;
/*  64 */       int debug9 = (debug5 + 1 << 5) - 1;
/*  65 */       int debug10 = debug4 << 9;
/*  66 */       int debug11 = debug5 << 9;
/*  67 */       int debug12 = (debug4 + 1 << 9) - 1;
/*  68 */       int debug13 = (debug5 + 1 << 9) - 1;
/*  69 */       debug3.append(String.format("Region: (%d,%d; contains chunks %d,%d to %d,%d, blocks %d,0,%d to %d,255,%d)", new Object[] { Integer.valueOf(debug4), Integer.valueOf(debug5), Integer.valueOf(debug6), Integer.valueOf(debug7), Integer.valueOf(debug8), Integer.valueOf(debug9), Integer.valueOf(debug10), Integer.valueOf(debug11), Integer.valueOf(debug12), Integer.valueOf(debug13) }));
/*  70 */     } catch (Throwable debug4) {
/*  71 */       debug3.append("(Error finding world loc)");
/*     */     } 
/*     */     
/*  74 */     return debug3.toString();
/*     */   }
/*     */   
/*     */   public CrashReportCategory setDetail(String debug1, CrashReportDetail<String> debug2) {
/*     */     try {
/*  79 */       setDetail(debug1, debug2.call());
/*  80 */     } catch (Throwable debug3) {
/*  81 */       setDetailError(debug1, debug3);
/*     */     } 
/*  83 */     return this;
/*     */   }
/*     */   
/*     */   public CrashReportCategory setDetail(String debug1, Object debug2) {
/*  87 */     this.entries.add(new Entry(debug1, debug2));
/*  88 */     return this;
/*     */   }
/*     */   
/*     */   public void setDetailError(String debug1, Throwable debug2) {
/*  92 */     setDetail(debug1, debug2);
/*     */   }
/*     */   
/*     */   public int fillInStackTrace(int debug1) {
/*  96 */     StackTraceElement[] debug2 = Thread.currentThread().getStackTrace();
/*     */ 
/*     */     
/*  99 */     if (debug2.length <= 0) {
/* 100 */       return 0;
/*     */     }
/*     */     
/* 103 */     this.stackTrace = new StackTraceElement[debug2.length - 3 - debug1];
/* 104 */     System.arraycopy(debug2, 3 + debug1, this.stackTrace, 0, this.stackTrace.length);
/* 105 */     return this.stackTrace.length;
/*     */   }
/*     */   
/*     */   public boolean validateStackTrace(StackTraceElement debug1, StackTraceElement debug2) {
/* 109 */     if (this.stackTrace.length == 0 || debug1 == null) {
/* 110 */       return false;
/*     */     }
/*     */     
/* 113 */     StackTraceElement debug3 = this.stackTrace[0];
/*     */ 
/*     */     
/* 116 */     if (debug3.isNativeMethod() != debug1.isNativeMethod() || 
/* 117 */       !debug3.getClassName().equals(debug1.getClassName()) || 
/* 118 */       !debug3.getFileName().equals(debug1.getFileName()) || 
/* 119 */       !debug3.getMethodName().equals(debug1.getMethodName()))
/*     */     {
/* 121 */       return false;
/*     */     }
/*     */     
/* 124 */     if (((debug2 != null) ? true : false) != ((this.stackTrace.length > 1) ? true : false)) {
/* 125 */       return false;
/*     */     }
/* 127 */     if (debug2 != null && !this.stackTrace[1].equals(debug2)) {
/* 128 */       return false;
/*     */     }
/*     */     
/* 131 */     this.stackTrace[0] = debug1;
/*     */     
/* 133 */     return true;
/*     */   }
/*     */   
/*     */   public void trimStacktrace(int debug1) {
/* 137 */     StackTraceElement[] debug2 = new StackTraceElement[this.stackTrace.length - debug1];
/* 138 */     System.arraycopy(this.stackTrace, 0, debug2, 0, debug2.length);
/* 139 */     this.stackTrace = debug2;
/*     */   }
/*     */   
/*     */   public void getDetails(StringBuilder debug1) {
/* 143 */     debug1.append("-- ").append(this.title).append(" --\n");
/* 144 */     debug1.append("Details:");
/*     */     
/* 146 */     for (Entry debug3 : this.entries) {
/* 147 */       debug1.append("\n\t");
/* 148 */       debug1.append(debug3.getKey());
/* 149 */       debug1.append(": ");
/* 150 */       debug1.append(debug3.getValue());
/*     */     } 
/*     */     
/* 153 */     if (this.stackTrace != null && this.stackTrace.length > 0) {
/* 154 */       debug1.append("\nStacktrace:");
/*     */       
/* 156 */       for (StackTraceElement debug5 : this.stackTrace) {
/* 157 */         debug1.append("\n\tat ");
/* 158 */         debug1.append(debug5);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public StackTraceElement[] getStacktrace() {
/* 164 */     return this.stackTrace;
/*     */   }
/*     */   
/*     */   public static void populateBlockDetails(CrashReportCategory debug0, BlockPos debug1, @Nullable BlockState debug2) {
/* 168 */     if (debug2 != null) {
/* 169 */       debug0.setDetail("Block", debug2::toString);
/*     */     }
/*     */     
/* 172 */     debug0.setDetail("Block location", () -> formatLocation(debug0));
/*     */   }
/*     */   
/*     */   static class Entry {
/*     */     private final String key;
/*     */     private final String value;
/*     */     
/*     */     public Entry(String debug1, @Nullable Object debug2) {
/* 180 */       this.key = debug1;
/*     */       
/* 182 */       if (debug2 == null) {
/* 183 */         this.value = "~~NULL~~";
/* 184 */       } else if (debug2 instanceof Throwable) {
/* 185 */         Throwable debug3 = (Throwable)debug2;
/* 186 */         this.value = "~~ERROR~~ " + debug3.getClass().getSimpleName() + ": " + debug3.getMessage();
/*     */       } else {
/* 188 */         this.value = debug2.toString();
/*     */       } 
/*     */     }
/*     */     
/*     */     public String getKey() {
/* 193 */       return this.key;
/*     */     }
/*     */     
/*     */     public String getValue() {
/* 197 */       return this.value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\CrashReportCategory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */