/*     */ package org.apache.commons.io.comparator;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.Serializable;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import org.apache.commons.io.IOCase;
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
/*     */ public class NameFileComparator
/*     */   extends AbstractFileComparator
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 8397947749814525798L;
/*  57 */   public static final Comparator<File> NAME_COMPARATOR = new NameFileComparator();
/*     */ 
/*     */   
/*  60 */   public static final Comparator<File> NAME_REVERSE = new ReverseComparator(NAME_COMPARATOR);
/*     */ 
/*     */   
/*  63 */   public static final Comparator<File> NAME_INSENSITIVE_COMPARATOR = new NameFileComparator(IOCase.INSENSITIVE);
/*     */ 
/*     */   
/*  66 */   public static final Comparator<File> NAME_INSENSITIVE_REVERSE = new ReverseComparator(NAME_INSENSITIVE_COMPARATOR);
/*     */ 
/*     */   
/*  69 */   public static final Comparator<File> NAME_SYSTEM_COMPARATOR = new NameFileComparator(IOCase.SYSTEM);
/*     */ 
/*     */   
/*  72 */   public static final Comparator<File> NAME_SYSTEM_REVERSE = new ReverseComparator(NAME_SYSTEM_COMPARATOR);
/*     */ 
/*     */ 
/*     */   
/*     */   private final IOCase caseSensitivity;
/*     */ 
/*     */ 
/*     */   
/*     */   public NameFileComparator() {
/*  81 */     this.caseSensitivity = IOCase.SENSITIVE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NameFileComparator(IOCase caseSensitivity) {
/*  90 */     this.caseSensitivity = (caseSensitivity == null) ? IOCase.SENSITIVE : caseSensitivity;
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
/*     */   public int compare(File file1, File file2) {
/* 104 */     return this.caseSensitivity.checkCompareTo(file1.getName(), file2.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 114 */     return super.toString() + "[caseSensitivity=" + this.caseSensitivity + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\io\comparator\NameFileComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */