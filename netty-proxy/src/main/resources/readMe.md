# 相关命令
- 生成kserver.keystore
    > keytool -genkey -alias serverkey -keystore kserver.keystore -keyalg RSA -deststoretype pkcs12 -dname "CN=chen, OU=chen, O=chen, L=gz, ST=gd, C=cn" -storepass 123456 -keypass 123456 -ext san=ip:127.0.0.1
- 生成p12
    > keytool -importkeystore -srckeystore kserver.keystore -srcstorepass 123456 -srckeypass 123456 -srcalias serverkey -destalias serverkey -destkeystore identity.p12 -deststoretype PKCS12 -deststorepass 123456 -destkeypass 123456
- 生成pem (包含私钥和证书)
    > openssl pkcs12 -in identity.p12  -nodes -out temp.pem
