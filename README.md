# WebsiteMod for Minecraft 1.21.1 (Fabric)

Öffne Webseiten direkt aus dem Minecraft-Chat!

## Verwendung

```
/website https://google.com
/website minecraft.net
/website youtube.com
```

Die URL wird automatisch mit `https://` ergänzt, falls kein Protokoll angegeben ist.

## Installation

1. **Fabric Loader** für Minecraft 1.21.1 installieren: https://fabricmc.net/use/
2. **Fabric API** herunterladen: https://modrinth.com/mod/fabric-api (Version für 1.21.1)
3. Beide `.jar`-Dateien in den `mods`-Ordner legen:
   - Windows: `%appdata%\.minecraft\mods\`
   - Linux: `~/.minecraft/mods/`
   - Mac: `~/Library/Application Support/minecraft/mods/`

## Selbst kompilieren

### Voraussetzungen
- Java 21 JDK
- (optional) Gradle

### Build-Befehl
```bash
# Windows
gradlew.bat build

# Linux / Mac
./gradlew build
```

Die fertige `.jar` findest du in `build/libs/websitemod-1.0.0.jar`.

## Was passiert beim /website-Befehl?

1. Du gibst `/website [URL]` im Chat ein
2. Ein Minecraft-Screen erscheint kurz als Bestätigung
3. Die Webseite öffnet sich automatisch in deinem Standard-Browser
4. Das Spiel pausiert währenddessen

## Kompatibilität

- Minecraft: **1.21.1**
- Loader: **Fabric 0.16.5+**
- Java: **21+**
- OS: Windows, macOS, Linux

## Hinweis

Die Mod öffnet Webseiten im **System-Browser** (Chrome, Firefox, etc.), nicht in einem eingebetteten Browser im Spiel, da Minecraft keine Browser-Engine mitbringt.
