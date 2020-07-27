package com.barrelsofdata.sparkexamples

import org.apache.log4j.Logger
import org.apache.spark.sql.SparkSession

object Driver {

  val JOB_NAME: String = "es-bitnine"
  val LOG: Logger = Logger.getLogger(this.getClass.getCanonicalName)

  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder().appName(JOB_NAME).getOrCreate()
    spark.sql("SELECT 'hello' AS col1").show()
    LOG.info("SPARK-SQL: select test")
    spark.sql("show tables in default").show()
    LOG.info("SPARK-SQL: show tables")

    LOG.warn("Dummy warn message")
    LOG.error("Dummy error message")
  }

}

/*
## NOTE: jar 파일이 --class 보다 뒤에 명시되어야 한다
    ==> 안그러면 SparkException 발생 : No main class set in JAR

spark-submit --executor-memory 1g \
    --master spark://minmac:7077 \
    --class com.barrelsofdata.sparkexamples.Driver \
    build/libs/spark-boilerplate-1.0.jar

 */