package com.barrelsofdata.sparkexamples

import org.apache.log4j.Logger
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._


object WordCount {

  val JOB_NAME: String = "WordCount with string"
  val LOG: Logger = Logger.getLogger(this.getClass.getCanonicalName)

  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder().appName(JOB_NAME).getOrCreate()
    import spark.implicits._    // sparkSession 생성 후에 import 가능

    val input = "Hello hello world Hello hello world Hello how are you world"
    val lines: Seq[String] = input.split(" ").filter(_ != "").toSeq
    val df = spark.sparkContext.parallelize(lines).toDF("word")

    val result = df
            .groupBy(col("word"))                  // Count number of occurences of each word
            .agg(count("*") as "numOccurances")
            .orderBy(col("numOccurances").desc)     // Show most common words first

    result.show(false)
    LOG.info(result)    // WordCount$: [word: string, numOccurances: bigint]
/*
+-----+-------------+
|word |numOccurances|
+-----+-------------+
|Hello|3            |
|world|3            |
|hello|2            |
|how  |1            |
|you  |1            |
|are  |1            |
+-----+-------------+
 */
  }
}
