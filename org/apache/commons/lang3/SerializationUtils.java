/*     */ package org.apache.commons.lang3;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.ObjectStreamClass;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public class SerializationUtils
/*     */ {
/*     */   public static <T extends Serializable> T clone(T object) {
/*  78 */     if (object == null) {
/*  79 */       return null;
/*     */     }
/*  81 */     byte[] objectData = serialize((Serializable)object);
/*  82 */     ByteArrayInputStream bais = new ByteArrayInputStream(objectData);
/*     */     
/*  84 */     ClassLoaderAwareObjectInputStream in = null;
/*     */     
/*     */     try {
/*  87 */       in = new ClassLoaderAwareObjectInputStream(bais, object.getClass().getClassLoader());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  94 */       Serializable serializable = (Serializable)in.readObject();
/*  95 */       return (T)serializable;
/*     */     }
/*  97 */     catch (ClassNotFoundException ex) {
/*  98 */       throw new SerializationException("ClassNotFoundException while reading cloned object data", ex);
/*  99 */     } catch (IOException ex) {
/* 100 */       throw new SerializationException("IOException while reading cloned object data", ex);
/*     */     } finally {
/*     */       try {
/* 103 */         if (in != null) {
/* 104 */           in.close();
/*     */         }
/* 106 */       } catch (IOException ex) {
/* 107 */         throw new SerializationException("IOException on closing cloned object data InputStream.", ex);
/*     */       } 
/*     */     } 
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
/*     */ 
/*     */   
/*     */   public static <T extends Serializable> T roundtrip(T msg) {
/* 125 */     return (T)deserialize(serialize((Serializable)msg));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void serialize(Serializable obj, OutputStream outputStream) {
/* 146 */     if (outputStream == null) {
/* 147 */       throw new IllegalArgumentException("The OutputStream must not be null");
/*     */     }
/* 149 */     ObjectOutputStream out = null;
/*     */     
/*     */     try {
/* 152 */       out = new ObjectOutputStream(outputStream);
/* 153 */       out.writeObject(obj);
/*     */     }
/* 155 */     catch (IOException ex) {
/* 156 */       throw new SerializationException(ex);
/*     */     } finally {
/*     */       try {
/* 159 */         if (out != null) {
/* 160 */           out.close();
/*     */         }
/* 162 */       } catch (IOException iOException) {}
/*     */     } 
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
/*     */   public static byte[] serialize(Serializable obj) {
/* 177 */     ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
/* 178 */     serialize(obj, baos);
/* 179 */     return baos.toByteArray();
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
/*     */   public static <T> T deserialize(InputStream inputStream) {
/* 215 */     if (inputStream == null) {
/* 216 */       throw new IllegalArgumentException("The InputStream must not be null");
/*     */     }
/* 218 */     ObjectInputStream in = null;
/*     */     
/*     */     try {
/* 221 */       in = new ObjectInputStream(inputStream);
/*     */       
/* 223 */       T obj = (T)in.readObject();
/* 224 */       return obj;
/*     */     }
/* 226 */     catch (ClassNotFoundException ex) {
/* 227 */       throw new SerializationException(ex);
/* 228 */     } catch (IOException ex) {
/* 229 */       throw new SerializationException(ex);
/*     */     } finally {
/*     */       try {
/* 232 */         if (in != null) {
/* 233 */           in.close();
/*     */         }
/* 235 */       } catch (IOException iOException) {}
/*     */     } 
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
/*     */   public static <T> T deserialize(byte[] objectData) {
/* 262 */     if (objectData == null) {
/* 263 */       throw new IllegalArgumentException("The byte[] must not be null");
/*     */     }
/* 265 */     return deserialize(new ByteArrayInputStream(objectData));
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
/*     */   
/*     */   static class ClassLoaderAwareObjectInputStream
/*     */     extends ObjectInputStream
/*     */   {
/* 282 */     private static final Map<String, Class<?>> primitiveTypes = new HashMap<String, Class<?>>();
/*     */     private final ClassLoader classLoader;
/*     */     
/*     */     static {
/* 286 */       primitiveTypes.put("byte", byte.class);
/* 287 */       primitiveTypes.put("short", short.class);
/* 288 */       primitiveTypes.put("int", int.class);
/* 289 */       primitiveTypes.put("long", long.class);
/* 290 */       primitiveTypes.put("float", float.class);
/* 291 */       primitiveTypes.put("double", double.class);
/* 292 */       primitiveTypes.put("boolean", boolean.class);
/* 293 */       primitiveTypes.put("char", char.class);
/* 294 */       primitiveTypes.put("void", void.class);
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
/*     */     
/*     */     public ClassLoaderAwareObjectInputStream(InputStream in, ClassLoader classLoader) throws IOException {
/* 307 */       super(in);
/* 308 */       this.classLoader = classLoader;
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
/*     */     
/*     */     protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
/* 321 */       String name = desc.getName();
/*     */       try {
/* 323 */         return Class.forName(name, false, this.classLoader);
/* 324 */       } catch (ClassNotFoundException ex) {
/*     */         try {
/* 326 */           return Class.forName(name, false, Thread.currentThread().getContextClassLoader());
/* 327 */         } catch (ClassNotFoundException cnfe) {
/* 328 */           Class<?> cls = primitiveTypes.get(name);
/* 329 */           if (cls != null) {
/* 330 */             return cls;
/*     */           }
/* 332 */           throw cnfe;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\SerializationUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */