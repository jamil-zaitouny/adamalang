package org.adamalang.rxhtml.atl;

import org.adamalang.rxhtml.atl.tree.Tree;
import org.adamalang.rxhtml.atl.tree.Text;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class ParserTests {

  @Test
  public void simple() {
    Tree tree = Parser.parse("xyz");
    Assert.assertTrue(tree instanceof Text);
    Assert.assertEquals(((Text) tree).text, "xyz");
    Assert.assertEquals("TEXT(xyz)", tree.debug());
    Assert.assertEquals("\"xyz\"", tree.js("$X"));
    Set<String> vars = tree.variables();
    Assert.assertEquals(0, vars.size());
  }

  @Test
  public void variable() {
    Tree tree = Parser.parse("hi {first|trim} {last}");
    Assert.assertEquals("[TEXT(hi ),TRANSFORM(LOOKUP[first],trim),TEXT( ),LOOKUP[last]]", tree.debug());
    Assert.assertEquals("\"hi \" + trim($X.first) + \" \" + $X.last", tree.js("$X"));
    Set<String> vars = tree.variables();
    Assert.assertEquals(2, vars.size());
    Assert.assertTrue(vars.contains("first"));
    Assert.assertTrue(vars.contains("last"));
  }

  @Test
  public void condition_trailing() {
    Tree tree = Parser.parse("hi [b]active");
    Assert.assertEquals("[TEXT(hi ),(LOOKUP[b]) ? (TEXT(active)) : (EMPTY)]", tree.debug());
    Assert.assertEquals("\"hi \" + ($X.b) ? (\"active\") : (\"\")", tree.js("$X"));
    Set<String> vars = tree.variables();
    Assert.assertEquals(1, vars.size());
    Assert.assertTrue(vars.contains("b"));
  }

  @Test
  public void condition_trailing_negate() {
    Tree tree = Parser.parse("hi [!b]inactive");
    Assert.assertEquals("[TEXT(hi ),(!(LOOKUP[b])) ? (TEXT(inactive)) : (EMPTY)]", tree.debug());
    Assert.assertEquals("\"hi \" + (!($X.b)) ? (\"inactive\") : (\"\")", tree.js("$X"));
    Set<String> vars = tree.variables();
    Assert.assertEquals(1, vars.size());
    Assert.assertTrue(vars.contains("b"));
  }

  @Test
  public void condition() {
    Tree tree = Parser.parse("hi [b]A[#b]B[/b] there");
    Assert.assertEquals("[TEXT(hi ),(LOOKUP[b]) ? (TEXT(A)) : (TEXT(B)),TEXT( there)]", tree.debug());
    Assert.assertEquals("\"hi \" + ($X.b) ? (\"A\") : (\"B\") + \" there\"", tree.js("$X"));
    Set<String> vars = tree.variables();
    Assert.assertEquals(1, vars.size());
    Assert.assertTrue(vars.contains("b"));
  }
}
