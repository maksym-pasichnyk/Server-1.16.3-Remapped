/*     */ package org.apache.logging.log4j.spi;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.ThreadContext;
/*     */ import org.apache.logging.log4j.util.StringBuilderFormattable;
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
/*     */ public class MutableThreadContextStack
/*     */   implements ThreadContextStack, StringBuilderFormattable
/*     */ {
/*     */   private static final long serialVersionUID = 50505011L;
/*     */   private final List<String> list;
/*     */   private boolean frozen;
/*     */   
/*     */   public MutableThreadContextStack() {
/*  44 */     this(new ArrayList<>());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MutableThreadContextStack(List<String> list) {
/*  52 */     this.list = new ArrayList<>(list);
/*     */   }
/*     */   
/*     */   private MutableThreadContextStack(MutableThreadContextStack stack) {
/*  56 */     this.list = new ArrayList<>(stack.list);
/*     */   }
/*     */   
/*     */   private void checkInvariants() {
/*  60 */     if (this.frozen) {
/*  61 */       throw new UnsupportedOperationException("context stack has been frozen");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String pop() {
/*  67 */     checkInvariants();
/*  68 */     if (this.list.isEmpty()) {
/*  69 */       return null;
/*     */     }
/*  71 */     int last = this.list.size() - 1;
/*  72 */     String result = this.list.remove(last);
/*  73 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String peek() {
/*  78 */     if (this.list.isEmpty()) {
/*  79 */       return null;
/*     */     }
/*  81 */     int last = this.list.size() - 1;
/*  82 */     return this.list.get(last);
/*     */   }
/*     */ 
/*     */   
/*     */   public void push(String message) {
/*  87 */     checkInvariants();
/*  88 */     this.list.add(message);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDepth() {
/*  93 */     return this.list.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> asList() {
/*  98 */     return this.list;
/*     */   }
/*     */ 
/*     */   
/*     */   public void trim(int depth) {
/* 103 */     checkInvariants();
/* 104 */     if (depth < 0) {
/* 105 */       throw new IllegalArgumentException("Maximum stack depth cannot be negative");
/*     */     }
/* 107 */     if (this.list == null) {
/*     */       return;
/*     */     }
/* 110 */     List<String> copy = new ArrayList<>(this.list.size());
/* 111 */     int count = Math.min(depth, this.list.size());
/* 112 */     for (int i = 0; i < count; i++) {
/* 113 */       copy.add(this.list.get(i));
/*     */     }
/* 115 */     this.list.clear();
/* 116 */     this.list.addAll(copy);
/*     */   }
/*     */ 
/*     */   
/*     */   public ThreadContextStack copy() {
/* 121 */     return new MutableThreadContextStack(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 126 */     checkInvariants();
/* 127 */     this.list.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 132 */     return this.list.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 137 */     return this.list.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object o) {
/* 142 */     return this.list.contains(o);
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<String> iterator() {
/* 147 */     return this.list.iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 152 */     return this.list.toArray();
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T[] toArray(T[] ts) {
/* 157 */     return this.list.toArray(ts);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean add(String s) {
/* 162 */     checkInvariants();
/* 163 */     return this.list.add(s);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(Object o) {
/* 168 */     checkInvariants();
/* 169 */     return this.list.remove(o);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsAll(Collection<?> objects) {
/* 174 */     return this.list.containsAll(objects);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addAll(Collection<? extends String> strings) {
/* 179 */     checkInvariants();
/* 180 */     return this.list.addAll(strings);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeAll(Collection<?> objects) {
/* 185 */     checkInvariants();
/* 186 */     return this.list.removeAll(objects);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean retainAll(Collection<?> objects) {
/* 191 */     checkInvariants();
/* 192 */     return this.list.retainAll(objects);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 197 */     return String.valueOf(this.list);
/*     */   }
/*     */ 
/*     */   
/*     */   public void formatTo(StringBuilder buffer) {
/* 202 */     buffer.append('[');
/* 203 */     for (int i = 0; i < this.list.size(); i++) {
/* 204 */       if (i > 0) {
/* 205 */         buffer.append(',').append(' ');
/*     */       }
/* 207 */       buffer.append(this.list.get(i));
/*     */     } 
/* 209 */     buffer.append(']');
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 214 */     int prime = 31;
/* 215 */     int result = 1;
/* 216 */     result = 31 * result + ((this.list == null) ? 0 : this.list.hashCode());
/* 217 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 222 */     if (this == obj) {
/* 223 */       return true;
/*     */     }
/* 225 */     if (obj == null) {
/* 226 */       return false;
/*     */     }
/* 228 */     if (!(obj instanceof ThreadContextStack)) {
/* 229 */       return false;
/*     */     }
/* 231 */     ThreadContextStack other = (ThreadContextStack)obj;
/* 232 */     List<String> otherAsList = other.asList();
/* 233 */     if (this.list == null) {
/* 234 */       if (otherAsList != null) {
/* 235 */         return false;
/*     */       }
/* 237 */     } else if (!this.list.equals(otherAsList)) {
/* 238 */       return false;
/*     */     } 
/* 240 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public ThreadContext.ContextStack getImmutableStackOrNull() {
/* 245 */     return copy();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void freeze() {
/* 252 */     this.frozen = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFrozen() {
/* 260 */     return this.frozen;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\spi\MutableThreadContextStack.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */