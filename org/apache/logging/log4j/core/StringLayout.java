package org.apache.logging.log4j.core;

import java.nio.charset.Charset;

public interface StringLayout extends Layout<String> {
  Charset getCharset();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\StringLayout.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */