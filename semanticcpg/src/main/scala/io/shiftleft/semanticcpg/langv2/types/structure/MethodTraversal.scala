package io.shiftleft.semanticcpg.langv2.types.structure

import io.shiftleft.codepropertygraph.generated.nodes
import io.shiftleft.semanticcpg.langv2._
import overflowdb.traversal.help
import overflowdb.traversal.help.Doc

import scala.collection.IterableOnceOps
import scala.jdk.CollectionConverters._

trait MethodTraversalImplicits {
  implicit def toMethodTraversalSingle[I <: nodes.Method](trav: I) = {
    new MethodTraversal[I, SingleTravTypes.type](trav: Single[I])
  }
  implicit def toMethodTraversalGeneric[I <: nodes.Method, T[_] <: SupportedTypes[_]](trav: TravTypesFor[T]#Collection[I]) = {
    new MethodTraversal[I, TravTypesFor[T]](trav)
  }
  implicit def toMethodTraversalIterOnceOps[I <: nodes.Method, CC[_], C](trav: IterableOnceOps[I, CC, C]) = {
    new MethodTraversal[I, IterableOnceOpsTravTypes[CC, C]](trav)
  }
}

/**
  * A method, function, or procedure
  * */
@help.Traversal(elementType = classOf[nodes.Method])
class MethodTraversal[I <: nodes.Method, TT <: TravTypes](val trav: TT#Collection[I]) extends AnyVal {

  /**
    * Traverse to parameters of the method
    * */
  @Doc("All parameters")
  def parameter(implicit ops1: TravOps[TT]) = {
    ops1.oneToMany(trav)(_._astOut.asScala.collect { case par: nodes.MethodParameterIn => par })
  }

  /**
    * Traverse to formal return parameter
    * */
  @Doc("All formal return parameters")
  def methodReturn(implicit ops1: TravOps[TT]) = {
    ops1.oneToOne(trav)(_._astOut.asScala.collectFirst { case ret: nodes.MethodReturn => ret }.get)
  }

}
