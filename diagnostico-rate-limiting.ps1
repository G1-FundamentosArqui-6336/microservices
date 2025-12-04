# ================================================
# Diagnóstico Rápido - Rate Limiting
# ================================================

$BaseUrl = "http://localhost:8080"

Write-Host "`n=== DIAGNÓSTICO DE RATE LIMITING ===" -ForegroundColor Cyan
Write-Host ""

# Test 1: Gateway arriba?
Write-Host "1. Verificando que el Gateway está arriba..." -ForegroundColor Yellow
try {
    $health = Invoke-RestMethod -Uri "$BaseUrl/actuator/health" -Method GET
    Write-Host "   OK - Gateway está UP" -ForegroundColor Green
    Write-Host "   Status: $($health.status)" -ForegroundColor Gray
} catch {
    Write-Host "   XX - Gateway NO está disponible" -ForegroundColor Red
    Write-Host "   Error: $($_.Exception.Message)" -ForegroundColor Red
    exit
}

# Test 2: Configuración cargada?
Write-Host "`n2. Verificando configuración de Rate Limiting..." -ForegroundColor Yellow
try {
    $config = Invoke-RestMethod -Uri "$BaseUrl/actuator/ratelimit/config" -Method GET
    Write-Host "   OK - Configuración obtenida" -ForegroundColor Green
    Write-Host "   Enabled: $($config.enabled)" -ForegroundColor Gray
    Write-Host "   Include Headers: $($config.includeHeaders)" -ForegroundColor Gray
    Write-Host "   Global: $($config.global.maxRequests) req / $($config.global.windowSeconds)s" -ForegroundColor Gray
    Write-Host "   Endpoints configurados: $($config.endpoints.Count)" -ForegroundColor Gray
} catch {
    Write-Host "   XX - No se pudo obtener configuración" -ForegroundColor Red
    Write-Host "   Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 3: Headers presentes?
Write-Host "`n3. Verificando Headers de Rate Limiting..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$BaseUrl/actuator/health" -Method GET -ErrorAction Stop

    $hasLimit = $response.Headers['X-RateLimit-Limit']
    $hasRemaining = $response.Headers['X-RateLimit-Remaining']
    $hasReset = $response.Headers['X-RateLimit-Reset']

    if ($hasLimit) {
        Write-Host "   OK - Headers presentes:" -ForegroundColor Green
        Write-Host "     X-RateLimit-Limit: $hasLimit" -ForegroundColor Gray
        Write-Host "     X-RateLimit-Remaining: $hasRemaining" -ForegroundColor Gray
        Write-Host "     X-RateLimit-Reset: $hasReset" -ForegroundColor Gray
    } else {
        Write-Host "   XX - Headers NO presentes" -ForegroundColor Red
        Write-Host "   Esto indica que el filtro NO se está ejecutando" -ForegroundColor Yellow
        Write-Host ""
        Write-Host "   Posibles causas:" -ForegroundColor Yellow
        Write-Host "   - El JAR no fue reconstruido" -ForegroundColor White
        Write-Host "   - El filtro no está siendo registrado como Bean" -ForegroundColor White
        Write-Host "   - El orden del filtro es incorrecto" -ForegroundColor White
    }
} catch {
    Write-Host "   XX - Error al hacer request" -ForegroundColor Red
    Write-Host "   Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 4: Prueba rápida de bloqueo
Write-Host "`n4. Prueba rápida de bloqueo (50 requests)..." -ForegroundColor Yellow
$blocked = 0
$allowed = 0

1..50 | ForEach-Object {
    try {
        $null = Invoke-WebRequest -Uri "$BaseUrl/actuator/health" -Method GET -ErrorAction Stop
        $allowed++
    } catch {
        if ($_.Exception.Response.StatusCode -eq 429) {
            $blocked++
        }
    }
}

Write-Host "   Permitidos: $allowed" -ForegroundColor $(if ($allowed -gt 0) { "Green" } else { "Red" })
Write-Host "   Bloqueados: $blocked" -ForegroundColor $(if ($blocked -gt 0) { "Green" } else { "Yellow" })

if ($blocked -eq 0) {
    Write-Host "   XX - No hubo bloqueos en 50 requests (límite actuator: 200/10s)" -ForegroundColor Yellow
    Write-Host "   Esto es NORMAL para /actuator/health ya que tiene límite alto" -ForegroundColor Gray
}

# Test 5: Verificar logs del contenedor
Write-Host "`n5. Verificando logs del Gateway..." -ForegroundColor Yellow
Write-Host "   Ejecuta este comando para ver logs:" -ForegroundColor Gray
Write-Host "   docker logs gateway-service 2>&1 | Select-String 'RateLimitFilter'" -ForegroundColor Cyan
Write-Host ""
Write-Host "   Deberías ver algo como:" -ForegroundColor Gray
Write-Host "   'RateLimitFilter initialized. Enabled: true, Global limit: 100 req/10 seconds'" -ForegroundColor White

# Test 6: Métricas
Write-Host "`n6. Verificando métricas Prometheus..." -ForegroundColor Yellow
try {
    $metrics = Invoke-RestMethod -Uri "$BaseUrl/actuator/prometheus" -Method GET

    $allowedLine = $metrics -split "`n" | Where-Object { $_ -match "^gateway_ratelimit_allowed_total" -and $_ -notmatch "^#" } | Select-Object -First 1
    $blockedLine = $metrics -split "`n" | Where-Object { $_ -match "^gateway_ratelimit_blocked_total" -and $_ -notmatch "^#" } | Select-Object -First 1

    if ($allowedLine) {
        Write-Host "   OK - Métrica allowed encontrada:" -ForegroundColor Green
        Write-Host "   $allowedLine" -ForegroundColor Gray
    } else {
        Write-Host "   XX - Métrica allowed NO encontrada" -ForegroundColor Red
    }

    if ($blockedLine) {
        Write-Host "   OK - Métrica blocked encontrada:" -ForegroundColor Green
        Write-Host "   $blockedLine" -ForegroundColor Gray
    } else {
        Write-Host "   Métrica blocked NO encontrada (normal si aún no hay bloqueos)" -ForegroundColor Yellow
    }
} catch {
    Write-Host "   XX - Error al obtener métricas" -ForegroundColor Red
}

# Resumen
Write-Host "`n=== RESUMEN ===" -ForegroundColor Cyan
Write-Host ""
Write-Host "Si los headers NO están presentes, necesitas:" -ForegroundColor Yellow
Write-Host "  1. Reconstruir: .\mvnw clean package -DskipTests -pl gateway-service" -ForegroundColor White
Write-Host "  2. Reiniciar: docker-compose restart gateway-service" -ForegroundColor White
Write-Host "  3. Esperar 15-20 segundos" -ForegroundColor White
Write-Host "  4. Ejecutar este script de nuevo" -ForegroundColor White
Write-Host ""

