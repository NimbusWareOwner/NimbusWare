Set objShell = CreateObject("WScript.Shell")
Set objFSO = CreateObject("Scripting.FileSystemObject")

' Get the directory where the script is located
strScriptPath = objFSO.GetParentFolderName(WScript.ScriptFullName)

' Change to the script directory
objShell.CurrentDirectory = strScriptPath

' Check if Java is installed
On Error Resume Next
Set objExec = objShell.Exec("java -version")
If Err.Number <> 0 Then
    MsgBox "Java is not installed or not in PATH!" & vbCrLf & "Please install Java 8 or higher from https://adoptium.net/", vbCritical, "NimbusWare - Java Error"
    WScript.Quit
End If
On Error GoTo 0

' Check if JAR file exists
If Not objFSO.FileExists("build\libs\workspace-1.0.0.jar") Then
    MsgBox "JAR file not found!" & vbCrLf & "Please compile the project first:" & vbCrLf & "gradlew clean build", vbCritical, "NimbusWare - JAR Error"
    WScript.Quit
End If

' Launch NimbusWare
objShell.Run "java -Xmx2G -Xms1G -jar ""build\libs\workspace-1.0.0.jar""", 1, False