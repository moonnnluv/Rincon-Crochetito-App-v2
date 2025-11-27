
# Rincon Crochetito App v2 – README

Aplicación Android nativa desarrollada en Kotlin con layouts XML y ViewBinding, orientada a la venta de productos de crochet para la tienda “Rincón Crochetito”. El proyecto implementa un flujo completo de e-commerce básico con roles de Cliente y Administrador, usando Firebase como backend principal y una integración opcional con un backend Node.js vía Retrofit.

---

## 1. Descripción general

**Rincon Crochetito App v2** es una aplicación Android nativa desarrollada en **Kotlin** con layouts **XML** y **ViewBinding**. Está orientada a la venta de productos de crochet (“Rincón Crochetito”) y contempla dos tipos de usuario:

- **Cliente**: puede navegar el catálogo, administrar un carrito, realizar un pago simulado, solicitar envío y gestionar su perfil.
- **Administrador**: puede gestionar productos (incluyendo múltiples imágenes), categorías, usuarios y el ciclo de vida de las órdenes/pagos.

La app usa **Firebase** como backend principal (**Auth**, **Realtime Database** y **Storage**) y tiene además una integración opcional con un backend **Node.js** expuesto vía **Retrofit** (para un endpoint de creación de preferencia de pago).


---

## 2. Objetivo del proyecto

El objetivo es implementar un flujo completo de e-commerce básico en Android, cumpliendo con:

- Autenticación con login/logout para Cliente y Admin.
- Persistencia de sesión basada en `FirebaseAuth` (usuario autenticado permanece activo entre ejecuciones).
- Determinación del rol (cliente/administrador) desde Firebase Realtime Database y redirección de inicio según rol.
- Catálogo de productos, categorías y detalle de producto con múltiples imágenes.
- Carrito editable (agregar, modificar cantidad, eliminar ítems).
- Flujo de pago simulado que genera una orden asociada al cliente.
- Panel de administración con:
  - CRUD de productos.
  - Múltiples imágenes por producto almacenadas en Firebase Storage.
  - Gestión básica de usuarios (listar, editar, bloquear/desbloquear).
  - Gestión de órdenes/pagos (pendiente → aceptado/rechazado → enviado).
- Uso de Retrofit como cliente HTTP (integración lista con un backend Node).
- Uso de RecyclerView para listados.
- Uso de ViewBinding para la vinculación entre Activities/Fragments y XML.

---

## 3. Arquitectura y módulos principales

**Paquete raíz:** `com.example.rincon_crochetitov2`

### 3.1 Clases principales en la raíz

- `Constantes.kt`  
  - Funciones utilitarias de tiempo/fecha.  
  - Funciones para agregar/eliminar productos de favoritos en la ruta `Usuarios/{uid}/Favoritos` de Firebase Realtime Database.
- `SeleccionarTipoActivity.kt`  
  - Pantalla de selección inicial de tipo de usuario / entrada al flujo de autenticación.
- `SplashScreenActivity.kt`  
  - Pantalla inicial que decide a qué vista enviar según si hay usuario autenticado y su tipo (cliente/administrador).

### 3.2 Paquete `Adaptadores`

Contiene adaptadores de RecyclerView para múltiples listados:

- `AdaptadorProducto`, `AdaptadorProductoC`, `AdaptadorProductoAleatorio`, `AdaptadorProductoOrden`.
- `AdaptadorCliente` (lista de usuarios).
- `AdaptadorOrdenCompra`, `AdaptadorOrdenCompraA` (órdenes).
- `AdaptadorCategoriaA`, `AdaptadorCategoriaC` (categorías).
- `AdaptadorImagenSeleccionada`, `AdaptadorImgSlider` (galería de imágenes).
- Otros adaptadores auxiliares según los listados de la app.

### 3.3 Paquete `Administrador`

- `LoginActivityAdmin.kt`  
  - Login de administrador con FirebaseAuth (email/password).
- `RegistroAdminActivity.kt`  
  - Registro de administradores.
- `MainActivityAdmin.kt`  
  - Actividad principal del rol administrador; aloja fragments del panel de administración.

**Subcarpeta `Fragment`:**

