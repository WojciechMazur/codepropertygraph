package io.shiftleft.semanticcpg.language.operatorextension

import io.shiftleft.codepropertygraph.generated.Operators
import io.shiftleft.semanticcpg.testing.MockCpg
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import io.shiftleft.semanticcpg.language._

class OperatorExtensionTests extends AnyWordSpec with Matchers {

  private val methodName = "method"

  "NodeTypeStarters" should {

    "allow retrieving assignments" in {
      val cpg = MockCpg()
        .withMethod(methodName)
        .withCallInMethod(methodName, Operators.assignment, Some("x = 10"))
        .cpg

      val List(x: opnodes.Assignment) = cpg.assignment.l
      x.name shouldBe Operators.assignment
      x.code shouldBe "x = 10"
    }

    "allow retrieving arithmetic expressions" in {
      val cpg = MockCpg()
        .withMethod(methodName)
        .withCallInMethod(methodName, Operators.addition, Some("10 + 20"))
        .cpg
      val List(x) = cpg.arithmetic.l
      x.name shouldBe Operators.addition
      x.code shouldBe "10 + 20"
    }

  }

}
