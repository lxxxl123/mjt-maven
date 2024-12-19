
# 输出到本地
## 思源黑体
#pyftsubset.exe /d/FontCompress/SourceHanSansCN-Normal.ttf --text-file=./fonts/textfile.txt --output-file=./fonts/SourceHanSansCN-Normal-sim.ttf
## 思源宋体
#pyftsubset.exe /d/FontCompress/SourceHanSerifCN-Regular.ttf --text-file=./fonts/textfile.txt --output-file=./fonts/SourceHanSerifCN-Regular-sim.ttf

backpath="/d/20221014/qms-platform/qms-service/src/main/resources/userfiles/font"
# 输出到本地
## 思源黑体
pyftsubset.exe /d/FontCompress/SourceHanSansCN-Normal.ttf --text-file=./fonts/textfile.txt --output-file=${backpath}/SourceHanSansCN-Normal-sim.ttf
## 思源宋体
pyftsubset.exe /d/FontCompress/SourceHanSerifCN-Regular.ttf --text-file=./fonts/textfile.txt --output-file=${backpath}/SourceHanSerifCN-Regular-sim.ttf