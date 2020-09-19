package org.apache.logging.log4j.core.selector;

import java.net.URI;
import java.util.List;
import org.apache.logging.log4j.core.LoggerContext;

public interface ContextSelector {
  LoggerContext getContext(String paramString, ClassLoader paramClassLoader, boolean paramBoolean);
  
  LoggerContext getContext(String paramString, ClassLoader paramClassLoader, boolean paramBoolean, URI paramURI);
  
  List<LoggerContext> getLoggerContexts();
  
  void removeContext(LoggerContext paramLoggerContext);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\selector\ContextSelector.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */