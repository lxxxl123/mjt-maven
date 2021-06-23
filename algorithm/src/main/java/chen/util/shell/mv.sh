
fromPath="../copy"
toPath="../aim"
# 作用 剪切二级目录的文件,包含文件夹
for file in `find $fromPath -maxdepth 2 -mindepth 2 -type f`
do
  if [ "$file" != "." ];then
    dir=`basename $(dirname $file)`
    echo $dir
    toDir=$toPath"/"$dir
    mkdir $toDir
    mv $file $toDir
  fi
done
