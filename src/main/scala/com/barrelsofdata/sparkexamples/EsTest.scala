package com.barrelsofdata.sparkexamples

import org.apache.log4j.Logger
import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.sql.types.{ArrayType, StringType, StructField, StructType}
import org.apache.spark.sql.{Encoders, SparkSession}
import org.graphframes.GraphFrame

object EsTest {

  val JOB_NAME: String = "ES Test"
  val LOG: Logger = Logger.getLogger(this.getClass.getCanonicalName)

  def esConf() = Map(
    "es.nodes"->"tonyne.iptime.org",
    "es.port"->"39200",
    "es.nodes.wan.only"->"true",
    "es.mapping.id"->"id",
    "es.write.operation"->"upsert",
    "es.index.auto.create"->"false",
    "es.scroll.size"->"10000",
    "es.net.http.auth.user"->"elastic",			// elasticsearch security
    "es.net.http.auth.pass"->"bitnine",			// => curl -u user:password
    "es.mapping.date.rich"->"false"				  // for timestamp
  )

  val resourceV = "agensvertex"
  val resourceE = "agensedge"

  val datasource = "modern"		// "northwind"
  val esQueryV:String = s"""{ "query": { "bool": {
	"filter": { "term": { "datasource": "${datasource}" } }
}}}"""
  val esQueryE:String = s"""{ "query": { "bool": {
	"filter": { "term": { "datasource": "${datasource}" } }
}}}"""

  // -------------------------------

  case class ElasticProperty(key:String, `type`:String, value:String)
  case class ElasticElement(id:String, property:ElasticProperty)
  case class ElasticVertex(timestamp:String, datasource:String, id:String, label:String, properties:Array[ElasticProperty])
  case class ElasticEdge(timestamp:String, datasource:String, id:String, label:String, properties:Array[ElasticProperty], src:String, dst:String)

  val schemaP = StructType( Array(
    StructField("key", StringType, false),
    StructField("type", StringType, false),
    StructField("value", StringType, false)
  ))
  val encoderP = RowEncoder(schemaP)
  val schemaV = StructType( Array(
    StructField("timestamp", StringType, false),
    StructField("datasource", StringType, false),
    StructField("id", StringType, false),
    StructField("label", StringType, false),
    StructField("properties", new ArrayType(schemaP, true), false)
  ))
  val schemaE = schemaV.
    add(StructField("src", StringType, false)).
    add(StructField("dst", StringType, false))

  // -------------------------------

  def main(args: Array[String]): Unit = {

    // enableHiveSupport() ==> equal to "spark.sql.catalogImplementation=hive"
    val spark: SparkSession = SparkSession.builder().appName(JOB_NAME).getOrCreate()

    // Read Data from ES
    val dfV = spark.read.format("es").options(esConf).
      option("es.query", esQueryV).
      schema(Encoders.product[ElasticVertex].schema).		// schema(schemaV).
      load(resourceV)
    val dfE = spark.read.format("es").options(esConf).
      option("es.query", esQueryE).
      schema(Encoders.product[ElasticEdge].schema).		// schema(schemaE).
      load(resourceE)

    LOG.info("ES-HADOOP: vertices.count = "+dfV.count())
    LOG.info("ES-HADOOP: edges.count = "+dfE.count())

    // Make Graphframe from two Dataframes
    val gf = GraphFrame(dfV,dfE)

    dfV.createOrReplaceTempView("modern_v")
    dfE.createOrReplaceTempView("modern_e")
    spark.sql("Select * from modern_v limit 1").show(false)

    val df_fltr_lt = dfV.take(1)
    LOG.info("ES-HADOOP: select test ==> "+df_fltr_lt.toString)

  }

}

/*
## list indices
tonyne.iptime.org:39200/_cat/indices/agens*?v&s=index&pretty

## test search about agensvertex
http://tonyne.iptime.org:39200/agensvertex/_search?q=datasource:modern
*/

/*
## NOTE: jar 파일이 --class 보다 뒤에 명시되어야 한다
    ==> 안그러면 SparkException 발생 : No main class set in JAR

spark-submit --executor-memory 1g \
    --master spark://minmac:7077 \
    --class com.barrelsofdata.sparkexamples.EsTest \
    build/libs/spark-boilerplate-1.0.jar

 */