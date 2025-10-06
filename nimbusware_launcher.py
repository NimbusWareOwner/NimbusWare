#!/usr/bin/env python3
"""
NimbusWare Launcher
Advanced Minecraft Cheat Client
"""

import os
import sys
import subprocess
import platform
from pathlib import Path

def print_banner():
    """Print NimbusWare banner"""
    banner = """
 ███╗   ██╗██╗███╗   ███╗██████╗ ██╗   ██╗███████╗    ██╗    ██╗ █████╗ ██████╗ ███████╗
 ████╗  ██║██║████╗ ████║██╔══██╗██║   ██║██╔════╝    ██║    ██║██╔══██╗██╔══██╗██╔════╝
 ██╔██╗ ██║██║██╔████╔██║██████╔╝██║   ██║███████╗    ██║    ██║███████║██████╔╝█████╗  
 ██║╚██╗██║██║██║╚██╔╝██║██╔══██╗██║   ██║╚════██║    ██║    ██║██╔══██║██╔══██╗██╔══╝  
 ██║ ╚████║██║██║ ╚═╝ ██║██████╔╝╚██████╔╝███████║    ██║    ██║██║  ██║██║  ██║███████╗
 ╚═╝  ╚═══╝╚═╝╚═╝     ╚═╝╚═════╝  ╚═════╝ ╚══════╝    ╚═╝    ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝

 ═══════════════════════════════════════════════════════════════════════════════════════
 🎮 NimbusWare v1.0.0 - Advanced Minecraft Cheat Client
 ═══════════════════════════════════════════════════════════════════════════════════════
"""
    print(banner)

def check_java():
    """Check if Java is installed"""
    try:
        result = subprocess.run(['java', '-version'], 
                              capture_output=True, text=True, timeout=10)
        if result.returncode == 0:
            print("✅ Java found")
            return True
    except (subprocess.TimeoutExpired, FileNotFoundError):
        pass
    
    print("❌ Java is not installed or not in PATH!")
    print("Please install Java 8 or higher from https://adoptium.net/")
    return False

def check_jar():
    """Check if JAR file exists"""
    jar_path = Path("build/libs/workspace-1.0.0.jar")
    if jar_path.exists():
        size_mb = jar_path.stat().st_size / (1024 * 1024)
        print(f"✅ JAR file found: {jar_path} ({size_mb:.2f} MB)")
        return True
    
    print("❌ JAR file not found!")
    print("Please compile the project first:")
    print("  gradlew clean build")
    return False

def get_system_info():
    """Get system information"""
    print(f"🖥️  System: {platform.system()} {platform.release()}")
    print(f"🏗️  Architecture: {platform.machine()}")
    print(f"🐍 Python: {sys.version.split()[0]}")
    print()

def launch_nimbusware():
    """Launch NimbusWare"""
    jar_path = "build/libs/workspace-1.0.0.jar"
    
    # JVM options
    jvm_opts = [
        "-Xmx2G",
        "-Xms1G", 
        "-XX:+UseG1GC",
        "-XX:+UseStringDeduplication"
    ]
    
    # Java command
    cmd = ["java"] + jvm_opts + ["-jar", jar_path]
    
    print("🚀 Starting NimbusWare...")
    print(f"   Command: {' '.join(cmd)}")
    print()
    
    try:
        # Launch NimbusWare
        process = subprocess.run(cmd, check=False)
        
        if process.returncode == 0:
            print("✅ NimbusWare exited normally")
        else:
            print(f"❌ NimbusWare exited with code: {process.returncode}")
            
    except KeyboardInterrupt:
        print("\n⚠️  NimbusWare interrupted by user")
    except Exception as e:
        print(f"❌ Failed to start NimbusWare: {e}")

def main():
    """Main function"""
    try:
        print_banner()
        
        # Check prerequisites
        if not check_java():
            input("Press Enter to exit...")
            return 1
            
        if not check_jar():
            input("Press Enter to exit...")
            return 1
        
        # Show system info
        get_system_info()
        
        # Launch NimbusWare
        launch_nimbusware()
        
    except KeyboardInterrupt:
        print("\n⚠️  Launcher interrupted by user")
    except Exception as e:
        print(f"❌ An error occurred: {e}")
    finally:
        print("\nNimbusWare has stopped.")
        input("Press Enter to exit...")
    
    return 0

if __name__ == "__main__":
    sys.exit(main())