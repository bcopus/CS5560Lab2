package copus.lab2

import java.io._

import copus.corenlp.CoreNlpWrapper
import org.apache.spark.{SparkConf, SparkContext}

object Lab2Question2StartingLetter {

  def main(args: Array[String]): Unit = {
    System.setProperty("hadoop.home.dir", "C:\\winutils");

    val sparkConf = new SparkConf().setAppName("SparkTransformation").setMaster("local[*]")
    val sc = new SparkContext(sparkConf)
    val filePath = "input.txt"
    val lemmas = sc.parallelize(CoreNlpWrapper.F(filePath))

    val r = lemmas.map(lem => Tuple2(lem.toUpperCase.charAt(0), List(lem)))
              .distinct()
              .reduceByKey((a, b) => a ::: b)
              .collect()

    val pw = new PrintWriter(new File("output.txt" ))
      pw.write("Lab 2, copus.corenlp.Question 2\n")
      r.foreach { (e) =>
        val (firstChar, lemmas) = e
        pw.write("" + firstChar + " =>")
        lemmas.foreach(lem => pw.write(" " + lem))
        pw.write("\n")
      }
    pw.close()

  }//main()

}//SparkTransformation
