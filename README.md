# Spark Boilerplate
This is a boilerplate project for Apache Spark. The related blog post can be found at [https://www.barrelsofdata.com/spark-boilerplate-using-scala](https://www.barrelsofdata.com/spark-boilerplate-using-scala)

그 외에 연습이 필요한 부분들을 추가해 보았다.

## Build instructions
From the root of the project execute the below commands
- To clear all compiled classes, build and log directories
```shell script
./gradlew clean
```
- To run tests
```shell script
./gradlew test
```
- To build jar
```shell script
./gradlew shadowJar
```
- All combined
```shell script
./gradlew clean test shadowJar
```

## Run
```shell script
# Spark SQL test : "SELECT 'hello' AS col1", "show tables in default"
spark-submit --executor-memory 1g --master spark://minmac:7077 \
    --class com.barrelsofdata.sparkexamples.Driver \
    build/libs/spark-boilerplate-1.0.jar

# WordCount
spark-submit --executor-memory 1g --master spark://minmac:7077 \
    --class com.barrelsofdata.sparkexamples.WordCount \
    build/libs/spark-boilerplate-1.0.jar

# Calc Pi value : Java source 
spark-submit --executor-memory 1g --master spark://minmac:7077 \
    --class com.barrelsofdata.sparkexamples.CalcPi \
    build/libs/spark-boilerplate-1.0.jar
```

## Examples

참고 [HDFS 명령어](https://blog.voidmainvoid.net/175)

- path : <SPARK_HOME>/examples/src/main/resources
```shell script
# make directory
hdfs dfs -mkdir -p /user/data/examples
# copy files from local to hdfs (eq: appendToFile)
hdfs dfs -copyFromLocal <SPARK_HOME>/examples/src/main/resources/* /user/data/examples/
```
- ls files
```shell script
# list all
hdfs dfs -ls /user/data/example
# list by pattern
hdfs dfs -ls "/user/data/examples/*.csv"
```
- cat file
```shell script
hdfs dfs -cat /user/data/examples/employees.json
==>
{"name":"Michael", "salary":3000}
{"name":"Andy", "salary":4500}
{"name":"Justin", "salary":3500}
{"name":"Berta", "salary":4000}

hdfs dfs -cat hdfs://minubt:9000/user/data/examples/people.csv
==>
name;age;job
Jorge;30;Developer
Bob;32;Developer
```
- 그밖에 명령어들
```shell script
# tail file (opions: -f)
hdfs dfs -tail hdfs://minubt:9000/user/data/examples/people.csv
# check existing file (0을 리턴해야 하는데 안한다)
hdfs dfs -test -e hdfs://minubt:9000/user/data/examples/people.csv
```

## References

- [Spark Boilerplate](https://github.com/barrelsofdata/spark-boilerplate) : gradle, scala
- [FromTextFile.scala](https://github.com/spark-examples/spark-scala-examples/blob/master/src/main/scala/com/sparkbyexamples/spark/dataframe/FromTextFile.scala)
- [learning-hadoop-and-spark](https://github.com/lynnlangit/learning-hadoop-and-spark)

- [Incubator-Livy](https://github.com/apache/incubator-livy)
