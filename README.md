# Programación de Objetos Distribuidos

# TPE 2 - 2022

## Autores

+ Gonzalo Rossin - 60135
+ Jerónimo Brave - 61053
+ Mateo Bartellini Huapalla - 61438
+ Juan Ignacio Garcia Matweiszyn - 61441
+ Juan Manuel Negro - 61225
+ Mauro Daniel Sambartolomeo - 61279

## Instalación

Se provee el archivo `./install.sh` en el _root_ del proyecto que ejecutará los comandos de _Maven_
necesarios para la compilación e instalación del proyecto. Se generará el directorio `bin` en el
_root_ del proyecto con los archivos para correr cada sección. El mismo script de instalación se encarga
de extraer los tarballs y agregarle los permisos necesarios a los distintos archivos.

Dentro del directorio `bin` existen dos directorios, llamados `tpe2-g8-server-1.0-SNAPSHOT` y
`tpe2-g8-client-1.0-SNAPSHOT`. Cada uno contiene los archivos de servidor (Los nodos _Hazelcast_) y cliente (las diferentes consultas) respectivamente,
que estan listados a continuación.

+ Servidor
    + `run-server.sh`
+ Cliente
    + `query1`
    + `query2`
    + `query3`
    + `query4`
    + `query5`

## Funcionamiento general
Para poder utilizar el sistema se deben correr uno o mas nodos de _Hazelcast_ con
el _script_ `./run-server`. Por default los nodos escuchan por otros nodos con la máscara de interfaces "127.0.0.*", 
pero esto puede ser modificado facilmente con el parametro -DaddressMask seguido de la máscara deseada.

Ya funcionando el _cluster_, los clientes pueden conectarse al mismo, cada uno con su propio ejecutable, y realizar
las consultas. Cabe resaltar que a despues de cada consulta, los datos cargados al _cluster_ son eliminados para que cada consulta
tenga su propia informacion de entrada.

**Importante**: los scripts deben correrse dentro de los directorios en los que están creados para su
funcionamiento. Además, los mismos se deben correr con los comandos que se ejemplifican en la consigna.
