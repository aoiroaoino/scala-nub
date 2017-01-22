package scalanub

object NubTrees {

  sealed trait Tree {
    def show: String
  }

  case class Let(name: String, tree: String) extends Tree {
    def show = s"let $name = $tree;"
  }
}
