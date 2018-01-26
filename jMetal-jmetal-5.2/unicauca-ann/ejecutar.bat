@echo off
set /p compu=Computador 
java -cp target\unicauca-ann-5.2-jar-with-dependencies.jar co.edu.unicauca.exec.experiment.Run %compu%
RD /S /Q src
RD /S /Q target
shutdown.exe /s /t 00