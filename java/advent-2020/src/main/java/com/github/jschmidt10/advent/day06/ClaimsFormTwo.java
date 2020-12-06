package com.github.jschmidt10.advent.day06;

import com.github.jschmidt10.advent.util.IntersectSet;
import com.github.jschmidt10.advent.util.StringUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

/**
 * On claims form two, every member of a group must answer 'yes' to count as a 'yes' for the whole group.
 */
public class ClaimsFormTwo {
  private Set<Character> yesAnswers;

  public ClaimsFormTwo(Collection<String> answerLines) {
    Collection<Set<Character>> answerSets = convertToSets(answerLines);
    this.yesAnswers = IntersectSet.intersectAll(answerSets);
  }

  public int getNumYesAnswers() {
    return yesAnswers.size();
  }

  private Collection<Set<Character>> convertToSets(Collection<String> answerLines) {
    Collection<Set<Character>> charSets = new ArrayList<>();

    for (String answerLine : answerLines) {
      Set<Character> charSet = new TreeSet<>(StringUtils.asCharList(answerLine));
      charSets.add(charSet);
    }

    return charSets;
  }
}
