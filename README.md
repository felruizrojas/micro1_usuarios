# MICROSERVICIO 1: GESTIÓN DE USUARIOS

## USUARIOS

### GET - LISTAR TODOS LOS USUARIOS
http://localhost:8080/usuarios


### GET - OBTENER USUARIO POR ID
http://localhost:8080/usuarios/1


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
http://localhost:8080/usuarios/1

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
http://localhost:8080/usuarios/1/rol/1


### PUT - DESACTIVAR USUARIO
http://localhost:8080/usuarios/1/desactivar

### PUT - ACTIVAR USUARIO
http://localhost:8080/usuarios/1/activar



## ROLES

### GET - LISTAR TODOS LOS ROLES
http://localhost:8080/roles


### GET - OBTENER ROL POR ID
http://localhost:8080/roles/1


### POST - CREAR ROL
http://localhost:8080/roles

{
  "nombreRol": "SUPERVISOR",
  "permisos": [
    {
      "id": 1
    },
    {
      "id": 2
    }
  ]
}


### PUT - ACTUALIZAR ROL
http://localhost:8080/roles/1

{
  "nombreRol": "SUPERVISOR ACTUALIZADO",
  "permisos": [
    {
      "id": 2
    },
    {
      "id": 3
    }
  ]
}


### PUT - ASIGNAR PERMISO A ROL
http://localhost:8080/roles/1/permiso/1


### PUT - DESACTIVAR ROL
http://localhost:8080/roles/1/desactivar

### PUT - ACTIVAR ROL
http://localhost:8080/roles/1/desactivar



## PERMISOS

### GET - LISTAR TODOS LOS PERMISOS
http://localhost:8080/permisos


### GET - OBTENER PERMISO POR ID
http://localhost:8080/permisos/1


### POST - CREAR PERMISO
http://localhost:8080/permisos

{
  "nombrePermiso": "VER_USUARIOS"
}

      --- JSON CON rol (si ya tienes uno creado con ID = 1):
      {
        "nombrePermiso": "EDITAR_USUARIOS",
        "rol": {
          "id": 1
        }
      }


### PUT - ACTUALIZAR PERMISO
http://localhost:8080/permisos/1

{
  "nombrePermiso": "MODIFICAR_USUARIOS",
  "rol": {
    "id": 1
  }
}


### PUT - DESACTIVAR PERMISO
http://localhost:8080/permisos/1/desactivar

### PUT - ACTIVAR PERMISO
http://localhost:8080/permisos/1/activar