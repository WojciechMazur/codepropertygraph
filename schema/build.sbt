name := "codepropertygraph-schema"

libraryDependencies += "io.shiftleft" %% "overflowdb-codegen" % "1.98+1-13aea12f+20210913-0906"

// TODO move to sbt plugin
val classWithSchema = settingKey[String]("")
val fieldName = settingKey[String]("")
val generateDomainClasses2 = taskKey[Seq[File]]("generate overflowdb domain classes for our schema")

classWithSchema := "io.shiftleft.codepropertygraph.schema.CpgSchema$"
fieldName := "instance"

// TODO move to sbt plugin
generateDomainClasses2 := Def.taskDyn {
  val outputRoot = sourceManaged.value / "odb-codegen"
  val classWithSchema_ = classWithSchema.value
  val fieldName_ = fieldName.value
  // TODO add caching back in
  Def.task {
    FileUtils.deleteRecursively(outputRoot)
    (Compile/runMain).toTask(s" overflowdb.codegen.Main --classWithSchema=$classWithSchema_ --field=$fieldName_ --out=$outputRoot").value
    FileUtils.listFilesRecursively(outputRoot)
  }
}.value


val generateDomainClasses = taskKey[Seq[File]]("generate overflowdb domain classes for our schema")
generateDomainClasses := Def.taskDyn {
  val outputRoot = target.value / "odb-codegen"
  val currentSchemaMd5 = FileUtils.md5(sourceDirectory.value, file("schema/build.sbt"))

  if (outputRoot.exists && lastSchemaMd5 == Some(currentSchemaMd5)) {
    Def.task {
      FileUtils.listFilesRecursively(outputRoot)
    }
  } else {
    Def.task {
      FileUtils.deleteRecursively(outputRoot)
      (Compile/runMain).toTask(s" io.shiftleft.codepropertygraph.schema.Codegen schema/target/odb-codegen").value
      lastSchemaMd5(currentSchemaMd5)
      FileUtils.listFilesRecursively(outputRoot)
    }
  }
}.value

val generateProtobuf = taskKey[File]("generate protobuf definitions: cpg.proto")
generateProtobuf := Def.taskDyn {
  val outputRoot = target.value / "protos"
  val outputFile = outputRoot / "cpg.proto"
  val currentSchemaMd5 = FileUtils.md5(sourceDirectory.value, file("schema/build.sbt"))

  if (outputRoot.exists && lastSchemaMd5 == Some(currentSchemaMd5)) {
    Def.task {
      outputFile
    }
  } else {
    Def.task {
      FileUtils.deleteRecursively(outputRoot)
      val invoked = (Compile/runMain).toTask(s" io.shiftleft.codepropertygraph.schema.Protogen schema/target/protos").value
      lastSchemaMd5(currentSchemaMd5)
      outputFile
    }
  }
}.value

lazy val schemaMd5File = file("target/schema-src.md5")
def lastSchemaMd5: Option[String] = scala.util.Try(IO.read(schemaMd5File)).toOption
def lastSchemaMd5(value: String): Unit = IO.write(schemaMd5File, value)
