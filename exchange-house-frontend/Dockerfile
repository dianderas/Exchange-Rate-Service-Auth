# Usar una imagen de Node.js para construir la aplicación
FROM node:18 AS build

# Establecer el directorio de trabajo en el contenedor
WORKDIR /app

# Copiar los archivos del proyecto al contenedor
COPY package.json package-lock.json ./

# Instalar dependencias
RUN npm install

# Copiar el resto de los archivos del proyecto
COPY . .

# Construir la aplicación
RUN npm run build

# Usar una imagen ligera de servidor web para servir los archivos estáticos
FROM nginx:stable-alpine AS production

# Copiar los archivos de construcción al directorio de Nginx
COPY --from=build /app/dist /usr/share/nginx/html

# Copiar la configuración personalizada de Nginx (opcional)
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Exponer el puerto 80 para el servidor web
EXPOSE 80

# Iniciar Nginx
CMD ["nginx", "-g", "daemon off;"]