# Test Script for Task Management API

Write-Host "=== Testing Task Management API ===" -ForegroundColor Green
Write-Host ""

# Wait for application to be ready
Write-Host "Waiting for application to start..." -ForegroundColor Yellow
Start-Sleep -Seconds 5

# Test 1: Register User
Write-Host "1. Testing User Registration..." -ForegroundColor Cyan
$registerBody = @{
    username = "testuser"
    email = "test@example.com"
    password = "password123"
} | ConvertTo-Json

try {
    $registerResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/register" `
        -Method POST `
        -ContentType "application/json" `
        -Body $registerBody
    
    Write-Host "   ✓ User registered successfully!" -ForegroundColor Green
    Write-Host "   Username: $($registerResponse.username)" -ForegroundColor Gray
    Write-Host "   Email: $($registerResponse.email)" -ForegroundColor Gray
    Write-Host "   Token: $($registerResponse.token.Substring(0, 20))..." -ForegroundColor Gray
    $token = $registerResponse.token
} catch {
    Write-Host "   ✗ Registration failed: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

Write-Host ""

# Test 2: Login
Write-Host "2. Testing User Login..." -ForegroundColor Cyan
$loginBody = @{
    usernameOrEmail = "testuser"
    password = "password123"
} | ConvertTo-Json

try {
    $loginResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" `
        -Method POST `
        -ContentType "application/json" `
        -Body $loginBody
    
    Write-Host "   ✓ Login successful!" -ForegroundColor Green
    Write-Host "   Token: $($loginResponse.token.Substring(0, 20))..." -ForegroundColor Gray
    $token = $loginResponse.token
} catch {
    Write-Host "   ✗ Login failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""

# Test 3: Create Task
Write-Host "3. Testing Task Creation..." -ForegroundColor Cyan
$taskBody = @{
    title = "Complete API testing"
    description = "Test the Task Management API endpoints"
    status = "TODO"
    priority = "HIGH"
    category = "Testing"
} | ConvertTo-Json

$headers = @{
    "Authorization" = "Bearer $token"
    "Content-Type" = "application/json"
}

try {
    $createTaskResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/tasks" `
        -Method POST `
        -Headers $headers `
        -Body $taskBody
    
    Write-Host "   ✓ Task created successfully!" -ForegroundColor Green
    Write-Host "   Task ID: $($createTaskResponse.id)" -ForegroundColor Gray
    Write-Host "   Title: $($createTaskResponse.title)" -ForegroundColor Gray
    Write-Host "   Status: $($createTaskResponse.status)" -ForegroundColor Gray
    $taskId = $createTaskResponse.id
} catch {
    Write-Host "   ✗ Task creation failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""

# Test 4: Get All Tasks
Write-Host "4. Testing Get All Tasks..." -ForegroundColor Cyan
try {
    $allTasks = Invoke-RestMethod -Uri "http://localhost:8080/api/tasks" `
        -Method GET `
        -Headers $headers
    
    Write-Host "   ✓ Retrieved $($allTasks.Count) task(s)" -ForegroundColor Green
    foreach ($task in $allTasks) {
        Write-Host "   - $($task.title) ($($task.status))" -ForegroundColor Gray
    }
} catch {
    Write-Host "   ✗ Get tasks failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""

# Test 5: Update Task
if ($taskId) {
    Write-Host "5. Testing Task Update..." -ForegroundColor Cyan
    $updateBody = @{
        title = "Complete API testing - UPDATED"
        status = "IN_PROGRESS"
    } | ConvertTo-Json
    
    try {
        $updateResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/tasks/$taskId" `
            -Method PUT `
            -Headers $headers `
            -Body $updateBody
        
        Write-Host "   ✓ Task updated successfully!" -ForegroundColor Green
        Write-Host "   New Status: $($updateResponse.status)" -ForegroundColor Gray
    } catch {
        Write-Host "   ✗ Task update failed: $($_.Exception.Message)" -ForegroundColor Red
    }
    
    Write-Host ""
}

# Test 6: Get Task by ID
if ($taskId) {
    Write-Host "6. Testing Get Task by ID..." -ForegroundColor Cyan
    try {
        $taskResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/tasks/$taskId" `
            -Method GET `
            -Headers $headers
        
        Write-Host "   ✓ Task retrieved successfully!" -ForegroundColor Green
        Write-Host "   Title: $($taskResponse.title)" -ForegroundColor Gray
        Write-Host "   Description: $($taskResponse.description)" -ForegroundColor Gray
        Write-Host "   Priority: $($taskResponse.priority)" -ForegroundColor Gray
    } catch {
        Write-Host "   ✗ Get task failed: $($_.Exception.Message)" -ForegroundColor Red
    }
    
    Write-Host ""
}

Write-Host "=== API Testing Complete ===" -ForegroundColor Green
Write-Host ""
Write-Host "API Endpoints:" -ForegroundColor Yellow
Write-Host "  - Swagger UI: http://localhost:8080/swagger-ui.html" -ForegroundColor Cyan
Write-Host "  - H2 Console: http://localhost:8080/h2-console" -ForegroundColor Cyan
Write-Host "  - API Docs: http://localhost:8080/api-docs" -ForegroundColor Cyan
Write-Host ""
Write-Host "H2 Console Credentials:" -ForegroundColor Yellow
Write-Host "  - JDBC URL: jdbc:h2:mem:taskdb" -ForegroundColor Cyan
Write-Host "  - Username: sa" -ForegroundColor Cyan
Write-Host "  - Password: (leave empty)" -ForegroundColor Cyan

