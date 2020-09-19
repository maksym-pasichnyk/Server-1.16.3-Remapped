package org.apache.logging.log4j.core.config.builder.api;

public interface ScriptFileComponentBuilder extends ComponentBuilder<ScriptFileComponentBuilder> {
  ScriptFileComponentBuilder addLanguage(String paramString);
  
  ScriptFileComponentBuilder addIsWatched(boolean paramBoolean);
  
  ScriptFileComponentBuilder addIsWatched(String paramString);
  
  ScriptFileComponentBuilder addCharset(String paramString);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\builder\api\ScriptFileComponentBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */