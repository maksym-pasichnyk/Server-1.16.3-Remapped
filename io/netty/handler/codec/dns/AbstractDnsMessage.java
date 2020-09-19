/*     */ package io.netty.handler.codec.dns;
/*     */ 
/*     */ import io.netty.util.AbstractReferenceCounted;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.ReferenceCounted;
/*     */ import io.netty.util.ResourceLeakDetector;
/*     */ import io.netty.util.ResourceLeakDetectorFactory;
/*     */ import io.netty.util.ResourceLeakTracker;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public abstract class AbstractDnsMessage
/*     */   extends AbstractReferenceCounted
/*     */   implements DnsMessage
/*     */ {
/*  39 */   private static final ResourceLeakDetector<DnsMessage> leakDetector = ResourceLeakDetectorFactory.instance().newResourceLeakDetector(DnsMessage.class);
/*     */   
/*  41 */   private static final int SECTION_QUESTION = DnsSection.QUESTION.ordinal();
/*     */   
/*     */   private static final int SECTION_COUNT = 4;
/*  44 */   private final ResourceLeakTracker<DnsMessage> leak = leakDetector.track(this);
/*     */   
/*     */   private short id;
/*     */   
/*     */   private DnsOpCode opCode;
/*     */   
/*     */   private boolean recursionDesired;
/*     */   
/*     */   private byte z;
/*     */   
/*     */   private Object questions;
/*     */   
/*     */   private Object answers;
/*     */   private Object authorities;
/*     */   private Object additionals;
/*     */   
/*     */   protected AbstractDnsMessage(int id) {
/*  61 */     this(id, DnsOpCode.QUERY);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractDnsMessage(int id, DnsOpCode opCode) {
/*  68 */     setId(id);
/*  69 */     setOpCode(opCode);
/*     */   }
/*     */ 
/*     */   
/*     */   public int id() {
/*  74 */     return this.id & 0xFFFF;
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsMessage setId(int id) {
/*  79 */     this.id = (short)id;
/*  80 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsOpCode opCode() {
/*  85 */     return this.opCode;
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsMessage setOpCode(DnsOpCode opCode) {
/*  90 */     this.opCode = (DnsOpCode)ObjectUtil.checkNotNull(opCode, "opCode");
/*  91 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRecursionDesired() {
/*  96 */     return this.recursionDesired;
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsMessage setRecursionDesired(boolean recursionDesired) {
/* 101 */     this.recursionDesired = recursionDesired;
/* 102 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int z() {
/* 107 */     return this.z;
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsMessage setZ(int z) {
/* 112 */     this.z = (byte)(z & 0x7);
/* 113 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int count(DnsSection section) {
/* 118 */     return count(sectionOrdinal(section));
/*     */   }
/*     */   
/*     */   private int count(int section) {
/* 122 */     Object records = sectionAt(section);
/* 123 */     if (records == null) {
/* 124 */       return 0;
/*     */     }
/* 126 */     if (records instanceof DnsRecord) {
/* 127 */       return 1;
/*     */     }
/*     */ 
/*     */     
/* 131 */     List<DnsRecord> recordList = (List<DnsRecord>)records;
/* 132 */     return recordList.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public int count() {
/* 137 */     int count = 0;
/* 138 */     for (int i = 0; i < 4; i++) {
/* 139 */       count += count(i);
/*     */     }
/* 141 */     return count;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends DnsRecord> T recordAt(DnsSection section) {
/* 146 */     return recordAt(sectionOrdinal(section));
/*     */   }
/*     */   
/*     */   private <T extends DnsRecord> T recordAt(int section) {
/* 150 */     Object records = sectionAt(section);
/* 151 */     if (records == null) {
/* 152 */       return null;
/*     */     }
/*     */     
/* 155 */     if (records instanceof DnsRecord) {
/* 156 */       return castRecord(records);
/*     */     }
/*     */ 
/*     */     
/* 160 */     List<DnsRecord> recordList = (List<DnsRecord>)records;
/* 161 */     if (recordList.isEmpty()) {
/* 162 */       return null;
/*     */     }
/*     */     
/* 165 */     return castRecord(recordList.get(0));
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends DnsRecord> T recordAt(DnsSection section, int index) {
/* 170 */     return recordAt(sectionOrdinal(section), index);
/*     */   }
/*     */   
/*     */   private <T extends DnsRecord> T recordAt(int section, int index) {
/* 174 */     Object records = sectionAt(section);
/* 175 */     if (records == null) {
/* 176 */       throw new IndexOutOfBoundsException("index: " + index + " (expected: none)");
/*     */     }
/*     */     
/* 179 */     if (records instanceof DnsRecord) {
/* 180 */       if (index == 0) {
/* 181 */         return castRecord(records);
/*     */       }
/* 183 */       throw new IndexOutOfBoundsException("index: " + index + "' (expected: 0)");
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 188 */     List<DnsRecord> recordList = (List<DnsRecord>)records;
/* 189 */     return castRecord(recordList.get(index));
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsMessage setRecord(DnsSection section, DnsRecord record) {
/* 194 */     setRecord(sectionOrdinal(section), record);
/* 195 */     return this;
/*     */   }
/*     */   
/*     */   private void setRecord(int section, DnsRecord record) {
/* 199 */     clear(section);
/* 200 */     setSection(section, checkQuestion(section, record));
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends DnsRecord> T setRecord(DnsSection section, int index, DnsRecord record) {
/* 205 */     return setRecord(sectionOrdinal(section), index, record);
/*     */   }
/*     */   
/*     */   private <T extends DnsRecord> T setRecord(int section, int index, DnsRecord record) {
/* 209 */     checkQuestion(section, record);
/*     */     
/* 211 */     Object records = sectionAt(section);
/* 212 */     if (records == null) {
/* 213 */       throw new IndexOutOfBoundsException("index: " + index + " (expected: none)");
/*     */     }
/*     */     
/* 216 */     if (records instanceof DnsRecord) {
/* 217 */       if (index == 0) {
/* 218 */         setSection(section, record);
/* 219 */         return castRecord(records);
/*     */       } 
/* 221 */       throw new IndexOutOfBoundsException("index: " + index + " (expected: 0)");
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 226 */     List<DnsRecord> recordList = (List<DnsRecord>)records;
/* 227 */     return castRecord(recordList.set(index, record));
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsMessage addRecord(DnsSection section, DnsRecord record) {
/* 232 */     addRecord(sectionOrdinal(section), record);
/* 233 */     return this;
/*     */   }
/*     */   
/*     */   private void addRecord(int section, DnsRecord record) {
/* 237 */     checkQuestion(section, record);
/*     */     
/* 239 */     Object records = sectionAt(section);
/* 240 */     if (records == null) {
/* 241 */       setSection(section, record);
/*     */       
/*     */       return;
/*     */     } 
/* 245 */     if (records instanceof DnsRecord) {
/* 246 */       List<DnsRecord> list = newRecordList();
/* 247 */       list.add(castRecord(records));
/* 248 */       list.add(record);
/* 249 */       setSection(section, list);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 254 */     List<DnsRecord> recordList = (List<DnsRecord>)records;
/* 255 */     recordList.add(record);
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsMessage addRecord(DnsSection section, int index, DnsRecord record) {
/* 260 */     addRecord(sectionOrdinal(section), index, record);
/* 261 */     return this;
/*     */   }
/*     */   
/*     */   private void addRecord(int section, int index, DnsRecord record) {
/* 265 */     checkQuestion(section, record);
/*     */     
/* 267 */     Object records = sectionAt(section);
/* 268 */     if (records == null) {
/* 269 */       if (index != 0) {
/* 270 */         throw new IndexOutOfBoundsException("index: " + index + " (expected: 0)");
/*     */       }
/*     */       
/* 273 */       setSection(section, record);
/*     */       
/*     */       return;
/*     */     } 
/* 277 */     if (records instanceof DnsRecord) {
/*     */       List<DnsRecord> list;
/* 279 */       if (index == 0) {
/* 280 */         list = newRecordList();
/* 281 */         list.add(record);
/* 282 */         list.add(castRecord(records));
/* 283 */       } else if (index == 1) {
/* 284 */         list = newRecordList();
/* 285 */         list.add(castRecord(records));
/* 286 */         list.add(record);
/*     */       } else {
/* 288 */         throw new IndexOutOfBoundsException("index: " + index + " (expected: 0 or 1)");
/*     */       } 
/* 290 */       setSection(section, list);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 295 */     List<DnsRecord> recordList = (List<DnsRecord>)records;
/* 296 */     recordList.add(index, record);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends DnsRecord> T removeRecord(DnsSection section, int index) {
/* 301 */     return removeRecord(sectionOrdinal(section), index);
/*     */   }
/*     */   
/*     */   private <T extends DnsRecord> T removeRecord(int section, int index) {
/* 305 */     Object records = sectionAt(section);
/* 306 */     if (records == null) {
/* 307 */       throw new IndexOutOfBoundsException("index: " + index + " (expected: none)");
/*     */     }
/*     */     
/* 310 */     if (records instanceof DnsRecord) {
/* 311 */       if (index != 0) {
/* 312 */         throw new IndexOutOfBoundsException("index: " + index + " (expected: 0)");
/*     */       }
/*     */       
/* 315 */       T record = castRecord(records);
/* 316 */       setSection(section, null);
/* 317 */       return record;
/*     */     } 
/*     */ 
/*     */     
/* 321 */     List<DnsRecord> recordList = (List<DnsRecord>)records;
/* 322 */     return castRecord(recordList.remove(index));
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsMessage clear(DnsSection section) {
/* 327 */     clear(sectionOrdinal(section));
/* 328 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsMessage clear() {
/* 333 */     for (int i = 0; i < 4; i++) {
/* 334 */       clear(i);
/*     */     }
/* 336 */     return this;
/*     */   }
/*     */   
/*     */   private void clear(int section) {
/* 340 */     Object recordOrList = sectionAt(section);
/* 341 */     setSection(section, null);
/* 342 */     if (recordOrList instanceof ReferenceCounted) {
/* 343 */       ((ReferenceCounted)recordOrList).release();
/* 344 */     } else if (recordOrList instanceof List) {
/*     */       
/* 346 */       List<DnsRecord> list = (List<DnsRecord>)recordOrList;
/* 347 */       if (!list.isEmpty()) {
/* 348 */         for (DnsRecord r : list) {
/* 349 */           ReferenceCountUtil.release(r);
/*     */         }
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsMessage touch() {
/* 357 */     return (DnsMessage)super.touch();
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsMessage touch(Object hint) {
/* 362 */     if (this.leak != null) {
/* 363 */       this.leak.record(hint);
/*     */     }
/* 365 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsMessage retain() {
/* 370 */     return (DnsMessage)super.retain();
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsMessage retain(int increment) {
/* 375 */     return (DnsMessage)super.retain(increment);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void deallocate() {
/* 380 */     clear();
/*     */     
/* 382 */     ResourceLeakTracker<DnsMessage> leak = this.leak;
/* 383 */     if (leak != null) {
/* 384 */       boolean closed = leak.close(this);
/* 385 */       assert closed;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 391 */     if (this == obj) {
/* 392 */       return true;
/*     */     }
/*     */     
/* 395 */     if (!(obj instanceof DnsMessage)) {
/* 396 */       return false;
/*     */     }
/*     */     
/* 399 */     DnsMessage that = (DnsMessage)obj;
/* 400 */     if (id() != that.id()) {
/* 401 */       return false;
/*     */     }
/*     */     
/* 404 */     if (this instanceof DnsQuery) {
/* 405 */       if (!(that instanceof DnsQuery)) {
/* 406 */         return false;
/*     */       }
/* 408 */     } else if (that instanceof DnsQuery) {
/* 409 */       return false;
/*     */     } 
/*     */     
/* 412 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 417 */     return id() * 31 + ((this instanceof DnsQuery) ? 0 : 1);
/*     */   }
/*     */   
/*     */   private Object sectionAt(int section) {
/* 421 */     switch (section) {
/*     */       case 0:
/* 423 */         return this.questions;
/*     */       case 1:
/* 425 */         return this.answers;
/*     */       case 2:
/* 427 */         return this.authorities;
/*     */       case 3:
/* 429 */         return this.additionals;
/*     */     } 
/*     */     
/* 432 */     throw new Error();
/*     */   }
/*     */   
/*     */   private void setSection(int section, Object value) {
/* 436 */     switch (section) {
/*     */       case 0:
/* 438 */         this.questions = value;
/*     */         return;
/*     */       case 1:
/* 441 */         this.answers = value;
/*     */         return;
/*     */       case 2:
/* 444 */         this.authorities = value;
/*     */         return;
/*     */       case 3:
/* 447 */         this.additionals = value;
/*     */         return;
/*     */     } 
/*     */     
/* 451 */     throw new Error();
/*     */   }
/*     */   
/*     */   private static int sectionOrdinal(DnsSection section) {
/* 455 */     return ((DnsSection)ObjectUtil.checkNotNull(section, "section")).ordinal();
/*     */   }
/*     */   
/*     */   private static DnsRecord checkQuestion(int section, DnsRecord record) {
/* 459 */     if (section == SECTION_QUESTION && !(ObjectUtil.checkNotNull(record, "record") instanceof DnsQuestion)) {
/* 460 */       throw new IllegalArgumentException("record: " + record + " (expected: " + 
/* 461 */           StringUtil.simpleClassName(DnsQuestion.class) + ')');
/*     */     }
/* 463 */     return record;
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T extends DnsRecord> T castRecord(Object record) {
/* 468 */     return (T)record;
/*     */   }
/*     */   
/*     */   private static ArrayList<DnsRecord> newRecordList() {
/* 472 */     return new ArrayList<DnsRecord>(2);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\dns\AbstractDnsMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */