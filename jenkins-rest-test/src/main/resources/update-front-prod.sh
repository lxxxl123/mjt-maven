find /d/workspaces/qms-prod/qms-service/src/main/resources -maxdepth 1| egrep "static|template" | xargs rm -r
cp /d/20221014/qms-front/target/classes/*  /d/workspaces/qms-prod/qms-service/src/main/resources -r
echo "copy finish"