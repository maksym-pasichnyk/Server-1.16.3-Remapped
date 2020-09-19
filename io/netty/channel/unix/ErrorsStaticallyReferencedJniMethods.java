package io.netty.channel.unix;

final class ErrorsStaticallyReferencedJniMethods {
  static native int errnoENOENT();
  
  static native int errnoEBADF();
  
  static native int errnoEPIPE();
  
  static native int errnoECONNRESET();
  
  static native int errnoENOTCONN();
  
  static native int errnoEAGAIN();
  
  static native int errnoEWOULDBLOCK();
  
  static native int errnoEINPROGRESS();
  
  static native int errorECONNREFUSED();
  
  static native int errorEISCONN();
  
  static native int errorEALREADY();
  
  static native int errorENETUNREACH();
  
  static native String strError(int paramInt);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channe\\unix\ErrorsStaticallyReferencedJniMethods.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */