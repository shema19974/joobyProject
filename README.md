# instructor_robot_server

## java

```bash
$ brew cask install adoptopenjdk8
$ brew cask install java
$ vi ~/.bash_profile
export JAVA_HOME=`/usr/libexec/java_home -v "1.8"`
$ source ~/.bash_profile
```

## mongodb

```bash
brew tap mongodb/brew
brew install mongodb-community
brew services start mongodb-community
```

## Python

```bash
$ brew install python3
$ rm -rf /usr/local/bin/python
$ ln -s /usr/local/bin/python3 /usr/local/bin/python
```

## run

```bash
# 開発環境での起動
./gradlew joobyRun

# 本番環境での起動
java -jar myfat.jar prod

```


## test

```bash
./gradlew test
```

## help

* Read the [module documentation](https://jooby.io/)

## 本番デプロイ

```bash
1. jenkinsでビルド
    - https://robot.diveintocode.jp/job/robot_api_staging/
2. cd /var/lib/robot/
3. sh robot_server_start.sh
```
