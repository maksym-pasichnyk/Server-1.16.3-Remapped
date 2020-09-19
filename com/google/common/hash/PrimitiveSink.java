package com.google.common.hash;

import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.nio.charset.Charset;

@Beta
@CanIgnoreReturnValue
public interface PrimitiveSink {
  PrimitiveSink putByte(byte paramByte);
  
  PrimitiveSink putBytes(byte[] paramArrayOfbyte);
  
  PrimitiveSink putBytes(byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
  
  PrimitiveSink putShort(short paramShort);
  
  PrimitiveSink putInt(int paramInt);
  
  PrimitiveSink putLong(long paramLong);
  
  PrimitiveSink putFloat(float paramFloat);
  
  PrimitiveSink putDouble(double paramDouble);
  
  PrimitiveSink putBoolean(boolean paramBoolean);
  
  PrimitiveSink putChar(char paramChar);
  
  PrimitiveSink putUnencodedChars(CharSequence paramCharSequence);
  
  PrimitiveSink putString(CharSequence paramCharSequence, Charset paramCharset);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\hash\PrimitiveSink.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */