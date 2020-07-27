package com.barrelsofdata.sparkexamples

import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}

// Create the case classes for our domain
case class Department(id: String, name: String)
case class Person(name: String, age: Long)

object WordCount2 {

  def main(args: Array[String]) {
    println("Start now")
    val conf = new SparkConf().setAppName("Spark Scala WordCount Example").setMaster("local[1]")
    val spark = SparkSession.builder().config(conf).appName("CsvExample").master("local").getOrCreate()
    val sc: SparkContext = spark.sparkContext
    import spark.implicits._

    //val df = spark.read.options(Map("inferSchema"->"true","delimiter"->",","header"->"true")).csv("C:\\Sankha\\Study\\data\\salary.csv")

    // Create the Departments
    val department1 = Department("123456", "Computer Science")
    val department2 = Department("789012", "Mechanical Engineering")
    val department3 = Department("345678", "Theater and Drama")
    val department4 = Department("901234", "Indoor Recreation")

    val caseClassDS: Dataset[Person] = Seq(Person("Andy", 32)).toDS()
    val df: DataFrame = Seq(department1, department2, department3, department4).toDF

  }
}
