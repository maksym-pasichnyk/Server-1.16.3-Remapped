/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.io.IOException;
/*    */ import java.io.Writer;
/*    */ import java.util.List;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ import javax.annotation.Nullable;
/*    */ import org.apache.commons.lang3.StringEscapeUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CsvOutput
/*    */ {
/*    */   private final Writer output;
/*    */   private final int columnCount;
/*    */   
/*    */   private CsvOutput(Writer debug1, List<String> debug2) throws IOException {
/* 20 */     this.output = debug1;
/* 21 */     this.columnCount = debug2.size();
/* 22 */     writeLine(debug2.stream());
/*    */   }
/*    */   
/*    */   public static Builder builder() {
/* 26 */     return new Builder();
/*    */   }
/*    */   
/*    */   public void writeRow(Object... debug1) throws IOException {
/* 30 */     if (debug1.length != this.columnCount) {
/* 31 */       throw new IllegalArgumentException("Invalid number of columns, expected " + this.columnCount + ", but got " + debug1.length);
/*    */     }
/*    */     
/* 34 */     writeLine(Stream.of(debug1));
/*    */   }
/*    */   
/*    */   private void writeLine(Stream<?> debug1) throws IOException {
/* 38 */     this.output.write((String)debug1.<CharSequence>map(CsvOutput::getStringValue).collect(Collectors.joining(",")) + "\r\n");
/*    */   }
/*    */   
/*    */   private static String getStringValue(@Nullable Object debug0) {
/* 42 */     return StringEscapeUtils.escapeCsv((debug0 != null) ? debug0.toString() : "[null]");
/*    */   }
/*    */   
/*    */   public static class Builder {
/* 46 */     private final List<String> headers = Lists.newArrayList();
/*    */     
/*    */     public Builder addColumn(String debug1) {
/* 49 */       this.headers.add(debug1);
/* 50 */       return this;
/*    */     }
/*    */     
/*    */     public CsvOutput build(Writer debug1) throws IOException {
/* 54 */       return new CsvOutput(debug1, this.headers);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\CsvOutput.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */