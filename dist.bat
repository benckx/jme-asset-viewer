call dist-clean.bat

for /f %%i in ('findstr version gradle.properties') do set gradleVersion=%%i
set softwareVersion=%gradleVersion:~8%
set jarFile=jme-viewer-%softwareVersion%.jar
set distWinFolder=dist-win64-%softwareVersion%

echo %gradleVersion%
echo %softwareVersion%
echo %jarFile%

set JAVA_HOME="C:\Users\Benoit\.jdks\adopt-openjdk-1.8.0_275"
call gradlew clean build

mkdir "%distWinFolder%"
mkdir "%distWinFolder%\jre1.8.0_251"

copy "build\libs\%jarFile%" "%distWinFolder%\%jarFile%"
xcopy /E /H /C /I "C:\Users\Benoit\Downloads\jre1.8.0_251" "%distWinFolder%\jre1.8.0_251"
copy "README.md" "%distWinFolder%\README.md"

echo jre1.8.0_251\bin\java -jar %jarFile% > "%distWinFolder%/viewer.bat"
echo jre1.8.0_251\bin\java -jar %jarFile% skip-jme > "%distWinFolder%/viewer_skip_jme.bat"

set "PATH=C:\Program Files\7-Zip;%PATH%"
7z a "%distWinFolder%.zip" "%distWinFolder%/*"
