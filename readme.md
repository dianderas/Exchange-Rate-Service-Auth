# Proyecto Casa de Cambios Digital

![Arquitectura](https://i.ibb.co/Q3zgffQR/Screenshot-2025-03-23-181605.png)

Este proyecto es una **implementación de una casa de cambios digital** utilizando microservicios. Incluye un frontend construido con React (Vite.js) y un backend dividido en varios servicios que interactúan entre sí a través de un Gateway.

---

## Requisitos para ejecutar el proyecto

Asegúrate de tener instalados los siguientes componentes en tu sistema antes de comenzar:

- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)

---

## Instrucciones para ejecutar el proyecto

Sigue los pasos a continuación para levantar el sistema completo:

1. **Clona el repositorio** en tu máquina local:
   ```bash
   git clone https://github.com/tu-repo/exchange-house.git
   cd exchange-house
   ```
2. **Levantar el backend**: Navega a la carpeta del backend:
   ```bash
   cd exchange-house-backend/
   ```
   Luego, ejecuta el siguiente comando:
   ```bash
   docker-compose up -d
   ```
3. Levantar el frontend: Navega a la carpeta del frontend:
   ```bash
   cd ../exchange-house-frontend/
   ```
   Luego, ejecuta el siguiente comando:
   ```bash
   docker-compose up -d
   ```
4. **Interacción a través del frontend**: Una vez que ambos servicios estén en ejecución, accede al frontend en el siguiente enlace:

   http://127.0.0.1:5173/

5. **Inicio de sesión**: Usa las siguientes credenciales para iniciar sesión en el sistema:

- Usuario: `user`
- Contraseña: `groupUser1234`

![Dashboard](https://i.ibb.co/9HrVczpx/Screenshot-2025-03-23-193412.png)

## Colección de Postman

El repositorio contiene dos carpetas principales:

- Archivo: `exchange-house.postman_collection.json`
