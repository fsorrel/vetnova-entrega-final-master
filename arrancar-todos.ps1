# Java: usa el del PATH; si no está, intenta ubicaciones conocidas
$java = (Get-Command java -ErrorAction SilentlyContinue).Source
if (-not $java) {
    $candidatos = @(
        'C:\Program Files\Java\jdk-26\bin\java.exe',
        'C:\Program Files\Eclipse Adoptium\jdk-25.0.2.10-hotspot\bin\java.exe'
    )
    $java = $candidatos | Where-Object { Test-Path $_ } | Select-Object -First 1
}
if (-not $java) {
    Write-Host "ERROR: no se encontro 'java' en el PATH ni en las rutas conocidas. Instala un JDK o agregalo al PATH." -ForegroundColor Red
    exit 1
}
Write-Host "Usando Java: $java" -ForegroundColor DarkGray

$raiz = $PSScriptRoot
if (-not $raiz) { $raiz = (Get-Location).Path }

# Orden según dependencias entre microservicios
$servicios = @(
    "vetnova_auth",
    "vetnova_catalogo",
    "vetnova_inventario",
    "vetnova_ventas",
    "vetnova_envio",
    "vetnova_ficha",
    "vetnova_agenda",
    "vetnova_facturacion",
    "vetnova_notificaciones",
    "vetnova_laboratorio",
    "vetnova_reportes",
    "vetnova_soporte"
)

foreach ($servicio in $servicios) {
    Write-Host "Iniciando $servicio..." -ForegroundColor Cyan
    $jar = Get-ChildItem "$raiz\$servicio\target\*.jar" -ErrorAction SilentlyContinue |
           Where-Object { $_.Name -notlike "*.jar.original" } |
           Select-Object -First 1

    if (-not $jar) {
        Write-Host "  AVISO: no se encontró JAR para $servicio (ejecuta .\compilar-todos.ps1 primero)" -ForegroundColor Yellow
        continue
    }

    Start-Process powershell -ArgumentList @(
        "-NoExit",
        "-Command",
        "& '$java' -jar '$($jar.FullName)'"
    )
    Start-Sleep -Seconds 5
}

Write-Host "Iniciando Gateway..." -ForegroundColor Cyan
Start-Process powershell -ArgumentList @(
    "-NoExit",
    "-Command",
    ".\mvnw.cmd spring-boot:run"
) -WorkingDirectory "$raiz\getawayspring-profeAlejandro"

Write-Host "Todo iniciado." -ForegroundColor Green