- `FragmentInicioA.kt`: inicio del panel de admin.  
- `FragmentCategoriasA.kt`: gestión de categorías, con subida de imagen a Firebase Storage.  
- `FragmentProductosA.kt`, `FragmentMisProductosA.kt`: listados de productos y gestión.  
- `FragmentMiTiendaA.kt`: configuración/visión general de la tienda.  
- `FragmentOrdenesA.kt`: listado y gestión de órdenes y pagos.

**Subcarpeta `ListaClientes`:**

- `ListaClientesActivity.kt`, `EditarClienteActivity.kt`: listados y edición de usuarios, bloqueo/desbloqueo.

**Subcarpeta `Orden`:**

- `DetalleOrdenAActivity.kt`: detalle de una orden/pedido visto por el admin.

**Subcarpeta `Productos`:**

- `AgregarProductoActivity.kt`: creación/edición de producto, con subida de múltiples imágenes a Firebase Storage.  
- `ProductosCatAActivity.kt`: productos filtrados por categoría para admin.

### 3.4 Paquete `Cliente`

- `LoginClienteActivity.kt`  
  - Login de cliente con FirebaseAuth (email/password), acceso a registro, recuperación de contraseña y login por teléfono.
- `RegistroClienteActivity.kt`, `RecuperarPasswordActivity.kt`, `ActualizarPasswordActivity.kt`, `LoginTelActivity.kt`.
- `MainActivityCliente.kt`  
  - Actividad principal del rol cliente; contiene el bottom navigation o tabs con los fragments del cliente.

**Subcarpeta `Fragments`:**

- `FragmentInicioC.kt`: inicio para cliente (catálogo/resúmenes).  
- `FragmentTiendaC.kt`: listado principal de productos/tienda.  
- `FragmentCarritoC.kt`: carrito de compras (usa `CarritoManager`).  
- `FragmentMisOrdenesC.kt`: historial/estado de órdenes del cliente.  
- `FragmentFavoritosC.kt`: productos marcados como favoritos.  
- `FragmentMiPerfilC.kt`: perfil de usuario (ver/editar datos personales).

**Subcarpeta `Carrito`:**

- `CarritoManager.kt`: lógica de gestión del carrito del cliente.

**Subcarpeta `Pago`:**

- `PagoActivity.kt`: pantalla del flujo de pago simulado (y eventual integración con backend Node).

**Subcarpeta `Orden`:**

- `DetalleOrdenCActivity.kt`: detalle de orden visto por el cliente.

**Subcarpeta `Productos`:**

- `ProductosCatCActivity.kt`: productos filtrados por categoría para cliente.

### 3.5 Otros paquetes

**Paquete `Calificacion`:**

- `CalificarProductoActivity.kt`, `MostrarCalificacionesActivity.kt`: flujo de calificación de productos/tienda.

**Paquete `DetalleProducto`:**

- `DetalleProductoActivity.kt`: muestra detalle de un producto con galería de imágenes y opciones de agregar al carrito/favoritos.

**Paquete `Filtro`:**

- `FiltroProducto.kt`: lógica de filtrado/búsqueda de productos.

**Paquete `Mapas`:**

- `SeleccionarUbicacionActivity.kt`: elección de ubicación/dirección de envío mediante mapa.

**Paquete `Modelos`:**

- `ModeloProducto`, `ModeloProductoCarrito`, `ModeloProductoOrden`, `ModeloOrdenCompra`.  
- `ModeloCategoria`, `ModeloCalificacion`, `ModeloUsuario`, `ModeloImgSlider`, `ModeloImagenSeleccionada`, `ResponseHttp`.

**Paquete `Network`:**

- `RetrofitClient.kt`: configuración del cliente HTTP Retrofit.

**Paquete `data`:**

- `ApiService.kt`: interfaz con el endpoint HTTP del backend Node.

### 3.6 Arquitectura general

- Arquitectura basada en **Activities** y **Fragments** con helpers y managers (por ejemplo, `CarritoManager`), sin patrones MVVM formales.
- **ViewBinding** habilitado y utilizado en las Activities y Fragments para enlazar vistas XML de forma segura.

---

## 4. Backend y almacenamiento de datos

### 4.1 Firebase

El backend principal es **Firebase**:

#### FirebaseAuth

- Utilizado en `LoginClienteActivity` y `LoginActivityAdmin` para autenticación por email/contraseña.
- Permite que el estado de autenticación persista entre ejecuciones (usuario mantiene sesión iniciada mientras no cierre sesión).

#### Firebase Realtime Database

**Nodos principales usados por la app:**

- `Usuarios`
  - Información de clientes y administradores.
  - Incluye el campo `tipoUsuario` con valores como `"cliente"` o `"administrador"`.
  - Incluye subnodo `Favoritos`:
    - `Usuarios/{uid}/Favoritos/{idProducto}` con campos:
      - `idProducto`
      - `idFav` (timestamp)

- `Categorias`
  - Cada categoría se guarda con:
    - `id`
    - `categoria`
    - `imagenUrl` (URL de Firebase Storage).

- `Productos`
  - Cada producto se guarda con:
    - `id`
    - `nombre`
    - `descripcion`
    - `categoria`
    - `precio`
    - `precioDesc`
    - `notaDesc`
  - Subnodo `Imagenes`:
    - `Productos/{idProducto}/Imagenes/{idImagen}` con:
      - `id`
      - `imagenUrl` (URL de Firebase Storage).

- Existen otros nodos asociados a órdenes/órdenes de compra y calificaciones, representados por los modelos:
  - `ModeloOrdenCompra`, `ModeloProductoOrden`, `ModeloCalificacion`.

#### Firebase Storage

- Usado para almacenar binarios (imágenes).

**Para categorías:**

- Carpeta lógica: `Categorias/{idCategoria}`  
- La URL resultante se guarda en `Categorias/{idCategoria}/imagenUrl` en Realtime Database.

**Para productos:**

- Carpeta lógica: `Productos/{idImagen}`  
- La URL resultante se guarda en `Productos/{idProducto}/Imagenes/{idImagen}/imagenUrl`.

### 4.2 Backend Node (integración vía Retrofit, opcional)

Además de Firebase, la app incluye una integración opcional con un backend **Node.js**:

- `RetrofitClient.kt`:
  - `BASE_URL = "http://192.168.152.176:3000"`
  - Esta URL apunta a un servidor Node en la red local (IP de la máquina que ejecuta el backend).

- `ApiService.kt`:
  - Método:
    - `@POST("crear_preferencia")`
      `enviarOrdenDeCompra(@Body requestBody: RequestBody): Call<ResponseHttp>`
  - Este endpoint está pensado para crear una “preferencia de pago” en un backend Node (ejemplo: integración futura con pasarelas de pago).

**En la versión actual del proyecto:**

- El flujo de pago se maneja de forma simulada en la app utilizando Firebase (registro de órdenes en Realtime Database).
- La integración vía Retrofit + Node queda lista para ser utilizada en una futura versión con pasarela de pago real.

---

## 5. Manejo de sesión y ruteo por rol

- El estado de sesión se basa en `FirebaseAuth`:
  - Al iniciar la app, `SplashScreenActivity` obtiene el usuario actual:
    - `firebaseAuth.currentUser`
  - Si no hay usuario autenticado:
    - Redirige a `SeleccionarTipoActivity` (desde ahí se elige flujo de login de cliente o admin).
  - Si hay usuario autenticado:
    - Consulta en Realtime Database: `Usuarios/{uid}/tipoUsuario`
    - Según el valor de `tipoUsuario`:
      - `"administrador"` → `MainActivityAdmin`
      - `"cliente"` → `MainActivityCliente`

- El cierre de sesión (logout) se realiza en las Activities principales de cada rol llamando a `FirebaseAuth.signOut()`, y luego se redirige a la pantalla inicial (login o selección de tipo de usuario).

**SharedPreferences:**

- El mecanismo principal de persistencia de sesión es `FirebaseAuth`.
- `SharedPreferences` se puede usar en la app para pequeñas preferencias locales (flags, filtros, etc.), sin almacenar contraseñas ni datos sensibles.

---

## 6. Pasos de configuración y ejecución (Android + backend)

### 6.1 Requisitos previos

- Android Studio (cualquier versión reciente que soporte proyectos Kotlin + Gradle).
- JDK instalado (se incluye con Android Studio).
- Acceso a una cuenta de Firebase (para crear el proyecto).

Opcional:

- Backend Node.js en ejecución (solo si se desea probar el endpoint `/crear_preferencia` vía Retrofit).

