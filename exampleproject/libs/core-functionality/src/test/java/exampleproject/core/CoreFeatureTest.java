package exampleproject.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoreFeatureTest {

  private CoreFeature underTest = new CoreFeature();

  @Test
  void shall_do_core_stuff() {
    assertEquals(CoreFeature.CORE_STUFF, underTest.doCoreStuff());
  }
}