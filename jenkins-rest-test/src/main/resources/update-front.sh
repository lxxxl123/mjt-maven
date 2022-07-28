find /c/Users/chenwh3/IdeaProjects/qms-platform/qms-service/src/main/resources -maxdepth 1| egrep "static|template" | xargs rm -r
cp /c/Users/chenwh3/IdeaProjects/qms-front/target/classes/*  /c/Users/chenwh3/IdeaProjects/qms-platform/qms-service/src/main/resources -r
echo "copy finish"