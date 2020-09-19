/*     */ package com.google.common.net;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Ascii;
/*     */ import com.google.common.base.CharMatcher;
/*     */ import com.google.common.base.Charsets;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableListMultimap;
/*     */ import com.google.common.collect.ImmutableMultiset;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.ListMultimap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Multimap;
/*     */ import com.google.common.collect.Multimaps;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Nullable;
/*     */ import javax.annotation.concurrent.Immutable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @GwtCompatible
/*     */ @Immutable
/*     */ public final class MediaType
/*     */ {
/*     */   private static final String CHARSET_ATTRIBUTE = "charset";
/*  83 */   private static final ImmutableListMultimap<String, String> UTF_8_CONSTANT_PARAMETERS = ImmutableListMultimap.of("charset", Ascii.toLowerCase(Charsets.UTF_8.name()));
/*     */ 
/*     */ 
/*     */   
/*  87 */   private static final CharMatcher TOKEN_MATCHER = CharMatcher.ascii()
/*  88 */     .and(CharMatcher.javaIsoControl().negate())
/*  89 */     .and(CharMatcher.isNot(' '))
/*  90 */     .and(CharMatcher.noneOf("()<>@,;:\\\"/[]?="));
/*  91 */   private static final CharMatcher QUOTED_TEXT_MATCHER = CharMatcher.ascii().and(CharMatcher.noneOf("\"\\\r"));
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  96 */   private static final CharMatcher LINEAR_WHITE_SPACE = CharMatcher.anyOf(" \t\r\n");
/*     */   
/*     */   private static final String APPLICATION_TYPE = "application";
/*     */   
/*     */   private static final String AUDIO_TYPE = "audio";
/*     */   
/*     */   private static final String IMAGE_TYPE = "image";
/*     */   
/*     */   private static final String TEXT_TYPE = "text";
/*     */   private static final String VIDEO_TYPE = "video";
/*     */   private static final String WILDCARD = "*";
/* 107 */   private static final Map<MediaType, MediaType> KNOWN_TYPES = Maps.newHashMap();
/*     */   
/*     */   private static MediaType createConstant(String type, String subtype) {
/* 110 */     return addKnownType(new MediaType(type, subtype, ImmutableListMultimap.of()));
/*     */   }
/*     */   
/*     */   private static MediaType createConstantUtf8(String type, String subtype) {
/* 114 */     return addKnownType(new MediaType(type, subtype, UTF_8_CONSTANT_PARAMETERS));
/*     */   }
/*     */   
/*     */   private static MediaType addKnownType(MediaType mediaType) {
/* 118 */     KNOWN_TYPES.put(mediaType, mediaType);
/* 119 */     return mediaType;
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
/* 132 */   public static final MediaType ANY_TYPE = createConstant("*", "*");
/* 133 */   public static final MediaType ANY_TEXT_TYPE = createConstant("text", "*");
/* 134 */   public static final MediaType ANY_IMAGE_TYPE = createConstant("image", "*");
/* 135 */   public static final MediaType ANY_AUDIO_TYPE = createConstant("audio", "*");
/* 136 */   public static final MediaType ANY_VIDEO_TYPE = createConstant("video", "*");
/* 137 */   public static final MediaType ANY_APPLICATION_TYPE = createConstant("application", "*");
/*     */ 
/*     */ 
/*     */   
/* 141 */   public static final MediaType CACHE_MANIFEST_UTF_8 = createConstantUtf8("text", "cache-manifest");
/* 142 */   public static final MediaType CSS_UTF_8 = createConstantUtf8("text", "css");
/* 143 */   public static final MediaType CSV_UTF_8 = createConstantUtf8("text", "csv");
/* 144 */   public static final MediaType HTML_UTF_8 = createConstantUtf8("text", "html");
/* 145 */   public static final MediaType I_CALENDAR_UTF_8 = createConstantUtf8("text", "calendar");
/* 146 */   public static final MediaType PLAIN_TEXT_UTF_8 = createConstantUtf8("text", "plain");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 152 */   public static final MediaType TEXT_JAVASCRIPT_UTF_8 = createConstantUtf8("text", "javascript");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 159 */   public static final MediaType TSV_UTF_8 = createConstantUtf8("text", "tab-separated-values");
/* 160 */   public static final MediaType VCARD_UTF_8 = createConstantUtf8("text", "vcard");
/* 161 */   public static final MediaType WML_UTF_8 = createConstantUtf8("text", "vnd.wap.wml");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 167 */   public static final MediaType XML_UTF_8 = createConstantUtf8("text", "xml");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 174 */   public static final MediaType VTT_UTF_8 = createConstantUtf8("text", "vtt");
/*     */ 
/*     */   
/* 177 */   public static final MediaType BMP = createConstant("image", "bmp");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 187 */   public static final MediaType CRW = createConstant("image", "x-canon-crw");
/* 188 */   public static final MediaType GIF = createConstant("image", "gif");
/* 189 */   public static final MediaType ICO = createConstant("image", "vnd.microsoft.icon");
/* 190 */   public static final MediaType JPEG = createConstant("image", "jpeg");
/* 191 */   public static final MediaType PNG = createConstant("image", "png");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 209 */   public static final MediaType PSD = createConstant("image", "vnd.adobe.photoshop");
/* 210 */   public static final MediaType SVG_UTF_8 = createConstantUtf8("image", "svg+xml");
/* 211 */   public static final MediaType TIFF = createConstant("image", "tiff");
/* 212 */   public static final MediaType WEBP = createConstant("image", "webp");
/*     */ 
/*     */   
/* 215 */   public static final MediaType MP4_AUDIO = createConstant("audio", "mp4");
/* 216 */   public static final MediaType MPEG_AUDIO = createConstant("audio", "mpeg");
/* 217 */   public static final MediaType OGG_AUDIO = createConstant("audio", "ogg");
/* 218 */   public static final MediaType WEBM_AUDIO = createConstant("audio", "webm");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 226 */   public static final MediaType L24_AUDIO = createConstant("audio", "l24");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 234 */   public static final MediaType BASIC_AUDIO = createConstant("audio", "basic");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 242 */   public static final MediaType AAC_AUDIO = createConstant("audio", "aac");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 250 */   public static final MediaType VORBIS_AUDIO = createConstant("audio", "vorbis");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 259 */   public static final MediaType WMA_AUDIO = createConstant("audio", "x-ms-wma");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 268 */   public static final MediaType WAX_AUDIO = createConstant("audio", "x-ms-wax");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 276 */   public static final MediaType VND_REAL_AUDIO = createConstant("audio", "vnd.rn-realaudio");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 284 */   public static final MediaType VND_WAVE_AUDIO = createConstant("audio", "vnd.wave");
/*     */ 
/*     */   
/* 287 */   public static final MediaType MP4_VIDEO = createConstant("video", "mp4");
/* 288 */   public static final MediaType MPEG_VIDEO = createConstant("video", "mpeg");
/* 289 */   public static final MediaType OGG_VIDEO = createConstant("video", "ogg");
/* 290 */   public static final MediaType QUICKTIME = createConstant("video", "quicktime");
/* 291 */   public static final MediaType WEBM_VIDEO = createConstant("video", "webm");
/* 292 */   public static final MediaType WMV = createConstant("video", "x-ms-wmv");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 301 */   public static final MediaType FLV_VIDEO = createConstant("video", "x-flv");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 310 */   public static final MediaType THREE_GPP_VIDEO = createConstant("video", "3gpp");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 319 */   public static final MediaType THREE_GPP2_VIDEO = createConstant("video", "3gpp2");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 327 */   public static final MediaType APPLICATION_XML_UTF_8 = createConstantUtf8("application", "xml");
/* 328 */   public static final MediaType ATOM_UTF_8 = createConstantUtf8("application", "atom+xml");
/* 329 */   public static final MediaType BZIP2 = createConstant("application", "x-bzip2");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 336 */   public static final MediaType DART_UTF_8 = createConstantUtf8("application", "dart");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 344 */   public static final MediaType APPLE_PASSBOOK = createConstant("application", "vnd.apple.pkpass");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 354 */   public static final MediaType EOT = createConstant("application", "vnd.ms-fontobject");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 364 */   public static final MediaType EPUB = createConstant("application", "epub+zip");
/*     */   
/* 366 */   public static final MediaType FORM_DATA = createConstant("application", "x-www-form-urlencoded");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 374 */   public static final MediaType KEY_ARCHIVE = createConstant("application", "pkcs12");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 385 */   public static final MediaType APPLICATION_BINARY = createConstant("application", "binary");
/*     */   
/* 387 */   public static final MediaType GZIP = createConstant("application", "x-gzip");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 394 */   public static final MediaType JAVASCRIPT_UTF_8 = createConstantUtf8("application", "javascript");
/* 395 */   public static final MediaType JSON_UTF_8 = createConstantUtf8("application", "json");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 403 */   public static final MediaType MANIFEST_JSON_UTF_8 = createConstantUtf8("application", "manifest+json");
/* 404 */   public static final MediaType KML = createConstant("application", "vnd.google-earth.kml+xml");
/* 405 */   public static final MediaType KMZ = createConstant("application", "vnd.google-earth.kmz");
/* 406 */   public static final MediaType MBOX = createConstant("application", "mbox");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 415 */   public static final MediaType APPLE_MOBILE_CONFIG = createConstant("application", "x-apple-aspen-config");
/* 416 */   public static final MediaType MICROSOFT_EXCEL = createConstant("application", "vnd.ms-excel");
/*     */   
/* 418 */   public static final MediaType MICROSOFT_POWERPOINT = createConstant("application", "vnd.ms-powerpoint");
/* 419 */   public static final MediaType MICROSOFT_WORD = createConstant("application", "msword");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 428 */   public static final MediaType NACL_APPLICATION = createConstant("application", "x-nacl");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 438 */   public static final MediaType NACL_PORTABLE_APPLICATION = createConstant("application", "x-pnacl");
/*     */   
/* 440 */   public static final MediaType OCTET_STREAM = createConstant("application", "octet-stream");
/*     */   
/* 442 */   public static final MediaType OGG_CONTAINER = createConstant("application", "ogg");
/*     */   
/* 444 */   public static final MediaType OOXML_DOCUMENT = createConstant("application", "vnd.openxmlformats-officedocument.wordprocessingml.document");
/*     */ 
/*     */   
/* 447 */   public static final MediaType OOXML_PRESENTATION = createConstant("application", "vnd.openxmlformats-officedocument.presentationml.presentation");
/*     */ 
/*     */   
/* 450 */   public static final MediaType OOXML_SHEET = createConstant("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet");
/*     */   
/* 452 */   public static final MediaType OPENDOCUMENT_GRAPHICS = createConstant("application", "vnd.oasis.opendocument.graphics");
/*     */   
/* 454 */   public static final MediaType OPENDOCUMENT_PRESENTATION = createConstant("application", "vnd.oasis.opendocument.presentation");
/*     */   
/* 456 */   public static final MediaType OPENDOCUMENT_SPREADSHEET = createConstant("application", "vnd.oasis.opendocument.spreadsheet");
/*     */   
/* 458 */   public static final MediaType OPENDOCUMENT_TEXT = createConstant("application", "vnd.oasis.opendocument.text");
/* 459 */   public static final MediaType PDF = createConstant("application", "pdf");
/* 460 */   public static final MediaType POSTSCRIPT = createConstant("application", "postscript");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 466 */   public static final MediaType PROTOBUF = createConstant("application", "protobuf");
/*     */   
/* 468 */   public static final MediaType RDF_XML_UTF_8 = createConstantUtf8("application", "rdf+xml");
/* 469 */   public static final MediaType RTF_UTF_8 = createConstantUtf8("application", "rtf");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 479 */   public static final MediaType SFNT = createConstant("application", "font-sfnt");
/*     */   
/* 481 */   public static final MediaType SHOCKWAVE_FLASH = createConstant("application", "x-shockwave-flash");
/* 482 */   public static final MediaType SKETCHUP = createConstant("application", "vnd.sketchup.skp");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 494 */   public static final MediaType SOAP_XML_UTF_8 = createConstantUtf8("application", "soap+xml");
/* 495 */   public static final MediaType TAR = createConstant("application", "x-tar");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 504 */   public static final MediaType WOFF = createConstant("application", "font-woff");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 511 */   public static final MediaType WOFF2 = createConstant("application", "font-woff2");
/* 512 */   public static final MediaType XHTML_UTF_8 = createConstantUtf8("application", "xhtml+xml");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 519 */   public static final MediaType XRD_UTF_8 = createConstantUtf8("application", "xrd+xml");
/* 520 */   public static final MediaType ZIP = createConstant("application", "zip");
/*     */   
/*     */   private final String type;
/*     */   
/*     */   private final String subtype;
/*     */   
/*     */   private final ImmutableListMultimap<String, String> parameters;
/*     */   @LazyInit
/*     */   private String toString;
/*     */   @LazyInit
/*     */   private int hashCode;
/*     */   
/*     */   private MediaType(String type, String subtype, ImmutableListMultimap<String, String> parameters) {
/* 533 */     this.type = type;
/* 534 */     this.subtype = subtype;
/* 535 */     this.parameters = parameters;
/*     */   }
/*     */ 
/*     */   
/*     */   public String type() {
/* 540 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public String subtype() {
/* 545 */     return this.subtype;
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableListMultimap<String, String> parameters() {
/* 550 */     return this.parameters;
/*     */   }
/*     */   
/*     */   private Map<String, ImmutableMultiset<String>> parametersAsMap() {
/* 554 */     return Maps.transformValues((Map)this.parameters
/* 555 */         .asMap(), new Function<Collection<String>, ImmutableMultiset<String>>()
/*     */         {
/*     */           public ImmutableMultiset<String> apply(Collection<String> input)
/*     */           {
/* 559 */             return ImmutableMultiset.copyOf(input);
/*     */           }
/*     */         });
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
/*     */   public Optional<Charset> charset() {
/* 573 */     ImmutableSet<String> charsetValues = ImmutableSet.copyOf((Collection)this.parameters.get("charset"));
/* 574 */     switch (charsetValues.size()) {
/*     */       case 0:
/* 576 */         return Optional.absent();
/*     */       case 1:
/* 578 */         return Optional.of(Charset.forName((String)Iterables.getOnlyElement((Iterable)charsetValues)));
/*     */     } 
/* 580 */     throw new IllegalStateException("Multiple charset values defined: " + charsetValues);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MediaType withoutParameters() {
/* 589 */     return this.parameters.isEmpty() ? this : create(this.type, this.subtype);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MediaType withParameters(Multimap<String, String> parameters) {
/* 598 */     return create(this.type, this.subtype, parameters);
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
/*     */   public MediaType withParameter(String attribute, String value) {
/* 610 */     Preconditions.checkNotNull(attribute);
/* 611 */     Preconditions.checkNotNull(value);
/* 612 */     String normalizedAttribute = normalizeToken(attribute);
/* 613 */     ImmutableListMultimap.Builder<String, String> builder = ImmutableListMultimap.builder();
/* 614 */     for (UnmodifiableIterator<Map.Entry<String, String>> unmodifiableIterator = this.parameters.entries().iterator(); unmodifiableIterator.hasNext(); ) { Map.Entry<String, String> entry = unmodifiableIterator.next();
/* 615 */       String key = entry.getKey();
/* 616 */       if (!normalizedAttribute.equals(key)) {
/* 617 */         builder.put(key, entry.getValue());
/*     */       } }
/*     */     
/* 620 */     builder.put(normalizedAttribute, normalizeParameterValue(normalizedAttribute, value));
/* 621 */     MediaType mediaType = new MediaType(this.type, this.subtype, builder.build());
/*     */     
/* 623 */     return (MediaType)MoreObjects.firstNonNull(KNOWN_TYPES.get(mediaType), mediaType);
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
/*     */   public MediaType withCharset(Charset charset) {
/* 636 */     Preconditions.checkNotNull(charset);
/* 637 */     return withParameter("charset", charset.name());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasWildcard() {
/* 642 */     return ("*".equals(this.type) || "*".equals(this.subtype));
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
/*     */   public boolean is(MediaType mediaTypeRange) {
/* 672 */     return ((mediaTypeRange.type.equals("*") || mediaTypeRange.type.equals(this.type)) && (mediaTypeRange.subtype
/* 673 */       .equals("*") || mediaTypeRange.subtype.equals(this.subtype)) && this.parameters
/* 674 */       .entries().containsAll((Collection)mediaTypeRange.parameters.entries()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MediaType create(String type, String subtype) {
/* 684 */     return create(type, subtype, (Multimap<String, String>)ImmutableListMultimap.of());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static MediaType createApplicationType(String subtype) {
/* 693 */     return create("application", subtype);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static MediaType createAudioType(String subtype) {
/* 702 */     return create("audio", subtype);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static MediaType createImageType(String subtype) {
/* 711 */     return create("image", subtype);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static MediaType createTextType(String subtype) {
/* 720 */     return create("text", subtype);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static MediaType createVideoType(String subtype) {
/* 729 */     return create("video", subtype);
/*     */   }
/*     */ 
/*     */   
/*     */   private static MediaType create(String type, String subtype, Multimap<String, String> parameters) {
/* 734 */     Preconditions.checkNotNull(type);
/* 735 */     Preconditions.checkNotNull(subtype);
/* 736 */     Preconditions.checkNotNull(parameters);
/* 737 */     String normalizedType = normalizeToken(type);
/* 738 */     String normalizedSubtype = normalizeToken(subtype);
/* 739 */     Preconditions.checkArgument((
/* 740 */         !"*".equals(normalizedType) || "*".equals(normalizedSubtype)), "A wildcard type cannot be used with a non-wildcard subtype");
/*     */     
/* 742 */     ImmutableListMultimap.Builder<String, String> builder = ImmutableListMultimap.builder();
/* 743 */     for (Map.Entry<String, String> entry : (Iterable<Map.Entry<String, String>>)parameters.entries()) {
/* 744 */       String attribute = normalizeToken(entry.getKey());
/* 745 */       builder.put(attribute, normalizeParameterValue(attribute, entry.getValue()));
/*     */     } 
/* 747 */     MediaType mediaType = new MediaType(normalizedType, normalizedSubtype, builder.build());
/*     */     
/* 749 */     return (MediaType)MoreObjects.firstNonNull(KNOWN_TYPES.get(mediaType), mediaType);
/*     */   }
/*     */   
/*     */   private static String normalizeToken(String token) {
/* 753 */     Preconditions.checkArgument(TOKEN_MATCHER.matchesAllOf(token));
/* 754 */     return Ascii.toLowerCase(token);
/*     */   }
/*     */   
/*     */   private static String normalizeParameterValue(String attribute, String value) {
/* 758 */     return "charset".equals(attribute) ? Ascii.toLowerCase(value) : value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MediaType parse(String input) {
/* 767 */     Preconditions.checkNotNull(input);
/* 768 */     Tokenizer tokenizer = new Tokenizer(input);
/*     */     try {
/* 770 */       String type = tokenizer.consumeToken(TOKEN_MATCHER);
/* 771 */       tokenizer.consumeCharacter('/');
/* 772 */       String subtype = tokenizer.consumeToken(TOKEN_MATCHER);
/* 773 */       ImmutableListMultimap.Builder<String, String> parameters = ImmutableListMultimap.builder();
/* 774 */       while (tokenizer.hasMore()) {
/* 775 */         String value; tokenizer.consumeTokenIfPresent(LINEAR_WHITE_SPACE);
/* 776 */         tokenizer.consumeCharacter(';');
/* 777 */         tokenizer.consumeTokenIfPresent(LINEAR_WHITE_SPACE);
/* 778 */         String attribute = tokenizer.consumeToken(TOKEN_MATCHER);
/* 779 */         tokenizer.consumeCharacter('=');
/*     */         
/* 781 */         if ('"' == tokenizer.previewChar()) {
/* 782 */           tokenizer.consumeCharacter('"');
/* 783 */           StringBuilder valueBuilder = new StringBuilder();
/* 784 */           while ('"' != tokenizer.previewChar()) {
/* 785 */             if ('\\' == tokenizer.previewChar()) {
/* 786 */               tokenizer.consumeCharacter('\\');
/* 787 */               valueBuilder.append(tokenizer.consumeCharacter(CharMatcher.ascii())); continue;
/*     */             } 
/* 789 */             valueBuilder.append(tokenizer.consumeToken(QUOTED_TEXT_MATCHER));
/*     */           } 
/*     */           
/* 792 */           value = valueBuilder.toString();
/* 793 */           tokenizer.consumeCharacter('"');
/*     */         } else {
/* 795 */           value = tokenizer.consumeToken(TOKEN_MATCHER);
/*     */         } 
/* 797 */         parameters.put(attribute, value);
/*     */       } 
/* 799 */       return create(type, subtype, (Multimap<String, String>)parameters.build());
/* 800 */     } catch (IllegalStateException e) {
/* 801 */       throw new IllegalArgumentException("Could not parse '" + input + "'", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static final class Tokenizer {
/*     */     final String input;
/* 807 */     int position = 0;
/*     */     
/*     */     Tokenizer(String input) {
/* 810 */       this.input = input;
/*     */     }
/*     */     
/*     */     String consumeTokenIfPresent(CharMatcher matcher) {
/* 814 */       Preconditions.checkState(hasMore());
/* 815 */       int startPosition = this.position;
/* 816 */       this.position = matcher.negate().indexIn(this.input, startPosition);
/* 817 */       return hasMore() ? this.input.substring(startPosition, this.position) : this.input.substring(startPosition);
/*     */     }
/*     */     
/*     */     String consumeToken(CharMatcher matcher) {
/* 821 */       int startPosition = this.position;
/* 822 */       String token = consumeTokenIfPresent(matcher);
/* 823 */       Preconditions.checkState((this.position != startPosition));
/* 824 */       return token;
/*     */     }
/*     */     
/*     */     char consumeCharacter(CharMatcher matcher) {
/* 828 */       Preconditions.checkState(hasMore());
/* 829 */       char c = previewChar();
/* 830 */       Preconditions.checkState(matcher.matches(c));
/* 831 */       this.position++;
/* 832 */       return c;
/*     */     }
/*     */     
/*     */     char consumeCharacter(char c) {
/* 836 */       Preconditions.checkState(hasMore());
/* 837 */       Preconditions.checkState((previewChar() == c));
/* 838 */       this.position++;
/* 839 */       return c;
/*     */     }
/*     */     
/*     */     char previewChar() {
/* 843 */       Preconditions.checkState(hasMore());
/* 844 */       return this.input.charAt(this.position);
/*     */     }
/*     */     
/*     */     boolean hasMore() {
/* 848 */       return (this.position >= 0 && this.position < this.input.length());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object obj) {
/* 854 */     if (obj == this)
/* 855 */       return true; 
/* 856 */     if (obj instanceof MediaType) {
/* 857 */       MediaType that = (MediaType)obj;
/* 858 */       return (this.type.equals(that.type) && this.subtype
/* 859 */         .equals(that.subtype) && 
/*     */         
/* 861 */         parametersAsMap().equals(that.parametersAsMap()));
/*     */     } 
/* 863 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 870 */     int h = this.hashCode;
/* 871 */     if (h == 0) {
/* 872 */       h = Objects.hashCode(new Object[] { this.type, this.subtype, parametersAsMap() });
/* 873 */       this.hashCode = h;
/*     */     } 
/* 875 */     return h;
/*     */   }
/*     */   
/* 878 */   private static final Joiner.MapJoiner PARAMETER_JOINER = Joiner.on("; ").withKeyValueSeparator("=");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 887 */     String result = this.toString;
/* 888 */     if (result == null) {
/* 889 */       result = computeToString();
/* 890 */       this.toString = result;
/*     */     } 
/* 892 */     return result;
/*     */   }
/*     */   
/*     */   private String computeToString() {
/* 896 */     StringBuilder builder = (new StringBuilder()).append(this.type).append('/').append(this.subtype);
/* 897 */     if (!this.parameters.isEmpty()) {
/* 898 */       builder.append("; ");
/*     */       
/* 900 */       ListMultimap listMultimap = Multimaps.transformValues((ListMultimap)this.parameters, new Function<String, String>()
/*     */           {
/*     */             
/*     */             public String apply(String value)
/*     */             {
/* 905 */               return MediaType.TOKEN_MATCHER.matchesAllOf(value) ? value : MediaType.escapeAndQuote(value);
/*     */             }
/*     */           });
/* 908 */       PARAMETER_JOINER.appendTo(builder, listMultimap.entries());
/*     */     } 
/* 910 */     return builder.toString();
/*     */   }
/*     */   
/*     */   private static String escapeAndQuote(String value) {
/* 914 */     StringBuilder escaped = (new StringBuilder(value.length() + 16)).append('"');
/* 915 */     for (int i = 0; i < value.length(); i++) {
/* 916 */       char ch = value.charAt(i);
/* 917 */       if (ch == '\r' || ch == '\\' || ch == '"') {
/* 918 */         escaped.append('\\');
/*     */       }
/* 920 */       escaped.append(ch);
/*     */     } 
/* 922 */     return escaped.append('"').toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\net\MediaType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */