package copus.qa

import java.io.{File, PrintWriter}

import copus.corenlp.{CoreNlpWrapper, Document, Question, Sentence}
import org.apache.spark.{SparkConf, SparkContext}

object QASystemLab2 {
  def main(args: Array[String]): Unit = {
    System.setProperty("hadoop.home.dir", "C:\\winutils")
    val sparkConf = new SparkConf().setAppName("QASystemLab2").setMaster("local[*]")
    val sc = new SparkContext(sparkConf)

    val question = new Question("Who is president of the United States?")

    val documents = sc.objectFile[Document]("documents.obj")

    val bestDocs = documents.map(document => new Tuple2(document, CoreNlpWrapper.scoreDocument(document, question)))
      .filter(tup => tup._2 > 0.0)
      .sortBy(tup => tup._2, ascending = false)
      .take(5)
    //bestDocs.foreach(tup => println("doc score: " + tup._2 + "\n" + tup._1))

    val sentences = bestDocs.flatMap(tup => tup._1.getSentences)
    val qualifiedSentences = sentences.map(sentence => new Tuple2(sentence, CoreNlpWrapper.scoreSentence(sentence, question)))
      .filter(tup => tup._2 > 0.0)
    val bestSentences = qualifiedSentences.sortBy(tup => tup._2).takeRight(50)

    val potentialAnswers = bestSentences.map(sentence => CoreNlpWrapper.extractAnswer(sentence._1, question))
      .filter(answer => answer != null)

    println("Question: " + question.getText() + "\nPotential Answers:\n")
    potentialAnswers.foreach(ans => println("  " + ans))
  }//main()

}//object QASystemLab2