### 6.2 Configuración del proyecto en Android Studio

1. Clonar o descomprimir el repositorio:
   - `git clone [URL_DEL_REPOSITORIO]`
   - Abrir la carpeta raíz del proyecto en Android Studio.
2. Esperar a que Gradle termine de sincronizar y se descarguen las dependencias.
3. Verificar que el paquete de la app sea:
   - `applicationId: "com.example.rincon_crochetitov2"` (en `app/build.gradle`).

### 6.3 Configuración de Firebase

1. Crear un proyecto en Firebase Console.
2. En Firebase Console, agregar una app Android:
   - Nombre del paquete: `com.example.rincon_crochetitov2`
   - Descargar el archivo `google-services.json`.
3. Copiar `google-services.json` a la carpeta:
   - `app/google-services.json`
4. Habilitar los servicios necesarios en Firebase:
   - **Authentication**:
     - Habilitar proveedor Email/Password.
   - **Realtime Database**:
     - Crear la base de datos y configurar reglas adecuadas para pruebas (por ejemplo, permisos de lectura/escritura durante el desarrollo).
     - Configurar los nodos `Usuarios`, `Categorias`, `Productos` según se vayan usando.
   - **Storage**:
     - Crear el bucket de Firebase Storage (se crea automáticamente al habilitar Storage).
     - Las carpetas `Categorias/` y `Productos/` se generan automáticamente al subir imágenes desde la app.

5. Crear usuarios de prueba en Firebase Authentication:

   - **Administrador:**
     - Email: `admin@rincon.crochetito.com`
     - Contraseña: `Admin123$`
     - En Realtime Database, en el nodo `Usuarios/{uid}` correspondiente a este email, asegurarse de que el campo:
       - `tipoUsuario = "administrador"`

   - **Cliente:**
     - Email: `cliente@rincon.crochetito.com`
     - Contraseña: `Cliente123$`
     - En Realtime Database, en el nodo `Usuarios/{uid}` correspondiente, asegurarse de que:
       - `tipoUsuario = "cliente"`

### 6.4 Configuración del backend Node (opcional)

Para que la integración de Retrofit hacia el backend Node funcione:

1. Tener un servidor Node.js ejecutándose en la máquina de desarrollo o en la red local, escuchando en el puerto `3000`.
2. El endpoint esperado por la app es:
   - `POST /crear_preferencia`
   - Base URL configurada en `RetrofitClient`:
     - `private const val BASE_URL = "http://192.168.152.176:3000"`
3. Si el docente ejecuta el backend Node en otro host o IP:
   - Cambiar la constante `BASE_URL` en:
     - `com.example.rincon_crochetitov2.Network.RetrofitClient`
   - Por ejemplo:
     - `"http://10.0.2.2:3000"` (para emulador accediendo al host local).
     - `"http://[IP_DEL_SERVIDOR]:3000"` para red local.
4. La app funcionará correctamente sin el backend Node si se evalúa solo el flujo de pago simulado con Firebase; la parte de Node es una integración adicional para futuras pasarelas de pago.

### 6.5 Ejecución de la app

1. Conectar un dispositivo Android con depuración USB o iniciar un emulador desde Android Studio.
2. Seleccionar el dispositivo de destino en la barra superior de Android Studio.
3. Pulsar **Run** (▶).
4. La app se abrirá en `SplashScreenActivity`:
   - Si no hay usuario autenticado → se mostrará selección de tipo de usuario / login.
   - Si ya hay usuario autenticado → se redirigirá automáticamente a `MainActivityAdmin` o `MainActivityCliente` según el valor de `tipoUsuario` en Realtime Database.

---

## 7. Variables y URLs importantes

- **RetrofitClient.BASE_URL** (backend Node opcional):
  - Archivo: `com.example.rincon_crochetitov2.Network.RetrofitClient`
  - Valor por defecto:
    - `"http://192.168.152.176:3000"`
  - Se debe adaptar si el servidor Node corre en otra IP o se prueba en entorno distinto.

- **Endpoint Node expuesto vía Retrofit:**
  - Archivo: `com.example.rincon_crochetitov2.data.ApiService`
  - Método:
    - `POST "crear_preferencia"` (ruta relativa)
  - Se recomienda mantener la misma ruta en el backend Node.

- **Rutas lógicas de Firebase Storage:**
  - Categorías:
    - Carpeta: `Categorias/{idCategoria}`
  - Productos:
    - Carpeta: `Productos/{idImagen}`

- **Nodos de Realtime Database usados:**
  - `Usuarios`
  - `Categorias`
  - `Productos`
  - `Productos/{idProducto}/Imagenes`
  - Nodos asociados a órdenes y calificaciones (de acuerdo a los modelos del paquete `Modelos`).

- La URL de la Realtime Database y del Storage se configuran automáticamente desde el archivo `google-services.json` y la consola de Firebase; no hay una constante explícita en el código para modificarlas.

---

## 8. Usuarios de prueba y credenciales de demo

**Administrador:**

- Email: `admin@rincon.crochetito.com`  
- Contraseña: `Admin123$`

**Cliente:**

- Email: `cliente@rincon.crochetito.com`  
- Contraseña: `Cliente123$`

Estos usuarios deben existir en Firebase Authentication y tener el campo `tipoUsuario` correcto en el nodo `Usuarios` de la Realtime Database para que la redirección por rol funcione correctamente.

---

## 9. Nota sobre almacenamiento de imágenes

- Todas las imágenes (categorías, productos) se almacenan en Firebase Storage:

  - **Categorías:**
    - Se suben a `Categorias/{idCategoria}` desde `FragmentCategoriasA`.
    - Se obtiene la URL de descarga y se guarda en:
      - Realtime Database → `Categorias/{idCategoria}/imagenUrl`.

  - **Productos:**
    - Se seleccionan varias imágenes en `AgregarProductoActivity`.
    - Cada imagen se guarda en:
      - `Productos/{idImagen}` en Firebase Storage.
    - La URL de descarga se guarda en:
      - Realtime Database → `Productos/{idProducto}/Imagenes/{idImagen}/imagenUrl`.

- En la base de datos solo se almacenan las URLs (strings), no las imágenes en sí.
- El archivo `google-services.json` y las claves de Firebase no deben subirse a repositorios públicos.

---

## 10. Flujo sugerido para el video de demostración (5–7 minutos)

### 10.1 Login como Admin

1. Abrir la app.  
2. Elegir flujo de administrador o ir directamente a `LoginActivityAdmin`.  
3. Ingresar credenciales:
   - `admin@rincon.crochetito.com` / `Admin123$`
4. Mostrar `MainActivityAdmin`.

Desde el panel de administración:

- Ir a `FragmentCategoriasA`:
  - Crear una nueva categoría con imagen (se sube a Storage).
- Ir a la sección de productos:
  - Abrir `AgregarProductoActivity`.
  - Crear un producto nuevo con:
    - Nombre, descripción, categoría, precio.
    - Descuento opcional.
    - Varias imágenes (galería).
  - Guardar producto y mostrarlo en el listado.
- Ir a `ListaClientesActivity`:
  - Mostrar lista de usuarios.
  - Bloquear y desbloquear a un usuario de prueba.
- Ir a `FragmentOrdenesA`:
  - Visualizar órdenes pendientes.
  - Cambiar estado de una orden:
    - De `PENDIENTE` a `ACEPTADA` y luego `ENVIADA`.
    - Otra orden marcarla como `RECHAZADA`.

### 10.2 Logout (admin)

- Desde `MainActivityAdmin`, ejecutar la acción de cerrar sesión (`FirebaseAuth.signOut()`).
- Volver a la pantalla de login o selección de tipo de usuario.

### 10.3 Login como Cliente

1. Ir a `LoginClienteActivity`.  
2. Ingresar credenciales:
   - `cliente@rincon.crochetito.com` / `Cliente123$`
3. Mostrar `MainActivityCliente` con sus fragments (inicio, tienda, carrito, órdenes, perfil).

**Flujo sugerido:**

- Ir a `FragmentTiendaC`:
  - Navegar por el catálogo.
  - Abrir `DetalleProductoActivity` de un producto.
  - Agregar al carrito.
- Ir a `FragmentCarritoC`:
  - Ver lista de ítems.
  - Modificar cantidad.
  - Eliminar un ítem.
- Desde el carrito, ir al flujo de pago (`PagoActivity`):
  - Confirmar la compra (pago simulado).
  - Confirmar solicitud de envío (usando `SeleccionarUbicacionActivity` si aplica).
- Ir a `FragmentMisOrdenesC`:
  - Ver que la nueva orden aparece como `PENDIENTE`.
- Ir a `FragmentMiPerfilC`:
  - Mostrar edición de datos personales básicos.

### 10.4 Logout (cliente)

- Cerrar sesión desde el menú correspondiente.  
- Verificar que se vuelve a la pantalla de login/selección.

Este flujo cubre los puntos principales requeridos para la demo.

---

## 11. Consideraciones de seguridad y buenas prácticas

- No se exponen claves ni secretos sensibles en el código:
  - La configuración de Firebase (API keys, IDs, etc.) se gestiona mediante `google-services.json` y la consola de Firebase.
  - Este archivo no debe subirse a repositorios públicos.
- Las credenciales de prueba (admin/cliente) se usan únicamente para fines educativos/demostrativos.
- El backend Node (si se utiliza) debe gestionar sus claves y secretos mediante variables de entorno en el servidor, nunca desde la app Android.
- En la app no se almacenan contraseñas en texto plano ni en `SharedPreferences`.

---

## 12. Checklist de requisitos (rúbrica)

**Autenticación y sesión:**

- Login y logout para Cliente y Admin.
- Persistencia de usuario autenticado vía `FirebaseAuth`.
- Redirección por rol:
  - `SplashScreenActivity` consulta `Usuarios/{uid}/tipoUsuario` en Realtime Database.
  - Redirige a `MainActivityAdmin` o `MainActivityCliente`.

**Vista Cliente:**

- Catálogo de productos con RecyclerView.
- Detalle de producto con múltiples imágenes.
- Carrito editable:
  - Agregar productos.
  - Cambiar cantidades.
  - Eliminar ítems.
- Pago simulado y solicitud de envío.
- Perfil:
  - Ver y editar datos personales.
- Historial de pedidos:
  - `FragmentMisOrdenesC`.

**Vista Admin:**

- Gestión de productos:
  - Agregar, editar, listar, buscar productos.
  - Múltiples imágenes por producto (Firebase Storage + Realtime Database).
- Gestión de usuarios:
  - Lista de clientes.
  - Edición de datos.
  - Bloquear/desbloquear usuario.
- Gestión de pagos/órdenes:
  - Listado de órdenes pendientes (`FragmentOrdenesA`).
  - Cambios de estado: `PENDIENTE` → `ACEPTADO`/`RECHAZADO` → `ENVIADO`.

**Integración técnica:**

- Kotlin + XML.
- ViewBinding en Activities/Fragments.
- Retrofit (`Network/RetrofitClient` + `data/ApiService`).
- RecyclerView con adaptadores en el paquete `Adaptadores`.
- FirebaseAuth, Firebase Realtime Database, Firebase Storage.

**UI/UX:**

- Pantallas responsivas (uso de layouts adaptados).
- Estados de carga, vacío y error gestionados mediante feedback visual (toasts/snackbars).
- Confirmaciones en operaciones destructivas (borrado, etc.).
- Controles visibles según rol (cliente/admin).
- Ícono personalizado de la app configurado en recursos.

**Entrega:**

- Estructura de repositorio organizada por paquetes (`Administrador`, `Cliente`, `Modelos`, `Network`, etc.).
- README con:
  - Pasos de configuración Android + Firebase + (opcional) Node.
  - Variables/URLs necesarias (`BASE_URL`, nodos de Firebase, rutas de Storage).
  - Usuarios de prueba y credenciales de demo.
  - Nota sobre almacenamiento de imágenes en Firebase Storage.
- APK compilable desde Android Studio con las instrucciones indicadas.
- Flujo de demo claramente definido para el video.

---

## 13. Trabajo futuro y mejoras posibles

- Sustituir el pago simulado por integración real con una pasarela de pago usando el endpoint `/crear_preferencia` del backend Node.
- Añadir paginación y filtros avanzados en las pantallas de catálogo y listas de órdenes.
- Mejorar soporte offline del carrito (persistencia local más robusta).
- Añadir tests instrumentados de UI y pruebas de integración de la capa de red.

---

_Fin del README._
```


