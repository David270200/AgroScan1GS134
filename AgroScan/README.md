# AgroScan — Detector de Enfermedades en Plantas

Aplicación móvil Android desarrollada en Java con layouts XML para el Parcial de Programación Móvil.
Fecha de entrega: miércoles 10 de junio de 2026. Grupo de 3 personas.

---

## Descripción

AgroScan permite a agricultores y estudiantes de agronomía detectar enfermedades en plantas mediante el análisis de fotografías. El usuario puede capturar una imagen con la cámara o seleccionarla desde la galería, y la aplicación muestra un diagnóstico con el nombre de la posible enfermedad, su descripción, causas y tratamiento recomendado.

Toda la información se gestiona con SharedPreferences para persistencia ligera y estructuras en memoria (ArrayList, HashMap) para datos temporales de sesión. No requiere conexión a internet ni base de datos externa.

---

## Requisitos del Parcial

- Lenguaje Java con layouts XML en Android Studio
- Exactamente 7 Activities implementadas
- Persistencia con SharedPreferences, sin base de datos
- Diseño con tema verde naturaleza y CardViews
- Aplicación funcional y sin errores de compilación
- Código fuente entregado en archivo .zip

---

## Tecnologías Utilizadas

Java para la lógica de todas las Activities, listeners, intents y adaptadores. XML para los layouts, menús, estilos y recursos de la aplicación. SharedPreferences como capa de persistencia de usuarios, escaneos e historial. ML Kit simulado mediante MLKitHelper.java para el análisis de enfermedades. Intents explícitos para la navegación entre Activities e intents implícitos para el acceso a cámara y galería. ListView con adaptadores personalizados, CardView, Material Design, ProgressBar, DatePicker, FloatingActionButton y FileProvider para acceso seguro a imágenes.

---

## Las 7 Activities

### 1. LoginActivity

Pantalla de inicio de sesión. Valida credenciales contra SharedPreferences y redirige al usuario según su rol (admin o usuario normal). Crea el usuario admin/admin123 en el primer arranque. Incluye auto-login si ya existe sesión activa.

### 2. RegisterActivity

Registro de nuevos usuarios. Valida que todos los campos estén llenos, que las contraseñas coincidan y guarda los datos en SharedPreferences. Al finalizar redirige al Login.

### 3. HomeActivity

Pantalla principal del usuario autenticado. Muestra un saludo personalizado, estadísticas en CardViews (total de escaneos, plantas sanas, enfermedades detectadas) y una lista de escaneos recientes. Incluye FloatingActionButton para iniciar un nuevo escaneo y menú con opciones de cerrar sesión y acerca de.

### 4. AdminHubActivity

Panel exclusivo del administrador. Muestra un contador de usuarios registrados y una lista con nombre y correo de cada uno. Permite eliminar usuarios mediante menú contextual con long-press, excepto la cuenta de admin.

### 5. ScanActivity

Captura o selecciona una imagen de planta para análisis. El usuario ingresa nombre de la planta, tipo de cultivo y síntomas observados. Soporta foto con cámara (FileProvider) y selección desde galería. Muestra un ProgressBar durante el análisis y navega automáticamente al detalle del resultado.

### 6. RecordDetailActivity

Detalle completo de un escaneo. Muestra los datos de la planta, el diagnóstico con descripción y tratamiento, y un historial de registros previos. Incluye DatePicker para filtrar el historial por fecha y menú contextual para eliminar entradas.

### 7. AddHistoryActivity

Permite registrar manualmente un diagnóstico o tratamiento en el historial de una planta. Valida los campos requeridos, genera un ID único con System.currentTimeMillis() y guarda el registro en SharedPreferences.

---

## Flujo de Navegación

LoginActivity dirige a HomeActivity para usuarios normales o a AdminHubActivity para el administrador. Desde HomeActivity se accede a ScanActivity mediante el FAB y a RecordDetailActivity al tocar un escaneo de la lista. ScanActivity navega a RecordDetailActivity al finalizar el análisis. Desde RecordDetailActivity se puede abrir AddHistoryActivity para agregar registros al historial. Cualquier Activity puede cerrar sesión y regresar a LoginActivity limpiando el back stack.

---

## Estructura del Proyecto

```
AgroScan/
├── app/
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/agroscan/
│       │   ├── LoginActivity.java
│       │   ├── RegisterActivity.java
│       │   ├── HomeActivity.java
│       │   ├── AdminHubActivity.java
│       │   ├── ScanActivity.java
│       │   ├── RecordDetailActivity.java
│       │   ├── AddHistoryActivity.java
│       │   ├── adapters/
│       │   │   ├── PlantAdapter.java
│       │   │   ├── HistoryAdapter.java
│       │   │   └── UserAdapter.java
│       │   ├── models/
│       │   │   ├── PlantRecord.java
│       │   │   ├── HistoryEntry.java
│       │   │   └── User.java
│       │   └── utils/
│       │       ├── SharedPrefsHelper.java
│       │       └── MLKitHelper.java
│       └── res/
│           ├── layout/        (7 activities + 3 items de lista)
│           ├── values/        (colors, strings, themes)
│           ├── drawable/      (fondos, íconos vectoriales, badges)
│           ├── menu/          (menú home y menús contextuales)
│           └── xml/
│               └── file_paths.xml
```

---

## Persistencia con SharedPreferences

Toda la capa de datos está centralizada en SharedPrefsHelper.java usando claves estructuradas:

- Sesión activa: `current_user`
- Datos de usuario: `user_carlos_pass`, `user_carlos_name`, `user_carlos_email`
- Lista de usuarios: `all_users` (StringSet)
- Escaneo: `scan_1717800000000_plant`, `_crop`, `_diagnosis`, `_date`
- Lista de escaneos: `all_scan_ids` (StringSet)
- Historial: `hist_1717800001000_disease`, `_treatment`, `_observations`
- Historial por planta: `hist_for_scan_1717800000000` (StringSet)

---

## Permisos Requeridos

CAMERA para capturar fotos desde ScanActivity. READ_EXTERNAL_STORAGE y WRITE_EXTERNAL_STORAGE para acceso a galería en Android anteriores a la versión 10. READ_MEDIA_IMAGES para galería en Android 13 y superior.

---

## Diseño Visual

La paleta usa tonos de verde naturaleza: verde oscuro (#2E7D32) para la Action Bar y botones principales, verde medio (#4CAF50) para el FAB y acentos, y un verde muy claro (#F1F8E9) como fondo de todas las pantallas. Los CardViews tienen esquinas redondeadas de 12dp y elevación de 2dp. Los badges de estado de diagnóstico son verde (sana), rojo (enferma) y amarillo (en revisión).

---

## Credenciales de Prueba
se usan los datos quemados para este caso:
El usuario administrador se crea automáticamente en el primer arranque con el usuario `admin` y contraseña `admin123`. Cualquier usuario registrado desde la app puede iniciar sesión con las credenciales que definió al registrarse.

# Promp Utilizado
Necesito que me ayudes a desarrollar una aplicación Android completa basándote en el documento que te adjunto.

Primero analiza todo el documento y luego crea el proyecto respetando exactamente los requisitos que aparecen allí. La aplicación se llama AgroScan y debe hacerse en Java y XML usando Android Studio.

Quiero que generes el proyecto de forma ordenada, mostrando primero la estructura de carpetas y después el código de cada archivo. No omitas clases, layouts, modelos, adaptadores, utilidades ni configuraciones necesarias para que el proyecto funcione.

Ten en cuenta que es un parcial universitario, así que necesito que se implementen correctamente los temas vistos en clase como:

* Activities
* Intents explícitos e implícitos
* SharedPreferences
* ListView con adaptadores personalizados
* Menús y menús contextuales
* ProgressBar
* DatePicker
* Paso de datos con Bundle y putExtra
* Diseño Material Design

Quiero que el código sea funcional, esté bien organizado y listo para copiar y pegar en Android Studio.

Si el proyecto es muy grande, genera los archivos por partes siguiendo este orden:

1. Estructura completa del proyecto.
2. AndroidManifest y Gradle.
3. Modelos y utilidades.
4. Activities Java.
5. Layouts XML.
6. Adaptadores.
7. Menús, colores, strings y themes.
8. Explicación final de cómo ejecutar el proyecto.

Antes de generar código, resume brevemente lo que entendiste del documento para confirmar que todos los requisitos fueron considerados.




## División de Trabajo

Integrante 1 se encarga de LoginActivity y RegisterActivity, junto con SharedPrefsHelper.java, el modelo User.java y los archivos de colores y temas.

Integrante 2 se encarga de HomeActivity y AdminHubActivity, junto con PlantAdapter.java, UserAdapter.java, los menús XML y strings.xml.

Integrante 3 se encarga de ScanActivity, RecordDetailActivity y AddHistoryActivity, junto con HistoryAdapter.java, MLKitHelper.java, los modelos PlantRecord.java e HistoryEntry.java, y la configuración de permisos.

