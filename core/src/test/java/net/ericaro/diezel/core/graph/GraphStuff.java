package net.ericaro.diezel.core.graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import net.ericaro.diezel.core.DiezelHost;
import net.ericaro.diezel.core.FileUtil;
import net.ericaro.diezel.core.Target;
import net.ericaro.diezel.core.FlowManager;
import net.ericaro.diezel.core.State;
import net.ericaro.diezel.core.Transition;
import net.ericaro.diezel.core.graph.Graph;
import net.ericaro.diezel.core.graph.Graph.BNF;
import net.ericaro.diezel.core.graph.Graph.S;
import net.ericaro.diezel.core.graph.Graph.T;

public class GraphStuff {

	

	public static void main2(String[] args) throws IOException {
		BNF bnf = new BNF();
		// Graph toto = bnf.ch("toto");
		// Graph titi = bnf.ch("titi");
		// Graph opt = bnf.opt(toto);
		// Graph seq = bnf.opt(bnf.seq(bnf.ch("a"), bnf.ch("b"),
		// bnf.ch("c") ));
		// Graph seq = (bnf.sel(bnf.ch("a"), bnf.ch("b"),
		// bnf.ch("c") ));
		// Graph seq = bnf.iter(bnf.ch("a") );

		// lhs = lower-case { ["-"] lower-case | ["-"] digit }
		/*
		 * bnf.rule("lhs", bnf.ch("lhs")); bnf.rule("character",
		 * bnf.ch("character")); // (* non terminal rules *) // option = "[" rhs
		 * "]" bnf.rule("option", bnf.seq(bnf.ch("opt"), bnf.lhs("rhs"),
		 * bnf.ch("pto"))); // repetition = "{" rhs "}" bnf.rule("repetition",
		 * bnf.seq(bnf.ch("iter"), bnf.lhs("rhs"), bnf.ch("reti"))); // sequence
		 * = empty-symbol | {character | lhs |option |repetition }
		 * bnf.rule("sequence", bnf.iter(bnf.sel(bnf.lhs("character"),
		 * bnf.lhs("lhs"), bnf.lhs("option") ,bnf.lhs("repetition") ))) ; // rhs
		 * = sequence { "|" sequence } bnf.rule("rhs", bnf.seq(
		 * bnf.lhs("sequence"), bnf.iter( bnf.seq( bnf.ch("or"),
		 * bnf.lhs("sequence") ) ) ) ); // ebnf-rule= lhs "=" rhs ";"
		 * bnf.rule("rule", bnf.seq( bnf.lhs("lhs"), bnf.ch("equalsTo"),
		 * bnf.lhs("rhs") ) ); // ebnf-description = { ebnf-rule }
		 * bnf.rule("ebnf", bnf.iter( bnf.lhs("rule")) );
		 * 
		 * /*
		 */
		// graph(new File("./graph.dot"), bnf.ch("toto") );
		// graph(new File("./graph.dot"), bnf.seq(bnf.ch("toto"),
		// bnf.ch("titi")) );
		// graph(new File("./graph.dot"), bnf.lhs("symb") );
		// bnf.rule("res", bnf
		// .seq(bnf.ch("toto"), bnf.lhs("symb"), bnf.ch("titi")));
		// bnf.rule("symb", bnf.sel(bnf.ch("tutu"), bnf.ch("lulu")));
		// Graph res = bnf.definitions.get("res");
		// Graph symb = bnf.definitions.get("symb");
		//
		// graph("res", res);
		// graph("symb", symb);
		// substituteAll(res, symb);
		// graph("final", res);

	}

	public static void main(String[] args) throws IOException {
		BNF bnf = new BNF();
		/*
		 * bnf.rule("res", bnf .seq(bnf.ch("toto"), bnf.lhs("symb"),
		 * bnf.ch("titi"))); bnf.rule("symb", bnf.sel(bnf.ch("tutu"),
		 * bnf.ch("lulu"))); Graph res = bnf.definitions.get("res"); Graph symb
		 * = bnf.definitions.get("symb");
		 */

		Transition start = new Transition().doc("starter").name("start");
		Transition opt1 = new Transition().doc("option1").name("opt1");
		Transition opt2 = new Transition().doc("option2").name("opt2");

		Graph workflow = bnf.seq(bnf.term(start), bnf.sel( bnf.term(opt1), bnf.term(opt2)));

		graph("workflow_before", workflow);
		Graph.reduce(workflow);
		int i = 0;
		for (S s : workflow.states)
			s.state = new State().name("State" + ++i);

		workflow.in.state.name("InitState");
		workflow.out.state.name("double").exiting(true);

		graph("workflow_after", workflow);
		// substituteAll(res, symb);
		new DiezelHost().flowOf(workflow).toFile( new File("./src/test/java/") );

		// graph("final", res);

	}

	

}
