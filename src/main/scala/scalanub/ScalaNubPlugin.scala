package scalanub

import java.nio.file.{Files, Paths}

import scala.collection.mutable.ArrayBuffer
import scala.tools.nsc.{Global, Phase}
import scala.tools.nsc.plugins.{Plugin, PluginComponent}

class ScalaNubPlugin(val global: Global) extends Plugin { self =>
  import global._

  val name = "scalanub"
  val description = "Compile to nub"
  val components: List[PluginComponent] = List[PluginComponent](Component)

  private object Component extends PluginComponent {
    override val global: ScalaNubPlugin.this.global.type = ScalaNubPlugin.this.global
    override val runsAfter: List[String] = List("typer")
    override val phaseName = ScalaNubPlugin.this.description

    override def newPhase(prev: Phase): Phase = new ScalaNubPhase(prev)

    class ScalaNubPhase(prev: Phase) extends StdPhase(prev) {
      override val name = self.name

      def apply(unit: CompilationUnit): Unit = {
        unit.body.foreach {
          case ClassDef(mods, name, tparams, impl) if name.toString == "ScalaNubCode" =>
            val code = parseTree(impl)
            Files.write(Paths.get("/tmp/ScalaNubCode.nub"), code.map(_.show).mkString("\n").getBytes())
          case _ =>
//            println("sorry, not supported syntax...")
        }
      }

      def parseTree(tree: Tree): List[NubTrees.Tree] = {
        val nubCode: ArrayBuffer[NubTrees.Tree] = ArrayBuffer.empty

        val traverser = new Traverser {
          override def traverse(tree: Tree): Unit = tree match {
            case ValDef(mods, name, tpt, rhs) if mods.isMutable =>
              nubCode += NubTrees.Let(name.toString, rhs.toString)
            case _ =>
              super.traverse(tree)
          }
        }
        traverser.traverse(tree)
        nubCode.toList
      }
    }
  }
}
