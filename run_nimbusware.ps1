# NimbusWare Launcher PowerShell Script
# Advanced Minecraft Cheat Client

param(
    [switch]$Debug,
    [switch]$NoLogo,
    [string]$Memory = "2G"
)

# Set console encoding
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8

# Clear screen
Clear-Host

# Show logo
if (-not $NoLogo) {
    Write-Host ""
    Write-Host " ███╗   ██╗██╗███╗   ███╗██████╗ ██╗   ██╗███████╗    ██╗    ██╗ █████╗ ██████╗ ███████╗" -ForegroundColor Cyan
    Write-Host " ████╗  ██║██║████╗ ████║██╔══██╗██║   ██║██╔════╝    ██║    ██║██╔══██╗██╔══██╗██╔════╝" -ForegroundColor Cyan
    Write-Host " ██╔██╗ ██║██║██╔████╔██║██████╔╝██║   ██║███████╗    ██║    ██║███████║██████╔╝█████╗  " -ForegroundColor Cyan
    Write-Host " ██║╚██╗██║██║██║╚██╔╝██║██╔══██╗██║   ██║╚════██║    ██║    ██║██╔══██║██╔══██╗██╔══╝  " -ForegroundColor Cyan
    Write-Host " ██║ ╚████║██║██║ ╚═╝ ██║██████╔╝╚██████╔╝███████║    ██║    ██║██║  ██║██║  ██║███████╗" -ForegroundColor Cyan
    Write-Host " ╚═╝  ╚═══╝╚═╝╚═╝     ╚═╝╚═════╝  ╚═════╝ ╚══════╝    ╚═╝    ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝" -ForegroundColor Cyan
    Write-Host ""
    Write-Host " ═══════════════════════════════════════════════════════════════════════════════════════" -ForegroundColor Green
    Write-Host " 🎮 NimbusWare v1.0.0 - Advanced Minecraft Cheat Client" -ForegroundColor Green
    Write-Host " ═══════════════════════════════════════════════════════════════════════════════════════" -ForegroundColor Green
    Write-Host ""
}

# Function to check Java installation
function Test-JavaInstallation {
    try {
        $javaVersion = java -version 2>&1 | Select-String "version"
        if ($javaVersion) {
            Write-Host "✅ Java found: $javaVersion" -ForegroundColor Green
            return $true
        }
    }
    catch {
        Write-Host "❌ Java is not installed or not in PATH!" -ForegroundColor Red
        Write-Host "Please install Java 8 or higher from https://adoptium.net/" -ForegroundColor Yellow
        return $false
    }
    return $false
}

# Function to check JAR file
function Test-JarFile {
    $jarPath = "build\libs\workspace-1.0.0.jar"
    if (Test-Path $jarPath) {
        $jarSize = (Get-Item $jarPath).Length / 1MB
        Write-Host "✅ JAR file found: $jarPath ($([math]::Round($jarSize, 2)) MB)" -ForegroundColor Green
        return $true
    }
    else {
        Write-Host "❌ JAR file not found: $jarPath" -ForegroundColor Red
        Write-Host "Please compile the project first:" -ForegroundColor Yellow
        Write-Host "  gradlew clean build" -ForegroundColor Yellow
        return $false
    }
}

# Function to get system info
function Get-SystemInfo {
    $os = [System.Environment]::OSVersion.VersionString
    $arch = [System.Environment]::Is64BitOperatingSystem
    $ram = [math]::Round((Get-WmiObject -Class Win32_ComputerSystem).TotalPhysicalMemory / 1GB, 2)
    
    Write-Host "🖥️  System Information:" -ForegroundColor Cyan
    Write-Host "   OS: $os" -ForegroundColor White
    Write-Host "   Architecture: $(if($arch){'64-bit'}else{'32-bit'})" -ForegroundColor White
    Write-Host "   RAM: $ram GB" -ForegroundColor White
    Write-Host ""
}

# Function to launch NimbusWare
function Start-NimbusWare {
    $jarPath = "build\libs\workspace-1.0.0.jar"
    
    # JVM options
    $jvmOpts = @(
        "-Xmx$Memory",
        "-Xms1G",
        "-XX:+UseG1GC",
        "-XX:+UseStringDeduplication",
        "-XX:+OptimizeStringConcat"
    )
    
    if ($Debug) {
        $jvmOpts += @(
            "-Djava.util.logging.config.file=logging.properties",
            "-Dcom.example.nimbusware.debug=true"
        )
        Write-Host "🐛 Debug mode enabled" -ForegroundColor Yellow
    }
    
    Write-Host "🚀 Starting NimbusWare..." -ForegroundColor Green
    Write-Host "   Memory: $Memory" -ForegroundColor White
    Write-Host "   JVM Options: $($jvmOpts -join ' ')" -ForegroundColor White
    Write-Host ""
    
    try {
        # Start the Java process
        $process = Start-Process -FilePath "java" -ArgumentList ($jvmOpts + "-jar", $jarPath) -NoNewWindow -PassThru -Wait
        
        if ($process.ExitCode -eq 0) {
            Write-Host "✅ NimbusWare exited normally" -ForegroundColor Green
        }
        else {
            Write-Host "❌ NimbusWare exited with code: $($process.ExitCode)" -ForegroundColor Red
        }
    }
    catch {
        Write-Host "❌ Failed to start NimbusWare: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# Main execution
try {
    # Check prerequisites
    if (-not (Test-JavaInstallation)) {
        Read-Host "Press Enter to exit"
        exit 1
    }
    
    if (-not (Test-JarFile)) {
        Read-Host "Press Enter to exit"
        exit 1
    }
    
    # Show system info
    Get-SystemInfo
    
    # Launch NimbusWare
    Start-NimbusWare
}
catch {
    Write-Host "❌ An error occurred: $($_.Exception.Message)" -ForegroundColor Red
}
finally {
    Write-Host ""
    Write-Host "NimbusWare has stopped." -ForegroundColor Yellow
    Read-Host "Press Enter to exit"
}