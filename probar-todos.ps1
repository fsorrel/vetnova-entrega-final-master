# Ejecuta las pruebas unitarias (mvn test) de los 12 microservicios y muestra el conteo por servicio.
# Uso: abrir PowerShell en la raiz del proyecto y ejecutar:  .\probar-todos.ps1
# Usa 'mvn' del PATH; si no esta, usa el mvnw.cmd del gateway. Requiere un JDK (proyecto en Java 17).

$raiz = $PSScriptRoot
if (-not $raiz) { $raiz = Get-Location }

$servicios = @(
    "vetnova_auth",
    "vetnova_catalogo",
    "vetnova_inventario",
    "vetnova_ventas",
    "vetnova_envio",
    "vetnova_agenda",
    "vetnova_ficha",
    "vetnova_soporte",
    "vetnova_laboratorio",
    "vetnova_facturacion",
    "vetnova_reportes",
    "vetnova_notificaciones"
)

$resultados = @()

foreach ($servicio in $servicios) {
    $carpeta = Join-Path $raiz $servicio
    Write-Host ""
    Write-Host "==============================================" -ForegroundColor Cyan
    Write-Host " Probando: $servicio" -ForegroundColor Cyan
    Write-Host "==============================================" -ForegroundColor Cyan

    if (-not (Test-Path $carpeta)) {
        Write-Host "AVISO: la carpeta $servicio no existe en $raiz" -ForegroundColor Yellow
        $resultados += [PSCustomObject]@{ Servicio = $servicio; Estado = "NO EXISTE"; Pruebas = "" }
        continue
    }
    if (-not (Test-Path (Join-Path $carpeta "pom.xml"))) {
        Write-Host "AVISO: $servicio no tiene pom.xml" -ForegroundColor Yellow
        $resultados += [PSCustomObject]@{ Servicio = $servicio; Estado = "SIN POM"; Pruebas = "" }
        continue
    }

    # Usar mvnw.cmd del gateway si mvn no esta en el PATH
    $mvn = if (Get-Command mvn -ErrorAction SilentlyContinue) { "mvn" } else { "$raiz\getawayspring-profeAlejandro\mvnw.cmd" }

    Push-Location $carpeta
    try {
        $salida = & $mvn test 2>&1
        $exit = $LASTEXITCODE

        # Ultima linea de resumen de pruebas, sin el prefijo [INFO]/[ERROR]
        $resumen = ($salida | Select-String -Pattern 'Tests run: \d+, Failures:').Line | Select-Object -Last 1
        if ($resumen) { $resumen = ($resumen -replace '^\[[A-Z]+\]\s*', '').Trim() }

        if ($exit -eq 0) {
            Write-Host "OK: $servicio  ->  $resumen" -ForegroundColor Green
            $resultados += [PSCustomObject]@{ Servicio = $servicio; Estado = "OK"; Pruebas = $resumen }
        } else {
            Write-Host "FAIL: $servicio  ->  $resumen" -ForegroundColor Red
            # Mostrar las lineas de fallo para ubicar el problema rapido
            $salida | Select-String -Pattern '<<< (FAILURE|ERROR)|BUILD FAILURE' |
                ForEach-Object { Write-Host ("   " + $_.Line) -ForegroundColor Red }
            $resultados += [PSCustomObject]@{ Servicio = $servicio; Estado = "FAIL"; Pruebas = $resumen }
        }
    }
    finally {
        Pop-Location
    }
}

Write-Host ""
Write-Host "================= RESUMEN =================" -ForegroundColor Cyan
$resultados | Format-Table -AutoSize

$fallidos = ($resultados | Where-Object { $_.Estado -ne "OK" }).Count
if ($fallidos -eq 0) {
    Write-Host "Los 12 microservicios pasaron sus pruebas unitarias." -ForegroundColor Green
    Write-Host "Reporte de cobertura por servicio en: <servicio>\target\site\jacoco\index.html" -ForegroundColor DarkGray
} else {
    Write-Host "$fallidos servicio(s) con problemas. Revisar arriba." -ForegroundColor Red
}
