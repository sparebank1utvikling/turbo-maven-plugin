package exampleproject.b;

import exampleproject.b.CoolFeature;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoolFeatureTest {

  private CoolFeature underTest = new CoolFeature();

  @Test
  void shall_do_cool_stuff() {
    assertEquals(CoolFeature.COOL_STUFF, underTest.doCoolStuff());
  }
}