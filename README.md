# MICROSERVICIO 1: GESTIÓN DE USUARIOS

## USUARIOS

### GET - LISTAR TODOS LOS USUARIOS
http://localhost:8080/usuarios

### GET - OBTENER USUARIO POR ID
http://localhost:8080/usuarios/5

### POST - CREAR USUARIO
http://localhost:8080/usuarios

{
  "user": "jdoe123",
  "pass": "password123",
  "run": "12345678-9",
  "primerNombre": "Juan",
  "segundoNombre": "Carlos",
  "primerApellido": "Doe",
  "segundoApellido": "Smith",
  "correo": "juan.doe@example.com",
  "direccion": "Calle Falsa 123",
  "ciudad": "Santiago",
  "region": "Metropolitana",
  "rol": {
    "id": 1
  }
}


### PUT - ACTUALIZAR USUARIO
http://localhost:8080/usuarios/5

{
  "user": "jdoe123",
  "pass": "nuevaPassword",
  "run": "12345678-9",
  "primerNombre": "Juan Actualizado",
  "segundoNombre": "Carlos",
  "primerApellido": "Doe",
  "segundoApellido": "Smith",
  "correo": "juan.doe@update.com",
  "direccion": "Nueva Calle 456",
  "ciudad": "Providencia",
  "region": "Metropolitana",
  "rol": {
    "id": 2
  }
}


### PUT - ASIGNAR ROL A USUARIO
http://localhost:8080/usuarios/5/rol/2

### PUT - DESACTIVAR USUARIO
http://localhost:8080/usuarios/5/desactivar
