package PerfectGym;

import java.util.*;
import org.overture.codegen.runtime.*;

@SuppressWarnings("all")
public class Main extends Test {
  public static void main() {

    new TestUser().test();
    new TestPerfectGym().test();
    new TestGymClass().test();
    new TestExercise().test();
    new TestPlan().test();
  }

  public Main() {}

  public String toString() {

    return "Main{}";
  }
}
