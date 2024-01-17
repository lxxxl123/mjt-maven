find /d/20221014/qms-platform-copy/qms-service/src/main/resources -maxdepth 1| egrep "static|template" | xargs rm -r
cp /d/workspace-new/qms-front-copy/target/classes/*  /d/20221014/qms-platform-copy/qms-service/src/main/resources -r
echo "copy finish"