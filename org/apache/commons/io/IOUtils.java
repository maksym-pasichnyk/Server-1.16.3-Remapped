/*      */ package org.apache.commons.io;
/*      */ 
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.BufferedWriter;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.CharArrayWriter;
/*      */ import java.io.Closeable;
/*      */ import java.io.EOFException;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.OutputStream;
/*      */ import java.io.OutputStreamWriter;
/*      */ import java.io.PrintWriter;
/*      */ import java.io.Reader;
/*      */ import java.io.Writer;
/*      */ import java.net.HttpURLConnection;
/*      */ import java.net.ServerSocket;
/*      */ import java.net.Socket;
/*      */ import java.net.URI;
/*      */ import java.net.URL;
/*      */ import java.net.URLConnection;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.channels.ReadableByteChannel;
/*      */ import java.nio.channels.Selector;
/*      */ import java.nio.charset.Charset;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.List;
/*      */ import org.apache.commons.io.output.ByteArrayOutputStream;
/*      */ import org.apache.commons.io.output.StringBuilderWriter;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class IOUtils
/*      */ {
/*      */   public static final int EOF = -1;
/*      */   public static final char DIR_SEPARATOR_UNIX = '/';
/*      */   public static final char DIR_SEPARATOR_WINDOWS = '\\';
/*  122 */   public static final char DIR_SEPARATOR = File.separatorChar;
/*      */   
/*      */   public static final String LINE_SEPARATOR_UNIX = "\n";
/*      */   
/*      */   public static final String LINE_SEPARATOR_WINDOWS = "\r\n";
/*      */   
/*      */   public static final String LINE_SEPARATOR;
/*      */   
/*      */   private static final int DEFAULT_BUFFER_SIZE = 4096;
/*      */   
/*      */   private static final int SKIP_BUFFER_SIZE = 2048;
/*      */   
/*      */   private static char[] SKIP_CHAR_BUFFER;
/*      */   private static byte[] SKIP_BYTE_BUFFER;
/*      */   
/*      */   static {
/*  138 */     StringBuilderWriter buf = new StringBuilderWriter(4);
/*  139 */     PrintWriter out = new PrintWriter((Writer)buf);
/*  140 */     out.println();
/*  141 */     LINE_SEPARATOR = buf.toString();
/*  142 */     out.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void close(URLConnection conn) {
/*  188 */     if (conn instanceof HttpURLConnection) {
/*  189 */       ((HttpURLConnection)conn).disconnect();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void closeQuietly(Reader input) {
/*  217 */     closeQuietly(input);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void closeQuietly(Writer output) {
/*  243 */     closeQuietly(output);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void closeQuietly(InputStream input) {
/*  270 */     closeQuietly(input);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void closeQuietly(OutputStream output) {
/*  298 */     closeQuietly(output);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void closeQuietly(Closeable closeable) {
/*      */     try {
/*  338 */       if (closeable != null) {
/*  339 */         closeable.close();
/*      */       }
/*  341 */     } catch (IOException iOException) {}
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void closeQuietly(Closeable... closeables) {
/*  390 */     if (closeables == null) {
/*      */       return;
/*      */     }
/*  393 */     for (Closeable closeable : closeables) {
/*  394 */       closeQuietly(closeable);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void closeQuietly(Socket sock) {
/*  422 */     if (sock != null) {
/*      */       try {
/*  424 */         sock.close();
/*  425 */       } catch (IOException iOException) {}
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void closeQuietly(Selector selector) {
/*  455 */     if (selector != null) {
/*      */       try {
/*  457 */         selector.close();
/*  458 */       } catch (IOException iOException) {}
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void closeQuietly(ServerSocket sock) {
/*  488 */     if (sock != null) {
/*      */       try {
/*  490 */         sock.close();
/*  491 */       } catch (IOException iOException) {}
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static InputStream toBufferedInputStream(InputStream input) throws IOException {
/*  519 */     return ByteArrayOutputStream.toBufferedInputStream(input);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static InputStream toBufferedInputStream(InputStream input, int size) throws IOException {
/*  545 */     return ByteArrayOutputStream.toBufferedInputStream(input, size);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BufferedReader toBufferedReader(Reader reader) {
/*  559 */     return (reader instanceof BufferedReader) ? (BufferedReader)reader : new BufferedReader(reader);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BufferedReader toBufferedReader(Reader reader, int size) {
/*  574 */     return (reader instanceof BufferedReader) ? (BufferedReader)reader : new BufferedReader(reader, size);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BufferedReader buffer(Reader reader) {
/*  587 */     return (reader instanceof BufferedReader) ? (BufferedReader)reader : new BufferedReader(reader);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BufferedReader buffer(Reader reader, int size) {
/*  601 */     return (reader instanceof BufferedReader) ? (BufferedReader)reader : new BufferedReader(reader, size);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BufferedWriter buffer(Writer writer) {
/*  614 */     return (writer instanceof BufferedWriter) ? (BufferedWriter)writer : new BufferedWriter(writer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BufferedWriter buffer(Writer writer, int size) {
/*  628 */     return (writer instanceof BufferedWriter) ? (BufferedWriter)writer : new BufferedWriter(writer, size);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BufferedOutputStream buffer(OutputStream outputStream) {
/*  642 */     if (outputStream == null) {
/*  643 */       throw new NullPointerException();
/*      */     }
/*  645 */     return (outputStream instanceof BufferedOutputStream) ? (BufferedOutputStream)outputStream : new BufferedOutputStream(outputStream);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BufferedOutputStream buffer(OutputStream outputStream, int size) {
/*  661 */     if (outputStream == null) {
/*  662 */       throw new NullPointerException();
/*      */     }
/*  664 */     return (outputStream instanceof BufferedOutputStream) ? (BufferedOutputStream)outputStream : new BufferedOutputStream(outputStream, size);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BufferedInputStream buffer(InputStream inputStream) {
/*  679 */     if (inputStream == null) {
/*  680 */       throw new NullPointerException();
/*      */     }
/*  682 */     return (inputStream instanceof BufferedInputStream) ? (BufferedInputStream)inputStream : new BufferedInputStream(inputStream);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BufferedInputStream buffer(InputStream inputStream, int size) {
/*  698 */     if (inputStream == null) {
/*  699 */       throw new NullPointerException();
/*      */     }
/*  701 */     return (inputStream instanceof BufferedInputStream) ? (BufferedInputStream)inputStream : new BufferedInputStream(inputStream, size);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] toByteArray(InputStream input) throws IOException {
/*  720 */     ByteArrayOutputStream output = new ByteArrayOutputStream();
/*  721 */     copy(input, (OutputStream)output);
/*  722 */     return output.toByteArray();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] toByteArray(InputStream input, long size) throws IOException {
/*  744 */     if (size > 2147483647L) {
/*  745 */       throw new IllegalArgumentException("Size cannot be greater than Integer max value: " + size);
/*      */     }
/*      */     
/*  748 */     return toByteArray(input, (int)size);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] toByteArray(InputStream input, int size) throws IOException {
/*  766 */     if (size < 0) {
/*  767 */       throw new IllegalArgumentException("Size must be equal or greater than zero: " + size);
/*      */     }
/*      */     
/*  770 */     if (size == 0) {
/*  771 */       return new byte[0];
/*      */     }
/*      */     
/*  774 */     byte[] data = new byte[size];
/*  775 */     int offset = 0;
/*      */     
/*      */     int readed;
/*  778 */     while (offset < size && (readed = input.read(data, offset, size - offset)) != -1) {
/*  779 */       offset += readed;
/*      */     }
/*      */     
/*  782 */     if (offset != size) {
/*  783 */       throw new IOException("Unexpected readed size. current: " + offset + ", excepted: " + size);
/*      */     }
/*      */     
/*  786 */     return data;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static byte[] toByteArray(Reader input) throws IOException {
/*  804 */     return toByteArray(input, Charset.defaultCharset());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] toByteArray(Reader input, Charset encoding) throws IOException {
/*  822 */     ByteArrayOutputStream output = new ByteArrayOutputStream();
/*  823 */     copy(input, (OutputStream)output, encoding);
/*  824 */     return output.toByteArray();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] toByteArray(Reader input, String encoding) throws IOException {
/*  848 */     return toByteArray(input, Charsets.toCharset(encoding));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static byte[] toByteArray(String input) throws IOException {
/*  866 */     return input.getBytes(Charset.defaultCharset());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] toByteArray(URI uri) throws IOException {
/*  879 */     return toByteArray(uri.toURL());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] toByteArray(URL url) throws IOException {
/*  892 */     URLConnection conn = url.openConnection();
/*      */     try {
/*  894 */       return toByteArray(conn);
/*      */     } finally {
/*  896 */       close(conn);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] toByteArray(URLConnection urlConn) throws IOException {
/*  910 */     InputStream inputStream = urlConn.getInputStream();
/*      */     try {
/*  912 */       return toByteArray(inputStream);
/*      */     } finally {
/*  914 */       inputStream.close();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static char[] toCharArray(InputStream is) throws IOException {
/*  937 */     return toCharArray(is, Charset.defaultCharset());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char[] toCharArray(InputStream is, Charset encoding) throws IOException {
/*  956 */     CharArrayWriter output = new CharArrayWriter();
/*  957 */     copy(is, output, encoding);
/*  958 */     return output.toCharArray();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char[] toCharArray(InputStream is, String encoding) throws IOException {
/*  982 */     return toCharArray(is, Charsets.toCharset(encoding));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char[] toCharArray(Reader input) throws IOException {
/*  998 */     CharArrayWriter sw = new CharArrayWriter();
/*  999 */     copy(input, sw);
/* 1000 */     return sw.toCharArray();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static String toString(InputStream input) throws IOException {
/* 1021 */     return toString(input, Charset.defaultCharset());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(InputStream input, Charset encoding) throws IOException {
/* 1040 */     StringBuilderWriter sw = new StringBuilderWriter();
/* 1041 */     copy(input, (Writer)sw, encoding);
/* 1042 */     return sw.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(InputStream input, String encoding) throws IOException {
/* 1066 */     return toString(input, Charsets.toCharset(encoding));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(Reader input) throws IOException {
/* 1081 */     StringBuilderWriter sw = new StringBuilderWriter();
/* 1082 */     copy(input, (Writer)sw);
/* 1083 */     return sw.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static String toString(URI uri) throws IOException {
/* 1097 */     return toString(uri, Charset.defaultCharset());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(URI uri, Charset encoding) throws IOException {
/* 1110 */     return toString(uri.toURL(), Charsets.toCharset(encoding));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(URI uri, String encoding) throws IOException {
/* 1126 */     return toString(uri, Charsets.toCharset(encoding));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static String toString(URL url) throws IOException {
/* 1140 */     return toString(url, Charset.defaultCharset());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(URL url, Charset encoding) throws IOException {
/* 1153 */     InputStream inputStream = url.openStream();
/*      */     try {
/* 1155 */       return toString(inputStream, encoding);
/*      */     } finally {
/* 1157 */       inputStream.close();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(URL url, String encoding) throws IOException {
/* 1174 */     return toString(url, Charsets.toCharset(encoding));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static String toString(byte[] input) throws IOException {
/* 1190 */     return new String(input, Charset.defaultCharset());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(byte[] input, String encoding) throws IOException {
/* 1207 */     return new String(input, Charsets.toCharset(encoding));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static List<String> readLines(InputStream input) throws IOException {
/* 1229 */     return readLines(input, Charset.defaultCharset());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static List<String> readLines(InputStream input, Charset encoding) throws IOException {
/* 1247 */     InputStreamReader reader = new InputStreamReader(input, Charsets.toCharset(encoding));
/* 1248 */     return readLines(reader);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static List<String> readLines(InputStream input, String encoding) throws IOException {
/* 1272 */     return readLines(input, Charsets.toCharset(encoding));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static List<String> readLines(Reader input) throws IOException {
/* 1289 */     BufferedReader reader = toBufferedReader(input);
/* 1290 */     List<String> list = new ArrayList<String>();
/* 1291 */     String line = reader.readLine();
/* 1292 */     while (line != null) {
/* 1293 */       list.add(line);
/* 1294 */       line = reader.readLine();
/*      */     } 
/* 1296 */     return list;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static LineIterator lineIterator(Reader reader) {
/* 1330 */     return new LineIterator(reader);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static LineIterator lineIterator(InputStream input, Charset encoding) throws IOException {
/* 1364 */     return new LineIterator(new InputStreamReader(input, Charsets.toCharset(encoding)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static LineIterator lineIterator(InputStream input, String encoding) throws IOException {
/* 1401 */     return lineIterator(input, Charsets.toCharset(encoding));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static InputStream toInputStream(CharSequence input) {
/* 1417 */     return toInputStream(input, Charset.defaultCharset());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static InputStream toInputStream(CharSequence input, Charset encoding) {
/* 1430 */     return toInputStream(input.toString(), encoding);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static InputStream toInputStream(CharSequence input, String encoding) throws IOException {
/* 1450 */     return toInputStream(input, Charsets.toCharset(encoding));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static InputStream toInputStream(String input) {
/* 1466 */     return toInputStream(input, Charset.defaultCharset());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static InputStream toInputStream(String input, Charset encoding) {
/* 1479 */     return new ByteArrayInputStream(input.getBytes(Charsets.toCharset(encoding)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static InputStream toInputStream(String input, String encoding) throws IOException {
/* 1499 */     byte[] bytes = input.getBytes(Charsets.toCharset(encoding));
/* 1500 */     return new ByteArrayInputStream(bytes);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(byte[] data, OutputStream output) throws IOException {
/* 1518 */     if (data != null) {
/* 1519 */       output.write(data);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void writeChunked(byte[] data, OutputStream output) throws IOException {
/* 1537 */     if (data != null) {
/* 1538 */       int bytes = data.length;
/* 1539 */       int offset = 0;
/* 1540 */       while (bytes > 0) {
/* 1541 */         int chunk = Math.min(bytes, 4096);
/* 1542 */         output.write(data, offset, chunk);
/* 1543 */         bytes -= chunk;
/* 1544 */         offset += chunk;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void write(byte[] data, Writer output) throws IOException {
/* 1565 */     write(data, output, Charset.defaultCharset());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(byte[] data, Writer output, Charset encoding) throws IOException {
/* 1583 */     if (data != null) {
/* 1584 */       output.write(new String(data, Charsets.toCharset(encoding)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(byte[] data, Writer output, String encoding) throws IOException {
/* 1609 */     write(data, output, Charsets.toCharset(encoding));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(char[] data, Writer output) throws IOException {
/* 1626 */     if (data != null) {
/* 1627 */       output.write(data);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void writeChunked(char[] data, Writer output) throws IOException {
/* 1644 */     if (data != null) {
/* 1645 */       int bytes = data.length;
/* 1646 */       int offset = 0;
/* 1647 */       while (bytes > 0) {
/* 1648 */         int chunk = Math.min(bytes, 4096);
/* 1649 */         output.write(data, offset, chunk);
/* 1650 */         bytes -= chunk;
/* 1651 */         offset += chunk;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void write(char[] data, OutputStream output) throws IOException {
/* 1674 */     write(data, output, Charset.defaultCharset());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(char[] data, OutputStream output, Charset encoding) throws IOException {
/* 1693 */     if (data != null) {
/* 1694 */       output.write((new String(data)).getBytes(Charsets.toCharset(encoding)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(char[] data, OutputStream output, String encoding) throws IOException {
/* 1720 */     write(data, output, Charsets.toCharset(encoding));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(CharSequence data, Writer output) throws IOException {
/* 1736 */     if (data != null) {
/* 1737 */       write(data.toString(), output);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void write(CharSequence data, OutputStream output) throws IOException {
/* 1758 */     write(data, output, Charset.defaultCharset());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(CharSequence data, OutputStream output, Charset encoding) throws IOException {
/* 1776 */     if (data != null) {
/* 1777 */       write(data.toString(), output, encoding);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(CharSequence data, OutputStream output, String encoding) throws IOException {
/* 1801 */     write(data, output, Charsets.toCharset(encoding));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(String data, Writer output) throws IOException {
/* 1817 */     if (data != null) {
/* 1818 */       output.write(data);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void write(String data, OutputStream output) throws IOException {
/* 1839 */     write(data, output, Charset.defaultCharset());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(String data, OutputStream output, Charset encoding) throws IOException {
/* 1856 */     if (data != null) {
/* 1857 */       output.write(data.getBytes(Charsets.toCharset(encoding)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void write(String data, OutputStream output, String encoding) throws IOException {
/* 1881 */     write(data, output, Charsets.toCharset(encoding));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void write(StringBuffer data, Writer output) throws IOException {
/* 1900 */     if (data != null) {
/* 1901 */       output.write(data.toString());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void write(StringBuffer data, OutputStream output) throws IOException {
/* 1922 */     write(data, output, (String)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void write(StringBuffer data, OutputStream output, String encoding) throws IOException {
/* 1947 */     if (data != null) {
/* 1948 */       output.write(data.toString().getBytes(Charsets.toCharset(encoding)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void writeLines(Collection<?> lines, String lineEnding, OutputStream output) throws IOException {
/* 1971 */     writeLines(lines, lineEnding, output, Charset.defaultCharset());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void writeLines(Collection<?> lines, String lineEnding, OutputStream output, Charset encoding) throws IOException {
/* 1989 */     if (lines == null) {
/*      */       return;
/*      */     }
/* 1992 */     if (lineEnding == null) {
/* 1993 */       lineEnding = LINE_SEPARATOR;
/*      */     }
/* 1995 */     Charset cs = Charsets.toCharset(encoding);
/* 1996 */     for (Object line : lines) {
/* 1997 */       if (line != null) {
/* 1998 */         output.write(line.toString().getBytes(cs));
/*      */       }
/* 2000 */       output.write(lineEnding.getBytes(cs));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void writeLines(Collection<?> lines, String lineEnding, OutputStream output, String encoding) throws IOException {
/* 2025 */     writeLines(lines, lineEnding, output, Charsets.toCharset(encoding));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void writeLines(Collection<?> lines, String lineEnding, Writer writer) throws IOException {
/* 2041 */     if (lines == null) {
/*      */       return;
/*      */     }
/* 2044 */     if (lineEnding == null) {
/* 2045 */       lineEnding = LINE_SEPARATOR;
/*      */     }
/* 2047 */     for (Object line : lines) {
/* 2048 */       if (line != null) {
/* 2049 */         writer.write(line.toString());
/*      */       }
/* 2051 */       writer.write(lineEnding);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int copy(InputStream input, OutputStream output) throws IOException {
/* 2078 */     long count = copyLarge(input, output);
/* 2079 */     if (count > 2147483647L) {
/* 2080 */       return -1;
/*      */     }
/* 2082 */     return (int)count;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long copy(InputStream input, OutputStream output, int bufferSize) throws IOException {
/* 2102 */     return copyLarge(input, output, new byte[bufferSize]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long copyLarge(InputStream input, OutputStream output) throws IOException {
/* 2123 */     return copy(input, output, 4096);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long copyLarge(InputStream input, OutputStream output, byte[] buffer) throws IOException {
/* 2144 */     long count = 0L;
/*      */     int n;
/* 2146 */     while (-1 != (n = input.read(buffer))) {
/* 2147 */       output.write(buffer, 0, n);
/* 2148 */       count += n;
/*      */     } 
/* 2150 */     return count;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long copyLarge(InputStream input, OutputStream output, long inputOffset, long length) throws IOException {
/* 2179 */     return copyLarge(input, output, inputOffset, length, new byte[4096]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long copyLarge(InputStream input, OutputStream output, long inputOffset, long length, byte[] buffer) throws IOException {
/* 2208 */     if (inputOffset > 0L) {
/* 2209 */       skipFully(input, inputOffset);
/*      */     }
/* 2211 */     if (length == 0L) {
/* 2212 */       return 0L;
/*      */     }
/* 2214 */     int bufferLength = buffer.length;
/* 2215 */     int bytesToRead = bufferLength;
/* 2216 */     if (length > 0L && length < bufferLength) {
/* 2217 */       bytesToRead = (int)length;
/*      */     }
/*      */     
/* 2220 */     long totalRead = 0L; int read;
/* 2221 */     while (bytesToRead > 0 && -1 != (read = input.read(buffer, 0, bytesToRead))) {
/* 2222 */       output.write(buffer, 0, read);
/* 2223 */       totalRead += read;
/* 2224 */       if (length > 0L)
/*      */       {
/* 2226 */         bytesToRead = (int)Math.min(length - totalRead, bufferLength);
/*      */       }
/*      */     } 
/* 2229 */     return totalRead;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void copy(InputStream input, Writer output) throws IOException {
/* 2251 */     copy(input, output, Charset.defaultCharset());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void copy(InputStream input, Writer output, Charset inputEncoding) throws IOException {
/* 2272 */     InputStreamReader in = new InputStreamReader(input, Charsets.toCharset(inputEncoding));
/* 2273 */     copy(in, output);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void copy(InputStream input, Writer output, String inputEncoding) throws IOException {
/* 2300 */     copy(input, output, Charsets.toCharset(inputEncoding));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int copy(Reader input, Writer output) throws IOException {
/* 2325 */     long count = copyLarge(input, output);
/* 2326 */     if (count > 2147483647L) {
/* 2327 */       return -1;
/*      */     }
/* 2329 */     return (int)count;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long copyLarge(Reader input, Writer output) throws IOException {
/* 2348 */     return copyLarge(input, output, new char[4096]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long copyLarge(Reader input, Writer output, char[] buffer) throws IOException {
/* 2367 */     long count = 0L;
/*      */     int n;
/* 2369 */     while (-1 != (n = input.read(buffer))) {
/* 2370 */       output.write(buffer, 0, n);
/* 2371 */       count += n;
/*      */     } 
/* 2373 */     return count;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long copyLarge(Reader input, Writer output, long inputOffset, long length) throws IOException {
/* 2397 */     return copyLarge(input, output, inputOffset, length, new char[4096]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long copyLarge(Reader input, Writer output, long inputOffset, long length, char[] buffer) throws IOException {
/* 2422 */     if (inputOffset > 0L) {
/* 2423 */       skipFully(input, inputOffset);
/*      */     }
/* 2425 */     if (length == 0L) {
/* 2426 */       return 0L;
/*      */     }
/* 2428 */     int bytesToRead = buffer.length;
/* 2429 */     if (length > 0L && length < buffer.length) {
/* 2430 */       bytesToRead = (int)length;
/*      */     }
/*      */     
/* 2433 */     long totalRead = 0L; int read;
/* 2434 */     while (bytesToRead > 0 && -1 != (read = input.read(buffer, 0, bytesToRead))) {
/* 2435 */       output.write(buffer, 0, read);
/* 2436 */       totalRead += read;
/* 2437 */       if (length > 0L)
/*      */       {
/* 2439 */         bytesToRead = (int)Math.min(length - totalRead, buffer.length);
/*      */       }
/*      */     } 
/* 2442 */     return totalRead;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void copy(Reader input, OutputStream output) throws IOException {
/* 2468 */     copy(input, output, Charset.defaultCharset());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void copy(Reader input, OutputStream output, Charset outputEncoding) throws IOException {
/* 2496 */     OutputStreamWriter out = new OutputStreamWriter(output, Charsets.toCharset(outputEncoding));
/* 2497 */     copy(input, out);
/*      */ 
/*      */     
/* 2500 */     out.flush();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void copy(Reader input, OutputStream output, String outputEncoding) throws IOException {
/* 2531 */     copy(input, output, Charsets.toCharset(outputEncoding));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean contentEquals(InputStream input1, InputStream input2) throws IOException {
/* 2553 */     if (input1 == input2) {
/* 2554 */       return true;
/*      */     }
/* 2556 */     if (!(input1 instanceof BufferedInputStream)) {
/* 2557 */       input1 = new BufferedInputStream(input1);
/*      */     }
/* 2559 */     if (!(input2 instanceof BufferedInputStream)) {
/* 2560 */       input2 = new BufferedInputStream(input2);
/*      */     }
/*      */     
/* 2563 */     int ch = input1.read();
/* 2564 */     while (-1 != ch) {
/* 2565 */       int i = input2.read();
/* 2566 */       if (ch != i) {
/* 2567 */         return false;
/*      */       }
/* 2569 */       ch = input1.read();
/*      */     } 
/*      */     
/* 2572 */     int ch2 = input2.read();
/* 2573 */     return (ch2 == -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean contentEquals(Reader input1, Reader input2) throws IOException {
/* 2593 */     if (input1 == input2) {
/* 2594 */       return true;
/*      */     }
/*      */     
/* 2597 */     input1 = toBufferedReader(input1);
/* 2598 */     input2 = toBufferedReader(input2);
/*      */     
/* 2600 */     int ch = input1.read();
/* 2601 */     while (-1 != ch) {
/* 2602 */       int i = input2.read();
/* 2603 */       if (ch != i) {
/* 2604 */         return false;
/*      */       }
/* 2606 */       ch = input1.read();
/*      */     } 
/*      */     
/* 2609 */     int ch2 = input2.read();
/* 2610 */     return (ch2 == -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean contentEqualsIgnoreEOL(Reader input1, Reader input2) throws IOException {
/* 2629 */     if (input1 == input2) {
/* 2630 */       return true;
/*      */     }
/* 2632 */     BufferedReader br1 = toBufferedReader(input1);
/* 2633 */     BufferedReader br2 = toBufferedReader(input2);
/*      */     
/* 2635 */     String line1 = br1.readLine();
/* 2636 */     String line2 = br2.readLine();
/* 2637 */     while (line1 != null && line2 != null && line1.equals(line2)) {
/* 2638 */       line1 = br1.readLine();
/* 2639 */       line2 = br2.readLine();
/*      */     } 
/* 2641 */     return (line1 == null) ? ((line2 == null)) : line1.equals(line2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long skip(InputStream input, long toSkip) throws IOException {
/* 2666 */     if (toSkip < 0L) {
/* 2667 */       throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2674 */     if (SKIP_BYTE_BUFFER == null) {
/* 2675 */       SKIP_BYTE_BUFFER = new byte[2048];
/*      */     }
/* 2677 */     long remain = toSkip;
/* 2678 */     while (remain > 0L) {
/*      */       
/* 2680 */       long n = input.read(SKIP_BYTE_BUFFER, 0, (int)Math.min(remain, 2048L));
/* 2681 */       if (n < 0L) {
/*      */         break;
/*      */       }
/* 2684 */       remain -= n;
/*      */     } 
/* 2686 */     return toSkip - remain;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long skip(ReadableByteChannel input, long toSkip) throws IOException {
/* 2702 */     if (toSkip < 0L) {
/* 2703 */       throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
/*      */     }
/* 2705 */     ByteBuffer skipByteBuffer = ByteBuffer.allocate((int)Math.min(toSkip, 2048L));
/* 2706 */     long remain = toSkip;
/* 2707 */     while (remain > 0L) {
/* 2708 */       skipByteBuffer.position(0);
/* 2709 */       skipByteBuffer.limit((int)Math.min(remain, 2048L));
/* 2710 */       int n = input.read(skipByteBuffer);
/* 2711 */       if (n == -1) {
/*      */         break;
/*      */       }
/* 2714 */       remain -= n;
/*      */     } 
/* 2716 */     return toSkip - remain;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long skip(Reader input, long toSkip) throws IOException {
/* 2741 */     if (toSkip < 0L) {
/* 2742 */       throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2749 */     if (SKIP_CHAR_BUFFER == null) {
/* 2750 */       SKIP_CHAR_BUFFER = new char[2048];
/*      */     }
/* 2752 */     long remain = toSkip;
/* 2753 */     while (remain > 0L) {
/*      */       
/* 2755 */       long n = input.read(SKIP_CHAR_BUFFER, 0, (int)Math.min(remain, 2048L));
/* 2756 */       if (n < 0L) {
/*      */         break;
/*      */       }
/* 2759 */       remain -= n;
/*      */     } 
/* 2761 */     return toSkip - remain;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void skipFully(InputStream input, long toSkip) throws IOException {
/* 2784 */     if (toSkip < 0L) {
/* 2785 */       throw new IllegalArgumentException("Bytes to skip must not be negative: " + toSkip);
/*      */     }
/* 2787 */     long skipped = skip(input, toSkip);
/* 2788 */     if (skipped != toSkip) {
/* 2789 */       throw new EOFException("Bytes to skip: " + toSkip + " actual: " + skipped);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void skipFully(ReadableByteChannel input, long toSkip) throws IOException {
/* 2804 */     if (toSkip < 0L) {
/* 2805 */       throw new IllegalArgumentException("Bytes to skip must not be negative: " + toSkip);
/*      */     }
/* 2807 */     long skipped = skip(input, toSkip);
/* 2808 */     if (skipped != toSkip) {
/* 2809 */       throw new EOFException("Bytes to skip: " + toSkip + " actual: " + skipped);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void skipFully(Reader input, long toSkip) throws IOException {
/* 2833 */     long skipped = skip(input, toSkip);
/* 2834 */     if (skipped != toSkip) {
/* 2835 */       throw new EOFException("Chars to skip: " + toSkip + " actual: " + skipped);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int read(Reader input, char[] buffer, int offset, int length) throws IOException {
/* 2856 */     if (length < 0) {
/* 2857 */       throw new IllegalArgumentException("Length must not be negative: " + length);
/*      */     }
/* 2859 */     int remaining = length;
/* 2860 */     while (remaining > 0) {
/* 2861 */       int location = length - remaining;
/* 2862 */       int count = input.read(buffer, offset + location, remaining);
/* 2863 */       if (-1 == count) {
/*      */         break;
/*      */       }
/* 2866 */       remaining -= count;
/*      */     } 
/* 2868 */     return length - remaining;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int read(Reader input, char[] buffer) throws IOException {
/* 2884 */     return read(input, buffer, 0, buffer.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int read(InputStream input, byte[] buffer, int offset, int length) throws IOException {
/* 2903 */     if (length < 0) {
/* 2904 */       throw new IllegalArgumentException("Length must not be negative: " + length);
/*      */     }
/* 2906 */     int remaining = length;
/* 2907 */     while (remaining > 0) {
/* 2908 */       int location = length - remaining;
/* 2909 */       int count = input.read(buffer, offset + location, remaining);
/* 2910 */       if (-1 == count) {
/*      */         break;
/*      */       }
/* 2913 */       remaining -= count;
/*      */     } 
/* 2915 */     return length - remaining;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int read(InputStream input, byte[] buffer) throws IOException {
/* 2931 */     return read(input, buffer, 0, buffer.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int read(ReadableByteChannel input, ByteBuffer buffer) throws IOException {
/* 2948 */     int length = buffer.remaining();
/* 2949 */     while (buffer.remaining() > 0) {
/* 2950 */       int count = input.read(buffer);
/* 2951 */       if (-1 == count) {
/*      */         break;
/*      */       }
/*      */     } 
/* 2955 */     return length - buffer.remaining();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void readFully(Reader input, char[] buffer, int offset, int length) throws IOException {
/* 2975 */     int actual = read(input, buffer, offset, length);
/* 2976 */     if (actual != length) {
/* 2977 */       throw new EOFException("Length to read: " + length + " actual: " + actual);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void readFully(Reader input, char[] buffer) throws IOException {
/* 2995 */     readFully(input, buffer, 0, buffer.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void readFully(InputStream input, byte[] buffer, int offset, int length) throws IOException {
/* 3015 */     int actual = read(input, buffer, offset, length);
/* 3016 */     if (actual != length) {
/* 3017 */       throw new EOFException("Length to read: " + length + " actual: " + actual);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void readFully(InputStream input, byte[] buffer) throws IOException {
/* 3035 */     readFully(input, buffer, 0, buffer.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] readFully(InputStream input, int length) throws IOException {
/* 3053 */     byte[] buffer = new byte[length];
/* 3054 */     readFully(input, buffer, 0, buffer.length);
/* 3055 */     return buffer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void readFully(ReadableByteChannel input, ByteBuffer buffer) throws IOException {
/* 3071 */     int expected = buffer.remaining();
/* 3072 */     int actual = read(input, buffer);
/* 3073 */     if (actual != expected)
/* 3074 */       throw new EOFException("Length to read: " + expected + " actual: " + actual); 
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\io\IOUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */