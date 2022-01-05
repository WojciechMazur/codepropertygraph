package io.shiftleft.semanticcpg.language.operatorextension

import io.shiftleft.codepropertygraph.generated.Operators
import io.shiftleft.semanticcpg.language._
import io.shiftleft.semanticcpg.testing.MockCpg
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class OperatorExtensionTests extends AnyWordSpec with Matchers {

  private val methodName = "method"

  "NodeTypeStarters" should {

    "allow retrieving assignments" in {
      val cpg = mockCpgWithCallAndCode(Operators.assignment, "x = 10")
      val List(x: opnodes.Assignment) = cpg.assignment.l
      x.name shouldBe Operators.assignment
      x.code shouldBe "x = 10"
    }

    "allow retrieving arithmetic expressions" in {
      val cpg = mockCpgWithCallAndCode(Operators.addition, "10 + 20")
      val List(x: opnodes.Arithmetic) = cpg.arithmetic.l
      x.name shouldBe Operators.addition
      x.code shouldBe "10 + 20"
    }

    "include '+=' in both assignments and arithmetics" in {
      val cpg = mockCpgWithCallAndCode(Operators.assignmentPlus, "x += 10")
      val List(y: opnodes.Arithmetic) = cpg.arithmetic.l
      val List(x: opnodes.Assignment) = cpg.assignment.l
      x.id shouldBe y.id
      x.name shouldBe Operators.assignmentPlus
      x.code shouldBe "x += 10"
    }

    "allow retrieving array accesses" in {
      val cpg = mockCpgWithCallAndCode(Operators.indexAccess, "x[i]")
      val List(x: opnodes.ArrayAccess) = cpg.arrayAccess.l
      x.name shouldBe Operators.indexAccess
      x.code shouldBe "x[i]"
    }

  }

  private def mockCpgWithCallAndCode(name: String, code: String) =
    MockCpg()
      .withMethod(methodName)
      .withCallInMethod(methodName, name, Some(code))
      .cpg

}
