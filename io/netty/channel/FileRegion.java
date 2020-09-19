package io.netty.channel;

import io.netty.util.ReferenceCounted;
import java.io.IOException;
import java.nio.channels.WritableByteChannel;

public interface FileRegion extends ReferenceCounted {
  long position();
  
  @Deprecated
  long transfered();
  
  long transferred();
  
  long count();
  
  long transferTo(WritableByteChannel paramWritableByteChannel, long paramLong) throws IOException;
  
  FileRegion retain();
  
  FileRegion retain(int paramInt);
  
  FileRegion touch();
  
  FileRegion touch(Object paramObject);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\FileRegion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */