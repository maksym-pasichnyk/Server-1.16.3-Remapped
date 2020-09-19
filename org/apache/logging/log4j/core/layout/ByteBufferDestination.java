package org.apache.logging.log4j.core.layout;

import java.nio.ByteBuffer;

public interface ByteBufferDestination {
  ByteBuffer getByteBuffer();
  
  ByteBuffer drain(ByteBuffer paramByteBuffer);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\layout\ByteBufferDestination.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */