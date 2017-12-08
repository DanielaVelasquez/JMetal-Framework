@echo off
set /p inicio=Inicio 
set /p final=Final  
java -cp target\unicauca-ann-5.2-jar-with-dependencies.jar co.edu.unicauca.parameters.SolisAndWetsParametersAdjust %inicio% %final%
REM RD /S /Q src
REM RD /S /Q target
REM shutdown.exe /s /t 00