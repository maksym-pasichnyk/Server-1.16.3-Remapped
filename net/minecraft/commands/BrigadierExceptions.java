/*     */ package net.minecraft.commands;
/*     */ 
/*     */ public class BrigadierExceptions implements BuiltInExceptionProvider {
/*     */   private static final Dynamic2CommandExceptionType DOUBLE_TOO_SMALL;
/*     */   private static final Dynamic2CommandExceptionType DOUBLE_TOO_BIG;
/*     */   private static final Dynamic2CommandExceptionType FLOAT_TOO_SMALL;
/*     */   private static final Dynamic2CommandExceptionType FLOAT_TOO_BIG;
/*     */   
/*     */   static {
/*  10 */     DOUBLE_TOO_SMALL = new Dynamic2CommandExceptionType((debug0, debug1) -> new TranslatableComponent("argument.double.low", new Object[] { debug1, debug0 }));
/*  11 */     DOUBLE_TOO_BIG = new Dynamic2CommandExceptionType((debug0, debug1) -> new TranslatableComponent("argument.double.big", new Object[] { debug1, debug0 }));
/*     */     
/*  13 */     FLOAT_TOO_SMALL = new Dynamic2CommandExceptionType((debug0, debug1) -> new TranslatableComponent("argument.float.low", new Object[] { debug1, debug0 }));
/*  14 */     FLOAT_TOO_BIG = new Dynamic2CommandExceptionType((debug0, debug1) -> new TranslatableComponent("argument.float.big", new Object[] { debug1, debug0 }));
/*     */     
/*  16 */     INTEGER_TOO_SMALL = new Dynamic2CommandExceptionType((debug0, debug1) -> new TranslatableComponent("argument.integer.low", new Object[] { debug1, debug0 }));
/*  17 */     INTEGER_TOO_BIG = new Dynamic2CommandExceptionType((debug0, debug1) -> new TranslatableComponent("argument.integer.big", new Object[] { debug1, debug0 }));
/*     */     
/*  19 */     LONG_TOO_SMALL = new Dynamic2CommandExceptionType((debug0, debug1) -> new TranslatableComponent("argument.long.low", new Object[] { debug1, debug0 }));
/*  20 */     LONG_TOO_BIG = new Dynamic2CommandExceptionType((debug0, debug1) -> new TranslatableComponent("argument.long.big", new Object[] { debug1, debug0 }));
/*     */     
/*  22 */     LITERAL_INCORRECT = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("argument.literal.incorrect", new Object[] { debug0 }));
/*     */   }
/*  24 */   private static final Dynamic2CommandExceptionType INTEGER_TOO_SMALL; private static final Dynamic2CommandExceptionType INTEGER_TOO_BIG; private static final Dynamic2CommandExceptionType LONG_TOO_SMALL; private static final Dynamic2CommandExceptionType LONG_TOO_BIG; private static final DynamicCommandExceptionType LITERAL_INCORRECT; private static final SimpleCommandExceptionType READER_EXPECTED_START_OF_QUOTE = new SimpleCommandExceptionType((Message)new TranslatableComponent("parsing.quote.expected.start"));
/*  25 */   private static final SimpleCommandExceptionType READER_EXPECTED_END_OF_QUOTE = new SimpleCommandExceptionType((Message)new TranslatableComponent("parsing.quote.expected.end")); private static final DynamicCommandExceptionType READER_INVALID_ESCAPE; private static final DynamicCommandExceptionType READER_INVALID_BOOL; private static final DynamicCommandExceptionType READER_INVALID_INT; static {
/*  26 */     READER_INVALID_ESCAPE = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("parsing.quote.escape", new Object[] { debug0 }));
/*  27 */     READER_INVALID_BOOL = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("parsing.bool.invalid", new Object[] { debug0 }));
/*  28 */     READER_INVALID_INT = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("parsing.int.invalid", new Object[] { debug0 }));
/*  29 */   } private static final SimpleCommandExceptionType READER_EXPECTED_INT = new SimpleCommandExceptionType((Message)new TranslatableComponent("parsing.int.expected")); private static final DynamicCommandExceptionType READER_INVALID_LONG; static {
/*  30 */     READER_INVALID_LONG = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("parsing.long.invalid", new Object[] { debug0 }));
/*  31 */   } private static final SimpleCommandExceptionType READER_EXPECTED_LONG = new SimpleCommandExceptionType((Message)new TranslatableComponent("parsing.long.expected")); private static final DynamicCommandExceptionType READER_INVALID_DOUBLE; static {
/*  32 */     READER_INVALID_DOUBLE = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("parsing.double.invalid", new Object[] { debug0 }));
/*  33 */   } private static final SimpleCommandExceptionType READER_EXPECTED_DOUBLE = new SimpleCommandExceptionType((Message)new TranslatableComponent("parsing.double.expected")); private static final DynamicCommandExceptionType READER_INVALID_FLOAT; static {
/*  34 */     READER_INVALID_FLOAT = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("parsing.float.invalid", new Object[] { debug0 }));
/*  35 */   } private static final SimpleCommandExceptionType READER_EXPECTED_FLOAT = new SimpleCommandExceptionType((Message)new TranslatableComponent("parsing.float.expected"));
/*  36 */   private static final SimpleCommandExceptionType READER_EXPECTED_BOOL = new SimpleCommandExceptionType((Message)new TranslatableComponent("parsing.bool.expected")); private static final DynamicCommandExceptionType READER_EXPECTED_SYMBOL; static {
/*  37 */     READER_EXPECTED_SYMBOL = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("parsing.expected", new Object[] { debug0 }));
/*     */   }
/*  39 */   private static final SimpleCommandExceptionType DISPATCHER_UNKNOWN_COMMAND = new SimpleCommandExceptionType((Message)new TranslatableComponent("command.unknown.command"));
/*  40 */   private static final SimpleCommandExceptionType DISPATCHER_UNKNOWN_ARGUMENT = new SimpleCommandExceptionType((Message)new TranslatableComponent("command.unknown.argument"));
/*  41 */   private static final SimpleCommandExceptionType DISPATCHER_EXPECTED_ARGUMENT_SEPARATOR = new SimpleCommandExceptionType((Message)new TranslatableComponent("command.expected.separator")); private static final DynamicCommandExceptionType DISPATCHER_PARSE_EXCEPTION; static {
/*  42 */     DISPATCHER_PARSE_EXCEPTION = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("command.exception", new Object[] { debug0 }));
/*     */   }
/*     */   
/*     */   public Dynamic2CommandExceptionType doubleTooLow() {
/*  46 */     return DOUBLE_TOO_SMALL;
/*     */   }
/*     */ 
/*     */   
/*     */   public Dynamic2CommandExceptionType doubleTooHigh() {
/*  51 */     return DOUBLE_TOO_BIG;
/*     */   }
/*     */ 
/*     */   
/*     */   public Dynamic2CommandExceptionType floatTooLow() {
/*  56 */     return FLOAT_TOO_SMALL;
/*     */   }
/*     */ 
/*     */   
/*     */   public Dynamic2CommandExceptionType floatTooHigh() {
/*  61 */     return FLOAT_TOO_BIG;
/*     */   }
/*     */ 
/*     */   
/*     */   public Dynamic2CommandExceptionType integerTooLow() {
/*  66 */     return INTEGER_TOO_SMALL;
/*     */   }
/*     */ 
/*     */   
/*     */   public Dynamic2CommandExceptionType integerTooHigh() {
/*  71 */     return INTEGER_TOO_BIG;
/*     */   }
/*     */ 
/*     */   
/*     */   public Dynamic2CommandExceptionType longTooLow() {
/*  76 */     return LONG_TOO_SMALL;
/*     */   }
/*     */ 
/*     */   
/*     */   public Dynamic2CommandExceptionType longTooHigh() {
/*  81 */     return LONG_TOO_BIG;
/*     */   }
/*     */ 
/*     */   
/*     */   public DynamicCommandExceptionType literalIncorrect() {
/*  86 */     return LITERAL_INCORRECT;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleCommandExceptionType readerExpectedStartOfQuote() {
/*  91 */     return READER_EXPECTED_START_OF_QUOTE;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleCommandExceptionType readerExpectedEndOfQuote() {
/*  96 */     return READER_EXPECTED_END_OF_QUOTE;
/*     */   }
/*     */ 
/*     */   
/*     */   public DynamicCommandExceptionType readerInvalidEscape() {
/* 101 */     return READER_INVALID_ESCAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public DynamicCommandExceptionType readerInvalidBool() {
/* 106 */     return READER_INVALID_BOOL;
/*     */   }
/*     */ 
/*     */   
/*     */   public DynamicCommandExceptionType readerInvalidInt() {
/* 111 */     return READER_INVALID_INT;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleCommandExceptionType readerExpectedInt() {
/* 116 */     return READER_EXPECTED_INT;
/*     */   }
/*     */ 
/*     */   
/*     */   public DynamicCommandExceptionType readerInvalidLong() {
/* 121 */     return READER_INVALID_LONG;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleCommandExceptionType readerExpectedLong() {
/* 126 */     return READER_EXPECTED_LONG;
/*     */   }
/*     */ 
/*     */   
/*     */   public DynamicCommandExceptionType readerInvalidDouble() {
/* 131 */     return READER_INVALID_DOUBLE;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleCommandExceptionType readerExpectedDouble() {
/* 136 */     return READER_EXPECTED_DOUBLE;
/*     */   }
/*     */ 
/*     */   
/*     */   public DynamicCommandExceptionType readerInvalidFloat() {
/* 141 */     return READER_INVALID_FLOAT;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleCommandExceptionType readerExpectedFloat() {
/* 146 */     return READER_EXPECTED_FLOAT;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleCommandExceptionType readerExpectedBool() {
/* 151 */     return READER_EXPECTED_BOOL;
/*     */   }
/*     */ 
/*     */   
/*     */   public DynamicCommandExceptionType readerExpectedSymbol() {
/* 156 */     return READER_EXPECTED_SYMBOL;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleCommandExceptionType dispatcherUnknownCommand() {
/* 161 */     return DISPATCHER_UNKNOWN_COMMAND;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleCommandExceptionType dispatcherUnknownArgument() {
/* 166 */     return DISPATCHER_UNKNOWN_ARGUMENT;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleCommandExceptionType dispatcherExpectedArgumentSeparator() {
/* 171 */     return DISPATCHER_EXPECTED_ARGUMENT_SEPARATOR;
/*     */   }
/*     */ 
/*     */   
/*     */   public DynamicCommandExceptionType dispatcherParseException() {
/* 176 */     return DISPATCHER_PARSE_EXCEPTION;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\BrigadierExceptions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */