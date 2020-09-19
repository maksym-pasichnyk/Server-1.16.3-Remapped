/*     */ package org.apache.logging.log4j.core.impl;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.apache.logging.log4j.core.pattern.PlainTextRenderer;
/*     */ import org.apache.logging.log4j.core.pattern.TextRenderer;
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
/*     */ public final class ExtendedStackTraceElement
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -2171069569241280505L;
/*     */   private final ExtendedClassInfo extraClassInfo;
/*     */   private final StackTraceElement stackTraceElement;
/*     */   
/*     */   public ExtendedStackTraceElement(StackTraceElement stackTraceElement, ExtendedClassInfo extraClassInfo) {
/*  45 */     this.stackTraceElement = stackTraceElement;
/*  46 */     this.extraClassInfo = extraClassInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExtendedStackTraceElement(String declaringClass, String methodName, String fileName, int lineNumber, boolean exact, String location, String version) {
/*  54 */     this(new StackTraceElement(declaringClass, methodName, fileName, lineNumber), new ExtendedClassInfo(exact, location, version));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  60 */     if (this == obj) {
/*  61 */       return true;
/*     */     }
/*  63 */     if (obj == null) {
/*  64 */       return false;
/*     */     }
/*  66 */     if (!(obj instanceof ExtendedStackTraceElement)) {
/*  67 */       return false;
/*     */     }
/*  69 */     ExtendedStackTraceElement other = (ExtendedStackTraceElement)obj;
/*  70 */     if (this.extraClassInfo == null) {
/*  71 */       if (other.extraClassInfo != null) {
/*  72 */         return false;
/*     */       }
/*  74 */     } else if (!this.extraClassInfo.equals(other.extraClassInfo)) {
/*  75 */       return false;
/*     */     } 
/*  77 */     if (this.stackTraceElement == null) {
/*  78 */       if (other.stackTraceElement != null) {
/*  79 */         return false;
/*     */       }
/*  81 */     } else if (!this.stackTraceElement.equals(other.stackTraceElement)) {
/*  82 */       return false;
/*     */     } 
/*  84 */     return true;
/*     */   }
/*     */   
/*     */   public String getClassName() {
/*  88 */     return this.stackTraceElement.getClassName();
/*     */   }
/*     */   
/*     */   public boolean getExact() {
/*  92 */     return this.extraClassInfo.getExact();
/*     */   }
/*     */   
/*     */   public ExtendedClassInfo getExtraClassInfo() {
/*  96 */     return this.extraClassInfo;
/*     */   }
/*     */   
/*     */   public String getFileName() {
/* 100 */     return this.stackTraceElement.getFileName();
/*     */   }
/*     */   
/*     */   public int getLineNumber() {
/* 104 */     return this.stackTraceElement.getLineNumber();
/*     */   }
/*     */   
/*     */   public String getLocation() {
/* 108 */     return this.extraClassInfo.getLocation();
/*     */   }
/*     */   
/*     */   public String getMethodName() {
/* 112 */     return this.stackTraceElement.getMethodName();
/*     */   }
/*     */   
/*     */   public StackTraceElement getStackTraceElement() {
/* 116 */     return this.stackTraceElement;
/*     */   }
/*     */   
/*     */   public String getVersion() {
/* 120 */     return this.extraClassInfo.getVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 125 */     int prime = 31;
/* 126 */     int result = 1;
/* 127 */     result = 31 * result + ((this.extraClassInfo == null) ? 0 : this.extraClassInfo.hashCode());
/* 128 */     result = 31 * result + ((this.stackTraceElement == null) ? 0 : this.stackTraceElement.hashCode());
/* 129 */     return result;
/*     */   }
/*     */   
/*     */   public boolean isNativeMethod() {
/* 133 */     return this.stackTraceElement.isNativeMethod();
/*     */   }
/*     */   
/*     */   void renderOn(StringBuilder output, TextRenderer textRenderer) {
/* 137 */     render(this.stackTraceElement, output, textRenderer);
/* 138 */     textRenderer.render(" ", output, "Text");
/* 139 */     this.extraClassInfo.renderOn(output, textRenderer);
/*     */   }
/*     */   
/*     */   private void render(StackTraceElement stElement, StringBuilder output, TextRenderer textRenderer) {
/* 143 */     String fileName = stElement.getFileName();
/* 144 */     int lineNumber = stElement.getLineNumber();
/* 145 */     textRenderer.render(getClassName(), output, "StackTraceElement.ClassName");
/* 146 */     textRenderer.render(".", output, "StackTraceElement.ClassMethodSeparator");
/* 147 */     textRenderer.render(stElement.getMethodName(), output, "StackTraceElement.MethodName");
/* 148 */     if (stElement.isNativeMethod()) {
/* 149 */       textRenderer.render("(Native Method)", output, "StackTraceElement.NativeMethod");
/* 150 */     } else if (fileName != null && lineNumber >= 0) {
/* 151 */       textRenderer.render("(", output, "StackTraceElement.Container");
/* 152 */       textRenderer.render(fileName, output, "StackTraceElement.FileName");
/* 153 */       textRenderer.render(":", output, "StackTraceElement.ContainerSeparator");
/* 154 */       textRenderer.render(Integer.toString(lineNumber), output, "StackTraceElement.LineNumber");
/* 155 */       textRenderer.render(")", output, "StackTraceElement.Container");
/* 156 */     } else if (fileName != null) {
/* 157 */       textRenderer.render("(", output, "StackTraceElement.Container");
/* 158 */       textRenderer.render(fileName, output, "StackTraceElement.FileName");
/* 159 */       textRenderer.render(")", output, "StackTraceElement.Container");
/*     */     } else {
/* 161 */       textRenderer.render("(", output, "StackTraceElement.Container");
/* 162 */       textRenderer.render("Unknown Source", output, "StackTraceElement.UnknownSource");
/* 163 */       textRenderer.render(")", output, "StackTraceElement.Container");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 169 */     StringBuilder sb = new StringBuilder();
/* 170 */     renderOn(sb, (TextRenderer)PlainTextRenderer.getInstance());
/* 171 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\impl\ExtendedStackTraceElement.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */