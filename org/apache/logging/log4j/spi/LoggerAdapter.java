package org.apache.logging.log4j.spi;

import java.io.Closeable;

public interface LoggerAdapter<L> extends Closeable {
  L getLogger(String paramString);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\spi\LoggerAdapter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */