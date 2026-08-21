# Bot Discord — registro de cuentas WoW

Bot de Discord que crea cuentas de World of Warcraft por **SOAP**, compatible con **AzerothCore** y **TrinityCore**. El jugador usa `/registrar`, completa usuario, contraseña y correo, y al terminar recibe el `realmlist`.

No usa base de datos: habla directo con el SOAP del reino.

## Requisitos

- **JDK 21** (`java -version`)
- Conexión al SOAP del servidor (por defecto `http://127.0.0.1:7878`)
- Un bot de Discord con token válido (pestaña **Bot**, no el Client Secret de OAuth2)

Maven no es obligatorio: el repo incluye el wrapper (`mvnw` / `mvnw.cmd`).

## Bot de Discord

1. Crea una aplicación en [Discord Developer Portal](https://discord.com/developers/applications).
2. En **Bot**, copia el token (`Reset Token` si hace falta). Debe verse en tres partes separadas por puntos.
3. Invita el bot al servidor con los scopes `bot` y `applications.commands`.
4. Copia el ID del servidor (modo desarrollador → clic derecho en el servidor → Copiar ID del servidor). Con `DISCORD_GUILD_ID` el comando `/registrar` aparece al instante. Sin él, el comando global puede tardar hasta una hora.

## SOAP del reino

En `worldserver.conf` (AzerothCore o TrinityCore) SOAP debe estar activo, por ejemplo:

```ini
SOAP.Enabled = 1
SOAP.IP = "127.0.0.1"
SOAP.Port = 7878
```

Usa una cuenta **GM** con permiso para `account create`. Ese usuario y contraseña van en `SOAP_USERNAME` y `SOAP_PASSWORD`.

Cambia el emulador con `WOW_EMULATOR`:

- `AzerothCore` (namespace SOAP `urn:AC`)
- `TrinityCore` (namespace SOAP `urn:TC`)

## Variables de entorno

No subas tokens ni contraseñas al repositorio. Configúralas en el sistema, en la IDE o al lanzar el proceso.

| Variable | Descripción |
|---|---|
| `DISCORD_BOT_TOKEN` | Token del bot (obligatorio) |
| `DISCORD_GUILD_ID` | ID del servidor Discord (recomendado) |
| `WOW_EMULATOR` | `AzerothCore` o `TrinityCore` |
| `WOW_REALMLIST` | Texto que se envía al jugador, ej. `set realmlist logon.midominio.com` |
| `SOAP_URI` | URL del SOAP, ej. `http://127.0.0.1:7878` |
| `SOAP_USERNAME` | Usuario GM del SOAP |
| `SOAP_PASSWORD` | Contraseña GM del SOAP |

Opcionales de anti-spam: `REGISTER_COOLDOWN_SECONDS` (300), `REGISTER_FAILED_COOLDOWN_SECONDS` (30), `REGISTER_MAX_ATTEMPTS_PER_HOUR` (3), `REGISTER_MAX_ACCOUNTS_PER_USER` (1).

## Clonar y preparar

```bash
git clone git@github.com:Wow-Libre/botdiscord-private-server.git
cd botdiscord-private-server
```

## Levantar el proyecto en local

Desde la raíz del repo.

**Windows (PowerShell):**

```powershell
$env:DISCORD_BOT_TOKEN="tu_token"
$env:DISCORD_GUILD_ID="id_del_servidor"
$env:WOW_EMULATOR="AzerothCore"
$env:WOW_REALMLIST="set realmlist logon.midominio.com"
$env:SOAP_URI="http://127.0.0.1:7878"
$env:SOAP_USERNAME="admin"
$env:SOAP_PASSWORD="tu_password_gm"

.\mvnw.cmd spring-boot:run
```

**Linux / macOS:**

```bash
export DISCORD_BOT_TOKEN="tu_token"
export DISCORD_GUILD_ID="id_del_servidor"
export WOW_EMULATOR="AzerothCore"
export WOW_REALMLIST="set realmlist logon.midominio.com"
export SOAP_URI="http://127.0.0.1:7878"
export SOAP_USERNAME="admin"
export SOAP_PASSWORD="tu_password_gm"

./mvnw spring-boot:run
```

Si arrancó bien verás un log similar a `JDA listo`. En Discord escribe **`/registrar`**.

También puedes pegar los valores en `src/main/resources/application.yml` solo en tu máquina. Ese archivo no debe llevar secretos a git.

## Compilar el JAR

**Windows:**

```powershell
.\mvnw.cmd -DskipTests package
```

**Linux / macOS:**

```bash
./mvnw -DskipTests package
```

El JAR queda en:

```text
target/register-discord-0.0.1-SNAPSHOT.jar
```

Para correr tests: `.\mvnw.cmd test` o `./mvnw test`.

## Ejecutar el JAR

Necesitas JDK 21. Las mismas variables de entorno aplican.

**Windows (PowerShell):**

```powershell
$env:DISCORD_BOT_TOKEN="tu_token"
$env:DISCORD_GUILD_ID="id_del_servidor"
$env:WOW_EMULATOR="AzerothCore"
$env:WOW_REALMLIST="set realmlist logon.midominio.com"
$env:SOAP_URI="http://127.0.0.1:7878"
$env:SOAP_USERNAME="admin"
$env:SOAP_PASSWORD="tu_password_gm"

java -jar target/register-discord-0.0.1-SNAPSHOT.jar
```

**Linux / macOS:**

```bash
export DISCORD_BOT_TOKEN="tu_token"
export DISCORD_GUILD_ID="id_del_servidor"
export WOW_EMULATOR="AzerothCore"
export WOW_REALMLIST="set realmlist logon.midominio.com"
export SOAP_URI="http://127.0.0.1:7878"
export SOAP_USERNAME="admin"
export SOAP_PASSWORD="tu_password_gm"

java -jar target/register-discord-0.0.1-SNAPSHOT.jar
```

También puedes pasar propiedades de Spring:

```bash
java -jar target/register-discord-0.0.1-SNAPSHOT.jar \
  --discord.bot.token=tu_token \
  --discord.bot.guild-id=id_del_servidor \
  --wow.emulator=AzerothCore \
  --wow.realmlist="set realmlist logon.midominio.com" \
  --soap.uri=http://127.0.0.1:7878 \
  --soap.username=admin \
  --soap.password=tu_password_gm
```

## Uso en Discord

1. El bot debe estar en línea.
2. En el servidor, `/registrar`.
3. Completa usuario (3–16 alfanumérico), contraseña (6–16) y correo.
4. Recibirás un mensaje privado al comando (efímero) con el realmlist.


## Evidencias





https://github.com/user-attachments/assets/5f4f6eab-46bf-4587-a650-1e6a27d495f6





## Jar compilado (Segundo método)

Si no quieres compilar el proyecto en tu máquina, puedes usar el JAR ya empaquetado. Descárgalo desde MediaFire:

[botwowlibre.jar](https://www.mediafire.com/file/4zeqhe355bciyyg/botwowlibre.jar/file)

https://www.mediafire.com/file/4zeqhe355bciyyg/botwowlibre.jar/file

### Configurar variables de entorno

Antes de ejecutar el JAR, define las variables de entorno del bot. El **token** se obtiene en el [Discord Developer Portal](https://discord.com/developers) (pestaña **Bot**, no el Client Secret).

En el siguiente video se muestra cómo configurar las variables de entorno, incluido el token del bot:

https://github.com/user-attachments/assets/1f07bb79-1689-4969-9b3b-f8c63841d641

Cuando ya tengas:

- las variables de entorno del bot (`DISCORD_BOT_TOKEN`, y si aplica `DISCORD_GUILD_ID`)
- las credenciales SOAP (`SOAP_URI`, `SOAP_USERNAME`, `SOAP_PASSWORD`)
- el SOAP del reino en ejecución, ya sea en tu VPS o en tu PC personal

puedes descargar el JAR compilado y arrancarlo.

El siguiente video muestra ese paso (descarga del JAR compilado):

https://github.com/user-attachments/assets/8851001d-20ec-4553-bbf1-5c8711eb856d






## Problemas frecuentes

| Error | Qué revisar |
|---|---|
| `The provided token is invalid!` | Estás usando el Client Secret u un token viejo. Genera uno nuevo en **Bot → Reset Token**. |
| `Falta discord.bot.token` | No exportaste `DISCORD_BOT_TOKEN`. |
| `/registrar` no aparece | Bot invitado con `applications.commands`, `DISCORD_GUILD_ID` correcto, bot en línea. |
| Fallo SOAP / no crea la cuenta | SOAP encendido, URI, usuario GM y emulador (`AzerothCore` vs `TrinityCore`). |
