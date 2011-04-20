/* sbt -- Simple Build Tool
 * Copyright 2008, 2009, 2010  Mark Harrah
 */
package sbt

	import java.io.File
	import compiler.AnalyzingCompiler

final class Console(compiler: AnalyzingCompiler)
{
	/** Starts an interactive scala interpreter session with the given classpath.*/
	def apply(classpath: Seq[File], log: Logger): Option[String] =
		apply(classpath, Nil, "", log)

	def apply(classpath: Seq[File], options: Seq[String], initialCommands: String, log: Logger): Option[String] =
		apply(classpath, options, initialCommands)(None, Nil)(log)
	
	def apply(classpath: Seq[File], options: Seq[String], loader: ClassLoader, initialCommands: String)(bindings: (String, Any)*)(implicit log: Logger): Option[String] =
		apply(classpath, options, initialCommands)(Some(loader), bindings)
	
	def apply(classpath: Seq[File], options: Seq[String], initialCommands: String)(loader: Option[ClassLoader], bindings: Seq[(String, Any)])(implicit log: Logger): Option[String] =
	{
		def console0 = compiler.console(classpath, options, initialCommands, log)(loader, bindings)
		JLine.withJLine( Run.executeTrapExit(console0, log) )
	}
}
object Console
{
	def apply(conf: Compiler.Inputs): Console = new Console( conf.compilers.scalac )
}