package copus.qa

import java.io.{File, PrintWriter}

import copus.corenlp.{CoreNlpWrapper, Document, Utilities}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object CorpusPrepper {
  val sparkConf = new SparkConf().setAppName("QASystemLab2").setMaster("local[*]")
  val sc = new SparkContext(sparkConf)

  def main(args: Array[String]): Unit = {
    System.setProperty("hadoop.home.dir", "C:\\winutils")

    getDocumentRdd.saveAsObjectFile("documents.obj")

  }//main()

  def getDocumentRdd : RDD[Document] = {
    val files = sc.wholeTextFiles("corpus")
    val documents = files.map(file => CoreNlpWrapper.prepareText(file._2)).toArray
    sc.parallelize(documents)
  }
}//CorpusPrepper
