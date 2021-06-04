package io.shiftleft.codepropertygraph.schema

import overflowdb.schema.{Schema, SchemaBuilder}

class CpgSchema(builder: SchemaBuilder) {

  val base = Base(builder)
  val operators = Operators(builder)

  val metaData = MetaData(builder, base)
  val namespaces = Namespace(builder, base)

  val typeSchema = Type(builder, base)
  val method = Method(builder, base, typeSchema)
  val fs = FileSystem(builder, base, namespaces, method, typeSchema)
  val ast = Ast(builder, base, namespaces, method, typeSchema, fs)

  val callGraph = CallGraph(builder, method, ast)
  val cfg = Cfg(builder, base, method, ast)
  val dominators = Dominators(builder, method, ast)
  val pdg = Pdg(builder, method, ast)

  val shortcuts = Shortcuts(builder, base, method, ast, typeSchema, fs)

  val sourceSpecific = Comment(builder, base, ast, fs)
  val closure = Closure(builder, base, method, ast, callGraph)
  val tagsAndLocation = TagsAndLocation(builder, base, typeSchema, method, ast, fs)
  val finding = Finding(builder, base)
  val deprecated = Deprecated(builder, base, method, typeSchema, ast, tagsAndLocation)
  val protoSerialize = ProtoSerialize(builder, ast)
}

object CpgSchema {
  val instance: Schema = {
    val builder = new SchemaBuilder("io.shiftleft.codepropertygraph.generated")
    new CpgSchema(builder)
    builder.build
  }
}
