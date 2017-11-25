@echo off
set /p compu=Computador 
java -cp target\unicauca-ann-5.2-jar-with-dependencies.jar co.edu.unicauca.experiment.Experiment %compu%
REM RD /S /Q src
REM RD /S /Q target
REM shutdown.exe /s /t 00
