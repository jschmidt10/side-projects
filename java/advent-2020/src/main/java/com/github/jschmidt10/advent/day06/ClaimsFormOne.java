package com.github.jschmidt10.advent.day06;

import com.github.jschmidt10.advent.util.StringUtils;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * On claims form one, any 'yes' answer from any group member counts as a 'yes' for the whole group.
 */
public class ClaimsFormOne {
  private Set<Character> yesAnswers;

  public ClaimsFormOne(Collection<String> answerLines) {
    this.yesAnswers = answerLines
        .stream()
        .flatMap(StringUtils::streamChars)
        .collect(Collectors.toSet());
  }

  public int getNumYesAnswers() {
    return yesAnswers.size();
  }
}
