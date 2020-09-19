/*     */ package io.netty.handler.codec.http2;
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
/*     */ final class HpackDynamicTable
/*     */ {
/*     */   HpackHeaderField[] hpackHeaderFields;
/*     */   int head;
/*     */   int tail;
/*     */   private long size;
/*  44 */   private long capacity = -1L;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   HpackDynamicTable(long initialCapacity) {
/*  50 */     setCapacity(initialCapacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/*     */     int length;
/*  58 */     if (this.head < this.tail) {
/*  59 */       length = this.hpackHeaderFields.length - this.tail + this.head;
/*     */     } else {
/*  61 */       length = this.head - this.tail;
/*     */     } 
/*  63 */     return length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long size() {
/*  70 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long capacity() {
/*  77 */     return this.capacity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HpackHeaderField getEntry(int index) {
/*  85 */     if (index <= 0 || index > length()) {
/*  86 */       throw new IndexOutOfBoundsException();
/*     */     }
/*  88 */     int i = this.head - index;
/*  89 */     if (i < 0) {
/*  90 */       return this.hpackHeaderFields[i + this.hpackHeaderFields.length];
/*     */     }
/*  92 */     return this.hpackHeaderFields[i];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(HpackHeaderField header) {
/* 103 */     int headerSize = header.size();
/* 104 */     if (headerSize > this.capacity) {
/* 105 */       clear();
/*     */       return;
/*     */     } 
/* 108 */     while (this.capacity - this.size < headerSize) {
/* 109 */       remove();
/*     */     }
/* 111 */     this.hpackHeaderFields[this.head++] = header;
/* 112 */     this.size += header.size();
/* 113 */     if (this.head == this.hpackHeaderFields.length) {
/* 114 */       this.head = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HpackHeaderField remove() {
/* 122 */     HpackHeaderField removed = this.hpackHeaderFields[this.tail];
/* 123 */     if (removed == null) {
/* 124 */       return null;
/*     */     }
/* 126 */     this.size -= removed.size();
/* 127 */     this.hpackHeaderFields[this.tail++] = null;
/* 128 */     if (this.tail == this.hpackHeaderFields.length) {
/* 129 */       this.tail = 0;
/*     */     }
/* 131 */     return removed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 138 */     while (this.tail != this.head) {
/* 139 */       this.hpackHeaderFields[this.tail++] = null;
/* 140 */       if (this.tail == this.hpackHeaderFields.length) {
/* 141 */         this.tail = 0;
/*     */       }
/*     */     } 
/* 144 */     this.head = 0;
/* 145 */     this.tail = 0;
/* 146 */     this.size = 0L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCapacity(long capacity) {
/* 154 */     if (capacity < 0L || capacity > 4294967295L) {
/* 155 */       throw new IllegalArgumentException("capacity is invalid: " + capacity);
/*     */     }
/*     */     
/* 158 */     if (this.capacity == capacity) {
/*     */       return;
/*     */     }
/* 161 */     this.capacity = capacity;
/*     */     
/* 163 */     if (capacity == 0L) {
/* 164 */       clear();
/*     */     } else {
/*     */       
/* 167 */       while (this.size > capacity) {
/* 168 */         remove();
/*     */       }
/*     */     } 
/*     */     
/* 172 */     int maxEntries = (int)(capacity / 32L);
/* 173 */     if (capacity % 32L != 0L) {
/* 174 */       maxEntries++;
/*     */     }
/*     */ 
/*     */     
/* 178 */     if (this.hpackHeaderFields != null && this.hpackHeaderFields.length == maxEntries) {
/*     */       return;
/*     */     }
/*     */     
/* 182 */     HpackHeaderField[] tmp = new HpackHeaderField[maxEntries];
/*     */ 
/*     */     
/* 185 */     int len = length();
/* 186 */     int cursor = this.tail;
/* 187 */     for (int i = 0; i < len; i++) {
/* 188 */       HpackHeaderField entry = this.hpackHeaderFields[cursor++];
/* 189 */       tmp[i] = entry;
/* 190 */       if (cursor == this.hpackHeaderFields.length) {
/* 191 */         cursor = 0;
/*     */       }
/*     */     } 
/*     */     
/* 195 */     this.tail = 0;
/* 196 */     this.head = this.tail + len;
/* 197 */     this.hpackHeaderFields = tmp;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\HpackDynamicTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */