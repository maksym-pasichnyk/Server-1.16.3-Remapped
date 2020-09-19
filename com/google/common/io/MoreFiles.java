/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.TreeTraverser;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.channels.Channels;
/*     */ import java.nio.channels.SeekableByteChannel;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.file.DirectoryIteratorException;
/*     */ import java.nio.file.DirectoryStream;
/*     */ import java.nio.file.FileAlreadyExistsException;
/*     */ import java.nio.file.FileSystemException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.NoSuchFileException;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.SecureDirectoryStream;
/*     */ import java.nio.file.attribute.BasicFileAttributeView;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.nio.file.attribute.FileTime;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import javax.annotation.Nullable;
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
/*     */ @Beta
/*     */ @AndroidIncompatible
/*     */ @GwtIncompatible
/*     */ public final class MoreFiles
/*     */ {
/*     */   public static ByteSource asByteSource(Path path, OpenOption... options) {
/*  79 */     return new PathByteSource(path, options);
/*     */   }
/*     */   
/*     */   private static final class PathByteSource
/*     */     extends ByteSource {
/*  84 */     private static final LinkOption[] FOLLOW_LINKS = new LinkOption[0];
/*     */     
/*     */     private final Path path;
/*     */     private final OpenOption[] options;
/*     */     private final boolean followLinks;
/*     */     
/*     */     private PathByteSource(Path path, OpenOption... options) {
/*  91 */       this.path = (Path)Preconditions.checkNotNull(path);
/*  92 */       this.options = (OpenOption[])options.clone();
/*  93 */       this.followLinks = followLinks(this.options);
/*     */     }
/*     */ 
/*     */     
/*     */     private static boolean followLinks(OpenOption[] options) {
/*  98 */       for (OpenOption option : options) {
/*  99 */         if (option == LinkOption.NOFOLLOW_LINKS) {
/* 100 */           return false;
/*     */         }
/*     */       } 
/* 103 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream openStream() throws IOException {
/* 108 */       return Files.newInputStream(this.path, this.options);
/*     */     }
/*     */     
/*     */     private BasicFileAttributes readAttributes() throws IOException {
/* 112 */       (new LinkOption[1])[0] = LinkOption.NOFOLLOW_LINKS; return Files.readAttributes(this.path, BasicFileAttributes.class, this.followLinks ? FOLLOW_LINKS : new LinkOption[1]);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Optional<Long> sizeIfKnown() {
/*     */       BasicFileAttributes attrs;
/*     */       try {
/* 121 */         attrs = readAttributes();
/* 122 */       } catch (IOException e) {
/*     */         
/* 124 */         return Optional.absent();
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 129 */       if (attrs.isDirectory() || attrs.isSymbolicLink()) {
/* 130 */         return Optional.absent();
/*     */       }
/*     */       
/* 133 */       return Optional.of(Long.valueOf(attrs.size()));
/*     */     }
/*     */ 
/*     */     
/*     */     public long size() throws IOException {
/* 138 */       BasicFileAttributes attrs = readAttributes();
/*     */ 
/*     */ 
/*     */       
/* 142 */       if (attrs.isDirectory())
/* 143 */         throw new IOException("can't read: is a directory"); 
/* 144 */       if (attrs.isSymbolicLink()) {
/* 145 */         throw new IOException("can't read: is a symbolic link");
/*     */       }
/*     */       
/* 148 */       return attrs.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] read() throws IOException {
/* 153 */       try (SeekableByteChannel channel = Files.newByteChannel(this.path, this.options)) {
/* 154 */         return Files.readFile(
/* 155 */             Channels.newInputStream(channel), channel.size());
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 161 */       return "MoreFiles.asByteSource(" + this.path + ", " + Arrays.toString((Object[])this.options) + ")";
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
/*     */   public static ByteSink asByteSink(Path path, OpenOption... options) {
/* 176 */     return new PathByteSink(path, options);
/*     */   }
/*     */   
/*     */   private static final class PathByteSink
/*     */     extends ByteSink {
/*     */     private final Path path;
/*     */     private final OpenOption[] options;
/*     */     
/*     */     private PathByteSink(Path path, OpenOption... options) {
/* 185 */       this.path = (Path)Preconditions.checkNotNull(path);
/* 186 */       this.options = (OpenOption[])options.clone();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public OutputStream openStream() throws IOException {
/* 192 */       return Files.newOutputStream(this.path, this.options);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 197 */       return "MoreFiles.asByteSink(" + this.path + ", " + Arrays.toString((Object[])this.options) + ")";
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
/*     */   public static CharSource asCharSource(Path path, Charset charset, OpenOption... options) {
/* 211 */     return asByteSource(path, options).asCharSource(charset);
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
/*     */   public static CharSink asCharSink(Path path, Charset charset, OpenOption... options) {
/* 226 */     return asByteSink(path, options).asCharSink(charset);
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
/*     */   public static ImmutableList<Path> listFiles(Path dir) throws IOException {
/* 238 */     try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
/* 239 */       return ImmutableList.copyOf(stream);
/* 240 */     } catch (DirectoryIteratorException e) {
/* 241 */       throw e.getCause();
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
/*     */   public static TreeTraverser<Path> directoryTreeTraverser() {
/* 261 */     return DirectoryTreeTraverser.INSTANCE;
/*     */   }
/*     */   
/*     */   private static final class DirectoryTreeTraverser
/*     */     extends TreeTraverser<Path> {
/* 266 */     private static final DirectoryTreeTraverser INSTANCE = new DirectoryTreeTraverser();
/*     */ 
/*     */     
/*     */     public Iterable<Path> children(Path dir) {
/* 270 */       if (Files.isDirectory(dir, new LinkOption[] { LinkOption.NOFOLLOW_LINKS })) {
/*     */         try {
/* 272 */           return (Iterable<Path>)MoreFiles.listFiles(dir);
/* 273 */         } catch (IOException e) {
/*     */           
/* 275 */           throw new DirectoryIteratorException(e);
/*     */         } 
/*     */       }
/* 278 */       return (Iterable<Path>)ImmutableList.of();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Predicate<Path> isDirectory(LinkOption... options) {
/* 287 */     final LinkOption[] optionsCopy = (LinkOption[])options.clone();
/* 288 */     return new Predicate<Path>()
/*     */       {
/*     */         public boolean apply(Path input) {
/* 291 */           return Files.isDirectory(input, optionsCopy);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 296 */           return "MoreFiles.isDirectory(" + Arrays.toString((Object[])optionsCopy) + ")";
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Predicate<Path> isRegularFile(LinkOption... options) {
/* 306 */     final LinkOption[] optionsCopy = (LinkOption[])options.clone();
/* 307 */     return new Predicate<Path>()
/*     */       {
/*     */         public boolean apply(Path input) {
/* 310 */           return Files.isRegularFile(input, optionsCopy);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 315 */           return "MoreFiles.isRegularFile(" + Arrays.toString((Object[])optionsCopy) + ")";
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void touch(Path path) throws IOException {
/* 325 */     Preconditions.checkNotNull(path);
/*     */     
/*     */     try {
/* 328 */       Files.setLastModifiedTime(path, FileTime.fromMillis(System.currentTimeMillis()));
/* 329 */     } catch (NoSuchFileException e) {
/*     */       try {
/* 331 */         Files.createFile(path, (FileAttribute<?>[])new FileAttribute[0]);
/* 332 */       } catch (FileAlreadyExistsException fileAlreadyExistsException) {}
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
/*     */   public static void createParentDirectories(Path path, FileAttribute<?>... attrs) throws IOException {
/* 357 */     Path normalizedAbsolutePath = path.toAbsolutePath().normalize();
/* 358 */     Path parent = normalizedAbsolutePath.getParent();
/* 359 */     if (parent == null) {
/*     */       return;
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
/* 371 */     if (!Files.isDirectory(parent, new LinkOption[0])) {
/* 372 */       Files.createDirectories(parent, attrs);
/* 373 */       if (!Files.isDirectory(parent, new LinkOption[0])) {
/* 374 */         throw new IOException("Unable to create parent directories of " + path);
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
/*     */   public static String getFileExtension(Path path) {
/* 392 */     Path name = path.getFileName();
/*     */ 
/*     */     
/* 395 */     if (name == null) {
/* 396 */       return "";
/*     */     }
/*     */     
/* 399 */     String fileName = name.toString();
/* 400 */     int dotIndex = fileName.lastIndexOf('.');
/* 401 */     return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getNameWithoutExtension(Path path) {
/* 410 */     Path name = path.getFileName();
/*     */ 
/*     */     
/* 413 */     if (name == null) {
/* 414 */       return "";
/*     */     }
/*     */     
/* 417 */     String fileName = name.toString();
/* 418 */     int dotIndex = fileName.lastIndexOf('.');
/* 419 */     return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
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
/*     */   public static void deleteRecursively(Path path, RecursiveDeleteOption... options) throws IOException {
/* 455 */     Path parentPath = getParentPath(path);
/* 456 */     if (parentPath == null) {
/* 457 */       throw new FileSystemException(path.toString(), null, "can't delete recursively");
/*     */     }
/*     */     
/* 460 */     Collection<IOException> exceptions = null;
/*     */     try {
/* 462 */       boolean sdsSupported = false;
/* 463 */       try (DirectoryStream<Path> parent = Files.newDirectoryStream(parentPath)) {
/* 464 */         if (parent instanceof SecureDirectoryStream) {
/* 465 */           sdsSupported = true;
/* 466 */           exceptions = deleteRecursivelySecure((SecureDirectoryStream<Path>)parent, path
/* 467 */               .getFileName());
/*     */         } 
/*     */       } 
/*     */       
/* 471 */       if (!sdsSupported) {
/* 472 */         checkAllowsInsecure(path, options);
/* 473 */         exceptions = deleteRecursivelyInsecure(path);
/*     */       } 
/* 475 */     } catch (IOException e) {
/* 476 */       if (exceptions == null) {
/* 477 */         throw e;
/*     */       }
/* 479 */       exceptions.add(e);
/*     */     } 
/*     */ 
/*     */     
/* 483 */     if (exceptions != null) {
/* 484 */       throwDeleteFailed(path, exceptions);
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
/*     */   public static void deleteDirectoryContents(Path path, RecursiveDeleteOption... options) throws IOException {
/* 525 */     Collection<IOException> exceptions = null;
/* 526 */     try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
/* 527 */       if (stream instanceof SecureDirectoryStream) {
/* 528 */         SecureDirectoryStream<Path> sds = (SecureDirectoryStream<Path>)stream;
/* 529 */         exceptions = deleteDirectoryContentsSecure(sds);
/*     */       } else {
/* 531 */         checkAllowsInsecure(path, options);
/* 532 */         exceptions = deleteDirectoryContentsInsecure(stream);
/*     */       } 
/* 534 */     } catch (IOException e) {
/* 535 */       if (exceptions == null) {
/* 536 */         throw e;
/*     */       }
/* 538 */       exceptions.add(e);
/*     */     } 
/*     */ 
/*     */     
/* 542 */     if (exceptions != null) {
/* 543 */       throwDeleteFailed(path, exceptions);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static Collection<IOException> deleteRecursivelySecure(SecureDirectoryStream<Path> dir, Path path) {
/* 554 */     Collection<IOException> exceptions = null;
/*     */     try {
/* 556 */       if (isDirectory(dir, path, new LinkOption[] { LinkOption.NOFOLLOW_LINKS })) {
/* 557 */         try (SecureDirectoryStream<Path> childDir = dir.newDirectoryStream(path, new LinkOption[] { LinkOption.NOFOLLOW_LINKS })) {
/* 558 */           exceptions = deleteDirectoryContentsSecure(childDir);
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 563 */         if (exceptions == null) {
/* 564 */           dir.deleteDirectory(path);
/*     */         }
/*     */       } else {
/* 567 */         dir.deleteFile(path);
/*     */       } 
/*     */       
/* 570 */       return exceptions;
/* 571 */     } catch (IOException e) {
/* 572 */       return addException(exceptions, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static Collection<IOException> deleteDirectoryContentsSecure(SecureDirectoryStream<Path> dir) {
/* 583 */     Collection<IOException> exceptions = null;
/*     */     try {
/* 585 */       for (Path path : dir) {
/* 586 */         exceptions = concat(exceptions, deleteRecursivelySecure(dir, path.getFileName()));
/*     */       }
/*     */       
/* 589 */       return exceptions;
/* 590 */     } catch (DirectoryIteratorException e) {
/* 591 */       return addException(exceptions, e.getCause());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static Collection<IOException> deleteRecursivelyInsecure(Path path) {
/* 601 */     Collection<IOException> exceptions = null;
/*     */     try {
/* 603 */       if (Files.isDirectory(path, new LinkOption[] { LinkOption.NOFOLLOW_LINKS })) {
/* 604 */         try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
/* 605 */           exceptions = deleteDirectoryContentsInsecure(stream);
/*     */         } 
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 611 */       if (exceptions == null) {
/* 612 */         Files.delete(path);
/*     */       }
/*     */       
/* 615 */       return exceptions;
/* 616 */     } catch (IOException e) {
/* 617 */       return addException(exceptions, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static Collection<IOException> deleteDirectoryContentsInsecure(DirectoryStream<Path> dir) {
/* 629 */     Collection<IOException> exceptions = null;
/*     */     try {
/* 631 */       for (Path entry : dir) {
/* 632 */         exceptions = concat(exceptions, deleteRecursivelyInsecure(entry));
/*     */       }
/*     */       
/* 635 */       return exceptions;
/* 636 */     } catch (DirectoryIteratorException e) {
/* 637 */       return addException(exceptions, e.getCause());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static Path getParentPath(Path path) throws IOException {
/* 648 */     Path parent = path.getParent();
/*     */ 
/*     */     
/* 651 */     if (parent != null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 657 */       return parent;
/*     */     }
/*     */ 
/*     */     
/* 661 */     if (path.getNameCount() == 0)
/*     */     {
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
/* 673 */       return null;
/*     */     }
/*     */     
/* 676 */     return path.getFileSystem().getPath(".", new String[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void checkAllowsInsecure(Path path, RecursiveDeleteOption[] options) throws InsecureRecursiveDeleteException {
/* 685 */     if (!Arrays.<RecursiveDeleteOption>asList(options).contains(RecursiveDeleteOption.ALLOW_INSECURE)) {
/* 686 */       throw new InsecureRecursiveDeleteException(path.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isDirectory(SecureDirectoryStream<Path> dir, Path name, LinkOption... options) throws IOException {
/* 695 */     return ((BasicFileAttributeView)dir.<BasicFileAttributeView>getFileAttributeView(name, BasicFileAttributeView.class, options))
/* 696 */       .readAttributes()
/* 697 */       .isDirectory();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Collection<IOException> addException(@Nullable Collection<IOException> exceptions, IOException e) {
/* 706 */     if (exceptions == null) {
/* 707 */       exceptions = new ArrayList<>();
/*     */     }
/* 709 */     exceptions.add(e);
/* 710 */     return exceptions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static Collection<IOException> concat(@Nullable Collection<IOException> exceptions, @Nullable Collection<IOException> other) {
/* 721 */     if (exceptions == null)
/* 722 */       return other; 
/* 723 */     if (other != null) {
/* 724 */       exceptions.addAll(other);
/*     */     }
/* 726 */     return exceptions;
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
/*     */   private static void throwDeleteFailed(Path path, Collection<IOException> exceptions) throws FileSystemException {
/* 738 */     FileSystemException deleteFailed = new FileSystemException(path.toString(), null, "failed to delete one or more files; see suppressed exceptions for details");
/*     */     
/* 740 */     for (IOException e : exceptions) {
/* 741 */       deleteFailed.addSuppressed(e);
/*     */     }
/* 743 */     throw deleteFailed;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\io\MoreFiles.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */