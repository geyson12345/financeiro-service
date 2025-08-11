#!/bin/sh

echo "Variáveis de ambiente (Setadas no .env)"
printenv

echo "Iniciando Api de Indices"
exec java -server -XX:MetaspaceSize=64M -XX:MaxMetaspaceSize=300M -Xmx1g -Xms1g -Djava.security.egd=file:/dev/./urandom -jar /app.jar