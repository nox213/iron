package io.github.iltotore.iron

import _root_.cats.Show
import _root_.cats.kernel.*
import _root_.cats.derived.*
import _root_.cats.instances.all.*

import io.github.iltotore.iron.cats.given
import io.github.iltotore.iron.constraint.all.*

import utest.{Show as _, *}

object CatsSuite extends TestSuite:

  type AgeR = DescribedAs[
    Greater[0] & Less[120],
    "Persons's age must be an integer between 1 and 120"
  ]

  type NameR = DescribedAs[
    Alphanumeric & MinLength[1] & MaxLength[50],
    "Person's name must be an alphanumeric of max length 50"
  ]

  case class Person(
      name: String :| NameR,
      surname: String :| Contain["z"],
      age: Int :| Greater[0]
  ) derives Eq, Order, Show

  given BoundedSemilattice[String] = BoundedSemilattice.instance("", _ + _)

  val tests: Tests = Tests {

    test("Cats instances are resolved for String iron types") {
      Eq[String :| NameR]
      Hash[String :| NameR]
      Order[String :| NameR]
      PartialOrder[String :| NameR]
      Show[String :| NameR]
      ()
    }

    test("Cats instances are resolved for Int iron types") {
      Eq[Int :| AgeR]
      Hash[Int :| AgeR]
      UpperBounded[Int :| AgeR]
      Order[Int :| AgeR]
      PartialOrder[Int :| AgeR]
      Show[Int :| AgeR]
      LowerBounded[Int :| AgeR]
    }

    test("Cats instances are resolved for a case class with iron types") {
      Eq[Person]
      Order[Person]
      Show[Person]
    }

    test("alley") {
      test("commutativeMonoid") {
        test("int") {
          test("pos") - assert(CommutativeMonoid[Int :| Positive].combine(1, 5) == 6)
          test("neg") - assert(CommutativeMonoid[Int :| Negative].combine(-1, -5) == -6)
        }

        test("long") {
          test("pos") - assert(CommutativeMonoid[Long :| Positive].combine(1, 5) == 6)
          test("neg") - assert(CommutativeMonoid[Long :| Negative].combine(-1, -5) == -6)
        }

        test("float") {
          test("pos") - assert(CommutativeMonoid[Float :| Positive].combine(1, 5) == 6)
          test("neg") - assert(CommutativeMonoid[Float :| Negative].combine(-1, -5) == -6)
        }

        test("double") {
          test("pos") - assert(CommutativeMonoid[Double :| Positive].combine(1, 5) == 6)
          test("neg") - assert(CommutativeMonoid[Double :| Negative].combine(-1, -5) == -6)
        }
      }
    }
  }
