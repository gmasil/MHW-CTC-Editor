set JAVA_HOME=C:\Program Files\BellSoft\LibericaNIK-22-OpenJDK-17
set GRAALVM_HOME=C:\Program Files\BellSoft\LibericaNIK-22-OpenJDK-17

CALL vcvars64

CALL gradlew.bat nativeCompile

editbin /SUBSYSTEM:WINDOWS build\native\nativeCompile\MHW-CTC-Editor.exe
