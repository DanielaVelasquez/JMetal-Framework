java -cp target\unicauca-ann-5.2-jar-with-dependencies.jar cliente.Cliente port 444 ip 192.168.127.72
timeout 500
del /f NGHSparametros.txt PSOparametros.txt cmaesparametros.txt DEbinparametros.txt DEexpparametros.txt GHSparametros.txt hsparametros.txt ihsparametros.txt m_elmparametros.txt
Rd /Q /S target
RD /Q /S src