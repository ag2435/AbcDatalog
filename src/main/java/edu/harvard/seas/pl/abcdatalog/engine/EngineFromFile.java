package edu.harvard.seas.pl.abcdatalog.engine;

/*-
 * #%L
 * AbcDatalog
 * %%
 * Copyright (C) 2016 - 2021 President and Fellows of Harvard College
 * %%
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the President and Fellows of Harvard College nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

import edu.harvard.seas.pl.abcdatalog.ast.Clause;
import edu.harvard.seas.pl.abcdatalog.ast.PositiveAtom;
import edu.harvard.seas.pl.abcdatalog.ast.PredicateSym;
import edu.harvard.seas.pl.abcdatalog.ast.Term;
import edu.harvard.seas.pl.abcdatalog.ast.Variable;
import edu.harvard.seas.pl.abcdatalog.ast.validation.DatalogValidationException;
import edu.harvard.seas.pl.abcdatalog.engine.bottomup.sequential.SemiNaiveEngine;
import edu.harvard.seas.pl.abcdatalog.parser.DatalogParseException;
import edu.harvard.seas.pl.abcdatalog.parser.DatalogParser;
import edu.harvard.seas.pl.abcdatalog.parser.DatalogTokenizer;
import java.io.Reader;
import java.io.StringReader;
import java.io.FileReader;
import java.util.Set;

public final class EngineFromFile {

  private EngineFromFile() {
    throw new AssertionError("impossible");
  }

  private static Reader readInProgram(String filepath) {
    // Create FileReader from filepath
    try {
      FileReader fr = new FileReader(filepath);
      return fr;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  private static PositiveAtom makeQuery() throws DatalogParseException {
    PositiveAtom q;
    // We can construct a query by parsing text...
    String s = "brother(X, Y)?";
    DatalogTokenizer t = new DatalogTokenizer(new StringReader(s));
    q = DatalogParser.parseQuery(t);
    // Or we can construct it programmatically by building an AST.
    // Variable x = Variable.create("X");
    // Variable y = Variable.create("Y");
    // PredicateSym p = PredicateSym.create("tc", 2);
    // q = PositiveAtom.create(p, new Term[] {x, y});
    return q;
  }

  public static void main(String[] args) throws DatalogParseException, DatalogValidationException {
    // print args
    // for (String arg : args) {
    //   System.out.println(arg);
    // }
    // Reader r = readInProgram();

    // Read in program from file
    Reader r = readInProgram(args[0]);
    DatalogTokenizer t = new DatalogTokenizer(r);
    Set<Clause> prog = DatalogParser.parseProgram(t);
    // You can choose what sort of engine you want here.
    DatalogEngine e = SemiNaiveEngine.newEngine();
    e.init(prog);
    PositiveAtom q = makeQuery();
    Set<PositiveAtom> results = e.query(q);
    for (PositiveAtom result : results) {
      System.out.println(result);
    }
  }
}
