

count=`cat text.txt|head -n1 | wc -w`
for (( i = 1; i <= $count; i++ )); do
   awk -v args=$i '{print $args}' text.txt | xargs  echo
done