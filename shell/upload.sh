upload() {
  echo "开始上传"
  scp -i C:/Users/shixin/Desktop/secret_z ./app/release/app-release.apk root@81.70.85.63:/home/app
  echo "结束上传"
}

#调用函数上传
upload