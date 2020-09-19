/*     */ package org.apache.logging.log4j;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import org.apache.logging.log4j.util.PerformanceSensitive;
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
/*     */ public final class MarkerManager
/*     */ {
/*  31 */   private static final ConcurrentMap<String, Marker> MARKERS = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void clear() {
/*  41 */     MARKERS.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean exists(String key) {
/*  52 */     return MARKERS.containsKey(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Marker getMarker(String name) {
/*  63 */     Marker result = MARKERS.get(name);
/*  64 */     if (result == null) {
/*  65 */       MARKERS.putIfAbsent(name, new Log4jMarker(name));
/*  66 */       result = MARKERS.get(name);
/*     */     } 
/*  68 */     return result;
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
/*     */   @Deprecated
/*     */   public static Marker getMarker(String name, String parent) {
/*  82 */     Marker parentMarker = MARKERS.get(parent);
/*  83 */     if (parentMarker == null) {
/*  84 */       throw new IllegalArgumentException("Parent Marker " + parent + " has not been defined");
/*     */     }
/*  86 */     return getMarker(name, parentMarker);
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
/*     */   @Deprecated
/*     */   public static Marker getMarker(String name, Marker parent) {
/* 100 */     return getMarker(name).addParents(new Marker[] { parent });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Log4jMarker
/*     */     implements Marker, StringBuilderFormattable
/*     */   {
/*     */     private static final long serialVersionUID = 100L;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String name;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private volatile Marker[] parents;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Log4jMarker() {
/* 127 */       this.name = null;
/* 128 */       this.parents = null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Log4jMarker(String name) {
/* 140 */       MarkerManager.requireNonNull(name, "Marker name cannot be null.");
/* 141 */       this.name = name;
/* 142 */       this.parents = null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public synchronized Marker addParents(Marker... parentMarkers) {
/* 149 */       MarkerManager.requireNonNull(parentMarkers, "A parent marker must be specified");
/*     */ 
/*     */       
/* 152 */       Marker[] localParents = this.parents;
/*     */       
/* 154 */       int count = 0;
/* 155 */       int size = parentMarkers.length;
/* 156 */       if (localParents != null) {
/* 157 */         for (Marker parent : parentMarkers) {
/* 158 */           if (!contains(parent, localParents) && !parent.isInstanceOf(this)) {
/* 159 */             count++;
/*     */           }
/*     */         } 
/* 162 */         if (count == 0) {
/* 163 */           return this;
/*     */         }
/* 165 */         size = localParents.length + count;
/*     */       } 
/* 167 */       Marker[] markers = new Marker[size];
/* 168 */       if (localParents != null)
/*     */       {
/*     */         
/* 171 */         System.arraycopy(localParents, 0, markers, 0, localParents.length);
/*     */       }
/* 173 */       int index = (localParents == null) ? 0 : localParents.length;
/* 174 */       for (Marker parent : parentMarkers) {
/* 175 */         if (localParents == null || (!contains(parent, localParents) && !parent.isInstanceOf(this))) {
/* 176 */           markers[index++] = parent;
/*     */         }
/*     */       } 
/* 179 */       this.parents = markers;
/* 180 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public synchronized boolean remove(Marker parent) {
/* 185 */       MarkerManager.requireNonNull(parent, "A parent marker must be specified");
/* 186 */       Marker[] localParents = this.parents;
/* 187 */       if (localParents == null) {
/* 188 */         return false;
/*     */       }
/* 190 */       int localParentsLength = localParents.length;
/* 191 */       if (localParentsLength == 1) {
/* 192 */         if (localParents[0].equals(parent)) {
/* 193 */           this.parents = null;
/* 194 */           return true;
/*     */         } 
/* 196 */         return false;
/*     */       } 
/* 198 */       int index = 0;
/* 199 */       Marker[] markers = new Marker[localParentsLength - 1];
/*     */       
/* 201 */       for (int i = 0; i < localParentsLength; i++) {
/* 202 */         Marker marker = localParents[i];
/* 203 */         if (!marker.equals(parent)) {
/* 204 */           if (index == localParentsLength - 1)
/*     */           {
/* 206 */             return false;
/*     */           }
/* 208 */           markers[index++] = marker;
/*     */         } 
/*     */       } 
/* 211 */       this.parents = markers;
/* 212 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public Marker setParents(Marker... markers) {
/* 217 */       if (markers == null || markers.length == 0) {
/* 218 */         this.parents = null;
/*     */       } else {
/* 220 */         Marker[] array = new Marker[markers.length];
/* 221 */         System.arraycopy(markers, 0, array, 0, markers.length);
/* 222 */         this.parents = array;
/*     */       } 
/* 224 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getName() {
/* 229 */       return this.name;
/*     */     }
/*     */ 
/*     */     
/*     */     public Marker[] getParents() {
/* 234 */       if (this.parents == null) {
/* 235 */         return null;
/*     */       }
/* 237 */       return Arrays.<Marker>copyOf(this.parents, this.parents.length);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasParents() {
/* 242 */       return (this.parents != null);
/*     */     }
/*     */ 
/*     */     
/*     */     @PerformanceSensitive({"allocation", "unrolled"})
/*     */     public boolean isInstanceOf(Marker marker) {
/* 248 */       MarkerManager.requireNonNull(marker, "A marker parameter is required");
/* 249 */       if (this == marker) {
/* 250 */         return true;
/*     */       }
/* 252 */       Marker[] localParents = this.parents;
/* 253 */       if (localParents != null) {
/*     */         
/* 255 */         int localParentsLength = localParents.length;
/* 256 */         if (localParentsLength == 1) {
/* 257 */           return checkParent(localParents[0], marker);
/*     */         }
/* 259 */         if (localParentsLength == 2) {
/* 260 */           return (checkParent(localParents[0], marker) || checkParent(localParents[1], marker));
/*     */         }
/*     */         
/* 263 */         for (int i = 0; i < localParentsLength; i++) {
/* 264 */           Marker localParent = localParents[i];
/* 265 */           if (checkParent(localParent, marker)) {
/* 266 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/* 270 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     @PerformanceSensitive({"allocation", "unrolled"})
/*     */     public boolean isInstanceOf(String markerName) {
/* 276 */       MarkerManager.requireNonNull(markerName, "A marker name is required");
/* 277 */       if (markerName.equals(getName())) {
/* 278 */         return true;
/*     */       }
/*     */       
/* 281 */       Marker marker = (Marker)MarkerManager.MARKERS.get(markerName);
/* 282 */       if (marker == null) {
/* 283 */         return false;
/*     */       }
/* 285 */       Marker[] localParents = this.parents;
/* 286 */       if (localParents != null) {
/* 287 */         int localParentsLength = localParents.length;
/* 288 */         if (localParentsLength == 1) {
/* 289 */           return checkParent(localParents[0], marker);
/*     */         }
/* 291 */         if (localParentsLength == 2) {
/* 292 */           return (checkParent(localParents[0], marker) || checkParent(localParents[1], marker));
/*     */         }
/*     */         
/* 295 */         for (int i = 0; i < localParentsLength; i++) {
/* 296 */           Marker localParent = localParents[i];
/* 297 */           if (checkParent(localParent, marker)) {
/* 298 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 303 */       return false;
/*     */     }
/*     */     
/*     */     @PerformanceSensitive({"allocation", "unrolled"})
/*     */     private static boolean checkParent(Marker parent, Marker marker) {
/* 308 */       if (parent == marker) {
/* 309 */         return true;
/*     */       }
/* 311 */       Marker[] localParents = (parent instanceof Log4jMarker) ? ((Log4jMarker)parent).parents : parent.getParents();
/*     */       
/* 313 */       if (localParents != null) {
/* 314 */         int localParentsLength = localParents.length;
/* 315 */         if (localParentsLength == 1) {
/* 316 */           return checkParent(localParents[0], marker);
/*     */         }
/* 318 */         if (localParentsLength == 2) {
/* 319 */           return (checkParent(localParents[0], marker) || checkParent(localParents[1], marker));
/*     */         }
/*     */         
/* 322 */         for (int i = 0; i < localParentsLength; i++) {
/* 323 */           Marker localParent = localParents[i];
/* 324 */           if (checkParent(localParent, marker)) {
/* 325 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/* 329 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @PerformanceSensitive({"allocation"})
/*     */     private static boolean contains(Marker parent, Marker... localParents) {
/* 339 */       for (int i = 0, localParentsLength = localParents.length; i < localParentsLength; i++) {
/* 340 */         Marker marker = localParents[i];
/* 341 */         if (marker == parent) {
/* 342 */           return true;
/*     */         }
/*     */       } 
/* 345 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 350 */       if (this == o) {
/* 351 */         return true;
/*     */       }
/* 353 */       if (o == null || !(o instanceof Marker)) {
/* 354 */         return false;
/*     */       }
/* 356 */       Marker marker = (Marker)o;
/* 357 */       return this.name.equals(marker.getName());
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 362 */       return this.name.hashCode();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 368 */       StringBuilder sb = new StringBuilder();
/* 369 */       formatTo(sb);
/* 370 */       return sb.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public void formatTo(StringBuilder sb) {
/* 375 */       sb.append(this.name);
/* 376 */       Marker[] localParents = this.parents;
/* 377 */       if (localParents != null) {
/* 378 */         addParentInfo(sb, localParents);
/*     */       }
/*     */     }
/*     */     
/*     */     @PerformanceSensitive({"allocation"})
/*     */     private static void addParentInfo(StringBuilder sb, Marker... parents) {
/* 384 */       sb.append("[ ");
/* 385 */       boolean first = true;
/*     */       
/* 387 */       for (int i = 0, parentsLength = parents.length; i < parentsLength; i++) {
/* 388 */         Marker marker = parents[i];
/* 389 */         if (!first) {
/* 390 */           sb.append(", ");
/*     */         }
/* 392 */         first = false;
/* 393 */         sb.append(marker.getName());
/* 394 */         Marker[] p = (marker instanceof Log4jMarker) ? ((Log4jMarker)marker).parents : marker.getParents();
/* 395 */         if (p != null) {
/* 396 */           addParentInfo(sb, p);
/*     */         }
/*     */       } 
/* 399 */       sb.append(" ]");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static void requireNonNull(Object obj, String message) {
/* 405 */     if (obj == null)
/* 406 */       throw new IllegalArgumentException(message); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\MarkerManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */