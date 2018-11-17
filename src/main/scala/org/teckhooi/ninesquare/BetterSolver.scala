package org.teckhooi.ninesquare

object BetterSolver {
  type Sheet = Map[String, String]

  val digits = ('1' to '9').mkString
  val alphas = ('A' to 'I').mkString

  def cross(rows: String, cols: String) = (for {
    row <- rows
    col <- cols} yield {
    "" + row + col
  }).toSet

  val horizontals = digits.map(d => cross(alphas, d.toString)).toSet
  val verticals = alphas.map(a => cross(a.toString, digits)).toSet
  val blocks = for {
    colBlk <- alphas.grouped(3)
    rowBlk <- digits.grouped(3)
  } yield (cross(colBlk, rowBlk))

  val all = horizontals ++ verticals ++ blocks

  val peers = cross(alphas, digits).foldLeft(Map.empty[String, Set[String]])((m, a) =>
    m.updated(a, all.filter(_.contains(a)).flatMap(_ - a)))

  val everything = cross(alphas, digits).foldLeft(Map.empty[String, Set[String]])((m, a) =>
    m.updated(a, all.filter(_.contains(a)).flatten))

  def pos(ndx: Int) = "" + alphas(ndx / 9) + digits(ndx % 9)

  def addNth(l: List[List[String]], extras: List[String], newList: List[List[String]] = Nil, ndx: Int = 0): List[List[String]] =
    l match {
      case Nil => newList.reverse
      case xs +: xss if (ndx % 3 == 0 && ndx > 1) => addNth(xss, extras, xs +: extras +: newList, ndx + 1)
      case xs +: xss => addNth(xss, extras, xs +: newList, ndx + 1)
    }

  def prettyFormat(s: String) =
    List("+-----+-----+-----+") ++
      addNth(s.grouped(9).map(_.grouped(3).toList).toList, List("---", "---", "---"))
        .map(x => if (x(0).startsWith("-")) "+-----+-----+-----+" else s"| ${x(0)} | ${x(1)} | ${x(2)} |") ++
      List("+-----+-----+-----+")

  def init(puzzle: String): Map[String, String] = {
    def fill(i: Char, cell: String, solution: Map[String, String]) = {
      val cells = all.filter(_.contains(cell)).flatten.toSet
      if (i == '0') all.filter(_.contains(cell)).flatten
        .foldLeft(solution)((m, c) => m + m.get(c).map(x => c -> x.replace(i.toString, "")).getOrElse(c -> (digits.replace(i.toString, ""))))
      else (cells - cell).foldLeft(solution + (cell -> i.toString))((m, c) =>
        m + m.get(c).map(x => c -> x.replace(i.toString, "")).getOrElse(c -> (digits.replace(i.toString, ""))))
    }

    def _init(cells: List[(Char, Int)], solution: Map[String, String]): Map[String, String] = cells match {
      case Nil => solution
      case x :: xs => x match {
        case ('.', ndx) => _init(xs, fill('0', pos(ndx), solution))
        case (d, ndx) => _init(xs, fill(d, pos(ndx), solution))
      }
    }

    _init(puzzle.zipWithIndex.toList, Map.empty[String, String])
  }

  // println(init(emptyPuzzle).filter(_._2.size > 1).toList.sortBy(_._2.size))

  def prettyPrint(puzzle: Map[String, String]) =
    puzzle.toList.sortBy(_._1).map(_._2).grouped(9).toList.foreach(line => {
      line.foreach(x => {
        x.foreach(print);
        print(" " * (9 - x.size))
      })
      println
    })

  def print2List(puzzle: Map[String, String]) = {
    print("List(")
    puzzle.toList.sortBy(_._1).map(_._2).grouped(9).toList.foreach(line => {
      line.foreach(x => {
        print("\"")
        x.foreach(print)
        print("\",")
      })
      println
    })
    print(")")
  }

  def isSolved(solution: Map[String, String]) = {
    val result = all.map(y => y.foldLeft(Set.empty[Char])((s, z) => s ++ solution(z)))
    result.size == 1 && result.headOption.map(_.size == 9).getOrElse(false)
  }

  def eliminate(solution: Map[String, String], cell: String, num: Char): Option[Map[String, String]] = {
    println(s"Eliminate ($cell, $num)")
    prettyPrint(solution)
    println
    val nums = solution(cell)
    solution match {
      case xs if !nums.contains(num) => Some(xs)
      case xs =>
        val xss = xs.updated(cell, nums.replace(num.toString, ""))
        xss(cell) match {
          case ys if ys.isEmpty => None
          case ys if ys.size == 1 =>
            peers(cell).foldLeft(Option(xss))((s, c) => s.flatMap(s2 => eliminate(s2, c, ys.head).flatMap(s3 => {
              everything(cell).filter(c => s3(c).contains(num)) match {
                case zs if zs.isEmpty => None
                case zs if zs.size == 1 => assign(s3, zs.head, num)
                case _ => Some(s3)
              }
            })))
          case _ => everything(cell).filter(c => xss(c).contains(num)) match {
            case zs if zs.isEmpty => None
            case zs if zs.size == 1 => assign(xss, zs.head, num)
            case _ => Some(xss)
          }

        }
    }
  }

  def assign(solution: Map[String, String], cell: String, num: Char): Option[Map[String, String]] = {
    println(s"Assign ($cell, $num)")
    solution(cell).replace(num.toString, "").foldLeft(Option(solution))((s, c) => s.flatMap(m => eliminate(m, cell, c)))
  }

  def search(solution: Option[Map[String, String]]): Option[Map[String, String]] =
    solution.flatMap(m => Option(m.filter(_._2.size > 1).toList.minBy(_._2.size))) match {
      case None => solution
      case Some((cell, nums)) =>
        nums.foldLeft(solution)((s, d) => s.flatMap(m => search(assign(m, cell, d))))
    }
}

object BetterSolverApp {

  import BetterSolver._

  val emptyPuzzle = "3..62..7....7...2...........5...81......4...8.........7.25......4....8........3.."

  // val emptyPuzzle = "8.....4..72......9..4.........1.7..23.5...9...4...........8..7..17..............."


  prettyFormat(emptyPuzzle) foreach println

  /* test cases will be moved to unit tests
assert(all.filter(_.contains("A1")).flatten.toSet ==
  Set("A9", "G1", "A4", "C1", "H1", "A5", "D1", "I1", "E1", "B3", "A2", "A1", "A6", "B2", "F1", "C3", "A8", "A3", "B1", "A7", "C2"))

assert(all.filter(_.contains(pos(18))).flatten.toSet ==
  Set("C6", "G1", "C1", "H1", "C8", "C7", "D1", "I1", "E1", "C9", "B3", "A2", "C4", "A1", "B2", "F1", "C3", "C5", "A3", "B1", "C2"))

println(eliminate('3', pos(6), Map[String, String]()))

*/

  // prettyPrint(init(emptyPuzzle))

  // println(solve(init(emptyPuzzle)))

  val startMills = System.currentTimeMillis
  println(search(Some(init(emptyPuzzle))))

  /*
solve(init(emptyPuzzle)) match {
  case Some(solved) =>
    if (isSolved(solved)) prettyFormat(solved.toList.sortBy(_._1).map(_._2.head).mkString).foreach(println)
    else println("Sudoku solution is incorrect")
  case None => println("Sudoku is not solved")
}
*/

  println(s"\nTime taken: ${System.currentTimeMillis - startMills}ms")
}