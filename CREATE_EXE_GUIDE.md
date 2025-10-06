# 🎮 NimbusWare - Создание EXE файла

## 📋 **Готовые файлы для запуска:**

### **1. NimbusWare.bat** - Основной bat файл
- ✅ Проверка Java
- ✅ Проверка JAR файла
- ✅ Запуск с оптимизированными параметрами
- ✅ Обработка ошибок

### **2. NimbusWare.vbs** - Скрытый запуск
- ✅ Запуск без консоли
- ✅ Проверка зависимостей
- ✅ Обработка ошибок

### **3. run_nimbusware.ps1** - PowerShell скрипт
- ✅ Продвинутый интерфейс
- ✅ Системная информация
- ✅ Настройки памяти
- ✅ Debug режим

## 🔧 **Создание EXE файла:**

### **Способ 1: Bat To Exe Converter**
1. Скачайте **Bat To Exe Converter** с https://bat2exe.net/
2. Откройте `NimbusWare.bat`
3. Выберите настройки:
   - **Icon**: Добавьте иконку (опционально)
   - **Version Info**: Заполните информацию о версии
   - **Compression**: Включите сжатие
4. Нажмите **Compile**

### **Способ 2: Advanced BAT to EXE Converter**
1. Скачайте **Advanced BAT to EXE Converter**
2. Откройте `NimbusWare.bat`
3. Настройки:
   - **Invisible application**: Да (для скрытого запуска)
   - **Include additional files**: Добавьте папку `build`
   - **Icon**: Выберите иконку
4. Нажмите **Convert**

### **Способ 3: PowerShell to EXE**
1. Установите **ps2exe** модуль:
   ```powershell
   Install-Module ps2exe
   ```
2. Конвертируйте PowerShell скрипт:
   ```powershell
   ps2exe -inputFile "run_nimbusware.ps1" -outputFile "NimbusWare.exe" -iconFile "icon.ico"
   ```

### **Способ 4: Java Packager (jpackage)**
1. Убедитесь, что у вас Java 14+
2. Создайте exe с помощью jpackage:
   ```bash
   jpackage --input build/libs --main-jar workspace-1.0.0.jar --main-class com.example.nimbusware.Main --name "NimbusWare" --type exe --dest dist
   ```

## 📦 **Создание установщика:**

### **Inno Setup**
1. Скачайте **Inno Setup** с https://jrsoftware.org/isinfo.php
2. Создайте `.iss` файл:
   ```ini
   [Setup]
   AppName=NimbusWare
   AppVersion=1.0.0
   DefaultDirName={pf}\NimbusWare
   DefaultGroupName=NimbusWare
   OutputDir=dist
   OutputBaseFilename=NimbusWare-Setup
   
   [Files]
   Source: "build\libs\workspace-1.0.0.jar"; DestDir: "{app}"
   Source: "NimbusWare.bat"; DestDir: "{app}"
   Source: "NimbusWare.vbs"; DestDir: "{app}"
   
   [Icons]
   Name: "{group}\NimbusWare"; Filename: "{app}\NimbusWare.bat"
   Name: "{commondesktop}\NimbusWare"; Filename: "{app}\NimbusWare.bat"
   ```

### **NSIS**
1. Скачайте **NSIS** с https://nsis.sourceforge.io/
2. Создайте `.nsi` файл для установщика

## 🚀 **Готовые решения:**

### **Простой запуск:**
- Используйте `NimbusWare.bat` - готов к использованию
- Двойной клик для запуска

### **Скрытый запуск:**
- Используйте `NimbusWare.vbs` - запуск без консоли
- Двойной клик для запуска

### **Продвинутый запуск:**
- Используйте `run_nimbusware.ps1` - PowerShell скрипт
- Правый клик → "Запустить с помощью PowerShell"

## 📋 **Требования для exe:**

### **Обязательные файлы:**
- ✅ `build\libs\workspace-1.0.0.jar` - Основной JAR файл
- ✅ `NimbusWare.bat` - Bat файл для запуска
- ✅ Java 8+ установлена в системе

### **Опциональные файлы:**
- 🎨 `icon.ico` - Иконка для exe
- 📄 `README.txt` - Инструкция пользователя
- 🔧 `config.json` - Конфигурация

## 🎯 **Рекомендации:**

### **Для простого использования:**
1. Используйте `NimbusWare.bat`
2. Конвертируйте в exe с помощью Bat To Exe Converter
3. Добавьте иконку и информацию о версии

### **Для продвинутого использования:**
1. Используйте PowerShell скрипт
2. Конвертируйте с помощью ps2exe
3. Создайте установщик с Inno Setup

### **Для распространения:**
1. Создайте установщик
2. Включите проверку Java
3. Добавьте инструкции по установке

## ✅ **Готово!**

Теперь у вас есть все необходимые файлы для создания exe файла NimbusWare! 🚀